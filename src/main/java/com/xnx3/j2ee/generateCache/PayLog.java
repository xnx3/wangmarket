package com.xnx3.j2ee.generateCache;

import org.springframework.stereotype.Component;

/**
 * ping++在线支付
 * @author 管雷鸣
 */
@Component
public class PayLog extends BaseGenerate {
	public PayLog() {
		channel();
	}
	
	public void channel(){
		createCacheObject("channel");
		
		cacheAdd(com.xnx3.j2ee.entity.PayLog.CHANNEL_ALIPAY, "支付宝手机支付");
		cacheAdd(com.xnx3.j2ee.entity.PayLog.CHANNEL_ALIPAY_WAP, "支付宝手机网页支付");
		cacheAdd(com.xnx3.j2ee.entity.PayLog.CHANNEL_ALIPAY_QR, "支付宝扫码支付");
		cacheAdd(com.xnx3.j2ee.entity.PayLog.CHANNEL_ALIPAY_PC_DIRECT, "支付宝PC网页支付");
		cacheAdd(com.xnx3.j2ee.entity.PayLog.CHANNEL_UPACP, "银联全渠道支付");
		cacheAdd(com.xnx3.j2ee.entity.PayLog.CHANNEL_UPACP_WAP, "银联全渠道手机网页支付");
		cacheAdd(com.xnx3.j2ee.entity.PayLog.CHANNEL_UPACP_PC, "银联PC网页支付");
		cacheAdd(com.xnx3.j2ee.entity.PayLog.CHANNEL_CP_B2B, "银联企业网银支付");
		cacheAdd(com.xnx3.j2ee.entity.PayLog.CHANNEL_WX, "微信支付");
		cacheAdd(com.xnx3.j2ee.entity.PayLog.CHANNEL_WX_PUB, "微信公众账号支付");
		cacheAdd(com.xnx3.j2ee.entity.PayLog.CHANNEL_WX_PUB_QR, "微信公众账号扫码支付");
		cacheAdd(com.xnx3.j2ee.entity.PayLog.CHANNEL_APPLEPAY_UPACP, "Apple Pay");
		cacheAdd(com.xnx3.j2ee.entity.PayLog.CHANNEL_BFB, "百度钱包移动快捷支付");
		cacheAdd(com.xnx3.j2ee.entity.PayLog.CHANNEL_BFB_WAP, "百度钱包手机网页支付");
		cacheAdd(com.xnx3.j2ee.entity.PayLog.CHANNEL_YEEPAY_WAP, "易宝手机网页支付");
		cacheAdd(com.xnx3.j2ee.entity.PayLog.CHANNEL_JDPAY_WAP, "京东手机网页支付");
		cacheAdd(com.xnx3.j2ee.entity.PayLog.CHANNEL_CNP_U, "应用内快捷支付（银联）");
		cacheAdd(com.xnx3.j2ee.entity.PayLog.CHANNEL_CNP_F, "应用内快捷支付（外卡）");
		cacheAdd(com.xnx3.j2ee.entity.PayLog.CHANNEL_FQLPAY_WAP, "分期乐支付");
		cacheAdd(com.xnx3.j2ee.entity.PayLog.CHANNEL_QGBC_WAP, "量化派支付");
		
		generateCacheFile();
	}
	
}
