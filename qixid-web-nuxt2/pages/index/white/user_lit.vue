<template>
  <div class="mt-30">
    <div class="white-user-title font-s-17">
      站点中状态正常的所有用户......
    </div>
    <el-skeleton style="padding: 20px 10px" :rows="6" animated v-if="initialLoading"/>
    <div class="flex-space-between flex-wrap-wrap white-user-admin" v-show="!initialLoading">
      <div v-for="(item,index) in userList" :key="index" class="white-user-item" :ref="`allUserItem${index}`">
        <div class="flex-left">
          <div>
            <el-avatar :size="60" v-if="item.avatar" :src="item.avatar" fit="cover"></el-avatar>
            <el-avatar :size="60" v-else src="/img/tx.jpg" fit="cover"></el-avatar>
          </div>
          <div class="ml-10">
            <nuxt-link class="text-underline-hover"
                       :to="'/user_home/article?uuid='+$base64.encode(item.uuid)" target="_blank">
              <p class="font-s-18 line-height-28 font-bold-s">{{ item.nickname }}</p>
            </nuxt-link>
            <p class="font-s-16 line-height-28 color-grey">{{ item.occupation ? item.occupation : '职业 --' }}</p>
          </div>
        </div>
        <div class="flex-space-between flex-wrap-wrap mt-10">
          <div class="flex-8 mr-10">
            <p class="font-s-16">
              <svg t="1761648078672" class="icon svg-translateY-7" viewBox="0 0 1024 1024" version="1.1"
                   xmlns="http://www.w3.org/2000/svg" p-id="18045" width="30" height="30">
                <path
                  d="M413.930667 616.533333Q461.013333 597.333333 512 597.333333a21.333333 21.333333 0 1 0 0-42.666666q-59.349333 0-114.197333 22.357333-54.869333 22.4-96.874667 63.744-42.048 41.408-64.810667 95.530667-12.693333 30.165333-16.789333 45.44Q213.333333 804.16 213.333333 832a21.333333 21.333333 0 1 0 42.666667 0q0-22.229333 4.544-39.253333 3.349333-12.437333 14.890667-39.893334 19.456-46.250667 55.424-81.664 36.010667-35.456 83.072-54.656z"
                  fill="#0097e6" p-id="18046"></path>
                <path
                  d="M746.666667 373.333333C746.666667 497.045333 646.378667 597.333333 522.666667 597.333333S298.666667 497.045333 298.666667 373.333333 398.954667 149.333333 522.666667 149.333333 746.666667 249.621333 746.666667 373.333333z m-42.666667 0q0-34.581333-12.736-66.752-13.824-34.922667-40.384-61.461333-26.538667-26.56-61.461333-40.384Q557.226667 192 522.666667 192t-66.752 12.736q-34.922667 13.824-61.461334 40.384-26.56 26.538667-40.384 61.461333Q341.333333 338.773333 341.333333 373.333333t12.736 66.752q13.824 34.922667 40.384 61.461334 26.538667 26.56 61.461334 40.384Q488.106667 554.666667 522.666667 554.666667t66.752-12.736q34.922667-13.824 61.461333-40.384 26.56-26.538667 40.384-61.461334Q704 407.893333 704 373.333333z"
                  fill="#0097e6" p-id="18047"></path>
                <path
                  d="M661.333333 661.333333a21.333333 21.333333 0 0 1 21.333334-21.333333h128a21.333333 21.333333 0 0 1 0 42.666667h-128a21.333333 21.333333 0 0 1-21.333334-21.333334zM576 746.666667a21.333333 21.333333 0 0 1 21.333333-21.333334h213.333334a21.333333 21.333333 0 1 1 0 42.666667H597.333333a21.333333 21.333333 0 0 1-21.333333-21.333333zM576 832a21.333333 21.333333 0 0 1 21.333333-21.333333h213.333334a21.333333 21.333333 0 1 1 0 42.666666H597.333333a21.333333 21.333333 0 0 1-21.333333-21.333333z"
                  fill="#fdcb6e" p-id="18048"></path>
              </svg>
              基本信息
            </p>
            <hr class="hr-item mt-8"/>
            <div class="mt-8 font-s-16 line-height-28 color-grey">
              <div class="color-grey5" :title="$utils.parseTime(item.createTime, '{y}-{m}-{d} {h}:{i}')">
                加入时间：{{ $utils.parseTime(item.createTime, '{y}-{m}-{d}') }}
              </div>
              <div class="">用户来源：
                <el-tag size="mini" type="danger">{{ item.source }}</el-tag>
              </div>
              <div class="">用户类型：{{ item.roleId == 1 ? '普通用户' : item.roleId == 2 ? '创作者' : '管理员' }}</div>
              <div class="">个人简介：{{ item.introduce ? item.introduce : '--' }}</div>
              <div class="">备注：{{ item.remark ? item.remark : '--' }}</div>

            </div>
          </div>
          <div class="flex-4">
            <p class="font-s-16">
              <svg t="1761647955636" class="icon svg-translateY-7" viewBox="0 0 1024 1024" version="1.1"
                   xmlns="http://www.w3.org/2000/svg" p-id="14048" width="30" height="30">
                <path
                  d="M527.872 271.552a192.448 192.448 0 0 0-192.192 192.192c0 40.832 12.928 78.72 34.816 109.952a192.128 192.128 0 0 0 157.44 82.24 192.512 192.512 0 0 0 192.256-192.32 192.512 192.512 0 0 0-192.32-192.064z"
                  fill="#FD9303" p-id="14049"></path>
                <path
                  d="M527.872 595.904a132.352 132.352 0 0 1-132.096-132.16c0-72.832 59.264-132.224 132.224-132.224 72.96 0 132.16 59.392 132.16 132.224A132.48 132.48 0 0 1 527.872 595.84z"
                  fill="#FFC109" p-id="14050"></path>
                <path d="M495.168 614.72l167.936-93.056 155.136 279.872-103.68 11.008-64.256 82.048z" fill="#D8D8D8"
                      p-id="14051"></path>
                <path d="M562.24 614.72L394.304 521.664l-155.136 279.872 103.68 11.008 64.256 82.048z" fill="#D8D8D8"
                      p-id="14052"></path>
                <path d="M528.704 452.16m-288 0a288 288 0 1 0 576 0 288 288 0 1 0-576 0Z" fill="#FFD409"
                      p-id="14053"></path>
                <path
                  d="M528.704 228.16a224 224 0 1 1 0 448 224 224 0 0 1 0-448z m0 32a192 192 0 1 0 0 384 192 192 0 0 0 0-384z"
                  fill="#FFA121" p-id="14054"></path>
                <path
                  d="M528.704 532.16l-94.08 49.408 18.048-104.704-76.16-74.176 105.216-15.296 46.976-95.232 47.04 95.232 105.152 15.36L604.8 476.8l17.92 104.704z"
                  fill="#FD9303" p-id="14055"></path>
              </svg>
              荣誉/贡献
            </p>
            <hr class="hr-item mt-8"/>
            <div class="mt-8 font-s-16 line-height-28 color-grey">
              <div class="">文章数：{{ item.articleCount }}</div>
              <div class="">随笔数：{{ item.dictumCount }}</div>
              <div class="">小记数：{{ item.timeNotesCount }}</div>
              <div class="">专栏数：{{ item.specialColumnCount }}</div>
              <div class="">专辑数：{{ item.albumCount }}</div>
              <div class="">获赞数：{{ item.fansFabulousCount }}</div>
              <div class="">关注数：{{ item.followCount }}</div>
              <div class="">关注Ta：{{ item.fansFollowCount }}</div>
              <div class="">评论数：{{ item.commentCount }}</div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>


<script>
import {createAnimator} from '~/plugins/animationUtils'

export default {
  name: "whiteUserList",
  data() {
    return {
      initialLoading: true,
      userList: [],
      animator: null, // 动画器实例
    }
  },
  methods: {
    fdUserLists() {
      this.$API("/white/userAllList", "get").then(res => {
        this.userList = res.rows;
        this.initialLoading = false;
        this.animator.triggerAllItemsAnimation(this.userList, 'allUserItem');
      })
    }
  },
  mounted() {
    // 初始化动画器
    this.animator = createAnimator(this, 'allUser');
    this.fdUserLists();
  }
}
</script>

<style scoped>
.white-user-item {
  padding: 10px;
  box-shadow: 0 6px 8px 0 #ecf0f1;
  width: 30%;
  margin-bottom: 35px;
  border-radius: 6px;
  border: 1px solid #ffffff;
  transition: all 0.4s linear 0s;
}

.white-user-item:hover {
  border: 1px solid #bdc3c7;
}

.white-user-admin {

}

.white-user-title {
  background-color: #F3F3F3;
  padding: 15px;
  margin-bottom: 30px;
  border-radius: 4px;
  font-style: italic;
}

@media (max-width: 510px) {
  .white-user-item {
    width: 100%;
  }

  .white-user-admin {
    padding: 0 15px;
  }
}
</style>
