package cn.com.flaginfo.platform.sso.vo;

import cn.com.flaginfo.platform.ucenter.api.vo.EnterpriseVO;
import cn.com.flaginfo.platform.ucenter.api.vo.MemberVO;

/**
 * @author tiantian
 */
public class UserDetail {
    private User user;

    private EnterpriseVO enterpriseVO;

    private MemberVO memberVO;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public EnterpriseVO getEnterpriseVO() {
        return enterpriseVO;
    }

    public void setEnterpriseVO(EnterpriseVO enterpriseVO) {
        this.enterpriseVO = enterpriseVO;
    }

    public MemberVO getMemberVO() {
        return memberVO;
    }

    public void setMemberVO(MemberVO memberVO) {
        this.memberVO = memberVO;
    }
}
