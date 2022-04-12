package kr.hs.mirim.encryption.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class EncryptionController {
    @GetMapping("/main")
    public String home() {
        return "home";
    }

    @RequestMapping("/encryption")
    public String encryption(@RequestParam("string") String string, @RequestParam("key") String key, Model model) {
        System.out.println(string);
        model.addAttribute("string", string);
        model.addAttribute("key", key);
        return "encryption";
    }
}
