var merge = require('webpack-merge')
var prodEnv = require('./prod.env')

module.exports = merge(prodEnv, {
    NODE_ENV: '"development"',

    //配置开发环境的接口地址
    //API_SERVER: '"//10.0.0.193:8999"',
	API_SERVER: '"//localhost:4000"',
    //API_SERVER_SSO: '"localhost:4000"',

})
