package com.nzgreens.common.exception;

/**
 * @author lenovo
 * @version V1.0
 */
public class ErrorCodes {

	/**
	 * =============== 公共异常 ==================
	 */
	public static final String SYSTEM_ERROR = "00001";

	public static final String ID_ILLEGAL = "00002";

	public static final String ILLEGAL_PARAM = "00003";

	public static final String UPDATE_ERROR = "00004";

	public static final String DATA_ILLEGAL = "10000";

	public static final String TITLE_ILLEGAL = "10001";

	public static final String WEIGHT_ILLEGAL = "10002";

	public static final String COST_PRICE_ILLEGAL = "10003";

	public static final String SELL_PRICE_ILLEGAL = "10004";

	public static final String SYSTEM_BALANCE_NOT_ENOUGH = "10005";

	public static final String STOCK_ILLEGAL = "10006";

	public static final String PRODUCT_IMAGE_ILLEGAL = "10007";

	/**
	 * =============== 用户异常 ==================
	 */
	public static final String USER_NOT_EXIST_ILLEGAL = "20000";

	public static final String USER_MOBILE_ILLEGAL = "20001";

	public static final String USER_TYPE_ILLEGAL = "20002";

	public static final String USER_PASSWORD_ILLEGAL = "20003";

	public static final String USER_BALANCE_ILLEGAL = "20004";

	public static final String BALANCE_NOT_ENOUGH = "20005";

	public static final String USER_MOBILE_ERROR = "20006";

	public static final String USER_AGENT_SETTING_ERROR = "20007";

	public static final String USER_AGENT_NOT_NULL = "20008";

	public static final String USER_AGENT_NOT_UPDATE_TYPE = "20009";

	public static final String USER_ORDER_STATUS_ERROR = "20010";

	/**
	 * =============== 代理返佣异常 ==================
	 */
	public static final String AGENT_USER_ID_ILLEGAL = "30000";

	public static final String ORDER_REBATE_ILLEGAL = "30001";

	public static final String MONTH_REBATE_ILLEGAL = "30002";

	public static final String REBATE_STATUS_ILLEGAL = "30003";

	public static final String REBATE_STATUS_HANDLE_SUCC = "30004";

    public static final String REBATE_AMOUNT_ILLEGAL = "30005";

	/**
	 * =============== 订单凭证异常 ==================
	 */
	public static final String ORDER_NUMBER_ILLEGAL = "40000";

	public static final String ORDER_CERT_URL_ILLEGAL = "40001";

	public static final String ORDER_STATUS_ILLEGAL = "40002";

	public static final String ORDER_TYPE_ILLEGAL = "40003";

	public static final String LOGISTICS_NUMBER_ILLEGAL = "40004";

	/**
	 * =============== 产品分类 ==================
	 */
	public static final String PRODUCT_BRAND_NAME_ILLEGAL = "50000";

	public static final String PRODUCT_CATEGORY_NAME_ILLEGAL = "50001";

	public static final String PRODUCT_STATUS_ILLEGAL = "50002";

	public static final String COIN_MONEY_ILLEGAL = "50003";

	public static final String COIN_ILLEGAL = "50004";

	public static final String PRODUCT_WEIGHT_ILLEGAL = "50005";

	public static final String FREIGHT_ILLEGAL = "50006";

	public static final String SEARCH_KEYWORD_ILLEGAL = "50007";

	public static final String PRODUCTS_STATUS_ILLEGAL = "50008";

	public static final String PRODUCTS_IS_VALID_ILLEGAL = "50009";
}
