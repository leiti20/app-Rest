package fr.univrouen.sepa26.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class IndexController {

    @GetMapping("/")
    public String index(Model model) {
        model.addAttribute("version", "1.0.0");
        model.addAttribute("developer", "Leiticia Mouhoubi");
        model.addAttribute("year", "2026");
        return "index";
    }
}