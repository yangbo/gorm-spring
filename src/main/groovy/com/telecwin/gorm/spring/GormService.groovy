package com.telecwin.gorm.spring

import java.lang.annotation.Retention
import java.lang.annotation.RetentionPolicy

/**
 * Indicate this interface is a <a href="http://gorm.grails.org/latest/hibernate/manual/index.html#dataServices">GORM Data Service</a>.
 */
@Retention(RetentionPolicy.RUNTIME)
@interface GormService {
}
