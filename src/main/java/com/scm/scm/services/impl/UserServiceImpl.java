package com.scm.scm.services.impl;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.scm.scm.entities.User;
import com.scm.scm.helpers.AppConstants;
import com.scm.scm.helpers.Helper;
import com.scm.scm.helpers.ResourceNotFoundException;
import com.scm.scm.repositories.UserRepo;
import com.scm.scm.services.EmailService;
import com.scm.scm.services.UserService;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private PasswordEncoder passwordEncoder;

      @Autowired
    private EmailService emailService;

    @Autowired
    private  Helper helper;

    private Logger logger=LoggerFactory.getLogger(this.getClass());

    
/*********************************************** */
    @Override
    public User saveUser(User user) {
        // Check if the user already exists by email
        Optional<User> existingUserOpt = userRepo.findByEmail(user.getEmail());

        if (existingUserOpt.isPresent()) {
            User existingUser = existingUserOpt.get();
            
            // Check if the provider matches
            if (existingUser.getProvider().equals(user.getProvider()) &&
                existingUser.getProviderUserId().equals(user.getProviderUserId())) {
                // Return the existing user if the provider matches
                return existingUser;
            } else {
                // Handle the case where the user exists with a different provider
                throw new IllegalArgumentException("User exists with a different authentication provider.");
            }
        }
        /**************************************************** */

        // If the user does not exist, generate a new user ID
        String userId = UUID.randomUUID().toString();
        user.setUserId(userId);

        // Set password only if it is needed
        user.setPassword(passwordEncoder.encode(user.getPassword())); // Uncomment if you handle passwords for regular users

        // Set user role
        user.setRoleList(List.of(AppConstants.ROLE_USER));

        logger.info(user.getProvider().toString());
        String emailToken = UUID.randomUUID().toString();
        user.setEmailToken(emailToken);
        User savedUser = userRepo.save(user);
        String emailLink = helper.getLinkForEmailVerificatiton(emailToken);
        emailService.sendEmail(savedUser.getEmail(), "Verify Account : Smart  Contact Manager", emailLink);
        return savedUser;

    }


    @Override
    public Optional<User> getUserById(String id) {
       return userRepo.findById(id);
         }

    @Override
    public Optional<User> updateUser(User user) {
        User user2=userRepo.findById(user.getUserId()).orElseThrow(()->new ResourceNotFoundException("user not found"));
        //   update user2 from user

        user2.setName(user.getName());
        user2.setEmail(user.getEmail());
        user2.setPassword(user.getPassword());
        user2.setAbout(user.getAbout());
        user2.setPhoneNumber(user.getPhoneNumber());
        user2.setProfilePic(user.getProfilePic());
        user2.setEnabled(user.isEnabled());
        user2.setEmailVerified(user.isEmailVerified());
        user2.setPhoneVerified(user.isPhoneVerified());
        user2.setProvider(user.getProvider());
        user2.setProviderUserId(user.getProviderUserId());

       // save the user in database

       User save=userRepo.save(user2);
       return Optional.ofNullable(save);

    }

    @Override
    public void deleteUser(String id) {
        User user2=userRepo.findById(id).orElseThrow(()->new ResourceNotFoundException("user not found"));
         userRepo.delete(user2);
    }

    @Override
    public boolean isUserExist(String userId) {
        User user2=userRepo.findById(userId).orElse(null);
        return user2!=null?true:false;
            }

    @Override
    public boolean isUserExistByEmail(String email) {
       User user= userRepo.findByEmail(email).orElse(null);
        return user!=null?true:false;

           }

    @Override
    public List<User> getAllUsers() {
        return userRepo.findAll();

         }


    @Override
    public User getUserByEmail(String email) {
        return userRepo.findByEmail(email).orElseThrow(null);
         }

}
