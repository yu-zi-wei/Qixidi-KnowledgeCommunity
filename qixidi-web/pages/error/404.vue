<template>
  <body>

  <!-- Error Page -->
  <div class="error">
    <div class="container-floud">
      <div class="col-xs-12 ground-color text-center">
        <div class="container-error-404">
          <div class="clip">
            <div class="shadow"><span class="digit thirdDigit">4</span></div>
          </div>
          <div class="clip">
            <div class="shadow"><span class="digit secondDigit">0</span></div>
          </div>
          <div class="clip">
            <div class="shadow"><span class="digit firstDigit">4</span></div>
          </div>
          <div class="msg">OH!<span class="triangle"></span></div>
        </div>
        <h2>页面走丢了！
          <nuxt-link class="text-underline" to="/">返回主页</nuxt-link>
        </h2>
      </div>
    </div>
  </div>
  <!-- Error Page -->
  </body>
</template>

<script>
export default {
  name: "error404"
}
</script>

<style scoped>
/* Error Page */
.error .clip .shadow {
  height: 180px; /*Contrall*/
}

.error .clip:nth-of-type(2) .shadow {
  width: 130px; /*Contrall play with javascript*/
}

.error .clip:nth-of-type(1) .shadow, .error .clip:nth-of-type(3) .shadow {
  width: 250px; /*Contrall*/
}

.error .digit {
  width: 150px; /*Contrall*/
  height: 150px; /*Contrall*/
  line-height: 150px; /*Contrall*/
  font-size: 120px;
  font-weight: bold;
}

.error h2 /*Contrall*/
{
  font-size: 32px;
}

.error .msg /*Contrall*/
{
  top: -190px;
  left: 30%;
  width: 80px;
  height: 80px;
  line-height: 80px;
  font-size: 32px;
}

.error span.triangle /*Contrall*/
{
  top: 70%;
  right: 0%;
  border-left: 20px solid #535353;
  border-top: 15px solid transparent;
  border-bottom: 15px solid transparent;
}


.error .container-error-404 {
  margin-top: 10%;
  position: relative;
  height: 250px;
  padding-top: 40px;
}

.error .container-error-404 .clip {
  display: inline-block;
  transform: skew(-45deg);
}

.error .clip .shadow {

  overflow: hidden;
}

.error .clip:nth-of-type(2) .shadow {
  overflow: hidden;
  position: relative;
  box-shadow: inset 20px 0px 20px -15px rgba(150, 150, 150, 0.8), 20px 0px 20px -15px rgba(150, 150, 150, 0.8);
}

.error .clip:nth-of-type(3) .shadow:after, .error .clip:nth-of-type(1) .shadow:after {
  content: "";
  position: absolute;
  right: -8px;
  bottom: 0px;
  z-index: 9999;
  height: 100%;
  width: 10px;
  background: linear-gradient(90deg, transparent, rgba(173, 173, 173, 0.8), transparent);
  border-radius: 50%;
}

.error .clip:nth-of-type(3) .shadow:after {
  left: -8px;
}

.error .digit {
  position: relative;
  top: 8%;
  color: white;
  background: var(--theme-color);
  border-radius: 50%;
  display: inline-block;
  transform: skew(45deg);
}

.error .clip:nth-of-type(2) .digit {
  left: -10%;
}

.error .clip:nth-of-type(1) .digit {
  right: -20%;
}

.error .clip:nth-of-type(3) .digit {
  left: -20%;
}

.error h2 {
  color: #A2A2A2;
  font-weight: bold;
  padding-bottom: 20px;
}

.error .msg {
  position: relative;
  z-index: 9999;
  display: block;
  background: #535353;
  color: #A2A2A2;
  border-radius: 50%;
  font-style: italic;
}

.error .triangle {
  position: absolute;
  z-index: 999;
  transform: rotate(45deg);
  content: "";
  width: 0;
  height: 0;
}

/* Error Page */
@media (max-width: 767px) {
  /* Error Page */
  .error .clip .shadow {
    height: 100px; /*Contrall*/
  }

  .error .clip:nth-of-type(2) .shadow {
    width: 80px; /*Contrall play with javascript*/
  }

  .error .clip:nth-of-type(1) .shadow, .error .clip:nth-of-type(3) .shadow {
    width: 100px; /*Contrall*/
  }

  .error .digit {
    width: 80px; /*Contrall*/
    height: 80px; /*Contrall*/
    line-height: 80px; /*Contrall*/
    font-size: 52px;
  }

  .error h2 /*Contrall*/
  {
    font-size: 24px;
  }

  .error .msg /*Contrall*/
  {
    top: -110px;
    left: 15%;
    width: 40px;
    height: 40px;
    line-height: 40px;
    font-size: 18px;
  }

  .error span.triangle /*Contrall*/
  {
    top: 70%;
    right: -3%;
    border-left: 10px solid #535353;
    border-top: 8px solid transparent;
    border-bottom: 8px solid transparent;
  }

  .error .container-error-404 {
    height: 150px;
  }

  /* Error Page */
}

hr {
  margin: 0px;
  padding: 0px;
  border-top: 1px dashed #999;
}

/*--------------------------------------------FrameWork------------------------*/
</style>
