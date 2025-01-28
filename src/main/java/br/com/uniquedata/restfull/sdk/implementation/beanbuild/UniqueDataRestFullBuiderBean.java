package br.com.uniquedata.restfull.sdk.implementation.beanbuild;

import java.util.concurrent.atomic.AtomicBoolean;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;

import br.com.uniquedata.restfull.sdk.annotation.advanced.AutoAuthentication;
import br.com.uniquedata.restfull.sdk.annotation.simple.UniqueDataRestFullClient;
import br.com.uniquedata.restfull.sdk.exception.UniqueDataRestFullException;
import br.com.uniquedata.restfull.sdk.exception.UniqueDataRestFullException.ExceptionType;
import br.com.uniquedata.restfull.sdk.helper.ObjectReflectionHelper;
import br.com.uniquedata.restfull.sdk.implementation.authentication.UniqueDataRestFullAutoAuthencationBuild;
import br.com.uniquedata.restfull.sdk.implementation.authentication.UniqueDataRestFullAutoAuthenticationInvocationHandler;
import br.com.uniquedata.restfull.sdk.implementation.authentication.UniqueDataRestFullAutoAuthenticationValidade;
import br.com.uniquedata.restfull.sdk.implementation.start.UniqueDataRestFullManagerBean;

@Order(Ordered.HIGHEST_PRECEDENCE)
public class UniqueDataRestFullBuiderBean implements BeanPostProcessor, BeanFactoryAware {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(UniqueDataRestFullBuiderBean.class);
	
	private static AtomicBoolean newBuildBean = new AtomicBoolean(true);
	
    private DefaultListableBeanFactory beanFactory;

    @Override
    public void setBeanFactory(final BeanFactory beanFactory) throws BeansException {
        if (beanFactory instanceof DefaultListableBeanFactory) {
            this.beanFactory = (DefaultListableBeanFactory) beanFactory;
            build();
        } else {
            throw new IllegalStateException("BeanFactory not instance of DefaultListableBeanFactory");
        }
    }

    public void build() {
		try {
			for (final Class<?> classType : UniqueDataRestFullManagerBean.getClassTypes()) {
				if(classType.isInterface()) {
					if(classType.isAnnotationPresent(UniqueDataRestFullClient.class)) {
						invocationHandlerMethodRequestBuild(classType);
					}else if(classType.isAnnotationPresent(AutoAuthentication.class)) {
						autoAuthenticationInterfaceBuild(classType);	
					}
				}else if(classType.isAnnotationPresent(AutoAuthentication.class)){
					autoAuthenticationClassBuild(classType);
				}
			}
		}catch (Exception e) {
			LOGGER.error(null, e);
		}finally {
			newBuildBean.set(false);
		}
		
		LOGGER.info("UniqueData RestFull SDK initialized successfully!");
	}
    
    private void autoAuthenticationInterfaceBuild(final Class<?> interfacetype) {
    	final AutoAuthentication annotation = interfacetype.getAnnotation(AutoAuthentication.class);
    	final UniqueDataRestFullAutoAuthenticationValidade authenticationValidade = new UniqueDataRestFullAutoAuthenticationValidade();
		final UniqueDataRestFullAutoAuthenticationValidade.Validate validate = authenticationValidade.validate(annotation);
		
		if(validate.isSuccess()) {
			final Object newProxyInstance = ObjectReflectionHelper.newProxyInstance(
				UniqueDataRestFullAutoAuthencationBuild.class, new UniqueDataRestFullAutoAuthenticationInvocationHandler(annotation));
			
			((UniqueDataRestFullAutoAuthencationBuild) newProxyInstance).authenticate();
			((UniqueDataRestFullAutoAuthencationBuild) newProxyInstance).interception();;
			
			if(isSpringBoot()) {
				beanFactory.registerSingleton(interfacetype.getSimpleName(), newProxyInstance);
			}
				
			UniqueDataRestFullManagerBean.addBean(interfacetype, newProxyInstance);	
		}else {
			LOGGER.error(validate.getMessage());
			throw new UniqueDataRestFullException(validate.getMessage(), ExceptionType.START_CONFIGURATION);
		}
    }
    
    private void autoAuthenticationClassBuild(final Class<?> classType) {
    	final AutoAuthentication annotation = classType.getAnnotation(AutoAuthentication.class);
    	final UniqueDataRestFullAutoAuthenticationValidade authenticationValidade = new UniqueDataRestFullAutoAuthenticationValidade();
		final UniqueDataRestFullAutoAuthenticationValidade.Validate validate = authenticationValidade.validate(annotation);
		
		if(validate.isSuccess()) {
			System.out.println("FALTA IMPLEMENTAR o AutoAuthentication para quando for class e nao interface UniqueDataRestFullBuiderBean");
			
//			if(isSpringBoot()) {
//				beanFactory.registerSingleton(classType.getSimpleName(), newProxyInstance);
//			}
//				
//			UniqueDataRestFullManagerBean.addBean(classType, newProxyInstance);	
			
		}else {
			throw new UniqueDataRestFullException(validate.getMessage(), ExceptionType.START_CONFIGURATION);
		}
    }
    
    private void invocationHandlerMethodRequestBuild(final Class<?> interfaceType) {
    	final UniqueDataRestFullClient annotation = interfaceType.getAnnotation(UniqueDataRestFullClient.class);
    	final Class<?> autoAuthEndpointMonitor = annotation.autoAuthEndpointMonitor();	
    	
    	if(autoAuthEndpointMonitor.equals(Void.class)) {
    		final Object newProxyInstance = ObjectReflectionHelper.newProxyInstance(interfaceType, 
				new UniqueDataRestFullBuiderBeanInvocationHandler(annotation.baseUrl(), Void.class));
    		
    		if(isSpringBoot()) {
    			beanFactory.registerSingleton(interfaceType.getSimpleName(), newProxyInstance);
			}
				
			UniqueDataRestFullManagerBean.addBean(interfaceType, newProxyInstance);			
    	}else if(autoAuthEndpointMonitor.isAnnotationPresent(AutoAuthentication.class)) {
    		final Class<?> typeClassCredential = autoAuthEndpointMonitor.getAnnotation(
				AutoAuthentication.class).authenticate().typeClassCredential();
				
			final Object newProxyInstance = ObjectReflectionHelper.newProxyInstance(interfaceType, 
				new UniqueDataRestFullBuiderBeanInvocationHandler(annotation.baseUrl(), typeClassCredential));
			
			if(isSpringBoot()) {
				beanFactory.registerSingleton(interfaceType.getSimpleName(), newProxyInstance);
			}
			
			UniqueDataRestFullManagerBean.addBean(interfaceType, newProxyInstance);
    	}else {
    		LOGGER.error("AutoAuthEndpointMonitor["+autoAuthEndpointMonitor.getSimpleName()+"] is enbale but no have @AutoAuthentication");
			throw new UniqueDataRestFullException("AutoAuthEndpointMonitor["+autoAuthEndpointMonitor.getSimpleName()+"] is enbale but no have @AutoAuthentication", ExceptionType.START_CONFIGURATION);
		}
    }
    
	public static boolean isNewBuildBean() {
		return newBuildBean.get();
	}
	
	private boolean isSpringBoot() {
		return beanFactory != null;
	}
	
}