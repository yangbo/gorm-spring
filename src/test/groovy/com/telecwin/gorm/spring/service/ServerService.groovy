package com.telecwin.gorm.spring.service

import com.telecwin.gorm.spring.GormService
import com.telecwin.gorm.spring.po.Server
import grails.gorm.services.Service

@GormService
@Service(Server)
interface ServerService {
    Server save(Server server)

    List<Server> listAll()
}
