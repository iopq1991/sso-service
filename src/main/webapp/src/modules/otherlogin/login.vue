<template>
	<div class="login-main" @click="eventBus.$emit('body-click')">
		<div v-if="!pageStatus" class="cnt-wraper">
			<div class="login-panel b-center inlnb lay-tbl">

				<div class="login-box lay-cell">
					<div>
						<div>
							<el-tabs :class="showCorp ? 'hide-tab':''" v-model="spActive">
								<el-tab-pane name="corp">
									<span slot="label" class="left-tab"><a>企业编号登录</a></span>
									<login-cont @lgForgetPwd="goForgetPwd"></login-cont>
								</el-tab-pane>
								<el-tab-pane name="mdn">
									<span slot="label"><a>手机登录</a></span>
									<login-cont-quick :show-corp.sync="showCorp"
													  @lgForgetPwd="goForgetPwd"></login-cont-quick>
								</el-tab-pane>
							</el-tabs>
						</div>

					</div>
				</div>
			</div>
		</div>
		<bind-account v-if="pageStatus=='bindAccount'" @goToForgetPass="goForgetPwd"></bind-account>
		<forgot-pass-sp v-if="pageStatus=='forgetSpPwd'" @lgForgetPwdDone="backLogin"></forgot-pass-sp>
		<forgot-pass-mdn v-if="pageStatus=='forgetMdnPwd'" @lgForgetPwdDone="backLogin"></forgot-pass-mdn>
		<bind-enter v-if="pageStatus=='bindEnter'"></bind-enter>
	</div>
</template>


<script>
	import Vue from 'vue';
	import $x from '@/assets/js/$x';
	import {Input, Checkbox, Button, Carousel, CarouselItem, Tabs, TabPane} from 'element-ui';
	Vue.use(Input).use(Checkbox).use(Button).use(Carousel).use(CarouselItem).use(Tabs).use(TabPane);

	Vue.prototype.$x = $x;
	const comps = {
		'login-cont': require('../login/children/login-cont-sp.vue'),
		'login-cont-quick': require('../login/children/login-cont-mdn.vue'),
		'header-nav': require('../login/children/header.nav.vue'),
		'forgot-pass-sp': require('../login/children/forgot-pass-sp.vue'),
		'forgot-pass-mdn': require('../login/children/forgot-pass-mdn.vue'),
		'bind-account':require('../bind/bind.vue'),
		'bind-enter':require('./enter')
	}
	export default {
		name:"login",
		data(){
			return{
				pageStatus: '', //页面状态，空值为登录界面，forgetSpPwd|forgetMdnPwd为忘记密码界面
				loginCfg: {}, //登录页的配置，包括轮播图

				showCorp: false //是否切换到选择企业
			}
		},
		components:comps,
		created(){
			if (location.hash === '#forgetpwd') {
				this.goForgetPwd('forgetMdnPwd') //forgetSpPwd
			}else if(location.hash === '#bindAccount'){
				this.goForgetPwd('bindAccount');
			}else if(location.hash==='#bindEnter'){
				this.goForgetPwd('bindEnter');
			}
		},
		computed:{
			spActive: {
				get () {
					return this.$x.storageUtil.getObj('login_tab') || 'mdn';
				},
				set (v) {
					this.$x.storageUtil.setObj('login_tab', v)
				}
			},
		},
		methods:{
			//跳到忘记密码
			goForgetPwd(gotoPage) {
				this.pageStatus = gotoPage
			},
			//由忘记密码返回登陆
			backLogin() {
				const tab=this.$x.storageUtil.getObj('from_tab');
				if(tab===0){
					location.hash = ''
					this.pageStatus = ''
				}else{
					location.hash = '#bindAccount'
					this.pageStatus = 'bindAccount'
				}

			},
		}
	}
</script>
<style lang="scss" scoped>
	@import "../../assets/css/defines.scss";
	.login-panel {
		max-width: 12rem;
		height: 4.0rem;

		.login-box {
			height: 100%;
			padding: 0 .2rem;

			> div {
				height: 4.7rem;
				width: 4rem;
				background: #fff;
				box-shadow: 0px 2px .13rem 1px rgba(0, 0, 0, 0.12);
			}
		}
	}

	.cnt-wraper {
		padding-top: 1.2rem;
	}

	.lg-tab-box {
		padding: 0 .4rem;
	}

	.left-tab:after {
		content: '';
		display: inline-block;
		height: 20px;
		border-right: 1px solid #eee;
		position: absolute;
		right: 0;
		top: 12px;
	}

	//移动端布局
	@media (max-width: 768px) {
		.login-panel {
			width:100%;
			.login-box {
				padding: 0;
				> div {
					height: 4.8rem;
					width:100%;
				}
			}
		}
		.cnt-wraper {
			padding-top: 1.2rem;
		}
	}
</style>
<style  lang="scss">
	@import "../../assets/css/defines.scss";
	body{
		background:#fff !important;
	}
	@media(max-width:768px){
		.el-message-box{
			width:auto !important;
		}
	}
	//覆盖element样式，单位用px，防止错位
	.login-main {
		.el-tabs__nav {
			width: 100%;
		}
		.el-tabs__header {
			line-height: 60px;

			.el-tabs__item {
				width: 50%;
				text-align: center;
				font-size: 16px;
				font-family: "Microsoft YaHei";

			}
		}
		.el-tabs__item.is-active {
			font-weight: bold;
		}
		.hide-tab .el-tabs__header {
			display: none;
		}
		//tab页的底部
		.el-tabs__active-bar {
			display: none;
		}

		.el-button--large {
			height: 46px;
		}

		//输入框图标
		.el-input-group__prepend {
			padding: .10px 12px;
		}



		//输入框
		.login-box {
			.el-input__inner {
				background: #f5f5f5 !important;
				&:focus {
					background: #fff !important;
				}
			}
		}


		.el-tabs__header {
			margin-bottom: 0;
		}
	}

</style>
