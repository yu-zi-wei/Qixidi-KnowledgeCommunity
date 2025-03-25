<template>
  <div :style="{'height':mavonHeight==null?'90vh':mavonHeight+'px'}">
    <mavon-editor
      v-model="editorValue"
      :codeStyle="codeStyle"
      :boxShadow="false"
      :placeholder="'内容···'"
      ref="md"
      class="md"
      @imgAdd="imgAdd"
      @change="change"
      :ishljs="true"
      :navigation="false"
      fontSize="18px"
      toolbarsBackground="#FFFFFF"
      previewBackground="#FFFFFF"
      style="height: 100%"
    >
      <!-- 右工具栏后加入自定义按钮  -->
      <template slot="right-toolbar-after">
        <div class="button-item op-icon" @click="shortcutKeyStatus = true" title="快捷键">
          <svg t="1699188702507" class="icon icon-size-20 svg-translateY-4" viewBox="0 0 1024 1024" version="1.1"
               xmlns="http://www.w3.org/2000/svg"
               p-id="14631">
            <path
              d="M908.544 156.16H141.056c-30.6944 0-57.6512 11.0592-79.5648 32.6144-22.016 21.6576-33.3312 48.384-33.3312 78.848v488.7808c0 30.464 11.3152 57.1648 33.3312 78.8224A110.2848 110.2848 0 0 0 141.056 867.84h767.488c30.72 0 57.6512-11.0592 79.5648-32.6144 22.016-21.6576 33.3312-48.3584 33.3312-78.8224V267.6224c0-30.464-11.3152-57.1904-33.3312-78.848A110.208 110.208 0 0 0 908.5696 156.16zM141.056 202.24h767.488c18.6112 0 33.9456 6.2976 47.2576 19.4048 13.2352 13.0048 19.5584 27.904 19.5584 45.9776v488.7808c0 18.048-6.3232 32.9216-19.5584 45.952-13.312 13.1072-28.672 19.4048-47.232 19.4048H141.056a64.256 64.256 0 0 1-47.2832-19.4048c-13.2352-13.0304-19.5584-27.904-19.5584-45.952V267.6224c0-18.048 6.3232-32.9728 19.5584-45.9776A64.256 64.256 0 0 1 141.056 202.24z"
              fill="#666666" p-id="14632"></path>
            <path
              d="M281.6 476.16a23.04 23.04 0 0 1 2.8928 45.9008L281.6 522.24H204.8a23.04 23.04 0 0 1-2.8928-45.9008L204.8 476.16h76.8z"
              fill="#666666" p-id="14633"></path>
            <path
              d="M460.8 476.16a23.04 23.04 0 0 1 2.8928 45.9008L460.8 522.24h-76.8a23.04 23.04 0 0 1-2.8928-45.9008L384 476.16h76.8z"
              fill="#FF6D00" p-id="14634"></path>
            <path
              d="M640 476.16a23.04 23.04 0 0 1 2.8928 45.9008L640 522.24h-76.8a23.04 23.04 0 0 1-2.8928-45.9008L563.2 476.16h76.8zM844.8 629.76a23.04 23.04 0 0 1 2.8928 45.9008L844.8 675.84H204.8a23.04 23.04 0 0 1-2.8928-45.9008L204.8 629.76h640zM819.2 476.16a23.04 23.04 0 0 1 2.8928 45.9008L819.2 522.24h-76.8a23.04 23.04 0 0 1-2.8928-45.9008L742.4 476.16h76.8zM281.6 348.16a23.04 23.04 0 0 1 2.8928 45.9008L281.6 394.24H204.8a23.04 23.04 0 0 1-2.8928-45.9008L204.8 348.16h76.8zM460.8 348.16a23.04 23.04 0 0 1 2.8928 45.9008L460.8 394.24h-76.8a23.04 23.04 0 0 1-2.8928-45.9008L384 348.16h76.8z"
              fill="#666666" p-id="14635"></path>
            <path
              d="M640 348.16a23.04 23.04 0 0 1 2.8928 45.9008L640 394.24h-76.8a23.04 23.04 0 0 1-2.8928-45.9008L563.2 348.16h76.8z"
              fill="#FF6D00" p-id="14636"></path>
            <path
              d="M819.2 348.16a23.04 23.04 0 0 1 2.8928 45.9008L819.2 394.24h-76.8a23.04 23.04 0 0 1-2.8928-45.9008L742.4 348.16h76.8z"
              fill="#666666" p-id="14637"></path>
          </svg>
        </div>
      </template>
      <!-- 左工具栏后加入自定义按钮  -->
      <template slot="left-toolbar-after">
        <el-dropdown @command="handleTheme">
          <div class="el-dropdown-link">
            <div class="button-item" title="代码主题">
              <span class="align-items-center">
                <svg t="1699188540938" class="icon icon-size-20 svg-translateY-4" viewBox="0 0 1024 1024" version="1.1"
                     xmlns="http://www.w3.org/2000/svg" p-id="13428"><path
                  d="M533.333333 853.333333c179.2 0 320-145.066667 320-320S708.266667 213.333333 533.333333 213.333333 213.333333 358.4 213.333333 533.333333c0 93.866667 34.133333 132.266667 115.2 157.866667 8.533333 4.266667 12.8 4.266667 21.333334 8.533333 4.266667 0 21.333333 4.266667 21.333333 8.533334 51.2 17.066667 76.8 25.6 98.133333 51.2 17.066667 17.066667 29.866667 38.4 42.666667 59.733333 4.266667 8.533333 8.533333 17.066667 8.533333 21.333333 8.533333 0 8.533333 8.533333 12.8 12.8z m0 42.666667c-59.733333 0-21.333333-42.666667-89.6-110.933333-64-64-273.066667-25.6-273.066666-251.733334C170.666667 332.8 332.8 170.666667 533.333333 170.666667S896 332.8 896 533.333333 733.866667 896 533.333333 896z m72.533334-106.666667c-42.666667 0-76.8-34.133333-76.8-76.8s34.133333-72.533333 76.8-72.533333 76.8 34.133333 76.8 72.533333-34.133333 76.8-76.8 76.8z m0-42.666666c17.066667 0 34.133333-12.8 34.133333-34.133334s-12.8-29.866667-34.133333-29.866666-34.133333 12.8-34.133334 29.866666 17.066667 34.133333 34.133334 34.133334zM640 384c-25.6 0-42.666667-17.066667-42.666667-42.666667s17.066667-42.666667 42.666667-42.666666 42.666667 17.066667 42.666667 42.666666-17.066667 42.666667-42.666667 42.666667z m-119.466667-34.133333c-25.6 0-42.666667-17.066667-42.666666-42.666667s17.066667-42.666667 42.666666-42.666667 42.666667 17.066667 42.666667 42.666667-17.066667 42.666667-42.666667 42.666667zM725.333333 469.333333c-25.6 0-42.666667-17.066667-42.666666-42.666666s17.066667-42.666667 42.666666-42.666667 42.666667 17.066667 42.666667 42.666667-17.066667 42.666667-42.666667 42.666666zM405.333333 384c-25.6 0-42.666667-17.066667-42.666666-42.666667s17.066667-42.666667 42.666666-42.666666 42.666667 17.066667 42.666667 42.666666-17.066667 42.666667-42.666667 42.666667z"
                  fill="#444444" p-id="13429"></path></svg>
              </span>

            </div>
          </div>
          <div class="codeStyle-cl">
            <el-dropdown-menu slot="dropdown" style="height: 30%;overflow: auto;">
              <el-dropdown-item v-for="(item,index) of codeStyleList" :command="item" :key="index"
                                :style="codeStyle==item?'background-color: #eaeaea;':{}">
                {{ item }}
              </el-dropdown-item>
            </el-dropdown-menu>
          </div>
        </el-dropdown>
      </template>
    </mavon-editor>
    <div>
      <el-drawer
        title="常用快捷键"
        :modal="false"
        :visible.sync="shortcutKeyStatus">
        <div style="margin: 0 20px">
          <template>
            <el-table
              :data="shortcutList"
              border
              style="width: 100%">
              <el-table-column
                prop="key"
                label="快捷键"
                width="120">
              </el-table-column>
              <el-table-column
                prop="address"
                label="功能">
              </el-table-column>
            </el-table>
          </template>
        </div>
      </el-drawer>
    </div>
  </div>
</template>

<script>
import 'mavon-editor/dist/css/index.css'

export default {
  name: "MavonEditorModule",
  props: {
    // 接收值父组件传递值
    content: String,
    //主题
    theme: String,
    //编辑器高度
    mavonHeight: Number,
  },
  //官网：https://gitee.com/kongfanqun/mavonEditor#/kongfanqun/mavonEditor/blob/master/src/lib/core/hljs/lang.hljs.css.js
  data() {
    return {
      shortcutKeyStatus: false,
      editorValue: this.content != null ? this.content : "",
      shortcutList: [
        {key: "F8", address: "开启/关闭导航 ",},
        {key: "F9", address: "预览/编辑切换",},
        {key: "F10", address: "开启/关闭全屏",},
        {key: "F11", address: "开启/关闭阅读模式",},
        {key: "F12", address: "单栏/双栏切换",},
        {key: "CTRL + D", address: "删除选中行",},
        {key: "CTRL + S", address: "触发保存",},
        {key: "CTRL + Z", address: "上一步",},
        {key: "CTRL + Y", address: "下一步",},
        {key: "CTRL + BreakSpace", address: "清空编辑",},
        {key: "CTRL + SHIFT + C", address: "居中",},
        {key: "CTRL + SHIFT + L", address: "居左",},
        {key: "CTRL + SHIFT + R", address: "居右",},
      ],
      toolbars: {
        bold: true, // 粗体
        boxShadow: false, //边框阴影
        italic: true, // 斜体
        header: true, // 标题
        underline: true, // 下划线
        strikethrough: true, // 中划线
        mark: true, // 标记
        superscript: true, // 上角标
        subscript: true, // 下角标
        quote: true, // 引用
        ol: true, // 有序列表
        ul: true, // 无序列表
        link: true, // 链接
        imagelink: true, // 图片链接
        code: true, // code
        table: true, // 表格
        fullscreen: true, // 全屏编辑
        readmodel: true, // 沉浸式阅读
        htmlcode: false, // 展示html源码
        help: true, // 帮助
        /* 1.3.5 */
        undo: true, // 上一步
        redo: true, // 下一步
        trash: true, // 清空
        save: true, // 保存（触发events中的save事件）
        /* 1.4.2 */
        navigation: true, // 导航目录
        /* 2.1.8 */
        alignleft: true, // 左对齐
        aligncenter: true, // 居中
        alignright: true, // 右对齐
        /* 2.2.1 */
        subfield: true, // 单双栏模式
        preview: true, // 预览
      },
      //主题地址：https://gitee.com/kongfanqun/mavonEditor/blob/master/src/lib/core/hljs/lang.hljs.css.js
      codeStyleList: [
        'agate',
        'androidstudio',
        'arduino-light',
        'ascetic',
        'atom-one-dark',
        'atom-one-light',
        'brown-paper',
        'codepen-embed',
        'color-brewer',
        'docco',
        'dracula',
        'far',
        'foundation',
        'github',
        'googlecode',
        'grayscale',
        'hopscotch',
        'hybrid',
        'idea',
        'ir-black',
        'magula',
        'mono-blue',
        'monokai-sublime',
        'monokai',
        'obsidian',
        'ocean',
        'paraiso-dark',
        'paraiso-light',
        'pojoaque',
        'purebasic',
        'routeros',
        'school-book',
        'sunburst',
        'vs',
        'vs2015',
        'xcode',
        'xt256',
      ],
      //主题
      codeStyle: this.theme == null ? 'androidstudio' : this.theme,
    }
  },
  methods: {
    //主题选择触发事件
    handleTheme(command) {
      this.codeStyle = command;
      this.$modal.notify('已选择主题：' + command);
    },
    // 绑定@imgAdd event
    imgAdd(pos, $file) {
      // 第一步.将图片上传到服务器.
      let formData = new FormData();
      formData.append('file', $file)
      // this.img_file[pos] = $file;
      this.$API('/system/oss/upload', this.$post(), null, formData).then(item => {
        // 第二步.将返回的url替换到文本原位置![...](0) -> ![...](url)
        this.$refs.md.$img2Url(pos, item.data.url)
      })
    },
    change(value, render) {
      this.editorValue = render
    },
  },
  watch: {
    editorValue() {
      // console.log('html格式', this.$refs.md.d_render)
      // console.log('md格式', this.$refs.md.d_value)
      this.$emit('update:mdContent', this.$refs.md.d_value)
      this.$emit('update:theme', this.codeStyle)
    },
    codeStyle() {
      // console.log('主题：', this.codeStyle)
      this.$emit('update:theme', this.codeStyle)
    },
  },
};
</script>

<style>
@import url("components/css/mavon-editor-module.css");

/*
代码块
 */
.markdown-body .highlight pre, .markdown-body pre {
  padding: 6px !important;
  font-size: 100% !important;
}

/**
图片
 */
.markdown-body img {
  border: 1px solid #bdc3c7 !important;
  border-radius: 2px !important;
}

/*富文本*/
.markdown-body {
  z-index: 10 !important;
}

.el-collapse-item__header {
  line-height: 16px !important;
  border: none !important;
  height: initial;
}

.el-collapse-item__header {
  background-color: transparent;
}

.el-collapse {
  border: none;
}

/*伪元素设置，会导致轮播图按钮图标消失*/
/*.el-icon-arrow-right:before {*/
/*  content: none;*/
/*}*/

.el-collapse-item__wrap {
  border: none;
}

/*编辑器*/
.v-note-wrapper {
  min-width: 0px !important;
  min-height: 0px !important;
  border: none !important;
}

.v-note-wrapper .v-note-panel .v-note-show .v-show-content, .v-note-wrapper .v-note-panel .v-note-show .v-show-content-html {
  //padding: 10px 10px 0 10px !important;
  padding: 0px !important;
}
</style>
