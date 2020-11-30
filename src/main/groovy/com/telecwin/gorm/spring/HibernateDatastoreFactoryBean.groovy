package com.telecwin.gorm.spring

import grails.gorm.annotation.Entity
import groovy.util.logging.Slf4j
import org.grails.orm.hibernate.HibernateDatastore
import org.springframework.beans.factory.FactoryBean

import java.lang.annotation.Annotation

/**
 * Factory bean that create HibernateDatastore bean.
 * Because we need to solve the issue that Package parameter of HibernateDatastore constructor can not be used
 * in the xml spring configure.
 *
 * Chinese Blog: https://blog.csdn.net/yangbo_hr/article/details/110308332
 *
 * @author bo.yang
 */
@Slf4j
class HibernateDatastoreFactoryBean implements FactoryBean<HibernateDatastore> {

    Map configure
    List<String> packages = []
    List<Class<? extends Annotation>> entityAnnotations = [Entity, javax.persistence.Entity]
    List<String> ignoredPackages = []

    @Override
    HibernateDatastore getObject() throws Exception {
        log.debug("start build HibernateDatastore object.")
        SpringClassScanner springClassScanner = new SpringClassScanner(packages: packages, annotations: entityAnnotations,
                ignoredPackages: ignoredPackages, classLoader: ClassLoader.getSystemClassLoader())
        Class[] classes = springClassScanner.getClassFromPackages()
        def store = new HibernateDatastore(configure, classes)
        log.debug("return HibernateDatastore object: {}", store)
        return store
    }

    @Override
    Class<?> getObjectType() {
        return HibernateDatastore
    }

    @SuppressWarnings("unused")
    void printClassLoaderHierarchy(ClassLoader classLoader) {
        log.debug "=" * 50 + " '${classLoader}' 's ClassLoader Hierarchy:"
        def cl = this.class.classLoader
        while (cl) {
            log.debug cl as String
            cl = cl.parent
        }
    }

}
