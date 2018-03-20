<template>
    <forgot-pass-sp :hide-corp="true" @lgForgetPwdDone="$emit('lgForgetPwdDone')">

    </forgot-pass-sp>
</template>

<script>
    import forgotPassSp from './forgot-pass-sp.vue';

    export default {
        components: {forgotPassSp},
        props: {},
        methods: {}
    }
</script>

<style scoped>
</style>
