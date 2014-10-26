package com.mobileAdHome.domain;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;

import com.mobileAdHome.util.AppPathUtil;
import com.mobileAdHome.util.AppStorageUtil;

@Entity
public class AppStorage implements Serializable {
	
	public AppStorage() {
		super();
		this.createTime = Calendar.getInstance().getTime();
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer appId;
	
	@ManyToOne
	@JoinColumn(name="catId")
	private Category category;
	
	@ManyToOne
	@JoinColumn(name="user_id")
	private UserInfo userInfo;
	
	@Transient
	private RealApp realApp;	

	public UserInfo getUserInfo() {
		return userInfo;
	}

	public void setUserInfo(UserInfo userInfo) {
		this.userInfo = userInfo;
	}

	@Column(length=2000)
	private String jsonString;
	
	private Date createTime;
	
	public Date updateTime;

	private String coverImg;
	
	private String description;
	

	public Date getCreateTime() {
		return createTime;
	}

	
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public String getJsonString() {
		return jsonString;
	}

	public void setJsonString(String jsonString) {
		this.jsonString = jsonString;
	}

	public Category getCategory() {
		return category;
	}

	public void setCategory(Category category) {
		this.category = category;
	}

	public Integer getAppId() {
		return appId;
	}

	public void setAppId(Integer appId) {
		this.appId = appId;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	public RealApp getRealApp() {
		return realApp;
	}

	public void setRealApp(RealApp realApp) {
		this.realApp = realApp;
	}

	public String getCoverImg() {
		return coverImg;
	}

	public void setCoverImg(String coverImg) {
		this.coverImg = coverImg;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public AppStorage cloneAppStorage(){
		AppStorage appStorage = new AppStorage();
		appStorage.setCategory(this.category);
		appStorage.setCoverImg(this.coverImg);
		appStorage.setDescription(this.description);
		appStorage.setCreateTime(new Date());
		appStorage.setUserInfo(this.userInfo);
		appStorage.setJsonString(this.jsonString);
		appStorage.setRealApp(AppStorageUtil.JsonObjectToRealApp(this.jsonString));
		return appStorage;
	}
	
	public String getAppPath(){
		return AppPathUtil.getAppPath("app",this.appId);
	}
	
	public String getRelativeAppPath(){
		return AppPathUtil.getRelativeAppPath("app",this.appId);
	}
}
