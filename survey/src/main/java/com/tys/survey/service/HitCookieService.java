package com.tys.survey.service;

import org.springframework.stereotype.Service;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Service
public class HitCookieService {

    public boolean updateHit(HttpServletRequest request, HttpServletResponse response, String cookieName) {
        Cookie[] cookies = request.getCookies();
        boolean status = false;

        if (cookies != null) {
            for (Cookie c : cookies) {
                if (c.getName().equals(cookieName)) {
                    status = true;
                    break;
                }
            }
        }

        if (!status) {
            Cookie cookie = new Cookie(cookieName, "true");
            cookie.setMaxAge(24 * 60 * 60);
            response.addCookie(cookie);
        }

        return status;
    }
}
