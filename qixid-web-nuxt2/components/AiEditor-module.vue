<template>
  <div class="my-editor">
    <div :id="aiEditorId" class="aiEditor-class" :style="{'height':editorHeight==''?'':editorHeight}">
      <div :class="{'aie-container':true, 'aie-container-default-height':editorHeight=='100%'}"
           style="overflow: hidden">
        <!--        &lt;!&ndash;        头部&ndash;&gt;-->
        <div class="aie-container-header" :style="{'display':editable?'block':'none'}"></div>
        <!--        &lt;!&ndash;        底部&ndash;&gt;-->
        <div class="aie-container-footer" :style="{'display':editable?'block':'none'}"></div>
      </div>
    </div>
  </div>
</template>

<script>
import "aieditor/dist/style.css";

export default {
  name: "AiEditorModule",
  props: {
    // 接收值父组件传递值
    content: {
      type: String,
      default: "",
    },
    //主题(未使用)
    theme: String,
    //编辑器高度
    editorHeight: {
      type: String,
      default: "",
    },
    //是否只读
    editable: {
      type: Boolean,
      default: true,
    },
    //极简模式
    minimalistMode: {
      type: Boolean,
      default: true,
    },
    aiEditorId: {
      type: String,
      default: "aiEditor",
    },
    toolbarSize: {
      type: String,
      default: "small",
    },
  },
  data() {
    return {
      editorContent: this.content,
      AiEditorEvn: null,
      tocTags: ["H1", "H2", "H3", "H4"],//筛选目录
    }
  },
  methods: {
    init() {
      let toolbarExcludeKeysList = ["undo", "redo", "brush", "eraser", "ai"]
      if (this.minimalistMode) {
        let surplusExclude = ["heading", "font-family", "font-size", "source-code", "printer"]
        toolbarExcludeKeysList.push(...surplusExclude)
      }
      // 只有在客户端才会执行这段代码
      const {AiEditor} = require('aieditor'); // 动态加载模块
      this.AiEditorEvn = new AiEditor({
        element: "#" + this.aiEditorId,//挂载
        toolbarSize: this.toolbarSize,// 工具栏大小 默认 small，可选 'small' | 'medium' | 'large'
        content: this.content,//内容
        draggable: false,//是否可拖拽
        textCounter: false,//是否显示字数
        contentIsMarkdown: true,
        theme: "light",//主题 支持配置为 "light"（亮色） 或者 "dark"（暗色）
        placeholder: "支持Markdown...",
        contentRetention: false,//是否自动保存
        editable: this.editable, //是否只读 false:只读,true:可编写
        contentRetentionKey: "ai-editor-content",//自动保存（缓存）到 localStorage 的 key 值，默认为：ai-editor-content。
        //配置浮动菜单
        textSelectionBubbleMenu: {
          enable: true,
          items: ["Bold", "Italic", "Underline", "Strike", "code", "comment"],
        },
        //排除菜单
        toolbarExcludeKeys: toolbarExcludeKeysList,
        //图片上传逻辑
        image: {
          allowBase64: true,
          defaultSize: 800,//默认图片大小
          uploadUrl: "/api/system/oss/upload",
          uploadHeaders: {
            Authorization: "Bearer " + this.$store.state.token,
          },
          uploader: (file, uploadUrl, headers, formName) => {
            const formData = new FormData();
            formData.append('file', file);
            return new Promise((resolve, reject) => {
              fetch(uploadUrl, {
                method: "post",
                headers: {'Accept': 'application/json', ...headers},
                body: formData,
              }).then((resp) => resp.json())
                .then(json => {
                  resolve(json);
                }).catch((error) => {
                reject(error);
              })
            });
          },
        },
        //附件上传
        attachment: {
          uploadUrl: "/api/system/oss/upload",
          uploadFormName: "attachment", //上传时的文件表单名称
          uploadHeaders: () => {
            return {
              "Authorization": "Bearer " + this.$store.state.token,
            }
          },
          uploader: (file, uploadUrl, headers, formName) => {
            const formData = new FormData();
            formData.append('file', file);
            return new Promise((resolve, reject) => {
              fetch(uploadUrl, {
                method: "post",
                headers: {'Accept': 'application/json', ...headers},
                body: formData,
              }).then((resp) => resp.json())
                .then(json => {
                  resolve(json);
                }).catch((error) => {
                reject(error);
              })
            });
          },
        },
        //内容变化监听
        onChange: (aiEditor) => {
          this.$emit('update:mdContent', aiEditor.getMarkdown());
          this.$emit('update:htmlContent', aiEditor.getHtml());
          //自定义获取目录逻辑
          this.$emit('update:outline', this.generateDirectory(this.aiEditorId));
        },
        //实例被创建
        onCreated: (aiEditor) => {
          //获取目录列表(api)
          // this.$emit('update:outline', aiEditor.getOutline());
          //自定义获取目录逻辑
          this.$emit('update:outline', this.generateDirectory(this.aiEditorId));
        },
      });
    },
    generateDirectory(aiEditorId) {
      let container = document.querySelector('#' + aiEditorId);
      let childNodes = container.children[0].children[2].children[0].childNodes;
      let tocArray = [];
      childNodes.forEach(item => {
        let tagName = item.tagName;
        //是否包含 tocTags存在的标签
        if (tagName == undefined || !this.tocTags.includes(tagName.toUpperCase())) {
          return true;
        }
        tocArray.push({
          id: item.id,
          text: item.innerText,
          level: Number.parseInt(tagName.substring(1)),
          pos: item.offsetTop,
        })
      });
      return tocArray;
    },
  },
  mounted() {
    this.init();
  }
}
</script>

<style>
/*关闭字数计数器*/
aie-footer {
  display: none;
}

/**
自定义主题颜色
 */
.my-editor .aie-theme-light {

}

/**
 当editorHeight=100%时， 编辑器默认高度
 */
.aie-container-default-height {
  min-height: 80vh !important;
}

.aie-container {
  border: none !important;
  overflow: hidden !important;
  width: 100%;
}

.aie-container::-webkit-scrollbar {
  display: none;
}

.aie-content p {
  margin: 2px 0;
  line-height: 30px;
}

.aie-content h1 {
  padding: 15px 0;
}

.aie-content h2 {
  padding: 15px 0;
}

.aie-content h3 {
  padding: 15px 0;
}

.aie-content h4 {
  padding: 15px 0;
}

.aie-content h5 {
  padding: 15px 0;
}


/*代码块相关*/
.aie-content pre code {
  font-size: 16px !important;
  line-height: 24px !important;
}

.aie-container .aie-codeblock-wrapper {
  margin: 10px 0;
}

/*高亮块*/
mark {
  padding: 6px;
  border-radius: 2px;
}

/*标注*/
.aie-content blockquote {
  border-left: 0.2rem solid #22a6b3 !important;
  background-color: #eef7fa !important;
  padding: 4px 6px !important;
}

/*
高亮块
 */
.aie-content div.warning {
  border-radius: 4px;
}

.aiEditor-class li {
  list-style-type: disc !important;
  //color: var(--theme-color);

}
</style>
