<template>
  <div style="width: 1000px;margin: auto;">
    <div class="flex-left">
      <div class="flex-9 background-color-fefefe">
        <div style="padding: 60px 60px 100px 60px">
          <ai-editor-module :ai-editor-id="'aiEditor-dictum'" :content="dictumInfo.content"
                            :editable="false"></ai-editor-module>
          <div class="flex-right mt-20">
            <div
              v-if="(dictumInfo.worksName!=null && dictumInfo.worksName!='')||(dictumInfo.author!=null && dictumInfo.author!='')"
              class="color-grey-2 font-s-14">——
              <span v-if="dictumInfo.author!=null" class="cursor-pointer text-underline-hover" title="作者"
                    @click="jumpUrlBaidu('www.baidu.com',dictumInfo.author)">
                {{ dictumInfo.author }}
                </span>
              <span v-if="dictumInfo.worksName!=null && dictumInfo.worksName!=''" title="名言出处">
                《<span class="color-ffc312 cursor-pointer text-underline-hover"
                       @click="jumpUrlBaidu('www.baidu.com',dictumInfo.worksName)">{{ dictumInfo.worksName }}</span>》
                </span>
            </div>
          </div>
        </div>
      </div>
      <div class="flex-2">
        <div class="padding-10 dictum-details-right">
          <div class="dictum-re-name-cl mt-20 mb-6">作者</div>
          <div class="flex-left">
            <div>
              <nuxt-link :to="'/user_home/article?uuid='+$base64.encode(dictumInfo.tripartiteUser.uuid)"
                         target="_blank">
                <el-avatar v-if="dictumInfo.tripartiteUser.avatar" :src="dictumInfo.tripartiteUser.avatar"/>
                <el-avatar v-else src="/img/tx.jpg"></el-avatar>
              </nuxt-link>
            </div>
            <div class="ml-6 mt-2">
              <nuxt-link :to="'/user_home/article?uuid='+$base64.encode(dictumInfo.tripartiteUser.uuid)"
                         target="_blank">
                <p class="text-underline-hover">{{ dictumInfo.tripartiteUser.nickname }}</p>
              </nuxt-link>
              <p class="font-s-13 color-grey-2 mt-2">{{ dictumInfo.tripartiteUser.occupation }}</p>
            </div>
          </div>
          <!--          作者数据-->
          <div class="flex-space-between mt-10 font-s-14" style="padding: 0 30px">
            <div class="text-center">
              <p>名言</p>
              <p class="mt-4 font-bold-s">{{ dictumInfo.tripartiteUser.dictumCount }}</p>
            </div>
            <div class="text-center">
              <p>专辑</p>
              <p class="mt-4 font-bold-s">{{ dictumInfo.tripartiteUser.albumCount }}</p>
            </div>
            <div class="text-center">
              <p>关注</p>
              <P class="mt-4 font-bold-s">{{ dictumInfo.tripartiteUser.fansFollowCount }}</P>
            </div>
          </div>
          <div class="dictum-re-name-cl mt-40 mb-6">更多...</div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import AiEditorModule from "../../../../components/AiEditor-module.vue";

export default {
  name: "dictumDetails",
  components: {AiEditorModule},
  head() {
    return {
      title: "名言详情" + ' - ' + process.env.PROJECT_NAME,
      meta: [
        {
          hid: 'description',
          name: 'description',
          content: this.dictumInfo.content
        },
        {hid: 'keywords', name: 'keywords', content: this.dictumInfo.content}
      ]
    }
  },
  data() {
    return {
      dictumInfo: {
        tripartiteUser: {}
      },
      id: null,
    }
  },
  async asyncData({app, params, store}) {
    const id = Buffer.from(params.id, 'base64').toString('utf-8');
    let token = store.state.token;
    const https = require('https');
    const response = await fetch(`${process.env.SERVICE_PROTOCOL}${process.env.SERVER_URL}/white/dictum/info/${id}`, {
      headers: {
        'Authorization': 'Bearer ' + token
      },
      //不做https校验，如果你的https是被信任的建议注释该代码，因为http是不安全的
      agent: new https.Agent({rejectUnauthorized: false})
    });
    const data = await response.json();
    return {
      dictumInfo: data.data,
      id: id,
    }
  },
  methods: {
    jumpUrlBaidu(url, keywords) {
      let path = window.location.protocol + '//' + url + '/s?wd=' + keywords
      window.open(path, '_blank');
    },
  },
  mounted() {

  }
}
</script>
<style>
.dictum-re-name-cl {
  padding-left: 6px;
  font-size: 16px;
  margin-bottom: 20px;
  border-left: 2px solid #FB7299;
}

.dictum-details-right {
  width: 260px;
  background-color: #FEFEFE;
  margin-left: 20px;
}
</style>
