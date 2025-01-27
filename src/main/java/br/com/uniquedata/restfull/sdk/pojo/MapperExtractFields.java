package br.com.uniquedata.restfull.sdk.pojo;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.stream.Stream;

public class MapperExtractFields extends ArrayList<MapperExtractField> {

	private static final long serialVersionUID = 7919712175525421672L;

	public boolean contains(final Class<? extends Annotation> annotation) {
		return super.stream().anyMatch(filter -> filter.getAnnotation().equals(annotation));
	}
	
	public boolean add(final Class<? extends Annotation> annotation, final ExtractField field) {
		return super.add(new MapperExtractField(field, annotation));
	}
	
	public MapperExtractField get(final Class<? extends Annotation> annotation) {
		return parse(annotation).findAny().orElse(null);	
	}
	
	private Stream<MapperExtractField> parse(final Class<? extends Annotation> annotation) {
		return super.stream().filter(filter -> filter.getAnnotation().equals(annotation));
	}
	
}
