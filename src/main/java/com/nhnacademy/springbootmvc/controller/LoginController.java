package com.nhnacademy.springbootmvc.controller;

import com.nhnacademy.springbootmvc.domain.Auth;
import com.nhnacademy.springbootmvc.service.UserService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

@Slf4j
@Controller
@RequestMapping("/cs")
public class LoginController {
    private final UserService userService;
    public LoginController(UserService userService) {
        this.userService = userService;
    }


    @GetMapping("/login")
    public String login(@CookieValue(value = "SESSION", required = false) String session,
                        Model model) {
        if (StringUtils.hasText(session)) {
            model.addAttribute("id", session);
            return "redirect:/cs";
        } else {
            return "loginForm";
        }
    }

    @PostMapping("/login")
    public String doLogin(@RequestParam("id") String id,
                          @RequestParam("pwd") String pwd,
                          HttpServletResponse response,
                          ModelMap modelMap) {
        if (userService.match(id, pwd)) {
            Cookie cookie = new Cookie("SESSION", id);
            System.out.println(cookie);
            response.addCookie(cookie);

            modelMap.put("id", id);
            return "redirect:/cs";
        } else {
            return "redirect:/cs/login";
        }
    }

    @GetMapping("/logout")
    public String logout(HttpServletResponse response) {
        Cookie cookie = new Cookie("SESSION", null);
        cookie.setMaxAge(0);
        response.addCookie(cookie);
        return "redirect:/cs/login";
    }

}
