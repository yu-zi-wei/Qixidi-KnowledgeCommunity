export default ($axios) => ({
  // 网站统计数据
  getSiteTotalData() {
    return $axios.get('/white/site/total-data')
  },
  // 网站信息
  getSiteInfo() {
    return $axios.get('/white/site/info')
  },
  // 选中的文�?
  getSelectedArticles() {
    return $axios.get('/white/article/selected')
  },
  // 标签分组列表
  getLabelGroupingList(params) {
    return $axios.get('/white/label/grouping/list', { params })
  },
  // 侧边栏列�?
  getSidebarList(params) {
    return $axios.get('/white/configure/sidebar/list', { params })
  },
  // 导航列表
  getNavigationList(params) {
    return $axios.get('/white/configure/navigation/list', { params })
  },
  // 文章基础信息
  getArticleBasic(id) {
    return $axios.get(`/white/article/basic/${id}`)
  },
  // 文章评论列表
  getArticleCommentList(params) {
    return $axios.get('/white/article/comment/list', { params })
  },
  // 友情链接
  getFriendLink() {
    return $axios.get('/white/site/friend-link')
  },
  // 特别推荐列表
  getSpecialList(params) {
    return $axios.get('/white/aut/special/list', { params })
  },
  // 文章详情
  getArticleDetails(id) {
    return $axios.get(`/white/article/details/${id}`)
  },
  // 相关文章
  getRelatedArticleList(params) {
    return $axios.get('/white/article/related/list', { params })
  },
  // 增加浏览记录
  addBrowseCount(id, labelId) {
    return $axios.get(`/white/article/add/browse-count/${id}/${labelId}`)
  },
  // 收藏列表
  getCollectionList(uuid) {
    return $axios.get(`/white/collection/list/${uuid}`)
  },
  // 用户文章列表
  getUserArticleList(params) {
    return $axios.get('/white/article/user/list', { params })
  },
  // 文章列表 (搜索/推荐列表
  getArticleList(params) {
    return $axios.get('/white/article/list', { params })
  },
  // 语录评论列表
  getDictumCommentList(id) {
    return $axios.get(`/white/dictum/comment/list/${id}`)
  },
  // 语录信息列表
  getDictumInfoList(params) {
    return $axios.get('/white/dictum/info/list', { params })
  },
  // 反馈详情
  getFeedbackById(id) {
    return $axios.get(`/white/feedback/byId/${id}`)
  },
  // 反馈列表
  getFeedbackList(params) {
    return $axios.get('/white/feedback/list', { params })
  },
  // 反馈状态汇总
  getFeedbackStatusSum() {
    return $axios.get('/white/feedback/status/sum')
  },
  // 专题详情
  getSpecialInfo(id) {
    return $axios.get(`/white/special/${id}`)
  },
  // 用户信息
  getUserInfo(uuid) {
    return $axios.get(`/white/user/info/${uuid}`)
  },
  // 专题列表
  getSpecialSelect(params) {
    return $axios.get('/white/select/special', { params })
  },
  // 更新专题
  updateSpecial(specialId, userId, data) {
    return $axios.post(`/white/update/special/${specialId}/${userId}`, data)
  },
  // 删除专题
  deleteSpecial(id) {
    return $axios.delete(`/white/delete/special/${id}`)
  },
  // 时间轴列表
  getTimeNotesList(data) {
    return $axios.post('/white/time/notes/list', data)
  },
  // 时间轴详情
  getTimeNotesInfo(id) {
    return $axios.get(`/white/time/notes/getInfo/${id}`)
  },
  // 文章归档
  getArticleArchive(params) {
    return $axios.get('/white/article/archive', { params })
  },
  // 推荐文章列表
  getRecommendArticleList(params) {
    return $axios.get('/white/article/recommend/list', { params })
  },
  // 推荐语录专辑
  getRecommendedDictumAlbum() {
    return $axios.get('/white/dictum/recommended/album')
  },
  // 热门语录作记录
  getPopularAuthors() {
    return $axios.get('/white/dictum/popular/authors')
  },
  // 热门语录标签
  getPopularLabel() {
    return $axios.get('/white/dictum/popular/label')
  },
  // 工具列表
  getToolList() {
    return $axios.get('/white/configure/tool/list')
  },

  // 工具子列表
  getToolChildList(params) {
    return $axios.get('/white/configure/tool/child/list', { params })
  },
  // 语录分组列表
  getDictumGroupList() {
    return $axios.get('/white/dictum/group/list')
  },
  // 系统标签
  getSystemLabel(params) {
    return $axios.get('/white/dictum/system/label', { params })
  },
  // 关注列表
  getFollowList(uid, type) {
    return $axios.get(`/white/user/follow/list/${uid}/${type}`)
  },
  // 所有用户列表
  getUserAllList() {
    return $axios.get('/white/userAllList')
  },
  // 关注的文章列表
  getFollowArticleList(params) {
    return $axios.get('/white/article/follow/list', { params })
  },
  // 文章标签列表
  getArticleLabelList(params) {
    return $axios.get('/white/article/label/list', { params })
  },
  // 文章排序列表
  getArticleSort(params) {
    return $axios.get('/white/article/sort', { params })
  },
  // 收藏夹详情
  getCollectionInfo(id) {
    return $axios.get(`/white/collection/${id}`)
  },
  // 收藏夹文章列表
  getCollectionArticleList(params) {
    return $axios.get('/white/collection/article/list', { params })
  },
  // 标签列表
  getLabelList(params) {
    return $axios.get('/white/label/list', { params })
  },
  // 用户搜索列表
  getUserList(params) {
    return $axios.get('/white/user/list', { params })
  },
  // 语录专辑列表
  getDictumAlbumList(params) {
    return $axios.get('/white/dictum/album/list', { params })
  },
  // 语录专辑详情
  getDictumAlbumInfo(id) {
    return $axios.get(`/white/dictum/album/${id}`)
  },
  // 标签分组详情
  getLabelGroupingInfo(id) {
    return $axios.get(`/white/label/grouping/info/${id}`)
  },
  // 标签详情
  getLabelInfo(id, type) {
    return $axios.get(`/white/label/info/${id}/${type}`)
  },
  // 用户数据详情
  getUserData(uuid) {
    return $axios.get(`/white/user/data/${uuid}`)
  }
})


