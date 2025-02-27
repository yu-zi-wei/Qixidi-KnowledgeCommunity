<template>
  <div>
    <!--    <button @click="getMarkdownContent">获取 Markdown 内容</button>-->
    <div :id="vditorId"></div>
  </div>
</template>

<script>

// 从本地静态文件引入 Vditor
import Vditor from 'static/vditor/dist/index.min.js'
export default {
  props: {
    // 接收父组件传递的 Markdown 内容
    vditorId: {
      type: String,
      default: 'vditor'
    },
    content: {
      type: String,
      default: ''
    },
    mode: {
      type: String,
      default: 'wysiwyg'
    },
    height: {
      type: String,
      default: '400px'
    },
    outline: {
      type: Boolean,
      default: true
    }
  },
  data() {
    return {
      vditor: null
    }
  },
  mounted() {
    this.initVditor()
  },
  beforeDestroy() {
    if (this.vditor) {
      this.vditor.destroy()
    }
  },
  methods: {
    getMarkdownContent() {
      const markdownContent = this.vditor.getValue();
      console.log('Markdown 内容:', markdownContent);
    },
    initVditor() {
      this.vditor = new Vditor(this.vditorId, {
        height: this.height,
        width: '100%',
        mode: this.mode,//wysiwyg-所见即所得、sv-分屏预览、ir-即时渲染、preview-预览模式
        value: this.content, // 设置要预览的 Markdown 内容
        placeholder: '编写内容......',
        theme: 'classic',
        counter: {
          enable: true,
          type: 'markdown'
        },
        //默认打开目录
        outline: {
          enable: this.outline,
        },
        preview: {
          actions: [],//预览页面 复制到其他地方
          delay: 0,
          hljs: {
            style: 'atom-one-dark',
            lineNumber: true,// 显示行号
            enable: true,// 启用代码高亮
          }
        },
        tab: '\t',
        typewriterMode: true,
        toolbarConfig: {
          pin: true
        },
        cache: {
          enable: false
        },
        hint: {
          emoji: {
            "confused": "😕",
            "astonished": "😲",
            "baby": "👶",
            "smile": "😄",
            "blush": "😊",
            "angel": "👼",
            "cold_sweat": "😰",
            "confounded": "😖",
            "cry": "😢",
            "full_moon_with_face": "🌝",
            "grimacing": "😬",
            "grin": "😁",
            "grinning": "😀",
            "heart_eyes": "😍",
            "kissing": "😗",
            "kissing_cat": "😽",
            "kissing_closed_eyes": "😚",
            "kissing_heart": "😘",
            "kissing_smiling_eyes": "😙",
            "clown_face": "🤡",
            "+1": "👍",
            "-1": "👎",
            "clap": "👏",
            "fist_oncoming": "👊",
            "fist_raised": "✊",
            "birthday": "🎂",
            "eyes": "👀️",
            "heart": "❤️",
            "broken_heart": "💔",
            "rocket": "🚀️",
            "beer": "🍺",
            "beers": "🍻",
            "cocktail": "🍸",
            "coffee": "☕️",
            "tada": "🎉️",
            "anger": "💢",
            "fire": "🔥",
            "fish": "🐟",
            "carousel_horse": "🎠",
            "carrot": "🥕",
            "cat": "🐱",
            "cat2": "🐈",
            "chicken": "🐔",
          },
        },
        after: {},
        input: (value) => {
          this.$emit('update:mdContent', value);
          this.$emit('update:htmlContent', value);
          //更新主题
          // this.$emit('update:theme', this.codeStyle)
        },
        upload: {
          url: '/api/system/oss/upload', // 后端上传接口地址
          accept: 'image/*', // 允许上传的文件类型
          max: 10 * 1024 * 1024, // 最大上传文件大小，这里设置为 10MB
          fieldName: 'file', // 上传文件的表单字段名
          multiple: false, // 是否允许上传多个文件
          headers: {
            Authorization: `Bearer ${this.$store.state.token}`
          },
          // 处理文件名，去除特殊字符
          filename(name) {
            return name.replace(/[^(a-zA-Z0-9\u4e00-\u9fa5\.)]/g, "")
              .replace(/[\?\\/:|<>\*\[\]\(\)\$%\{\}@~]/g, "")
              .replace("/\\s/g", "");
          },
          format(files, responseText) {
            let code = JSON.parse(responseText)
            let msg = JSON.parse(responseText)
            let data = JSON.parse(responseText)
            let filName = data.data.fileName
            let filUrl = data.data.url
            let succ = {}
            succ[filName] = filUrl
            //图片回显
            return JSON.stringify({
              msg,
              code,
              data: {
                errFiles: [],
                succMap: succ
              }
            })
          },
          error(err) {
            console.error('文件上传出错:', err);
          }
        },
        toolbar: [
          'headings',//标题
          'bold',//加粗
          'italic',//斜体
          'strike',//删除线
          'link',//链接
          'emoji',//表情
          '|',
          'list',//列表
          'ordered-list',//列表
          'check',//任务列表项
          'outdent',//减少缩进
          'indent',//缩进
          '|',
          'quote',//引用
          'line',//分割线
          'code',//代码块
          'inline-code',//行内代码
          'insert-before',//插入起始行
          'insert-after',//插入末行
          '|',
          'upload',//上传
          // 'record',//录音
          'table',//表格
          '|',
          'undo',//下一步
          'redo',//上一步
          '|',
          'fullscreen',//全屏
          'preview',//预览
          'outline',//大纲
          'code-theme',//代码主题
          // 'content-theme',//主题
          'export',//导出
          'help',//帮助
          'edit-mode',//切换模式
          // {
          //   name: 'custom',
          //   tip: '自定义按钮',
          //   tipPosition: 's',
          //   icon: '<svg viewBox="0 0 1024 1024"><path d="M512 85.333333C276.266667 85.333333 85.333333 276.266667 85.333333 512s190.933333 426.666667 426.666667 426.666667 426.666667-190.933333 426.666667-426.666667S747.733333 85.333333 512 85.333333z m0 768c-188.544 0-341.333333-152.789333-341.333333-341.333333S323.456 170.666667 512 170.666667 853.333333 323.456 853.333333 512 700.544 853.333333 512 853.333333z m136.533333-443.733333l-170.666666 102.4-102.4 170.666667-68.266667-68.266667 102.4-170.666667 170.666667-102.4 68.266666 68.266667z"></path></svg>',
          //   click: () => {
          //     alert('自定义按钮点击事件');
          //   },
          // },
        ]
      })
    }
  }
}
</script>

<style>
@import url("components/css/vditor-components.css");
</style>
