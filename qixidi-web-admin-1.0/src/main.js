import Vue from 'vue'

import Cookies from 'js-cookie'

import Element from 'element-ui'

import './assets/styles/element-variables.scss'

import '@/assets/styles/index.scss' // global css
import '@/assets/styles/ruoyi.scss' // ruoyi css
import App from './App'
import store from './store'
import router from './router'
import directive from './directive' // directive
import plugins from './plugins' // plugins
import {download} from '@/utils/request'

import './assets/icons' // icon
import './permission' // permission control
import {getDicts} from "@/api/system/dict/data";
import {getConfigKey, updateConfigByKey} from "@/api/system/config";
import {parseTime, resetForm, addDateRange, selectDictLabel, selectDictLabels, handleTree} from "@/utils/ruoyi";
// 分页组件
import Pagination from "@/components/Pagination";
// 自定义表格工具组件
import RightToolbar from "@/components/RightToolbar"
// 富文本组件
import Editor from "@/components/Editor"
// 文件上传组件
import FileUpload from "@/components/FileUpload"
// 图片上传组件
import ImageUpload from "@/components/ImageUpload"
// 图片预览组件
import ImagePreview from "@/components/ImagePreview"
// 字典标签组件
import DictTag from '@/components/DictTag'
// 头部标签组件
import VueMeta from 'vue-meta'
// 字典数据组件
import DictData from '@/components/DictData'
//全局css
import './assets/css/index.css'
import './assets/css/svg.css'
//font-awesome图标库
import 'font-awesome/css/font-awesome.min.css'
//iconPark图标库
import {install} from '@icon-park/vue/es/all';

install(Vue, 'svg');

//代码高亮
import 'highlight.js/styles/googlecode.css'
import hljs from 'highlight.js'

// 全局注册mavon-editor编辑器
import mavonEditor from 'mavon-editor'
import 'mavon-editor/dist/css/index.css'
// use
Vue.use(mavonEditor)

/* 3、v-md-editor 二次封装  */
import mdEditor from '@/components/v-md-editor/index';

Vue.component('MdEditor', mdEditor);
/* v-md-editor 编辑器 end  */


// 全局方法挂载
Vue.prototype.getDicts = getDicts
Vue.prototype.getConfigKey = getConfigKey
Vue.prototype.updateConfigByKey = updateConfigByKey
Vue.prototype.parseTime = parseTime
Vue.prototype.resetForm = resetForm
Vue.prototype.addDateRange = addDateRange
Vue.prototype.selectDictLabel = selectDictLabel
Vue.prototype.selectDictLabels = selectDictLabels
Vue.prototype.handleTree = handleTree
Vue.prototype.download = download
// Vue.prototype.VMdEditor = VMdEditor
// 全局修改默认配置，点击空白处不能关闭弹窗
Element.Dialog.props.closeOnClickModal.default = false
// 全局修改默认配置，按下ESC不能关闭弹窗
Element.Dialog.props.closeOnPressEscape.default = true
// 全局组件挂载
Vue.component('DictTag', DictTag)
Vue.component('Pagination', Pagination)
Vue.component('RightToolbar', RightToolbar)
Vue.component('Editor', Editor)
Vue.component('FileUpload', FileUpload)
Vue.component('ImageUpload', ImageUpload)
Vue.component('ImagePreview', ImagePreview)

Vue.use(directive)
Vue.use(plugins)
Vue.use(VueMeta)
DictData.install()
Vue.use(Element)
// Vue.use(VMdEditor)	;
/**
 * If you don't want to use mock-server
 * you want to use MockJs for mock api
 * you can execute: mockXHR()
 *
 * Currently MockJs will be used in the production environment,
 * please remove it before going online! ! !
 */

Vue.use(Element, {
  size: Cookies.get('size') || 'medium' // set element-ui default size
});

Vue.config.productionTip = false;

new Vue({
  el: '#app',
  router,
  store,
  render: h => h(App)
});
hljs.highlightCode = function () {
  //自定义highlightCode方法，将只执行一次的逻辑去掉
  let blocks = document.querySelectorAll('pre code');
  [].forEach.call(blocks, hljs.highlightBlock);
};
