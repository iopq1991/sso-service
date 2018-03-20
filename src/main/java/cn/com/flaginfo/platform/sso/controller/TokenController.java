package cn.com.flaginfo.platform.sso.controller;

import cn.com.flaginfo.platform.common.redis.RedisFacade;
import cn.com.flaginfo.platform.sso.common.Constants;
import cn.com.flaginfo.platform.sso.common.utils.CookieUtils;
import cn.com.flaginfo.platform.sso.common.vo.ReturnObject;
import cn.com.flaginfo.platform.sso.common.vo.token.ChoiceSpVO;
import cn.com.flaginfo.platform.sso.common.vo.token.GetInfoByTokenVO;
import cn.com.flaginfo.platform.sso.common.vo.token.GetTokenVO;
import cn.com.flaginfo.platform.sso.login.LoginHandler;
import cn.com.flaginfo.platform.sso.service.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;

/**
 * @author chenglong.xu
 */

@Controller
@RequestMapping("/sso")
public class TokenController {

    private final TokenService tokenService;
    
    @Autowired
    public TokenController(TokenService tokenService) {
        this.tokenService = tokenService;
    }
    
    /**
     * 通过账号密码或手机号密码获取用户token
     *
     * @param getTokenVO
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/token/get")
    public ReturnObject getToken(@RequestBody GetTokenVO getTokenVO, HttpServletResponse response) {
        if(getTokenVO == null
                || StringUtils.isEmpty(getTokenVO.getType())
                || StringUtils.isEmpty(getTokenVO.getSource())){
            return new ReturnObject(Constants.EMPTY_PARAM_CODE, Constants.EMPTY_PARAM_MSG);
        }

        LoginHandler loginHandler  = tokenService.getLoginHandler(getTokenVO.getType());
        if(loginHandler == null){
            return new ReturnObject(Constants.LOGIN_TYPE_WRONG_CODE, Constants.LOGIN_TYPE_WRONG_MSG);
        }else if(!loginHandler.validateParam(getTokenVO)){
            return new ReturnObject(Constants.EMPTY_PARAM_CODE, Constants.EMPTY_PARAM_MSG);
        }

        return tokenService.getToken(getTokenVO,loginHandler,response);
    }

    /**
     * 选择用户企业
     *
     * @param choiceSpVO
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/sp/choice")
    public ReturnObject choiceSp(@RequestBody ChoiceSpVO choiceSpVO) {
        if (StringUtils.isEmpty(choiceSpVO.getSpId())
                || StringUtils.isEmpty(choiceSpVO.getToken())) {
            return new ReturnObject(Constants.EMPTY_PARAM_CODE, Constants.EMPTY_PARAM_MSG);
        }
        return tokenService.choiceSp(choiceSpVO);
    }

    /**
     * 根据token获取用户信息
     *
     * @param getInfoByTokenVO
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/info/get-by-token")
    public ReturnObject getInfoByToken(@RequestBody GetInfoByTokenVO getInfoByTokenVO) {
        if (StringUtils.isEmpty(getInfoByTokenVO.getToken())) {
            return new ReturnObject(Constants.EMPTY_PARAM_CODE, Constants.EMPTY_PARAM_MSG);
        }
        return tokenService.getInfoByToken(getInfoByTokenVO);
    }

    /**
     * 根据token获取用户信息
     *
     * @param userId 用户id
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/enterprise/list")
    public ReturnObject getEnterpriseListByUserId(@RequestParam Long userId) {
        if (StringUtils.isEmpty(userId)) {
            return new ReturnObject(Constants.EMPTY_PARAM_CODE, Constants.EMPTY_PARAM_MSG);
        }
        return tokenService.getEnterpriseListByUserId(userId);
    }

    /**
     * 登出功能，sso server只删除token缓存，由发起端删除cookie
     * @param token token
     * @return 返回值
     */
    @ResponseBody
    @RequestMapping(value = "/logout")
    public ReturnObject logout(@RequestParam String token,HttpServletResponse response){
        if (StringUtils.isEmpty(token)) {
            return new ReturnObject(Constants.EMPTY_PARAM_CODE, Constants.EMPTY_PARAM_MSG);
        }
    
        CookieUtils.writeCookie(response,Constants.TOKEN_COOKIE_NAME,Constants.NULL_STR);
        RedisFacade.del(token);
        return new ReturnObject();
    }
}
