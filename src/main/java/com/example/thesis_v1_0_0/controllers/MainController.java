package com.example.thesis_v1_0_0.controllers;

import com.example.thesis_v1_0_0.classes.Encryption;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@Controller
public class MainController {

    @GetMapping("/")
    public String index(Model model) {
        Encryption e = new Encryption();

        return "index";
    }

}
