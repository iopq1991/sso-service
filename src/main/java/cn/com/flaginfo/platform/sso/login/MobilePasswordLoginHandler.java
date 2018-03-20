package cn.com.flaginfo.platform.sso.login;

import cn.com.flaginfo.platform.api.common.base.BaseResponse;
import cn.com.flaginfo.platform.sso.common.Constants;
import cn.com.flaginfo.platform.sso.common.vo.ReturnObject;
import cn.com.flaginfo.platform.sso.common.vo.TokenInfo;
import cn.com.flaginfo.platform.sso.common.vo.token.GetTokenVO;
import cn.com.flaginfo.platform.ucenter.api.AuthService;
import cn.com.flaginfo.platform.ucenter.api.EnterpriseService;
import cn.com.flaginfo.platform.ucenter.api.util.Page;
import cn.com.flaginfo.platform.ucenter.api.vo.EnterpriseVO;
import cn.com.flaginfo.platform.ucenter.api.vo.UserVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
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
public class MobilePasswordLoginHandler implements LoginHandler{
    private final EnterpriseService enterpriseService;
    private final AuthService authService;


    @Autowired
    public MobilePasswordLoginHandler(EnterpriseService enterpriseService, AuthService authService) {
        this.enterpriseService = enterpriseService;
        this.authService = authService;
    }

    @Override
    public boolean validateParam(GetTokenVO getTokenVO) {
        return getTokenVO != null
                && !StringUtils.isEmpty(getTokenVO.getPassword())
                && !StringUtils.isEmpty(getTokenVO.getMobile());
    }

    @Override
    public ReturnObject authUser(GetTokenVO getTokenVO, Map<String, Object> resultMap, TokenInfo tokenInfo) {
        BaseResponse<UserVO> resp = authService.authUserByMobile(Constants.PRODUCT_ID, getTokenVO.getMobile(), getTokenVO.getPassword());
        if (Constants.SUCCESS_CODE_LONG.equals(resp.getCode())) {
            UserVO userVO = resp.getData();
            BaseResponse<List<EnterpriseVO>> entResp = enterpriseService.getEnterpriseListByUserId(Constants.PRODUCT_ID, userVO.getId(), new Page());
            resultMap.put("enterpriseVOList", entResp.getData());
            tokenInfo.setUserId(userVO.getId());
            tokenInfo.setUserName(userVO.getUserName());
            tokenInfo.setMobile(userVO.getMobile());
        }else{
            return new ReturnObject(Constants.WRONG_PASSWORD_CODE,Constants.WRONG_PASSWORD_MSG);
        }
        return new ReturnObject();
    }
}
