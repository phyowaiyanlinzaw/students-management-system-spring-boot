package com.ojt.StudentsBoot.controller;

import com.ojt.StudentsBoot.model.User;
import com.ojt.StudentsBoot.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
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

    @GetMapping("/disable/{id}")
    public String processUserDelete(
            @PathVariable Long id,
            RedirectAttributes redirectAttributes
    ){
        System.out.print("In Disabled URL");
        User user = userService.getUserById(id);
        user.setEnabled(false);
        userService.save(user);
        redirectAttributes.addFlashAttribute("success", "userDisableSuccess");
        return "redirect:/user/list";
    }

    @GetMapping("/toggle-active/{id}+{status}")
    public String processUserEnable(
            @PathVariable Long id,
            @PathVariable Boolean status,
            RedirectAttributes redirectAttributes
    ){
        User user = userService.getUserById(id);
        user.setEnabled(status);
        userService.save(user);
        redirectAttributes.addFlashAttribute("success", "userEnableSuccess");
        return "redirect:/user/list";
    }

    @GetMapping("/profile")
    public ModelAndView profileView(){
        return new ModelAndView("user-profile","user",new User());
    }
}
