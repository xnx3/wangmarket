/*
 Navicat Premium Data Transfer

 Source Server         : localhost
 Source Server Type    : MySQL
 Source Server Version : 50623
 Source Host           : localhost
 Source Database       : wangmarket_ori

 Target Server Type    : MySQL
 Target Server Version : 50623
 File Encoding         : utf-8

 Date: 03/28/2019 17:18:40 PM
*/

SET NAMES utf8;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
--  Table structure for `agency`
-- ----------------------------
DROP TABLE IF EXISTS `agency`;
CREATE TABLE `agency` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` char(38) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '人名，公司名',
  `phone` char(20) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '代理的联系电话',
  `userid` int(11) DEFAULT NULL COMMENT '对应user.id',
  `reg_oss_have` int(11) DEFAULT NULL COMMENT '其客户注册成功后，会员所拥有的免费OSS空间',
  `oss_price` int(11) DEFAULT '1' COMMENT 'OSS空间的售价，单位是毛， 时间是年。  如填写30，则为每10M空间3元每年',
  `address` char(80) COLLATE utf8_unicode_ci DEFAULT '' COMMENT '办公地址',
  `qq` char(13) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '代理商的QQ',
  `site_size` int(11) DEFAULT '0' COMMENT '站点数量，站点余额。1个对应着一个网站/年',
  `version` int(11) DEFAULT '0' COMMENT '乐观锁',
  `parent_id` int(11) DEFAULT '0' COMMENT '推荐人id，父级代理的agency.id。若父级代理是总管理，则为0',
  `addtime` int(11) DEFAULT '0' COMMENT '开通时间',
  `expiretime` int(11) DEFAULT '0' COMMENT '到期时间。按年进行续费（站币）',
  `state` tinyint(2) DEFAULT '1' COMMENT '代理状态，1正常；2冻结',
  PRIMARY KEY (`id`),
  KEY `userid` (`userid`,`parent_id`,`expiretime`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=52 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci ROW_FORMAT=DYNAMIC COMMENT='代理商信息';

-- ----------------------------
--  Records of `agency`
-- ----------------------------
BEGIN;
INSERT INTO `agency` VALUES ('51', '管雷鸣', '17000000001', '392', '1024', '120', '山东潍坊', '921153866', '99999999', '0', '0', '1512818402', '2143123200', '1');
COMMIT;

-- ----------------------------
--  Table structure for `agency_data`
-- ----------------------------
DROP TABLE IF EXISTS `agency_data`;
CREATE TABLE `agency_data` (
  `id` int(11) DEFAULT NULL COMMENT '对应 agency.id',
  `notice` text COLLATE utf8_unicode_ci COMMENT '代理的公告信息，显示给下级用户看的'
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci COMMENT='代理表的变长字段表，存储代理的公告等';

-- ----------------------------
--  Table structure for `area`
-- ----------------------------
DROP TABLE IF EXISTS `area`;
CREATE TABLE `area` (
  `id` int(11) NOT NULL,
  `province` char(20) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '省份',
  `city` char(30) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '城市',
  `district` char(40) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '区',
  `longitude` float(5,2) DEFAULT NULL COMMENT '经度',
  `latitude` float(5,2) DEFAULT NULL COMMENT '纬度',
  PRIMARY KEY (`id`),
  KEY `province` (`province`,`city`,`district`,`longitude`,`latitude`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci ROW_FORMAT=DYNAMIC COMMENT='地区，省市区，地理城市表（暂未用到，预留）';

-- ----------------------------
--  Table structure for `carousel`
-- ----------------------------
DROP TABLE IF EXISTS `carousel`;
CREATE TABLE `carousel` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `url` char(120) COLLATE utf8_unicode_ci DEFAULT '' COMMENT '点击跳转的目标url',
  `addtime` int(11) DEFAULT NULL COMMENT '添加时间',
  `isshow` tinyint(2) DEFAULT '1' COMMENT '是否显示，1为显示，0为不显示',
  `rank` int(11) DEFAULT '0' COMMENT '排序，数小越靠前',
  `siteid` int(11) DEFAULT NULL COMMENT '轮播图属于哪个站点，对应site.id',
  `userid` int(11) DEFAULT NULL COMMENT '轮播图属于哪个用户建立的，对应user.id',
  `image` char(120) COLLATE utf8_unicode_ci DEFAULT '' COMMENT '轮播图的url，分两种，一种只是文件名，如asd.png  另一种为绝对路径',
  `type` tinyint(2) DEFAULT '1' COMMENT '类型，默认1:内页通用的头部图(有的模版首页也用)；2:只有首页顶部才会使用的图',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=252 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci ROW_FORMAT=DYNAMIC COMMENT='网站轮播图，手机、电脑模式网站用到。现主要做CMS类型网站，CMS模式网站这个是用不到的。';

-- ----------------------------
--  Table structure for `collect`
-- ----------------------------
DROP TABLE IF EXISTS `collect`;
CREATE TABLE `collect` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `userid` int(11) DEFAULT NULL COMMENT '用户id，发起方的user.id，我的id，发起关注的人',
  `othersid` int(11) DEFAULT NULL COMMENT 'userid这个人关注的其他用户，被关注人',
  `addtime` int(11) DEFAULT NULL COMMENT '添加时间',
  PRIMARY KEY (`id`),
  KEY `userid` (`userid`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci ROW_FORMAT=DYNAMIC COMMENT='用户关注表，用户可互相关注，（暂未用到，预留）';

-- ----------------------------
--  Table structure for `exchange`
-- ----------------------------
DROP TABLE IF EXISTS `exchange`;
CREATE TABLE `exchange` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `type` char(5) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '兑换类型，如空间、代理、域名等',
  `goodsid` int(11) DEFAULT NULL COMMENT '兑换商品的编号,goods.id',
  `addtime` int(11) DEFAULT NULL COMMENT '提交时间',
  `userid` int(11) DEFAULT NULL COMMENT '申请兑换的用户id',
  `siteid` int(11) DEFAULT NULL COMMENT '申请兑换的用户的所属网站',
  `user_remark` char(30) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '用户提交的备注信息，备注说明',
  `status` tinyint(2) DEFAULT NULL COMMENT '当前的状态，2:申请中； 3:已兑换完毕；4拒绝兑换',
  `kefu_remark` char(30) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '客服备注',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci COMMENT='积分兑换的兑换申请列表。（暂未用到，预留）';

-- ----------------------------
--  Table structure for `feedback`
-- ----------------------------
DROP TABLE IF EXISTS `feedback`;
CREATE TABLE `feedback` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `addtime` int(11) DEFAULT '0' COMMENT '添加时间',
  `userid` int(11) DEFAULT NULL COMMENT '哪个用户提出的',
  `text` varchar(1000) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '问题反馈的反馈内容',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=17 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci COMMENT='问题反馈（暂未用到，已废弃）';

-- ----------------------------
--  Table structure for `form`
-- ----------------------------
DROP TABLE IF EXISTS `form`;
CREATE TABLE `form` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '自动编号',
  `siteid` int(11) DEFAULT NULL COMMENT '当前反馈的信息是属于那个站点',
  `addtime` int(11) DEFAULT NULL COMMENT '信息发布时间',
  `state` tinyint(2) DEFAULT NULL COMMENT '状态。 1：已读； 0：未读，默认为0',
  `title` char(50) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '在后台反馈列表显示的标题',
  PRIMARY KEY (`id`),
  KEY `siteid` (`siteid`,`state`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci COMMENT='万能表单（formManage）的信息反馈记录表';

-- ----------------------------
--  Table structure for `form_data`
-- ----------------------------
DROP TABLE IF EXISTS `form_data`;
CREATE TABLE `form_data` (
  `id` int(11) NOT NULL COMMENT 'form的分表，对应 form.id',
  `text` text COLLATE utf8_unicode_ci COMMENT '自定义表单的内容，格式为json字符串',
  PRIMARY KEY (`id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci COMMENT='form表的分表';

-- ----------------------------
--  Table structure for `friend`
-- ----------------------------
DROP TABLE IF EXISTS `friend`;
CREATE TABLE `friend` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT COMMENT '自动编号',
  `self` int(10) unsigned NOT NULL COMMENT '自己，例如QQ登录后，这个QQ的账号.此处关联user.id',
  `other` int(10) unsigned NOT NULL COMMENT '自己，例如QQ登录后，这个QQ的好友的账号,此处关联user.id',
  PRIMARY KEY (`id`),
  KEY `self` (`self`),
  KEY `other` (`other`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci ROW_FORMAT=DYNAMIC COMMENT='好友关系表，可以互相加好友。（暂未用到，预留）';

-- ----------------------------
--  Table structure for `friend_log`
-- ----------------------------
DROP TABLE IF EXISTS `friend_log`;
CREATE TABLE `friend_log` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT COMMENT '对应friend.id',
  `self` int(10) unsigned NOT NULL COMMENT '主动添加请求的user.id',
  `other` int(10) unsigned NOT NULL COMMENT '被动接受好友添加的user.id',
  `time` int(10) unsigned NOT NULL COMMENT '执行此操作当前的时间，UNIX时间戳',
  `state` tinyint(3) unsigned NOT NULL COMMENT '1：主动发出添加好友申请;2：被动者接受或者拒绝好友申请;3：删除好友',
  `ip` char(15) COLLATE utf8_unicode_ci NOT NULL COMMENT '当前操作者的ip地址',
  PRIMARY KEY (`id`),
  KEY `self` (`self`,`other`,`time`,`state`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci ROW_FORMAT=DYNAMIC COMMENT='好友操作日志记录';

-- ----------------------------
--  Table structure for `goods`
-- ----------------------------
DROP TABLE IF EXISTS `goods`;
CREATE TABLE `goods` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `money` int(11) DEFAULT NULL COMMENT '积分、money， 同user.money',
  `explain` char(100) CHARACTER SET utf8 COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '说明',
  `deadline` char(10) CHARACTER SET utf8 COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '有效期，期限，如1年、1次',
  `type` char(5) CHARACTER SET utf8 COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '所属分类，如域名、空间等',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC COMMENT='积分兑换的可兑换商品表（暂未用到，预留）';

-- ----------------------------
--  Records of `goods`
-- ----------------------------
BEGIN;
INSERT INTO `goods` VALUES ('1', '400', '.top  .bin 后缀的顶级域名任选一个', '1年', '域名'), ('2', '6000', '.com  .cn 后缀的顶级域名任选一个', '1年', '域名'), ('3', '2000', '在您原有的附件存储空间基础上，增加100MB', '永久', '空间'), ('4', '20000', '在您原有的附件存储空间基础上，增加1000MB', '永久', '空间'), ('5', '1000', '普通代理。拥有代理后台，可以在代理后台开通任意数量的网站。其建立的网站可以送人，但不可售卖，不可用于商业用途', '1年', '代理资格'), ('6', '200000', '商用代理，同普通代理，其建立的网站允许对外出售，允许其用于商业用途', '1年', '代理资格'), ('7', '1000', '如果您想将网站独立出去，放到自己的服务器或者FTP上，我们可以吧您网站的源代码(html文件)、图片、附件等打包给你，直接上传就可以开通访问', '1次', '网站迁移'), ('8', '500000', '苹果 Apple iPhone7 4G手机 全网通(32G)', '1台', '手机');
COMMIT;

-- ----------------------------
--  Table structure for `im`
-- ----------------------------
DROP TABLE IF EXISTS `im`;
CREATE TABLE `im` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `siteid` int(11) DEFAULT NULL,
  `auto_reply` char(200) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '无客服在线时，自动回复的文字。若为空，或者空字符串，则不开启此功能',
  `use_off_line_email` tinyint(1) DEFAULT '0' COMMENT '是否使用离线时的邮件通知提醒，默认为0，不启用。  1为启用',
  `email` char(40) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '邮件内容，若上面开启邮件提醒通知了，那么这里是必须要填写的，不然是要通知谁',
  `userid` int(11) DEFAULT '0' COMMENT '此条是属于哪个用户。im应用取便是通过这个来的',
  `use_kefu` smallint(6) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `siteid` (`siteid`,`userid`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci ROW_FORMAT=DYNAMIC COMMENT='网站在线客服设置';

-- ----------------------------
--  Records of `im`
-- ----------------------------
BEGIN;
INSERT INTO `im` VALUES ('8', '0', '亲，我现在不在，你可以加我QQ921153866', '0', null, '0', null), ('9', '0', '亲，我现在不在线，你可以先加我QQ921153866，或者直接在这里留言', '0', null, '0', null), ('10', '219', '抱歉，我现在不在，你可以先留下联系方式，我一会回来联系您，', '1', '9211538@qq.com', '243', '1'), ('11', null, null, '0', 'sd', '373', null), ('12', null, '哈喽，我现在不在，你可以先关注微信公众号wangmarket', '0', null, '326', '1');
COMMIT;

-- ----------------------------
--  Table structure for `input_model`
-- ----------------------------
DROP TABLE IF EXISTS `input_model`;
CREATE TABLE `input_model` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `siteid` int(11) DEFAULT '0' COMMENT '对应site.id',
  `text` varchar(20000) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '输入模型的内容',
  `remark` varchar(30) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '备注说明',
  `code_name` char(30) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '模型代码，每个网站的模型代码是唯一的',
  PRIMARY KEY (`id`),
  KEY `siteid` (`siteid`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci ROW_FORMAT=DYNAMIC COMMENT='输入模型';

-- ----------------------------
--  Table structure for `log`
-- ----------------------------
DROP TABLE IF EXISTS `log`;
CREATE TABLE `log` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `addtime` int(11) DEFAULT NULL,
  `goalid` int(11) DEFAULT NULL,
  `isdelete` smallint(6) NOT NULL,
  `type` int(11) DEFAULT NULL,
  `userid` int(11) DEFAULT NULL,
  `value` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci COMMENT='用户动作日志（暂未用到，已废弃，由阿里云日志服务取代）';

-- ----------------------------
--  Table structure for `message`
-- ----------------------------
DROP TABLE IF EXISTS `message`;
CREATE TABLE `message` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `senderid` int(11) unsigned NOT NULL COMMENT '自己的userid，发信人的userid，若是为0则为系统信息',
  `recipientid` int(11) unsigned NOT NULL COMMENT '给谁发信，这就是谁的userid，目标用户的userid，收信人id',
  `time` int(11) unsigned NOT NULL COMMENT '此信息的发送时间',
  `state` tinyint(3) unsigned NOT NULL COMMENT '0:未读，1:已读 ，2已删除',
  `isdelete` tinyint(2) NOT NULL DEFAULT '0' COMMENT '是否已经被删除。0正常，1已删除，',
  PRIMARY KEY (`id`),
  KEY `self` (`senderid`,`recipientid`,`time`,`state`,`isdelete`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=45 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci ROW_FORMAT=DYNAMIC COMMENT='发信箱，发信，用户发信表，站内信。（暂未用到，预留）';

-- ----------------------------
--  Table structure for `message_data`
-- ----------------------------
DROP TABLE IF EXISTS `message_data`;
CREATE TABLE `message_data` (
  `id` int(11) unsigned NOT NULL COMMENT '对应message.id',
  `content` varchar(400) COLLATE utf8_unicode_ci NOT NULL COMMENT '内容',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci ROW_FORMAT=DYNAMIC COMMENT='对应message，存储内容';

-- ----------------------------
--  Table structure for `news`
-- ----------------------------
DROP TABLE IF EXISTS `news`;
CREATE TABLE `news` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `userid` int(11) DEFAULT '0' COMMENT '对应user.id，是哪个用户发表的',
  `addtime` int(11) DEFAULT '0' COMMENT '发布时间',
  `title` char(60) COLLATE utf8_unicode_ci DEFAULT NULL,
  `titlepic` char(100) COLLATE utf8_unicode_ci DEFAULT '' COMMENT '头图',
  `intro` char(160) COLLATE utf8_unicode_ci DEFAULT '' COMMENT '简介,从内容正文里自动剪切出开始的160个汉字',
  `sharenum` int(11) NOT NULL DEFAULT '0' COMMENT '分享的总数量',
  `supportnum` int(11) NOT NULL DEFAULT '0' COMMENT '支持的总数量',
  `opposenum` int(11) DEFAULT '0' COMMENT '反对的总数量',
  `readnum` int(11) DEFAULT '0' COMMENT '阅读的总数量',
  `type` tinyint(2) DEFAULT '0' COMMENT '1新闻；2图文；3独立页面',
  `status` tinyint(2) DEFAULT '1' COMMENT '1：正常显示；2：隐藏不显示',
  `commentnum` int(11) DEFAULT '0' COMMENT '评论的总数量',
  `cid` int(11) DEFAULT '0' COMMENT '所属栏目id，对应site_column.id',
  `siteid` int(11) DEFAULT '0' COMMENT '所属站点，对应site.id',
  `legitimate` tinyint(2) DEFAULT '1' COMMENT '是否是合法的，1是，0不是，涉嫌',
  `reserve1` char(10) COLLATE utf8_unicode_ci DEFAULT '' COMMENT '预留字段，用户可使用输入模型来进行扩展',
  `reserve2` char(10) COLLATE utf8_unicode_ci DEFAULT '' COMMENT '预留字段，用户可使用输入模型来进行扩展',
  PRIMARY KEY (`id`,`supportnum`,`sharenum`),
  KEY `userid` (`userid`,`type`,`supportnum`,`readnum`,`commentnum`,`cid`,`status`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=2341 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci ROW_FORMAT=DYNAMIC COMMENT='网站的新闻、产品、独立页面等，都是存储在这';

-- ----------------------------
--  Table structure for `news_comment`
-- ----------------------------
DROP TABLE IF EXISTS `news_comment`;
CREATE TABLE `news_comment` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `newsid` int(11) DEFAULT NULL COMMENT '关联news.id',
  `userid` int(11) DEFAULT NULL COMMENT '关联user.id，评论用户的id',
  `addtime` int(11) DEFAULT NULL COMMENT '添加时间',
  `text` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '评论内容',
  PRIMARY KEY (`id`),
  KEY `newsid` (`newsid`,`userid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci ROW_FORMAT=DYNAMIC COMMENT='news的评论（暂未用到，预留）';

-- ----------------------------
--  Table structure for `news_data`
-- ----------------------------
DROP TABLE IF EXISTS `news_data`;
CREATE TABLE `news_data` (
  `id` int(11) NOT NULL COMMENT '对应news.id',
  `text` mediumtext COLLATE utf8_unicode_ci COMMENT '信息内容',
  `extend` text COLLATE utf8_unicode_ci COMMENT '其他扩展字段，以json形式存在',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci ROW_FORMAT=DYNAMIC COMMENT='news内容分表';

-- ----------------------------
--  Table structure for `pay_log`
-- ----------------------------
DROP TABLE IF EXISTS `pay_log`;
CREATE TABLE `pay_log` (
  `id` int(111) NOT NULL AUTO_INCREMENT,
  `channel` char(10) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '支付通道，alipay：支付宝； wx：微信，同ping++的channel，若中间有下划线，去掉下划线。',
  `addtime` int(11) DEFAULT NULL COMMENT '支付时间',
  `money` float(6,2) DEFAULT NULL COMMENT '付款金额，单位：元',
  `orderno` char(12) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '订单号，2个随机数＋10位linux时间戳',
  `userid` int(11) DEFAULT NULL COMMENT '支付的用户，关联user.id',
  PRIMARY KEY (`id`),
  KEY `orderno` (`orderno`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci ROW_FORMAT=DYNAMIC COMMENT='支付成功的日志记录（暂未用到，预留）';

-- ----------------------------
--  Table structure for `permission`
-- ----------------------------
DROP TABLE IF EXISTS `permission`;
CREATE TABLE `permission` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `description` char(100) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '描述信息，备注，只是给后台设置权限的人看的',
  `url` char(80) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '资源url',
  `name` char(50) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '名字，菜单的名字，显示给用户的',
  `parent_id` int(11) DEFAULT NULL COMMENT '上级资源的id',
  `percode` char(80) COLLATE utf8_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `url` (`url`,`name`,`percode`),
  KEY `parent_id` (`parent_id`)
) ENGINE=InnoDB AUTO_INCREMENT=120 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci ROW_FORMAT=DYNAMIC COMMENT='Shiro权限管理中的资源';

-- ----------------------------
--  Records of `permission`
-- ----------------------------
BEGIN;
INSERT INTO `permission` VALUES ('1', '用户前台用户的个人中心', '/user/info.do', '个人中心', '0', 'user'), ('2', '用户前台用户的个人中心', '/user/info.do', '我的信息', '1', 'userInfo'), ('3', '前端用户在论坛发表帖子', '/bbs/addPost.do', '发帖', '4', 'bbsAddPost'), ('4', '前端，论坛', '/bbs/list.do', '论坛', '0', 'bbs'), ('7', '用户端的我的好友', '/friend/list.do', '我的好友', '0', 'friend'), ('9', '前端的我的好友，添加好友', '/friend/add.do', '添加好友', '7', 'friendAdd'), ('10', '前端，我的好友，好友列表', '/friend/list.do', '好友列表', '7', 'friendList'), ('11', '前端的我的好友，删除好友', '/friend/delete.do', '删除好友', '7', 'friendDelete'), ('12', '后台的用户管理', '/admin/user/list.do', '用户管理', '0', 'adminUser'), ('13', '后台用户管理下的菜单', '/admin/user/list.do', '用户列表', '12', 'adminUserList'), ('14', '后台用户管理下的菜单', '/admin/user/delete.do', '删除用户', '12', 'adminUserDelete'), ('15', '管理后台－系统管理栏目', '/admin/system/index.do', '系统管理', '0', 'adminSystem'), ('16', '管理后台－系统管理－系统参数、系统变量', '/admin/system/variableList.do', '全局变量', '15', 'adminSystemVariable'), ('17', '管理后台－系统管理，刷新所有缓存', '/admin/system/generateAllCache.do', '刷新缓存', '15', 'adminSystemGenerateAllCache'), ('18', '前端，用户中心，注销登录', '/user/logout.do', '注销', '1', 'userLogout'), ('19', '前端，用户中心，更改头像', '/user/uploadHead.do', '更改头像', '1', 'userUploadHead'), ('20', '前端，用户中心，更改用户自己的昵称', '/user/updateNickName.do', '更改昵称', '1', 'userUpdateNickName'), ('21', '前端，用户中心，更改密码', '/user/updatePassword.do', '更改密码', '1', 'userUpdatePassword'), ('22', '前端，用户中心，邮件邀请用户注册', '/user/inviteEmail.do', '邮件邀请注册', '1', 'userInviteEmail'), ('23', '前端，好友中心，首页', '/friend/index.do', '首页', '7', 'friendIndex'), ('24', '前端，站内信', '/message/list.do', '信息', '0', 'message'), ('25', '前端，站内信，信息列表', '/message/list.do', '信息列表', '24', 'messageList'), ('26', '前端，站内信，阅读信息', '/message/view.do', '阅读信息', '24', 'messageView'), ('27', '前端，站内信，发送信息', '/message/send.do', '发送信息', '24', 'messageSend'), ('28', '前端，论坛，帖子列表', '/bbs/list.do', '帖子列表', '4', 'bbsList'), ('29', '前端，论坛，帖子详情', '/bbs/view.do', '帖子详情', '4', 'bbsView'), ('30', '前端，论坛，回复帖子', '/bbs/addComment.do', '回复帖子', '4', 'bbsAddComment'), ('31', '前端，项目组', '/project/index.do', '项目组', '0', 'project'), ('32', '前端，项目组，项目列表', '/project/list.do', '列表', '31', 'projectList'), ('33', '前端，项目组，项目详情', '/project/view.do', '项目详情', '31', 'projectView'), ('34', '前端，项目组，添加项目', '/project/add.do', '添加项目', '31', 'projectAdd'), ('35', '前端，项目组，删除项目', '/project/delete.do', '删除项目', '31', 'projectDelete'), ('36', '前端，项目组，列出属于自己管理的项目列表', '/project/adminList.do', '管理项目列表', '31', 'projectAdminList'), ('37', '前端，项目组，添加成员到项目组', '/project/addMember.do', '添加成员到项目组', '31', 'projectAddMember'), ('38', '前端，项目组，获取项目组用户列表', '/project/memberList.do', '获取项目组用户列表', '31', 'projectMemberList'), ('39', '前端，图表（线框图，流程图等）', '/paint/index.do', '图表', '0', 'paint'), ('40', '前端，图表，图表首页', '/paint/index.do', '首页', '39', 'paintIndex'), ('41', '前端，图表，添加图表（流程、线框图）', '/paint/add.do', '添加', '39', 'paintAdd'), ('42', '前端，图表，评论图表（流程、线框图）', '/paint/addComment.do', '评论图表', '39', 'paintAddComment'), ('43', '前端，图表，该图（流程、线框图）的评论列表', '/paint/commentList.do', '该图评论列表', '39', 'paintCommentList'), ('44', '后台，权限管理', '/admin/role/roleList.do', '权限管理', '0', 'adminRole'), ('46', '后台，权限管理，新增、编辑角色', '/admin/role/editRole.do', '编辑角色', '44', 'adminRoleRole'), ('48', '后台，权限管理，角色列表', '/admin/role/roleList.do', '角色列表', '44', 'adminRoleRoleList'), ('49', '后台，权限管理，删除角色', '/admin/role/deleteRole.do', '删除角色', '44', 'adminRoleDeleteRole'), ('51', '后台，权限管理，资源Permission的添加、编辑功能', '/admin/role/editPermission.do', '编辑资源', '44', 'adminRolePermission'), ('53', '后台，权限管理，资源Permission列表', '/admin/role/permissionList.do', '资源列表', '44', 'adminRolePermissionList'), ('54', '后台，权限管理，删除资源Permission', '/admin/role/deletePermission.do', '删除资源', '44', 'adminRoleDeletePermission'), ('55', '后台，权限管理，编辑角色下资源', '/admin/role/editRolePermission.do', '编辑角色下资源', '44', 'adminRoleEditRolePermission'), ('56', '后台，权限管理，编辑用户所属角色', '/admin/role/editUserRole.do', '编辑用户所属角色', '44', 'adminRoleEditUserRole'), ('57', '后台，论坛管理', '/admin/bbs/postList.do', '论坛管理', '0', 'adminBbs'), ('58', '后台，论坛管理，帖子列表', '/admin/bbs/postList.do', '帖子列表', '57', 'adminBbsPostList'), ('59', '后台，论坛管理，删除帖子', '/admin/bbs/deletePost.do', '删除帖子', '57', 'adminBbsDeletePost'), ('60', '后台，论坛管理，添加板块', '/admin/bbs/addClass.do', '添加板块', '57', 'adminBbsAddClass'), ('61', '后台，论坛管理，添加，修改板块', '/admin/bbs/editClass.do', '修改板块', '57', 'adminBbsClass'), ('63', '后台，论坛管理，板块列表', '/admin/bbs/classList.do', '板块列表', '57', 'adminBbsClassList'), ('64', '后台，论坛管理，删除板块', '/admin/bbs/deleteClass.do', '删除板块', '57', 'adminBbsDeleteClass'), ('65', '后台，站内信消息管理', '/admin/message/list.do', '消息管理', '0', 'adminMessage'), ('66', '后台，站内信消息管理，消息列表', '/admin/message/list.do', '消息列表', '65', 'adminMessageList'), ('67', '后台，站内信消息管理，删除消息', '/admin/message/delete.do', '删除消息', '65', 'adminMessageDelete'), ('68', '后台，系统设置，用户注册后自动拥有的一个权限', '/admin/system/userRegRole.do', '注册用户权限', '15', 'adminSystemUserRegRole'), ('71', '后台，日志管理', '/admin/log/list.do', '日志管理', '0', 'adminLog'), ('72', '后台，日志管理，日志列表', '/admin/log/list.do', '日志列表', '71', 'adminLogList'), ('74', '管理后台－系统管理，新增、修改系统的全局变量', '/admin/system/variable.do', '修改全局变量', '15', 'adminSystemVariable'), ('75', '邀请注册页面，介绍说明页面', '/user/invite.do', '邀请注册页面', '1', 'userInvite'), ('77', '后台，论坛管理，帖子编辑、添加', '/admin/bbs/post.do', '添加修改帖子', '57', 'adminBbsPost'), ('78', '后台，论坛管理，删除帖子回复', '/admin/bbs/deleteComment.do', '删除回帖', '57', 'adminBbsDeletePostComment'), ('79', '后台，论坛管理，回帖列表', '/admin/bbs/commentList.do', '回帖列表', '57', 'adminBbsPostCommentList'), ('80', '后台，用户管理，查看用户详情', '/admin/user/view.do', '用户详情', '12', 'adminUserView'), ('81', '后台，用户管理，冻结、解除冻结会员', '/admin/user/updateFreeze.do', '冻结会员', '12', 'adminUserUpdateFreeze'), ('82', '后台，历史发送的短信验证码', '/admin/smslog/list.do', '验证码管理', '0', 'adminSmsLog'), ('83', '后台，历史发送的短信验证码列表', '/admin/smslog/list.do', '验证码列表', '82', 'adminSmsLogList'), ('86', '后台，在线支付记录', '/admin/payLog/list.do', '支付记录', '0', 'adminPayLog'), ('87', '后台，在线支付记录，记录列表', '/admin/payLog/list.do', '支付列表', '86', 'adminPayLogList'), ('88', '建站代理', '', '建站代理', '0', 'agencyIndex'), ('89', '建站代理，代理商后台首页', '', '首页', '88', 'agencyIndex'), ('90', '建站代理，代理商会员站点列表', '', '会员站点列表', '88', 'agencyUserList'), ('91', '建站代理，添加用户站点', '', '添加用户站点', '88', 'agencyAdd'), ('92', '信息文章相关操作', '', '文章管理', '0', 'adminNews'), ('93', 'News数据表，信息列表', '', '信息列表', '92', 'adminNewsList'), ('94', 'News数据表，信息详情', '', '信息详情', '92', 'adminNewsView'), ('95', 'News数据表，删除信息', '', '删除信息', '92', 'adminNewsDelete'), ('96', 'News数据表，合法性改为合法状态', '', '改为合法', '92', 'adminNewsCancelLegitimate'), ('97', '建站代理，代理商后台，开通其下级普通代理', '', '开通普通代理', '88', 'AgencyNormalAdd'), ('98', '查看当前在线的会员', '', '在线会员', '12', 'adminOnlineUserList'), ('99', '总管理后台的网站管理', '', '全部网站管理', '0', 'adminSite'), ('100', '网站列表', '', '网站列表', '99', 'adminSiteList'), ('101', '网站详情页面', '', '网站详情', '99', 'adminSiteView'), ('102', '添加一个网站跟用户', '', '添加网站', '99', 'adminSiteAdd'), ('103', '访问统计相关', '', '访问统计', '0', 'adminRequestLog'), ('104', '网站的访问情况', '', '访问统计', '103', 'adminRequestLogFangWen'), ('105', '操作的日志列表', '', '操作日志', '88', 'agencyActionLogList'), ('106', '资金变动日志', '', '资金日志', '88', 'agencySiteSizeLogList'), ('107', '我的下级代理商列表', '', '下级列表', '88', 'agencySubAgencyList'), ('108', '给我的下级代理充值站币', null, '站币充值', '88', 'agencyTransferSiteSizeToSubAgencyList'), ('109', '给我开通的网站续费延长使用时间', null, '网站续费', '88', 'agencySiteXuFie'), ('110', '给我下级的代理延长使用期限', '', '代理延期', '88', 'agencyYanQi'), ('111', '将我下级的代理冻结，暂停', '', '冻结代理', '88', 'agencyAgencyFreeze'), ('112', '将我下级的代理接触冻结，恢复正常', '', '解冻代理', '88', 'agencyAgencyUnFreeze'), ('113', '修改站点、代理帐户的密码', '', '修改密码', '88', 'agencySiteUpdatePassword'), ('114', '后台管理首页，登录后台的话，需要授权此项，不然登录成功后仍然无法进入后台，被此页给拦截了', null, '管理后台', '0', 'adminIndex'), ('115', '管理后台首页', '', '后台首页', '114', 'adminIndexIndex'), ('116', '删除系统变量', 'admin/system/deleteVariable.do', '删除变量', '15', 'adminSystemDeleteVariable'), ('117', '后台，日志管理，所有动作的日志图表', '/admin/log/cartogram.do', '统计图表', '71', 'adminLogCartogram'), ('118', '将自己直属下级的某个网站冻结', '', '冻结网站', '88', 'agencySiteFreeze'), ('119', '将自己直属下级的某个网站解除冻结', '', '网站解冻', '88', 'agencySiteFreeze');
COMMIT;

-- ----------------------------
--  Table structure for `role`
-- ----------------------------
DROP TABLE IF EXISTS `role`;
CREATE TABLE `role` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` char(30) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '角色名',
  `description` char(50) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '角色说明',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci ROW_FORMAT=DYNAMIC COMMENT='Shiro权限管理中的角色表';

-- ----------------------------
--  Records of `role`
-- ----------------------------
BEGIN;
INSERT INTO `role` VALUES ('1', '建站用户', '建立网站的用户'), ('2', '论坛用户', '用户网站自己的论坛用户'), ('9', '总管理', '总后台管理，超级管理员'), ('10', '代理', '商代理，可以开通子代理、网站');
COMMIT;

-- ----------------------------
--  Table structure for `role_permission`
-- ----------------------------
DROP TABLE IF EXISTS `role_permission`;
CREATE TABLE `role_permission` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `roleid` int(11) DEFAULT NULL COMMENT '角色id，role.id，一个角色可以拥有多个permission资源',
  `permissionid` int(11) DEFAULT NULL COMMENT '资源id，permission.id，一个角色可以拥有多个permission资源',
  PRIMARY KEY (`id`),
  KEY `roleid` (`roleid`)
) ENGINE=InnoDB AUTO_INCREMENT=212 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci ROW_FORMAT=DYNAMIC COMMENT='Shiro权限管理中，角色所拥有哪些资源的操作权限';

-- ----------------------------
--  Records of `role_permission`
-- ----------------------------
BEGIN;
INSERT INTO `role_permission` VALUES ('1', '9', '7'), ('4', '9', '4'), ('5', '9', '3'), ('6', '1', '1'), ('7', '1', '2'), ('8', '1', '4'), ('9', '1', '3'), ('10', '1', '7'), ('11', '1', '9'), ('12', '9', '12'), ('13', '9', '13'), ('14', '9', '1'), ('15', '9', '2'), ('16', '9', '9'), ('17', '9', '15'), ('18', '9', '16'), ('19', '9', '17'), ('20', '9', '18'), ('21', '9', '19'), ('23', '9', '21'), ('24', '9', '22'), ('25', '9', '28'), ('27', '9', '30'), ('28', '9', '11'), ('29', '9', '23'), ('30', '9', '14'), ('31', '9', '24'), ('32', '9', '26'), ('33', '9', '27'), ('34', '9', '31'), ('35', '9', '32'), ('36', '9', '33'), ('37', '9', '34'), ('38', '9', '35'), ('39', '9', '36'), ('40', '9', '37'), ('41', '9', '38'), ('47', '9', '68'), ('48', '9', '25'), ('49', '9', '44'), ('51', '9', '46'), ('53', '9', '48'), ('54', '9', '49'), ('56', '9', '51'), ('58', '9', '53'), ('59', '9', '54'), ('60', '9', '55'), ('61', '9', '56'), ('75', '9', '71'), ('76', '9', '72'), ('77', '9', '74'), ('78', '9', '10'), ('79', '9', '20'), ('80', '9', '75'), ('81', '9', '29'), ('85', '1', '18'), ('86', '1', '19'), ('87', '1', '20'), ('88', '1', '21'), ('89', '1', '22'), ('90', '1', '75'), ('91', '1', '28'), ('92', '1', '29'), ('93', '1', '30'), ('94', '1', '10'), ('95', '1', '11'), ('96', '1', '23'), ('97', '1', '24'), ('98', '1', '25'), ('99', '1', '26'), ('100', '1', '27'), ('101', '9', '80'), ('104', '9', '81'), ('109', '10', '1'), ('110', '10', '2'), ('111', '10', '18'), ('112', '10', '20'), ('113', '10', '21'), ('114', '10', '22'), ('115', '10', '75'), ('116', '10', '24'), ('117', '10', '25'), ('118', '10', '26'), ('119', '10', '27'), ('120', '10', '88'), ('121', '10', '89'), ('122', '10', '90'), ('123', '10', '91'), ('124', '11', '1'), ('125', '11', '2'), ('126', '11', '18'), ('127', '11', '19'), ('128', '11', '20'), ('129', '11', '21'), ('130', '11', '22'), ('131', '11', '75'), ('132', '11', '7'), ('133', '11', '9'), ('134', '11', '10'), ('135', '11', '11'), ('136', '11', '23'), ('137', '11', '24'), ('138', '11', '25'), ('139', '11', '26'), ('140', '11', '27'), ('141', '11', '88'), ('142', '11', '89'), ('143', '11', '90'), ('144', '11', '91'), ('145', '12', '1'), ('146', '12', '2'), ('147', '12', '18'), ('148', '12', '19'), ('149', '12', '20'), ('150', '12', '21'), ('151', '12', '22'), ('152', '12', '75'), ('153', '12', '88'), ('154', '12', '89'), ('155', '12', '90'), ('156', '12', '91'), ('157', '9', '92'), ('158', '9', '93'), ('159', '9', '94'), ('160', '9', '95'), ('161', '9', '96'), ('162', '12', '4'), ('163', '12', '3'), ('164', '12', '28'), ('165', '12', '29'), ('166', '12', '30'), ('167', '12', '7'), ('168', '12', '9'), ('169', '12', '10'), ('170', '12', '11'), ('171', '12', '23'), ('172', '12', '24'), ('173', '12', '25'), ('174', '12', '26'), ('175', '12', '27'), ('176', '10', '19'), ('177', '10', '4'), ('178', '10', '3'), ('179', '10', '28'), ('180', '10', '29'), ('181', '10', '30'), ('182', '10', '7'), ('183', '10', '9'), ('184', '10', '10'), ('185', '10', '11'), ('186', '10', '23'), ('187', '10', '97'), ('188', '9', '98'), ('189', '9', '99'), ('190', '9', '100'), ('191', '9', '101'), ('192', '9', '102'), ('193', '9', '103'), ('194', '9', '104'), ('195', '10', '105'), ('196', '10', '106'), ('197', '10', '107'), ('198', '10', '108'), ('199', '10', '109'), ('200', '10', '110'), ('201', '10', '111'), ('202', '10', '112'), ('203', '10', '113'), ('204', '9', '114'), ('205', '9', '115'), ('206', '10', '114'), ('207', '10', '115'), ('208', '9', '117'), ('209', '9', '116'), ('210', '10', '118'), ('211', '10', '119');
COMMIT;

-- ----------------------------
--  Table structure for `site`
-- ----------------------------
DROP TABLE IF EXISTS `site`;
CREATE TABLE `site` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` char(40) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '站点名字',
  `userid` int(11) DEFAULT '0' COMMENT '用户id，0为游客',
  `addtime` int(11) DEFAULT NULL COMMENT '创建时间',
  `m_show_banner` tinyint(1) DEFAULT '1' COMMENT '是否在首页显示Banner  1显示 0不显示',
  `phone` char(14) COLLATE utf8_unicode_ci DEFAULT '',
  `qq` char(12) COLLATE utf8_unicode_ci DEFAULT '',
  `template_id` int(11) DEFAULT '1' COMMENT '模版编号',
  `domain` char(30) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '二级域名',
  `about_us_cid` int(11) DEFAULT '0' COMMENT '网站关于我们介绍页面所对应的news.cid',
  `logo` char(80) COLLATE utf8_unicode_ci DEFAULT '' COMMENT 'PC端的LOGO',
  `client` tinyint(2) DEFAULT '2' COMMENT '客户端类型，1:PC，  2:WAP，3:CMS',
  `keywords` char(38) COLLATE utf8_unicode_ci DEFAULT '' COMMENT '搜索的关键词',
  `address` char(80) COLLATE utf8_unicode_ci DEFAULT '' COMMENT '地址（公司地址，办公地址，联系地址）',
  `username` char(10) COLLATE utf8_unicode_ci DEFAULT '' COMMENT '联系人姓名',
  `company_name` char(30) COLLATE utf8_unicode_ci DEFAULT '' COMMENT '公司名、工作室名字、团体名字',
  `bind_domain` char(30) CHARACTER SET utf8 DEFAULT NULL,
  `column_id` char(80) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '栏目id，每个id用英文逗号分割。这里主要是记录各个列表页面的栏目，如  ,12,23,  前后都要有,',
  `state` tinyint(2) DEFAULT '1' COMMENT '站点状态，1正常；2冻结',
  `contact_us_cid` int(11) DEFAULT '0' COMMENT '网站联系我们介绍页面所对应的news.cid',
  `div_template` char(15) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '自定义模版，位于/template/模版名字，修改HTML的形式',
  `template_name` char(20) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '自定义模版，位于/template/模版名字，修改HTML的形式',
  `column_code_url` tinyint(2) DEFAULT '0' COMMENT '是否使用栏目代码。默认为0，不使用，url使用id进行编码。  1:使用栏目代码作为URL，以后都会用1',
  `expiretime` int(11) DEFAULT '0' COMMENT '过期时间',
  `use_kefu` tinyint(1) DEFAULT '0' COMMENT '是否开启在线客服功能。0不开启，默认；  1开启',
  PRIMARY KEY (`id`),
  UNIQUE KEY `domain` (`domain`,`userid`,`state`,`expiretime`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=337 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci ROW_FORMAT=DYNAMIC COMMENT='网站，每个站点都会存为一条记录';

-- ----------------------------
--  Records of `site`
-- ----------------------------
BEGIN;
INSERT INTO `site` VALUES ('219', '测试演示自定义模版站', '243', '1488446743', '1', '17753600820', '25689732', '1', 'cs', '591', null, '3', '测试演示的自定义模版站', '潍坊软件园', '雷爷', '', '', null, '1', '0', null, '', '0', '2000123200', '1');
COMMIT;

-- ----------------------------
--  Table structure for `site_column`
-- ----------------------------
DROP TABLE IF EXISTS `site_column`;
CREATE TABLE `site_column` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` char(60) COLLATE utf8_unicode_ci DEFAULT NULL,
  `url` char(100) COLLATE utf8_unicode_ci DEFAULT '' COMMENT '链接地址',
  `icon` char(100) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '本栏目的图片、图标，可在模版中使用{siteColumn.icon}进行调用此图以显示',
  `rank` int(4) DEFAULT '0' COMMENT '排序,数字越小越往前',
  `used` tinyint(1) DEFAULT '1' COMMENT '是否启用。1启用，0不启用',
  `siteid` int(11) DEFAULT NULL COMMENT '对应的站点id,site.id',
  `userid` int(11) DEFAULT NULL COMMENT '对应user.id',
  `parentid` int(11) DEFAULT NULL COMMENT '上级栏目的id，若是顶级栏目，则为0',
  `type` tinyint(3) DEFAULT '6' COMMENT '所属类型，1新闻信息；2图文信息；3独立页面；4留言板；5超链接；6纯文字',
  `client` tinyint(2) DEFAULT '2' COMMENT 'PC:1； WAP:2',
  `template_page_list_name` char(20) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '当前栏目所使用的列表模版名字。当site.templateName有值时，整个网站使用的自定义模版，这里的才会生效',
  `template_page_view_name` char(20) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '当前栏目所使用的内容模版名字。当site.templateName有值时，整个网站使用的自定义模版，这里的才会生效',
  `code_name` char(20) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '当前栏目的代码，当前用户的某个网站内，这个栏目代码是唯一的',
  `parent_code_name` char(20) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '父级栏目代码，父栏目的栏目代码',
  `list_num` int(4) DEFAULT '0' COMMENT '适用于CMS模式下，新闻、图文列表，在生成列表页时，每页显示多少条数据',
  `input_model_code_name` char(30) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT 'CMS模式有效，绑定此栏目的输入模型 ，对应input_model表的code_name，若是为null或者空，则为默认的输入模型',
  `edit_mode` tinyint(2) DEFAULT '0' COMMENT '若是独立页面，内容编辑的方式，使用富文本编辑框，使用输入模型，则为0， 若是使用模版进行编辑，则为1',
  `list_rank` tinyint(2) DEFAULT '1' COMMENT '栏目内信息的列表排序规则，1:按照发布时间倒序，发布时间越晚，排序越靠前; 2:按照发布时间正序，发布时间越早，排序越靠前。 如果这里为null，则是v4.4版本以前的，那么默认程序里面会将不为2的全部都认为是1。',
  `edit_use_titlepic` tinyint(2) DEFAULT NULL COMMENT '内容管理中，添加内容时，封面图片的输入。 0隐藏，1显示，若是null，则是兼容v4.6以前的版本，需要根据栏目类型type进行判断',
  `edit_use_intro` tinyint(2) DEFAULT NULL COMMENT '内容管理中，添加内容时，文章简介的输入 0隐藏，1显示，若是null，则是兼容v4.6以前的版本，需要根据栏目类型type进行判断',
  `edit_use_text` tinyint(2) DEFAULT NULL COMMENT '内容管理中，添加内容时，文章详情的输入 0隐藏，1显示，若是null，则是兼容v4.6以前的版本，需要根据栏目类型type进行判断',
  `edit_use_extend_photos` tinyint(2) DEFAULT NULL COMMENT '内容管理中，添加内容时，图集的输入 0隐藏，1显示，若是null，则是兼容v4.6以前的版本，需要根据栏目类型type进行判断',
  `use_generate_view` tinyint(2) DEFAULT '1' COMMENT '是否生成内容页面。取值1生成；0不生成，如果为null则默认为1，默认是生成。set时使用 SiteColumn.USED_ENABLE 赋值',
  PRIMARY KEY (`id`),
  KEY `rank` (`rank`,`used`,`siteid`,`userid`,`parentid`,`type`,`client`,`code_name`,`parent_code_name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci ROW_FORMAT=DYNAMIC COMMENT='栏目表，网站上的栏目';

-- ----------------------------
--  Table structure for `site_data`
-- ----------------------------
DROP TABLE IF EXISTS `site_data`;
CREATE TABLE `site_data` (
  `id` int(11) NOT NULL DEFAULT '0' COMMENT 'site.id',
  `index_description` varchar(400) COLLATE utf8_unicode_ci DEFAULT '' COMMENT '首页的描述',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci ROW_FORMAT=DYNAMIC COMMENT='站点相关的一些信息，不常用或者长度变动比较大的。主要是电脑端网站有用到。';

-- ----------------------------
--  Table structure for `site_size_change`
-- ----------------------------
DROP TABLE IF EXISTS `site_size_change`;
CREATE TABLE `site_size_change` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `agency_id` int(11) DEFAULT NULL COMMENT '对应Agency.id，当前操作的若是代理，这里为当前的代理的id',
  `site_size_change` int(11) DEFAULT '0' COMMENT '代理变动的“站”余额的多少，消耗为负数，增加为正数  {@link Agency}.siteSize',
  `change_before` int(11) DEFAULT '0' COMMENT '变动前，站点的站余额是多少， {@link Agency}.siteSize',
  `change_after` int(11) DEFAULT '0' COMMENT '变化之后的站点的站余额是多少。{@link Agency}.siteSize',
  `goalid` int(11) DEFAULT '0' COMMENT '其余额变动，是开通的哪个站点引起的，记录站点的id，或者是哪个人给他增加的，记录给他增加的人的userid',
  `addtime` int(11) DEFAULT NULL COMMENT '变动时间',
  `userid` int(11) DEFAULT '0' COMMENT '当前操作的用户的user.id',
  PRIMARY KEY (`id`),
  KEY `site_size_change` (`site_size_change`,`addtime`,`userid`)
) ENGINE=InnoDB AUTO_INCREMENT=179 DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC COMMENT='站币变动日志记录表。站币变动日志由阿里云日志服务跟此表共同记录';

-- ----------------------------
--  Table structure for `sms_log`
-- ----------------------------
DROP TABLE IF EXISTS `sms_log`;
CREATE TABLE `sms_log` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `code` char(6) CHARACTER SET utf8 DEFAULT NULL COMMENT '发送的验证码，6位数字',
  `userid` int(11) DEFAULT NULL COMMENT '使用此验证码的用户编号，user.id',
  `used` tinyint(2) DEFAULT '0' COMMENT '是否使用，0未使用，1已使用',
  `type` tinyint(3) DEFAULT NULL COMMENT '验证码所属功能类型，  1:登录  ； 2:找回密码',
  `addtime` int(11) DEFAULT NULL COMMENT '创建添加时间，linux时间戳10位',
  `phone` char(11) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '接收短信的手机号',
  `ip` char(15) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '触发发送操作的客户ip地址',
  PRIMARY KEY (`id`),
  KEY `code` (`code`,`userid`,`used`,`type`,`addtime`)
) ENGINE=InnoDB AUTO_INCREMENT=75 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci ROW_FORMAT=DYNAMIC COMMENT='短信验证码发送的日志记录';

-- ----------------------------
--  Table structure for `system`
-- ----------------------------
DROP TABLE IF EXISTS `system`;
CREATE TABLE `system` (
  `name` varchar(200) CHARACTER SET utf8 COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '参数名,程序内调用',
  `description` char(200) CHARACTER SET utf8 COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '说明描述',
  `value` varchar(2000) CHARACTER SET utf8 COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '值',
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `lasttime` int(11) DEFAULT '0' COMMENT '最后修改时间，10位时间戳',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=10001 DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC COMMENT='系统变量，系统的一些参数相关，比如系统名字等';

-- ----------------------------
--  Records of `system`
-- ----------------------------
BEGIN;
INSERT INTO `system` VALUES ('USER_REG_ROLE', '用户注册后的权限，其值对应角色 role.id', '1', '6', '1506333513'), ('SITE_NAME', '网站名称', '网·市场', '7', null), ('SITE_KEYWORDS', '网站SEO搜索的关键字，首页根内页没有设置description的都默认用此', 'IW', '8', null), ('SITE_DESCRIPTION', '网站SEO描述，首页根内页没有设置description的都默认用此', '管雷鸣', '9', null), ('CURRENCY_NAME', '站内货币名字', '仙玉', '10', null), ('INVITEREG_AWARD_ONE', '邀请注册后奖励给邀请人多少站内货币（一级下线，直接推荐人，值必须为整数）', '5', '11', null), ('INVITEREG_AWARD_TWO', '邀请注册后奖励给邀请人多少站内货币（二级下线，值必须为整数）', '2', '12', null), ('INVITEREG_AWARD_THREE', '邀请注册后奖励给邀请人多少站内货币（三级下线，值必须为整数）', '1', '13', null), ('INVITEREG_AWARD_FOUR', '邀请注册后奖励给邀请人多少站内货币（四级下线，值必须为整数）', '1', '14', null), ('ROLE_USER_ID', '普通用户的角色id，其值对应角色 role.id', '1', '15', '1506333544'), ('ROLE_SUPERADMIN_ID', '超级管理员的角色id，其值对应角色 role.id', '9', '16', '1506333534'), ('BBS_DEFAULT_PUBLISH_CLASSID', '论坛中，如果帖子发布时，没有指明要发布到哪个论坛板块，那么默认选中哪个板块(分类)，这里便是分类的id，即数据表中的 post_class.id', '3', '20', '1506478724'), ('USER_HEAD_PATH', '用户头像(User.head)上传OSS或服务器进行存储的路径，存储于哪个文件夹中。<br/><b>注意</b><br/>1.这里最前面不要加/，最后要带/，如 head/<br/>2.使用中时，中途最好别改动，不然改动之前的用户设置好的头像就都没了', 'head/', '21', '1506481173'), ('ALLOW_USER_REG', '是否允许用户自行注册。<br/>1：允许用户自行注册<br/>0：禁止用户自行注册', '1', '22', '1507537911'), ('LIST_EVERYPAGE_NUMBER', '所有列表页面，每页显示的列表条数。', '15', '23', '1507538582'), ('SERVICE_MAIL', '网站管理员的邮箱。<br/>当网站出现什么问题，或者什么提醒时，会自动向管理员邮箱发送提示信息', '123456@qq.com', '24', '1511934294'), ('AGENCY_ROLE', '代理商得角色id', '10', '25', '1511943731'), ('ALIYUN_ACCESSKEYID', '阿里云平台的accessKeyId。<br/>若/src下的配置文件中有关此参数为空，则参数变会从这里赋值。<br/>可从这里获取 https://ak-console.aliyun.com', 'null', '26', '1512626213'), ('ALIYUN_ACCESSKEYSECRET', '阿里云平台的accessKeySecret。<br/>若/src下的配置文件中有关此参数为空，则参数变会从这里赋值。<br/>可从这里获取 https://ak-console.aliyun.com', 'null', '27', '1512616421'), ('ALIYUN_OSS_BUCKETNAME', '其实就是xnx3Config配置文件中配置OSS节点进行文件上传的OSS配置。若xml文件中没有配置，那么会自动从这里读取。<br/>若值为auto，则会自动创建。建议值不必修改，默认即可。它可自动给你赋值。', 'auto', '28', '1512626183'), ('IW_AUTO_INSTALL_USE', '是否允许通过访问/install/目录进行可视化配置参数。<br/>true：允许使用<br/>false:不允许使用<br/>建议不要动此处。执行完/install 配置完后，此处会自动变为false', 'true', '29', '1512616421'), ('ALIYUN_LOG_SITESIZECHANGE', '站币变动的日志记录。此项无需改动', 'sitemoneychange', '30', '1512700960'), ('AUTO_ASSIGN_DOMAIN', '网站生成后，会自动分配给网站一个二级域名。这里便是泛解析的主域名。<br/>如果分配有多个二级域名，则用,分割。并且第一个是作为主域名会显示给用户看到。后面的其他的域名用户不会看到，只可以使用访问网站。', 'wang.market', '31', '1512717500'), ('MASTER_SITE_URL', '设置当前建站系统的域名。如建站系统的登录地址为 http://wang.market/login.do ，那么就将 http://wang.market/  填写到此处。', '', '134', '1515401613'), ('ATTACHMENT_FILE_URL', '设置当前建站系统中，上传的图片、附件的访问域名。若后续想要将附件转到云上存储、或开通CDN加速，可平滑上云使用。', '', '135', '1515401592'), ('ATTACHMENT_FILE_MODE', '当前文件附件存储使用的模式，用的阿里云oss，还是服务器本身磁盘进行存储。<br/>可选一：aliyunOSS：阿里云OSS模式存储<br/>可选二：localFile：服务器本身磁盘进行附件存储', 'localFile', '136', '1515395510'), ('SITEUSER_FIRST_USE_EXPLAIN_URL', '网站建站用户第一天登陆网站管理后台时，在欢迎页面会自动通过iframe引入的入门使用说明的视频，这里便是播放的视频的页面网址', '//video.leimingyun.com/sitehelp/sitehelp.html', '137', '1533238686'), ('AGENCYUSER_FIRST_USE_EXPLAIN_URL', '代理用户前15天登陆代理后台时，会自动弹出使用教程的提示。这里便是教程的链接地址', 'http://www.wscso.com/jianzhanDemo.html', '138', '1533238686'), ('SITE_TEMPLATE_DEVELOP_URL', '模版开发说明，模版开发入门', 'http://tag.wscso.com/4192.html', '139', '1540972613'), ('FEN_GE_XIAN', '分割线，系统变量，若您自己添加，请使用id为 10000以后的数字。 10000以前的数字为系统预留。', '10000', '10000', '1540972613');
COMMIT;

-- ----------------------------
--  Table structure for `template`
-- ----------------------------
DROP TABLE IF EXISTS `template`;
CREATE TABLE `template` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` char(50) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '模版的名字，编码，唯一，限制50个字符以内',
  `addtime` int(11) DEFAULT NULL COMMENT '模版添加时间',
  `userid` int(11) DEFAULT '0' COMMENT '此模版所属的用户，user.id。如果此模版是用户的私有模版，也就是 iscommon=0 时，这里存储导入此模版的用户的id',
  `remark` char(200) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '模版的简介，备注说明，限制300字以内',
  `preview_url` char(200) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '模版预览网址，示例网站网址，绝对路径，',
  `type` int(11) DEFAULT NULL COMMENT '模版所属分类，如广告、科技、生物、医疗等。',
  `companyname` char(50) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '模版开发者公司名字。如果没有公司，则填写个人姓名',
  `username` char(10) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '模版开发人员的名字，姓名',
  `siteurl` char(100) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '模版开发者官方网站、企业官网。如果是企业，这里是企业官网的网址，格式如： http://www.leimingyun.com  ，如果是个人，则填写个人网站即可',
  `terminal_mobile` tinyint(2) DEFAULT NULL COMMENT '网站模版是否支持手机端, 1支持，0不支持',
  `terminal_pc` tinyint(2) DEFAULT NULL COMMENT '网站模版是否支持PC端, 1支持，0不支持',
  `terminal_ipad` tinyint(2) DEFAULT NULL COMMENT '网站模版是否支持平板电脑, 1支持，0不支持',
  `terminal_display` tinyint(2) DEFAULT NULL COMMENT '网站模版是否支持展示机, 1支持，0不支持',
  `iscommon` tinyint(2) DEFAULT NULL COMMENT '是否是公共的模版 1是公共的模版， 0不是公共的，私有的，是用户自己开通网站导入的',
  `rank` int(11) DEFAULT NULL COMMENT '公共模版的排序，数字越小越靠前。',
  `wscso_down_url` char(100) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT 'wscso模版文件下载的url地址',
  `zip_down_url` char(100) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT 'zip模版素材包文件下载的url地址',
  `preview_pic` char(100) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '模版预览图的网址，preview.jpg 图片的网址',
  `resource_import` char(10) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT 'js、css等资源引用方式。 cloud：使用云端模版库； private:使用私有模版库，也就是本地的',
  PRIMARY KEY (`id`),
  KEY `name` (`name`,`userid`,`type`,`companyname`,`username`,`terminal_mobile`,`terminal_pc`,`terminal_ipad`,`terminal_display`,`iscommon`,`addtime`,`rank`) USING BTREE
) ENGINE=MyISAM AUTO_INCREMENT=2 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci COMMENT='模版';

-- ----------------------------
--  Table structure for `template_data`
-- ----------------------------
DROP TABLE IF EXISTS `template_data`;
CREATE TABLE `template_data` (
  `name` char(20) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL COMMENT '模版的名字，模版编码',
  `id` int(11) NOT NULL,
  PRIMARY KEY (`name`),
  UNIQUE KEY `id` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC COMMENT='模版，分表，（暂未用到，预留）';

-- ----------------------------
--  Table structure for `template_page`
-- ----------------------------
DROP TABLE IF EXISTS `template_page`;
CREATE TABLE `template_page` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` char(20) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '当前模版页面的名字，仅给用户自己看，无其他作用',
  `type` tinyint(2) DEFAULT '0' COMMENT '类型；0其他；1首页；2新闻列表；3新闻详情；4图片列表；5图片详情；6单页面如关于我们',
  `userid` int(11) DEFAULT NULL COMMENT '所属的用户id',
  `template_name` char(20) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '所属模版的名字',
  `siteid` int(11) DEFAULT NULL COMMENT '当前使用的站点id',
  `remark` char(30) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '备注，限制30个字以内',
  `edit_mode` tinyint(2) DEFAULT NULL COMMENT '当前模版页面的编辑模式，1:智能模式； 2:代码模式。  这里，判断只要不是2，那都是智能模式，以兼容以前的版本',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=507 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci ROW_FORMAT=DYNAMIC COMMENT='模版页面，CMS模式网站中用到';

-- ----------------------------
--  Table structure for `template_page_data`
-- ----------------------------
DROP TABLE IF EXISTS `template_page_data`;
CREATE TABLE `template_page_data` (
  `id` int(11) NOT NULL,
  `text` text COLLATE utf8mb4_unicode_ci COMMENT '当前模版页面的模版内容',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=DYNAMIC COMMENT='模版页面的分表，存储具体模版页面的内容';

-- ----------------------------
--  Table structure for `template_var`
-- ----------------------------
DROP TABLE IF EXISTS `template_var`;
CREATE TABLE `template_var` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `template_name` varchar(20) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '模版名字',
  `userid` int(11) DEFAULT NULL COMMENT '模版所属用户的id',
  `updatetime` int(11) DEFAULT NULL COMMENT '最后修改时间',
  `addtime` int(11) DEFAULT NULL COMMENT '添加时间',
  `var_name` char(20) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '变量名字',
  `remark` char(30) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '备注，限制30个字以内',
  `siteid` int(11) DEFAULT NULL COMMENT '当前模版变量所属的网站',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=418 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci COMMENT='模版变量。CMS模式网站中用到。 userid为1的变量都是通用模版的变量，再系统启动起来时，会缓存到全局缓存中。';

-- ----------------------------
--  Table structure for `template_var_data`
-- ----------------------------
DROP TABLE IF EXISTS `template_var_data`;
CREATE TABLE `template_var_data` (
  `id` int(11) NOT NULL COMMENT '对应template_var.id',
  `text` text CHARACTER SET utf8 COLLATE utf8_unicode_ci COMMENT '模版变量的内容文字',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC COMMENT='模版变量，分表，存储具体变量的内容';

-- ----------------------------
--  Table structure for `user`
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT COMMENT '用户id编号',
  `username` char(20) COLLATE utf8_unicode_ci NOT NULL COMMENT '用户名',
  `email` char(40) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '邮箱',
  `password` char(32) COLLATE utf8_unicode_ci NOT NULL COMMENT '加密后的密码',
  `head` char(100) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '头像',
  `nickname` char(30) COLLATE utf8_unicode_ci NOT NULL COMMENT '姓名、昵称',
  `authority` char(20) COLLATE utf8_unicode_ci NOT NULL COMMENT '用户权限,主要纪录表再user_role表，一个用户可以有多个权限。多个权限id用,分割，如2,3,5',
  `regtime` int(10) unsigned NOT NULL COMMENT '注册时间,时间戳',
  `lasttime` int(10) unsigned NOT NULL COMMENT '最后登录时间,时间戳',
  `regip` char(15) COLLATE utf8_unicode_ci NOT NULL COMMENT '注册ip',
  `salt` char(6) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT 'shiro加密使用',
  `phone` char(11) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '手机号,11位',
  `currency` int(11) DEFAULT '0' COMMENT '资金，可以是积分、金币、等等站内虚拟货币',
  `referrerid` int(11) DEFAULT '0' COMMENT '推荐人的用户id。若没有推荐人则默认为0',
  `freezemoney` float(8,2) DEFAULT '0.00' COMMENT '账户冻结余额，金钱,RMB，单位：元',
  `lastip` char(15) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '最后一次登陆的ip',
  `isfreeze` tinyint(2) DEFAULT '0' COMMENT '是否已冻结，1已冻结（拉入黑名单），0正常',
  `money` float(8,2) DEFAULT '0.00' COMMENT '账户可用余额，金钱,RMB，单位：元',
  `idcardauth` tinyint(2) DEFAULT '0' COMMENT '是否已经经过真实身份认证了（身份证、银行卡绑定等）。默认为没有认证。预留字段。1已认证；0未认证',
  `sign` char(80) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '个人签名',
  `sex` char(4) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '男、女、未知',
  `oss_update_date` char(8) COLLATE utf8_unicode_ci DEFAULT '' COMMENT '当前用户的OSS存储，最后一次更新的时间，格式如  20160405  年月日',
  `oss_size` int(11) DEFAULT '0' COMMENT 'OSS存储的site/userid/news 下面的图文附件等，当天的存储大小',
  `oss_size_have` int(11) DEFAULT '30' COMMENT 'OSS所拥有的存储空间大小，单位MB，如免费用户是30MB，收费用户可能买了后是100MB',
  `version` int(11) DEFAULT '0',
  `siteid` int(11) DEFAULT NULL COMMENT '此用户拥有哪个站点的管理权。网站开通子账号会用到这个。如果这个有值，那么就是子账号了',
  PRIMARY KEY (`id`),
  UNIQUE KEY `email` (`email`,`username`,`phone`) USING BTREE,
  KEY `username` (`username`,`email`,`phone`,`isfreeze`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=393 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci COMMENT='用户信息表。系统登陆的用户信息都在此处';

-- ----------------------------
--  Records of `user`
-- ----------------------------
BEGIN;
INSERT INTO `user` VALUES ('1', 'admin', '', '94940b4491a87f15333ed68cc0cdf833', 'default.png', '总管理', '9', '1512818402', '1512818402', '127.0.0.1', '9738', '17000000002', '0', '0', '0.00', '127.0.0.1', '0', '0.00', '0', null, null, null, '0', '1024', '0', null), ('243', 'wangzhan', '', '0c5a0883e40a2a6ad84a42eab27519e6', '70877108e0684e1d9586f327eb5aafb5.png', '客服小红', '1', '1488446743', '1515402694', '218.56.88.231', '6922', '', '0', '392', '0.00', '127.0.0.1', '0', '0.00', '0', null, null, '20180108', '0', '1000', '254', null), ('392', 'agency', '', '80c5df10de72fde1b346de758c70d337', 'default.png', '代理', '10', '1512818402', '1515402763', '127.0.0.1', '9738', '17000000001', '0', '1', '0.00', '127.0.0.1', '0', '0.00', '0', null, null, null, '0', '1024', '1', null);
COMMIT;

-- ----------------------------
--  Table structure for `user_role`
-- ----------------------------
DROP TABLE IF EXISTS `user_role`;
CREATE TABLE `user_role` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `userid` int(11) DEFAULT NULL COMMENT '用户的id，user.id,一个用户可以有多个角色',
  `roleid` int(11) DEFAULT NULL COMMENT '角色的id，role.id ，一个用户可以有多个角色',
  PRIMARY KEY (`id`),
  KEY `userid` (`userid`)
) ENGINE=InnoDB AUTO_INCREMENT=414 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci ROW_FORMAT=DYNAMIC COMMENT='用户拥有哪些角色';

-- ----------------------------
--  Records of `user_role`
-- ----------------------------
BEGIN;
INSERT INTO `user_role` VALUES ('257', '243', '1'), ('412', '392', '10'), ('413', '1', '9');
COMMIT;

SET FOREIGN_KEY_CHECKS = 1;
