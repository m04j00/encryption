package kr.hs.mirim.encryption.controller;

import kr.hs.mirim.encryption.dto.MaterialDto;
import kr.hs.mirim.encryption.service.EncryptionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequiredArgsConstructor
public class EncryptionController {
    private final EncryptionService service;
    @GetMapping("/")
    public String home() {
        return "home";
    }

    @PostMapping("/encryption")
    public String encryption(MaterialDto dataDto, Model model) {
        System.out.println(dataDto.getKey());
        int[] a = {1, 2, 3};
        model.addAttribute("string", dataDto.getString());
        model.addAttribute("key", dataDto.getKey());
        model.addAttribute("arr", a);
        service.stringToEncryption(dataDto);
        return "encryption";
    }
}

// 1. 입력받은 문자열, 암호키를 공백제거하고 암호화 만들어서 세 개를 dto에 저장
// 2. 문자열, 암호키로 암호화
// 3. 암호판 출력
// 4.