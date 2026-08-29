package com.xnx3.wangmarket.admin.service.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.xnx3.FileUtil;
import com.xnx3.j2ee.dao.SqlDAO;
import com.xnx3.j2ee.util.ConsoleUtil;
import com.xnx3.j2ee.util.SystemUtil;
import com.xnx3.j2ee.vo.BaseVO;
import com.xnx3.wangmarket.admin.cache.Template;
import com.xnx3.wangmarket.admin.entity.InputModel;
import com.xnx3.wangmarket.admin.entity.News;
import com.xnx3.wangmarket.admin.entity.Site;
import com.xnx3.wangmarket.admin.entity.SiteColumn;
import com.xnx3.wangmarket.admin.service.InputModelService;
import com.xnx3.wangmarket.admin.util.ActionLogUtil;
import com.xnx3.wangmarket.admin.util.SessionUtil;
import com.xnx3.wangmarket.admin.vo.bean.NewsInit;

import cn.zvo.http.Http;
import cn.zvo.http.Response;

@Service("InputModelService")
public class InputModelServiceImpl implements InputModelService {
	//默认的系统输入模型，只加载一次。位于应用根目录 static/inputModel/default.html
	private static String defaultInputModelText = null;	
	//自定义输入模型中用于判断是否已经支持 HTML 页面名称的唯一模板变量。
	private static final String HTML_NAME_TEMPLATE_TOKEN = "{news.htmlName}";
	//旧输入模型自动升级时插入的 HTML 页面名称控件，保持与系统默认输入模型一致。
	private static final String HTML_NAME_INPUT_MODEL_BLOCK =
			"<!-- 生成文章详情页的 HTML 名称。由栏目管理中的\"生成HTML命名\"开关控制显示。 -->\n"
			+ "<div class=\"layui-form-item\" id=\"sitecolumn_editUseHtmlName\" style=\"display:none;\">\n"
			+ "\t<label class=\"layui-form-label\" id=\"label_htmlName\">生成HTML命名</label>\n"
			+ "\t<div class=\"layui-input-block\">\n"
			+ "\t\t<input type=\"text\" name=\"htmlName\" autocomplete=\"off\" placeholder=\"请输入HTML文件名，不含.html\" class=\"layui-input\" value=\"{news.htmlName}\" style=\"width:130px; display:inline-block;\">\n"
			+ "\t\t<span style=\"font-size:16px; margin-left:5px;\">.html</span>\n"
			+ "\t\t<div class=\"explain\">填写英文和数字，例如填写 world 后页面地址为 world.html；留空则按照文章 ID 命名，例如 123.html。</div>\n"
			+ "\t</div>\n"
			+ "</div>\n";
	//使用正则识别真实 HTML 标签；匹配前会屏蔽注释，避免把说明文字中的标签误认为控件。
	private static final Pattern TITLE_INPUT_PATTERN = Pattern.compile(
			"(?is)<input\\b[^>]*\\sname\\s*=\\s*([\"'])title\\1[^>]*>");
	private static final Pattern TITLEPIC_CONTAINER_PATTERN = Pattern.compile(
			"(?is)<[^>]*\\sid\\s*=\\s*([\"'])sitecolumn_editUseTitlepic\\1[^>]*>");

	@Resource
	private SqlDAO sqlDAO;

	public String getInputModelText(int modelId) {
		Map<Integer, InputModel> map = getInputModelBySession();
		
		InputModel inputModel = map.get(modelId);
		if(inputModel == null){
			return null;
		}else{
			return inputModel.getText();
		}
	}

	public List<InputModel> getInputModelListForSession() {
		Map<Integer, InputModel> map = getInputModelBySession();
		
		List<InputModel> inputModelList = new ArrayList<InputModel>();
		for (InputModel model : map.values()) {
			inputModelList.add(model);
		}
		return inputModelList;
	}
	
	public Map<Integer, InputModel> getInputModelBySession(int siteid){
		Map<Integer, InputModel> map = SessionUtil.getInputModel();
		
		//若是第一次使用，需要从数据库加载输入模型数据
		if(map == null){
			map = new HashMap<Integer, InputModel>();
			
			List<InputModel> inputModelList = sqlDAO.findBySqlQuery("SELECT * FROM input_model WHERE siteid = " + siteid, InputModel.class);
			if(inputModelList != null && inputModelList.size() > 0){
				//如果取到了当前网站有自己的输入模型，那么将其加入session缓存中
				for (int i = 0; i < inputModelList.size(); i++) {
					InputModel model = inputModelList.get(i);
					map.put(model.getId(), model);
				}
				SessionUtil.setInputModel(map);
			}
		}
		
		return map;
	}
	
	/**
	 * 获取当前session中的输入模型。若没有，则从数据库中加载当前网站的输入模型数据到Session中。
	 */
	public Map<Integer, InputModel> getInputModelBySession(){
		Site site = SessionUtil.getSite();
		int siteid = 0;
		if(site != null){
			siteid = site.getId();
		}
		return getInputModelBySession(siteid);
	}

	public InputModel getInputModelById(int modelId) {
		return getInputModelBySession().get(modelId);
	}

	public BaseVO saveInputModel(InputModel inputModel) {
		BaseVO vo = new BaseVO();
		
		// 新建模型没有主键，继续使用 persist；已有模型来自查询或 Session 缓存，
		// 可能已经脱离原事务，必须使用 merge 重新关联到当前事务后再更新。
		InputModel savedInputModel = inputModel;
		if(inputModel.getId() != null && inputModel.getId() > 0){
			savedInputModel = sqlDAO.merge(inputModel);
		}else{
			sqlDAO.save(inputModel);
		}
		if(savedInputModel.getId() != null && savedInputModel.getId() > 0){
			//数据库的保存成功，那么更新Session缓存的
			Map<Integer, InputModel> map = getInputModelBySession();
			map.put(savedInputModel.getId(), savedInputModel);
			SessionUtil.setInputModel(map);
			
			vo.setInfo(savedInputModel.getId()+"");
			return vo;
		}else{
			vo.setBaseVO(BaseVO.FAILURE, "保存失败");
			return vo;
		}
	}

	/**
	 * 检查并按兼容规则升级旧版自定义输入模型。
	 * <p>自动修改前只依赖用户确认的三个条件：模型中没有
	 * {@code {news.htmlName}}、能唯一找到文章标题输入、能唯一找到标题图片模块，
	 * 且标题输入位于标题图片模块之前。任一结构条件不满足都不猜测修改。</p>
	 */
	public BaseVO ensureHtmlNameField(InputModel inputModel) {
		BaseVO vo = new BaseVO();
		if(inputModel == null) {
			vo.setBaseVO(BaseVO.FAILURE, "当前栏目使用的自定义输入模型不存在，无法自动增加 HTML 命名字段");
			return vo;
		}

		String text = inputModel.getText();
		if(text == null) {
			vo.setBaseVO(BaseVO.FAILURE, "当前自定义输入模型内容为空，无法自动增加 HTML 命名字段");
			return vo;
		}
		//只要模型已经使用该变量，就视为用户已经完成自定义适配，不再重复改动。
		if(text.indexOf(HTML_NAME_TEMPLATE_TOKEN) > -1) {
			return vo;
		}

		//屏蔽 HTML 注释内容但保留字符串长度，保证正则得到的下标仍可用于原文插入。
		String searchableText = maskHtmlComments(text);
		Matcher titleMatcher = TITLE_INPUT_PATTERN.matcher(searchableText);
		boolean titleFound = titleMatcher.find();
		int titleEnd = titleFound ? titleMatcher.end() : -1;
		boolean titleDuplicate = titleFound && titleMatcher.find();
		Matcher titlepicMatcher = TITLEPIC_CONTAINER_PATTERN.matcher(searchableText);
		boolean titlepicFound = titlepicMatcher.find();
		int titlepicStart = titlepicFound ? titlepicMatcher.start() : -1;
		boolean titlepicDuplicate = titlepicFound && titlepicMatcher.find();
		if(!titleFound || titleDuplicate || !titlepicFound || titlepicDuplicate) {
			vo.setBaseVO(BaseVO.FAILURE, "当前自定义输入模型无法定位唯一的文章标题和标题图片输入，请手动增加 HTML 命名字段后再开启此栏目设置");
			return vo;
		}

		if(titleEnd > titlepicStart) {
			vo.setBaseVO(BaseVO.FAILURE, "当前自定义输入模型中文章标题与标题图片输入顺序不符合自动升级条件，请手动增加 HTML 命名字段后再开启此栏目设置");
			return vo;
		}

		String upgradedText = text.substring(0, titlepicStart) + HTML_NAME_INPUT_MODEL_BLOCK + text.substring(titlepicStart);
		inputModel.setText(upgradedText);
		BaseVO saveVO = saveInputModel(inputModel);
		if(saveVO.getResult() - BaseVO.FAILURE != 0) {
			return saveVO;
		}
		//落库失败时恢复调用方持有的对象，避免 Session 缓存出现未保存的模型内容。
		inputModel.setText(text);
		return saveVO;
	}

	/**
	 * 将 HTML 注释区域替换为空格，避免注释中的示例标签参与结构定位，同时保留原字符串长度。
	 */
	private String maskHtmlComments(String text) {
		StringBuilder masked = new StringBuilder(text);
		int commentStart = text.indexOf("<!--");
		while(commentStart > -1) {
			int commentEnd = text.indexOf("-->", commentStart + 4);
			int end = commentEnd > -1 ? commentEnd + 3 : text.length();
			for(int i = commentStart; i < end; i++) {
				masked.setCharAt(i, ' ');
			}
			commentStart = commentEnd > -1 ? text.indexOf("<!--", end) : -1;
		}
		return masked.toString();
	}

	public BaseVO removeInputModel(int inputModelId) {
		BaseVO vo = new BaseVO();
		Site site = SessionUtil.getSite();
		if(site == null){
			vo.setBaseVO(BaseVO.FAILURE, "您无权操作此条输入模型");
			return vo;
		}
		//判断要删除的这个输入模型是否是该用户的
		InputModel inputModel = sqlDAO.findById(InputModel.class, inputModelId);
		if(inputModel == null){
			vo.setBaseVO(BaseVO.FAILURE, "要删除的输入模型不存在");
			return vo;
		}
		if(inputModel.getSiteid() - site.getId() != 0){
			vo.setBaseVO(BaseVO.FAILURE, "要删除的输入模型不属于您，删除失败");
			return vo;
		}
		
		sqlDAO.delete(inputModel);
		
		//数据库的删除了，那么也要删除掉Session缓存中的
		Map<Integer, InputModel> map = getInputModelBySession();
		map.remove(inputModelId);
		SessionUtil.setInputModel(map);
		
		ActionLogUtil.insertUpdateDatabase(null, inputModel.getSiteid(), "删除输入模型", inputModel.getRemark());
		
		return vo;
	}

	public String getInputModelTextByIdForNews(NewsInit newsInit) {
		SiteColumn siteColumn = newsInit.getSiteColumn();
		
		InputModel im = getInputModelBySiteColumn(siteColumn);
		String text = null;	//输入模型中获取的自定义模型具体内容
		
		//如果该栏目没有输入模型，那么用默认的
		if(im == null){
			text = getDefaultInputModelText();
		}else{
			//该栏目有输入模型，那么用自定义的输入模型
			text = im.getText();
		}
		
		if(text == null){
			return "出错！获取输入模型失败！请重新尝试";
		}else{
			//将输入模型进行动态数据的替换
			text = text.replaceAll(Template.regex("siteColumn.type"), siteColumn.getType()+"");
			
			News news = newsInit.getNews();
			
			//v4.6 ，过滤掉所有自定义扩展的值调用
			Map<String, Boolean> map = new HashMap<String, Boolean>();
			if(text.indexOf("extend.") > 0){
				Pattern p = Pattern.compile(Template.regex("news.extend.(\\w*?)"));
				Matcher m = p.matcher(text);
				while(m.find()){
					map.put(m.group(1), true);
				}
			}
			
			if(news == null || news.getId() == null){
				text = text.replaceAll(Template.regex("news.title"), "");
				text = text.replaceAll(Template.regex("news.htmlName"), "");
				text = text.replaceAll(Template.regex("titlepicImage"), "");
				text = text.replaceAll(Template.regex("news.titlepic"), "");
				text = text.replaceAll(Template.regex("text"), "");
				text = text.replaceAll(Template.regex("news.intro"), "");
				text = text.replaceAll(Template.regex("news.reserve1"), "");
				text = text.replaceAll(Template.regex("news.reserve2"), "");
				
				//v4.6,自定义 extend
				if(map.size() > 0){
					for (Map.Entry<String, Boolean> entry : map.entrySet()) {
						text = text.replaceAll(Template.regex("news.extend."+entry.getKey()), "");
					}
				}
				
			}else{
				text = Template.replaceAll(text, Template.regex("news.title"), news.getTitle());
				text = Template.replaceAll(text, Template.regex("news.htmlName"), news.getHtmlName() == null ? "" : news.getHtmlName());
				text = Template.replaceAll(text, Template.regex("titlepicImage"), newsInit.getTitlepicImage());
				text = Template.replaceAll(text, Template.regex("news.titlepic"), news.getTitlepic());
				text = Template.replaceAll(text, Template.regex("news.intro"), news.getIntro());
				text = Template.replaceAll(text, Template.regex("news.reserve1"), news.getReserve1());
				text = Template.replaceAll(text, Template.regex("news.reserve2"), news.getReserve2());
				
				//此处因replaceAll容易出问题，而且｛text｝也只会出现一次，所以直接换为了replace
				text = Template.replaceAll(text, Template.regex("text"), newsInit.getNewsDataBean().getText());
				
				//v4.6,自定义 extend
				if(map.size() > 0){
					for (Map.Entry<String, Boolean> entry : map.entrySet()) {
						text = Template.replaceAll(text, Template.regex("news.extend."+entry.getKey()), newsInit.getNewsDataBean().getExtendJson(entry.getKey()));
					}
				}
			}
		}
		return text;
	}
	
	public InputModel getInputModelBySiteColumn(SiteColumn siteColumn) {
		Map<Integer, InputModel> map = getInputModelBySession(siteColumn.getSiteid());
		for (Integer key : map.keySet()) {
			InputModel inputModel = map.get(key);
			//此处判断将 siteColumn.getCodeName 改为 siteColumn.getInputModelCodeName()  ，感谢 https://gitee.com/tendeness 提出问题所在
			if(inputModel != null && inputModel.getCodeName() != null && inputModel.getCodeName().equals(siteColumn.getInputModelCodeName())){
				return inputModel;
			}
		}
		return null;
	}
	
	public InputModel getInputModelByCodeName(String codeName) {
		Map<Integer, InputModel> map = getInputModelBySession();
		for (Integer key : map.keySet()) {
			InputModel inputModel = map.get(key);
			if(inputModel != null && inputModel.getCodeName() != null && inputModel.getCodeName().equals(codeName)){
				return inputModel;
			}
		}
		return null;
	}

	public String getDefaultInputModelText(){
		if(defaultInputModelText == null){	
			defaultInputModelText = FileUtil.read(SystemUtil.getProjectPath()+"static/inputModel/default.html");
			if(defaultInputModelText == null || defaultInputModelText.length() < 1) {
				ConsoleUtil.info("检测到系统默认的输入模型不存在！（src/main/resources/static/inputModel/default.html）");
				ConsoleUtil.info("正在从云端 http://down.zvo.cn/wangmarket/resources/inputModel_default.html 加载默认的输入模型");
				Http http = new Http(Http.UTF8);
				Response hr;
				try {
					hr = http.get("http://down.zvo.cn/wangmarket/resources/inputModel_default.html");
				} catch (IOException e) {
					e.printStackTrace();
					return "出错！因本地未发现输入模型，获取云端输入模型时失败！请稍后重新尝试";
				}
				if(hr.getCode() != 200) {
					ConsoleUtil.error("http://down.zvo.cn/wangmarket/resources/inputModel_default.html 获取失败，request code："+hr.getCode());
				}
				defaultInputModelText = hr.getContent();
			}
		}
		return defaultInputModelText;
	}
}
