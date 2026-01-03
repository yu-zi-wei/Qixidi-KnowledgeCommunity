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
      this.$api.whiteApi.getUserAllList().then(res => {
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
