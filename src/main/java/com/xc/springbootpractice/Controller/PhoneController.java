package com.xc.springbootpractice.Controller;


import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.ArrayList;

@Controller
public class PhoneController {

    @GetMapping("/phone")
    public String phone(Model model) {
        model.addAttribute("phone", "phone");

        return "phone";
    }
}
