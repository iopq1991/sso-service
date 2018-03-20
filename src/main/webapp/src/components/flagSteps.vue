<template>
    <ul class="flag-steps w100 clearfix-child">
        <li v-for="(step,index) in steps" :class="active>index?'hover':''" :style="{width:99/steps.length+'%'}">
            <div v-if="num"><span class="num">{{num}}</span>{{step}}</div>
			<div v-else><span class="num">{{index + 1}}</span>{{step}}</div>
        </li>
    </ul>
</template>
<script>
    //import  from '';
    export default {
        props: {
            //当前是哪一步
            active: {
                type: Number,
                default: 1
            },
            steps: Array ,//每一步的文本，数组
			num:Number
        }
    }

</script>
<style scoped lang="scss">
@import "../assets/css/defines.scss";
    .flag-steps {
        li {
            border-top: 4px dotted #cbcbcb;
            float: left;
            width: 32% !important;
            margin-right: 2px;
            position: relative;
            height: 4px;
            &:last-child{
                margin-right: 0px;
                width:34% !important;
                }
            > div {
                display: block;
                font-size: 16px;
                text-align: center;
                position: relative;
                top: -28px;
                margin: 0 auto;
                color: #666;
                .num {
                    display: block;
                    border-radius: 50%;
                    -webkit-border-radius: 50%;
                    -moz-border-radius: 50%;
                    border: 2px solid #c4ceda;
                    width: 34px;
                    height: 34px;
                    text-align: center;
                    line-height: 34px;
                    margin: 5px auto 0;
                    color: #fff;
                    background: #c4ceda;
                    margin-bottom: 15px;
                }
            }
        }
    }

    .flag-steps li.hover {
        border-top: 4px dotted $color-primary;
    }

    .flag-steps li.hover .num {
        background: #fff;
        border: 2px solid $color-primary;
        color: $color-primary;
    }

    .flag-steps li.hover > div {
        color: $color-primary;
    }

	@media(max-width:768px){
		.lg-forget-step{
			width:100% !important;
			>li{
				width:100% !important;
			}
			>li:last-child{
				width:100% !important;
			}


		}
	}
</style>
