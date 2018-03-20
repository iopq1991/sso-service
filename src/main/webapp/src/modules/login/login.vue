<template>
    <div class="login-main" @click="eventBus.$emit('body-click')">
        <link rel="shortcut icon" href="../../assets/img/favorites.png">
        <img src="../../assets/img/login/login_bg.png" class="longin-bg-img full"/>

        <div class="white-bg head-wraper">
            <header-nav :page-status="pageStatus" @lgForgetPwdDone="backLogin"></header-nav>
        </div>
        <div v-if="!pageStatus" class="cnt-wraper">
            <div class="login-panel b-center inlnb lay-tbl">
                <el-carousel class="el-carousel full lay-cell"
                             :arrow="loginCfg.banners.length>1?'always':'never'">
                    <el-carousel-item class="banner-item" v-for="(banner,index) in loginCfg.banners" :key="index">
                        <img class="full" :src="banner.img" @click="location=banner.link"/>
                    </el-carousel-item>
                </el-carousel>

                <div class="login-box lay-cell">
                    <div>
                        <div>
                            <el-tabs :class="showCorp ? 'hide-tab':''" v-model="spActive">
                                <el-tab-pane name="corp">
                                    <span slot="label" class="left-tab"><a>企业编号登录</a></span>
                                    <login-cont @lgForgetPwd="goForgetPwd"></login-cont>
                                </el-tab-pane>
                                <el-tab-pane name="mdn">
                                    <span slot="label"><a>手机登录</a></span>
                                    <login-cont-quick :show-corp.sync="showCorp"
                                                      @lgForgetPwd="goForgetPwd"></login-cont-quick>
                                </el-tab-pane>
                            </el-tabs>
                        </div>
                        <!--<valid-mdn-form></valid-mdn-form>-->
                    </div>
                </div>
            </div>
            <float-contact></float-contact>
        </div>

        <forgot-pass-sp v-if="pageStatus=='forgetSpPwd'" @lgForgetPwdDone="backLogin"></forgot-pass-sp>
        <forgot-pass-mdn v-if="pageStatus=='forgetMdnPwd'" @lgForgetPwdDone="backLogin"></forgot-pass-mdn>

        <footer-nav :login-cfg="loginCfg"></footer-nav>
    </div>
</template>
<script type="text/ecmascript-6">
    import Vue from 'vue';
    import $x from '@/assets/js/$x';
    import {Input, Checkbox, Button, Carousel, CarouselItem, Tabs, TabPane} from 'element-ui';

    let banner1 = require('@/assets/img/login/banner1.png');

    Vue.use(Input).use(Checkbox).use(Button).use(Carousel).use(CarouselItem).use(Tabs).use(TabPane);

    Vue.prototype.$x = $x;

    const comps = {
        'footer-nav': require('./children/footer-nav.vue'),
        'login-cont': require('./children/login-cont-sp.vue'),
        'login-cont-quick': require('./children/login-cont-mdn.vue'),
        'header-nav': require('./children/header.nav.vue'),
        'float-contact': require('./children/float-contact.vue'),

        'forgot-pass-sp': require('./children/forgot-pass-sp.vue'),
        'forgot-pass-mdn': require('./children/forgot-pass-mdn.vue'),
    }

    export default {
        components: comps,
        data() {
            return {
                pageStatus: '', //页面状态，空值为登录界面，forgetSpPwd|forgetMdnPwd为忘记密码界面
                loginCfg: {}, //登录页的配置，包括轮播图

                showCorp: false //是否切换到选择企业
            }
        },
        created() {
            //暂时没有轮播图
//            $x.post('/User/initLogin', {}).then((res) => {
//                this.loginCfg = res.data
//            })
            this.loginCfg = {
                banners: [{img: banner1, link: '#'}]
            };
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
            //由忘记密码返回登陆
            backLogin() {
                location.hash = ''
                this.pageStatus = ''
            },
            //跳到忘记密码
            goForgetPwd(gotoPage) {
                this.pageStatus = gotoPage
            },
        }
    }
</script>
<style lang="scss" scoped>
    @import "../../assets/css/defines.scss";

    .longin-bg-img {
        position: fixed;
        z-index: -99;
    }

    .login-panel {
        max-width: 12rem;
        height: 4.7rem;

        .login-box {
            height: 100%;
            padding: 0 .2rem;

            > div {
                height: 4.7rem;
                width: 4rem;
                background: #fff;
                box-shadow: 0px 2px .13rem 1px rgba(0, 0, 0, 0.12);
            }
        }
    }

    .head-wraper {
        border-bottom: 1px solid $color-primary;
        box-shadow: 0 0 0.12rem rgba(0, 0, 0, 0.2);
    }

    .cnt-wraper {
        padding-top: 1.2rem;
    }

    .lg-tab-box {
        padding: 0 .4rem;
    }

    .left-tab:after {
        content: '';
        display: inline-block;
        height: 20px;
        border-right: 1px solid #eee;
        position: absolute;
        right: 0;
        top: 12px;
    }

    //移动端布局
    @media (max-width: 768px) {
        .login-panel {
            > .el-carousel {
                display: none;
            }
            .login-box {
                padding: 0;
                > div {
                    height: 4.3rem;
                    width: 3.5rem;
                }
            }
        }
        .cnt-wraper {
            padding-top: .2rem;
        }
    }
</style>

<style lang="scss">
    @import "../../assets/css/defines.scss";

    //覆盖element样式，单位用px，防止错位
    .login-main {
        .el-tabs__nav {
            width: 100%;
        }
        .el-tabs__header {
            line-height: 60px;

            .el-tabs__item {
                width: 50%;
                text-align: center;
                font-size: 16px;
                font-family: "Microsoft YaHei";

            }
        }
        .el-tabs__item.is-active {
            font-weight: bold;
        }
        .hide-tab .el-tabs__header {
            display: none;
        }
        //tab页的底部
        .el-tabs__active-bar {
            display: none;
        }

        .el-button--large {
            height: 46px;
        }

        //输入框图标
        .el-input-group__prepend {
            padding: .10px 12px;
        }

        //轮播图底部
        .login-panel > .el-carousel .el-carousel__indicators {
            &:before, &:after {
                content: '';
                display: inline-block;
                width: 8px;
                height: 8px;
                border-radius: 50%;
                background: #ddd;
                margin: 0 5px;
            }
            button {
                height: 8px;
                border-radius: 4px;
                background: $color-primary;
            }
        }

        //输入框
        .login-box {
            .el-input__inner {
                background: #f5f5f5 !important;
                &:focus {
                    background: #fff !important;
                }
            }
        }
        //切换企业轮播图比例
        //.corp-switch.is-active {
        //    transform: translateX(80px) scale(1.204) !important;
        //}

        .el-tabs__header {
            margin-bottom: 0;
        }

        .lg-footer {
            margin-top: 1rem;
        }
    }
</style>
