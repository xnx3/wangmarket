# 网市场云建站系统详细搭建步骤
(以放到香港服务器为例)

### 1.参照 README.md 文件中的“[极速体验步骤](https://github.com/xnx3/wangmarket/blob/master/README.md#项目极速搭建体验步骤)”搭建起来<br/>

### 2.配置及创建日志服务
日志服务主要用来记录用户登录后台的动作、网站的访问等。<br/>
##### 2.1 配置 /src/systemConfig.xml 文件<br/>
将其中的 log.logService.use 设置为 true ,便是开启日志服务的使用<br/>

##### 2.2 阿里云后台开通日志服务<br/>
阿里云日志服务控制台管理地址： [https://sls.console.aliyun.com](https://sls.console.aliyun.com)<br/>
2.2.1 创建Project，Project名称为：requestlog， 所属地域为：香港<br/>
2.2.2 进入新建立的 requestlog，进行创建 Logstore。一共需要创建四个 Logstore，其名字分别为：fangwen、sitemoneychange、kefu、siteconsole， 创建时其余项都是默认，Shard数据都选择1即可，花费的费用最低。<br/>
2.2.3 建立好 Logstore 后，右侧有个查询分析一览，点击进入“查询”，设置其查询分析属性，开启全文索引。同样，2.2.2 步骤中创建的四个Logstore都开启全文索引<br/>
如此，日志服务开启完毕！<br/>

##### 2.3 开启IM会话的会话内容记录<br/>
如果使用IM客服功能，可以讲对话进行记录，也是使用的日志服务。以上设置好后，可以直接到 /src/imConfig.xml 文件中将 aliyunLogKefu.use 设置为 true 即可！<br/>

### 3.配置消息服务
消息服务主要用于程序分布式部署使用。用以更新域名改动、IM客服相关设置的更新。如果不是分布式部署，只是部署一个，那消息服务可以不用管，忽略即可。<br/>
##### 3.1 配置 /src/domainConfig.xml 文件<br/>
将其中的 aliyunMNS_Domain.use 设置为 true ,便是开启消息服务的使用
##### 3.2 阿里云后台开通消息服务<br/>
3.2.1 阿里云消息服务控制台管理地址： [https://mns.console.aliyun.com](https://mns.console.aliyun.com)<br/>
3.2.2 进入后定位到香港区域，创建队列 wangmarketDomain 、wangmarketkefu 这两个，生命周期都是200，取出消息隐藏时长1。<br/>
3.2.3 创建完成后，回到 /src/domainConfig.xml ,将 aliyunMNS_Domain.endpoint 填上。<br/>如此，域名的消息服务配置完毕。<br/>
##### 3.3 开通IM的消息服务<br/>
若是IM客服项目单独部署，是要开启消息服务的<br/>
3.3.1 配置 /src/imConfig.xml 文件，将其中 aliyunMNS_kefu.use 设置为 true <br/>
3.3.2 将 aliyunMNS_kefu.endpoint 填上（在 3.2.2 得到的endpoint）

### 4.配置阿里绿网文本过滤服务
如果是自己用，那这项直接忽略。如果是给客户用，可以开启，会自动监测客户发布的文章是否是合法的，是否跟法律擦边。
##### 4.1 配置 /src/systemConfig.xml 文件<br/>
将其中的 aliyunTextFilter.use 设置为 true ,便是开启文本过滤服务的使用<br/>
##### 4.2 阿里云后台管理云盾文本过滤服务
云顿管理控制台：[https://yundun.console.aliyun.com/?spm=&p=cts#/greenWeb/config2](https://yundun.console.aliyun.com/?spm=&p=cts#/greenWeb/config2) 以前叫绿网，现在没找到，估计给整一块了，也可能不用设置就能直接用。

### 5.配置邮件发送
位于 /src/xnx3Config.xml 文件中的 mail 节点。当然，我比较倾向于直接用阿里云的邮件推送服务。<br/>
邮件推送控制台 [https://dm.console.aliyun.com](https://dm.console.aliyun.com)

### 6.为OSS配置CDN加速
锦上添花的功能。没有也没关系。但是有了速度真的是很嗨，并且花的钱更少！当然，几十个网站之内可能也感觉不出什么<br/>
位于 /src/xnx3Config.xml 文件中的 aliyunOSS.url ，将OSS的CDN访问域名加上。

### 7.配置管理后台访问域名
位于 /src/wangMarketConfig.xml 文件中的 masterSiteUrl , 将您当前项目的访问域名填上。如 http://localhost:8080/admin/

### 8.配置IM客服
位于 /src/wangMarketConfig.xml 文件中的 websocketUrl , 将您im应用的访问域名＋访问文件websocket 填上。如 http://localhost:8080/im/websocket

### 9.配置阿里云短信功能
短信选择了阿里云，主要是，第一不用先垫上几千块钱，第二，确实是便宜！<br/>
##### 9.1 配置 /src/xnx3Config.xml 文件中
配置其中的  AliyunSMSUtil 节点下的属性
##### 9.2 阿里云平台开通
阿里云短信控制台：[https://dysms.console.aliyun.com/dysms.htm](https://dysms.console.aliyun.com/dysms.htm)

