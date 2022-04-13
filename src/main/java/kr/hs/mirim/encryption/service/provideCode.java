package kr.hs.mirim.encryption.service;

import java.util.ArrayList;
import java.util.Scanner;

public class provideCode {

    public static char[][] alphabetBoard = new char[5][5];
    public static boolean oddFlag = false; //글자수 출력
    public static String zCheck = "";

    public static void main(String[] args) {

        String decryption;
        String encryption;

        Scanner sc = new Scanner(System.in);    //입력을 위한 Scanner 정의
        System.out.print("암호화에 쓰일 키를 입력하세요 : ");
        String key = sc.nextLine().toUpperCase();                //key 입력
        System.out.print("암호화할 문자열을 입력하세요 : ");
        String str = sc.nextLine().toUpperCase();                //문자열 입력
        StringBuilder blankCheck = new StringBuilder();

        setBoard(key);                            //암호화에 쓰일 암호판 세팅

        for (int i = 0; i < str.length(); i++) {
            if (str.charAt(i) == ' ') //공백제거
            {
                str = str.substring(0, i) + str.substring(i + 1);
                blankCheck.append(10);
            } else {
                blankCheck.append(0);
            }
            if (str.charAt(i) == 'z') //z를 q로 바꿔줘서 처리함.
            {
                str = str.substring(0, i) + 'q' + str.substring(i + 1);
                zCheck += 1;
            } else {
                zCheck += 0;
            }
        }

        encryption = strEncryption(key, str);


        //출력부분
        System.out.println("암호화된 문자열 : " + encryption);


        for (int i = 0; i < encryption.length(); i++) {
            if (encryption.charAt(i) == ' ') //공백제거
                encryption = encryption.substring(0, i) + encryption.substring(i + 1);
        }

        decryption = strDecryption(key, encryption, zCheck);

        for (int i = 0; i < decryption.length(); i++) {
            if (blankCheck.charAt(i) == '1') {
                decryption = decryption.substring(0, i) + " " + decryption.substring(i);
            }
        }

        System.out.println("복호화된 문자열 : " + decryption);
    }

    private static String strDecryption(String key, String str, String zCheck) {
        ArrayList<char[]> playFair = new ArrayList<>(); //바꾸기 전 쌍자암호를 저장할 곳
        ArrayList<char[]> decPlayFair = new ArrayList<>(); //바꾼 후의 쌍자암호 저장할 곳
        int x1 = 0, x2 = 0, y1 = 0, y2 = 0; //쌍자 암호 두 글자의 각각의 행,열 값
        StringBuilder decStr = new StringBuilder();

        for (int i = 0; i < str.length(); i += 2) {
            char[] tmpArr = new char[2];
            tmpArr[0] = str.charAt(i);
            tmpArr[1] = str.charAt(i + 1);
            playFair.add(tmpArr);
        }


        for (char[] chars : playFair) {

            char[] tmpArr = new char[2];
            for (int j = 0; j < alphabetBoard.length; j++) {
                for (int k = 0; k < alphabetBoard[j].length; k++) {
                    if (alphabetBoard[j][k] == chars[0]) {
                        x1 = j;
                        y1 = k;
                    }
                    if (alphabetBoard[j][k] == chars[1]) {
                        x2 = j;
                        y2 = k;
                    }
                }
            }

            if (x1 == x2) //행이 같은 경우 각각 바로 아래열 대입
            {
                tmpArr[0] = alphabetBoard[x1][(y1 + 4) % 5];
                tmpArr[1] = alphabetBoard[x2][(y2 + 4) % 5];
            } else if (y1 == y2) //열이 같은 경우 각각 바로 옆 열 대입
            {
                tmpArr[0] = alphabetBoard[(x1 + 4) % 5][y1];
                tmpArr[1] = alphabetBoard[(x2 + 4) % 5][y2];
            } else //행, 열 다른경우 각자 대각선에 있는 곳.
            {
                tmpArr[0] = alphabetBoard[x2][y1];
                tmpArr[1] = alphabetBoard[x1][y2];
            }

            decPlayFair.add(tmpArr);

        }

        for (int i = 0; i < decPlayFair.size(); i++) //중복 문자열 돌려놓음
        {
            if (i != decPlayFair.size() - 1 && decPlayFair.get(i)[1] == 'x'
                    && decPlayFair.get(i)[0] == decPlayFair.get(i + 1)[0]) {
                decStr.append(decPlayFair.get(i)[0]);
            } else {
                decStr.append(decPlayFair.get(i)[0]).append(decPlayFair.get(i)[1]);
            }
        }


        for (int i = 0; i < zCheck.length(); i++)//z위치 찾아서 q로 돌려놓음
        {
            if (zCheck.charAt(i) == '1')
                decStr = new StringBuilder(decStr.substring(0, i) + 'z' + decStr.substring(i + 1));

        }


        if (oddFlag) decStr = new StringBuilder(decStr.substring(0, decStr.length() - 1));

        return decStr.toString();
    }

    private static String strEncryption(String key, String str) {
        ArrayList<char[]> playFair = new ArrayList<>();
        ArrayList<char[]> encPlayFair = new ArrayList<>();
        int x1 = 0, x2 = 0, y1 = 0, y2 = 0;
        StringBuilder encStr = new StringBuilder();


        for (int i = 0; i < str.length(); i += 2) // arraylist 세팅
        {
            char[] tmpArr = new char[2];
            tmpArr[0] = str.charAt(i);
            try {
                if (str.charAt(i) == str.charAt(i + 1)) //글이 반복되면 x추가
                {
                    tmpArr[1] = 'x';
                    i--;
                } else {
                    tmpArr[1] = str.charAt(i + 1);
                }
            } catch (StringIndexOutOfBoundsException e) {
                tmpArr[1] = 'x';
                oddFlag = true;
            }
            playFair.add(tmpArr);
        }

        for (char[] value : playFair) {
            System.out.print(value[0] + "" + value[1] + " ");
        }
        System.out.println();

        for (char[] chars : playFair) {

            char[] tmpArr = new char[2];
            for (int j = 0; j < alphabetBoard.length; j++) //쌍자암호의 각각 위치체크
            {
                for (int k = 0; k < alphabetBoard[j].length; k++) {
                    if (alphabetBoard[j][k] == chars[0]) {
                        x1 = j;
                        y1 = k;
                    }
                    if (alphabetBoard[j][k] == chars[1]) {
                        x2 = j;
                        y2 = k;
                    }
                }
            }

            if (x1 == x2) //행이 같은경우
            {
                tmpArr[0] = alphabetBoard[x1][(y1 + 1) % 5];
                tmpArr[1] = alphabetBoard[x2][(y2 + 1) % 5];
            } else if (y1 == y2) //열이 같은 경우
            {
                tmpArr[0] = alphabetBoard[(x1 + 1) % 5][y1];
                tmpArr[1] = alphabetBoard[(x2 + 1) % 5][y2];
            } else //행, 열 모두 다른경우
            {
                tmpArr[0] = alphabetBoard[x2][y1];
                tmpArr[1] = alphabetBoard[x1][y2];
            }

            encPlayFair.add(tmpArr);

        }

        for (char[] chars : encPlayFair) {
            encStr.append(chars[0]).append(chars[1]).append(" ");
        }

        return encStr.toString();
    }

    private static void setBoard(String key) {
        StringBuilder keyForSet = new StringBuilder();                    // 중복된 문자가 제거된 문자열을 저장할 문자열.
        boolean duplicationFlag = false;        // 문자 중복을 체크하기 위한 flag 변수.
        int keyLengthCount = 0;                    // alphabetBoard에 keyForSet을 넣기 위한 count변수.

        key += "ABCDEFGHIJKLMNOPQRSTUVWXYZ";    // 키에 모든 알파벳을 추가.

        // 중복처리
        for (int i = 0; i < key.length(); i++) {
            for (int j = 0; j < keyForSet.length(); j++) {
                if (key.charAt(i) == keyForSet.charAt(j)) {
                    duplicationFlag = true;
                    break;
                }
            }
            if (!(duplicationFlag)) keyForSet.append(key.charAt(i));
            duplicationFlag = false;
        }
        //배열에 대입
        for (int i = 0; i < alphabetBoard.length; i++) {
            for (int j = 0; j < alphabetBoard[i].length; j++) {
                alphabetBoard[i][j] = keyForSet.charAt(keyLengthCount++);
            }
        }
        //배열에 대입
        for (char[] chars : alphabetBoard) {
            for (char aChar : chars) {
                System.out.print(aChar + "-");
            }
            System.out.println();
        }

    }

}
