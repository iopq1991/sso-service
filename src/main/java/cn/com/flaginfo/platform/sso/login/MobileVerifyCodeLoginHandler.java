package cn.com.flaginfo.platform.sso.login;

import cn.com.flaginfo.platform.api.common.base.BaseResponse;
import cn.com.flaginfo.platform.common.redis.RedisFacade;
import cn.com.flaginfo.platform.common.util.SystemMessage;
import cn.com.flaginfo.platform.sso.common.Constants;
import cn.com.flaginfo.platform.sso.common.vo.ReturnObject;
import cn.com.flaginfo.platform.sso.common.vo.TokenInfo;
import cn.com.flaginfo.platform.sso.common.vo.token.GetTokenVO;
import cn.com.flaginfo.platform.ucenter.api.EnterpriseService;
import cn.com.flaginfo.platform.ucenter.api.UserService;
import cn.com.flaginfo.platform.ucenter.api.util.Page;
import cn.com.flaginfo.platform.ucenter.api.vo.CommonCond;
import cn.com.flaginfo.platform.ucenter.api.vo.EnterpriseVO;
import cn.com.flaginfo.platform.ucenter.api.vo.UserVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Map;

/**
 * @author liangcai.liu
 * @version V1.0
 * Date:  2018/2/28
 * Time: 15:01
 */
@Component
public class MobileVerifyCodeLoginHandler implements LoginHandler{
    private final EnterpriseService enterpriseService;
    private final UserService userService;


    @Autowired
    public MobileVerifyCodeLoginHandler(EnterpriseService enterpriseService, UserService userService) {
        this.enterpriseService = enterpriseService;
        this.userService = userService;
    }

    @Override
    public boolean validateParam(GetTokenVO getTokenVO) {
        return getTokenVO != null
                && !StringUtils.isEmpty(getTokenVO.getVerifyCode())
                && !StringUtils.isEmpty(getTokenVO.getMobile());
    }

    @Override
    public ReturnObject authUser(GetTokenVO getTokenVO, Map<String, Object> resultMap, TokenInfo tokenInfo) {
        String mobile = getTokenVO.getMobile();
        String verifyCode = getTokenVO.getVerifyCode();
        if(!verifyCode.equals(RedisFacade.get(Constants.VERIFY_MOBILE_ + mobile,String.class))){
            return new ReturnObject(Constants.VERIFY_CODE_WRONG_CODE,Constants.VERIFY_CODE_WRONG_MSG);
        }


        CommonCond commonCond = new CommonCond();
        commonCond.setProductId(SystemMessage.getString("productId"));
        BaseResponse<List<UserVO>> response = userService.getUserListByMobile(commonCond,getTokenVO.getMobile(),new Page());
        if(!Constants.SUCCESS_CODE_LONG.equals(response.getCode()) || CollectionUtils.isEmpty(response.getData())){
            return new ReturnObject(Constants.MOBILE_ERROR_CODE,Constants.MOBILE_IS_INVALID);
        }

        UserVO userVO = response.getData().iterator().next();
        BaseResponse<List<EnterpriseVO>> entResp = enterpriseService.getEnterpriseListByUserId(Constants.PRODUCT_ID, userVO.getId(), new Page());
        resultMap.put("enterpriseVOList", entResp.getData());
        tokenInfo.setUserId(userVO.getId());
        tokenInfo.setUserName(userVO.getUserName());
        tokenInfo.setMobile(userVO.getMobile());
        return new ReturnObject();
    }
}
