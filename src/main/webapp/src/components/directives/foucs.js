import Vue from 'vue';
/*
Usage:
<input v-focus />
Or:
<div v-focus="'input.this1focus'">
    <input />
    <input class="this1focus" />
</div>
 */
Vue.directive('focus', {
    inserted: function (el, bindding) {
        if (bindding.value) {
            var e = el.querySelector && el.querySelector(bindding.value);
            e && setTimeout(() => e.focus(), 100);
        }
        else {
            el.focus()
        }
    }
})