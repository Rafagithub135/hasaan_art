package com.example.hasaan_art.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AboutUsController {
    @GetMapping("/about-us")
    public String aboutUsPage() {
        return "about/index";
    }
}
