import Vue from 'vue';

const globalConfig = {
  //域名
  domainName() {
    return "https://petrichor.ren/";
  },
}
Vue.prototype.$globalConfig = globalConfig;
