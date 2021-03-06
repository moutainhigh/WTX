/*
 * PROJECT NAME: lt-trade
 * PACKAGE NAME: com.lt.trade.order.service.impl
 * FILE    NAME: AbstractCashOrderBaseService.java
 * COPYRIGHT: Copyright(c) 2017 LT All Rights Reserved.
 */ 
package com.lt.trade.order.service.impl.cash;

import java.io.Serializable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSONObject;
import com.lt.api.fund.IFundTradeApiService;
import com.lt.enums.fund.FundTypeEnum;
import com.lt.enums.trade.DeferStatusEnum;
import com.lt.model.trade.OrderCashInfo;
import com.lt.util.utils.DoubleTools;
import com.lt.vo.fund.FundOrderVo;

/**
 * TODO（描述类的职责）
 * @author XieZhibing
 * @date 2016年12月13日 下午1:46:29
 * @version <b>1.0.0</b>
 */
public abstract class AbstractCashOrderBaseService implements Serializable{
	/**
	 * TODO（用一句话描述这个变量表示什么）
	 */
	private static final long serialVersionUID = 8571982298501138370L;
	/** 日志 */
	private static Logger logger = LoggerFactory.getLogger(AbstractCashOrderBaseService.class);
	
	/**
	 * TODO 退款
	 * @author XieZhibing
	 * @date 2016年12月12日 下午7:42:06
	 * @param cashOrdersInfo
	 * @param failCount
	 */
	public void refund(OrderCashInfo cashOrdersInfo, int failCount) {
		//有委托失败发生时，重新计算退款金额
		if(failCount > 0){
			long startTime = System.currentTimeMillis();
			//汇率
			Double rate = cashOrdersInfo.getRate();
			//退回递延费用
			double deferFund = 0.00;
			//递延状态
			if(DeferStatusEnum.DEFER.getValue() == cashOrdersInfo.getDeferStatus()){
				deferFund = DoubleTools.mul(DoubleTools.mul(cashOrdersInfo.getPerDeferFund(), rate ), failCount);
			}
			//每手止损保证金=每手止损金额+每手止损参数
			double perHoldFund = DoubleTools.add(cashOrdersInfo.getPerStopLoss() , cashOrdersInfo.getPerSurcharge());
			// 退回保证金
			double holdFund = DoubleTools.mul(DoubleTools.mul(perHoldFund,rate  ), failCount);
			// 退回手续费
			double actualCounterFee =DoubleTools.mul( DoubleTools.mul(cashOrdersInfo.getPerCounterFee(),rate  ),failCount );
			
			// 初始化订单资金对象
			FundOrderVo fundOrderVo = new FundOrderVo(
					FundTypeEnum.CASH, cashOrdersInfo.getProductName(),
					cashOrdersInfo.getOrderId(), cashOrdersInfo.getUserId(),
					holdFund, deferFund, actualCounterFee);
			logger.info("委托失败发生, 计算退款金额, failCount:{}, fundOrderVo:{}", failCount, JSONObject.toJSONString(fundOrderVo));
			
			logger.info("调用远程资金接口退款开始");
			//调用远程资金接口退款
			getFundTradeApiService().doRefund(fundOrderVo);
			logger.info("调用远程资金接口退款完成, time:{}ms", System.currentTimeMillis()-startTime);
		}	
		
	}

	/** 
	 * 获取 资金分布式接口: 资金扣款、结算、退款业务接口 
	 * @return fundTradeApiService 
	 */
	public abstract IFundTradeApiService getFundTradeApiService();
	
}
