package br.com.uniquedata.restfull.sdk.pojo;

public class UniqueDataParameterizedType {
	
	private Class<?> classType;

	private Class<?> firstSuperClassTypeLoop;
	
	public UniqueDataParameterizedType() {}
	
	public UniqueDataParameterizedType(final Class<?> classType, final Class<?> firstSuperClassTypeLoop) {
		this.classType = classType;
		this.firstSuperClassTypeLoop = firstSuperClassTypeLoop;
	}
	
	public Class<?> getClassType() {
		return classType;
	}

	public void setClassType(final Class<?> classType) {
		this.classType = classType;
	}

	public Class<?> getFirstSuperClassTypeLoop() {
		return firstSuperClassTypeLoop;
	}
	
	public void setFirstSuperClassTypeLoop(final Class<?> firstSuperClassTypeLoop) {
		this.firstSuperClassTypeLoop = firstSuperClassTypeLoop;
	}
	
	public boolean hasFirstSuperClassTypeLoop() {
		return firstSuperClassTypeLoop != null;
	}

	public UniqueDataParameterizedType loadClassType(final Class<?> classType) {
		this.classType = classType;
		return this;
	}
	
}
