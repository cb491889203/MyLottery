package com.coconut.mylottery.engine.Impl;

import android.util.Log;
import android.util.Xml;

import com.coconut.mylottery.bean.ShoppingCart;
import com.coconut.mylottery.bean.Ticket;
import com.coconut.mylottery.bean.User;
import com.coconut.mylottery.engine.BaseEngine;
import com.coconut.mylottery.engine.UserEngine;
import com.coconut.mylottery.net.HttpURLUtil;
import com.coconut.mylottery.net.protocol.Message;
import com.coconut.mylottery.net.protocol.element.BalanceElement;
import com.coconut.mylottery.net.protocol.element.BetElement;
import com.coconut.mylottery.net.protocol.element.UserLoginElement;
import com.coconut.mylottery.util.ConstantValue;
import com.coconut.mylottery.util.DES;
import com.coconut.mylottery.util.GlobalParams;

import org.apache.commons.codec.digest.DigestUtils;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;

/**
 * 使用的baseEngine后的UserLogin逻辑.
 * Created by Administrator on 2016/6/23 0023.
 */
public class UserEngineImpl extends BaseEngine implements UserEngine {

    private static final String TAG = "UserEngineImpl";

    public Message login(User user) {
        //第一步: 完善element,获取message的xml.
        UserLoginElement userElement = new UserLoginElement();
        userElement.getActpassword().setTagValue(user.getPassword());
        Message message = new Message();
        message.getHeader().getUsername().setTagValue(user.getUsername());
        String xml = message.getXml(userElement);
        //第二三步:
        Message result = getResult(xml);

        if (result != null) {
            //第四步:对body明文进行第二次解析.
            XmlPullParser parser = Xml.newPullParser();
            DES des = new DES();
            String desInfo = des.authcode(result.getBody().getServiceBodyInsideDESInfo(), "DECODE",
                    ConstantValue.DES_PASSWORD);
            String body = "<body>" + desInfo + "</body>";
            try {
                parser.setInput(new StringReader(body));

                int eventype1 = parser.getEventType();
                while (eventype1 != XmlPullParser.END_DOCUMENT) {
                    switch (parser.getEventType()) {
                        case XmlPullParser.START_TAG:
                            if ("errorcode".equals(parser.getName())) {
                                result.getBody().getOelement().setErrorcode(parser.nextText());
                            } else if ("errormsg".equals(parser.getName())) {
                                result.getBody().getOelement().setErrormsg(parser.nextText());
                            }
                            break;
                    }
                    eventype1 = parser.next();
                }
            } catch (XmlPullParserException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            Log.i(TAG, "login: result中没有数据.用测试的数据.默认返回成功.");
            result = new Message();
            result.getBody().getOelement().setErrorcode(ConstantValue.SUCCESS);

        }
        return result;
    }


    @Override
    public Message getBalance(User user) {
        BalanceElement element = new BalanceElement();
        Message message = new Message();
        message.getHeader().getUsername().setTagValue(user.getUsername());
        String xml = message.getXml(element);

        Message result = super.getResult(xml);
        BalanceElement resultElement = null;
        if (result != null) {

            // 第四步：请求结果的数据处理
            // body部分的第二次解析，解析的是明文内容

            XmlPullParser parser = Xml.newPullParser();
            try {

                DES des = new DES();
                String body = "<body>" + des.authcode(result.getBody().getServiceBodyInsideDESInfo(),
                        "ENCODE", ConstantValue.DES_PASSWORD) + "</body>";

                parser.setInput(new StringReader(body));

                int eventType = parser.getEventType();
                String name;



                while (eventType != XmlPullParser.END_DOCUMENT) {
                    switch (eventType) {
                        case XmlPullParser.START_TAG:
                            name = parser.getName();
                            if ("errorcode".equals(name)) {
                                result.getBody().getOelement().setErrorcode(parser.nextText());
                            }
                            if ("errormsg".equals(name)) {
                                result.getBody().getOelement().setErrormsg(parser.nextText());
                            }

                            // 正对于当前请求
                            if ("element".equals(name)) {
                                resultElement = new BalanceElement();
                                result.getBody().getElements().add(resultElement);
                            }

                            if ("investvalues".equals(name)) {
                                if (resultElement != null) {
                                    resultElement.setInvestvalues(parser.nextText());
                                }
                            }

                            break;
                    }
                    eventType = parser.next();
                }


            } catch (Exception e) {
                e.printStackTrace();
            }

        } else {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            Log.i(TAG, "getBalance: 联网不成功,使用测试的返回数据!获取可用金额.");
            result = new Message();
            result.getBody().getOelement().setErrorcode(ConstantValue.SUCCESS);
            result.getHeader().getTransactiontype().setTagValue(element.getTransactionType());
            resultElement = new BalanceElement();
            resultElement.setInvestvalues("88");
            result.getBody().getElements().add(resultElement);
        }

        return result;
    }

    @Override
    public Message bet(User user) {
        BetElement element = new BetElement();
        element.getLotteryid().setTagValue(String.valueOf(ShoppingCart.newInstance().getLotteryId()));

        StringBuffer codeBuffer = new StringBuffer();
        for (Ticket item : ShoppingCart.newInstance().getTickets()) {
            codeBuffer.append("^").append(item.getRedNum().replaceAll(" ", "")).append("|").append(item.getBlueNum().replaceAll(" ", ""));
        }
        element.getLotterycode().setTagValue(codeBuffer.substring(1));

        element.getIssue().setTagValue(ShoppingCart.newInstance().getIssue());
        element.getLotteryvalue().setTagValue((ShoppingCart.newInstance().getLotteryvalue() * 100) + "");

        element.getLotterynumber().setTagValue(ShoppingCart.newInstance().getLotterynumber().toString());
        element.getAppnumbers().setTagValue(ShoppingCart.newInstance().getAppnumbers().toString());
        element.getIssuesnumbers().setTagValue(ShoppingCart.newInstance().getIssuesnumbers().toString());

        element.getIssueflag().setTagValue(ShoppingCart.newInstance().getIssuesnumbers() > 1 ? "1" : "0");

        Message message = new Message();
        message.getHeader().getUsername().setTagValue(user.getUsername());
        String xml = message.getXml(element);
        Message result = getResult(xml);

        if (result != null) {

            // 第四步：请求结果的数据处理
            // body部分的第二次解析，解析的是明文内容

            XmlPullParser parser = Xml.newPullParser();
            try {

                DES des = new DES();
                String body = "<body>" + des.authcode(result.getBody().getServiceBodyInsideDESInfo(), "ENCODE", ConstantValue.DES_PASSWORD) + "</body>";

                parser.setInput(new StringReader(body));

                int eventType = parser.getEventType();
                String name;

                BetElement resultElement = null;

                while (eventType != XmlPullParser.END_DOCUMENT) {
                    switch (eventType) {
                        case XmlPullParser.START_TAG:
                            name = parser.getName();
                            if ("errorcode".equals(name)) {
                                result.getBody().getOelement().setErrorcode(parser.nextText());
                            }
                            if ("errormsg".equals(name)) {
                                result.getBody().getOelement().setErrormsg(parser.nextText());
                            }

                            // 正对于当前请求
                            if ("element".equals(name)) {
                                resultElement = new BetElement();
                                result.getBody().getElements().add(resultElement);
                            }

                            if ("actvalue".equals(name)) {
                                if (resultElement != null) {
                                    resultElement.setActvalue(parser.nextText());
                                }
                            }

                            break;
                    }
                    eventType = parser.next();
                }



            } catch (Exception e) {
                e.printStackTrace();
            }

        }else{
            Log.i(TAG, "bet: result为空,返回测试用的数据,betMessage!");
            result = new Message();
            result.getHeader().getUsername().setTagValue(user.getUsername());
            float actvalue = GlobalParams.MONEY - ShoppingCart.newInstance().getLotteryvalue();
            element.setActvalue(String.valueOf(actvalue));
            result.getBody().getElements().add(element);
            result.getBody().getOelement().setErrorcode(ConstantValue.SUCCESS);

        }

        return result;
    }


    /**
     * 最原始的方法,没有进行分离. 所有逻辑都在这一个方法里.
     *
     * @param user 用户的javabean
     * @return 返回登录后的服务器回复信息.
     * @throws XmlPullParserException
     * @throws IOException
     */
    public Message login1(User user) throws XmlPullParserException, IOException {

        //第一步: 完善element,获取message的xml.
        UserLoginElement userElement = new UserLoginElement();
        userElement.getActpassword().setTagValue(user.getPassword());
        Message message = new Message();
        message.getHeader().getUsername().setTagValue(user.getUsername());
        String xml = message.getXml(userElement);

        //第二步:利用HttpClientUtil发送xml到服务器,等待回复.在这里没有进行网络判断.
        HttpURLUtil clientUtil = new HttpURLUtil();
        InputStream inputStream = clientUtil.sendXml(ConstantValue.LOTTERY_URI, xml);
        if (inputStream != null) {

            Message result = new Message();
            //第三步:对返回的数据进行数据校验.(MD5数据校验)
            //原始数据还原:digest = 时间戳(xml解析)+代理商密码(常量)+明文body(xml文件解析+DES解密)

            XmlPullParser parser = Xml.newPullParser();
            parser.setInput(inputStream, ConstantValue.ENCODING);
            int eventype = parser.getEventType();
            while (eventype != XmlPullParser.END_DOCUMENT) {
                switch (parser.getEventType()) {
                    case XmlPullParser.START_TAG:
                        if ("timestamp".equals(parser.getName())) {
                            result.getHeader().getTimestamp().setTagValue(parser.nextText());
                        } else if ("digest".equals(parser.getName())) {
                            result.getHeader().getDigest().setTagValue(parser.nextText());
                        } else if ("body".equals(parser.getName())) {
                            result.getBody().setServiceBodyInsideDESInfo(parser.nextText());
                        }
                        break;
                }
                eventype = parser.next();
            }

            // 还原digest = timestamp + agientid + body
            DES des = new DES();
            String desInfo = des.authcode(result.getBody().getServiceBodyInsideDESInfo(), "DECODE",
                    ConstantValue.DES_PASSWORD);
            String timestamp = result.getHeader().getTimestamp().getTagValue();
            String body = "<body>" + desInfo + "</body>";
            String digest = result.getHeader().getDigest().getTagValue();
            String md5Hex = DigestUtils.md5Hex(timestamp + ConstantValue.AGENTER_PASSWORD + body);

            if (md5Hex.equals(digest)) {
                //第四步:对body明文进行第二次解析.
                parser = Xml.newPullParser();
                parser.setInput(new StringReader(body));

                int eventype1 = parser.getEventType();
                while (eventype1 != XmlPullParser.END_DOCUMENT) {
                    switch (parser.getEventType()) {
                        case XmlPullParser.START_TAG:
                            if ("errorcode".equals(parser.getName())) {
                                result.getBody().getOelement().setErrorcode(parser.nextText());
                            } else if ("errormsg".equals(parser.getName())) {
                                result.getBody().getOelement().setErrormsg(parser.nextText());
                            }
                            break;
                    }
                    eventype1 = parser.next();
                }
                return result;
            } else {
                Log.i(TAG, "login: 服务器端的digest与客户端解析的digest不一致!");
            }
        } else {
            Log.i(TAG, "login: 发送文件后,返回的输入流为空!!");
        }

        return null;
    }
}
