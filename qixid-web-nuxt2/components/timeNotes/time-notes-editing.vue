<template>
  <div>
    <el-dialog
      title="时光小记"
      :visible.sync="timeNotesDialogVisible"
      :close-on-click-modal="false"
      :close-on-press-escape="false"
      :before-close="handleClose"
      width="50%">
      <div v-if="timeNotesDialogVisible">
        <div class="mb-20" style="background-color: #F3F3F3;padding: 15px;border-radius: 4px">
          <p class="mb-10">记录时间:</p>
          <el-date-picker
            v-model="timeNotes.recordTime"
            align="right"
            type="date"
            value-format="yyyy-MM-dd"
            placeholder="记录时间">
          </el-date-picker>
          <p class="mb-10 mt-20">Title:</p>
          <div style="padding: 0px 6px 6px 6px;border: 1px solid #e2e2e5;border-radius: 4px;background: #ffffff">
            <emoji-module :content.sync="timeNotes.title"
                          id="time-notes-editing"></emoji-module>
            <textarea style="white-space:pre-line" id="time-notes-editing"
                      v-model="timeNotes.title"
                      placeholder="小记Title..."
                      rows="4" class="time-notes-editing-textarea"></textarea>
          </div>
          <div class="font-s-14 mt-20 mb-10">小记详情：</div>
          <div style="margin: 2px">
            <VditorMd :height="'400px'"
                      :outline="false"
                      :placeholder="'时光小记详情...'"
                      :vditorId="'articlePublish'"
                      :mdContent.sync="timeNotes.content"
                      :content="timeNotes.content"
                      v-if="timeNotesDialogVisible"></VditorMd>
          </div>
        </div>
      </div>
      <div class="text-right mt-10">
        <el-button type="primary" @click="jottingsPublish">
          {{ timeNotes.id == null ? '发 布' : '保 存' }}
        </el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import VditorMd from "../vditorComponents/Vditor-md.vue";
import EmojiModule from "../emoji-module.vue";

export default {
  name: "timeNotesEditing",
  components: {EmojiModule, VditorMd},
  props: {
    timeNotesDialogVisible: {
      type: Boolean,
      default: false,
    },
    timeNotes: {
      type: Object,
      default: function () {
        return {
          id: null,
          recordTime: '',
          title: '',
          content: null,
        }
      }
    }
  },
  data() {
    return {}
  },
  methods: {
    handleClose() {
      //触发父组件事件（取消弹框)
      this.$emit('timeNotesDialogVisibleMethod', false)
    },
    jottingsPublish() {
      if (this.timeNotes.id != null) {
        this.$API("/frontDesk/time/notes/update", this.$post(), null, this.timeNotes).then(res => {
          if (res.code === 200) {
            this.$modal.notifySuccess("更新成功");
            this.jottingsDialogVisible = false
            //发布后钩子
            this.$emit('success')
            //触发父组件事件（取消弹框)
            this.$emit('timeNotesDialogVisibleMethod', false)
          }
        });
      } else {
        this.$API("/frontDesk/time/notes/add", this.$post(), null, this.timeNotes).then(res => {
          if (res.code === 200) {
            this.$modal.notifySuccess("发布成功");
            this.jottingsDialogVisible = false
            //发布后钩子
            this.$emit('success')
            //触发父组件事件（取消弹框)
            this.$emit('timeNotesDialogVisibleMethod', false)
          }
        });
      }
    },
  }
}

</script>
<style scoped>
.time-notes-editing-textarea {
  width: 100%;
  white-space: pre-wrap;
  font-size: 16px;
  border: unset;
  padding: unset;
  margin: unset;
  resize: none;
}

.time-notes-editing-textarea::-webkit-scrollbar {
  width: 6px;
  height: 6px;
  background-color: #ced6e0;
  border-radius: 4px;
}

.time-notes-editing-textarea::-webkit-scrollbar-track {
  background: #FFFFFF;
  border-radius: 4px;
}

.time-notes-editing-textarea::-webkit-scrollbar-thumb {
  background: #ced6e0;
  border-radius: 4px;
}
</style>
