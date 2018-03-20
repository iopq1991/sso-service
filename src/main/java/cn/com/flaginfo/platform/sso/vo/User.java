package cn.com.flaginfo.platform.sso.vo;

import cn.com.flaginfo.platform.ucenter.api.vo.EnterpriseVO;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @author tiantian
 */
public class User implements Serializable {
    private static final long serialVersionUID = 941134329899937999L;
    private Long id;
    private String spCode;
    private String userNo;
    private String userName;
    private String mobile;
    private String email;
    private String passWord;
    private String wechatOpenId;
    private String nickName;
    private String avatar;
    private String city;
    private Date ctime;
    private Date utime;
    private Integer status;
    private List<EnterpriseVO> enterprises;
    private int isDefault;
    private int dest;
    private int usage;
    private String memberId;
    private Integer rememberPwd;
    private Integer useRemembered;

    public String getSpCode() {
        return spCode;
    }

    public void setSpCode(String spCode) {
        this.spCode = spCode;
    }

    public void setRememberPwd(Integer rememberPwd) {
        this.rememberPwd = rememberPwd;
    }

    public Integer getRememberPwd() {
        return this.rememberPwd;
    }

    public void setUseRemembered(Integer useRemembered) {
        this.useRemembered = useRemembered;
    }

    public Integer getUseRemembered() {
        return this.useRemembered;
    }

    public String getMemberId() {
        return memberId;
    }

    public void setMemberId(String memberId) {
        this.memberId = memberId;
    }

    public int getUsage() {
        return usage;
    }

    public void setUsage(int usage) {
        this.usage = usage;
    }

    public int getIsDefault() {
        return isDefault;
    }

    public void setIsDefault(int isDefault) {
        this.isDefault = isDefault;
    }

    public int getDest() {
        return dest;
    }

    public void setDest(int dest) {
        this.dest = dest;
    }

    private String name;

    private String password;

    private String spId;

    private String corpId;

    private String userId;

    private String token;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getCorpId() {
        return corpId;
    }

    public void setCorpId(String corpId) {
        this.corpId = corpId;
    }

    public String getSpId() {
        return spId;
    }

    public void setSpId(String spId) {
        this.spId = spId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUserNo() {
        return userNo;
    }

    public void setUserNo(String userNo) {
        this.userNo = userNo;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassWord() {
        return passWord;
    }

    public void setPassWord(String passWord) {
        this.passWord = passWord;
    }

    public String getWechatOpenId() {
        return wechatOpenId;
    }

    public void setWechatOpenId(String wechatOpenId) {
        this.wechatOpenId = wechatOpenId;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public Date getCtime() {
        return ctime;
    }

    public void setCtime(Date ctime) {
        this.ctime = ctime;
    }

    public Date getUtime() {
        return utime;
    }

    public void setUtime(Date utime) {
        this.utime = utime;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public List<EnterpriseVO> getEnterprises() {
        return enterprises;
    }

    public void setEnterprises(List<EnterpriseVO> enterprises) {
        this.enterprises = enterprises;
    }

//    public static void main(String[]args){
//        Jedis jedis = JedisConn.getInstance();
//        byte[] dest = jedis.get("32fa30ad-4881-4627-8002-f42a4457f82b".getBytes());
//        System.err.println(dest);
//    }
}
