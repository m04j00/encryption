package kr.hs.mirim.encryption.controller;

import kr.hs.mirim.encryption.dto.EncryptionData;
import kr.hs.mirim.encryption.dto.InputData;
import kr.hs.mirim.encryption.dto.MaterialData;
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
    public String encryption(InputData dataDto, Model model) {
        MaterialData materialData =  service.stringToEncryption(dataDto);
        char[][] board = service.createBoard(materialData);
        EncryptionData encryptionData = service.plainToEncryption(materialData.getPlain(), board);
        String encryptionToPlain = service.encryptionToPlain(encryptionData.getEncryption(), board, materialData.getZPoint(), encryptionData.getOverlapPoint());

        model.addAttribute("string", dataDto.getPlain());
        model.addAttribute("key", dataDto.getKey());
        return "encryption";
    }
}