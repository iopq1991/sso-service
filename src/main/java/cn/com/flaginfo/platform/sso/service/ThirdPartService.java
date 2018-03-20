package cn.com.flaginfo.platform.sso.service;

import cn.com.flaginfo.platform.api.common.base.BaseResponse;
import cn.com.flaginfo.platform.common.util.SystemMessage;
import cn.com.flaginfo.platform.sso.common.Constants;
import cn.com.flaginfo.platform.sso.common.utils.JsonUtils;
import cn.com.flaginfo.platform.sso.common.vo.ReturnObject;
import cn.com.flaginfo.platform.sso.common.vo.TokenInfo;
import cn.com.flaginfo.platform.ucenter.api.AuthService;
import cn.com.flaginfo.platform.ucenter.api.EnterpriseService;
import cn.com.flaginfo.platform.ucenter.api.MemberService;
import cn.com.flaginfo.platform.ucenter.api.UserService;
import cn.com.flaginfo.platform.ucenter.api.util.Page;
import cn.com.flaginfo.platform.ucenter.api.vo.*;
import cn.com.flaginfo.wechat.core.service.WxBusRelationService;
import cn.com.flaginfo.wechat.datatype.JsonView;
import cn.com.flaginfo.wechat.datatype.dto.wxConfig.WxConfigInput;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class ThirdPartService {

    private final UserService userService;
    
    private final AuthService authService;
    
    private final MemberService memberService;

    private final EnterpriseService enterpriseService;

    private final WxBusRelationService wxBusRelationService;

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    public ThirdPartService(UserService userService, AuthService authService, MemberService memberService, EnterpriseService enterpriseService, WxBusRelationService wxBusRelationService) {
        this.userService = userService;
        this.authService = authService;
        this.memberService = memberService;
        this.enterpriseService = enterpriseService;
        this.wxBusRelationService = wxBusRelationService;
    }

    public UserThirdPartyVO getThirdPartyInfo(String identifier, Integer identifyType,String appId){
        CommonCond commonCond = new CommonCond();
        commonCond.setProductId(SystemMessage.getString("productId"));

        BaseResponse<List<UserThirdPartyVO>> response = userService.getThirdPartyListByIdentifier(commonCond,identifyType,identifier,appId,new Page());

        if(response.getCode() == 200L){
            List<UserThirdPartyVO> userThirdPartyVOList = response.getData();
            if(!CollectionUtils.isEmpty(userThirdPartyVOList)){
                return userThirdPartyVOList.iterator().next();
            }
        }
        return null;
    }



    public TokenInfo getUserAndMemberInfo(Long userId,String spId) {
        CommonCond commonCond = new CommonCond();
        commonCond.setProductId(SystemMessage.getString("productId"));
    
        TokenInfo tokenInfo = new TokenInfo();

        BaseResponse<UserVO> userVOBaseResponse = userService.getUserDetail(commonCond,userId);
        UserVO userVO = userVOBaseResponse.getData();

        tokenInfo.setUserId(userVO.getId());
        tokenInfo.setUserName(userVO.getUserName());
        tokenInfo.setMobile(userVO.getMobile());

        if(!StringUtils.isEmpty(spId)){
            commonCond.setSpId(spId);
            BaseResponse<List<MemberVO>> memberResponse = memberService.getMemberListByUserId(commonCond,userVO.getId(),new Page());
            if(memberResponse.getCode() == 200L) {
                addMemberInfo(spId, tokenInfo, memberResponse);
            }
        }

        return tokenInfo;
    }
    
    private void addMemberInfo(String spId, TokenInfo tokenInfo, BaseResponse<List<MemberVO>> memberResponse) {
        List<MemberVO> memberVOList = memberResponse.getData();
        if(!CollectionUtils.isEmpty(memberVOList)){
            for(MemberVO memberVO : memberVOList){
                if(spId.equals(memberVO.getSpId())){
                    tokenInfo.setSpId(spId);
                    tokenInfo.setMemberId(memberVO.getId());
                    tokenInfo.setMemberName(memberVO.getName());
                }
            }
        }
    }
    
    public Long validateUserInfo(String personalPhoneNo, String password) {
        BaseResponse<UserVO> baseResponse = authService.authUserByMobile(SystemMessage.getString("productId"),personalPhoneNo,password);
        if(baseResponse.getCode() == 200L && baseResponse.getData() != null){
            return baseResponse.getData().getId();
        }
        return null;
    }
    
    public BaseResponse<Long> bindThirdPartyUser(Long userId,String identifier, Integer identifyType, String appId,String spId){
        CommonCond commonCond = new CommonCond();
        commonCond.setProductId(SystemMessage.getString("productId"));
        UserThirdPartyVO vo = new UserThirdPartyVO();
        vo.setAppId(appId);
        vo.setCreateDate(new Date());
        vo.setIdentifier(identifier);
        vo.setIdentifyType(identifyType);
        vo.setUserId(userId);
        vo.setSpId(spId);
        return userService.addUserThirdParty(commonCond,vo);
    }
    
    public boolean checkUserThirdParty(Long userId, Integer identifyType,String identifier) {
        CommonCond commonCond = new CommonCond();
        commonCond.setProductId(SystemMessage.getString("productId"));

        UserThirdPartyVO vo = new UserThirdPartyVO();
        vo.setIdentifier(identifier);
        vo.setIdentifyType(identifyType);
        vo.setUserId(userId);
        BaseResponse<Boolean> baseResponse = userService.checkUserThirdParty(commonCond,vo);
        return baseResponse.getCode() == 200L && baseResponse.getData();
    }
    
    public TokenInfo getTokenInfoWithoutMember(Long userId) {
        CommonCond commonCond = new CommonCond();
        commonCond.setProductId(SystemMessage.getString("productId"));
        TokenInfo tokenInfo = new TokenInfo();
    
        BaseResponse<UserVO> response = userService.getUserDetail(commonCond,userId);
        if(response.getCode() == 200L){
            UserVO userVO = response.getData();
            if(userVO != null){
                tokenInfo.setUserId(userVO.getId());
                tokenInfo.setUserName(userVO.getUserName());
                tokenInfo.setMobile(userVO.getMobile());
            }
        }
    
        return tokenInfo;
        
    }
    
    public Long getUserIdByPhone(String personalPhoneNo) {
        CommonCond commonCond = new CommonCond();
        commonCond.setProductId(SystemMessage.getString("productId"));
    
        BaseResponse<List<UserVO>> response = userService.getUserListByMobile(commonCond,personalPhoneNo,new Page());
        if(response.getCode() == 200L){
            List<UserVO> userVOList = response.getData();
            if(!CollectionUtils.isEmpty(userVOList)){
                UserVO userVO = userVOList.iterator().next();
                
                return userVO.getId();
            }
        }
        return null;
    }

    public List<EnterpriseVO> getUserEnterpriseList(Long userId) {
        BaseResponse<List<EnterpriseVO>> response = enterpriseService.getEnterpriseListByUserId(SystemMessage.getString("productId"), userId, new Page());

        if(response.getCode() == 200 && response.getData() != null){
            return response.getData();
        }
        return new ArrayList<>();
    }

    public boolean updateThirdParty(UserThirdPartyVO userThirdPartyVO, String spId){

        CommonCond commonCond = new CommonCond();
        commonCond.setProductId(SystemMessage.getString("productId"));
        userThirdPartyVO.setSpId(spId);

        BaseResponse<Boolean> response = userService.updateUserThirdParty(commonCond,userThirdPartyVO);

        return response != null && response.getCode() == 200L&& response.getData();
    }


    public boolean checkEnterpriseUser(String spId, Long userId) {
        CommonCond commonCond = new CommonCond();
        commonCond.setProductId(SystemMessage.getString("productId"));
        commonCond.setSpId(spId);

        BaseResponse<Boolean> response = userService.checkEnterpriseUser(commonCond,userId);
        return response.getCode() == 200L && response.getData();
    }

    public boolean deleteThirdParty(Long id) {
        CommonCond commonCond = new CommonCond();
        commonCond.setProductId(SystemMessage.getString("productId"));

        BaseResponse<Boolean> response = userService.deleteUserThirdParty(commonCond,id);
        return response != null && response.getCode() == 200;
    }


    public void filterEnterpriseVOList(Integer type, String appId, List<EnterpriseVO> enterpriseVOList) {
        if(!Constants.THIRD_PART_LOGIN_TYPE_WEIXIN.equals(type)){
            return;
        }

        WxConfigInput input = new WxConfigInput();
        input.setWxId(appId);
        JsonView jsonView =  wxBusRelationService.queryUsalbeSpList(input);

        logger.info("调用微信接口,参数wxId:{},返回值：{}" ,appId,JsonUtils.getJsonString(jsonView));
        if(Constants.SUCCESS_CODE.equals(jsonView.getReturnCode())){

            List<String> spIdList = JsonUtils.jsonStringToList(JsonUtils.getJsonString(jsonView.get("data")),String.class);

            enterpriseVOList.removeIf(vo -> !spIdList.contains(vo.getSpId()));

        }

    }

    public boolean checkAndUpdateAppId(String oldAppId, String newAppId, UserThirdPartyVO userThirdPartyVO) {
        if(StringUtils.isEmpty(oldAppId)
                || StringUtils.isEmpty(newAppId)
                || StringUtils.isEmpty(userThirdPartyVO)
                || oldAppId.equals(newAppId)){
            return true;
        }


        CommonCond commonCond = new CommonCond();
        commonCond.setProductId(SystemMessage.getString("productId"));

        userThirdPartyVO.setAppId(newAppId);
        BaseResponse<Boolean> response = userService.updateUserThirdParty(commonCond,userThirdPartyVO);

        return response != null && response.getCode() == 200L&& response.getData();
    }

    public ReturnObject getEnterpriseListByUserId(Long userId, String appId) {
        BaseResponse<List<EnterpriseVO>> response = enterpriseService.getEnterpriseListByUserId(Constants.PRODUCT_ID, userId, new Page());

        if(response.getCode() != 200L || response.getData() == null){
            return new ReturnObject(Constants.USER_SP_NOT_FOUND_CODE,Constants.USER_SP_NOT_FOUND_MSG);
        }
        List<EnterpriseVO> enterpriseVOList = response.getData();
        if(!StringUtils.isEmpty(appId)){
            filterEnterpriseVOList(Constants.THIRD_PART_LOGIN_TYPE_WEIXIN,appId,enterpriseVOList);
        }

        return new ReturnObject(enterpriseVOList);
    }
}
