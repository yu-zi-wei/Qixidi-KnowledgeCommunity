<template>
  <div class="mt-20">
    <time-notes-list :query-params="queryParams"></time-notes-list>
  </div>
</template>

<script>

import TimeNotesList from "../../../components/timeNotes/time-notes-list.vue";

export default {
  name: "article",
  components: {TimeNotesList},
  data() {
    return {
      queryParams: {
        pageNum: 1,
        pageSize: 20,
        title: this.$route.query.data,
      },
      reloadNum: 1,
    }
  },
  watch: {
    $route() {
      this.queryParams.title = this.$route.query.data;
      this.reloadNum = this.reloadNum + 1;
    },
  },
}
</script>

<style scoped>

</style>
