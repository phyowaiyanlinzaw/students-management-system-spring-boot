package com.ojt.StudentsBoot.controller;

import com.ojt.StudentsBoot.model.User;
import com.ojt.StudentsBoot.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

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
            System.out.println(user.getName());
            redirectAttributes.addFlashAttribute("success", "userAddSuccess");
        }catch (DataIntegrityViolationException e){
            redirectAttributes.addFlashAttribute("error", "userDupe");
        }
            return "redirect:/user/list";
    }

    @GetMapping("/edit")
    public ModelAndView userEditView(){
        return new ModelAndView("user-edit","user",new User());
    }

    @PostMapping("/edit")
    public String processUserEdit(){
        return "redirect:/user/list";
    }

    @GetMapping("/disable/{id}")
    public String processUserDelete(
            @PathVariable Long id,
            RedirectAttributes redirectAttributes
    ){
        User user = userService.getUserById(id);
        user.setEnabled(false);
        userService.save(user);
        redirectAttributes.addFlashAttribute("success", "userDisableSuccess");
        return "redirect:/user/list";
    }

    @GetMapping("/enable/{id}")
    public String processUserEnable(
            @PathVariable Long id,
            RedirectAttributes redirectAttributes
    ){
        User user = userService.getUserById(id);
        user.setEnabled(true);
        userService.save(user);
        redirectAttributes.addFlashAttribute("success", "userEnableSuccess");
        return "redirect:/user/list";
    }

    @GetMapping("/profile")
    public ModelAndView profileView(){
        return new ModelAndView("user-profile","user",new User());
    }
}
