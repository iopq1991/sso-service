<template>
	<div class="choose-corp enter">
		<div v-if="ajaxMsg" class="err-ajax">{{ajaxMsg}}</div>
		<el-carousel class="carousel" :interval="0" type="card" height="100px" arrow="always" trigger="click"
					 :initial-index="0" @change="handleCorpChange">
			<el-carousel-item v-for="(item,index) in enterprises" :key="index" label=" ">
				<img v-if="item.logo" :src="item.logo"/>
				<img v-if="!item.logo" src="../../assets/img/logo_default.png"/>
			</el-carousel-item>
		</el-carousel>
		<div class="full t-center corp-name" style="margin-top:10px;">{{ selectedCorp&&selectedCorp.spName||'' }}</div>
		<div slot="footer" class="t-center">
			<button @click="goHome" class="submit" :loading="going">
				进 入
			</button>
		</div>
	</div>
</template>

<script>
	export default {
		name: "enter",
		data() {
			return {
				enterprises: [],
				selectedCorp: {},
				going: false,
				selectedSpCode: '',
				data: {},
				ajaxMsg:''
			}
		},
		created() {
			this.data = this.GetRequest();
			console.log(this.data);
			this.$x.get(this.$x.config.API_SERVER_SSO + '/third-part/enterprise/list', {userId:Number(this.data.userId),appId:this.data.appId}
				, {noToken: true, maskOptions: false}).then((res) => {
				if (res.data) {
					if (res.data.length > 1) {
						this.enterprises = res.data;
					}
					else {
						this.selectedCorp = res.data[0];
						this.goHome();
					}
				}
				else {
					throw res;
				}
			}).catch((res)=>{
				this.ajaxMsg = res.msg||'服务器异常，请稍后再试';
			})
		},
		methods: {
			goHome() {
				this.choseSp();
			},
			handleCorpChange(index) {
				this.selectedCorp = this.enterprises[index];
			},
			choseSp() {
				let pars = {token: this.data.token, spId: this.selectedCorp.spId,  identifyType: this.data.identifyType,identifier:this.data.identifier};
				let config = {
					noToken: true,
					showError: true,
					maskOptions: false
				};
				this.$x.post(this.$x.config.API_SERVER_SSO + '/third-part/sp/choice', pars, config)
					.then((res) => {
						if(res.returnCode==200){
							this.going = true;
							window.location.href=`${this.data.redirectUri.indexOf('?')>-1?this.data.redirectUri+'&':this.data.redirectUri+'?'}token=${this.data.token}`;
						}else{
							throw res;
						}
					}).catch(res=>{
					this.ajaxMsg = res.msg||'服务器异常，请稍后再试';
				})
			},
			GetRequest() {
				const url = location.search; //获取url中"?"符后的字串
				const theRequest = {};
				if (url.indexOf("?") != -1) {
					const str = url.substr(1);
					const strs = str.split("&");
					for (var i = 0; i < strs.length; i++) {
						if(strs[i].split("=")[0]==='redirectUri'){
							if(strs[i].split("=")[1].indexOf('?'>-1)){
								const arr=strs[i].split("=");
								let result="";
								for(var j=0;j<arr.length;j++){
									if(j!==0){
										result=result+arr[j]+"=";
									}
								}
								result=/=$/g.test(result)?result.slice(0,result.length-1):result;
								theRequest[strs[i].split("=")[0]]=result;
							}else{
								theRequest[strs[i].split("=")[0]] = (strs[i].split("=")[1]);
							}
						}else{
							theRequest[strs[i].split("=")[0]] = (strs[i].split("=")[1]);
						}

					}
				}
				return theRequest;
			}

		}
	}
</script>

<style>

</style>


<style lang='scss'>
	@import "../../assets/css/defines.scss";
	.enter {
		margin-top: 180px;

	.submit {
		display: block;
		height: 40px;
		width: 85%;
		background-color: #06bf04;
		outline: none;
		margin: 30px auto;
		color: #fff;
		border-radius: 8px;
		border: 1px solid #E6E6E6;
		cursor: pointer;
	}

	img {
		height: 100px;
		width: 100px;
	}

	.el-carousel__item--card {
		text-align: center;
	}

	.el-carousel__arrow {
		width: 28px;
		height: 28px;
	}

	.el-carousel__button {
		padding: 2px 12px;
	}

	.el-carousel__indicators {
		margin-top: 20px;
	}
	.err-ajax {
		text-align:center;
		color: #f33;
		animation: shake 1s ease-out forwards;
	}
	}
</style>
