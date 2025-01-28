package br.com.uniquedata.restfull.sdk.annotation.start;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.context.annotation.Import;

import br.com.uniquedata.restfull.sdk.implementation.beanbuild.UniqueDataRestFullBuiderBeanSpringBootRegister;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Import(UniqueDataRestFullBuiderBeanSpringBootRegister.class)
public @interface EnableUniqueDataRestFullClient {

}
