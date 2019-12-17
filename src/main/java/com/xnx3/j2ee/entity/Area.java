package com.xnx3.j2ee.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import static javax.persistence.GenerationType.IDENTITY;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 地区，省、市、区、经纬度
 */
@Entity
@Table(name = "area")
public class Area implements java.io.Serializable {

	// Fields

	private Integer id;
	private String province;	//省
	private String city;		//市
	private Integer district;	//区
	private float longitude;	//经度，如 118.22
	private float latitude;	//玮度，如 39.63
	
	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "id", unique = true, nullable = false)
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	/**
	 * 省
	 * @return
	 */
	public String getProvince() {
		return province;
	}
	/**
	 * 省
	 * @param province
	 */
	public void setProvince(String province) {
		this.province = province;
	}
	/**
	 * 市
	 * @return
	 */
	public String getCity() {
		return city;
	}
	/**
	 * 市
	 * @param city
	 */
	public void setCity(String city) {
		this.city = city;
	}
	/**
	 * 区
	 * @return
	 */
	public Integer getDistrict() {
		return district;
	}
	/**
	 * 区
	 * @param district
	 */
	public void setDistrict(Integer district) {
		this.district = district;
	}
	/**
	 * 经度，如 118.22
	 * @return
	 */
	public float getLongitude() {
		return longitude;
	}
	/**
	 * 经度，如 118.22
	 * @param longitude
	 */
	public void setLongitude(float longitude) {
		this.longitude = longitude;
	}
	/**
	 * 玮度，如 39.63
	 * @return
	 */
	public float getLatitude() {
		return latitude;
	}
	/**
	 * 玮度，如 39.63
	 * @param latitude
	 */
	public void setLatitude(float latitude) {
		this.latitude = latitude;
	}
	@Override
	public String toString() {
		return "Area [id=" + id + ", province=" + province + ", city=" + city + ", district=" + district
				+ ", longitude=" + longitude + ", latitude=" + latitude + "]";
	}
	
}