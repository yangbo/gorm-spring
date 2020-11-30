package com.telecwin.gorm.spring.test

import com.telecwin.gorm.spring.po.Person
import com.telecwin.gorm.spring.service.PersonService
import org.springframework.context.support.ClassPathXmlApplicationContext
import spock.lang.Specification

class ServiceTest extends Specification {

    PersonService personService

    def setup() {
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("/spring-datasource-sqlite-gorm.xml")
        personService = context.getBean(PersonService)
    }

    def testService() {
        when:
        personService.savePerson(new Person(name: "Yang"))

        then:
        personService.findByName("Yang")
        personService.getAll().size() > 0
    }
}
