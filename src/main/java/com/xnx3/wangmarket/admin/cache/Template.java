package com.xnx3.wangmarket.admin.cache;

import com.xnx3.Lang;
import com.xnx3.FileUtil;
import com.xnx3.j2ee.Global;
import com.xnx3.j2ee.util.AttachmentUtil;
import com.xnx3.j2ee.util.SystemUtil;
import com.xnx3.wangmarket.admin.G;

/**
 * 模版总控制
 * @author 管雷鸣
 */
public class Template {
	private com.xnx3.wangmarket.admin.entity.Site site;
	private int templateId;
//	private boolean editMode = false;	//是否是编辑模式，默认不是编辑模式，用户端前台查看，正常网站浏览模式
	//新建页面，页面的模版
	public final static String newHtml = "<!DOCTYPE html>\n"
			+ "<html>\n"
			+ "<head>\n"
			+ "<meta charset=\"utf-8\">\n"
			+ "<title>新建页面</title>\n"
			+ "<meta http-equiv=\"X-UA-Compatible\" content=\"IE=edge,chrome=1\">\n"
			+ "<meta name=\"viewport\" content=\"width=device-width, initial-scale=1, maximum-scale=1\">\n"
			+ "<!--XNX3HTMLEDIT--></head>\n"
			+ "<body>\n\n\n"
			+ "页面内容\n\n\n</body>\n"
			+ "</html>";
	
	public Template(com.xnx3.wangmarket.admin.entity.Site site) {
		this.site = site;
		if(site.getTemplateId() != null){
			templateId = site.getTemplateId();
		}else{
			templateId = 0;
		}
//		editMode = false;
	}
	
	public Template(com.xnx3.wangmarket.admin.entity.Site site, boolean editMode) {
		this.site = site;
		this.templateId = site.getTemplateId();
//		this.editMode = editMode;
	}
	
	
	/**
	 * 获取当前首页模版的html代码。仅仅当前首页的模版，不包含引入的common文件
	 * @param templateId 模版编号
	 * @return 模版的HTML代码
	 */
	public String getIndexTemplateHtml_Only(){
		return FileUtil.read(SystemUtil.getProjectPath()+"/static/template/"+templateId+"/index.html");
	}
	
	/**
	 * 替换如  首页<!--aboutUs_Start-->12<!--aboutUs_End-->新闻这种
	 * @param text 总的原始的html源代码
	 * @param htmlTag 标签，如 aboutUs
	 * @param targetContent 将标签替换为的内容
	 * @return 替换后的网页HTML代码
	 */
	public static String replaceHtmlAnnoTag(String text,String htmlTag,String targetContent){
		if(text == null){
			return "";
		}
		
		//替换掉 \r\n 、 \n  、 \r
		text = text.replaceAll("\r\n","{rn}").replaceAll("\n", "{n}").replaceAll("\r", "{r}");
		
		//替换标签
		text = text.replaceAll("(<!--"+htmlTag+"_Start-->.*?<!--"+htmlTag+"_End-->)", "<!--"+htmlTag+"_Start-->"+targetContent+"<!--"+htmlTag+"_End-->");
		
		//加上换行符
		text = text.replaceAll(regex("rn"), "\r\n").replaceAll(regex("n"), "\n").replaceAll(regex("r"), "\r");
		
		return text;
	}
	
	/**
	 * 获取指定HTML注释中间的文字内容。 如 <pre>首页<!--aboutUs_Start-->12<!--aboutUs_End-->新闻 ，获取其内的 12</pre>
	 * @param sourceText 原始字符串，要提取的原始字符
	 * @param htmlTag 替换的标签，如 aboutUs
	 * @return
	 */
	public static String getAnnoCenterString(String sourceText,String htmlTag){
		return Lang.subString(sourceText, "<!--"+htmlTag+"_Start-->", "<!--"+htmlTag+"_End-->",2);
	}
	
	/**
	 * 获取指定HTML注释中间的文字内容,通过start＋id来获取。 如 <pre>首页<!--aboutUs_Start--><!--id=12-->这是内容啊啊啊<!--aboutUs_End-->新闻 ，获取其内的 12</pre>
	 * @param sourceText 原始字符串，要提取的原始字符
	 * @param htmlTag 替换的标签，如 aboutUs
	 * @return
	 */
	public static String getAnnoCenterStringById(String sourceText,String htmlTag, String id){
		return Lang.subString(sourceText, "<!--"+htmlTag+"_Start--><!--id="+id+"-->", "<!--"+htmlTag+"_End-->",2);
	}
	
	/**
	 * 判断是否存在，判断指定HTML注释（start＋id）是否存在。 如 <pre>首页<!--aboutUs_Start--><!--id=12-->内容啊啊啊啊啊啊<!--aboutUs_End-->新闻 ，获取其内的 12</pre>
	 * @param sourceText 原始字符串，要提取的原始字符
	 * @param htmlTag 替换的标签，如 aboutUs
	 * @return true：存在此标签
	 */
	public static boolean isAnnoCenterStringById_Have(String sourceText,String htmlTag, String id){
		if(sourceText.indexOf("<!--"+htmlTag+"_Start--><!--id="+id+"-->") > -1){
			return true;
		}else{
			return false;
		}
	}
	
	/**
	 * 判断 sourceText 中，是否存在 <!--id=12--> 这个栏目配置的注释
	 * @param sourceText 要查询是否存在于哪里的的原始字符串
	 * @param id SiteColumn.id
	 * @return 若发现了，返回true
	 */
	public static boolean isAnnoConfigById_Have(String sourceText, int id){
		if(sourceText.indexOf("<!--id="+id+"-->") > -1){
			return true;
		}else{
			return false;
		}
	}
	/**
	 * 获取HTML注释中的指定配置名的文本内容，若没发现这个配置，返回""
	 * @param sourceText 指定的HTML内容
	 * @param configName 参数名
	 * @return 值
	 */
	public static String getConfigValue(String sourceText,String configName){
		if(sourceText.indexOf("<!--"+configName+"=") > -1){
			return Lang.subString(sourceText, "<!--"+configName+"=", "-->",2);
		}else{
			return "";
		}
	}
	
	/**
	 * 更改某个配置属性的数值，如 <!--id=3-->
	 * @param sourceText 原文件，配置属性所在的原始字符串
	 * @param configName 属性的名字
	 * @param configValue 要更改为的值
	 * @return 更改后的源字符串
	 */
	public static String setConfigValue(String sourceText,String configName,String configValue){
		String value = getConfigValue(sourceText, configName);
		return sourceText.replaceAll("<!--"+configName+"="+value+"-->", "<!--"+configName+"="+configValue+"-->");
	}
	
	/**
	 * 获取HTML注释中的指定配置名的数字内容
	 * @param sourceText 指定的HTML内容
	 * @param configName 参数名
	 * @param defaultValue 若转换出错会用的数值
	 * @return 值
	 */
	public static int getConfigValue(String sourceText,String configName, int defaultValue){
		return Lang.stringToInt(getConfigValue(sourceText, configName), defaultValue);
	}
	
	/**
	 * 获取模版中的可替换的{}标签
	 * @param regexString 标签英文名
	 * @return 完整标签
	 */
	public static String regex(String regexString){
		return "\\{"+regexString+"\\}";
	}
	
	/**
	 * PC端网站进入编辑模式时，替换当前生成好的网站的一些东西，将相对路径改为绝对路径，等等
	 * @param text
	 */
	public String replaceForEditModeTag(String text){
		text = text.replaceAll("src=\"data", "src=\""+AttachmentUtil.netUrl()+"site/"+site.getId()+"/data");
		text = text.replaceAll("\"news/", "\""+AttachmentUtil.netUrl()+"site/"+site.getId()+"/news/");
		text = text.replaceAll("\"images/", "\""+AttachmentUtil.netUrl()+"site/"+site.getId()+"/images/");
		text = text.replaceAll("controllerRegEditMode", "edit");	//替换header中的js edit变量，设置其为编辑模式
		text = text.replaceAll(regex("_masterSiteUrl"), SystemUtil.get("MASTER_SITE_URL"));
		
		return text;
	}
	
	/**
	 * 替换特殊字符，避免再执行替换使，因为特殊字符的存在，影响正则匹配，导致替换出错
	 * @param sourceText 进行替换的原始字符串 sourceText.replaceALl
	 * @param regex 要替换sourceText的什么文字
	 * @param replacement 要将regex替换成什么
	 * @return 替换好的
	 */
	public static String replaceAll(String sourceText, String regex, String replacement){
		if(sourceText == null){
			return null;
		}
		if(regex == null || replacement == null){
			return sourceText;
		}

		//将$符号替换为 \$
		replacement = replacement.replaceAll("\\$", "\\\\\\$");  
		
		return sourceText.replaceAll(regex, replacement);
	}
	
}
