package com.coolvisit.wgcontroller.controller;

public class Int2Bytes {

    public static void main(String[] args) {
        try {
            StringBuilder sb = new StringBuilder();
            byte []out = int2bytes(123456);
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
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    static byte[] int2bytes(int n) {
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
}