package com.tys.survey.controller;

import com.tys.survey.dto.JoinFormDTO;
import com.tys.survey.dto.LoginMemberDTO;
import com.tys.survey.dto.PasswordDTO;
import com.tys.survey.service.MyPageService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Map;

@Controller
@RequestMapping("/mypage")
public class MyPageController {

    private final MyPageService service;

    public MyPageController(MyPageService service) {
        this.service = service;
    }

    @GetMapping("/pwform")
    public String getPwForm(Model model){
        model.addAttribute("joinFormDTO",new JoinFormDTO());
        return "mypage/mypagePwForm";
    }

    @GetMapping("/leaveform")
    public String getLeaveForm(){
        return "mypage/mypageLeaveForm";
    }

    @PostMapping("/update/password")
    public String updatePassword(@Validated @ModelAttribute PasswordDTO passwordDTO, BindingResult bindingResult,
                                 @SessionAttribute(value = "loginMember",required = false) LoginMemberDTO loginMember,
                                 RedirectAttributes redirect){
        if (bindingResult.hasErrors()){
            return "mypage/mypagePwForm";
        }
        service.updatePassword(loginMember,passwordDTO);
        redirect.addFlashAttribute("status",true);
        return "redirect:/mypage/pwform";
    }

    @ResponseBody
    @PostMapping("/leave")
    public Map<String,Object> leaveMember(@SessionAttribute(value = "loginMember",required = false) LoginMemberDTO loginMember,
                                          @RequestParam String leave){
        return service.deleteMember(loginMember,leave);
    }

}
