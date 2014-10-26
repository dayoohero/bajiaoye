package com.mobileAdHome.dao;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import com.mobileAdHome.core.Page;

public abstract  class GenericDaoImpl<T extends Serializable ,PK extends Serializable> implements GenericDao<T,PK>{
    
	@PersistenceContext(unitName = "jpa.mobileAdHome")
	protected EntityManager em;
    
	private Class<T> entityClass;
	
	
	@SuppressWarnings("unchecked")
	public GenericDaoImpl(){
	  /**
	   * 获取泛型的实际类型
	   */
	  ParameterizedType type =	(ParameterizedType) this.getClass().getGenericSuperclass();
	  entityClass = (Class<T>)type.getActualTypeArguments()[0];

	}
	
	public void deleteObject(T obj) {
		// TODO Auto-generated method stub
		em.remove(em.merge(obj));
		
		
	}
	
	public T findObject(PK id) {
		// TODO Auto-generated method stub
		return (T)em.find(entityClass,id);
	}
	
	public void saveObject(T obj) {
		// TODO Auto-generated method stub
		em.persist(obj);
	}
	
	public void updateObject(T obj) {
		// TODO Auto-generated method stub
		em.merge(obj);		
	}
	
}