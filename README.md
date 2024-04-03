# lover

## 安装依赖

```
npm install

# 建议不要直接使用 cnpm 安装依赖，会有各种诡异的 bug。可以通过如下操作解决 npm 下载速度慢的问题

npm install --registry=https://registry.npmmirror.com

### Compiles and hot-reloads for development
```
## 启动
```
npm run dev
```

### Compiles and minifies for production

## 生产环境打包

```bash
npm run build

# 重启nginx
/data/app/nginx/sbin/nginx -s reload

# nginx启动停止命令
./sbin/nginx   ##命令启动
./sbin/nginx -s stop  ## 停止命令

## 用于重启
./sbin/nginx -t   ##检查 配置是否正确
./sbin/nginx -s reload  ## 重启
```

### Lints and fixes files

```
npm run lint
```

### Customize configuration

See [Configuration Reference](https://cli.vuejs.org/config/).
