package br.com.uniquedata.restfull.sdk.pojo;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.List;

public class ReflectMapperFilter {
	
	private boolean enable;
	
	private List<String> fieldNames;

	private List<Class<? extends Annotation>> annotations;
	
	public ReflectMapperFilter() {
		this.enable = true;
		this.fieldNames = new ArrayList<>();
		this.annotations = new ArrayList<>();
	}
	
	public List<Class<? extends Annotation>> getAnnotations() {
		return annotations;
	}
	
	public void setAnnotations(final List<Class<? extends Annotation>> annotations) {
		this.annotations = annotations;
	}
	
	public List<String> getFieldNames() {
		return fieldNames;
	}
	
	public void setFieldNames(final List<String> fieldNames) {
		this.fieldNames = fieldNames;
	}
	
	public boolean isEnable() {
		return enable;
	}
	
	public void setEnable(final boolean enable) {
		this.enable = enable;
	}
	
	public <T> T addAnntotation(final T classType, final Class<? extends Annotation> annotation) {
		this.annotations.add(annotation);
		return classType;
	}
	
}
