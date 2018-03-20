<template>
    <transition name="slide-fade" @before-enter="handleEnter">
        <div v-if="visible" class="slide-pop">
            <slot></slot>
        </div>
    </transition>
</template>

<script>
    //import  from '';
    export default {
        props: {
            visible:Boolean
        },
        data() {
            return {};
        },
        methods: {
            handleEnter(){
                this.$emit('show')
            }
        },
        computed: {},
        components: {}
    }
</script>

<style scoped lang="scss">
    @import "../../src/assets/css/defines.scss";
    .slide-pop {
        position: absolute;
        top: 0;
        right: 0;
        z-index: 999;
        box-shadow: 0 3px 10px 3px rgba(0, 0, 0, 0.12);
        border: 1px solid $gray-border-color;
    }
</style>
