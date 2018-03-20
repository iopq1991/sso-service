<template>
    <div class="lg-tab-box" @keydown.enter="handleMdnLogin" v-focus="'input'">
        <div v-if="showCorp" class="choose-corp">
            <h2 class="t-center">请选择登录企业</h2>
            <el-carousel class="carousel" :interval="0" type="card" height="100px" arrow="always" trigger="click"
                         :initial-index="enterprises.findIndex(i=>i.spCode==selectedSpCode)" @change="handleCorpChange">
                <el-carousel-item v-for="item in enterprises" :key="item.spCode" label=" " class="corp-switch">
                    <img v-if="item.logo" :src="item.logo"/>
                    <img v-if="!item.logo" src="../../../assets/img/logo_default.png"/>
                </el-carousel-item>
            </el-carousel>
            <div class="full t-center" :class="ajaxMsg?'corp-name':'corp-names'">{{ selectedCorp.spName }}</div>
			<div v-if="ajaxMsg" class="t-center err-ajax" style="padding: 25px">{{ajaxMsg}}</div>
            <div slot="footer" class="t-center">
                <el-button type="primary" size="large" class="w100" @click="goHome"
                           :disabled="grayButton && going" :loading="going">
                    进 入
                </el-button>
            </div>
            <div class="t-right back-login"><a @click="switchCorpShow(false)">返回登录</a></div>
        </div>
        <div v-if="!showCorp" class="mdn-form">
            <div v-if="ajaxMsg" class="err-ajax">{{ajaxMsg}}</div>
            <msg-icon-input placeholder="请输入手机号" v-model="mdn" :msg.sync="mdnMsg" @input="ajaxMsg=''">
                <i class="fi-cell-phone-number"></i>
            </msg-icon-input>
            <msg-icon-input placeholder="请输入密码" v-model="password" :msg.sync="passwordMsg" type="password"
                            @input="ajaxMsg=''">
                <i class="fi-password"></i>
            </msg-icon-input>
            <!--<el-input placeholder="请输入校验码" v-model="vCode">-->
            <!--<template slot="prepend"><i class="fa fa-building-o"></i></template>-->
            <!--</el-input>-->

            <div>
                <div class="left">
                    <el-checkbox v-model="rememberMdn">记住手机号</el-checkbox>
                </div>
                <div class="right">
                    <el-checkbox v-model="rememberPwd"  style="display:none;">记住密码</el-checkbox>
                </div>
                <div class="clearfix"></div>
            </div>
            <div class="t-center">
                <el-button type="primary" size="large" class="w100" @click="handleMdnLogin"
                           :disabled="grayButton && going" :loading="going">{{buttonText}}
                </el-button>
            </div>
            <div class="t-right">
                <a @click="handleForgetMdnPwd" class="main-color">忘记密码？</a>
            </div>
        </div>
    </div>
</template>

<script>
    require('@/components/directives/foucs.js');
    import validData from '@/assets/js/validData';
    import Vue from 'vue';
    import {Dialog, Carousel, Button} from 'element-ui';
    import msgIconInput from '@/components/msgIconInput.vue';
    import util from '../util/loginUtil.js';

    Vue.use(Dialog).use(Carousel).use(Button);

    const RememberKey = 'loginMdnRemember';
    const RememberPwdKey = 'loginMdnPwdRemember';

    export default {
        props: {
            showCorp: Boolean, //切换到选择企业
            grayButton: Boolean, //点击后按钮是否变灰
        },
        data() {
            return {
                mdn: '',            //手机号
                password: '',       //密码
                rememberMdn: false, //记住手机号
                rememberPwd: false, //记住密码

                enterprises: [],    //企业列表
                selectedCorp: {},   //手机号登陆成功后切换的企业
                selectedSpCode: '', //手机号登陆成功后切换的企业Code
                token: '',          //登录token

                mdnMsg: '',         //提示信息
                passwordMsg: '',

                ajaxMsg: '',        //登录返回结果
                going: false,       //正在请求ajax
            }
        },
        created() {
            this.loadRemembered()
        },
        components: {msgIconInput},
        computed: {
            //登录按钮文本
            buttonText() {
                return this.going ? '登录中...' : '登 录'
            }
        },
        methods: {
            //提交登录
            handleMdnLogin() {
                if (!this.isInputValid()) {
                    return
                }
                this.saveRemember();
                this.going = true;

                var pars = {
                    //dest: this.$x.CONST.LOGIN_DEST.PC,
                    mobile: this.mdn,
                    password: this.password,
                    rememberPwd: this.rememberPwd ? 1 : 0,
                    useRemembered: this.password === this.$x.CONST.FAKE_PWD ? 1 : 0,
					type:2,
					source:2
                };

                this.$x.post(this.$x.config.API_SERVER_SSO + '/sso/token/get', pars
                    , {noToken: true, maskOptions: false})
                    .then((res) => {
                        if (res.data && res.data.token) {
                            this.token = res.data.token;

                            if (res.data.enterpriseVOList) {
                                if (res.data.enterpriseVOList.length > 1) {
                                    this.enterprises = res.data.enterpriseVOList;
                                    this.switchCorpShow(true);
                                }
                                else {
                                    this.selectedCorp = res.data.enterpriseVOList[0];
                                    this.selectedSpCode = res.data.enterpriseVOList[0].spCode;
                                    this.goHome();
                                }
                            }
                            else {
                                this.ajaxMsg = '此用户缺少企业信息'
                            }
                        }
                        else {
                            throw res;
                        }
                    })
                    .catch(res => {
                        if (res.returnCode == '2001')
                            this.ajaxMsg = '用户信息错误';
                        else
                            this.ajaxMsg = res.msg||'服务器异常，请稍后再试';
                    })
                    .then(res => {
                        this.going = false;
                    })
            },
            //切换企业Tab页是否显示（登录后选企业时，上面的Tab要隐藏掉）
            switchCorpShow(show) {
                this.$emit('update:showCorp', show);
				this.ajaxMsg = '';
            },
            //登录成功，上报当前企业，跳转
            goHome() {
                this.$x.tokenUtil.set(this.token);
                if (this.selectedSpCode) {
                    this.$x.storageUtil.setObj(this.$x.CONST.REMEMBER_SP_KEY, this.selectedSpCode);

                    let pars = {token: this.token, spId: this.selectedCorp.spId,corpId:this.selectedCorp.id};
                    let config = {
                        noToken: true,
                        showError: true,
                        maskOptions: false
                    };
                    this.$x.post(this.$x.config.API_SERVER_SSO + '/sso/sp/choice', pars, config)
                        .then((res) => {
                            //this.switchCorpShow(false);
                            //this.going = true;
							if(res.returnCode=='200'){
								util.goRedirect();
							}else{
								throw res;
							}

                        }).catch(res=>{
						this.ajaxMsg = res.msg||'服务器异常，请稍后再试';
					})
                }
                else {
                    util.goRedirect();
                }
            },
            //跳到忘记密码
            handleForgetMdnPwd() {
                location.hash = '#forgetpwd';
				this.$x.storageUtil.setObj('from_tab', 0);
                this.$emit('lgForgetPwd', 'forgetMdnPwd')
            },
            //切换企业
            handleCorpChange(index) {

                this.selectedCorp = this.enterprises[index];
                this.selectedSpCode = this.enterprises[index].spCode;
				this.ajaxMsg = '';
            },
            //输入的校验
            isInputValid() {
                if (!this.mdn) {
                    this.mdnMsg = '手机号码不能为空';
                    return false
                }
                else if (!validData.isMdn(this.mdn)) {
                    this.mdnMsg = '手机号输入有误';
                    return false
                }
                if (!this.password) {
                    this.passwordMsg = '密码不能为空';
                    return false
                }

                return true
            },
            //加载记住的账号密码
            loadRemembered() {
                var loginRem = this.$x.storageUtil.getObj(RememberKey)
                if (loginRem) {
                    this.rememberMdn = true;
                    this.mdn = loginRem.mdn;
                }

                this.rememberPwd = this.$x.storageUtil.getObj(RememberPwdKey)
                if (this.rememberPwd) {
                    this.password = this.$x.CONST.FAKE_PWD;
                }

                //当前选中的公司
                this.selectedSpCode = this.$x.storageUtil.getObj(this.$x.CONST.REMEMBER_SP_KEY) || '';
            },
            //保存记住账号密码
            saveRemember() {
                var data = this.rememberMdn ? {mdn: this.mdn} : undefined;
                this.$x.storageUtil.setObj(RememberKey, data)

                this.$x.storageUtil.setObj(RememberPwdKey, this.rememberPwd)
            }
        }
    }
</script>

<style scoped lang="scss">
    @import "../../../assets/css/defines.scss";

    .lg-tab-box .mdn-form > div {
        padding-bottom: .2rem;
        &:first-child {
            margin-top: .3rem;
        }
        a {
            color: $color-primary;
        }
    }

    .fa-mobile {
        font-size: .2rem;
    }

    .carousel {
        overflow: visible;
    }

    .choose-corp {
        width: 100%;
        background: #fff;
        position: relative;
        overflow: hidden;

        h2 {
            margin: .6rem 0;
            font-size: .16rem;
            color: #333;
        }
        .corp-switch {
            border-radius: .19rem;
            box-shadow: 0px 9px 25px -7px rgba(0, 0, 0, 0.55);
            width: .9rem;
            height: .9rem;
            left: .35rem;

            img {
                width: 100%;
                max-height: 100%;
            }

            &:not(.is-active) {
                filter: grayscale(100%);
                filter: gray;
            }
        }
        .corp-name {
            margin: .1rem 0;
        }
		.corp-names {
			margin: .4rem 0;
		}
        .back-login {
            padding: .2rem;
            color: $color-primary;
        }
    }

    .err-ajax {
        color: #f33;
        animation: shake 1s ease-out forwards;
    }
</style>

<style lang="scss">
    @import "../../../assets/css/defines.scss";

    .choose-corp {

        .el-carousel__item h3 {
            color: #475669;
            font-size: 14px;
            opacity: 0.75;
            line-height: 200px;
            margin: 0;
        }

        .el-carousel__item:nth-child(2n) {
            background-color: #99a9bf;
        }

        .el-carousel__item:nth-child(2n+1) {
            background-color: #d3dce6;
        }

        .el-carousel__arrow {
            background: none;
            color: #999;
            font-size: .2rem;

            &:hover {
                color: $color-primary;
            }
        }
        .el-carousel__arrow--left {
            left: -10px;
        }
        .el-carousel__arrow--right {
            right: -10px
        }
        .el-carousel__indicators {
            display: none;
        }
        .el-carousel__item--card:not(.is-in-stage) {
            visibility: hidden;
        }
    }
</style>
