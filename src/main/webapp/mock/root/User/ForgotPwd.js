module.exports = {
    disabled: 0,
    status: 200,
    body: function (query, post, header, request){
      var data = {
      }
      return ok(data)
    
      function ok(d){
        return {status:0,data:d,msg:'密码修改成功'}
      }
    }
}
