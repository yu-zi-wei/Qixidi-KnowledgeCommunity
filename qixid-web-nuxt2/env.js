const defaultConfig = {
  //默认主题颜色
  THEME_COLOR: '#5C56E3',
}

const config = {
  ...defaultConfig,
  //开发环境
  dev: {
    NODE_ENV: 'dev',
    //文章分享地址
    ARTICLE_SHARE_URL: 'http://localhost:3000/article/article-details',
    // 代理的后端请求服务器地址
    // SERVER_URL: '127.0.0.1:9001',
    SERVER_URL: 'server.qixidi.top',
    THEME_COLOR: defaultConfig.THEME_COLOR,
  },
  //测试环境
  test: {
    NODE_ENV: 'test',
    //文章分享地址
    ARTICLE_SHARE_URL: 'http://localhost:3000/article/article-details',
    // 代理的后端请求服务器地址
    SERVER_URL: '127.0.0.1:9001',
    THEME_COLOR: defaultConfig.THEME_COLOR,
  },
  //生产环境
  prod: {
    NODE_ENV: 'prod',
    //文章分享地址
    ARTICLE_SHARE_URL: 'https://qixidi.top/article/article-details',
    // 代理的后端请求服务器地址
    SERVER_URL: 'server.qixidi.top',
    THEME_COLOR: defaultConfig.THEME_COLOR,
  },
}

export default config;
