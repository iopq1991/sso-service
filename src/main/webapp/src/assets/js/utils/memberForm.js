/*****************************************************************
 ** Copyright (c) 上海帜讯（flaginfo）南京分公司研发部
 ** 创建人:      hc
 ** 创建日期:    2017/10/23
 ** 修改人:
 ** 修改日期:
 ** 描 述:
 **-----------------------------------------------------------------
 ******************************************************************/

import $$x from '../../js/$x.js'

const defaultUrls={
	UploadUrl:$$x.http.defaults.baseURL+'/file/upload'
}

export default {

	data(){
		var validateMobile = (rule, value, callback) => {
			var reg=/^[1][3,4,5,7,8][0-9]{9}$/;
			if (!reg.test(value)) {
				callback(new Error('手机号码不正确,请重新输入'))
			} else {
				callback()
			}
		};

		var validateOrgs=(rule, value, callback) => {
			if(value!=null&&value.length==0){
				callback(new Error('请选择部门'))
			}
			else{
				callback()
			}
		};

		var validateEmail= (rule, value, callback) => {
			var reg = /^([a-zA-Z0-9]+[_|\_|\.]?)*[a-zA-Z0-9]+@([a-zA-Z0-9]+[_|\_|\.]?)*[a-zA-Z0-9]+\.[a-zA-Z]{2,3}$/;
			if (!reg.test(value)) {
				callback(new Error('邮箱地址不正确,请重新输入'))
			} else {
				callback()
			}
		}

		return {
			dialogVisible: false,
			popoverVisible:false,
			form: {
				employeeNo: "",
				name: "",
				sex: 1,
				avatar: "",
				birthday: "",
				mobile: "",
				telephone:"",
				email: "",
				position:"",
				orgIdList:[],
				attributes:[],
				passHid:1
			},
			editable:false,
		    isAdmin:false,
			memberID:"",
			dialogSlider:false,
			formAttributes:[],
			isUpload:false,
			headerName: "",
			isHasAdd:false,
			rules: {
				name: [
					{required: true, message: '请输入姓名', trigger: 'blur'}
				],
				mobile: [
					{required: true, message: '请输入手机号码', trigger: 'blur'},
					{validator: validateMobile, trigger: 'blur'}
				],
				orgIdList: [
					{type:'array',required: true, message: '请选择部门', trigger: 'blur'},
					{validator: validateOrgs, trigger: 'blur'}
				],
				email:[
					{required: true, message: '请输入邮箱地址', trigger: 'blur'},
					{validator: validateEmail, trigger: 'blur'}
				]
			},
			maxlength: 11,
			defaultUrls: defaultUrls
		}
	},
	handleMobile(val){
       return val.replace(/(\d{3})\d{4}(\d{4})/, '$1****$2')
	}
}
