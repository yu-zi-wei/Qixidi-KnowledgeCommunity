<template>
  <div>测试页面
    <div>
      <el-button @click="steToken">设置token</el-button>
      <el-button @click="requestJuejing">请求</el-button>
      <el-button @click="dynamicRouting">动态路由</el-button>
      <el-button @click="getSessionStorage">getSessionStorage</el-button>
      <el-button @click="$router.push('/dictum');"> this.$router.push('/dictum');</el-button>
      <el-button @click="$router.push('/xxx');"> this.$router.push('/xxx');</el-button>
      <el-nuxt-link to="/">主页</el-nuxt-link>
      <el-button @click="websocketMain('1625400372048826368')"> 测试websocket客户端1</el-button>
      <el-button @click="websocketMain('ziwei12')"> 测试websocket客户端2</el-button>
      <el-button @click="websocketSend"> 发送消息</el-button>
      <el-button @click="websocketClose('ziwei1')"> 关闭链接ziwei1</el-button>


    </div>

    <div v-if="submissionLoading">
      <!--      <submission-chart :profile="submissionObj"></submission-chart>-->
    </div>
    <div>
      <!--      <div v-for="(item,index) in toolList" :key="index">-->
      <!--        <p>{{ item.toolName }}</p>-->
      <!--      </div>-->
    </div>
    <ai-editor-module></ai-editor-module>
  </div>
</template>

<script>
import SubmissionChart from "../components/submission-chart";
import AiEditorModule from "../components/AiEditor-module";
import https from "https";

// import WebSocket from 'websocket';
export default {
  name: "test",
  components: {AiEditorModule, SubmissionChart},
  data() {
    return {
      title: "测试",
      meta: [
        {hid: 'description', name: 'description', content: '描述'},
        {hid: 'keywords', name: 'keywords', content: '关键字'}
      ],
      socket: null,
      submissionObj: {},
      submissionLoading: false,
    }
  },
  async asyncData({app, params}) {
    const https = require('https');
    const response = await fetch(`${process.env.SERVICE_PROTOCOL}${process.env.SERVER_URL}/white/configure/tool/child/list?id=2`, {
      //不做https校验，如果你的https是被信任的建议注释该代码，因为http是不安全的
      agent: new https.Agent({rejectUnauthorized: false})
    });
    const data = await response.json();
    return {
      toolList: data,
    }
  },

  methods: {
    tesTtoolList() {
      // console.log("this.toolList:", JSON.stringify(this.toolList))
      this.$API("/user/census/submission", "get").then(res => {
        this.submissionObj = res.data.reduce((acc, item) => {
          acc[item.dateTimes] = item.censusSum;
          return acc;
        }, {});
        this.submissionLoading = true;
      });
    },
    getColor(number) {
      let num = number / this.props.maxData;
      let level = 1;
      if (num == 0) {
        num = 0.1;
        level = 1
      } else if (num > 0 && num < 0.3) {
        num = 0.3;
        level = 2
      } else if (num > 0.3 && num < 0.6) {
        num = 0.6;
        level = 3
      } else if (num < 0.9) {
        num = 0.8;
        level = 4
      } else {
        num = 1;
        level = 5
      }
      if (this.props.maxData == 0) {
        num = 0.1;
        level = 1
      }
      return 'rgba(55,126,259,' + num + ')';
    },
    websocketClose() {
      this.socket.close(1000, 'Connection closed');
    },
    websocketSend() {
      this.socket.send("发送测试数据")
    },
    websocketMain(name) {
      const url = process.env.WEBSOCKET_PROTOCOL + process.env.SERVER_URL + `/websocket/${name}/-1`;
      // const url = 'ws://127.0.0.1:9001/websocket/' + name;
      this.socket = new WebSocket(url);

      this.socket.onopen = () => {
        console.log('链接成功');
        // 在这里可以执行连接成功后的操作
      };

      this.socket.onmessage = (event) => {
        console.log('Received message: ', event);
        // 处理接收到的消息
      };
      this.socket.onclose = (event) => {
        console.log('链接关闭：', event);
      };
    },

    getSessionStorage() {
      // sessionStorage.setItem('url',"/test")
      let url = sessionStorage.getItem('url');
      console.log(url)
    },
    steToken() {
      this.$store.commit('setToken', "67676767fgf")
      // this.setToken('token', "")
    },
    requestJuejing() {
      let data = {
        "name": "参数"
      };
      // let newVar = this.$axios.post('/api/list');
      this.$API("/white/configure/tool/list", this.$get(), null).then(tes => {
        console.log("requestType:", this.$get())
      })
    },
    dynamicRouting() {
      this.$router.push({
        path: `/article/${34}`
      })
    },

  },
  mounted() {
    this.tesTtoolList();
  }


}
</script>

<style scoped>
.graph {
  display: grid;
  grid-template-columns: repeat(12, 21px); /*竖向12列，21px宽*/
  grid-template-rows: repeat(7, 21px); /*横向7列，21px宽*/
  padding-inline-start: 0px;
  grid-auto-flow: column; /*生成7*12的格子后，设置为竖向排布*/
  margin: 20px 20px 5px 20px;
}

.months {
  display: grid;
  grid-template-columns: repeat(12, 21px);
  grid-template-rows: 21px;
  font-size: 8px;
  color: #aaa;
  padding-inline-start: 0px;
  margin: 5px 20px 5px 20px;
}

.li-month {
  display: inline-block;
}

.li-day {
  background-color: #eaeaea;
  list-style: none; /*记得把list的圆点效果去掉*/
  margin: 1.5px;
  border-radius: 3px;
}

.li-day:hover { /*添加hover强调效果*/
  box-shadow: 0px 0px 5px rgb(57, 120, 255);
}

.graph li[data-level="1"] {
  background-color: #d9ecff;
}

.graph li[data-level="2"] {
  background-color: #8cc5ff;
}

.graph li[data-level="3"] {
  background-color: #409eff;
}

.submission-chart {
  width: 820px;
  height: 180px;
  background-color: #fff;
  margin: auto;
  margin-top: 20px;
  padding: 0px 0;
  font-size: 12px;
}

.submission-chart .calendar {
  margin-left: 16px;
  margin-right: 16px;
  display: flex;
}

.submission-chart .calendar .weeks {
  width: 30px;
  margin-right: 3px;
  margin-top: 22px;

}

.submission-chart .calendar .weeks.week {
  margin-top: 13px;
  width: 30px;
  height: 14px;
}
</style>
