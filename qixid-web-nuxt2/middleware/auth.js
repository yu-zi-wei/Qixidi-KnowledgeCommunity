//白名单
const whiteList = [
  '/',//主页
  '/tool',
  '/test',
  '/label',
  '/introduce',
  '/friend_link',
  '/about_author',
  '/issues',
  '/content',
  '/time_notes',
  '/error/**',
  '/search/**',
  '/article/**',
  '/login',
  '/dictum',
  '/xxx',
  '/transfer/**',
  '/last-article',
  '/user_home/**',
  '/popular-group/**',
  '/article-details/**',
  '/external_info/**',
  '/dictum/dictum-details/**',
  '/time_notes/details/**',
  '/article_archive',

];

export default ({store, route, redirect, params, query, req, res}) => {
  if (route.path == "/") {
    redirect('/content')
  }
  if (!route.matched.length) {
    redirect('/error/404')
  }

  let url = route.path;
  let startsWithWhiteList = false;
  for (let i = 0; i < whiteList.length; i++) {
    const item = whiteList[i];
    if (item.endsWith('**')) {
      const prefix = item.slice(0, -2);
      if (url.startsWith(prefix)) {
        startsWithWhiteList = true;
        break;
      }
    } else if (url === item) {
      startsWithWhiteList = true;
      break;
    }
  }
  store.commit('getToken');
  if (!store.state.token && !startsWithWhiteList) {
    redirect('/');
  }
}
