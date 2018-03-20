package cn.com.flaginfo.platform.sso.controller;

import cn.com.flaginfo.platform.api.common.base.BaseResponse;
import cn.com.flaginfo.platform.common.redis.RedisFacade;
import cn.com.flaginfo.platform.sso.common.Constants;
import cn.com.flaginfo.platform.sso.common.vo.ReturnObject;
import cn.com.flaginfo.platform.sso.util.Base64Encoder;
import cn.com.flaginfo.platform.sso.vo.User;
import cn.com.flaginfo.platform.sso.vo.UserDetail;
import cn.com.flaginfo.platform.sso.vo.UserMember;
import cn.com.flaginfo.platform.ucenter.api.AuthService;
import cn.com.flaginfo.platform.ucenter.api.EnterpriseService;
import cn.com.flaginfo.platform.ucenter.api.MemberService;
import cn.com.flaginfo.platform.ucenter.api.UserService;
import cn.com.flaginfo.platform.ucenter.api.util.Page;
import cn.com.flaginfo.platform.ucenter.api.vo.CommonCond;
import cn.com.flaginfo.platform.ucenter.api.vo.EnterpriseVO;
import cn.com.flaginfo.platform.ucenter.api.vo.MemberVO;
import cn.com.flaginfo.platform.ucenter.api.vo.UserVO;
import com.taobao.pandora.boot.loader.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


/**
 * @author edward.tian on 2017/9/26.
 */
@Controller
@RequestMapping("/User")
public class UserController {

    private final EnterpriseService enterpriseService;

    private final AuthService authService;
    
    private final MemberService memberService;
    
    private final UserService userService;

    private static Logger logger = LoggerFactory.getLogger(UserController.class);

    private String productId = "3UDO5RAHPMBQEODP7ILJV3LLZY3ZK";

    private String emptyMsg = "参数为空或者参数不正确";
    
    @Autowired
    public UserController(EnterpriseService enterpriseService, AuthService authService, MemberService memberService, UserService userService) {
        this.enterpriseService = enterpriseService;
        this.authService = authService;
        this.memberService = memberService;
        this.userService = userService;
    }
    
    
    @RequestMapping(value = "/User/Enterprise")
    @ResponseBody
    public ReturnObject loginEnt(@RequestBody User user, HttpServletResponse response, HttpServletRequest request) {
        ReturnObject returnObject = new ReturnObject();
        List<EnterpriseVO> enterpriseVOS = new ArrayList<>();
        CommonCond commonCond = new CommonCond();
        commonCond.setProductId(productId);
        commonCond.setSpCode(user.getSpId());
        EnterpriseVO value = enterpriseService.getEnterpriseDetail(commonCond).getData();
        
        if(value == null){
            user.setPassword(null);
            returnObject.setMsg("用户企业信息错误");
            returnObject.setReturnCode("2001");
            returnObject.setStatus(1);
            logger.info("Request /User/Enterprise return {}", returnObject);
            return returnObject;
        }
        
        commonCond.setSpId(value.getSpId());
        //其值其实是spCode
        String spId = user.getSpId();
        if (StringUtils.isEmpty(user.getSpId()) || StringUtils.isEmpty(user.getName()) || StringUtils.isEmpty(user.getPassword())) {
            returnObject.setMsg(emptyMsg);
            returnObject.setReturnCode("9999");
            returnObject.setStatus(1);
            return returnObject;
        }
        if (user.getUseRemembered() != null && user.getUseRemembered() == 1) {
            Cookie[] cookies = request.getCookies();
            if (cookies != null && cookies.length != 0) {
                for (Cookie cookie : cookies) {
                    if ("tokenBack".equals(cookie.getName())) {
                        user.setPassword(Base64Encoder.decrpty(cookie.getValue()));
                    }
                }
            }
        }
        UserVO userVO;
        BaseResponse<UserVO> resp = authService.authUserBySpCode(productId, user.getSpId(), user.getName(), user.getPassword());
        if (Constants.SUCCESS_CODE_LONG.equals(resp.getCode())) {
            userVO = resp.getData();
            enterpriseVOS.add(value);
            user.setEnterprises(enterpriseVOS);
            if (user.getRememberPwd() != null && user.getRememberPwd() == 1) {
                //创建新cookie
                Cookie cookie = new Cookie("tokenBack", Base64Encoder.encrpty(user.getPassword()));
                // 设置存在时间为5分钟
                cookie.setMaxAge(5184000);
                //设置作用域
                cookie.setPath("/");
                cookie.setHttpOnly(true);
                response.addCookie(cookie);
            }
            BeanUtils.copyProperties(userVO, user);
            user.setSpId(spId);
        } else {
            user.setPassword(null);
            returnObject.setMsg("用户名或者密码错误");
            returnObject.setReturnCode("2001");
            returnObject.setStatus(1);
            logger.info("Request /User/Enterprise return {}", returnObject);
            return returnObject;
        }
        String token = UUID.randomUUID().toString();
        Page page = new Page();
        page.setBegin(0);
        page.setInterval(20);
        MemberVO memberVO = null;
        BaseResponse<java.util.List<MemberVO>> memberList = memberService.getMemberListByUserId(commonCond, userVO.getId(), page);
        if (memberList.getData() != null && Constants.SUCCESS_CODE_LONG.equals(memberList.getCode()) && memberList.getData().size() > 0) {
            memberVO = memberList.getData().get(0);
        }
        if (memberVO != null) {
            user.setMemberId(memberVO.getId().toString());
        }
        user.setUserId(userVO.getId().toString());
        returnObject.setData(user);
        user.setSpId(value.getSpId());
        user.setToken(token);
        RedisFacade.set(token, user);
        returnObject.setReturnValue(token);
        returnObject.setReturnCode("0000");
        returnObject.setStatus(0);
        returnObject.setMsg("登录成功");
        return returnObject;
    }

    @RequestMapping(value = "/User")
    @ResponseBody
    public ReturnObject loginUser(@RequestBody User user, HttpServletResponse response, HttpServletRequest request) {
        logger.info("Request /User return start...");
        ReturnObject returnObject = new ReturnObject();
        if (StringUtils.isEmpty(user.getName()) || StringUtils.isEmpty(user.getPassword())) {
            returnObject.setMsg(emptyMsg);
            returnObject.setReturnCode("9999");
            returnObject.setStatus(1);
            logger.info("Request /User return {}", returnObject);
            return returnObject;
        }
        try {
            UserVO userVO;
            if (user.getUseRemembered() != null && user.getUseRemembered() == 1) {
                Cookie[] cookies = request.getCookies();
                if (cookies != null && cookies.length != 0) {
                    for (Cookie cookie : cookies) {
                        if ("tokenBackPhone".equals(cookie.getName())) {
                            user.setPassword(Base64Encoder.decrpty(cookie.getValue()));
                        }
                    }
                }
            }
            Long startTime1 = System.currentTimeMillis();
            BaseResponse<UserVO> resp = authService.authUser(productId, user.getName(), user.getPassword());
            logger.info("authService.authUser 耗时:" + (System.currentTimeMillis() - startTime1));
            if (Constants.SUCCESS_CODE_LONG.equals(resp.getCode())) {
                userVO = resp.getData();
                Page page = new Page();
                page.setBegin(0);
                page.setInterval(20);
                List<EnterpriseVO> enterpriseVOS = null;
                startTime1 = System.currentTimeMillis();
                BaseResponse<List<EnterpriseVO>> entResp = enterpriseService.getEnterpriseListByUserId(productId, userVO.getId(), page);
                logger.info("authService.authUser 耗时:" + (System.currentTimeMillis() - startTime1));
                if (Constants.SUCCESS_CODE_LONG.equals(entResp.getCode())) {
                    enterpriseVOS = entResp.getData();
                }
                BeanUtils.copyProperties(userVO, user);
                user.setEnterprises(enterpriseVOS);
                if (user.getRememberPwd() != null && user.getRememberPwd() == 1) {
                    //创建新cookie
                    Cookie cookie = new Cookie("tokenBackPhone", Base64Encoder.encrpty(user.getPassword()));
                    // 设置存在时间为5分钟
                    cookie.setMaxAge(5184000);
                    //设置作用域
                    cookie.setPath("/");
                    cookie.setHttpOnly(true);
                    response.addCookie(cookie);
                }
            } else {
                user.setPassword(null);
                returnObject.setMsg("用户名或者密码错误");
                returnObject.setReturnCode("2001");
                returnObject.setStatus(1);
                logger.info("Request /User return {}", returnObject);
                return returnObject;
            }
            String token = UUID.randomUUID().toString();
            user.setToken(token);
            user.setMobile(userVO.getMobile());
            user.setUserName(userVO.getUserName());
            user.setUserId(String.valueOf(userVO.getId()));
            RedisFacade.set(token, user);
            returnObject.setData(user);
            returnObject.setReturnValue(token);
            returnObject.setReturnCode("0000");
            returnObject.setStatus(0);
            returnObject.setMsg("登录成功");
        } catch (Exception e) {
            logger.error("Error : {}", e);
            logger.info(e.getLocalizedMessage());
        }
        logger.info("Request /User return {}", returnObject);
        return returnObject;
    }

    @RequestMapping(value = "/Report")
    @ResponseBody
    public ReturnObject appendDetail(@RequestBody UserMember user) {
        logger.info("Request /Report return start...");
        ReturnObject returnObject = new ReturnObject();
        if (null == user || StringUtils.isEmpty(user.getToken())) {
            returnObject.setMsg(emptyMsg);
            returnObject.setReturnCode("9999");
            returnObject.setStatus(1);
            logger.info("Request /Report return {}", returnObject);
            return returnObject;
        }
        try {
            User userVO = (User) RedisFacade.get(user.getToken());
            if (!StringUtils.isEmpty(user.getCorpId())) {
                userVO.setCorpId(user.getCorpId());
            }
            if (!StringUtils.isEmpty(user.getSpId())) {
                userVO.setSpId(user.getSpId());
            }
            if (!StringUtils.isEmpty(user.getMemberId())) {
                userVO.setMemberId(user.getMemberId());
            }
            RedisFacade.set(user.getToken(), userVO);
            returnObject.setReturnCode("0000");
            returnObject.setStatus(0);
            returnObject.setMsg("上报成功");
        } catch (Exception e) {
            logger.error("Error : {}", e);
            logger.info(e.getLocalizedMessage());
        }
        logger.info("Request /Report return {}", returnObject);
        return returnObject;
    }

    @RequestMapping(value = "/Member")
    @ResponseBody
    public ReturnObject appendMember(@RequestBody UserMember user) {
        logger.info("Request /Member return start...");
        ReturnObject returnObject = new ReturnObject();
        if (StringUtils.isEmpty(user.getMemberId()) || StringUtils.isEmpty(user.getToken())) {
            returnObject.setMsg(emptyMsg);
            returnObject.setReturnCode("9999");
            returnObject.setStatus(1);
            logger.info("Request /Member return {}", returnObject);
            return returnObject;
        }
        try {
            User userVO = (User) RedisFacade.get(user.getToken());
            userVO.setMemberId(user.getMemberId());
            RedisFacade.set(user.getToken(), userVO);
            returnObject.setReturnCode("0000");
            returnObject.setStatus(0);
            returnObject.setMsg("登录成功");
        } catch (Exception e) {
            logger.error("Error : {}", e);
            logger.info(e.getLocalizedMessage());
        }
        logger.info("Request /Member return {}", returnObject);
        return returnObject;
    }

    @RequestMapping(value = "/Enterprise")
    @ResponseBody
    public ReturnObject enterprise(@RequestBody UserMember user) {
        logger.info("Request /Enterprise return start...");
        ReturnObject returnObject = new ReturnObject();
        if (StringUtils.isEmpty(user.getSpId()) || StringUtils.isEmpty(user.getToken())) {
            returnObject.setMsg(emptyMsg);
            returnObject.setReturnCode("9999");
            returnObject.setStatus(1);
            logger.info("Request /Enterprise return {}", returnObject);
            return returnObject;
        }
        try {
            User userVO = (User) RedisFacade.get(user.getToken());
            userVO.setSpId(user.getSpId());
            userVO.setCorpId(user.getCorpId());
            RedisFacade.set(user.getToken(), userVO);
            returnObject.setReturnCode("0000");
            returnObject.setStatus(0);
            returnObject.setMsg("登录成功");
        } catch (Exception e) {
            logger.error("Error : {}", e);
            logger.info(e.getLocalizedMessage());
        }
        logger.info("Request /Enterprise return {}", returnObject);
        return returnObject;
    }

    @RequestMapping(value = "/Validation/PC")
    public String jump(String token, String redirectUrl) {
        logger.info("Request /Validation/PC return start...");
        ReturnObject returnObject = new ReturnObject();
        String returnUrl = "redirect:/User/Go";
        if (token == null) {
            returnUrl = "redirect:/login.html?redirectUrl=" + redirectUrl;
            logger.info("Request /Validation/PC : token is null, return [{}]", returnUrl);
            return returnUrl;
        } else {
            Object userVo = RedisFacade.get(token);
            if (userVo == null) {
                returnUrl = "redirect:/login.html?redirectUrl=" + redirectUrl;
                logger.info("Request /Validation/PC : get redis cache is null, return [{}]", returnUrl);
                return "redirect:/login.html?redirectUrl=" + redirectUrl;
            } else {
                returnObject.setReturnCode("0000");
                returnObject.setStatus(0);
                returnObject.setMsg("登录成功");
            }
        }
        logger.info("Request /Validation/PC : return [{}]", returnUrl);
        return returnUrl;
    }

    @RequestMapping(value = "/Go")
    @ResponseBody
    public ReturnObject socket(String token) {
        logger.info("Request /Go return start...");
        ReturnObject returnObject = new ReturnObject();
        returnObject.setReturnCode("0000");
        returnObject.setStatus(0);
        returnObject.setMsg("登录成功");
        logger.info("Request /Go return {}", returnObject);
        return returnObject;
    }

    @RequestMapping(value = "/Detail")
    @ResponseBody
    public ReturnObject detail(UserMember user) {
        logger.info("Request /Detail return start...");
        ReturnObject returnObject = new ReturnObject();
        if (StringUtils.isEmpty(user.getToken())) {
            returnObject.setMsg(emptyMsg);
            returnObject.setReturnCode("9999");
            returnObject.setStatus(1);
            logger.info("Request /Detail return {}", returnObject);
            return returnObject;
        }
        try {
            User userVO = (User) RedisFacade.get(user.getToken());
            if (userVO != null) {
                UserMember userMember = new UserMember();
                userMember.setSpId(userVO.getSpId());
                userMember.setCorpId(userVO.getCorpId());
                userMember.setMemberId(userVO.getMemberId());
                userMember.setUserId(userVO.getUserId());
                userMember.setMobile(userVO.getMobile());
                userMember.setUserName(userVO.getUserName());
                returnObject.setReturnCode("0000");
                returnObject.setStatus(0);
                returnObject.setMsg("获取用户详情成功");
                returnObject.setData(userMember);
            } else {
                returnObject.setReturnCode("9999");
                returnObject.setStatus(1);
                returnObject.setMsg("token失效");
            }

        } catch (Exception e) {
            logger.error("Error : {}", e);
            logger.info(e.getLocalizedMessage());
        }
        logger.info("Request /Detail return {}", returnObject);
        return returnObject;
    }

    @RequestMapping(value = "/AppValidation")
    @ResponseBody
    public ReturnObject appValidation(@RequestBody UserMember user) {
        logger.info("Request /AppValidation return start...");
        ReturnObject returnObject = new ReturnObject();
        if (StringUtils.isEmpty(user.getToken())) {
            returnObject.setMsg(emptyMsg);
            returnObject.setReturnCode("9999");
            returnObject.setStatus(1);
            logger.info("Request /AppValidation return {}", returnObject);
            return returnObject;
        }
        try {
            User userVO = (User) RedisFacade.get(user.getToken());
            if (userVO != null) {
                UserMember userMember = new UserMember();
                userMember.setSpId(userVO.getSpId());
                userMember.setCorpId(userVO.getCorpId());
                userMember.setMemberId(userVO.getMemberId());
                userMember.setUserId(userVO.getUserId());
                userMember.setMobile(userVO.getMobile());
                userMember.setUserName(userVO.getUserName());
                returnObject.setReturnCode("0000");
                returnObject.setStatus(0);
                returnObject.setMsg("验证成功");
                returnObject.setData(userMember);
            } else {
                returnObject.setReturnCode("9002");
                returnObject.setStatus(1);
                returnObject.setMsg("token不存在");
            }
        } catch (Exception e) {
            logger.error("Error : {}", e);
            logger.info(e.getLocalizedMessage());
        }
        logger.info("Request /AppValidation return {}", returnObject);
        return returnObject;
    }

    @RequestMapping(value = "/Validation")
    @ResponseBody
    public ReturnObject validation(@RequestBody UserMember user) {
        logger.info("Request /Validation return start...");
        ReturnObject returnObject = new ReturnObject();
        if (StringUtils.isEmpty(user.getToken())) {
            returnObject.setMsg(emptyMsg);
            returnObject.setReturnCode("9999");
            returnObject.setStatus(1);
            logger.info("Request /Validation return {}", returnObject);
            return returnObject;
        }
        UserMember userMember = new UserMember();
        try {
            Object obj = RedisFacade.get(user.getToken());
            if (obj != null) {
                User userInfo = (User) obj;
                userMember.setSpId(userInfo.getSpId());
                userMember.setMemberId(userInfo.getMemberId());
                userMember.setUserId(userInfo.getId().toString());
                returnObject.setData(userMember);
                returnObject.setReturnCode("0000");
                returnObject.setStatus(0);
                returnObject.setMsg("验证成功");
                Page page = new Page();
                page.setBegin(0);
                page.setInterval(20);
                CommonCond commonCond = new CommonCond();
                commonCond.setSpId(userInfo.getSpId());
                commonCond.setProductId(productId);
                MemberVO memberVO = null;
                User newUserInfo = new User();
                Long startTime1 = System.currentTimeMillis();
                //解决用户信息修改的bug
                BaseResponse<UserVO> userDetailRes = userService.getUserDetail(commonCond, new Long(userInfo.getUserId()));
                logger.info("用户详情接口耗时:" + (System.currentTimeMillis() - startTime1));
                if (Constants.SUCCESS_CODE_LONG.equals(userDetailRes.getCode()) && userDetailRes.getData() != null) {
                    BeanUtils.copyProperties(userDetailRes.getData(), newUserInfo);
                    newUserInfo.setUserId(userInfo.getUserId());
                    newUserInfo.setSpId(userInfo.getSpId());
                    newUserInfo.setMemberId(userInfo.getMemberId());
                }
                Long startTime2 = System.currentTimeMillis();
                commonCond.setSpId(userInfo.getSpId());
                BaseResponse<java.util.List<MemberVO>> memberList = memberService.getMemberListByUserId(commonCond, new Long(userInfo.getUserId()), page);
                logger.info("成员列表接口耗时:" + (System.currentTimeMillis() - startTime2));
                if (Constants.SUCCESS_CODE_LONG.equals(memberList.getCode()) && memberList.getData() != null && memberList.getData().size() > 0) {
                    memberVO = memberList.getData().get(0);
                }
                Long startTime3 = System.currentTimeMillis();
                BaseResponse<EnterpriseVO> resp = enterpriseService.getEnterpriseDetail(commonCond);
                logger.info("企业详情接口耗时:" + (System.currentTimeMillis() - startTime3));
                EnterpriseVO enterpriseVO = null;
                if (Constants.SUCCESS_CODE_LONG.equals(resp.getCode()) && resp.getData() != null) {
                    enterpriseVO = resp.getData();
                }
                UserDetail userDetail = new UserDetail();
                userDetail.setUser(newUserInfo);
                userInfo.setEnterprises(null);
                userDetail.setEnterpriseVO(enterpriseVO);
                userDetail.setMemberVO(memberVO);
                returnObject.setReturnCode("0000");
                returnObject.setStatus(0);
                returnObject.setMsg("获取member成功");
                returnObject.setData(userDetail);
                //为token设置memberId
                if (userInfo.getMemberId() != null && "".equals(userInfo.getMemberId())) {
                    userInfo.setMemberId(String.valueOf(memberVO.getId()));
                    RedisFacade.set(user.getToken(), userInfo);
                }
            } else {
                returnObject.setReturnCode("9002");
                returnObject.setStatus(1);
                returnObject.setMsg("token不存在");
            }

        } catch (Exception e) {
            returnObject.setReturnCode("9008");
            returnObject.setStatus(1);
            logger.error("Error : {}", e);
            logger.info(e.getLocalizedMessage());
        }
        logger.info("Request /Validation return {}", returnObject);
        return returnObject;
    }

    @RequestMapping(value = "/Logout")
    @ResponseBody
    public ReturnObject logout(@RequestBody UserMember user) {
        logger.info("Request /Logout return start...");
        ReturnObject returnObject = new ReturnObject();
        if (StringUtils.isEmpty(user.getToken())) {
            returnObject.setMsg(emptyMsg);
            returnObject.setReturnCode("9999");
            returnObject.setStatus(1);
            logger.info("Request /Logout return {}", returnObject);
            return returnObject;
        }
        try {
            returnObject.setReturnCode("0000");
            returnObject.setStatus(0);
            returnObject.setMsg("登出成功");
        } catch (Exception e) {
            logger.error("Error : {}", e);
            logger.info(e.getLocalizedMessage());
        }
        logger.info("Request /Logout return {}", returnObject);
        return returnObject;
    }

    @RequestMapping(value = "/Member/Get")
    @ResponseBody
    public ReturnObject getMember(UserMember user) {
        logger.info("Request /Member/Get return start...");
        ReturnObject returnObject = new ReturnObject();
        if (StringUtils.isEmpty(user.getToken())) {
            returnObject.setMsg(emptyMsg);
            returnObject.setReturnCode("9999");
            returnObject.setStatus(1);
            logger.info("Request /Member/Get return {}", returnObject);
            return returnObject;
        }
        try {
            Object obj = RedisFacade.get(user.getToken());
            if (obj != null) {
                User userInfo = (User) obj;
                Page page = new Page();
                page.setBegin(0);
                page.setInterval(20);
                CommonCond commonCond = new CommonCond();
                commonCond.setSpId(user.getSpId());
                commonCond.setProductId(productId);
                BaseResponse<java.util.List<MemberVO>> memberList = memberService.getMemberListByUserId(commonCond, new Long(userInfo.getUserId()), page);
                if (Constants.SUCCESS_CODE_LONG.equals(memberList.getCode()) && memberList.getData() != null && memberList.getData().size() > 0) {
                    MemberVO memberVO = memberList.getData().get(0);
                    returnObject.setData(memberVO);
                } else {
                    returnObject.setReturnCode("8002");
                    returnObject.setStatus(1);
                    returnObject.setMsg("该用户不是该企业成员");
                }
            }
            returnObject.setReturnCode("0000");
            returnObject.setStatus(0);
            returnObject.setMsg("获取member成功");
        } catch (Exception e) {
            logger.error("Error : {}", e);
            logger.info(e.getLocalizedMessage());
        }
        logger.info("Request /Member/Get return {}", returnObject);
        return returnObject;
    }

    @RequestMapping(value = "/Detail/All")
    @ResponseBody
    public ReturnObject all(UserMember user) {
        logger.info("Request /Detail/All return start...");
        ReturnObject returnObject = new ReturnObject();
        if (StringUtils.isEmpty(user.getToken())) {
            returnObject.setMsg(emptyMsg);
            returnObject.setReturnCode("9999");
            returnObject.setStatus(1);
            logger.info("Request /Detail/All return {}", returnObject);
            return returnObject;
        }
        try {
            User userInfo = (User) RedisFacade.get(user.getToken());
            Page page = new Page();
            page.setBegin(0);
            page.setInterval(20);
            CommonCond commonCond = new CommonCond();
            commonCond.setSpId(userInfo.getSpId());
            commonCond.setProductId(productId);
            MemberVO memberVO = null;
            BaseResponse<java.util.List<MemberVO>> memberList = memberService.getMemberListByUserId(commonCond, new Long(userInfo.getUserId()), page);
            if (Constants.SUCCESS_CODE_LONG.equals(memberList.getCode()) && memberList.getData() != null && memberList.getData().size() > 0) {
                memberVO = memberList.getData().get(0);
            }
            BaseResponse<EnterpriseVO> resp = enterpriseService.getEnterpriseDetail(commonCond);
            EnterpriseVO enterpriseVO = null;
            if (Constants.SUCCESS_CODE_LONG.equals(resp.getCode()) && resp.getData() != null) {
                enterpriseVO = resp.getData();
            }
            UserDetail userDetail = new UserDetail();
            userDetail.setUser(userInfo);
            userDetail.setEnterpriseVO(enterpriseVO);
            userDetail.setMemberVO(memberVO);
            returnObject.setReturnCode("0000");
            returnObject.setStatus(0);
            returnObject.setMsg("获取member成功");
            returnObject.setData(userDetail);
        } catch (Exception e) {
            logger.error("Error : {}", e);
            logger.info(e.getLocalizedMessage());
        }
        logger.info("Request /Detail/All return {}", returnObject);
        return returnObject;
    }

    @RequestMapping(value = "/OK")
    public String ok() {
        return "OK";
    }

}
