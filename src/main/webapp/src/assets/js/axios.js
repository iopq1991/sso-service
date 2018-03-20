/*
自定义的axios 第3个参数（config）字段意义：
{
showError:   true表示ajax出错时，弹出alert框提示。默认不弹出
noToken：    true表示请求时，不在header中带入token。默认带入
maskOptions:  表示请求时显示遮罩层的选项。默认{body: true}
}

*/

import axios from 'axios'
import {Loading, Message, MessageBox} from 'element-ui'
import appConfig from '../../../config/app-config'
import $x from './$x.js';

//element Message配置
const ERROR_MSG_OP = {type: 'error', closeOnPressEscape: true}

//for el-upload
axios.getYxtHeaders = function () {
    var r = {}
    r[$x.CONST.TOKEN_HEADER] = $x.tokenUtil.get()
    return r;
}

// 超时时间
// axios.defaults.timeout = 8000
axios.defaults.baseURL = appConfig.API_SERVER
axios.defaults.headers.post['Content-Type'] = 'application/json'
axios.defaults.withCredentials = true;
axios.defaults.timeout = 20000; //20秒，超时报错
axios.defaults.maskOptions = {target: '.app-body > div'}; //默认的遮罩曾目标

axios.defaults.transformRequest = function (request) {
    return JSON.stringify(request)
}

// http请求拦截器
axios.interceptors.request.use(function (config) {
    if (!config.noToken) {
        config.headers[$x.CONST.TOKEN_HEADER] = $x.tokenUtil.get();
    }
    //遮罩层
    var maskOpt = config.maskOptions;
    if (maskOpt === undefined) maskOpt = axios.defaults.maskOptions;
    $x.loading.show(maskOpt);

    return config
}, fail)

// http响应拦截器
axios.interceptors.response.use(function (res) {
    $x.loading.close(res.config.maskOptions);
    var data = res.data;
    if (data.status == 2) { //session失败
        $x.alert('您需要重新登陆', '提示', {
            callback: action => {
                var url = appConfig.LOGIN_PATH;
                var path = location.href.match(/https?:\/\/[^\/]+(\/.+)/i)[1];
                if (path && path !== '/main.html#/') {
                    url += url.indexOf('?') > -1 ? '&' : '?';
                    url += 'redirectUrl=' + encodeURIComponent(path);
                }
                location.href = url
            }
        })
        return {};
    }
    else if (data.status != 0) { //错误
        if (data.returnCode == '9999') {
            console.error(data);
            data.msg = '服务器开小差啦，请稍后重试！';
        }

        if (res.config.showError === 'toast')
            $x.toast.error(data.msg)
        else if (res.config.showError)
            $x.alert(data.msg, ERROR_MSG_OP)

        return Promise.reject(data)
    }
    return data
}, fail)

function fail(error) {
    $x.loading.close(error.config.maskOptions);
    var msg = '网络异常，请稍后重试';
    $x.alert(msg, ERROR_MSG_OP)
    return Promise.reject(error)
}

export default axios
