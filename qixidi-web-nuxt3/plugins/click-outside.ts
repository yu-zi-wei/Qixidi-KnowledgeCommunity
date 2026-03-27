import type { Directive } from 'vue'

//点击元素外部时触发的指令
export default defineNuxtPlugin((nuxtApp) => {
  const clickOutside: Directive = {
    mounted(el, binding) {
      el._clickOutside = (event: Event) => {
        // 检查点击的元素是否在绑定元素内部
        if (!(el === event.target || el.contains(event.target))) {
          binding.value(event)
        }
      }
      document.addEventListener('click', el._clickOutside)
    },
    unmounted(el) {
      document.removeEventListener('click', el._clickOutside)
    }
  }

  nuxtApp.vueApp.directive('click-outside', clickOutside)
})
