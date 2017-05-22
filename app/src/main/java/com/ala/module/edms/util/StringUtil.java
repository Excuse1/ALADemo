package com.ala.module.edms.util;

import java.util.regex.Pattern;

public class StringUtil {


    public static boolean isZeroNull(String str) {
        if (str == null)
            return true;

        if (str.length() == 0)
            return true;

        for (int i = 0; i < str.length(); i++) {
            if (str.charAt(i) != '0') {
                return false;
            }
        }

        return true;
    }


    public static boolean isEmpty(String str) {
        if (str == null || str.length() == 0)
            return true;
        else
            return false;
    }


    public static boolean isSfExpress(String express) {
        if (null == express || express.length() != 12)
            return false;

        String seq = express.substring(3, 11);
        String checkCode = express.substring(11);

        return String.valueOf(genCheckCode(seq)).equals(checkCode);

    }

    private static char genCheckCode(String js) {
        if (js == null || js.length() != 8)
            return ' ';

        char CB;
        int P0, P1, P2, P3, P4, P5, P6, P7;
        int A0, A1, A2, A3, A4, A5, A6, A7;
        int Q0, Q1, Q2, Q3, Q4, Q5, Q6, Q7;
        char s0, s1, s2, s3, s4, s5, s6, s7;

        s0 = js.charAt(7);
        s1 = js.charAt(6);
        s2 = js.charAt(5);
        s3 = js.charAt(4);
        s4 = js.charAt(3);
        s5 = js.charAt(2);
        s6 = js.charAt(1);
        s7 = js.charAt(0);

        A0 = s0 - '0';
        A1 = s1 - '0';
        A2 = s2 - '0';
        A3 = s3 - '0';
        A4 = s4 - '0';
        A5 = s5 - '0';
        A6 = s6 - '0';
        A7 = s7 - '0';

        P0 = A0 * 1;
        P1 = A1 * 3;
        P2 = A2 * 5;
        P3 = A3 * 7;
        P4 = A4 * 9;
        P5 = A5 * 11;
        P6 = A6 * 13;
        P7 = A7 * 15;

        Q0 = (P0 / 10) + (P0 - 10 * (P0 / 10));
        Q1 = (P1 / 10) + (P1 - 10 * (P1 / 10));
        Q2 = (P2 / 10) + (P2 - 10 * (P2 / 10));
        Q3 = (P3 / 10) + (P3 - 10 * (P3 / 10));
        Q4 = (P4 / 10) + (P4 - 10 * (P4 / 10));
        Q5 = (P5 / 10) + (P5 - 10 * (P5 / 10));
        Q6 = (P6 / 10) + (P6 - 10 * (P6 / 10));
        Q7 = (P7 / 10) + (P7 - 10 * (P7 / 10));

        int Q = Q0 + Q1 + Q2 + Q3 + Q4 + Q5 + Q6 + Q7;
        int m = ((Q / 10) + 1) * 10;
        m = (m - Q) - 10 * ((m - Q) / 10);

        CB = String.valueOf(m).charAt(0);

        return CB;
    }

    public static String exception2Str(Throwable ex) {
        StringBuffer exceptionStr = new StringBuffer();
        exceptionStr.append("Exception: " + ex.getMessage() + "\n");
        StackTraceElement[] elements = ex.getStackTrace();
        for (int i = 0; i < elements.length; i++) {
            exceptionStr.append(elements[i].toString() + "\n");
        }
        return exceptionStr.toString();
    }

    public static String octet2HexString(byte[] octet) {
        return octet2HexString(octet, true);
    }

    public static String octet2HexString(byte[] octet, boolean isLowerCase) {
        if (octet == null)
            return "";

        StringBuilder sb = new StringBuilder(octet.length * 2);
        for (byte b : octet) {
            char h = (char) ((b >> 4) & 0x0f);
            char l = (char) (b & 0x0f);
            if (h < 10)
                h = (char) (h + '0');
            else if (isLowerCase)
                h = (char) ((h - 10) + 'a');
            else
                h = (char) ((h - 10) + 'A');
            sb.append(h);

            if (l < 10)
                l = (char) (l + '0');
            else if (isLowerCase)
                l = (char) ((l - 10) + 'a');
            else
                l = (char) ((l - 10) + 'A');
            sb.append(l);
        }

        return sb.toString();

    }

    public static byte[] hexString2Octet(String src) {
        int m = 0, n = 0;
        int l = src.length() / 2;
        byte[] ret = new byte[l];
        for (int i = 0; i < l; i++) {
            m = i * 2 + 1;
            n = m + 1;
            ret[i] = Integer.decode("0x" + src.substring(i * 2, m) + src.substring(m, n)).byteValue();
        }
        return ret;
    }


    public static int getCharacterNum(final String content) {
        if (null == content || "".equals(content)) {
            return 0;
        } else {
            return (content.length() + getChineseNum(content));
        }
    }

    public static int getChineseNum(String s) {

        int num = 0;

        char[] myChar = s.toCharArray();

        for (int i = 0; i < myChar.length; i++) {

            if ((char) (byte) myChar[i] != myChar[i]) {
                num++;
            }
        }
        return num;
    }

    /**
     * 鏍￠獙娉ㄥ唽鐨勫瘑鐮�
     */
    public static boolean validatePassword(String password) {
        String rex = "^(?![a-zA-Z]+$)(?![0-9]+$)[0-9A-Za-z]{6,12}$";
        Pattern PASSWORD_REX_PATTERN = Pattern.compile(rex);
        return PASSWORD_REX_PATTERN.matcher(password).matches();
    }

    /**
     * 鏍￠獙楠岃瘉鐮�
     */
    public static boolean validateVercode(String vercode) {
        boolean result = false;
        if (!isEmpty(vercode) && vercode.length() == 6) {
            result = true;
        }
        return result;
    }

    /**
     * 瀛楃涓叉槸鍚︽槸鍙湁鏁板瓧鍜屽瓧姣嶇粍鎴�
     */
    public static boolean isMark(String s) {
        for (int i = 0; i < s.length(); i++) {
            int chr = s.charAt(i);
            int ix = (int) chr;
            if ((ix >= 48 && ix <= 57) || (ix >= 65 && ix <= 90) || (ix >= 97 && ix <= 122)) {
                continue;
            } else {
                return false;
            }
        }
        return true;
    }

    public static String[] companyNameList() {
        String[] companyName = { "椤轰赴蹇��", "鐢抽�氬揩閫�", "鍦嗛�氬揩閫�", "涓�氬揩閫�", "姹囬�氬揩杩�", "闊佃揪蹇��", "EMS", "鍏朵粬" };
        return companyName;
    }

    public static String[] companyCodeList() {
        String[] companyCode = { "SF", "STO", "YTO", "ZTO", "BEST", "YD", "EMS", "OTHER" };
        return companyCode;
    }

    /**
     * 鏍规嵁Code鏌ヨcompanyname
     */
    public static String code2company(String company_code) {
        String[] companyCode = companyCodeList();
        String[] companyName = companyNameList();

        for (int i = 0; i < companyCode.length; i++) {
            if (companyCode[i].equals(company_code)) {
                return companyName[i];
            }
        }

        return "鍏朵粬";
    }
}
