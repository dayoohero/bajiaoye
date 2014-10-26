package com.mobileAdHome.dao;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.Hibernate;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.Transformers;
import org.hibernate.type.Type;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.mobileAdHome.core.Page;
import com.mobileAdHome.core.ReflectionUtils;
//import org.springframework.util.ReflectionUtils;
//import com.uniqlickframework.modules.utils.AssertUtils;

/**
 * 封装Hibernate原生API的DAO泛型基类.
 * 
 * 直接使用Hibernate原生API.
 * 
 * @param <T>
 *            DAO操作的对象类型
 * @param <ID>
 *            主键类型
 * 
 * @author xiaozhou
 */
@SuppressWarnings("all")
public class HibernateDao<T, ID extends Serializable> {

	protected Logger logger = LoggerFactory.getLogger(getClass());

	protected SessionFactory sessionFactory;

	protected Class<T> entityClass;

	/**
	 * 通过子类的泛型定义取得对象类型Class. eg. public class UserDao extends
	 * SimpleHibernateDao<User, Long>
	 */
	public HibernateDao() {
		this.entityClass = ReflectionUtils.getSuperClassGenricType(getClass());
	}

	public HibernateDao(Class<T> entityClass) {
		this.entityClass = entityClass;
	}

	/**
	 * 取得sessionFactory.
	 */
	public SessionFactory getSessionFactory() {
		return sessionFactory;
	}

	/**
	 * 采用@Autowired按类型注入SessionFactory, 当有多个SesionFactory的时候在子类重载本函数.
	 */
	@Autowired
	public void setSessionFactory(final SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	/**
	 * 取得当前Session.
	 */
	public Session getSession() {
		return sessionFactory.getCurrentSession();
	}

	/**
	 * 保存新增或修改的对象.
	 */
	public void save(final T entity) {
		//AssertUtils.notNull(entity, "entity不能为空");
		getSession().saveOrUpdate(entity);
		logger.debug("save entity: {}", entity);
	}

	public void insert(final T entity){
		
		getSession().save(entity);
	}
	
	
	public void merge(final T entity) {
		//AssertUtils.notNull(entity, "entity不能为空");
		getSession().merge(entity);
		logger.debug("merge entity: {}", entity);
	}

	public void update(final T entity) {
		//AssertUtils.notNull(entity, "entity不能为空");
		getSession().update(entity);
		logger.debug("update entity: {}", entity);
	}

	/**
	 * 删除对象.
	 * 
	 * @param entity
	 *            对象必须是session中的对象或含id属性的transient对象.
	 */
	public void delete(final T entity) {
		//AssertUtils.notNull(entity, "entity不能为空");
		getSession().delete(entity);
		logger.debug("delete entity: {}", entity);
	}

	/**
	 * 按id删除对象.
	 */
	public void delete(final ID id) {
		//AssertUtils.notNull(id, "id不能为空");
		delete(get(id));
		logger.debug("delete entity {},id is {}", entityClass.getSimpleName(),
				id);
	}

	/**
	 * 按id获取对象.
	 */
	public T load(final ID id) {
		//AssertUtils.notNull(id, "id不能为空");
		return (T) getSession().load(entityClass, id);
	}

	/**
	 * 按id获取对象.
	 */
	public T get(final ID id) {
		//AssertUtils.notNull(id, "id不能为空");
		return (T) getSession().get(entityClass, id);
	}

	/**
	 * 按属性查找唯一对象, 匹配方式为相等.
	 */
	public T findUniqueBy(final String propertyName, final Object value) {
		//AssertUtils.hasText(propertyName, "propertyName不能为空");
		Criterion criterion = Restrictions.eq(propertyName, value);
		return (T) createCriteria(criterion).uniqueResult();
	}

	/**
	 * 按HQL查询唯一对象.
	 * 
	 * @param values
	 *            数量可变的参数,按顺序绑定.
	 */
	public <X> X findUnique(final String hql, final Object... values) {
		return (X) createQuery(hql, values).uniqueResult();
	}

	/**
	 * 按HQL查询唯一对象.
	 * 
	 * @param values
	 *            命名参数,按名称绑定.
	 */
	public <X> X findUnique(final String hql, final Map<String, ?> values) {
		return (X) createQuery(hql, values).uniqueResult();
	}

	/**
	 * 根据查询HQL与参数列表创建Query对象. 与find()函数可进行更加灵活的操作.
	 * 
	 * @param values
	 *            数量可变的参数,按顺序绑定.
	 */
	public Query createQuery(final String queryString, final Object... values) {
		//AssertUtils.hasText(queryString, "queryString不能为空");
		Query query = getSession().createQuery(queryString);
		if (values != null) {
			for (int i = 0; i < values.length; i++) {
				query.setParameter(i, values[i]);
			}
		}
		return query;
	}

	/**
	 * 根据查询HQL与参数列表创建Query对象. 与find()函数可进行更加灵活的操作.
	 * 
	 * @param values
	 *            命名参数,按名称绑定.
	 */
	public Query createQuery(final String queryString,
			final Map<String, ?> values) {
		//AssertUtils.hasText(queryString, "queryString不能为空");
		Query query = getSession().createQuery(queryString);
		if (values != null) {
			query.setProperties(values);
		}
		return query;
	}

	/**
	 * 按Criteria查询唯一对象.
	 * 
	 * @param criterions
	 *            数量可变的Criterion.
	 */
	public T findUnique(final Criterion... criterions) {
		return (T) createCriteria(criterions).uniqueResult();
	}

	/**
	 * 根据Criterion条件创建Criteria. 与find()函数可进行更加灵活的操作.
	 * 
	 * @param criterions
	 *            数量可变的Criterion.
	 */
	public Criteria createCriteria(final Criterion... criterions) {
		Criteria criteria = getSession().createCriteria(entityClass);
		for (Criterion c : criterions) {
			criteria.add(c);
		}
		return criteria;
	}

	/**
	 * 初始化对象. 使用load()方法得到的仅是对象Proxy, 在传到View层前需要进行初始化. 如果传入entity,
	 * 则只初始化entity的直接属性,但不会初始化延迟加载的关联集合和属性. 如需初始化关联属性,需执行:
	 * Hibernate.initialize(user.getRoles())，初始化User的直接属性和关联集合.
	 * Hibernate.initialize
	 * (user.getDescription())，初始化User的直接属性和延迟加载的Description属性.
	 */
	public void initProxyObject(Object proxy) {
		Hibernate.initialize(proxy);
	}

	/**
	 * Flush当前Session.
	 */
	public void flush() {
		getSession().flush();
	}

	/**
	 * 判断对象的属性值在数据库内是否唯一.
	 * 
	 * 在修改对象的情景下,如果属性新修改的值(value)等于属性原来的值(orgValue)则不作比较.
	 */
	public boolean isPropertyUnique(final String propertyName,
			final Object newValue, final Object oldValue) {
		if (newValue == null || newValue.equals(oldValue)) {
			return true;
		}
		Object object = findUniqueBy(propertyName, newValue);
		return (object == null);
	}

	// -- 分页查询函数 --//

//	/**
//	 * 分页获取全部对象.
//	 */
//	public Page<T> getAll(final Page<T> page) {
//		return findPage(page);
//	}

	public List<T> findAll(final String hql, final Object... values) {
		Query query = createQuery(hql, values);
		return query.list();
	}

	public List<Map<String, Object>> execSqlQueryToMap(final String sql,
			final Object[] args, final Map<String, Type> scalarMap) {
		SQLQuery query = getSession().createSQLQuery(sql);
		if (args != null && args.length > 0) {
			for (int i = 0; i < args.length; i++) {
				query.setParameter(i, args[i]);
			}
		}
		for (String key : scalarMap.keySet()) {
			query.addScalar(key, scalarMap.get(key));
		}
		return (List<Map<String, Object>>) query.setResultTransformer(
				Transformers.ALIAS_TO_ENTITY_MAP).list();
	}
	
	public List<List> execSqlQueryToList(final String sql,
			final Object[] args, final Map<String, Type> scalarMap) {
		SQLQuery query = getSession().createSQLQuery(sql);
		if (args != null && args.length > 0) {
			for (int i = 0; i < args.length; i++) {
				query.setParameter(i, args[i]);
			}
		}
		for (String key : scalarMap.keySet()) {
			query.addScalar(key, scalarMap.get(key));
		}
		return (List<List>) query.setResultTransformer(
				Transformers.TO_LIST).list();
	}

	public <E> List<E> execSqlQuery(final String sql, final Object[] args,
			final Map<String, Type> scalarMap, final Class<E> clazz) {
		SQLQuery query = getSession().createSQLQuery(sql);
		if (args != null && args.length > 0) {
			for (int i = 0; i < args.length; i++) {
				query.setParameter(i, args[i]);
			}
		}
		for (String key : scalarMap.keySet()) {
			query.addScalar(key, scalarMap.get(key));
		}
		return query.setResultTransformer(Transformers.aliasToBean(clazz))
				.list();
	}

	public List<T> execSqlQuery(final String sql, final String alias,
			final Object... values) {
		Query query = createSqlQuery(sql, alias, values);
		return query.list();
	}

	public Integer execSqlUpdate(final String sql, final Object... values) {
		Query query = createSqlQuery(sql, null, values);
		return query.executeUpdate();
	}

	public List<T> execSqlQuery(final String sql, final String alias,
			final Map bean) {
		Query query = createSqlQuery(sql, alias, bean);
		return query.list();
	}

	public Query createSqlQuery(final String sql, final String alias,
			final Object... values) {
		//AssertUtils.hasText(sql, "querySql不能为空");
		Query query = null;
		if (StringUtils.isNotEmpty(alias)) {
			query = getSession().createSQLQuery(sql).addEntity(alias,
					entityClass);
		} else {
			query = getSession().createSQLQuery(sql);
		}
		if (values != null ) {
			for (int i = 0; i < values.length; i++) {
				query.setParameter(i, values[i]);
			}
		}
		return query;
	}

	public Query createSqlQuery(final String sql, final String alias,
			final Map bean) {
		//AssertUtils.hasText(sql, "querySql不能为空");
		Query query = null;
		if (StringUtils.isNotEmpty(alias)) {
			query = getSession().createSQLQuery(sql).addEntity(alias,
					entityClass);
		} else {
			query = getSession().createSQLQuery(sql);
		}
		if (bean != null) {
			query.setProperties(bean);
		}
		return query;
	}

	/**
	 * 按HQL分页查询.
	 * 
	 * @param page
	 *            分页参数.
	 * @param hql
	 *            hql语句.
	 * @param values
	 *            数量可变的查询参数,按顺序绑定.
	 * 
	 * @return 分页查询结果, 附带结果列表及所有查询输入参数.
	 */
	public Page<T> findPage(final Page<T> page, final String hql,
			final Object... values) {
		//AssertUtils.notNull(page, "page不能为空");

		Query q = createQuery(hql, values);

		long totalCount = countHqlResult(hql, values);
		page.setTotalCount(totalCount);

		setPageParameterToQuery(q, page);

		List result = q.list();
		page.setResult(result);
		return page;
	}

//	public <V> Page<V> findPageSqlByNames(final Page<V> page, String sql,
//			Map<String,Object> parameters, Map<String, Type> scalarMap, Class<V> clazz) {
//		AssertUtils.notNull(page, "page is null.");
//		String countSql = "select count(*) from "
//				+ StringUtils.substringAfter(sql, "from");
//		SQLQuery query = getSession().createSQLQuery(countSql);
//		if(parameters != null && !parameters.isEmpty()){
//			for(Entry<String, Object> entry:parameters.entrySet()){
//				if(entry.getValue() instanceof Collection){
//					query.setParameterList(entry.getKey(), (Collection) entry.getValue());
//				} else if(entry.getValue() instanceof Object[]){
//					query.setParameterList(entry.getKey(), (Object[])entry.getValue());
//				} else {
//					query.setParameter(entry.getKey(), entry.getValue());
//				}
//			}
//		}
//		List list = query.list();
//		long totalCount = 0;
//		if (list != null && list.size() > 0) {
//			BigDecimal decimal = new BigDecimal(list.get(0).toString());
//			totalCount = decimal.longValue();
//		}
//		page.setTotalCount(totalCount);
//		sql += " limit " + page.getPageFirst() + "," + page.getPageSize();
//		List<V> resultList = execSqlQueryByName(sql, parameters, scalarMap, clazz);
//		page.setResult(resultList);
//		return page;
//	}
	
	public <E> List<E> execSqlQueryByName(final String sql, final Map<String,Object> parameters,
			final Map<String, Type> scalarMap, final Class<E> clazz) {
		SQLQuery query = getSession().createSQLQuery(sql);
		if(parameters != null && !parameters.isEmpty()){
			for(Entry<String, Object> entry:parameters.entrySet()){
				if(entry.getValue() instanceof Collection){
					query.setParameterList(entry.getKey(), (Collection) entry.getValue());
				} else if(entry.getValue() instanceof Object[]){
					query.setParameterList(entry.getKey(), (Object[])entry.getValue());
				} else {
					query.setParameter(entry.getKey(), entry.getValue());
				}
			}
		}
		for (String key : scalarMap.keySet()) {
			query.addScalar(key, scalarMap.get(key));
		}
		return query.setResultTransformer(Transformers.aliasToBean(clazz))
				.list();
	}
	
	public <V> Page<V> findPageSql(final Page<V> page, String sql,
			Object[] args, Map<String, Type> scalarMap, Class<V> clazz) {
		//AssertUtils.notNull(page, "page is null.");
		String countSql = "select count(*) from "
				+ StringUtils.substringAfter(sql, "from");
		SQLQuery query = getSession().createSQLQuery(countSql);
		if (args != null && args.length > 0) {
			for (int i = 0; i < args.length; i++) {
				query.setParameter(i, args[i]);
			}
		}
		List list = query.list();
		long totalCount = 0;
		if (list != null && list.size() > 0) {
			BigDecimal decimal = new BigDecimal(list.get(0).toString());
			totalCount = decimal.longValue();
		}
		page.setTotalCount(totalCount);
		sql += " limit " + page.getPageFirst() + "," + page.getPageSize();
		List<V> resultList = execSqlQuery(sql, args, scalarMap, clazz);
		page.setResult(resultList);
		return page;
	}

	public Page<T> findPageSql(final Page<T> page, String sql) {
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
		List<T> rs = getSession().createSQLQuery(sql).addEntity(entityClass)
				.list();
		page.setResult(rs);
		return page;
	}

	/**
	 * 按HQL分页查询.
	 * 
	 * @param page
	 *            分页参数.
	 * @param hql
	 *            hql语句.
	 * @param values
	 *            命名参数,按名称绑定.
	 * 
	 * @return 分页查询结果, 附带结果列表及所有查询输入参数.
	 */
	public Page<T> findPage(final Page<T> page, final String hql,
			final Map<String, ?> values) {
		//AssertUtils.notNull(page, "page不能为空");

		Query q = createQuery(hql, values);

		long totalCount = countHqlResult(hql, values);
		page.setTotalCount(totalCount);

		setPageParameterToQuery(q, page);

		List result = q.list();
		page.setResult(result);
		return page;
	}

//	/**
//	 * 按Criteria分页查询.
//	 * 
//	 * @param page
//	 *            分页参数.
//	 * @param criterions
//	 *            数量可变的Criterion.
//	 * 
//	 * @return 分页查询结果.附带结果列表及所有查询输入参数.
//	 */
//	public Page<T> findPage(final Page<T> page, final Criterion... criterions) {
//		//AssertUtils.notNull(page, "page不能为空");
//
//		Criteria c = createCriteria(criterions);
//
//		long totalCount = countCriteriaResult(c);
//		page.setTotalCount(totalCount);
//
//		setPageParameterToCriteria(c, page);
//
//		List result = c.list();
//		page.setResult(result);
//		return page;
//	}

	/**
	 * 设置分页参数到Criteria对象,辅助函数.
	 */
	protected Criteria setPageParameterToCriteria(final Criteria c,
			final Page<T> page) {
		//AssertUtils.isTrue(page.getPageSize() > 0,
		//		"Page Size must larger than zero");

		c.setFirstResult(page.getPageFirst());
		c.setMaxResults(page.getPageSize());
		return c;
	}

//	/**
//	 * 执行count查询获得本次Criteria查询所能获得的对象总数.
//	 */
//	protected long countCriteriaResult(final Criteria c) {
//		CriteriaImpl impl = (CriteriaImpl) c;
//
//		// 先把Projection、ResultTransformer、OrderBy取出来,清空三者后再执行Count操作
//		Projection projection = impl.getProjection();
//		ResultTransformer transformer = impl.getResultTransformer();
//
//		List<CriteriaImpl.OrderEntry> orderEntries = null;
//		try {
//			orderEntries = (List) ReflectionUtils.getFieldValue(impl,
//					"orderEntries");
//			ReflectionUtils
//					.setFieldValue(impl, "orderEntries", new ArrayList());
//		} catch (Exception e) {
//			logger.error("不可能抛出的异常:{}", e.getMessage());
//		}
//
//		// 执行Count查询
//		Long totalCountObject = (Long) c.setProjection(Projections.rowCount())
//				.uniqueResult();
//		long totalCount = (totalCountObject != null) ? totalCountObject : 0;
//
//		// 将之前的Projection,ResultTransformer和OrderBy条件重新设回去
//		c.setProjection(projection);
//
//		if (projection == null) {
//			c.setResultTransformer(CriteriaSpecification.ROOT_ENTITY);
//		}
//		if (transformer != null) {
//			c.setResultTransformer(transformer);
//		}
//		try {
//			ReflectionUtils.setFieldValue(impl, "orderEntries", orderEntries);
//		} catch (Exception e) {
//			logger.error("不可能抛出的异常:{}", e.getMessage());
//		}
//
//		return totalCount;
//	}

	/**
	 * 执行count查询获得本次Hql查询所能获得的对象总数.
	 * 
	 * 本函数只能自动处理简单的hql语句,复杂的hql查询请另行编写count语句查询.
	 */
	protected long countHqlResult(final String hql, final Object... values) {
		String countHql = prepareCountHql(hql);

		try {
			Long count = findUnique(countHql, values);
			return count;
		} catch (Exception e) {
			throw new RuntimeException("hql can't be auto count, hql is:"
					+ countHql, e);
		}
	}

	/**
	 * 执行count查询获得本次Hql查询所能获得的对象总数.
	 * 
	 * 本函数只能自动处理简单的hql语句,复杂的hql查询请另行编写count语句查询.
	 */
	protected long countHqlResult(final String hql, final Map<String, ?> values) {
		String countHql = prepareCountHql(hql);

		try {
			Long count = findUnique(countHql, values);
			return count;
		} catch (Exception e) {
			throw new RuntimeException("hql can't be auto count, hql is:"
					+ countHql, e);
		}
	}

	private String prepareCountHql(String orgHql) {
		String fromHql = orgHql;
		// select子句与order by子句会影响count查询,进行简单的排除.
		fromHql = "from " + StringUtils.substringAfter(fromHql, "from");
		fromHql = StringUtils.substringBefore(fromHql, "order by");

		String countHql = "select count(*) " + fromHql;
		return countHql;
	}

	/**
	 * 设置分页参数到Query对象,辅助函数.
	 */
	protected Query setPageParameterToQuery(final Query q, final Page<T> page) {
		//AssertUtils.isTrue(page.getPageSize() > 0,
		//		"Page Size must larger than zero");

		q.setFirstResult(page.getPageFirst());
		q.setMaxResults(page.getPageSize());

		return q;
	}
}