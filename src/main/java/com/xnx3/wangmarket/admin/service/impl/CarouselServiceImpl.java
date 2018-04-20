package com.xnx3.wangmarket.admin.service.impl;

import java.util.List;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;
import com.xnx3.j2ee.dao.SqlDAO;
import com.xnx3.wangmarket.admin.entity.Carousel;
import com.xnx3.wangmarket.admin.service.CarouselService;

@Service("carouselService")
public class CarouselServiceImpl implements CarouselService {

	@Resource
	private SqlDAO sqlDAO;

	public List<Carousel> findBySiteid(int siteid) {
		// TODO Auto-generated method stub
		return sqlDAO.findBySqlQuery("SELECT * FROM carousel WHERE siteid = "+siteid+" ORDER BY rank ASC", Carousel.class);
	}
	
	public Carousel findAloneBySiteid(int siteid, short type) {
		Carousel c = sqlDAO.findAloneBySqlQuery("SELECT * FROM carousel WHERE siteid = "+siteid+" AND type = "+type+" ORDER BY rank ASC", Carousel.class);
		if(c == null){
			return null;
		}else{
			return c;
		}
	}
	
	
}
