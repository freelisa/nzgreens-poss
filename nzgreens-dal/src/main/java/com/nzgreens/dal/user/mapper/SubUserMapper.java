package com.nzgreens.dal.user.mapper;

import com.nzgreens.common.form.console.ChargeRecordForm;
import com.nzgreens.common.form.console.WithdrawRecordForm;
import com.nzgreens.common.model.console.BatchUpdateProductNumberModel;
import com.nzgreens.common.model.console.ChargeRecordModel;
import com.nzgreens.common.model.console.WithdrawRecordModel;
import com.nzgreens.dal.user.example.Orders;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface SubUserMapper {

	/**
	 * 扣除余额
	 * @param userId
	 * @param amount
	 * @return
	 */
	int reduceBalance(@Param("userId") Long userId, @Param("amount") Integer amount);

	/**
	 * 新增余额
	 * @param userId
	 * @param amount
	 * @return
	 */
	int addBalance(@Param("userId") Long userId, @Param("amount") Integer amount);

	/**
	 * 修改销量
	 * @param productId
	 * @param number
	 */
	void updateProductSalesVolume(@Param("productId") Long productId,@Param("number") Long number);

	/**
	 * 批量修改库存
	 * @param list
	 */
	void batchUpdateProductNumber(List<Orders> list);

	/**
	 * 充值列表
	 * @param form
	 * @return
	 */
	List<ChargeRecordModel> selectChargeRecordForPage(ChargeRecordForm form);

	/**
	 * 提现列表
	 * @param form
	 * @return
	 */
	List<WithdrawRecordModel> selectWithdrawRecordForPage(WithdrawRecordForm form);
}
