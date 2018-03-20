/**
 * store仅仅用来存储用户信息等一些全局通用的数据，
 * 不建议将每一个页面的数据存储到Store中
 */
import Vue from 'vue'
import Vuex from 'vuex'
import getters from './getters'
import actions from './actions'
import mutations from './mutations'

Vue.use(Vuex)

const state = {}

export default new Vuex.Store({
  state,
  getters,
  actions,
  mutations
})
