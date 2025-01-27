package br.com.uniquedata.restfull.sdk.pojo;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

public class ExtractField {

	private Field field;

	private String fieldName;
	
	private Object fieldValue;
	
	private List<ExtractAnnotation> fieldAnnotations;

	public ExtractField() {
		this.fieldAnnotations = new ArrayList<>();
	}
	
	public ExtractField(final Field field, final String fieldName, final Object fieldValue) {
		this.field = field;
		this.fieldName = fieldName;
		this.fieldValue = fieldValue;
		this.fieldAnnotations = new ArrayList<>();
	}

	public Field getField() {
		return field;
	}
	
	public void setField(final Field field) {
		this.field = field;
	}

	public String getFieldName() {
		return fieldName;
	}

	public void setFieldName(final String fieldName) {
		this.fieldName = fieldName;
	}

	public Object getFieldValue() {
		return fieldValue;
	}

	public void setFieldValue(final Object fieldValue) {
		this.fieldValue = fieldValue;
	}

	public List<ExtractAnnotation> getFieldAnnotations() {
		return fieldAnnotations;
	}
	
	public void setFieldAnnotations(final List<ExtractAnnotation> fieldAnnotations) {
		this.fieldAnnotations = fieldAnnotations;
	}

	public void addFieldAnnotation(final ExtractAnnotation fieldAnnotation) {
		this.fieldAnnotations.add(fieldAnnotation);
	}
	
	public Optional<ExtractAnnotation> getExtractAnnotationBy(final Annotation annotation) {
		final Stream<ExtractAnnotation> stream = this.fieldAnnotations.stream();
		return stream.filter(filter -> filter.getAnnotation().equals(annotation)).findAny();
	}
	
	public Optional<ExtractAnnotation> getExtractAnnotationBy(final Class<? extends Annotation> annotation) {
		final Stream<ExtractAnnotation> stream = this.fieldAnnotations.stream();
		return stream.filter(filter -> filter.getAnnotation().annotationType().equals(annotation)).findAny();
	}
	
}
