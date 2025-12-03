package com.example.libraryapi.controller;

import com.example.libraryapi.security.CustomAuthentication;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class LoginViewController {
    @GetMapping("/login")
    public String paginaLogin(){
        return "login";
    }

    @GetMapping("/")
    @ResponseBody
    public String paginaHome(Authentication authentication){
        if (authentication instanceof CustomAuthentication customAuthentication){
            System.out.println(customAuthentication.getUsuario());
        }
        return "seja bem-vindo " + authentication.getName();
    }


    @GetMapping("/authorized")
    @ResponseBody
    public String getAuthorizationCode(@RequestParam("code") String code){
        return "Seu authorization code: " + code;
    }

    @GetMapping("/login/oauth2/code/google")
    @ResponseBody
    public String getAuthorizationCodeGoogle(@RequestParam("code") String code){
        return "Seu authorization code: " + code;
    }
}
