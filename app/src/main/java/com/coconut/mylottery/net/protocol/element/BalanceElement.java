package com.coconut.mylottery.net.protocol.element;

import com.coconut.mylottery.net.protocol.Element;

import org.xmlpull.v1.XmlSerializer;


/**
 * 获取余额
 * 
 * @author Administrator
 * 
 */
public class BalanceElement extends Element {

	/*************** 回复信息 **********************/
	// investvalues 可投注金额
	private String investvalues;

	public String getInvestvalues() {
		return investvalues;
	}

	public void setInvestvalues(String investvalues) {
		this.investvalues = investvalues;
	}

	/*************************************/

	@Override
	public void serializerElement(XmlSerializer serializer) {
	}

	@Override
	public String getTransactionType() {
		return "11007";
	}

}
