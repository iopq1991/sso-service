package cn.com.flaginfo.platform.sso.controller;

import cn.com.flaginfo.platform.common.redis.RedisFacade;
import cn.com.flaginfo.platform.sso.common.Constants;
import cn.com.flaginfo.platform.sso.common.utils.CookieUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;

@Controller
public class LoginController {

    @Value("${sso.server}")
    private String ssoServer;

    @RequestMapping(value = "/index", method = RequestMethod.GET)
    public String index(@RequestParam(required = false) String redirectUrl,  HttpServletRequest request){

        String token = CookieUtils.getCookie(request, Constants.TOKEN_COOKIE_NAME);
        if(StringUtils.isEmpty(token) || RedisFacade.get(token) == null){
            return "redirect:"+ ssoServer +"/dist/login.html?redirectUrl=" + redirectUrl;
        }
        return "redirect:" + redirectUrl;
    }
}
