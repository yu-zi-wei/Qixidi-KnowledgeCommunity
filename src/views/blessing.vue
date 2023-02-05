<template>
  <div>
    <a-spin :loading="loading" tip="加速获取祝福中..." style="width: 100%" :size="28">
      <div class="ml-15">
        <a-space size="large" title="切换样式">
          <a-switch checked-color="#ffc312" unchecked-color="#ce5fee" v-model="isList"/>
        </a-space>
      </div>
      <div>
        <div class="barrageButton">
        <span class="add-button" @click="handleClick" title="送上祝福">
          <span>送上祝福</span>
          <div class="liquid"></div>
        </span>
        </div>
        <!--        弹幕区-->
        <div class="barrageBox" v-if="!isList"></div>
        <!--        列表区-->
        <div class="listBox" v-if="isList">
          <a-row :gutter="24">
            <a-col :span="2" style="height: 1px">
            </a-col>
            <a-col :span="20">
              <div class="comment-list-cl">
                <div class="overflow-hidden">
                  <div class="item-list" v-for="item of barrageList">
                    <a-trigger trigger="hover">
                      <div>
                        <span>{{ item.name }}：</span>
                        <span v-text="item.content"></span>
                      </div>
                      <template #content>
                        <div class="hover-content">
                          <div class="mb-6">祝福人：{{ item.name }}</div>
                          <div>时间：{{ parseTimes(item.createTime, '{y}-{m}-{d} {h}:{i}') }}</div>
                        </div>
                      </template>
                    </a-trigger>
                  </div>
                </div>

              </div>
            </a-col>
            <a-col :span="2" style="height: 1px">
            </a-col>
          </a-row>
        </div>
        <a-modal v-model:visible="visible" @ok="addComment(value)" v-if="barrageList.length>0">
          <template #title>
            祝福语
          </template>
          <div class="overflow-hidden">
            <a-form :model="form">
              <a-row>
                <a-col :span="12">
                  <a-form-item field="value" label="头像">
                    <a-select :style="{width:'160px'}" placeholder="头像">
                      <a-option v-for="item of headPortrait">
                        <span>{{ item }}</span>
                      </a-option>
                    </a-select>
                  </a-form-item>
                </a-col>
                <a-col :span="12">
                  <a-form-item field="value" label="昵称">
                    <a-input v-model="form.name" placeholder="昵称"/>
                  </a-form-item>
                </a-col>
              </a-row>
              <a-row>
                <a-mention v-model="form.content" :data="['Bytedance', 'Bytedesign', 'Bytenumner']"
                           @keyup.enter="addComment"
                           type="textarea"
                           placeholder="祝福语" :max-length="60" allow-clear show-word-limit style="height: 120px"/>
              </a-row>

            </a-form>
            <div class="fl-right mt-20">
              <button class="send-button" @click="addComment">送上祝福</button>
            </div>
          </div>
        </a-modal>
        <div v-if="!loading && barrageList.length==0" class="text-center" style="color: #fefefe;font-size: 18px">
          <svg t="1670079174437" class="icon" viewBox="0 0 1567 1024" version="1.1" xmlns="http://www.w3.org/2000/svg"
               p-id="11978" width="54" height="54">
            <path
                d="M156.662278 699.758173h21.097186A10.444152 10.444152 0 0 1 187.994733 710.202325c0 5.765172-4.490985 10.444152-10.235269 10.444152H156.662278v21.097186A10.444152 10.444152 0 0 1 146.218126 751.978932a10.277045 10.277045 0 0 1-10.444152-10.235269V720.646477H114.676787A10.444152 10.444152 0 0 1 104.441518 710.202325c0-5.765172 4.490985-10.444152 10.235269-10.444152H135.773974v-21.097187A10.444152 10.444152 0 0 1 146.218126 668.425717c5.765172 0 10.444152 4.490985 10.444152 10.235269v21.097187z m1378.628042-83.553215v-21.097186A10.277045 10.277045 0 0 0 1524.846168 584.872503a10.444152 10.444152 0 0 0-10.444152 10.235269v21.097186h-21.097186a10.277045 10.277045 0 0 0-10.235269 10.444152c0 5.598065 4.595427 10.444152 10.235269 10.444152h21.097186v21.097187c0 5.744284 4.67898 10.235269 10.444152 10.235268a10.444152 10.444152 0 0 0 10.444152-10.235268V637.093262h21.097187c5.744284 0 10.235269-4.67898 10.235268-10.444152a10.444152 10.444152 0 0 0-10.235268-10.444152H1535.29032zM776.460024 960.861969H250.596979A20.80475 20.80475 0 0 1 229.77134 939.973665c0-11.530344 9.462402-20.888304 20.825639-20.888303h94.728457A83.010119 83.010119 0 0 1 334.212859 877.413196v-605.96969A83.49055 83.49055 0 0 1 417.849627 187.994733H480.430984V167.001988A83.49055 83.49055 0 0 1 564.067752 83.553215h501.152182A83.448773 83.448773 0 0 1 1148.856702 167.001988v605.969689c0 15.185797-4.052331 29.410732-11.133466 41.672166h115.554096c11.551232 0 20.909192 9.274407 20.909192 20.888304 0 11.530344-9.295295 20.888304-20.888304 20.888304H1002.638576v20.992745c0 15.185797-4.052331 29.410732-11.133466 41.672166h11.196131c11.488567 0 20.825639 9.274407 20.825639 20.888303 0 11.530344-9.462402 20.888304-20.825639 20.888304h-109.893365c9.545955 16.000441 7.478013 36.972297-6.41271 50.863019a41.672166 41.672166 0 0 1-59.072122 0L776.460024 960.861969z m76.367638-41.776607h66.424806c22.977134 0 41.609501-18.59059 41.609501-41.881049V270.461756c0-22.559368-18.047494-40.690416-40.314426-40.690416H416.303892c-22.266932 0-40.314426 18.214601-40.314426 40.690416v606.742557c0 23.123352 18.799473 41.881049 41.588613 41.881049h317.084449l-10.736588-10.757477a41.693054 41.693054 0 0 1-10.861918-40.377091l-19.718558-19.739447A146.259902 146.259902 0 0 1 502.363703 627.693525a146.218126 146.218126 0 0 1 220.517822 190.981761l19.739447 19.739447a41.630389 41.630389 0 0 1 40.377091 10.841029L852.827662 919.085362zM1002.638576 814.643843h62.852906A41.797496 41.797496 0 0 0 1107.080095 772.867236V167.106429c0-23.14424-18.632367-41.776607-41.588613-41.776607H563.775316A41.797496 41.797496 0 0 0 522.207592 167.106429v20.888304h396.794216A83.448773 83.448773 0 0 1 1002.638576 271.443506V814.643843zM266.325872 46.998683h31.123572c8.773088 0 15.875111 6.955805 15.875111 15.666228 0 8.647758-7.102023 15.666228-15.875111 15.666228h-31.123572v31.123572c0 8.773088-6.955805 15.875111-15.666228 15.875111a15.770669 15.770669 0 0 1-15.666228-15.875111V78.331139H203.869844A15.728893 15.728893 0 0 1 187.994733 62.664911c0-8.647758 7.102023-15.666228 15.875111-15.666228h31.123572V15.875111c0-8.773088 6.955805-15.875111 15.666228-15.875111 8.647758 0 15.666228 7.102023 15.666228 15.875111v31.123572zM20.888304 939.973665c0-11.530344 9.462402-20.888304 20.825638-20.888303h125.455152c11.488567 0 20.825639 9.274407 20.825639 20.888303 0 11.530344-9.462402 20.888304-20.825639 20.888304H41.713942A20.80475 20.80475 0 0 1 20.888304 939.973665z m658.733544-135.021995a104.441518 104.441518 0 1 0-147.722083-147.722083 104.441518 104.441518 0 0 0 147.722083 147.722083zM459.542681 313.324555a20.888304 20.888304 0 0 1 20.867415-20.888304H710.202325a20.888304 20.888304 0 1 1 0 41.776608H480.430984A20.825639 20.825639 0 0 1 459.542681 313.324555z m0 104.441518c0-11.530344 9.295295-20.888304 20.742085-20.888303h334.505295c11.44679 0 20.742086 9.274407 20.742086 20.888303 0 11.530344-9.295295 20.888304-20.742086 20.888304H480.284766A20.762974 20.762974 0 0 1 459.542681 417.766073z m0 104.441519c0-11.530344 9.316183-20.888304 20.846527-20.888304h146.301679c11.509455 0 20.846527 9.274407 20.846527 20.888304 0 11.530344-9.316183 20.888304-20.846527 20.888303h-146.301679A20.80475 20.80475 0 0 1 459.542681 522.207592zM62.664911 396.87777a62.664911 62.664911 0 1 1 0-125.329822 62.664911 62.664911 0 0 1 0 125.329822z m0-31.332456a31.332456 31.332456 0 1 0 0-62.664911 31.332456 31.332456 0 0 0 0 62.664911zM1357.739739 271.547948a62.664911 62.664911 0 1 1 0-125.329822 62.664911 62.664911 0 0 1 0 125.329822z m0-31.332456a31.332456 31.332456 0 1 0 0-62.664911 31.332456 31.332456 0 0 0 0 62.664911z"
                fill="#ffffff" p-id="11979"></path>
          </svg>
          <div>暂无祝福</div>
        </div>
      </div>
    </a-spin>
  </div>
</template>

<script>
import './css/blessing.css'
import {commentAdd, listComment} from "@/api/lover";
import {Notification} from '@arco-design/web-vue';
import {formatDate, parseTime} from "@/utils/dateUitls";

export default {
  name: 'blessing',
  data() {
    return {
      loading: true,
      headPortrait: [
        '👼', '🤶', '🎅', '👨', '🧒', '👵', '🤴', '👸', '👩', '‍🦳',
        '👴', '👩', '👮', '🕵️', '♀', '‍♂', '👨', '👩', '‍🚀',
        '👩', '‍🚒', '🧚', '🙆', '🤦', '💂',],
      form: {
        value: '',
        content: '',
        name: null,
      },
      visible: false,
      // 执行频率
      frequency: 10,
      barrageList: [],
      // 随机与顶部的距离
      topLists: [20, 40, 60, 80, 100, 120, 140, 160, 180, 200, 220, 240, 260, 280, 300,
        320, 340, 360, 380, 400, 420, 440, 460, 480],
      // 随机评论颜色
      colorss: ["#fefefe", "#ffc312", "#fefefe",
        "#fff200", "#eb2f06", "#2f3542",
        "#eb2f06", "#2f3542", "#fff200"],
      //以列表展示
      isList: false,
    }
  },
  watch: {
    isList: {
      handler: function () {
        this.listComments();
      }
    }
  },
  methods: {
    parseTimes(value, args) {
      return parseTime(value, args);
    },
    listComments() {
      listComment().then(res => {
        this.barrageList = res.rows;
        this.createBarrage();
        this.loading = false;
      })
    },
    handleClick() {
      this.visible = true;
    },
    colorRandom() {
      let string = this.colorss[Math.floor((Math.random() * colors.length))];

    },
    addComment() {
      if (!this.form.name) {
        Notification.info({
          title: '提示',
          content: '昵称不能为空噢',
          showIcon: false,
          closable: true,
          style: {background: "#ce5fee", border: "none", color: "#fefefe"}
        })
        return;
      }
      if (!this.form.content) {
        Notification.info({
          title: '提示',
          content: '祝福语不能为空噢',
          showIcon: false,
          closable: true,
          style: {background: "#ce5fee", border: "none", color: "#fefefe"}
        })
        return;
      }
      let barrageList = this.barrageList;
      let topList = this.topLists;
      let barrageBox = document.querySelector('.barrageBox')
      let my_set = setInterval(() => {
        // barrageBox.innerHTML = ''
        this.createBarrage()
      }, (barrageList.length + 1) * 1000);
      commentAdd(this.form).then(res => {
        if (res.code == 200) {
          Notification.info({
            title: '成功',
            content: '感谢您的祝福',
            showIcon: false,
            closable: true,
            style: {background: "#ce5fee", border: "none", color: "#fefefe"}
          })
        }
      });
      let formatDate1 = formatDate(new Date(), "yyyy-MM-dd hh:mm");
      let data = {content: this.form.content, createTime: formatDate1};
      this.barrageList.push(data)
      this.visible = false;
      let num = Math.floor(Math.random() * topList.length)
      //控制弹幕展示
      if (this.isList) return;

      let div = document.createElement('div')
      div.innerHTML = this.form.name + "：" + this.form.content
      div.classList.add('box')
      div.style.top = topList[num] + 'px'

      // 加上边框，醒目标志
      div.style.border = '1px solid red'
      div.style.borderRadius = '40px'
      div.style.padding = '8px 10px'
      div.style.fontSize = '14px'
      div.style.letterSpacing = '3px'
      div.style.color = '#fefefe'
      if ((this.form.content.length * 17) > 200) {
        div.style.minWidth = ((this.form.content.length) * 17) + 'px'
      } else {
        div.style.minWidth = '200px'
      }
      this.form.content = '';
      barrageBox.appendChild(div)
      clearInterval(my_set)
      // 因为清除的话会重新计算时间，会造成一段时间的空白期，所以需要渲染一次。
      this.createBarrage()
      my_set = setInterval(() => {
        this.createBarrage()
      }, (barrageList.length + this.frequency) * 1000);

      //到底删除div
      setTimeout(function () {
        div.remove();
      }, 12000);
    },
    createBarrage() {
      let barrageList = this.barrageList;
      let topList = this.topLists;
      let colors = this.colorss;

      let barrageBox = document.querySelector('.barrageBox')
      for (let index = 0; index < barrageList.length; index++) {
        (function (index) {
          setTimeout(() => {
            let num = Math.floor(Math.random() * topList.length)
            let div = document.createElement('div')
            div.innerHTML = barrageList[index].name + "：" + barrageList[index].content
            // + '<p/>' + '<span style="font-size: 10px">' + barrageList[index].createTime + '</span>';
            div.classList.add('box')
            //距离顶部的距离
            div.style.top = topList[num] + 'px'
            // 随机获得一个颜色
            div.style.color = '' + colors[Math.floor((Math.random() * colors.length))] + ''
            //设置div样式
            div.style.background = '#ce5fee'
            // div.style.opacity = '0.6'
            div.style.padding = '8px 10px';
            div.style.fontSize = '14px'
            div.style.borderRadius = '40px'
            div.style.letterSpacing = '3px'

            if ((((barrageList[index].content.length) + (barrageList[index].name.length + 2)) * 17) > 140) {
              div.style.minWidth = (((barrageList[index].content.length) + (barrageList[index].name.length + 2)) * 17) + 'px'
            } else {
              div.style.minWidth = '140px'
            }
            barrageBox.appendChild(div)
            //到底删除div
            setTimeout(function () {
              div.remove();
            }, 12000);

          }, index * 1000);
        })(index)
      }
    },
  },
  mounted() {
    this.listComments();
    setInterval(() => {
      if (this.isList) {
        this.loading = false;
        return;
      } else {
        this.createBarrage()
        this.loading = false;
      }
    }, (this.barrageList.length + this.frequency) * 1000);
  }
}
</script>
<style>
.arco-modal {
  background-color: #be2edd;
}

.arco-modal-header {
  border-bottom: 1px solid #ce5fee;
}

.arco-modal-footer {
  display: none;
  border-top: 1px solid #ce5fee;
}

.arco-btn-primary, .arco-btn-primary[type='button'], .arco-btn-primary[type='submit'] {
  background-color: #be2edd;
}

.arco-spin-tip, .arco-spin-icon {
  color: #fefefe;
}

.arco-spin-mask {
  background-color: transparent;
}
</style>