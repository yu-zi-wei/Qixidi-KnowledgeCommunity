<template>
  <div class="loading-page bg-opacity" :style="{display:hide?'none':'block'}">
    <div class="dark" @dblclick="close">
      <div class="la-ball-spin-clockwise la-2x">
        <div></div>
        <div></div>
        <div></div>
        <div></div>
        <div></div>
        <div></div>
        <div></div>
        <div></div>
      </div>
    </div>
  </div>
</template>

<style scoped lang="scss">
.show {
  display: block;
}

.hide {
  display: none;
}

.loading-page {
  background: rgba(0, 0, 0, .65);
  display: none;
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  min-height: 100%;
  min-width: 100%;
  transition: all 1s;
  z-index: 20000;
  &.hide {
    display: none;
  }
  &.bg-opacity {
    background: rgba(0, 0, 0, 0);
  }
  .dark {
    width: 100px;
    height: 100px;
    border-radius: 10px;
    background: rgba(0, 0, 0, .65);
    position: absolute;
    top: 40%;
    left: 50%;
    margin-left: -50px;
    text-align: center;
    img {
      width: 70px;
      height: 70px;
      margin-top: 15px;
    }
  }
}
.la-ball-spin-clockwise{
  width: 64px;
  height: 64px;
  margin-top: 18px;
  margin-left: 18px;
  display: block;
  font-size: 0;
  color: #fff;
  position: relative;
  box-sizing: border-box;
  animation-play-state: running;
}
.la-ball-spin-clockwise>div{
  width: 16px;
  height: 16px;
  margin-top: -8px;
  margin-left: -8px;
  position: absolute;
  border-radius: 100%;
  animation: ball-spin-clockwise 1s infinite ease-in-out;
  display: inline-block;
  float: none;
  background-color: currentColor;
  border: 0 solid currentColor;
  animation-play-state: running;
}
.la-ball-spin-clockwise>div:nth-child(1){
  top: 5%;
  left: 50%;
  webkit-animation-delay: -.875s;
  -moz-animation-delay: -.875s;
  -o-animation-delay: -.875s;
  animation-delay: -.875s;
}
.la-ball-spin-clockwise>div:nth-child(2) {
  top: 18.1801948466%;
  left: 81.8198051534%;
  -webkit-animation-delay: -.75s;
  -moz-animation-delay: -.75s;
  -o-animation-delay: -.75s;
  animation-delay: -.75s;
}
.la-ball-spin-clockwise>div:nth-child(3) {
  top: 50%;
  left: 95%;
  -webkit-animation-delay: -.625s;
  -moz-animation-delay: -.625s;
  -o-animation-delay: -.625s;
  animation-delay: -.625s;
}
.la-ball-spin-clockwise>div:nth-child(4) {
  top: 81.8198051534%;
  left: 81.8198051534%;
  -webkit-animation-delay: -.5s;
  -moz-animation-delay: -.5s;
  -o-animation-delay: -.5s;
  animation-delay: -.5s;
}
.la-ball-spin-clockwise>div:nth-child(5) {
  top: 94.9999999966%;
  left: 50.0000000005%;
  -webkit-animation-delay: -.375s;
  -moz-animation-delay: -.375s;
  -o-animation-delay: -.375s;
  animation-delay: -.375s;
}
.la-ball-spin-clockwise>div:nth-child(6) {
  top: 81.8198046966%;
  left: 18.1801949248%;
  -webkit-animation-delay: -.25s;
  -moz-animation-delay: -.25s;
  -o-animation-delay: -.25s;
  animation-delay: -.25s;
}
.la-ball-spin-clockwise>div:nth-child(7) {
  top: 49.9999750815%;
  left: 5.0000051215%;
  -webkit-animation-delay: -.125s;
  -moz-animation-delay: -.125s;
  -o-animation-delay: -.125s;
  animation-delay: -.125s;
}
.la-ball-spin-clockwise>div:nth-child(8) {
  top: 18.179464974%;
  left: 18.1803700518%;
  -webkit-animation-delay: 0s;
  -moz-animation-delay: 0s;
  -o-animation-delay: 0s;
  animation-delay: 0s;
}
@-webkit-keyframes ball-spin-clockwise{
  0%,100%{
    opacity:1;
    -webkit-transform:scale(1);
    transform:scale(1)}
  20%{
    opacity:1
  }
  80%{
    opacity:0;
    -webkit-transform:scale(0);
    transform:scale(0)
  }
}
@-moz-keyframes ball-spin-clockwise{
  0%,100%{
    opacity:1;
    -moz-transform:scale(1);
    transform:scale(1)
  }
  20%{
    opacity:1
  }
  80%{
    opacity:0;
    -moz-transform:scale(0);
    transform:scale(0)
  }
}
@-o-keyframes ball-spin-clockwise{
  0%,100%{
    opacity:1;
    -o-transform:scale(1);
    transform:scale(1)
  }
  20%{
    opacity:1
  }
  80%{
    opacity:0;
    -o-transform:scale(0);
    transform:scale(0)
  }
}
@keyframes ball-spin-clockwise{
  0%,100%{
    opacity:1;
    -webkit-transform:scale(1);
    -moz-transform:scale(1);
    -o-transform:scale(1);
    transform:scale(1)
  }
  20%{
    opacity:1
  }
  80%{
    opacity:0;
    -webkit-transform:scale(0);
    -moz-transform:scale(0);
    -o-transform:scale(0);transform:scale(0)
  }
}
</style>