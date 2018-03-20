<template>
    <el-input v-bind="$props" :value="value" class="pre-icon-input" @input="emitInput">
        <template slot="prepend">
            <slot></slot>
        </template>
    </el-input>
</template>

<script>
    /*
    左侧带icon的自定义控件。
    */
    import Vue from 'vue';
    import {Input} from 'element-ui';

    Vue.use(Input);

    export default {
        props: {
            placeholder: String,//以下3个同input
            value: String,
            type: String,
        },
        methods: {
            emitInput (val) {
                this.$emit('input', val);
            }
        },
//        computed: {},
//        components: {}
    }
</script>

<style lang="scss">
    .pre-icon-input {
        position: relative;
        > div {
            position: absolute;
            border: 0;
            top: 13px;
            background: transparent;
        }
        > input {
            text-indent: 35px;

            height: 46px;
            line-height: 46px;
        }

    }
</style>
