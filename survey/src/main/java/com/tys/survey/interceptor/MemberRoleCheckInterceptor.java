package com.tys.survey.interceptor;

import com.tys.survey.dto.LoginMemberDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Slf4j
public class MemberRoleCheckInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String requestURI = request.getRequestURI();
        log.info("회원 권한 체크 인터셉터 실행 {}", requestURI);
        LoginMemberDTO loginMember = (LoginMemberDTO)  request.getSession(false).getAttribute("loginMember");
        if (!loginMember.getMemberRole().equals("A")){
            log.info("권한 없는 회원의 요청");
            response.sendRedirect("/");
            return false;
        }
        return true;
    }
}
