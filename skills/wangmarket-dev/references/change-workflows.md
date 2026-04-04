# 改动工作流

## 总原则

1. 先判断任务属于哪一类：安装/启动、模板、内容管理、域名访问、插件扩展、输入模型、国际化、存储。
2. 先确认本仓库落点，再去官方文档索引里找对应页面，不要反过来凭印象硬改。
3. 优先使用官方推荐顺序：`功能插件 > 重写指定文件 > 直接修改核心`。
4. 控制影响面，优先做最小闭环修改，并同步检查相关 Controller、Service、JSP、JS、配置文件。

## 场景入口

### 安装、启动、配置

- 先读官方文档：
  `40827`、`40846`、`40847`、`40848`

- 先看本仓库：
  `src/main/java/com/Application.java`
  `src/main/resources/application.properties`
  `src/main/java/com/xnx3/wangmarket/admin/controller/InstallController_.java`
  `src/main/webapp/WEB-INF/view/install`

- 常见任务：
  切换 SQLite/MySQL、上传限制、短信配置、授权配置、安装流程调整、默认启动行为。

### 模板开发与页面展示

- 先读官方文档：
  `40838`、`40839`、`40840`、`40841`、`40843`、`40844`、`43836`

- 先看本仓库：
  `src/main/java/com/xnx3/wangmarket/admin/controller/TemplateController.java`
  `src/main/java/com/xnx3/wangmarket/admin/controller/ColumnController.java`
  `src/main/java/com/xnx3/wangmarket/plugin/templateDevelop`
  `src/main/webapp/WEB-INF/view/template`
  `src/main/webapp/WEB-INF/view/templateTag`
  `src/main/webapp/websiteTemplate`

- 补充判断：
  如果任务是“复制第三方网站样式”或“导入第三方模板”，先读 `40842` 与 `43836`。

### 内容管理与栏目逻辑

- 先看本仓库：
  `src/main/java/com/xnx3/wangmarket/admin/controller/NewsController.java`
  `src/main/java/com/xnx3/wangmarket/admin/controller/ColumnController.java`
  `src/main/webapp/WEB-INF/view/news`
  `src/main/webapp/WEB-INF/view/column`

- 常见检查点：
  是否影响栏目绑定模板、内容字段、生成后的页面输出或缓存。

### 域名、访问链路、上线

- 先读官方文档：
  `40831`、`40833`、`40836`、`40837`、`41949`

- 先看本仓库：
  `src/main/java/com/xnx3/wangmarket/domain/controller/PublicController.java`
  `src/main/java/com/xnx3/wangmarket/domain`
  `src/main/webapp/WEB-INF/view/domain`

- 常见判断：
  先分清是后台绑定域名问题、前台访问路由问题，还是 CDN / HTTPS 部署问题。

### 插件与功能扩展

- 先读官方文档：
  `40845`、`40849`、`40850`、`40851`

- 先看本仓库：
  `src/main/java/com/xnx3/wangmarket/plugin`
  `src/main/java/com/xnx3/wangmarket/superadmin/controller/PluginManageController.java`
  `src/main/webapp/WEB-INF/view/plugin`

- 处理原则：
  能做成插件就不要先改核心；如果只是覆盖个别页面或类，优先考虑“重写指定文件”的方式。

### 输入模型、多语种、存储

- 输入模型
  官方文档：`40834`、`43885`
  本仓库重点：`src/main/webapp/WEB-INF/view/inputModel`

- 多语种
  官方文档：`40853`
  本仓库重点：`src/main/resources/application.properties` 中 `translate.*` 配置，以及前台页面接入点。

- 分布式存储
  官方文档：`40852`
  本仓库重点：`application.properties`、上传链路、相关插件或存储接入代码。

## 推荐命令

- 看仓库当前改动：
  `git status --short`

- 搜控制器与映射：
  `rg -n "class .*Controller|@RequestMapping|@GetMapping|@PostMapping" src/main/java/com/xnx3/wangmarket`

- 搜 JSP/模板：
  `rg --files src/main/webapp/WEB-INF/view`

- Java/JSP/配置改动后验证：
  `mvn -q -DskipTests compile`

- 纯文档改动验证：
  `git diff --check`

## 什么时候先停下来

- 如果当前仓库不是网市场本体，或者目录结构与 `repo-architecture.md` 差异很大，先更新 skill 的结构说明。
- 如果需求实际指向 README/官网中的外部项目，例如 `plugin/phoneCreateSite/reg.do`，不要在当前仓库里误修。
