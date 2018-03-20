<template>
    <div class="lay-tbl">
        <el-input class="lay-cell vc-input" placeholder="请输入验证码" :value="value"
                  @input="handleInput" :class="inputClass"></el-input>
        <div class="lay-cell valid-code" @click="loadValidCode">
            <a @click="loadValidCode">
                <img :src="validCodeUrl" alt="loading..."/>
                <!--<span class="main-color">换一张</span>-->
            </a>
        </div>
    </div>
</template>

<script>
    /*
    短信验证码发送、输入组件
    */
	import appConfig from '../../../config/app-config'
    export default {
        props: {
            loadUrl: String,    //获取验证码图片地址的接口url
            value: String,      //v-model值
            inputClass: Object, //输入框的样式
        },
        data () {
            return {
                validCodeUrl: ''    //验证码图片地址
            };
        },
        created () {
            this.loadValidCode()
        },
        methods: {
            //实现v-model
            handleInput (val) {
                this.$emit('input', val);
                this.$emit('change');
            },
            //调用接口，获取验证码地址
            loadValidCode () {
                //显示图形验证码
				this.validCodeUrl=appConfig.API_SERVER+this.loadUrl+"?width=71&height=20&codeCount=4&lineCount=0&ramdom="+Math.random();
            }
        },
        computed: {},
        components: {}
    }
</script>

<style scoped lang="scss">
    .valid-code {

        img {
            width: 1.1rem;
            height: .45rem;
            vertical-align: middle;
            margin-left: .1rem;
        }
    }
</style>
