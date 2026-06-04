package com.scm.scm.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.scm.scm.repositories.UserRepo;

@Service
public class SecurityCustomUserDetailService implements UserDetailsService{


    @Autowired
    private UserRepo userRepo;
    
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        //  load the user
       return userRepo.findByEmail(username).orElseThrow(()->new UsernameNotFoundException("user not found with this email"));
         
    }
    

}
