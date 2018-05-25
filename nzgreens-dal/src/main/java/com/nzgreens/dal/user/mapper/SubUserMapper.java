package com.nzgreens.dal.user.mapper;

import com.nzgreens.common.form.console.ChargeRecordForm;
import com.nzgreens.common.form.console.WithdrawRecordForm;
import com.nzgreens.common.model.console.ChargeRecordModel;
import com.nzgreens.common.model.console.WithdrawRecordModel;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface SubUserMapper {

	int reduceBalance(@Param("userId") Long userId, @Param("amount") Integer amount);

	int addBalance(@Param("userId") Long userId, @Param("amount") Integer amount);

	void updateProductSalesVolume(@Param("productId") Long productId,@Param("number") Long number);

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
