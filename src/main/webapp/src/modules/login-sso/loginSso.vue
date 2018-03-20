<template>
    <div class="login-main">
        <img class="ssoLogin-bg full" src="../../assets/img/loginSso/ssologin_bg.jpg"/>
        <div class="logo">
            <img src="../../assets/img/loginSso/logo.png" alt=""/>
        </div>
        <div class="login-main-box" v-if="!pageStatus">
            <div class="login-box">
                <div class="boxShadow">
                    <div class="login-box-header">欢迎登录智慧会议</div>
                    <div class="login-box-content">
                        <el-tabs :class="showCorp ? 'hide-tab':''" v-model="spActive">
                            <el-tab-pane name="corp">
                                <span slot="label"><a class="title">企业登录</a></span>
                                <div class="label-box">
                                    <login-cont @lgForgetPwd="goForgetPwd" :gray-button="true"></login-cont>
                                </div>
                            </el-tab-pane>
                            <el-tab-pane name="mdn">
                                <span slot="label"><a class="title">快速登录</a></span>
                                <div class="label-box">
                                    <login-cont-quick :show-corp.sync="showCorp" @lgForgetPwd="goForgetPwd"
                                                      :gray-button="true">
                                    </login-cont-quick>
                                </div>
                            </el-tab-pane>
                        </el-tabs>
                    </div>
                </div>
            </div>
        </div>
        <div class="forgetStyle">
            <forgot-pass-sp v-if="pageStatus=='forgetSpPwd'" @lgForgetPwdDone="backLogin"></forgot-pass-sp>
            <forgot-pass-mdn v-if="pageStatus=='forgetMdnPwd'" @lgForgetPwdDone="backLogin"></forgot-pass-mdn>
        </div>
    </div>
</template>

<script>
    //import  from '';
    import Vue from 'vue';
    import $x from '@/assets/js/$x';
    import {Tabs, TabPane} from 'element-ui';

    Vue.use(Tabs).use(TabPane);
    Vue.prototype.$x = $x;
    const comps = {
        'login-cont': require('../login/children/login-cont-sp.vue'),
        'login-cont-quick': require('../login/children/login-cont-mdn.vue'),

        'forgot-pass-sp': require('../login/children/forgot-pass-sp.vue'),
        'forgot-pass-mdn': require('../login/children/forgot-pass-mdn.vue'),
    }
    export default {
        props: {},
        data() {
            return {
                pageStatus: '',
                showCorp: false //是否切换到选择企业
            };
        },
        created() {
            if (location.hash === '#forgetpwd') {
                this.goForgetPwd('forgetMdnPwd') //forgetSpPwd
            }
        },
        computed: { //corp-显示企业登录，mdn-手机登录
            spActive: {
                get () {
                    return this.$x.storageUtil.getObj('login_tab') || 'mdn';
                },
                set (v) {
                    this.$x.storageUtil.setObj('login_tab', v)
                }
            },
        },
        methods: {
            backLogin() {
                location.hash = ''
                this.pageStatus = ''
            },
            goForgetPwd(gotoPage) {
                this.pageStatus = gotoPage
            },
        },
        components: comps
    }
</script>
<style lang="scss">
    @media (max-width: 768px) {
        html {
            font-size: 625% !important;
        }
    }

    @media (max-width: 360px) {
        .login-main-box .lg-tab-box .right {
            display: none;
        }
    }

    .login-main {
        width: 100%;
        position: relative;
        .ssoLogin-bg {
            position: fixed;
        }
        .logo {
            width: 2.4rem;
            position: relative;
            top: .36rem;
            left: .6rem;
            z-index: 2;
        }
        .login-main-box {
            width: 100%;
            height: 100%;
            position: relative;
            .login-box {
                max-width: 4.8rem;
                height: 5.02rem;
                margin: 2rem auto 0 auto;
                background: #FAFAFA;
                border-radius: .1rem;
                .boxShadow {
                    height: 100%;
                    box-shadow: 0px 2px .13rem 1px rgba(0, 0, 0, 0.12);
                    .login-box-header {
                        width: 100%;
                        height: 0.86rem;
                        line-height: 0.86rem;
                        background-color: #E0E0E0;
                        border-radius: 0.08rem;
                        text-align: center;
                        font-size: 0.26rem;
                        color: #333333;
                    }
                    .login-box-content {
                        height: 4.16rem;
                        .hide-tab {
                            height: 100%;
                        }
                        .el-tabs__item {
                            width: 2.4rem;
                            text-align: center;
                        }
                        .title {
                            font-size: 0.18rem;
                        }
                        .label-box {
                            margin: 0 0.5rem;
                            .choose-corp h2 {
                                margin: .3rem 0;
                            }
                            .lg-tab-box {
                                .choose-corp {
                                    background: #FAFAFA;
                                }
                                > div {
                                    padding-bottom: .16rem;
                                    .w100 {
                                        height: .42rem;
                                        border-radius: .5rem;
                                    }
                                    .el-input__inner {
                                        border-radius: .5rem;
                                    }
                                    :-moz-placeholder {
                                        /* Mozilla Firefox 4 to 18 */
                                        color: #C0C0C0;
                                        font-size: .14rem;
                                    }
                                    ::-moz-placeholder {
                                        /* Mozilla Firefox 19+ */
                                        color: #C0C0C0;
                                        font-size: .14rem;
                                    }
                                    input:-ms-input-placeholder {
                                        color: #C0C0C0;
                                        font-size: .14rem;
                                    }
                                    input::-webkit-input-placeholder {
                                        color: #C0C0C0;
                                        font-size: .14rem;
                                    }
                                }
                            }
                            .lg-tab-box .lg-form {
                                padding-bottom: 0;
                            }
                            .lg-tab-box > .t-center,
                            .lg-tab-box .lg-form > .t-center {
                                padding-bottom: 0;
                            }
                            .lg-tab-box > .t-right,
                            .lg-tab-box .lg-form > .t-right {
                                margin: .2rem 0;
                                .main-color {
                                    font-size: .12rem;
                                    color: #666666;
                                }
                            }
                            .lg-tab-box .lg-form > div {
                                padding: 0;
                                padding-bottom: .16rem;
                            }
                            .lg-tab-box .mdn-form .t-right .main-color {
                                font-size: .12rem;
                                color: #666666;
                            }
                        }
                    }
                }
                .el-tabs {
                    height: 100%;
                }
                .el-tabs__header {
                    border-bottom: 0;
                    line-height: 0.68rem;
                    margin: 0;
                }
                .hide-tab .el-tabs__header {
                    display: none;
                }
                .el-tabs__content {
                    margin-bottom: 1rem;
                    height: 100%;
                    .el-tab-pane {
                        height: 100%;
                        /*background: #fff;*/
                    }
                }
            }
        }
        .forgetStyle {
            position: relative;
            margin: 1.8rem auto 0;
            width: 8rem;
        }
    }
</style>