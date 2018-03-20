package cn.com.flaginfo.platform.sso.rpc;

import cn.com.flaginfo.platform.global.api.MessageNoticeApi;
import cn.com.flaginfo.platform.sso.common.Constants;
import cn.com.flaginfo.platform.ucenter.api.AuthService;
import cn.com.flaginfo.platform.ucenter.api.EnterpriseService;
import cn.com.flaginfo.platform.ucenter.api.MemberService;
import cn.com.flaginfo.platform.ucenter.api.UserService;
import cn.com.flaginfo.wechat.core.service.WxBusRelationService;
import com.alibaba.boot.hsf.annotation.HSFConsumer;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RpcConfiguration {
    
    @HSFConsumer(serviceVersion = Constants.USER_SERVICE_VERSION, serviceGroup = Constants.USER_SERVICE_GROUP, clientTimeout = 30000)
    private UserService userService;
    
    @HSFConsumer(serviceVersion =  Constants.USER_SERVICE_VERSION, serviceGroup = Constants.USER_SERVICE_GROUP, clientTimeout = 30000)
    private AuthService authService;
    
    @HSFConsumer(serviceVersion =  Constants.USER_SERVICE_VERSION, serviceGroup = Constants.USER_SERVICE_GROUP, clientTimeout = 30000)
    private EnterpriseService enterpriseService;
    
    @HSFConsumer(serviceVersion =  Constants.USER_SERVICE_VERSION, serviceGroup = Constants.USER_SERVICE_GROUP, clientTimeout = 30000)
    private MemberService memberService;
    
    @HSFConsumer(serviceVersion = Constants.COMMON_SERVICE_VERSION, serviceGroup = Constants.COMMON_SERVICE_GROUP, clientTimeout = 30000)
    private MessageNoticeApi messageNoticeApi;

    @HSFConsumer(serviceVersion = Constants.PLATFORM_SERVICE_VERSION, serviceGroup = Constants.PLATFORM_SERVICE_GROUP, clientTimeout = 30000)
    private WxBusRelationService wxBusRelationService;
}
