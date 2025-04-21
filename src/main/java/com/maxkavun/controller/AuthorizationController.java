package com.maxkavun.controller;

import com.maxkavun.dto.UserLoginDto;
import com.maxkavun.service.AuthorizationService;
import com.maxkavun.util.CookieUtil;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.time.Duration;
import java.time.LocalDateTime;


@Slf4j
@Controller
public class AuthorizationController {

    private final AuthorizationService authorizationService;

    public AuthorizationController(AuthorizationService authorizationService) {
        this.authorizationService = authorizationService;
    }

    @GetMapping("/")
    public String loginPage(Model model) {
        model.addAttribute("userLoginDto", new UserLoginDto());
        return "login";
    }


    @PostMapping("/processingLogin")
    public String processLogin(@Valid @ModelAttribute("userLoginDto") UserLoginDto userLoginDto, BindingResult bindingResult,  HttpServletResponse response) {
        if (bindingResult.hasErrors()) {
            return "login";
        }

        var sessionID = authorizationService.saveSessionIfUserAuthorized(userLoginDto.getUsername(), userLoginDto.getPassword());
        if (sessionID.isPresent()) {

            var sessionDto = authorizationService.getSessionDto(sessionID.get());
            var sessionExpirationTime = sessionDto.expiresAt();
            long maxTime = Duration.between(LocalDateTime.now(), sessionExpirationTime).getSeconds();

            Cookie cookie = new Cookie("CUSTOM_SESSION_ID", sessionID.get().toString());
            cookie.setHttpOnly(true);
            cookie.setPath("/");
            cookie.setMaxAge((int) maxTime);
            response.addCookie(cookie);

            return "redirect:/home";
        }
        return "redirect:/";
    }


    @PostMapping("/logOut")
    public String logOut(HttpServletRequest request, HttpServletResponse response) {
        var sessionId = CookieUtil.getSessionIdFromCookie(request);

        authorizationService.changeExpireTimeInSession(sessionId , LocalDateTime.now());

        Cookie cookie = new Cookie(CookieUtil.getDEFAULT_SESSION_COOKIE_NAME(), sessionId);
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        cookie.setMaxAge(0);
        response.addCookie(cookie);

        return "redirect:/home";
    }


}
