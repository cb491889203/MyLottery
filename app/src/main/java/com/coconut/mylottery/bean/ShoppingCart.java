package com.coconut.mylottery.bean;

import com.coconut.mylottery.util.GlobalParams;

import java.util.ArrayList;
import java.util.List;

/**
 * 彩票购物车
 * Created by Administrator on 2016/7/8 0008.
 */
public class ShoppingCart {

   /* 投注：
    lotteryid string * 玩法编号
    issue string * 期号（当前销售期）
    lotterycode string * 投注号码，注与注之间^分割
    lotterynumber string  注数
    lotteryvalue string  方案金额，以分为单位
    appnumbers string  倍数
    issuesnumbers string  追期
    issueflag int * 是否多期追号 0否，1多期
    bonusstop int * 中奖后是否停止：0不停，1停
    */

    private Integer lotteryId;
    private String issue;
    private List<Ticket> tickets = new ArrayList<>();
    private Integer lotterynumber;  // 计算
    private Integer lotteryvalue;
    private static ShoppingCart newInstance;

    /**
     * 倍数
     */
    private Integer appnumbers=1;
    /**
     * 追期
     */
    private Integer issuesnumbers=1;

    private  ShoppingCart() {
    }

    public synchronized static ShoppingCart newInstance() {
        if (newInstance == null) {
         newInstance = new ShoppingCart();
        }
        return newInstance;
    }
    public Integer getLotteryId() {
        return lotteryId;
    }

    public void setLotteryId(Integer lotteryId) {
        this.lotteryId = lotteryId;
    }

    public String getIssue() {
        return issue;
    }

    public void setIssue(String issue) {
        this.issue = issue;
    }

    public List<Ticket> getTickets() {
        return tickets;
    }

    /**
     * @return 所有的注数
     */
    public Integer getLotterynumber() {
        lotterynumber = 0;
        for (Ticket t : tickets) {
            lotterynumber += t.getNum();
        }
        return lotterynumber*appnumbers*issuesnumbers;
    }

    /**
     * 获取到注数才能计算金额.
     * @return 总金额
     */
    public Integer getLotteryvalue() {

        lotteryvalue = 2 * getLotterynumber();
        return lotteryvalue;
    }

    /**
     * @return 倍数
     */
    public Integer getAppnumbers() {
        return appnumbers;
    }

    /**
     * @return 追期
     */
    public Integer getIssuesnumbers() {
        return issuesnumbers;
    }

    /**
     * 操作倍数
     */
    public boolean addAppnumbers(boolean isAdd) {
        if (isAdd) {
            appnumbers++;
            if (appnumbers > 99) {
                appnumbers--;
                return false;
            }

            if (getLotteryvalue() > GlobalParams.MONEY) {
                appnumbers--;
                return false;
            }
        } else {
            appnumbers--;
            if (appnumbers == 0) {
                appnumbers++;
                return false;
            }
        }
        return true;
    }

    /**
     * 操作追期
     */
    public boolean addIssuesnumbers(boolean isAdd) {
        if (isAdd) {
            issuesnumbers++;
            if (issuesnumbers > 99) {
                issuesnumbers--;
                return false;
            }

            if (getLotteryvalue() > GlobalParams.MONEY) {
                issuesnumbers--;
                return false;
            }
        } else {
            issuesnumbers--;
            if (issuesnumbers == 0) {
                issuesnumbers++;
                return false;
            }
        }
        return true;
    }

    /**
     * 清空购物车
     */
    public void clear() {
        tickets.clear();
        lotterynumber = 0;
        lotteryvalue = 0;

        appnumbers = 1;
        issuesnumbers = 1;

    }
}
