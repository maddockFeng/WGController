package com.coolvisit.wgcontroller.pingzhi;

import android.util.Log;

import java.util.HashMap;
import java.util.List;
import java.util.Set;

/**
 * Created by qicool on 2018/3/26.
 */

public class PZApi {

    /**
     *
     * @param devices 可以打开的设备列表 0003000010，000300为设备组 0010表示二号门
     * @param decodePhone 用户卡号，代表用户身份，8位十六进制（11位手机号转16进制后取后8位），由开发者生成
     * @param time 代表有效期的截止时间，长度为10，比如：2016年10月20日15时30分，相应的传入的格式为1610201530
     * @return
     */
    public static String getQRCode(List<String> devices,String decodePhone,String time){
        if (devices.size() == 0){
            return "-1";
        }
        String QRCode = null;
//        final  String u = "chuizikeji";
//        final  String p = "chuizikeji";
//        final  String key = "88888888";

        String paramBuild = "http://123.pingzhi.com:8080/qnsite/a/api/qrcfs?u=chuizikeji&p=chuizikeji&device=%1$s&time=%2$s&key=88888888&card=%3$s&floor=00000100000001&type=00";
        HashMap<String,Integer> dMap = new HashMap<>();
        for(String device:devices){

            String d = device.substring(0,5);
            String door = device.substring(6);
            Integer doors = dMap.get(d);
            if (door != null){
                dMap.put(d,doors+Integer.parseInt(door));
            }else {
                dMap.put(d,Integer.parseInt(door));
            }
        }

        Set<String> dSet = dMap.keySet();
        StringBuilder dBuilder = new StringBuilder();
        if (dSet != null){
            for (String d:dSet){
                if (dBuilder.length() > 0){
                    dBuilder.append(",");
                }
                dBuilder.append("d");
                dBuilder.append(Integer.toString(dMap.get(d), 2));
            }
        }
        return QRCode;

    }
}
