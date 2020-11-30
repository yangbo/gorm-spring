package com.telecwin.gorm.spring.service

import com.telecwin.gorm.spring.GormService
import com.telecwin.gorm.spring.po.Person
import grails.gorm.services.Service
import grails.gorm.transactions.Transactional

@GormService
interface PersonService {
    Person savePerson(Person person)
    Person findByName(String name)
    List<Person> getAll()
}

@Service(Person)
abstract class PersonServiceImpl implements PersonService {

    @Transactional
    List<Person> getAll() {
        Person.getAll()
    }
}
