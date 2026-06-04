package com.scm.scm.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.scm.scm.entities.User;
import com.scm.scm.forms.UserForm;
import com.scm.scm.helpers.Message;
import com.scm.scm.helpers.MessageType;
import com.scm.scm.services.UserService;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

@Controller
public class PageController {

    @Autowired
    private UserService userService;

    @GetMapping("/")
    public String  index(){
        return "redirect:/home";
    }

    @GetMapping("/home")
    public String home(Model model){
        System.out.println("Home Page Handler");

        // sending data to view 
        model.addAttribute("name","Substring Technologies");
        model.addAttribute("Engineering","Projects");
        model.addAttribute("githubRepo","https://github.com/himanshi1320");        
        return "home";
    }
 

    // about
    @GetMapping("/about")
    public String aboutPage(Model model){
        model.addAttribute("isLogin",true);
        System.out.println("About Page Loading");
        return "about";
    }

    //services
    @GetMapping("/services")
    public String servicesPage(){
        System.out.println("Services Page Loading");
        return "services";
    }

    // contact
    @GetMapping("/contact")
    public String contact(){
        return "contact";
    }

     // login

    //  this is login page

     @GetMapping("/login")
     public String showLoginForm(Model model) {
         // Add any necessary attributes to the model if needed
         return "login"; // Return the name of the login view
     }
    //  @PostMapping("/login")
    //  public String login(){
    //      return "login";
    //  }


    //  registration page
      // signup
    @GetMapping("/register")
    public String register(Model model){
        UserForm userForm=new UserForm();
        // userForm.setName("Himanshi");
        // userForm.setAbout("This is about: Write something about yourself");
        model.addAttribute("userForm",userForm);
        return "register";
    }

    // processing registration
    @RequestMapping(value="/do-register",method=RequestMethod.POST)
    public String processingRegister(@Valid @ModelAttribute UserForm userForm,BindingResult rBindingResult,HttpSession session){
        System.out.println("Processing Register");

        System.out.println(userForm);

        // fetch the form data

        // validate the data
        if(rBindingResult.hasErrors()){
            return "register";
        }
        




        // save to database
        // User user=User.builder()
        // .name(userForm.getName())
        // .email(userForm.getEmail())
        // .password(userForm.getPassword())
        // .about(userForm.getAbout())
        // .phoneNumber(userForm.getPhoneNumber())
        // .profilePic("https://thumbs.dreamstime.com/b/default-avatar-profile-icon-social-media-user-vector-default-avatar-profile-icon-social-media-user-vector-portrait-176194876.jpg")
        // .build();

        User user=new User();
        user.setName(userForm.getName());
        user.setEmail(userForm.getEmail());
        user.setPassword(userForm.getPassword());
        user.setAbout(userForm.getAbout());
        user.setEnabled(false);
        user.setPhoneNumber(userForm.getPhoneNumber());
        user.setProfilePic("https://thumbs.dreamstime.com/b/default-avatar-profile-icon-social-media-user-vector-default-avatar-profile-icon-social-media-user-vector-portrait-176194876.jpg");

       User savedUser= userService.saveUser(user);
       System.out.println("user saved: ");


        // give message "registration successful"


        Message message=Message.builder().content("Registration Successful").type(MessageType.green).build();
        session.setAttribute("message",message);

        // redirect login page


        return "redirect:/register";
    }


}
