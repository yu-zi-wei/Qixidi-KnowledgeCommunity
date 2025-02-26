<template>
  <div :id="id"></div>
</template>

<script>
import Vditor from 'static/vditor/dist/index.min.js'

export default {
  name: "VditorPreview",
  props: {
    // 接收父组件传递的 Markdown 内容
    content: {
      type: String,
      default: ''
    },
    id: {
      type: String,
      default: 'contentId'
    }
  },
  methods: {
    initVditor() {
      this.$nextTick(() => {
        let tocArray = [];//目录
        let content = this.content;
        let id = this.id;
        Vditor.preview(document.getElementById(id), content, {
          speech: {
            enable: false,
          },
          cache: {
            enable: false
          },
          width: '100%',
          hljs: {
            style: 'atom-one-dark',//主题：androidstudio,atom-one-dark
            lineNumber: false,// 显示行号
            enable: true,// 启用代码高亮
          },
          anchor: 1,
          after() {
            //生成目录
            let tocTags = ["H1", "H2", "H3", "H4"];//筛选目录
            let element = document.getElementById(id);
            let childNodes = element.children;
            for (let i = 0; i < childNodes.length; i++) {
              let node = childNodes[i];
              // 判断节点是否为 H 标签
              if (tocTags.includes(node.tagName)) {
                let id = node.getAttribute("id");
                tocArray.push({
                  id: id,
                  text: node.textContent.trim(),
                  level: Number.parseInt(node.tagName.substring(1)),
                  pos: node.offsetTop,
                })
              }
            }
          },
        });
        this.$emit('update:outline', tocArray);
      });
    },
  },
  mounted() {
    this.initVditor();
  }
}
</script>

<style>
.vditor-reset {
  width: 100%;
}

.vditor-reset::-webkit-scrollbar {
  width: 4px;
  height: 4px;
  background-color: #ced6e0;
}

.vditor-reset::-webkit-scrollbar-track {
  background: #fefefe;
  border-radius: 2px;
}

.vditor-reset::-webkit-scrollbar-thumb {
  background: #ced6e0;
  border-radius: 2px;
}
</style>
