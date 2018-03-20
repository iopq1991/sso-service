package cn.com.flaginfo.platform.sso.common;

/**
 * @author chenglong.xu
 */

public interface Constants {
    
    String NULL_STR = "";

    String PRODUCT_ID = "3UDO5RAHPMBQEODP7ILJV3LLZY3ZK";
    
    Long SUCCESS_CODE_LONG = 200L;
    String SUCCESS_CODE = "200";
    String SUCCESS_MSG = "请求成功";
    
    String EMPTY_PARAM_CODE = "502";
    String EMPTY_PARAM_MSG = "请求参数不能为空";
    
    String WRONG_PASSWORD_CODE = "503";
    String WRONG_PASSWORD_MSG = "用户/密码无匹配";
    
    String LOGIN_TYPE_WRONG_CODE = "504";
    String LOGIN_TYPE_WRONG_MSG = "登录类型有误";
    
    String VERIFY_CODE_WRONG_CODE = "505";
    String VERIFY_CODE_WRONG_MSG = "验证码错误";
    
    String MOBILE_ERROR_CODE = "506";
    String MOBILE_IS_NULL = "手机号不能为空";
    String MOBILE_IS_INVALID = "手机号码无效";
    
    String PASSWORD_UPDATE_ERROR_CODE = "507";
    String PASSWORD_UPDATE_ERROR_MSG = "修改密码错误";
    
    String WE_CHAT_BIND_PASSWORD_NULL_CODE = "508";
    String WE_CHAT_BIND_PASSWORD_NULL_MSG = "微信绑定密码不能为空";
    
    String THIRD_PARTY_USER_BOUND_CODE = "509";
    String THIRD_PARTY_USER_BOUND_MSG = "用户已经绑定";
    
    String USER_SP_NOT_FOUND_CODE = "510";
    String USER_SP_NOT_FOUND_MSG = "未找到用户所在的企业列表";

    String USER_MEMBER_NOT_FOUND_CODE = "511";
    String USER_MEMBER_NOT_FOUND_MSG = "未找到用户成员信息";

    String THIRD_PART_CHOICE_SP_ERROR_CODE = "512";
    String THIRD_PART_CHOICE_SP_ERROR_MSG = "选择企业失败";

    String THIRD_PART_INFO_NOT_FOUND_ERROR_CODE = "513";
    String THIRD_PART_INFO_NOT_FOUND_ERROR_MSG = "第三方绑定信息未找到";

    String INVALID_TOKEN_ERROR_CODE = "514";
    String INVALID_TOKEN_ERROR_MSG = "无效的token";

    String THIRD_PART_UNBIND_ERROR_CODE = "515";
    String THIRD_PART_UNBIND_ERROR_MSG = "第三方绑定解绑失败";

    String ENTERPRISE_STATUS_ERROR_CODE = "516";
    String ENTERPRISE_STATUS_ERROR_MSG = "企业状态错误，无法选择";

    String MEMBER_STATUS_ERROR_CODE = "517";
    String MEMBER_STATUS_ERROR_MSG = "成员状态错误";

    /**
     * 企业 账号 密码 获取token
     */
     String LOGIN_TYPE_1 = "1";

    /**
     * 手机号 密码 获取token
     */
     String LOGIN_TYPE_2 = "2";

    /**
     * 手机号验证码获取token
     */
    String LOGIN_TYPE_3 = "3";

    /**
     * 登录来源 1 app 需要做防重复登录
     */
     String LOGIN_SOURCE_1 = "1";
    
     String SESSION_ = "session_";
    
     String VERIFY_MOBILE_ = "verify_mobile_";
    
     String TOKEN_COOKIE_NAME = "TOKEN_COOKIE_";
    
    // 第三方-微信登录
    Integer THIRD_PART_LOGIN_TYPE_WEIXIN = 1;
    // 第三方-钉钉登录
    Integer THIRD_PART_LOGIN_TYPE_DINGDING = 2;
    
    String USER_SERVICE_VERSION = "6.1.0";
    String USER_SERVICE_GROUP = "user-center-service";
    
    String COMMON_SERVICE_VERSION = "6.1.0";
    String COMMON_SERVICE_GROUP = "common-service";

    String PLATFORM_SERVICE_VERSION = "6.1.0" ;
    String PLATFORM_SERVICE_GROUP = "platform-service";

    String THIRD_PART_UNBIND_SUCCESS = "第三方登录解除绑定成功";

    Integer ENTERPRISE_STATUS_NORMAL = 0;
    Integer ENTERPRISE_STATUS_PRE_OPEN = 3;

    Integer MEMBER_STATUS_NORMAL = 0;

}
