import Vue from "vue";
import ElementUI from "element-ui";
Vue.use(ElementUI);

// 全局修改默认配置，点击空白处不能关闭弹窗
ElementUI.Dialog.props.closeOnClickModal.default = false
// 全局修改默认配置，按下ESC可以关闭弹窗
ElementUI.Dialog.props.closeOnPressEscape.default = true
