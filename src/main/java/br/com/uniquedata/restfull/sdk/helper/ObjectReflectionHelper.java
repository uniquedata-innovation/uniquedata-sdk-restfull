package br.com.uniquedata.restfull.sdk.helper;

import java.lang.annotation.Annotation;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Proxy;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.management.RuntimeErrorException;

import br.com.uniquedata.restfull.sdk.pojo.ExtractAnnotation;
import br.com.uniquedata.restfull.sdk.pojo.ExtractField;
import br.com.uniquedata.restfull.sdk.pojo.ExtractMethod;

/**
 * The ObjectReflectionHelper helps to manipulate and inspect class settings 
 * and values through reflection.
 * 
 * <p>
 * This class provides utility methods for instantiating objects, creating proxy 
 * instances, retrieving field values, and handling generic return types.
 * </p>
 * 
 * @author Jaderson Berti
 * @author Unique Data Inovatation (company)
 * @since 1.0
 */
public class ObjectReflectionHelper {
	
	public static Class<?> newArrayType(final Class<?> elementType) {
		return Array.newInstance(elementType, 0).getClass();
	}
	
	public static Class<?> newCollectionType(final Class<? extends Collection<?>> collectionClass) {
		return Array.newInstance(collectionClass, 0).getClass();
	}
	
	public static ArrayList<?> newInstanceArraList(final Object[] objects) {
		return new ArrayList<>(Arrays.asList(objects));
	}
	
	public static ArrayList<?> newInstanceArraList(final Object object) {
		return newInstanceArraList((Object[]) object);
	}
	
	@SuppressWarnings("unchecked")
	public static Collection<Object> newCollection(final Class<?> collectionType) {
	    try {
	        return (Collection<Object>) collectionType.getDeclaredConstructor().newInstance();
	    } catch (Exception e) {
	        return new ArrayList<>();
	    }
	}

	@SuppressWarnings("unchecked")
	public static Map<Object, Object> newMap(final Class<?> mapType) {
	    try {
	        return (Map<Object, Object>) mapType.getDeclaredConstructor().newInstance();
	    } catch (Exception e) {
	        return new HashMap<>();
	    }
	}
	
	public static Object newInstance(final Class<?> classType) {
		try {
			return classType.getDeclaredConstructor().newInstance();
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeErrorException(new Error(e), "Erro create new Instance!");
		}
	}
	
	public static Object newProxyInstance(final Class<?> interfaceType, final Object invocationHandlerImpl) {
		try {
			final InvocationHandler invocationHandler =  (InvocationHandler) invocationHandlerImpl;
			return Proxy.newProxyInstance(interfaceType.getClassLoader(),new Class<?>[]{ interfaceType }, invocationHandler);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeErrorException(new Error(e), "Erro create new Instance!");
		}
	}
	
	public static Collection<?> newInstanceCollection(final Class<? extends Collection<?>> collectionType, final Object object) {
        if (object == null) {
            throw new IllegalArgumentException("The object to convert cannot be null");
        }
        
        if (!object.getClass().isArray()) {
            throw new IllegalArgumentException("The object must be an array, but was: " + object.getClass().getName());
        }
        
        try {
            @SuppressWarnings("unchecked")
			final Collection<Object> collection = (Collection<Object>) collectionType.getDeclaredConstructor().newInstance();
            collection.addAll(Arrays.asList((Object[]) object));
            
            return collection;
        } catch (ClassCastException e) {
            throw new IllegalArgumentException("Failed to cast the provided object to the expected type array", e);
        } catch (NoSuchMethodException e) {
            throw new IllegalArgumentException("The collection type " + collectionType.getName() + " must have a no-argument constructor", e);
        } catch (Exception e) {
            throw new RuntimeException("Failed to instantiate collection type: " + collectionType.getName(), e);
        }
    }
	
	public static Map<String, Object> getFieldNameAndValue(final Object object, final Annotation annotation){
		return getFieldNameAndValue(object, annotation.annotationType());
	}
	
	public static Map<String, Object> getFieldNameAndValue(final Object object, final Class<? extends Annotation> annotation){
		return Arrays.asList(object.getClass().getDeclaredFields()).stream()
			.map(field -> {
			
				if(field.isAnnotationPresent(annotation)) {
					final ExtractField extractField = ObjectReflectionHelper.extractField(object, field);
					final Optional<ExtractAnnotation> extractAnnotationOptional = extractField.getExtractAnnotationBy(annotation);
					
					if(extractAnnotationOptional.isPresent()) {
						final ExtractAnnotation extractAnnotation = extractAnnotationOptional.get();
						return new ExtractField(field, extractAnnotation.getAnnotationValue().toString(), extractField.getFieldValue());
					}
				}	
			
			return ObjectReflectionHelper.extractField(object, field);
		}).collect(Collectors.toMap(ExtractField::getFieldName, ExtractField::getFieldValue));
	}
	
	public static Map<Field, Object> getFieldAndValue(final Object object){
		final Map<Field, Object> fields = new HashMap<>();
		
		try {
			for (final Field field : object.getClass().getDeclaredFields()) {
				field.setAccessible(true);
				fields.put(field, field.get(object));
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
		
		return fields;
	}
	
	public static List<ExtractAnnotation> extractAnnotations(final Annotation ... annotations) {
		return Arrays.asList(annotations).stream().map(annotation -> {
			for (final Method method : annotation.annotationType().getDeclaredMethods()) {
				final ExtractMethod extractMethod = extractMethod(annotation, method.getName());

				final ExtractAnnotation extractAnnotation = new ExtractAnnotation();
				extractAnnotation.setAnnotation(annotation);
				extractAnnotation.setAnnotationName(annotation.annotationType().getSimpleName());
				extractAnnotation.setAnnotationValue(extractMethod.getMethodValue());

				return extractAnnotation;
			}
			
			return null;
		}).collect(Collectors.toList());
	}
	
	public static ExtractAnnotation extractAnnotationByMethodName(final Annotation annotation, final String methodName) {
		final ExtractAnnotation extractAnnotation = new ExtractAnnotation();
		extractAnnotation.setAnnotation(annotation);
		extractAnnotation.setAnnotationValue(extractMethod(annotation, methodName).getMethodValue());
		extractAnnotation.setAnnotationName(annotation.annotationType().getSimpleName());
		
		return extractAnnotation;
	}
	
	public static ExtractAnnotation extractAnnotationByFieldAndMethodName(
			final Annotation annotation, final Field field, final String methodName) {
		
		return Arrays.asList(field.getAnnotations()).stream()
			.filter(filterAnnotation -> filterAnnotation.equals(annotation))
			.map(annotationFound -> {
				final Object objectAnnotation = field.getAnnotation(annotationFound.annotationType());
				final ExtractMethod extractMethod = extractMethod(objectAnnotation, methodName);

				final ExtractAnnotation extractAnnotation = new ExtractAnnotation();
				extractAnnotation.setAnnotation(annotationFound);
				extractAnnotation.setAnnotationValue(extractMethod.getMethodValue());
				extractAnnotation.setAnnotationName(annotationFound.annotationType().getSimpleName());

				return extractAnnotation;
			}).findAny().orElseThrow(() -> new RuntimeException("Method Not Found {"+methodName+"}"));
	}
	
	public static ExtractMethod extractMethod(final Object object, final String methodName, final Object... paramters){
		try {
			final Stream<Method> stream = Arrays.asList(object.getClass().getMethods()).stream();
			final Optional<Method> optionalMethod = stream.filter(filter -> filter.getName().equals(methodName)).findAny();
			
			if(optionalMethod.isPresent()) {
				final Method method = optionalMethod.get();
				
				if(paramters == null || paramters.length == 0) {
					return new ExtractMethod(methodName, method.invoke(object));
				}else {
					return new ExtractMethod(methodName, method.invoke(object, paramters));
				}
			}
			
			throw new RuntimeException("Method Not Found {"+methodName+"}");
		}catch (Exception e) {
			throw new RuntimeException(e);
		}
	}	
	
	public static ExtractField extractField(final Object object, final Field field){
		try {
			field.setAccessible(true);
			
			final ExtractField extractField = new ExtractField();
			extractField.setField(field);
			extractField.setFieldName(field.getName());
			extractField.setFieldValue(field.get(object));

			if(field.getAnnotations() != null) {
				extractField.setFieldAnnotations(extractAnnotations(field.getAnnotations()));
			}
			
			return extractField;
		}catch (Exception e) {
 			throw new RuntimeException(e);
		}
	}
	
	public static Field transferField(final Object objectIn, final Field fieldIn, 
			final Object objectOut, final Field fieldOut){
		
		try {
			fieldIn.setAccessible(true);
			fieldOut.setAccessible(true);
			
			fieldOut.set(objectOut, fieldIn.get(objectIn));
		}catch (Exception e) {
 			throw new RuntimeException(e);
		}
		
		return fieldOut;
	}
	
	public static Field addValueField(final Object value, final Object objectOut, final Field fieldOut){
		try {
			fieldOut.setAccessible(true);
			fieldOut.set(objectOut, value);
		}catch (Exception e) {
 			throw new RuntimeException(e);
		}
		
		return fieldOut;
	}
	
	@SuppressWarnings("unchecked")
	public static Class<? extends Collection<?>> toCollectionType(final Class<?> arrayType) {
		if (!Collection.class.equals(arrayType) && !Collection.class.isAssignableFrom(arrayType)) {
            throw new IllegalArgumentException("The object must be an collection, but was: " + arrayType.getClass().getName());
        }
		
		return (Class<? extends Collection<?>>) arrayType;
	}
	
	public static ExtractField extractFieldByName(final Object object, final String fieldName){
		final Stream<Field> stream = Arrays.asList(object.getClass().getDeclaredFields()).stream();
		return extractField(object, stream.filter(field -> field.getName().equals(fieldName)).findAny().get());
	}
	
	public static ExtractField extractFieldByAnnotation(final Object object, final Class<? extends Annotation> annotation){
		final Stream<Field> stream = Arrays.asList(object.getClass().getDeclaredFields()).stream();
		return extractField(object, stream.filter(field -> field.isAnnotationPresent(annotation)).findAny().get());
	}
	
	public static Object getValueByFieldName(final String fieldName, final Object object){
		final Stream<Field> stream = Arrays.asList(object.getClass().getDeclaredFields()).stream();
 		return stream.filter(filter -> filter.getName().equals(fieldName)).findAny().orElse(null);
	}
	
	public static boolean isParameterizedType(final Type returnType) {
		return returnType instanceof ParameterizedType;
	}
	
	public static boolean isDirectClass(final Type returnType) {
		return returnType instanceof Class<?>;
	}
	
	public static boolean containsAnnotation(final Class<?> classType, final Class<? extends Annotation> annotation){
		final Stream<Field> stream = Arrays.asList(classType.getDeclaredFields()).stream();
		return stream.anyMatch(filter -> filter.isAnnotationPresent(annotation));
	}
	
	@SuppressWarnings("unchecked")
	public static <T> T reflection(final Class<?> classType, final Object object) {
		return (T) classType.cast(object);
	}

}