package com.xnx3.wangmarket.admin.service;

import java.util.List;
import java.util.Map;

import com.xnx3.j2ee.vo.BaseVO;
import com.xnx3.wangmarket.admin.entity.InputModel;
import com.xnx3.wangmarket.admin.entity.News;
import com.xnx3.wangmarket.admin.entity.SiteColumn;
import com.xnx3.wangmarket.admin.vo.bean.NewsInit;

/**
 * 输入模型，CMS模式下的自定义输入相关
 * @author 管雷鸣
 */
public interface InputModelService {
	
	/**
	 * 根据输入模型的id,获取自己网站内这个模型
	 * @param modelId 要获取的哪个输入模型的内容
	 * @return 若返回null，则没有这个模型
	 */
	public InputModel getInputModelById(int modelId);
	
	/**
	 * 根据输入模型的模型代码,获取自己网站内这个模型
	 * @param codeName 模型代码，inputModel.codeName
	 * @return 若返回null，则没有这个模型
	 */
	public InputModel getInputModelByCodeName(String codeName);
	
	/**
	 * 根据栏目,获取这个栏目所用的输入模型
	 * @param siteColumn 栏目。其实只用到了里面的 codeName、siteid
	 * @return 若返回null，则没有这个模型
	 */
	public InputModel getInputModelBySiteColumn(SiteColumn siteColumn);
	
	
	/**
	 * 根据输入模型的id,获取这个模型的内容。
	 * @param modelId 要获取的哪个输入模型的内容
	 * @return 若返回null，则没有这个模型，使用默认的即可
	 */
	public String getInputModelText(int modelId);
	
	/**
	 * 从 session 缓存中，加载当前网站的输入模型列表。若缓存中没有，则从数据库加载到缓存，然后再返回。
	 * @return 当前网站的输入模型列表
	 */
	public List<InputModel> getInputModelListForSession();
	
	/**
	 * 编辑，保存输入模型，1保存到数据库；2更新当前的session缓存，保证缓存中的输入模型是最新的
	 * @param inputModel 要更新的缓存中的输入模型
	 * @return {@link BaseVO}
	 */
	public BaseVO saveInputModel(InputModel inputModel);
	
	/**
	 * 根据输入模型的id，删除该输入模型。1删除数据库、2更新缓存中的输入模型将其删除
	 * @param inputModelId 要删除的输入模型的id, {@link InputModel}.id
	 */
	public BaseVO removeInputModel(int inputModelId);
	
	/**
	 * 根据当前栏目设定的输入模型的id(siteColumn.inputModelId)来获取当前栏目的内容管理的表单提交使用的输入模型。
	 * @param newsInit {@link News} 、 {@link SiteColumn}等内容编辑时用到的数据源，用于正则替换输入模型内容的参数，起赋值作用
	 * @return 若是有输入模型，拿到自定义的输入模型。若是没有输入模型，则使用系统模型
	 */
	public String getInputModelTextByIdForNews(NewsInit newsInit);
	
	/**
	 * 从 session 缓存中，加载当前网站的输入模型列表。若缓存中没有，则从数据库加载到缓存，然后再返回。
	 * @return 当前网站的输入模型列表
	 */
	public Map<Integer, InputModel> getInputModelBySession();
	
	/**
	 * 从 session 缓存中，加载指定网站的输入模型列表。若缓存中没有，则从数据库加载到缓存，然后再返回。
	 * @return 当前网站的输入模型列表
	 */
	public Map<Integer, InputModel> getInputModelBySession(int siteid);
	
	
	/**
	 * 获取系统默认的输入模型的内容
	 */
	public String getDefaultInputModelText();
	
}
