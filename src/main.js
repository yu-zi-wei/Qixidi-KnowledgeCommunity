import {createApp} from 'vue'
import App from './App.vue'
import './registerServiceWorker'
import router from './router'
import store from './store'

// arco-design 组件库
import ArcoVue from '@arco-design/web-vue';
import '@arco-design/web-vue/dist/arco.css';

import './assets/css/index.css'

const app = createApp(App);
app.use(store);
app.use(router);
app.use(ArcoVue);
app.mount('#app')

// createApp(App).use(store).use(router).mount('#app')
