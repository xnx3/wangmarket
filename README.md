![输入图片说明](//cdn.weiunity.com/site/341/news/4dc19cce6540459d9cfb441bda11d146.png "1.png")

# 简介
* **网站使用方面**，延续了帝国CMS、织梦CMS的建站方式，有模版页面、模版变量、栏目绑定模版、内容管理等，用过帝国、织梦的，可快速使用！
* **整体结构简介**， SAAS云建站系统，可通过[后台](http://help.wscso.com/5717.html)任意开通多个网站，每个网站使用自己的账号进行独立管理。让每个互联网公司都可私有化部署自己的SAAS云建站平台。
* **建站服务人员**，招聘一个计算机专业的大学生，懂点html、会点PS作图，就完全足够，刚毕业大学生具有认真、学习能力强、工资成本相对更省等优点，必须首选。至于后台Java开发人员、服务器运维，统统干掉不要，用本系统做网站，已经不需要服务器运维及Java开发。


# 功能

* 在线开通网站，无需任何操作服务器操作
	* 可通过后台[（系统中的代理后台）](http://help.wscso.com/5717.html)在线开通网站
	* 用户可通过手机号+验证码方式自助开通网站（须配置短信通道购买短信验证码条数）
* 域名及绑定
	* 开通的网站，系统自动分配一个二级域名，以供测试。（本系统安装时输入的域名，自动分配的二级域名就是从这个上自动分配出来的）
	* 网站可以绑定自己的顶级域名，在网站管理后台-域名设置 中，按照提示步骤进行设置、解析，即可完成绑定。
	* 如果网站想绑定多个顶级域名，可以在功能插件-多域名绑定中绑定多个。不过不建议一个网站绑定多个，多个对SEO优化不好
* 模板
	* 模板采用 HTML 方式制作模板，可通过网站后台任意编写html（及js、css等）代码
	* 模板体系还包含模板变量（多个模板页中有公共的代码块，可以作为模板变量）、全局变量等，方便模板页中动态引用
	* 模板编辑时内置代码编辑器，更方便编辑书写代码
	* 内置半可视化的界面编辑（待升级完善，有垃圾代码产生，推荐用纯代码方式编辑）
	* 云端模板库百多套模板开放免费使用，是默认自带的，安装本系统后，创建一个网站，登录进入网站管理后台时，就可以看到选择模板这里
* 网站访问及生成网站
	* 网站做好后可以点击网站管理后台中的 生成整站 ，即可一键生成网站所有的 html 静态页面。
	* 网站访问
		* 开源版本在网站访问时，会直接将服务器磁盘上的 html 文件拿来显示
		* 企业版在网站访问时，因为企业版采用云存储，html文件不在服务器，存在于云存储（分布式存储）上，系统会先从内存中读缓存，缓存没有再从云存储读。
		* 两者在性能、使用行上基本都差不多，无非就是后续可扩展性及安全性，企业版考虑的更多。
* 安全
	* 数据、附件等都在你自己服务器或者相关华为云阿里云账户上，数据都在手里！不少老板的心里，数据自己掌握着心里头才是安全的，我们系统在这方面让你安心。
	* 系统完全独立运行，不受我们控制。我们万一哪天一不小心倒闭了，没事，您安装的私有SAAS云建站不受影响，你是独立的。（有的单位像是油田，是不开外网的，纯粹内网访问，支撑无外网环境的正常使用，足以证明其完全的独立）
	* 安防检测-网站分离。在某些场景，如政府单位，会定期进行安防检测，本系统可以将 网站访问-后端管理 完全分离独立，管理后台进行了什么设置，MQ推送通知网站访问服务器进行网站更新，而网站访问服务器，就只有固定的html、及 sitemap.xml 等访问请求可进入，从入口层就对安全进行保障。（这种的是需要我们介入进行协助部署）
	* 备份还原。可对模板进行备份及还原操作，改动某个模板时，可以先导出一个备份，如果改错了，还可以通过备份，有选择的将某个模板页进行还原回原本正常的样子。
	* 系统开源，可用于商业用途！但开源版本的我们网站管理后台左下角的标识要带着，至于所做的网站，访问看到的网站不需要放置我方任何标识。多么宽松的条件。
* 快速出网站
	* 快速做网站。开通网站-登录网站管理后台-选好模板-改改文字图片-绑定域名-上线 ，你完全可以不用管服务器、模板html代码，将时间用在正确的地方。
	* 快速复制网站。内置网站模板导出导入功能，你做好的网站，可以快速复制同样的出来上线交付
	* 对系统的所有操作、网站访问、是哪个人进行的操作等，都会进行详细记录。以便有异常时可以对其分析、追踪、及精准统计（需要配合ES使用，ElasticSearch云模块价格不菲，一个月三百多）
* 高效
	* 网站生成静态html页面，当打开网站时，直接显示的静态html页面，不需要服务器处理什么耗时逻辑运算。
	* 配套软件 [扒网站工具 https://gitee.com/mail_osc/templatespider](https://gitee.com/mail_osc/templatespider)  看好哪个网站，自动扒下来做成模版。所见网站，皆可为我所用
* 可扩展及功能定制
	* 开放式模板机制，同帝国CMS、织梦CMS的模板方式，网站想怎么显示就能怎么写html，同时有完善的模板开发辅助软件、插件、及文档。
	* 成熟的插件机制，有数十种扩展插件可直接拿来使用或看其源代码参考，同时有完善的插件开发示例及说明、二次开发文档可供参考 （[wm.zvo.cn](http://wm.zvo.cn)）

# 扩展的功能插件
为避免系统整体过于臃肿笨重，系统本身纯粹是模板建站内容管理的功能，如果需要更多功能，比如商城、客服坐席等，可以根据自己需求加入相关功能插件。
* [**万能表单反馈** 你可以用来做留言反馈、在线预约等各种表单。无条数限制、无类型限制、无需额外设置，做到模版上后，自动识别数据格式！](http://www.wang.market/10068.html)
* [**站内搜索** 让模版支持当前站内文章搜索功能。可搜索当前使用该模版的网站的某篇或多篇文章。](http://www.wang.market/10102.html)
* [**在线客服** 网站侧边栏的在线客服功能，设置上QQ、电话、微信后即自动显示。另有二十余套客服模板可选择](http://www.wang.market/10103.html)
* [**网站转移** 可将您网站所有数据都导出来，快速复制，创建出一个完全一模一样的网站！](http://www.wang.market/10104.html)
* [**模版开发** 模版开发专用插件，按照其内步骤快速制作网市场云建站系统所用的模版。所做的模版可以自己部署自己的私有模版库，也可以拿出来共享给我们，加入云端模版库。](http://www.wang.market/10105.html)
* [**私有模版库** 本地私有模版库，可以打造属于你自己SAAS云建站系统的私有模版库！](http://www.wang.market/10121.html)
* [**论坛** 每个网站都能有一个自己的、独立的论坛。包含发帖、回帖、论坛版块、最新动态、最火帖子、个人中心、我的发贴、我的回帖等。](http://www.wang.market/10067.html)
* [**数据字典** 当前网市场云建站系统的数据结构。以便二次开发。](http://www.wang.market/10128.html)
* [**子账号（网站）** 网站管理后台，可以开通子账号，并设置子账号的用户有管理网站的哪些权限。比如运营人员可以让其只能看到 功能插件 的功能，查看网站访问统计。又比如编辑人员可以让其只看到 内容管理、生成整站 功能，以免不懂技术改坏了网站，让其只能编辑文章、生成网站。](http://www.wang.market/13811.html)
* [**CNZZ访问统计** CNZZ网站访客统计，全方位统计网站访客情况。](http://www.wang.market/18875.html)
* [**内容同步** 可以指定一个其他网站，当对方网站内容管理中，文章有增加、修改、删除时，会自动将最新的文章同步到当前网站。](http://www.wang.market/19569.html)
* [**百度商桥** 百度商桥 插件，快速让网站拥有在线客服功能。](http://www.wang.market/19571.html)
* [**多域名绑定** 让一个网站绑定多个域名。](http://www.wang.market/19572.html)
* [**sitemap** 点击生成整站时，自动生成 sitemap.xml 文件](http://www.wang.market/19573.html)
* [**插件开发入门示例** 插件开发入门示例，引导你如何开发网市场云建站系统的功能插件。](http://www.wang.market/10122.html)
* [**文件管理插件** 提供文件的在线管理、新增文件夹、文件、编辑、删除文件等。可以将之理解为一个简单的在线FTP管理](http://www.wang.market/21019.html)
* [**请求频率拦截** 请求频率拦截，当频率过快，会触发禁止访问保护，请求将会被提前拦截丢弃。](http://www.wang.market/31072.html)
* [**后台请求日志** 可以对所有动态请求进行详细日志记录，什么时间请求的，是哪个用户发起的，请求的什么url等，以便被恶意攻击时的追踪。](http://www.wang.market/31073.html)
* [**客服坐席** 打通了 www.kefu.zvo.cn 与网市场云建站，让网市场云建站具有在线客服的功能（类似于百度商桥）本插件具体的功能，可以进入 www.kefu.zvo.cn 查看，在线体验。](http://www.wang.market/31074.html)
* [**在线商城** 打通了 www.shop.zvo.cn 与网市场云建站，让网市场云建站具有在线商城的功能。本插件具体的功能，可以进入 www.shop.zvo.cn 查看，在线体验。](http://www.wang.market/34167.html)
* [**API接口** 可以通过API接口开通、管理网站、调取网站栏目、内容等信息]()
  
......


# 快速体验功能
#### 2分钟快速体验从代理开通网站，到网站管理、发布（推荐）
[点此查看](https://gitee.com/leimingyun/dashboard/wikis/leimingyun/wangmarket_site_learn/preview?doc_id=1258300&sort_id=4364015) ， 从登录代理平台，通过代理平台开通网站、登录开通的网站管理后台，选择指定模板、改动相关信息、快速发布网站进行预览看到效果。

#### 只是看看网站管理后台的功能
[点此免费开通 http://wang.market/regByPhone.do?inviteid=50](http://wang.market/regByPhone.do?inviteid=50) 你可以使用你的手机号，免费自助开通一个网站进行使用或体验。一个手机号只可免费开通一次。

## 项目部署

#### 方式一：项目在本地快速运行体验，提供一键运行包
1. [点此下载 Windows 64一键运行包](http://www.wang.market/down.html) , 
2. 解压出来运行,注意不要有中文路径
3. 双击 “ 启动.bat ”
4. 访问 [localhost](http://localhost) 

就这么简单！
[查看详细视频演示](http://help.wscso.com/5683.html)

#### 方式二：放到线上，华为云服务器，花一元快速部署到线上使用
按照此网址开通账号、选配服务器，仅需要一元，就能在线上将网市场云建站系统部署起来使用，不再需要其他额外花费！ 
[http://help.wscso.com/16329.html](http://help.wscso.com/16329.html) 

#### 方式三：自定义部署，如部署到私有服务器、阿里云、腾讯云等
参考：  https://gitee.com/leimingyun/wangmarket_deploy  


## 配套的软件：扒网站工具、模版计算工具
看好哪个网站，用它拔下来做成模版。所见网站，皆可为我所用！
[gitee.com/mail_osc/templatespider](https://gitee.com/mail_osc/templatespider)

## 系统二次开发

#### 当前使用的开源框架介绍
SpringBoot2.6.1、Shiro、Redis5、Mysql5.7 (必须这个版本)、[ElasticSearch 7.10.1](https://gitee.com/leimingyun/elasticsearch)、  
[前端信息提示 msg.js](https://gitee.com/mail_osc/msg)、[客服坐席 kefu.js](https://gitee.com/mail_osc/kefu.js)、网站管理后台Layui

#### 开发前的说明
采用 Java 开发，数据库提供两种，默认使用 sqlite 3 ，也可以自己配置成使用 mysql。  
 **一定注意版本号不要错，jdk1.8、mysql要用5.7，重要的事说三遍**  
另外开发请用 Eclipse ，我们没用过idea，如果你用idea出现异常你可以自己百度搜索解决方式，因为有其他idea的朋友而且不少都能正常运行，但是我们没使用过idea，如果你idea出现异常我们也没法提供任何帮助。  
![](//cdn.weiunity.com/site/341/news/a3cc04ca18c64fc2ac4ad8bcb197ecaf.png)  
二次开发方面，请查阅开发文档：  [wm.zvo.cn](http://wm.zvo.cn/log.html)  
从网市场云建站5这个版本以后，我们将网市场云建站系统的底层基础支持，比如常用工具类、权限控制等，单独进行了抽离，调整包装出了一套集成开发框架，我们称之为 wm 。 也就是网市场云建站的基础操作，都是在wm文档之中的。  
二次开发文档，也就是wm的基础开发文档，包含数据库操作、文件操作、日志操作、短信发送、ajax请求等最基础的功能模块，通过它，可以让一个刚毕业的软件系的大学生在一个月内快速进入开发状态，拥有项目开发能力。

#### 当前目录结构
```
wangmarket                          项目
├─src                               项目源代码 ( Maven )
├─pom.xml                           项目源代码 pom ( Maven )
├─else                              其他的杂七杂八相关文件，一般用不到
│  ├─wangmarket.sql                 项目运行所需要的数据库文件( Mysql数据库，默认sqlite3)
└─README.md                         说明
```

#### 进行二次开发
二次开发时，我们不建议您直接在这个项目进行改动，不然您将失去跟随我们版本升级的能力。众所周知的织梦CMS，也是因为版本不升级失去维护，而出现大家所知晓的安全缺陷。  
我们建议您可以在这个项目 https://gitee.com/leimingyun/wangmarket_deploy 上进行扩展开发您自己想做的模块及功能、以及一些原有的功能及页面更改。比如，登陆页面的重写。  
我们网市场有新版本时，您只需直接更新 WEB-INF/lib/wangmarket-xxx.jar 即可完成版本的升级


## 使用的成本投入
#### 1. 无成本<br/>
如果你只是需要一个两个网站，不需要费这么大的劲熟悉程序购买服务器搭建等。可直接在线开通网站 [http://wang.market/regByPhone.do](http://wang.market/regByPhone.do?inviteid=50) 无任何广告，可永久使用。让人人都能有网站！<br/>
#### 2. 1000元成本，即可代理加盟<br/>
免费开通代理平台，最低预充值1000元，即可拥有开通1000个网站名额。你即可尝试开展自己的业务！<br/>
开通网站不收费，网站按使用空间大小及流量计费，一个普通企业网站，一天费用也就一分钱。另外，你可以继续开通你的下级代理。详细介绍参考：http://www.wang.market/shouquanhezuo.html  
（这种方式是在我们服务器给你开通代理账户，你所做的网站，数据实际上都是在我们服务器上，你只管使用。）
#### 3. 100元成本，安装到自己服务器<br/>
一台1核2G，安装网市场云建站系统，可以支撑几千个网站。详细步骤参见： [http://help.wscso.com/16329.html](http://help.wscso.com/16329.html)  
建议参与我们下面的帮安装的活动  
#### 4. 用于大集团、政府方面需要安全防护、攻防演练等场景  
可进行私有服务器（以及局域网环境）的部署、以及上云情况下的部署。  
比如满足政府每年的攻防演练要求等
#### 其他。。定制部署，例如
两台2核4G服务器(或更多) + 一台1核2G服务器 + 负载均衡 + Mysql云数据库 + 云存储 + CDN + Elasticsearch 
(http://www.wang.market/price.html)）架构如下：  
![](http://cdn.weiunity.com/site/341/templateimage/4f6088b65e514321a7caed3c1f62a547.png)<br/>




# 其他

* 开发者姓名：管雷鸣
* 开发者微信：xnx3com
* 官方网站：[www.wang.market](http://www.leimingyun.com)
* GitHub：[github.com/xnx3/wangmarket](https://github.com/xnx3/wangmarket)
* 开源中国：[gitee.com/mail_osc/wangmarket](https://gitee.com/mail_osc/wangmarket)
* QQ交流群：老群已满，新群 740332119
* 微信公众号：wangmarket
* [云端免费模版库,百多个模版，持续增加中](http://wang.market/template.do)
* [升级日志 www.wang.market/log.html](http://www.wang.market/log.html)
* [有偿帮助 www.wang.market/pay.html](http://www.wang.market/pay.html)
* [企业版授权 www.wang.market/price.html](http://www.wang.market/price.html)
	* 拥有更好的服务，帮安装、使用指导
	* **建议大中型企业购买企业授权，提供更好的服务支持，小微、初创企业及工作室，用开源版本的就足够，创业不易，更应相互扶持**
* [帮助文档，系统使用的帮助说明 help.wscso.com] (http://help.wscso.com)
* [模版文档,模版开发说明，及模版标签 tag.wscso.com](http://tag.wscso.com)


### 云端模版库
你的时间是非常宝贵的！不会让你一接触就要自己去学习做模版。
我们附带有 [云端模版库，点此查看](http://wang.market/template.jsp)  
虽然模版不多只有几百套，但却是可以在创建网站后一键导入，直接拿过来使用！一键导入之后，会自动创建栏目、页面。你只需要改一下栏目名字、改改文字、图片，就可以达到成本网站交付标准！作为初期的你，足够用此来来服务客户、及熟悉整个系统！另外我们的模版库的模版会持续增加。
[所见网站，皆可为我所用－无限模版计划](https://github.com/xnx3/templatespider)


### 项目经历
网市场云建站系统，于09年开始用php开发wap系统建站。之后用java重构，[于2010年底发布第一版的Java版建站系统](https://down.chinaz.com/soft/29191.htm)，经过十多年不断更新迭代、功能完善……



### 活动，免费帮安装+调试
1. 华为云通过我们链接 huawei.leimingyun.com 开通的华为云账户，购买服务器（1核2G服务器一年88元）安装网市场，帮你安装好、调试好，你可以不用管服务器所有操作，只管用！只不过我方不再提供人工方面的帮助。
2. 注意，此活动需要购买一年服务器，是因为我们想把有限的精力放在真正想用的人身上。买了服务器，那证明你确实想用它。
4. 会教授在合法合规的前提下，如何使用未备案的域名解析到国内服务器
5. 技术指导你如何开通网站、选择模板、修改、填充网站信息、网站上线，让你真正用它来发展业务赚钱。  

###### 重点-作者的话
我们支持白嫖，但我们不想被无意义的白嫖！很多工作室、小微公司生存赚点钱都不容易，其资金积累方面如果购买授权，显然是有不小的负担的，如果您属于这方面，我们很愿意帮助您，因为您是真的在用我们这套系统去赚钱生存下去，是真心在用它了，我们也在后面的三个月让您熟练使用它，以达到第四个月不再购买授权的情况下，依旧能很好的使用。如果您用它赚到钱了，撑过了艰难的资金紧张阶段了，希望您在考虑购买我们的授权。  
我们开源不易，还要赚钱糊口，精力实在有限，我们只能将时间放在真正想用它的朋友那里。如果你能掏买服务器的百八十块钱，不管后面能不能用不用起来，起码是有想法，舍得去投入那么一点点去做的，我们也愿意帮助您。
联系微信： xnx3com

# 常见问题-问答
#### 这个安装后只是一个网站，还是也有代理后台可以开通多个网站
安装的是全功能的，包含代理后台。这个安装后其实就是你自己私有化部署了一套SAAS云建站系统。代理后台的使用可以参考： [http://help.wscso.com/5717.html](http://help.wscso.com/5717.html)

#### 可以一键生成html网页吗？还是伪静态
是一键生成的html网页，是真静态，不是伪静态

#### 做的网站对手机适配程度怎么样？
我们模板库的数百模板中，大部分模板都是支持手机电脑同步访问的。其实这个问题跟我们建站系统没啥关系，对手机适配怎么样，取决于你做的模板中css样式是怎么写的，是不是响应式做了手机端的适配