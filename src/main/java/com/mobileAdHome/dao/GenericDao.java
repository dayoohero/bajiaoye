package com.mobileAdHome.dao;

import java.io.Serializable;

public interface GenericDao<T extends Serializable,PK extends Serializable> {
    
	/**
	 * 保存一个对象
	 * @param obj
	 */
	void saveObject(T obj);
	/**
	 * 删除一个对象
	 * @param obj
	 */
	void deleteObject(T obj);
	/**
	 * 更新一个对象
	 * @param ojb
	 */
	void updateObject(T ojb);
	/**
	 * 根据主键id找到一个对象
	 * @param id
	 */
	T findObject(PK id);
	
}



