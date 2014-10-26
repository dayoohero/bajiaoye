package com.mobileAdHome.dao;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

public class JpaHibernateDao {

	@PersistenceContext(unitName = "jpa.mobileAdHome")
	protected EntityManager entityManager;

}
