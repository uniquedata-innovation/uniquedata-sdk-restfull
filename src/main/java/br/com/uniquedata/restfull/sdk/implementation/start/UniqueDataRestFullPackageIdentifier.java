package br.com.uniquedata.restfull.sdk.implementation.start;

import java.net.URL;
import java.util.HashSet;
import java.util.Set;

import org.reflections.Reflections;
import org.reflections.scanners.SubTypesScanner;
import org.reflections.scanners.TypeAnnotationsScanner;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;
import org.reflections.util.FilterBuilder;

import br.com.uniquedata.restfull.sdk.annotation.advanced.AutoAuthentication;
import br.com.uniquedata.restfull.sdk.annotation.simple.UniqueDataRestFullClient;

public class UniqueDataRestFullPackageIdentifier {
	
	public static void packageSimpleScanners() {
        final Set<URL> urls = new HashSet<>(ClasspathHelper.forClassLoader());
        urls.addAll(ClasspathHelper.forJavaClassPath());

        final Reflections reflections = new Reflections(new ConfigurationBuilder()
        	.setUrls(urls)
            .setScanners(new TypeAnnotationsScanner(), new SubTypesScanner()));
		
		reflections.getTypesAnnotatedWith(AutoAuthentication.class).forEach(classType -> {
			UniqueDataRestFullManagerBean.addClassType(classType);
		});
		
		reflections.getTypesAnnotatedWith(UniqueDataRestFullClient.class).forEach(classType -> {
			UniqueDataRestFullManagerBean.addClassType(classType);
		});
	}

	public static void packageAdvancedScanners(final Class<?> classeType) {
		final Reflections reflections = new Reflections(new ConfigurationBuilder()
			.filterInputsBy(new FilterBuilder().include(".*\\.class$"))
			.setUrls(ClasspathHelper.forJavaClassPath())
			.setScanners(new SubTypesScanner(false), new TypeAnnotationsScanner()));
				
		reflections.getTypesAnnotatedWith(AutoAuthentication.class).forEach(classType -> {
			UniqueDataRestFullManagerBean.addClassType(classType);
		});
		
		reflections.getTypesAnnotatedWith(UniqueDataRestFullClient.class).forEach(classType -> {
			UniqueDataRestFullManagerBean.addClassType(classType);
		});
	}
	
}