import Vue from 'vue'
import breadCrumb from '@/modules/common/header/children/pathNav.vue';
import topMenu from '@/modules/common/header/children/topMenu.vue';
import userInfo from '@/modules/common/header/children/userInfo.vue';
import header from '@/modules/common/header/header.vue'

import { createVue, createTest, destroyVM } from '../util';

describe('breadCrumb.vue', () => {
    let vm;
    afterEach(() => {
        destroyVM(vm);
    });
    // var sandbox = sinon.sandbox.create();
    //
    // // stub some console methods
    // sandbox.stub(window.console, "log");
    // sandbox.stub(window.console, "error");

    it('is fixed', () => {
        vm = createTest(breadCrumb, {});
        vm.eventBus.$emit('nav-path-change', 0, {link: '/tt', name: 'TEST1'});
        console.error(vm.content);
        // var t1 = vm.$el.querySelector('.el-breadcrumb__item__inner:first-child');
        // expect(t1).not.null;
        // expect(t1.textContent).toEqual('TEST1');
        //
        // vm.eventBus.$emit('nav-path-change', 0, {link: '/tt2', name: 'TEST2'})
        // expect(vm.$el.querySelector('.el-breadcrumb__item__inner:eq(1)').textContent).toEqual('TEST2');

    });

})
