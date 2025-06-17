import env from './env'
import {encode} from 'base-64';

export default {
  // Global page headers: https://go.nuxtjs.dev/config-head
  server: {
    port: 9006,
    host: '0.0.0.0'
  },
  head: {
    title: '栖息地',
    htmlAttrs: {
      lang: 'en'
    },
    meta: [
      {charset: 'utf-8'},
      {name: 'viewport', content: 'width=device-width, initial-scale=1'},
      {
        hid: 'description',
        name: 'description',
        content: '栖息地知识社区，知识社区，钰紫薇，钰紫薇博客，知识社区，论坛，心灵栖息地'
      },
      {name: 'format-detection', content: 'telephone=no'},
      {
        hid: 'keywords',
        name: 'keywords',
        content: "栖息地知识社区，知识社区，钰紫薇，钰紫薇博客，知识社区，论坛，心灵栖息地"
      },
      {name: "baidu-site-verification", content: "codeva-vo8bZBYJNH"}
    ],
    link: [
      {rel: 'icon', type: 'image/x-icon', href: '/favicon.ico'}//ico配置
    ],
    script: [
      {src: 'https://hm.baidu.com/hm.js?c58934a7bcf2e29f8f3eb821b7a8293e'}
    ]
  },
  env: {
    NODE_ENV: env[process.env.NODE_ENV].NODE_ENV,
    // 请求服务器地址
    SERVER_URL: env[process.env.NODE_ENV].SERVER_URL,
    //主题颜色
    THEME_COLOR: env[process.env.NODE_ENV].THEME_COLOR,
    //服务端协议
    SERVICE_PROTOCOL: env[process.env.NODE_ENV].SERVICE_PROTOCOL,
    //webSocket 协议
    WEBSOCKET_PROTOCOL: env[process.env.NODE_ENV].WEBSOCKET_PROTOCOL,
    //项目名称
    PROJECT_NAME: "栖息地",
    //登录中转地址
    LOGIN_TRANSFER: "/transfer/login",
    //登出中转地址
    LOG_OUT_TRANSFER: "/transfer/log-out",
  },

  // Global CSS: https://go.nuxtjs.dev/config-css
  css: [
    '~/static/css/reset.css',//浏览器配置

    //---------element-ui配置--------
    '~/static/css/element-ui/index.css',//主题配置

    // "element-ui/lib/theme-chalk/index.css", //默认配置
    //---------全局css配置---------
    '~/static/css/common/index.css',
    '~/static/css/common/color.css',
    '~/static/css/common/layout.css',//布局css
    '~/static/css/common/svg.css',
    '~/static/css/common/element.css',
    '~/static/css/common/module.css',// 通用组件 css
    '~/static/css/common/theme.css',//主题配置
    '~/static/vditor/dist/index.css'//vditor
  ],
  // Plugins to run before rendering page: https://go.nuxtjs.dev/config-plugins
  plugins: [
    '~/plugins/axios.js',  //axios 请求拦截
    '~/plugins/element-ui/element-ui.js',  //element-ui
    '~/plugins/modal.js',  //提示
    '~/plugins/base64.js',  //base64加解密
    '~/plugins/utils.js',
    '~/plugins/init.js',//全局配置
    '~/plugins/mavon-editor.js',//富文本编辑器
    //=================api==================
    '~/api/API.js', // axios 请求封装
    '~/api/requestType.js', // axios 请求类型封装
    '~/plugins/baiduGa.js',//百度统计
    {src: '~/plugins/cropper.js', mode: 'client'},//只在客户端渲染
    {src: '~/plugins/waterfall2.js', mode: 'client'},//只在客户端渲染
  ],
  // mixins: [
  //   '~/plugins/utils.js',
  // ],
  // Auto import components: https://go.nuxtjs.dev/config-components
  components: true,

  // Modules for dev and build (recommended): https://go.nuxtjs.dev/config-modules
  buildModules: [],

  // Modules: https://go.nuxtjs.dev/config-modules
  modules: [
    '@nuxtjs/axios',
    '@nuxtjs/proxy',
    'cookie-universal-nuxt',
    '@nuxtjs/feed',
  ],

  feed: [
    {
      path: '/rss.xml',
      async create(feed) {
        // 获取环境配置
        const envConfig = env[process.env.NODE_ENV];
        //网站域名
        const domainNameUrl = `${envConfig.SERVICE_PROTOCOL}/qixidi.top`
        // 获取当前年份
        const currentYear = new Date().getFullYear();
        // 设置 RSS 订阅源的基本信息
        feed.options = {
          title: '栖息地知识社区',
          description: '栖息地知识社区，知识社区，钰紫薇，钰紫薇博客，知识社区，论坛，心灵栖息地',
          link: `${domainNameUrl}/rss.xml`,
          language: 'zh-CN',
          copyright: `Copyright © ${currentYear} 栖息地知识社区. All rights reserved.`, // 添加版权信息
          image: `${domainNameUrl}/static/img/logo.png`, // 添加网站图标
        };
        try {
          const https = require('https');
          const response = await fetch(`${envConfig.SERVICE_PROTOCOL}${envConfig.SERVER_URL}/white/article/recommend/list`, {
            //不做https校验，如果你的https是被信任的建议注释该代码，因为http是不安全的
            agent: new https.Agent({rejectUnauthorized: false})
          });
          const data = await response.json();
          const articles = data.rows || [];
          articles.forEach(article => {
            // 为每篇文章添加到 RSS 订阅源中
            feed.addItem({
              title: article.articleTitle,
              id: article.id,
              link: `${domainNameUrl}/article-details/${encode(article.id)}`, // 文章的链接
              description: article.articleAbstract,
              date: new Date(article.createTime), // 文章创建时间
            });
          });
        } catch (error) {
          console.error('Error fetching articles:', error);
          feed.addItem({
            title: 'Error Fetching Articles',
            id: 'error',
            link: `${domainNameUrl}`,
            description: 'There was an error fetching the latest articles. Please try again later.',
            content: 'There was an error fetching the latest articles. Please try again later.',
            date: new Date(),
          });
        }
      },
      cacheTime: 1000 * 60 * 15, // 缓存时间，15 分钟
      type: 'rss2', // 订阅源类型，这里使用 RSS 2.0
    },
  ],

  axios: {
    proxy: true,
  },
  proxy: { // axios 配置
    '/api': {
      changeOrigin: true, // 是否跨域
      target: env[process.env.NODE_ENV].SERVICE_PROTOCOL + env[process.env.NODE_ENV].SERVER_URL,
      pathRewrite: {
        '^/api': '', //请求前缀规则匹配
      }
    },
  },
  loading: {//请求加载配置
    color: env[process.env.NODE_ENV].THEME_COLOR,
    height: '2px'
  },
  router: {
    middleware: 'auth', //路由守卫配置
    // extendRoutes(routes, resolve) {
    //   // 添加通用的动态路由处理逻辑
    //   routes.push({
    //     path: '*',
    //     component: resolve(__dirname, 'pages/error/404.vue')
    //   })
    // }
  },
  // Build Configuration: https://go.nuxtjs.dev/config-build
  build: {
    extractCSS: {allChunks: true},// 将内嵌的样式提取到外部，解决查看网页源代码是出现css问题
    extend(config, {isDev, isClient}) {
      config.module.rules.push({
        // test: /\.js$/,
        test: /\.(js|mjs)$/,
        include: /node_modules/,
        type: 'javascript/auto',
        use: {
          loader: 'babel-loader',
          options: {
            presets: [
              // 确保这里的预设能处理 aieditor 模块中的语法
              ['@babel/preset-env']
            ],
          }
        }
      });
    },
    // 防止多次打包
    vendor: ['element-ui', 'axios'],
    optimization: {
      splitChunks: {
        chunks: 'all',
      },
    },
  }
}
