<template>
  <div class="mt-30">
    <div class="time-notes-details">
      <div class="mood-notes-details-id-title">
        <h2 class="font-s-24 line-height-28">{{ moodNotes.title }}</h2>
        <div class="flex-space-between mt-10">
          <div class="font-s-14 color-grey-2">
            <svg t="1741416645564" class="icon-theme-1 icon-size-16 svg-translateY-3" viewBox="0 0 1024 1024"
                 version="1.1" xmlns="http://www.w3.org/2000/svg"
                 p-id="38483">
              <path
                d="M512 64C264.946 64 64 264.946 64 512s200.946 448 448 448 448-200.946 448-448S759.054 64 512 64z m0 831.828c-211.613 0-383.828-172.215-383.828-383.828S300.216 128.172 512 128.172c211.785 0 383.828 172.044 383.828 383.828 0 211.785-172.215 383.828-383.828 383.828z"
                p-id="38484"></path>
              <path
                d="M672.172 512h-160V288.173c0-17.72-14.28-32.172-32-32.172s-32 14.451-32 32.172v256c0 17.719 14.28 31.999 32 31.999h192c17.72 0 32.173-14.28 32.173-31.999C704.172 526.28 689.721 512 672.172 512z"
                p-id="38485"></path>
            </svg>
            {{ moodNotes.recordTime }}
          </div>
          <div class="_module_hiding" v-if="moodNotes.isAuthor==0" @click="update">
            <svg t="1742972825057" class="icon-theme-2 icon-hover cursor-pointer icon-size-18 svg-translateY-3"
                 viewBox="0 0 1024 1024"
                 version="1.1"
                 xmlns="http://www.w3.org/2000/svg" p-id="46397">
              <path
                d="M526.41 117.029v58.514a7.314 7.314 0 0 1-7.315 7.314H219.429a36.571 36.571 0 0 0-35.987 29.989l-0.585 6.583V804.57a36.571 36.571 0 0 0 29.989 35.987l6.583 0.585H804.57a36.571 36.571 0 0 0 35.987-29.989l0.585-6.583v-317.44a7.314 7.314 0 0 1 7.314-7.314h58.514a7.314 7.314 0 0 1 7.315 7.314v317.44a109.714 109.714 0 0 1-99.182 109.203l-10.533 0.512H219.43a109.714 109.714 0 0 1-109.203-99.182l-0.512-10.533V219.43a109.714 109.714 0 0 1 99.182-109.203l10.533-0.512h299.666a7.314 7.314 0 0 1 7.314 7.315z m307.345 31.817l41.4 41.399a7.314 7.314 0 0 1 0 10.313L419.985 655.726a7.314 7.314 0 0 1-10.313 0l-41.399-41.4a7.314 7.314 0 0 1 0-10.312l455.168-455.168a7.314 7.314 0 0 1 10.313 0z"
                p-id="46398"></path>
            </svg>
          </div>
        </div>
      </div>
      <div>
        <vditor-preview v-if="moodNotes.content!=null" :id="'moodNotes-content-'+moodNotes.id"
                        :content="moodNotes.content"></vditor-preview>
      </div>
    </div>
    <time-notes-editing :time-notes-dialog-visible="timeNotesDialogVisible"
                        :time-notes="moodNotes"
                        @timeNotesDialogVisibleMethod="timeNotesDialogVisibleMethod"></time-notes-editing>
  </div>
</template>

<script>
import VditorPreview from "../../../../components/vditorComponents/Vditor-preview.vue";
import TimeNotesEditing from "../../../../components/timeNotes/time-notes-editing.vue";

export default {
  name: "details",
  components: {TimeNotesEditing, VditorPreview},
  head() {
    return {
      title: this.moodNotes.title + ' - ' + process.env.PROJECT_NAME,
      meta: [
        {
          hid: 'description',
          name: 'description',
          content: this.moodNotes.title
        },
        {hid: 'keywords', name: 'keywords', content: this.moodNotes.content}
      ]
    }
  },
  data() {
    return {
      moodNotes: {},
      timeNotesDialogVisible: false,
    }
  },
  async asyncData({app, params, store}) {
    const id = Buffer.from(params.id, 'base64').toString('utf-8');
    let token = store.state.token;
    const https = require('https');
    const response = await fetch(`${process.env.SERVICE_PROTOCOL}${process.env.SERVER_URL}/white/time/notes/getInfo/${id}`, {
      headers: {
        'Authorization': 'Bearer ' + token
      },
      //不做https校验，如果你的https是被信任的建议注释该代码，因为http是不安全的
      agent: new https.Agent({rejectUnauthorized: false})
    });
    const data = await response.json();
    return {
      moodNotes: data.data,
    }
  },
  methods: {
    update() {
      this.timeNotesDialogVisible = true;
    },
    timeNotesDialogVisibleMethod(val) {
      this.timeNotesDialogVisible = val
    },
  }
}
</script>


<style>
.time-notes-details {
  width: 940px;
  margin: auto
}

.mood-notes-details-id-title {
  padding: 10px;
  border-radius: 4px;
  margin-bottom: 20px;
  font-size: 24px;
  line-height: 30px;
  border-bottom: 1px solid #bdc3c7;
}

@media (max-width: 510px) {
  .time-notes-details {
    width: 100%;
    margin: auto;
  }
}
</style>
