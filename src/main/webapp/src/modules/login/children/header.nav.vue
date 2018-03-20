<template>
    <div class="top-banner b-center">
        <div class="t-left">
            <div class="logo">
                <img class="left"
                     src="../../../assets/img/unioncom.png"
                     alt="logo">
                <div class="left">
                    <strong> 一信通</strong><span>&nbsp;&nbsp;<b>·</b>&nbsp;&nbsp;</span>
                    <span>{{navTitle}}</span>
                </div>
            </div>
        </div>
        <!--<div class="lay-cell t-center">-->

        <!--</div>-->
        <!--<div class="lay-cell t-right lg-tel">-->
        <!--<div class="left t-right">-->
        <!--<i class="lay-cell fa fa-phone"></i>-->
        <!--<b class="lay-cell text">&nbsp; 咨询<br/>电话</b>-->
        <!--</div>-->
        <div v-if="pageStatus" class="right lg-now">
            马上<a class="main-color" @click="$emit('lgForgetPwdDone')">登录</a>
        </div>
    </div>
</template>

<script>
    //import  from '';
    export default {
        props: {
            pageStatus: ''  //页面状态，指示为登录或忘记密码。见login.vue
        },
        data () {
            return {};
        },
        methods: {},
        computed: {
            //左上方标题
            navTitle () {
                return (this.pageStatus || '').match(/forget\w+Pwd/) ? '找回密码' : '用户登录';
            }
        },
        components: {}
    }
</script>

<style scoped lang="scss">
    @import "../../../assets/css/defines.scss";

    .top-banner {
        height: .7rem;
        max-width: 12rem;
        padding: .1rem .1rem .09rem .1rem;

        .logo {
            > img {float: left;
                width: .86rem;
                height: .50rem;
            }
            > div {float: left;
                line-height: .4rem;
                height: .4rem;
                border-left: 1px solid #eee;
                margin: .1rem .1rem;
                padding-left: .1rem;

                > strong {float: left;
                    font-size: .22rem;
                    color: #333;
//                    font-weight:normal;
                }

                > span {float: left;
                    font-size: .16rem;
                    color: #828282;
                }
            }

        }
        .lg-now {
            line-height: .6rem;
        }
    }

    /*.lg-tel {
        width: 2.8rem;
        .left {
            margin-top: .08rem;
            padding: 0 .06rem;
            color: $color-primary;
            border: 1px solid $color-primary;
            border-radius: .05rem;

            i.fa {
                font-size: .4rem;
            }
        }

        h3 {
            color: $color-primary;
            margin: 0;
            font-size: .26rem;
        }

        .lg-tel-time {
            white-space: nowrap;
            font-size: .12rem;
        }
    }*/
</style>
