## 开发

```bash

# 进入项目目录
cd aurora-ui

# 安装依赖
npm install

# 建议不要直接使用 cnpm 安装依赖，会有各种诡异的 bug。可以通过如下操作解决 npm 下载速度慢的问题
npm install --registry=https://registry.npmmirror.com

# 启动服务
npm run dev

# 账号密码
admin/123456
```

浏览器访问 http://localhost:81

## 发布

```bash
# 构建测试环境
npm run build:stage

# 构建生产环境
npm run build:prod

## 上传dist.zip并解压
cd /data/aurora/web/aurora-ht/
unzip -u dist.zip

# 重启nginx
/data/app/nginx/sbin/nginx -s reload

# nginx启动停止命令
./sbin/nginx   ##命令启动
./sbin/nginx -s stop  ## 停止命令

## 用于重启
./sbin/nginx -t   ##检查 配置是否正确
./sbin/nginx -s reload  ## 重启
```
