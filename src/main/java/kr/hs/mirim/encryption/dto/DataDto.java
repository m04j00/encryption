package kr.hs.mirim.encryption.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DataDto {
    private String plain;
    private String key;
    private String zPoint;
    private String encryption;
    private String overlap;
}
