package com.coconut.mylottery.net.protocol.element;

import com.coconut.mylottery.net.protocol.Element;
import com.coconut.mylottery.net.protocol.Leaf;

import org.xmlpull.v1.XmlSerializer;


/**
 * 投注请求
 * 
 * @author Administrator
 * 
 */
public class BetElement extends Element {
	// lotteryid string * 玩法编号
	private Leaf lotteryid = new Leaf("lotteryid");
	// issue string * 期号（当前销售期）
	// lotterycode string * 投注号码，注与注之间^分割
	private Leaf lotterycode = new Leaf("lotterycode");

	// issue string * 期号（当前销售期）
	private Leaf issue = new Leaf("issue");

	// lotterynumber string 注数
	private Leaf lotterynumber = new Leaf("lotterynumber");
	// lotteryvalue string 方案金额，以分为单位
	private Leaf lotteryvalue = new Leaf("lotteryvalue");
	// appnumbers string 倍数
	private Leaf appnumbers = new Leaf("appnumbers");
	// issuesnumbers string 追期
	private Leaf issuesnumbers = new Leaf("issuesnumbers");
	// issueflag int * 是否多期追号 0否，1多期
	private Leaf issueflag = new Leaf("issueflag");

	private Leaf bonusstop = new Leaf("bonusstop", "1");
	
	/*********************************************/
//	actvalue int * 用户账户余额
	private String actvalue;
	
	
	public String getActvalue() {
		return actvalue;
	}

	public void setActvalue(String actvalue) {
		this.actvalue = actvalue;
	}

	/*********************************************/

	@Override
	public void serializerElement(XmlSerializer serializer) {
		try {
			serializer.startTag(null, "element");
			lotteryid.serializerLeaf(serializer);
			issue.serializerLeaf(serializer);
			lotteryvalue.serializerLeaf(serializer);
			lotterynumber.serializerLeaf(serializer);
			appnumbers.serializerLeaf(serializer);
			issuesnumbers.serializerLeaf(serializer);
			lotterycode.serializerLeaf(serializer);
			issueflag.serializerLeaf(serializer);
			bonusstop.serializerLeaf(serializer);
			serializer.endTag(null, "element");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public String getTransactionType() {
		return "12006";
	}

	public Leaf getLotteryid() {
		return lotteryid;
	}

	public Leaf getIssue() {
		return issue;
	}

	/**
	 * @return 投注号码，注与注之间^分割
	 */
	public Leaf getLotterycode() {
		return lotterycode;
	}

	/**
	 * @return 注数
	 */
	public Leaf getLotterynumber() {
		return lotterynumber;
	}

	/**
	 * @return 总金额,方案金额，以分为单位
	 */
	public Leaf getLotteryvalue() {
		return lotteryvalue;
	}

	/**
	 * @return 倍数
	 */
	public Leaf getAppnumbers() {
		return appnumbers;
	}

	/**
	 * @return 追期
	 */
	public Leaf getIssuesnumbers() {
		return issuesnumbers;
	}

	/**
	 * @return 是否多期追期
	 */
	public Leaf getIssueflag() {
		return issueflag;
	}

}
