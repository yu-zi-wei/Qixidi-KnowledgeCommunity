# aurora-nuxt2

## 启动，部署文档

```bash
# install dependencies
npm install
npm install --registry=http://registry.npmmirror.com

# serve with hot reload at localhost:3000
npm run dev

# 打包
# 打包之前建议先删除.nuxt文件
npm run build
## 部署上线
# 1、在其他地方新建一个文件夹：nux2-deployment
# 2、将 .nuxt、package.json、nuxt.config.js、static文件拷贝到nux2-deployment
# 3、将nux2-deployment上传到服务器
# 4、解压nux2-deployment.zip文件，unzip -u nux2-deployment.zip
# 5、安装依赖(后续更新，如果package.json没有发生变化，无需执行这一步)，在项目文件跟目录执行
# npm install --registry=http://registry.npmmirror.com
# 6、安装pm2：执行 npm i pm2 -g
# 7、提供pm2来启动项目，管理项目:执行 pm2 start npm --name "qixidi" -- run start
# 5、提供nginx代理端口即可

## 项目更新
# 如果没有添加新的配置，只需要上传.nuxt文件和其他修改过的文件即可
# 如果有添加新的配置，需要上传所有文件，并执行npm install拉取配置
# 执行pm2 reload qixidi命令，重新加载文件
pm2 reload qixidi
# 启动
pm2 start npm --name "qixidi" -- run start

#查看与监视进程
pm2 list #显示所有进程信息；
pm2 show 11 || pm2 info 11 #查看进程id为 11 的详细信息；
pm2 monit #进入监视页面，监视每个node进程的CPU和内存的使用情况。


#重载、重启
pm2 restart 11 #重启id为 11 的进程；
pm2 restart all #重启所有进程；
pm2 reload 11 #0秒停机重载id为 11 进程（用于 NETWORKED 进程）；
pm2 reload all #重载所有进程；

#停止、删除进程
pm2 stop/delete 11 #停止/删除id为 11 的进程；
pm2 stop/delete all #停止/删除所有进程；

#日志操作
pm2 logs #显示所有进程的日志；
pm2 logs 11 #显示进程id为 11 的日志；
pm2 flush #清空所有日志文件；
pm2 reloadLogs #重载所有日志；
pm2 startup #产生 init 脚本，保持进程活着；

#杀进程
pm2 kill #杀死pm2进程
```

# nginx 相关
```shell
#检查配置
/data/app/nginx/sbin/nginx -t
# 重启nginx
/data/app/nginx/sbin/nginx -s reload
```