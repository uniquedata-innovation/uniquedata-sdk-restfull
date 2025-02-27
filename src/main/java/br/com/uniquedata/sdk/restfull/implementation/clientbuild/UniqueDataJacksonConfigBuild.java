package br.com.uniquedata.sdk.restfull.implementation.clientbuild;

import java.io.IOException;
import java.text.SimpleDateFormat;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.AnnotationIntrospector;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.introspect.AnnotationIntrospectorPair;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import br.com.uniquedata.sdk.restfull.implementation.beanbuild.UniqueDataRestFullIntrospectorField;

public class UniqueDataJacksonConfigBuild {
	
    private static final Logger LOGGER = LoggerFactory.getLogger(UniqueDataJacksonConfigBuild.class);
	
	private static ObjectMapper objectMapper;
	
	static {
	 	objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        objectMapper.configure(JsonGenerator.Feature.WRITE_BIGDECIMAL_AS_PLAIN, true);
        
        final AnnotationIntrospector defaultIntrospector = objectMapper
        	.getSerializationConfig().getAnnotationIntrospector();

        final AnnotationIntrospector introspectorPair = new AnnotationIntrospectorPair(
        	new UniqueDataRestFullIntrospectorField(), defaultIntrospector
        );

        objectMapper.setAnnotationIntrospector(introspectorPair);
        LOGGER.info("The com.fasterxml.jackson.databind.ObjectMapper process has been successfully customized and initialized by UniqueData RestFull SDK!");
	}
	
	public static ObjectMapper getCustomObjectMapper() {
        return objectMapper;
    }
	
	public static String toJson(final Object object) {
		try {
			return objectMapper.writeValueAsString(object);
		}catch (Exception e) {
			LOGGER.error("jackson.databind.ObjectMapper", e);
			throw new RuntimeException(e);
		}
	}
	
	public static String toJsonPrettyPrinting(final Object object) {
		try {
			return objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(object);
		}catch (Exception e) {
			LOGGER.error("jackson.databind.ObjectMapper", e);
			throw new RuntimeException(e);
		}
	}
	
	public static <T> T from(final String json, final Class<T> type) {
		try {
			return objectMapper.readValue(json, type);
		}catch (Exception e) {
			LOGGER.error("jackson.databind.ObjectMapper", e);
			throw new RuntimeException(e);
		}
	}
	
	public static <T> T from(final String json, final Class<T> type, final String pattern) {
		try {
			return objectMapper.setDateFormat(new SimpleDateFormat(pattern)).readValue(json, type);
		}catch (Exception e) {
			LOGGER.error("jackson.databind.ObjectMapper", e);
			throw new RuntimeException(e);
		}
	}
	
	public static boolean canParse(final String json, final Class<?> type) {
	    try {
	    	objectMapper.readValue(json, type); return true;
	    } catch (IOException e) {
	    	return false;
	    }
	}
	
	public static boolean canParseOrThrow(final String json, final Class<?> type) {
	    try {
	    	objectMapper.readValue(json, type); return true;
	    } catch (IOException e) {
	    	LOGGER.error("jackson.databind.ObjectMapper", e);
	    	throw new RuntimeException(e);
	    }
	}

}
