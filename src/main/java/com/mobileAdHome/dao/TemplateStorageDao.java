package com.mobileAdHome.dao;

import java.util.List;

import javax.persistence.Query;

import org.springframework.stereotype.Repository;

import com.mobileAdHome.domain.TemplateStorage;
@Repository
public class TemplateStorageDao extends GenericDaoImpl<TemplateStorage, Integer>{
	public List<TemplateStorage> getAllTemplateStorage(){
		String hql = "from TemplateStorage order by templateId desc";
		Query query = em.createQuery(hql);
		return query.getResultList();
	}
}
