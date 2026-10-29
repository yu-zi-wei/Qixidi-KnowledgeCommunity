<template>
  <div>
    <mavon-editor
      v-model="editorValue"
      :codeStyle="codeStyle"
      :placeholder="'开始编写 ···'"
      ref="md"
      class="md"
      @imgAdd="imgAdd"
      @change="change"
      :ishljs="true"
      :navigation="false"
      :toolbarsBackground="'#f9f9f9'"
      style="height: calc(100vh - 60px);border: none">
      <!-- 右工具栏后加入自定义按钮  -->
      <template slot="right-toolbar-after">
        <button
          type="button"
          @click="shortcutKeyStatus = true"
          class="op-icon fa fa-keyboard-o"
          aria-hidden="true"
          title="快捷键"
        ></button>
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
import {fileUpload} from "@/api/business/article/articleInform";

export default {
  name: "index",
  props: {
    // 接收值父组件传递值
    content: String
  },
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
        htmlcode: true, // 展示html源码
        help: true, // 帮助
        /* 1.3.5 */
        undo: true, // 上一步
        redo: true, // 下一步
        trash: true, // 清空
        save: false, // 保存（触发events中的save事件）
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
      //主题
      codeStyle: 'androidstudio',
    }
  },
  methods: {
    // 绑定@imgAdd event
    imgAdd(pos, $file) {
      // 第一步.将图片上传到服务器.
      let formData = new FormData();
      formData.append('file', $file)
      // this.img_file[pos] = $file;
      fileUpload(formData).then(item => {
        // 第二步.将返回的url替换到文本原位置![...](0) -> ![...](url)
        this.$refs.md.$img2Url(pos, item.data.url)
      })
    },
    change(value, render) {
      this.editorValue = render
    },
  },
  watch: {
    editorValue: function (newNum, oldNum) {
      // console.log('html格式', this.$refs.md.d_render)
      // console.log('md格式', this.$refs.md.d_value)
      this.$emit('update:mdContent', this.$refs.md.d_value)
      this.$emit('update:theme', this.codeStyle)
    }
  },
};
</script>

<style>

</style>
