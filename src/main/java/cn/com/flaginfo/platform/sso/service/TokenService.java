package cn.com.flaginfo.platform.sso.service;

import cn.com.flaginfo.platform.api.common.base.BaseResponse;
import cn.com.flaginfo.platform.common.redis.RedisFacade;
import cn.com.flaginfo.platform.common.util.SystemMessage;
import cn.com.flaginfo.platform.sso.common.Constants;
import cn.com.flaginfo.platform.sso.common.utils.ContextUtils;
import cn.com.flaginfo.platform.sso.common.utils.CookieUtils;
import cn.com.flaginfo.platform.sso.common.utils.TokenUtils;
import cn.com.flaginfo.platform.sso.common.vo.ReturnObject;
import cn.com.flaginfo.platform.sso.common.vo.TokenInfo;
import cn.com.flaginfo.platform.sso.common.vo.token.ChoiceSpVO;
import cn.com.flaginfo.platform.sso.common.vo.token.GetInfoByTokenVO;
import cn.com.flaginfo.platform.sso.common.vo.token.GetTokenVO;
import cn.com.flaginfo.platform.sso.login.LoginHandler;
import cn.com.flaginfo.platform.sso.login.MobilePasswordLoginHandler;
import cn.com.flaginfo.platform.sso.login.MobileVerifyCodeLoginHandler;
import cn.com.flaginfo.platform.sso.login.SpCodeLoginHandler;
import cn.com.flaginfo.platform.ucenter.api.EnterpriseService;
import cn.com.flaginfo.platform.ucenter.api.MemberService;
import cn.com.flaginfo.platform.ucenter.api.util.Page;
import cn.com.flaginfo.platform.ucenter.api.vo.CommonCond;
import cn.com.flaginfo.platform.ucenter.api.vo.EnterpriseVO;
import cn.com.flaginfo.platform.ucenter.api.vo.MemberVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * @author chenglong.xu
 */

@Service
public class TokenService {

    private final EnterpriseService enterpriseService;

    private final MemberService memberService;

    @Autowired
    public TokenService(EnterpriseService enterpriseService, MemberService memberService ) {
        this.enterpriseService = enterpriseService;
        this.memberService = memberService;
    }
    
    public ReturnObject getToken(GetTokenVO getTokenVO, LoginHandler loginHandler, HttpServletResponse response) {
        Map<String, Object> returnMap = new HashMap<>(4);
        TokenInfo tokenInfo = new TokenInfo();

        ReturnObject returnObject = loginHandler.authUser(getTokenVO,returnMap,tokenInfo);
        if(!Constants.SUCCESS_CODE.equals(returnObject.getReturnCode())){
            return returnObject;
        }

        String token = generateToken(null,tokenInfo,getTokenVO.getSource());
        CookieUtils.writeCookie(response,Constants.TOKEN_COOKIE_NAME, TokenUtils.getEncryptedToken(token));
        
        //防重复递交
        if (Constants.LOGIN_SOURCE_1.equals(getTokenVO.getSource())) {
            List tokenList = RedisFacade.hmget("appMobileHm", tokenInfo.getMobile());
            if (tokenList.size() > 0) {
                String oldToken = (String) tokenList.get(0);
                if (null != oldToken) {
                    RedisFacade.del(oldToken);
                }
            }
            RedisFacade.hset("appMobileHm", tokenInfo.getMobile(), token);
        }
        returnMap.put("token", token);
        returnMap.put("userId",tokenInfo.getUserId());
        returnMap.put("userName",tokenInfo.getUserName());
        return new ReturnObject(returnMap);
    }

    public ReturnObject choiceSp(ChoiceSpVO choiceSpVO) {
        TokenInfo tokenInfo = (TokenInfo) RedisFacade.get(choiceSpVO.getToken());

        if(tokenInfo == null){
            return new ReturnObject(Constants.INVALID_TOKEN_ERROR_CODE,Constants.INVALID_TOKEN_ERROR_MSG);
        }

        CommonCond commonCond = new CommonCond();
        commonCond.setProductId(Constants.PRODUCT_ID);
        commonCond.setSpId(choiceSpVO.getSpId());

        if(!validateEnterpriseStatus(commonCond)){
            return new ReturnObject(Constants.ENTERPRISE_STATUS_ERROR_CODE,Constants.ENTERPRISE_STATUS_ERROR_MSG);
        }

        BaseResponse<List<MemberVO>> memberList = memberService.getMemberListByUserId(commonCond, tokenInfo.getUserId(), new Page());

        if(memberList == null || memberList.getData() == null || CollectionUtils.isEmpty(memberList.getData())){
            return new ReturnObject(Constants.USER_MEMBER_NOT_FOUND_CODE,Constants.USER_MEMBER_NOT_FOUND_MSG);
        }

        MemberVO memberVO = memberList.getData().get(0);
        if(!Constants.MEMBER_STATUS_NORMAL.equals(memberVO.getStatus())){
            return new ReturnObject(Constants.MEMBER_STATUS_ERROR_CODE,Constants.MEMBER_STATUS_ERROR_MSG);
        }

        tokenInfo.setMemberId(memberVO.getId());
        tokenInfo.setMemberName(memberVO.getName());
        tokenInfo.setSpId(choiceSpVO.getSpId());
    
        generateToken(choiceSpVO.getToken(),tokenInfo);
        return new ReturnObject();
    }

    private boolean validateEnterpriseStatus(CommonCond commonCond){
        BaseResponse<EnterpriseVO> response = enterpriseService.getEnterpriseDetail(commonCond);
        if(response.getCode() == 200L && response.getData() != null){
            Integer status = response.getData().getStatus();
            return status.equals(Constants.ENTERPRISE_STATUS_NORMAL) || status.equals(Constants.ENTERPRISE_STATUS_PRE_OPEN);
        }

        return false;
    }

    public ReturnObject getInfoByToken(GetInfoByTokenVO getInfoByTokenVO) {
        TokenInfo tokenInfo = (TokenInfo) RedisFacade.get(getInfoByTokenVO.getToken());
        if (tokenInfo != null ) {
            if(!StringUtils.isEmpty(tokenInfo.getTtl())){
                RedisFacade.expire(getInfoByTokenVO.getToken(),tokenInfo.getTtl());
            }else{
                Integer ttl = SystemMessage.getInteger("ttl");
                if(ttl == null){
                    ttl = 60;
                }
                RedisFacade.expire(getInfoByTokenVO.getToken(),ttl);
            }
        }
        return new ReturnObject(tokenInfo);
    }

    public String generateToken(String token,TokenInfo tokenInfo){
        return generateToken(token,tokenInfo,null);
    }

    private String generateToken(String token, TokenInfo tokenInfo, String loginSource){
        if(StringUtils.isEmpty(token)){
            token = UUID.randomUUID().toString();
        }

        Integer ttl = SystemMessage.getInteger("ttl");
        if(ttl == null){
            ttl = 60;
        }

        if(!StringUtils.isEmpty(tokenInfo.getTtl())){
            ttl =  tokenInfo.getTtl();
        }else{
            if(Constants.LOGIN_SOURCE_1.equals(loginSource)){
                ttl = ttl * 6;
            }
        }
        tokenInfo.setTtl(ttl);
        RedisFacade.set(token, tokenInfo, ttl, TimeUnit.MINUTES);
        return token;
    }


    public ReturnObject getEnterpriseListByUserId(Long userId) {
        BaseResponse<List<EnterpriseVO>> response = enterpriseService.getEnterpriseListByUserId(Constants.PRODUCT_ID, userId, new Page());

        if(response.getCode() != 200L || response.getData() == null){
            return new ReturnObject(Constants.USER_SP_NOT_FOUND_CODE,Constants.USER_SP_NOT_FOUND_MSG);
        }

        return new ReturnObject(response.getData());
    }

    public LoginHandler getLoginHandler(String type) {
        switch (type){
            case Constants.LOGIN_TYPE_1:
                return ContextUtils.getApplicationContext().getBean(SpCodeLoginHandler.class);
            case Constants.LOGIN_TYPE_2:
                return ContextUtils.getApplicationContext().getBean(MobilePasswordLoginHandler.class);
            case Constants.LOGIN_TYPE_3:
                return ContextUtils.getApplicationContext().getBean(MobileVerifyCodeLoginHandler.class);
            default:
                return null;
        }
    }
}
