package com.ojt.StudentsBoot.controller;

import com.ojt.StudentsBoot.model.Course;
import com.ojt.StudentsBoot.model.CustomerUserDetails;
import com.ojt.StudentsBoot.model.Role;
import com.ojt.StudentsBoot.model.User;
import com.ojt.StudentsBoot.service.CourseService;
import com.ojt.StudentsBoot.service.UserService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
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
    public String profileView(ModelMap modelMap, Principal principal) {

        // Get the currently authenticated user
        String username = principal.getName();
        User user = userService.getUserByUsername(username);

        modelMap.addAttribute("user",user);

        return "user-profile"; // You may want to return an error page or handle this case differently
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
            redirectAttributes.addFlashAttribute("error","userDupe");
            return "redirect:/user/profile";
        }

        return "redirect:/user/profile";
    }

    @PostMapping("/change-password")
    public String changePassword(
            @RequestParam("currentPassword") String currentPw,
            @RequestParam("newPassword") String pw,
            @RequestParam("renewPassword") String rePw,
            @RequestParam("id") Long id,
            RedirectAttributes redirectAttributes,
            ModelMap modelMap
    ){
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        User userFromDb = userService.getUserById(id);
        if(pw.equals(rePw)&&bCryptPasswordEncoder.matches(currentPw,userFromDb.getPassword())){
            userFromDb.setPassword(bCryptPasswordEncoder.encode(pw));
            userService.save(userFromDb);
            redirectAttributes.addFlashAttribute("success","changePwSuccess");
        }else{
            redirectAttributes.addFlashAttribute("error","changePwError");
            return "redirect:/user/profile";
        }


        return "redirect:/user/profile";
    }

    @GetMapping("/report/pdf")
    public String exportPdfFile(HttpServletResponse response){
        userService.generatePdf(response);
        return "redirect:/user/list";
    }

    @GetMapping("/report/excel")
    public String exportExcelFile(HttpServletResponse response){
        userService.generateExcel(response);
        return "redirect:/user/list";
    }
}
