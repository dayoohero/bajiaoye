package com.mobileAdHome.domain;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

@Entity
public class Category {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer catId;
	
	private String title;
	
	@OneToMany(mappedBy="category")
	private List<TemplateStorage> templateStorage;
	
	@OneToMany(mappedBy="category")
	private List<AppStorage> appStorage;


	public List<TemplateStorage> getTemplateStorage() {
		return templateStorage;
	}

	public void setTemplateStorage(List<TemplateStorage> templateStorage) {
		this.templateStorage = templateStorage;
	}

	public Integer getCatId() {
		return catId;
	}

	public void setCatId(Integer catId) {
		this.catId = catId;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public List<AppStorage> getAppStorage() {
		return appStorage;
	}

	public void setAppStorage(List<AppStorage> appStorage) {
		this.appStorage = appStorage;
	}
	

}
