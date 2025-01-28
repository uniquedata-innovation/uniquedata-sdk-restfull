package br.com.uniquedata.restfull.sdk.implementation.start;

import org.reflections.Reflections;
import org.reflections.scanners.SubTypesScanner;
import org.reflections.scanners.TypeAnnotationsScanner;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;
import org.reflections.util.FilterBuilder;

import br.com.uniquedata.restfull.sdk.annotation.advanced.AutoAuthentication;
import br.com.uniquedata.restfull.sdk.annotation.simple.UniqueDataRestFullClient;

public class UniqueDataRestFullPackageIdentifier {
	
	public static void packageSimpleScanners(final Class<?> classeType) {
		final Reflections reflections = new Reflections(new ConfigurationBuilder()
			.filterInputsBy(new FilterBuilder().include(".*\\.class$"))
			.setUrls(ClasspathHelper.forJavaClassPath()));
		
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