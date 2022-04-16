package kr.hs.mirim.encryption.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;

@Getter
@AllArgsConstructor
public class MaterialData {
    @NonNull
    private String plain;
    @NonNull
    private String key;
    @NonNull
    private String zPoint;
}
