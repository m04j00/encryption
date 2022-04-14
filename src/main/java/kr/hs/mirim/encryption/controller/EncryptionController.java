package kr.hs.mirim.encryption.controller;

import kr.hs.mirim.encryption.dto.DataDto;
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
    public String encryption(DataDto dataDto, Model model) {
        // 입력받은 문자열과 암호키
        model.addAttribute("string", dataDto.getPlain());
        model.addAttribute("key", dataDto.getKey());

        System.out.println(dataDto.getPlain());
        System.out.println(dataDto.getKey());
        DataDto resultDto = service.stringToEncryption(dataDto);
        char[][] board = service.createBoard(resultDto);
        resultDto = service.plainToEncryption(resultDto);
        System.out.println(resultDto.getKey());
        System.out.println(resultDto.getPlain());
        return "encryption";
    }
}