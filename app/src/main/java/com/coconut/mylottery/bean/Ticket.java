package com.coconut.mylottery.bean;

/**
 * 用户投注信息的封装
 * Created by Administrator on 2016/7/8 0008.
 */
public class Ticket {

    private String redNum;
    private String blueNum;
    /**
     * 注数
     */
    private int num;
    private int value;
    private long time;
    private String issue;

    public String getIssue() {
        return issue;
    }

    public void setIssue(String issue) {
        this.issue = issue;
    }

    /**
     * @return 成功购买的时间
     */
    public long getTime() {
        return time;
    }

    /**
     * @param time 设置成功购买的时间
     */
    public void setTime(long time) {
        this.time = time;
    }

    /**
     * @return 保证有注数后,才能得到金额
     */
    public int getValue() {
        return num * 2;
    }

    public String getRedNum() {
        return redNum;
    }

    public void setRedNum(String redNum) {
        this.redNum = redNum;
    }

    public String getBlueNum() {
        return blueNum;
    }

    public void setBlueNum(String blueNum) {
        this.blueNum = blueNum;
    }

    /**
     * 注数
     *
     * @return 注数
     */
    public int getNum() {
        return num;
    }

    /**
     * 注数
     *
     * @param num 注数
     */
    public void setNum(int num) {
        this.num = num;
    }


}
