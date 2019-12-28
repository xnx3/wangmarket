package com.xnx3.j2ee.entity;

import static javax.persistence.GenerationType.IDENTITY;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import com.xnx3.DateUtil;
import com.xnx3.StringUtil;

/**
 * 支付日志
 */
@Entity
@Table(name="pay_log")
public class PayLog  implements java.io.Serializable {

	/**
	 * 支付宝手机支付
	 */
	public final static String CHANNEL_ALIPAY="alipay";	
	/**
	 * 支付宝手机网页支付
	 */
	public final static String CHANNEL_ALIPAY_WAP="alipay_wap";	
	/**
	 * 支付宝扫码支付
	 */
	public final static String CHANNEL_ALIPAY_QR="alipay_qr";	
	/**
	 * 支付宝 PC 网页支付
	 */
	public final static String CHANNEL_ALIPAY_PC_DIRECT="alipay_pc_direct";	
	/**
	 * 银联全渠道支付（2015 年 1 月 1 日后的银联新商户使用。若有疑问，请与 Ping++ 或者相关的收单行联系）
	 */
	public final static String CHANNEL_UPACP="upacp";	
	/**
	 * 银联全渠道手机网页支付（2015 年 1 月 1 日后的银联新商户使用。若有疑问，请与 Ping++ 或者相关的收单行联系）
	 */
	public final static String CHANNEL_UPACP_WAP="upacp_wap";	
	/**
	 * 银联 PC 网页支付
	 */
	public final static String CHANNEL_UPACP_PC="upacp_pc";	
	/**
	 * 银联企业网银支付
	 */
	public final static String CHANNEL_CP_B2B="cp_b2b";	
	/**
	 * 微信支付
	 */
	public final static String CHANNEL_WX="wx";	
	/**
	 * 微信公众账号支付
	 */
	public final static String CHANNEL_WX_PUB="wx_pub";	
	
	/**
	 * 微信公众账号扫码支付
	 */
	public final static String CHANNEL_WX_PUB_QR="wx_pub_qr";	
	/**
	 * Apple Pay
	 */
	public final static String CHANNEL_APPLEPAY_UPACP="applepay_upacp";	
	/**
	 * 百度钱包移动快捷支付
	 */
	public final static String CHANNEL_BFB="bfb";	
	/**
	 * 百度钱包手机网页支付
	 */
	public final static String CHANNEL_BFB_WAP="bfb_wap";	
	/**
	 * 易宝手机网页支付
	 */
	public final static String CHANNEL_YEEPAY_WAP="yeepay_wap";	
	/**
	 * 京东手机网页支付
	 */
	public final static String CHANNEL_JDPAY_WAP="jdpay_wap";	
	/**
	 * 应用内快捷支付（银联）
	 */
	public final static String CHANNEL_CNP_U="cnp_u";	
	/**
	 * 应用内快捷支付（外卡）
	 */
	public final static String CHANNEL_CNP_F="cnp_f";	
	/**
	 * 分期乐支付
	 */
	public final static String CHANNEL_FQLPAY_WAP="fqlpay_wap";	
	/**
	 * 量化派支付
	 */
	public final static String CHANNEL_QGBC_WAP="qgbc_wap";	
	

	private Integer id;	//自动编号
	private String channel;	//支付方式
	private Integer addtime;	//支付时间，10位时间戳
	private Double money;		//支付金额，单位：元
	private String orderno;		//支付订单号，可以关联到订单的订单号
	private Integer userid;		//支付的人的用户id，user.id ，是谁支付的


    @Id 
    @GeneratedValue(strategy = IDENTITY)
    @Column(name="id", unique=true, nullable=false)
    public Integer getId() {
        return this.id;
    }
    
    public void setId(Integer id) {
        this.id = id;
    }
    
    @Column(name="channel", length=10)
    public String getChannel() {
        return this.channel;
    }
    
    public void setChannel(String channel) {
        this.channel = channel;
    }
    
    @Column(name="addtime")
    public Integer getAddtime() {
        return this.addtime;
    }
    
    public void setAddtime(Integer addtime) {
        this.addtime = addtime;
    }
    
    @Column(name = "money", columnDefinition="double(8,2) comment '支付金额，单位是元'")
    public Double getMoney() {
        return this.money;
    }
    
    public void setMoney(Double money) {
        this.money = money;
    }
    
    @Column(name="orderno", length=12)
    public String getOrderno() {
        return this.orderno;
    }
    
    public void setOrderno(String orderno) {
        this.orderno = orderno;
    }

	public Integer getUserid() {
		return userid;
	}

	public void setUserid(Integer userid) {
		this.userid = userid;
	}


}