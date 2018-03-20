import CONST from './CONST.js';

export default {
	DEFAULT_MSG: {
		ADDMSG: "保存成功",
		ERRORMSG: "保存失败",
		ISDETELEMSG: "是否删除",
		TIPMSG: "提示",
		OKMSG: '确定',
		CANCELMSG: '取消',
		DELETEMSG: '删除',
		DELETESUCCESSMSG: '删除成功',
		DELETEDEPSUCCESSMSG: '部门删除成功',
		DELETEDEPERRORMSG: '部门删除失败',
		DELETEERRORMSG: '删除失败',
		ISENTDETELEMSG: '请先删除部门及以下所有成员后再删除该公司',
		ISDEPDETELEMSG: '请先删除部门下所有成员后再删除该部门',
		MOREMSG: "更多",
		STOPMSG: "收起",
		ADJUSETMENTMSG: '调整部门操作成功',
		NOSELECTMEMBERMSG: '未选择成员',
		MEMBERADDMSG: '成员添加成功',
		MEMBEREDITMSG: '成员修改成功',
		UPLOADERRORMSG: '上传文件错误',
		EXPORTSUCCESSMSG: '导出成功',
		TOPSUCCESSMSG: '置顶操作成功',
		TOPERRORMSG: '置顶操作失败',
		SORTSUCCESSMSG: '排序操作成功',
		SORTERRORMSG: '排序操作失败',
		PHONEERRORMSG: '该手机号码已存在',
		ACCOUNTERRORMSG:'账号已存在',
		NAMEERRORMSG: '姓名已存在',
		USERCENTERRORMSG:'次手机号码已存在企业通讯录中，无法添加',
		ORGNAMEERRORMSG: '请输入部门名称',
		NOSELECTDEPMSG:'未选中任何部门'
	},
	FORM_MSG: {
		ADDDEPARTEMT: '添加部门',
		EDITDEPARTEMT: '修改部门',
		DEPARTEMTNAME: '部门名称',
		MEMBERMSG: '成员信息',
		SLELETDEPMSG: '选择部门',
		SELETMEMBERMSG: '选择成员',
		SELETDEPEPORMEMBER: '选择部门、成员',
		SELECTEDDEPORMEMBER: '已选择部门、成员',
		SELECTEDDEP: '已选择部门',
		SELECTEDMEMBER: '已选择成员'
	},
	OPERATE_MSG: {
		ADDCHILDDEPARTEMT: '添加子部门',
		DELETEMSG: '删除',
		EDITDEPARTEMTNAME: '修改名称',
		MOVEMSG: '上移',
		DOWNMSG: '下移',
		ADDMEMBER: '添加成员',
		EXPORTORIMPORTMSG: '批量导入/导出',
		ADJUSETMENTMSG: '调整部门',
		STICKMSG: '置顶',
		SORTMSG: '排序',
		SHIFOUTMSG: '移除',
		COMPLETEDELTEMSG: '彻底删除',
		COMPLETEMSG: '完成'
	},
	VALIDATE: {
		NO_VALICODE: '请输入验证码',
		ERR_VALICODE: '验证码有误，请输入' + CONST.SMS_VALI_CODE_LENGTH + '位短信验证码',
		NO_MDN: '请输入手机号码',
		ERR_MDN: '手机号码输入有误',
		UPLOADTYPE: '上传图片只能是 {format} 格式!',
		UPLOADSIZE: '上传图片大小不能超过 {sizeM}MB',
		EMAILERRORMSG: '邮箱地址格式有误，请重新输入'
	},
	APP_MSG: {
		VISIBLE_SCOPE_ALL: '全部人员',
		VISIBLE_SCOPE_PART: '部分人员',
		VISIBLE_SCOPE_ADMIN: '仅管理员',
		// PROMPT: function (visualRange = '全部人员', appName = '') {
		// 	return '你当前应用名:"' + appName +
		// 		'",可见范围设置为' + visualRange +
		// 		',将对(' + visualRange +
		// 		')可见,请确认';
		// },
		REUSEMSG: '启用成功',
		STOPMSG: '停用成功'
	}
}
