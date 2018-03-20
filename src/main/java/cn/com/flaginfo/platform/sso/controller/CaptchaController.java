package cn.com.flaginfo.platform.sso.controller;

import cn.com.flaginfo.platform.common.redis.RedisFacade;
import cn.com.flaginfo.platform.sso.common.Constants;
import cn.com.flaginfo.platform.sso.common.vo.ReturnObject;
import cn.com.flaginfo.platform.sso.service.CaptchaService;
import cn.com.flaginfo.platform.sso.util.ImageCodeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Map;

@Controller
@RequestMapping("/captcha")
public class CaptchaController {
    
    private final CaptchaService captchaService;

    @Value("${sso.mobile.login.verify.message}")
    private String mobileCodeLoginContent;

    @Autowired
    public CaptchaController(CaptchaService captchaService) {
        this.captchaService = captchaService;
    }
    
    @RequestMapping(value = "/getImageCode", method = RequestMethod.GET)
    public void getImageCode(@RequestParam int width, @RequestParam int height,
                             @RequestParam int codeCount, @RequestParam int lineCount,HttpServletResponse response, HttpSession session) throws IOException {
        Map<String, Object> map = ImageCodeUtils.generate(width, height, codeCount, lineCount);
        RedisFacade.set(Constants.SESSION_+ session.getId(),map.get("code"));
        BufferedImage image = (BufferedImage)map.get("image");
        ServletOutputStream os = response.getOutputStream();
        ImageIO.write(image, "png", os);
        os.close();
    }
    
    
    @RequestMapping(value = "/verifyImageCode", method = RequestMethod.GET)
    @ResponseBody
    public ReturnObject verifyImageCode(@RequestParam String code, HttpSession session){
        Object rCode = RedisFacade.get(Constants.SESSION_+ session.getId());
        
        ReturnObject returnObject = new ReturnObject();
        
        if(StringUtils.isEmpty(rCode) || !code.equalsIgnoreCase(rCode.toString())){
            returnObject.setReturnCode(Constants.VERIFY_CODE_WRONG_CODE);
            returnObject.setMsg(Constants.VERIFY_CODE_WRONG_MSG);
        }
        return returnObject;
    }
    
    @RequestMapping(value = "/verifyImageCodeAndMobile", method = RequestMethod.GET)
    @ResponseBody
    public ReturnObject verifyImageCodeAndMobile(@RequestParam String code,@RequestParam String mobile,HttpSession session){
        Object rCode = RedisFacade.get(Constants.SESSION_+ session.getId());
        
        ReturnObject returnObject = new ReturnObject();
        
        if(StringUtils.isEmpty(rCode) || !code.equalsIgnoreCase(rCode.toString())){
            returnObject.setReturnCode(Constants.VERIFY_CODE_WRONG_CODE);
            returnObject.setMsg(Constants.VERIFY_CODE_WRONG_MSG);
            return returnObject;
        }
        
        if(StringUtils.isEmpty(mobile)){
            returnObject.setReturnCode(Constants.MOBILE_ERROR_CODE);
            returnObject.setMsg(Constants.MOBILE_IS_NULL);
            return returnObject;
        }
        
        return captchaService.checkMobile(mobile);
    }
    
    
    @RequestMapping(value = "/send/mobile/code", method = RequestMethod.GET)
    @ResponseBody
    public ReturnObject sendMobileCode(@RequestParam String mobile){
        captchaService.sendVerifyCodeMsg(mobile,mobileCodeLoginContent);
        return new ReturnObject();
    }
    
    @RequestMapping(value = "/verify/mobile/code", method = RequestMethod.GET)
    @ResponseBody
    public ReturnObject verifyMobileCode(@RequestParam String mobile,@RequestParam String code){
        
        ReturnObject returnObject = new ReturnObject();
        Object rCode = RedisFacade.get(Constants.VERIFY_MOBILE_ + mobile);
    
        if(!code.equals(rCode)){
            returnObject.setReturnCode(Constants.VERIFY_CODE_WRONG_CODE);
            returnObject.setMsg(Constants.VERIFY_CODE_WRONG_MSG);
        }
        return returnObject;
    }
}
