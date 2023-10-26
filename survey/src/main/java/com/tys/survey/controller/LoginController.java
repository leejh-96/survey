package com.tys.survey.controller;

import com.tys.survey.dto.JoinFormDTO;
import com.tys.survey.dto.LoginMemberDTO;
import com.tys.survey.service.LoginService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Controller
public class LoginController {

    private final LoginService service;

    public LoginController(LoginService service){
        this.service = service;
    }

    @GetMapping("/loginForm")
    public String loginForm(Model model){
        model.addAttribute("loginMemberDTO",new LoginMemberDTO());
        return "login/loginForm";
    }

    @PostMapping("/login")
    public String login(@Validated @ModelAttribute LoginMemberDTO login, BindingResult bindingResult, HttpSession httpSession){
        if (bindingResult.hasErrors()){
            return "login/loginForm";
        }
        if (!service.login(login, httpSession)){
            bindingResult.reject("LoginFail");
            return "login/loginForm";
        }
        return "redirect:/";
    }

    @GetMapping("/logout")
    public String logout(HttpServletRequest request){
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }
        return "redirect:/";
    }

    @GetMapping("/findPassword")
    public String findPassword(Model model){
        model.addAttribute("joinFormDTO",new JoinFormDTO());
        return "join/findPassword";
    }

}
