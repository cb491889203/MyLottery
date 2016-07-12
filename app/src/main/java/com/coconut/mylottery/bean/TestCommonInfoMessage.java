package com.coconut.mylottery.bean;

import com.coconut.mylottery.net.protocol.Body;
import com.coconut.mylottery.net.protocol.Header;
import com.coconut.mylottery.net.protocol.Message;
import com.coconut.mylottery.net.protocol.element.CurrentIssueElement;
import com.coconut.mylottery.util.ConstantValue;

/**
 * Created by Administrator on 2016/7/8 0008.
 */
public class TestCommonInfoMessage extends Message{
    private Header header = new Header();
    private Body body = new Body();

    public Header getHeader() {
        header.getTransactiontype().setTagValue("12002");
        header.getUsername().setTagValue("陈宝");
        return header;
    }

    public Body getBody() {
        //getOelement().getErrorcode().equals(ConstantValue.SUCCESS)
        body.getOelement().setErrorcode(ConstantValue.SUCCESS);
        CurrentIssueElement element = new CurrentIssueElement();
        element.getLotteryid().setTagValue(ConstantValue.LOTTERYID_SSQ+"");
        element.getIssue().setTagValue("2016078");
        element.getLasttime().setTagValue("32946");
        body.getElements().add(element);
        return body;
    }
}
