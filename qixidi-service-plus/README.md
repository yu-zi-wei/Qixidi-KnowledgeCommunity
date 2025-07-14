# qixidi-service启动文档

> 前提：你有完整的java开发环境，以及数据库环境

1、加作者微信：zsh2978824265，获取获取数据库脚本

2、在本地创建数据库“aurora_admin”，并执行SQL脚本生成对应的数据表。

3、在qixidi-startup的resources目录下创建applicationConfig/dev文件

4、将resources模块下的config.properties拷贝至applicationConfig/dev并自行修改配置

5、修改config.properties文件的配置参数，如数据库用户名、密码等。

6、启动qixidi-service项目。

## 打包
```
## 打生产环境包
mvn clean package -P prod
```

### 数据表文档
b_article_information
- id：（-11：关于作者）
- id：（-12：友链）
- id：（-13：作者）
