package com.wxwall.modules.user.entity;

import javax.persistence.Entity;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import com.wxwall.common.entity.BaseEntity;

/**
 * 付费套餐
 * 
 * @author Locki<lockiyang@qq.com>
 * @since 2015年2月28日
 * 
 */
@Entity
@Table(name = "t_vippackage")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
// 二级缓存
public class VipPackage extends BaseEntity {

	private static final long serialVersionUID = 1L;

	private String name; // 套餐名称
	private String description; // 说明和描述
	private long price; // 价格（元）
	private int validMouth; // 有效时间（月）
	private String image; // 图片地址

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public long getPrice() {
		return price;
	}

	public void setPrice(long price) {
		this.price = price;
	}

	public int getValidMouth() {
		return validMouth;
	}

	public void setValidMouth(int validMouth) {
		this.validMouth = validMouth;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

}
