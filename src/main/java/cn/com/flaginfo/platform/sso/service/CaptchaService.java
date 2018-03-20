package cn.com.flaginfo.platform.sso.service;

import cn.com.flaginfo.platform.api.common.base.BaseResponse;
import cn.com.flaginfo.platform.api.common.base.SingleValueJsonViewResponse;
import cn.com.flaginfo.platform.common.redis.RedisFacade;
import cn.com.flaginfo.platform.common.util.SystemMessage;
import cn.com.flaginfo.platform.global.api.MessageNoticeApi;
import cn.com.flaginfo.platform.sso.common.Constants;
import cn.com.flaginfo.platform.sso.common.vo.ReturnObject;
import cn.com.flaginfo.platform.ucenter.api.UserService;
import cn.com.flaginfo.platform.ucenter.api.util.Page;
import cn.com.flaginfo.platform.ucenter.api.vo.CommonCond;
import cn.com.flaginfo.platform.ucenter.api.vo.UserVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
public class CaptchaService {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());


    private final UserService userService;
    
    private final MessageNoticeApi messageNoticeApi;
    
    @Autowired
    public CaptchaService(UserService userService, MessageNoticeApi messageNoticeApi) {
        this.userService = userService;
        this.messageNoticeApi = messageNoticeApi;
    }
    
    
    public ReturnObject checkMobile(String mobile){
        ReturnObject returnObject = new ReturnObject();
        CommonCond commonCond = new CommonCond();
        commonCond.setProductId(SystemMessage.getString("productId"));
        BaseResponse<List<UserVO>> response = userService.getUserListByMobile(commonCond, mobile, new Page());
        if (response.getCode() != 200L || response.getData().size() == 0) {
            returnObject.setReturnCode(Constants.MOBILE_ERROR_CODE);
            returnObject.setMsg(Constants.MOBILE_IS_INVALID);
            return returnObject;
        }
    
        returnObject.setData(response.getData().get(0));
        return returnObject;
    }
    
    public void sendVerifyCodeMsg(String mobile,String content){
        SingleValueJsonViewResponse<List<String>> response = messageNoticeApi.sendVerifyCode(mobile,content,null,null,null,null,null);
        
        if(Constants.SUCCESS_CODE.equals(response.getReturnCode())){
            String verifyCode = (String) response.get("verifyCode");
            logger.info("发送手机验证码成功,mobile:{},code:{}",mobile,verifyCode);
            RedisFacade.set(Constants.VERIFY_MOBILE_ + mobile,verifyCode,60 * 5, TimeUnit.SECONDS);
        }
        logger.warn("发送手机验证码失败,mobile:{}",mobile);
    }
    
    public boolean checkMobileVerifyCode(String mobile,String code){
        if(StringUtils.isEmpty(mobile) || StringUtils.isEmpty(code)){
            return false;
        }
    
        String verifyCode =RedisFacade.get(Constants.VERIFY_MOBILE_ + mobile,String.class);
        
        return verifyCode.equals(code);
        
    }
    
    
}
