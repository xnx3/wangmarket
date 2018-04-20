package com.xnx3.j2ee.generateCache;
import java.util.List;
import com.xnx3.Lang;

/**
 * 信息相关数据缓存生成
 * @author 管雷鸣
 *
 */
public class Log extends BaseGenerate {

	/**
	 * log.type 值－描述 缓存
	 */
	public void type(List<String> list){
		createCacheObject("type");
		
		for (int i = 0; i < list.size(); i++) {
    		String[] array = list.get(i).split("#");
    		String name = array[0];
    		Short value = (short) Lang.stringToInt(array[1], 0);
    		String description = array[2];
    		cacheAdd(value, description);
		}
		generateCacheFile();
	}
	
}
