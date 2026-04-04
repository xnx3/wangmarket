# 仓库结构

这份说明基于当前 `wangmarket` 主仓整理。  
如果第三方只是复制了 `skills/`，但目标仓库代码结构已经明显变化，先更新本文件，再进行大规模改动。

## 技术栈与打包方式

- `pom.xml`
  当前项目是 Maven + Java 8 + Spring Boot 2.6.1 的 `war` 项目，版本号为 `7.2.0`。

- `src/main/java/com/Application.java`
  Spring Boot 启动入口，包含启动日志输出和 `restart()` 逻辑。

- `src/main/resources/application.properties`
  主配置文件。默认使用 SQLite，也支持切换到 MySQL 5.7；还包含上传、Redis、RabbitMQ、短信、授权、多语种、日志等配置。

- `src/main/webapp/WEB-INF/lib/`
  项目依赖了本地 `jar`，不是所有依赖都只来自远程 Maven 仓库。编译或整理依赖时不要误删这里的包。

## 主要 Java 包

- `src/main/java/com/xnx3/wangmarket/admin`
  网站管理后台的主体逻辑，常见的栏目、内容、模板、安装等控制器都在这里。

- `src/main/java/com/xnx3/wangmarket/agencyadmin`
  代理后台相关逻辑，偏向站点开通与代理层管理。

- `src/main/java/com/xnx3/wangmarket/domain`
  前台访问链路、域名解析、页面访问、缓存与生成相关逻辑。

- `src/main/java/com/xnx3/wangmarket/plugin`
  内置插件与扩展点。当前可直接看到 `markdown`、`newsSearch`、`templateDevelop` 等子目录。

- `src/main/java/com/xnx3/wangmarket/superadmin`
  总后台与系统级管理能力，例如插件管理。

- `src/main/java/com/xnx3/wangmarket/system`
  系统基础支撑代码。

## 常见改动落点

- 安装与启动
  `src/main/java/com/Application.java`
  `src/main/java/com/xnx3/wangmarket/admin/controller/InstallController_.java`
  `src/main/webapp/WEB-INF/view/install`

- 模板与栏目
  `src/main/java/com/xnx3/wangmarket/admin/controller/TemplateController.java`
  `src/main/java/com/xnx3/wangmarket/admin/controller/ColumnController.java`
  `src/main/webapp/WEB-INF/view/template`
  `src/main/webapp/WEB-INF/view/templateTag`
  `src/main/webapp/websiteTemplate`

- 内容管理
  `src/main/java/com/xnx3/wangmarket/admin/controller/NewsController.java`
  `src/main/webapp/WEB-INF/view/news`

- 域名与前台访问
  `src/main/java/com/xnx3/wangmarket/domain/controller/PublicController.java`
  `src/main/webapp/WEB-INF/view/domain`

- 插件管理与扩展
  `src/main/java/com/xnx3/wangmarket/superadmin/controller/PluginManageController.java`
  `src/main/webapp/WEB-INF/view/plugin`
  `src/main/java/com/xnx3/wangmarket/plugin`

- 输入模型
  `src/main/webapp/WEB-INF/view/inputModel`
  官方文档优先读 `40834` 与 `43885`

- 模板开发插件
  `src/main/java/com/xnx3/wangmarket/plugin/templateDevelop`
  `src/main/webapp/WEB-INF/view/plugin/templateDevelop`

## 视图与静态资源

- `src/main/webapp/WEB-INF/view/...`
  JSP 视图主目录。当前能看到 `agency`、`column`、`domain`、`install`、`news`、`plugin`、`site`、`template`、`templateTag`、`superadmin` 等子目录。

- `src/main/resources/static/...`
  静态资源目录。Maven 打包时会映射到 `static/`。

- `src/main/webapp/websiteTemplate/...`
  网站模板相关目录。做模板导入、模板文件调整时通常要一起看。

## 搜索建议

- 找控制器或路由：
  `rg -n "class .*Controller|@RequestMapping|@GetMapping|@PostMapping" src/main/java/com/xnx3/wangmarket`

- 找视图：
  `rg --files src/main/webapp/WEB-INF/view`

- 找插件目录：
  `find src/main/java/com/xnx3/wangmarket/plugin -maxdepth 2 -type d`

- 找配置项：
  `rg -n "authorize|sms\\.|translate\\.|spring.datasource|fileupload" src/main/resources/application.properties`

## 特别说明

- README 和官网会提到 `plugin/phoneCreateSite/reg.do`。这个路径对应的是另一个仓库的项目，不属于当前仓库维护范围。遇到相关描述时，不要把它当成当前 repo 的 bug 或必须修复的功能点。
