<template>
    <div class="forget-ctn b-center white-bg">
		<template v-if="isPhone">
			<div v-if="curStep==1" class="fgt-box" @keydown.enter="handleValidCode" v-focus="'input'">
				<flag-steps :active="curStep" finish-status="success" class="lg-forget-step"
							:steps="['确认账号']" :num="curStep">
				</flag-steps>
				<msg-input v-if="!hideCorp" :msg.sync="spCodeMsg" placeholder="请输入企业编号" v-model="spCode"></msg-input>
				<msg-input v-if="!hideCorp" :msg.sync="accountMsg" placeholder="请输入用户名" v-model="account"></msg-input>
				<msg-input :msg.sync="mdnMsg" placeholder="请输入手机号" v-model="mdn"></msg-input>
				<msg-input :msg="validCodeMsg">
					<validate-img v-model="validCode" :load-url="urls.LoadValidImg" @input="validCodeMsg=''"
								  :inputClass="{'err-input': validCodeMsg}"></validate-img>
				</msg-input>

				<el-button type="primary" class="w100 forget-bt" @click="handleValidCode">下一步</el-button>
			</div>

			<div v-if="curStep==2" class="fgt-box" @keydown.enter="handleMdnCheck" v-focus="'input'">
				<flag-steps :active="curStep" finish-status="success" class="lg-forget-step"
							:steps="['验证身份']" :num="curStep">
				</flag-steps>
				<h3 class="p-left"><b>手机验证</b></h3>
				<div class="p-left fgt-tip"><img src="../../../assets/img/login/tip.png"> 为了您的账号安全，请完成身份验证</div>
				<h4>{{mdnHidden}}</h4>
				<validate-mdn v-model="mdnValidCode" :mdn="mdn" :validImageCode="validCode"
							  :msg.sync="mdnValidCodeMsg"></validate-mdn>
				<el-button type="primary" class="w100 forget-bt" @click="handleMdnCheck" :disabled="mdnValidCode.length!=6">
					下一步
				</el-button>
			</div>

			<div v-if="curStep==3" class="fgt-box" @keydown.enter="" v-focus="'input'">
				<flag-steps :active="curStep" finish-status="success" class="lg-forget-step"
							:steps="['重置密码']" :num="curStep">
				</flag-steps>
				<div class="fgt-tip">您正在找回的账号是：{{mdnHidden}}</div>

					<msg-input slot="reference" :msg.sync="passwordMsg" placeholder="设置新密码" v-model="password"
							   type="password"></msg-input>

				<msg-input :msg.sync="password2Msg" placeholder="确认新密码" v-model="password2" type="password"></msg-input>
				<div style="margin-top:5px;">(6 ~ 16位字母、数字或标点符号组合)</div>
				<el-button type="primary" class="w100 forget-bt" @click="handleSetPassword">确定</el-button>
			</div>

			<div v-if="curStep==4" class="fgt-box b-center t-center" @keydown.enter="" v-focus="'input'">
				<div><img src="../../../assets/img/login/success-big.png"/></div>
				<div>重置密码成功，请牢记新密码</div>
				<el-button class="w100 forget-bt" type="primary" size="large" @click="handleGoLogin">重新登录</el-button>
			</div>
		</template>
		<template v-else>
			<div>
				<flag-steps :active="curStep" finish-status="success" class="lg-forget-step"
							:steps="['确认账号','验证身份','重置密码']">
				</flag-steps>
			</div>

			<div v-if="curStep==1" class="fgt-box" @keydown.enter="handleValidCode" v-focus="'input'">
				<msg-input v-if="!hideCorp" :msg.sync="spCodeMsg" placeholder="请输入企业编号" v-model="spCode"></msg-input>
				<msg-input v-if="!hideCorp" :msg.sync="accountMsg" placeholder="请输入用户名" v-model="account"></msg-input>
				<msg-input :msg.sync="mdnMsg" placeholder="请输入手机号" v-model="mdn"></msg-input>
				<msg-input :msg="validCodeMsg">
					<validate-img v-model="validCode" :load-url="urls.LoadValidImg" @input="validCodeMsg=''"
								  :inputClass="{'err-input': validCodeMsg}"></validate-img>
				</msg-input>

				<el-button type="primary" class="w100 forget-bt" @click="handleValidCode">下一步</el-button>
			</div>

			<div v-if="curStep==2" class="fgt-box" @keydown.enter="handleMdnCheck" v-focus="'input'">
				<h3 class="p-left"><b>手机验证</b></h3>
				<div class="p-left fgt-tip"><img src="../../../assets/img/login/tip.png"> 为了您的账号安全，请完成身份验证</div>
				<h4>{{mdnHidden}}</h4>
				<validate-mdn v-model="mdnValidCode" :mdn="mdn" :validImageCode="validCode"
							  :msg.sync="mdnValidCodeMsg"></validate-mdn>
				<el-button type="primary" class="w100 forget-bt" @click="handleMdnCheck" :disabled="mdnValidCode.length!=6">
					下一步
				</el-button>
			</div>

			<div v-if="curStep==3" class="fgt-box" @keydown.enter="" v-focus="'input'">
				<div class="fgt-tip">您正在找回的账号是：{{mdnHidden}}</div>

				<el-popover class="pwd-str-wp"
							placement="right"
							width="350"
							@hide="showPwdStrength=true"
							v-model="showPwdStrength"
							popper-class="pwd-strength-poper">
					<pwd-strength :pwd-info="pwdInfo"></pwd-strength>

					<msg-input slot="reference" :msg.sync="passwordMsg" placeholder="设置新密码" v-model="password"
							   type="password"></msg-input>
				</el-popover>

				<msg-input :msg.sync="password2Msg" placeholder="确认新密码" v-model="password2" type="password"></msg-input>

				<el-button type="primary" class="w100 forget-bt" @click="handleSetPassword">确定</el-button>
			</div>

			<div v-if="curStep==4" class="fgt-box b-center t-center" @keydown.enter="" v-focus="'input'">
				<div><img src="../../../assets/img/login/success-big.png"/></div>
				<div>重置密码成功，请牢记新密码</div>
				<el-button class="w100 forget-bt" type="primary" size="large" @click="handleGoLogin">重新登录</el-button>
			</div>
		</template>
    </div>
</template>

<script>
    import Vue from 'vue';
    import {Input, Row, Col, Steps, Step} from 'element-ui';

    Vue.use(Input).use(Row).use(Col).use(Steps).use(Step);

    import msgInput from "@/components/msgInput.vue"
    import validData from '@/assets/js/validData';
    import validateImg from "@/components/validateCode/validateImg.vue"
    import validateMdn from "@/components/validateCode/validateMdn.vue"
    import pwdStrength from "@/components/pwdStrength.vue"
    import flagSteps from "@/components/flagSteps.vue"

    const defaultUrls = {
        LoadValidImg: '/captcha/getImageCode',         //获取验证码图片地址的接口url
        //SendValidSms: '/common/sendVerifyCodeMsg',        //发送短信的url
        CheckValidImg: '/captcha/verifyImageCodeAndMobile',     //校验图形验证码
        CheckValidSms: '/captcha/verify/mobile/code',     //校验短信验证码（校验手机）
        GetBackPwd: '/security/update/password',   //修改新密码
    }
    export default {
        props: {
            hideCorp: Boolean,  //是否手机用户找回密码。手机登录方式，需要隐藏企业字段
            urls: {             //几个步骤的接口地址
                Type: Object,
                default() {
                    return defaultUrls
                }
            }
        },
        data() {
            return {
                curStep: 1,     //当前步骤

                spCode: '',     //企业编号
                account: '',    //用户名
                mdn: '',        //手机号
                validCode: '',  //图形验证码
                mdnValidCode: '', //短信验证码
                password: '',   //新密码
                password2: '',  //确认新密码

                spCodeMsg: '',  //以下Msg结尾的都是对应的提示信息
                accountMsg: '',
                mdnMsg: '',
                validCodeMsg: '',
                mdnValidCodeMsg: '',
                passwordMsg: '',
                password2Msg: '',

                sessionId: undefined, //图形验证码sessionId

                smsVCodeLength: this.$x.CONST.SMS_VALI_CODE_LENGTH, //短信验证码长度，常量
                showPwdStrength: false, //显示密码提示
                userId: '',             //找回密码用
            }
        },
        components: {msgInput, validateMdn, validateImg, pwdStrength, flagSteps},
        computed: {
            //计算密码强度、是否符合密码策略
            pwdInfo() {
                return validData.getPasswordType(this.password);
            },
            //将手机号中间变成****
            mdnHidden() {
                return this.mdn.replace(/(\d{3})\d{4}(\d+)/, '$1****$2')
            },
			isPhone(){
            	return /Android|webOS|iPhone|iPod|BlackBerry/i.test(navigator.userAgent)?true:false;
			}
        },
        methods: {
            //第1步，检验图形验证码，成功则跳到第2步
            handleValidCode() {
                if (this.isInputValid()) {
                    var pars = {
                        mobile: this.mdn,
						code: this.validCode
                    };
                    this.$x.get(this.urls.CheckValidImg, pars).then((res) => {

                    	if(res.returnCode==="200"){
                    		this.userId=res.data.id;
							this.curStep = 2;
						}else{
                    		return Promise.reject(res);
						}

                    }).catch(res => {
                        if (res.returnCode == '506') {
                            this.mdnMsg =res.msg|| '您输入的手机号码不存在'
                        }
                        else if (res.returnCode == '505') {
                            this.validCodeMsg = res.msg||'验证码不正确'
                        }
                        else
                            this.$x.toast.error(res.msg);
                    })
                }
            },
            //第2步，检验短信验证码，成功则跳到第3步
            handleMdnCheck() {
                if (this.isMdnCodeValid()) {
                    var pars = {
                        mobile: this.mdn,
                        code: this.mdnValidCode
                    };
                    this.$x.get(this.urls.CheckValidSms, pars).then((res) => {

                        if(res.returnCode==='200'){
							this.curStep = 3;
						}else{
                        	return Promise.reject(res);
						}

                    }).catch(res => {
                        if (res.returnCode == '505') {
                            this.mdnValidCodeMsg =res.msg || '短信验证码不正确'
                        }
                        else
                            this.mdnValidCodeMsg = res.msg;
                    })
                }
            },
            //第3步，修改新密码
            handleSetPassword() {
                if (this.isPasswordValid()) {
                    var pars = {
						newPassword: this.password,//	新密码	string
                        userId: this.userId,//	用户ID	string
                        verifyCode: this.mdnValidCode,  //验证码
                        mobile: this.mdn, //手机号
                    };
                    //校验修改新密码
                    this.$x.post(this.urls.GetBackPwd, pars, {noToken: true}).then((res) => {
                        //if (res.data) {
                        this.curStep = 4;
                        //}
                    }).catch(res => this.passwordMsg = res.msg)
                }
            },
            //第4步，返回
            handleGoLogin() {
                this.$emit('lgForgetPwdDone');
            },
            //第1步校验，用户名手机校验
            isInputValid() {
                if (!this.hideCorp) {
                    this.spCodeMsg = this.spCode ? '' : '企业编号不能为空';
                    this.accountMsg = this.account ? '' : '用户名不能为空';
                }
                this.mdnMsg = this.mdn ? '' : this.$x.msg.VALIDATE.NO_MDN;
                if (this.mdn && !validData.isMdn(this.mdn)) {
                    this.mdnMsg = this.$x.msg.VALIDATE.ERR_MDN
                }
                this.validCodeMsg = this.validCode ? '' : this.$x.msg.VALIDATE.NO_VALICODE;

                return !this.spCodeMsg && !this.accountMsg && !this.mdnMsg && !this.validCodeMsg;
            },
            //第2步校验，手机验证码正确否？
            isMdnCodeValid() {
                this.mdnValidCodeMsg = validData.checkValiCode(this.mdnValidCode)
                    ? '' : this.$x.msg.VALIDATE.ERR_VALICODE;
                return !this.mdnValidCodeMsg
            },
            //第3步校验，密码输入正确否？
            isPasswordValid() {
                this.passwordMsg = this.password ? '' : '新密码不能为空';
                this.password2Msg = this.password2 ? '' : '确认新密码不能为空';
                if (this.password !== this.password2) this.password2Msg = '您两次输入的密码不一致';

                var pwdInfo = this.pwdInfo;
                if (!this.passwordMsg && !pwdInfo.isOk()) {
                    this.passwordMsg = '密码不符合要求'
                }
                return !this.passwordMsg && !this.password2Msg;
            },
        }
    }
</script>

<style scoped lang="scss">

    .forget-ctn {
        max-width: 12rem;
        padding-top: 1.2rem;
    }

    .lg-forget-step {
        padding: .5rem 0;
        width: 8rem;
        margin: 0 auto 60px;
    }

    .fgt-box {
        width: 3.2rem;
        margin: auto;

        > div {
            padding: .05rem 0;
        }
        h3 {
            margin: 0 0 .16rem -.7rem;
        }
        .p-left {
            margin: 0 -.7rem;
        }
        .fgt-tip {
            color: #666;
            padding-bottom: .12rem;
            img {
                vertical-align: middle;
            }
        }
    }

    .forget-bt {
        height: .45rem;
        width: 3.2rem;
        margin: .4rem 0 1rem 0;
    }

    .fa-check {
        font-size: .6rem;
        margin-right: .1rem;
    }
	@media(max-width:768px){
		.fgt-box{
			.p-left{
				margin:0;
			}
		}
	}
</style>
<style lang="scss">

    .forget-ctn {
        .el-input {
            width: 3.2rem;
            .el-input__inner {
                height: .46rem;
            }
        }
        button {
            height: .46rem;
        }

        .vc-input {
            width: 2rem;
        }
        .el-input__inner {
            background: #f5f5f5;
            &:focus {
                background: #fff;
            }
        }
        //.valid-code {
        //换一张
        //span {
        //    position: absolute;
        //    display: inline-block;
        //    transform: translate(1.3rem, -100%);
        //}
        //}
    }

    .pwd-strength-poper {
        color: #333;
        background: #f5f5f5;
        border: 1px solid #ddd;
        box-shadow: none;

        .popper__arrow:after {
            border-right-color: #f5f5f5 !important;
        }
    }

</style>
