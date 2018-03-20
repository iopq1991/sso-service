//For 单元测试和测试环境的打包发布
var merge = require('webpack-merge')
var devEnv = require('./dev.env')

module.exports = merge(devEnv, {
    NODE_ENV: '"testing"',

    API_SERVER: '"//portal-service.test61.ums86.com"',
    API_SERVER_SSO: '"//zhhy-sso.test61.umss.cn"',
})
