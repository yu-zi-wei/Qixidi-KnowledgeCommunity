# 基础镜像使用java
FROM java:8
# VOLUME 指定了临时文件目录为/tmp。
# 作者
MAINTAINER petrichor <tomorrow@qq.com>
# 将xxxxxx.jar包添加到容器中并更名为app.jar
ADD aurora-admin.jar app.jar
# 运行jar包
RUN bash -c 'touch /app.jar'
ENTRYPOINT ["java","-jar","/app.jar"]
###声明启动端口号
EXPOSE 9001
