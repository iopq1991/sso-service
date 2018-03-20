import $x from "@/assets/js/$x.js";

export default {
    //登录成功，跳转到主页或是redirectUrl
    goRedirect () {
        var target = location.search.match(/redirectUrl=([^&]+)/i);
        if (target) {
            var targetUrl = decodeURIComponent(target[1]);
            if (targetUrl.match(/^https?:\/\//))
                $x.goUrl(targetUrl, true, true);
            else
                location = targetUrl;
        }
        else {
            location = '/main.html';
        }
    }
}