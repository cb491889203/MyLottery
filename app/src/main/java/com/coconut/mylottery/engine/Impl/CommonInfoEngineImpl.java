package com.coconut.mylottery.engine.Impl;

import android.util.Log;

import com.coconut.mylottery.bean.TestCommonInfoMessage;
import com.coconut.mylottery.engine.BaseEngine;
import com.coconut.mylottery.engine.CommonInfoEngine;
import com.coconut.mylottery.net.protocol.Message;
import com.coconut.mylottery.net.protocol.element.CurrentIssueElement;
import com.coconut.mylottery.util.ConstantValue;
import com.coconut.mylottery.util.DES;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;

/**
 * Created by Administrator on 2016/7/3 0003.
 */
public class CommonInfoEngineImpl extends BaseEngine implements CommonInfoEngine {
    private static final String TAG = "CommonInfoEngineImpl";

    /**
     * 将查看当前期的彩票数据的 请求业务发送给服务器, 服务器返回数据. 其中已经对数据进行解析.并将数据封装在message中返回.
     * @return 已经封装了所有数据的message信息.
     */
    @Override
    public Message getCommonInfo(Integer lotteryId) {
        //第一步: 完善element,获取message的xml.
        CurrentIssueElement element = new CurrentIssueElement();
        element.getLotteryid().setTagValue(lotteryId+"");
        element.getIssues().setTagValue("1");
        Message message = new Message();
        message.getHeader().getUsername().setTagValue("黄燕");
        String xml = message.getXml(element);
        Message result = getResult(xml);
        // 第四步;解密DES body部分
        if(result!= null){
            String desInfo = result.getBody().getServiceBodyInsideDESInfo();
            DES des = new DES();
            String body = "<body>"+des.authcode(desInfo, "DECODE", ConstantValue.DES_PASSWORD)+"</body>";
            try {
                XmlPullParser parser =  XmlPullParserFactory.newInstance().newPullParser();

                int eventType = parser.getEventType();
                CurrentIssueElement issueElement= null;
                while (eventType!=XmlPullParser.END_DOCUMENT) {
                    switch(eventType){
                        case XmlPullParser.START_TAG:
                            if ("errorcode".equals(parser.getName())) {
                                result.getBody().getOelement().setErrorcode(parser.nextText());
                            } else if ("errormsg".equals(parser.getName())) {
                                result.getBody().getOelement().setErrormsg(parser.nextText());
                            } else if ("element".equals(parser.getName())) {
                                issueElement = new CurrentIssueElement();
                                result.getBody().getElements().add(issueElement);
                            } else if ("issue".equals(parser.getName())) {
                                if (issueElement != null) {
                                issueElement.getIssue().setTagValue(parser.nextText());


                                }
                            } else if ("lasttime".equals(parser.getName())) {
                                if (issueElement != null) {
                                issueElement.getLasttime().setTagValue(parser.nextText());
                                }
                            }
                        break;
                        default:

                        break;
                    }
                    eventType = parser.next();
                }

            } catch (XmlPullParserException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return result;
        }else{
            Log.i(TAG, "getCommonInfo: 服务器返回的message为空!!!!,返回一个手动添加的默认message");
        }
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        //为了测试.这里回复一个手动添加的Message
        TestCommonInfoMessage message1 = new TestCommonInfoMessage();
       CurrentIssueElement element1 = (CurrentIssueElement) message1.getBody().getElements().get(0);
        element1.getLotteryid().setTagValue(lotteryId.toString());
        if(lotteryId==ConstantValue.LOTTERYID_3D){
            element1.getIssue().setTagValue("20160711");
            element1.getLasttime().setTagValue("24684");
        }else if(lotteryId==ConstantValue.LOTTERYID_7LC){
            element1.getIssue().setTagValue("20160709");
            element1.getLasttime().setTagValue("76700");
        }
        return message1;
    }
}
