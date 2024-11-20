import Vue from 'vue';

Vue.prototype.$post = function () {
  return "POST"
}
Vue.prototype.$get = function () {
  return "GET"
}
Vue.prototype.$put = function () {
  return "PUT"
}
