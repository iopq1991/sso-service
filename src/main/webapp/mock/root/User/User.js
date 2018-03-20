module.exports = {
    disabled: 1,
    status: 200,
    body: function (query, post, header, request){
      var data = {
        enterprises:[
          {
            spId: '001',
            spName: '上海帜讯信息技术股份有限公司',
            icon: 'https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1507695032224&di=06d38f6f9ea502c9fdd9a4cd1744feab&imgtype=0&src=http%3A%2F%2Fimgsrc.baidu.com%2Fimage%2Fc0%253Dshijue1%252C0%252C0%252C294%252C40%2Fsign%3D8a78f9da953df8dcb23087d2a57818fe%2Fd000baa1cd11728bbda77b0fc2fcc3cec3fd2c11.jpg'
          },
          {
            spId: '002',
            spName: '北京帜讯',
            icon: 'https://ss1.bdstatic.com/70cFvXSh_Q1YnxGkpoWK1HF6hhy/it/u=4250693722,302659819&fm=27&gp=0.jpg'
          },
          {
            spId: '003',
            spName: '杭州帜讯',
            icon: 'http://res.ums86.com/5/0/static/images/logo_unicom.png'
          }
        ]
      }
    
      return  {
        status: 0,
        data: data,
        msg: '手机号或密码错误',
        returnValue: 'token-xxxxxxx'
      }
    }
}
