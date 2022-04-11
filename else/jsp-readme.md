# WEB-INF/view/ 中的jsp页面说明

#### agency	代理后台的一些页面
* actionLogList.jsp	操作日志，用户登录代理后台进行操作的所有操作记录进行查看，就是通过这个页面
* add.jsp	网站管理-开通网站
* addAgency.jsp	下级代理-开通下级代理
* index.jsp	代理后台主页，也就是显示左侧菜单的这个页面
* siteSizeLogList.jsp	资金日志，代理后台站币资金变动的日志记录
* subAgencyList.jsp	下级代理的列表页面
* systemSet.jsp	系统设置
* userList.jsp	网站管理的列表页面
* welcome.jsp	登录代理后台成功后，看到的中间的欢迎页面，包括到期时间、最后登录ip等。

#### column 网站管理后台-栏目管理
* popupColumnForTemplate.jsp	添加/编辑/复制 栏目
* popupListForTemplate.jsp	栏目管理进入后的列表

#### domain
* common/head.jsp	已废弃，配合pcPreview.jsp使用的
* 404.jsp	网站访问时某个html页面未发现，就会出现这个404提示页面
* display.jsp	网站正常访问所显示的页面，就是通过这个来进行显示的
* notFindDomain.jsp	访问网站时，如果域名未在网站管理后台进行绑定，就不知道这个域名访问后要显示哪个网站，也就是会显示这个[未发现对应网站]的提示页面
* notFindIndexHtml.jsp	访问网站时，如果网站还没有生成整站，就会显示这个[您的网站尚未生成，请按照以下步骤生成网站]的提示页面
* pause.jsp	当代理后台中，对网站设置了冻结，那这个网站再访问的时候就会访问到这个页面，出现网站无法访问的提示。
* pcPreview.jsp	已废弃
* robots.jsp	当访问网站中的 robots.txt 文件时就是通过这个页面将robots.txt输出的，这个页面不用进行任何改动。
* welcome.jsp	系统安装的欢迎页，第一次部署使用，进行访问时会提示你安装，这个就是安装的欢迎页

#### inputModel	网站管理后台-模板管理-输入模型
* list.jsp	输入模型列表
* edit.jsp	添加或编辑某个输入模型

#### install	网市场第一次使用，通过安装引导进行安装的一些页面

#### news 网站管理后台-内容管理
* listForTemplate.jsp	点开内容管理后看到的文章列表
* newsChangeColumnForSelectColumn.jsp	将某篇文章，转移到其他栏目中去
* newsForTemplate.jsp	新增/编辑文章（注意，这里使用了输入模型，并不是文章的所有录入项都在这个jsp中）

#### site
* popupBasicInfo.jsp	点击系统设置-基本信息，弹出的弹出窗口显示到期时间、注册时间等信息
* popupInfo.jsp	点击系统设置-网站设置，弹出的网站标题、分配域名等信息
* popupSiteUpdate.jsp	点击系统设置-网站设置，点击其中某项如联系地址，所弹出的修改页面(用途不大，已被全局变量取代，未来版本考虑废弃)
* popupBindDomain.jsp	系统设置-绑定域名，弹出的绑定域名设置窗口


#### siteVar		网站管理后台-模板管理-全局变量
* list.jsp	全局变量列表
* property.jsp	属性编辑，可设定这个全局变量名字、填写说明等，适用于模板开发者定义全局变量
* edit.jsp		全局变量的值编辑，适用于网站使用者直接修改全局变量的内容。

#### superadmin	总管理后台
* index
	* welcome.jsp	欢迎页面，当前v5.5未使用
* news	文章管理
	* list.jsp	列表页面
	* view.jsp	查看文章简介
* site	网站管理
	* list.jsp	网站列表
	* view.jsp	查看网站详情
	
#### template	网站管理后台-模板管理及


* templateVar.jsp	新增/修改 模板变量
* templateVarList.jsp	模板变量列表
* templateVarListForUsed.jsp	当在模板管理-模板页面-编辑某个模板页面时，上方会有一些常用标签的提醒，其中有个[模板变量]点开就是这个页面
* welcome.jsp	网站管理后台登录成功后看到的欢迎页面，包含用户名、到期时间等


#### templateTag

#### 跟目录的几个