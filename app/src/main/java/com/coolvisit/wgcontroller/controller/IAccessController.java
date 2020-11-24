package com.coolvisit.wgcontroller.controller;

/**
 * Created by qicool on 2017/11/9.
 */

public interface IAccessController {

    /**
     * 搜控制器
     * @param ip
     * @param port
     * @return
     */
    public int searchDevices(String ip,int port);

    /**
     * 远程开门
     * @param id 控制器 sn 号
     * @param doorNo 门号
     * @param ip 门禁控制器ip 地址
     * @param port 端口号
     * @return
     */
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

    /**
     * 顺序批量添加 ic 卡
     * @param cardId
     * @param startDate
     * @param endDate
     * @param id 控制器 sn 号
     * @param doorNO
     * @param count
     * @param index
     * @param ip
     * @param port
     * @return 1：成功，-1：卡号过长，-2：SN错误 -3：日期格式错误
     */
    public int addByOrder(String cardId, String startDate, String endDate, String id, String doorNO,long count,long index, String ip, int port);


    /**
     * 梯控
     * @param cardId
     * @param startDate
     * @param endDate
     * @param id
     * @param floors 授权的门号 1，2,10,27
     * @param ip
     * @param port
     * @return 1：成功，-1：卡号过长，-2：SN错误 -3：日期格式错误
     */
    public int liftModifyCard(String cardId,String startDate,String endDate,String id,String floors,String ip, int port);

    public int getAllCards(String sn, String ip, int port);
}
