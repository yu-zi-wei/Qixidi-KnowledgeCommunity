<template>
  <div>
    <mavon-editor
      v-model="article.html"
      :placeholder="'开始编写 ···'"
      ref="md"
      class="md"
      @imgAdd="imgAdd"
      @change="change"
      :ishljs="true"
      :navigation="true"
      :toolbarsBackground="'#f9f9f9'"
      style="height: calc(100vh - 50px)">
      <!-- 右工具栏后加入自定义按钮  -->
      <template slot="right-toolbar-after">
        <el-button
          type="button"
          @click="shortcutKeyStatus = true"
          aria-hidden="true"
          title="快捷键">
          <svg-keyboard-one theme="outline" size="24" fill="#333"/>
        </el-button>
        <el-button size="mini" circle title="快捷键" @click="shortcutKeyStatus = true">
          <svg-keyboard-one theme="outline" size="24" fill="#333"/>
        </el-button>
      </template>
    </mavon-editor>
    <div>
      <el-drawer
        title="常用快捷键"
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
                prop="keycode"
                label="keycode"
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
import upload from "@/components/amap/index.vue";
import 'mavon-editor/dist/css/index.css'

export default {
  name: "BytembEditer",
  components: {
    upload,
  },
  data() {
    return {
      shortcutKeyStatus: false,
      article: {
        //是否有封面（1，一张封面，2：三张封面，3：无封面）
        isCover: '3',
        articleTitle: '',//标题
        articleCover: null,//封面
        articleAbstract: null,//摘要
        html: null,
        articleContent: '',//内容
        type: '1',//文章类型（1：原创，2：转载）
        reprintUrl: null,//转载地址
        specialId: null,//专栏id
        babelId: null,//标签（多个以逗号隔开）
        isPublic: '1',//是否公开（1：公开，2：不公开）
      },
      shortcutList: [
        {
          key: "F8",
          keycode: "119",
          address: "开启/关闭导航 ",
        },
        {
          key: "F9",
          keycode: "120",
          address: "预览/编辑切换",
        },
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
    }
  },
  methods: {
    // 绑定@imgAdd event
    imgAdd(pos, $file) {
      console.log("=========>添加图片")
      // 第一步.将图片上传到服务器.
      let formData = new FormData();
      formData.append('file', $file)
      // this.img_file[pos] = $file;
      uploadImg(formData).then(item => {
        console.log("地址" + item);
        //   let _res = res.data;
        //   // 第二步.将返回的url替换到文本原位置![...](0) -> ![...](url)
        this.$refs.md.$img2Url(pos, item)
      })
    },
    change(value, render) {
      this.article.articleContent = render
    },
  }
};
</script>

<style>

</style>
