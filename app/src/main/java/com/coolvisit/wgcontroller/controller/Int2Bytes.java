package com.coolvisit.wgcontroller.controller;

public class Int2Bytes {

    public static void main(String[] args) {
        try {
            StringBuilder sb = new StringBuilder();
            byte []out = long2bytes(0);
            for (byte b : out) {
                sb.append(String.format("%02X ", b));
            }
            System.out.println(sb.toString());

            sb = new StringBuilder();
            byte []reverseOut = reverse(out);
            for (byte b : reverseOut) {
                sb.append(String.format("%02X ", b));
            }
            System.out.println(sb.toString());
            System.out.println(String.valueOf(BinaryToHexString(reverseOut)));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    static byte[] long2bytes(long n) {
        byte[] ab = new byte[4];
        ab[0] = (byte) (0xff & n);
        ab[1] = (byte) ((0xff00 & n) >> 8);
        ab[2] = (byte) ((0xff0000 & n) >> 16);
        ab[3] = (byte) ((0xff000000 & n) >> 24);
        return ab;
    }

    static byte[] reverse(byte[] ab) {
        byte[] newAb = new byte[ab.length];

        for (int i = 0; i < ab.length; i++) {
            newAb[i] = ab[ab.length - 1 - i];
        }
        return newAb;
    }

    public static long BinaryToHexString(byte[] bytes) {
        String hexStr = "0123456789ABCDEF";
        String result = "";
        String hex = "";
        for (byte b : bytes) {
            hex = String.valueOf(hexStr.charAt((b & 0xF0) >> 4));
            hex += String.valueOf(hexStr.charAt(b & 0x0F));
            result += hex;
        }
        result = result.replaceFirst("0", "");
        Long i= Long.parseLong(result,16);
        return i;
    }
}