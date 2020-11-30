package com.telecwin.gorm.spring

import grails.gorm.annotation.Entity
import groovy.util.logging.Slf4j
import org.grails.datastore.gorm.utils.AnnotationMetadataReaderFactory
import org.springframework.beans.factory.annotation.AnnotatedBeanDefinition
import org.springframework.beans.factory.config.BeanDefinition
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider
import org.springframework.core.type.AnnotationMetadata
import org.springframework.core.type.filter.AnnotationTypeFilter

import java.lang.annotation.Annotation

/**
 * Scan classes or interface with the annotation under the packages.
 */
@Slf4j
class SpringClassScanner {
    /**
     * scan the classes under these packages
     */
    List<String> packages = []
    /**
     * scan classes with these annotations
     */
    List<Class<? extends Annotation>> annotations = [Entity, javax.persistence.Entity]
    /**
     * ignore classes under these packages
     */
    List<String> ignoredPackages = []
    /**
     * Load scanned class with this class loader
     */
    ClassLoader classLoader

    /**
     * Scan classes in the packages and load these Class with specified class-loader.
     * @param classLoader The class loader to load entity class
     * @return the scanned classes or interfaces.
     */
    @SuppressWarnings("GroovyAccessibility")
    Class[] getClassFromPackages() {
        ClassPathScanningCandidateComponentProvider componentProvider = new ClassPathScanningCandidateComponentProvider(false) {
            /**
             * Only require the class is independent, it can be class or interface.
             */
            protected boolean isCandidateComponent(AnnotatedBeanDefinition beanDefinition) {
                AnnotationMetadata metadata = beanDefinition.getMetadata()
                return metadata.isIndependent()
            }
        }
        componentProvider.setMetadataReaderFactory(new AnnotationMetadataReaderFactory(classLoader))
        for (ann in annotations) {
            componentProvider.addIncludeFilter(new AnnotationTypeFilter(ann))
        }
        Collection<Class> classes = new HashSet<>()
        for (String packageName in packages) {
            if (ignoredPackages.contains(packageName)) {
                log.error("Package [$packageName] will not be scanned as it is too generic and will slow down startup time. Use a more specific package")
            } else {
                for (BeanDefinition candidate in componentProvider.findCandidateComponents(packageName)) {
                    Class persistentEntity = Class.forName(candidate.beanClassName, false, classLoader)
                    classes.add persistentEntity
                }
            }
        }
        return classes as Class[]
    }
}
