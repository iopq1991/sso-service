<template>
    <div>
        <div class="lay-tbl">
            <msg-input :msg.sync="msg">
                <el-input placeholder="手机验证码" class="lay-cell vcode-input" :value="value"
                          @input="handleInput" :class="{'err-input': msg}"></el-input>
            </msg-input>
            <div class="lay-cell send-vcode">
                <el-button class="sms-send-bt" @click="handleSend" type="primary" :disabled="restSeconds>0">{{buttonText}}</el-button>
            </div>
        </div>
        <div v-if="hasSent" class="smsvc-tip">
            <img src="../../assets/img/login/success.png"> 验证码已发送到手机，15分钟之内有效
        </div>
    </div>
</template>

<script>
    /*
    短信验证码发送、输入组件
    */
    import Vue from 'vue';
    import {Button, Input} from 'element-ui';
    import msgInput from "../../components/msgInput.vue";
    import validData from '../../assets/js/validData'

    Vue.use(Button).use(Input);

    export default {
        props: {
            mdn: String,            //手机号码
            validImageCode: String, //图形验证码
            //sessionId: String,    //获取图形验证码时得到的sessionId
            value: String,          //v-model值
            sendUrl: {              //发送短信的url
                type: String,
                default: '/captcha/send/mobile/code'
            },
            msg: String              //提示信息
        },
        data () {
            return {
                restSeconds: -1, //发送之后，不能再次发送所剩余的时间
                hasSent: false, //短信验证码已发送成功
            }
        },
        computed: {
            //按钮文本
            buttonText () {
                var left = this.restSeconds > 0 ? '(' + this.restSeconds + ')' : '';
                return this.restSeconds > -1
                    ? '重新发送' + left
                    : '发送验证码';
            }
        },
        methods: {
            //发送短信验证码
            handleSend () {
                if (!this.mdn) {
                    return this.emitMdnErr(this.$x.msg.VALIDATE.NO_MDN);
                }
                if (!validData.isMdn(this.mdn)) {
                    return this.emitMdnErr(this.$x.msg.VALIDATE.ERR_MDN);
                }
                let it = this;
                var pars = {validCode: it.validImageCode, mobile: it.mdn};
                this.$x.get(this.sendUrl, pars).then((res) => {
                    it.restSeconds = 60;
                    countSeconds();
                    this.hasSent = true;
                }).catch(res => {
                    this.hasSent = false;
                    this.setMsg(res.msg);
                })

                function countSeconds () {
                    it.restSeconds -= 1;
                    if (it.restSeconds > 0)
                        setTimeout(countSeconds, 1000);
                }
            },
            //同步msg文本
            setMsg (msg) {
                this.$emit('update:msg', msg)
            },
            //触发手机号有误时间，供父组件提示
            emitMdnErr (msg) {
                //手机号有误，通知父组件显示信息
                this.$emit('mdnError', msg)
            },
            //处理输入，清空错误信息
            handleInput (val) {
                this.$emit('input', val);
                this.$emit('change');
                this.setMsg('')
            }
        },
        components: {msgInput}
    }
</script>

<style scoped lang="scss">
    .vcode-input {
        width: 2rem;
    }

    .send-vcode {
        padding-left: .1rem;
        vertical-align: top;
    }

    .sms-send-bt {
        width: 1.2rem;
    }

    .smsvc-tip {
        line-height: .36rem;
        img {
            vertical-align: middle;
        }
    }
</style>
