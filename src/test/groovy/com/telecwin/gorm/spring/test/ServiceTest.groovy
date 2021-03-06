package com.telecwin.gorm.spring.test

import com.telecwin.gorm.spring.po.Person
import com.telecwin.gorm.spring.po.Server
import com.telecwin.gorm.spring.service.PersonService
import com.telecwin.gorm.spring.service.ServerService
import org.springframework.context.support.ClassPathXmlApplicationContext
import spock.lang.Specification

class ServiceTest extends Specification {

    PersonService personService
    ServerService serverService

    def setup() {
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("/spring-datasource-sqlite-gorm.xml")
        personService = context.getBean(PersonService)
        serverService = context.getBean(ServerService)
    }

    def testService() {
        when:
        personService.savePerson(new Person(name: "Yang"))
        serverService.save(new Server(name: 'server1'))
        then:
        personService.findByName("Yang")
        personService.getAll().size() > 0

        serverService.listAll().size() > 0
    }
}
