<template>
    <div class="lg-footer">
        <div class="lg-police t-center">
            <a target="_blank" href="http://www.beian.gov.cn/portal/registerSystemInfo?recordcode=31011502002252">
                <img src="http://www.beian.gov.cn/img/ghs.png"/>
                <span>沪公网安备 31011502002252号</span>&nbsp; &nbsp;&nbsp; &nbsp;
                <span>沪ICP备10032707号</span>
            </a>
        </div>
    </div>
</template>

<script>
    export default {
        components: {},
        props: {},
        methods: {}
    }
</script>

<style scoped lang="scss">
    @import "../../../assets/css/defines.scss";

    .lg-contact {
        font-size: .12rem;
        margin: .3rem auto;

        h6 {
            margin: .04rem 0;
            color: $color-primary;
        }
        > section {
            width: 2.5rem;
            padding-left: .4rem;
            height: .8rem;
        }

    }

    /*
    .lg-footer {
        &:before {
            content: '';
            display: block;
            height: 1.5rem;
        }
    }*/

    .lg-police {
        //position: fixed;
        //bottom: 0;
        padding: .5rem 0 .8rem;
        width: 100%;

        a {
            font-size: .12rem;
            color: #939393;

            img {
                border: 0;
                vertical-align: middle;
            }
        }
    }
</style>
