package cn.com.flaginfo.platform.sso.login;

import cn.com.flaginfo.platform.sso.common.vo.ReturnObject;
import cn.com.flaginfo.platform.sso.common.vo.TokenInfo;
import cn.com.flaginfo.platform.sso.common.vo.token.GetTokenVO;

import java.util.Map;

/**
 * @author liangcai.liu
 * @version V1.0
 * Date:  2018/2/28
 * Time: 14:47
 */
public interface LoginHandler {

    boolean validateParam(GetTokenVO getTokenVO);

    ReturnObject authUser(GetTokenVO getTokenVO, Map<String, Object> resultMap, TokenInfo tokenInfo);
}
