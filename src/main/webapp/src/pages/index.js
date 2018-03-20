import Vue from 'vue'
import Login from '@/modules/login/login.vue';

require('../assets/fonts/flag-icon.css');
require('../assets/css/common.scss');

Vue.prototype.eventBus = new Vue();

new Vue({
    el: '#app',
    template: '<login></login>',
    components: {Login}
})
