<template>
    <msg-input :msg.sync="msg">
        <icon-input v-bind="$props" :class="{'err-input': msg}" @input="handleInput">
            <slot></slot>
        </icon-input>
    </msg-input>
</template>

<script>
    import msgInput from './msgInput.vue';
    import iconInput from './iconInput.vue';

    export default {
        props: {
            msg: String, //提示信息
            value: String,//以下3个同input
            placeholder: String,
            type: String,
        },
        methods: {
            handleInput (val) {
                this.$emit('input', val);
                this.$emit('update:msg', ''); //通过父组件的sync绑定，来清空msg
            }
        },
//        computed: {},
        components: {msgInput, iconInput}
    }
</script>
