# wangmarket
网市场云建站系统，打破传统建站的高成本，让价格不再是阻碍的门槛，让每个人都能有自己的网站！ [www.wang.market](http://www.wang.market)
<br/>
## 在线快速体验
##### CMS模式网站管理后台（另外还有[手机模式、电脑模式](http://www.wang.market/index.html#about)）
登录网址：[wang.market](http://wang.market)<br/>
登录帐号：ceshicms<br/>
登录密码：ceshicms<br/>
##### 代理后台，可自由开通、管理网站
登录网址：[wang.market](http://wang.market)<br/>
登录帐号：tiyan<br/>
登录密码：tiyan<br/>
<br/>
## 运行环境<br/>
语言：Java 1.8<br/>
数据库：Mysql 5.5<br/>
运行容器：Tomcat8<br/>
运行环境：Linux (Mac开发，未再Windows下实验)<br/>
<br/>
## 项目简介<br/>
网市场云建站系统，于09年开发wap系统建站。之后在xnx3、iw等基础上开发而来。<br/>
于15年重新启动，<br/>
16年开始试运行<br/>
17年底正式开源发布！<br/>
截止17年底：<br/>
共建立网站服务客户一千余个，经过市场及客户验证。而非一时兴起作出来扔网上开源后就不管的<br/>
svn版本更新迭代837次！<br/>
版本功能性升级57次！[升级日志](http://www.wang.market/log.html)<br/>
<br/>
## 云端模版库<br/>
你的时间是非常宝贵的！不会让你一接触就要自己去学习做模版。
我们附带有 [云端模版库，点此查看](http://wang.market/template.jsp)<br/>
虽然模版不多只有不到20套，但却是可以在创建网站后一键导入，直接拿过来使用！一键导入之后，会自动创建栏目、页面。你只需要改一下栏目名字、改改文字、图片，就可以达到成本网站交付标准！作为初期的你，足够用此来来服务客户、及熟悉整个系统！另外我们的模版库的模版会持续增加。<br/>
<br/>
## 项目极速搭建体验步骤
##### 1.下载应用程序
运行程序在 [/application/wangmarket.zip](https://github.com/xnx3/wangmarket/application/wangmarket.zip) 下载后解压缩，解压后的文件可直接丢到tomcat8中<br/>
##### 2.配置数据库连接
数据库SQL文件： [wangmarket.sql](https://github.com/xnx3/wangmarket/blob/master/wangmarket.sql)<br/>
数据库修改的配置文件： /WEB-INF/classes/db.properties<br/>
##### 3.执行安装
数据库配置好后，将项目运行起来，访问项目 /install/index.do 进行下一步下一步安装
##### 4.登陆体验
登陆地址为 /login.do<br/>
登陆账号密码都是 ceshicms<br/>
另，附带10分钟视频说明，让你10分钟就会基本使用。[视频点此查看](http://www.wscso.com/site_basicUse.html)
<br/>
<br/>
本项目的初衷是每个应用功能进行单独部署，完全独立，进行分部式部署。更多资料正在努力更新中～～～计划于周六周末(2017.12.9～10)整理好大部分，开源发布！<br/>
<br/>

## 使用的成本投入
#### 2.五元成本<br/>
如果你只是需要一个两个网站，那你可直接找我们开通即可！不要费这么大的劲熟悉程序购买服务器搭建等。我们给你开通网站收费为一个网站一年收你5块钱，我想让网站的门槛(技术支持)降低到一顿饭之内，人人都能有网站！<br/>
你相当于乐捐赞助我们，我们为你提供技术支持，保证你能正常使用网站。你只管使用即可！
#### 3.七百左右成本<br/>
一台1核2G，安装网市场云建站系统，可以支撑起至少200个以上的网站(也可能过1000个没啥问题。网站本身做好后就很少会登陆后台修改了)。服务器成本一年大约七百。<br/>
#### 4.六千以上的成本<br/>
购买两台及以上，配置为2核4G及以上的云服务器。采用负载均衡、OSS、RDS、Docker、MQ、SLS、及弹性伸缩方案，将网站访问、管理后台、客服等功能完全分离，一年预计最低消耗六千元以上。建立十来万个网站不在话下。此种方式架构部署如下：<br/>
![](http://cdn.weiunity.com/site/341/templateimage/4f6088b65e514321a7caed3c1f62a547.png)<br/>
<br/>

## 交流、反馈<br/>
开发者姓名：管雷鸣<br/>
开发者QQ：921153866<br/>
开发者微信：xnx3com<br/>
开发者微信公众号：wangmarket<br/>
交流QQ群：472328584<br/>
官方网站：http://www.wang.market<br/>
GitHub：https://github.com/xnx3/wangmarket<br/>
开源中国：http://git.oschina.net/xnx3/wangmarket<br/>

<br/>
## 作者简介<br/>
对网站这种普惠全民的事，有极大的热爱。又或者说，对于能改变什么，有强烈的兴致。<br/>
高一接触网吧认识电脑，高二接触到网站(UBB标签排版那种)，高三辍学自学HTML、PHP，然后进入四大名校之新华电脑。<br/>
入校期间开始用新学的Java自写建站系统，1年毕业，毕业半年后，于2011年2月，将自己写的最初版本的WAP手机建站系统发布到了 [中国站长站](http://down.chinaz.com/soft/29191.htm) [功能说明](http://www.xnx3.com/software/xxJspMql/20121102/8.html)<br/>
而后发布了多项开发包，如 <br/>
[Java的辅助开发包](https://github.com/xnx3/xnx3) ，找图找色模拟键盘按键鼠标点击， <br/>
[iw web快速开发架构](https://github.com/xnx3/iw) 集成了权限、会员、日志、支付等常用项目的功能于一体，适用于有什么新项目可以直接拿来做新功能即可，简化开发时的重复工作。<br/>
而网市场云建站系统，便是在以上产品的基础上而来。<br/>

<br/>
## 版权声明<br/>
著作权登记号：2017SR608214<br/>
本软件使用 Apache License 2.0 协议。您可以随意修改、发布，或用于商业领域。但首页底部需保留我们“网市场”版权标示及链接！<br/>
另外可以与我们有更多合作可以查看： [http://www.wang.market/index.html#join](http://www.wang.market/index.html#join) 我们希望得到你的捐助，以帮助我更好的将此不断研发升级，惠及更多的人！<br/>
