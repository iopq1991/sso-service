import Vue from 'vue';
import Login from '@/modules/otherlogin/login';
require('../assets/fonts/flag-icon.css');
require('../assets/css/common.scss');
Vue.prototype.eventBus = new Vue();
new Vue({
	el:"#app",
	template:'<Login></Login>',
	components:{Login}
});
