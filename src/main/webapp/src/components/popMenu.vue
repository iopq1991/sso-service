<template>
    <div>
        <el-popover ref="popover1" popper-class="head-pop-menu" trigger="click" v-model="showPop">
            <div v-for="(item,index) in items || []" :key="index" @click="handleItemClick(item)"
                 class="el-dropdown-menu__item"
                 :class="index==curIndex?'actived':''">
                <slot name="pop-item" :item="item"></slot>
            </div>
        </el-popover>
        <span v-popover:popover1 class="mn-label">
			<slot :isShown="showPop"></slot>
		</span>
    </div>
</template>

<script>
    //弹出菜单，带指示三角
    import Vue from 'vue';
    import {Popover} from "element-ui";

    Vue.use(Popover);

    export default {
        props: {
            items: Array,       //菜单项
            curIndex: Number    //当前选中项的索引
        },
        data() {
            return {showPop: false}
        },
        methods: {
            handleItemClick(item) {
                this.showPop = false;
                this.$emit('item-click', item)
            }
        }
    }
</script>

<style scoped lang="scss">
    @import "../assets/css/defines";

    /*.el-dropdown-menu__item {
        border-left: .03rem solid transparent;
        &.actived {
            border-color: $color-primary;
        }
    }*/

    .mn-label:hover {
        color: $color-primary;
    }
</style>
<style lang="scss">
    @import "../assets/css/defines";

    .head-pop-menu {
        padding: .05rem 0;
        border: 0;
        min-width: 1.8rem;
        color: #333;

        .el-dropdown-menu__item:not(.is-disabled):hover {
            color: #fff;
            background: $color-primary;
        }
    }

    .el-button-group .el-button {
        border-radius: 0 !important;
    }

</style>
