import config from '../../../config/app-config.js';
import axios from './axios.js';
import {Loading, Message, MessageBox} from 'element-ui';
import CONST from './CONST.js'
import msg from './msg.js'
import storageUtil from './utils/storageUtil.js';
import frequence from './utils/frequence.js';
import platInfo from './utils/platInfo.js';
import tokenUtil from './utils/tokenUtil.js';
import domUtil from './utils/domUtil.js';
import loading from './loading.js';
//import router from '../../router/router.js';

const msgDialog = {
    alert: MessageBox.alert,
    confirm: MessageBox.confirm,
    prompt: MessageBox.prompt,
    toast: Message, //msg, type = [success,info,warning,error], duration
    loading: Loading.service
};

const urlClick = {
    goUrl(url, withToken, inSameWindow) {
        if (this.isAbsUrl(url)) {  // 绝对地址
            if (withToken) {
                url += url.indexOf('?') > -1 ? "&" : '?';
                url += 'token=' + tokenUtil.get();
            }
            if (inSameWindow)
                location = url;
            else
                window.open(url);
        }
        else {
            location = url;
            //router.push(url);
        }
    },
    isAbsUrl(url) {
        return url.split('?')[0].indexOf('//') > -1  // 绝对地址
    },
    isSameRoute(router1, router2, toLevel) {
        var r1 = router1.split('/'), r2 = router2.split('/');
        for (var i = 0; i <= toLevel; i++) {
            if (r1[i] != r2[i]) return false
        }
        return true;
    }
}

const util = {
    clone(obj, deep) {
        return deep ? JSON.parse(JSON.stringify(obj)) : Object.assign({}, obj)
    },
}

var mixed = {
    plat: platInfo.getPlat(),
    ...util,
    ...frequence,
    ...msgDialog,
    ...urlClick,
    config,
    http: axios,
    post: axios.post,
    get(url, pars, config) {
        if (pars) {
            pars.f_rnd = +new Date;  // 防止火狐缓存GET请求
            url += url.indexOf('?') > -1 ? '&' : '?';
            url += Object.keys(pars).map(key => key + '=' + pars[key]).join('&');
        }
        return axios.get(url, config)
    },

    storageUtil,
    CONST,
    msg,
    tokenUtil,
    ...domUtil,
    loading
};

var $x = domUtil

Object.assign($x, mixed);

export default $x;