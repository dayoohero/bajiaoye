package com.mobileAdHome.dao;

import java.util.List;

import javax.persistence.Query;

import org.springframework.stereotype.Repository;

import com.mobileAdHome.core.Page;
import com.mobileAdHome.domain.AppStorage;

@Repository
public class AppStorageDao extends GenericDaoImpl<AppStorage,Integer> {
	
	public Page<AppStorage> getAppListByUser(Page<AppStorage> page,Integer userId){
		String sql = "select app_id as appId,create_time as createTime,json_string as jsonString from app_storage where user_id = " + userId;
		String countSql = "select count(*) from app_storage where user_id = " + userId;
		page.setTotalCount(em.createNativeQuery(countSql).getFirstResult());
		
		//em.createNativeQuery(sqlString, resultClass)
		sql += " order by update_time desc ";
		sql += " limit " + page.getPageFirst() + "," + page.getPageSize();
		List<AppStorage> rs = em.createNativeQuery(sql).getResultList();
		page.setResult(rs);
		return page;
	}
	
	public Page<AppStorage> getHAppListByUser(Page<AppStorage> page,Integer userId){
		String hql = "from AppStorage where userInfo.userId = " + userId;
		String countSql = "select count(*) from app_storage where user_id = " + userId;
		page.setTotalCount(em.createNativeQuery(countSql).getFirstResult());
		
		//em.createNativeQuery(sqlString, resultClass)
		hql += " order by updateTime desc ";
		//hql += " limit " + page.getPageFirst() + "," + page.getPageSize();
		Query query = em.createQuery(hql);
		query.setFirstResult(page.getPageFirst());
		query.setMaxResults( page.getPageSize());
		List<AppStorage> rs = query.getResultList();
		page.setResult(rs);
		return page;
	}
	
	/*public Page<T> findPageSql(final Page<T> page, String sql) {
		//AssertUtils.notNull(page, "page不能为空");
		String countSql = "select count(*) from "
				+ StringUtils.substringAfter(sql, "from");
		countSql = StringUtils.substringBefore(countSql, "order by");
		List list = getSession().createSQLQuery(countSql).list();
		Integer totalCount = 0;
		if (list != null && list.size() > 0) {

			BigInteger c = (BigInteger) list.get(0);
			totalCount = c.intValue();
		}
		page.setTotalCount(totalCount);
		sql += " limit " + page.getPageFirst() + "," + page.getPageSize();
		List<T> rs = em.createNativeQuery(sql, );
				.list();
		page.setResult(rs);
		return page;
	}*/

}
