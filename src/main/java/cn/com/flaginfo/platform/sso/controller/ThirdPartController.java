package cn.com.flaginfo.platform.sso.controller;

import cn.com.flaginfo.platform.api.common.base.BaseResponse;
import cn.com.flaginfo.platform.common.util.JsonHelper;
import cn.com.flaginfo.platform.sso.common.Constants;
import cn.com.flaginfo.platform.sso.common.utils.CookieUtils;
import cn.com.flaginfo.platform.sso.common.vo.ReturnObject;
import cn.com.flaginfo.platform.sso.common.vo.TokenInfo;
import cn.com.flaginfo.platform.sso.common.vo.token.ChoiceSpVO;
import cn.com.flaginfo.platform.sso.service.ThirdPartService;
import cn.com.flaginfo.platform.sso.service.TokenService;
import cn.com.flaginfo.platform.sso.vo.third.part.ThirdPartBindRequest;
import cn.com.flaginfo.platform.sso.vo.third.part.ThirdPartUnBindRequest;
import cn.com.flaginfo.platform.ucenter.api.vo.EnterpriseVO;
import cn.com.flaginfo.platform.ucenter.api.vo.UserThirdPartyVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/third-part")
public class ThirdPartController {
    private Logger logger = LoggerFactory.getLogger(getClass());


    private final ThirdPartService thirdPartService;

    private final TokenService tokenService;
    
    @Value("${sso.server}")
    private String ssoServer;
    
    @Autowired
    public ThirdPartController(ThirdPartService thirdPartService, TokenService tokenService) {
        this.thirdPartService = thirdPartService;
        this.tokenService = tokenService;
    }


    @RequestMapping(value = "/auth",method = RequestMethod.GET)
    public String auth(@RequestParam String sign,@RequestParam Integer type,
                       @RequestParam String appId,@RequestParam(required = false) String spId,
                       @RequestParam String redirectUri,
                       HttpServletResponse response){
        logger.info("第三方鉴权, sign :{},type:{},appId:{},spId{},redirectUri:{}",sign,type,appId,spId,redirectUri);

        if (StringUtils.isEmpty(sign) || StringUtils.isEmpty(type)) {
            CookieUtils.writeCookie(response,"returnMsg", "登录标志 登录类型 不能为空");
            return generateRedirectString("/returnMsg.html");
        } else if (Constants.THIRD_PART_LOGIN_TYPE_WEIXIN.equals(type)
                || Constants.THIRD_PART_LOGIN_TYPE_DINGDING.equals(type)) {
            if (StringUtils.isEmpty(appId)) {
                CookieUtils.writeCookie(response,"returnMsg", "appId 不能为空");
                return generateRedirectString("/returnMsg.html");
            }
        }

        // 查询第三方信息，查询到跳转业务页面，查询不到跳转绑定页面
        UserThirdPartyVO userThirdPartyVO = thirdPartService.getThirdPartyInfo(sign,type,null);

        if(userThirdPartyVO == null){
            CookieUtils.writeCookie(response,"redirectUri", redirectUri);
            CookieUtils.writeCookie(response,"sign", sign);
            CookieUtils.writeCookie(response,"type", String.valueOf(type));
            CookieUtils.writeCookie(response,"spId", spId);
            CookieUtils.writeCookie(response,"appId", appId);

            return generateRedirectString("/dist/login.html#bindAccount");
        }else{
            if(!thirdPartService.checkAndUpdateAppId(userThirdPartyVO.getAppId(),appId,userThirdPartyVO)){
                CookieUtils.writeCookie(response,"returnMsg", "更新appId失败");
                return generateRedirectString("/returnMsg.html");
            }

            if(StringUtils.isEmpty(userThirdPartyVO.getSpId())){
                TokenInfo tokenInfo = thirdPartService.getTokenInfoWithoutMember(userThirdPartyVO.getUserId());

                String token = tokenService.generateToken(null, tokenInfo);
                return generateRedirectString("/dist/login.html?userId=" + userThirdPartyVO.getUserId() +
                        "&identifyType="+type + "&identifier=" + sign + "&token="+ token + "&redirectUri="+redirectUri + "#bindEnter");
            }else{
                TokenInfo tokenInfo = thirdPartService.getUserAndMemberInfo(userThirdPartyVO.getUserId(),userThirdPartyVO.getSpId());
                String token = tokenService.generateToken(null, tokenInfo);
                return "redirect:"+ redirectUri +
                        (redirectUri.contains("?") ? "&" : "?" )
                        + "token=" + token;
            }
        }

    }

    @RequestMapping(value = "/auth/ding",method = RequestMethod.GET)
    public String authDingDing(@RequestParam String sign,@RequestParam Integer type,
                       @RequestParam String appId,@RequestParam String spId,@RequestParam String personalPhoneNo,
                       @RequestParam String redirectUri,
                       HttpServletResponse response){
        logger.info("第三方鉴权 钉钉, sign :{},type:{},appId:{},spId{},redirectUri:{}",sign,type,appId,spId,redirectUri);

        if (StringUtils.isEmpty(sign) || StringUtils.isEmpty(type)
                || StringUtils.isEmpty(spId)|| StringUtils.isEmpty(personalPhoneNo)) {
            CookieUtils.writeCookie(response,"returnMsg", "登录标志 登录类型 企业ID 手机号 不能为空");
            return generateRedirectString("/returnMsg.html");
        } else if (Constants.THIRD_PART_LOGIN_TYPE_DINGDING.equals(type)) {
            if (StringUtils.isEmpty(appId)) {
                CookieUtils.writeCookie(response,"returnMsg", "appId 不能为空");
                return generateRedirectString("/returnMsg.html");
            }
        }

        // 查询钉钉第三方信息，查询到跳转业务页面，查询不到绑定后跳转业务页面
        UserThirdPartyVO userThirdPartyVO = thirdPartService.getThirdPartyInfo(sign,type,null);

        Long userId = null;
        if(userThirdPartyVO == null){
            if(Constants.THIRD_PART_LOGIN_TYPE_DINGDING.equals(type)){
                userId = thirdPartService.getUserIdByPhone(personalPhoneNo);
            }

            if(StringUtils.isEmpty(userId)){
                CookieUtils.writeCookie(response,"returnMsg", "该手机号未在平台注册");
                return generateRedirectString("/returnMsg.html");
            }

            if(thirdPartService.checkUserThirdParty(userId,type,sign)){
                CookieUtils.writeCookie(response,"returnMsg", "用户已绑定");
                return generateRedirectString("/returnMsg.html");
            }

            BaseResponse<Long> response1 = thirdPartService.bindThirdPartyUser(userId,sign,type,appId,spId);
            if(response1 == null || response1.getCode() != 200){
                CookieUtils.writeCookie(response,"returnMsg", "绑定用户失败");
                return generateRedirectString("/returnMsg.html");
            }
        }else{
            userId = userThirdPartyVO.getUserId();
        }

        if(!thirdPartService.checkEnterpriseUser(spId,userId)){
            CookieUtils.writeCookie(response,"returnMsg", "用户未在指定企业内");
            return generateRedirectString("/returnMsg.html");
        }

        TokenInfo tokenInfo = thirdPartService.getUserAndMemberInfo(userId,spId);
        String token = tokenService.generateToken(null, tokenInfo);
        return "redirect:" + redirectUri +
                (redirectUri.contains("?") ? "&" : "?" )
                + "token=" + token;

    }

    @ResponseBody
    @RequestMapping(value = "/user/bind",method = RequestMethod.POST)
    public ReturnObject bindSignUser(@RequestBody ThirdPartBindRequest thirdPartBindRequest){
        logger.info("第三方绑定 :{}" + JsonHelper.parseToJson(thirdPartBindRequest));

        String personalPhoneNo = thirdPartBindRequest.getPersonalPhoneNo();
        String password = thirdPartBindRequest.getPassword();
        String sign = thirdPartBindRequest.getSign();
        Integer type = thirdPartBindRequest.getType();
        String appId = thirdPartBindRequest.getAppId();
//        String spId = thirdPartBindRequest.getSpId();

        if (StringUtils.isEmpty(sign) || StringUtils.isEmpty(type)) {
            return new ReturnObject(Constants.EMPTY_PARAM_CODE,Constants.EMPTY_PARAM_MSG);
        }

        Long userId = null;
        if(Constants.THIRD_PART_LOGIN_TYPE_WEIXIN.equals(type)){
            if(StringUtils.isEmpty(password)){
                return new ReturnObject(Constants.WE_CHAT_BIND_PASSWORD_NULL_CODE,Constants.WE_CHAT_BIND_PASSWORD_NULL_MSG);
            }
            userId = thirdPartService.validateUserInfo(personalPhoneNo,password);
        }else if(Constants.THIRD_PART_LOGIN_TYPE_DINGDING.equals(type)){
            userId = thirdPartService.getUserIdByPhone(personalPhoneNo);
        }

        if(StringUtils.isEmpty(userId)){
            return new ReturnObject(Constants.WRONG_PASSWORD_CODE,Constants.WRONG_PASSWORD_MSG);
        }

//        if(!thirdPartService.checkEnterpriseUser(spId,userId)){
//            return new ReturnObject(Constants.WRONG_MEMBER_CODE,Constants.WRONG_MEMBER_MSG);
//        }

        if(thirdPartService.checkUserThirdParty(userId,type,sign)){
            return new ReturnObject(Constants.THIRD_PARTY_USER_BOUND_CODE,Constants.THIRD_PARTY_USER_BOUND_MSG);
        }

        List<EnterpriseVO> enterpriseVOList = thirdPartService.getUserEnterpriseList(userId);
        thirdPartService.filterEnterpriseVOList(type,appId,enterpriseVOList);
        if(CollectionUtils.isEmpty(enterpriseVOList)){
            return new ReturnObject(Constants.USER_SP_NOT_FOUND_CODE,Constants.USER_SP_NOT_FOUND_MSG);
        }


        BaseResponse<Long> response = thirdPartService.bindThirdPartyUser(userId,sign,type,appId,null);
        if(response.getCode() == 200L && response.getData() != null){
            ReturnObject returnObject = new ReturnObject();
            Map<String,Object> returnMap = new HashMap<>();


            TokenInfo tokenInfo = thirdPartService.getTokenInfoWithoutMember(userId);
            String token = tokenService.generateToken(null, tokenInfo);

            returnMap.put("userId", userId);
            returnMap.put("enterpriseVOList", enterpriseVOList);
            returnMap.put("identifier", sign);
            returnMap.put("identifyType", type);
            returnMap.put("token",token);
            returnObject.setData(returnMap);
            return returnObject;
        }else {
            return new ReturnObject(response.getCode().toString(),response.getMessage());
        }
    }


    /**
     * 选择用户企业
     *
     * @param choiceSpVO 选择企业VO
     * @return returnObject
     */
    @ResponseBody
    @RequestMapping(value = "/sp/choice")
    public ReturnObject choiceSp(@RequestBody ChoiceSpVO choiceSpVO) {
        if (StringUtils.isEmpty(choiceSpVO.getSpId())
                || StringUtils.isEmpty(choiceSpVO.getToken())
                || StringUtils.isEmpty(choiceSpVO.getIdentifier())
                || StringUtils.isEmpty(choiceSpVO.getIdentifyType())) {
            return new ReturnObject(Constants.EMPTY_PARAM_CODE, Constants.EMPTY_PARAM_MSG);
        }

        UserThirdPartyVO userThirdPartyVO = thirdPartService.getThirdPartyInfo(choiceSpVO.getIdentifier(),choiceSpVO.getIdentifyType(),null);

        if(userThirdPartyVO == null){
            return new ReturnObject(Constants.THIRD_PART_INFO_NOT_FOUND_ERROR_CODE, Constants.THIRD_PART_INFO_NOT_FOUND_ERROR_MSG);
        }

        ReturnObject returnObject = tokenService.choiceSp(choiceSpVO);

        if(!Constants.SUCCESS_CODE.equals(returnObject.getReturnCode())){
            return returnObject;
        }

        boolean updateResult = thirdPartService.updateThirdParty(userThirdPartyVO,choiceSpVO.getSpId());

        if(!updateResult){
            returnObject.setReturnCode(Constants.THIRD_PART_CHOICE_SP_ERROR_CODE);
            returnObject.setMsg(Constants.THIRD_PART_CHOICE_SP_ERROR_MSG);
        }

        return returnObject;
    }


    /**
     * 解除绑定
     *
     * @param unBindRequest 解除绑定请求
     * @return returnObject
     */
    @ResponseBody
    @RequestMapping(value = "/unbind")
    public ReturnObject unBind(@RequestBody ThirdPartUnBindRequest unBindRequest) {
        if (unBindRequest ==null || StringUtils.isEmpty(unBindRequest.getSign())
                || StringUtils.isEmpty(unBindRequest.getType())
                || StringUtils.isEmpty(unBindRequest.getAppId())
                || StringUtils.isEmpty(unBindRequest.getUserId())) {
            return new ReturnObject(Constants.EMPTY_PARAM_CODE, Constants.EMPTY_PARAM_MSG);
        }

        UserThirdPartyVO userThirdPartyVO = thirdPartService.getThirdPartyInfo(unBindRequest.getSign(),unBindRequest.getType(),null);

        if(userThirdPartyVO == null || !userThirdPartyVO.getUserId().equals(unBindRequest.getUserId())){
            return new ReturnObject(Constants.THIRD_PART_INFO_NOT_FOUND_ERROR_CODE, Constants.THIRD_PART_INFO_NOT_FOUND_ERROR_MSG);
        }


        boolean deleteResult = thirdPartService.deleteThirdParty(userThirdPartyVO.getId());

        if(!deleteResult){
            return new ReturnObject(Constants.THIRD_PART_UNBIND_ERROR_CODE,Constants.THIRD_PART_UNBIND_ERROR_MSG);
        }

        return new ReturnObject(Constants.SUCCESS_CODE,Constants.THIRD_PART_UNBIND_SUCCESS);
    }

    private String generateRedirectString(String path){
        if(StringUtils.isEmpty(ssoServer)){
            return "redirect:" + path;
        }
        return "redirect:" + ssoServer + path;
    }

    /**
     * 根据token获取用户信息
     *
     * @param userId 用户id
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/enterprise/list")
    public ReturnObject getEnterpriseListByUserId(@RequestParam Long userId,@RequestParam(required = false) String appId ) {
        if (StringUtils.isEmpty(userId)) {
            return new ReturnObject(Constants.EMPTY_PARAM_CODE, Constants.EMPTY_PARAM_MSG);
        }

        return thirdPartService.getEnterpriseListByUserId(userId,appId);
    }
}
