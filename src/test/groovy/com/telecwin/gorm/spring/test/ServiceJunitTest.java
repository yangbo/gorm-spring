package com.telecwin.gorm.spring.test;

import com.telecwin.gorm.spring.po.Person;
import com.telecwin.gorm.spring.po.Server;
import com.telecwin.gorm.spring.service.PersonService;
import com.telecwin.gorm.spring.service.ServerService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class ServiceJunitTest {

    private PersonService personService;
    private ServerService serverService;

    @BeforeEach
    void setup() {
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("/spring-datasource-sqlite-gorm.xml");
        personService = context.getBean(PersonService.class);
        serverService = context.getBean(ServerService.class);
    }

    @Test
    public void testService() {

        Person person = new Person();
        person.setName("Yang");
        personService.savePerson(person);
        Server server = new Server();
        server.setName("server1");
        serverService.save(server);

        Person yang = personService.findByName("Yang");
        Assertions.assertNotNull(yang);

        Assertions.assertTrue(personService.getAll().size() > 0);
        Assertions.assertTrue(serverService.listAll().size() > 0);
    }
}
