<template>
  <div>
    <dictumList :album-id="albumId==null?null:parseInt(albumId)" :isSelf="true" :uid="uuid"></dictumList>
  </div>
</template>

<script>

export default {
  name: "albumList",
  data() {
    return {
      albumId: this.$route.query.code == null ? null : this.$base64.decode(this.$route.query.code),
      uuid: this.$route.query.uuid == null ? null : this.$base64.decode(this.$route.query.uuid)
    }
  },
  watch: {
    // 监听路由是否变化
    '$route'() {
      this.albumId = this.$route.query.code == null ? null : this.$base64.decode(this.$route.query.code)
      this.uuid = this.$route.query.uuid == null ? null : this.$base64.decode(this.$route.query.uuid)
    },
  },
  methods: {},

}
</script>

<style scoped>

</style>
