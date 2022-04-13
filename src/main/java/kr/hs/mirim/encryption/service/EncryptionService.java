package kr.hs.mirim.encryption.service;

import kr.hs.mirim.encryption.dto.MaterialDto;
import kr.hs.mirim.encryption.dto.ResultDto;
import org.springframework.stereotype.Service;

@Service
public class EncryptionService {
    public void stringToEncryption(MaterialDto materialDto){
        ResultDto resultDto = new ResultDto();
        String string = materialDto.getString().toUpperCase().replaceAll(" ", "");
        string = string.replaceAll("Z", "Q");
        String key = materialDto.getKey().toUpperCase().replaceAll(" ", "");
    }

}
