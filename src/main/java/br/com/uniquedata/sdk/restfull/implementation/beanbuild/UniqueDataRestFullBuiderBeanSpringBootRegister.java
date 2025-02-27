package br.com.uniquedata.sdk.restfull.implementation.beanbuild;

import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.type.AnnotationMetadata;

import br.com.uniquedata.sdk.restfull.implementation.start.UniqueDataRestFull;

public class UniqueDataRestFullBuiderBeanSpringBootRegister implements ImportBeanDefinitionRegistrar, ApplicationContextAware {
	
    @Override
    public void registerBeanDefinitions(final AnnotationMetadata metadata, final BeanDefinitionRegistry registry) {
    	if (!registry.containsBeanDefinition("uniqueDataRestFullBuiderBean")) {
        	UniqueDataRestFull.startSptringBootScan();
        	
            final BeanDefinitionBuilder builder = BeanDefinitionBuilder.genericBeanDefinition(UniqueDataRestFullBuiderBean.class);
            builder.setAutowireMode(AutowireCapableBeanFactory.AUTOWIRE_BY_TYPE);
            registry.registerBeanDefinition("uniqueDataRestFullBuiderBean", builder.getBeanDefinition());
        }
    }

	@Override
	public void setApplicationContext(final ApplicationContext applicationContext)	{}

	@SuppressWarnings("unused")
	private String renameBean(final String classTypeSimpleName) {
		return String.valueOf(classTypeSimpleName.charAt(0)).toLowerCase()
			.concat(classTypeSimpleName.substring(1, classTypeSimpleName.length()));
	}
	
}
