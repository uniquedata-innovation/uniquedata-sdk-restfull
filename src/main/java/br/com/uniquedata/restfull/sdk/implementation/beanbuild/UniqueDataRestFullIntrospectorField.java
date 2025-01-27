package br.com.uniquedata.restfull.sdk.implementation.beanbuild;

import com.fasterxml.jackson.databind.PropertyName;
import com.fasterxml.jackson.databind.introspect.Annotated;
import com.fasterxml.jackson.databind.introspect.JacksonAnnotationIntrospector;

import br.com.uniquedata.restfull.sdk.annotation.simple.RestFullField;

public class UniqueDataRestFullIntrospectorField extends JacksonAnnotationIntrospector {

	private static final long serialVersionUID = -4994914553775349204L;

    @Override
    public PropertyName findNameForSerialization(final Annotated a) {
        final PropertyName defaultName = super.findNameForSerialization(a);
        final RestFullField ann = _findAnnotation(a, RestFullField.class);
        
        if (ann != null && !ann.value().isEmpty()) {
            return PropertyName.construct(ann.value());
        }

        return defaultName;
    }

    @Override
    public PropertyName findNameForDeserialization(final Annotated a) {
        final PropertyName defaultName = super.findNameForDeserialization(a);
        final RestFullField ann = _findAnnotation(a, RestFullField.class);
        
        if (ann != null && !ann.value().isEmpty()) {
            return PropertyName.construct(ann.value());
        }
        
        return defaultName;
    }
	
}
