var config = require('./mock-config.js');
var ssoConfig = {
    proxyTarget: 'http://10.0.0.204:4000', //后台接口服务地址（代理目标），为空表示不代理
    port: 8086, //端口
};
module.exports = Object.assign(config, ssoConfig);

