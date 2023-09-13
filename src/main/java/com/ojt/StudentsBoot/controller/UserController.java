package com.ojt.StudentsBoot.controller;

import com.ojt.StudentsBoot.model.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/user")
public class UserController {

    @GetMapping("/list")
    public String users(){
        return "user-list";
    }

    @GetMapping("/add")
    public ModelAndView userAddView(){
        return new ModelAndView("user-add","user",new User());
    }

    @PostMapping("/add")
    public String processUserAdd(){
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

    @GetMapping("/delete")
    public String processUserDelete(){
        return "redirect:/user/list";
    }
}
