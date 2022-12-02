<template>
  <div>
    <a-spin :loading="loading" tip="加速获取祝福中..." style="width: 100%" :size="28">
      <div class="mb-40">
        <div class="barrageBox">
          <div class="barrageButton" @click="handleClick">
        <span class="add-button">
          <span>送上祝福</span>
          <div class="liquid"></div>
        </span>
          </div>
        </div>
        <a-modal v-model:visible="visible" @ok="addComment(value)">
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
      </div>
    </a-spin>
  </div>
</template>

<script>
import './css/blessing.css'
import {commentAdd, listComment} from "@/api/lover";
import {Notification} from '@arco-design/web-vue';
import {formatDate} from "@/uitls/dateUitls";

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
      // 频率4秒执行一次
      frequency: 4,
      barrageList: [],
      // 随机与顶部的距离
      topLists: [20, 40, 60, 80, 100, 120, 140, 160, 180, 200, 220, 240, 260, 280, 300, 320, 340, 360],
      // 随机评论颜色
      colorss: ["#fefefe", "#ffc312", "#fefefe",
        "#fff200", "#eb2f06", "#2f3542",
        "#eb2f06", "#2f3542", "#fff200"],
    }
  },
  methods: {
    listComments() {
      listComment().then(res => {
        this.barrageList = res.rows;
      })
    },
    handleClick() {
      this.visible = true;
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
      })
      let formatDate1 = formatDate(new Date(), "yyyy-MM-dd hh:mm");
      let data = {content: this.form.content, createTime: formatDate1};
      this.barrageList.push(data)
      this.visible = false;
      let num = Math.floor(Math.random() * 10)
      let div = document.createElement('div')
      div.innerHTML = this.form.name + "：" + this.form.content
      // + '<p/>' + '<span style="font-size: 10px">' + formatDate1 + '</span>';
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
        // barrageBox.innerHTML = ''
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
            let num = Math.floor(Math.random() * 10)
            let div = document.createElement('div')
            div.innerHTML = barrageList[index].name + "：" + barrageList[index].content
            // + '<p/>' + '<span style="font-size: 10px">' + barrageList[index].createTime + '</span>';
            div.classList.add('box')
            //距离顶部的距离
            div.style.top = topList[num] + 'px'
            // div.style.top = '300px'
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
    this.createBarrage();
    setInterval(() => {
      // barrageBox.innerHTML = ''
      this.createBarrage()
      this.loading = false;
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