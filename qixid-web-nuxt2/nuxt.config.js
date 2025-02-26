import env from './env'

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
      {hid: 'description', name: 'description', content: '栖息地知识社区，栖息地社区，知识社区，论坛，心灵栖息地'},
      {name: 'format-detection', content: 'telephone=no'},
      {hid: 'keywords', name: 'keywords', content: "栖息地知识社区，极光社区，知识社区，论坛，心灵栖息地"},
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
