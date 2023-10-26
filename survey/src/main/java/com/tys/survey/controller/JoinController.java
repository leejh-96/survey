package com.tys.survey.controller;

import com.tys.survey.dto.JoinFormDTO;
import com.tys.survey.service.EmailService;
import com.tys.survey.service.JoinService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Controller
@RequestMapping("/join")
public class JoinController {

    private final EmailService emailService;

    private final JoinService joinService;

    public JoinController(EmailService emailService, JoinService joinService) {
        this.emailService = emailService;
        this.joinService = joinService;
    }

    @GetMapping("/form")
    public String joinForm(@RequestParam(required = false) Boolean result, Model model){
        model.addAttribute("result",result);
        model.addAttribute("joinFormDTO",new JoinFormDTO());
        return "join/joinForm";
    }

    @PostMapping("/save")
    public String joinSave(@Validated @ModelAttribute JoinFormDTO joinFormDTO, BindingResult bindingResult){
        if (bindingResult.hasErrors()){
            return "join/joinForm";
        }
        joinService.save(joinFormDTO);
        return "redirect:/join/form?result=true";
    }

    @ResponseBody
    @PostMapping("/update/password")
    public String updatePassword(@ModelAttribute JoinFormDTO joinFormDTO){
        joinService.updatePassword(joinFormDTO);
        return "/loginForm";
    }

    @ResponseBody
    @PostMapping("/sendMail")
    public Map<String, Object> sendMail(@RequestParam("memberEmail") String memberEmail){
        Map<String, Object> map = joinService.emailCheck(memberEmail);
        if ((boolean)map.get("result")){
            map = emailService.sendMail(map,memberEmail);
        }
        return map;
    }

    @ResponseBody
    @PostMapping("/nickNameCheck")
    public boolean nickNameCheck(@RequestParam("nickName")String nickName){
        return joinService.nickNameCheck(nickName);
    }

    @ResponseBody
    @PostMapping("/findPassword")
    public Map<String, Object> findPassword(@RequestParam("memberId")String memberId){
        Map<String, Object> map = joinService.emailCheck(memberId);
        if ((boolean)map.get("result") == false){
            map = emailService.sendMail(map,memberId);
        }
        return map;
    }

}
