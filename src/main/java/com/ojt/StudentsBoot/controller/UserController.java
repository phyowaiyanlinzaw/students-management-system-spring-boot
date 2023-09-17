package com.ojt.StudentsBoot.controller;

import com.ojt.StudentsBoot.model.Course;
import com.ojt.StudentsBoot.model.Role;
import com.ojt.StudentsBoot.model.User;
import com.ojt.StudentsBoot.service.CourseService;
import com.ojt.StudentsBoot.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/user")
@Slf4j
public class UserController {

    private final UserService userService;
    private final CourseService courseService;

    @GetMapping("/list")
    public String users(){
        return "user-list";
    }

    @GetMapping("/add")
    public ModelAndView userAddView(){
        return new ModelAndView("user-add","user",new User());
    }

    @PostMapping("/add")
    public String processUserAdd(
            @ModelAttribute("user") User user,
            RedirectAttributes redirectAttributes
    ){
        try{
            userService.save(user);
            redirectAttributes.addFlashAttribute("success", "userAddSuccess");
        }catch (DataIntegrityViolationException e){
            redirectAttributes.addFlashAttribute("error", "userDupe");
            return "redirect:/user/add";
        }
            return "redirect:/user/list";
    }

    @GetMapping("/edit/{id}")
    public ModelAndView userEditView(
            @PathVariable Long id
    ){
        User user = userService.getUserById(id);
        return new ModelAndView("user-edit","user",user);
    }

    @PostMapping("/edit")
    public String processUserEdit(
            @ModelAttribute("user") User user,
            RedirectAttributes redirectAttributes,
            ModelMap modelMap
    ){
        try{
            userService.save(user);
            redirectAttributes.addFlashAttribute("success","userEditSuccess");
        }catch (DataIntegrityViolationException e){
            modelMap.addAttribute("error","userDupe");
            return "user-edit";
        }
        return "redirect:/user/list";
    }

    @GetMapping("/profile")
    public String profileView(
            Principal principal,
            ModelMap modelMap
            ){
        String username = principal.getName();
        User user = userService.getUserByUsername(username);

        if (user == null) {
            // Handle the case where the user is not found
            modelMap.addAttribute("error", "userNotFound");
            return "user-profile"; // You may want to return an error page or handle this case differently
        }

        modelMap.addAttribute("user",user);
        return "user-profile";
    }

    @PostMapping("/update-user-profile")
    public String processUserProfile(
            @ModelAttribute("user") User user,
            RedirectAttributes redirectAttributes,
            ModelMap modelMap
    ){
        try{
            User userFromDB = userService.getUserById(user.getId());
            user.setRoles(userFromDB.getRoles());
            user.setPassword(userFromDB.getPassword());
            user.setEnabled(userFromDB.isEnabled());
            userService.save(user);
            redirectAttributes.addFlashAttribute("success","userEditSuccess");
        }catch (DataIntegrityViolationException e){
            modelMap.addAttribute("error","userDupe");
            return "user-profile";
        }


        return "user-profile";
    }
}
