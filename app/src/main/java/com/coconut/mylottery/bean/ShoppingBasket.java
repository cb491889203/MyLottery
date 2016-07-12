package com.coconut.mylottery.bean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/7/10 0010.
 */
public class ShoppingBasket {
    private static ShoppingBasket newInstance;
    private ShoppingBasket(){}
    public synchronized static ShoppingBasket newInstance(){
        if(newInstance == null){
            newInstance = new ShoppingBasket();
        }
        return newInstance;
    }

    public static List<Ticket> tickets = new ArrayList<>();

    private Integer lotterynumber;  // 计算
    private Integer lotteryvalue;

    /**
     * @return 总注数
     */
    public Integer getLotterynumber() {
        lotterynumber = 0;
        for (Ticket t : tickets) {
            lotterynumber += t.getNum();
        }
        return lotterynumber;
    }

    /**
     * @param lotterynumber 总注数
     */
    public void setLotterynumber(Integer lotterynumber) {
        this.lotterynumber = lotterynumber;
    }

    /**
     * @return 总金额
     */
    public Integer getLotteryvalue() {
        return getLotterynumber()*2;
    }

    /**
     * @param lotteryvalue 总金额
     */
    public void setLotteryvalue(Integer lotteryvalue) {
        this.lotteryvalue = lotteryvalue;
    }
}
