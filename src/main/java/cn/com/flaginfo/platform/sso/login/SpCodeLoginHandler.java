package cn.com.flaginfo.platform.sso.login;

import cn.com.flaginfo.platform.api.common.base.BaseResponse;
import cn.com.flaginfo.platform.sso.common.Constants;
import cn.com.flaginfo.platform.sso.common.vo.ReturnObject;
import cn.com.flaginfo.platform.sso.common.vo.TokenInfo;
import cn.com.flaginfo.platform.sso.common.vo.token.GetTokenVO;
import cn.com.flaginfo.platform.ucenter.api.AuthService;
import cn.com.flaginfo.platform.ucenter.api.EnterpriseService;
import cn.com.flaginfo.platform.ucenter.api.MemberService;
import cn.com.flaginfo.platform.ucenter.api.util.Page;
import cn.com.flaginfo.platform.ucenter.api.vo.CommonCond;
import cn.com.flaginfo.platform.ucenter.api.vo.EnterpriseVO;
import cn.com.flaginfo.platform.ucenter.api.vo.MemberVO;
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
 * Time: 14:50
 */
@Component
public class SpCodeLoginHandler implements LoginHandler{

    private final EnterpriseService enterpriseService;

    private final AuthService authService;

    private final MemberService memberService;

    @Autowired
    public SpCodeLoginHandler(EnterpriseService enterpriseService, AuthService authService, MemberService memberService) {
        this.enterpriseService = enterpriseService;
        this.authService = authService;
        this.memberService = memberService;
    }

    @Override
    public boolean validateParam(GetTokenVO getTokenVO) {
        return getTokenVO != null
                && !StringUtils.isEmpty(getTokenVO.getPassword())
                && !StringUtils.isEmpty(getTokenVO.getSpCode())
                && !StringUtils.isEmpty(getTokenVO.getName());
    }

    @Override
    public ReturnObject authUser(GetTokenVO getTokenVO, Map<String, Object> resultMap, TokenInfo tokenInfo) {
        String spCode = getTokenVO.getSpCode();
        BaseResponse<UserVO> resp = authService.authUserBySpCode(Constants.PRODUCT_ID, spCode, getTokenVO.getName(), getTokenVO.getPassword());
        if (Constants.SUCCESS_CODE_LONG.equals(resp.getCode())) {
            UserVO userVO = resp.getData();
            CommonCond commonCond = new CommonCond();
            commonCond.setProductId(Constants.PRODUCT_ID);
            commonCond.setSpCode(spCode);
            EnterpriseVO enterpriseVO = enterpriseService.getEnterpriseDetail(commonCond).getData();
            commonCond.setSpId(enterpriseVO.getSpId());
            BaseResponse<List<MemberVO>> memberList = memberService.getMemberListByUserId(commonCond, userVO.getId(), new Page());
            if(memberList == null || memberList.getData() == null || CollectionUtils.isEmpty(memberList.getData())){
                return new ReturnObject(Constants.USER_MEMBER_NOT_FOUND_CODE,Constants.USER_MEMBER_NOT_FOUND_MSG);
            }
            MemberVO memberVO = memberList.getData().get(0);
            if(!Constants.MEMBER_STATUS_NORMAL.equals(memberVO.getStatus())){
                return new ReturnObject(Constants.MEMBER_STATUS_ERROR_CODE,Constants.MEMBER_STATUS_ERROR_MSG);
            }

            tokenInfo.setMemberId(memberVO.getId());
            tokenInfo.setMemberName(memberVO.getName());
            tokenInfo.setUserId(userVO.getId());
            tokenInfo.setUserName(userVO.getUserName());
            tokenInfo.setMobile(userVO.getMobile());
            tokenInfo.setSpId(enterpriseVO.getSpId());
        }else{
            return new ReturnObject(Constants.WRONG_PASSWORD_CODE,Constants.WRONG_PASSWORD_MSG);
        }
        return new ReturnObject();
    }
}
