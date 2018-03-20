package cn.com.flaginfo.platform.sso.vo.third.part;

public class ThirdPartBindRequest {
    private String personalPhoneNo;
    private String password;
    private String sign;
    private Integer type;
    private String appId;
    private String spId;
    private Long userId;
    
    public String getPersonalPhoneNo() {
        return personalPhoneNo;
    }
    
    public void setPersonalPhoneNo(String personalPhoneNo) {
        this.personalPhoneNo = personalPhoneNo;
    }
    
    public String getPassword() {
        return password;
    }
    
    public void setPassword(String password) {
        this.password = password;
    }
    
    public String getSign() {
        return sign;
    }
    
    public void setSign(String sign) {
        this.sign = sign;
    }
    
    public Integer getType() {
        return type;
    }
    
    public void setType(Integer type) {
        this.type = type;
    }
    
    public String getAppId() {
        return appId;
    }
    
    public void setAppId(String appId) {
        this.appId = appId;
    }
    
    public String getSpId() {
        return spId;
    }
    
    public void setSpId(String spId) {
        this.spId = spId;
    }
    
    public Long getUserId() {
        return userId;
    }
    
    public void setUserId(Long userId) {
        this.userId = userId;
    }
}
