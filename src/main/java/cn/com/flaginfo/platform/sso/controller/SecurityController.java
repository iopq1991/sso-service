package cn.com.flaginfo.platform.sso.controller;

import cn.com.flaginfo.platform.sso.common.Constants;
import cn.com.flaginfo.platform.sso.common.vo.ReturnObject;
import cn.com.flaginfo.platform.sso.service.CaptchaService;
import cn.com.flaginfo.platform.sso.service.SecurityService;
import cn.com.flaginfo.platform.sso.vo.UpdatePasswordParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/security")
public class SecurityController {
    
    private final CaptchaService captchaService;
    
    private final SecurityService securityService;
    
    @Autowired
    public SecurityController(CaptchaService captchaService, SecurityService securityService) {
        this.captchaService = captchaService;
        this.securityService = securityService;
    }
    
    @RequestMapping("/update/password")
    @ResponseBody
    public ReturnObject updatePassword(@RequestBody UpdatePasswordParam updatePasswordParam){
        ReturnObject returnObject = new ReturnObject();
        String mobile = updatePasswordParam.getMobile();
        String newPassword = updatePasswordParam.getNewPassword();
        Long userId = updatePasswordParam.getUserId();
        String verifyCode = updatePasswordParam.getVerifyCode();
        
        if(StringUtils.isEmpty(mobile) ||
                StringUtils.isEmpty(newPassword)||
                StringUtils.isEmpty(userId)||
                StringUtils.isEmpty(verifyCode)){
            returnObject.setReturnCode(Constants.EMPTY_PARAM_CODE);
            returnObject.setMsg(Constants.EMPTY_PARAM_MSG);
            return returnObject;
        }
    
        if(!captchaService.checkMobileVerifyCode(mobile,verifyCode)){
            returnObject.setReturnCode(Constants.VERIFY_CODE_WRONG_CODE);
            returnObject.setMsg(Constants.VERIFY_CODE_WRONG_MSG);
            return returnObject;
        }
        
        return securityService.updatePassword(userId,newPassword);
    }
    
}
