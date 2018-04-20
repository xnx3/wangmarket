package com.xnx3.wangmarket.admin.controller;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import com.xnx3.j2ee.service.SqlService;
import com.xnx3.j2ee.util.Page;
import com.xnx3.j2ee.util.Sql;
import com.xnx3.wangmarket.admin.entity.Template;
import com.xnx3.wangmarket.admin.util.AliyunLog;
import com.xnx3.wangmarket.admin.vo.TemplateHotWord;

/**
 * 模版文件，模版选择
 * @author 管雷鸣
 */
@Controller
@RequestMapping("/template")
public class TemplateFileController extends BaseController {
	@Resource
	private SqlService sqlService;
	
	//热词
	public static List<TemplateHotWord> hotWordList;
	/**
	 * 外部的模版列表，这里是从网上直接下载的，尚未进行转换过的模版。若要使用，还需要用自动化模版组建进行转换
	 */
	@RequestMapping("/templateExternalList${url.suffix}")
	public String templateExternalList(HttpServletRequest request,Model model){
		AliyunLog.addActionLog(getUserId(), "查看外部的模版列表");
		
		if(hotWordList == null){
			//此结果来源于自动分词
			hotWordList = new ArrayList<TemplateHotWord>();
			hotWordList.add(new TemplateHotWord("公司", 70));
			hotWordList.add(new TemplateHotWord("蓝色", 59));
			hotWordList.add(new TemplateHotWord("简洁", 98));
			hotWordList.add(new TemplateHotWord("黑色", 108));
			hotWordList.add(new TemplateHotWord("商务", 71));
			hotWordList.add(new TemplateHotWord("官网", 70));
			hotWordList.add(new TemplateHotWord("大图", 79));
			hotWordList.add(new TemplateHotWord("下载", 78));
			hotWordList.add(new TemplateHotWord("企业", 196));
			hotWordList.add(new TemplateHotWord("绿色", 52));
			hotWordList.add(new TemplateHotWord("展示", 71));
			hotWordList.add(new TemplateHotWord("摄影", 52));
			hotWordList.add(new TemplateHotWord("设计", 52));
			hotWordList.add(new TemplateHotWord("大气", 152));
		}
		model.addAttribute("hotWordList", hotWordList);
		
		Sql sql = new Sql(request);
	    //因多表查询，这里设定上form搜索的是哪个数据表的字段，这里是log表
	    sql.setSearchTable("template");
	    //增加添加搜索字段
	    sql.setSearchColumn(new String[]{"code","name"});
	    //查询log数据表的记录总条数
	    int count = sqlService.count("template", sql.getWhere());
	    //每页显示20条
	    Page page = new Page(count, 20, request);
	    //创建查询语句，只有SELECT、FROM，原生sql查询。其他的where、limit等会自动拼接
	    sql.setSelectFromAndPage("SELECT * FROM template", page);
	    //当用户没有选择排序方式时，系统默认排序。
	    sql.setDefaultOrderBy("id ASC");
	    List<Template> list = sqlService.findBySql(sql, Template.class);
	    
	    model.addAttribute("list", list);
	    model.addAttribute("page", page);
	    return "templateExternal/templateExternalList";
	}
	
//	/**
//	 * 对template.name进行自动分词
//	 * @param request
//	 * @param model
//	 * @return
//	 */
//	@RequestMapping("/templateExternalList1")
//	public String templateExternalList1(HttpServletRequest request,Model model){
//		List<Template> list = sqlService.findBySqlQuery("SELECT * FROM template", Template.class);
//		//统计词出现的次数  key:词   value：次数
//		Map<String, Integer> cishuMap = new HashMap<String, Integer>();
//		
//		//分词，然后统计每个词出现的次数
//		for (int i = 0; i < list.size(); i++) {
//			List<Term> termList = StandardTokenizer.segment(list.get(i).getName());
//		    for (int j = 0; j < termList.size(); j++) {
//		    	String word = termList.get(j).word;
//		    	Integer num = cishuMap.get(word);
//				if(num == null){
//					num = 1;
//				}else{
//					num = num+1;
//				}
//				cishuMap.put(word, num);
//			}
//		}
//		
//		//将出现次数>100的词输出
//		for (Map.Entry<String, Integer> entry : cishuMap.entrySet()) {
//			if(entry.getValue() > 50 && entry.getKey().trim().length() > 1){
//				System.out.println(entry.getKey()+"  , "+entry.getValue());
//			}
//		}
//	    
//	    
//	    return "templateExternal/123";
//	}
	
	
}