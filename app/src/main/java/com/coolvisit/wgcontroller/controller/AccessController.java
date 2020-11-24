package com.coolvisit.wgcontroller.controller;

/**
 * Created by qicool on 2017/11/22.
 */

public class AccessController  {

    public static final int ERROR_CARD_ID = -1;
    public static final int ERROR_SN = -2;
    public static final int ERROR_CARD_DATE = -3;


    private static IAccessController ac = new WGController();

    public static int searchDevices(String ip, int port) {
        return 0;
    }


    public static int open(String id, int doorNo, String ip, int port) {
        return ac.open(id,doorNo,ip,port);
    }


    public static int modifyCard(String cardId, String startDate, String endDate, String id, String doorNO, String ip, int port) {
        return ac.modifyCard(cardId,startDate,endDate,id,doorNO,ip,port);
    }


    public static int deleteCard(String cardId, String id, String ip, int port) {
        return ac.deleteCard(cardId,id,ip,port);
    }


    public static int checkCard(String cardId, String id, String doorNO, String ip, int port) {
        return ac.checkCard(cardId,id,doorNO,ip,port);
    }

    /**
     * 清空控制器所有权限
     * @param id
     * @param ip
     * @param port
     * @return
     */
    public static int clearCard(String id,String ip, int port) {
        return ac.clearCard(id,ip,port);
    }

    public static int getAllCards(String id,String ip, int port) {
        return ac.getAllCards(id,ip,port);
    }
}
