package com.coolvisit.wgcontroller.controller;

/**
 * Created by qicool on 2017/11/9.
 */

public interface IAccessController {

    public int searchDevices(String ip,int port);

    public int open(String id,int doorNo,String ip,int port);

    /**
     * 添加修改控制器权限
     * @param cardId ic 卡号
     * @param startDate 开始日期 20100101
     * @param endDate 截止日期 20290101
     * @param id 控制器 sn 号
     * @param doorNO 授权的门号 123
     * @param ip
     * @param port
     * @return 成功 1 失败 其他
     */
    public int modifyCard(String cardId,String startDate,String endDate,String id,String doorNO,String ip, int port);

    /**
     * 删除控制器权限
     * @param cardId ic 卡号
     * @param id 控制器 sn 号
     * @param ip
     * @param port
     * @return 成功 1 失败 其他
     */
    public int deleteCard(String cardId,String id,String ip, int port);

    /**
     * 检查控制器权限
     * @param cardId ic 卡号
     * @param id 控制器 sn 号
     * @param ip
     * @param port
     * @return 成功 1 失败 其他
     */
    public int checkCard(String cardId,String id,String doorNO,String ip, int port);

    /**
     * 清空控制器所有权限
     * @param id
     * @param ip
     * @param port
     * @return
     */
    public int clearCard(String id,String ip, int port);

    public int addByOrder(String cardId, String startDate, String endDate, String id, String doorNO,long count,long index, String ip, int port);
}
