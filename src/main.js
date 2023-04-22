import {createApp} from 'vue'
import App from './App.vue'
import './registerServiceWorker'
import router from './router'
import store from './store'

// arco-design 组件库
import ArcoVue from '@arco-design/web-vue';
import '@arco-design/web-vue/dist/arco.css';

import './assets/css/index.css'
import './assets/css/index-media.css'

//顶部页面加载条
import NProgress from 'nprogress';
import 'nprogress/nprogress.css';

NProgress.configure({
    easing: 'ease',
    speed: 500,
    showSpinner: false,
    trickleSpeed: 200,
    minimum: 0.3
})

//路由监听
router.beforeEach((to, from, next) => {
    NProgress.start();
    next();
});

//路由跳转结束
router.afterEach(() => {
    NProgress.done()
})

const app = createApp(App);
app.use(store);
app.use(router);
app.use(ArcoVue);
app.mount('#app')

// createApp(App).use(store).use(router).mount('#app')
