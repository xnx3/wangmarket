# 官方文档索引

抓取基准：2026-04-04 读取 `https://cms.zvo.cn/40845.html` 左侧官方导航。  
范围说明：这里只保留网市场本体相关文档，`AI-SEO` 分类及其安装文档已明确排除。

## 总入口

- 系统简介 / 文档目录
  URL: `https://cms.zvo.cn/welcome.html`
  用途：官方文档总导航。先不确定某个需求该读哪一篇时，从这里开始。

## 系统部署及安装方式

- 前言 - 几种安装方式的说明
  URL: `https://cms.zvo.cn/40827.html`
  用途：先判断应该走 Linux 服务器、本地/Windows，还是只补充授权、短信等可选配置。

- linux命令行方式部署
  URL: `https://cms.zvo.cn/40828.html`
  用途：Linux 服务器部署步骤总览。

- 电脑本地使用或windows服务器部署
  URL: `https://cms.zvo.cn/40829.html`
  用途：本地开发环境或 Windows 服务器部署时参考。

- 授权码配置（可选）
  URL: `https://cms.zvo.cn/40830.html`
  用途：处理 `application.properties` 中 `authorize` 配置时参考。

- 短信通道配置（可选）
  URL: `https://cms.zvo.cn/41008.html`
  用途：处理 `sms.uid`、`sms.password` 或短信相关插件能力时参考。

## 使用说明及快速入门

- 入门-开通及管理发布网站
  URL: `https://cms.zvo.cn/40831.html`
  用途：理解“开站、管理、上线”的标准流程。

- 三种后台及相关说明
  URL: `https://cms.zvo.cn/40832.html`
  用途：区分代理后台、网站管理后台、其他后台入口及职责。

- 域名绑定及解析
  URL: `https://cms.zvo.cn/40833.html`
  用途：域名绑定、解析与访问链路问题先看这里。

- markdown模版插件的使用
  URL: `https://cms.zvo.cn/40835.html`
  用途：涉及 markdown 模板、模板渲染插件时参考。

- 未备案域名使用国内服务器（CDN方式）
  URL: `https://cms.zvo.cn/40837.html`
  用途：未备案但要走国内访问链路时参考 CDN 方案。

- 未备案域名使用国内服务器（网站分离插件的方式）
  URL: `https://cms.zvo.cn/40836.html`
  用途：未备案域名但使用网站分离插件时参考。

- 配置HTTPS证书 - 采用CDN
  URL: `https://cms.zvo.cn/41949.html`
  用途：HTTPS 上线或 CDN 证书链路时参考。

## 模板体系入门及开发

- 准备工作，调试环境准备
  URL: `https://cms.zvo.cn/40838.html`
  用途：模板开发前的调试环境和准备工作。

- 模板制作基础入门
  URL: `https://cms.zvo.cn/40839.html`
  用途：模板体系基础规则与常见制作方式。

- 在首页中调出最新的6篇新闻（动态栏目调用使用案例）
  URL: `https://cms.zvo.cn/40840.html`
  用途：动态栏目调用、首页数据输出示例。

- 在线留言（表单提交案例）
  URL: `https://cms.zvo.cn/40841.html`
  用途：表单提交、留言场景与模板侧接入参考。

- 输入模型的使用示例
  URL: `https://cms.zvo.cn/40834.html`
  用途：输入模型基础使用与字段扩展示例。

- 用扒网站工具复制别人网站
  URL: `https://cms.zvo.cn/40842.html`
  用途：需要复制第三方站点样式或导入外部模板时参考。

- 自定义“网”图标
  URL: `https://cms.zvo.cn/10249.html`
  用途：品牌化、站点图标或默认标识替换时参考。

- css、js等模板文件放哪？
  URL: `https://cms.zvo.cn/40844.html`
  用途：模板静态资源目录规划和放置位置说明。

- 模板常见问题及注意事项
  URL: `https://cms.zvo.cn/40843.html`
  用途：模板调试时先排查常见坑。

- 修改第三方做好的模版导入自己的私有模板库
  URL: `https://cms.zvo.cn/43836.html`
  用途：将外部模板整理后纳入私有模板库时参考。

## 二次开发及功能扩展

- 前言
  URL: `https://cms.zvo.cn/40845.html`
  用途：最重要的入口。定义了二次开发的优先级、环境、扩展方式与升级影响。

- 本地环境及git导入项目
  URL: `https://cms.zvo.cn/40846.html`
  用途：本地导入项目、建立开发环境时参考。

- 配置Mysql数据库（可选）
  URL: `https://cms.zvo.cn/40847.html`
  用途：当默认 SQLite 不满足需求，需要切换 MySQL 时参考。

- 本地运行，跑起项目来
  URL: `https://cms.zvo.cn/40848.html`
  用途：本地启动、运行与基础验证时参考。

- 二次开发：功能插件的形式
  URL: `https://cms.zvo.cn/40849.html`
  用途：官方推荐的第一优先级扩展方式。

- 二次开发：重写指定文件的形式
  URL: `https://cms.zvo.cn/40850.html`
  用途：当插件不合适、但又要尽量保留升级能力时参考。

- 二次开发：修改wangmarket本身的形式
  URL: `https://cms.zvo.cn/40851.html`
  用途：只在必须直接改核心时参考，同时要接受后续升级成本。

- 自定义分布式存储方式
  URL: `https://cms.zvo.cn/40852.html`
  用途：图片、附件等存储方式要对接自定义对象存储或分布式存储时参考。

- 国际化支持，多语种切换
  URL: `https://cms.zvo.cn/40853.html`
  用途：多语种切换、国际化展示链路改动时参考。

- 重写系统默认输入模型
  URL: `https://cms.zvo.cn/43885.html`
  用途：要改输入模型默认行为或表单字段机制时参考。

## 推荐阅读顺序

- 安装并跑起项目：`40827 -> 40846 -> 40847 -> 40848`
- 模板开发：`40838 -> 40839 -> 40840 / 40841 / 40844 / 40843 -> 43836`
- 域名与上线：`40831 -> 40833 -> 40837 / 40836 -> 41949`
- 二次开发：`40845 -> 40849 / 40850 / 40851 -> 40852 / 40853 / 43885`
