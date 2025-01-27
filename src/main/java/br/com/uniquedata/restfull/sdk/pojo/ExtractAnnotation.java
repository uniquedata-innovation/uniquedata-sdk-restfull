package br.com.uniquedata.restfull.sdk.pojo;

import java.lang.annotation.Annotation;

public class ExtractAnnotation {

	private String annotationName;
	
	private Object annotationValue;
	
	private Annotation annotation;

	public String getAnnotationName() {
		return annotationName;
	}

	public void setAnnotationName(final String annotationName) {
		this.annotationName = annotationName;
	}

	public Object getAnnotationValue() {
		return annotationValue;
	}

	public void setAnnotationValue(final Object annotationValue) {
		this.annotationValue = annotationValue;
	}

	public Annotation getAnnotation() {
		return annotation;
	}

	public void setAnnotation(final Annotation annotation) {
		this.annotation = annotation;
	}

	public ExtractAnnotation() {}
	
	public ExtractAnnotation(final String annotationName, final String annotationValue, final Annotation annotation) {
		this.annotationName = annotationName;
		this.annotationValue = annotationValue;
		this.annotation = annotation;
	}
	
}
