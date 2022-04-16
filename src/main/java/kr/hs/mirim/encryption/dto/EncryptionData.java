package kr.hs.mirim.encryption.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;

@Getter
@AllArgsConstructor
public class EncryptionData {
    @NonNull
    private String plain;
    @NonNull
    private String encryption;
    @NonNull
    private String overlapPoint;
}
