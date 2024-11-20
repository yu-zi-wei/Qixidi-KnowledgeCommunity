<template>
  <div style="background-color: #fefefe;padding: 20px;border-radius: 4px">
    <template>
      <el-tabs v-model="activeName">
        <el-tab-pane label="A币收益" name="A币收益">
          <div class="mt-10">
            <el-collapse accordion :accordion="true">
              <el-collapse-item>
                <template slot="title">
                  <div class="font-bold font-s-14 color-grey4">什么是A币</div>
                </template>
                <div class="font-s-14 color-grey4">
                  A币是{{ websiteName }} 提供的一种虚拟货币。
                </div>
              </el-collapse-item>
              <el-collapse-item>
                <template slot="title">
                  <div class="font-bold font-s-14 color-grey4"> A币有什么作用？</div>
                </template>
                <div class="font-s-14 color-grey4">
                  1. A币 可用于兑换社区准备的惊喜好物。<br/>
                  2. A币 可用于兑换社区提供的内部道具，如补签卡......
                </div>
              </el-collapse-item>
              <el-collapse-item>
                <template slot="title">
                  <div class="font-bold font-s-14 color-grey4"> A币如何获取</div>
                </template>
                <div class="font-s-14 color-grey4">
                  获取A币的方式有很多种，如下<br/>
                  1. 每日签到，每天签到可获得一定的A币。<br/>
                  2. 充值，可在个人信息页面通过充值获取更多的 A币。
                </div>
              </el-collapse-item>

            </el-collapse>
          </div>
        </el-tab-pane>

        <el-tab-pane label="内容审核" name="内容审核">
          <div class="mt-10">
            <el-collapse accordion :accordion="true">
              <el-collapse-item>
                <template slot="title">
                  <div class="font-bold font-s-14 color-grey4">
                    为什么会有内容审核 <i class="header-icon el-icon-info"></i>
                  </div>
                </template>
                <div class="font-s-14 color-grey4">
                  1. 根据国家相关法律法规规定，保障平台内容安全：过滤涉黄、涉暴、涉毒、赌博等安全底线问题。<br/>
                  2. 保障用户体验，有助于提升平台长期价值：打击低俗、辱骂、广告等低质内容。
                </div>
              </el-collapse-item>

              <el-collapse-item>
                <template slot="title">
                  <div class="font-bold font-s-14 color-grey4">什么样的内容需要审核 ？</div>
                </template>
                <div class="font-s-14 color-grey4">
                  按照相关法律法规要求，用户在{{ websiteName }}平台发布和传播的内容均需要审核，包括但不限于；<br/>
                  1. 资料：用户头像昵称、职位、个人介绍等......<br/>
                  2. 所有写入内容：文章、图片、评论......
                </div>
              </el-collapse-item>

              <el-collapse-item>
                <template slot="title">
                  <div class="font-bold font-s-14 color-grey4">
                    有几种审核状态
                  </div>
                </template>
                <div class="font-s-14 color-grey4">
                  1. 审核中：内容发布后将进入审核状态，有相关的标签提示你的内容正在审核中。<br/>
                  2. 审核不通过（自见）：内容审核不通过，系统会有消息提醒，进行内容调整，内容修改并提交后，会再次进入审核。<br/>
                  3. 审核通过：审核通过后的文章会发布成功并可能获得被推荐机会。<br/>
                  4. 内容被删除：如果你发布的内容不符合相关法律法规要求，会直接删除，同时会有系统消息发送给你。
                </div>
              </el-collapse-item>


            </el-collapse>
          </div>
        </el-tab-pane>
        <el-tab-pane label="名言广场" name="名言广场">
          <div class="mt-10">
            <el-collapse accordion :accordion="true">
              <el-collapse-item>
                <template slot="title">
                  <div class="font-bold font-s-14 color-grey4">
                    名言广场是什么
                  </div>
                </template>
                <div class="font-s-14 color-grey4">
                  1.提供一个记录与分享名言名句的模块。<br/>
                </div>
              </el-collapse-item>
              <el-collapse-item>
                <template slot="title">
                  <div class="font-bold font-s-14 color-grey4">
                    如何玩转名言广场 <i class="header-icon el-icon-info"></i>
                  </div>
                </template>
                <div class="font-s-14 color-grey5">
                  1. 名言发布主要有三个选项，分别是名言分类，名言内容，收录的专辑。<br/>
                  2. 名言分类：有网站提供，主要有《电影台词》、《文学作品》、《小时摘抄》等。<br/>
                  3. 名言内容：作品里非常有意义的短句，对话，或自己的经验总结等。<br/>
                  4. 专辑：专辑有用户自己创建，创建专辑时可选择公开，方便被网站推荐给更多的人。
                </div>
              </el-collapse-item>
            </el-collapse>
          </div>
        </el-tab-pane>
      </el-tabs>
    </template>
  </div>
</template>

<script>
export default {
  name: "doubt",
  data() {
    return {
      activeName: "A币收益",
      websiteName: process.env.PROJECT_NAME,
    }
  },
  methods: {},
}
</script>

<style scoped>
.el-collapse {
  border-top: none;
}
</style>
