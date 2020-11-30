package com.telecwin.gorm.spring

import groovy.util.logging.Slf4j
import org.grails.orm.hibernate.HibernateDatastore
import org.springframework.beans.BeansException
import org.springframework.beans.factory.BeanNameAware
import org.springframework.beans.factory.config.BeanDefinition
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory
import org.springframework.beans.factory.support.BeanDefinitionBuilder
import org.springframework.beans.factory.support.BeanDefinitionRegistry
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor
import org.springframework.lang.Nullable

/**
 * Scan GORM entity and service class based on package name and annotations.
 *
 * Then register services as spring beans.
 * Register entity class as hibernate persistent entity.
 *
 * @author bo.yang
 */
@Slf4j
class GormScanner implements BeanDefinitionRegistryPostProcessor, BeanNameAware {
    /**
     * Scanned service packages
     */
    String[] servicePackages = []
    /**
     * Ignored packages
     */
    String[] ignoredPackages = []
    /**
     * Scanned interfaces or classes that annotated with these Annotation
     */
    Class[] serviceAnnotations = [GormService]

    HibernateDatastore hibernateDataStore

    /**
     * My Bean Name
     */
    String name

    /**
     * Scan and register service beans.
     * @param registry
     * @throws BeansException
     */
    @Override
    void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry) throws BeansException {
        Class[] serviceClasses = scanServiceClasses()
        for (Class serviceClass in serviceClasses) {
            String serviceBeanName = serviceClass.getSimpleName().uncapitalize()
            BeanDefinitionBuilder beanDefinitionBuilder = BeanDefinitionBuilder.genericBeanDefinition(serviceClass)
            // set instance factory method and argument
            beanDefinitionBuilder.setFactoryMethodOnBean('createServiceInstance', name)
            beanDefinitionBuilder.addConstructorArgValue(serviceClass)
            // set dependencies
            // beanDefinitionBuilder.addPropertyReference("personDao", "personDao")
            BeanDefinition serviceDefinition = beanDefinitionBuilder.getRawBeanDefinition()
            // register bean
            registry.registerBeanDefinition(serviceBeanName, serviceDefinition)
        }
    }

    @Override
    void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
    }

    @SuppressWarnings('unused')
    Object createServiceInstance(Class serviceClass) throws Exception {
        hibernateDataStore.getService(serviceClass)
    }

    /**
     * Assert that an object is not {@code null}.
     * <pre class="code">Assert.notNull(clazz, "The class must not be null");</pre>
     * @param object the object to check
     * @param message the exception message to use if the assertion fails
     * @throws java.lang.IllegalArgumentException if the object is {@code null}
     */
    @SuppressWarnings('unused')
    static void notNull(@Nullable Object object, String message) {
        if (object == null) {
            throw new IllegalArgumentException(message)
        }
    }

    Class[] scanServiceClasses() {
        SpringClassScanner springClassScanner = new SpringClassScanner(packages: servicePackages,
                annotations: serviceAnnotations, ignoredPackages: ignoredPackages,
                classLoader: ClassLoader.getSystemClassLoader())
        springClassScanner.getClassFromPackages()
    }

    @Override
    void setBeanName(String name) {
        this.name = name
    }
}
