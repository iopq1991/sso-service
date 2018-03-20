package cn.com.flaginfo.platform.sso.service;

import cn.com.flaginfo.platform.api.common.base.BaseResponse;
import cn.com.flaginfo.platform.common.util.SystemMessage;
import cn.com.flaginfo.platform.sso.common.Constants;
import cn.com.flaginfo.platform.sso.common.vo.ReturnObject;
import cn.com.flaginfo.platform.ucenter.api.UserService;
import cn.com.flaginfo.platform.ucenter.api.vo.CommonCond;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
public class SecurityService {
    
    private final UserService userService;
    
    @Autowired
    public SecurityService(UserService userService) {
        this.userService = userService;
    }
    
    public ReturnObject updatePassword(Long userId, String password){
        ReturnObject returnObject = new ReturnObject();
    
        if(StringUtils.isEmpty(userId)||
                StringUtils.isEmpty(password)){
            returnObject.setReturnCode(Constants.EMPTY_PARAM_CODE);
            returnObject.setMsg(Constants.EMPTY_PARAM_MSG);
            return returnObject;
        }
        
        CommonCond commonCond = new CommonCond();
        commonCond.setProductId(SystemMessage.getString("productId"));
        BaseResponse<Boolean> response = userService.updateUserPassWord(commonCond, userId, password);
        if (response.getCode() != 200L || !response.getData()) {
            returnObject.setReturnCode(Constants.PASSWORD_UPDATE_ERROR_CODE);
            returnObject.setMsg(Constants.PASSWORD_UPDATE_ERROR_MSG);
        }
        
        return returnObject;
    }
    
}
