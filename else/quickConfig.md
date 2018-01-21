# 网市场云建站系统快速配置使用
（前提：已安装JDK8、Mysql、Tomcat8）

#### 1. 下载运行程序运行程序在 [/application/wangmarket.zip](https://github.com/xnx3/wangmarket/application/wangmarket.zip) 
#### 2. 放入Tomcat 8将第一步下载的安装包解压后，放到tomcat 8中。<br/>
需要注意的是，要放到tomcat的 webapps/ROOT 目录，目的是让项目能用 http://localhost:8080/ 直接访问，而不要使用 http://localhost:8080/wangmarket/ 访问<br/>#### 3. 导入数据库及配置其连接MySql数据库的SQL文件： [wangmarket.sql](https://github.com/xnx3/wangmarket/blob/master/wangmarket.sql) , 可新建一个数据库后直接将其导入。<br/>然后修改数据库的配置文件： [/WEB-INF/classes/db.properties](https://github.com/xnx3/wangmarket/blob/master/SourceCode/admin/src/db.properties)<br/>

#### 4. 启动Tomcat
#### 5. 执行安装访问 /install/index.do 进行安装。如 http://localhost:8080/install/index.do<br/>(第一步中，若不是对云特别熟悉，我们建议您选择使用服务器本身进行附件存储)<br/>
[查看安装示例视频](https://v.qq.com/x/page/c053533596l.html)

#### 6. 安装完毕
#### 7. 登陆账号登陆地址为 /login.do<br/>总管理后台登录的账号密码都是 admin<br/>代理后台登录的账号密码都是 agency<br/>提供快速体验的账号密码都是 wangzhan<br/>
[三种后台的功能及作用简介可以点击此处查看](http://www.wang.market/2524.html)<br/>另，附带10分钟视频说明，让你10分钟就会CMS模式建站的基本使用。[视频点此查看](https://v.qq.com/x/page/k0516y0fouw.html)<br/><br/>


##### 我们有以下可选收费项为您服务：
1. 如果您没有Java技术人员，以上步骤我们可帮你安装，包括配置服务器环境、安装程序，泛解析域名，帮您配置好，您可直接拿来使用，一次性收费500元。安装完后任务结束。您可永久免费使用。但我们不包后续的系统维护。<br/>2. 我们给你安装使用云模块的系统，使用OSS、CDN、Docker、云数据库、负载均衡，自动扩容，无网站上限。安装调试费用2000元，每个月维护费500元(或按年5000)，我们给你维护系统，定期升级。
3. 我们可帮您制作模版，您在网上看好哪个网站，将对方网址给我们，我们可以将其拿过来做成模版，让您快速创建一个跟对方大致一样的网站。每次(一个网站)收费100元。同时我们也会将此模版共享到云模版库。
<br/>
<br/>

## 使用说明及视频示例

1. [快速建立网站、绑定域名的视频示例－时长30秒](http://www.wang.market/2525.html)
2. [代理平台快速开通多个网站视频示例－时长10秒](https://v.qq.com/x/page/a0535dso42a.html)
3. [网站管理后台使用说明-时长14分钟](https://v.qq.com/x/page/k0516y0fouw.html)



