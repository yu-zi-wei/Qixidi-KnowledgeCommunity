# qixidi-service启动文档

> 前提：你有完整的java开发环境，以及数据库环境、Redis环境

1、在本地创建数据库 `qixidi_admin`，执行仓库根目录 `deploy/db/qixidi_admin.sql` 生成对应的数据表和初始数据。

2、在qixidi-startup的resources目录下创建applicationConfig/dev文件。

3、将仓库根目录 `deploy/config/dev/config.properties` 拷贝至applicationConfig/dev。

4、修改config.properties文件的配置参数，如数据库用户名、密码、Redis、AI apiKey等。

5、启动qixidi-service项目。

## 打包

```
## 打生产环境包
mvn clean package -P prod -DskipTests
```

### 数据表文档

b_article_information

- id：（-11：关于栖息地）
- id：（-12：友链）
- id：（-13：关于作者）
