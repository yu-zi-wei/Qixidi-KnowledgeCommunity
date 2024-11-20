import Vue from "vue";

const utils = {
  // 日期格式化
  parseTime(time, pattern) {
    if (arguments.length === 0 || !time) {
      return null
    }
    const format = pattern || '{y}-{m}-{d} {h}:{i}:{s}'
    let date
    if (typeof time === 'object') {
      date = time
    } else {
      if ((typeof time === 'string') && (/^[0-9]+$/.test(time))) {
        time = parseInt(time)
      } else if (typeof time === 'string') {
        time = time.replace(new RegExp(/-/gm), '/').replace('T', ' ').replace(new RegExp(/\.[\d]{3}/gm), '');
      }
      if ((typeof time === 'number') && (time.toString().length === 10)) {
        time = time * 1000
      }
      date = new Date(time)
    }
    const formatObj = {
      y: date.getFullYear(),
      m: date.getMonth() + 1,
      d: date.getDate(),
      h: date.getHours(),
      i: date.getMinutes(),
      s: date.getSeconds(),
      a: date.getDay()
    }
    const time_str = format.replace(/{(y|m|d|h|i|s|a)+}/g, (result, key) => {
      let value = formatObj[key]
      // Note: getDay() returns 0 on Sunday
      if (key === 'a') {
        return ['日', '一', '二', '三', '四', '五', '六'][value]
      }
      if (result.length > 0 && value < 10) {
        value = '0' + value
      }
      return value || 0
    })
    return time_str
  },
  /**
   * date 转字符串 yyyy-mm-dd hh:ii:ss
   * @param date
   * @returns {string}
   */
  dateTransitionStr(date) {
    var y = date.getFullYear();
    var m = date.getMonth() + 1;
    m = m < 10 ? ('0' + m) : m;
    var d = date.getDate();
    d = d < 10 ? ('0' + d) : d;
    var h = date.getHours();
    h = h < 10 ? ('0' + h) : h;
    var minute = date.getMinutes();
    minute = minute < 10 ? ('0' + minute) : minute;
    var second = date.getSeconds();
    second = second < 10 ? ('0' + second) : second;
    return y + '-' + m + '-' + d + ' ' + h + ':' + minute + ':' + second;
  },

  reckonTime(time, format = '{y}-{m}-{d} {h}:{i}') {
    let tstamp = (new Date().valueOf()) - (new Date(time).valueOf())
    if (tstamp < 0) return 'NN-HH'
    let second = 1000 * 60  //1分钟内
    let min = 1000 * 60 * 60   //1小时内
    let hour = 1000 * 60 * 60 * 24 //一天内
    let day = 1000 * 60 * 60 * 24 * 7 //七天内
    let oct = 1000 * 60 * 60 * 24 * 30 //一个月内
    let result;
    switch (true) {
      case (new Date().getFullYear() != new Date(time).getFullYear()):
        result = this.parseTime(time, format)
        break;
      case (tstamp < second):
        result = "刚刚";
        break;
      case (tstamp < min):
        result = (Math.ceil(tstamp / second) + '分钟前')
        break;
      case (tstamp < hour):
        result = (Math.ceil(tstamp / min) + '小时前')
        break;
      case (tstamp < day):
        result = (Math.ceil(tstamp / hour) + '天前')
        break;
      case (tstamp < hour * 15):
        result = (Math.ceil(tstamp / hour) + '天前')
        break;
      case (tstamp < oct * 6):
        result = (Math.ceil(tstamp / oct) + '月前')
        break;
      default:
        result = this.parseTime(time, format)
    }
    return result;
  },

  obtainTimePeriod(time) {
// 获取当前时间
    let timeNow = new Date();
    if (time != null) {
      timeNow = time;
    }
    // 获取当前小时
    let hours = timeNow.getHours();
    // 设置默认文字
    let text = ``;
    // 判断当前时间段
    if (hours >= 0 && hours <= 10) {
      text = `早上`;
    } else if (hours > 10 && hours <= 14) {
      text = `中午`;
    } else if (hours > 14 && hours <= 18) {
      text = `下午`;
    } else if (hours > 18 && hours <= 24) {
      text = `晚上`;
    }
    // 返回当前时间段对应的状态
    return text;
  },
  // 转换字符串，undefined,null等转化为""
  parseStrEmpty(str) {
    if (!str || str == "undefined" || str == "null") {
      return "";
    }
    return str;
  },


  /**
   * 私信聊天时间推算
   * 参数：yyyy-MM-dd HH:mm:ss
   */
  cutChatTime(time) {
    let tstamp = (new Date().valueOf()) - (new Date(time).valueOf())
    if (tstamp < 0) return 'NH'
    let collect;
    let result;
    let collecth;
    let nowHours = new Date(time).getHours()
    if (new Date().getDate() === new Date(time).getDate()) {
      collect = time.split(' ')
      let hm = collect[1].split(":")[0] + ":" + collect[1].split(":")[1]
      switch (true) {
        case (nowHours >= 0 && nowHours < 6):
          result = '凌晨' + hm
          break;
        case (nowHours >= 6 && nowHours < 12):
          result = '上午' + hm
          break;
        case (nowHours >= 12 && nowHours < 14):
          result = '中午' + hm
          break;
        case (nowHours >= 14 && nowHours < 18):
          result = '下午' + hm
          break;
        case (nowHours >= 18 && nowHours < 24):
          result = '傍晚' + hm
          break;
      }
      return result;
    }
    if (new Date().getDate() - new Date(time).getDate() === 1) {
      collect = time.split(' ')
      let hm = collect[1].split(":")[0] + ":" + collect[1].split(":")[1]
      switch (true) {
        case (nowHours >= 0 && nowHours < 6):
          result = '昨天 凌晨' + hm
          break;
        case (nowHours >= 6 && nowHours < 12):
          result = '昨天 上午' + hm
          break;
        case (nowHours >= 12 && nowHours < 14):
          result = '昨天 中午' + hm
          break;
        case (nowHours >= 14 && nowHours < 18):
          result = '昨天 下午' + hm
          break;
        case (nowHours >= 18 && nowHours < 24):
          result = '昨天 傍晚' + hm
          break;
      }
      return result;
    }
    // 一天外
    collect = time.split('-')
    let collectNow = collect[2]
    collecth = collectNow.split(' ')
    switch (true) {
      case (nowHours >= 0 && nowHours < 6):
        result = ((collect[1] + '-' + collect[2]).split(' ')[0]) + " 凌晨" + (collecth[1].split(":")[0] + ":" + collecth[1].split(":")[1])
        break;
      case (nowHours >= 6 && nowHours < 12):
        result = ((collect[1] + '-' + collect[2]).split(' ')[0]) + " 上午" + (collecth[1].split(":")[0] + ":" + collecth[1].split(":")[1])
        break;
      case (nowHours >= 12 && nowHours < 14):
        result = ((collect[1] + '-' + collect[2]).split(' ')[0]) + " 中午" + (collecth[1].split(":")[0] + ":" + collecth[1].split(":")[1])
        break;
      case (nowHours >= 14 && nowHours < 18):
        result = ((collect[1] + '-' + collect[2]).split(' ')[0]) + " 下午" + (collecth[1].split(":")[0] + ":" + collecth[1].split(":")[1])
        break;
      case (nowHours >= 18 && nowHours < 24):
        result = ((collect[1] + '-' + collect[2]).split(' ')[0]) + " 傍晚" + (collecth[1].split(":")[0] + ":" + collecth[1].split(":")[1])
        break;
    }
    return result;
  },


  /**
   * 数字转中文
   * @param figure 具体数字
   * @returns {string}
   */
  numberToText(figure) {
    let character = "";
    switch (figure) {
      case 0:
        character = "零";
        break
      case 1:
        character = "一";
        break
      case 2:
        character = "二";
        break
      case 3:
        character = "三";
        break
      case 4:
        character = "四";
        break
      case 5:
        character = "五";
        break
      case 6:
        character = "六";
        break
      case 7:
        character = "七";
        break
      case 8:
        character = "八";
        break
      case 9:
        character = "九";
        break
    }
    return character;
  },
  //姓名脱敏
  handleName(name) {
    let arr = Array.from(name)
    let result = ''
    if (arr.length === 2) {
      result = arr[0] + '*'
    } else if (arr.length > 2) {
      for (let i = 1; i < arr.length - 1; i++) {
        arr[i] = '*'
      }
      result = arr.join("")
    } else {
      return name
    }
    return result
  },

//电话脱敏
  handlePhone(phone) {
    return phone.replace(/^(.{3})(?:\d+)(.{4})$/, "$1****$2")
  },

//邮箱脱敏
  handleEmail(email) {
    return email.replace(/^(.{0,3}).*@(.*)$/, "$1***@$2")
  },

//身份证脱敏
  handleIdCard(id) {
    return id.replace(/^(.{4})(?:\d+)(.{4})$/, "$1**********$2");
  },

}
Vue.prototype.$utils = utils;
