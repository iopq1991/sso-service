<template>
    <div class="float-contact">
        <transition name="slide-fade">
            <div v-show="showInfo" class="ct-info bordered" @click.stop>
                <header><strong>联系我们</strong> <span>（工作日9:00-19:00）</span></header>

                <section class="lay-tbl">
                    <i class="fi-consulting-telephone"></i>
                    <div class="lay-cell">咨询电话<br/> 400-820-8969</div>
                </section>

                <section class="lay-tbl">
                    <i class="fi-consulting-qq"></i>
                    <div class="lay-cell">咨询QQ<br/>
                        <a :href="'tencent://message/?uin='+qqNumber+'&Site=&Menu=yes'"
                           class="main-color">{{qqNumber}}</a>
                    </div>
                </section>

                <section class="lay-tbl">
                    <i class="fi-focus-on-wechat"></i>
                    <div class="lay-cell">搜索关注“e兆社区”<br/> <img class="wc-img" src="../../../assets/img/ez-code.jpg">
                    </div>
                </section>
            </div>
        </transition>
        <div class="v-top" @click.stop="showInfo=!showInfo">
            <div class="lg-phone t-center pointer">
                <a class="fi-contact-us"></a>
                <div>联</div>
                <div>系</div>
                <div>我</div>
                <div>们</div>
            </div>
        </div>
    </div>
</template>

<script>
    const QQ_NUMBER = 4000806909;
    //import  from '';
    export default {
        props: {},
        data () {
            return {
                qqNumber: QQ_NUMBER,    //QQ号码
                showInfo: false         //显示信息
            };
        },
        created () {
            //触发影藏
            this.eventBus.$on('body-click', this.hideInfo)
        },
//        destroyed(){
//            this.eventBus.$off('body-click', this.hideInfo)
//        },
        computed: {},
        components: {},
        methods: {
            hideInfo () {
                this.showInfo = false;
            }
        }
    }
</script>

<style scoped lang="scss">
    @import "../../../assets/css/defines.scss";

    $float-width: .46rem;

    .float-contact {
        position: fixed;
        right: .2rem;
        bottom: .7rem;
        z-index: 999;

        > .ct-info {
            position: absolute;
            width: 3rem;
            bottom: 0;
            right: $float-width + .1rem;
            background: #fff;
            padding: .2rem;
            box-shadow: .01rem .01rem .08rem rgba(0, 0, 0, 0.4);

            img {
                width: 1rem;
            }
            header {
                font-size: .16rem;
                span {
                    font-size: .12rem;
                    color: #888;
                }
            }
            > section {
                line-height: .2rem;

                > i {
                    display: inline-block;
                    margin: .2rem .2rem .2rem 0;
                    font-size: .3rem;
                 }

                .main-color {
                    color: $color-primary;
                    font-weight: bold;
                }
                .wc-img {
                    height: 1rem;
                }
            }

        }

        .lg-phone {
            width: $float-width;
            background: $color-primary;
            color: #fff;
            padding: .1rem 0;
            line-height: .16rem;

            > [class^=fi-] {
                font-size: .3rem;
            }
        }
    }
    .float-contact > .ct-info > section:last-child > i{margin-top: .05rem;}
</style>
