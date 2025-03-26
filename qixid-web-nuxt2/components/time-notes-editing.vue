<template>
  <div>
    <el-dialog
      title="时光小记"
      :visible.sync="timeNotesDialogVisible"
      :close-on-click-modal="false"
      :close-on-press-escape="false"
      :before-close="handleClose"
      width="45%">
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
          <el-input
            type="textarea"
            prefix-icon="el-icon-link el-input__icon"
            :rows="4"
            placeholder="小记Title"
            v-model="timeNotes.title">
          </el-input>
        </div>
        <el-collapse v-model="activeName" accordion>
          <el-collapse-item :name="timeNotes.content==null?'0':'1'">
            <template slot="title">
              <div class="ml-10 mb-10">
                <span class="text-underline font-s-14">编写详情</span>
                <span class="font-s-13 color-grey-2">点击展开</span>
                <svg t="1742898375461" class="icon" viewBox="0 0 1024 1024" version="1.1"
                     xmlns="http://www.w3.org/2000/svg" p-id="44703" width="12" height="12">
                  <path
                    d="M966.4 323.2c-9.6-9.6-25.6-9.6-35.2 0l-416 416-425.6-416c-9.6-9.6-25.6-9.6-35.2 0-9.6 9.6-9.6 25.6 0 35.2l441.6 432c9.6 9.6 25.6 9.6 35.2 0l435.2-432C976 345.6 976 332.8 966.4 323.2z"
                    p-id="44704"></path>
                </svg>
              </div>
            </template>
            <VditorMd :height="'400px'"
                      :outline="false"
                      :placeholder="'时光小记详情...'"
                      :vditorId="'articlePublish'"
                      :mdContent.sync="timeNotes.content"
                      :content="timeNotes.content"
                      v-if="timeNotesDialogVisible"></VditorMd>
          </el-collapse-item>
        </el-collapse>
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
import VditorMd from "./Vditor-md.vue";

export default {
  name: "timeNotesEditing",
  components: {VditorMd},
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
    return {
      activeName: '1'
    }
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

</style>
