<template>
  <div class="mt-30">
    <div class="time-notes-model">
      <div class="mood-title flex-space-between">
        <div class="font-s-16">
          记录每天小心情......
        </div>
        <div>
          <el-popover
            placement="top-start"
            title="展示规则"
            width="200"
            trigger="hover"
            content="在基于所有用户均可记录 “时光小记” 的前提下，系统将根据用户[登录状态]提供差异化展示：已登录用户仅可查看自己所记录的 “时光小记”，而未登录用户则能够浏览全部用户的 “时光小记” 内容。">
            <div class="font-s-14 color-grey cursor-pointer hover-cl" slot="reference">展示规则！</div>
          </el-popover>
        </div>
      </div>
      <time-notes-list :query-params="queryParams"></time-notes-list>
    </div>

  </div>
</template>

<script>
import TimeNotesList from "../../../components/timeNotes/time-notes-list.vue";

export default {
  name: "moodNotesIndex",
  head: {
    title: `时光小记 - ${process.env.PROJECT_NAME}`,
  },
  components: {TimeNotesList},
  data() {
    return {
      queryParams: {
        pageNum: 1,
        pageSize: 40,
        title: null,
      },
    }
  },
  methods: {},
  mounted() {
  },
}
</script>

<style>
.time-notes-model {
  width: 1000px;
  margin: auto
}

@media (max-width: 510px) {
  .time-notes-model {
    width: 100%;
    margin: auto;
  }
}
</style>
