package com.xnx3.j2ee.func;

import javax.servlet.http.HttpServletRequest;
import com.aliyun.openservices.log.common.LogItem;
import com.aliyun.openservices.log.exception.LogException;
import com.xnx3.j2ee.Global;
import com.xnx3.j2ee.entity.User;
import com.xnx3.j2ee.shiro.ShiroFunc;
import com.xnx3.j2ee.util.ActionLogUtil;
import com.xnx3.j2ee.util.IpUtil;
import com.xnx3.net.AliyunLogUtil;

/**
 * 会员动作日志的缓存及使用。
 * 有其他日志需要记录，可以参考这个类。可吧这个类复制出来，在此基础上进行修改
 * @author 管雷鸣
 * @deprecated 已废弃！！ 请使用 {@link ActionLogUtil}
 */
public class ActionLogCache extends ActionLogUtil{
}
