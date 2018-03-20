module.exports = {
    disabled: 1,
    status: 200,
    headers: function (query, post, header, request) {
        var r = {}
        if (post.rememberPwd && this.checkFn(post, header)) {
            process._cookiev = 'a=b' + (+new Date);
            r['set-cookie'] = process._cookiev + '; Max-Age=30000; path=/';
        }
        else {
            process._cookiev = '';
            r['set-cookie'] = process._cookiev + '; Max-Age=0; path=/';
        }
        return r;
    },
    body: function (query, post, header, request) {
        return {
            status: this.checkFn(post, header) ? 0 : 1,
            data: '', returnValue: 'token-xxxxxxx',
            msg: '用户名密码错误'
        }
    },
    checkFn: function (post, header) {
        if (post.password == '123456' || post.password == '') return true;

        if (post.useRemembered && post.password == '_fakepwd') {
            var cookie = header['Cookie'] || header['cookie'];
            return cookie && cookie.indexOf(process._cookiev) > -1
        }
    }
}
