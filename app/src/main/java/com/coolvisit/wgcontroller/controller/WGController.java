package com.coolvisit.wgcontroller.controller;

import java.io.IOException;
import java.lang.reflect.Array;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
//import android.util.Log;
import com.coolvisit.wgcontroller.MainActivity;
/**
 * Created by qicool on 2017/11/9.
 */

public class WGController implements IAccessController {

    private static int tag = 1;
    private final static String TAG = "WGController";


    public WGController(){

    }

    private byte[] newByte(){
        byte[] byteCmd = new byte[] {(byte) 0x17, (byte) 0x00,
                (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00,
                (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00,
                (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00,
                (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00,
                (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00,
                (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00,
                (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00,
                (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00,
                (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00,
                (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00,
                (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00,
                (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00,
                (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00,
                (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00,
                (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00,
                (byte) 0x00, (byte) 0x00 };
        return byteCmd;
    }

    public int searchDevices(String ip,int port){
        byte[] byteCmd= newByte();
        int ret = -13;
        byteCmd[1] = (byte) 0x94;


        byte content[] = sendCmd(byteCmd,ip,port);

        if ((content != null) && (content.length == 64)) {
            ret = content[8];
        } else {
            ret = -13;
        }

        return ret;
    }

    @Override
    public int open(String id,int doorNO,String ip, int port) {
        byte[] byteCmd= newByte();
        byte[] idByte = long2bytes(Long.parseLong(id));
        int ret = -13;
        byteCmd[1] = (byte) 0x40;
        byteCmd[8] = (byte) doorNO;
        byteCmd[40] = (byte) (tag & 0xff);
        byteCmd[41] = (byte) ((tag >> 8) & 0xff);
        byteCmd[42] = (byte) ((tag >> 16) & 0xff);
        byteCmd[43] = (byte) ((tag >> 24) & 0xff);

        for(int i=0,j=4;i<4;i++,j++){
            byteCmd[j] = (byte)(idByte[i]);
        }

        byte content[] = sendCmd(byteCmd,ip,port);


        if ((content != null) && (content.length == 64)) {
            ret = content[8];
        } else {
            ret = -13;
        }
        tag++;

        return ret;
    }



    @Override
    public int modifyCard(String cardId,String startDate,String endDate,String id,String doorNO,String ip, int port) {

        if(cardId == null){
            return AccessController.ERROR_CARD_ID;
        }
        if (startDate == null || startDate.length() != 8
                ||endDate == null || endDate.length() != 8){
            return -2;
        }

        //cardid最大值4294967295  2147483647
        try {
            long Max_cardId= 4294967295l;
            if( Long.parseLong(cardId)>Max_cardId){
                return AccessController.ERROR_CARD_ID;
            }
        }catch (Exception e){
            return AccessController.ERROR_CARD_ID;
        }

        //id最大值4294967295  2147483647
        try {
            long Max_id = 4294967295l;
            if( Long.parseLong(id)>Max_id){
                return AccessController.ERROR_SN;
            }
        }catch (Exception e){
            return AccessController.ERROR_SN;
        }
        byte[] byteCmd= newByte();
        byteCmd[1] = (byte) 0x50;

        byte[] idByte = long2bytes(Long.parseLong(id));
        byte[] cardByte = long2bytes(Long.parseLong(cardId));
        int ret = -13;

        byteCmd[20] = (byte) 0x00;
        byteCmd[21] = (byte) 0x00;
        byteCmd[22] = (byte) 0x00;
        byteCmd[23] = (byte) 0x00;
        if(doorNO.contains("1")){
            byteCmd[20] = (byte) 0x01;
        }
        if (doorNO.contains("2")){
            byteCmd[21] = (byte) 0x01;
        }
        if (doorNO.contains("3")){
            byteCmd[22] = (byte) 0x01;
        }
        if (doorNO.contains("4")){
            byteCmd[23] = (byte) 0x01;
        }

        byteCmd[40] = (byte) (tag & 0xff);
        byteCmd[41] = (byte) ((tag >> 8) & 0xff);
        byteCmd[42] = (byte) ((tag >> 16) & 0xff);
        byteCmd[43] = (byte) ((tag >> 24) & 0xff);

        //设备号
        for(int i=0,j=4;i<4;i++,j++){
            byteCmd[j] = (byte)(idByte[i]);
        }

        //卡号
        for(int i=0,j=8;i<4;i++,j++){
            byteCmd[j] = (byte)(cardByte[i]);
        }

        byte[] startByte = hexStringToByte(startDate);
        for(int i=0,j=12;i<4;i++,j++){
            byteCmd[j] = (byte)(startByte[i]);
        }
        byte[] endByte = hexStringToByte(endDate);
        for(int i=0,j=16;i<4;i++,j++){
            byteCmd[j] = (byte)(endByte[i]);
        }

        byte content[] = sendCmd(byteCmd,ip,port);


        if ((content != null) && (content.length == 64)) {
            ret = content[8];
        } else {
            ret = -13;
        }
        tag++;

        return ret;
    }

    @Override
    public int deleteCard(String cardId, String id, String ip, int port) {

        if(cardId == null){
            return AccessController.ERROR_CARD_ID;
        }

        //cardid最大值4294967295  2147483647
        try {
            long Max_cardId= 4294967295l;
            if( Long.parseLong(cardId)>Max_cardId){
                return AccessController.ERROR_CARD_ID;
            }
        }catch (Exception e){
            return AccessController.ERROR_CARD_ID;
        }

        //id最大值4294967295  2147483647
        try {
            long Max_id = 4294967295l;
            if( Long.parseLong(id)>Max_id){
                return AccessController.ERROR_SN;
            }
        }catch (Exception e){
            return AccessController.ERROR_SN;
        }


        byte[] idByte = long2bytes(Long.parseLong(id));
        byte[] cardByte = long2bytes(Long.parseLong(cardId));
        int ret = -13;
        byte[] byteCmd= newByte();
        byteCmd[1] = (byte) 0x52;

        byteCmd[40] = (byte) (tag & 0xff);
        byteCmd[41] = (byte) ((tag >> 8) & 0xff);
        byteCmd[42] = (byte) ((tag >> 16) & 0xff);
        byteCmd[43] = (byte) ((tag >> 24) & 0xff);

        //设备号
        for(int i=0,j=4;i<4;i++,j++){
            byteCmd[j] = (byte)(idByte[i]);
        }

        //卡号
        for(int i=0,j=8;i<4;i++,j++){
            byteCmd[j] = (byte)(cardByte[i]);
        }

        byte content[] = sendCmd(byteCmd,ip,port);


        if ((content != null) && (content.length == 64)) {
            ret = content[8];
        } else {
            ret = -13;
        }
        tag++;

        return ret;
    }

    @Override
    public int checkCard(String cardId, String id, String doorNO,String ip, int port) {

        if(cardId == null){
            return AccessController.ERROR_CARD_ID;
        }

        //cardid最大值4294967295  2147483647
        try {
            long Max_cardId= 4294967295l;
            if( Long.parseLong(cardId)>Max_cardId){
                return AccessController.ERROR_CARD_ID;
            }
        }catch (Exception e){
            return AccessController.ERROR_CARD_ID;
        }

        //id最大值4294967295  2147483647
        try {
            long Max_id = 4294967295l;
            if( Long.parseLong(id)>Max_id){
                return AccessController.ERROR_SN;
            }
        }catch (Exception e){
            return AccessController.ERROR_SN;
        }

        byte[] idByte = long2bytes(Long.parseLong(id));
        byte[] cardByte = long2bytes(Long.parseLong(cardId));

        int ret = -13;
        byte[] byteCmd= newByte();
        byteCmd[1] = (byte) 0x5A;

        byteCmd[40] = (byte) (tag & 0xff);
        byteCmd[41] = (byte) ((tag >> 8) & 0xff);
        byteCmd[42] = (byte) ((tag >> 16) & 0xff);
        byteCmd[43] = (byte) ((tag >> 24) & 0xff);

        //设备号
        for(int i=0,j=4;i<4;i++,j++){
            byteCmd[j] = (byte)(idByte[i]);
        }

        //卡号
        for(int i=0,j=8;i<4;i++,j++){
            byteCmd[j] = (byte)(cardByte[i]);
        }

        byte content[] = sendCmd(byteCmd,ip,port);


        if ((content != null) && (content.length == 64)) {
            ret = content[8];
        } else {
            ret = -13;
        }
        tag++;


        try {
            if (byteCmd[8] == content[8]
                    && byteCmd[9] == content[9]
                    && byteCmd[10] == content[10]
                    && byteCmd[11] == content[11]) {

                if (doorNO.contains("1")) {
                    if (content[20] != (byte) 0x01) {
                        return -1;
                    }
                }
                if (doorNO.contains("2")) {
                    if (content[21] != (byte) 0x01) {
                        return -1;
                    }
                }
                if (doorNO.contains("3")) {
                    if (content[22] != (byte) 0x01) {
                        return -1;
                    }
                }
                if (doorNO.contains("4")) {
                    if (content[23] != (byte) 0x01) {
                        return -1;
                    }
                }
                ret = 1;
            }
        }catch (Exception e){

        }

        return ret;
    }

    public int openByCard(String cardId, String id, int doorNO, String ip, int port) {

        if(cardId == null){
            return AccessController.ERROR_CARD_ID;
        }

        //cardid最大值4294967295  2147483647
        try {
            long Max_cardId= 4294967295l;
            if( Long.parseLong(cardId)>Max_cardId){
                return AccessController.ERROR_CARD_ID;
            }
        }catch (Exception e){
            return AccessController.ERROR_CARD_ID;
        }

        //id最大值4294967295  2147483647
        try {
            long Max_id = 4294967295l;
            if( Long.parseLong(id)>Max_id){
                return AccessController.ERROR_SN;
            }
        }catch (Exception e){
            return AccessController.ERROR_SN;
        }

        byte[] idByte = long2bytes(Long.parseLong(id));
        byte[] cardByte = long2bytes(Long.parseLong(cardId));

        int ret = -13;
        byte[] byteCmd= newByte();
        byteCmd[1] = (byte) 0x40;
        byteCmd[8] = (byte) doorNO;
        byteCmd[40] = (byte) (tag & 0xff);
        byteCmd[41] = (byte) ((tag >> 8) & 0xff);
        byteCmd[42] = (byte) ((tag >> 16) & 0xff);
        byteCmd[43] = (byte) ((tag >> 24) & 0xff);

        //设备号
        for(int i=0,j=4;i<4;i++,j++){
            byteCmd[j] = (byte)(idByte[i]);
        }

        //卡号
        for(int i=0,j=20;i<4;i++,j++){
            byteCmd[j] = (byte)(cardByte[i]);
        }

        byte content[] = sendCmd(byteCmd,ip,port);


        if ((content != null) && (content.length == 64)) {
            ret = content[8];
        } else {
            ret = -13;
        }
        tag++;

        return ret;
    }



    private byte[] sendCmd(byte[] byteCmd,String ip,int port ){
        Log("IAccessController sendCmd: "+BinaryToHexString(byteCmd));
        return sendByUDP(byteCmd,ip,port);
    }

    private byte[] sendByUDP(byte[] byteCmd,String ip,int port ){
        byte content[] = null;
        DatagramPacket snddataPacket;
        try {
            int controllerPort = port;

            content = null;
            DatagramSocket dataSocket = new DatagramSocket();
            dataSocket.setSoTimeout(1000);

            snddataPacket = new DatagramPacket((byteCmd), byteCmd.length,
                    InetAddress.getByName(ip), controllerPort);
            dataSocket.send(snddataPacket);

            byte recvDataByte[] = new byte[64];

            Thread.sleep(200);
            DatagramPacket dataPacket = new DatagramPacket(recvDataByte,
                    recvDataByte.length);
            dataSocket.receive(dataPacket);

            content = dataPacket.getData();
            Log("IAccessController receive:"+BinaryToHexString(content));
            dataSocket.close();
        } catch (NumberFormatException exNum) {
            exNum.printStackTrace();
        }

        catch (UnknownHostException e1) {
            e1.printStackTrace();
            return content;
        } catch (SocketException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {

        }
        return content;
    }


    /*
    * 把16进制字符串转换成字节数组
    * @param hex
    * @return
    */
    public static byte[] hexStringToByte(String hex) {
        int len = (hex.length() / 2); //除以2是因为十六进制比如a1使用两个字符代表一个byte
        byte[] result = new byte[len];
        char[] achar = hex.toCharArray();
        for (int i = 0; i < len; i++) {
            //乘以2是因为十六进制比如a1使用两个字符代表一个byte,pos代表的是数组的位置
            //第一个16进制数的起始位置是0第二个是2以此类推
            int pos = i * 2;

            //<<4位就是乘以16  比如说十六进制的"11",在这里也就是1*16|1,而其中的"|"或运算就相当于十进制中的加法运算
            //如00010000|00000001结果就是00010001 而00010000就有点类似于十进制中10而00000001相当于十进制中的1，与是其中的或运算就相当于是10+1(此处说法可能不太对，)
            result[i] = (byte) (toByte(achar[pos]) << 4 | toByte(achar[pos + 1]));
        }
        return result;
    }

    private static byte toByte(char c) {
        byte b = (byte) "0123456789ABCDEF".indexOf(c);
        return b;
    }

    //将字节数组转换为short类型，即统计字符串长度
    public static short bytes2Short2(byte[] b) {
        short i = (short) (((b[1] & 0xff) << 8) | b[0] & 0xff);
        return i;
    }
    //将字节数组转换为16进制字符串
    public static String BinaryToHexString(byte[] bytes) {
        String hexStr = "0123456789ABCDEF";
        String result = "";
        String hex = "";
        for (byte b : bytes) {
            hex = String.valueOf(hexStr.charAt((b & 0xF0) >> 4));
            hex += String.valueOf(hexStr.charAt(b & 0x0F));
            result += hex + " ";
        }
        return result;
    }

    /**
     * @功能: BCD码转为10进制串(阿拉伯数据)
     * @参数: BCD码
     * @结果: 10进制串
     */
    public static String bcd2Str(byte[] bytes) {
        StringBuffer temp = new StringBuffer(bytes.length * 2);
        for (int i = 0; i < bytes.length; i++) {
            temp.append((byte) ((bytes[i] & 0xf0) >>> 4));
            temp.append((byte) (bytes[i] & 0x0f));
        }
        return temp.toString().substring(0, 1).equalsIgnoreCase("0") ? temp
                .toString().substring(1) : temp.toString();
    }

    /**
     * @功能: 10进制串转为BCD码
     * @参数: 10进制串
     * @结果: BCD码
     */
    public static byte[] str2Bcd(String asc) {
        int len = asc.length();
        int mod = len % 2;
        if (mod != 0) {
            asc = "0" + asc;
            len = asc.length();
        }
        byte abt[] = new byte[len];
        if (len >= 2) {
            len = len / 2;
        }
        byte bbt[] = new byte[len];
        abt = asc.getBytes();
        int j, k;
        for (int p = 0; p < asc.length() / 2; p++) {
            if ((abt[2 * p] >= '0') && (abt[2 * p] <= '9')) {
                j = abt[2 * p] - '0';
            } else if ((abt[2 * p] >= 'a') && (abt[2 * p] <= 'z')) {
                j = abt[2 * p] - 'a' + 0x0a;
            } else {
                j = abt[2 * p] - 'A' + 0x0a;
            }
            if ((abt[2 * p + 1] >= '0') && (abt[2 * p + 1] <= '9')) {
                k = abt[2 * p + 1] - '0';
            } else if ((abt[2 * p + 1] >= 'a') && (abt[2 * p + 1] <= 'z')) {
                k = abt[2 * p + 1] - 'a' + 0x0a;
            } else {
                k = abt[2 * p + 1] - 'A' + 0x0a;
            }
            int a = (j << 4) + k;
            byte b = (byte) a;
            bbt[p] = b;
        }
        return bbt;
    }

    static byte[] long2bytes(int n) {
        byte[] ab = new byte[4];
        ab[0] = (byte) (0xff & n);
        ab[1] = (byte) ((0xff00 & n) >> 8);
        ab[2] = (byte) ((0xff0000 & n) >> 16);
        ab[3] = (byte) ((0xff000000 & n) >> 24);
        return ab;
    }

    static byte[] long2bytes(long n) {
        byte[] ab = new byte[4];
        ab[0] = (byte) (0xff & n);
        ab[1] = (byte) ((0xff00 & n) >> 8);
        ab[2] = (byte) ((0xff0000 & n) >> 16);
        ab[3] = (byte) ((0xff000000 & n) >> 24);
        return ab;
    }


    StringBuilder build = new StringBuilder();

    private void Log(String msg){
//        System.out.print(msg);
//        Log.d(TAG,msg);
if (msg.contains("sendCmd")){
            build.delete(0,build.length());
            build.append(msg);
        }else {
            build.append("\r\n");
            build.append(msg);
        }
       MainActivity.mPrintView.setText(build.toString());
    }
}
