export default ($axios) => ({
  // 用户基础信息
  getUserBasics() {
    return $axios.get('/frontDesk/user/basics')
  },
  // 用户信息
  getUserInfo() {
    return $axios.get('/frontDesk/user/info')
  },
  // 创作者申请
  applyCreator(data) {
    return $axios.post('/frontDesk/user/creator/application', data)
  },
  // 更新邮箱
  updateEmail(data) {
    return $axios.put('/frontDesk/user/update/email', data)
  },
  // 更新用户信息
  updateUserInfo(data) {
    return $axios.put('/frontDesk/user/update/info', data)
  },
  // 删除文章
  deleteArticle(id) {
    return $axios.delete(`/user/delete/article/${id}`)
  },
  // 文章列表
  getArticleList(params) {
    return $axios.get('/user/article/list', {params})
  },
  // 插入文章
  insertArticle(data) {
    return $axios.post('/user/insert/article', data)
  },
  // 保存草稿
  saveDraft(data) {
    return $axios.put('/user/save/draft', data)
  },
  // 最近文章列表
  getLatelyArticleList(params) {
    return $axios.get('/user/lately/article/list', {params})
  },
  // 获取文章详情
  getArticle(id) {
    return $axios.get(`/user/get/article/${id}`)
  },
  // 取消关注
  cancelFollow(data) {
    return $axios.post('/user/follow/cancel', data)
  },
  // 添加关注
  addFollow(data) {
    return $axios.post('/user/follow/add', data)
  },
  // 用户统计：用户数
  getCensusUserCount(params) {
    return $axios.get('/user/census/count/user', {params})
  },
  // 用户统计：投
  getCensusSubmission() {
    return $axios.get('/user/census/submission')
  },
  // 用户统计
  getCensusCollection(params) {
    return $axios.get('/user/census/collection', {params})
  },
  // 用户统计
  getCensusSpecial(params) {
    return $axios.get('/user/census/special', {params})
  },
  // 获取关注列表
  getFollowList(type) {
    return $axios.get(`/user/follow/list/${type}`)
  },
  // 用户报表列表
  getUserReportList() {
    return $axios.get('/frontDesk/user/report/list')
  },
  // 用户签到
  report(data) {
    return $axios.post('/frontDesk/user/report', data)
  }
})

