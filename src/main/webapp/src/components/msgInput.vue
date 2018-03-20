<template>
    <div>
        <el-input v-if="!$slots.default" :placeholder="placeholder" :value="value"
                  @input="handleInput" :class="{'err-input': msg}" :type="type"></el-input>
        <slot></slot>
        <div class="msg" v-if="msg">
            <span>{{msg}}</span>
        </div>
    </div>
</template>

<script>
    /*
    右侧为提示文本的输入框或自定义控件。用法：
    1、输入框：
        <msg-input :msg.sync="mdnMsg" placeholder="请输入手机号" v-model="mdn"></msg-input>
    2、自定义左侧控件：
        <msg-input :msg.sync="validCodeMsg">
            <validate-img v-model="validCode" :load-url="urls.LoadValidImg"></validate-img>
        </msg-input>
    */
    import Vue from 'vue';
    import {Row, Col} from 'element-ui';

    Vue.use(Row).use(Col);

    //import  from '';
    export default {
        props: {
            msg: String,            //右侧显示的错误文本
            placeholder: String,    //以下3个同input
            value: String,
            type: String,
        },
        methods: {
            handleInput (val) {
                this.$emit('input', val);
                this.$emit('update:msg', ''); //通过父组件的sync绑定，来清空msg
            }
        },
//        computed: {},
        components: {}
    }
</script>

<style scoped lang="scss">
    .msg {
        color: #f33;
        vertical-align: middle;
        padding: 0 0 .2rem .05rem;
        span {
            position: absolute;
        }
    }
</style>
