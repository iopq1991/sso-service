<template>
	<div class="bind-container">
		<div class='icon'>
		</div>
		<p class="title">授权成功！请绑定平台账号完成登录</p>
		<p id="tip" style="color:red;text-align: center;margin-bottom:30px">{{msg}}</p>
		<form method="post" class="form">
			<div class="item">
				<label>手机号</label>
				<div>
					<input type="text" name="personalPhoneNo" placeholder="请输入平台手机号" v-model="personalPhoneNo" autocomplete="off"/>
				</div>
			</div>
			<div class="item">
				<label>密码</label>
				<div>
					<input type="password" name="password" placeholder="请输入平台密码" v-model="password" autocomplete="new-password"/>
				</div>
			</div>
			<div>
				<input type="button" value="提交" class="submit" @click.prevent="submitForm()" id="submit"/>
			</div>
			<p class="forget">
				<a @click="goToForgetPass">忘记密码</a>
			</p>
		</form>
	</div>
</template>

<script>
	export default {
		name: "",
		data(){
			return{
				params:null,
				personalPhoneNo:'',
				password:'',
				msg:''
			}
		},
		created(){
			this.params=this.queryString();
		},
		methods:{
			submitForm(){
				const {personalPhoneNo,password}=this;
				if(!personalPhoneNo){
					this.msg='手机号不能为空';
					return false;
				}else{
					if(!/^1[3|4|5|7|8|9]\d{9}$/.test(personalPhoneNo)){
						this.msg='手机号码不合法';
						return false;
					}
				}
				if(!password){
					this.msg='密码不能为空';
					return false;
				}else{
					this.msg="";
				}
				const {sign,type,appId,spId,redirectUri}=this.params;
				this.$x.post('/third-part/user/bind',{
					sign:sign,
					type:type,
					appId:appId,
					spId:spId,
					redirectUri:redirectUri,
					personalPhoneNo:this.personalPhoneNo,
					password:this.password
				},{noToken: true, maskOptions: false}).then((res)=>{
					if(res.returnCode!="200"){
						return Promise.reject(res);
					}
					const data=res.data;
					const enterpriseVOList=data.enterpriseVOList;
					if(enterpriseVOList.length>1){
						location=`/dist/login.html?token=${data.token}&userId=${data.userId}&identifyType=${data.identifyType}&identifier=${data.identifier}&appId=${appId}&redirectUri=${redirectUri}#bindEnter`;
					}else{
						let config = {
							noToken: true,
							showError: true,
							maskOptions: false
						};
						let pars = {token: data.token, spId: enterpriseVOList[0].spId, identifyType: data.identifyType,identifier:data.identifier};
						this.$x.post(this.$x.config.API_SERVER_SSO + '/third-part/sp/choice', pars, config)
							.then((res) => {
								if(res.returnCode==200){
									this.going = true;
									window.location.href=`${redirectUri.indexOf('?')>-1?redirectUri+'&':redirectUri+'?'}token=${data.token}`;
								}else{
									throw res;
								}
							}).catch(res=>{
							this.msg = res.msg||'服务器异常，请稍后再试';
						})
					}

				}).catch((res)=>{
					this.msg=res.msg;
				});
			},
			goToForgetPass(){
				location.hash = '#forgetpwd';
				this.$x.storageUtil.setObj('from_tab', 1);
				this.$emit('goToForgetPass','forgetMdnPwd');
			},
			queryString () {
				var cookie = document.cookie.trim();
				var arr = cookie.split(";");
				var obj = {};
				for (var i in arr) {
					var item = arr[i].trim().split("=");
					obj[item[0]] = arr[i].trim().substring(item[0].length + 1, arr[i].trim().length);
				}
				return obj;
			}
		}
	}
</script>

<style scoped lang="scss">
	input:-webkit-autofill, textarea:-webkit-autofill, select:-webkit-autofill {
		background-color:#fff;
		background-image: none;
	}
	.icon {
		margin: 60px auto 10px;
		width: 140px;
		height: 132px;
		background-size: 100% 100%;
		background-image: url('data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAASIAAAEICAIAAAB0626xAAAABGdBTUEAAK/INwWK6QAAABl0RVh0U29mdHdhcmUAQWRvYmUgSW1hZ2VSZWFkeXHJZTwAAArhaVRYdFhNTDpjb20uYWRvYmUueG1wAAAAAAA8P3hwYWNrZXQgYmVnaW49J++7vycgaWQ9J1c1TTBNcENlaGlIenJlU3pOVGN6a2M5ZCc/Pgo8P2Fkb2JlLXhhcC1maWx0ZXJzIGVzYz0iTEYiPz4KPHg6eG1wbWV0YSB4bWxuczp4PSdhZG9iZTpuczptZXRhLycgeDp4bXB0az0nWE1QIHRvb2xraXQgMy4wLTI4LCBmcmFtZXdvcmsgMS42Jz4KPHJkZjpSREYgeG1sbnM6cmRmPSdodHRwOi8vd3d3LnczLm9yZy8xOTk5LzAyLzIyLXJkZi1zeW50YXgtbnMjJyB4bWxuczppWD0naHR0cDovL25zLmFkb2JlLmNvbS9pWC8xLjAvJz4KCiA8cmRmOkRlc2NyaXB0aW9uIHJkZjphYm91dD0ndXVpZDo4NTkzNDBiOC03MmFmLTExZTctYTYyZC04NjcwMjM0ODQ4ODMnCiAgeG1sbnM6ZXhpZj0naHR0cDovL25zLmFkb2JlLmNvbS9leGlmLzEuMC8nPgogIDxleGlmOlBpeGVsWERpbWVuc2lvbj4yOTA8L2V4aWY6UGl4ZWxYRGltZW5zaW9uPgogIDxleGlmOlBpeGVsWURpbWVuc2lvbj4yNzQ8L2V4aWY6UGl4ZWxZRGltZW5zaW9uPgogPC9yZGY6RGVzY3JpcHRpb24+CgogPHJkZjpEZXNjcmlwdGlvbiByZGY6YWJvdXQ9J3V1aWQ6ODU5MzQwYjgtNzJhZi0xMWU3LWE2MmQtODY3MDIzNDg0ODgzJwogIHhtbG5zOnBkZj0naHR0cDovL25zLmFkb2JlLmNvbS9wZGYvMS4zLyc+CiA8L3JkZjpEZXNjcmlwdGlvbj4KCiA8cmRmOkRlc2NyaXB0aW9uIHJkZjphYm91dD0ndXVpZDo4NTkzNDBiOC03MmFmLTExZTctYTYyZC04NjcwMjM0ODQ4ODMnCiAgeG1sbnM6cGhvdG9zaG9wPSdodHRwOi8vbnMuYWRvYmUuY29tL3Bob3Rvc2hvcC8xLjAvJz4KIDwvcmRmOkRlc2NyaXB0aW9uPgoKIDxyZGY6RGVzY3JpcHRpb24gcmRmOmFib3V0PSd1dWlkOjg1OTM0MGI4LTcyYWYtMTFlNy1hNjJkLTg2NzAyMzQ4NDg4MycKICB4bWxuczp0aWZmPSdodHRwOi8vbnMuYWRvYmUuY29tL3RpZmYvMS4wLyc+CiAgPHRpZmY6WFJlc29sdXRpb24+NzIvMTwvdGlmZjpYUmVzb2x1dGlvbj4KICA8dGlmZjpZUmVzb2x1dGlvbj43Mi8xPC90aWZmOllSZXNvbHV0aW9uPgogIDx0aWZmOlJlc29sdXRpb25Vbml0PjI8L3RpZmY6UmVzb2x1dGlvblVuaXQ+CiA8L3JkZjpEZXNjcmlwdGlvbj4KCiA8cmRmOkRlc2NyaXB0aW9uIHJkZjphYm91dD0ndXVpZDo4NTkzNDBiOC03MmFmLTExZTctYTYyZC04NjcwMjM0ODQ4ODMnCiAgeG1sbnM6eGFwPSdodHRwOi8vbnMuYWRvYmUuY29tL3hhcC8xLjAvJz4KICA8eGFwOkNyZWF0b3JUb29sPkFkb2JlIFBob3Rvc2hvcCBDQyAyMDE1IChXaW5kb3dzKTwveGFwOkNyZWF0b3JUb29sPgogIDx4YXA6Q3JlYXRlRGF0ZT4yMDE3LTA3LTI3VDA5OjQzOjI4WjwveGFwOkNyZWF0ZURhdGU+CiAgPHhhcDpNZXRhZGF0YURhdGU+MjAxNy0wNy0yN1QwOTo0MzoyOFo8L3hhcDpNZXRhZGF0YURhdGU+CiA8L3JkZjpEZXNjcmlwdGlvbj4KCiA8cmRmOkRlc2NyaXB0aW9uIHJkZjphYm91dD0ndXVpZDo4NTkzNDBiOC03MmFmLTExZTctYTYyZC04NjcwMjM0ODQ4ODMnCiAgeG1sbnM6c3RSZWY9J2h0dHA6Ly9ucy5hZG9iZS5jb20veGFwLzEuMC9zVHlwZS9SZXNvdXJjZVJlZiMnCiAgeG1sbnM6eGFwTU09J2h0dHA6Ly9ucy5hZG9iZS5jb20veGFwLzEuMC9tbS8nPgogIDx4YXBNTTpPcmlnaW5hbERvY3VtZW50SUQ+eG1wLmRpZDpFOTM3NERDQTlCNUZFNzExOTQxREVERjkyMTlEM0JCQTwveGFwTU06T3JpZ2luYWxEb2N1bWVudElEPgogIDx4YXBNTTpEb2N1bWVudElEPmFkb2JlOmRvY2lkOmltYWdlcmVhZHk6ODU5MzQwYjktNzJhZi0xMWU3LWE2MmQtODY3MDIzNDg0ODgzPC94YXBNTTpEb2N1bWVudElEPgogIDx4YXBNTTpJbnN0YW5jZUlEPnhtcC5paWQ6NTk2MUY1OUQ3MjlCMTFFN0JCNkJDNDNGQzY3QzcwNkM8L3hhcE1NOkluc3RhbmNlSUQ+CiAgPHhhcE1NOkRlcml2ZWRGcm9tIHJkZjpwYXJzZVR5cGU9J1Jlc291cmNlJz4KICAgPHN0UmVmOmluc3RhbmNlSUQ+dXVpZDo4NTkzNDBiNS03MmFmLTExZTctYTYyZC04NjcwMjM0ODQ4ODM8L3N0UmVmOmluc3RhbmNlSUQ+CiAgIDxzdFJlZjpkb2N1bWVudElEPnhtcC5kaWQ6NTk2MUY1OUU3MjlCMTFFN0JCNkJDNDNGQzY3QzcwNkM8L3N0UmVmOmRvY3VtZW50SUQ+CiAgPC94YXBNTTpEZXJpdmVkRnJvbT4KICA8eGFwTU06VmVyc2lvbklEPjE8L3hhcE1NOlZlcnNpb25JRD4KIDwvcmRmOkRlc2NyaXB0aW9uPgoKIDxyZGY6RGVzY3JpcHRpb24gcmRmOmFib3V0PSd1dWlkOjg1OTM0MGI4LTcyYWYtMTFlNy1hNjJkLTg2NzAyMzQ4NDg4MycKICB4bWxuczp4YXBSaWdodHM9J2h0dHA6Ly9ucy5hZG9iZS5jb20veGFwLzEuMC9yaWdodHMvJz4KIDwvcmRmOkRlc2NyaXB0aW9uPgoKIDxyZGY6RGVzY3JpcHRpb24gcmRmOmFib3V0PSd1dWlkOjg1OTM0MGI4LTcyYWYtMTFlNy1hNjJkLTg2NzAyMzQ4NDg4MycKICB4bWxuczpkYz0naHR0cDovL3B1cmwub3JnL2RjL2VsZW1lbnRzLzEuMS8nPgogIDxkYzpkZXNjcmlwdGlvbj4KICAgPHJkZjpBbHQ+CiAgICA8cmRmOmxpIHhtbDpsYW5nPSd4LWRlZmF1bHQnLz4KICAgPC9yZGY6QWx0PgogIDwvZGM6ZGVzY3JpcHRpb24+CiAgPGRjOnJpZ2h0cz4KICAgPHJkZjpBbHQ+CiAgICA8cmRmOmxpIHhtbDpsYW5nPSd4LWRlZmF1bHQnLz4KICAg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');
	}

	.title {
		margin: 0px 0 20px 0;
		text-align: center;
	}

	.form > div {
		display: -webkit-box;
		display: -webkit-flex;
		display: -ms-flexbox;
		display: flex;
		width: 85%;
		-webkit-align-items: center;
		align-items: center;
		margin: 0 auto 10px;
	}

	.form > div > label {
		flex: 2;
		text-align: right;
		padding-right: 20px;
	}

	.form > div > div {
		flex: 8;
	}

	.form > div > div > input {
		height: 40px;
		width: 95%;
		outline: none;
		box-shadow: none;
		border: none;
		padding-left: 2px;
		background-color: #f2f2f2;
	}

	.submit {
		display: block;
		height: 40px;
		width: 100%;
		background-color: #06bf04;
		outline: none;
		margin: 10px auto;
		color: #fff;
		border-radius: 8px;
		border: 1px solid #E6E6E6;
		cursor:pointer;
	}

	.item {
		background-color: #f2f2f2;
		border-radius: 6px;
	}

	h4 {
		position: absolute;
		left: 50%;
		top: 50%;
		transform: translate(-50%, -50%);
		color: #fff;
		font-weight: normal;
		font-size: 18px;
	}

	.forget {
		text-align: right;
		width: 85%;
		margin: 0 auto;
	}

	.forget a {
		text-decoration: none;
		color: #4595CE;
	}
</style>
