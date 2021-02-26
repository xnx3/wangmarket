/*
 Navicat Premium Data Transfer

 Source Server         : local.mysql.leimingyun.com
 Source Server Type    : MySQL
 Source Server Version : 50725
 Source Host           : local.mysql.leimingyun.com
 Source Database       : wangmarket

 Target Server Type    : MySQL
 Target Server Version : 50725
 File Encoding         : utf-8

 Date: 02/24/2021 18:35:51 PM
*/

SET NAMES utf8;
SET FOREIGN_KEY_CHECKS = 0;

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
  `menu` smallint(6) DEFAULT NULL,
  `rank` int(11) DEFAULT '0' COMMENT '排序，数字越小越靠前',
  `icon` char(100) COLLATE utf8_unicode_ci DEFAULT '' COMMENT '图标字符，这里是layui 的图标 ， https://www.layui.com/doc/element/icon.html ，这里存的是 unicode  字符，如  &#xe60c;',
  PRIMARY KEY (`id`),
  UNIQUE KEY `url` (`url`,`name`,`percode`),
  KEY `parent_id` (`parent_id`),
  KEY `suoyin_index` (`menu`,`rank`)
) ENGINE=InnoDB AUTO_INCREMENT=122 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci ROW_FORMAT=DYNAMIC COMMENT='Shiro权限管理中的资源';

-- ----------------------------
--  Records of `permission`
-- ----------------------------
BEGIN;
INSERT INTO `permission` VALUES ('12', '后台的用户管理', '/admin/user/list.do', '用户管理', '0', 'adminUser', '1', '1', '&#xe612;'), ('13', '后台用户管理下的菜单', '/admin/user/list.do', '用户列表', '12', 'adminUserList', null, '0', ''), ('14', '后台用户管理下的菜单', '/admin/user/delete.do', '删除用户', '12', 'adminUserDelete', null, '0', ''), ('15', '管理后台－系统管理栏目', '/admin/system/index.do', '系统管理', '0', 'adminSystem', '1', '4', '&#xe614;'), ('16', '管理后台－系统管理－系统参数、系统变量', '/admin/system/variableList.do', '系统变量', '15', 'adminSystemVariable', '1', '0', ''), ('18', '退出登录，注销登录状态', '/user/logout.do', '退出登录', '0', 'userLogout', '1', '20', '&#xe633;'), ('21', '更改当前登录的密码', 'javascript:updatePassword();', '更改密码', '0', 'adminUserUpdatePassword', '1', '19', '&#xe642;'), ('44', '后台，权限管理', '/admin/role/roleList.do', '权限管理', '0', 'adminRole', '1', '3', '&#xe628;'), ('46', '后台，权限管理，新增、编辑角色', '/admin/role/editRole.do', '编辑角色', '44', 'adminRoleRole', null, '101', ''), ('48', '后台，权限管理，角色列表', '/admin/role/roleList.do', '角色管理', '44', 'adminRoleRoleList', '1', '1', ''), ('49', '后台，权限管理，删除角色', '/admin/role/deleteRole.do', '删除角色', '44', 'adminRoleDeleteRole', null, '102', ''), ('51', '后台，权限管理，资源Permission的添加、编辑功能', '/admin/role/editPermission.do', '编辑资源', '44', 'adminRolePermission', null, '103', ''), ('53', '后台，权限管理，资源Permission列表', '/admin/role/permissionList.do', '资源管理', '44', 'adminRolePermissionList', '1', '2', ''), ('54', '后台，权限管理，删除资源Permission', '/admin/role/deletePermission.do', '删除资源', '44', 'adminRoleDeletePermission', null, '104', ''), ('55', '后台，权限管理，编辑角色下资源', '/admin/role/editRolePermission.do', '编辑角色下资源', '44', 'adminRoleEditRolePermission', null, '105', ''), ('56', '后台，权限管理，编辑用户所属角色', '/admin/role/editUserRole.do', '编辑用户所属角色', '44', 'adminRoleEditUserRole', null, '106', ''), ('71', '后台，日志管理', '/admin/log/list.do', '日志统计', '0', 'adminLog', '1', '5', '&#xe62c;'), ('72', '后台，日志管理，用户动作的日志列表', '/admin/log/list.do', '用户动作', '71', 'adminLogList', '1', '1', ''), ('74', '管理后台－系统管理，新增、修改系统的全局变量', '/admin/system/variable.do', '修改变量', '15', 'adminSystemVariable', null, '0', ''), ('80', '后台，用户管理，查看用户详情', '/admin/user/view.do', '用户详情', '12', 'adminUserView', null, '0', ''), ('81', '后台，用户管理，冻结、解除冻结会员。冻结后用户将不能登录', '/admin/user/updateFreeze.do', '冻结用户', '12', 'adminUserUpdateFreeze', null, '0', ''), ('82', '后台，历史发送的短信验证码', '/admin/smslog/list.do', '短信验证', '0', 'adminSmsLogList', '1', '2', '&#xe63a;'), ('114', '后台管理首页，登录后台的话，需要授权此项，不然登录成功后仍然无法进入后台，被此页给拦截了', null, '管理后台', '0', 'adminIndex', null, '0', ''), ('115', '管理后台首页', '', '后台首页', '114', 'adminIndexIndex', null, '0', ''), ('116', '删除系统变量', 'admin/system/deleteVariable.do', '删除变量', '15', 'adminSystemDeleteVariable', null, '0', ''), ('117', '后台，日志管理，所有动作的日志图表', '/admin/log/cartogram.do', '动作统计', '71', 'adminLogCartogram', '1', '2', ''), ('120', '可以将某个资源设置为菜单是菜单项', '/admin/role/editPermissionMenu.do', '设为菜单', '44', 'adminRoleEditPermissionMenu', '0', '107', ''), ('121', '对资源进行排序', '/admin/role/savePermissionRank.do', '资源排序', '44', 'adminRoleEditPermissionRank', '0', '108', '');
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
INSERT INTO `role` VALUES ('9', '总管理', '总后台管理，超级管理员');
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
) ENGINE=InnoDB AUTO_INCREMENT=216 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci ROW_FORMAT=DYNAMIC COMMENT='Shiro权限管理中，角色所拥有哪些资源的操作权限';

-- ----------------------------
--  Records of `role_permission`
-- ----------------------------
BEGIN;
INSERT INTO `role_permission` VALUES ('6', '1', '1'), ('7', '1', '2'), ('8', '1', '4'), ('9', '1', '3'), ('10', '1', '7'), ('11', '1', '9'), ('12', '9', '12'), ('13', '9', '13'), ('14', '9', '1'), ('17', '9', '15'), ('18', '9', '16'), ('20', '9', '18'), ('23', '9', '21'), ('30', '9', '14'), ('49', '9', '44'), ('51', '9', '46'), ('53', '9', '48'), ('54', '9', '49'), ('56', '9', '51'), ('58', '9', '53'), ('59', '9', '54'), ('60', '9', '55'), ('61', '9', '56'), ('75', '9', '71'), ('76', '9', '72'), ('77', '9', '74'), ('85', '1', '18'), ('86', '1', '19'), ('87', '1', '20'), ('88', '1', '21'), ('89', '1', '22'), ('90', '1', '75'), ('91', '1', '28'), ('92', '1', '29'), ('93', '1', '30'), ('94', '1', '10'), ('95', '1', '11'), ('96', '1', '23'), ('97', '1', '24'), ('98', '1', '25'), ('99', '1', '26'), ('100', '1', '27'), ('101', '9', '80'), ('104', '9', '81'), ('109', '10', '1'), ('110', '10', '2'), ('111', '10', '18'), ('112', '10', '20'), ('113', '10', '21'), ('114', '10', '22'), ('115', '10', '75'), ('116', '10', '24'), ('117', '10', '25'), ('118', '10', '26'), ('119', '10', '27'), ('120', '10', '88'), ('121', '10', '89'), ('122', '10', '90'), ('123', '10', '91'), ('124', '11', '1'), ('125', '11', '2'), ('126', '11', '18'), ('127', '11', '19'), ('128', '11', '20'), ('129', '11', '21'), ('130', '11', '22'), ('131', '11', '75'), ('132', '11', '7'), ('133', '11', '9'), ('134', '11', '10'), ('135', '11', '11'), ('136', '11', '23'), ('137', '11', '24'), ('138', '11', '25'), ('139', '11', '26'), ('140', '11', '27'), ('141', '11', '88'), ('142', '11', '89'), ('143', '11', '90'), ('144', '11', '91'), ('145', '12', '1'), ('146', '12', '2'), ('147', '12', '18'), ('148', '12', '19'), ('149', '12', '20'), ('150', '12', '21'), ('151', '12', '22'), ('152', '12', '75'), ('153', '12', '88'), ('154', '12', '89'), ('155', '12', '90'), ('156', '12', '91'), ('157', '9', '92'), ('158', '9', '93'), ('159', '9', '94'), ('160', '9', '95'), ('161', '9', '96'), ('162', '12', '4'), ('163', '12', '3'), ('164', '12', '28'), ('165', '12', '29'), ('166', '12', '30'), ('167', '12', '7'), ('168', '12', '9'), ('169', '12', '10'), ('170', '12', '11'), ('171', '12', '23'), ('172', '12', '24'), ('173', '12', '25'), ('174', '12', '26'), ('175', '12', '27'), ('176', '10', '19'), ('177', '10', '4'), ('178', '10', '3'), ('179', '10', '28'), ('180', '10', '29'), ('181', '10', '30'), ('182', '10', '7'), ('183', '10', '9'), ('184', '10', '10'), ('185', '10', '11'), ('186', '10', '23'), ('187', '10', '97'), ('188', '9', '98'), ('189', '9', '99'), ('190', '9', '100'), ('191', '9', '101'), ('192', '9', '102'), ('193', '9', '103'), ('194', '9', '104'), ('195', '10', '105'), ('196', '10', '106'), ('197', '10', '107'), ('198', '10', '108'), ('199', '10', '109'), ('200', '10', '110'), ('201', '10', '111'), ('202', '10', '112'), ('203', '10', '113'), ('204', '9', '114'), ('205', '9', '115'), ('206', '10', '114'), ('207', '10', '115'), ('208', '9', '117'), ('209', '9', '116'), ('210', '10', '118'), ('211', '10', '119'), ('212', '9', '120'), ('213', '9', '121'), ('214', '9', '82'), ('215', '9', '83');
COMMIT;

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
INSERT INTO `system` VALUES ('USER_REG_ROLE', '用户注册后的权限，其值对应角色 role.id', '1', '6', '1506333513'), ('SITE_NAME', '网站名称', '网·市场', '7', null), ('SITE_KEYWORDS', '网站SEO搜索的关键字，首页根内页没有设置description的都默认用此', '网市场云建站系统', '8', null), ('SITE_DESCRIPTION', '网站SEO描述，首页根内页没有设置description的都默认用此', '管雷鸣', '9', null), ('CURRENCY_NAME', '站内货币名字', '仙玉', '10', null), ('INVITEREG_AWARD_ONE', '邀请注册后奖励给邀请人多少站内货币（一级下线，直接推荐人，值必须为整数）', '5', '11', null), ('INVITEREG_AWARD_TWO', '邀请注册后奖励给邀请人多少站内货币（二级下线，值必须为整数）', '2', '12', null), ('INVITEREG_AWARD_THREE', '邀请注册后奖励给邀请人多少站内货币（三级下线，值必须为整数）', '1', '13', null), ('INVITEREG_AWARD_FOUR', '邀请注册后奖励给邀请人多少站内货币（四级下线，值必须为整数）', '1', '14', null), ('ROLE_USER_ID', '普通用户的角色id，其值对应角色 role.id', '1', '15', '1506333544'), ('ROLE_SUPERADMIN_ID', '超级管理员的角色id，其值对应角色 role.id', '9', '16', '1506333534'), ('BBS_DEFAULT_PUBLISH_CLASSID', '论坛中，如果帖子发布时，没有指明要发布到哪个论坛板块，那么默认选中哪个板块(分类)，这里便是分类的id，即数据表中的 post_class.id', '3', '20', '1506478724'), ('USER_HEAD_PATH', '用户头像(User.head)上传OSS或服务器进行存储的路径，存储于哪个文件夹中。<br/><b>注意</b><br/>1.这里最前面不要加/，最后要带/，如 head/<br/>2.使用中时，中途最好别改动，不然改动之前的用户设置好的头像就都没了', 'head/', '21', '1506481173'), ('ALLOW_USER_REG', '是否允许用户自行注册。<br/>1：允许用户自行注册<br/>0：禁止用户自行注册', '1', '22', '1507537911'), ('LIST_EVERYPAGE_NUMBER', '所有列表页面，每页显示的列表条数。', '15', '23', '1507538582'), ('SERVICE_MAIL', '网站管理员的邮箱。<br/>当网站出现什么问题，或者什么提醒时，会自动向管理员邮箱发送提示信息', '123456@qq.com', '24', '1511934294'), ('AGENCY_ROLE', '代理商得角色id', '10', '25', '1511943731'), ('ALIYUN_ACCESSKEYID', '阿里云平台的accessKeyId。<br/>若/src下的配置文件中有关此参数为空，则参数变会从这里赋值。<br/>可从这里获取 https://ak-console.aliyun.com', 'null', '26', '1512626213'), ('ALIYUN_ACCESSKEYSECRET', '阿里云平台的accessKeySecret。<br/>若/src下的配置文件中有关此参数为空，则参数变会从这里赋值。<br/>可从这里获取 https://ak-console.aliyun.com', 'null', '27', '1512616421'), ('ALIYUN_OSS_BUCKETNAME', '其实就是xnx3Config配置文件中配置OSS节点进行文件上传的OSS配置。若xml文件中没有配置，那么会自动从这里读取。<br/>若值为auto，则会自动创建。建议值不必修改，默认即可。它可自动给你赋值。', 'auto', '28', '1512626183'), ('IW_AUTO_INSTALL_USE', '是否允许通过访问/install/目录进行可视化配置参数。<br/>true：允许使用<br/>false:不允许使用<br/>建议不要动此处。执行完/install 配置完后，此处会自动变为false', 'true', '29', '1512616421'), ('ALIYUN_LOG_SITESIZECHANGE', '站币变动的日志记录。此项无需改动', 'sitemoneychange', '30', '1512700960'), ('AUTO_ASSIGN_DOMAIN', '网站生成后，会自动分配给网站一个二级域名。这里便是泛解析的主域名。<br/>如果分配有多个二级域名，则用,分割。并且第一个是作为主域名会显示给用户看到。后面的其他的域名用户不会看到，只可以使用访问网站。', '', '31', '1512717500'), ('MASTER_SITE_URL', '设置当前建站系统的域名。如建站系统的登录地址为 http://wang.market/login.do ，那么就将 http://wang.market/  填写到此处。', '', '134', '1515401613'), ('ATTACHMENT_FILE_URL', '设置当前建站系统中，上传的图片、附件的访问域名。若后续想要将附件转到云上存储、或开通CDN加速，可平滑上云使用。', '', '135', '1515401592'), ('ATTACHMENT_FILE_MODE', '当前文件附件存储使用的模式，用的阿里云oss，还是服务器本身磁盘进行存储。<br/>可选一：aliyunOSS：阿里云OSS模式存储<br/>可选二：localFile：服务器本身磁盘进行附件存储', 'localFile', '136', '1515395510'), ('SITEUSER_FIRST_USE_EXPLAIN_URL', '网站建站用户第一天登陆网站管理后台时，在欢迎页面会自动通过iframe引入的入门使用说明的视频，这里便是播放的视频的页面网址', '//video.leimingyun.com/sitehelp/sitehelp.html', '137', '1533238686'), ('AGENCYUSER_FIRST_USE_EXPLAIN_URL', '代理用户前15天登陆代理后台时，会自动弹出使用教程的提示。这里便是教程的链接地址', '//www.wscso.com/jianzhanDemo.html', '138', '1533238686'), ('SITE_TEMPLATE_DEVELOP_URL', '模版开发说明，模版开发入门', '//tag.wscso.com/4192.html', '139', '1540972613'), ('FORBID_DOMAIN', '保留不给用户申请的二级域名。多个用|分割，且填写字符必须小写，格式如 admin|domain|m|wap|www  如果留空不填则无保留域名', 'admin|domain', '149', '1566269940'), ('STATIC_RESOURCE_PATH', '系统静态资源如css、js等调用的路径。填写如:  //res.weiunity.com/   默认是/ 则是调取当前项目的资源，以相对路径调用', '/', '150', '1540972613'), ('ROLE_ADMIN_SHOW', '总管理后台中，是否显示权限管理菜单。1为显示，0为不显示', '0', '151', '1540972613'), ('FEN_GE_XIAN', '分割线，系统变量，若您自己添加，请使用id为 10000以后的数字。 10000以前的数字为系统预留。', '10000', '10000', '1540972613');
COMMIT;

-- ----------------------------
--  Table structure for `user`
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT COMMENT '用户id编号',
  `username` char(40) COLLATE utf8_unicode_ci NOT NULL COMMENT '用户名',
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
  `sign` char(80) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '个人签名',
  `sex` char(4) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '男、女、未知',
  `version` int(11) DEFAULT '0',
  PRIMARY KEY (`id`),
  UNIQUE KEY `email` (`email`,`username`,`phone`) USING BTREE,
  KEY `username` (`username`,`email`,`phone`,`isfreeze`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=393 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci COMMENT='用户信息表。系统登陆的用户信息都在此处';

-- ----------------------------
--  Records of `user`
-- ----------------------------
BEGIN;
INSERT INTO `user` VALUES ('1', 'admin', '', '94940b4491a87f15333ed68cc0cdf833', 'default.png', '总管理', '9', '1512818402', '1614161812', '127.0.0.1', '9738', '17000000002', '0', '0', '0.00', '127.0.0.1', '0', '0.00', null, null, '0');
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
INSERT INTO `user_role` VALUES ('413', '1', '9');
COMMIT;


-- ----------------------------
-- ----------------------------
-- 以上是wm的
-- 以下是wangmarket附加的
-- ----------------------------
-- ----------------------------

-- ----------------------------
--  user insert data
-- ----------------------------
BEGIN;
INSERT INTO `user` VALUES ('243', 'wangzhan', '', '0c5a0883e40a2a6ad84a42eab27519e6', '70877108e0684e1d9586f327eb5aafb5.png', '客服小红', '1', '1488446743', '1515402694', '218.56.88.231', '6922', '', '0', '392', '0.00', '127.0.0.1', '0', '0.00', null, null, '254'), ('392', 'agency', '', '80c5df10de72fde1b346de758c70d337', 'default.png', '代理', '10', '1512818402', '1515402763', '127.0.0.1', '9738', '17000000001', '0', '1', '0.00', '127.0.0.1', '0', '0.00', null, null, '1');
COMMIT;

-- ----------------------------
--  user_role insert data
-- ----------------------------
BEGIN;
INSERT INTO `user_role` VALUES ('257', '243', '1'), ('412', '392', '10');
COMMIT;


-- ----------------------------
--  role insert data
-- ----------------------------
BEGIN;
INSERT INTO `role` VALUES ('1', '建站用户', '建立网站的用户'), ('2', '论坛用户', '用户网站自己的论坛用户'),  ('10', '代理', '商代理，可以开通子代理、网站');
COMMIT;


-- ----------------------------
--  permission insert data
-- ----------------------------
BEGIN;
INSERT INTO `permission` VALUES ('88', '建站代理', '', '建站代理', '0', 'agencyIndex', null, '0', ''), ('89', '建站代理，代理商后台首页', '', '首页', '88', 'agencyIndex', null, '0', ''), ('90', '建站代理，代理商会员站点列表', '', '会员站点列表', '88', 'agencyUserList', null, '0', ''), ('91', '建站代理，添加用户站点', '', '添加用户站点', '88', 'agencyAdd', null, '0', ''), ('92', '信息文章相关操作', '', '文章管理', '0', 'adminNews', null, '0', ''), ('93', 'News数据表，信息列表', '', '信息列表', '92', 'adminNewsList', null, '0', ''), ('94', 'News数据表，信息详情', '', '信息详情', '92', 'adminNewsView', null, '0', ''), ('95', 'News数据表，删除信息', '', '删除信息', '92', 'adminNewsDelete', null, '0', ''), ('96', 'News数据表，合法性改为合法状态', '', '改为合法', '92', 'adminNewsCancelLegitimate', null, '0', ''), ('97', '建站代理，代理商后台，开通其下级普通代理', '', '开通普通代理', '88', 'AgencyNormalAdd', null, '0', ''), ('99', '总管理后台的网站管理', '', '全部网站管理', '0', 'adminSite', null, '0', ''), ('100', '网站列表', '', '网站列表', '99', 'adminSiteList', null, '0', ''), ('101', '网站详情页面', '', '网站详情', '99', 'adminSiteView', null, '0', ''), ('102', '添加一个网站跟用户', '', '添加网站', '99', 'adminSiteAdd', null, '0', ''), ('103', '访问统计相关', '', '访问统计', '0', 'adminRequestLog', null, '0', ''), ('104', '网站的访问情况', '', '访问统计', '103', 'adminRequestLogFangWen', null, '0', ''), ('105', '操作的日志列表', '', '操作日志', '88', 'agencyActionLogList', null, '0', ''), ('106', '资金变动日志', '', '资金日志', '88', 'agencySiteSizeLogList', null, '0', ''), ('107', '我的下级代理商列表', '', '下级列表', '88', 'agencySubAgencyList', null, '0', ''), ('108', '给我的下级代理充值站币', null, '站币充值', '88', 'agencyTransferSiteSizeToSubAgencyList', null, '0', ''), ('109', '给我开通的网站续费延长使用时间', null, '网站续费', '88', 'agencySiteXuFie', null, '0', ''), ('110', '给我下级的代理延长使用期限', '', '代理延期', '88', 'agencyYanQi', null, '0', ''), ('111', '将我下级的代理冻结，暂停', '', '冻结代理', '88', 'agencyAgencyFreeze', null, '0', ''), ('112', '将我下级的代理接触冻结，恢复正常', '', '解冻代理', '88', 'agencyAgencyUnFreeze', null, '0', ''), ('113', '修改站点、代理帐户的密码', '', '修改密码', '88', 'agencySiteUpdatePassword', null, '0', ''), ('118', '将自己直属下级的某个网站冻结', '', '冻结网站', '88', 'agencySiteFreeze', null, '0', ''), ('119', '将自己直属下级的某个网站解除冻结', '', '网站解冻', '88', 'agencySiteFreeze', null, '0', '');
COMMIT;


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
  `allow_create_sub_agency` tinyint(1) DEFAULT '0' COMMENT '此代理是否允许开通下级代理，是否有开通下级代理的功能。0不允许，1允许',
  `allow_sub_agency_create_sub` tinyint(1) DEFAULT '0' COMMENT '若此代理允许开通下级代理，开通的下级代理是否允许继续开通其下级代理功能。0不允许，1允许',
  `money` int(11) DEFAULT '0' COMMENT '账户余额，单位是分',
  PRIMARY KEY (`id`),
  KEY `userid` (`userid`,`parent_id`,`expiretime`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=52 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci ROW_FORMAT=DYNAMIC COMMENT='代理商信息';

-- ----------------------------
--  Records of `agency`
-- ----------------------------
BEGIN;
INSERT INTO `agency` VALUES ('51', '管雷鸣', '17000000001', '392', '1024', '120', '山东潍坊', '921153866', '99999999', '0', '0', '1512818402', '2143123200', '1', '0', '0', '0');
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
--  Table structure for `news`
-- ----------------------------
DROP TABLE IF EXISTS `news`;
CREATE TABLE `news` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `userid` int(11) DEFAULT '0' COMMENT '对应user.id，是哪个用户发表的',
  `addtime` int(11) DEFAULT '0' COMMENT '发布时间',
  `title` char(60) COLLATE utf8_unicode_ci DEFAULT NULL,
  `titlepic` char(200) COLLATE utf8_unicode_ci DEFAULT '' COMMENT '头图',
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
  KEY `userid` (`userid`,`type`,`supportnum`,`readnum`,`commentnum`,`cid`,`status`) USING BTREE,
  KEY `suoyin_index` (`userid`,`addtime`,`type`,`status`,`cid`,`siteid`)
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
  `text` mediumtext CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci COMMENT '信息内容',
  `extend` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci COMMENT '其他扩展字段，以json形式存在',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci ROW_FORMAT=DYNAMIC COMMENT='news内容分表';


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
  `attachment_size` int(11) DEFAULT NULL COMMENT '当前附件占用的空间大小，服务器空间，或云存储空间。计算的是 site/siteid/ 下的空间占用大小，单位是KB',
  `attachment_size_have` int(11) DEFAULT NULL COMMENT '当前用户网站所拥有的空间大小，单位是MB',
  `attachment_update_date` int(11) DEFAULT NULL COMMENT '当前附件占用空间大小，最后一次计算的日期，存储格式如 20191224 ，每天登录时都会计算一次',
  PRIMARY KEY (`id`),
  UNIQUE KEY `domain` (`domain`,`userid`,`state`,`expiretime`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=337 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci ROW_FORMAT=DYNAMIC COMMENT='网站，每个站点都会存为一条记录';

-- ----------------------------
--  Records of `site`
-- ----------------------------
BEGIN;
INSERT INTO `site` VALUES ('219', '测试演示自定义模版站', '243', '1488446743', '1', '17753600820', '25689732', '1', 'cs', '591', null, '3', '测试演示的自定义模版站', '潍坊软件园', '雷爷', '', '', null, '1', '0', null, '', '0', '2000123200', '1', null, '1000', null);
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
  `admin_news_used` int(2) DEFAULT NULL COMMENT '是否在内容管理中显示这个栏目。',
  `template_code_column_used` int(2) DEFAULT NULL COMMENT '是否在模版调用中显示（调取子栏目列表）。1使用，0不使用',
  `template_code_news_used` int(2) DEFAULT NULL COMMENT '是否在模版调用中显示（调取文章列表）。1使用，0不使用',
  `description` char(200) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT 'SEO description',
  `keywords` char(50) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT 'SEO keywords',
  PRIMARY KEY (`id`),
  KEY `rank` (`rank`,`used`,`siteid`,`userid`,`parentid`,`type`,`client`,`code_name`,`parent_code_name`)
) ENGINE=InnoDB AUTO_INCREMENT=1755 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci ROW_FORMAT=DYNAMIC COMMENT='栏目表，网站上的栏目';


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
--  Table structure for `site_user`
-- ----------------------------
DROP TABLE IF EXISTS `site_user`;
CREATE TABLE `site_user` (
  `id` int(11) NOT NULL,
  `siteid` int(11) DEFAULT NULL COMMENT 'v4.9增加,v5.0版本从user表中转移到site_user,此用户拥有哪个站点的管理权。网站开通子账号会用到这个。如果这个有值，那么就是子账号了。对应 site.id',
  PRIMARY KEY (`id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ----------------------------
--  Table structure for `site_var`
-- ----------------------------
DROP TABLE IF EXISTS `site_var`;
CREATE TABLE `site_var` (
  `id` int(11) NOT NULL,
  `text` mediumtext COLLATE utf8mb4_unicode_ci COMMENT '当前模版页面的模版内容',
  PRIMARY KEY (`id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;


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
  `text` mediumtext COLLATE utf8mb4_unicode_ci COMMENT '当前模版页面的模版内容',
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
  `text` mediumtext CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci COMMENT '模版变量的内容文字',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC COMMENT='模版变量，分表，存储具体变量的内容';

SET FOREIGN_KEY_CHECKS = 1;