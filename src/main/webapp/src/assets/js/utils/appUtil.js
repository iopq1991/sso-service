import $x from '@/assets/js/$x';

var ajaxConfig = {
	maskOptions: {
		target: '.role-manage-box'
	}
};
var showError = {
	showError: true
};
const defaultUrls = {
	getOrgInitTree: "/Organize/Root",
	getOrgChild: "/Organize/Privilege/Child",
	getAppList: "/User/App/List",
	getAppDetail: "/User/App/Detail",
	SAVE_URL: "/User/App/Create",
	UPDATE_URL: "User/App/Update",
	getSearchkey: "/Organize/Privilege/GetOrgOrMemberByName"
};
export default {
	/**
	 * @name 渲染根节点
	 * @param obj 当前组件下作用域 [Object] [必填]
	 * @param selectedData  右侧选中列表 [Array] [非必填]
	 */
	renderRootNode:function(obj,selectedData=[]){
		$x.get(defaultUrls.getOrgInitTree)
		.then(res => {
			for(var i=0;i<res.data.length;i++){
				let temp=res.data[i];
				for(var j=0;j<selectedData.length;j++){
					if(selectedData[j].id==temp.id){
						temp.checked=true;
					}
				}
			}
			obj.$refs['dialogMemberLeft'].treeData = res.data;
		})
	},
	/**
	 * @name 子节点渲染(含权限限制)
	 * @param node: 当前父节点 [Object] [必填]
	 * @param selectedData: 右侧选中列表 [Array] [非必填]
	 * @param isOnlyOrg: 分类（1:仅部门,不传或传其他:部门及成员均呈现）[Number/String] [非必填]
	 */
	renderSubNode: function (node, resolve, selectedData = [], isOnlyOrg = 0) {
		if (node.data.memberFlag || !node.data.canClick) {
			resolve([]);
		}
		$x.post(defaultUrls.getOrgChild, {
			id: node.data.id
		}, ajaxConfig, showError)
			.then(res => {
				if (res.status == 0) {
					let arr = [];
					res.data.memberList = res.data.memberList || [];
					res.data.orgList = res.data.orgList || [];
					[...res.data.orgList].map(item => {
						if(isOnlyOrg==1){
							item.isLeaf= !Boolean(item.hasChild - 0);
						}else{
							item.isLeaf = !(Boolean(item.hasChild - 0) || Boolean(item.hasMember - 0));							
						}
					});
					[...res.data.memberList].map(item => {
						item.memberOperate = res.data.memberOperate;
						item.isLeaf = item.memberFlag;
					});
					for (var i = 0; i < selectedData.length; i++) {
						for (var j = 0; j < res.data.orgList.length; j++) {
							var temp = res.data.orgList[j];
							if (temp.id == selectedData[i].id) {
								temp.checked = true;
							}
						}
						if (isOnlyOrg != 1) {
							for (var j = 0; j < res.data.memberList.length; j++) {
								var temp = res.data.memberList[j];
								if (temp.id == selectedData[i].id) {
									temp.checked = true;
								}
							}
						}

					}
					let obj={},brr=[];
					for(var i=0;i<res.data.memberList.length;i++){//成员去重
						let temp=res.data.memberList[i];
						if(!obj[temp.id]){
							brr.push(temp);
							obj[temp.id]=true;
						}
					}	
					if (isOnlyOrg == 1) {
						arr.push(...res.data.orgList);
					} else {
						arr.push(...brr,...res.data.orgList);

					}
					node.data.childNodes = arr;
					resolve(arr);
				}
			}).catch(res => {
			$x.alert(res.msg);
		})
	},
	/**
	 * @name 成员或部门选择(含权限控制)
	 * @param arr: 右侧选中列表 [Array]
	 * @param node: 当前被点击节点 [Object]
	 */
	treeControl: function (arr = [], node) {
		if (node.data.memberFlag) {
			if (!node.data.memberOperate) {
				node.data.checked = false;
				for (var i = 0; i < arr.length; i++) {
					if (!arr[i].memberOperate && arr[i].memberFlag) {
						arr = arr.splice(i, 1);
					}
				}
			}

		} else {
			if (!node.data.canOperate) {
				node.data.checked = false;
				for (var i = 0; i < arr.length; i++) {
					if (!arr[i].canOperate && !arr[i].memberFlag) {
						arr = arr.splice(i, 1);
					}
				}
			}
		}
		let tag = document.getElementsByClassName('correct' + node.data.id);
		if (node.data.checked) {
			document.getElementById('correct' + node.data.id).style.display = "inline-block"
			for (var i = 0; i < tag.length; i++) {
				tag[i].style.display = "inline-block";
			}
		} else {
			document.getElementById('correct' + node.data.id).style.display = "none"
			for (var i = 0; i < tag.length; i++) {
				tag[i].style.display = "none";
			}
		}
	},
	/**
	 * @name 树内容搜索
	 * @param org: 调用所在作用域下对象 [Object]
	 * @param searchkey: 搜索关键字 [String]
	 * @param isOnlyOrg: 搜索结果分类（1:仅部门，2:仅成员，不传或传其他:部门及成员均呈现）[Number/String]
	 */
	searchData: function (org, url, searchkey, isOnlyOrg = 0) {
		$x.post(url, {
			name: searchkey
		}, ajaxConfig, showError)
			.then(res => {
				if (res.status == 0) {
					if (isOnlyOrg == 1) {
						res.data.memberCount = 0;
					} else if (isOnlyOrg == 2) {
						res.data.orgCount = 0;
					}
					org.queryData = res.data||{};
				} else {
					console.log('请求失败');
				}

			}).catch(res => {
			$x.alert(res.msg);
		})
	},
	/**
	 * @name 搜索选择
	 * @param selectedData: 右侧列表 [Array]
	 * @param item: 选中数据 [Sring]
	 * @param nodeList: 对应选中的所有节点 [Array]
	 */
	searchItemClick: function (selectedData = [], item, nodeList) {
		let flag = true;
		for (let val of selectedData) {
			if (val.id == item.id) {
				flag = false;
				break;
			}
		}
		if (flag) {
			if (nodeList.length>0) {
				for(let i=0;i<nodeList.length;i++){
					nodeList[i].checked = true;
					let tag = document.getElementsByClassName('correct' + nodeList[i].id);
					if (nodeList[i].checked) {
						document.getElementById('correct' +  nodeList[i].id).style.display = "inline-block"
						tag[i].style.display = "inline-block";
					} else {
						document.getElementById('correct' +  nodeList[i].id).style.display = "none"
						tag[i].style.display = "none";
					}
				}
			}
			item.checked = true;
			selectedData.push(item);
		}
	}
}
