package com.coconut.mylottery.net.protocol;

import org.xmlpull.v1.XmlSerializer;

/**
 * 每种请求信息都对应了不同的element,在获取完整Message信息包前必须指定Element的实现者,并对element中的Leaf叶子进行赋值,否者获取的message信息不完整. 如 当前彩票数据查询,
 * 注册用户,
 * 修改密码等Element.
 * Created by Administrator on 2016/6/18 0018.
 */
public abstract class Element {
    // 不会将所有的请求用到的叶子放到Element中
    // Element将作为所有请求的代表，Element所有请求的公共部分
    // 公共部分：
    // ①每个请求都需要序列化自己
    /**
     * 每个请求都需要序列化自己
     * @param serializer
     */
    public abstract void serializerElement(XmlSerializer serializer);
    // ②每个请求都有自己的标识
    /**
     * 每个请求都有自己的标识
     * @return 返回当前请求对应的标识码
     */
    public abstract String getTransactionType();




    // 包含内容
    // 序列化
    // 特有：请求标示

    // <lotteryid>118</lotteryid>
//	private Leaf lotteryid = new Leaf("lotteryid");
    // <issues>1</issues>
//	private Leaf issues = new Leaf("issues", "1");



//	public Leaf getLotteryid() {
//		return lotteryid;
//	}

//	/**
//	 * 序列化请求
//	 */
//	public void serializerElement(XmlSerializer serializer) {
//		try {
//			serializer.startTag(null, "element");
//			lotteryid.serializerLeaf(serializer);
//			issues.serializerLeaf(serializer);
//			serializer.endTag(null, "element");
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	}

//	/**
//	 * 获取请求的标示
//	 */
//	public String getTransactionType() {
//		return "12002";
//	}

}
