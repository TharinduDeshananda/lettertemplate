package com.example.letter.lettertemplate;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalConfig {

    @ExceptionHandler({Exception.class})
    public String controllerErrorPage(Model model, Exception exception){

        model.addAttribute("errorContent",exception.getMessage());
        return "errorPage";
    }

}
