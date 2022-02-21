package com.allteran.reviewmanager.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.concurrent.ThreadLocalRandom;

@Controller
public class MainController {

    @GetMapping
    public String index(Model model, HttpServletResponse response, @CookieValue(name = "user_id") String anonUserId) {;
        Cookie cookie = new Cookie("user_id", String.valueOf(ThreadLocalRandom.current().nextLong()));
        if(anonUserId.isEmpty()) {
            response.addCookie(cookie);
        }
        return "index";
    }
}
