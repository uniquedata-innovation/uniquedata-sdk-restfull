package br.com.uniquedata.sdk.restfull.implementation.start;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

import br.com.uniquedata.sdk.helper.object.ObjectReflectionHelper;

public class UniqueDataRestFullManagerBean {

	private static Map<Class<?>, Object> contextsBeanBuild;
	
	private static List<Class<?>> temporaryContextsBeanBuild;
	
	
	static {
		contextsBeanBuild = new ConcurrentHashMap<>();
		temporaryContextsBeanBuild = new CopyOnWriteArrayList<>();
	}

	public static synchronized void addBean(final Class<?> classType, final Object object) {
		contextsBeanBuild.put(classType, object);
		temporaryContextsBeanBuild.remove(classType);
	}
	
	public static synchronized void addClassType(final Class<?> classType) {
		temporaryContextsBeanBuild.add(classType);
	}
	
	public static synchronized Object removeBean(final Class<?> classType) {
		return contextsBeanBuild.remove(classType);
	}
	
	public static synchronized <T> T getBean(final Class<?> classType) {
		return ObjectReflectionHelper.reflection(classType, contextsBeanBuild.get(classType));
	}
	
	public static synchronized List<Class<?>> getClassTypes() {
		return temporaryContextsBeanBuild;
	}
	
}
