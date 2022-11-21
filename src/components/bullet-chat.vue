<template>
  <div>
    <div class="boxDom" id="boxDom">
      <div class="idDom" id="idDom">
        <div class="content">
          <p class="title">吐槽:</p>
          <input type="text" class="text" id="text"/>
          <button type="button" class="btn" id="btn">发表</button>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: "bulletChat",
  mounted() {
    var colors = ["#369", "#16481f", "orange", "red", "skyblue", "gray", "#aaa", "lightgreen", "green", "darkblue", "#06257b", "darkred", "#e0a111", "black", "#e2128f", "#860dad", "#94ad0d", "#ce3131"]; //用来设置的颜色
    function $(id) {
      return document.getElementById(id);
    }

    window.onload = function () {
      $('btn').onclick = function () {
        var timer;//timer不能设置成全局变量，如果设置成全局变量，他的值将始终不会清空
        var input = $('text');
        var span = document.createElement('span'); //创建一个span标签
        span.innerHTML = input.value; //行级元素用value来获取值，块级元素用innerhtml来获取其值
        $('boxDom').appendChild(span);//将span放入页面div中
        span.style.color = colors[parseInt(Math.random() * colors.length)]; //随机产生颜色，用Math.random获得的值是小数所以要取整并且取值大于等于0小于1,有18种颜色
        span.style.top = (Math.random() * 400) + 'px'; //随机产生一个值为[0,400);
        var wWidth = document.body.clientWidth;
        span.style.left = document.body.clientWidth + 'px';//初始值为页面可见区域的值，此时不能再页面中显示
        var num = wWidth;
        timer = setInterval(function () {
          num--;                   //让内容移动
          if (num < -400) {
            $('boxDom').removeChild(span); //清除元素
            clearInterval(timer);
          }
          span.style.left = num + 'px';
        }, 10)
      }
    }
  }
}
</script>

<style>
html, body {
  margin: 0px;
  padding: 0px;
  width: 100%;
  height: 100%;
  font-family: "微软雅黑";
  font-size: 62.5%;
}

.boxDom {
  width: 100%;
  height: 100%;
  position: relative;
  overflow: hidden;
}

.idDom {
  width: 100%;
  height: 100px;
  background: #666;
  position: fixed;
  bottom: 0px;
}

.content {
  display: inline-block;
  width: 430px;
  height: 40px;
  position: absolute;
  left: 0px;
  right: 0px;
  top: 0px;
  bottom: 0px;
  margin: auto;
}

.title {
  display: inline;
  font-size: 4em;
  vertical-align: bottom;
  color: #fff;
}

.text {
  border: none;
  width: 300px;
  height: 30px;
  border-radius: 5px;
  font-size: 2.4em;
}

.btn {
  width: 60px;
  height: 30px;
  background: #f90000;
  border: none;
  color: #fff;
  font-size: 2.4em;
}

span {
  width: 300px;
  height: 40px;
  position: absolute;
  overflow: hidden;
  color: #000;
  font-size: 4em;
  line-height: 1.5em;
  cursor: pointer;
  white-space: nowrap;
}
</style>