<template>
  <div class="mt-20">
    <div class="text-center mb-20">
      <a class="fancy" href="#">
        <span class="top-key"></span>
        <span class="text">关于
          <span style="color: #fed330;font-size: 16px">❤</span>
          我们</span>
        <span class="bottom-key-1"></span>
        <span class="bottom-key-2"></span>
      </a>
    </div>
    <div>
      <a-row>
        <a-col :span="3">
        </a-col>
        <a-col :span="18" class="about-text">
          撑着油纸伞，
          <br/>
          独自 彷徨在悠长、悠长
          <br/>
          又寂寥的雨巷
          <br/>
          我希望逢着
          <br/>
          一个丁香一样的
          <br/>
          结着愁怨的姑娘
          <br/>
          她是有
          <br/>
          丁香一样的颜色
          <br/>
          丁香一样的芬芳
          <br/>
          丁香一样的忧愁
          <br/>
          在雨中哀怨
          <br/>
          哀怨又彷徨
          <br/>
          她彷徨在这寂寥的雨巷
          <br/>
          撑着油纸伞
          <br/>
          像我一样
          <br/>
          像我一样地
          <br/>
          默默彳亍着
          <br/>
          冷漠、凄清，又惆怅
          <br/>
          她静默地走近
          <br/>
          走近，又投出
          <br/>
          太息一般的眼光
          <br/>
          她飘过
          <br/>
          像梦一般的
          <br/>
          像梦一般的凄婉迷茫
          <br/>
          像梦中飘过
          <br/>
          一枝丁香地
          <br/>
          我身旁飘过这女郎
          <br/>
          她静默地远了、远了
          <br/>
          到了颓圮的篱墙
          <br/>
          走尽这雨巷
          <br/>
          在雨的哀曲里
          <br/>
          消了她的颜色
          <br/>
          散了她的芬芳
          <br/>
          消散了，甚至她的
          <br/>
          太息般的眼光
          <br/>
          丁香般的惆怅
          <br/>
          撑着油纸伞，独自
          <br/>
          彷徨在悠长、悠长
          <br/>
          又寂寥的雨巷
          <br/>
          我希望飘过
          <br/>
          一个丁香一样的
          <br/>
          结着愁怨的姑娘

        </a-col>
        <a-col :span="3">
        </a-col>
      </a-row>
    </div>

  </div>
</template>

<script>

export default {
  name: "about"
}
</script>
<style>
.about-text {
  /*text-align: center;*/
  background-color: #ce5fee;
  padding: 20px 10px 20px 40px;
  letter-spacing: 4px;
  line-height: 2;
  border-radius: 4px;
  color: #fefefe;
}

/* From www.lingdaima.com */
.fancy {
  background-color: transparent;
  border: 2px solid #fefefe;
  border-radius: 0;
  box-sizing: border-box;
  color: #fff;
  cursor: pointer;
  display: inline-block;
  font-weight: 700;
  letter-spacing: 0.05em;
  outline: none;
  overflow: visible;
  padding: 1.25em 2em;
  position: relative;
  text-align: center;
  text-decoration: none;
  text-transform: none;
  transition: all 0.3s ease-in-out;
  user-select: none;
  font-size: 13px;
}

.fancy::before {
  content: " ";
  width: 1.5625rem;
  height: 2px;
  background: #fefefe;
  top: 50%;
  left: 1.5em;
  position: absolute;
  transform: translateY(-50%);
  transform-origin: center;
  transition: background 0.3s linear, width 0.3s linear;
}

.fancy .text {
  font-size: 1.125em;
  line-height: 1.33333em;
  padding-left: 2em;
  display: block;
  text-align: left;
  transition: all 0.3s ease-in-out;
  text-transform: uppercase;
  text-decoration: none;
  color: #fefefe;
}

.fancy .top-key {
  height: 2px;
  width: 1.5625rem;
  top: -2px;
  left: 0.625rem;
  position: absolute;
  background: #be2edd;
  transition: width 0.5s ease-out, left 0.3s ease-out;
}

.fancy .bottom-key-1 {
  height: 2px;
  width: 1.5625rem;
  right: 1.875rem;
  bottom: -2px;
  position: absolute;
  background: #be2edd;
  transition: width 0.5s ease-out, right 0.3s ease-out;
}

.fancy .bottom-key-2 {
  height: 2px;
  width: 0.625rem;
  right: 0.625rem;
  bottom: -2px;
  position: absolute;
  background: #be2edd;
  transition: width 0.5s ease-out, right 0.3s ease-out;
}

.fancy:hover {
  color: white;
  background: #ce5fee;
}

.fancy:hover::before {
  width: 0.9375rem;
  background: white;
}

.fancy:hover .text {
  color: white;
  padding-left: 1.5em;
}

.fancy:hover .top-key {
  left: -2px;
  width: 0px;
}

.fancy:hover .bottom-key-1,
.fancy:hover .bottom-key-2 {
  right: 0;
  width: 0;
}
</style>
