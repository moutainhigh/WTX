/*
 * PROJECT NAME: lt-trade
 * PACKAGE NAME: com.lt.trade.order.dao
 * FILE    NAME: CashOrderEntrustDao.java
 * COPYRIGHT: Copyright(c) 2017 LT All Rights Reserved.
 */ 
package com.lt.trade.order.dao;

import java.io.Serializable;

import com.lt.model.trade.OrderCashEntrustChildInfo;
import com.lt.model.trade.OrderCashEntrustInfo;

/**
 * 委托记录子数据数据接口
 * @author XieZhibing
 * @date 2016年12月12日 下午1:55:46
 * @version <b>1.0.0</b>
 */
public interface OrderCashEntrustChildInfoDao extends Serializable {
	
	/**
	 * 
	 * 添加委托记录
	 * @author XieZhibing
	 * @date 2016年12月12日 下午1:59:25
	 * @param entrustRecord
	 */
	public void add(OrderCashEntrustInfo entrustRecord);
	
	/**
	 * 
	 *  更新委托记录
	 * @author XieZhibing
	 * @date 2016年12月12日 下午1:59:36
	 * @param entrustRecord
	 */
	public int update(OrderCashEntrustInfo entrustRecord);
	
	
	/**
	 * 
	 * 根据报给交易所的委托编码查询委托记录
	 * @author XieZhibing
	 * @date 2016年12月12日 下午12:02:47
	 * @param entrustCode
	 * @return
	 */
	public OrderCashEntrustInfo queryByEntrustCode(String entrustCode);
	
	/**
	 * 
	 * 查询最后一个委托单号(报给交易所的)
	 * @author XieZhibing
	 * @date 2016年12月14日 下午8:18:49
	 * @return
	 */
	public String queryLastEntrustId();
}
