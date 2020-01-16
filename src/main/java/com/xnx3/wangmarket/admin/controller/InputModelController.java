package com.xnx3.wangmarket.admin.controller;

import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.xnx3.j2ee.service.SqlService;
import com.xnx3.j2ee.vo.BaseVO;
import com.xnx3.wangmarket.admin.entity.InputModel;
import com.xnx3.wangmarket.admin.entity.Site;
import com.xnx3.wangmarket.admin.service.InputModelService;
import com.xnx3.wangmarket.admin.util.ActionLogUtil;

/**
 * CMS模式下，输入模型相关操作
 * @author 管雷鸣
 */
@Controller
@RequestMapping("/inputModel/")
public class InputModelController extends BaseController {

	@Resource
	private SqlService sqlService;
	@Resource
	private InputModelService inputModelService;
	
	/**
	 * 当前输入模型列表
	 */
	@RequestMapping("list${url.suffix}")
	public String list(HttpServletRequest request, Model model){
		ActionLogUtil.insert(request, "进入CMS模式下自定义输入模型列表");
		model.addAttribute("list", inputModelService.getInputModelListForSession());
		return "inputModel/list";
	}
	
	/**
	 * 增加、修改输入模型
	 * @param id 如果传入inputModel.id，那么便是编辑模式
	 */
	@RequestMapping("edit${url.suffix}")
	public String edit(HttpServletRequest request, Model model,
			@RequestParam(value = "id", required = false , defaultValue="0") int id){
		InputModel inputModel = inputModelService.getInputModelById(id);
		
		ActionLogUtil.insert(request, "进入输入模型编辑页面", inputModel == null? "添加页面":inputModel.toString());
		model.addAttribute("inputModel", inputModel);
		return "inputModel/edit";
	}
	

	/**
	 * edit 中，获取的原本的输入模型的内容
	 */
	@RequestMapping(value="getInputModelTextById${url.suffix}", method = RequestMethod.POST)
	@ResponseBody
	public BaseVO getInputModelTextById(HttpServletRequest request, Model model,
			@RequestParam(value = "id", required = false , defaultValue="0") int id){
		InputModel inputModel = inputModelService.getInputModelById(id);
		if(inputModel == null){
			//为空，可能是输入模型不存在，是新增的情况，输出系统默认输入模型
			BaseVO vo = new BaseVO();
			vo.setBaseVO(3, inputModelService.getDefaultInputModelText());
			return vo;
		}else{
			ActionLogUtil.insert(request, inputModel.getId(), "获取输入模型的内容", inputModel.getCodeName());
			return success(inputModel.getText());
		}
	}

	
	/**
	 * 保存输入模型
	 */
	@RequestMapping(value="save${url.suffix}", method = RequestMethod.POST)
	@ResponseBody
	public BaseVO save(HttpServletRequest request, Model model,
			InputModel im){
		Site site = getSite();
		InputModel inputModel;
		if(im.getId() != null && im.getId() > 0){
			inputModel = sqlService.findById(InputModel.class, im.getId());
			if(inputModel == null){
				return error("要编辑的输入模型不存在！");
			}
			if(inputModel.getSiteid() - site.getId() != 0){
				return error("该输入模型不属于你，无法操作");
			}
		}else{
			inputModel = new InputModel();
			inputModel.setSiteid(site.getId());
		}
		
		inputModel.setRemark(filter(im.getRemark()));
		inputModel.setCodeName(filter(im.getCodeName()));
		inputModel.setText(im.getText());
		
		//跟当前模型以外的，自己的输入模型不能模型代码重复，从缓存中判断是否有重复的
		List<InputModel> cacheList = inputModelService.getInputModelListForSession();
		for (int i = 0; i < cacheList.size(); i++) {
			InputModel m = cacheList.get(i);
			if(inputModel.getCodeName().equals(m.getCodeName())){
				//输入模型代码重复了，判断是否是同一个id，同一个id的话，是同一个模型，没事。如果不是同一个模型，是不允许重复的
				if(inputModel.getId() != null && inputModel.getId() - m.getId() == 0){
					//是同一个，可忽略
				}else{
					//不是同一个，那就不行了，出现错误提示
					return error("模型代码重复！保存失败，请更改模型代码重试");
				}
			}
		}
		
		BaseVO vo = inputModelService.saveInputModel(inputModel);
		if(vo.getResult() - BaseVO.SUCCESS == 0){
			ActionLogUtil.insertUpdateDatabase(request, inputModel.getId(), "输入模型保存成功", inputModel.getCodeName());
		}
		
		return vo;
	}


	/**
	 * 删除某个输入模型
	 * @param id 要删除的输入模型的id，对应 {@link InputModel}.id
	 */
	@RequestMapping(value="delete${url.suffix}", method = RequestMethod.POST)
	@ResponseBody
	public BaseVO delete(HttpServletRequest request, Model model,
			@RequestParam(value = "id", required = false , defaultValue="0") int id){
		ActionLogUtil.insertUpdateDatabase(request, id, "输入模型删除");
		return inputModelService.removeInputModel(id);
	}
	
}