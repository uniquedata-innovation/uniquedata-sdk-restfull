package br.com.uniquedata.sdk.restfull.annotation.start;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.context.annotation.Import;

import br.com.uniquedata.sdk.restfull.implementation.beanbuild.UniqueDataRestFullBuiderBeanSpringBootRegister;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Import(UniqueDataRestFullBuiderBeanSpringBootRegister.class)
public @interface EnableUniqueDataRestFullClient {

}
