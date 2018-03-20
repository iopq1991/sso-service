<template>
    <div class="pwd-strength">
        <div :class="'s-' + strength">
            <span>安全等级</span>
            <div class="mc-count" v-for="n in strength"></div>
            <div class="mc-count mc-gray" v-for="n in 3-strength"></div>
        </div>

        <div><i :class="lengthCls"></i> 6 ~ 16位字符</div>
        <div><i :class="charsCls"></i> 只包含大写字母、小写字母、数字或标点符号（除空格）</div>
        <div><i :class="matchCls"></i> 大写字母、小写字母、数字或标点符号至少包含2种</div>
    </div>
</template>

<script>
    import {Popover} from 'element-ui';
    import Vue from 'vue';

    Vue.use(Popover);

    export default {
        props: {
            pwdInfo: Object //传入的密码信息参数
        },
        computed: {
            //密码强度
            strength () {
                var r = (this.pwdInfo.matchCount || 1) - 1;
                if (r > 3) r = 3;
                if (r < 0) r = 0;
                return r
            },
            //长度的校验结果
            lengthCls () {
                return this.getCls(this.pwdInfo.lengthOk)
            },
            //字符的校验结果
            charsCls () {
                return this.getCls(this.pwdInfo.charsOk)
            },
            //符合数目的校验结果
            matchCls () {
                return this.getCls(this.pwdInfo.matchOk)
            }
        },
        components: {},
        methods: {
            //是否符合的显示结果
            getCls (check) {
                if (this.pwdInfo.empty)
                    return 'fi-circle';
                else
                    return check ? 'fi-correct' : 'fi-error';
            }
        }
    }
</script>

<style scoped lang="scss">
    @import "../assets/css/defines.scss";

    .pwd-strength {
        > div {
            line-height: .2rem;
            &:first-child {
                padding-bottom: .12rem;
                span {
                    margin-right: .2rem;
                }
            }
        }
        .mc-count {
            display: inline-block;
            height: .08rem;
            width: .42rem;
            margin-left: .02rem;
            border-radius: .04rem;
        }

        .s-1 > div {
            background: #f5533c;
        }
        .s-2 > div {
            background: $color-primary;
        }
        .s-3 > div {
            background: #0ea65b;
        }
        .mc-gray {
            background: #bbb !important;
        }
    }

    .fi-correct {
        color: #3b3;
    }

    .fi-error {
        color: #f33;
    }

    .fi-circle {
        color: #bbb;
        font-size: .16rem;
        vertical-align: top;
    }
</style>
<style lang="scss">
    @import "../assets/css/defines.scss";

</style>