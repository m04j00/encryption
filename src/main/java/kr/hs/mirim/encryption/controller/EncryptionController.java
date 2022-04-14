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
        DataDto materialDto =  service.stringToEncryption(dataDto);
        char[][] board = service.createBoard(materialDto);
        DataDto resultDto = service.plainToEncryption(materialDto, board);
        String encryptionToPlain = service.encryptionToPlain(resultDto.getEncryption(), board, materialDto.getZPoint(), resultDto.getOverlap());

        model.addAttribute("string", dataDto.getPlain());
        model.addAttribute("key", dataDto.getKey());
        return "encryption";
    }
}