export default (ctx, inject) => {
  const { $axios } = ctx

  const repositories = {}

  // 1. 自动化扫描 api/modules 目录下的所有 .js 文件
  const files = require.context('./modules', false, /\.js$/)

  files.keys().forEach((key) => {
    // 2. 获取文件名作为模块名 (例如: ./site.js -> site)
    const moduleName = key.replace(/^\.\/(.*)\.\w+$/, '$1')

    // 3. 导入模块定义
    const moduleConfig = files(key).default

    // 4. 执行初始化并挂载
    if (typeof moduleConfig === 'function') {
      repositories[moduleName] = moduleConfig($axios)
    }
  })

  // 注入到 Nuxt 实例中
  inject('api', repositories)
}
