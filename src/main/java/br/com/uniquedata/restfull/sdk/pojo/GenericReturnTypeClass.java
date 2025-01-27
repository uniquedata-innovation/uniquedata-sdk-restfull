package br.com.uniquedata.restfull.sdk.pojo;

import java.lang.reflect.Type;
import java.util.List;

import br.com.uniquedata.restfull.sdk.helper.GenericReturnTypeClassHelper;

public class GenericReturnTypeClass {

	private Class<?> classType;
	
	private Class<?> superClassType;
	
	private GenericReturnType returnType;
	
	private List<Type> typesSubLevels;
	
	public GenericReturnTypeClass() {}
	
	public GenericReturnTypeClass(final Class<?> classType, 
			final GenericReturnType returnType) {
		
		this.classType = classType;
		this.returnType = returnType;
	}

	public enum GenericReturnType {
		DIRECT_CLASS, PARAMETERIZED_TYPE
	}
	
	public GenericReturnType getReturnType() {
		return returnType;
	}

	public void setReturnType(final GenericReturnType returnType) {
		this.returnType = returnType;
	}
	
	public Class<?> getClassType() {
		return classType;
	}

	public void setClassType(final Class<?> classType) {
		this.classType = classType;
	}

	public Class<?> getSuperClassType() {
		return superClassType;
	}

	public void setSuperClassType(final Class<?> superClassType) {
		this.superClassType = superClassType;
	}

	public List<Type> getTypesSubLevels() {
		return typesSubLevels;
	}
	
	public void setTypesSubLevels(final List<Type> typesSubLevels) {
		this.typesSubLevels = typesSubLevels;
	}
	
	public boolean containsType(final Type filterType) {
		return GenericReturnTypeClassHelper.containsType(this, filterType);
	}
	
}