# WEB-INF/view/ 中的jsp页面说明

#### agency
#### column
#### common
#### domain
#### inputModel	网站管理后台-模板管理-输入模型
* list.jsp	输入模型列表
* edit.jsp	添加或编辑某个输入模型

#### install
#### news 网站管理后台-内容管理
listForTemplate.jsp	文章列表
newsChangeColumnForSelectColumn.jsp	将某篇文章，转移到其他栏目中去
newsForTemplate.jsp	新增/编辑文章（注意，这里使用了输入模型，并不是文章的所有录入项都在这个jsp中）

#### site
* popupBasicInfo.jsp	点击系统设置-基本信息，弹出的弹出窗口显示到期时间、注册时间等信息
* popupInfo.jsp	点击系统设置-网站设置，弹出的网站标题、分配域名等信息
* popupSiteUpdate.jsp	点击系统设置-网站设置，点击其中某项如联系地址，所弹出的修改页面(用途不大，已被全局变量取代，未来版本考虑废弃)
* popupBindDomain.jsp	系统设置-绑定域名，弹出的绑定域名设置窗口


#### siteVar		网站管理后台-模板管理-全局变量
* list.jsp	全局变量列表
* property.jsp	属性编辑，可设定这个全局变量名字、填写说明等，适用于模板开发者定义全局变量
* edit.jsp		全局变量的值编辑，适用于网站使用者直接修改全局变量的内容。

#### superadmin
#### template
#### templateTag
#### wm