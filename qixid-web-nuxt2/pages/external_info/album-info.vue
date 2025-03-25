<template>
  <div class="module-main-2">
    <div class="album-info">
      <el-skeleton class="mt-10 mb-10" :rows="8" animated v-if="loading"/>
      <div v-if="!loading" class="flex-left">
        <div class="img-info-div">
          <el-image class="img-info" v-if="info.cover" :src="info.cover" fit="cover"></el-image>
          <el-image class="img-info" v-else src="/img/shu.jpg" fit="cover"></el-image>
        </div>
        <div>
          <div class="ml-20">
            <div class="font-s-20 font-bold">
              <svg t="1682497983576" class="icon icon-size-32 icon-theme svg-translateY-6" viewBox="0 0 1024 1024"
                   version="1.1"
                   xmlns="http://www.w3.org/2000/svg"
                   p-id="1424">
                <path
                  d="M876.031892 284.340083V88.000564c0-15.461511-6.574755-24.095393-20.988641-29.803146-11.126508-4.443378-21.964016-1.770126-35.041275 1.697876L90.601566 284.340083V901.572285c0 30.959148 25.070768 56.029916 56.029916 56.029916H876.031892c30.959148 0 56.138291-25.070768 56.138291-56.029916V340.478374c0-30.959148-25.179143-56.138291-56.138291-56.138291z m-56.029916-168.306498v168.306498H272.88845l547.113526-168.306498z m56.029916 533.06089v28.033021h-112.168208v-28.033021-28.10527h112.168208v28.10527z m0-84.243561h-140.201228c-15.569886 0-28.105271 12.643759-28.105271 28.10527v112.204332c0 15.497636 12.535384 28.105271 28.105271 28.105271h140.201228v140.201228c0 15.461511-12.535384 28.105271-28.033021 28.10527H174.736753c-15.461511 0-28.105271-12.643759-28.105271-28.10527V340.478374h701.367389c15.497636 0 28.033021 12.535384 28.033021 28.033021v196.339519z"
                  p-id="1425"></path>
              </svg>
              <span class="ml-6 color-grey5">{{ info.name }}</span>
            </div>
            <div class="mt-15">
              <el-tag class="mr-10" size="medium">搜录 --</el-tag>
              <el-tag class="mr-10" type="warning" size="medium">关注 --</el-tag>
              <el-tag class="mr-10" size="medium" type="danger">点赞 --</el-tag>
            </div>
            <div class="mt-15">
              <div class="color-grey font-s-14">
                创建时间：
                {{ $utils.parseTime(info.createTime, '{y}年{m}月{d}日') }}
              </div>
              <div class="mt-10">介绍:
                <span class="font-s-14 color-grey">{{ info.briefIntroduction }}</span>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
    <div class="mt-20">
      <dictumList
        v-if="!loading"
        :album-id="info.id"
        :typography="2"></dictumList>
    </div>
  </div>
</template>

<script>

export default {
  name: "albumInfo",
  data() {
    return {
      loading: true,
      cover: "",
      id: this.$route.query.data,
      info: null,
    }
  },
  methods: {
    getDictumAlbums() {
      if (this.id == null) {
        this.$router.push('/index')
        return;
      }
      this.loading = true;
      this.$API("/white/dictum/album/" + this.id, "get").then(res => {
        this.info = res.data;
      }).finally(() => this.loading = false)
    }
  },
  mounted() {
    this.getDictumAlbums()

  }
}
</script>

<style scoped>
.img-info-div {
  width: 160px;
  height: 160px;
  overflow: hidden;
  border-radius: 4px;
}

.img-info {
  width: 100%;
  height: 100%;
  transition: .6s;;
}

.img-info:hover {
  transform: scale(1.2);
}

.album-info {
  background-color: #FFFFFF;
  padding: 20px;
  border-radius: 0 0 20px 20px;
  border-bottom: 4px solid #95a5a6;
}
</style>
