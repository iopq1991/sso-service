<template>
    <div class="lg-tab-box" @keydown.enter="handleSpLogin" v-focus="'.el-input:nth-child(2) input'">
        <div class="lg-form">
            <div v-if="ajaxMsg" class="err-ajax">{{ajaxMsg}}</div>
            <msg-icon-input :msg.sync="spCodeMsg" placeholder="企业编号" v-model="spCode" @input="ajaxMsg=''">
                <i class="fi-enterprise-code"></i>
            </msg-icon-input>
            <msg-icon-input :msg.sync="accountMsg" placeholder="用户名" v-model="account" @input="ajaxMsg=''">
                <i class="fi-user-name"></i>
            </msg-icon-input>
            <msg-icon-input :msg.sync="passwordMsg" placeholder="密码" v-model="password" type="password"
                            @input="ajaxMsg=''">
                <i class="fi-password"></i>
            </msg-icon-input>
            <div>
                <div class="left">
                    <el-checkbox v-model="rememberUsn">记住企业编号和用户名</el-checkbox>
                </div>
                <div class="right">
                    <el-checkbox v-model="rememberPwd" style="display:none;">记住密码</el-checkbox>
                </div>
                <div class="clearfix"></div>
            </div>
            <div class="t-center">
                <el-button type="primary" size="large" class="w100" @click="handleSpLogin"
                           :disabled="grayButton && going" :loading="going">{{buttonText}}
                </el-button>
            </div>
        </div>
        <div class="t-right">
            <a @click="handleForgetPwd" class="main-color">忘记密码？</a>
        </div>
    </div>
</template>

<script>
    import Vue from 'vue';
    import {Input, Checkbox, Button, Carousel, CarouselItem, Tabs, TabPane} from 'element-ui';

    Vue.use(Input).use(Checkbox).use(Button).use(Carousel).use(CarouselItem).use(Tabs).use(TabPane);
    require('@/components/directives/foucs.js');

    import validData from '@/assets/js/validData';
    import iconInput from '@/components/iconInput.vue';
    import msgIconInput from '@/components/msgIconInput.vue';
    import util from '../util/loginUtil.js';

    const RememberKey = 'loginSpRemember';
    const RememberPwdKey = 'loginSpPwdRemember';

    export default {
        props: {
            grayButton: Boolean, //点击后按钮是否变灰
        },
        data() {
            return {
                spCode: '',         //输入的spCode
                account: '',        //输入的账号
                password: '',       //密码
                rememberUsn: false, //是否记住用户名
                rememberPwd: false, //是否记住密码

                spCodeMsg: '',      //以下msg为显示的错误提示
                accountMsg: '',
                passwordMsg: '',

                ajaxMsg: '',        //登录返回结果
                going: false,       //正在请求ajax
            }
        },
        created() {
            this.loadRemembered();
        },
        computed: {
            //登录按钮文本
            buttonText() {
                return this.going ? '登录中...' : '登 录'
            }
        },
        components: {iconInput, msgIconInput},
        methods: {
            //提交登录
            handleSpLogin() {
                if (!this.isInputValid()) {
                    return
                }
                this.saveRemember();
                this.going = true;
                var pars = {
                    //dest: this.$x.CONST.LOGIN_DEST.PC,
					spCode: this.spCode,
                    name: this.account,
                    password: this.password,
                    rememberPwd: this.rememberPwd ? 1 : 0,
                    useRemembered: this.password === this.$x.CONST.FAKE_PWD ? 1 : 0,
					type:1,
					source:2
                };
                this.$x.post(this.$x.config.API_SERVER_SSO + '/sso/token/get', pars
                    , {noToken: true, maskOptions: false})
                    .then((res) => {
                        if (res.data && res.data.token) {
                            this.$x.tokenUtil.set(res.data.token);
                            this.$x.storageUtil.setObj(this.$x.CONST.REMEMBER_SP_KEY, this.spCode);
                            util.goRedirect();
                        }
                        else {
                            throw res;
                        }
                    })
                    .catch(res => {
                        if (res.returnCode == '2001')
                            this.ajaxMsg = '用户信息错误'
                        else
                            this.ajaxMsg = res.msg||'服务器异常，请稍后再试';
                    })
                    .then(res => {
                        this.going = false;
                    })
            },
            //跳到忘记密码
            handleForgetPwd() {
                location.hash = '#forgetpwd';
                this.$emit('lgForgetPwd', 'forgetMdnPwd') //forgetSpPwd
            },
            //检验输入内容
            isInputValid() {
                if (!this.spCode) {
                    this.spCodeMsg = '企业编号不能为空';
                    return false
                }
                if (!this.account) {
                    this.accountMsg = '用户名不能为空';
                    return false
                }
                if (!this.password) {
                    this.passwordMsg = '密码不能为空';
                    return false
                }
                return true
            },
            //读取记住的账号密码并设置
            loadRemembered() {
                var loginRem = this.$x.storageUtil.getObj(RememberKey)
                if (loginRem) {
                    this.rememberUsn = true;
                    this.spCode = loginRem.spCode;
                    this.account = loginRem.account;
                }

                this.rememberPwd = this.$x.storageUtil.getObj(RememberPwdKey)
                if (this.rememberPwd) {
                    this.password = this.$x.CONST.FAKE_PWD;
                }
            },
            //保存spCode、账号
            saveRemember() {
                var data = this.rememberUsn ? {spCode: this.spCode, account: this.account} : undefined;
                this.$x.storageUtil.setObj(RememberKey, data)

                this.$x.storageUtil.setObj(RememberPwdKey, this.rememberPwd)
            }
        }
    }
</script>

<style scoped lang="scss">
    @import "../../../assets/css/defines.scss";

    .lg-form > {
        .err-ajax {
            color: #f33;
            animation: shake 1s ease-out forwards;
        }
        div {
            padding-bottom: .2rem;
            &:first-child {
                margin-top: .3rem;
            }
            a {
                color: $color-primary;
            }

        }
    }
</style>
