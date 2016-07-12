package com.coconut.mylottery.engine;

import android.util.Xml;

import com.coconut.mylottery.net.HttpURLUtil;
import com.coconut.mylottery.net.protocol.Message;
import com.coconut.mylottery.util.ConstantValue;
import com.coconut.mylottery.util.DES;

import org.apache.commons.codec.digest.DigestUtils;
import org.xmlpull.v1.XmlPullParser;

import java.io.InputStream;

/**
 * 提供每个业务逻辑engine类共同的逻辑部分,用于处理所有请求信息中一样的业务逻辑.比如对返回的数据进行第一次DES解密,
 * 从而进行MD5值校验.本类中就一个公共方法,getResult().在具体业务的实现engine类中调用这个方法即可.
 * <p>
 * 实现这个类的子类需要自己完成剩余的2步:第一步: 创建一个具体业务的element,声明一个Message,将这个element对象当做参数传入message.getXml(element)
 * 获取message的xml.
 * 第二步&第三步: 调用BaseEngine中已经写好的方法getResult(xml),获取从服务器返回来的message信息.
 * 第四步:对body明文进行第二次解析.
 * Created by Administrator on 2016/6/23 0023.
 */
public abstract class BaseEngine {

    /**
     * BaseEngine 中的方法每个实现的业务engine类都需要用
     * 用于处理所有请求信息中一样的业务逻辑.比如对返回的数据进行第一次DES解密,从而进行MD5值校验.
     *
     * @param xml 需要发送给服务器的xml信息.这个xml是完整的一个message信息了.
     * @return 如果MD5值匹配, 就返回服务器发送的Message信息, 其中已经解析了服务器返回的xml文件, 并将xml文件中的信息封装到message对象中包括body的DES信息, 头信息.
     * 不匹配,
     * 返回null.
     */
    public Message getResult(String xml) {

        //第二步:利用HttpClientUtil发送xml到服务器,等待回复.在这里没有进行网络判断.
        HttpURLUtil clientUtil = new HttpURLUtil();
        InputStream inputStream = clientUtil.sendXmlByOkHttp(ConstantValue.LOTTERY_URI, xml);
//        InputStream inputStream = clientUtil.sendXml(ConstantValue.LOTTERY_URI, xml);
        if (inputStream != null) {

            Message result = new Message();
            //第三步:对返回的数据进行数据校验.(MD5数据校验)
            //原始数据还原:digest = 时间戳(xml解析)+代理商密码(常量)+明文body(xml文件解析+DES解密)

            XmlPullParser parser = Xml.newPullParser();
            try {
                parser.setInput(inputStream, ConstantValue.ENCODING);
                int eventype = parser.getEventType();
                while (eventype != XmlPullParser.END_DOCUMENT) {
                    switch (parser.getEventType()) {
                        case XmlPullParser.START_TAG:
                            if ("messengerid".equals(parser.getName())) {
                                result.getHeader().getMessengerId().setTagValue(parser.nextText());
                            } else if ("timestamp".equals(parser.getName())) {
                                result.getHeader().getTimestamp().setTagValue(parser.nextText());
                            } else if ("digest".equals(parser.getName())) {
                                result.getHeader().getDigest().setTagValue(parser.nextText());
                            } else if ("body".equals(parser.getName())) {
                                result.getBody().setServiceBodyInsideDESInfo(parser.nextText());
                            } else if ("transactiontype".equals(parser.getName())) {
                                result.getHeader().getTransactiontype().setTagValue(parser.nextText());
                            } else if ("username".equals(parser.getName())) {
                                result.getHeader().getUsername().setTagValue(parser.nextText());
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
                    return result;
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}
