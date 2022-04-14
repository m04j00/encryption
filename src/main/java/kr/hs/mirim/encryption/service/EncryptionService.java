package kr.hs.mirim.encryption.service;

import kr.hs.mirim.encryption.dto.DataDto;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class EncryptionService {
    public static char[][] board = new char[5][5];

    // 입력받은 문자열을 정리하여 DTO로 변환하는 메소드
    public DataDto stringToEncryption(DataDto materialDto) {
        DataDto dataDto = new DataDto();

        // 문자열 공백 제거 및 대문자 변환
        dataDto.setPlain(cleanString(materialDto.getPlain()).replaceAll("Z", "Q")); // 문자열의 Z -> Q 변환
        dataDto.setKey(cleanString(materialDto.getKey()));

        return dataDto;
    }

    // 암호판 생성하는 메소드
    public char[][] createBoard(DataDto dataDto) {
        String key = dataDto.getKey() + "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        StringBuilder boardKey = new StringBuilder();

        // key의 중복을 제거하여 boardKey에 저장
        for (int i = 0; i < key.length(); i++) {
            if (!boardKey.toString().contains(String.valueOf(key.charAt(i)))) {
                boardKey.append(key.charAt(i));
            }
        }

        // 암호판에 키 넣기
        for (int i = 0, k = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                board[i][j] = boardKey.charAt(k++);
            }
        }
        return board;
    }

    // 암호화 메소드
    public DataDto plainToEncryption(DataDto dataDto) {
        List<Character> plainList = new ArrayList<>();
        List<Character> encryptionList = new ArrayList<>();
        String plain = dataDto.getPlain();
        DataDto resultDto = new DataDto();

        //연속되는 글자가 중복시 X 추가
        if (plain.length() % 2 != 0) plain += 'X'; // plain이 2의 배수가 아니면 두 글자씩 자를 때 마지막에 한 개가 남아서 X로 채움
        System.out.println(plain);
        System.out.println(Arrays.deepToString(board));
        for (int i = 0; i < plain.length(); i += 2) {
            if (String.valueOf(plain.charAt(i)).equals((String.valueOf(plain.charAt(i + 1))))) {  //연속되는 글자가 같으면
                plainList.add(plain.charAt(i));
                plainList.add('X');
            } else {
                plainList.add(plain.charAt(i));
                plainList.add(plain.charAt(i + 1));
            }
        }

        StringBuilder cutTwoPlain = new StringBuilder();
        for (int i = 0; i < plainList.size(); i += 2) {
            cutTwoPlain.append(plainList.get(i)).append(plainList.get(i + 1)).append(" ");
        }
        resultDto.setPlain(cutTwoPlain.toString());
        int[][] point;
        StringBuilder cutTwoEncryption = new StringBuilder();
        for (int i = 0; i < plainList.size(); i += 2) {

            point = rowColSearch(plainList.get(i), plainList.get(i + 1));

            if (point[0][1] == point[1][1]) {  //같은 행에 있을 때
                encryptionList.add(board[isFirst(point[0][0])][point[0][1]]);
                encryptionList.add(board[isFirst(point[1][0])][point[1][1]]);
            } else if (point[0][0] == point[1][0]) {  //같은 열에 있을 때
                encryptionList.add(board[point[0][0]][isFirst(point[0][1])]);
                encryptionList.add(board[point[1][0]][isFirst(point[1][1])]);
            } else {  //다른 행, 다른 열에 있을 때
                encryptionList.add(board[point[1][0]][point[0][1]]);
                encryptionList.add(board[point[0][0]][point[1][1]]);
            }

            //암호화된 문자열 출력
            cutTwoEncryption.append(encryptionList.get(i)).append(encryptionList.get(i + 1)).append(" ");
        }
        resultDto.setKey(cutTwoEncryption.toString());
        return resultDto;
    }

    // 복호화 메소드
    public String encryptionToPlain(List<Character> encryptionList) {
        List<Character> plainList = new ArrayList<>();

        int[][] point;
        StringBuilder cutTwoPlain = new StringBuilder();
        for (int i = 0; i < encryptionList.size(); i += 2) {
            point = rowColSearch(encryptionList.get(i), encryptionList.get(i + 1));

            if (point[0][1] == point[1][1]) {  //같은 행에 있을 때
                plainList.add(board[isFirst(point[0][0])][point[0][1]]);
                plainList.add(board[isFirst(point[1][0])][point[1][1]]);
            } else if (point[0][0] == point[1][0]) {  //같은 열에 있을 때
                plainList.add(board[point[0][0]][isFirst(point[0][1])]);
                plainList.add(board[point[1][0]][isFirst(point[1][1])]);
            } else {  //다른 행, 다른 열에 있을 때
                plainList.add(board[point[1][0]][point[0][1]]);
                plainList.add(board[point[0][0]][point[1][1]]);
            }
            cutTwoPlain.append(plainList.get(i)).append(plainList.get(i + 1)).append(" ");
        }
        return cutTwoPlain.toString();
    }

    // 문자열에 포함된 공백을 제거하고 대문자로 변환하는 메소드
    public String cleanString(String string) {
        return string.toUpperCase().replaceAll(" ", "");
    }

    public int[][] rowColSearch(char a, char b) {
        int[][] point = new int[2][2];
        for (int j = 0; j < 5; j++) {
            for (int k = 0; k < 5; k++) {
                if (board[j][k] == a) {  //첫번째 글자 검색
                    point[0][0] = j;
                    point[0][1] = k;
                }
                if (board[j][k] == b) {  //두번째 글자 검색
                    point[1][0] = j;
                    point[1][1] = k;
                }
            }
        }
        return point;
    }

    // 암호화 - 행이나 열의 마지막 값일 경우
    public int isLast(int k) {
        if (k >= 4) return 0;
        else return k + 1;
    }

    // 복호화 - 행이나 열의 첫번째 값일 경우
    public int isFirst(int k) {
        if (k <= 0) return 4;
        else return k - 1;
    }
}
