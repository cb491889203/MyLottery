package com.coconut.mylottery.net.protocol.element;

import com.coconut.mylottery.net.protocol.Element;
import com.coconut.mylottery.net.protocol.Leaf;

import org.xmlpull.v1.XmlSerializer;

import java.io.IOException;

/**
 * 当前彩票信息的元素. 其中包括的Leaf是 lotteryid 和issues.
 * 头信息中的TransactionType的值是12002,
 * Created by Administrator on 2016/6/21 0021.
 */
public class CurrentIssueElement extends Element{

    /**
     * 客户想要查看的彩票玩法Id.SSQ为118
     */
    private Leaf lotteryid = new Leaf("lotteryid");
    /**
     * 客户想要查看的彩票期号,需要获取的最大期数max=100期，当前期为1
     */
    private Leaf issues = new Leaf("issues");

    /**
     * @return 客户想要查看的彩票期号
     */
    public Leaf getIssues() {
        return issues;
    }

    @Override
    public void serializerElement(XmlSerializer serializer) {
        try {
            serializer.startTag(null, "element");
            lotteryid.serializerLeaf(serializer);
            issues.serializerLeaf(serializer);
            serializer.endTag(null, "element");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @Override
    public String getTransactionType() {
        return "12002";
    }

    public Leaf getLotteryid() {
        return lotteryid;
    }


    //******************服务器返回的数据****************//

    /**
     *到期结剩余时间（秒）, 截止到endtime1.
     */
    private Leaf lasttime = new Leaf("lasttime");
    /**
     * 当前彩票玩法的期号.
     */
    private Leaf issue = new Leaf("issue");

    /**
     * 官方开期时间（yyyy-mm-dd hh24:mi）
     */
    private Leaf starttimestamp = new Leaf("starttimestamp");

    /**
     * 官方截止时间（yyyy-mm-dd hh24:mi）
     */
    private Leaf endtimestamp = new Leaf("endtimestamp");

    /**
     * 代购期结时间（yyyy-mm-dd hh24:mi）
     */
    private Leaf endtime1 = new Leaf("endtime1");
    /**
     * 单式方案截止时间（yyyy-mm-dd hh24:mi）包括预发方案上传
     */
    private Leaf endtime2 = new Leaf("endtime2");
    /**
     *复式方案截止时间（yyyy-mm-dd hh24:mi）
     */
    private Leaf endtime3 = new Leaf("endtime3");
    /**
     *开奖时间（yyyy-mm-dd hh24:mi）
     */
    private Leaf bonustime = new Leaf("bonustime");
    /**
     *玩法状态
     */
    private Leaf status = new Leaf("status");

    public Leaf getLasttime() {
        return lasttime;
    }

    public Leaf getIssue() {
        return issue;
    }


    //******************服务器返回的数据****************//


}
