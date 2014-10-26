package com.mobileAdHome.core;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;



/**
 * 分页参数及查询结果封装.
 * 
 * 页码算法封装。
 * 
 * @param <T> Page中记录的类型.
 * 
 */
@SuppressWarnings("serial")
public class Page<T> implements java.io.Serializable{
	
	Logger logger = LoggerFactory.getLogger(getClass());

	//-- 分页参数 --//
	protected int pageNo = 1;
	protected int pageSize = 20;

	//-- 返回结果 --//
	protected List<T> result = new ArrayList<T>();
	

	
	protected long totalCount = 0;

	//-- 构造函数 --//
	public Page() {
	}

	public Page(int pageSize) {
		this.pageSize = pageSize;
	}

	//-- 分页参数访问函数 --//
	/**
	 * 获得当前页的页号,序号从1开始,默认为1.
	 */
	public int getPageNo() {
		return pageNo;
	}

	/**
	 * 设置当前页的页号,序号从1开始,低于1时自动调整为1.
	 */
	public void setPageNo(final int pageNo) {
		this.pageNo = pageNo;

		if (pageNo < 1) {
			this.pageNo = 1;
		}
	}

	/**
	 * 获得每页的记录数量, 默认为-1.
	 */
	public int getPageSize() {
		return pageSize;
	}

	/**
	 * 设置每页的记录数量.
	 */
	public void setPageSize(final int pageSize) {
		this.pageSize = pageSize;
	}

	/**
	 * 根据pageNo和pageSize计算当前页第一条记录在总结果集中的位置,序号从0开始.
	 */
	public int getPageFirst() {
		return ((pageNo - 1) * pageSize);
	}

	/**
	 * 根据pageNo和pageSize计算当前页最后一条记录在总结果集中的位置.
	 */
	public int getPageLast(){
		return (pageNo * pageSize);
	}

	//-- 访问查询结果函数 --//

	/**
	 * 获得页内的记录列表.
	 */
	public List<T> getResult() {
		return result;
	}

	/**
	 * 设置页内的记录列表.
	 */
	public void setResult(final List<T> result) {
		this.result = result;
	}

	/**
	 * 获得总记录数, 默认值为-1.
	 */
	public long getTotalCount() {
		return totalCount;
	}

	/**
	 * 设置总记录数.
	 */
	public void setTotalCount(final long totalCount) {
		this.totalCount = totalCount;
	}

	/**
	 * 根据pageSize与totalCount计算总页数, 默认值为-1.
	 */
	public long getTotalPages() {
		if (totalCount < 0) {
			return -1;
		}

		long count = totalCount / pageSize;
		if (totalCount % pageSize > 0) {
			count++;
		}
		return count;
	}

	/**
	 * 是否还有下一页.
	 */
	public boolean isHasNext() {
		return (pageNo + 1 <= getTotalPages());
	}

	/**
	 * 取得下页的页号, 序号从1开始.
	 * 当前页为尾页时仍返回尾页序号.
	 */
	public int getNextPage() {
		if (isHasNext()) {
			return pageNo + 1;
		} else {
			return pageNo;
		}
	}

	/**
	 * 是否还有上一页.
	 */
	public boolean isHasPre() {
		return (pageNo - 1 >= 1);
	}

	/**
	 * 取得上页的页号, 序号从1开始.
	 * 当前页为首页时返回首页序号.
	 */
	public int getPrePage() {
		if (isHasPre()) {
			return pageNo - 1;
		} else {
			return pageNo;
		}
	}
	
	
	/**
	 * 分页，简单风格
	 * @return
	 */
	public List<Integer> getPageNoListEasy(){
		Integer pageNum = Integer.valueOf(String.valueOf(this.getTotalPages()));
		if (pageNum < 1)
			return null;
		
		try{
			List<Integer> pageList = new ArrayList<Integer>(4);
			for(int i = 1;i<5;i++ ){
				pageList.add(i);
			}
			if(pageNum<5){
				for (int i = pageNum; i < 5; i++) {
					pageList.remove(pageNum.intValue());
				}
				return pageList;
			}
			
			if(pageNo>4){
				Integer startPage = (pageNo+2)<=pageNum?pageNo-1:((pageNo+1)<=pageNum?pageNo-2:pageNo-3);
				pageList.remove(0);
				pageList.add(0,startPage);
				pageList.remove(1);
				pageList.add(1,startPage+1);
				pageList.remove(2);
				pageList.add(2,startPage+2);
				pageList.remove(3);
				pageList.add(3,startPage+3);
			}
			if(pageNo>99){
				pageList.remove(0);
			}
			if(pageNo>999){
				pageList.remove(1);
			}
			if(pageNo>9999){
				pageList.remove(2);
			}
			return pageList;
		}catch(Exception e){
			logger.error("分页错误");
			logger.error(e.toString());
			return null;
		}
	}
	
	/**
	 * 分页的页码  常用风格
	 * @return
	 */
	public List<Integer> getPageNoList(){

		Integer pageNum = Integer.valueOf(String.valueOf(this.getTotalPages()));
		if (pageNum < 1)
			return null;
		try {
			List<Integer> pageList = new ArrayList<Integer>(10);
			for (int i = 1; i <= 10; i++) {
				Integer midInt = new Integer(i);
				pageList.add(midInt);
			}
			if (pageNum <= 10) {
				for (int i = pageNum; i < 10; i++) {
					pageList.remove(pageNum.intValue());
				}
				return pageList;
			}
			if (pageNo > 5) {
				pageList.remove(2);
				pageList.add(2, new Integer(-1));
			}
			if ((pageNum - pageNo) > 5) {
				pageList.remove(7);
				pageList.add(7, new Integer(-1));
				pageList.remove(8);
				pageList.add(8, new Integer(pageNum - 1));
				pageList.remove(9);
				pageList.add(9, new Integer(pageNum));
			}
			if (pageNo > 5 && (pageNum - pageNo) > 5) {
				int tmp = pageNo - 1;
				int rtmp = 3;
				for (int i = tmp; i <= (pageNo +2); i++) {
					pageList.remove(rtmp);
					pageList.add(rtmp, new Integer(i));
					rtmp++;
				}
			}
			if (pageNo <= 5) {
				int tmp = pageNo + 2;
				for (int i = tmp; i < 7 && pageList.size() > tmp; i++) {
					pageList.remove(tmp);
				}
			}

			if ((pageNum - pageNo) <= 5) {
				int tmp = pageNo - 2;
				int rtmp = 3;
				for (int i = 10; i > 3 && pageList.size() > rtmp; i--) {
					pageList.remove(rtmp);
				}
				for (int i = tmp; i <= pageNum && rtmp<10; i++) {
					pageList.add(rtmp, new Integer(i));
					rtmp++;
				}
			}
			return pageList;
		} catch (Exception e) {
			logger.error("分页错误");
			logger.error(e.toString());
			return null;
		}
	
	}
}
