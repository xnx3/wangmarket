package com.xnx3.wangmarket.plugin.formManage.bean;

/**
 * 反馈频率，控制其反馈的频率。每个ip都会对其有限制
 * @author 管雷鸣
 *
 */
public class Frequency {
	public String ip;		//提交表单的人的ip地址
	public int lasttime;	//最后一次提交表单所用的时间，10位时间戳
	public int forbidtime;	//禁止提交的时间，10位时间戳。默认是0。 表单是否可以正常提交，是有此控制。需要用此来进行判断，若是此处是一个小时以后，则当前一个小时之内都是无法提交的。一个小时后才可以提交反馈
	public int errorNumber;	//此人在不可以提交表单时，持续尝试提交的次数。若次数过大，肯定是非法操作，需禁用ip
	
	public Frequency() {
		ip = "";
		lasttime = 0;
		forbidtime = 0;
		errorNumber = 0;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public int getLasttime() {
		return lasttime;
	}

	public void setLasttime(int lasttime) {
		this.lasttime = lasttime;
	}

	public int getForbidtime() {
		return forbidtime;
	}

	public void setForbidtime(int forbidtime) {
		this.forbidtime = forbidtime;
	}

	public int getErrorNumber() {
		return errorNumber;
	}

	public void setErrorNumber(int errorNumber) {
		this.errorNumber = errorNumber;
	}

	@Override
	public String toString() {
		return "Frequency [ip=" + ip + ", lasttime=" + lasttime
				+ ", forbidtime=" + forbidtime + ", errorNumber=" + errorNumber
				+ "]";
	}
	
}
