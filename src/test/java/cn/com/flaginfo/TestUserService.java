package cn.com.flaginfo;

import cn.com.flaginfo.platform.api.common.base.BaseResponse;
import cn.com.flaginfo.platform.common.support.PropertiesSourceInitializer;
import cn.com.flaginfo.platform.ucenter.api.EnterpriseService;
import cn.com.flaginfo.platform.ucenter.api.MemberService;
import cn.com.flaginfo.platform.ucenter.api.UserService;
import cn.com.flaginfo.platform.ucenter.api.util.Page;
import cn.com.flaginfo.platform.ucenter.api.vo.CommonCond;
import cn.com.flaginfo.platform.ucenter.api.vo.EnterpriseVO;
import cn.com.flaginfo.platform.ucenter.api.vo.MemberVO;
import cn.com.flaginfo.platform.ucenter.api.vo.UserThirdPartyVO;
import com.alibaba.boot.hsf.annotation.HSFConsumer;
import com.taobao.pandora.boot.test.junit4.DelegateTo;
import com.taobao.pandora.boot.test.junit4.PandoraBootRunner;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

import static org.junit.Assert.assertNotNull;

@RunWith(PandoraBootRunner.class)
@DelegateTo(SpringJUnit4ClassRunner.class)
// 加载测试需要的类，一定要加入Spring Boot的启动类，其次需要加入本类
@SpringBootTest(classes = {SSOServerApplication.class,TestUserService.class})
@ContextConfiguration(initializers = PropertiesSourceInitializer.class)
public class TestUserService {
    
    private static final Logger logger = LoggerFactory.getLogger(TestUserService.class);
    
    @HSFConsumer(serviceGroup = "user-center-service", serviceVersion = "6.1.0")
    private UserService userService;
    
    @HSFConsumer( serviceGroup = "user-center-service", serviceVersion = "6.1.0")
    private MemberService memberService;
    
    @HSFConsumer( serviceGroup = "user-center-service", serviceVersion = "6.1.0")
    private  EnterpriseService enterpriseService;
    
//    @Test
    public void enterpriseTest() {
    
        CommonCond commonCond = new CommonCond();
        commonCond.setProductId("3UDO5RAHPMBQEODP7ILJV3LLZY3ZK");
        commonCond.setSpCode("100330");
        
        BaseResponse<EnterpriseVO> response1 = enterpriseService.getEnterpriseDetail(commonCond);
        
        logger.info("response 1 : " + response1.getData());
    
        CommonCond commonCond2 = new CommonCond();
        commonCond2.setProductId("3UDO5RAHPMBQEODP7ILJV3LLZY3ZK");
        commonCond2.setSpCode("100328");
        BaseResponse<EnterpriseVO> response2 = enterpriseService.getEnterpriseDetail(commonCond2);
        logger.info("response 2 : " + response2.getData());
    }
    
    @Test
    public void memberTest() {
        
        CommonCond commonCond = new CommonCond();
        commonCond.setProductId("3UDO5RAHPMBQEODP7ILJV3LLZY3ZK");

        UserThirdPartyVO vo = new UserThirdPartyVO();
        vo.setSpId("123");
        vo.setIdentifier("1111");
        vo.setIdentifyType(1);
        vo.setId(10127L);

        userService.updateUserThirdParty(commonCond,vo);
//        BaseResponse<List<UserThirdPartyVO>> response = userService.getThirdPartyListByIdentifier(commonCond,1,"test",null,new Page());
//
//        if(response.getData() == null){
//            return;
//        }
//        List<UserThirdPartyVO> userVOList = response.getData();
//        for(UserThirdPartyVO userVO : userVOList){
//            System.out.println(userVO);
//        }
        
//        assertNotNull(response);
//        assertNotNull(response.getData());
    }
    
    
//    @Test
    public void organizationTest() {
        CommonCond commonCond = new CommonCond();
        commonCond.setProductId("3UDO5RAHPMBQEODP7ILJV3LLZY3ZK");
        commonCond.setCropId(3L);
    
        BaseResponse<List<MemberVO>> response = memberService.getMemberListByUserId(commonCond,3L,new Page());
    
        if(response.getData() == null){
            return;
        }
        List<MemberVO> userVOList = response.getData();
        for(MemberVO userVO : userVOList){
            System.out.println(userVO);
        }
        
        assertNotNull(response);
        assertNotNull(response.getData());
    }
}
