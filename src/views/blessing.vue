<template>
  <div>
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
</template>

<script>
import './css/blessing.css'

export default {
  name: 'blessing',
  data() {
    return {
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
      barrageList: [
        {content: '百年好合', createTime: ''},
        {content: '好事成双', createTime: ''},
        {content: '999', createTime: ''},
        {content: '永结同心', createTime: ''},
        {content: '我要当伴郎', createTime: ''},
        {content: '永远幸福', createTime: ''},
        {content: '幸福永远', createTime: ''},
      ],
      // 随机与顶部的距离
      topLists: [20, 40, 60, 80, 100, 110, 120, 140, 150, 160, 180, 200],
      // 随机评论颜色
      colorss: ["#fefefe", "#ffc312", "#ea2027", "#fff200", "#17c0eb", "#27ae60", "#eb2f06"],
    }
  },
  methods: {
    handleClick() {
      this.visible = true;
    },
    addComment() {
      if (!this.form.content) return
      let barrageList = this.barrageList;
      let topList = this.topLists;
      let barrageBox = document.querySelector('.barrageBox')
      let my_set = setInterval(() => {
        // barrageBox.innerHTML = ''
        this.createBarrage()
      }, (barrageList.length + 1) * 1000);

      let data = {content: this.form.content, createTime: ''};
      this.barrageList.push(data)

      this.visible = false;
      let num = Math.floor(Math.random() * 10)
      let div = document.createElement('div')
      div.innerText = this.form.content
      div.classList.add('box')
      div.style.top = topList[num] + 'px'

      // 加上边框，醒目标志
      div.style.border = '1px solid red'
      div.style.borderRadius = '40px'
      div.style.padding = '5px 10px'
      div.style.fontSize = '14px'
      div.style.letterSpacing = '3px'
      div.style.color = '#fefefe'

      this.form.content = '';
      barrageBox.appendChild(div)
      clearInterval(my_set)
      // 因为清除的话会重新计算时间，会造成一段时间的空白期，所以需要渲染一次。
      this.createBarrage()
      my_set = setInterval(() => {
        // barrageBox.innerHTML = ''
        this.createBarrage()
      }, (barrageList.length + this.frequency) * 1000);

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
            div.innerText = barrageList[index].content
            div.classList.add('box')
            //距离顶部的距离
            div.style.top = topList[num] + 'px'
            // 随机获得一个颜色
            div.style.color = '' + colors[Math.floor((Math.random() * colors.length))] + ''
            //设置div样式
            div.style.background = '#ce5fee'
            // div.style.opacity = '0.6'
            div.style.padding = '5px 10px';
            div.style.fontSize = '14px'
            div.style.borderRadius = '40px'
            div.style.letterSpacing = '3px'
            div.style.width = (barrageList[index].content.length * 17) + 'px'

            barrageBox.appendChild(div)
          }, index * 1000);
        })(index)
      }
    },
  },
  mounted() {
    this.createBarrage();
    setInterval(() => {
      // barrageBox.innerHTML = ''
      this.createBarrage()
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
</style>