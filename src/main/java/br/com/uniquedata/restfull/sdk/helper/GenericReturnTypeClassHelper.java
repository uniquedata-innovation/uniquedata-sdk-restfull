package br.com.uniquedata.restfull.sdk.helper;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import br.com.uniquedata.restfull.sdk.pojo.GenericReturnTypeClass;
import br.com.uniquedata.restfull.sdk.pojo.GenericReturnTypeClass.GenericReturnType;
import br.com.uniquedata.restfull.sdk.pojo.UniqueDataParameterizedType;

public class GenericReturnTypeClassHelper {
	
	public static List<Type> extractTypesSubLevel(final Type type) {
		return extractTypesSubLevel(type, new HashSet<Type>());
	}
	
	public static List<Type> extractTypesSubLevel(final Type type, final Set<Type> typesSubLevel) {
		if (type instanceof ParameterizedType) {
			typesSubLevel.add(((ParameterizedType) type).getRawType());
			
			final ParameterizedType parameterizedType = (ParameterizedType) type;
			
            if (parameterizedType.getActualTypeArguments().length > 0) {
            	return extractTypesSubLevel(parameterizedType.getActualTypeArguments()[0], typesSubLevel);
            }
        }
		
		return typesSubLevel.stream().collect(Collectors.toList());
	}
	
	public static UniqueDataParameterizedType extractParameterizedType(final Type typeRoot, 
			final UniqueDataParameterizedType uniqueDataParameterizedType) {
		
		if (typeRoot instanceof ParameterizedType) {
			final ParameterizedType parameterizedTypeRoot = (ParameterizedType) typeRoot;
			final Class<?> rawTypeRoot = (Class<?>) parameterizedTypeRoot.getRawType();
			
			if(uniqueDataParameterizedType.getFirstSuperClassTypeLoop() == null) {
				uniqueDataParameterizedType.setFirstSuperClassTypeLoop(rawTypeRoot);
			}
			
			if(parameterizedTypeRoot.getActualTypeArguments().length > 0) {
				final Type type = parameterizedTypeRoot.getActualTypeArguments()[0];
				
				if (type instanceof ParameterizedType) {
					extractParameterizedType(type, uniqueDataParameterizedType);
				}else {
					uniqueDataParameterizedType.setClassType((Class<?>) type);
				}
			}
			
			return uniqueDataParameterizedType;
		}
		
		return uniqueDataParameterizedType.loadClassType((Class<?>) typeRoot);
	}
	
	public static GenericReturnTypeClass getGenericReturnTypeClass(final Type type) {
		if (type instanceof ParameterizedType) {
			final UniqueDataParameterizedType extractParameterizedType = GenericReturnTypeClassHelper
				.extractParameterizedType(type, new UniqueDataParameterizedType());
			
            final ParameterizedType parameterizedType = (ParameterizedType) type;
            final Class<?> rawType = (Class<?>) parameterizedType.getRawType();

            final GenericReturnTypeClass genericReturnTypeClass = new GenericReturnTypeClass();
			genericReturnTypeClass.setReturnType(GenericReturnType.PARAMETERIZED_TYPE);
        	genericReturnTypeClass.setClassType(extractParameterizedType.getClassType());
        	genericReturnTypeClass.setSuperClassType(rawType);
        	
        	if (parameterizedType.getActualTypeArguments().length > 0) {
        		genericReturnTypeClass.setTypesSubLevels(extractTypesSubLevel(type));
        	}
            
            return genericReturnTypeClass;
        }
		
		return new GenericReturnTypeClass((Class<?>) type, GenericReturnType.DIRECT_CLASS);
	}
	
	public static boolean containsType(final GenericReturnTypeClass genericReturnTypeClass, final Type filterType) {
		final Stream<Type> stream = genericReturnTypeClass.getTypesSubLevels().stream();
		return stream.anyMatch(type -> filterType.equals(type) || containsSuperType(type, filterType));
	}
	
	public static boolean containsSuperType(final Type type, final Type filterType) {
		return (type instanceof Class<?>) ? (((Class<?>) filterType).isAssignableFrom((Class<?>) type)) : false;
	}
	
}
