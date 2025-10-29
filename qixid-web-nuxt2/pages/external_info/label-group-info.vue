<template>
  <div class="module-main-2">
    <div class="label-group-info-title">
      <el-skeleton style="padding: 20px 10px" :rows="3" animated v-if="initialLoading"/>
      <div v-show="!initialLoading" class="margin-auto">
        <p class="font-s-32 font-bold text-center">{{ labelGroupInfo.groupingName }}</p>
        <div class="font-s-16 line-height-24 text-center mt-6">
          <span>文章数：{{ labelGroupInfo.articleNumber }}</span>
        </div>
      </div>
    </div>
    <div>
      <ArticleSearchList :query-params="queryParams"></ArticleSearchList>
    </div>
  </div>
</template>

<script>
import ArticleSearchList from "../../components/article/article-search-list.vue";

export default {
  name: "labelGroupInfo",
  components: {ArticleSearchList},
  data() {
    return {
      queryParams: {
        pageNum: 1,
        pageSize: 20,
        groupingId: this.$route.query.data,
      },
      labelGroupInfo: {},
      initialLoading: true,
    }
  },
  methods: {
    LabelGroupingInfos() {
      this.$API("/white/label/grouping/info/" + this.queryParams.groupingId, "get").then(res => {
        this.labelGroupInfo = res.data;
        this.initialLoading = false;
      })
    },
  },
  mounted() {
    this.LabelGroupingInfos();
  }
}
</script>

<style scoped>
.label-group-info-title {
  background: rgb(141, 46, 205, 0.3);
  background: linear-gradient(90deg, rgba(141, 46, 205, 0.3) 0%, rgba(29, 227, 253, 0.3) 50%, rgba(252, 69, 249, 0.3) 100%);
  padding: 40px 0;
  margin-bottom: 20px;
  margin-top: 20px;
  border-radius: 4px;
}
</style>
