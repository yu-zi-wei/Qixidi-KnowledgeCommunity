<template>
  <div>
    <ArticleSearchList :query-params="queryParams" :reload="reloadNum"></ArticleSearchList>
  </div>
</template>

<script>

import ArticleSearchList from "../../../components/article/article-search-list.vue";

export default {
  name: "data",
  components: {ArticleSearchList},
  data() {
    return {
      queryParams: {
        pageNum: 1,
        pageSize: 20,
        articleTitle: this.$route.query.data,
      },
      reloadNum: 1,
    }
  },
  watch: {
    $route() {
      this.queryParams.articleTitle = this.$route.query.data;
      this.reloadNum = this.reloadNum + 1;
    },
  },
}
</script>

<style scoped>

</style>
