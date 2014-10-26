package com.mobileAdHome.domain;

public class DrawWordsAnimate extends StartAnimate  {
	
	private String hollowImg;
	
	private String textImg;
	
	private String solidImg;
	
	private String coordinate;
	

	public String getCoordinate() {
		return coordinate;
	}

	public void setCoordinate(String coordinate) {
		this.coordinate = coordinate;
	}

	public String getHollowImg() {
		return hollowImg;
	}

	public void setHollowImg(String hollowImg) {
		this.hollowImg = hollowImg;
	}

	public String getSolidImg() {
		return solidImg;
	}

	public void setSolidImg(String solidImg) {
		this.solidImg = solidImg;
	}
	
	public String getTextImg() {
		return textImg;
	}

	public void setTextImg(String textImg) {
		this.textImg = textImg;
	}

}
