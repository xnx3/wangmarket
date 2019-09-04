package com.xnx3.wangmarket.plugin.wangMarketUpgrade.controller;

import java.io.File;
import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.xnx3.BaseVO;
import com.xnx3.j2ee.controller.BaseController;
import com.xnx3.wangmarket.plugin.wangMarketUpgrade.util.TomcatUtil;

/**
 * 开发中。
 * 控制网市场版本升级的Controller类
 * @author 李鑫
 */
@Controller
@RequestMapping("/plugin/wangMarketUpgrade")
public class wangMarketUpgradeController extends BaseController {
	
	/**
	 * 当前环境的容器类
	 */
	@Resource
	ApplicationContext applicationContext;
	
	/**
	 * 升级网市场为最新版本
	 * @author 李鑫
	 * @param vesion 网市场即将进行升级的版本
	 */
	@ResponseBody
	@RequestMapping("/upgrade${url.suffix}")
	public BaseVO upgrade(Model model,HttpServletRequest request,
			@RequestParam(value = "version", required = true, defaultValue = "") String vesion) {
		try {
			String version = "4.10";
			String rootPath = request.getServletContext().getRealPath("/");
			// 获得当前的环境
			String environment = applicationContext.getEnvironment().getProperty("os.name");
			// 获取磁盘未使用的磁盘空间
			int unUsedSize = getUnUsedSize(environment);
			System.out.println("可用空间:" + unUsedSize + "G");
			if(unUsedSize == 0) {
				return error("内存空间计算失败，升级失败");
			}
			// 获取剩余空间的容量。单位：bit
			BigInteger unUsedSizeBigInteger = BigInteger.valueOf(unUsedSize);
			BigInteger unUsedSizeBigIntegerForB = null;
			String unit = "1024";
			unUsedSizeBigIntegerForB = unUsedSizeBigInteger.multiply(new BigInteger(unit)).multiply(new BigInteger(unit)).multiply(new BigInteger(unit));
			
			System.out.println("剩余空间的b:" + unUsedSizeBigIntegerForB.toString());
			// 检查剩余空间是否足够本次更新
			boolean checkDiskCapacity = checkDiskCapacity(unUsedSizeBigIntegerForB, rootPath);
			if(checkDiskCapacity == false){
				return error("磁盘空间剩余不足，升级失败。");
			}
			// 进行备份
			boolean webappsFileBak = webappsFileBak(version, rootPath);
			if(webappsFileBak == false) {
				return error("文件备份失败，升级失败。");
			}
			return success("升级成功");			
		} catch (Exception e) {
			e.printStackTrace();
			return error("升级失败");
		}
	}
	
	/**
	 * 对ROOT文件夹进行压缩备份
	 * @author 李鑫
	 * @param version 需要升级网市场的版本信息 例："4.10"
	 * @param rootPath 当前tomcat的ROOT根目录
	 * @return true：备份成功；否则反之。
	 */
	private boolean webappsFileBak(String version, String rootPath) {
		// 获取ROOT文件的大小
		File rootFile = new File(rootPath);
		BigInteger fileSize = getFileSize(rootFile);
		// 创建存放压缩备份文件的bak文件夹
		String bakPath = rootPath.substring(0, rootPath.length() - 13) + "bak/";
		if(new File(bakPath).exists() == false) {
			new File(bakPath).mkdirs();
		}
		// 备份文件1
		String zipFileName = "upgrade_bak_" + new SimpleDateFormat("yyyyMMdd").format(new Date()) + "_" + version + ".zip";
		// 备份文件2
		String zipFileName_bak = "upgrade_bak2_" + new SimpleDateFormat("yyyyMMdd").format(new Date()) + "_" + version + ".zip";
		// 压缩备份文件1命令
		String command = "zip -r " + bakPath + zipFileName + " " + rootPath + "*";
		// 压缩备份文件2命令
		String command_bak = "zip -r " + bakPath + zipFileName_bak + " " + rootPath + "*";
		
		// 执行压缩备份文件1命令
		TomcatUtil.exeCommand(command);
		// 执行压缩备份文件2命令
		TomcatUtil.exeCommand(command_bak);

		// 判断两次压缩文件是否相同
		BigInteger zipFileSize = new BigInteger(new File(bakPath + zipFileName).length() + "");
		BigInteger zipBakFileSize = new BigInteger(new File(bakPath + zipFileName_bak).length() + "");
		// 不相同返回失败
		if(zipFileSize.compareTo(zipBakFileSize) != 0) {
			return false;
		}
		// 判断压缩文件是否可用，是否大于原ROOT文件夹的三分之二并且文件是否大于60M
		BigInteger condition = fileSize.multiply(new BigInteger(2 + "")).divide(new BigInteger(3 + ""));
		BigInteger fileMinSize = new BigInteger(60 + "").multiply(new BigInteger(1024 + "")).multiply(new BigInteger(1024 + ""));
		// 不满足条件返回失败
		if(zipBakFileSize.compareTo(condition) < 0 || zipBakFileSize.compareTo(fileMinSize) < 0) {
			return false;
		}
		
		return true;
	}
	
	/**
	 * 得到当前磁盘的未使用的空间大小
	 * @author 李鑫
	 * @return 当前剩余的空间单位。单位：G
	 */
	private int getUnUsedSize(String environment) {
		// 执行查看磁盘空间命令
		List<String> resultList = TomcatUtil.exeCommand("df -hl");
		// 获取查询结果
		String result = resultList.get(1);
		int indexOf = result.indexOf(" ");
		result = result.substring(indexOf).replaceAll(" ", "");
		
		// 根据查询结果得到不同得到结果
		Map<String, Object> map = getDiskCapacityInteger(result, environment);
		// 获取硬盘的总内存
		int totalSize = (int) map.get("result");
		result = (String) map.get("string");
		
		// 获取硬盘的已经使用的内存
		map = getDiskCapacityInteger(result, environment);
		int usedSize = (int) map.get("result");
		result = (String) map.get("string");
		
		// 获取硬盘的未使用的内存
		map = getDiskCapacityInteger(result, environment);
		int unUsedSize = (int) map.get("result");
		
		// 检查结果是否正确
		if((usedSize + unUsedSize) >= totalSize + 3 || (usedSize + unUsedSize) <= totalSize - 3) {
			return 0;
		}
		return unUsedSize;
	}
	
	/**
	 * 检查当前此磁盘剩余空间是否满足当前升级
	 * @author 李鑫
	 * @param unUsedForB 当前磁盘剩余的空间大小，单位：bit
	 * @param rootPath 用户的当前tomcat环境的ROOT根目录
	 * @return 返回true标识磁盘空间足够，否则反之。
	 */
	private boolean checkDiskCapacity(BigInteger unUsedForB, String rootPath) {
		
		File file = new File(rootPath);
		BigInteger fileSize = getFileSize(file);
		System.out.println("升级需要的内存：" + fileSize.multiply(new BigInteger(4 + "")).toString());
		// 当前的容量是否足够ROOT文件大小的四倍
		if(fileSize.multiply(new BigInteger(4 + "")).compareTo(unUsedForB) > 0) {
			return false;
		}
		return true;
	}

	/**
	 * 获取文件夹的大小
	 * @author 李鑫 
	 * @param file 需要检索大小的文件
	 * @return 文件大小的字节 单位：bit
	 */
	private static BigInteger getFileSize(File file) {
		// 得到文件夹下文件列表
		File[] listFiles = file.listFiles();
		// 转为list循环遍历
		List<File> list = Arrays.asList(listFiles);
		Iterator<File> iterator = list.iterator();
		BigInteger fileLength = new BigInteger("0");
		while (iterator.hasNext()) {
			File nowFile = (File) iterator.next();
			// 如果是文件夹进行嵌套循环
			if(nowFile.isDirectory() || nowFile.getAbsolutePath().endsWith(File.separator)) {
				fileLength = fileLength.add(new BigInteger("" + getFileSize(nowFile)));
				continue;
			}
			// 如果是文件添加文件的长度
			fileLength = fileLength.add(new BigInteger("" + nowFile.length()));
		}
		return fileLength;
	}
	
	/**
	 * 获取当前字符串中最前方的以G为单位的数值，不足1G则判断为1G
	 * @author 李鑫
	 * @param string 需要判断的字符串，如："50M2.6K48G6%/"
	 * @return <ul>
	 * 				<li>result:获得的整数</li>
	 * 				<li>string:出去此次结果后的字符串，如："50M2.6K48G6%/"，则返回 "2.6K48G6%/"</li>
	 * 			</ul>
	 */
	private static Map<String, Object> getDiskCapacityInteger(String string, String environment) {
		Map<String, Object> map = new HashMap<String, Object>(2);
		String G = "G";
		int gSize = 1;
		if(environment.indexOf("Mac") != -1) {
			G = "Gi";
			gSize = 2;
		}
		// 查询字符串中的
		int indexOfG = string.indexOf(G);
		int indexOfM = string.indexOf("M");
		int indexOfK = string.indexOf("K");
		// 如果有K存在的话确认K是否在第一位
		if(indexOfK != -1) {
			// 当前字符串第一位数的单位可能是K的情况
			if(indexOfG > indexOfK) {
				// 如果存在M那么第一位也有可能是M
				if(indexOfM != -1) {
					// 如果第一位是M的情况
					if(indexOfM < indexOfK) {
						map.put("result", 0);
						map.put("string", string.substring(indexOfM + gSize));
						return map;
					}
				}
				// 如果第一位是K的情况
				map.put("result", 0);
				map.put("string", string.substring(indexOfK + gSize));
				return map;
			}
		}
		// 如果有K存在的话确认M是否在第一位
		if(indexOfM != -1) {
			// 当前字符串第一位数的单位可能是M的情况
			if(indexOfG > indexOfM) {
				if(indexOfK != -1) {
					// 如果第一位是K的情况
					if(indexOfK < indexOfM) {
						map.put("result", 0);
						map.put("string", string.substring(indexOfK + gSize));
						return map;
					}
				}
				// 如果第一位是M的情况
				map.put("result", 1);
				map.put("string", string.substring(indexOfM + gSize));
				return map;
			}
		}
		// 如果当时第一位是G的情况
		String number = string.substring(0, indexOfG);
		// 判断是否有小数
		int indexOfSpot = number.indexOf(".");
		Integer usedSize = null;
		// 如果有小数则只取前面的整数部分
		if(indexOfSpot != -1) {
			usedSize = Integer.parseInt(number.substring(0, indexOfSpot));
		}
		if(indexOfSpot == -1) {
			usedSize = Integer.parseInt(number);
		}
		// 得到当前的容量数值
		map.put("result", usedSize.intValue());
		map.put("string", string.substring(indexOfG + gSize));
		return map;
	}
}
