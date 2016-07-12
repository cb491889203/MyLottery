package com.coconut.mylottery.net.protocol;

/**
 * 处理服务器回复信息的状态信息元素
 * Created by Administrator on 2016/6/23 0023.
 */
public class Oelement {

    private String errorcode;
    private String errormsg;

    public void setErrormsg(String errormsg) {
        this.errormsg = errormsg;
    }

    public String getErrormsg() {

        return errormsg;
    }

    public String getErrorcode() {

        return errorcode;
    }

    public void setErrorcode(String errorcode) {
        this.errorcode = errorcode;
    }
}
