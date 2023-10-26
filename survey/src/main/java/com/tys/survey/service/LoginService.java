package com.tys.survey.service;

import com.tys.survey.dto.LoginMemberDTO;
import com.tys.survey.repository.LoginRepository;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;

@Service
public class LoginService {

    private final JoinService joinService;

    private final LoginRepository repository;

    public LoginService(JoinService joinService, LoginRepository repository) {
        this.joinService = joinService;
        this.repository = repository;
    }

    public boolean login(LoginMemberDTO loginMemberDTO, HttpSession httpSession) {
        LoginMemberDTO login = repository.login(loginMemberDTO.getMemberId());
        if (login != null){
            boolean result = joinService.checkPassword(loginMemberDTO.getMemberPassword(), login.getMemberPassword());
            if (result){
                httpSession.setAttribute("loginMember",LoginMemberDTO.builder()
                                                                        .memberNo(login.getMemberNo())
                                                                        .memberNickname(login.getMemberNickname())
                                                                        .memberRole(login.getMemberRole())
                                                                        .build());
                return true;
            }
        }
        return false;
    }

}
