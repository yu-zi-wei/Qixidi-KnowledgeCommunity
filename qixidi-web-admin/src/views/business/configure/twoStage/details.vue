<template>
  <div style="  background-color: #f4f5f5" v-loading="loading">
    <div style="width: 1200px;margin: auto;">
      <div style="text-align: center;padding: 20px 0">
        <span style="font-size: 40px;font-weight: bold" v-text="siteFileInfo.title"></span>
      </div>
      <mavon-editor class="markdown"
                    style="padding: 20px"
                    :value="siteFileInfo.content"
                    :subfield="false"
                    :defaultOpen="prop.defaultOpen"
                    :boxShadow="prop.boxShadow"
                    :toolbarsFlag="prop.toolbarsFlag"
                    :editable="prop.editable"
                    :codeStyle="siteFileInfo.theme"
                    :scrollStyle="prop.scrollStyle"
                    :navigation="false"
                    ref="markdown"
      />
    </div>
  </div>
</template>

<script>
import {getSiteFile} from "@/api/business/configure/siteFile";

export default {
  name: "details",
  computed: {
    prop() {
      let data = {
        subfield: false,// 单双栏模式
        defaultOpen: 'preview',//edit： 默认展示编辑区域 ， preview： 默认展示预览区域
        editable: false,
        toolbarsFlag: false,
        scrollStyle: true,
        boxShadow: false,  //边框阴影
      }
      return data
    }
  },
  data() {
    return {
      loading: false,
      siteFileInfo: {
        content: null,
        theme: null,
      },
    }
  },
  methods: {
    getInfo() {
      this.loading = true;
      let id = this.$route.query.id
      getSiteFile(id).then(res => {
        this.siteFileInfo = res.data;
        this.loading = false;
        console.log("this.siteFileInfo:", this.siteFileInfo)
      });
    },
  },
  mounted() {
    this.getInfo();
  }
}
</script>

<style scoped>

.v-note-wrapper {
  border: none;
}

/*富文本*/
.markdown-body {
  z-index: 10;
}
</style>
