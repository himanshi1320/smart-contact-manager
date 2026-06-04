package com.scm.scm.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.scm.scm.entities.Contact;
import com.scm.scm.services.ContactService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;


@RestController
@RequestMapping("/api")
public class ApiController {

    // get contacts of the user

    @Autowired
    private ContactService contactService;
    
@GetMapping("/contacts/{contactId}")
public Contact getContact(@PathVariable String contactId){
    return contactService.getById(contactId);

    }

}
