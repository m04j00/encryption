package kr.hs.mirim.encryption.service;

import kr.hs.mirim.encryption.dto.EncryptionData;
import kr.hs.mirim.encryption.dto.InputData;
import kr.hs.mirim.encryption.dto.MaterialData;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class EncryptionService {

    // 입력받은 문자열을 정리하여 DTO로 변환하는 메소드
    public MaterialData stringToEncryption(InputData inputData) {
        String spacePoint = spaceCheck(inputData.getPlain()); // 공백 위치 체크
        String plain = cleanString(inputData.getPlain()); // 공백제거, 대문자 변환
        String zPoint = zCheck(plain); // plain의 Z 위치 체크, 복호화할 때 사용
        plain = plain.replaceAll("Z", "Q"); // Z -> Q 변환
        String key = cleanString(inputData.getKey()); // 공백제거, 대문자 변환
        return new MaterialData(plain, key, zPoint, spacePoint);
    }

    // 암호판 생성하는 메소드
    public char[][] createBoard(MaterialData materialData) {
        char[][] board = new char[5][5];
        String key = materialData.getKey() + "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
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
    public EncryptionData plainToEncryption(String plain, char[][] board) {
        List<Character> plainList = new ArrayList<>(); // 중복 처리한 palin을 저장할 list
        List<Character> encryptionList = new ArrayList<>(); // 암호문을 저장할 list
        StringBuilder overlapPoint = new StringBuilder(); // 중복 위치 체크하기 위한 변수 선언

        //연속되는 글자가 중복시 X 추가
        if (plain.length() % 2 != 0) plain += 'X'; // plain이 2의 배수가 아니면 두 글자씩 자를 때 마지막에 한 개가 남아서 X로 채움
        for (int i = 0; i < plain.length(); i += 2) {
            if (String.valueOf(plain.charAt(i)).equals((String.valueOf(plain.charAt(i + 1))))) {  //연속되는 글자가 같으면
                plainList.add(plain.charAt(i));
                plainList.add('X');
                overlapPoint.append("01"); // 중복 위치는 1, 중복 위치가 아닐 경우 0
            } else {
                plainList.add(plain.charAt(i));
                plainList.add(plain.charAt(i + 1));
                overlapPoint.append("00");
            }
        }

        StringBuilder cutTwoPlain = new StringBuilder(); // 두 글자씩 자른 plain 저장할 변수 선언
        for (int i = 0; i < plainList.size(); i += 2) {
            cutTwoPlain.append(plainList.get(i)).append(plainList.get(i + 1)).append(" ");
        }

        StringBuilder cutTwoEncryption = new StringBuilder(); // 두 글자씩 자른 암호문 저장할 변수 선언
        for (int i = 0; i < plainList.size(); i += 2) {
            // 각 좌표를 저장할 2차원 배열, 5*5 암호문에서의 좌표 저장
            int[][] point = rowColSearch(plainList.get(i), plainList.get(i + 1), board);

            if (point[0][1] == point[1][1]) {  //같은 행에 있을 때
                encryptionList.add(board[isLast(point[0][0])][point[0][1]]);
                encryptionList.add(board[isLast(point[1][0])][point[1][1]]);
            } else if (point[0][0] == point[1][0]) {  //같은 열에 있을 때
                encryptionList.add(board[point[0][0]][isLast(point[0][1])]);
                encryptionList.add(board[point[1][0]][isLast(point[1][1])]);
            } else {  //다른 행, 다른 열에 있을 때
                encryptionList.add(board[point[1][0]][point[0][1]]);
                encryptionList.add(board[point[0][0]][point[1][1]]);
            }

            cutTwoEncryption.append(encryptionList.get(i)).append(encryptionList.get(i + 1)).append(" ");
        }
        return new EncryptionData(cutTwoPlain.toString(), cutTwoEncryption.toString(), overlapPoint.toString());
    }

    // 복호화 메소드
    public String encryptionToPlain(String encryption, char[][] board, String _zPoint, String _overlapPoint, String _spacePoint) {
        List<Character> plainList = new ArrayList<>(); // 복호문 저장할 리스트 선언
        encryption = encryption.replaceAll(" ", ""); // 파라미터로 받은 암호문은 두 글자씩 띄어져있어서 공백 제거
        StringBuilder resultPlain = new StringBuilder(); // 복호문 저장할 변수 선언

        for (int i = 0; i < encryption.length(); i += 2) {
            int[][] point = rowColSearch(encryption.charAt(i), encryption.charAt(i + 1), board);

            if (point[0][1] == point[1][1]) {  //행이 같은 경우 각각 바로 아래열 대입
                plainList.add(board[isFirst(point[0][0])][point[0][1]]);
                plainList.add(board[isFirst(point[1][0])][point[1][1]]);
            } else if (point[0][0] == point[1][0]) {  //열이 같은 경우 각각 바로 옆 열 대입
                plainList.add(board[point[0][0]][isFirst(point[0][1])]);
                plainList.add(board[point[1][0]][isFirst(point[1][1])]);
            } else {  //행, 열 다른 경우 각자 대각선에 있는 곳
                plainList.add(board[point[1][0]][point[0][1]]);
                plainList.add(board[point[0][0]][point[1][1]]);
            }
            resultPlain.append(plainList.get(i)).append(plainList.get(i + 1));
        }

        String[] overlapPoint = _overlapPoint.split(""); // 중복 위치를 문자열 배열로 선언
        String[] zPoint = _zPoint.split(""); // Z 위치를 문자열 배열로 선언

        // Z -> Q, 중복 값 되돌리는 반복문
        for (int i = 0; i < zPoint.length; i++) {
            if (overlapPoint[i].equals("1") && resultPlain.charAt(i) == 'X') {
                resultPlain = new StringBuilder(resultPlain.substring(0, i) + resultPlain.charAt(i - 1) + resultPlain.substring(i + 1));
            }
            if (zPoint[i].equals("1") && resultPlain.charAt(i) == 'Q') {
                resultPlain = new StringBuilder(resultPlain.substring(0, i) + 'Z' + resultPlain.substring(i + 1));
            }

        }

        String[] spacePoint = _spacePoint.split(""); // 공백 위치를 문자열 배열로 선언

        //원래의 공백 자리에 띄어쓰기를 하는 반복문
        for (int i = 0; i < spacePoint.length; i++) {
            if (spacePoint[i].equals("1")) {
                resultPlain = new StringBuilder(resultPlain.substring(0, i) + ' ' + resultPlain.substring(i));
            }
        }
        // 마지막 값이 X이면 X를 자름
        if (resultPlain.charAt(resultPlain.length() - 1) == 'X') {
            resultPlain = new StringBuilder(resultPlain.substring(0, resultPlain.length() - 1));
        }
        return resultPlain.toString();
    }

    // 문자열에 포함된 공백을 제거하고 대문자로 변환하는 메소드
    public String cleanString(String string) {
        return string.toUpperCase().replaceAll(" ", "");
    }

    public int[][] rowColSearch(char a, char b, char[][] board) {
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

    // 공백 위치 체크하는 메소드
    public String spaceCheck(String plain) {
        StringBuilder spacePoint = new StringBuilder();
        String[] plainArray = plain.split("");
        for (String s : plainArray) {
            if (s.equals(" ")) {
                spacePoint.append("1");
            } else {
                spacePoint.append("0");
            }
        }
        return spacePoint.toString();
    }

    // Z 위치 체크하는 메소드
    public String zCheck(String plain) {
        StringBuilder zPoint = new StringBuilder();
        String[] plainArray = plain.split("");
        for (String s : plainArray) {
            if (s.equals("Z")) {
                zPoint.append("1");
            } else {
                zPoint.append("0");
            }
        }
        return zPoint.toString();
    }

}