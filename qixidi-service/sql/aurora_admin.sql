/*
 Navicat Premium Data Transfer

 Source Server         : localhost
 Source Server Type    : MySQL
 Source Server Version : 80027
 Source Host           : localhost:3306
 Source Schema         : aurora_admin

 Target Server Type    : MySQL
 Target Server Version : 80027
 File Encoding         : 65001

 Date: 10/12/2024 20:30:23
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for b_article_comment
-- ----------------------------
DROP TABLE IF EXISTS `b_article_comment`;
CREATE TABLE `b_article_comment`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'id',
  `article_id` bigint NOT NULL COMMENT '文章id',
  `uid` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_cs_0900_ai_ci NOT NULL COMMENT '文章用户id',
  `parent_id` bigint NOT NULL COMMENT '父级评论id',
  `comment_grade` int NOT NULL DEFAULT 1 COMMENT '评论等级（1：一级，2：二级，3：三级及以下）',
  `target_id` bigint NULL DEFAULT NULL COMMENT '目标id',
  `target_uid` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_cs_0900_ai_ci NOT NULL COMMENT '目标用户id',
  `comment_uid` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_cs_0900_ai_ci NOT NULL COMMENT '评论人id',
  `content` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_cs_0900_ai_ci NOT NULL COMMENT '评论内容',
  `type` int NOT NULL COMMENT '评论类型（1：文章，2：评论）',
  `state` int NOT NULL DEFAULT 0 COMMENT '评论状态（0：正常，1：已删除）',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT NULL COMMENT '删除时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_cs_0900_ai_ci COMMENT = '文章评论表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of b_article_comment
-- ----------------------------

-- ----------------------------
-- Table structure for b_article_information
-- ----------------------------
DROP TABLE IF EXISTS `b_article_information`;
CREATE TABLE `b_article_information`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '文章id',
  `user_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_cs_0900_ai_ci NOT NULL COMMENT '作者id',
  `article_title` varchar(160) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '文章标题',
  `article_cover` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '文章封面',
  `article_abstract` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '文章摘要',
  `article_content` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '文章内容',
  `article_content_md` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_cs_0900_ai_ci NULL COMMENT '文章内容md格式',
  `theme` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '主题',
  `type` int NOT NULL DEFAULT 1 COMMENT '文章类型（1：原创，2：转载）',
  `platform_type` int NOT NULL DEFAULT 1 COMMENT '平台类型（1：文章，2：关于作者）',
  `reprint_url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '转载地址',
  `state` int NOT NULL DEFAULT 0 COMMENT '文章状态（0：正常，1：已删除）',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_cs_0900_ai_ci NULL DEFAULT NULL COMMENT '修改者id',
  `update_time` datetime NULL DEFAULT NULL COMMENT '修改时间',
  `special_id` bigint NULL DEFAULT NULL COMMENT '专栏id',
  `label_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '标签（多个以逗号隔开）',
  `grouping_id` bigint NULL DEFAULT NULL COMMENT '分类id',
  `like_times` bigint NOT NULL DEFAULT 0 COMMENT '点赞次数',
  `comment_times` bigint NOT NULL DEFAULT 0 COMMENT '评论次数',
  `collection_times` bigint NOT NULL DEFAULT 0 COMMENT '收藏次数',
  `number_times` bigint NOT NULL DEFAULT 0 COMMENT '浏览次数',
  `is_public` int NOT NULL DEFAULT 1 COMMENT '是否公开（1：公开，2：不公开）',
  `audit_state` int NOT NULL DEFAULT 1 COMMENT '审核状态（1：审核中，2：审核通过，3：审核不通过，4：草稿）',
  `audit_time` datetime NULL DEFAULT NULL COMMENT '审核时间',
  `heat_weight` double NOT NULL DEFAULT 0 COMMENT '热度权重',
  PRIMARY KEY (`id`) USING BTREE,
  FULLTEXT INDEX `article_information`(`article_title`) WITH PARSER `ngram`,
  FULLTEXT INDEX `article_abstract`(`article_abstract`)
) ENGINE = InnoDB AUTO_INCREMENT = 62 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_cs_0900_ai_ci COMMENT = '文章信息表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of b_article_information
-- ----------------------------
INSERT INTO `b_article_information` VALUES (-12, '1636168030713155584', '友链', NULL, '关于友链交换1、关于友情链接，如果贵站想和本站建立友好关系，欢迎各位站长朋友们来此交换友情链接！2、欢迎合法的站点交换友情链接，提交链接前请确认你的网站没有违反中华人民共和国法律、中华人民共和国澳门特别行政区基本法、中华人民共和国香港特别行', '<h2>关于友链交换</h2><p>1、关于友情链接，如果贵站想和本站建立友好关系，欢迎各位站长朋友们来此交换友情链接！</p><p>2、欢迎合法的站点交换友情链接，提交链接前请确认你的网站没有违反中华人民共和国法律、中华人民共和国澳门特别行政区基本法、中华人民共和国香港特别行政区法律的内容，且内容积极向上。</p><p>3、将不定期检查死链等无效链接，如果发现贵站取消本站链接或贵站无法访问，我会在通知未果的情况下删除您的链接。<br>个人博客优先，不接受商业类、营销产品类、商城类的网站。</p><h2>申请条件</h2><ol><li><p>得有一定的文章内容（文章数目≥10），稳定运行一段时间（运行3个月）。</p></li><li><p>交换友链前请先添加本站友链到贵站。</p></li><li><p>如果以上条件你都符合，可以到下边申请友链哦，<span style=\"color: rgb(115, 52, 197)\"><strong>紫薇</strong></span> 一般会在48小时内做出回复的！</p></li></ol><p>申请格式，按照下方格式即可：</p><blockquote><p><span style=\"font-family: Overpass, ui-sans-serif, system-ui, -apple-system, BlinkMacSystemFont, Segoe UI, Roboto, Helvetica Neue, Arial, Noto Sans, sans-serif, Apple Color Emoji, Segoe UI Emoji, Segoe UI Symbol, Noto Color Emoji; color: rgb(0, 0, 0); font-size: 16px\">站点名称：xxx</span><br><span style=\"font-family: Overpass, ui-sans-serif, system-ui, -apple-system, BlinkMacSystemFont, Segoe UI, Roboto, Helvetica Neue, Arial, Noto Sans, sans-serif, Apple Color Emoji, Segoe UI Emoji, Segoe UI Symbol, Noto Color Emoji; color: rgb(0, 0, 0); font-size: 16px\">站点链接：xxx</span><a target=\"_blank\" rel=\"noopener noreferrer nofollow\" href=\"https://tkgso.xn--funhttps-yb5mg70eei0ak07jsjymjuc://cravatar.cn/avatar/ba76faef3cc8252bd0433b3515124389?s=512%EF%BF%BC%E7%AB%99%E7%82%B9%E6%8F%8F%E8%BF%B0%EF%BC%9A%E5%BF%83%E6%9C%89%E5%A4%9A%E5%AE%BD%EF%BC%8C%E4%B8%96%E7%95%8C%E5%B0%B1%E6%9C%89%E5%A4%9A%E8%BF%9C\"><br><span style=\"font-family: Overpass, ui-sans-serif, system-ui, -apple-system, BlinkMacSystemFont, Segoe UI, Roboto, Helvetica Neue, Arial, Noto Sans, sans-serif, Apple Color Emoji, Segoe UI Emoji, Segoe UI Symbol, Noto Color Emoji; color: rgb(0, 0, 0); font-size: 16px\">站点Logo：xxx</span><br><span style=\"font-family: Overpass, ui-sans-serif, system-ui, -apple-system, BlinkMacSystemFont, Segoe UI, Roboto, Helvetica Neue, Arial, Noto Sans, sans-serif, Apple Color Emoji, Segoe UI Emoji, Segoe UI Symbol, Noto Color Emoji; color: rgb(0, 0, 0); font-size: 16px\">站点描述：xxx</span></a></p></blockquote><h2><span style=\"color: rgb(0, 176, 80)\">本站友链</span></h2><blockquote><p><span style=\"font-family: Overpass, ui-sans-serif, system-ui, -apple-system, BlinkMacSystemFont, Segoe UI, Roboto, Helvetica Neue, Arial, Noto Sans, sans-serif, Apple Color Emoji, Segoe UI Emoji, Segoe UI Symbol, Noto Color Emoji; color: rgb(0, 0, 0); font-size: 16px\">站点名称：</span><span style=\"font-family: Overpass, ui-sans-serif, system-ui, -apple-system, BlinkMacSystemFont, Segoe UI, Roboto, Helvetica Neue, Arial, Noto Sans, sans-serif, Apple Color Emoji, Segoe UI Emoji, Segoe UI Symbol, Noto Color Emoji; color: rgb(0, 176, 80); font-size: 16px\"><strong>栖息地</strong></span><br><span style=\"font-family: Overpass, ui-sans-serif, system-ui, -apple-system, BlinkMacSystemFont, Segoe UI, Roboto, Helvetica Neue, Arial, Noto Sans, sans-serif, Apple Color Emoji, Segoe UI Emoji, Segoe UI Symbol, Noto Color Emoji; color: rgb(0, 0, 0); font-size: 16px\">站点链接：</span><a target=\"_blank\" rel=\"noopener noreferrer nofollow\" href=\"http://qixidi.top/\">http://qixidi.top/</a><a target=\"_blank\" rel=\"noopener noreferrer nofollow\" href=\"https://tkgso.xn--funhttps-yb5mg70eei0ak07jsjymjuc://cravatar.cn/avatar/ba76faef3cc8252bd0433b3515124389?s=512%EF%BF%BC%E7%AB%99%E7%82%B9%E6%8F%8F%E8%BF%B0%EF%BC%9A%E5%BF%83%E6%9C%89%E5%A4%9A%E5%AE%BD%EF%BC%8C%E4%B8%96%E7%95%8C%E5%B0%B1%E6%9C%89%E5%A4%9A%E8%BF%9C\"><br><span style=\"font-family: Overpass, ui-sans-serif, system-ui, -apple-system, BlinkMacSystemFont, Segoe UI, Roboto, Helvetica Neue, Arial, Noto Sans, sans-serif, Apple Color Emoji, Segoe UI Emoji, Segoe UI Symbol, Noto Color Emoji; color: rgb(0, 0, 0); font-size: 16px\">站点Logo：</span></a><a target=\"_blank\" href=\"http://qixidi.top/img/logo.png\">http://qixidi.top/img/logo.png<br><span style=\"font-family: Overpass, ui-sans-serif, system-ui, -apple-system, BlinkMacSystemFont, Segoe UI, Roboto, Helvetica Neue, Arial, Noto Sans, sans-serif, Apple Color Emoji, Segoe UI Emoji, Segoe UI Symbol, Noto Color Emoji; color: rgb(0, 0, 0); font-size: 16px\">站点描述：</span></a><span style=\"color: rgb(0, 0, 0)\">他强任他强，清风拂山岗。</span></p></blockquote>', '## 关于友链交换\n\n1、关于友情链接，如果贵站想和本站建立友好关系，欢迎各位站长朋友们来此交换友情链接！\n\n2、欢迎合法的站点交换友情链接，提交链接前请确认你的网站没有违反中华人民共和国法律、中华人民共和国澳门特别行政区基本法、中华人民共和国香港特别行政区法律的内容，且内容积极向上。\n\n3、将不定期检查死链等无效链接，如果发现贵站取消本站链接或贵站无法访问，我会在通知未果的情况下删除您的链接。  \n个人博客优先，不接受商业类、营销产品类、商城类的网站。\n\n## 申请条件\n\n1.  得有一定的文章内容（文章数目≥10），稳定运行一段时间（运行3个月）。\n    \n2.  交换友链前请先添加本站友链到贵站。\n    \n3.  如果以上条件你都符合，可以到下边申请友链哦，**紫薇** 一般会在48小时内做出回复的！\n    \n\n申请格式，按照下方格式即可：\n\n> 站点名称：xxx  \n> 站点链接：xxx[  \n> 站点Logo：xxx  \n> 站点描述：xxx](https://tkgso.xn--funhttps-yb5mg70eei0ak07jsjymjuc://cravatar.cn/avatar/ba76faef3cc8252bd0433b3515124389?s=512%EF%BF%BC%E7%AB%99%E7%82%B9%E6%8F%8F%E8%BF%B0%EF%BC%9A%E5%BF%83%E6%9C%89%E5%A4%9A%E5%AE%BD%EF%BC%8C%E4%B8%96%E7%95%8C%E5%B0%B1%E6%9C%89%E5%A4%9A%E8%BF%9C)\n\n## 本站友链\n\n> 站点名称：**栖息地**  \n> 站点链接：[http://qixidi.top/](http://qixidi.top/)[  \n> 站点Logo：](https://tkgso.xn--funhttps-yb5mg70eei0ak07jsjymjuc://cravatar.cn/avatar/ba76faef3cc8252bd0433b3515124389?s=512%EF%BF%BC%E7%AB%99%E7%82%B9%E6%8F%8F%E8%BF%B0%EF%BC%9A%E5%BF%83%E6%9C%89%E5%A4%9A%E5%AE%BD%EF%BC%8C%E4%B8%96%E7%95%8C%E5%B0%B1%E6%9C%89%E5%A4%9A%E8%BF%9C)[http://qixidi.top/img/logo.png  \n> 站点描述：](http://qixidi.top/img/logo.png)他强任他强，清风拂山岗。', NULL, 1, 2, NULL, 0, '2024-09-18 22:23:28', '1636168030713155584', '2024-11-11 09:53:03', NULL, '7', 10, 0, 1, 0, 5, 1, 2, '2024-09-18 22:23:28', 730);
INSERT INTO `b_article_information` VALUES (-11, '1636168030713155584', '关于“栖息地”', NULL, '引言栖息地知识社区，个人程序人生的点滴记录和时光储备。淡泊明志，宁静致远。珍惜原创，矢志不渝。网站介绍栖息地是一个集知识积累、日常记录与互动交流于一体的综合性知识社区平台。我们致力于打造一个温馨、高效、充满启发性的在线空间，让每一位用户都能', '<h1>引言</h1><p>栖息地知识社区，个人程序人生的点滴记录和时光储备。淡泊明志，宁静致远。珍惜原创，矢志不渝。</p><h1>网站介绍</h1><p>栖息地是一个集知识积累、日常记录与互动交流于一体的综合性知识社区平台。我们致力于打造一个温馨、高效、充满启发性的在线空间，让每一位用户都能成为知识的贡献者与受益者。</p><h2>1、定位与特色</h2><h2>2、技术栈</h2><h3>后端</h3><ul><li><p>项目基于开源项目 “RuoYi” 进行二次开发；</p></li><li><p>权限认证框架：Sa-Token、Jwt 支持多终端认证系统；</p></li><li><p>第三方登录框架：Justauth 开箱即用的整合第三方登录的开源组件；</p></li><li><p>分布式任务调度：Xxl-Job 高性能、高可靠、易扩展；</p></li><li><p>长连接：WebSocket；</p></li></ul><h3>前端</h3><ul><li><p>前台主要基于 Nuxt.js 进行开发；</p></li><li><p>UI组件：Element UI；</p></li><li><p>数据可视化：Antv G2Plot；</p></li><li><p>makdown编辑器：<span style=\"color: rgb(0, 0, 0); font-size: 14px\">AiEditor </span><span style=\"font-family: ui-monospace, Menlo, Monaco, Consolas, Liberation Mono, Courier New, monospace; color: rgb(3, 47, 98); font-size: 14px\">(</span><a target=\"_blank\" rel=\"noopener noreferrer nofollow\" href=\"https://aieditor.dev/zh/getting-started.html\">https://aieditor.dev/zh/getting-started.html</a><span style=\"font-family: ui-monospace, Menlo, Monaco, Consolas, Liberation Mono, Courier New, monospace; color: rgb(3, 47, 98); font-size: 14px\">)</span><a target=\"_blank\" rel=\"noopener noreferrer nofollow\" href=\"https://aieditor.dev/zh/getting-started.html\"> </a>；</p></li></ul><h3>第三方应用</h3><ul><li><p>第三方登录（Gitee、微博、QQ[待审核]）；</p></li><li><p>云存储：阿里云OSS；</p></li><li><p>短信：阿里云短信；</p></li><li><p>支付：支付宝SDK；</p></li></ul><h3>技术选型架构图</h3><div style=\"text-align:left\"><img src=\"https://zsh-aurora.oss-cn-beijing.aliyuncs.com/aurora/2024/07/10/3a3dc98ffb3a4781982b83fb90b04704.png\" alt=\"image.png\" width=\"100%\" data-align=\"left\"></div><h2>4、开源地址</h2><blockquote><p><a target=\"_blank\" rel=\"noopener noreferrer nofollow\" href=\"https://gitee.com/yu-zi-wei/qixidi\">https://gitee.com/yu-zi-wei/qixidi</a></p></blockquote><h1>作者介绍</h1><h2>1、关于作者</h2><blockquote><p>昵称：钰紫薇<br>性别：♂<br>生日：2001.09<br>所在城市：成都<br>学历：普本<br>邮箱：2978824265@qq.com</p></blockquote><h2>2、技术专长</h2><p><strong>擅长：</strong> Java、javaScript、Mysql、Redis、Linux、Docker、数据结构与算法、服务端开发，(技能树持续点亮中…)<br><strong>常用技术栈：</strong> Spring全家桶、Mybatis-plus、Spring Cloud Alibaba 及周边组件、RocktMQ、Vue、Nuxt、uniapp</p><h1>未来展望</h1><h1>结语</h1><p>网站会持续优化和推出新功能，以丰富网站内容和满足日常使用。</p>', '# 引言\n\n栖息地知识社区，个人程序人生的点滴记录和时光储备。淡泊明志，宁静致远。珍惜原创，矢志不渝。\n\n# 网站介绍\n\n栖息地是一个集知识积累、日常记录与互动交流于一体的综合性知识社区平台。我们致力于打造一个温馨、高效、充满启发性的在线空间，让每一位用户都能成为知识的贡献者与受益者。\n\n## 1、定位与特色\n\n## 2、技术栈\n\n### 后端\n\n-   项目基于开源项目 “RuoYi” 进行二次开发；\n    \n-   权限认证框架：Sa-Token、Jwt 支持多终端认证系统；\n    \n-   第三方登录框架：Justauth 开箱即用的整合第三方登录的开源组件；\n    \n-   分布式任务调度：Xxl-Job 高性能、高可靠、易扩展；\n    \n-   长连接：WebSocket；\n    \n\n### 前端\n\n-   前台主要基于 Nuxt.js 进行开发；\n    \n-   UI组件：Element UI；\n    \n-   数据可视化：Antv G2Plot；\n    \n-   makdown编辑器：AiEditor ([https://aieditor.dev/zh/getting-started.html](https://aieditor.dev/zh/getting-started.html)) [](https://aieditor.dev/zh/getting-started.html)；\n    \n\n### 第三方应用\n\n-   第三方登录（Gitee、微博、QQ\\[待审核\\]）；\n    \n-   云存储：阿里云OSS；\n    \n-   短信：阿里云短信；\n    \n-   支付：支付宝SDK；\n    \n\n### 技术选型架构图\n\n![image.png](https://zsh-aurora.oss-cn-beijing.aliyuncs.com/aurora/2024/07/10/3a3dc98ffb3a4781982b83fb90b04704.png)\n\n## 4、开源地址\n\n> [https://gitee.com/yu-zi-wei/qixidi](https://gitee.com/yu-zi-wei/qixidi)\n\n# 作者介绍\n\n## 1、关于作者\n\n> 昵称：钰紫薇  \n> 性别：♂  \n> 生日：2001.09  \n> 所在城市：成都  \n> 学历：普本  \n> 邮箱：2978824265@qq.com\n\n## 2、技术专长\n\n**擅长：** Java、javaScript、Mysql、Redis、Linux、Docker、数据结构与算法、服务端开发，(技能树持续点亮中…)  \n**常用技术栈：** Spring全家桶、Mybatis-plus、Spring Cloud Alibaba 及周边组件、RocktMQ、Vue、Nuxt、uniapp\n\n# 未来展望\n\n# 结语\n\n网站会持续优化和推出新功能，以丰富网站内容和满足日常使用。', 'androidstudio', 1, 2, NULL, 0, '2024-07-01 22:10:50', '1636168030713155584', '2024-11-25 14:18:19', NULL, '6', 14, 8, -2, 0, 60, 1, 2, '2024-07-01 22:10:50', 730);
INSERT INTO `b_article_information` VALUES (62, '1636168030713155584', '关于“栖息地”', 'https://zsh-aurora.oss-cn-beijing.aliyuncs.com/aurora/2024/11/25/55eaf93b6f394182b8d39808dac2bbe4.png', '引言栖息地知识社区，个人程序人生的点滴记录和时光储备。淡泊明志，宁静致远。珍惜原创，矢志不渝。网站介绍栖息地是一个集知识积累、日常记录与互动交流于一体的综合性知识社区平台。我们致力于打造一个温馨、高效、充满启发性的在线空间，让每一位用户都能', '<h1>引言</h1><p>栖息地知识社区，个人程序人生的点滴记录和时光储备。淡泊明志，宁静致远。珍惜原创，矢志不渝。</p><h1>网站介绍</h1><p>栖息地是一个集知识积累、日常记录与互动交流于一体的综合性知识社区平台。我们致力于打造一个温馨、高效、充满启发性的在线空间，让每一位用户都能成为知识的贡献者与受益者。</p><h2>1、定位与特色</h2><h2>2、技术栈</h2><h3>后端</h3><ul><li><p>项目基于开源项目 “RuoYi” 进行二次开发；</p></li><li><p>权限认证框架：Sa-Token、Jwt 支持多终端认证系统；</p></li><li><p>第三方登录框架：Justauth 开箱即用的整合第三方登录的开源组件；</p></li><li><p>分布式任务调度：Xxl-Job 高性能、高可靠、易扩展；</p></li><li><p>长连接：WebSocket；</p></li></ul><h3>前端</h3><ul><li><p>前台主要基于 Nuxt.js 进行开发；</p></li><li><p>UI组件：Element UI；</p></li><li><p>数据可视化：Antv G2Plot；</p></li><li><p>makdown编辑器：<span style=\"color: rgb(0, 0, 0); font-size: 14px\">AiEditor </span><span style=\"font-family: ui-monospace, Menlo, Monaco, Consolas, Liberation Mono, Courier New, monospace; color: rgb(3, 47, 98); font-size: 14px\">(</span><a target=\"_blank\" rel=\"noopener noreferrer nofollow\" href=\"https://aieditor.dev/zh/getting-started.html\">https://aieditor.dev/zh/getting-started.html</a><span style=\"font-family: ui-monospace, Menlo, Monaco, Consolas, Liberation Mono, Courier New, monospace; color: rgb(3, 47, 98); font-size: 14px\">)</span><a target=\"_blank\" rel=\"noopener noreferrer nofollow\" href=\"https://aieditor.dev/zh/getting-started.html\"> </a>；</p></li></ul><h3>第三方应用</h3><ul><li><p>第三方登录（Gitee、微博、QQ[待审核]）；</p></li><li><p>云存储：阿里云OSS；</p></li><li><p>短信：阿里云短信；</p></li><li><p>支付：支付宝SDK；</p></li></ul><h3>技术选型架构图</h3><div style=\"text-align:left\"><img src=\"https://zsh-aurora.oss-cn-beijing.aliyuncs.com/aurora/2024/07/10/3a3dc98ffb3a4781982b83fb90b04704.png\" alt=\"image.png\" width=\"100%\" data-align=\"left\"></div><h2>4、开源地址</h2><blockquote><p><a target=\"_blank\" rel=\"noopener noreferrer nofollow\" href=\"https://gitee.com/yu-zi-wei/qixidi\">https://gitee.com/yu-zi-wei/qixidi</a></p></blockquote><h1>作者介绍</h1><h2>1、关于作者</h2><blockquote><p>昵称：钰紫薇<br>性别：♂<br>生日：2001.09<br>所在城市：成都<br>学历：普本<br>邮箱：2978824265@qq.com</p></blockquote><h2>2、技术专长</h2><p><strong>擅长：</strong> Java、javaScript、Mysql、Redis、Linux、Docker、数据结构与算法、服务端开发，(技能树持续点亮中…)<br><strong>常用技术栈：</strong> Spring全家桶、Mybatis-plus、Spring Cloud Alibaba 及周边组件、RocktMQ、Vue、Nuxt、uniapp</p><h1>未来展望</h1><h1>结语</h1><p>网站会持续优化和推出新功能，以丰富网站内容和满足日常使用。</p>', '# 引言\n\n栖息地知识社区，个人程序人生的点滴记录和时光储备。淡泊明志，宁静致远。珍惜原创，矢志不渝。\n\n# 网站介绍\n\n栖息地是一个集知识积累、日常记录与互动交流于一体的综合性知识社区平台。我们致力于打造一个温馨、高效、充满启发性的在线空间，让每一位用户都能成为知识的贡献者与受益者。\n\n## 1、定位与特色\n\n## 2、技术栈\n\n### 后端\n\n-   项目基于开源项目 “RuoYi” 进行二次开发；\n    \n-   权限认证框架：Sa-Token、Jwt 支持多终端认证系统；\n    \n-   第三方登录框架：Justauth 开箱即用的整合第三方登录的开源组件；\n    \n-   分布式任务调度：Xxl-Job 高性能、高可靠、易扩展；\n    \n-   长连接：WebSocket；\n    \n\n### 前端\n\n-   前台主要基于 Nuxt.js 进行开发；\n    \n-   UI组件：Element UI；\n    \n-   数据可视化：Antv G2Plot；\n    \n-   makdown编辑器：AiEditor ([https://aieditor.dev/zh/getting-started.html](https://aieditor.dev/zh/getting-started.html)) [](https://aieditor.dev/zh/getting-started.html)；\n    \n\n### 第三方应用\n\n-   第三方登录（Gitee、微博、QQ\\[待审核\\]）；\n    \n-   云存储：阿里云OSS；\n    \n-   短信：阿里云短信；\n    \n-   支付：支付宝SDK；\n    \n\n### 技术选型架构图\n\n![image.png](https://zsh-aurora.oss-cn-beijing.aliyuncs.com/aurora/2024/07/10/3a3dc98ffb3a4781982b83fb90b04704.png)\n\n## 4、开源地址\n\n> [https://gitee.com/yu-zi-wei/qixidi](https://gitee.com/yu-zi-wei/qixidi)\n\n# 作者介绍\n\n## 1、关于作者\n\n> 昵称：钰紫薇  \n> 性别：♂  \n> 生日：2001.09  \n> 所在城市：成都  \n> 学历：普本  \n> 邮箱：2978824265@qq.com\n\n## 2、技术专长\n\n**擅长：** Java、javaScript、Mysql、Redis、Linux、Docker、数据结构与算法、服务端开发，(技能树持续点亮中…)  \n**常用技术栈：** Spring全家桶、Mybatis-plus、Spring Cloud Alibaba 及周边组件、RocktMQ、Vue、Nuxt、uniapp\n\n# 未来展望\n\n# 结语\n\n网站会持续优化和推出新功能，以丰富网站内容和满足日常使用。', NULL, 1, 1, NULL, 0, '2024-11-25 14:45:27', NULL, '2024-11-25 14:45:27', NULL, '43,4', 13, 0, 0, 0, 1, 1, 2, '2024-11-25 14:45:27', 4.65);

-- ----------------------------
-- Table structure for b_browsing_history
-- ----------------------------
DROP TABLE IF EXISTS `b_browsing_history`;
CREATE TABLE `b_browsing_history`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'id',
  `uid` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_cs_0900_ai_ci NOT NULL COMMENT '用户id',
  `target_id` bigint NOT NULL COMMENT '目标id',
  `target_title` varchar(400) CHARACTER SET utf8mb4 COLLATE utf8mb4_cs_0900_ai_ci NOT NULL COMMENT '目标标题',
  `target_uid` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_cs_0900_ai_ci NOT NULL COMMENT '目标uid',
  `target_type` bigint NOT NULL COMMENT '目标类型（1：文章，2：帖子）',
  `state` bigint NOT NULL DEFAULT 0 COMMENT '状态',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime NOT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `u_ti_tu_ty`(`uid` ASC, `target_id` ASC, `target_uid` ASC, `target_type` ASC, `create_time` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_cs_0900_ai_ci COMMENT = '用户浏览历史表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of b_browsing_history
-- ----------------------------
INSERT INTO `b_browsing_history` VALUES (1, '1636168030713155584', 62, '关于“栖息地”', '1636168030713155584', 1, 0, '2024-11-25 00:00:00', '2024-11-25 14:53:18');

-- ----------------------------
-- Table structure for b_collection_information
-- ----------------------------
DROP TABLE IF EXISTS `b_collection_information`;
CREATE TABLE `b_collection_information`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '收藏夹id',
  `collection_name` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '收藏夹名称',
  `collection_introduce` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '收藏夹简介',
  `state` int NOT NULL DEFAULT 0 COMMENT '收藏夹状态（0：正常，1：未启用）',
  `included_count` int NOT NULL DEFAULT 0 COMMENT '收录数',
  `uid` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_cs_0900_ai_ci NOT NULL COMMENT '用户id',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '修改者id',
  `update_time` datetime NULL DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_cs_0900_ai_ci COMMENT = '收藏夹信息表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of b_collection_information
-- ----------------------------
INSERT INTO `b_collection_information` VALUES (1, '其他', '其他', 0, 0, '1636168030713155584', '2024-11-25 14:50:48', NULL, '2024-11-25 14:50:48');

-- ----------------------------
-- Table structure for b_collection_record
-- ----------------------------
DROP TABLE IF EXISTS `b_collection_record`;
CREATE TABLE `b_collection_record`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'id',
  `target_id` bigint NOT NULL COMMENT '目标id',
  `collection_id` bigint NOT NULL COMMENT '收藏夹id',
  `type` int NOT NULL COMMENT '收藏类型',
  `state` int NOT NULL DEFAULT 0 COMMENT '状态（0：正常，1：已删除）',
  `uid` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_cs_0900_ai_ci NOT NULL COMMENT '创建者id',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_cs_0900_ai_ci COMMENT = '文章收藏关联表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of b_collection_record
-- ----------------------------

-- ----------------------------
-- Table structure for b_count_user_website
-- ----------------------------
DROP TABLE IF EXISTS `b_count_user_website`;
CREATE TABLE `b_count_user_website`  (
  `uuid` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_cs_0900_ai_ci NOT NULL,
  `fabulous_count` int NOT NULL DEFAULT 0 COMMENT '点赞数',
  `fans_fabulous_count` int NOT NULL DEFAULT 0 COMMENT '被点赞数',
  `collection_count` int NOT NULL DEFAULT 0 COMMENT '收藏数',
  `follow_count` int NOT NULL DEFAULT 0 COMMENT '关注数',
  `fans_follow_count` int NOT NULL DEFAULT 0 COMMENT '被关注数',
  `comment_count` int NOT NULL DEFAULT 0 COMMENT '评论数',
  `fans_comment_count` int NOT NULL DEFAULT 0 COMMENT '被评论数',
  `article_count` int NOT NULL DEFAULT 0 COMMENT '文章数',
  `special_column_count` int NOT NULL DEFAULT 0 COMMENT '专栏数',
  `fans_special_column` int NOT NULL COMMENT '被关注专栏数',
  `circle_count` int NOT NULL DEFAULT 0 COMMENT '圈子数',
  `album_count` int NOT NULL DEFAULT 0 COMMENT '专辑数',
  `dictum_count` int NOT NULL DEFAULT 0 COMMENT '名言数',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`uuid`) USING BTREE,
  UNIQUE INDEX `uuid`(`uuid` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_cs_0900_ai_ci COMMENT = '用户网站信息统计表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of b_count_user_website
-- ----------------------------
INSERT INTO `b_count_user_website` VALUES ('1636168030713155584', 0, 0, 1, 0, 0, 0, 0, 3, 1, 0, 0, 1, 1, '2024-11-25 15:00:00');

-- ----------------------------
-- Table structure for b_dictum_album
-- ----------------------------
DROP TABLE IF EXISTS `b_dictum_album`;
CREATE TABLE `b_dictum_album`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'id',
  `uid` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_cs_0900_ai_ci NOT NULL COMMENT 'uid',
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_cs_0900_ai_ci NOT NULL COMMENT '专辑名称',
  `cover` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_cs_0900_ai_ci NULL DEFAULT NULL COMMENT '封面',
  `brief_introduction` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_cs_0900_ai_ci NULL DEFAULT NULL COMMENT '简介',
  `album_state` bigint NOT NULL DEFAULT 1 COMMENT '专辑状态（1：公开，2：私有，3：关注可看）',
  `employ_sum` bigint NOT NULL DEFAULT 0 COMMENT '收录总数',
  `help_sum` bigint NOT NULL DEFAULT 0 COMMENT '点赞总数',
  `follow_sum` bigint NOT NULL DEFAULT 0 COMMENT '关注总数',
  `state` bigint NOT NULL DEFAULT 0 COMMENT '状态（0：正常吗：已删除）',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime NOT NULL COMMENT '更新时间',
  `recommend_rate` bigint NULL DEFAULT NULL COMMENT '推荐率',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_cs_0900_ai_ci COMMENT = '名言专辑表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of b_dictum_album
-- ----------------------------
INSERT INTO `b_dictum_album` VALUES (1, '1636168030713155584', '小王子', 'https://zsh-aurora.oss-cn-beijing.aliyuncs.com/aurora/2024/11/25/a019d43046204f3da8e5988adcd48ea1.jpg', '小王子', 1, 1, 0, 0, 0, '2024-11-25 14:46:38', '2024-11-25 15:00:30', NULL);

-- ----------------------------
-- Table structure for b_dictum_group
-- ----------------------------
DROP TABLE IF EXISTS `b_dictum_group`;
CREATE TABLE `b_dictum_group`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'id',
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_cs_0900_ai_ci NOT NULL COMMENT '分组名称',
  `cover` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_cs_0900_ai_ci NULL DEFAULT NULL COMMENT '封面',
  `brief_introduction` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_cs_0900_ai_ci NULL DEFAULT NULL COMMENT '简介',
  `employ_sum` bigint NOT NULL DEFAULT 0 COMMENT '收录总数',
  `state` bigint NOT NULL DEFAULT 0 COMMENT '状态（0：正常吗：已删除）',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime NOT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 10 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_cs_0900_ai_ci COMMENT = '名言分组表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of b_dictum_group
-- ----------------------------
INSERT INTO `b_dictum_group` VALUES (1, '文学作品', NULL, '文学作品', 1, 0, '2023-04-24 19:36:02', '2023-04-24 19:36:05');
INSERT INTO `b_dictum_group` VALUES (2, '电影台词', NULL, '简介', 2, 0, '2023-04-24 19:36:02', '2023-04-24 19:36:05');
INSERT INTO `b_dictum_group` VALUES (3, '动漫台词', NULL, '简介', 12, 0, '2023-04-24 19:36:02', '2023-04-24 19:36:05');
INSERT INTO `b_dictum_group` VALUES (4, '小说摘抄', NULL, '简介', 0, 0, '2023-04-24 19:36:02', '2023-04-24 19:36:05');
INSERT INTO `b_dictum_group` VALUES (9, '网络流行', NULL, '网络流行', 9, 0, '2023-04-28 16:31:44', '2023-04-28 16:31:48');
INSERT INTO `b_dictum_group` VALUES (10, '音乐歌词', NULL, '音乐歌词', 10, 0, '2023-05-04 16:11:28', '2023-05-04 16:11:30');

-- ----------------------------
-- Table structure for b_dictum_info
-- ----------------------------
DROP TABLE IF EXISTS `b_dictum_info`;
CREATE TABLE `b_dictum_info`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'id',
  `uid` varchar(54) CHARACTER SET utf8mb4 COLLATE utf8mb4_cs_0900_ai_ci NOT NULL COMMENT '用户id',
  `content` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_cs_0900_ai_ci NOT NULL COMMENT '内容',
  `content_md` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_cs_0900_ai_ci NULL,
  `group_id` bigint NOT NULL COMMENT '分类id',
  `album_id` bigint NULL DEFAULT NULL COMMENT '专辑id',
  `author` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_cs_0900_ai_ci NULL DEFAULT NULL COMMENT '作者',
  `label` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_cs_0900_ai_ci NULL DEFAULT NULL COMMENT '标签（多个以逗号隔开）',
  `works_name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_cs_0900_ai_ci NULL DEFAULT NULL COMMENT '作品名称',
  `help_sum` bigint NOT NULL DEFAULT 0 COMMENT '点赞总数',
  `comment_sum` bigint NOT NULL DEFAULT 0 COMMENT '评论总数',
  `picture` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_cs_0900_ai_ci NULL COMMENT '图片（多个以逗号隔开）',
  `dictum_state` bigint NOT NULL DEFAULT 1 COMMENT '名言状态（1：公开，2：私有，3：关注可看）',
  `state` bigint NOT NULL DEFAULT 0 COMMENT '状态（0，正常，2：以删除）',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime NOT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_cs_0900_ai_ci COMMENT = '名言信息表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of b_dictum_info
-- ----------------------------
INSERT INTO `b_dictum_info` VALUES (1, '1636168030713155584', '<p>你对我来说，只不过是一个很普通的小男孩，而且和其他千千万万个小男孩没什么两样。</p><p style=\"line-height: 200%\">就是说，我不需要你，你也不会需要我。</p><p style=\"line-height: 250%\">对你来说，也是同样的道理。</p><p style=\"line-height: 200%\">我也只不过是一只很普通的狐狸，也和其他千千万万只狐狸没有什么区别。</p><p style=\"line-height: 200%\">但是，你一旦把我驯服了，我们彼此之间就会建立起不可缺少的关系，我们就会相互依恋，难舍难分——你会需要我，我也会需要你。</p><p style=\"line-height: 200%\">在我心里，你就是这个世界上我唯一的人；在你心里，我也是你在这个世界上唯一的狐狸了……</p>', '你对我来说，只不过是一个很普通的小男孩，而且和其他千千万万个小男孩没什么两样。\n\n就是说，我不需要你，你也不会需要我。\n\n对你来说，也是同样的道理。\n\n我也只不过是一只很普通的狐狸，也和其他千千万万只狐狸没有什么区别。\n\n但是，你一旦把我驯服了，我们彼此之间就会建立起不可缺少的关系，我们就会相互依恋，难舍难分——你会需要我，我也会需要你。\n\n在我心里，你就是这个世界上我唯一的人；在你心里，我也是你在这个世界上唯一的狐狸了……', 1, 1, ' 安托万·德·圣-埃克苏佩里 ', '小王子,友谊', '小王子', 0, 0, NULL, 1, 0, '2024-11-25 14:48:59', '2024-11-25 14:48:59');

-- ----------------------------
-- Table structure for b_fabulous_record
-- ----------------------------
DROP TABLE IF EXISTS `b_fabulous_record`;
CREATE TABLE `b_fabulous_record`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'id',
  `uid` varbinary(64) NOT NULL COMMENT '用户id',
  `type_id` bigint NOT NULL COMMENT '对应的作品或评论的id',
  `type` int NOT NULL COMMENT '点赞类型（1：文章，2：活动，3，圈子，4：评论）',
  `target_id` bigint NULL DEFAULT NULL COMMENT '目标id',
  `target_uid` bigint NULL DEFAULT NULL COMMENT '目标Uid',
  `state` int NOT NULL DEFAULT 0 COMMENT '状态（0：点赞有效，1：取消点赞）',
  `create_time` datetime NOT NULL COMMENT '点赞时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_cs_0900_ai_ci COMMENT = '点赞表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of b_fabulous_record
-- ----------------------------

-- ----------------------------
-- Table structure for b_feedback
-- ----------------------------
DROP TABLE IF EXISTS `b_feedback`;
CREATE TABLE `b_feedback`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `uid` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_cs_0900_ai_ci NOT NULL COMMENT '用户id',
  `nickname` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_cs_0900_ai_ci NOT NULL COMMENT '用户名称',
  `feedback_content` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_cs_0900_ai_ci NOT NULL COMMENT '反馈内容',
  `feedback_title` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_cs_0900_ai_ci NULL DEFAULT NULL COMMENT '标题',
  `create_time` datetime NOT NULL COMMENT '反馈时间',
  `status` int NULL DEFAULT 1 COMMENT '状态（1-待处理，2-进行中，3-已完成，4-已关闭）',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  `update_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_cs_0900_ai_ci NULL DEFAULT NULL COMMENT '更新人',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_cs_0900_ai_ci COMMENT = '用户反馈表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of b_feedback
-- ----------------------------

-- ----------------------------
-- Table structure for b_friend_link
-- ----------------------------
DROP TABLE IF EXISTS `b_friend_link`;
CREATE TABLE `b_friend_link`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'id',
  `link_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_cs_0900_ai_ci NULL DEFAULT NULL COMMENT '名称',
  `link_intro` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_cs_0900_ai_ci NULL DEFAULT NULL COMMENT '简介',
  `link_avatar` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_cs_0900_ai_ci NULL COMMENT '头像',
  `link_url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_cs_0900_ai_ci NULL DEFAULT NULL COMMENT '地址',
  `status` int NOT NULL DEFAULT 0 COMMENT '状态',
  `create_time` datetime NULL DEFAULT NULL COMMENT '加入时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 13 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_cs_0900_ai_ci COMMENT = '友链' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of b_friend_link
-- ----------------------------
INSERT INTO `b_friend_link` VALUES (1, '栖息地', '他强任他强，清风拂山岗。', 'http://qixidi.top/img/logo.png', 'http://qixidi.top/', 0, '2024-09-17 14:28:28');
INSERT INTO `b_friend_link` VALUES (12, '阿杜博客', '心有猛虎，细嗅蔷薇。', 'https://adu88.top/logo.png', '\r\nhttps://adu88.top', 0, '2024-11-05 15:13:30');
INSERT INTO `b_friend_link` VALUES (13, 'Imz', '仰望星空，脚踏实地。', 'https://blog.imz.me/img/avatar.webp\n\n\n', 'https://blog.imz.me/', 0, '2024-11-11 11:16:20');

-- ----------------------------
-- Table structure for b_gen_table
-- ----------------------------
DROP TABLE IF EXISTS `b_gen_table`;
CREATE TABLE `b_gen_table`  (
  `table_id` bigint NOT NULL COMMENT '编号',
  `table_name` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_cs_0900_ai_ci NULL DEFAULT '' COMMENT '表名称',
  `table_comment` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_cs_0900_ai_ci NULL DEFAULT '' COMMENT '表描述',
  `sub_table_name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_cs_0900_ai_ci NULL DEFAULT NULL COMMENT '关联子表的表名',
  `sub_table_fk_name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_cs_0900_ai_ci NULL DEFAULT NULL COMMENT '子表关联的外键名',
  `class_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_cs_0900_ai_ci NULL DEFAULT '' COMMENT '实体类名称',
  `tpl_category` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_cs_0900_ai_ci NULL DEFAULT 'crud' COMMENT '使用的模板（crud单表操作 tree树表操作）',
  `package_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_cs_0900_ai_ci NULL DEFAULT NULL COMMENT '生成包路径',
  `module_name` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_cs_0900_ai_ci NULL DEFAULT NULL COMMENT '生成模块名',
  `business_name` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_cs_0900_ai_ci NULL DEFAULT NULL COMMENT '生成业务名',
  `function_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_cs_0900_ai_ci NULL DEFAULT NULL COMMENT '生成功能名',
  `function_author` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_cs_0900_ai_ci NULL DEFAULT NULL COMMENT '生成功能作者',
  `gen_type` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_cs_0900_ai_ci NULL DEFAULT '0' COMMENT '生成代码方式（0zip压缩包 1自定义路径）',
  `gen_path` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_cs_0900_ai_ci NULL DEFAULT '/' COMMENT '生成路径（不填默认项目路径）',
  `options` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_cs_0900_ai_ci NULL DEFAULT NULL COMMENT '其它生成选项',
  `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_cs_0900_ai_ci NULL DEFAULT '' COMMENT '创建者',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_cs_0900_ai_ci NULL DEFAULT '' COMMENT '更新者',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_cs_0900_ai_ci NULL DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`table_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_cs_0900_ai_ci COMMENT = '代码生成业务表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of b_gen_table
-- ----------------------------

-- ----------------------------
-- Table structure for b_gen_table_column
-- ----------------------------
DROP TABLE IF EXISTS `b_gen_table_column`;
CREATE TABLE `b_gen_table_column`  (
  `column_id` bigint NOT NULL COMMENT '编号',
  `table_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_cs_0900_ai_ci NULL DEFAULT NULL COMMENT '归属表编号',
  `column_name` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_cs_0900_ai_ci NULL DEFAULT NULL COMMENT '列名称',
  `column_comment` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_cs_0900_ai_ci NULL DEFAULT NULL COMMENT '列描述',
  `column_type` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_cs_0900_ai_ci NULL DEFAULT NULL COMMENT '列类型',
  `java_type` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_cs_0900_ai_ci NULL DEFAULT NULL COMMENT 'JAVA类型',
  `java_field` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_cs_0900_ai_ci NULL DEFAULT NULL COMMENT 'JAVA字段名',
  `is_pk` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_cs_0900_ai_ci NULL DEFAULT NULL COMMENT '是否主键（1是）',
  `is_increment` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_cs_0900_ai_ci NULL DEFAULT NULL COMMENT '是否自增（1是）',
  `is_required` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_cs_0900_ai_ci NULL DEFAULT NULL COMMENT '是否必填（1是）',
  `is_insert` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_cs_0900_ai_ci NULL DEFAULT NULL COMMENT '是否为插入字段（1是）',
  `is_edit` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_cs_0900_ai_ci NULL DEFAULT NULL COMMENT '是否编辑字段（1是）',
  `is_list` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_cs_0900_ai_ci NULL DEFAULT NULL COMMENT '是否列表字段（1是）',
  `is_query` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_cs_0900_ai_ci NULL DEFAULT NULL COMMENT '是否查询字段（1是）',
  `query_type` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_cs_0900_ai_ci NULL DEFAULT 'EQ' COMMENT '查询方式（等于、不等于、大于、小于、范围）',
  `html_type` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_cs_0900_ai_ci NULL DEFAULT NULL COMMENT '显示类型（文本框、文本域、下拉框、复选框、单选框、日期控件）',
  `dict_type` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_cs_0900_ai_ci NULL DEFAULT '' COMMENT '字典类型',
  `sort` int NULL DEFAULT NULL COMMENT '排序',
  `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_cs_0900_ai_ci NULL DEFAULT '' COMMENT '创建者',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_cs_0900_ai_ci NULL DEFAULT '' COMMENT '更新者',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`column_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_cs_0900_ai_ci COMMENT = '代码生成业务表字段' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of b_gen_table_column
-- ----------------------------

-- ----------------------------
-- Table structure for b_label_grouping_info
-- ----------------------------
DROP TABLE IF EXISTS `b_label_grouping_info`;
CREATE TABLE `b_label_grouping_info`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'id',
  `grouping_name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_cs_0900_ai_ci NULL DEFAULT NULL COMMENT '分组名称',
  `icon` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_cs_0900_ai_ci NULL COMMENT '图标',
  `state` int NULL DEFAULT 0 COMMENT '状态（0：正常，1：已删除）',
  `grouping_describe` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_cs_0900_ai_ci NULL DEFAULT NULL COMMENT '描述',
  `create_time` datetime NULL DEFAULT NULL,
  `create_by` bigint NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 19 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_cs_0900_ai_ci COMMENT = '标签分组信息表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of b_label_grouping_info
-- ----------------------------
INSERT INTO `b_label_grouping_info` VALUES (1, '后端', '  <svg t=\"1678707308739\" class=\"icon\" viewBox=\"0 0 1024 1024\" version=\"1.1\"\r\n                           xmlns=\"http://www.w3.org/2000/svg\" p-id=\"6784\" width=\"20\" height=\"20\">\r\n                        <path\r\n                          d=\"M189.04576 205.55776c8.71936 0 16.1536-2.94912 22.30784-8.84736s9.23136-13.20448 9.23136-21.92384c0.51712-8.71936-2.432-16.2816-8.84224-22.69184-6.41024-6.40512-13.7216-9.87136-21.92384-10.38336-8.20736 0-15.3856 3.07712-21.53984 9.23136s-9.48736 13.33248-9.99936 21.53984v1.54112c0 8.20224 3.07712 15.38048 9.23136 21.53984 6.14912 6.14912 13.32736 9.48224 21.53472 9.99424zM280.58624 205.55776c8.20736 0 15.2576-2.94912 21.15584-8.84736 5.89824-5.89824 9.10336-12.94848 9.61536-21.15584 0.512-8.71936-2.176-16.41472-8.07424-23.08096s-13.2096-10.25536-21.92384-10.76736c-8.20736 0-15.5136 2.94912-21.92896 8.84736-6.41024 5.89824-9.87136 12.94848-10.38336 21.15584v2.30912c0 8.20736 2.94912 15.3856 8.84736 21.53984 5.89824 6.15424 13.2096 9.48736 21.92384 9.99936h0.768zM393.6768 151.7056c-6.15424-6.66624-13.5936-9.99936-22.31296-9.99936-8.20736 0.512-15.3856 3.72224-21.53984 9.61536-6.15424 5.89824-9.48736 12.94848-10.00448 21.15584v1.536c0 8.71936 3.07712 16.0256 9.23136 21.92384s13.58848 9.10336 22.30784 9.61536c8.20736 0 15.3856-3.07712 21.53984-9.23136s9.48736-13.33248 9.99936-21.54496c0.00512-8.70912-3.072-16.39936-9.22112-23.07072z\"\r\n                          p-id=\"6785\" fill=\"#1296db\"></path>\r\n                        <path\r\n                          d=\"M901.12 39.17824H122.88c-45.24032 0-81.92 36.67968-81.92 81.92v778.24c0 45.24544 36.67968 81.92 81.92 81.92h778.24c45.24544 0 81.92-36.67456 81.92-81.92v-778.24c0-45.24032-36.67456-81.92-81.92-81.92z m30.72 860.16c0 16.94208-13.77792 30.72-30.72 30.72H122.88c-16.94208 0-30.72-13.77792-30.72-30.72v-778.24c0-16.94208 13.77792-30.72 30.72-30.72h778.24c16.94208 0 30.72 13.77792 30.72 30.72v778.24z\"\r\n                          p-id=\"6786\" fill=\"#1296db\"></path>\r\n                        <path\r\n                          d=\"M71.68 252.93824h880.64v46.08H71.68zM303.616 455.3472c-3.01568 0-6.03136 3.2-7.04 8.00256l-49.26976 156.8 49.26976 156.8c1.00352 4.80256 4.0192 8.00256 7.04 8.00256h33.1776c6.03136 0 10.05056-11.20256 7.04-19.2l-46.24896-145.60256 46.24896-145.60256c3.01568-9.6-1.00352-19.2-7.04-19.2H303.616zM776.68864 618.5472l-57.79968-156.8c-1.15712-4.80256-4.62336-8.00256-8.0896-8.00256h-38.15424c-6.93248 0-11.56096 11.20256-8.08448 19.2l53.17632 145.60256-53.17632 145.60256c-3.47648 9.6 1.15712 19.2 8.08448 19.2h38.15424c3.46624 0 6.93248-3.2 8.0896-8.00256l57.79968-156.8zM440.62208 866.54464h42.5728c5.01248 0 10.01472-4.80256 10.01472-11.20256l92.76416-467.2c1.2544-8.00256-3.75296-16-10.01472-16h-42.57792c-5.01248 0-10.01984 4.80256-10.01984 11.20256l-92.75904 467.2c-2.50368 9.6 2.50368 16 10.01984 16z\"\r\n                          p-id=\"6787\" fill=\"#1296db\"></path>\r\n                      </svg>', 0, 'java', '2023-05-10 13:58:43', 1);
INSERT INTO `b_label_grouping_info` VALUES (2, '前端', '<svg t=\"1678708771396\" class=\"icon\" viewBox=\"0 0 1024 1024\" version=\"1.1\" xmlns=\"http://www.w3.org/2000/svg\" p-id=\"24298\" data-spm-anchor-id=\"a313x.7781069.0.i108\" width=\"20\" height=\"20\"><path d=\"M824.888889 630.992593l-122.026667-50.346667a28.444444 28.444444 0 1 0-21.143703 53.57037l60.112592 24.367408-60.112592 27.591111a28.444444 28.444444 0 0 0-14.601482 37.357037A28.918519 28.918519 0 0 0 693.096296 739.555556a15.454815 15.454815 0 0 0 11.377778-3.223704l120.414815-54.992593a28.918519 28.918519 0 0 0 16.213333-25.979259A25.694815 25.694815 0 0 0 824.888889 630.992593zM548.598519 517.30963a28.444444 28.444444 0 0 0-35.745186 17.825185l-61.629629 188.397037a28.444444 28.444444 0 0 0 17.825185 35.745185 17.540741 17.540741 0 0 0 8.154074 1.611852 31.668148 31.668148 0 0 0 27.591111-19.531852l61.724445-188.302222a28.444444 28.444444 0 0 0-17.92-35.745185zM358.684444 597.333333a30.151111 30.151111 0 0 0-37.357037-16.213333L199.111111 630.992593a30.435556 30.435556 0 0 0-17.825185 25.979259A28.444444 28.444444 0 0 0 197.973333 682.666667l120.130371 55.182222a30.056296 30.056296 0 0 0 11.377777 3.223704 28.918519 28.918519 0 0 0 25.97926-16.213334 28.444444 28.444444 0 0 0-14.601482-37.357037l-60.112592-27.591111 60.112592-24.367407c16.213333-7.774815 24.367407-24.082963 17.825185-38.210371z\" fill=\"#1296db\" p-id=\"24299\" data-spm-anchor-id=\"a313x.7781069.0.i106\" class=\"\"></path><path d=\"M853.333333 170.666667a56.888889 56.888889 0 0 1 56.888889 56.888889v568.888888a56.888889 56.888889 0 0 1-56.888889 56.888889H170.666667a56.888889 56.888889 0 0 1-56.888889-56.888889V227.555556a56.888889 56.888889 0 0 1 56.888889-56.888889h682.666666m0-56.888889H170.666667A113.777778 113.777778 0 0 0 56.888889 227.555556v568.888888a113.777778 113.777778 0 0 0 113.777778 113.777778h682.666666a113.777778 113.777778 0 0 0 113.777778-113.777778V227.555556a113.777778 113.777778 0 0 0-113.777778-113.777778z\" fill=\"#f19610\" p-id=\"24300\" data-spm-anchor-id=\"a313x.7781069.0.i105\" class=\"\"></path><path d=\"M967.111111 365.321481H56.888889v56.888889h910.222222v-56.888889z\" fill=\"#f19610\" p-id=\"24301\" data-spm-anchor-id=\"a313x.7781069.0.i109\" class=\"\"></path><path d=\"M224.995556 273.540741m-42.666667 0a42.666667 42.666667 0 1 0 85.333333 0 42.666667 42.666667 0 1 0-85.333333 0Z\" fill=\"#13227a\" p-id=\"24302\" data-spm-anchor-id=\"a313x.7781069.0.i111\" class=\"selected\"></path><path d=\"M357.451852 273.540741m-42.666667 0a42.666667 42.666667 0 1 0 85.333334 0 42.666667 42.666667 0 1 0-85.333334 0Z\" fill=\"#1296db\" p-id=\"24303\" data-spm-anchor-id=\"a313x.7781069.0.i110\" class=\"\"></path><path d=\"M489.908148 273.540741m-42.666667 0a42.666667 42.666667 0 1 0 85.333334 0 42.666667 42.666667 0 1 0-85.333334 0Z\" fill=\"#1afa29\" p-id=\"24304\" data-spm-anchor-id=\"a313x.7781069.0.i107\" class=\"\"></path></svg>', 0, 'html', '2023-05-10 13:58:46', 1);
INSERT INTO `b_label_grouping_info` VALUES (3, '测试', '<svg t=\"1678707698277\" class=\"icon\" viewBox=\"0 0 1129 1024\" version=\"1.1\" xmlns=\"http://www.w3.org/2000/svg\" p-id=\"9011\" width=\"20\" height=\"20\"><path d=\"M557.903448 775.062069c-108.049655 0-231.106207-38.311724-357.517241-152.893793a17.655172 17.655172 0 0 1-3.707586-21.186207l187.144827-359.812414a17.655172 17.655172 0 0 1 15.536552-9.533793h331.211034a17.655172 17.655172 0 0 1 15.536552 9.533793L933.252414 600.275862a17.655172 17.655172 0 0 1-3.177931 20.656552c-2.295172 3.001379-157.484138 154.129655-372.171035 154.129655z m-323.61931-170.01931c319.028966 279.304828 605.748966 50.670345 661.53931 0l-176.551724-338.449656H410.129655z\" fill=\"#3164AF\" p-id=\"9012\"></path><path d=\"M573.969655 454.091034a221.042759 221.042759 0 0 1-127.823448-46.433103l21.715862-27.895172c4.590345 3.531034 111.933793 85.097931 187.144828 2.11862L681.136552 406.068966a140.182069 140.182069 0 0 1-107.166897 48.022068zM583.503448 582.62069a426.372414 426.372414 0 0 1-197.914482-57.026207l17.655172-30.190345c1.942069 1.235862 192.088276 112.993103 314.968276 2.118621L741.517241 523.475862A229.517241 229.517241 0 0 1 583.503448 582.62069z\" fill=\"#3164AF\" p-id=\"9013\"></path></svg>', 0, 'pythion', '2022-08-16 11:06:21', 1);
INSERT INTO `b_label_grouping_info` VALUES (5, '人工智能', '<svg t=\"1678707753585\" class=\"icon\" viewBox=\"0 0 1024 1024\" version=\"1.1\" xmlns=\"http://www.w3.org/2000/svg\" p-id=\"10668\" width=\"20\" height=\"20\"><path d=\"M623.786667 627.958519l-94.814815-274.204445a25.220741 25.220741 0 0 0-9.481482-12.98963 39.158519 39.158519 0 0 0-40.58074 0 25.220741 25.220741 0 0 0-9.481482 12.98963l-93.677037 273.066667c-4.266667 12.515556-3.508148 21.238519 2.085926 26.453333a31.194074 31.194074 0 0 0 22.376296 7.774815 30.814815 30.814815 0 0 0 21.522963-6.637037 44.847407 44.847407 0 0 0 11.282963-17.445926l20.100741-52.148148h92.065185l21.712593 54.518518a43.045926 43.045926 0 0 0 10.429629 15.54963 28.444444 28.444444 0 0 0 21.048889 6.162963 32.142222 32.142222 0 0 0 22.376297-8.059259c5.688889-5.214815 6.637037-13.463704 3.034074-25.031111zM464.592593 533.617778l35.176296-112.82963 34.891852 112.82963zM718.696296 632.225185a29.677037 29.677037 0 0 1-7.111111 21.997037 30.340741 30.340741 0 0 1-22.85037 7.300741 28.444444 28.444444 0 0 1-22.471111-7.300741 30.814815 30.814815 0 0 1-6.637037-21.428148V364.373333q0-29.013333 29.013333-29.013333A33.564444 33.564444 0 0 1 711.111111 341.333333a27.117037 27.117037 0 0 1 7.774815 22.186667z\" fill=\"#0097FF\" p-id=\"10669\"></path><path d=\"M555.804444 56.888889a410.074074 410.074074 0 0 0-409.41037 409.41037 35.460741 35.460741 0 0 0 0 4.930371c-28.444444 29.297778-88.746667 96.331852-88.746667 143.454814 0 52.148148 49.303704 72.248889 84.48 73.386667l40.106667 9.007408 7.585185 197.025185a73.765926 73.765926 0 0 0 74.145185 73.007407h150.281482a29.297778 29.297778 0 1 0 0-58.500741H263.964444a14.980741 14.980741 0 0 1-14.885925-14.885926l-9.481482-243.768888-87.04-18.962963-3.034074-0.663704h-3.318519a64.948148 64.948148 0 0 1-19.911111-4.551111c-9.481481-3.792593-9.481481-7.300741-9.481481-10.619259 0-16.971852 37.262222-68.171852 79.549629-109.700741a29.297778 29.297778 0 0 0 7.300741-29.771852 28.444444 28.444444 0 0 0 1.422222-9.007407 350.814815 350.814815 0 0 1 701.62963 0c0 16.308148 29.297778 29.297778 29.297778 29.297777s29.297778-12.98963 29.297778-29.297777A409.979259 409.979259 0 0 0 555.804444 56.888889z\" fill=\"#0097FF\" p-id=\"10670\"></path><path d=\"M860.728889 291.271111s146.299259 247.751111-81.92 485.451852c0 0-11.282963 50.725926 47.407407 36.124444 0 0 163.555556-134.542222 136.912593-392.438518z\" fill=\"#0097FF\" p-id=\"10671\"></path></svg>', 0, '人工智能', '2023-05-10 13:58:54', 1);
INSERT INTO `b_label_grouping_info` VALUES (6, '开发工具', '<svg t=\"1678707813307\" class=\"icon\" viewBox=\"0 0 1024 1024\" version=\"1.1\" xmlns=\"http://www.w3.org/2000/svg\" p-id=\"11550\" width=\"20\" height=\"20\"><path d=\"M911.7 385.3l-0.3-1.5c-0.2-1-0.3-1.9-0.6-2.9-0.2-0.6-0.4-1.1-0.5-1.7-0.3-0.8-0.5-1.7-0.9-2.5-0.2-0.6-0.5-1.1-0.8-1.7-0.4-0.8-0.8-1.5-1.2-2.3-0.3-0.5-0.6-1.1-1-1.6-0.8-1.2-1.7-2.4-2.6-3.6-0.5-0.6-1.1-1.3-1.7-1.9-0.4-0.5-0.9-0.9-1.4-1.3-0.6-0.6-1.3-1.1-1.9-1.6-0.5-0.4-1-0.8-1.6-1.2-0.2-0.1-0.4-0.3-0.6-0.4L531.1 117.8c-11.5-7.7-26.6-7.7-38.1 0L127.3 361.3c-0.2 0.1-0.4 0.3-0.6 0.4-0.5 0.4-1 0.8-1.6 1.2-0.7 0.5-1.3 1.1-1.9 1.6-0.5 0.4-0.9 0.9-1.4 1.3-0.6 0.6-1.2 1.2-1.7 1.9-1 1.1-1.8 2.3-2.6 3.6-0.3 0.5-0.7 1-1 1.6-0.4 0.7-0.8 1.5-1.2 2.3-0.3 0.5-0.5 1.1-0.8 1.7-0.3 0.8-0.6 1.7-0.9 2.5-0.2 0.6-0.4 1.1-0.5 1.7-0.2 0.9-0.4 1.9-0.6 2.9l-0.3 1.5c-0.2 1.5-0.3 3-0.3 4.5v243.5c0 1.5 0.1 3 0.3 4.5l0.3 1.5c0.2 1 0.4 1.9 0.6 2.9 0.2 0.6 0.3 1.1 0.5 1.7 0.3 0.9 0.6 1.7 0.9 2.5 0.2 0.6 0.5 1.1 0.8 1.7 0.4 0.8 0.7 1.5 1.2 2.3 0.3 0.5 0.6 1.1 1 1.6 0.5 0.7 0.9 1.4 1.5 2.1l1.2 1.5c0.5 0.6 1.1 1.3 1.7 1.9 0.4 0.5 0.9 0.9 1.4 1.3 0.6 0.6 1.3 1.1 1.9 1.6 0.5 0.4 1 0.8 1.6 1.2 0.2 0.1 0.4 0.3 0.6 0.4L493 905.7c5.6 3.8 12.3 5.8 19.1 5.8 6.6 0 13.3-1.9 19.1-5.8l365.6-243.5c0.2-0.1 0.4-0.3 0.6-0.4 0.5-0.4 1-0.8 1.6-1.2 0.7-0.5 1.3-1.1 1.9-1.6 0.5-0.4 0.9-0.9 1.4-1.3 0.6-0.6 1.2-1.2 1.7-1.9l1.2-1.5 1.5-2.1c0.3-0.5 0.7-1 1-1.6 0.4-0.8 0.8-1.5 1.2-2.3 0.3-0.5 0.5-1.1 0.8-1.7 0.3-0.8 0.6-1.7 0.9-2.5 0.2-0.5 0.4-1.1 0.5-1.7 0.3-0.9 0.4-1.9 0.6-2.9l0.3-1.5c0.2-1.5 0.3-3 0.3-4.5V389.8c-0.3-1.5-0.4-3-0.6-4.5zM546.4 210.5l269.4 179.4-120.3 80.4-149-99.6V210.5z m-68.8 0v160.2l-149 99.6-120.3-80.4 269.3-179.4zM180.7 454.1l86 57.5-86 57.5v-115z m296.9 358.5L208.3 633.2l120.3-80.4 149 99.6v160.2zM512 592.8l-121.6-81.2L512 430.3l121.6 81.2L512 592.8z m34.4 219.8V652.4l149-99.6 120.3 80.4-269.3 179.4zM843.3 569l-86-57.5 86-57.5v115z\" fill=\"#13227a\" p-id=\"11551\"></path></svg>', 0, '开发工具', '2023-05-10 13:58:59', 1);
INSERT INTO `b_label_grouping_info` VALUES (7, '阅读', '<svg t=\"1678708446187\" class=\"icon\" viewBox=\"0 0 1024 1024\" version=\"1.1\" xmlns=\"http://www.w3.org/2000/svg\" p-id=\"14331\" width=\"20\" height=\"20\"><path d=\"M900.829867 143.581867a34.133333 34.133333 0 0 1 43.690666 52.394666l-4.437333 3.6864a173.960533 173.960533 0 0 0-24.951467 27.6992C897.706667 251.460267 887.466667 278.254933 887.466667 307.2c0 28.945067 10.24 55.739733 27.648 79.854933 6.263467 8.635733 12.970667 16.247467 19.626666 22.715734l2.9696 2.833066c1.792 1.655467 3.191467 2.8672 4.096 3.584l0.5632 0.4608a34.133333 34.133333 0 0 1-41.540266 54.1696c-10.973867-8.3968-26.0608-23.074133-41.028267-43.776C834.56 392.123733 819.2 351.914667 819.2 307.2c0-44.714667 15.36-84.923733 40.618667-119.842133 14.9504-20.6848 30.037333-35.362133 41.0112-43.776zM75.3152 559.5136a34.133333 34.133333 0 0 1 47.854933-6.331733c10.9568 8.413867 26.0608 23.0912 41.028267 43.776C189.44 631.876267 204.8 672.085333 204.8 716.8c0 44.714667-15.36 84.923733-40.618667 119.842133-14.9504 20.701867-30.037333 35.3792-41.0112 43.776a34.133333 34.133333 0 0 1-43.690666-52.3776l4.437333-3.703466c1.365333-1.194667 3.191467-2.850133 5.358933-4.949334 6.656-6.485333 13.346133-14.097067 19.592534-22.7328C126.293333 772.539733 136.533333 745.745067 136.533333 716.8c0-28.945067-10.24-55.739733-27.648-79.837867a173.960533 173.960533 0 0 0-19.626666-22.715733l-4.232534-3.9936a77.858133 77.858133 0 0 0-2.816-2.440533l-0.580266-0.443734a34.133333 34.133333 0 0 1-6.314667-47.854933z\" fill=\"#f19610\" p-id=\"14332\" data-spm-anchor-id=\"a313x.7781069.0.i55\" class=\"selected\"></path><path d=\"M921.6 136.533333a34.133333 34.133333 0 0 1 2.56 68.181334L921.6 204.8H238.933333a102.4 102.4 0 0 0-3.84 204.731733L238.933333 409.6h682.666667a34.133333 34.133333 0 0 1 2.56 68.181333L921.6 477.866667H238.933333C144.674133 477.866667 68.266667 401.4592 68.266667 307.2c0-92.672 73.847467-168.072533 165.888-170.5984L238.933333 136.533333h682.666667zM785.066667 546.133333c94.2592 0 170.666667 76.407467 170.666666 170.666667 0 92.672-73.847467 168.072533-165.888 170.5984L785.066667 887.466667H102.4a34.133333 34.133333 0 0 1-2.56-68.164267L102.4 819.2h682.666667a102.4 102.4 0 0 0 3.84-204.731733L785.066667 614.4H102.4a34.133333 34.133333 0 0 1-2.56-68.164267L102.4 546.133333h682.666667z\" fill=\"#f19610\" p-id=\"14333\" data-spm-anchor-id=\"a313x.7781069.0.i54\" class=\"selected\"></path><path d=\"M375.466667 256v221.866667a34.133333 34.133333 0 1 1-68.266667 0V256h68.266667z\" fill=\"#00B386\" p-id=\"14334\"></path></svg>', 0, '阅读', '2023-05-10 13:59:03', 1);
INSERT INTO `b_label_grouping_info` VALUES (8, '数据结构与算法', '<svg t=\"1678708486878\" class=\"icon\" viewBox=\"0 0 1024 1024\" version=\"1.1\" xmlns=\"http://www.w3.org/2000/svg\" p-id=\"15161\" width=\"20\" height=\"20\"><path d=\"M819.80324 325.3c4.9-4.9 12.4-6 18.7-2.9 17.3 8.7 36.8 13.6 57.5 13.6 70.7 0 128-57.3 128-128 0-70.6-57.4-128-128-128-70.7 0-128 57.3-128 128 0 18.8 4.1 36.7 11.4 52.8 2.7 6 1.3 13.1-3.3 17.8l-24.2 24.2c-5.7 5.7-14.9 6.3-21.2 1.2-38.1-30.1-86.3-48-138.6-48-18.8 0-37.1 2.3-54.6 6.7-6.9 1.7-14.1-1.4-17.7-7.5l-6.6-11.4c-3.4-5.8-2.7-13.1 1.6-18.3 18.6-22.6 29.7-51.6 29.3-83.2C543.10324 73 486.30324 16.6 417.00324 16c-70.6-0.6-128.1 56.1-129 126.3-0.9 70.3 55.7 128.6 126 129.6 9.4 0.2 18.6-0.7 27.4-2.5 6.8-1.4 13.6 1.7 17.1 7.7l2.2 3.8c4 7 2.2 15.9-4.2 20.8-42.4 32.3-73 79.4-84 133.6-1.5 7.4-8.1 12.7-15.7 12.7h-94.1c-6.6 0-12.6-4-14.9-10.2-18.1-48-64.3-82.2-118.5-82.8C58.70324 354.3 0.50324 411.5 0.00324 482.1-0.49676 553.2 57.00324 611 128.00324 611c56.7 0 104.8-36.9 121.6-87.9 2.2-6.6 8.3-11.1 15.2-11.1h92c7.6 0 14.2 5.4 15.7 12.9 9.5 46.7 33.5 88 67 119.2 5.4 5 6.6 13.2 2.9 19.6l-21.7 37.6c-3.6 6.3-11.1 9.4-18.2 7.4-11.1-3.1-22.7-4.7-34.8-4.7-69.8 0.1-127 56.8-127.8 126.6-0.8 71.7 57.4 130 129.1 129.4 69.5-0.6 126.3-57.3 126.9-126.8 0.3-28-8.5-53.9-23.5-75.1-3.6-5.1-3.9-11.8-0.8-17.2l24.9-43.1c3.9-6.7 12-9.7 19.3-7 23.7 8.6 49.3 13.2 76 13.2 34.9 0 67.9-8 97.3-22.2 7.6-3.6 16.7-0.9 20.9 6.4l37 64c-26.3 23.5-43 57.7-43 95.8 0 70.9 58 128.5 128.9 128 69.7-0.5 126.2-56.7 127.1-126.3 0.9-70.1-57-129.3-127.1-129.7-6.2 0-12.3 0.4-18.3 1.2-6.5 0.9-12.8-2.2-16.1-7.8l-39.2-67.9c-3.4-5.9-2.7-13.3 1.8-18.4 34.2-39.3 54.9-90.7 54.9-147 0-38.9-9.9-75.5-27.4-107.4-3.4-6.2-2.2-13.9 2.8-18.9l28.4-28.5z m44.3-136c1.3-15.6 14-28.2 29.7-29.3 18.7-1.3 34.2 13.5 34.2 31.9 0 9.5-4.2 18.1-10.8 24 5.6 4.9 9.4 11.7 10.5 19.4 0.4 2.4-1.5 4.6-4 4.6h-55.4c-2.4 0-4.3-2.2-4-4.6 1.1-7.7 4.9-14.5 10.5-19.4-7.2-6.3-11.5-16-10.7-26.6zM159.70324 507.4c0.4 2.4-1.5 4.6-4 4.6h-55.4c-2.4 0-4.3-2.2-4-4.6 1.1-7.7 4.9-14.5 10.5-19.4-7.3-6.4-11.6-16.1-10.7-26.7 1.3-15.6 14-28.2 29.7-29.3 18.7-1.3 34.2 13.5 34.2 31.9 0 9.5-4.2 18.1-10.8 24 5.5 5 9.4 11.8 10.5 19.5z m240 352c0.4 2.4-1.5 4.6-4 4.6h-55.4c-2.4 0-4.3-2.2-4-4.6 1.1-7.7 4.9-14.5 10.5-19.4-7.3-6.4-11.6-16.1-10.7-26.7 1.3-15.6 14-28.2 29.7-29.3 18.7-1.3 34.2 13.5 34.2 31.9 0 9.5-4.2 18.1-10.8 24 5.5 5 9.4 11.8 10.5 19.5z m48-688c0.4 2.4-1.5 4.6-4 4.6h-55.4c-2.4 0-4.3-2.2-4-4.6 1.1-7.7 4.9-14.5 10.5-19.4-7.3-6.4-11.6-16.1-10.7-26.7 1.3-15.6 14-28.2 29.7-29.3 18.7-1.3 34.2 13.5 34.2 31.9 0 9.5-4.2 18.1-10.8 24 5.5 5 9.4 11.8 10.5 19.5z m207.7 379.5c0.7 4.8-3.1 9.1-7.9 9.1H536.60324c-4.9 0-8.6-4.3-7.9-9.1 2.9-20.2 15.2-37.4 32.3-46.9-20.5-11.4-34.1-33.7-32.9-59.1 1.5-32.8 28.1-59.4 60.9-60.9 36.7-1.7 67.1 27.6 67.1 63.9 0 24.1-13.3 45.1-33 56 17.1 9.6 29.4 26.8 32.3 47z m144.7 278.4c1.3-15.6 14-28.2 29.7-29.3 18.7-1.3 34.2 13.5 34.2 31.9 0 9.5-4.2 18.1-10.8 24 5.6 4.9 9.4 11.7 10.5 19.4 0.4 2.4-1.5 4.6-4 4.6h-55.4c-2.4 0-4.3-2.2-4-4.6 1.1-7.7 4.9-14.5 10.5-19.4-7.2-6.3-11.5-16-10.7-26.6z\" p-id=\"15162\" data-spm-anchor-id=\"a313x.7781069.0.i60\" class=\"selected\" fill=\"#1296db\"></path></svg>', 0, '数据结构与算法', '2023-05-10 13:59:10', 1);
INSERT INTO `b_label_grouping_info` VALUES (9, '大数据', '<svg t=\"1678708513702\" class=\"icon\" viewBox=\"0 0 1024 1024\" version=\"1.1\" xmlns=\"http://www.w3.org/2000/svg\" p-id=\"16333\" width=\"20\" height=\"20\"><path d=\"M218.5 726.5L474 868.4c10 6.7 25 10 38.4 10 13.4 0 28.4-3.3 38.4-10l255.4-141.9c21.7-11.7 40.1-41.7 40.1-66.8V359.2c0-25-16.7-53.4-40.1-66.8L550.8 150.5c-21.7-11.7-56.8-11.7-78.5 0L218.5 292.4c-23.4 13.4-40.1 41.7-40.1 66.8v300.5c0.1 25 16.8 53.4 40.1 66.8z m-5-367.3c0-13.4 10-30 21.7-36.7l255.4-141.9c5-3.3 13.4-5 21.7-5 8.3 0 16.7 1.7 21.7 5l255.4 141.9c11.7 6.7 21.7 23.4 21.7 36.7v300.5c0 13.4-10 30.1-21.7 36.7L534.1 838.3c-10 6.7-33.4 6.7-43.4 0L235.2 696.4c-11.7-6.7-21.7-23.4-21.7-36.7V359.2z\" fill=\"#EB6A1B\" p-id=\"16334\"></path><path d=\"M341.8 443.5c8.6 0 16.4-3.3 22.2-8.8l101.8 58.8c-1.6 4.8-2.4 10-2.4 15.4s0.9 10.5 2.4 15.4l-106.3 61.3c-4.9-3.9-11.1-6.2-17.8-6.2-15.8 0-28.6 12.8-28.6 28.6s12.8 28.6 28.6 28.6 28.6-12.8 28.6-28.6c0-1.4-0.1-2.8-0.3-4.2l106.3-61.3c6.9 7.6 16.2 13.1 26.6 15.4v117.4c-12.8 4.4-22 16.5-22 30.8 0 18 14.6 32.5 32.5 32.5 18 0 32.5-14.6 32.5-32.5 0-14.3-9.2-26.4-22-30.8V557.8c10.5-2.2 19.7-7.7 26.6-15.4l106.3 61.3c-0.2 1.4-0.3 2.8-0.3 4.2 0 15.8 12.8 28.6 28.6 28.6s28.6-12.8 28.6-28.6-12.8-28.6-28.6-28.6c-6.7 0-12.9 2.3-17.8 6.2L561 524.2c1.6-4.8 2.4-10 2.4-15.4s-0.9-10.5-2.4-15.4l100.6-58.1c5.7 5 13.2 8 21.4 8 18 0 32.5-14.6 32.5-32.5s-14.6-32.5-32.5-32.5c-18 0-32.5 14.6-32.5 32.5 0 2.2 0.2 4.3 0.6 6.3l-100.6 58.1c-6.9-7.6-16.2-13.1-26.6-15.4v-123c10.6-4.2 18.1-14.5 18.1-26.6 0-15.8-12.8-28.6-28.6-28.6s-28.6 12.8-28.6 28.6c0 12.1 7.5 22.4 18.1 26.6v123c-10.5 2.2-19.7 7.7-26.6 15.4l-102.4-59.1c0.3-1.7 0.4-3.4 0.4-5.2 0-18-14.6-32.5-32.5-32.5-18 0-32.5 14.6-32.5 32.5s14.5 32.6 32.5 32.6z m341.3-55c12.4 0 22.4 10.1 22.4 22.4s-10.1 22.4-22.4 22.4c-12.4 0-22.4-10.1-22.4-22.4s10-22.4 22.4-22.4zM513.5 683.6c12.4 0 22.4 10.1 22.4 22.4 0 12.4-10.1 22.4-22.4 22.4s-22.5-10-22.5-22.3c0-12.4 10.1-22.5 22.5-22.5zM341.8 388.5c12.4 0 22.4 10.1 22.4 22.4s-10.1 22.4-22.4 22.4c-12.4 0-22.4-10.1-22.4-22.4s10-22.4 22.4-22.4z\" fill=\"#EB6A1B\" p-id=\"16335\"></path></svg>', 0, '大数据', '2023-05-10 13:59:14', 1);
INSERT INTO `b_label_grouping_info` VALUES (10, '生活', '<svg t=\"1678708532781\" class=\"icon\" viewBox=\"0 0 1024 1024\" version=\"1.1\" xmlns=\"http://www.w3.org/2000/svg\" p-id=\"17463\" data-spm-anchor-id=\"a313x.7781069.0.i68\" width=\"20\" height=\"20\"><path d=\"M443.4 852.3c-209.2 0-379.5-170.2-379.5-379.5v-13.6h759v13.6c0 209.3-170.2 379.5-379.5 379.5z m-352-365.9c7.2 188 162.3 338.7 352 338.7s344.9-150.7 352-338.7h-704z\" fill=\"#1296db\" p-id=\"17464\" data-spm-anchor-id=\"a313x.7781069.0.i69\" class=\"\"></path><path d=\"M261.1 738.1c-55.4-41.8-98.3-97.8-124.1-161.9l25.3-10.1c23.9 59.5 63.7 111.5 115.2 150.3l-16.4 21.7zM863.3 632.9c-32.2 0-62.2-15.9-80.2-42.6l22.5-15.2c13 19.2 34.5 30.7 57.7 30.7 38.4 0 69.6-31.2 69.6-69.6s-31.2-69.6-69.6-69.6c-16.1 0-31.7 5.6-44.1 15.8l-17.3-21c17.2-14.2 39-22 61.4-22 53.4 0 96.8 43.4 96.8 96.8 0 53.3-43.4 96.7-96.8 96.7zM319.2 405.9c-5.6 0-10.8-3.5-12.8-9-20.4-57.1 5.8-90.5 28.9-120 17.8-22.6 34.6-44 34.6-76.8 0-66-31.7-92.7-32-93-5.8-4.8-6.7-13.3-1.9-19.1 4.8-5.8 13.3-6.6 19.1-1.9 1.7 1.4 41.9 35.3 41.9 114 0 42.2-21.5 69.5-40.4 93.6-22.4 28.5-40.1 51-24.7 94 2.5 7.1-1.1 14.9-8.2 17.4-1.4 0.5-2.9 0.8-4.5 0.8zM427.9 405.9c-5.6 0-10.8-3.5-12.8-9-20.4-57.1 5.8-90.5 28.9-120 17.8-22.6 34.6-44 34.6-76.8 0-65.1-30.8-91.9-32.1-93-5.7-4.8-6.5-13.4-1.7-19.1 4.8-5.7 13.2-6.6 19-1.8 1.7 1.4 41.9 35.3 41.9 114 0 42.2-21.5 69.5-40.4 93.6-22.4 28.5-40.1 51-24.7 94 2.5 7.1-1.1 14.9-8.2 17.4-1.5 0.4-3 0.7-4.5 0.7zM536.5 405.9c-5.6 0-10.8-3.5-12.8-9-20.4-57.1 5.8-90.5 28.9-120 17.8-22.6 34.6-44 34.6-76.8 0-65.1-30.8-91.9-32.1-93-5.7-4.8-6.5-13.4-1.7-19.1 4.8-5.7 13.2-6.6 19-1.8 1.7 1.4 41.9 35.3 41.9 114 0 42.2-21.5 69.5-40.4 93.6-22.4 28.5-40.1 51-24.7 94 2.5 7.1-1.1 14.9-8.2 17.4-1.4 0.4-3 0.7-4.5 0.7zM137.4 913.8h612V941h-612z\" fill=\"#1afa29\" p-id=\"17465\" data-spm-anchor-id=\"a313x.7781069.0.i70\" class=\"selected\"></path></svg>', 0, '生活', '2023-05-10 13:59:19', 1);
INSERT INTO `b_label_grouping_info` VALUES (11, '移动开发', '<svg t=\"1678707889175\" class=\"icon\" viewBox=\"0 0 1024 1024\" version=\"1.1\" xmlns=\"http://www.w3.org/2000/svg\" p-id=\"12009\" width=\"20\" height=\"20\"><path d=\"M735.288889 56.888889h-446.577778c-40.201481 0-72.817778 36.977778-72.817778 82.488889v745.244444c0 45.511111 32.711111 82.488889 72.817778 82.488889h446.577778c40.201481 0 72.817778-36.977778 72.817778-82.488889V139.377778c0.094815-45.511111-32.616296-82.488889-72.817778-82.488889z m-124.965926 53.57037l-4.740741 43.614815H440.50963l-4.361482-43.614815z m150.565926 784.402963a17.066667 17.066667 0 0 1-16.118519 17.92h-465.54074a17.066667 17.066667 0 0 1-16.118519-17.92V129.137778a17.161481 17.161481 0 0 1 16.118519-17.92h91.875555a37.925926 37.925926 0 0 0 0.948148 6.257778l13.463704 56.888888a30.72 30.72 0 0 0 28.444444 24.841482h218.074075a30.72 30.72 0 0 0 28.918518-24.936296l13.558519-57.931852a36.882963 36.882963 0 0 0 0.758518-4.835556h68.835556a17.161481 17.161481 0 0 1 16.118518 17.92z\" fill=\"#d4237a\" p-id=\"12010\"></path><path d=\"M592.592593 825.742222h-161.185186a26.737778 26.737778 0 0 0 0 53.570371h161.185186a26.737778 26.737778 0 0 0 0-53.570371zM554.856296 392.248889a23.988148 23.988148 0 0 0-31.004444 13.748148l-76.705185 197.783704A23.988148 23.988148 0 0 0 460.894815 635.259259a24.462222 24.462222 0 0 0 8.628148 1.611852 23.988148 23.988148 0 0 0 22.376296-15.36L568.888889 423.253333a23.988148 23.988148 0 0 0-14.032593-31.004444zM360.296296 515.887407l39.253334-53.57037a23.988148 23.988148 0 0 0-38.684445-28.444444L310.139259 502.518519a23.988148 23.988148 0 0 0 0.853334 29.392592l51.2 62.198519a23.988148 23.988148 0 0 0 37.072592-30.435556zM653.558519 432.924444A23.988148 23.988148 0 1 0 616.296296 463.36l39.348148 47.407407L616.296296 564.717037a23.988148 23.988148 0 1 0 38.684445 28.444444l50.346666-68.551111a23.893333 23.893333 0 0 0-0.853333-29.392592z\" fill=\"#d4237a\" p-id=\"12011\"></path></svg>', 0, '移动开发', '2023-05-10 13:59:25', 1);
INSERT INTO `b_label_grouping_info` VALUES (12, '操作系统', '<svg t=\"1678707944794\" class=\"icon\" viewBox=\"0 0 1168 1024\" version=\"1.1\" xmlns=\"http://www.w3.org/2000/svg\" p-id=\"13120\" width=\"20\" height=\"20\"><path d=\"M787.333831 588.946093a75.189984 75.189984 0 0 0-75.093984 75.092984 75.189984 75.189984 0 0 0 75.093984 75.093984 75.165984 75.165984 0 0 0 75.067984-75.093984 75.165984 75.165984 0 0 0-75.067984-75.092984m0 222.841953a147.919968 147.919968 0 0 1-147.749968-147.748969c0-81.455982 66.292986-147.747968 147.749968-147.747968 81.431983 0 147.747968 66.291986 147.747968 147.747968 0 81.480982-66.315986 147.748968-147.747968 147.748969m89.452981 102.106978c0.342 0.244 1.317 0.585 2.315999 0.585h0.122l71.484985-39.496992c-1.706-4.022999-9.605998-23.478995-9.605998-43.739991a113.858976 113.858976 0 0 1 107.739977-113.613975c0.853-4.729999 5.557999-31.524993 5.557998-51.639989s-4.704999-46.88399-5.559998-51.637989a113.858976 113.858976 0 0 1-107.737977-113.565976c0-20.357996 7.923998-39.813991 9.605998-43.81299l-70.313985-39.057992-0.293-0.025a5.339999 5.339999 0 0 0-2.706 0.732 196.753958 196.753958 0 0 1-26.014994 21.819995c-21.699995 15.239997-42.153991 22.967995-60.951987 22.967995-18.919996 0-39.594991-7.874998-61.390987-23.405995a194.705958 194.705958 0 0 1-26.160994-22.283995 5.777999 5.777999 0 0 0-2.729999-0.707h-0.245l-72.824985 40.032992c1.706 4.046999 9.579998 23.479995 9.579998 43.73999A113.858976 113.858976 0 0 1 528.896886 614.350088c-0.854 4.729999-5.509999 31.524993-5.509998 51.639989s4.655999 46.90799 5.533998 51.661989a113.858976 113.858976 0 0 1 107.763977 113.589975c0 20.334996-7.972998 39.863991-9.679998 43.788991l68.973985 38.545992h0.146c1.024 0 1.95-0.365 2.317-0.585a200.849957 200.849957 0 0 1 26.428994-23.161995c22.088995-16.188997 43.079991-24.429995 62.414987-24.429995 19.552996 0 40.715991 8.387998 62.949986 24.917995 14.921997 11.068998 25.259995 22.185995 26.551995 23.575995m22.673995 108.397976c-13.774997-0.025-27.135994-5.704999-35.765993-15.213996-11.775997-12.896997-49.005989-46.44599-79.530982-46.44599-30.280993 0-68.119985 33.767993-78.993984 45.54399-8.606998 9.386998-21.869995 14.969997-35.499992 14.969997-6.483999 0-12.603997-1.243-18.186996-3.706l-0.975-0.414-92.69698-51.833988-0.926-0.683a44.46999 44.46999 0 0 1-14.920996-53.735989c0.049-0.146 8.532998-19.699996 8.532998-37.594992a98.376979 98.376979 0 0 0-98.254979-98.254978h-3.900999c-15.505997 0-28.159994-13.799997-32.182993-35.108993-0.341-1.706-7.923998-42.178991-7.923999-74.044984 0-31.913993 7.557998-72.361984 7.899999-74.117984 4.119999-21.576995 16.992996-35.449992 32.816993-35.083993h3.266999a98.400979 98.400979 0 0 0 98.279979-98.303978c0-17.846996-8.484998-37.399992-8.559998-37.619992a44.34899 44.34899 0 0 1 15.068996-53.661989l0.95-0.658 97.839979-53.759988 1.025-0.415c5.485999-2.364999 11.507998-3.534999 17.895996-3.534999 13.604997 0 26.891994 5.436999 35.595992 14.629997 11.579998 12.115997 48.17699 43.640991 77.774984 43.64099 29.354994 0 65.755986-30.914993 77.287983-42.812991 8.654998-8.996998 21.844995-14.335997 35.302992-14.335997 6.509999 0 12.653997 1.22 18.285997 3.657l0.974999 0.414 94.47698 52.516988 0.95 0.658c16.895996 11.751997 23.332995 34.889993 14.945997 53.735989-0.05 0.146-8.532998 19.699996-8.532999 37.545992a98.400979 98.400979 0 0 0 98.254979 98.279978h3.29c15.775997-0.34 28.719994 13.506997 32.817993 35.107993 0.317 1.707 7.899998 42.203991 7.899998 74.069984 0 31.889993-7.582998 72.410984-7.949998 74.117984-4.045999 21.551995-16.991996 35.424992-32.766993 35.059993h-3.291a98.400979 98.400979 0 0 0-98.254979 98.278978c0 17.846996 8.483998 37.449992 8.556999 37.619992a44.42199 44.42199 0 0 1-15.017997 53.710989l-0.927 0.683-96.133979 53.100988-0.975 0.415a44.88499 44.88499 0 0 1-17.846996 3.583999z m222.061952-705.339848a48.76199 48.76199 0 1 1-97.523979 0V121.977194c0-13.457997-10.873998-24.453995-24.307995-24.453995H121.829974c-13.262997 0-24.306995 10.970998-24.306995 24.453995v584.995874c0 13.457997 10.873998 24.453995 24.307995 24.453995h219.499953a48.76199 48.76199 0 1 1 0 97.523979H48.908989A48.76199 48.76199 0 0 1 0 780.191052V48.76121C0 21.822215 21.893995 0.00022 48.907989 0.00022h1023.706781a48.76199 48.76199 0 0 1 48.907989 48.76199v268.189942zM219.427953 975.23701c0-26.940994 21.991995-48.76199 48.981989-48.761989h194.607959a48.76199 48.76199 0 1 1 0 97.523979H268.433942a48.76199 48.76199 0 0 1-49.005989-48.76199z\" p-id=\"13121\" fill=\"#f19610\"></path></svg>', 0, NULL, NULL, NULL);
INSERT INTO `b_label_grouping_info` VALUES (13, '开源', '<svg t=\"1678707776378\" class=\"icon\" viewBox=\"0 0 1024 1024\" version=\"1.1\" xmlns=\"http://www.w3.org/2000/svg\" p-id=\"10950\" width=\"20\" height=\"20\"><path d=\"M548.266667 928c-108.8 0-215.466667-40.533333-298.666667-123.733333C172.8 725.333333 128 618.666667 128 505.6c0-113.066667 44.8-217.6 123.733333-298.666667C330.666667 130.133333 437.333333 85.333333 548.266667 85.333333 661.333333 85.333333 768 130.133333 846.933333 209.066667c8.533333 8.533333 8.533333 21.333333 0 29.866666s-21.333333 8.533333-29.866666 0C744.533333 166.4 650.666667 128 548.266667 128c-100.266667 0-196.266667 38.4-266.666667 110.933333C209.066667 309.333333 170.666667 405.333333 170.666667 505.6s38.4 196.266667 110.933333 266.666667c147.2 147.2 388.266667 147.2 535.466667 0 8.533333-8.533333 21.333333-8.533333 29.866666 0s8.533333 21.333333 0 29.866666c-83.2 83.2-189.866667 125.866667-298.666666 125.866667z\" p-id=\"10951\" fill=\"#ffa502\"></path><path d=\"M548.266667 733.866667c-57.6 0-117.333333-21.333333-160-66.133334-42.666667-42.666667-66.133333-100.266667-66.133334-160s23.466667-117.333333 66.133334-160c89.6-89.6 232.533333-89.6 322.133333 0 8.533333 8.533333 8.533333 21.333333 0 29.866667s-21.333333 8.533333-29.866667 0c-72.533333-72.533333-189.866667-72.533333-262.4 0-34.133333 34.133333-53.333333 81.066667-53.333333 130.133333s19.2 96 53.333333 130.133334c72.533333 72.533333 189.866667 72.533333 262.4 0 8.533333-8.533333 21.333333-8.533333 29.866667 0s8.533333 21.333333 0 29.866666c-44.8 44.8-102.4 66.133333-162.133333 66.133334z\" p-id=\"10952\" fill=\"#ffa502\"></path></svg>', 0, NULL, NULL, NULL);
INSERT INTO `b_label_grouping_info` VALUES (14, '其他', '<svg t=\"1678708593734\" class=\"icon\" viewBox=\"0 0 1024 1024\" version=\"1.1\" xmlns=\"http://www.w3.org/2000/svg\" p-id=\"18907\" width=\"20\" height=\"20\"><path d=\"M512 512m-445 0a445 445 0 1 0 890 0 445 445 0 1 0-890 0Z\" fill=\"#E6F3FF\" p-id=\"18908\"></path><path d=\"M466 480.3H356.9c-8.3 0-15-6.7-15-15V356.1c0-8.3 6.7-15 15-15H466c8.3 0 15 6.7 15 15v109.1c0 8.4-6.7 15.1-15 15.1z\" fill=\"#2398F9\" p-id=\"18909\"></path><path d=\"M660.1 480.3H551c-8.3 0-15-6.7-15-15V356.1c0-8.3 6.7-15 15-15h109.1c8.3 0 15 6.7 15 15v109.1c0 8.4-6.7 15.1-15 15.1z\" fill=\"#B8DDFF\" p-id=\"18910\"></path><path d=\"M466 672.5H356.9c-8.3 0-15-6.7-15-15V548.4c0-8.3 6.7-15 15-15H466c8.3 0 15 6.7 15 15v109.1c0 8.3-6.7 15-15 15z\" fill=\"#22D0FF\" p-id=\"18911\"></path><path d=\"M595.4 686.8l-73.7-73.7c-5.6-5.6-5.6-14.7 0-20.3l73.7-73.7c5.6-5.6 14.7-5.6 20.3 0l73.7 73.7c5.6 5.6 5.6 14.7 0 20.3l-73.7 73.7c-5.6 5.6-14.7 5.6-20.3 0z\" fill=\"#2398F9\" p-id=\"18912\"></path></svg>', 0, NULL, NULL, NULL);
INSERT INTO `b_label_grouping_info` VALUES (15, '游戏 ', '<svg t=\"1678708614470\" class=\"icon\" viewBox=\"0 0 1024 1024\" version=\"1.1\" xmlns=\"http://www.w3.org/2000/svg\" p-id=\"20151\" width=\"20\" height=\"20\"><path d=\"M812.221666 596.623329c39.231513 0 71.148433-31.917944 71.148433-71.151503 0-39.23049-31.91692-71.14741-71.148433-71.14741-39.23356 0-71.151503 31.91692-71.151503 71.14741C741.069139 564.705386 772.988106 596.623329 812.221666 596.623329zM812.221666 491.163439c18.917862 0 34.30941 15.391549 34.30941 34.308387 0 18.919908-15.391549 34.31248-34.30941 34.31248-18.919908 0-34.31248-15.392572-34.31248-34.31248C777.908162 506.553964 793.300734 491.163439 812.221666 491.163439z\" fill=\"#FF9000\" p-id=\"20152\"></path><path d=\"M664.837944 467.568045c39.231513 0 71.14741-31.917944 71.14741-71.151503 0-39.23049-31.915897-71.14741-71.14741-71.14741s-71.14741 31.91692-71.14741 71.14741C593.690534 435.649078 625.606431 467.568045 664.837944 467.568045zM664.837944 362.107131c18.917862 0 34.308387 15.391549 34.308387 34.308387 0 18.919908-15.390525 34.31248-34.308387 34.31248s-34.308387-15.392572-34.308387-34.31248C630.529557 377.49868 645.920082 362.107131 664.837944 362.107131z\" fill=\"#FF9000\" p-id=\"20153\"></path><path d=\"M981.344504 494.64575l0-95.1685c0-38.181601-6.478552-71.91796-19.254529-100.272751-11.484565-25.486464-28.085662-46.812165-49.343825-63.382563-44.128033-34.399461-96.464005-39.535435-123.733069-39.535435L530.602683 196.286502l0-47.291073c0-10.172687-8.246825-18.419512-18.419512-18.419512s-18.419512 8.246825-18.419512 18.419512l0 47.291073L220.949204 196.286502c-27.269064 0-79.605036 5.136997-123.733069 39.535435-21.25714 16.570397-37.859259 37.896098-49.342802 63.382563-12.777001 28.354791-19.254529 62.09115-19.254529 100.272751l0 95.158267-0.01535 27.733645 0 151.796218c0 65.411779 53.218062 118.628818 118.631888 118.628818l47.955198 0c3.240811 0.156566 17.126053 0.596588 33.735335-2.153036 26.479071-4.379751 46.794769-14.349823 60.382229-29.635971 14.512529-16.32685 22.955828-28.534893 24.713868-31.140231 14.757099-19.72525 36.772508-31.719422 65.444525-35.655058 5.853311-0.803295 11.965519-1.336438 18.674315-1.626034l70.296019 0 0.316202 0.002047 0.37453-0.01535c0.423649-0.008186 42.156122-1.160429 61.145615 22.577205 6.353708 7.942903 17.944697 9.234315 25.889647 2.877537 7.943926-6.354731 9.231245-17.945721 2.877537-25.889647-29.641087-37.052894-84.874039-36.561707-90.896196-36.390815l-70.39528 0-0.76748 0.016373c-8.03193 0.334621-15.400758 0.974188-22.526039 1.952468-39.023782 5.354961-69.39858 22.364357-90.282213 50.554396l-0.567935 0.811482c-0.075725 0.113587-7.688099 11.539824-21.830191 27.450189-16.142655 18.159592-52.369741 20.199041-64.925708 19.462261l-0.587378-0.037862-49.0266 0c-45.101197 0-81.792864-36.68962-81.792864-81.789795L65.442478 522.379395l0.01535-27.733645 0-95.1685c0-61.172221 18.305925-106.458637 54.407144-134.601604 35.43914-27.626197 78.543867-31.751145 101.084233-31.751145l568.062853 0c22.540366 0 65.645093 4.124947 101.084233 31.751145 36.102243 28.142967 54.408167 73.429383 54.408167 134.601604l0 95.178733 0.01535 27.713178 0 151.796218c0 45.099151-36.691667 81.789795-81.792864 81.789795l-48.439222 0-1.174756 0.037862c-11.68718 0.750083-48.713468-1.222851-64.924685-19.462261-14.143115-15.910365-21.75549-27.336602-21.816888-27.429723l-0.581238-0.831948c-20.883633-28.190039-51.259454-45.199435-90.278119-50.553373-5.613858-0.772596-19.290345-1.197268-36.586267-1.673106-4.142343-0.113587-7.53358-0.205685-9.326413-0.280386-10.161431-0.423649-18.74697 7.472182-19.170618 17.635659s7.472182 18.74697 17.635659 19.170618c1.973958 0.081864 5.449106 0.178055 9.849322 0.298805 8.532327 0.234337 28.514427 0.784876 32.586163 1.343601 28.6669 3.934612 50.682309 15.928784 65.440431 35.653011 1.75804 2.604314 10.200316 14.813381 24.714891 31.142278 13.586436 15.284101 33.902134 25.255197 60.381206 29.634948 11.480472 1.899256 21.658276 2.275833 27.919886 2.275833 2.797719 0 4.814656-0.074701 5.815449-0.122797l47.956222 0c65.413825 0 118.631888-53.217039 118.631888-118.628818L981.359854 522.358929 981.344504 494.64575z\" fill=\"#FF9000\" p-id=\"20154\"></path><path d=\"M268.360004 585.593112c0.013303 0 0.025583 0 0.039909 0 10.153244 0 18.396999-8.221242 18.418488-18.380626l0.220011-105.304348 105.065917-0.23843c10.172687-0.022513 18.400069-8.28878 18.377556-18.461467-0.022513-10.159384-8.264221-18.377556-18.418488-18.377556-0.014326 0-0.028653 0-0.042979 0l-104.905258 0.23843 0.220011-105.146758c0.021489-10.172687-8.207939-18.436908-18.380626-18.458397-0.013303 0-0.025583 0-0.039909 0-10.154267 0-18.396999 8.222265-18.418488 18.380626l-0.220011 105.307418-105.065917 0.23843c-10.172687 0.022513-18.400069 8.28878-18.377556 18.461467 0.022513 10.159384 8.264221 18.377556 18.418488 18.377556 0.013303 0 0.028653 0 0.042979 0l104.905258-0.23843-0.220011 105.142665C249.957889 577.307402 258.187317 585.572646 268.360004 585.593112z\" fill=\"#FF9000\" p-id=\"20155\"></path></svg>', 0, NULL, NULL, NULL);
INSERT INTO `b_label_grouping_info` VALUES (16, '软件工程', '<svg t=\"1678708647514\" class=\"icon\" viewBox=\"0 0 1024 1024\" version=\"1.1\" xmlns=\"http://www.w3.org/2000/svg\" p-id=\"21245\" width=\"20\" height=\"20\"><path d=\"M530.324211 630.74807l27.486315 46.708772 37.367018 63.41614a3.413333 3.413333 0 0 1 0 5.030176q-31.079298 30.181053-61.799298 60.901053c-1.796491 1.796491-2.694737 1.97614-4.670878 0l-62.158596-61.080702c-1.437193-1.437193-1.97614-2.515088 0-4.491228q31.977544-53.894737 63.775439-107.789474l1.437193-2.155789z\" p-id=\"21246\" data-spm-anchor-id=\"a313x.7781069.0.i91\" class=\"selected\" fill=\"#13227a\"></path><path d=\"M842.374737 826.385965a9.162105 9.162105 0 0 1-8.982456-8.802807c-2.874386-87.848421-57.487719-196.53614-156.115088-234.442105a9.341754 9.341754 0 0 1-5.569123-6.28772 8.802807 8.802807 0 0 1 2.15579-8.08421c42.037895-45.630877 60.721404-89.824561 60.541754-140.665263a181.804912 181.804912 0 0 0-72.757895-143.719299 9.162105 9.162105 0 0 1-2.694737-12.575438 8.982456 8.982456 0 0 1 12.39579-2.515088 197.614035 197.614035 0 0 1 81.021754 159.528421 211.806316 211.806316 0 0 1-56.769122 143.719298c98.447719 44.373333 152.701754 155.396491 155.755789 245.580351a8.982456 8.982456 0 0 1-8.623158 9.341755z m-566.613333 0a8.802807 8.802807 0 0 1-8.443509-9.521404 258.515088 258.515088 0 0 1 206.596491-239.112982A158.091228 158.091228 0 0 1 415.169123 538.947368a8.982456 8.982456 0 0 1 13.114386-12.395789 141.38386 141.38386 0 0 0 211.267368-8.26386 8.982456 8.982456 0 1 1 13.832983 11.317895 157.73193 157.73193 0 0 1-70.242807 49.403509 259.054035 259.054035 0 0 1 200.488421 237.136842 8.982456 8.982456 0 0 1-8.623158 9.521403 8.802807 8.802807 0 0 1-9.341755-8.443508 240.909474 240.909474 0 0 0-480.920701 0 8.982456 8.982456 0 0 1-8.982456 9.162105z m360.735438-495.292632a9.162105 9.162105 0 0 1-6.467368-2.694737 141.204211 141.204211 0 0 0-197.614035-1.257543 9.162105 9.162105 0 0 1-12.755088 0 8.982456 8.982456 0 0 1 0-12.755088 159.169123 159.169123 0 0 1 223.663158 1.616842 8.982456 8.982456 0 0 1-6.28772 15.270175z\" p-id=\"21247\" data-spm-anchor-id=\"a313x.7781069.0.i84\" class=\"\" fill=\"#e71c80\"></path><path d=\"M591.584561 512a84.794386 84.794386 0 0 1-61.978947-26.947368 85.153684 85.153684 0 1 1 0-116.77193 85.153684 85.153684 0 1 1 61.978947 143.719298z m0-123.778246a38.804211 38.804211 0 1 0 38.624562 38.624562 38.624561 38.624561 0 0 0-38.624562-38.624562z m-123.778245 0a38.804211 38.804211 0 1 0 38.624561 38.624562A38.804211 38.804211 0 0 0 467.087719 388.221754z\" p-id=\"21248\" data-spm-anchor-id=\"a313x.7781069.0.i88\" class=\"selected\" fill=\"#13227a\"></path></svg>', 0, NULL, NULL, NULL);
INSERT INTO `b_label_grouping_info` VALUES (17, '项目', '<svg t=\"1678708736713\" class=\"icon\" viewBox=\"0 0 1024 1024\" version=\"1.1\" xmlns=\"http://www.w3.org/2000/svg\" p-id=\"22914\" id=\"mx_n_1678708736714\" width=\"20\" height=\"20\"><path d=\"M997.76 494.72l-133.12-76.8 128-72.32a32 32 0 0 0 0-55.68L704 123.52a32 32 0 0 0-32 0L512 213.76 346.88 118.4a32 32 0 0 0-32 0L26.24 284.8a32 32 0 0 0 0 55.68l133.76 76.8-128 72.32a32 32 0 0 0 0 55.68L192 635.52v114.56a32 32 0 0 0 16 27.52l288 166.4a32 32 0 0 0 32 0l288-166.4a32 32 0 1 0-32-55.68L512 880l-256-147.84v-59.52l64 39.04a32 32 0 0 0 32 0L512 620.8l166.4 96a32 32 0 0 0 32 0l106.24-64 181.12-105.6a32 32 0 0 0 0-55.68zM512 288l224 128L512 547.2l-224-128L384 362.88z m172.8-99.84l224 128-108.8 64L576 250.88z m-353.92-5.12L448 250.88l-91.52 53.12L224 380.16 106.24 312.32zM114.56 517.12l108.8-64L320 512l128 71.68-108.8 64z m579.2 135.04L576 583.68 700.16 512l99.84-57.6 117.12 67.84z\" fill=\"#d4237a\" p-id=\"22915\"></path></svg>', 0, '开源项目', '2023-03-13 19:58:28', 1);
INSERT INTO `b_label_grouping_info` VALUES (18, '运维', NULL, 0, '运维', '2024-02-23 12:03:10', 1);
INSERT INTO `b_label_grouping_info` VALUES (19, '英语', '<svg t=\"1729135678499\" class=\"icon\" viewBox=\"0 0 1079 1024\" version=\"1.1\" xmlns=\"http://www.w3.org/2000/svg\" p-id=\"7035\" width=\"20\" height=\"20\"><path d=\"M698.540252 68.643549C887.393266 68.643549 1037.27747 209.028141 1037.27747 378.539356c0 76.148276-27.649579 140.421238-72.258883 187.060483-16.680888 23.53871 11.000559 105.2988-11.008526 122.841696-22.291109 17.550862-72.290751-11.577355-110.912208-17.542895h-27.965064c-33.299634 11.583728-77.623727 17.542895-116.592537 17.542895-188.851421 0-338.737218-140.384591-338.737219-309.902179 0-169.511215 149.885798-309.895806 338.737219-309.895807z m11.346318 156.33572h-30.625976v54.086611h-111.00303v151.039391h29.705014v-17.834481h81.299609v104.745904h30.625976v-104.71563h81.645368v17.834481h29.67952v-151.045764h-111.324888v-54.110512z m-30.625976 83.182961V383.203121h-81.29961v-75.040891h81.29961z m112.242664 0V383.203121h-81.616688v-75.040891h81.645368-0.02868z m0 0\" fill=\"#5C84FF\" p-id=\"7036\"></path><path d=\"M362.752343 648.34622l-38.116362 100.282902h77.215827l-37.823184-103.469623z\" fill=\"#FBBA1A\" p-id=\"7037\"></path><path d=\"M698.540252 724.613998c-208.284041 0-374.149648-152.771373-378.529796-338.855128-165.239416 25.398161-288.944713 157.199321-288.944713 313.133514 0 78.018881 28.301263 143.819876 74.078501 191.639801 17.053734 24.1171-11.277803 107.892791 11.273023 125.86908 22.842411 17.974696 74.04026-11.86416 113.619327-17.976289h28.656583c34.144115 11.86416 79.530979 17.976289 119.457399 17.976289 184.138261 0 332.110434-130.2556 346.014094-292.524993-8.675846 0.487568-17.260871 0.737726-25.624418 0.737726z m-264.645971 111.799709l-21.480088-58.947953h-98.37087l-21.448222 58.947953h-37.169906l88.108038-228.753939h39.746369l88.07617 228.753939h-37.461491z\" fill=\"#FBBA1A\" p-id=\"7038\"></path><path d=\"M74.080094 311.078079l10.326568 42.478982 31.623419 30.198956-42.478982 10.324974-30.205329 31.618639-10.3186-42.478982L1.402157 353.021692l42.478982-10.326567 30.175055-31.617046h0.0239zM190.874988 54.763789l56.602527 33.137111 65.501443-2.670471-33.105244 56.602526 2.668878 65.533311-56.600933-33.137112-65.533311 2.670472 33.105245-56.602527-2.668879-65.501443 0.030274-0.031867zM1062.804694 1.604514l-10.618153 64.698389 25.954244 60.224235-64.699983-10.624526-60.224234 25.960617 10.624525-64.698389-25.960617-60.224235 64.699983 10.618152 60.224235-25.954243z m0 0\" fill=\"#FCC736\" p-id=\"7039\"></path></svg>', 0, '英语学习', '2024-10-24 09:54:46', 1);

-- ----------------------------
-- Table structure for b_label_info
-- ----------------------------
DROP TABLE IF EXISTS `b_label_info`;
CREATE TABLE `b_label_info`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'id',
  `label_grouping_id` bigint NULL DEFAULT NULL COMMENT '分组id',
  `label_name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_cs_0900_ai_ci NOT NULL COMMENT '名称',
  `label_describe` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_cs_0900_ai_ci NULL COMMENT '描述',
  `label_cover` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_cs_0900_ai_ci NULL COMMENT '封面',
  `follow_number` int NULL DEFAULT 0 COMMENT '关注数',
  `article_number` int NULL DEFAULT 0 COMMENT '文章数',
  `state` int NULL DEFAULT 0 COMMENT '状态（0：正常，1：已删除）',
  `create_time` datetime NULL DEFAULT NULL,
  `create_by` bigint NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 57 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_cs_0900_ai_ci COMMENT = '标签信息表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of b_label_info
-- ----------------------------
INSERT INTO `b_label_info` VALUES (2, 1, 'Java', 'mainsb fhsdbfs', '<svg t=\"1717317365777\" class=\"icon\" viewBox=\"0 0 1024 1024\" version=\"1.1\" xmlns=\"http://www.w3.org/2000/svg\" p-id=\"22530\" width=\"48\" height=\"48\"><path d=\"M725.952 170.048c-29.248 20.096-56.704 38.4-87.808 62.208-23.744 18.24-65.792 45.696-67.648 78.592-3.648 53.056 78.656 102.4 34.752 170.048-16.448 25.6-43.904 36.608-78.592 53.056-3.712-7.296 9.088-14.656 14.592-21.952 54.848-78.656-56.704-104.256-42.048-201.152 14.656-96.896 124.352-128 226.752-140.8z\" fill=\"#FF1515\" p-id=\"22531\"></path><path d=\"M563.2 0c16.448 16.448 29.248 47.552 29.248 78.656 0 96.896-102.4 151.744-151.744 215.744-11.008 14.656-25.6 36.544-25.6 60.352 0 52.992 54.848 111.552 74.944 153.6C457.152 486.4 415.104 455.296 384 420.48 354.752 384 323.648 327.296 351.104 276.096c40.192-74.944 162.688-120.64 206.592-201.152 11.008-20.096 20.096-51.2 5.504-74.944z\" fill=\"#FF1515\" p-id=\"22532\"></path><path d=\"M353.6 500.544c9.728-2.752 19.072-5.376 26.752-8.64a124.544 124.544 0 0 0-28.288 0.832c-4.672 0.512-8.768 0.96-11.968 0.96l-10.048 0.768c-50.56 3.84-150.72 11.328-149.056 52.288 0 36.544 93.312 45.696 133.504 49.344 120.704 7.36 296.256-3.648 352.896-45.696 9.152-5.504 25.6-16.448 21.952-23.744-89.6 16.448-219.392 25.6-325.44 20.096-25.6 0-53.056 0-71.36-14.656 12.8-17.92 38.208-25.088 61.056-31.552zM327.04 609.856c3.328-3.072 5.888-5.504 0.256-6.4-21.952 5.44-71.296 18.24-69.504 45.696 1.856 21.952 49.408 36.544 76.8 42.048 107.904 21.952 254.208 5.504 329.152-29.248-10.368-1.728-19.52-9.216-28.8-16.832-10.368-8.384-20.864-16.96-33.344-17.92-7.296-1.216-15.36 1.6-24.32 4.736a162.56 162.56 0 0 1-14.08 4.416c-65.856 12.8-199.296 25.6-241.408-16.448-1.728-3.584 2.112-7.104 5.248-10.048zM360.192 711.68c2.112-2.24 4.48-4.736 0-5.824l-10.752 2.816c-29.568 7.552-58.112 14.912-53.248 55.68 78.656 60.352 270.656 40.192 351.104-7.296-10.624-2.496-18.688-10.048-27.008-17.92-10.24-9.6-20.8-19.52-36.992-20.48-7.808-1.28-15.68 2.048-23.488 5.376-3.2 1.344-6.272 2.688-9.408 3.712-56.704 12.8-170.048 27.456-192-12.8-0.704-0.64 0.512-1.92 1.792-3.328z\" fill=\"#2365C4\" p-id=\"22533\"></path><path d=\"M264.064 783.36c9.728-0.576 18.56-1.088 24.832-2.56-42.048-36.544-177.344-20.096-179.2 36.544-1.792 32.96 40.256 56.704 75.008 67.712 107.84 36.544 279.744 38.4 418.752 23.744 64-7.296 221.248-34.752 213.888-104.256-3.648-18.24-20.096-31.04-38.4-32.896 14.656 64-102.4 84.096-164.544 91.456-135.296 14.592-298.048 10.944-378.496-20.16-14.656-5.44-36.608-20.096-34.752-32.896 2.688-22.848 36.032-24.96 62.912-26.624z\" fill=\"#2365C4\" p-id=\"22534\"></path><path d=\"M499.2 987.456c-93.248-11.008-182.848-23.808-257.856-56.704 197.504 47.552 486.4 43.904 625.408-56.704 1.984-1.472 4.032-3.392 6.016-5.376 5.44-5.12 11.136-10.56 17.728-9.216-30.912 92.8-140.224 108.8-245.184 124.032-12.8 1.92-25.6 3.776-38.208 5.76 0-1.792-107.904-1.792-107.904-1.792zM852.096 565.056c-1.792-75.008-89.6-91.456-140.8-47.552 40.256-9.152 75.008 9.152 82.304 36.544 11.968 58.304-42.048 101.952-79.168 131.904-8.32 6.72-15.744 12.672-21.376 18.048 69.44-3.648 162.688-53.056 159.04-138.944z\" fill=\"#2365C4\" p-id=\"22535\"></path></svg>', 1, 5, 0, '2022-08-16 12:12:35', 1);
INSERT INTO `b_label_info` VALUES (3, 12, '哲学', '哲学', '<svg t=\"1717330110873\" class=\"icon\" viewBox=\"0 0 1024 1024\" version=\"1.1\" xmlns=\"http://www.w3.org/2000/svg\" p-id=\"23808\" width=\"48\" height=\"48\"><path d=\"M874.418207 874.528084c-22.523165 22.523165-47.093891 42.99877-73.712178 60.914925-26.618286 17.916154-54.260353 33.784748-83.94998 46.070111-58.867364 25.594506-122.341739 39.92743-187.351785 41.97499-64.498155 2.04756-130.020091-7.678352-191.958796-30.201517-61.938705-22.011275-119.782289-56.819804-169.435631-101.866135-49.653342-45.046331-91.116442-100.330464-119.782288-162.269169-29.177737-61.938705-45.558221-130.020091-47.605782-199.637148C-1.937683 460.408975 10.34768 390.280028 35.942186 325.269982c12.797253-32.249078 29.177737-63.986265 48.117671-93.164002 19.451825-29.177737 41.4631-56.307914 66.033826-81.390529 25.082616-24.570726 52.212793-47.093891 81.39053-66.545716C261.17384 65.2298 292.399137 49.361206 325.160105 36.052063c65.010046-25.594506 135.138992-37.879869 204.244159-35.832309 69.105167 2.04756 137.698443 18.428044 199.637148 47.605782 61.938705 29.177737 117.222838 70.640837 162.269169 120.294179S971.16544 276.128531 993.176715 338.067235c22.523165 61.938705 32.249078 127.460641 30.201518 191.958797-2.04756 64.498155-16.380484 128.484421-41.97499 187.351784-12.797253 29.177737-28.153957 57.331694-46.070111 83.949981-17.916154 26.106396-38.391759 50.677122-60.914925 73.200287z m0 0c22.523165-22.523165 42.48688-47.605781 60.403034-74.224068 17.404264-26.618286 32.249078-55.284133 44.022551-84.46187 23.546946-59.379254 34.296638-122.853629 31.737188-185.816114-2.04756-62.962485-17.404264-124.90119-44.022551-180.697214-26.618286-55.796023-64.498155-105.961255-109.544486-146.400575-45.046331-40.43932-97.771013-71.664617-154.078927-91.116442-55.796023-19.963715-115.175278-28.665847-173.530752-26.618286-29.177737 1.02378-57.843584 5.118901-86.509431 11.261583-28.153957 6.142681-55.284133 15.356704-81.902419 26.618286-52.724683 23.035056-100.842354 55.796023-141.793564 96.747233-20.475605 20.475605-38.903649 42.48688-54.772243 66.033826-16.380484 23.546946-30.201517 48.629562-41.97499 75.247848-23.035056 52.724683-36.344199 110.056376-38.39176 168.41185-2.04756 58.355474 6.654572 117.734728 26.618287 173.530752 19.963715 55.796023 50.677122 108.520706 91.628332 154.078927 40.43932 45.046331 90.604552 82.9262 146.400575 109.544486 55.796023 26.618286 118.246618 41.4631 181.209103 44.022551 62.962485 2.04756 126.94875-8.702132 185.816115-32.249078 29.689627-11.773473 57.843584-26.106396 84.46187-44.02255 26.618286-17.404264 51.189012-37.367979 74.224068-59.891145z\" fill=\"#333333\" p-id=\"23809\"></path></svg>', 0, 2, 0, '2022-10-01 21:30:25', 1);
INSERT INTO `b_label_info` VALUES (4, 2, 'vue', 'vue', '<svg t=\"1717330143002\" class=\"icon\" viewBox=\"0 0 1024 1024\" version=\"1.1\" xmlns=\"http://www.w3.org/2000/svg\" p-id=\"24882\" width=\"48\" height=\"48\"><path d=\"M615.6 123.6h165.5L512 589.7 242.9 123.6H63.5L512 900.4l448.5-776.9z\" fill=\"#41B883\" p-id=\"24883\"></path><path d=\"M781.1 123.6H615.6L512 303 408.4 123.6H242.9L512 589.7z\" fill=\"#34495E\" p-id=\"24884\"></path></svg>', 1, 10, 0, '2022-10-01 21:30:36', 1);
INSERT INTO `b_label_info` VALUES (5, 2, 'html', 'html', '<svg t=\"1717330159484\" class=\"icon\" viewBox=\"0 0 1024 1024\" version=\"1.1\" xmlns=\"http://www.w3.org/2000/svg\" p-id=\"26003\" width=\"48\" height=\"48\"><path d=\"M145.6 0c-44.8 0-80 36.8-80 81.6v860.8c0 44.8 35.2 81.6 80 81.6h732.8c44.8 0 81.6-36.8 81.6-81.6V324.8L657.6 0h-512z\" fill=\"#F7622C\" p-id=\"26004\"></path><path d=\"M960 326.4v16H755.2s-100.8-20.8-99.2-108.8c0 0 4.8 92.8 97.6 92.8H960z\" fill=\"#F54921\" p-id=\"26005\"></path><path d=\"M657.6 0v233.6c0 25.6 17.6 92.8 97.6 92.8H960L657.6 0z\" fill=\"#FFFFFF\" p-id=\"26006\"></path><path d=\"M366.4 812.8h-4.8L240 758.4c-8-3.2-12.8-11.2-12.8-20.8 0-8 4.8-16 12.8-19.2l121.6-54.4c1.6 0 3.2-1.6 4.8-1.6 8 0 14.4 6.4 14.4 14.4 0 6.4-3.2 11.2-8 14.4l-112 48 112 46.4c4.8 3.2 8 8 8 14.4 0 8-6.4 12.8-14.4 12.8z m136-176L432 849.6c-3.2 6.4-8 9.6-14.4 9.6-9.6 0-16-6.4-16-14.4 0-1.6 1.6-3.2 1.6-4.8l70.4-211.2c3.2-6.4 8-11.2 14.4-11.2 9.6 0 14.4 6.4 14.4 14.4v4.8z m163.2 121.6L544 812.8h-4.8c-8 0-14.4-4.8-14.4-12.8 0-6.4 3.2-11.2 8-14.4l113.6-46.4-113.6-48c-4.8-3.2-8-8-8-14.4 0-8 6.4-14.4 14.4-14.4 1.6 0 3.2 1.6 4.8 1.6l121.6 54.4c8 3.2 12.8 11.2 12.8 19.2 0 9.6-4.8 17.6-12.8 20.8z\" fill=\"#FFFFFF\" p-id=\"26007\"></path></svg>', 0, 4, 0, '2022-10-01 21:30:48', 1);
INSERT INTO `b_label_info` VALUES (6, NULL, '生活', NULL, '<svg t=\"1717330180695\" class=\"icon\" viewBox=\"0 0 1024 1024\" version=\"1.1\" xmlns=\"http://www.w3.org/2000/svg\" p-id=\"27187\" width=\"48\" height=\"48\"><path d=\"M729.6 917.333333h-554.666667c-12.8 0-25.6-12.8-25.6-25.6s12.8-25.6 25.6-25.6h554.666667c12.8 0 25.6 12.8 25.6 25.6s-12.8 25.6-25.6 25.6zM789.333333 614.4H725.333333v-213.333333h64c59.733333 0 106.666667 46.933333 106.666667 106.666666s-46.933333 106.666667-106.666667 106.666667z m-12.8-51.2h8.533334c29.866667 0 55.466667-25.6 55.466666-55.466667 0-29.866667-25.6-55.466667-55.466666-55.466666h-8.533334v110.933333z\" fill=\"#6A3906\" p-id=\"27188\"></path><path d=\"M469.333333 840.533333h-29.866666c-170.666667 0-307.2-115.2-307.2-256V345.6c0-12.8 12.8-25.6 25.6-25.6h593.066666c8.533333 0 12.8 4.266667 17.066667 8.533333 4.266667 4.266667 8.533333 12.8 8.533333 17.066667v238.933333c-4.266667 145.066667-140.8 256-307.2 256zM179.2 375.466667v209.066666c0 110.933333 115.2 204.8 256 204.8h34.133333c136.533333 0 251.733333-89.6 256-200.533333v-55.466667V426.666667 375.466667H179.2z\" fill=\"#6A3906\" p-id=\"27189\"></path><path d=\"M661.333333 469.333333v-34.133333c0-12.8-8.533333-21.333333-21.333333-21.333333H260.266667c-12.8 0-21.333333 8.533333-21.333334 21.333333v145.066667c0 89.6 89.6 162.133333 200.533334 162.133333h21.333333c110.933333 0 196.266667-72.533333 200.533333-162.133333 4.266667-17.066667 0-25.6 0-110.933334z\" fill=\"#F5CB2B\" p-id=\"27190\"></path><path d=\"M332.8 273.066667h-8.533333c-12.8 0-21.333333-8.533333-21.333334-21.333334V149.333333c0-8.533333 8.533333-21.333333 21.333334-21.333333h8.533333c12.8 0 21.333333 8.533333 21.333333 21.333333v98.133334c4.266667 17.066667-8.533333 25.6-21.333333 25.6zM456.533333 273.066667h-8.533333c-12.8 0-21.333333-8.533333-21.333333-21.333334V149.333333c0-8.533333 8.533333-21.333333 21.333333-21.333333h8.533333c12.8 0 21.333333 8.533333 21.333334 21.333333v98.133334c4.266667 17.066667-8.533333 25.6-21.333334 25.6zM580.266667 273.066667h-8.533334c-12.8 0-21.333333-8.533333-21.333333-21.333334V149.333333c0-12.8 8.533333-21.333333 21.333333-21.333333h8.533334c12.8 0 21.333333 8.533333 21.333333 21.333333v98.133334c0 17.066667-8.533333 25.6-21.333333 25.6z\" fill=\"#6A3906\" p-id=\"27191\"></path></svg>', 0, 1, 0, NULL, NULL);
INSERT INTO `b_label_info` VALUES (7, NULL, '阅读', NULL, '<svg t=\"1717330211062\" class=\"icon\" viewBox=\"0 0 1024 1024\" version=\"1.1\" xmlns=\"http://www.w3.org/2000/svg\" p-id=\"30310\" width=\"48\" height=\"48\"><path d=\"M511.999814 511.999814m-511.999814 0a511.999814 511.999814 0 1 0 1023.999628 0 511.999814 511.999814 0 1 0-1023.999628 0Z\" fill=\"#3BE3A4\" p-id=\"30311\"></path><path d=\"M466.087394 2.029381A518.050721 518.050721 0 0 1 511.999814 0c282.763534 0 511.999814 229.226971 511.999814 511.999814 0 8.685379-0.195491 17.314903-0.633018 25.897881-15.127267 1.349818-30.440716 2.029381-45.91242 2.029381-282.763534 0-511.999814-229.226971-511.999814-511.999813 0-8.685379 0.2048-17.314903 0.633018-25.897882z\" fill=\"#FFFFFF\" opacity=\".1\" p-id=\"30312\"></path><path d=\"M204.799926 921.636901V446.836201c0-56.552707 45.856565-102.399963 102.399962-102.399963h316.508976c56.543398 0 102.399963 45.847256 102.399963 102.399963v474.763464H279.272626a27.927263 27.927263 0 0 0-27.731772 31.297152A509.374651 509.374651 0 0 1 204.799926 921.636901zM772.654264 952.775799V460.799832a23.272719 23.272719 0 1 1 46.545438 0v460.837069a513.461341 513.461341 0 0 1-46.545438 31.138898zM724.312173 978.040663A510.175233 510.175233 0 0 1 511.999814 1023.999628c-76.241427 0-148.573037-16.663267-213.597013-46.545438h426.011772l-0.1024 0.586473z\" fill=\"#F8FEFB\" p-id=\"30313\"></path></svg>', 1, 4, 0, NULL, NULL);
INSERT INTO `b_label_info` VALUES (8, NULL, 'Go', NULL, '<svg t=\"1717330236115\" class=\"icon\" viewBox=\"0 0 1024 1024\" version=\"1.1\" xmlns=\"http://www.w3.org/2000/svg\" p-id=\"33734\" width=\"48\" height=\"48\"><path d=\"M230.89850712 280.26422483c51.06042542-43.20497523 109.97629962-66.77132575 188.5308013-74.6267759 47.13269964-3.92772438 86.40995051-7.85545018 121.75947554-7.85545017 70.69905013 3.92772438 129.61492572 23.56635051 176.74762535 62.84360136h7.8554488c31.42180067-15.71090032 62.84359998-15.71090032 90.33767625-3.92772578 31.42180067 15.71090032 51.06042542 47.13269964 54.98814981 86.40995048 3.92772438 39.27724947-7.85545018 74.62677453-39.27725086 98.19312505-7.85545018 3.92772438-11.78317456 7.85545018-11.78317455 11.78317456 0 0 0 7.85545018 3.92772577 11.78317593 15.71090032 66.77132575 15.71090032 137.47037588 15.71089895 200.31397448v106.04857521c0 7.85545018 0 15.71090032-7.85545017 23.56635052-3.92772438 3.92772438-7.85545018 7.85545018-15.71089895 7.85545015s-11.78317456-3.92772438-15.71090035-7.85545015c-7.85545018-7.85545018-7.85545018-15.71090032-3.92772576-23.56635052v-74.62677453c0-78.55450031 0-141.39810029-11.78317457-200.31397587-11.78317456-70.69905013-27.4940749-117.83174977-54.98814979-157.1090006-19.63862475-31.42180067-47.13269964-51.06042542-82.48222471-70.69905015-58.91587557-27.4940749-117.83174977-27.4940749-176.74762535-19.63862474-35.34952507 3.92772438-82.4822247 7.85545018-125.68720132 23.56635052-78.55450031 23.56635051-129.61492572 82.4822247-149.25355045 176.74762534-19.63862475 82.4822247-23.56635051 168.89217518-15.71089896 255.30212567 3.92772438 23.56635051 3.92772438 47.13269964 7.85545018 70.69905013v3.92772439c3.92772438 23.56635051-11.78317456 31.42180067-15.71090034 31.42180068h-3.92772578c-3.92772438 0-11.78317456-3.92772438-11.78317456-3.92772576-3.92772438-3.92772438-7.85545018-11.78317456-7.85545017-19.63862474-3.92772438-31.42180067-7.85545018-62.84359998-7.85545019-90.33767487-7.85545018-78.55450031 0-153.18127484 7.85545019-212.09715042v-3.92772577c-39.27724947-19.63862475-62.84359998-66.77132575-54.98814979-113.904024 7.85545018-51.06042542 39.27724947-82.4822247 82.48222469-90.33767626 7.85545018-11.78317456 27.4940749-7.85545018 54.98814979 3.92772576m-51.06042402 39.27724946H168.05490716c-23.56635051 3.92772438-39.27724947 23.56635051-43.20497524 51.06042542-3.92772438 23.56635051 7.85545018 43.20497523 23.56635051 54.98814978 0-3.92772438 3.92772438-11.78317456 3.92772577-15.71090031l-3.92772577-3.92772438c-7.85545018-15.71090032-11.78317456-35.34952507 0-51.06042543 7.85545018-15.71090032 23.56635051-23.56635051 39.27725085-19.63862471 3.92772438-3.92772438 7.85545018-7.85545018 7.85544878-15.71090037h-15.71089896z m593.08647602-7.85545014c11.78317456 0 23.56635051 7.85545018 31.4217993 19.63862611 7.85545018 11.78317456 11.78317456 31.42180067-7.85545017 54.98814979 3.92772438 3.92772438 3.92772438 7.85545018 3.92772576 11.78317456v3.92772577c19.63862475-15.71090032 27.4940749-43.20497523 19.63862475-70.69905012-7.85545018-23.56635051-23.56635051-35.34952507-51.06042542-35.34952508h-15.71089894c0 3.92772438 3.92772438 3.92772438 3.92772438 7.85544879l3.92772441 3.92772578c3.92772438 3.92772438 7.85545018 3.92772438 11.78317593 3.9277244z\" p-id=\"33735\" fill=\"#12b8b8\"></path><path d=\"M576.53830905 680.89217518c-3.92772438-15.71090032-15.71090032-31.42180067-27.4940763-39.27724946l-3.92772438-3.92772579c-7.85545018-3.92772438-7.85545018-7.85545018-7.85545018-11.78317454 0-7.85545018-3.92772438-15.71090032-7.85545015-19.63862475-15.71090032-15.71090032-39.27724947-19.63862475-58.91587421-19.63862611-19.63862475 3.92772438-35.34952507 15.71090032-43.20497525 31.42180067-7.85545018 11.78317456-15.71090032 19.63862475-23.5663505 31.42180068-11.78317456 11.78317456-19.63862475 31.42180067-19.63862472 51.06042403 3.92772438 19.63862475 11.78317456 35.34952507 27.4940749 39.27725082 3.92772438 3.92772438 7.85545018 3.92772438 11.78317456 3.9277244l-3.9277244 23.56635053s0 35.34952507 19.63862473 51.06042541c7.85545018 3.92772438 11.78317456 7.85545018 19.63862473 7.85545017s15.71090032-3.92772438 23.56635051-7.85545017c11.78317456 11.78317456 27.4940749 11.78317456 43.20497524 3.92772439 11.78317456-7.85545018 19.63862475-23.56635051 19.63862474-43.20497525v-23.56634911-3.92772578h3.92772438c11.78317456-3.92772438 23.56635051-11.78317456 27.4940763-23.5663505 3.92772438-15.71090032 3.92772438-31.42180067 0-47.13269964zM462.63428367 747.66350093v27.49407489l-3.92772579 7.85545017v-7.85545015c0-7.85545018 0-19.63862475 3.92772579-31.42180067v-3.92772441l3.92772439-3.92772577v11.78317594h-3.92772439z m43.20497523 31.4217993h-3.92772578V747.66350093v-7.85545017c3.92772438 3.92772438 3.92772438 3.92772438 3.92772578 7.85545017v31.4217993z m35.34952506-74.62677452h-7.85545016c-7.85545018 0-15.71090032 0-19.63862612-3.9277258-7.85545018-3.92772438-19.63862475-7.85545018-27.49407489-7.85545017-7.85545018 0-15.71090032 3.92772438-27.49407491 7.85545017-7.85545018 3.92772438-15.71090032 7.85545018-27.49407491 7.85545018-7.85545018 0-7.85545018 0-7.85545015-3.92772438-3.92772438-7.85545018 0-11.78317456 0-15.71090037 7.85545018-11.78317456 11.78317456-11.78317456 23.5663505-7.85545016 27.4940749 11.78317456 51.06042542 11.78317456 74.62677453-7.85545015 7.85545018 3.92772438 15.71090032 7.85545018 19.63862611 15.71090031 3.92772438 7.85545018 0 11.78317456 0 15.71090037zM600.10465816 327.39692448c-66.77132575 0-121.75947555 62.84359998-121.75947554 137.47037588 0 35.34952507 11.78317456 70.69905013 35.34952506 98.19312506 23.56635051 27.4940749 54.98814978 43.20497523 86.40995048 43.20497522 66.77132575 0 117.83174977-62.84359998 117.83175115-137.47037587 3.92772438-78.55450031-51.06042542-141.39810029-117.83175115-141.39810029z m3.92772578 227.80805075c-23.56635051 0-43.20497523-11.78317456-58.91587557-27.49407488h3.92772438c11.78317456 0 27.4940749-7.85545018 35.34952508-15.71090035 7.85545018-11.78317456 15.71090032-23.56635051 15.71090033-39.27724947s-3.92772438-27.4940749-15.71090033-39.27725084c-7.85545018-11.78317456-23.56635051-19.63862475-35.34952508-19.63862474-7.85545018 0-11.78317456 0-19.63862471 3.92772441 15.71090032-27.4940749 39.27724947-47.13269964 70.69905012-47.13269965 23.56635051 0 43.20497523 11.78317456 58.91587559 27.4940749 15.71090032 15.71090032 23.56635051 39.27724947 23.5663505 66.77132575 0 54.98814978-31.42180067 90.33767489-78.55450031 90.33767487zM348.73025827 339.18009905c-31.42180067 0-62.84359998 15.71090032-86.40995045 39.27725085-23.56635051 27.4940749-35.34952507 58.91587557-35.34952508 94.26540063 0 35.34952507 11.78317456 70.69905013 35.34952508 94.26539925 23.56635051 27.4940749 54.98814978 43.20497523 86.40995045 43.20497527 62.84359998 0 117.83174977-62.84359998 117.83174979-137.47037452 0-70.69905013-54.98814978-133.5426501-117.83174979-133.54265148z m0 227.80805073c-23.56635051 0-43.20497523-11.78317456-62.84359996-27.49407488h3.92772441c11.78317456 0 23.56635051-7.85545018 35.34952505-15.71090034 7.85545018-11.78317456 15.71090032-27.4940749 15.71090035-39.27724946 0-31.42180067-23.56635051-58.91587557-51.0604254-58.91587561-3.92772438 0-11.78317456 0-15.71090036 3.92772581 15.71090032-27.4940749 39.27724947-43.20497523 70.69905015-43.20497525 47.13269964 0 82.4822247 39.27724947 82.48222607 94.26539928 3.92772438 47.13269964-35.34952507 86.40995051-78.55450031 86.40995045z\" p-id=\"33736\" fill=\"#12b8b8\"></path></svg>', 0, 0, 0, NULL, NULL);
INSERT INTO `b_label_info` VALUES (9, NULL, '开源', NULL, '<svg t=\"1717330258259\" class=\"icon\" viewBox=\"0 0 1024 1024\" version=\"1.1\" xmlns=\"http://www.w3.org/2000/svg\" p-id=\"36048\" width=\"48\" height=\"48\"><path d=\"M692.7872 599.0912l262.0928 84.4288c-81.9712 182.784-259.6352 298.1888-445.44 291.84-238.336-8.1408-445.2352-214.9376-445.44-465.92-0.2048-240.0256 195.6864-465.5104 445.44-465.92 210.5344-0.3584 402.4832 159.3856 445.44 384l-255.4368 19.712\" fill=\"#3BAD54\" p-id=\"36049\"></path><path d=\"M693.76 601.6c-42.4448 87.8592-136.2432 135.4752-225.28 117.76-77.312-15.36-136.3968-77.1072-153.6-148.48-24.064-99.84 34.1504-216.832 143.36-245.76 96.8192-25.6512 201.3696 26.4192 240.64 122.88\" fill=\"#FFFFFF\" p-id=\"36050\"></path></svg>', 0, 1, 0, NULL, NULL);
INSERT INTO `b_label_info` VALUES (10, NULL, 'Python', NULL, '<svg t=\"1717330278224\" class=\"icon\" viewBox=\"0 0 1029 1024\" version=\"1.1\" xmlns=\"http://www.w3.org/2000/svg\" p-id=\"37974\" width=\"48\" height=\"48\"><path d=\"M610.304 7.68l38.4 8.704 31.232 11.264 25.088 12.8 19.456 13.824 14.336 14.336 10.752 14.336 6.656 14.336 4.096 12.8 1.536 11.264 1.024 8.704-0.512 5.632V363.52l-2.048 26.624-5.632 23.552-9.216 19.456-11.264 16.384-12.8 13.312-14.336 10.752-14.848 8.192-14.848 6.144-14.336 4.096-12.8 3.072-11.264 1.536-9.216 1.024H376.832l-29.696 2.048-25.088 6.144-21.504 9.216-17.408 11.776-13.824 13.824-11.264 14.848-8.704 15.36-6.144 15.872-4.096 14.848-3.072 13.824-1.536 11.264-1.024 9.216v130.56H137.728l-9.216-1.024-11.776-3.072-13.824-5.12-14.848-7.68-15.36-11.264-15.36-15.36-14.848-19.456-13.824-25.088-11.776-31.232-8.704-37.376L2.048 563.2 0 510.464l2.56-52.224 6.656-44.544 10.24-36.864 13.824-30.208 15.36-24.576 16.896-18.944 17.92-13.824 17.92-10.24 16.896-6.656 15.36-4.096 13.824-2.048 10.24-0.512h6.656l2.56 0.512h348.16v-35.328H266.24l-0.512-117.248-1.024-15.872 2.048-14.336 4.608-13.312 7.168-11.776 10.752-11.264 13.312-9.728 16.384-8.704L337.92 20.992l21.504-6.144 24.576-5.12 27.648-4.608 30.208-2.56 32.768-1.536 35.84-1.024 54.272 2.048 45.568 5.632zM342.016 92.16l-9.728 14.336-3.584 17.408 3.584 17.408 9.728 14.336 14.336 9.216 17.408 4.096 17.408-4.096 14.336-9.216 9.728-14.336 3.584-17.408-4.096-17.92-9.728-13.824-14.336-9.216-17.408-4.096-17.408 4.096-13.824 9.216z\" fill=\"#0075AA\" p-id=\"37975\"></path><path d=\"M900.096 260.608l11.776 2.56 13.824 5.12 14.848 7.68 15.36 11.264 15.36 14.848 14.848 19.968 13.824 25.088 11.776 31.232 9.216 37.376 6.144 44.544 2.048 52.224-2.56 52.224-6.656 44.544-10.24 36.864-13.824 30.208-15.36 24.576-16.896 19.456-17.92 14.336-17.92 10.24-16.896 6.656-15.36 4.096-13.824 2.048-10.24 1.024-6.656-0.512h-350.72v34.816H762.88l0.512 117.76 1.024 15.36-2.048 14.336-4.608 13.312-7.168 12.288-10.752 10.752-13.312 10.24-16.384 8.704-18.944 7.168-21.504 6.144-24.576 5.632-27.136 4.096-30.208 3.072-32.768 1.536-35.84 0.512-54.272-1.536-45.568-6.144-38.4-8.704-31.232-10.752-25.088-12.8-19.456-14.336-14.336-14.336-10.752-14.336-6.656-14.336-4.096-12.8-1.536-10.752-1.024-8.704 0.512-5.632v-227.84l2.048-27.136 5.632-23.04 9.216-19.456 11.264-16.384 12.8-13.824 14.336-10.24 14.848-8.704 14.848-6.144 13.824-4.096 12.8-2.56 11.264-1.536 9.216-1.024 5.632-0.512h249.344l29.696-2.048 25.088-6.144 21.504-8.704 17.408-11.776 14.336-13.824 11.264-14.848 8.704-15.36 6.144-15.36 4.096-14.848 3.072-13.824 1.536-11.776 1.024-9.216V259.072h89.088l6.144 0.512 6.656 1.024z m-275.968 608.256l-9.728 14.336-3.584 17.408 3.584 17.408 9.728 14.336 14.336 9.728 17.408 3.584 17.408-3.584 14.336-9.728 9.728-14.336 3.584-17.408-3.584-17.408-9.728-14.336-14.336-9.728-17.408-3.584-17.408 3.584-14.336 9.728z\" fill=\"#FFD400\" p-id=\"37976\"></path></svg>', 0, 0, 0, NULL, NULL);
INSERT INTO `b_label_info` VALUES (11, NULL, '开发工具', NULL, '<svg t=\"1717330292711\" class=\"icon\" viewBox=\"0 0 1154 1024\" version=\"1.1\" xmlns=\"http://www.w3.org/2000/svg\" p-id=\"40286\" width=\"48\" height=\"48\"><path d=\"M375.619731 988.04167a62.238289 62.238289 0 0 1-17.378682-45.848638 51.429596 51.429596 0 0 1 1.13032-11.585788c12.504174-52.913142 37.936392-149.626216 68.949569-243.230907q17.096102-52.136047 81.595033-308.153709a709.205657 709.205657 0 0 0 28.25802-205.577095c-12.786754-44.930252-72.057951-34.68672-107.097896-29.035116s-131.399793 22.959641-166.157157 22.959642c-12.998689 0-11.091273-12.645464-7.770956-17.378683A309.566609 309.566609 0 0 1 428.320938 32.920593a773.563298 773.563298 0 0 1 504.546947 20.063195l7.064505-17.166748 92.333081 39.208003-81.029873 190.741635-92.121145-39.137358 7.064505-17.449327q-131.117213-58.140876-170.819731 19.356744a1225.832908 1225.832908 0 0 0-45.071542 194.203242q-48.391859 279.542463-79.405036 427.331908a1152.291411 1152.291411 0 0 1-37.724457 150.332666c-4.803863 12.362884-19.144809 23.030286-48.250569 23.030287a217.162884 217.162884 0 0 1-109.287892-35.39317z\" p-id=\"40287\" fill=\"#f4ea2a\"></path><path d=\"M0 586.14198V490.135357l210.098379-152.663953 58.917971 81.100517-162.483615 118.259814 169.54812 114.444981-56.092169 83.149224-219.918041-148.354605z m1153.492377-100.386616v96.006623l-210.098379 152.663953-58.988617-81.100517 162.483615-118.259814-169.54812-114.444981 56.51604-83.149224 219.847396 148.354605z\" p-id=\"40288\" fill=\"#f4ea2a\"></path></svg>', 0, 2, 0, NULL, NULL);
INSERT INTO `b_label_info` VALUES (12, NULL, '大数据', NULL, '<svg t=\"1717330327170\" class=\"icon\" viewBox=\"0 0 1024 1024\" version=\"1.1\" xmlns=\"http://www.w3.org/2000/svg\" p-id=\"42663\" width=\"48\" height=\"48\"><path d=\"M512 64.256a106.666667 106.666667 0 0 1 42.666667 204.458667v69.312l86.762666 50.090666 60.032-34.645333a106.688 106.688 0 1 1 42.666667 73.898667l-60.16 34.730666v100.053334l60.16 34.730666a106.688 106.688 0 1 1-42.666667 73.92l-60.266666-34.816L554.666667 685.952v69.589333a106.688 106.688 0 1 1-85.333334 0v-69.589333l-86.549333-49.962667-60.245333 34.773334a106.688 106.688 0 1 1-42.666667-73.877334l60.117333-34.773333v-100.010667l-60.138666-34.730666a106.688 106.688 0 1 1 42.666666-73.898667l60.032 34.645333L469.333333 338.026667v-69.312a106.688 106.688 0 0 1 42.666667-204.458667zM512 810.666667a42.666667 42.666667 0 1 0 0 85.333333 42.666667 42.666667 0 0 0 0-85.333333zM195.178667 645.76a42.666667 42.666667 0 1 0 42.666666 73.92 42.666667 42.666667 0 0 0-42.666666-73.898667z m575.36 15.637333a42.666667 42.666667 0 1 0 73.898666 42.666667 42.666667 42.666667 0 0 0-73.898666-42.666667zM512 411.925333l-86.677333 50.026667v100.074667L512 612.074667l86.656-50.048v-100.074667L512 411.925333z m274.154667-107.349333a42.666667 42.666667 0 1 0 42.666666 73.898667 42.666667 42.666667 0 0 0-42.666666-73.898667z m-606.592 15.616a42.666667 42.666667 0 1 0 73.898666 42.666667 42.666667 42.666667 0 0 0-73.898666-42.666667zM512 128.256a42.666667 42.666667 0 1 0 0 85.333333 42.666667 42.666667 0 0 0 0-85.333333z\" fill=\"#1296db\" p-id=\"42664\"></path></svg>', 0, 1, 0, NULL, NULL);
INSERT INTO `b_label_info` VALUES (13, NULL, 'Android', NULL, '<svg t=\"1717330344538\" class=\"icon\" viewBox=\"0 0 1024 1024\" version=\"1.1\" xmlns=\"http://www.w3.org/2000/svg\" p-id=\"44719\" width=\"48\" height=\"48\"><path d=\"M157.947546 735.493119c-45.735653 0-82.696442-37.226694-82.696442-82.696443v-253.141521c0-45.469748 36.960789-82.696442 82.696442-82.696443 22.070112 0 42.810699 8.508959 58.499091 24.197351 15.688393 15.688393 24.197351 36.428979 24.197352 58.499092v253.141521c0 22.070112-8.508959 42.810699-24.197352 58.499091-15.688393 15.422488-36.428979 24.197351-58.499091 24.197352\" fill=\"#FFFFFF\" p-id=\"44720\"></path><path d=\"M157.947546 339.028824c-33.504025 0-60.360426 27.122306-60.360426 60.360426v253.141521c0 33.504025 27.122306 60.360426 60.360426 60.626331 33.504025 0 60.360426-27.122306 60.360426-60.626331v-253.141521c0-33.23812-27.122306-60.360426-60.360426-60.360426M806.489743 351.526357H217.244352v-22.336017c0-91.205401 49.724227-176.029083 131.091145-226.285121L316.692807 45.469748c-3.988574-6.647624-4.786289-15.422488-2.65905-23.133731 2.393145-7.711244 7.711244-14.624773 14.890678-18.347443C333.44482 1.329525 338.497014 0 343.549208 0c11.168008 0 21.538302 6.115814 26.856401 15.954298l33.23812 60.360425c34.30174-12.497533 70.464814-18.879252 107.957414-18.879252 38.024409 0 74.453389 6.381719 108.755128 19.145157L653.328486 15.954298c5.318099-9.838484 15.688393-15.954298 26.856401-15.954298 5.052194 0 10.104388 1.329525 14.624773 3.722669 7.445339 3.988574 12.763438 10.636198 14.890678 18.613348 2.393145 7.977149 1.329525 16.220203-2.65905 23.399636l-31.64269 57.701376C756.765515 153.693067 806.489743 238.516749 806.489743 329.72215v21.804207z\" fill=\"#FFFFFF\" p-id=\"44721\"></path><path d=\"M645.351337 111.680083l42.278889-77.112438c2.12724-3.988574 0.797715-9.306674-3.19086-11.433914-4.254479-2.12724-9.306674-0.797715-11.433913 3.456765l-42.544793 77.910153c-35.89717-15.954298-76.048818-24.995066-118.327708-24.729161-42.278889 0-82.430538 8.774864-118.061802 24.729161l-42.544793-77.644248c-2.12724-3.988574-7.445339-5.584004-11.433913-3.456765s-5.584004 7.445339-3.456765 11.433914l42.278889 77.112438C295.95222 154.756687 239.846274 236.123604 239.846274 329.72215h544.573357c-0.265905-93.864451-56.105947-175.231368-139.068294-218.042067m-257.396001 119.125422c-12.497533 0-22.867827-10.104388-22.867826-22.867826 0-12.497533 10.104388-22.867827 22.867826-22.867827 12.497533 0 22.867827 10.370293 22.867827 22.867827 0 12.497533-10.104388 22.867827-22.867827 22.867826m248.089328 0c-12.497533 0-22.867827-10.104388-22.867827-22.867826 0-12.497533 10.104388-22.867827 22.867827-22.867827 12.497533 0 22.867827 10.370293 22.867826 22.867827 0 12.497533-10.370293 22.867827-22.867826 22.867826\" fill=\"#FFFFFF\" p-id=\"44722\"></path><path d=\"M410.823163 1024c-45.469748 0-82.696442-36.960789-82.696443-82.696442v-111.680084h-21.804206c-23.133731 0-44.937938-9.040769-61.424046-25.260971-16.486108-16.486108-25.526876-38.024409-25.260971-61.424045V328.392625H804.894313v414.545833c0 47.862893-38.822124 86.685017-86.685017 86.685016h-21.804206v111.680084c0 45.469748-36.960789 82.696442-82.696443 82.696442-22.070112 0-42.810699-8.508959-58.499091-24.197351-15.688393-15.688393-24.197351-36.428979-24.197351-58.499091v-111.680084h-37.226695v111.680084c-0.265905 45.469748-37.492599 82.696442-82.962347 82.696442\" fill=\"#FFFFFF\" p-id=\"44723\"></path><path d=\"M241.973513 742.672553c0 35.631265 28.717736 64.614905 64.614906 64.614905h43.874318V941.303558c0 33.23812 27.122306 60.360426 60.360426 60.360425 33.504025 0 60.360426-27.122306 60.360426-60.62633v-134.0161h81.632822v134.0161c0 33.23812 27.122306 60.360426 60.360426 60.360425 33.504025 0 60.360426-27.122306 60.360426-60.360425v-134.0161h44.140223c35.631265 0 64.614905-28.983641 64.614906-64.614905V349.930927H241.707608l0.265905 392.741626zM866.052454 735.227214c-45.469748 0-82.696442-36.960789-82.696443-82.696443v-253.141521c0-45.735653 36.960789-82.696442 82.696443-82.696443 45.735653 0 82.696442 36.960789 82.696442 82.696443v253.141521c0 45.735653-36.960789 82.696442-82.696442 82.696443\" fill=\"#FFFFFF\" p-id=\"44724\"></path><path d=\"M866.052454 339.028824c-33.504025 0-60.360426 27.122306-60.360426 60.360426v253.141521c0 33.504025 27.122306 60.360426 60.360426 60.360426 33.504025 0 60.360426-27.122306 60.360426-60.360426v-253.141521c0-33.23812-26.856401-60.360426-60.360426-60.360426\" fill=\"#FFFFFF\" p-id=\"44725\"></path><path d=\"M157.947546 339.028824c-33.504025 0-60.360426 27.122306-60.360426 60.360426v253.141521c0 33.504025 27.122306 60.360426 60.360426 60.360426 33.504025 0 60.360426-27.122306 60.360426-60.360426v-253.141521c0-33.23812-27.122306-60.360426-60.360426-60.360426M645.351337 111.680083l42.278889-77.112438c2.12724-3.988574 0.797715-9.040769-3.19086-11.433914-3.988574-2.12724-9.306674-0.797715-11.433913 3.456765l-42.544793 77.910153c-35.89717-15.954298-76.048818-24.995066-118.593612-24.995066-42.278889 0-82.430538 8.774864-118.061803 24.729161l-42.544793-77.644248c-2.12724-3.988574-7.445339-5.584004-11.433913-3.456765s-5.584004 7.445339-3.456765 11.433914l42.278889 77.112438c-82.962347 42.810699-139.068294 124.177616-139.068294 217.776162h544.573357c0-93.598546-55.840042-174.965464-138.802389-217.776162m-257.396001 119.125422c-12.497533 0-22.867827-10.104388-22.867826-22.867826 0-12.497533 10.104388-22.867827 22.867826-22.867827 12.497533 0 22.867827 10.370293 22.867827 22.867827 0 12.497533-10.104388 22.867827-22.867827 22.867826m248.089328 0c-12.497533 0-22.867827-10.104388-22.867827-22.867826 0-12.497533 10.104388-22.867827 22.867827-22.867827 12.497533 0 22.867827 10.370293 22.867826 22.867827 0 12.497533-10.370293 22.867827-22.867826 22.867826m-394.337056 119.657232v392.475721c0 35.631265 28.717736 64.614905 64.614906 64.614905h43.874318v134.016099c0 33.23812 27.122306 60.360426 60.360426 60.360426 33.504025 0 60.360426-27.122306 60.360426-60.62633v-134.0161h81.632822V941.303558c0 33.23812 27.122306 60.360426 60.360426 60.360425 33.504025 0 60.360426-27.122306 60.360426-60.360425v-134.0161h44.140223c35.631265 0 64.614905-28.717736 64.614906-64.614905V350.196832l-540.318879 0.265905z m684.971177 48.926513c0-33.504025-27.122306-60.360426-60.360426-60.360426-33.504025 0-60.360426 27.122306-60.360426 60.360426v253.141521c0 33.504025 27.122306 60.360426 60.360426 60.360426 33.504025 0 60.360426-27.122306 60.360426-60.360426v-253.141521z\" fill=\"#A4C439\" p-id=\"44726\"></path></svg>', 0, 0, 0, NULL, NULL);
INSERT INTO `b_label_info` VALUES (14, NULL, 'ios', NULL, '<svg t=\"1717330368297\" class=\"icon\" viewBox=\"0 0 1024 1024\" version=\"1.1\" xmlns=\"http://www.w3.org/2000/svg\" p-id=\"46659\" width=\"48\" height=\"48\"><path d=\"M313.173333 959.146667h397.44c137.173333 0 248.533333-111.146667 248.533334-248.533334V313.173333c0-137.173333-111.146667-248.533333-248.533334-248.533333H313.173333c-137.173333 0-248.533333 111.146667-248.533333 248.533333v397.44c0.213333 137.386667 111.36 248.533333 248.533333 248.533334z\" fill=\"#ECEFF1\" p-id=\"46660\"></path><path d=\"M233.386667 362.24c-10.453333 0-18.986667 8.533333-18.986667 18.986667s8.533333 18.986667 18.986667 18.986666 18.986667-8.533333 18.986666-18.986666-8.533333-18.986667-18.986666-18.986667z\" fill=\"#FFC107\" p-id=\"46661\"></path><path d=\"M312.746667 457.6l35.84-15.146667c16.853333-30.293333 45.653333-47.786667 82.773333-47.786666 8.533333 0 16.426667 1.066667 23.893333 2.773333l34.133334-14.293333c-16.853333-8.32-36.053333-13.013333-58.026667-13.013334-59.306667-0.213333-101.546667 32.426667-118.613333 87.466667z m-66.56 28.16v-39.04h-25.386667v49.706667l25.386667-10.666667z\" fill=\"#FF5722\" p-id=\"46662\"></path><path d=\"M246.186667 555.093333v-69.333333l-25.386667 10.666667v69.333333l25.386667-10.666667z m85.546666-39.466666c0-29.013333 6.186667-53.76 17.066667-73.173334l-35.84 15.146667c-5.333333 17.28-8.32 36.693333-8.32 58.026667 0 4.906667 0.64 9.386667 1.066667 14.293333l26.453333-11.093333c-0.213333-1.066667-0.426667-2.133333-0.426667-3.2z m339.84-140.373334c-6.826667 2.133333-13.44 4.906667-19.2 8.106667l19.2-8.106667z m-182.186666 7.68l-34.133334 14.293334c25.6 5.973333 45.44 21.546667 58.453334 44.586666l25.386666-10.666666c-11.52-21.546667-28.586667-37.76-49.706666-48.213334z\" fill=\"#F44336\" p-id=\"46663\"></path><path d=\"M246.186667 624.426667v-69.333334l-25.386667 10.666667v69.333333l25.386667-10.666666z m425.386666-249.173334l-19.2 8.106667c-24.32 13.653333-39.253333 36.266667-39.253333 64 0 7.466667 1.066667 14.08 2.986667 20.48l25.6-10.88c-0.853333-3.2-1.493333-6.613333-1.493334-10.453333 0-30.506667 27.733333-52.693333 67.84-52.693334 19.84 0 36.48 5.333333 48.64 14.293334l26.026667-11.093334c-17.28-17.066667-43.093333-27.733333-73.813333-27.733333-13.44 0.64-26.026667 2.56-37.333334 5.973333z m-339.626666 143.573334l-26.453334 11.093333c1.493333 23.893333 6.4 45.013333 14.933334 62.933333l24.96-10.453333c-8.533333-17.706667-13.013333-39.04-13.44-63.573333z m207.146666-87.466667l-25.386666 10.666667c9.386667 16.853333 14.933333 37.973333 16.426666 62.293333l26.453334-11.093333c-2.56-23.466667-8.106667-44.586667-17.493334-61.866667z\" fill=\"#E91E63\" p-id=\"46664\"></path><path d=\"M551.893333 564.266667c3.626667-14.933333 5.973333-30.933333 5.973334-48.64 0-7.893333-0.853333-15.146667-1.493334-22.4l-26.453333 11.093333c0.213333 3.84 0.64 7.253333 0.64 11.306667 0 24.106667-4.48 45.013333-11.946667 62.72l33.28-14.08z m-206.506666 18.133333l-24.96 10.453333c10.24 21.76 24.96 39.04 44.16 50.56l31.36-13.226666c-22.613333-8.106667-39.893333-24.746667-50.56-47.786667z m-99.2 42.026667l-25.386667 10.666666v20.693334h25.386667v-31.36z m395.52-167.253334l-25.6 10.88c6.186667 21.546667 22.826667 36.48 52.906666 46.933334l39.68-16.853334-8.32-2.133333c-36.266667-8.96-53.973333-20.693333-58.666666-38.826667z m136.533333-13.013333h26.88c-1.28-18.133333-9.386667-34.133333-22.186667-46.72l-26.026666 11.093333c11.733333 8.746667 19.413333 21.12 21.333333 35.626667z\" fill=\"#9C27B0\" p-id=\"46665\"></path><path d=\"M551.893333 564.266667l-33.066666 14.08c-15.573333 36.693333-45.866667 58.666667-87.466667 58.666666-13.013333 0-24.96-2.56-35.626667-6.613333l-31.36 13.226667c18.773333 11.306667 40.96 18.133333 66.986667 18.133333 63.146667-0.213333 106.026667-36.906667 120.533333-97.493333z m80 18.773333h-26.88c0.64 9.173333 3.2 17.493333 6.613334 25.386667l24.533333-10.453334c-1.92-4.693333-3.626667-9.6-4.266667-14.933333z m96.213334-80l-19.413334-4.906667-39.68 16.853334c5.76 1.92 12.16 3.84 18.986667 5.546666l32 8.106667c17.706667 4.266667 31.146667 9.813333 41.173333 16.64l29.866667-12.586667c-13.226667-12.8-33.706667-22.4-62.933333-29.653333z\" fill=\"#3F51B5\" p-id=\"46666\"></path><path d=\"M636.373333 597.973333l-24.533333 10.453334C621.013333 629.973333 640 646.4 665.6 654.933333l42.026667-17.706666c-34.346667-0.853333-61.013333-16-71.253334-39.253334z m171.52-3.2c0.853333-4.693333 1.493333-9.6 1.493334-14.933333 0-19.413333-5.76-34.986667-18.56-47.36l-29.866667 12.586667c14.293333 9.6 21.333333 21.973333 21.333333 37.546666 0 9.813333-3.2 18.56-8.746666 26.453334l34.346666-14.293334z\" fill=\"#03A9F4\" p-id=\"46667\"></path><path d=\"M807.893333 594.773333l-34.346666 14.506667c-11.733333 16.853333-34.56 28.16-63.36 28.16-1.066667 0-1.92-0.213333-2.773334-0.213333L665.6 654.933333c12.8 4.266667 26.88 6.613333 42.453333 6.613334 54.826667 0 92.586667-26.24 99.84-66.773334z\" fill=\"#009688\" p-id=\"46668\"></path></svg>', 0, 0, 0, NULL, NULL);
INSERT INTO `b_label_info` VALUES (15, NULL, '运维', NULL, '<svg t=\"1717330387076\" class=\"icon\" viewBox=\"0 0 1077 1024\" version=\"1.1\" xmlns=\"http://www.w3.org/2000/svg\" p-id=\"48689\" width=\"48\" height=\"48\"><path d=\"M893.44 811.008H184.32c-50.688 0-90.112-39.936-90.112-91.648V191.488c0-51.712 39.424-91.648 90.112-91.648h709.12c50.688 0 90.112 39.936 90.112 91.648v527.872c0 46.08-39.424 91.648-90.112 91.648z\" fill=\"#F33C3C\" p-id=\"48690\"></path><path d=\"M744.448 413.184l-44.544-5.632c-5.632-11.264-5.632-22.528-11.264-33.792l27.648-33.792c5.632-5.632 5.632-22.528-5.632-28.16l-27.648-28.16c-5.632-5.632-22.016-11.264-27.648-5.632l-33.28 28.16-33.28-16.896-5.632-39.424c0-11.264-11.264-16.896-22.016-16.896H522.24c-11.264 0-22.016 5.632-22.016 16.896l-5.632 39.424-33.28 16.896-33.28-28.16c-11.264-5.632-22.016-5.632-27.648 5.632l-27.648 28.16c-11.264 5.632-11.264 22.528-5.632 28.16L389.12 373.76c-5.632 11.264-5.632 22.528-11.264 33.792l-38.912 5.632c-11.264 0-22.016 11.264-22.016 22.528v39.424c0 11.264 5.632 22.528 16.896 22.528l38.912 5.632c11.264 11.264 16.896 22.528 22.016 33.792l-27.648 33.792c-5.632 5.632-5.632 22.528 5.632 28.16l27.648 28.16c5.632 11.264 16.896 11.264 27.648 5.632l33.28-28.16c11.264 5.632 22.016 11.264 33.28 11.264l5.632 45.056c0 11.264 11.264 16.896 22.016 16.896h38.912c11.264 0 22.016-5.632 22.016-16.896l5.632-45.056c11.264-5.632 22.016-5.632 33.28-11.264l33.28 28.16c5.632 5.632 22.016 5.632 27.648-5.632l27.648-28.16c5.632-5.632 11.264-22.528 5.632-28.16l-27.648-33.792c5.632-11.264 11.264-22.528 11.264-33.792l44.544-5.632c11.264 0 16.896-11.264 16.896-22.528v-39.424c0-11.264-5.632-22.528-16.896-22.528z\" fill=\"#FFFFFF\" p-id=\"48691\"></path><path d=\"M539.136 544.256c-50.176 0-89.088-37.888-89.088-86.016 0-48.64 38.912-91.648 89.088-91.648s89.088 37.888 89.088 86.016c-0.512 48.64-39.424 91.648-89.088 91.648zM900.096 870.912H183.808c-22.528 0-44.544 22.016-44.544 44.544 0 22.016 16.896 44.544 44.544 44.544h710.656c22.528 0 44.544-22.016 44.544-44.544 0-22.016-16.896-44.544-38.912-44.544z\" fill=\"#F33C3C\" p-id=\"48692\"></path></svg>', 1, 2, 0, NULL, NULL);
INSERT INTO `b_label_info` VALUES (16, NULL, '软件工程', NULL, '<svg t=\"1717330419236\" class=\"icon\" viewBox=\"0 0 1024 1024\" version=\"1.1\" xmlns=\"http://www.w3.org/2000/svg\" p-id=\"51447\" width=\"48\" height=\"48\"><path d=\"M443.487 432.065l3.606-11.418H240.375V337.12a36.055 36.055 0 0 1 36.055-36.657h147.828a36.055 36.055 0 0 1 36.055 36.657v51.078a152.034 152.034 0 0 1 60.092-60.092 96.148 96.148 0 0 0-96.147-87.735H276.43a96.148 96.148 0 0 0-96.148 96.749v356.949h139.415a158.644 158.644 0 0 1-12.019-32.45 161.048 161.048 0 0 1-7.21-27.643h-60.093V495.162h103.359a147.827 147.827 0 0 1 64.299-46.872 188.69 188.69 0 0 1 35.454-10.817z\" fill=\"#1296DB\" p-id=\"51448\"></path><path d=\"M64.304 901.387V84.129A24.037 24.037 0 0 1 88.34 60.092h560.662A24.037 24.037 0 0 1 673.04 84.13v224.145a150.832 150.832 0 0 1 39.06 19.83l17.427-22.834a24.037 24.037 0 0 1 24.037-22.835H867.74a24.037 24.037 0 0 1 24.037 24.036v13.822a173.667 173.667 0 0 1 57.088 39.66v-52.28a87.134 87.134 0 0 0-24.638-60.092 84.13 84.13 0 0 0-60.093-25.239H729.527V86.533a87.134 87.134 0 0 0-24.638-60.092A84.13 84.13 0 0 0 644.797 1.2H86.537A86.533 86.533 0 0 0 0.006 86.534v837.689a36.656 36.656 0 0 0 36.656 37.257H360.56a180.277 180.277 0 0 1-42.666-60.092z\" fill=\"#1296DB\" p-id=\"51449\"></path><path d=\"M948.264 728.32c-7.812-69.707 60.092-60.092 73.312-106.964-7.812-30.647-15.023-54.083-29.445-60.093s-60.092 22.836-96.148 0-15.624-80.523-30.046-109.969a82.327 82.327 0 0 0-73.313-30.647c-29.445 39.06-22.234 60.093-81.125 60.093s-44.468-60.093-88.936-60.093c-22.235 0-51.68 7.812-60.093 30.647s7.812 69.708-14.422 92.543c-37.257 30.046-60.092 0-96.148 15.023a64.299 64.299 0 0 0-31.248 70.308c7.812 30.647 60.092 39.06 60.092 84.73 0 60.093-51.68 60.093-60.092 84.73a75.716 75.716 0 0 0 22.234 76.919c15.023 15.023 60.093-15.624 96.148 15.023s15.023 54.083 7.812 84.73 54.083 46.272 83.529 46.272 30.647-60.093 90.74-60.093c44.468 0 44.468 30.647 66.702 54.083s60.092-7.812 81.125-22.835-14.423-46.27 0-84.73a76.317 76.317 0 0 1 37.257-30.647 228.952 228.952 0 0 1 73.313 0c22.234-7.211 29.445-60.093 29.445-76.919s-53.482-19.23-60.693-72.11zM742.748 841.295c-146.626 22.835-201.911-199.507-51.08-240.37a120.185 120.185 0 1 1 51.08 240.37z\" fill=\"#1296DB\" p-id=\"51450\"></path></svg>', 1, 1, 0, NULL, NULL);
INSERT INTO `b_label_info` VALUES (17, NULL, '程序人生', NULL, '<svg t=\"1717330456769\" class=\"icon\" viewBox=\"0 0 1024 1024\" version=\"1.1\" xmlns=\"http://www.w3.org/2000/svg\" p-id=\"59560\" width=\"48\" height=\"48\"><path d=\"M824.608 941.344l7.2-248.928-48.672-100.288a160.032 160.032 0 0 0-99.584-83.872l-96.992-28-8.672-79.008h-131.744l-8.704 79.072-98.016 28.256a159.936 159.936 0 0 0-98.784 82.176l-117.12 234.272c-27.296 54.592 12.512 118.752 73.536 118.592l627.552-2.272z\" fill=\"#80D8FF\" p-id=\"59561\"></path><path d=\"M720 944c0-248.064-20-385.824-193.888-446.752l-88.672-16.928-98.016 28.256a159.936 159.936 0 0 0-98.784 82.176l-117.12 234.272c-27.296 54.592 12.512 118.752 73.536 118.592L720 944z\" fill=\"#A9E4FF\" p-id=\"59562\"></path><path d=\"M251.424 943.584c-86.112 0-100.832-64-73.536-118.592L295.04 590.72a159.936 159.936 0 0 1 98.784-82.176l76.352-22.016-32.736-6.208-98.016 28.256a159.936 159.936 0 0 0-98.784 82.176l-117.12 234.272c-27.296 54.592 12.512 118.752 73.536 118.592L720 944v-0.032l-468.576-0.384z\" fill=\"#FFFFFF\" p-id=\"59563\"></path><path d=\"M436.48 488l9.632-86.752h131.776l9.632 86.752-75.52 23.36z\" fill=\"#E6CAA0\" p-id=\"59564\"></path><path d=\"M595.872 142.56c-19.424-19.456-47.648-29.312-83.872-29.312s-64.448 9.856-83.872 29.312c-32 32.032-34.624 66.816-34.592 99.648 0.32 168.32 60.032 191.04 118.432 191.04s118.112-22.688 118.432-191.04c0.096-32.832-2.528-67.584-34.528-99.648z\" fill=\"#E6CAA0\" p-id=\"59565\"></path><path d=\"M511.2 147.264a104.544 104.544 0 0 0-21.952-16.416 88.864 88.864 0 0 0-18.144-12.032c-17.088 4.768-31.68 12.448-42.976 23.744-32 32.032-34.624 66.816-34.592 99.648 0.288 155.744 51.456 186.752 105.344 190.528 0.064-0.48 0.288-0.832 0.384-1.312 33.728-35.36 50.976-82.4 51.168-173.184 0.064-37.568-3.936-75.616-39.232-110.976z\" fill=\"#FFE0B2\" p-id=\"59566\"></path><path d=\"M831.776 703.488a16 16 0 0 1-14.336-8.832l-48.384-96.768a144.384 144.384 0 0 0-88.864-73.952l-98.048-28.288a16 16 0 0 1-11.456-13.664l-7.072-66.272a16 16 0 1 1 31.808-3.392l5.952 55.584 87.744 25.312a176.544 176.544 0 0 1 108.64 90.4l48.384 96.768a16 16 0 0 1-14.368 23.104zM464 960H196.608a97.344 97.344 0 0 1-83.328-46.464 97.28 97.28 0 0 1-4.288-95.296l117.344-234.656a176.544 176.544 0 0 1 108.672-90.4l87.68-25.28 5.984-55.84a16 16 0 0 1 31.808 3.392l-7.136 66.528a16 16 0 0 1-11.456 13.664l-98.016 28.256a144.48 144.48 0 0 0-88.928 73.984L137.6 832.576a65.536 65.536 0 0 0 2.88 64.16A65.6 65.6 0 0 0 196.608 928H464a16 16 0 0 1 0 32z\" fill=\"#455A64\" p-id=\"59567\"></path><path d=\"M512 449.248a16 16 0 0 1 0-32c35.68 0 102.112 0 102.432-175.072 0.032-30.72-2.208-60.608-29.92-88.32-16.288-16.32-40.672-24.608-72.512-24.608-31.84 0-56.224 8.288-72.544 24.608-27.712 27.712-29.952 57.6-29.92 88.352 0.16 82.368 15.136 135.84 44.48 158.912a16 16 0 1 1-19.776 25.152c-37.984-29.856-56.544-90.016-56.704-184.032-0.032-34.656 2.944-74.656 39.264-111.008 22.528-22.56 54.56-33.984 95.2-33.984s72.672 11.424 95.2 34.016c36.32 36.352 39.296 76.352 39.264 110.976-0.288 143.168-41.76 207.008-134.464 207.008zM400 864h-160a16 16 0 0 1-14.048-23.68l96-176a16 16 0 1 1 28.096 15.296L266.944 832H400a16 16 0 0 1 0 32z\" fill=\"#455A64\" p-id=\"59568\"></path><path d=\"M869.824 960H443.936L550.4 640h425.888z\" fill=\"#78909C\" p-id=\"59569\"></path><path d=\"M869.824 960H443.936a15.968 15.968 0 0 1-15.2-21.056l106.496-320A16 16 0 0 1 550.4 608h425.888a15.968 15.968 0 0 1 15.2 21.056l-106.496 320a16 16 0 0 1-15.168 10.944z m-403.712-32h392.16l95.84-288H561.952l-95.84 288z\" fill=\"#455A64\" p-id=\"59570\"></path><path d=\"M720 752m-16 0a16 16 0 1 0 32 0 16 16 0 1 0-32 0Z\" fill=\"#455A64\" p-id=\"59571\"></path><path d=\"M976 960h-928a16 16 0 0 1 0-32h928a16 16 0 0 1 0 32z\" fill=\"#455A64\" p-id=\"59572\"></path></svg>', 0, 0, 0, NULL, NULL);
INSERT INTO `b_label_info` VALUES (19, NULL, '面试', NULL, '<svg t=\"1717330477348\" class=\"icon\" viewBox=\"0 0 1024 1024\" version=\"1.1\" xmlns=\"http://www.w3.org/2000/svg\" p-id=\"61815\" width=\"48\" height=\"48\"><path d=\"M896.469333 385.450667l-355.2 295.082666a53.333333 53.333333 0 0 1-64.768 0L121.557333 385.706667c2.133333-0.170667 4.266667-0.256 6.442667-0.256V128a85.333333 85.333333 0 0 1 85.333333-85.333333h597.333334a85.333333 85.333333 0 0 1 85.333333 85.333333v257.450667h0.469333z m64.256 29.738666c12.842667 14.933333 20.608 34.346667 20.608 55.594667v426.666667a85.333333 85.333333 0 0 1-85.333333 85.333333H128a85.333333 85.333333 0 0 1-85.333333-85.333333v-426.666667c0-19.797333 6.741333-38.016 18.048-52.48l376.917333 313.088a117.333333 117.333333 0 0 0 142.506667 0l380.586666-316.16zM341.333333 437.333333h150.997334a32 32 0 0 0 0-64H341.333333a32 32 0 0 0 0 64z m0-170.666666h347.818667a32 32 0 1 0 0-64H341.333333a32 32 0 0 0 0 64z\" fill=\"#7452BF\" p-id=\"61816\"></path></svg>', 0, 0, 0, NULL, NULL);
INSERT INTO `b_label_info` VALUES (20, NULL, '架构', NULL, '<svg t=\"1717330492047\" class=\"icon\" viewBox=\"0 0 1024 1024\" version=\"1.1\" xmlns=\"http://www.w3.org/2000/svg\" p-id=\"62915\" width=\"48\" height=\"48\"><path d=\"M528.384 729.088c-60.928 0-110.08 49.152-110.08 110.08s49.152 110.08 110.08 110.08 110.08-49.152 110.08-110.08c0.512-60.928-49.152-110.08-110.08-110.08z m0 165.376c-30.72 0-55.296-24.576-55.296-55.296 0-30.72 24.576-55.296 55.296-55.296 30.72 0 55.296 24.576 55.296 55.296 0 30.72-24.576 55.296-55.296 55.296z m358.912-165.376c-60.928 0-110.08 49.152-110.08 110.08s49.152 110.08 110.08 110.08 110.08-49.152 110.08-110.08-49.152-110.08-110.08-110.08z m0 165.376c-30.72 0-55.296-24.576-55.296-55.296 0-30.72 24.576-55.296 55.296-55.296 30.72 0 55.296 24.576 55.296 55.296-0.512 30.72-25.088 55.296-55.296 55.296zM169.984 729.088c-60.928 0-110.08 49.152-110.08 110.08s49.152 110.08 110.08 110.08 110.08-49.152 110.08-110.08-49.152-110.08-110.08-110.08z m0 165.376c-30.72 0-55.296-24.576-55.296-55.296 0-30.72 24.576-55.296 55.296-55.296S225.28 808.96 225.28 839.168c0 30.72-25.088 55.296-55.296 55.296z m689.664-468.992h-303.616V344.064c74.752-15.36 122.88-88.064 107.52-162.816-15.36-74.752-88.064-122.88-162.816-107.52-74.752 15.36-122.88 88.064-107.52 162.816 11.264 54.272 53.248 96.256 107.52 107.52v81.408H197.632c-30.72 0-55.296 24.576-55.296 55.296v165.376c0 15.36 12.288 27.648 27.648 27.648s27.648-12.288 27.648-27.648V508.416c0-15.36 12.288-27.648 27.648-27.648h606.72c15.36 0 27.648 12.288 27.648 27.648v137.728c0 15.36 12.288 27.648 27.648 27.648s27.648-12.288 27.648-27.648V480.768c0-30.72-25.088-55.296-55.296-55.296zM445.952 208.896c0-45.568 36.864-82.944 82.944-82.944s82.944 36.864 82.944 82.944c0 45.568-36.864 82.944-82.944 82.944s-82.944-36.864-82.944-82.944z\" fill=\"#0972E7\" p-id=\"62916\"></path><path d=\"M280.064 513.536h490.496c30.208 0 54.272 24.576 54.272 54.272v54.272c0 30.208-24.576 54.272-54.272 54.272H280.064c-30.208 0-54.272-24.576-54.272-54.272V568.32c0-30.208 24.064-54.784 54.272-54.784z\" fill=\"#CAE4FF\" p-id=\"62917\"></path></svg>', 0, 1, 0, NULL, NULL);
INSERT INTO `b_label_info` VALUES (21, NULL, '算法', NULL, '<svg t=\"1717330510831\" class=\"icon\" viewBox=\"0 0 1228 1024\" version=\"1.1\" xmlns=\"http://www.w3.org/2000/svg\" p-id=\"64064\" width=\"48\" height=\"48\"><path d=\"M857.23116 419.240086h-455.705693v50.921801h296.731702v50.871228H811.48639L935.486312 630.091971h110.512664c10.729759-30.550833 40.510741-50.201721 72.818827-48.05031 42.561003-3.544208 79.939354 28.086344 83.482759 70.648149 0.316289 3.793065 0.350808 7.604594 0.106768 11.402477 3.158078 42.081752-28.397817 78.753669-70.480372 81.910944-0.776274 0.057799-1.55897 0.105162-2.338455 0.139682-37.45943 2.428365-71.826609-20.763521-83.588724-56.409505H919.33548L784.517495 574.592803H533.8027v42.849998H636.365581c-0.358033 16.615633 0 76.306841 0 76.306841h51.281441l88.972067 114.95597h64.718123c8.783054-26.846072 33.753866-45.063222 61.998356-45.230197 39.378039-4.645602 75.065766 23.50978 79.710565 62.887819 0.325923 2.751076 0.491292 5.519813 0.494503 8.290156 6.457443 38.173891-19.252717 74.354516-57.425805 80.813565a69.97463 69.97463 0 0 1-11.189743 0.980176c-34.589545-0.765838-65.967226-20.462484-81.69179-51.281441h-75.64456c1.641655 2.666786-94.357818-114.699085-94.357818-114.699084 1.282016-0.307459-188.714834 0-188.714835 0a528.658581 528.658581 0 0 1 0 59.383749H614.719822l24.255549 62.203863a24.408074 24.408074 0 0 1-5.384949 28.307105c-94.357818 84.819363-266.66333 70.665811-266.66333 70.66581-15.04703-5.892296-24.761291-20.615812-24.255549-36.768249-4.178393-40.483446-40.383101-69.915225-80.865745-65.737635a73.716318 73.716318 0 0 0-16.158057 3.533772c-87.895559 17.53801-79.38384-32.410037-76.408792-37.742807 3.589966-49.897473-28.768695-47.076557-28.768695-47.076556-45.794541-2.820114-24.255549-22.614697-24.255549-22.614697 35.07602-16.973666 8.102309-22.614697 8.102309-22.614697-56.409504-4.256261-26.973712-28.307105-26.973712-28.307105 45.845918-57.948405-29.640498-65.02478-29.640498-65.02478-97.075981-1.383967 0-96.100622 0-96.100621a125.943416 125.943416 0 0 0 26.973712-115.947385 280.353687 280.353687 0 0 1 24.306926-186.715949c57.389681-85.827636 152.83926-138.493044 256.048367-141.280244A336.308826 336.308826 0 0 1 614.976706 187.808512h88.972068l145.486733-71.91411h102.468958c6.576252-34.475552 37.794183-58.660458 72.818826-56.409504 43.656777-2.524696 81.094533 30.818957 83.616821 74.474931a79.289114 79.289114 0 0 1-0.077868 10.344432c-0.738544 39.072185-28.933262 72.2015-67.384909 79.178332a101.231896 101.231896 0 0 1-88.972068-39.589166h-91.547337L704.100496 261.450173h-208.045419v53.742719h202.202092v53.742718H854.514602C862.052567 323.909316 892.557643 299.826361 939.061025 300.269488c1.477088 0.071446 2.954979 0.183833 4.425644 0.337161C983.270105 299.826361 1021.802832 329.528672 1024.553105 368.384111c0.390947 4.879207 0.933616 6.044019 0.460787 10.916-0.219155 39.610038-14.717093 90.979783-81.07928 92.050672-1.12066 0.018464-4.281147-0.535444-8.027651 0-20.33645 0-58.869177-8.563096-78.675801-52.110697z\" p-id=\"64065\" fill=\"#1296db\"></path></svg>', 0, 0, 0, NULL, NULL);
INSERT INTO `b_label_info` VALUES (22, NULL, '数据结构', NULL, '<svg t=\"1717330540613\" class=\"icon\" viewBox=\"0 0 1068 1024\" version=\"1.1\" xmlns=\"http://www.w3.org/2000/svg\" p-id=\"66394\" width=\"48\" height=\"48\"><path d=\"M817.117469 348.739286a16.43222 16.43222 0 0 0-2.907577 19.516015c18.062226 32.908494 28.326856 70.707006 28.326856 110.884444a230.844055 230.844055 0 0 1-56.697767 151.810805 16.520328 16.520328 0 0 0-1.850277 18.987364l40.441764 70.1343a16.43222 16.43222 0 0 0 16.652491 8.017867c6.167589-0.792976 12.467341-1.233518 18.899256-1.233518 72.381065 0.528651 132.162627 61.675893 131.237489 133.924795a132.250736 132.250736 0 0 1-131.281543 130.532622 132.118573 132.118573 0 1 1-88.593015-231.108381l-38.194999-66.081314a16.608437 16.608437 0 0 0-21.586562-6.608131 232.121627 232.121627 0 0 1-178.860089 9.295438 16.564383 16.564383 0 0 0-19.912503 7.22489l-25.683604 44.494752a16.520328 16.520328 0 0 0 0.792976 17.7979c15.507082 21.894942 24.49414 48.635847 24.273869 77.535408a132.691278 132.691278 0 0 1-131.14938 130.929109 132.250736 132.250736 0 0 1-133.308037-133.616416 132.603169 132.603169 0 0 1 167.934645-125.862875 16.608437 16.608437 0 0 0 18.767093-7.665432l22.423593-38.811759a16.476274 16.476274 0 0 0-2.995686-20.264936 230.88811 230.88811 0 0 1-69.165109-123.043406 16.476274 16.476274 0 0 0-16.256003-13.348425H273.444476a16.520328 16.520328 0 0 0-15.683299 11.454094 132.162627 132.162627 0 1 1-1.982439-88.108418 16.300057 16.300057 0 0 0 15.418973 10.57301H368.293188a16.476274 16.476274 0 0 0 16.211949-13.128154 231.108381 231.108381 0 0 1 86.742737-137.933728 16.652491 16.652491 0 0 0 4.317313-21.498454l-2.246765-3.920825a16.652491 16.652491 0 0 0-17.665738-7.929758 127.669098 127.669098 0 0 1-28.282802 2.555144c-71.808361-1.013247-131.061272-62.028326-130.136134-133.792632A132.074519 132.074519 0 0 1 430.497731 0.006168a132.735332 132.735332 0 0 1 131.14938 130.31235c0.440542 32.600115-10.969498 62.689139-30.265241 85.905708a16.520328 16.520328 0 0 0-1.630006 18.943309l6.784348 11.762474a16.388166 16.388166 0 0 0 18.282497 7.709487c18.062226-4.537584 37.005536-6.916511 56.389387-6.916511 54.01046 0 103.659554 18.502768 143.132126 49.560985 6.608131 5.198397 15.991678 4.669746 21.894941-1.233518l24.978737-24.978736c4.845963-4.845963 6.167589-12.203016 3.392174-18.370605a132.162627 132.162627 0 1 1 61.147242 63.614278 16.784654 16.784654 0 0 0-19.295743 2.995686l-29.340104 29.428211z\" fill=\"#20c95d\" p-id=\"66395\"></path></svg>', 0, 1, 0, NULL, NULL);
INSERT INTO `b_label_info` VALUES (23, NULL, 'css', NULL, '<svg t=\"1717330584375\" class=\"icon\" viewBox=\"0 0 1024 1024\" version=\"1.1\" xmlns=\"http://www.w3.org/2000/svg\" p-id=\"69779\" width=\"48\" height=\"48\"><path d=\"M911.36 962.56c0 33.93024-27.50976 61.44-61.44 61.44H174.08c-33.93024 0-61.44-27.50976-61.44-61.44V61.44c0-33.93024 27.50976-61.44 61.44-61.44h481.28l256 256v706.56z\" fill=\"#6D54B0\" p-id=\"69780\"></path><path d=\"M655.36 0l256 256h-215.04a40.96 40.96 0 0 1-40.96-40.96V0z\" fill=\"#DDD0FE\" p-id=\"69781\"></path><path d=\"M328.4992 773.0176c23.6544 0 43.008-6.0928 57.344-17.92 14.336-12.1856 24.0128-30.8224 28.3136-55.552h-37.632c-3.584 27.9552-19.3536 42.2912-47.6672 42.2912-16.4864 0-28.672-6.0928-36.9152-17.5616-8.96-11.8272-13.2608-28.3136-13.2608-49.1008s4.3008-37.2736 13.6192-48.7424c8.96-11.8272 21.1456-17.5616 36.9152-17.5616 12.9024 0 23.296 2.8672 31.1808 8.96 7.8848 5.7344 12.9024 15.0528 15.4112 27.5968h37.632c-3.584-22.9376-12.9024-40.1408-27.2384-51.6096-14.336-10.752-33.3312-16.128-56.9856-16.128-28.672 0-50.8928 9.6768-67.0208 29.0304-15.0528 17.92-22.5792 40.8576-22.5792 68.8128 0 28.672 7.5264 51.6096 22.9376 69.5296 15.7696 18.6368 37.632 27.9552 65.9456 27.9552z m189.952 0c54.8352 0 82.432-19.712 82.432-58.7776 0-16.4864-7.5264-29.3888-22.2208-38.7072-10.0352-6.4512-25.8048-12.544-46.9504-17.5616-18.6368-4.6592-30.8224-8.2432-37.2736-11.1104-10.0352-4.6592-15.0528-10.0352-15.0528-16.4864 0-7.168 3.2256-12.9024 10.0352-16.4864 5.7344-3.584 14.336-5.0176 25.8048-5.0176 12.9024 0 22.9376 2.1504 29.3888 6.4512 6.0928 3.9424 10.3936 10.752 13.2608 20.7872h36.9152c-4.3008-39.0656-30.8224-58.4192-78.848-58.4192-22.2208 0-40.4992 4.6592-54.4768 14.336-13.9776 9.3184-20.7872 22.5792-20.7872 39.424 0 15.7696 7.168 28.3136 21.504 37.2736 8.6016 5.7344 24.0128 11.1104 45.8752 16.128 18.9952 4.3008 31.5392 7.8848 37.2736 10.752 10.752 5.0176 16.4864 12.1856 16.4864 21.1456 0 16.4864-15.0528 25.088-44.4416 25.088-14.336 0-24.3712-2.5088-30.8224-7.168-6.4512-5.0176-11.1104-13.9776-13.2608-26.5216h-37.2736c4.3008 43.008 31.5392 64.8704 82.432 64.8704z m186.368 0c54.8352 0 82.432-19.712 82.432-58.7776 0-16.4864-7.5264-29.3888-22.2208-38.7072-10.0352-6.4512-25.8048-12.544-46.9504-17.5616-18.6368-4.6592-30.8224-8.2432-37.2736-11.1104-10.0352-4.6592-15.0528-10.0352-15.0528-16.4864 0-7.168 3.2256-12.9024 10.0352-16.4864 5.7344-3.584 14.336-5.0176 25.8048-5.0176 12.9024 0 22.9376 2.1504 29.3888 6.4512 6.0928 3.9424 10.3936 10.752 13.2608 20.7872h36.9152c-4.3008-39.0656-30.8224-58.4192-78.848-58.4192-22.2208 0-40.4992 4.6592-54.4768 14.336-13.9776 9.3184-20.7872 22.5792-20.7872 39.424 0 15.7696 7.168 28.3136 21.504 37.2736 8.6016 5.7344 24.0128 11.1104 45.8752 16.128 18.9952 4.3008 31.5392 7.8848 37.2736 10.752 10.752 5.0176 16.4864 12.1856 16.4864 21.1456 0 16.4864-15.0528 25.088-44.4416 25.088-14.336 0-24.3712-2.5088-30.8224-7.168-6.4512-5.0176-11.1104-13.9776-13.2608-26.5216h-37.2736c4.3008 43.008 31.5392 64.8704 82.432 64.8704z\" fill=\"#FFFFFF\" p-id=\"69782\"></path></svg>', 0, 0, 0, NULL, NULL);
INSERT INTO `b_label_info` VALUES (24, NULL, '数据库', NULL, '<svg t=\"1717330605087\" class=\"icon\" viewBox=\"0 0 1024 1024\" version=\"1.1\" xmlns=\"http://www.w3.org/2000/svg\" p-id=\"72029\" width=\"48\" height=\"48\"><path d=\"M170.496 164.352c0 67.072 152.576 121.856 341.504 121.856s341.504-54.272 341.504-121.856c0-67.072-152.576-121.856-341.504-121.856S170.496 97.28 170.496 164.352zM347.648 710.656c-68.096-14.848-130.048-36.352-177.152-66.048v219.648c0 55.296 106.496 102.4 249.856 117.248-51.2-51.2-83.456-121.344-83.456-198.656 2.048-25.6 6.656-49.152 10.752-72.192zM170.496 307.2v206.848c0 51.2 89.6 96.256 215.552 113.152 51.2-74.752 134.144-123.904 232.448-123.904 72.704 0 136.704 27.648 187.904 72.704 29.696-16.896 47.104-36.352 47.104-59.904V307.2c-80.896 51.2-210.944 83.456-341.504 83.456S251.904 358.4 170.496 307.2z m375.808 303.104l-130.048 117.248h437.248v-59.904h-307.2v-57.344z m-130.56 194.048v57.344h305.152V921.6l132.096-117.248H415.744z\" fill=\"#FF6A00\" p-id=\"72030\"></path></svg>', 0, 0, 0, NULL, NULL);
INSERT INTO `b_label_info` VALUES (25, NULL, 'Linux', NULL, '<svg t=\"1717330630792\" class=\"icon\" viewBox=\"0 0 1024 1024\" version=\"1.1\" xmlns=\"http://www.w3.org/2000/svg\" p-id=\"74233\" width=\"48\" height=\"48\"><path d=\"M417.317041 233.614876a10.008962 10.008962 0 0 0-8.825106 5.919278c-1.721972 3.65919-3.336321 5.381162-4.843046 5.381163s-2.798204 0-2.798204-2.798205c0-4.520176 3.551567-7.318381 10.762324-8.502236z m49.399071 7.96412c-1.506725 0-3.65919-0.860986-6.565018-3.65919a9.147976 9.147976 0 0 0-9.901339-2.582958c9.040353-4.197307 15.174878-3.766814 18.188329 1.076232a3.443944 3.443944 0 0 1-1.721972 5.165916zM267.397859 484.484664a2.798204 2.798204 0 0 0-3.443944 1.721972 47.999968 47.999968 0 0 0-2.582958 7.103134 28.735407 28.735407 0 0 1-3.121074 7.641251 58.00893 58.00893 0 0 1-5.596409 7.318381c-3.874437 4.197307-3.98206 6.457395-0.64574 6.887887 1.506725 0 3.874437-0.968609 7.103135-3.98206a28.304914 28.304914 0 0 0 7.103134-10.224208c0-1.183856 0.753363-2.475335 1.183856-3.98206a18.941691 18.941691 0 0 1 1.076232-3.443944 9.686092 9.686092 0 0 0 0.860986-2.475335 10.762325 10.762325 0 0 0 0-2.367711v-2.044842l-0.538116-1.506725-1.721972-1.076233z m485.488466 203.407936c0-6.780265-10.762325-14.744385-31.210742-23.89236l4.197307-15.605371a81.255551 81.255551 0 0 0 2.905827-14.744385c0.538116-5.058293 1.076232-9.147976 1.721972-12.161427a53.811623 53.811623 0 0 0 0-12.807166v-10.762325a86.098597 86.098597 0 0 0-1.937218-12.484296c-1.399102-6.457395-2.152465-10.762325-2.367711-11.623311s-1.076232-6.026902-2.798205-14.206269-2.690581-13.130036-3.121074-15.067254a167.784642 167.784642 0 0 0-26.690565-58.439423 133.237579 133.237579 0 0 0-40.896834-43.049299 161.43487 161.43487 0 0 1 32.286974 47.138982Q733.729387 582.314195 715.433435 648.179622a29.058277 29.058277 0 0 1-28.412537 23.784738c-11.730934 1.506725-19.049315-1.937218-21.52465-10.762325a177.68598 177.68598 0 0 1-4.843046-47.569475 271.533452 271.533452 0 0 0-6.565018-60.699511 381.093917 381.093917 0 0 0-10.762324-39.282485 159.067159 159.067159 0 0 0-10.762325-25.829579 164.017828 164.017828 0 0 0-8.825106-13.883399 49.183824 49.183824 0 0 0-7.318381-8.502237l-4.30493-3.98206a360.215007 360.215007 0 0 0-18.295952-58.00893 134.851928 134.851928 0 0 0-16.789226-32.286974 165.955046 165.955046 0 0 1-13.345283-18.726445 64.573948 64.573948 0 0 1-8.502237-22.708505 64.573948 64.573948 0 0 1 3.443944-30.349755 49.291447 49.291447 0 0 0 2.905828-27.766798c-2.044842-6.349772-10.762325-10.762325-25.18384-14.098645a118.385571 118.385571 0 0 1-25.614332-9.793716 124.089603 124.089603 0 0 0-20.233171-9.147976c-3.013451 0-5.058293-5.273539-6.242148-14.744385a56.502205 56.502205 0 0 1 4.627799-28.950653 22.170389 22.170389 0 0 1 20.448417-15.390124 26.475319 26.475319 0 0 1 28.950654 17.112096 43.049299 43.049299 0 0 1 2.260088 32.932714c-4.197307 7.210758-4.520176 12.161427-1.183856 15.067254a17.865459 17.865459 0 0 0 17.004473 0c4.950669-1.506725 7.426004-8.28699 7.426004-20.448417v-21.201779a129.147896 129.147896 0 0 0-7.64125-28.412538 45.73988 45.73988 0 0 0-11.946181-17.327342 51.551535 51.551535 0 0 0-13.345282-8.502237 75.336273 75.336273 0 0 0-15.282501-4.197306q-60.807134 4.520176-50.582926 76.089635a27.443928 27.443928 0 0 1-0.538117 8.502237 26.260072 26.260072 0 0 0-16.789226-6.026902 86.098597 86.098597 0 0 0-18.726445 0c-4.735423 0.753363-7.641251 0-8.825106-2.798205a107.623247 107.623247 0 0 0-9.255599-50.798172 30.349756 30.349756 0 0 0-25.614333-19.372185 24.645724 24.645724 0 0 0-23.569491 15.605371 86.098597 86.098597 0 0 0-9.363223 33.7937 83.730886 83.730886 0 0 0 2.044842 21.524649 75.336273 75.336273 0 0 0 7.318381 21.524649c3.228697 5.919279 6.134525 8.502236 8.825106 7.748874a15.928241 15.928241 0 0 0 9.147976-7.96412c1.506725-3.443944 0-4.950669-3.98206-4.520176s-5.596409-2.798204-8.825106-8.28699a40.573964 40.573964 0 0 1-5.381163-19.049315 33.901323 33.901323 0 0 1 5.058293-20.986533 20.448417 20.448417 0 0 1 19.372184-7.964121 17.219719 17.219719 0 0 1 15.282501 11.946181 48.753331 48.753331 0 0 1 5.381163 22.170389 81.578421 81.578421 0 0 1-0.860986 12.484296 62.421483 62.421483 0 0 0-17.54259 16.466357 48.645708 48.645708 0 0 1-15.60537 13.345283c-7.426004 4.30493-11.300441 6.672641-11.730934 7.103134a32.286974 32.286974 0 0 0-8.502237 14.959631 8.28699 8.28699 0 0 0 4.197307 10.224209 64.573948 64.573948 0 0 1 14.206268 10.762324 96.860922 96.860922 0 0 1 9.147976 10.762325 21.524649 21.524649 0 0 0 10.762325 7.426004A61.66812 61.66812 0 0 0 410.859647 323.049794a172.197195 172.197195 0 0 0 58.00893-8.502237c0.753363 0 5.058293-1.721972 13.022412-3.98206s14.529138-4.197307 19.587431-5.919278a113.650149 113.650149 0 0 0 16.789227-7.426004 27.766798 27.766798 0 0 0 11.838557-9.901339c3.443944-5.273539 7.210758-6.780265 11.408064-4.520176a7.318381 7.318381 0 0 1 3.65919 4.843046 7.748874 7.748874 0 0 1-1.721971 6.780264 15.067255 15.067255 0 0 1-9.363223 5.381163c-7.533627 2.260088-18.188329 6.349772-32.286974 12.161427L475.433595 323.049794a175.856385 175.856385 0 0 1-39.712978 13.130036 154.224113 154.224113 0 0 1-44.878894-1.183856c-3.766814-0.753363-5.488786 0-5.058293 1.183856a41.327327 41.327327 0 0 0 9.578469 10.762325 51.874405 51.874405 0 0 0 38.098629 12.484296A82.54703 82.54703 0 0 0 453.908945 355.336768a164.663568 164.663568 0 0 0 20.448417-7.96412c6.349772-3.013451 12.699543-6.349772 18.941692-9.901339s11.94618-6.780265 17.112096-9.686092a134.636682 134.636682 0 0 1 13.883399-6.780265 15.174878 15.174878 0 0 1 9.901338-1.399102 6.349772 6.349772 0 0 1 4.30493 8.717483 8.932729 8.932729 0 0 1-2.260088 2.905828 35.838541 35.838541 0 0 1-3.443944 2.582958c-1.076232 0.753363-2.690581 1.614349-4.843046 2.798204s-3.766814 1.937218-5.058293 2.582958l-5.704032 2.798204-5.381162 2.582958a247.533468 247.533468 0 0 0-38.313876 24.968594 233.542445 233.542445 0 0 1-37.775759 24.430477 29.1659 29.1659 0 0 1-27.874421 0c-7.96412-4.197307-19.802677-17.973082-35.730918-41.43495-8.28699-11.730934-13.022413-15.928241-14.206269-12.484297a22.278012 22.278012 0 0 0-0.538116 5.704032 77.165868 77.165868 0 0 1-8.502237 32.286974c-5.704032 11.94618-11.300441 22.385635-16.789226 31.533611a98.905764 98.905764 0 0 0-11.94618 32.932714 47.892345 47.892345 0 0 0 6.565018 35.730918q-13.130036 3.443944-35.515672 51.121042a295.533436 295.533436 0 0 0-27.013435 80.071696c-0.753363 6.780265-0.968609 19.910301-0.860986 39.174861s-0.753363 30.457379-3.121074 33.47083c-3.013451 9.147976-8.502236 9.686092-16.466357 1.721972a78.672593 78.672593 0 0 1-20.340793-53.811623 109.022349 109.022349 0 0 1 2.260088-32.286974c1.506725-7.210758 1.291479-10.762325-0.64574-10.224209a5.704032 5.704032 0 0 0-2.260088 2.798205q-20.340794 36.914774 5.704032 94.277964a48.107591 48.107591 0 0 0 14.206269 15.92824c7.533627 6.026902 12.053804 9.793715 13.668152 10.762325A591.174494 591.174494 0 0 0 315.613073 764.305106c32.286974 25.506709 49.39907 40.035848 52.843014 43.049298a31.425988 31.425988 0 0 1 10.116586 21.52465 34.654685 34.654685 0 0 1-7.964121 24.430477 35.085178 35.085178 0 0 1-25.829579 13.022413c3.121074 5.704032 8.60986 14.098645 16.466357 25.291463a102.457331 102.457331 0 0 1 15.92824 30.672625 152.071648 152.071648 0 0 1 3.98206 40.035848c17.434966-9.147976 18.726445-26.475319 3.982061-52.304898a53.811623 53.811623 0 0 0-5.919279-9.040353l-5.488786-6.780265c-1.076232-1.506725-1.506725-2.690581-1.076232-3.443943a15.282501 15.282501 0 0 1 7.426004-5.381163 10.762325 10.762325 0 0 1 11.300441 1.399102q26.152449 29.48877 94.277964 20.448417 75.336273-8.502236 100.520112-49.39907c8.717483-14.421515 15.174878-20.017924 19.264562-17.004473s6.457395 12.053804 5.704032 29.48877a235.802534 235.802534 0 0 1-13.022413 52.304898 37.883383 37.883383 0 0 0-3.443944 21.524649c1.183856 5.488786 5.704032 8.502236 13.668152 8.825106 1.076232-7.210758 3.874437-21.524649 8.179367-43.695038a401.542334 401.542334 0 0 0 7.641251-51.121042 191.246509 191.246509 0 0 0-3.659191-41.75782 297.040161 297.040161 0 0 1-4.197306-55.103102A61.775744 61.775744 0 0 1 629.119591 721.255807a35.946164 35.946164 0 0 1 28.950653-10.762325 32.286974 32.286974 0 0 1 19.587431-30.026886 70.385603 70.385603 0 0 1 41.219704-6.026901 69.094124 69.094124 0 0 1 34.008946 12.807166zM396.330508 218.224752a29.596393 29.596393 0 0 0-1.506725-17.004473c-2.044842-4.950669-4.197307-7.748874-6.457395-8.502237s-5.165916 0.538116-5.165916 3.98206a5.381162 5.381162 0 0 0 2.905828 3.443944c3.766814 0 5.058293 2.798204 3.874437 8.502237-1.076232 7.533627 0 11.300441 4.627799 11.300441a1.506725 1.506725 0 0 0 1.721972-1.721972z m237.847376 111.928176a11.730934 11.730934 0 0 0-3.659191-6.565018 17.004473 17.004473 0 0 0-7.426004-2.798204 21.524649 21.524649 0 0 1-7.748874-3.121074 26.152449 26.152449 0 0 1-5.381162-4.520177l-3.98206-4.627799-3.121074-3.659191-2.260088-2.260088s-1.183856 0-2.260089 0.860986c-5.381162 6.026902-3.98206 14.313892 3.98206 24.645724a43.049299 43.049299 0 0 0 21.52465 17.973082 8.28699 8.28699 0 0 0 8.28699-4.6278 15.605371 15.605371 0 0 0 1.937218-11.300441z m-101.058229-120.968529a21.524649 21.524649 0 0 0-2.798205-10.762325 21.524649 21.524649 0 0 0-6.242148-7.103134 6.995511 6.995511 0 0 0-5.165916-1.721972 6.457395 6.457395 0 0 0-4.520176 1.183856 1.399102 1.399102 0 0 0 0 2.260088 8.179367 8.179367 0 0 0 2.798204 1.721972c5.381162 1.506725 8.717483 7.318381 10.224209 17.542589 0 1.183856 1.506725 0.753363 4.627799-1.076232 0.753363-1.076232 1.076232-1.614349 1.076233-2.044842z m30.672625-132.26897a5.058293 5.058293 0 0 0-1.399102-2.905828 23.892361 23.892361 0 0 0-5.165916-3.98206l-5.381162-3.336321a23.031375 23.031375 0 0 0-13.560529-8.502236 6.995511 6.995511 0 0 0-6.565018 4.197306 11.730934 11.730934 0 0 0 0 7.426004 13.237659 13.237659 0 0 1 0 7.103135 16.789226 16.789226 0 0 1-3.443944 5.919278 26.367695 26.367695 0 0 0-3.336321 5.165916c0 0.860986 0 2.475335 1.721972 4.735423a3.336321 3.336321 0 0 0 4.520176 0c1.506725-1.076232 3.551567-2.798204 6.242149-5.058293a29.058277 29.058277 0 0 1 8.502236-5.165916 12.26905 12.26905 0 0 1 5.058293-0.538116 31.425988 31.425988 0 0 0 8.60986-1.076232 7.318381 7.318381 0 0 0 5.058292-3.98206zM884.401932 838.349899a75.874389 75.874389 0 0 1 17.650213 13.991022 23.784738 23.784738 0 0 1 6.780264 13.56053 32.286974 32.286974 0 0 1-1.399102 12.807166 29.811639 29.811639 0 0 1-8.825106 12.484297 148.304834 148.304834 0 0 1-13.345283 10.762324 152.179271 152.179271 0 0 1-17.004473 10.762325l-17.865459 9.363222c-4.950669 2.367711-10.762325 5.381162-18.188328 8.717483l-15.390125 7.426004a303.820426 303.820426 0 0 0-48.538084 32.286974 561.470478 561.470478 0 0 0-43.049299 36.376658 65.434934 65.434934 0 0 1-38.636745 10.762324 101.488722 101.488722 0 0 1-50.582926-8.179366 48.968577 48.968577 0 0 1-16.681604-13.345283 67.156906 67.156906 0 0 1-9.363222-14.529138 25.721956 25.721956 0 0 0-12.484297-10.762325 64.573948 64.573948 0 0 0-26.690565-5.381162c-16.681603 0-41.327327-0.538116-73.829547-0.538116-7.210758 0-17.973082 0-32.286974 0.860986s-25.399086 0.968609-32.932714 1.399102a129.901259 129.901259 0 0 0-45.201763 8.502236A105.147912 105.147912 0 0 0 357.048023 992.251142a107.623247 107.623247 0 0 1-24.645723 16.25111 62.529106 62.529106 0 0 1-30.457379 6.457395A257.004313 257.004313 0 0 1 238.662452 997.417058a584.609476 584.609476 0 0 0-82.977524-24.430477c-7.103134-1.506725-16.789226-3.336321-28.950653-5.381162s-21.524649-3.766814-28.412537-5.165916-14.206269-3.121074-22.385635-5.381162a63.712962 63.712962 0 0 1-19.049315-8.179367 27.982044 27.982044 0 0 1-9.578469-10.762325 51.551535 51.551535 0 0 1 3.874437-37.77576 162.188233 162.188233 0 0 0 10.762325-30.995495 84.161379 84.161379 0 0 0-2.260089-22.708505C57.102034 837.273667 55.702932 828.879054 53.335221 822.206412a69.201748 69.201748 0 0 1-2.582958-20.771286A20.55604 20.55604 0 0 1 56.671541 785.829755a57.578437 57.578437 0 0 1 32.286975-7.96412 80.717435 80.717435 0 0 0 34.116569-6.780265 79.103086 79.103086 0 0 0 23.784737-19.910301 50.367679 50.367679 0 0 0 6.887888-28.950653q11.838557 41.43495-18.188329 60.161395a73.076185 73.076185 0 0 1-47.138982 8.502237c-12.91479-1.076232-21.524649 0.753363-24.430477 5.704032-4.950669 5.704032-3.98206 16.466357 2.798205 32.286974a86.959583 86.959583 0 0 0 4.520176 10.224208c2.367711 4.520176 3.874437 7.96412 4.843046 10.762325a46.600866 46.600866 0 0 1 2.582958 9.686092A44.017908 44.017908 0 0 1 79.380047 871.928352a75.336273 75.336273 0 0 1-9.578469 27.766798 49.506694 49.506694 0 0 0-7.964121 27.228681q1.614349 9.686092 21.52465 14.852008c7.533627 2.260088 23.569491 5.704032 47.999968 10.762325s43.049299 8.60986 56.502204 11.623311c9.040353 2.367711 23.031375 6.457395 41.973067 12.484296s34.547062 10.762325 46.923735 13.130037a94.493211 94.493211 0 0 0 31.425988 2.260088 61.237627 61.237627 0 0 0 36.699527-15.928241A42.080689 42.080689 0 0 0 357.048023 948.556104a106.224145 106.224145 0 0 0-3.874437-33.578453 164.986437 164.986437 0 0 0-10.762324-29.48877c-3.766814-7.426004-7.533627-14.313892-11.300441-20.771286q-68.771255-107.623247-95.999936-137.434886-38.636746-41.973066-64.573948-22.708505c-4.197307 3.443944-6.995511 0.645739-8.502237-8.502237a94.923704 94.923704 0 0 1-1.183856-21.524649 89.865411 89.865411 0 0 1 5.704032-29.596393 165.201684 165.201684 0 0 1 13.668153-26.690565 156.914694 156.914694 0 0 0 12.484296-23.784738c3.013451-7.96412 7.96412-21.524649 14.959632-40.896834s12.59192-34.116569 16.789226-44.340777a347.407841 347.407841 0 0 1 17.004473-34.654686 144.968513 144.968513 0 0 1 22.170389-30.672625q62.421483-81.255551 70.385603-110.636698-6.780265-63.605339-9.040352-176.071631a207.712866 207.712866 0 0 1 13.668152-86.098598 125.488706 125.488706 0 0 1 60.161395-59.408032A124.62772 124.62772 0 0 1 458.213875 0.180054a213.847391 213.847391 0 0 1 60.269018 7.748873 149.811559 149.811559 0 0 1 50.582926 23.569491 163.694958 163.694958 0 0 1 51.874405 68.986502 176.502125 176.502125 0 0 1 16.789227 83.730886 365.919039 365.919039 0 0 0 17.004473 121.506645 313.937011 313.937011 0 0 0 75.336272 123.766734 318.88768 318.88768 0 0 1 56.502205 92.555992 490.654382 490.654382 0 0 1 33.793699 108.484233 197.488658 197.488658 0 0 1 2.905828 47.677098 96.860922 96.860922 0 0 1-6.780264 32.286974c-3.443944 7.533627-7.210758 11.730934-10.762325 12.484297a26.260072 26.260072 0 0 0-13.345283 10.762325c-5.165916 6.457395-10.762325 13.130036-15.390124 20.23317a68.340762 68.340762 0 0 1-23.031375 18.941691 64.573948 64.573948 0 0 1-34.547062 7.964121 76.842998 76.842998 0 0 1-17.973082-3.228698 28.197291 28.197291 0 0 1-12.699543-7.64125c-3.443944-3.65919-6.026902-6.565018-7.748874-8.825107a78.995463 78.995463 0 0 1-6.457395-11.62331c-2.690581-5.488786-4.412553-9.255599-5.165916-10.762325q-12.91479-21.309403-23.784737-17.542589c-7.210758 2.690581-12.484297 11.94618-15.928241 27.874421a113.004409 113.004409 0 0 0 3.98206 55.103102q10.762325 39.712978 0 110.744321-5.704032 36.80715 10.762325 57.040321a46.816112 46.816112 0 0 0 41.542573 18.726445A82.331784 82.331784 0 0 0 733.729387 970.726493a404.986278 404.986278 0 0 1 50.798172-37.77576 376.681364 376.681364 0 0 1 58.762293-24.107607 160.358638 160.358638 0 0 0 43.802661-20.771287c9.040353-6.995511 12.59192-13.560529 10.762325-19.587431a32.286974 32.286974 0 0 0-14.206269-16.143487 133.345203 133.345203 0 0 0-29.165899-13.345282 47.246605 47.246605 0 0 1-28.197291-27.336305 91.049267 91.049267 0 0 1-8.502237-41.11208 37.668136 37.668136 0 0 1 8.825107-27.013435 143.999904 143.999904 0 0 0 4.520176 32.286974 127.425924 127.425924 0 0 0 8.28699 23.031375 62.421483 62.421483 0 0 0 11.623311 16.143487 91.802629 91.802629 0 0 0 11.94618 10.762324c3.013451 2.044842 7.103134 4.520176 12.161427 7.318381s8.28699 4.6278 9.363222 5.381163z\" fill=\"#4BC97E\" p-id=\"74234\"></path><path d=\"M709.837026 754.941883m-269.058117 0a269.058117 269.058117 0 1 0 538.116234 0 269.058117 269.058117 0 1 0-538.116234 0Z\" fill=\"#45936E\" p-id=\"74235\"></path><path d=\"M709.837026 813.704176c45.632257 0 88.358686-22.816128 129.147896-69.309371-40.681587-47.569475-83.730886-69.416994-129.147896-69.416994s-88.358686 22.816128-129.147896 69.416994c40.789211 47.461852 83.623263 69.309371 129.147896 69.309371z m0 35.192802c-56.932698 0-108.268986-27.551551-155.730838-81.793668a34.547062 34.547062 0 0 1 0-45.524633c47.461852-53.811623 98.798141-81.793668 155.730838-81.793668S818.213635 667.444184 865.675487 721.255807a34.547062 34.547062 0 0 1 0 45.524633c-47.461852 53.811623-98.798141 81.793668-155.838461 81.793668z\" fill=\"#E2E2E2\" p-id=\"74236\"></path><path d=\"M709.837026 796.59208a52.304898 52.304898 0 1 1 52.304898-52.197275A51.551535 51.551535 0 0 1 709.837026 796.59208z m0-34.224193a17.112096 17.112096 0 1 0-17.004473-17.112096 17.542589 17.542589 0 0 0 17.004473 17.112096z\" fill=\"#E2E2E2\" p-id=\"74237\"></path><path d=\"M824.000088 602.766151m15.220226 15.220226l0 0q15.220226 15.220226 0 30.440451l-243.523609 243.523608q-15.220226 15.220226-30.440451 0l0 0q-15.220226-15.220226 0-30.440451l243.523609-243.523608q15.220226-15.220226 30.440451 0Z\" fill=\"#E2E2E2\" p-id=\"74238\"></path></svg>', 0, 1, 0, NULL, NULL);
INSERT INTO `b_label_info` VALUES (26, NULL, 'Docker', NULL, '<svg t=\"1717330648932\" class=\"icon\" viewBox=\"0 0 1024 1024\" version=\"1.1\" xmlns=\"http://www.w3.org/2000/svg\" p-id=\"76476\" width=\"48\" height=\"48\"><path d=\"M1006.762667 438.613333a163.242667 163.242667 0 0 0-120.917334-10.965333 160.853333 160.853333 0 0 0-65.408-102.4l-12.885333-10.154667-10.965333 12.373334a135.125333 135.125333 0 0 0-25.6 97.450666c1.706667 23.637333 10.154667 46.378667 24.448 65.493334-11.221333 6.272-22.997333 11.52-35.2 15.402666a233.386667 233.386667 0 0 1-72.448 11.690667H3.754667l-1.450667 15.317333a285.482667 285.482667 0 0 0 24.106667 148.992l9.386666 18.517334 1.024 1.706666c64.213333 106.197333 192.554667 161.152 315.52 161.152 238.165333 0 419.328-113.706667 509.610667-332.202666 60.330667 3.072 121.941333-14.293333 151.552-70.314667l7.509333-14.293333-14.250666-7.978667v0.213333zM203.093333 714.965333a53.546667 53.546667 0 0 1 2.048-106.965333 53.333333 53.333333 0 0 1 52.906667 53.504 54.229333 54.229333 0 0 1-54.954667 53.461333z\" fill=\"#039BC5\" p-id=\"76477\"></path><path d=\"M203.093333 633.258667a28.16 28.16 0 1 0 28.928 28.245333 28.501333 28.501333 0 0 0-8.704-20.138667 27.946667 27.946667 0 0 0-20.224-8.106666z\" fill=\"#38504F\" p-id=\"76478\"></path><path d=\"M54.869333 387.882667h97.109334v97.152H54.912V387.84m129.493333 0h97.109334v97.152H184.362667V387.84m0-129.493333h97.109333v97.152H184.362667V258.389333m129.578666 0h97.152v97.152H313.941333V258.389333m0 129.493334h97.152v97.152H313.941333V387.84m129.493334 0h97.152v97.152h-97.152V387.84m129.578666 0h97.152v97.152H573.013333V387.84m-129.578666-129.493333h97.152v97.152h-97.152V258.389333m0-129.578666h97.152v97.152h-97.152V128.810667\" fill=\"#2BB558\" p-id=\"76479\"></path><path d=\"M0 0h1024v1024H0z\" fill=\"#D8D8D8\" fill-opacity=\"0\" p-id=\"76480\"></path></svg>', 0, 1, 0, NULL, NULL);
INSERT INTO `b_label_info` VALUES (28, NULL, 'GitHub', NULL, '<svg t=\"1717330667481\" class=\"icon\" viewBox=\"0 0 1024 1024\" version=\"1.1\" xmlns=\"http://www.w3.org/2000/svg\" p-id=\"79261\" width=\"48\" height=\"48\"><path d=\"M443.392 786.062222c0-8.049778-0.284444-29.411556-0.455111-57.742222-94.435556 20.508444-114.346667-45.511111-114.346667-45.511111-15.445333-39.196444-37.688889-49.635556-37.688889-49.635556-30.833778-21.048889 2.304-20.650667 2.304-20.650666 34.076444 2.389333 52.024889 34.986667 52.024889 34.986666 30.264889 51.882667 79.445333 36.892444 98.787556 28.216889 3.072-21.959111 11.861333-36.920889 21.560889-45.397333-75.377778-8.561778-154.652444-37.688889-154.652445-167.765333 0-37.063111 13.226667-67.356444 34.958222-91.107556-3.498667-8.590222-15.132444-43.093333 3.328-89.827556 0 0 28.501333-9.130667 93.354667 34.787556a325.12 325.12 0 0 1 84.963556-11.406222c28.842667 0.113778 57.884444 3.896889 84.992 11.406222 64.824889-43.918222 93.269333-34.787556 93.269333-34.787556 18.517333 46.734222 6.855111 81.237333 3.384889 89.827556 21.76 23.751111 34.872889 54.044444 34.872889 91.107556 0 130.417778-79.36 159.089778-154.993778 167.509333 12.202667 10.467556 23.04 31.203556 23.04 62.862222 0 45.368889-0.398222 82.005333-0.398222 93.127111 0 9.073778-168.305778 8.988444-168.305778 0z\" fill=\"#7D849C\" p-id=\"79262\"></path><path d=\"M512 1024c282.766222 0 512-229.233778 512-512S794.766222 0 512 0 0 229.233778 0 512s229.233778 512 512 512z m0-28.444444C244.935111 995.555556 28.444444 779.064889 28.444444 512S244.935111 28.444444 512 28.444444s483.555556 216.490667 483.555556 483.555556-216.490667 483.555556-483.555556 483.555556z\" fill=\"#7D849C\" p-id=\"79263\"></path></svg>', 0, 0, 0, NULL, NULL);
INSERT INTO `b_label_info` VALUES (29, NULL, '代码规范', NULL, '<svg t=\"1717330682184\" class=\"icon\" viewBox=\"0 0 1024 1024\" version=\"1.1\" xmlns=\"http://www.w3.org/2000/svg\" p-id=\"81185\" width=\"48\" height=\"48\"><path d=\"M911.36 1024H192c-23.04 0-40.96-17.92-40.96-40.96V40.96C151.04 17.92 168.96 0 192 0h583.68l176.64 176.64v808.96c0 20.48-17.92 38.4-40.96 38.4zM192 15.36c-12.8 0-23.04 12.8-23.04 25.6v944.64c0 12.8 10.24 23.04 23.04 23.04h719.36c12.8 0 23.04-10.24 23.04-23.04V181.76L768 15.36H192z\" fill=\"#26359A\" p-id=\"81186\"></path><path d=\"M696.32 412.16H71.68V189.44h624.64c10.24 0 15.36 5.12 15.36 15.36v192c0 7.68-5.12 15.36-15.36 15.36z\" fill=\"#26359A\" p-id=\"81187\"></path><path d=\"M360.96 373.76H332.8l-58.88-71.68c-2.56-2.56-2.56-5.12-5.12-5.12v76.8h-17.92v-153.6h20.48v71.68c0-2.56 2.56-2.56 5.12-5.12L332.8 220.16h25.6l-64 74.24 66.56 79.36zM519.68 373.76v-102.4c0-7.68 0-17.92 2.56-30.72-2.56 7.68-2.56 12.8-5.12 15.36l-51.2 117.76h-10.24l-51.2-117.76c0-2.56-2.56-7.68-5.12-15.36v133.12h-17.92v-153.6H409.6l46.08 104.96c2.56 7.68 5.12 15.36 7.68 17.92 2.56-10.24 5.12-15.36 7.68-20.48l46.08-104.96h25.6v156.16h-23.04zM660.48 373.76h-81.92v-153.6h20.48V358.4h61.44v15.36z\" fill=\"#FFFFFF\" p-id=\"81188\"></path><path d=\"M71.68 412.16h79.36v79.36l-79.36-79.36zM775.68 0H768v181.76h181.76v-5.12L775.68 0z\" fill=\"#1D2E66\" p-id=\"81189\"></path><path d=\"M391.68 796.16l-102.4-71.68v-25.6l102.4-76.8v40.96l-74.24 48.64 74.24 46.08v38.4zM519.68 565.76l-76.8 279.04h-28.16l76.8-279.04h28.16zM647.68 721.92l-102.4 71.68v-35.84l74.24-46.08-74.24-48.64v-35.84l102.4 74.24v20.48z\" fill=\"#26359A\" p-id=\"81190\"></path></svg>', 0, 0, 0, NULL, NULL);
INSERT INTO `b_label_info` VALUES (32, NULL, 'React.js', NULL, '<svg t=\"1717330721915\" class=\"icon\" viewBox=\"0 0 1024 1024\" version=\"1.1\" xmlns=\"http://www.w3.org/2000/svg\" p-id=\"84972\" width=\"48\" height=\"48\"><path d=\"M512 511.8m-80 0a80 80 0 1 0 160 0 80 80 0 1 0-160 0Z\" fill=\"#61DAFB\" p-id=\"84973\"></path><path d=\"M960.5 511.8c0-62.8-73.8-117.2-188.5-150.1 28.9-115.8 18.7-206.9-35.7-238.3-54.5-31.4-138.5 5.3-224.3 88.2-85.8-82.9-169.8-119.6-224.3-88.2-54.4 31.4-64.6 122.6-35.7 238.3C137.3 394.6 63.5 449 63.5 511.8S137.3 629 252 661.9c-28.9 115.7-18.7 206.9 35.7 238.3 13.4 7.8 28.6 11.6 45.2 11.6 39.7 0 87.8-21.8 140-64.2 13-10.6 26.1-22.6 39.1-35.2 13 12.6 26.1 24.6 39.1 35.2 52.2 42.4 100.2 64.2 140 64.2 16.6 0 31.8-3.8 45.2-11.6 54.4-31.4 64.6-122.5 35.7-238.3 114.7-32.9 188.5-87.3 188.5-150.1zM716.8 157.2c35.3 20.4 42.7 94.3 17.6 194.8-36.7-8.4-76.7-14.7-119.3-18.6-24.7-34.9-50.2-66.4-75.8-94 59.2-57.3 114.2-88.4 152-88.4 9.6-0.1 18.2 2 25.5 6.2zM637 584c-13.8 24-28.4 47-43.3 69-26.1 2-53.3 3.1-81.7 3.1-28.3 0-55.5-1.1-81.6-3.1-15-22-29.5-45.1-43.3-69-14.1-24.5-26.7-48.6-38.1-72.2 11.4-23.6 24-47.7 38.1-72.2 14.1-24.5 28.7-47.4 43.4-69.1 26.1-2 53.3-3.1 81.6-3.1 28.3 0 55.5 1.1 81.6 3.1 14.7 21.6 29.3 44.6 43.4 69 14.1 24.5 26.7 48.6 38.1 72.2-11.5 23.7-24.1 47.8-38.2 72.3z m58.8-26.4c11.2 26.6 20.4 52.1 28 76.5-24.9 5.6-51.7 10.4-80.3 14 9.3-14.5 18.4-29.3 27.3-44.6 8.8-15.4 17.1-30.7 25-45.9zM512 756.5c-17.7-19.2-35.1-40.1-52.2-62.6 17.1 0.8 34.5 1.3 52.2 1.3 17.7 0 35.1-0.5 52.2-1.3-17.1 22.5-34.5 43.4-52.2 62.6zM380.5 648.1c-28.6-3.6-55.3-8.4-80.3-14 7.6-24.4 16.8-49.9 28-76.5 7.9 15.2 16.1 30.5 25 45.9 8.9 15.2 18 30 27.3 44.6zM328.2 466c-11.2-26.6-20.4-52.1-28-76.5 24.9-5.6 51.6-10.4 80.2-14-9.2 14.4-18.4 29.2-27.2 44.6-8.8 15.4-17.1 30.7-25 45.9zM512 267.1c17.3 18.7 34.8 39.8 52.1 62.7-17.1-0.8-34.4-1.3-52.1-1.3-17.7 0-35 0.5-52.1 1.3 17.3-22.9 34.8-44 52.1-62.7z m158.7 153c-8.9-15.3-18-30.1-27.2-44.6 28.6 3.6 55.3 8.4 80.2 14-7.6 24.4-16.8 49.9-28 76.5-7.8-15.2-16.1-30.5-25-45.9zM307.2 157.2c7.2-4.2 15.8-6.2 25.6-6.2 37.8 0 92.7 31.1 151.9 88.4-25.6 27.6-51.1 59.2-75.8 94-42.5 3.9-82.6 10.2-119.3 18.6-25.1-100.6-17.6-174.5 17.6-194.8zM102.5 511.8c0-40.8 60.3-84.2 160-112.6 11.1 36 25.6 73.8 43.5 112.6-17.8 38.8-32.4 76.6-43.5 112.6-99.7-28.4-160-71.9-160-112.6z m345.8 305.5c-59.7 48.5-111.1 66.4-141.1 49.2-35.3-20.4-42.7-94.3-17.6-194.8 36.7 8.4 76.7 14.7 119.3 18.6 24.4 34.5 49.9 66.1 75.8 94.2-12.1 11.7-24.2 22.9-36.4 32.8z m268.5 49.2c-29.9 17.3-81.4-0.6-141.1-49.2-12.1-9.9-24.3-21.1-36.5-32.8 26-28.1 51.4-59.7 75.8-94.2 42.5-3.9 82.6-10.2 119.3-18.7 25.2 100.6 17.7 174.5-17.5 194.9z m44.8-242.1c-11.1-36-25.6-73.8-43.5-112.6 17.8-38.8 32.4-76.6 43.5-112.6 99.7 28.5 160 71.9 160 112.6-0.1 40.7-60.4 84.2-160 112.6z\" fill=\"#61DAFB\" p-id=\"84974\"></path></svg>', 1, 0, 0, NULL, NULL);
INSERT INTO `b_label_info` VALUES (33, NULL, 'Git', NULL, '<svg t=\"1717330735568\" class=\"icon\" viewBox=\"0 0 1024 1024\" version=\"1.1\" xmlns=\"http://www.w3.org/2000/svg\" p-id=\"87148\" width=\"48\" height=\"48\"><path d=\"M110.933333 451.84L357.546667 204.8l72.106666 72.533333c-10.24 36.266667 6.4 75.946667 39.68 95.146667v236.373333c-25.6 14.506667-42.666667 42.24-42.666666 73.813334a85.333333 85.333333 0 0 0 85.333333 85.333333 85.333333 85.333333 0 0 0 85.333333-85.333333c0-31.573333-17.066667-59.306667-42.666666-73.813334V401.493333l88.32 89.173334c-2.986667 6.4-2.986667 13.653333-2.986667 21.333333a85.333333 85.333333 0 0 0 85.333333 85.333333 85.333333 85.333333 0 0 0 85.333334-85.333333 85.333333 85.333333 0 0 0-85.333334-85.333333c-7.68 0-14.933333 0-21.333333 2.986666L594.346667 320a84.48 84.48 0 0 0-49.066667-99.84c-18.346667-6.826667-37.546667-8.533333-54.613333-3.84L418.133333 144.213333l33.706667-33.28c33.28-33.706667 87.04-33.706667 120.32 0l340.906667 340.906667c33.706667 33.28 33.706667 87.04 0 120.32l-340.906667 340.906667c-33.28 33.706667-87.04 33.706667-120.32 0L110.933333 572.16c-33.706667-33.28-33.706667-87.04 0-120.32z\" fill=\"#E64A19\" p-id=\"87149\"></path></svg>', 0, 0, 0, NULL, NULL);
INSERT INTO `b_label_info` VALUES (34, NULL, '团队开发', NULL, '<svg t=\"1717330747400\" class=\"icon\" viewBox=\"0 0 1024 1024\" version=\"1.1\" xmlns=\"http://www.w3.org/2000/svg\" p-id=\"91341\" width=\"48\" height=\"48\"><path d=\"M681.83 335.58l-2.07-0.81c46.74 32.42 109.37 31.65 155.28-1.89s64.72-92.28 46.63-145.63c-18.09-53.35-68.98-89.25-126.18-89s-107.77 36.58-125.38 90.09c33.24 32.17 51.88 76.16 51.71 122.02 0.64 8.4 0.64 16.82 0.01 25.22zM670.74 591.3C653.9 519 588.38 464.93 509.73 464.93c-91.39 0-165.49 72.85-165.49 162.7v203.38a796.073 796.073 0 0 0 165.49 20.34c17.59-0.3 35.06-2.21 52.57-3.67-16.76-97.52 19.91-204.78 108.44-256.38z\" fill=\"#FEAD05\" p-id=\"91342\"></path><path d=\"M377.34 310.36a132.39 130.16 0 1 0 264.78 0 132.39 130.16 0 1 0-264.78 0Z\" fill=\"#FEAD05\" p-id=\"91343\"></path><path d=\"M708.9 574.01c24.55-8.45 51.22-14.25 81.79-14.25 53.81 0 97.63 15.14 132.76 39.07v-52.55c-0.21-71.5-47.86-134.5-117.46-155.25-69.6-20.76-144.82 5.6-185.38 64.95 43.96 27.44 74.85 69.87 88.29 118.03zM181.9 334.13c45.3 32.3 105.98 32.48 151.99 1.69-0.61-8.34-0.62-16.71 0-25.05-0.16-45.86 18.48-89.85 51.72-122.03-17.65-53.69-68.48-90.09-125.87-90.13-57.39-0.05-108.28 36.27-126.01 89.94S135.44 301 181.9 334.13zM213.46 391.03C143.87 411.78 96.2 474.78 96 546.28v203.38a795.237 795.237 0 0 0 165.49 18.71h41.37V627.63c0.01-69.59 36.23-134.35 95.98-171.65-40.55-59.35-115.78-85.71-185.38-64.95zM928 757.75c0-2.68-1.02-5.35-3.06-7.39a10.422 10.422 0 0 0-7.38-3.07H901.6c-6.61 0-13.61-5.16-15.58-11.47l-8.28-20.11c-3.11-5.81-1.83-14.4 2.82-19.07l11.33-11.31c1.89-1.89 3.06-4.5 3.06-7.39 0-2.89-1.16-5.5-3.06-7.4l-19.22-19.19c-1.89-1.89-4.5-3.06-7.38-3.06-2.77 0-5.43 1.1-7.39 3.06l-11.32 11.33c-4.65 4.66-13.26 5.95-19.08 2.82l-20.09-8.27c-6.28-1.95-11.46-8.96-11.46-15.57v-15.98c0-2.77-1.1-5.43-3.06-7.38a10.37 10.37 0 0 0-7.38-3.06h-27.15c-2.68 0-5.35 1.02-7.39 3.06a10.388 10.388 0 0 0-3.06 7.38v15.98c0 6.61-5.14 13.62-11.45 15.57l-20.11 8.27c-5.84 3.13-14.4 1.86-19.05-2.82l-11.37-11.32a10.422 10.422 0 0 0-7.39-3.05c-2.88 0-5.49 1.17-7.38 3.06l-19.17 19.18a10.45 10.45 0 0 0-3.06 7.38c0 2.88 1.17 5.49 3.06 7.38l11.31 11.33c4.68 4.68 5.96 13.25 2.82 19.07l-8.28 20.12c-1.95 6.31-8.95 11.47-15.56 11.47l-15.97-0.01c-2.68 0-5.35 1.02-7.39 3.06a10.411 10.411 0 0 0-3.06 7.39v27.14c0 2.67 1.02 5.35 3.06 7.39 1.96 1.96 4.62 3.06 7.39 3.06h15.98c6.61 0.01 13.6 5.17 15.55 11.47l8.29 20.11c3.11 5.83 1.84 14.43-2.82 19.08l-11.34 11.31a10.417 10.417 0 0 0-3.06 7.4c0 2.88 1.17 5.5 3.06 7.39l19.22 19.19c1.89 1.89 4.5 3.06 7.38 3.06 2.77 0 5.43-1.1 7.39-3.06l11.34-11.32c4.65-4.66 13.22-5.94 19.02-2.78l20.15 8.27c6.31 1.96 11.45 8.95 11.45 15.57v15.92c0 2.67 1.02 5.35 3.07 7.39 1.96 1.96 4.62 3.06 7.39 3.06h27.13c2.77 0 5.43-1.09 7.39-3.06 1.96-1.96 3.07-4.62 3.07-7.39v-15.92c0-6.61 5.18-13.61 11.46-15.57l20.12-8.27c5.82-3.16 14.42-1.88 19.1 2.78l11.26 11.31c1.89 1.9 4.5 3.08 7.39 3.08 2.77 0.01 5.44-1.09 7.39-3.06l19.22-19.21c1.9-1.9 3.06-4.51 3.06-7.4 0-2.89-1.17-5.5-3.06-7.39L880.56 846c-4.65-4.65-5.93-13.25-2.82-19.08l8.28-20.11c1.96-6.29 8.99-11.45 15.58-11.45h15.96c2.67 0 5.33-1.02 7.38-3.06 1.96-1.96 3.07-4.62 3.06-7.39v-27.16z m-96.02 13.55c0 33.16-26.87 60.02-60.02 60.02-33.17 0-60.04-26.86-60.04-60.02 0-33.15 26.87-60.02 60.04-60.02 33.15-0.01 60.02 26.87 60.02 60.02z m0 0\" fill=\"#FEAD05\" p-id=\"91344\"></path></svg>', 0, 0, 0, NULL, NULL);
INSERT INTO `b_label_info` VALUES (35, NULL, 'Redis', NULL, '<svg t=\"1717330762699\" class=\"icon\" viewBox=\"0 0 1191 1024\" version=\"1.1\" xmlns=\"http://www.w3.org/2000/svg\" p-id=\"93537\" width=\"48\" height=\"48\"><path d=\"M1144.888041 786.352873c-63.5904 33.145018-393.001891 168.587636-463.131928 205.149091-70.130036 36.566109-109.088582 36.212364-164.491636 9.728-55.3984-26.484364-405.950836-168.084945-469.099054-198.269673C16.607604 787.879564 0.009495 775.144727 0.009495 763.117382v-120.441018s456.378182-99.351273 530.054982-125.784437c73.672145-26.433164 99.234909-27.387345 161.931636-4.421818 62.706036 22.974836 437.611055 90.614691 499.577019 113.310255l-0.027928 118.737454c0.009309 11.906327-14.289455 24.966982-46.657163 41.835055\" fill=\"#912626\" p-id=\"93538\"></path><path d=\"M1144.864768 666.624c-63.585745 33.131055-392.987927 168.578327-463.117964 205.135127-70.125382 36.570764-109.083927 36.212364-164.482327 9.728-55.403055-26.465745-405.941527-168.0896-469.085091-198.255709-63.143564-30.184727-64.465455-50.957964-2.438982-75.245382 62.026473-24.296727 410.642618-161.070545 484.328728-187.503709 73.672145-26.423855 99.230255-27.387345 161.926981-4.412509 62.701382 22.965527 390.139345 153.297455 452.096 175.988364 61.979927 22.718836 64.3584 41.425455 0.772655 74.565818\" fill=\"#C6302B\" p-id=\"93539\"></path><path d=\"M1144.888041 591.471709c-63.5904 33.149673-393.001891 168.587636-463.131928 205.163055-70.130036 36.552145-109.088582 36.1984-164.491636 9.714036-55.403055-26.4704-405.950836-168.084945-469.099054-198.269673C16.607604 592.9984 0.009495 580.282182 0.009495 568.250182V447.7952s456.378182-99.346618 530.054982-125.779782c73.672145-26.433164 99.234909-27.392 161.931636-4.421818C754.706804 340.563782 1129.611823 408.189673 1191.573132 430.889891l-0.027928 118.751418c0.009309 11.901673-14.289455 24.962327-46.657163 41.8304\" fill=\"#912626\" p-id=\"93540\"></path><path d=\"M1144.864768 471.742836c-63.585745 33.140364-392.987927 168.578327-463.117964 205.149091-70.125382 36.5568-109.083927 36.1984-164.482327 9.714037-55.403055-26.465745-405.941527-168.084945-469.085091-198.255709-63.143564-30.175418-64.465455-50.953309-2.438982-75.250037C107.766877 388.817455 456.387677 252.034327 530.069132 225.605818c73.672145-26.428509 99.230255-27.387345 161.926981-4.417163 62.701382 22.965527 390.139345 153.288145 452.096 175.988363 61.979927 22.714182 64.3584 41.425455 0.772655 74.565818\" fill=\"#C6302B\" p-id=\"93541\"></path><path d=\"M1144.888041 389.366691c-63.5904 33.140364-393.001891 168.587636-463.131928 205.163054-70.130036 36.5568-109.088582 36.1984-164.491636 9.714037-55.403055-26.4704-405.950836-168.0896-469.099054-198.260364C16.607604 390.888727 0.009495 378.167855 0.009495 366.149818v-120.459636s456.378182-99.341964 530.054982-125.770473c73.672145-26.437818 99.234909-27.387345 161.931636-4.421818C754.706804 138.468073 1129.611823 206.093964 1191.573132 228.794182l-0.027928 118.746763c0.009309 11.892364-14.289455 24.953018-46.657163 41.825746\" fill=\"#912626\" p-id=\"93542\"></path><path d=\"M1144.864768 269.637818c-63.585745 33.140364-392.987927 168.587636-463.117964 205.144437-70.125382 36.5568-109.083927 36.1984-164.482327 9.728C461.866077 458.025891 111.32295 316.416 48.184041 286.240582-14.964177 256.069818-16.281414 235.287273 45.740404 210.9952 107.766877 186.707782 456.387677 49.943273 530.069132 23.505455c73.672145-26.433164 99.230255-27.382691 161.926981-4.41251 62.701382 22.970182 390.139345 153.2928 452.096 175.993019 61.979927 22.695564 64.3584 41.411491 0.772655 74.551854\" fill=\"#C6302B\" p-id=\"93543\"></path><path d=\"M741.399459 152.468945l-102.446546 10.635637-22.932945 55.184291-37.040873-61.579637-118.295272-10.630981 88.2688-31.832437-26.484364-48.863418 82.641454 32.321164 77.907782-25.506909-21.057163 50.52509 79.439127 29.7472m-131.495564 267.720146L418.709132 340.8896l273.966545-42.053818-82.771782 121.353309m-265.076363-237.046691c80.872727 0 146.432 25.413818 146.432 56.757527 0 31.353018-65.559273 56.762182-146.432 56.762182s-146.432-25.413818-146.432-56.762182c0-31.343709 65.559273-56.757527 146.432-56.757527\" fill=\"#FFFFFF\" p-id=\"93544\"></path><path d=\"M862.473495 167.554327l162.145746 64.074473-162.006109 64.013964-0.139637-128.093091\" fill=\"#621B1C\" p-id=\"93545\"></path><path d=\"M683.087313 238.512873l179.386182-70.958546 0.139637 128.088437-17.589528 6.879418-161.936291-64.009309\" fill=\"#9A2928\" p-id=\"93546\"></path></svg>', 0, 0, 0, NULL, NULL);
INSERT INTO `b_label_info` VALUES (36, NULL, '黑客', NULL, '<svg t=\"1717330781067\" class=\"icon\" viewBox=\"0 0 1024 1024\" version=\"1.1\" xmlns=\"http://www.w3.org/2000/svg\" p-id=\"96035\" width=\"48\" height=\"48\"><path d=\"M0 512a512 512 0 1 0 1024 0 512 512 0 1 0-1024 0Z\" fill=\"#455870\" p-id=\"96036\"></path><path d=\"M697.344 625.083733a287.744 287.744 0 0 0-7.748267-40.379733 117.998933 117.998933 0 0 0-17.408-38.229333 81.237333 81.237333 0 0 0-28.672-25.770667l21.879467-60.0064h-57.105067c4.778667-14.062933 6.5536-28.228267 5.358934-42.666667 6.621867-1.365333 12.561067-2.7648 17.92-4.232533 18.2272-4.983467 29.115733-10.615467 32.597333-16.896a8.977067 8.977067 0 0 0 1.194667-4.471467c0-10.1376-18.6368-18.944-55.978667-26.4192a218.112 218.112 0 0 0-13.7216-35.703466 138.24 138.24 0 0 0-18.807467-30.378667 26.180267 26.180267 0 0 0-20.2752-9.796267c-5.3248 0-12.8 2.730667-22.357333 8.226134-9.557333 5.495467-17.066667 8.226133-22.357333 8.226133-5.290667 0-12.765867-2.730667-22.357334-8.192-9.557333-5.5296-17.032533-8.260267-22.357333-8.260267a25.941333 25.941333 0 0 0-20.241067 9.898667 136.669867 136.669867 0 0 0-18.773333 30.378667 218.180267 218.180267 0 0 0-13.7216 35.669333c-37.2736 7.441067-55.978667 16.247467-55.978667 26.4192 0 1.672533 0.477867 3.208533 1.399467 4.778667 3.6864 6.178133 14.779733 11.776 33.28 16.7936 5.12 1.399467 10.786133 2.730667 17.066667 4.027733a81.442133 81.442133 0 0 0-0.580267 8.533333c0 11.400533 1.979733 22.766933 5.905067 34.133334h-57.173334l23.995734 58.709333c-9.4208 4.608-17.749333 11.264-24.951467 19.8656-7.202133 8.6016-12.834133 17.851733-16.964267 27.579733-4.096 9.762133-7.406933 20.753067-9.9328 32.938667a253.8496 253.8496 0 0 0-5.051733 33.4848c-0.785067 10.1376-1.160533 20.616533-1.160533 31.470933 0 21.504 6.485333 38.331733 19.456 50.653867 12.970667 12.253867 30.139733 18.397867 51.677866 18.397867h232.994134c21.504 0 38.741333-6.109867 51.712-18.432 12.970667-12.1856 19.456-29.0816 19.456-50.619734a318.225067 318.225067 0 0 0-2.218667-39.7312z m-185.4464 38.570667l-49.834667-131.8912 0.785067 0.682667c28.330667 24.1664 70.075733 24.132267 98.372267-0.068267l0.375466-0.341333-49.698133 131.652266z m75.8784-217.088a7.133867 7.133867 0 0 1-2.048 1.706667 4.608 4.608 0 0 0-2.048 2.798933 38.331733 38.331733 0 0 0-2.048 5.461333 67.447467 67.447467 0 0 1-3.8912 10.001067c-1.058133 2.286933-1.7408 3.754667-2.218667 4.539733a92.501333 92.501333 0 0 1-2.4576 3.754667 12.356267 12.356267 0 0 1-3.1744 3.584c-1.024 0.6144-2.2528 1.467733-3.754666 2.525867a13.1072 13.1072 0 0 1-4.676267 2.116266c-1.536 0.341333-3.413333 0.750933-5.393067 1.092267-2.048 0.375467-4.1984 0.546133-6.485333 0.546133a33.006933 33.006933 0 0 1-15.803733-3.2768 21.333333 21.333333 0 0 1-8.704-8.021333 44.714667 44.714667 0 0 1-3.857067-9.181867 41.642667 41.642667 0 0 0-3.072-7.850666c-1.1264-2.1504-2.696533-3.2768-4.642133-3.2768h-3.208534c-1.9456 0-3.447467 1.092267-4.676266 3.2768a42.325333 42.325333 0 0 0-3.072 7.850666 50.039467 50.039467 0 0 1-3.857067 9.216 20.7872 20.7872 0 0 1-8.704 7.9872 33.553067 33.553067 0 0 1-15.7696 3.2768c-2.286933 0-4.437333-0.2048-6.485333-0.546133s-3.822933-0.750933-5.461334-1.092267a13.1072 13.1072 0 0 1-4.642133-2.116266c-1.536-1.092267-2.833067-1.911467-3.754667-2.525867a11.776 11.776 0 0 1-3.242666-3.584l-2.389334-3.754667a103.287467 103.287467 0 0 1-4.3008-9.1136c-0.238933-0.682667-0.9216-2.525867-1.877333-5.461333a59.972267 59.972267 0 0 0-2.048-5.4272 5.256533 5.256533 0 0 0-2.048-2.798933 6.075733 6.075733 0 0 1-2.013867-1.706667 69.9392 69.9392 0 0 1-1.092266-7.236267 5.973333 5.973333 0 0 1 1.092266-1.6384c1.7408-1.365333 10.274133-2.116267 25.6-2.116266 12.3904 0 27.272533 1.706667 44.509867 5.0176a20.241067 20.241067 0 0 0 5.597867 0.580266 21.6064 21.6064 0 0 0 5.563733-0.580266c17.2032-3.3792 32.085333-5.0176 44.544-5.0176 15.2576 0 23.790933 0.750933 25.6 2.116266 0.341333 0.4096 0.682667 0.9216 1.024 1.6384-0.068267 3.208533-0.4096 5.632-0.6144 7.202134z\" fill=\"#FFFFFF\" p-id=\"96037\"></path></svg>', 0, 0, 0, NULL, NULL);
INSERT INTO `b_label_info` VALUES (37, NULL, '旅行', NULL, '<svg t=\"1717330825632\" class=\"icon\" viewBox=\"0 0 1024 1024\" version=\"1.1\" xmlns=\"http://www.w3.org/2000/svg\" p-id=\"98982\" width=\"48\" height=\"48\"><path d=\"M837.068 845.244c0 75.852-62.06 137.908-137.896 137.908H324.84c-75.844 0-137.908-62.064-137.908-137.908V305.432c0-75.848 62.064-137.904 137.908-137.904h374.332c75.852 0 137.896 62.056 137.896 137.904v539.812z\" fill=\"#AFE0CF\" p-id=\"98983\"></path><path d=\"M699.172 1003.148H324.84c-87.072 0-157.908-70.836-157.908-157.908V305.432c0-87.068 70.836-157.904 157.908-157.904h374.332c87.064 0 157.896 70.832 157.896 157.904v539.812c0 87.068-70.836 157.904-157.896 157.904zM324.84 187.528c-65.012 0-117.908 52.892-117.908 117.904v539.812c0 65.012 52.892 117.908 117.908 117.908h374.332c65.008 0 117.896-52.896 117.896-117.908V305.432c0-65.012-52.888-117.904-117.896-117.904H324.84z\" fill=\"#6E6E96\" p-id=\"98984\"></path><path d=\"M699.164 230.18H324.84c-75.84 0-137.908 51.416-137.908 114.268s62.064 114.268 137.908 114.268h65.18v9.944c0 24.7 24.78 44.916 55.068 44.916h143.172c30.288 0 55.076-20.212 55.076-44.916v-9.944h55.832c75.844 0 137.896-51.416 137.896-114.268 0.004-62.852-62.048-114.268-137.9-114.268z\" fill=\"#6E6E96\" opacity=\".15\" p-id=\"98985\"></path><path d=\"M421.016 396.06H324.84c-75.844 0-137.908-51.42-137.908-114.272 0-62.848 62.064-114.264 137.908-114.264h374.332c75.852 0 137.896 51.412 137.896 114.264 0 62.848-62.06 114.272-137.896 114.272h-84.128\" fill=\"#AFE0CF\" p-id=\"98986\"></path><path d=\"M699.172 416.06h-84.128a20 20 0 1 1 0-40h84.128c65.008 0 117.896-42.288 117.896-94.272 0-51.976-52.888-94.264-117.896-94.264H324.84c-65.012 0-117.908 42.288-117.908 94.264 0 51.984 52.892 94.272 117.908 94.272h96.176a20 20 0 0 1 0 40H324.84c-87.072 0-157.908-60.236-157.908-134.272 0-74.032 70.836-134.264 157.908-134.264h374.332c87.064 0 157.896 60.232 157.896 134.264 0 74.04-70.836 134.272-157.896 134.272z\" fill=\"#6E6E96\" p-id=\"98987\"></path><path d=\"M305.112 40.628h94.312v126.9H305.112z\" fill=\"#E2E2F0\" p-id=\"98988\"></path><path d=\"M399.42 187.528H305.112a20 20 0 0 1-20-20V40.628a20 20 0 0 1 20-20h94.312a20 20 0 0 1 20 20v126.9a20 20 0 0 1-20.004 20z m-74.308-40h54.312V60.628H325.112v86.9z\" fill=\"#6E6E96\" p-id=\"98989\"></path><path d=\"M636.572 40.628h94.324v126.9H636.572z\" fill=\"#E2E2F0\" p-id=\"98990\"></path><path d=\"M730.9 187.528H636.572a20 20 0 0 1-20-20V40.628a20 20 0 0 1 20-20h94.324a20 20 0 0 1 20 20v126.9a19.992 19.992 0 0 1-19.996 20z m-74.328-40h54.324V60.628H656.572v86.9z\" fill=\"#6E6E96\" p-id=\"98991\"></path><path d=\"M623.1 659.092c56.592 226.372-95.116 236.944-178.164 236.944-98.104 0-239.536-15.968-258-215.384v164.584c0 75.844 62.064 137.912 137.908 137.912h374.332c75.844 0 137.896-62.06 137.896-137.912v-298.056c-24.456 505.364-239.068 11.52-213.972 111.912z\" fill=\"#6E6E96\" opacity=\".15\" p-id=\"98992\"></path><path d=\"M421.016 372.764c0-22.228 18.188-40.424 40.42-40.424h105.104c22.224 0 40.416 18.192 40.416 40.424v52.552c0 22.232-18.192 40.42-40.416 40.42h-105.104c-22.228 0-40.42-18.192-40.42-40.42V372.764z\" fill=\"#FFFFFF\" p-id=\"98993\"></path><path d=\"M563.556 428.228H458.452c-22.228 0-40.42-18.192-40.42-40.424v39.076c0 22.232 18.192 40.42 40.42 40.42h105.104c22.228 0 40.416-18.188 40.416-40.42v-39.076c0 22.232-18.184 40.424-40.416 40.424z\" fill=\"#6E6E96\" opacity=\".15\" p-id=\"98994\"></path><path d=\"M305.112 133.564h94.312v33.968H305.112z\" fill=\"#6E6E96\" opacity=\".15\" p-id=\"98995\"></path><path d=\"M636.572 133.564h94.324v33.968H636.572z\" fill=\"#6E6E96\" opacity=\".15\" p-id=\"98996\"></path></svg>', 1, 0, 0, NULL, NULL);
INSERT INTO `b_label_info` VALUES (38, NULL, '美图', NULL, '<svg t=\"1717330845561\" class=\"icon\" viewBox=\"0 0 1024 1024\" version=\"1.1\" xmlns=\"http://www.w3.org/2000/svg\" p-id=\"102703\" width=\"48\" height=\"48\"><path d=\"M512 1024C76.96384 1024 0 947.03616 0 512S76.96384 0 512 0 1024 76.96384 1024 512 947.022507 1024 512 1024z m0-998.4C98.7136 25.6 25.6 98.7136 25.6 512S98.7136 998.4 512 998.4 998.4 925.272747 998.4 512 925.272747 25.6 512 25.6z\" fill=\"#C1D4FC\" p-id=\"102704\"></path><path d=\"M279.893333 785.066667c-101.034667 0-177.793707-5.270187-236.05248-21.149014C30.26944 698.69568 25.6 616.174933 25.6 512 25.6 98.7136 98.7136 25.6 512 25.6c31.402667 0 60.648107 0.49152 88.255147 1.460907C671.45728 83.367253 689.493333 187.665067 689.493333 375.466667c0 348.023467-61.576533 409.6-409.6 409.6z\" fill=\"#4D7DDD\" opacity=\".1\" p-id=\"102705\"></path><path d=\"M505.173333 546.133333v150.186667H286.72a27.306667 27.306667 0 0 1-27.306667-27.306667V382.293333a27.306667 27.306667 0 0 1 27.306667-27.306666h436.906667a27.306667 27.306667 0 0 1 27.306666 27.306666v163.84H505.173333z\" fill=\"#C1D4FC\" p-id=\"102706\"></path><path d=\"M805.546667 791.893333H559.786667c-21.13536 0-27.48416-20.8896-27.497814-38.038186V603.368107C532.302507 586.233173 552.290987 573.44 573.44 573.44h232.106667a23.415467 23.415467 0 0 1 27.306666 27.306667v150.186666c0 17.134933-6.157653 40.96-27.306666 40.96z m-232.106667-13.653333h146.937173l-27.497813-21.640533L636.654933 714.069333a24.098133 24.098133 0 0 0-27.060906 0l-60.156587 45.533867A38.406827 38.406827 0 0 0 573.44 778.24z m246.142293-174.871893a17.667413 17.667413 0 0 0-19.114666-15.52384H570.5728c-10.554027-0.013653-24.439467 4.34176-24.439467 12.9024v150.186666-3.263146l52.783787-39.949654a42.734933 42.734933 0 0 1 48.41472 0l32.07168 24.261974 64.170667-55.896747a41.069227 41.069227 0 0 1 48.128-0.12288L819.2 697.685333v14.718294l-38.37952-30.214827a22.555307 22.555307 0 0 0-26.200747 0l-65.3312 56.9344 14.37696 11.318613L740.502187 778.24H805.546667c10.52672 0 14.035627-15.824213 14.035626-24.384853V603.368107z m-222.549333 66.000213A31.279787 31.279787 0 1 1 628.053333 638.088533a31.143253 31.143253 0 0 1-30.993066 31.279787z m0-50.039467a18.75968 18.75968 0 1 0 18.55488 18.75968 18.67776 18.67776 0 0 0-18.527573-18.773333z m105.02144-258.321066a17.749333 17.749333 0 1 1-17.749333-17.749334 17.749333 17.749333 0 0 1 17.77664 17.73568z m20.753067-16.657067a17.558187 17.558187 0 0 0-17.503574-17.48992H404.138667a13.312 13.312 0 1 1 0-26.610347h301.192533a44.209493 44.209493 0 0 1 44.2368 44.086614V546.133333h-26.733227V344.337067z m-324.949334-76.130987h-9.775786a11.18208 11.18208 0 1 1 0-22.377813h9.857706a11.18208 11.18208 0 1 1 0 22.36416zM436.34688 417.792a88.214187 88.214187 0 1 1-34.310827-6.949547 87.73632 87.73632 0 0 1 34.310827 6.949547z m-95.88736 81.210027a61.576533 61.576533 0 1 0 61.576533-61.549227 61.549227 61.549227 0 0 0-61.576533 61.52192z m15.428267-230.741334h-63.638187a11.18208 11.18208 0 1 1 0-22.377813h63.638187a11.18208 11.18208 0 1 1 0 22.3232zM289.450667 313.5488a13.312 13.312 0 0 1-13.312 13.298347h-40.864427a17.57184 17.57184 0 0 0-17.503573 17.48992v311.159466a17.57184 17.57184 0 0 0 17.503573 17.48992H505.173333v26.610347H235.27424A44.127573 44.127573 0 0 1 191.146667 655.496533V344.337067a44.127573 44.127573 0 0 1 44.127573-44.100267h40.809813a13.312 13.312 0 0 1 13.366614 13.312z\" fill=\"#4D7DDD\" p-id=\"102707\"></path></svg>', 0, 0, 0, NULL, NULL);
INSERT INTO `b_label_info` VALUES (39, NULL, '摄影', NULL, '<svg t=\"1717330871485\" class=\"icon\" viewBox=\"0 0 1024 1024\" version=\"1.1\" xmlns=\"http://www.w3.org/2000/svg\" p-id=\"105030\" width=\"48\" height=\"48\"><path d=\"M669.467223 130.872195c29.71698 0 56.182135 18.681756 66.148402 46.589503l0.296711 0.84917 19.105342 55.72658c3.547536 8.688515 10.496749 15.704663 20.847641 21.051442l5.947192-17.31609c0.085916-0.42958 0.190814-0.859161 0.314693-1.288741 1.713327-5.971169 6.415735-9.882349 12.778521-12.12416 4.688421-1.652386 10.517729-2.470587 17.298107-2.558502 12.874427-0.166837 29.088593 2.327727 45.685386 7.083083 16.595793 4.755356 31.669073 11.226037 42.498497 18.185241 5.704429 3.66542 10.213026 7.445729 13.312999 11.328937 4.207891 5.270853 6.121022 11.07918 4.408695 17.04935a16.062314 16.062314 0 0 1-1.11591 2.84622l-8.083107 29.330357c7.624554 3.646439 14.022306 7.584593 18.533901 11.906373 21.045448 20.154318 31.900847 45.196862 32.118634 74.302439v306.499684c0 59.222166-45.391672 106.466029-106.750751 107.405112l-1.86418 0.014986-64.085416 0.004995C737.562724 860.881296 667.112523 894.126829 588.892909 894.126829c-78.22361 0-148.67481-33.247532-197.975664-86.37365l-9.54368-0.004996-5.071048-0.024975-4.748363-0.039961-17.513896-0.196808-4.558549-0.041959c-22.734798-0.181822-33.069705 0.155848-44.130903 1.569467l-1.183844 0.154849c-11.702572 1.568468-21.13536 4.332769-29.413276 8.780425-1.977069 40.039899-27.345296 68.165432-63.186295 68.165433-23.061479 0-36.793069-2.132917-59.469924-8.380816-28.350314-7.809374-51.146053-25.589011-66.972597-49.523637C72.145545 808.580371 64.626888 785.568843 64.437073 766.596371V367.074529c0-41.649327 30.799922-76.808991 71.967719-82.429503l8.491708-1.088936c16.538849-20.640843 45.400663-33.707083 77.286524-33.707083 21.685822 0 41.973011 6.044098 57.936422 16.402981l95.131098-12.171114c22.801733-2.768297 40.040898-10.643606 51.400804-22.677854 11.358907-12.033249 18.947497-25.402193 28.658014-53.358891 9.710517-27.956699 35.908933-46.792304 65.456078-47.171934H669.469221z m-80.574314 267.258006c-125.044886 0-226.412894 101.296078-226.412893 226.250053 0 124.954974 101.368008 226.251052 226.412893 226.251051 125.043887 0 226.411895-101.296078 226.411895-226.251051 0-21.834677-3.094978-42.948059-8.871337-62.923552a21.119376 21.119376 0 0 1-1.179847-3.077994l-0.152851-0.539473-1.35168-5.010107c-29.96274-89.888219-114.835856-154.698927-214.85618-154.698927z m-480.929342 49.698466v318.049405c0 10.252987 4.879235 25.351243 13.476839 38.353546 10.287953 15.559805 24.587988 26.712913 42.223766 31.571168l1.982064 0.541472c17.866552 4.837276 27.753897 6.273873 45.921155 6.273873 5.370755 0 9.664562-1.952094 12.942361-5.804332 4.133963-4.859255 6.744414-12.729569 6.839321-23.342205l0.002997-0.603411v-347.380761a874.34215 874.34215 0 0 1-37.163707 2.628434c-34.335469 1.688351-63.607883-5.620511-86.224796-20.287189z m480.929342-17.268136c112.203426 0 203.1616 90.893237 203.1616 203.016741S701.096336 836.593015 588.892909 836.593015c-112.203426 0-203.162599-90.893237-203.162599-203.016742s90.959173-203.016741 203.1616-203.016741z m0 19.770692c-101.277097 0-183.37792 82.041881-183.37792 183.246049 0 101.204168 82.100823 183.246049 183.37792 183.246049 101.276098 0 183.376921-82.041881 183.376921-183.246049 0-101.204168-82.100823-183.246049-183.376921-183.246049z m0 40.790166c78.732113 0 142.556784 63.779715 142.556785 142.455883 0 78.675169-63.824671 142.454884-142.556785 142.454884-78.733112 0-142.557783-63.779715-142.557783-142.454884 0-78.676168 63.82567-142.454884 142.557783-142.454884z m-314.013346-30.188519l-0.000999 310.11915c7.35282-2.239813 15.152203-3.874217 23.506045-4.993124l1.928117-0.250756c14.359977-1.815227 26.694931-2.107941 55.485814-1.80224l2.100949 0.027973c-24.716862-40.737218-38.946966-88.53454-38.946966-139.653619 0-20.523957 2.294759-40.513436 6.640515-59.725674l0.004995-63.006471c-0.049951-14.59175-6.122021-25.145444-17.350056-32.212542-9.045167-5.694439-21.006486-8.696507-33.368414-8.501698z m508.694228-149.979535l61.863587 229.276097c1.436597 4.375727 2.7643 8.801405 3.979114 13.273038l18.120304 0.002997c19.785678 0 35.861979 15.867504 36.181666 35.563271l0.004996 0.597416v52.23399c0 19.771692-15.878494 35.837003-35.588246 36.156691l-0.599415 0.004995-14.050279 0.001998c-6.261885 30.987739-17.83858 60.051356-33.739052 86.198822l31.20153-0.004995c37.604277 0 64.544968-27.303337 65.080445-62.845628l0.007992-1.078946V394.716535c0.63538-21.352148-17.620792-43.343672-48.510626-52.318908l-3.293784-0.964058c-28.918759-8.528671-53.134111-17.405003-80.658232-30.480234zM588.892909 534.617912c-54.693588 0-99.030291 44.305733-99.03029 98.95936s44.336702 98.95936 99.03029 98.95936c54.692589 0 99.030291-44.305733 99.030291-98.95936s-44.337701-98.95936-99.030291-98.95936z m278.643887 38.658248l-13.542775-0.001998a270.973378 270.973378 0 0 1 4.838276 51.106092c0 11.485783-0.719298 22.80473-2.112937 33.912882l10.817436-0.002997c8.953257 0 16.231149-7.168 16.398985-16.074303l0.002998-0.315692v-52.23399c0-8.946263-7.172995-16.219161-16.086291-16.386997l-0.315692-0.002997z m-697.878478-59.15623c19.690771 0 34.159641 20.038431 34.159641 43.495524 0 23.457093-14.467871 43.495524-34.159641 43.495524S135.497678 581.072546 135.497678 557.615454c0-23.457093 14.46887-43.495524 34.16064-43.495524z m0 19.770692c-7.114053 0-14.375961 10.057179-14.375961 23.724832 0 13.667653 7.261908 23.724831 14.375961 23.724831 7.113054 0 14.375961-10.057179 14.375961-23.724831 0-13.667653-7.262907-23.724831-14.375961-23.724832z m211.911056-236.766782l-70.132512 8.972238a62.229229 62.229229 0 0 1 2.604457 17.893526c0 42.544453-42.271719 74.140597-91.858295 74.140597-46.75534 0-87.008031-28.092566-91.451691-66.964605-13.394919 6.31783-22.531996 19.83463-22.762771 35.30752l-0.004995 0.601413v9.30791c0 8.831376 6.078064 20.871618 17.345061 30.282427 15.038314 12.559735 37.364511 19.453003 66.739825 18.008414 21.266232-1.045979 42.842162-2.94912 64.728788-5.711422l2.859208-0.366642 0.603411-0.083918c24.827754-3.427653 50.460722 1.308722 70.554099 13.714606l0.652363 0.406603c10.805448 6.801358 19.587871 15.565799 25.946661 25.928679l56.748582-170.176811a127.20178 127.20178 0 0 1-32.572191 8.739465z m216.28878-6.53362c-60.311102 0-110.007571 10.855399-149.325174 32.241515l-31.076652 93.177006c46.621471-38.347551 106.338154-61.374064 171.436581-61.374064 69.112507 0 132.158939 25.954654 179.910307 68.642966l-25.874732-95.886361c-36.983883-24.403169-85.185811-36.800062-145.07033-36.800062zM222.183024 293.346529c-27.838814 0-48.331801 15.317042-48.331801 30.644074 0 15.327032 20.492987 30.645073 48.331801 30.645073 27.838814 0 48.331801-15.318041 48.331801-30.645073s-20.492987-30.644074-48.331801-30.644074z m0 16.935461c12.702595 0 22.99954 6.638517 22.99954 14.82752 0 8.189003-10.296944 14.828519-22.99954 14.828519-12.702595 0-22.99954-6.639516-22.999539-14.828519s10.296944-14.82752 22.999539-14.82752z m572.530888-48.99815l-0.855165 2.296757-1.09493 2.964105 0.010989 0.006994-1.510525 4.129966 0.139863 0.183821 0.124878 0.157846 0.189815 0.233771c1.757284 2.118931 4.793319 4.748363 8.788418 7.493682 9.579645 6.58457 22.953584 12.772527 37.0688 16.817577 14.349986 4.111984 29.053627 6.219926 40.633319 6.067075 4.917198-0.065936 8.944265-0.547465 11.630642-1.313717l0.277728-0.08192 0.4096-0.130872 0.165838-0.056945 2.767298-10.166072c-1.733307 0.139863-3.544539 0.219785-5.428699 0.244761-12.874427 0.166837-29.088593-2.326728-45.685385-7.083083-16.595793-4.755356-31.669073-11.226037-42.498497-18.185241a78.9509 78.9509 0 0 1-5.134986-3.578505z m17.746669-19.712749c-3.536546 0.045955-6.525627 0.346661-8.807399 0.83918l-0.473537 0.105897 0.345662 0.343664c1.497538 1.452581 3.552531 3.074997 6.096047 4.767345l0.483528 0.318688 0.443567 0.287719c9.084129 5.8363 22.40512 11.555715 37.251621 15.81056 14.846501 4.253846 29.177506 6.457694 39.975961 6.317831 3.536546-0.045955 6.524628-0.346661 8.807399-0.839181l0.373635-0.082919 0.097905-0.023976-0.069932-0.07193c-1.516519-1.520515-3.668418-3.238837-6.370778-5.03808l-0.482529-0.31769-0.444566-0.287719c-9.08313-5.8373-22.40512-11.556714-37.251622-15.81056-14.846501-4.254845-29.176507-6.458693-39.974962-6.318829zM669.466224 174.367719H521.662564a26.706919 26.706919 0 0 0-18.229198 7.182985l-9.041171 16.620769-23.514037 67.69689 0.791227-0.26674c35.92192-12.051231 76.77902-18.181245 122.493378-18.493939l1.615422-0.007992 2.079969-0.003996c45.012043 0 85.013979 6.386763 119.872937 19.274177l1.172854 0.436574 1.537499 0.581432-25.705897-74.979778a26.707918 26.707918 0 0 0-24.464109-18.028394l-0.401608-0.008991-0.402607-0.002997z\" fill=\"#C19C78\" p-id=\"105031\"></path></svg>', 0, 0, 0, NULL, NULL);
INSERT INTO `b_label_info` VALUES (40, NULL, '美食', NULL, '<svg t=\"1717330909664\" class=\"icon\" viewBox=\"0 0 1024 1024\" version=\"1.1\" xmlns=\"http://www.w3.org/2000/svg\" p-id=\"114130\" width=\"48\" height=\"48\"><path d=\"M859.597 519.289c-12.842-13.228-28.755-22.175-46.105-26.148v-39.047h4.336c9.128 0 17.709-3.555 24.164-10.009C848.445 437.63 852 429.049 852 419.921v-23.826c0-59.858-23.31-116.133-65.636-158.459C744.038 195.31 687.764 172 627.905 172H396.094c-59.858 0-116.133 23.31-158.458 65.636C195.31 279.961 172 336.237 172 396.095v23.826c0 9.128 3.555 17.709 10.009 24.164s15.036 10.009 24.164 10.009h4.335v39.047c-17.35 3.973-33.264 12.92-46.106 26.147-17.678 18.209-27.414 42.377-27.414 68.051v1.422c0 25.674 9.736 49.841 27.415 68.05a95.216 95.216 0 0 0 18.93 15.112C175.979 682.363 172 694.78 172 707.815v47.78c0 20.486 7.978 39.745 22.462 54.229 14.485 14.486 33.745 22.464 54.23 22.464h526.614c20.486 0 39.746-7.978 54.229-22.463C844.022 795.342 852 776.082 852 755.596v-47.78c0-13.035-3.979-25.452-11.333-35.893a95.206 95.206 0 0 0 18.928-15.111c17.68-18.207 27.417-42.375 27.417-68.051v-1.422c0-25.677-9.738-49.845-27.415-68.05z m-204.096 11.515l-38.51 38.511-38.51-38.511h77.02zM212 396.095c0-49.174 19.149-95.404 53.92-130.175C300.691 231.149 346.921 212 396.094 212h231.811c49.174 0 95.403 19.149 130.175 53.92C792.851 300.691 812 346.921 812 396.095v18H212v-18z m561.491 57.999v36.709H250.509v-36.709h522.982zM812 755.596c0 9.801-3.816 19.016-10.748 25.946-6.93 6.931-16.145 10.747-25.945 10.747H248.693c-9.801 0-19.015-3.816-25.946-10.748A36.53 36.53 0 0 1 213.809 767h426.166c5.522 0 10-4.478 10-10s-4.478-10-10-10H212v-39.185c0-6.016 2.342-11.67 6.596-15.923a22.373 22.373 0 0 1 15.924-6.597h554.96c6.015 0 11.67 2.342 15.924 6.598A22.364 22.364 0 0 1 812 707.815v47.781z m35.012-166.835c0 15.213-5.723 29.484-16.115 40.188-10.234 10.542-23.798 16.348-38.191 16.348H231.296c-14.394 0-27.958-5.806-38.194-16.349-10.391-10.702-16.113-24.975-16.113-40.187v-1.422c0-15.213 5.723-29.485 16.113-40.188 10.236-10.542 23.8-16.348 38.195-16.348h290.617l70.916 70.917c6.455 6.452 15.036 10.006 24.162 10.006s17.707-3.554 24.165-10.008l70.914-70.915h80.635c14.394 0 27.957 5.806 38.192 16.349 10.392 10.701 16.114 24.973 16.114 40.187v1.422z\" fill=\"#f4ea2a\" p-id=\"114131\"></path><path d=\"M364.165 229.604h-1.83c-11.046 0-20 8.954-20 20s8.954 20 20 20h1.83c11.046 0 20-8.954 20-20s-8.954-20-20-20zM432.344 265.602h-1.83c-11.046 0-20 8.954-20 20s8.954 20 20 20h1.83c11.046 0 20-8.954 20-20s-8.954-20-20-20zM574.597 216.104h-2.095c-11.046 0-20 8.954-20 20s8.954 20 20 20h2.095c11.046 0 20-8.954 20-20s-8.954-20-20-20zM760.975 747h-60c-5.522 0-10 4.478-10 10s4.478 10 10 10h60c5.522 0 10-4.478 10-10s-4.478-10-10-10z\" fill=\"#f4ea2a\" p-id=\"114132\"></path></svg>', 0, 0, 0, NULL, NULL);
INSERT INTO `b_label_info` VALUES (41, NULL, 'PHP', NULL, '<svg t=\"1717330934589\" class=\"icon\" viewBox=\"0 0 1024 1024\" version=\"1.1\" xmlns=\"http://www.w3.org/2000/svg\" p-id=\"117295\" width=\"48\" height=\"48\"><path d=\"M161.792 578.56c27.648 0 49.152-4.096 61.44-15.36 12.288-10.24 21.504-27.648 26.624-52.224 4.096-23.552 3.072-37.888-4.096-48.128-7.168-9.216-24.576-13.312-51.2-13.312h-45.056l-24.576 130.048c0-1.024 36.864-1.024 36.864-1.024zM15.36 737.28c-1.024 0-4.096-1.024-4.096-3.072-1.024-1.024-1.024-3.072-1.024-6.144l64.512-342.016c0-3.072 3.072-6.144 6.144-6.144h141.312c45.056 0 76.8 12.288 98.304 36.864 21.504 24.576 27.648 58.368 18.432 100.352-3.072 17.408-9.216 33.792-17.408 48.128-7.168 13.312-18.432 27.648-30.72 39.936-15.36 13.312-31.744 24.576-51.2 30.72-18.432 6.144-40.96 9.216-70.656 9.216H112.64l-17.408 86.016c0 3.072-3.072 6.144-6.144 6.144H15.36z m531.456-91.136c-1.024 0-4.096-1.024-4.096-3.072-1.024-1.024-1.024-3.072-1.024-6.144l29.696-150.528c3.072-13.312 1.024-24.576-1.024-29.696-3.072-3.072-9.216-7.168-30.72-7.168h-52.224l-34.816 189.44c0 3.072-3.072 6.144-6.144 6.144h-72.704c-1.024 0-4.096-1.024-4.096-3.072-1.024-2.048-1.024-4.096-1.024-7.168l64.512-342.016c0-3.072 3.072-6.144 6.144-6.144H512c1.024 0 4.096 1.024 4.096 3.072 1.024 1.024 1.024 3.072 1.024 6.144L501.76 378.88h55.296c44.032 0 70.656 7.168 88.064 23.552 17.408 16.384 23.552 40.96 15.36 76.8l-31.744 160.768c0 3.072-3.072 6.144-6.144 6.144h-75.776c0-1.024 0 0 0 0z m279.552-67.584c29.696 0 51.2-4.096 64.512-15.36s23.552-27.648 27.648-52.224c4.096-23.552 3.072-37.888-4.096-48.128-7.168-9.216-26.624-13.312-52.224-13.312h-46.08l-26.624 130.048c-1.024-1.024 36.864-1.024 36.864-1.024zM674.816 737.28c-1.024 0-4.096-1.024-6.144-3.072-1.024-1.024-1.024-3.072-1.024-6.144l66.56-342.016c0-3.072 3.072-6.144 7.168-6.144h146.432c46.08 0 79.872 12.288 102.4 36.864 21.504 24.576 27.648 58.368 20.48 100.352-3.072 17.408-9.216 33.792-17.408 48.128-7.168 13.312-18.432 27.648-31.744 39.936-15.36 13.312-33.792 24.576-54.272 30.72-18.432 6.144-44.032 9.216-74.752 9.216h-58.368L757.76 731.136c0 3.072-3.072 6.144-7.168 6.144h-75.776z\" fill=\"#7A9ACE\" p-id=\"117296\"></path></svg>', 0, 0, 0, NULL, NULL);
INSERT INTO `b_label_info` VALUES (42, NULL, 'C++', NULL, '<svg t=\"1717330954849\" class=\"icon\" viewBox=\"0 0 1024 1024\" version=\"1.1\" xmlns=\"http://www.w3.org/2000/svg\" p-id=\"120418\" width=\"48\" height=\"48\"><path d=\"M861.0304 303.3088L161.792 705.8432a65.7408 65.7408 0 0 1-7.424-29.8496L153.6 337.3056c0-23.04 12.288-44.544 32.256-56.064L478.72 111.2064c19.968-11.52 44.7488-11.7248 64.6656-0.2048l293.7344 168.8576c10.1888 5.632 18.176 13.824 23.8592 23.4496z\" fill=\"#4C8EC0\" p-id=\"120419\"></path><path d=\"M860.672 708.9664a62.976 62.976 0 0 1-22.528 21.7088l-293.12 169.984c-19.968 11.5712-44.544 11.776-64.6656 0.2048l-293.5296-168.8064a64.4096 64.4096 0 0 1-25.3952-26.7776l348.8256-199.5264 350.3616 203.264z\" fill=\"#004678\" p-id=\"120420\"></path><path d=\"M511.232 267.1616a239.4112 239.4112 0 1 0 0 478.72 239.4112 239.4112 0 0 0 0-478.72z m0 359.5776a120.1152 120.1152 0 0 1-120.2176-120.2176 120.1152 120.1152 0 0 1 120.2176-120.1664 120.1152 120.1152 0 0 1 120.1664 120.1664 120.1152 120.1152 0 0 1-120.1664 120.2176z\" fill=\"#FFFFFF\" p-id=\"120421\"></path><path d=\"M870.4 674.6112c0 12.288-3.5328 24.2176-9.728 34.3552L509.2352 506.5216l352.1024-202.6496c5.4784 9.5744 8.3968 20.3264 8.3968 31.6416L870.4 674.6112z\" fill=\"#005A92\" p-id=\"120422\"></path><path d=\"M750.592 491.8784v27.3408h-27.3408v27.392h-27.3408v-27.392h-27.392v-27.3408h27.392v-27.3408h27.3408v27.3408h27.3408zM851.2512 492.8512v27.3408h-27.3408v27.392h-27.392v-27.392h-27.3408v-27.3408h27.3408v-27.3408h27.392v27.3408h27.3408z\" fill=\"#FFFFFF\" p-id=\"120423\"></path></svg>', 0, 0, 0, NULL, NULL);
INSERT INTO `b_label_info` VALUES (43, NULL, 'SpringBoot', NULL, '<svg t=\"1717330969432\" class=\"icon\" viewBox=\"0 0 1025 1024\" version=\"1.1\" xmlns=\"http://www.w3.org/2000/svg\" p-id=\"121522\" width=\"48\" height=\"48\"><path d=\"M155.731808 143.581954a509.861674 509.861674 0 0 1 721.912932 12.586544 445.222307 445.222307 0 0 0 54.82613-95.785729c63.999373 195.838082 103.252322 360.316472 88.319135 487.675225a510.075006 510.075006 0 0 1-837.538466 354.129865l-4.266625-3.626631-2.133312-2.559975a39.252949 39.252949 0 0 1-6.186606-4.053293l-6.399938-6.613269a212.051257 212.051257 0 0 1-18.986481-17.493162l-2.133312-2.559975A510.714999 510.714999 0 0 1 155.731808 143.581954z m75.945923 685.859951A43.370242 43.370242 0 0 0 170.664996 891.094635l8.106587 7.253262a42.666249 42.666249 0 0 0 57.386105-7.679925 44.372899 44.372899 0 0 0-6.186606-61.226067z m695.459857-716.57965c-52.692817 110.718916-183.46487 194.984757-296.103767 221.437832-106.665622 25.173087-199.251382 4.266625-297.170423 42.666249-223.357813 85.332498-219.091188 350.929897-97.065717 411.089308l7.466594 4.053293s88.319135-17.493162 175.571614-40.532936l7.466594-2.133313c34.986324-9.386575 69.332654-19.626474 97.279047-29.866374 145.491909-53.759474 305.490342-186.664839 358.18316-341.329991-27.306399 160.211765-165.331714 315.090248-303.35703 385.70289C505.595049 802.135506 447.995613 810.668756 327.46346 853.335004c-14.719856 5.333281-26.239743 10.453231-26.239743 10.453231h10.2399c26.879737-3.4133 50.772836-4.906619 50.772836-4.906618 171.731652-8.53325 441.595676 48.426192 567.034448-119.038835 127.998747-170.664996 56.746111-447.995613-2.133313-625.060546z\" fill=\"#82B85E\" p-id=\"121523\"></path><path d=\"M231.677731 829.441905a44.372899 44.372899 0 0 1 6.186607 61.226067A43.370242 43.370242 0 0 1 170.664996 835.841842a42.666249 42.666249 0 0 1 61.226067-6.399937z m695.459857-716.57965c58.879423 178.131589 130.558722 454.182219 2.133313 625.060546C803.832129 905.387828 533.328111 848.428386 362.663116 856.961636c0 0-23.893099 1.70665-50.772837 4.906618H301.65038s11.519887-5.11995 26.239743-10.453231C447.995613 810.668756 505.595049 802.135506 579.40766 764.162544c138.025315-70.612642 276.05063-225.491125 303.35703-385.70289-52.692817 153.811827-213.331244 286.717193-358.18316 341.329991a2424.509593 2424.509593 0 0 1-280.317255 72.532624l-7.253262-4.053294c-122.238803-60.159411-126.505428-325.75681 96.852385-411.089308 97.919041-37.75963 190.718133-16.853168 297.170423-42.666249 112.638897-26.666406 243.624281-110.932247 296.103767-221.651163z\" fill=\"#FFFFFF\" p-id=\"121524\"></path></svg>', 0, 4, 0, NULL, NULL);
INSERT INTO `b_label_info` VALUES (44, NULL, 'Mybatis', NULL, '<svg t=\"1717330985610\" class=\"icon\" viewBox=\"0 0 1024 1024\" version=\"1.1\" xmlns=\"http://www.w3.org/2000/svg\" p-id=\"123050\" width=\"48\" height=\"48\"><path d=\"M869.888 439.686737L929.684211 457.000421l-55.754106 16.303158 38.130527 15.898947-40.151579 7.491369-2.021053-56.993684z\" fill=\"#1A1A1A\" p-id=\"123051\"></path><path d=\"M309.045895 328.771368c-8.043789 101.672421 114.256842 264.690526 235.115789 277.652211 47.238737 5.039158 116.547368-39.397053 112.949895-86.568421-10.078316-131.557053-213.962105-138.186105-348.065684-191.08379z\" fill=\"#2B2222\" p-id=\"123052\"></path><path d=\"M94.315789 705.077895c132.325053 146.917053 370.661053 213.126737 543.973053 142.026105C782.888421 787.779368 876.638316 596.399158 876.705684 444.631579c0-103.949474-104.501895-222.181053-259.22021-173.433263-69.497263 21.881263-122.516211 91.418947-167.302737 154.597052C350.100211 558.457263 261.349053 647.599158 94.315789 705.077895z\" fill=\"#2B2222\" p-id=\"123053\"></path><path d=\"M193.872842 408.212211c8.407579 101.658947 155.338105 243.065263 276.709053 236.557473 47.440842-2.56 108.705684-57.465263 97.562947-103.464421-31.083789-128.269474-233.418105-102.265263-374.272-133.093052z\" fill=\"#2B2222\" p-id=\"123054\"></path><path d=\"M529.421474 328.165053c42.064842 3.759158 116.345263 17.017263 174.955789 20.62821 48.101053 2.977684 104.96 1.697684 144.599579 0.87579 28.712421 49.017263 31.043368 101.281684 22.945684 147.024842-84.547368 14.093474-147.509895 3.705263-216.481684-18.256842-62.746947-19.968-124.995368-50.081684-170.105263-100.244211 20.129684-28.496842 26.960842-34.654316 44.085895-50.027789z\" fill=\"#D40000\" p-id=\"123055\"></path><path d=\"M841.741474 447.932632c-8.326737 21.733053-37.362526 29.978947-64.87579 18.432-27.513263-11.560421-43.061895-38.534737-34.748631-60.254316 8.313263-21.733053 37.362526-29.992421 64.862315-18.432 27.513263 11.533474 43.075368 38.521263 34.762106 60.254316z\" fill=\"#FFFFFF\" p-id=\"123056\"></path><path d=\"M838.871579 445.318737c-6.265263 16.397474-26.947368 23.134316-46.214737 15.050105-19.267368-8.084211-29.776842-27.944421-23.511579-44.328421 6.278737-16.410947 26.987789-23.147789 46.241684-15.077053 19.267368 8.097684 29.776842 27.944421 23.484632 44.355369z\" fill=\"#1A1A1A\" p-id=\"123057\"></path><path d=\"M831.124211 424.272842c-2.223158 5.793684-9.269895 8.286316-15.764211 5.551158-6.480842-2.721684-9.930105-9.633684-7.706947-15.413895 2.223158-5.807158 9.256421-8.299789 15.737263-5.578105 6.494316 2.721684 9.970526 9.633684 7.733895 15.440842z\" fill=\"#FFFFFF\" p-id=\"123058\"></path><path d=\"M838.723368 416.848842c-10.576842-6.561684-19.361684-15.589053-29.884631-22.231579-10.132211-6.817684-20.601263-13.392842-31.959579-17.973895-4.985263-1.697684-9.970526-3.408842-14.928842-5.200842-3.395368 0.808421 0.646737-2.209684 1.347368-3.328l10.293895-11.439158c4.446316 0.579368 8.394105 3.031579 12.732632 4.042106 11.722105 4.810105 22.339368 11.870316 32.943157 18.701473 10.415158 7.006316 20.143158 15.036632 30.652632 21.908211 3.098947 0.565895 2.950737 2.721684 0.538947 4.042105l-11.735579 11.479579z\" fill=\"#1A1A1A\" p-id=\"123059\"></path><path d=\"M538.138947 384.350316c-20.277895-0.714105-43.883789 1.805474-59.418947-16.882527a158.976 158.976 0 0 1-32-53.773473c-1.253053-14.713263-1.266526-32.700632-14.753684-39.68-15.575579-9.862737-33.967158-8.448-50.997895-11.156211-32.134737-2.142316-62.760421 11.210105-92.698947 23.013053-14.228211 5.564632-31.717053 4.540632-42.132211-9.647158-8.124632-9.269895-13.069474-21.288421-18.189474-32.794947-7.504842-7.437474-7.949474-19.024842 2.182737-22.096842l18.714948-12.638316c9.593263 20.655158 16.882526 47.211789 37.106526 56.414316 20.749474 5.254737 39.989895-8.811789 59.553684-14.497685 27.755789-10.509474 57.384421-12.085895 86.029474-6.534737 16.491789 1.738105 36.459789 8.852211 41.445053 30.046316 1.468632 18.364632 4.783158 36.257684 15.804631 50.054737 10.442105 18.647579 25.734737 36.446316 45.824 37.200842 6.642526 2.613895 28.402526-2.694737 26.004211 3.664842l-22.487579 19.30779z\" fill=\"#D40000\" p-id=\"123060\"></path><path d=\"M524.544 374.649263c-14.686316-9.552842-32.687158-17.973895-38.938947-35.557052-4.136421-11.776-8.394105-23.673263-10.469053-35.988211-1.805474-16.734316 0.053895-33.603368-0.781474-50.364632 2.694737-10.711579-4.271158-20.560842-15.036631-22.541473-14.093474-4.082526-29.022316-3.557053-43.493053-5.537684-13.298526-0.727579-29.143579 2.155789-39.706947-7.814737-8.003368-9.485474-11.452632-21.786947-15.427369-33.293474-0.902737-6.022737 7.114105-6.251789 10.590316-9.808842L390.359579 161.684211c1.360842 11.210105 6.305684 21.652211 10.711579 31.932631 6.076632 10.778947 19.402105 11.304421 30.396631 11.021474 17.246316 1.091368 34.748632 1.428211 51.631158 5.241263 10.630737 2.479158 23.174737 9.970526 22.056421 22.325895 0.714105 25.559579-3.530105 52.062316 5.281685 76.705684 3.637895 11.021474 7.235368 22.918737 17.003789 30.248421 7.356632 6.790737 16.586105 11.237053 24.764632 16.653474-6.696421 5.834105-15.252211 10.226526-22.662737 15.535158l-4.985263 3.301052z\" fill=\"#D40000\" p-id=\"123061\"></path></svg>', 0, 2, 0, NULL, NULL);
INSERT INTO `b_label_info` VALUES (45, NULL, 'SpringCode', NULL, '<svg t=\"1717331021810\" class=\"icon\" viewBox=\"0 0 1140 1024\" version=\"1.1\" xmlns=\"http://www.w3.org/2000/svg\" p-id=\"126613\" width=\"48\" height=\"48\"><path d=\"M1126.133275 450.542218l-224.659865-388.973301C881.969177 27.783807 834.069894 0 795.061428 0H345.741697c-38.89733 0-86.907749 27.783807-106.411981 61.457782l-224.659866 388.973301c-19.5598 33.78511-19.5598 88.908183 0 122.915563l224.659866 388.973302c19.504233 33.78511 67.403516 61.457782 106.411981 61.457782h449.319731c38.89733 0 86.852181-27.783807 106.411982-61.457782l224.659865-388.973302c19.5598-33.729542 19.5598-88.963751 0-122.804428zM521.835468 215.157803a46.34339 46.34339 0 1 1 92.631213 0v276.782288a46.34339 46.34339 0 1 1-92.631213 0zM568.178858 805.73041A314.01259 314.01259 0 0 1 380.86043 239.885392a41.175602 41.175602 0 1 1 49.177339 66.069893 231.661385 231.661385 0 1 0 369.746906 185.984806A232.772737 232.772737 0 0 0 705.708704 305.62188a41.175602 41.175602 0 0 1 48.67723-66.681138 314.068157 314.068157 0 0 1-186.207076 566.789668z\" fill=\"#68BD45\" p-id=\"126614\"></path></svg>', 0, 0, 0, NULL, NULL);
INSERT INTO `b_label_info` VALUES (46, NULL, '购物', NULL, '<svg t=\"1717331037016\" class=\"icon\" viewBox=\"0 0 1024 1024\" version=\"1.1\" xmlns=\"http://www.w3.org/2000/svg\" p-id=\"127893\" width=\"48\" height=\"48\"><path d=\"M659 441.4c-9.2 0-18.5-3.2-26-9.6-16.9-14.4-18.9-39.7-4.5-56.6 20.9-24.5 32-54 32-85.2 0-76-66.9-137.8-149.1-137.8-82.2 0-149.1 61.8-149.1 137.8 0 31.2 11.1 60.7 32 85.2 14.4 16.9 12.4 42.2-4.5 56.6-16.9 14.4-42.2 12.4-56.6-4.5-33-38.7-51.2-87.4-51.2-137.3 0-120.3 102.9-218.1 229.4-218.1S740.8 169.7 740.8 290c0 49.8-18.2 98.6-51.2 137.3-8 9.3-19.2 14.1-30.6 14.1z\" fill=\"#FFD401\" p-id=\"127894\"></path><path d=\"M780.6 951.9H242c-49.5 0-90.3-36.6-92.5-83.1l-21.2-437.6c-2.4-49.6 39.7-91 92.5-91h580.9c52.8 0 94.9 41.4 92.5 91L873 868.8c-2.1 46.4-42.8 83.1-92.4 83.1z\" fill=\"#8BC03C\" p-id=\"127895\"></path><path d=\"M470.7 762.8h-97.8c-21.1 0-38.2-17.1-38.2-38.2v-81.2c0-78.4 63.5-141.9 141.9-141.9h97.8c21.1 0 38.2 17.1 38.2 38.2v81.2c0 78.3-63.5 141.9-141.9 141.9z\" fill=\"#FFFFFF\" p-id=\"127896\"></path><path d=\"M553.5 818.4h-97.8c-21.1 0-38.2-17.1-38.2-38.2V699c0-78.4 63.5-141.9 141.9-141.9h97.8c21.1 0 38.2 17.1 38.2 38.2v81.2c0 78.3-63.5 141.9-141.9 141.9z\" fill=\"#22802A\" p-id=\"127897\"></path><path d=\"M333.1 427.3c14.4 16.9 39.7 18.9 56.6 4.5 16.9-14.4 18.9-39.7 4.5-56.6-9.2-10.8-16.4-22.7-21.7-35.1h-84.3c7.8 31.7 23.1 61.7 44.9 87.2zM650.2 340.1c-5.3 12.5-12.5 24.3-21.7 35.1-14.4 16.9-12.4 42.2 4.5 56.6 7.6 6.4 16.8 9.6 26 9.6 11.4 0 22.6-4.8 30.6-14.1 21.8-25.5 37.1-55.5 44.9-87.2h-84.3z\" fill=\"#8BA000\" p-id=\"127898\"></path></svg>', 1, 0, 0, NULL, NULL);
INSERT INTO `b_label_info` VALUES (47, NULL, '春', NULL, '<svg t=\"1717331058323\" class=\"icon\" viewBox=\"0 0 1024 1024\" version=\"1.1\" xmlns=\"http://www.w3.org/2000/svg\" p-id=\"129991\" data-spm-anchor-id=\"a313x.search_index.0.i219.52783a81xDL4bD\" width=\"48\" height=\"48\"><path d=\"M499.3 851.63L226.37 578.7c-36.84-36.84-36.84-96.56 0-133.4L499.3 172.37c36.84-36.84 96.56-36.84 133.4 0L905.63 445.3c36.84 36.84 36.84 96.56 0 133.4L632.7 851.63c-36.84 36.84-96.56 36.84-133.4 0z\" fill=\"#71d789\" p-id=\"129992\" data-spm-anchor-id=\"a313x.search_index.0.i218.52783a81xDL4bD\" class=\"\"></path><path d=\"M458 891.26c-13.8 0-27.26-2.62-40-7.79-13.2-5.35-25.03-13.21-35.18-23.36L109.88 587.18c-10.15-10.15-18-21.98-23.36-35.18-5.17-12.74-7.79-26.2-7.79-40s2.62-27.26 7.79-40c5.35-13.2 13.21-25.03 23.36-35.18l272.94-272.94c10.15-10.15 21.98-18 35.18-23.36 12.74-5.17 26.2-7.79 40-7.79s27.26 2.62 40 7.79c13.2 5.35 25.03 13.21 35.18 23.36l68.23 68.23c4.69 4.69 4.69 12.28 0 16.97-4.69 4.69-12.28 4.69-16.97 0l-68.23-68.23c-7.86-7.86-17.02-13.95-27.23-18.09-9.86-4-20.29-6.03-30.99-6.03s-21.12 2.03-30.99 6.03c-10.2 4.14-19.36 10.22-27.23 18.09L126.85 453.79c-7.86 7.86-13.95 17.02-18.09 27.23-4 9.86-6.03 20.29-6.03 30.99s2.03 21.12 6.03 30.99c4.14 10.21 10.22 19.37 18.09 27.23l272.94 272.94c7.86 7.86 17.02 13.95 27.23 18.09 9.86 4 20.29 6.03 30.99 6.03s21.12-2.03 30.99-6.03c10.2-4.14 19.37-10.22 27.23-18.09l272.94-272.94c7.86-7.86 13.95-17.02 18.09-27.23 4-9.86 6.03-20.29 6.03-30.99s-2.03-21.12-6.03-30.99c-4.14-10.2-10.22-19.36-18.09-27.23l-68.23-68.23c-4.69-4.69-4.69-12.28 0-16.97 4.69-4.69 12.28-4.69 16.97 0l68.23 68.23c10.15 10.15 18 21.98 23.36 35.18 5.17 12.74 7.79 26.2 7.79 40s-2.62 27.26-7.79 40c-5.35 13.2-13.21 25.03-23.36 35.18L533.18 860.12c-10.15 10.15-21.98 18-35.18 23.36-12.74 5.16-26.2 7.78-40 7.78z\" fill=\"#0b7b26\" p-id=\"129993\" data-spm-anchor-id=\"a313x.search_index.0.i220.52783a81xDL4bD\" class=\"\"></path><path d=\"M456.44 261.64c-0.57 0-1.15 0.05-1.73 0.14-5.69 0.96-9.52 6.36-8.56 12.05 0.13 0.75 6.62 40.83 2.36 89.12H322.45c-5.77 0-10.45 4.68-10.45 10.45s4.68 10.45 10.45 10.45h123.53c-1.48 9.65-3.45 19.43-6.03 29.14h-50.73c-5.77 0-10.45 4.68-10.45 10.45s4.68 10.45 10.45 10.45h44.14c-2.92 7.82-6.35 15.44-10.29 22.8H295.75c-5.77 0-10.45 4.68-10.45 10.45s4.68 10.45 10.45 10.45h113.91c-3.13 4.09-6.43 8.06-10.03 11.81-27.38 28.45-65.93 42.8-114.96 42.8-11.99 0-24.6-0.86-37.83-2.58-0.47-0.06-0.94-0.1-1.4-0.1-5.15 0-9.62 3.86-10.3 9.12-0.74 5.72 3.3 10.96 9.02 11.7 14.17 1.84 27.71 2.76 40.64 2.76 54.83-0.01 98.44-16.5 129.94-49.27 7.81-8.12 14.53-16.96 20.4-26.24h14.65c0.36 0.35 0.6 0.78 1.02 1.08 27.7 19.5 109.85 74.38 155.98 74.38 8.88 0 16.43-2.04 22-6.74 4.14-3.51 4.66-9.72 1.15-13.87a9.837 9.837 0 0 0-7.51-3.47c-2.24 0-4.5 0.76-6.36 2.32-2.16 1.83-5.45 2.68-9.69 2.68-23.08 0-74.16-25.31-122-56.38h131.87c5.77 0 10.45-4.68 10.45-10.45s-4.68-10.45-10.45-10.45H446.51c3.49-7.46 6.56-15.08 9.17-22.8h67.08c5.77 0 10.45-4.68 10.45-10.45s-4.68-10.45-10.45-10.45h-61.03c2.41-9.78 4.25-19.54 5.63-29.14h122.18c5.77 0 10.45-4.68 10.45-10.45s-4.68-10.45-10.45-10.45H469.72c4.1-49.21-2.42-89.43-2.96-92.62-0.87-5.1-5.28-8.69-10.32-8.69z\" fill=\"#0b7b26\" p-id=\"129994\" data-spm-anchor-id=\"a313x.search_index.0.i226.52783a81xDL4bD\" class=\"selected\"></path><path d=\"M481.48 530.27h-63.12c-40.57 0-73.58 33.01-73.58 73.58s33.01 73.57 73.58 73.57h63.12c40.57 0 73.57-33.01 73.57-73.57 0.01-40.58-33-73.58-73.57-73.58z m-63.12 126.25c-29.05 0-52.68-23.63-52.68-52.67s23.63-52.68 52.68-52.68h63.12c29.05 0 52.67 23.63 52.67 52.68s-23.63 52.67-52.67 52.67h-63.12z\" fill=\"#0b7b26\" p-id=\"129995\" data-spm-anchor-id=\"a313x.search_index.0.i228.52783a81xDL4bD\" class=\"selected\"></path><path d=\"M510.19 596.01H384.8c-5.77 0-10.45 4.68-10.45 10.45s4.68 10.45 10.45 10.45h125.39c5.77 0 10.45-4.68 10.45-10.45s-4.68-10.45-10.45-10.45z\" fill=\"#0b7b26\" p-id=\"129996\" data-spm-anchor-id=\"a313x.search_index.0.i227.52783a81xDL4bD\" class=\"selected\"></path></svg>', 0, 0, 0, NULL, NULL);
INSERT INTO `b_label_info` VALUES (48, NULL, '夏', NULL, '<svg t=\"1717331124968\" class=\"icon\" viewBox=\"0 0 1024 1024\" version=\"1.1\" xmlns=\"http://www.w3.org/2000/svg\" p-id=\"133733\" width=\"48\" height=\"48\"><path d=\"M721.2 318.8v-55c34.1 0 61.8-27.7 61.8-61.8s-27.7-61.8-61.8-61.8c-34.1 0-61.8 27.7-61.8 61.8h-55c0-64.4 52.4-116.8 116.8-116.8S838 137.6 838 202s-52.4 116.8-116.8 116.8z\" fill=\"#FF5A06\" p-id=\"133734\"></path><path d=\"M373.6 174.5L319.2 64.6H161.3v55h123.8l27.2 55H199.1v254.2c0 142.3 108.7 259.6 247.3 273.4v202.3H305.6v55h336.6v-55H501.4V702.2C640 688.3 748.7 571 748.7 428.7V174.5H373.6z m320.1 254.2c0 121.2-98.6 219.8-219.8 219.8S254 550 254 428.7V229.5h85.5l109.8 221.8 49.2-24.4-97.7-197.4h292.9v199.2z\" fill=\"#1E59E4\" p-id=\"133735\"></path></svg>', 0, 0, 0, NULL, NULL);
INSERT INTO `b_label_info` VALUES (49, NULL, '秋', NULL, '<svg t=\"1717331154519\" class=\"icon\" viewBox=\"0 0 1024 1024\" version=\"1.1\" xmlns=\"http://www.w3.org/2000/svg\" p-id=\"137079\" width=\"48\" height=\"48\"><path d=\"M768.41921375 801.95633187c-65.67685594-53.10043687-122.96943187-135.54585188-122.96943187-135.54585187l-9.78165938 8.38428c18.16593844 25.15283812 74.061135 96.41921437 118.77729281 132.75109125 46.11353719 36.33187781 90.82969406 68.47161562 104.80349344 79.65065531 13.97379938 12.57641906 8.38427906 19.56331875 4.19213906 27.94759782-6.98689969 16.76855906 13.97379938 25.15283812 20.96069907 5.58952031 1.39738031-4.19214 2.79475969-9.78165938 2.79475968-16.76855906-2.79475969-26.55021844-53.10043687-48.90829687-118.77729281-102.00873375z\" fill=\"#d81e06\" p-id=\"137080\"></path><path d=\"M637.06550187 656.62882063c54.49781625-12.57641906 86.637555 8.38427906 117.37991344 13.97379937-2.79475969-8.38427906-15.37117875-20.96069906-8.38428-23.75545875 27.94759781-9.78165938 58.68995625-4.19214 88.03493438-6.98689969-5.58951938-5.58951938-22.35807844-12.57641906-16.76855907-18.16593844 11.17903969-11.17903969 30.74235844-8.38427906 46.11353719-12.57641906-2.79475969-2.79475969-9.78165938-8.38427906-6.98689969-9.78165937 20.96069906-9.78165938 44.71615688-11.17903969 67.07423625-16.76855906-20.96069906-4.19214-41.92139719-4.19214-61.48471593-12.57641907-2.79475969-1.39738031 2.79475969-6.98689969 4.19213906-9.78165937-32.13973781-4.19214-62.88209625 1.39738031-95.02183406-13.97379938 1.39738031-26.55021844 53.10043687-54.49781625 78.25327593-81.04803469-4.19214 0-13.97379938 2.79475969-12.57642-1.39738031 13.97379938-23.75545875 33.53711812-44.71615688 50.30567719-67.07423531-29.34497813 2.79475969-57.29257688 4.19214-86.637555 6.98689969 11.17903969-32.13973781 23.75545875-62.88209625 34.93449844-95.02183407-9.78165938 0-29.34497813 11.17903969-30.74235844 1.39737938-5.58951938-22.35807844 9.78165938-44.71615688 15.37117875-67.07423531-5.58951938 0-15.37117875 6.98689969-16.76855812 1.39737937-2.79475969-12.57641906 4.19214-26.55021844 6.98689875-39.1266375-4.19214 0-9.78165938 1.39738031-11.17903875-2.79475969-2.79475969-20.96069906 1.39738031-43.3187775 1.39737937-65.67685593-15.37117875 20.96069906-27.94759781 44.71615688-46.11353625 62.88209625-2.79475969 2.79475969-6.98689969-4.19214-9.78166031-6.98689969-8.38427906 13.97379938-11.17903969 33.53711812-25.15283813 43.3187775-5.58951938 4.19214-9.78165938-8.38427906-13.97379937-13.97379938-12.57641906 29.34497813-19.56331875 60.08733656-37.72925719 86.63755501-4.19214 5.58951938-11.17903969-11.17903969-12.57641906-13.97379938-2.79475969-2.79475969-4.19214 92.22707438-37.72925813 124.36681219-47.51091656-61.48471594-12.57641906-100.61135344-18.16593843-152.31441094-2.79475969 4.19214-8.38427906 15.37117875-9.78165938 9.78165938-6.98689969-19.56331875-4.19214-40.52401781-6.98689969-60.08733563-2.79475969 4.19214-8.38427906 16.76855906-9.78165937 12.57641906-5.58951938-13.97379938-2.79475969-30.74235844-4.19214-46.11353718-5.58951938 13.97379938-9.78165938 30.74235844-18.16593844 43.3187775-1.39738031 2.79475969-5.58951938-2.79475969-8.38428-5.58952032-1.39738031 15.37117875-15.37117875 46.11353719-32.13973781 46.11353719-12.57641906 0-30.74235844-46.11353719-46.11353719-68.47161562-1.39738031 1.39738031-4.19214 6.98689969-5.58951937 5.58952031-8.38427906-9.78165938-12.57641906-23.75545875-19.56331875-34.93449844-6.98689969 11.17903969-15.37117875 33.53711812-22.35807844 32.13973781-13.97379938-1.39738031-36.33187781-22.35807844-53.10043688-33.53711718 0 2.79475969 1.39738031 11.17903969-1.39738031 11.17903875-11.17903969-4.19214-19.56331875-13.97379938-29.34497812-20.96069813 0 2.79475969 2.79475969 11.17903969 0 9.78165938-18.16593844-8.38427906-33.53711812-22.35807844-48.90829688-33.53711813 4.19214 19.56331875 11.17903969 39.1266375 12.57641906 58.68995625 0 2.79475969-5.58951938-2.79475969-8.38427906-4.19214 2.79475969 11.17903969 8.38427906 23.75545875 8.38427906 34.93449844 0 4.19214-6.98689969-1.39738031-9.78165937-2.79476063 4.19214 20.96069906 13.97379938 48.90829687 11.17903969 62.88209625-1.39738031 6.98689969-25.15283812 5.58951938-39.1266375 8.38428 8.38427906 11.17903969 19.56331875 19.56331875 25.15283812 32.13973782 1.39738031 2.79475969-4.19214 2.79475969-6.98689969 4.19214 15.37117875 22.35807844 51.70305656 55.89519656 47.5109175 68.47161562-5.58951938 15.37117875-40.52401781 16.76855906-54.49781718 12.57641906 0 2.79475969 4.19214 8.38427906 1.39738031 9.78165938-15.37117875 4.19214-30.74235844 1.39738031-46.11353719 1.39737937 13.97379938 6.98689969 30.74235844 11.17903969 40.52401781 20.96069907 4.19214 4.19214-9.78165938 2.79475969-15.37117968 4.19214 18.16593844 9.78165938 37.72925719 15.37117875 53.10043687 29.34497812 4.19214 2.79475969-8.38427906 4.19214-12.57641906 5.58951938 44.71615688 25.15283812 93.62445375 6.98689969 134.14847156 74.061135-41.92139719 18.16593844-129.95633156-13.97379938-129.95633156-11.17903875 1.39738031 2.79475969 13.97379938 15.37117875 8.38427906 16.76855906-30.74235844 6.98689969-62.88209625 1.39738031-93.62445375 2.79475969 2.79475969 5.58951938 12.57641906 13.97379938 6.98689969 18.16593843-13.97379938 8.38427906-33.53711812 4.19214-48.90829688 6.98689969 1.39738031 4.19214 5.58951938 9.78165938 2.79475969 11.17903969-23.75545875 9.78165938-50.30567719 12.57641906-76.85589562 19.56331875 20.96069906 8.38427906 41.92139719 12.57641906 61.48471593 23.75545875 2.79475969 1.39738031-1.39738031 6.98689969-2.79475968 11.17903875 12.57641906 2.79475969 27.94759781 0 39.1266375 8.38428 4.19214 2.79475969-5.58951938 9.78165938-6.98689875 15.37117875 22.35807844 4.19214 48.90829687-2.79475969 68.47161562 11.17903969 8.38427906 5.58951938-8.38427906 19.56331875-12.57642 29.34497812 33.53711812 1.39738031 67.07423625 1.39738031 102.00873375 2.79475969-12.57641906 26.55021844-25.15283812 51.70305656-39.1266375 78.253275 26.55021844-6.98689969 53.10043687-18.16593844 81.04803563-22.35807844 4.19214 0-2.79475969 8.38427906-2.79476063 11.17903875 34.9344975-13.97379938 79.65065531-51.70305656 104.80349344-41.92139719 2.79475969 36.33187781-15.37117875 61.48471594-23.75545781 93.62445375 4.19214 0 11.17903969-4.19214 11.17903875-1.39737937 0 20.96069906-8.38427906 40.52401781-12.57641907 61.48471594 13.97379938-18.16593844 23.75545875-39.1266375 40.52401782-55.89519657 2.79475969-2.79475969 4.19214 6.98689969 6.98689968 9.78165938 9.78165938-12.57641906 13.97379938-32.13973781 27.94759782-37.72925719 6.98689969-2.79475969 6.98689969 15.37117875 9.78165937 22.35807844 12.57641906-26.55021844 19.56331875-57.29257688 39.1266375-78.253275 5.58951938-5.58951938 12.57641906 11.17903969 18.16593938 16.76855906 6.98689969-30.74235844-1.39738031-68.47161562 32.13973781-114.58515281-1.39738031-1.39738031-2.79475969-4.19214-4.19214-5.58952031l-120.17467219 138.34061156 110.39301281-143.93013094-321.39738-16.76855906 315.80786063 5.58951937-125.7641925-178.86462843-148.12227094-13.97379938h139.73799188l-117.37991344-194.23580719 127.16157281 185.85152813 53.10043594-114.58515281-41.92139719 128.55895218 127.16157188 177.46724813 132.75109218-345.15283781L632.87336281 638.46288219l199.8253275-53.10043688L632.87336281 649.64192094s1.39738031 2.79475969 4.19213907 6.98689968z\" fill=\"#d81e06\" p-id=\"137081\"></path></svg>', 0, 0, 0, NULL, NULL);
INSERT INTO `b_label_info` VALUES (50, NULL, '冬', NULL, '<svg t=\"1717331167840\" class=\"icon\" viewBox=\"0 0 1024 1024\" version=\"1.1\" xmlns=\"http://www.w3.org/2000/svg\" p-id=\"140130\" width=\"48\" height=\"48\"><path d=\"M5.1 831.1h253.5c2.8 0 5.1-2.3 5.1-5.1s-2.3-5.1-5.1-5.1H5.1C2.3 821 0 823.2 0 826s2.3 5.1 5.1 5.1z m314.3 0h55.8c2.8 0 5.1-2.3 5.1-5.1s-2.3-5.1-5.1-5.1h-55.8c-2.8 0-5.1 2.3-5.1 5.1s2.3 5.1 5.1 5.1z m106.4 0h253.5c2.8 0 5.1-2.3 5.1-5.1s-2.3-5.1-5.1-5.1H425.8c-2.8 0-5.1 2.3-5.1 5.1s2.3 5.1 5.1 5.1z m314.3 0h55.8c2.8 0 5.1-2.3 5.1-5.1s-2.3-5.1-5.1-5.1h-55.8c-2.8 0-5.1 2.3-5.1 5.1s2.3 5.1 5.1 5.1z m106.5 0H1019c2.8 0 5.1-2.3 5.1-5.1s-2.3-5.1-5.1-5.1H846.6c-2.8 0-5.1 2.3-5.1 5.1s2.3 5.1 5.1 5.1z\" fill=\"#050531\" p-id=\"140131\"></path><path d=\"M124.2 866.6a387.8 43.1 0 1 0 775.6 0 387.8 43.1 0 1 0-775.6 0Z\" fill=\"#CFCFD5\" p-id=\"140132\"></path><path d=\"M626 638.5a136.9 22.8 0 1 0 273.8 0 136.9 22.8 0 1 0-273.8 0Z\" fill=\"#CFCFD5\" p-id=\"140133\"></path><path d=\"M813.6 435.7a45.6 7.6 0 1 0 91.2 0 45.6 7.6 0 1 0-91.2 0Z\" fill=\"#CFCFD5\" p-id=\"140134\"></path><path d=\"M418.9 405.8c-25.4-24.4-41.2-58.8-41.2-96.8 0-74.2 60.1-134.3 134.3-134.3S646.3 234.8 646.3 309c0 38-15.8 72.4-41.2 96.8 89.6 36.7 152.7 124.8 152.7 227.6 0 135.8-110.1 245.9-245.9 245.9S266.1 769.2 266.1 633.4c0-102.8 63.2-190.9 152.8-227.6z\" fill=\"#E0E0F9\" p-id=\"140135\"></path><path d=\"M486.6 177c8.2-1.6 16.7-2.4 25.3-2.4 74.2 0 134.3 60.1 134.3 134.3 0 38-15.8 72.4-41.2 96.8 89.6 36.7 152.7 124.8 152.7 227.6 0 135.8-110.1 245.9-245.9 245.9-8.6 0-17-0.4-25.3-1.3C610.4 865.2 707 760.6 707 633.3c0-102.8-63.1-190.9-152.7-227.6 25.4-24.4 41.2-58.8 41.2-96.8 0.1-65.5-46.8-120-108.9-131.9z\" fill=\"#B8B8EB\" p-id=\"140136\"></path><path d=\"M371.7 402.1c2 1.9 2.1 5.2 0.1 7.2s-5.2 2.1-7.2 0.1c-27.1-26.1-42.8-62.1-42.8-100.5 0-52.3 29-99.4 74.5-123.4 2.5-1.3 5.5-0.4 6.8 2.1 1.3 2.5 0.4 5.5-2.1 6.8-42 22.4-69 66.1-69 114.6 0 35.6 14.5 68.9 39.7 93.1z m33.8-209.8c-2.5 1.2-5.6 0.1-6.8-2.4-1.2-2.5-0.1-5.6 2.4-6.8 1.6-0.8 3.2-1.5 4.8-2.2 2.6-1.1 5.6 0.1 6.7 2.6s-0.1 5.6-2.6 6.7c-1.6 0.8-3 1.4-4.5 2.1z m52-12.5c-2.8 0.1-5.1-2.1-5.2-4.9-0.1-2.8 2.1-5.1 4.9-5.2 1.4 0 2.7-0.1 4.1-0.1 77 0 139.4 62.4 139.4 139.4 0 13.5-1.9 26.7-5.7 39.4-0.8 2.7-3.6 4.2-6.3 3.4-2.7-0.8-4.2-3.6-3.4-6.3 3.5-11.8 5.2-24.1 5.2-36.6 0-71.4-57.9-129.3-129.3-129.3-1.2 0.1-2.4 0.1-3.7 0.2z m126.3 170.5c0.9-2.7 3.8-4.1 6.4-3.2 2.7 0.9 4.1 3.8 3.2 6.4-0.6 1.7-1.2 3.3-1.8 5-1 2.6-3.9 3.9-6.5 2.9s-3.9-3.9-2.9-6.5c0.6-1.6 1.1-3.1 1.6-4.6z m124.2 237c0.5 2.8-1.3 5.4-4.1 5.9s-5.4-1.3-5.9-4.1c-14.9-80.5-70.2-147.9-145.5-178.7-3.4-1.4-4.2-5.8-1.6-8.3 1.9-1.9 3.8-3.8 5.6-5.7 1.9-2.1 5.1-2.2 7.2-0.3s2.2 5.1 0.3 7.2c-0.2 0.3-0.5 0.5-0.7 0.8C638.2 437.4 692.9 506 708 587.3z m-9.1 6.8c-0.5-2.8 1.4-5.4 4.2-5.8 2.8-0.5 5.4 1.4 5.8 4.2 0.3 1.7 0.5 3.4 0.8 5.1 0.4 2.8-1.5 5.3-4.3 5.7-2.8 0.4-5.3-1.5-5.7-4.3-0.3-1.6-0.5-3.2-0.8-4.9z m2.7 54.1c0.2-2.8 2.6-4.9 5.4-4.8 2.8 0.2 4.9 2.6 4.8 5.4-5.4 88.8-57.4 167.4-135.5 207.7-2.5 1.3-5.5 0.3-6.8-2.2-1.3-2.5-0.3-5.5 2.2-6.8 74.8-38.7 124.8-114.1 129.9-199.3zM567.2 849.7c2.5-1.2 5.6-0.2 6.8 2.3 1.2 2.5 0.2 5.6-2.3 6.8-1.5 0.8-3.1 1.5-4.7 2.2-2.5 1.2-5.6 0.1-6.7-2.5-1.2-2.5-0.1-5.6 2.5-6.7 1.4-0.6 2.9-1.3 4.4-2.1z m-51 18.2c2.7-0.6 5.5 1.1 6.1 3.8 0.6 2.7-1.1 5.5-3.8 6.1-18.6 4.3-37.7 6.5-57 6.5-69.1 0-133.7-28-180.7-76.7-1.9-2-1.9-5.2 0.1-7.2s5.2-1.9 7.2 0.1c45.1 46.7 107.1 73.6 173.4 73.6 18.5 0.1 36.8-2 54.7-6.2zM284.6 797c1.9 2.1 1.8 5.3-0.3 7.2-2.1 1.9-5.3 1.8-7.2-0.3-1.2-1.3-2.3-2.5-3.5-3.8-1.9-2.1-1.7-5.3 0.4-7.2s5.3-1.7 7.2 0.4c1.1 1.2 2.3 2.4 3.4 3.7z m-32.5-44.3c1.4 2.4 0.5 5.5-1.9 6.9s-5.5 0.5-6.9-1.9c-21.5-37.6-32.9-80.1-32.9-124.3 0-43.8 11.2-85.9 32.3-123.2 1.4-2.4 4.5-3.3 6.9-1.9 2.4 1.4 3.3 4.5 1.9 6.9-20.2 35.8-31 76.2-31 118.3 0 42.3 11 83.2 31.6 119.2z m1.9-241.9c-1.4 2.4-4.5 3.2-6.9 1.8s-3.2-4.5-1.8-6.9c0.9-1.5 1.8-3 2.7-4.4 1.5-2.4 4.6-3.1 7-1.6 2.4 1.5 3.1 4.6 1.6 7-0.9 1.3-1.7 2.7-2.6 4.1z m32.8-43.3c-1.9 2-5.1 2.1-7.2 0.2-2-1.9-2.1-5.1-0.2-7.2 24.4-25.7 54-45.9 86.8-59.4 2.6-1.1 5.6 0.2 6.6 2.8 1.1 2.6-0.2 5.6-2.8 6.6-31.4 12.9-59.7 32.3-83.2 57z\" fill=\"#050531\" p-id=\"140137\"></path><path d=\"M176.7 450.9l-23-21.7c-2-1.9-2.1-5.1-0.2-7.2 1.9-2 5.1-2.1 7.2-0.2l26.9 25.4v-41.9c0-2.8 2.3-5.1 5.1-5.1s5.1 2.3 5.1 5.1V456c0 0.3 0 0.5-0.1 0.7l49.2 46.3c2 1.9 2.1 5.1 0.2 7.2-1.9 2-5.1 2.1-7.2 0.2l-52.4-49.3H142c-2.8 0-5.1-2.3-5.1-5.1s2.3-5.1 5.1-5.1h34.7zM738.3 453.4l23-21.7c2-1.9 2.1-5.1 0.2-7.2-1.9-2-5.1-2.1-7.2-0.2l-26.9 25.4v-41.9c0-2.8-2.3-5.1-5.1-5.1s-5.1 2.3-5.1 5.1v50.7c0 0.3 0 0.5 0.1 0.7l-49.2 46.3c-2 1.9-2.1 5.1-0.2 7.2 1.9 2 5.1 2.1 7.2 0.2l52.4-49.3H773c2.8 0 5.1-2.3 5.1-5.1s-2.3-5.1-5.1-5.1h-34.7z\" fill=\"#050531\" p-id=\"140138\"></path><path d=\"M509.5 296.3m-15.2 0a15.2 15.2 0 1 0 30.4 0 15.2 15.2 0 1 0-30.4 0Z\" fill=\"#050531\" p-id=\"140139\"></path><path d=\"M570.3 296.3m-15.2 0a15.2 15.2 0 1 0 30.4 0 15.2 15.2 0 1 0-30.4 0Z\" fill=\"#050531\" p-id=\"140140\"></path></svg>', 0, 0, 0, NULL, NULL);
INSERT INTO `b_label_info` VALUES (51, 10, '心得', '心得体会', '<svg t=\"1717331178682\" class=\"icon\" viewBox=\"0 0 1024 1024\" version=\"1.1\" xmlns=\"http://www.w3.org/2000/svg\" p-id=\"141457\" width=\"48\" height=\"48\"><path d=\"M96.896 160.928l0.896 0.032c30.272 1.696 61.664 5.664 94.208 11.872V480l64-64 64 64V208.352c51.008 20.16 104.32 49.6 160 88.48V768a32 32 0 0 0 63.776 3.744L544 768V296.8c73.408-51.2 142.72-86.144 208-104.8 61.696-17.632 119.776-27.968 174.208-31.04a32 32 0 0 1 33.728 30.144L960 192.96v575.808a32 32 0 0 1-30.56 31.968c-59.136 2.656-118.304 13.12-177.44 31.296-69.344 21.344-138.656 53.344-208 96h-64c-69.344-42.656-138.656-74.656-208-96a710.592 710.592 0 0 0-177.44-31.296 32 32 0 0 1-30.56-32V192.96a32 32 0 0 1 28.256-31.808l4.64-0.192z\" fill=\"#B19C4A\" p-id=\"141458\"></path></svg>', 0, 0, 0, '2023-03-14 09:55:55', 1);
INSERT INTO `b_label_info` VALUES (52, 7, '摘抄', '摘抄', '<svg t=\"1717682938009\" class=\"icon\" viewBox=\"0 0 1024 1024\" version=\"1.1\" xmlns=\"http://www.w3.org/2000/svg\" p-id=\"143811\" width=\"48\" height=\"48\"><path d=\"M827.733333 955.733333H230.4A128 128 0 0 1 102.4 827.733333V230.4A128 128 0 0 1 230.4 102.4h597.333333A128 128 0 0 1 955.733333 230.4v597.333333A128 128 0 0 1 827.733333 955.733333zM230.4 187.733333A42.666667 42.666667 0 0 0 187.733333 230.4v597.333333a42.666667 42.666667 0 0 0 42.666667 42.666667h597.333333a42.666667 42.666667 0 0 0 42.666667-42.666667V230.4a42.666667 42.666667 0 0 0-42.666667-42.666667H230.4z\" fill=\"#323232\" p-id=\"143812\"></path><path d=\"M328.226133 300.100267a28.740267 28.740267 0 0 1 57.514667 0V395.946667h38.365867a28.740267 28.740267 0 1 1 0 57.480533H385.706667v86.6304l24.3712-8.9088a28.7744 28.7744 0 0 1 19.797333 53.998933l-44.168533 16.145067v152.405333c0 10.001067-5.12 18.773333-12.834134 23.9616a28.706133 28.706133 0 0 1-24.064 4.846934l-43.451733-10.683734a28.740267 28.740267 0 1 1 13.755733-55.808l9.079467 2.218667v-95.880533l-24.3712 8.9088a28.7744 28.7744 0 0 1-19.797333-53.9648l44.168533-16.1792v-107.690667H289.8944a28.740267 28.740267 0 1 1 0-57.480533h38.331733V300.100267zM605.5936 285.730133a28.740267 28.740267 0 0 0-10.5472 39.253334l0.375467 0.682666h-97.826134a28.740267 28.740267 0 1 0 0 57.480534h262.0416a28.740267 28.740267 0 1 0 0-57.480534H661.845333l-16.964266-29.422933a28.7744 28.7744 0 0 0-39.287467-10.513067z\" fill=\"#F26A52\" p-id=\"143813\"></path><path d=\"M623.8208 514.116267a20.753067 20.753067 0 0 0-20.7872 20.7872v7.9872h-22.357333a19.182933 19.182933 0 1 0 0 38.331733h22.357333v12.765867h-22.357333a19.182933 19.182933 0 0 0-19.182934 19.1488v86.254933c0 10.581333 8.6016 19.182933 19.182934 19.182933h86.289066a19.319467 19.319467 0 0 0 3.1744-0.273066 19.182933 19.182933 0 0 0 22.357334-18.909867v-86.2208a19.182933 19.182933 0 0 0-22.357334-18.909867 19.319467 19.319467 0 0 0-3.1744-0.273066h-22.391466v-12.765867h22.391466a19.182933 19.182933 0 1 0 0-38.331733h-22.391466v-7.9872a20.753067 20.753067 0 0 0-20.753067-20.7872z m30.378667 166.126933v-47.9232h-54.340267v47.9232h54.306133z\" fill=\"#F26A52\" p-id=\"143814\"></path><path d=\"M538.2144 397.550933a28.7744 28.7744 0 0 0-10.513067 39.2192l5.9392 10.274134h-36.0448a28.7744 28.7744 0 0 0-28.7744 28.740266v277.9136a28.740267 28.740267 0 0 0 57.514667 0v-249.173333h204.526933v213.026133l-6.485333-1.604266a28.535467 28.535467 0 0 0-34.679467 20.957866c-3.754667 15.428267 5.7344 31.0272 21.128534 34.850134l42.154666 10.5472a28.535467 28.535467 0 0 0 35.396267-30.1056V475.818667a28.740267 28.740267 0 0 0-28.740267-28.740267H724.309333l5.905067-10.274133a28.7744 28.7744 0 0 0-10.513067-39.253334 28.5696 28.5696 0 0 0-39.1168 10.478934l-19.114666 33.314133a28.740267 28.740267 0 0 0-2.56 5.7344H599.04a28.945067 28.945067 0 0 0-2.491733-5.7344l-19.182934-33.314133a28.5696 28.5696 0 0 0-39.1168-10.4448z\" fill=\"#F26A52\" p-id=\"143815\"></path></svg>', 0, 1, 0, '2023-03-16 09:01:14', 1);
INSERT INTO `b_label_info` VALUES (53, 1, 'mysql', 'mysql', '<svg t=\"1717682967324\" class=\"icon\" viewBox=\"0 0 1024 1024\" version=\"1.1\" xmlns=\"http://www.w3.org/2000/svg\" p-id=\"144946\" width=\"48\" height=\"48\"><path d=\"M293.04 243.664c-9.744 0-16.336 1.184-23.2 2.784v1.12h1.184c4.576 8.784 12.368 15.2 18.128 23.072 4.56 9.04 8.464 18.08 13.024 27.04l1.184-1.248c7.968-5.6 11.856-14.56 11.856-28.16-3.392-3.968-3.888-7.952-6.768-11.84-3.392-5.664-10.672-8.464-15.232-12.944l-0.176 0.176z m577.28 491.264c-45.312-1.184-80.432 3.376-109.808 15.888-8.48 3.392-22.016 3.392-23.2 14.128 4.656 4.48 5.328 11.84 9.312 18.08 6.768 11.344 18.464 26.48 29.28 34.416 11.872 9.312 23.712 18.272 36.16 26.224 22.016 13.52 46.992 21.552 68.576 35.168 12.272 7.952 24.8 18.016 37.248 26.464 6.176 4.224 10.16 11.84 18.112 14.56v-1.696c-3.888-5.088-5.088-12.432-8.896-18.096-5.664-5.664-11.344-10.752-16.928-16.32a272.656 272.656 0 0 0-58.832-57.088c-18.112-12.336-57.744-29.6-65.184-50.304l-1.104-1.184c12.368-1.104 27.088-5.584 38.944-8.96 19.216-5.072 36.832-3.968 56.72-8.96 8.96-2.288 18.032-5.072 27.088-7.952v-5.072c-10.16-10.144-17.776-23.936-28.272-33.392a750.64 750.64 0 0 0-93.456-69.6c-17.776-11.328-40.304-18.592-59.008-28.24-6.768-3.376-18.112-5.072-22.016-10.72-10.16-12.352-16.08-28.768-23.28-43.472a1494.768 1494.768 0 0 1-46.304-98.336c-10.16-22.16-16.336-44.224-28.784-64.512-58.4-96.144-121.648-154.4-218.928-211.392-20.896-11.84-45.952-16.912-72.464-23.168-14.128-0.672-28.272-1.696-42.32-2.288-9.312-3.968-18.288-14.72-26.24-19.856-32.16-20.304-115.472-64.272-139.184-6.096-15.232 36.704 22.608 72.88 35.728 91.488 9.744 12.944 22.016 27.728 28.8 42.272 3.968 9.808 5.056 19.872 9.04 30.112 8.976 24.848 17.52 52.592 29.376 75.84 6.176 11.84 12.96 24.272 20.912 34.928 4.576 6.16 12.368 9.04 14.144 19.2-7.968 11.488-8.48 28.224-13.04 42.256-20.32 64-12.368 143.152 16.416 190.24 9.056 14.048 30.656 45.168 59.52 33.248 25.392-10.16 19.808-42.288 27.088-70.608 1.696-6.768 0.592-11.248 4.064-15.808v1.264c7.952 15.904 15.92 31.04 23.2 46.928 17.44 27.728 47.904 56.48 73.392 75.68 13.536 10.144 24.288 27.728 41.232 33.984v-1.68h-1.28c-3.632-4.912-8.464-7.28-13.024-11.248a291.344 291.344 0 0 1-29.632-33.824 740.48 740.48 0 0 1-63.248-102.992c-9.312-17.76-17.104-36.864-24.544-54.368-3.392-6.768-3.392-16.912-9.056-20.288-8.48 12.336-20.912 23.088-27.088 38.304-10.752 24.352-11.856 54.272-15.92 85.392-2.288 0.592-1.184 0-2.288 1.184-18.112-4.4-24.288-23.168-31.072-38.88-16.928-40.176-19.712-104.688-5.072-150.944 3.968-11.84 20.912-49.2 14.128-60.544-3.552-10.72-14.72-16.896-20.896-25.6a209.44 209.44 0 0 1-20.32-36.112c-13.552-31.632-20.32-66.64-35.04-98.256-6.784-14.624-18.64-29.92-28.288-43.376-10.752-15.216-22.608-25.952-31.152-43.968-2.8-6.176-6.768-16.4-2.288-23.168 1.184-4.56 3.552-6.336 7.952-7.616 7.456-6.08 28.368 1.872 35.728 5.248 20.912 8.448 38.528 16.4 56.048 28.24 7.952 5.584 16.512 16.32 26.672 19.104h11.84c18.128 3.984 38.528 1.184 55.456 6.176 30.048 9.632 57.136 23.68 81.44 38.896a503.568 503.568 0 0 1 176.512 193.28c6.768 13.024 9.728 24.96 15.904 38.48 11.856 27.904 26.496 56.064 38.528 83.04 11.84 26.624 23.28 53.76 40.288 75.84 8.48 11.84 42.496 18.016 57.744 24.176 11.248 5.072 28.784 9.728 38.944 15.904 19.456 11.84 38.432 25.36 56.704 38.4 9.312 6.4 37.504 20.528 39.2 31.952z\" fill=\"#3480AF\" p-id=\"144947\"></path></svg>', 0, 0, 0, '2023-04-14 14:26:52', 1);
INSERT INTO `b_label_info` VALUES (55, 2, 'JavaScrpit', 'JavaScrpit', '<svg t=\"1717682997587\" class=\"icon\" viewBox=\"0 0 1024 1024\" version=\"1.1\" xmlns=\"http://www.w3.org/2000/svg\" p-id=\"146924\" width=\"48\" height=\"48\"><path d=\"M145.621959 0c-44.79888 0-79.998 36.81188-79.998 81.61076v860.77848c0 44.79888 35.19912 81.61076 79.998 81.61076h732.781681a81.969151 81.969151 0 0 0 81.61076-81.61076V324.80468L657.60916 0h-511.987201z\" fill=\"#25B39E\" p-id=\"146925\"></path><path d=\"M657.60916 0v233.59416c0 25.59936 17.61236 92.79768 97.61036 92.79768h204.79488L657.60916 0z\" fill=\"#FFFFFF\" p-id=\"146926\"></path><path d=\"M339.511512 648.687783c1.61276 0 2.867128 0.614385 3.814305 1.868753s1.663958 2.764731 2.175945 4.582286 0.81918 3.737507 0.972776 5.862253 0.230394 4.044699 0.230394 5.862253v5.708658c2.099148 3.302317 4.78708 6.886228 8.0126 10.726132s6.963026 7.449414 11.18692 10.802929 8.78058 6.143846 13.721257 8.396591 10.214145 3.379116 15.820405 3.379115c5.99025 0 11.622109-1.407965 16.869978-4.198295s10.137347-6.527837 14.617235-11.18692 8.652584-9.98375 12.441289-15.974001 7.21902-12.236494 10.265343-18.661933 5.759856-12.876478 8.089398-19.276318 4.377491-12.287693 6.067048-17.689158 3.046324-10.086148 4.044699-14.02845 1.689558-6.681433 2.099147-8.166195c2.611135-10.598135 4.81268-20.761081 6.681433-30.437639s2.764731-19.250719 2.764731-28.645684c0-2.303942-0.691183-3.60951-2.099147-3.891103l-37.19587 10.495738a19.916302 19.916302 0 0 1-5.094273 0.895977c-3.788705 0-6.963026-1.20317-9.522962-3.60951s-3.814305-5.862253-3.814304-10.342141 0.435189-8.114997 1.279968-10.879728 2.201545-4.81268 4.044698-6.220645 4.275093-2.329542 7.270219-2.76473 6.655834-0.665583 10.956526-0.665584h2.099147c2.40634-0.511987 5.478263-1.20317 9.21577-2.099147s7.935802-1.919952 12.518087-3.071923l14.694033-3.737507 15.743606-4.044699c5.299068-1.356766 10.572536-2.662333 15.820404-3.967901a1637.283868 1637.283868 0 0 1 23.474614-5.631859l5.99025-1.279968a22.01545 22.01545 0 0 1 3.737506-0.588785c1.20317 0 2.303942 0.358391 3.302318 1.049574s1.817555 1.58716 2.483138 2.636734a14.514837 14.514837 0 0 1 2.175945 7.423814c0 2.995125-1.151971 5.60626-3.455913 7.807805s-5.19667 4.121497-8.703783 5.785455-7.321417 3.097523-11.468513 4.351891l-11.622109 3.532712c-3.60951 1.100772-6.681433 2.175946-9.21577 3.225519s-3.967901 2.227144-4.275093 3.532712c-0.511987 4.40309-0.998375 9.292568-1.510362 14.694033s-1.049574 10.546936-1.638359 15.462013c-0.204795 1.20317-0.614385 4.095898-1.279968 8.703783s-1.561561 10.188545-2.687933 16.79318-2.559936 13.823654-4.198295 21.682658-3.532712 15.564411-5.631859 23.167421c-2.303942 8.294193-5.324667 17.151571-9.062174 26.546536s-8.191795 18.559536-13.337266 27.442514-10.982125 17.228369-17.484363 24.984975-13.542061 14.130847-21.145072 19.122722a58.929727 58.929727 0 0 1-23.19302 8.089398 61.336067 61.336067 0 0 1-9.369366 0.742381c-5.19667 0-10.828529-0.691183-16.869978-2.099147s-12.134097-3.404715-18.226744-5.99025-11.9805-5.734257-17.61236-9.369366-10.674933-7.705407-15.078023-12.159696-7.910202-9.21577-10.495738-14.335642-3.891103-10.444539-3.891102-16.050799c0-4.300692 0.435189-8.0126 1.279968-11.110122s2.227144-5.60626 4.121497-7.500612 4.428689-3.302317 7.57741-4.198295 7.014225-1.484763 11.519712-1.484763zM536.0122 682.120547c3.404715 1.20317 6.886228 2.611135 10.418939 4.198295s7.21902 3.123122 11.033324 4.582285 7.782205 2.662333 11.929302 3.686308 8.575786 1.510362 13.286068 1.510363c5.503862 0 11.058924-0.639984 16.639584-1.945552s11.084523-3.225519 16.434789-5.785455 10.470138-5.657459 15.385216-9.292568 9.394965-7.884603 13.490862-12.671683c4.505487-5.094273 8.242994-9.727757 11.238119-13.874853s5.427064-7.859004 7.270219-11.110122 3.174321-6.041449 3.9679-8.396591 1.20317-4.377491 1.20317-6.067048c0-2.508737-0.844779-4.684683-2.559936-6.527837s-3.967901-3.379116-6.835029-4.582285-6.067048-2.073548-9.676558-2.636734-7.347016-0.81918-11.238119-0.81918c-2.303942 0-4.991875 0.076798-8.089398 0.230395l-9.446164 0.460788-9.292567 0.38399a150.39624 150.39624 0 0 1-7.807805 0.153597c-3.302317 0-6.732632-0.38399-10.265343-1.126372s-7.014225-1.971151-10.41894-3.686308-6.553436-3.9935-9.446164-6.911827a35.403915 35.403915 0 0 1-8.831779-15.743607 24.626584 24.626584 0 0 1-0.742381-6.143846c0-2.40634 0.179196-4.684683 0.537586-6.835029s0.767981-4.223894 1.279968-6.220645c1.407965-4.991875 3.737507-10.086148 6.963026-15.231619s7.142221-10.162946 11.698908-15.078023 9.497363-9.650959 14.847628-14.258844 10.854129-8.934177 16.511588-12.978875 11.21252-7.705407 16.639584-10.956526 10.521337-6.01585 15.231619-8.319792c6.604635-3.19992 13.337267-5.529462 20.172295-6.963026s13.61886-2.175946 20.325892-2.175946c3.9935 0 7.807805 0.537587 11.391716 1.587161s6.78383 2.585535 9.522961 4.582285 4.915077 4.479888 6.527837 7.423815 2.40634 6.323042 2.40634 10.137346c0 2.201545-0.255994 4.044699-0.742381 5.555061s-1.151971 2.713532-1.945552 3.686308-1.663958 1.61276-2.636734 2.022349-1.919952 0.588785-2.918327 0.588786c-2.687933 0-5.171071-0.307192-7.423814-0.895978s-4.428689-1.279968-6.527837-2.022349-4.198295-1.459164-6.297443-2.099148-4.40309-0.972776-6.911827-0.972775c-7.398215 0-14.924427 1.484763-22.578635 4.428689s-15.103622 6.707032-22.348242 11.238119-14.105247 9.625359-20.556286 15.231619-12.236494 11.058924-17.330766 16.357991c-2.687933 2.79033-4.684683 5.60626-5.913453 8.39659s-1.868753 5.452664-1.868753 7.961401c0 3.711907 1.61276 6.604635 4.81268 8.703783s7.091023 3.148721 11.698907 3.148721c2.687933 0 5.759856-0.051199 9.138972-0.153596a796.140096 796.140096 0 0 0 20.761081-0.844779c3.353516-0.153596 6.323042-0.230394 8.934176-0.230394 6.195045 0 12.159696 0.870378 17.842754 2.636734s10.751731 4.326292 15.154822 7.731006 7.935802 7.628609 10.572535 12.671684 3.967901 10.828529 3.967901 17.330766c0 3.9935-0.588785 8.038199-1.791955 12.159696s-2.841529 8.217395-4.940677 12.364491-4.505487 8.242994-7.19342 12.287693-5.555061 8.0126-8.550186 11.929302c-5.401465 6.988625-11.417315 13.59326-18.073148 19.788305s-13.721257 11.59651-21.22187 16.204395-15.436414 8.242994-23.781805 10.956526-16.921177 4.044699-25.727357 4.044699a74.724532 74.724532 0 0 1-23.551411-4.121497c-3.891103-1.356766-7.577411-3.046324-11.033324-5.094273s-6.476638-4.377491-9.062174-6.963026-4.684683-5.478263-6.220644-8.626984-2.329542-6.579036-2.329542-10.265343c0-1.791955 0.281593-3.532712 0.819179-5.171071s1.868753-2.559936 4.070299-2.559936z\" fill=\"#FFFFFF\" p-id=\"146927\"></path></svg>', 0, 2, 0, '2023-04-14 14:50:42', 1);
INSERT INTO `b_label_info` VALUES (56, 3, '测试', '测试', '<svg t=\"1717683024894\" class=\"icon\" viewBox=\"0 0 1024 1024\" version=\"1.1\" xmlns=\"http://www.w3.org/2000/svg\" p-id=\"3419\" width=\"48\" height=\"48\"><path d=\"M620.086613 89.429333H197.522773a52.824747 52.824747 0 0 0-52.824746 52.824747v739.498667a52.824747 52.824747 0 0 0 52.824746 52.824746h633.862827a52.81792 52.81792 0 0 0 52.81792-52.824746V353.532587L620.086613 89.429333z\" fill=\"#84BF56\" p-id=\"3420\"></path><path d=\"M672.91136 353.532587l211.285333 211.278506V353.532587h-211.285333z\" fill=\"#5F9922\" p-id=\"3421\"></path><path d=\"M540.85632 459.175253c-87.517867 0-158.460587 70.956373-158.460587 158.467414 0 24.378027 5.67296 47.383893 15.50336 68.000426l-60.962133 60.770987c-10.328747 10.335573-9.591467 26.309973 0.709973 36.631893l37.341867 37.35552c10.356053 10.356053 25.593173 11.803307 35.88096 1.508694l61.617493-61.44c20.739413 9.905493 43.840853 15.633067 68.34176 15.633066 87.524693 0 158.467413-70.94272 158.467414-158.460586S628.374187 459.175253 540.85632 459.175253z m0 264.103254c-58.34752 0-105.642667-47.295147-105.642667-105.63584S482.515627 512 540.85632 512c58.34752 0 105.642667 47.3088 105.642667 105.642667-0.006827 58.340693-47.301973 105.63584-105.642667 105.63584z\" fill=\"#FFFFFF\" p-id=\"3422\"></path><path d=\"M620.086613 89.429333v211.285334a52.824747 52.824747 0 0 0 52.824747 52.824746h211.285333L620.086613 89.429333z\" fill=\"#434854\" p-id=\"3423\"></path></svg>', 0, 2, 0, '2023-05-04 21:06:00', 1);
INSERT INTO `b_label_info` VALUES (57, 19, '英语', '学习音乐', '<svg t=\"1729135678499\" class=\"icon\" viewBox=\"0 0 1079 1024\" version=\"1.1\" xmlns=\"http://www.w3.org/2000/svg\" p-id=\"7035\" width=\"48\" height=\"48\"><path d=\"M698.540252 68.643549C887.393266 68.643549 1037.27747 209.028141 1037.27747 378.539356c0 76.148276-27.649579 140.421238-72.258883 187.060483-16.680888 23.53871 11.000559 105.2988-11.008526 122.841696-22.291109 17.550862-72.290751-11.577355-110.912208-17.542895h-27.965064c-33.299634 11.583728-77.623727 17.542895-116.592537 17.542895-188.851421 0-338.737218-140.384591-338.737219-309.902179 0-169.511215 149.885798-309.895806 338.737219-309.895807z m11.346318 156.33572h-30.625976v54.086611h-111.00303v151.039391h29.705014v-17.834481h81.299609v104.745904h30.625976v-104.71563h81.645368v17.834481h29.67952v-151.045764h-111.324888v-54.110512z m-30.625976 83.182961V383.203121h-81.29961v-75.040891h81.29961z m112.242664 0V383.203121h-81.616688v-75.040891h81.645368-0.02868z m0 0\" fill=\"#5C84FF\" p-id=\"7036\"></path><path d=\"M362.752343 648.34622l-38.116362 100.282902h77.215827l-37.823184-103.469623z\" fill=\"#FBBA1A\" p-id=\"7037\"></path><path d=\"M698.540252 724.613998c-208.284041 0-374.149648-152.771373-378.529796-338.855128-165.239416 25.398161-288.944713 157.199321-288.944713 313.133514 0 78.018881 28.301263 143.819876 74.078501 191.639801 17.053734 24.1171-11.277803 107.892791 11.273023 125.86908 22.842411 17.974696 74.04026-11.86416 113.619327-17.976289h28.656583c34.144115 11.86416 79.530979 17.976289 119.457399 17.976289 184.138261 0 332.110434-130.2556 346.014094-292.524993-8.675846 0.487568-17.260871 0.737726-25.624418 0.737726z m-264.645971 111.799709l-21.480088-58.947953h-98.37087l-21.448222 58.947953h-37.169906l88.108038-228.753939h39.746369l88.07617 228.753939h-37.461491z\" fill=\"#FBBA1A\" p-id=\"7038\"></path><path d=\"M74.080094 311.078079l10.326568 42.478982 31.623419 30.198956-42.478982 10.324974-30.205329 31.618639-10.3186-42.478982L1.402157 353.021692l42.478982-10.326567 30.175055-31.617046h0.0239zM190.874988 54.763789l56.602527 33.137111 65.501443-2.670471-33.105244 56.602526 2.668878 65.533311-56.600933-33.137112-65.533311 2.670472 33.105245-56.602527-2.668879-65.501443 0.030274-0.031867zM1062.804694 1.604514l-10.618153 64.698389 25.954244 60.224235-64.699983-10.624526-60.224234 25.960617 10.624525-64.698389-25.960617-60.224235 64.699983 10.618152 60.224235-25.954243z m0 0\" fill=\"#FCC736\" p-id=\"7039\"></path></svg>', 1, 1, 0, '2024-10-17 11:28:10', 1);

-- ----------------------------
-- Table structure for b_news_system_info
-- ----------------------------
DROP TABLE IF EXISTS `b_news_system_info`;
CREATE TABLE `b_news_system_info`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'id',
  `news_title` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_cs_0900_ai_ci NOT NULL COMMENT '消息标题',
  `news_content` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_cs_0900_ai_ci NULL COMMENT '消息内容',
  `is_details` bigint NOT NULL COMMENT '是否有详情（1：无，2：有）',
  `state` bigint NOT NULL DEFAULT 0 COMMENT '状态（0：正常，1：已过期）',
  `type` bigint NOT NULL COMMENT '消息类型（1：系统，2：用户）',
  `is_mass_air` bigint NOT NULL COMMENT '是否群发（1：群发，2：指定人发）',
  `uid` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_cs_0900_ai_ci NULL DEFAULT NULL COMMENT '接收人id',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `lapse_time` datetime NULL DEFAULT NULL COMMENT '失效时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_cs_0900_ai_ci COMMENT = '系统消息表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of b_news_system_info
-- ----------------------------
INSERT INTO `b_news_system_info` VALUES (1, '你的文章《关于“栖息地”》已审核通过！', '你的文章《关于“栖息地”》已审核通过！', 1, 0, 2, 2, '1636168030713155584', '2024-11-25 14:45:27', NULL);

-- ----------------------------
-- Table structure for b_news_user_info
-- ----------------------------
DROP TABLE IF EXISTS `b_news_user_info`;
CREATE TABLE `b_news_user_info`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'id',
  `news_content` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL COMMENT '消息内容',
  `type` int NOT NULL COMMENT ' 消息类型（1：评论消息，2：点赞消息，3：关注消息，4系统消息）',
  `prn_news_works_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_cs_0900_ai_ci NULL DEFAULT NULL COMMENT '生产消息的目标者id',
  `works_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_cs_0900_ai_ci NOT NULL COMMENT '目标消息id',
  `works_parent_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_cs_0900_ai_ci NOT NULL COMMENT '消息目标父级id',
  `works_parent_uid` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_cs_0900_ai_ci NULL DEFAULT NULL COMMENT '消息目标父级Uid',
  `works_type` int NOT NULL COMMENT '目标消息类型（1：文章，2：评论，3：圈子，4：活动）',
  `works_content` varchar(400) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '目标消息内容',
  `state` int NOT NULL DEFAULT 0 COMMENT '状态（0：正常，1：已删除）',
  `been_read` int NOT NULL DEFAULT 0 COMMENT '是否已读（0：未读，1：已读）',
  `sender_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_cs_0900_ai_ci NULL DEFAULT NULL COMMENT '发送者id',
  `recipient_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_cs_0900_ai_ci NULL DEFAULT NULL COMMENT '接收者id',
  `create_time` datetime NOT NULL,
  `update_time` datetime NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_cs_0900_ai_ci COMMENT = '用户消息表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of b_news_user_info
-- ----------------------------

-- ----------------------------
-- Table structure for b_news_user_record
-- ----------------------------
DROP TABLE IF EXISTS `b_news_user_record`;
CREATE TABLE `b_news_user_record`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'id',
  `uid` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_cs_0900_ai_ci NOT NULL COMMENT 'uid',
  `news_id` bigint NULL DEFAULT NULL COMMENT '消息id',
  `type` int NOT NULL COMMENT '类型（1：文章，2：点赞，3：关注）',
  `target_id` bigint NULL DEFAULT NULL COMMENT '目标id',
  `target_uid` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_cs_0900_ai_ci NOT NULL COMMENT '目标uid',
  `been_read` int NOT NULL DEFAULT 0 COMMENT '是否已读',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `content` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_cs_0900_ai_ci NULL DEFAULT NULL COMMENT '内容',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_cs_0900_ai_ci COMMENT = '消息记录表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of b_news_user_record
-- ----------------------------

-- ----------------------------
-- Table structure for b_private_news_info
-- ----------------------------
DROP TABLE IF EXISTS `b_private_news_info`;
CREATE TABLE `b_private_news_info`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'id',
  `uid` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_cs_0900_ai_ci NOT NULL COMMENT '用户id',
  `news_comment` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_cs_0900_ai_ci NULL COMMENT '消息内容',
  `reply_target_uid` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_cs_0900_ai_ci NOT NULL COMMENT '目标uid',
  `been_read` int NOT NULL DEFAULT 1 COMMENT '是否已读（1：未读，2：已读）',
  `time_mark` int NOT NULL DEFAULT 0 COMMENT '标识（与上一条消息时间间隔0：20分钟以内，1：超过20分钟）',
  `create_time` datetime NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `uid`(`uid` ASC, `reply_target_uid` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_cs_0900_ai_ci COMMENT = '私信记录表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of b_private_news_info
-- ----------------------------

-- ----------------------------
-- Table structure for b_private_user
-- ----------------------------
DROP TABLE IF EXISTS `b_private_user`;
CREATE TABLE `b_private_user`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'id',
  `uid` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_cs_0900_ai_ci NOT NULL COMMENT '用户id',
  `target_uid` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_cs_0900_ai_ci NULL DEFAULT NULL COMMENT '目标用户id',
  `last_news` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_cs_0900_ai_ci NULL COMMENT '最后一条聊天类容',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime NOT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_cs_0900_ai_ci COMMENT = '私信用户记录表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of b_private_user
-- ----------------------------

-- ----------------------------
-- Table structure for b_search_records
-- ----------------------------
DROP TABLE IF EXISTS `b_search_records`;
CREATE TABLE `b_search_records`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'id',
  `uid` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_cs_0900_ai_ci NOT NULL COMMENT '用户id',
  `content` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_cs_0900_ai_ci NOT NULL COMMENT '内容',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `uid`(`uid` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_cs_0900_ai_ci COMMENT = '搜索记录表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of b_search_records
-- ----------------------------

-- ----------------------------
-- Table structure for b_sign_log
-- ----------------------------
DROP TABLE IF EXISTS `b_sign_log`;
CREATE TABLE `b_sign_log`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'id',
  `uid` varbinary(64) NOT NULL COMMENT '用户id',
  `user_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '用户名称',
  `ip_address` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT 'ip地址',
  `location` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '登录地址',
  `create_time` datetime NOT NULL COMMENT '时间',
  `type` int NOT NULL COMMENT '类型（0：登录，1：登出）',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '登录日志' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of b_sign_log
-- ----------------------------

-- ----------------------------
-- Table structure for b_special_information
-- ----------------------------
DROP TABLE IF EXISTS `b_special_information`;
CREATE TABLE `b_special_information`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '专栏id',
  `special_name` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '专栏名称',
  `special_introduce` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '专栏简介',
  `state` int NOT NULL DEFAULT 0 COMMENT '专栏状态（0：正常，1：未启用）',
  `cover` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '封面',
  `uid` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_cs_0900_ai_ci NOT NULL COMMENT '用户id',
  `included_count` int NOT NULL DEFAULT 0 COMMENT '收录数',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_id` bigint NULL DEFAULT NULL COMMENT '修改者id',
  `update_time` datetime NULL DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_cs_0900_ai_ci COMMENT = '专栏信息表表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of b_special_information
-- ----------------------------
INSERT INTO `b_special_information` VALUES (1, '其他', '其他', 0, NULL, '1636168030713155584', 0, '2024-11-25 14:46:20', NULL, NULL);

-- ----------------------------
-- Table structure for b_stat_data
-- ----------------------------
DROP TABLE IF EXISTS `b_stat_data`;
CREATE TABLE `b_stat_data`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `stat_time` varchar(12) CHARACTER SET utf8mb4 COLLATE utf8mb4_cs_0900_ai_ci NOT NULL COMMENT '统计月份',
  `user_count` bigint NULL DEFAULT 0 COMMENT '用户总数',
  `onLine_user_count` bigint NULL DEFAULT 0 COMMENT '在线用户总数',
  `article_count` bigint NULL DEFAULT 0 COMMENT '文章总数',
  `special_count` bigint NULL DEFAULT 0 COMMENT '专栏总数',
  `favorites_count` bigint NULL DEFAULT 0 COMMENT '收藏夹总数',
  `dictum_group_count` bigint NULL DEFAULT 0 COMMENT '名言分类总数',
  `dictum_album_count` bigint NULL DEFAULT 0 COMMENT '名言专辑总数',
  `dictum_count` bigint NULL DEFAULT 0 COMMENT '名言总数',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `stat_time`(`stat_time` ASC) USING BTREE COMMENT 'stat_time 统计月份'
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_cs_0900_ai_ci COMMENT = '网站数据月统计表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of b_stat_data
-- ----------------------------

-- ----------------------------
-- Table structure for b_stat_data_info
-- ----------------------------
DROP TABLE IF EXISTS `b_stat_data_info`;
CREATE TABLE `b_stat_data_info`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'id',
  `stat_time` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_cs_0900_ai_ci NOT NULL COMMENT '统计时间',
  `user_count` bigint NULL DEFAULT 0 COMMENT '用户总数',
  `onLine_user_count` bigint NULL DEFAULT 0 COMMENT '在线用户总数',
  `article_count` bigint NULL DEFAULT 0 COMMENT '文章总数',
  `special_count` bigint NULL DEFAULT 0 COMMENT '专栏总数',
  `favorites_count` bigint NULL DEFAULT 0 COMMENT '收藏夹总数',
  `dictum_group_count` bigint NULL DEFAULT 0 COMMENT '名言分类总数',
  `dictum_album_count` bigint NULL DEFAULT 0 COMMENT '名言专辑总数',
  `dictum_count` bigint NULL DEFAULT 0 COMMENT '名言总数',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `stat_time`(`stat_time` ASC) USING BTREE COMMENT 'stat_time 唯一索引'
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_cs_0900_ai_ci COMMENT = '统计数据信息表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of b_stat_data_info
-- ----------------------------

-- ----------------------------
-- Table structure for b_system_task_config
-- ----------------------------
DROP TABLE IF EXISTS `b_system_task_config`;
CREATE TABLE `b_system_task_config`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'id',
  `task_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_cs_0900_ai_ci NULL DEFAULT NULL COMMENT '任务名称',
  `task_execution_time` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_cs_0900_ai_ci NULL DEFAULT NULL COMMENT '任务执行时间',
  `task_execution_sum` bigint NULL DEFAULT 0 COMMENT '执行次数',
  `task_url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_cs_0900_ai_ci NULL DEFAULT NULL COMMENT '接口地址',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 9 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_cs_0900_ai_ci COMMENT = '系统任务配置' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of b_system_task_config
-- ----------------------------
INSERT INTO `b_system_task_config` VALUES (1, '计算文章权重任务', '每小时执行一次', 193, '/system/task/calculating_article_weights');
INSERT INTO `b_system_task_config` VALUES (2, '同步文章点赞任务', '每周天晚上0点执行', 1, '/system/task/sync_article_click_like');
INSERT INTO `b_system_task_config` VALUES (3, '同步名言数据任务', '每半小时一次', 381, '/system/task/syncing_quotes_data');
INSERT INTO `b_system_task_config` VALUES (4, '计算热门数据任务', '每40分钟一次', 383, '/system/task/calculating_hot_data');
INSERT INTO `b_system_task_config` VALUES (5, '同步标签信息数据任务', '每天0点执行', 8, '/system/task/synchronize_label_information_data');
INSERT INTO `b_system_task_config` VALUES (6, '同步专栏文章数任务', '每天0点执行', 11, '/system/task/sync_number_columns');
INSERT INTO `b_system_task_config` VALUES (7, '更新网站每日数据任务', '每天23点执行', 9, '/system/task/update_website_daily_data');
INSERT INTO `b_system_task_config` VALUES (8, '更新网站每月数据任务', '每天0点执行', 8, '/system/task/update_website_monthly_data');
INSERT INTO `b_system_task_config` VALUES (9, '用户网站数据同步任务', '每小时同步一次', 193, '/system/task/syncing_user_site_data');

-- ----------------------------
-- Table structure for b_user_follow
-- ----------------------------
DROP TABLE IF EXISTS `b_user_follow`;
CREATE TABLE `b_user_follow`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'id',
  `uid` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_cs_0900_ai_ci NOT NULL COMMENT '用户id',
  `target_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_cs_0900_ai_ci NOT NULL COMMENT '关注目标id',
  `type` int NOT NULL COMMENT '关注类型（1：用户，2：标签，3：活动，4：圈子）',
  `state` int NOT NULL DEFAULT 0 COMMENT '状态（0：正常，1：已取消）',
  `create_time` datetime NOT NULL COMMENT '关注时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_cs_0900_ai_ci COMMENT = '用户关注表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of b_user_follow
-- ----------------------------

-- ----------------------------
-- Table structure for b_user_information
-- ----------------------------
DROP TABLE IF EXISTS `b_user_information`;
CREATE TABLE `b_user_information`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'id',
  `uuid` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_cs_0900_ai_ci NOT NULL COMMENT '用户id',
  `nickname` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '用户昵称',
  `homepage` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '个人主页地址',
  `introduce` varchar(260) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '个人简介',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '更新者id',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  `empirical` int NOT NULL DEFAULT 0 COMMENT '用户经验值',
  `grade` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '0' COMMENT '用户等级',
  `user_state` int NOT NULL DEFAULT 0 COMMENT '用户状态（0：正常，1：已删除，2：冻结，）',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uuid`(`uuid` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 16 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_cs_0900_ai_ci COMMENT = '用户偏好信息表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of b_user_information
-- ----------------------------
INSERT INTO `b_user_information` VALUES (1, '6520363', '美团干饭人', NULL, NULL, '2023-03-13 08:49:12', '6520363', NULL, 0, '0', 0);
INSERT INTO `b_user_information` VALUES (2, '9621443', 'gitee-little-love', NULL, NULL, '2023-03-14 15:35:08', '9621443', NULL, 0, '0', 0);
INSERT INTO `b_user_information` VALUES (3, '1636168030713155584', '钰紫薇', NULL, '一枚爱好文学的卑微打工人', '2023-03-16 08:50:32', '1636168030713155584', NULL, 0, '0', 0);
INSERT INTO `b_user_information` VALUES (4, '1640366618662014976', '誈', NULL, NULL, '2023-03-27 22:54:13', '1640366618662014976', NULL, 0, '0', 0);
INSERT INTO `b_user_information` VALUES (5, '1644996548775514112', '道友1644996548775514114', NULL, NULL, '2023-04-09 17:31:54', '1644996548775514112', NULL, 0, '0', 0);
INSERT INTO `b_user_information` VALUES (6, '1645682271283449856', '苏苏波儿蜜', NULL, '没有什么比躺平更重要。', '2023-04-11 14:56:43', '1645682271283449856', NULL, 0, '0', 0);
INSERT INTO `b_user_information` VALUES (7, '10362663', 'zhang-fuguihaha', NULL, NULL, '2024-01-13 19:50:21', '10362663', NULL, 0, '0', 0);
INSERT INTO `b_user_information` VALUES (8, '7032039195', '用户7032039195', NULL, NULL, '2024-02-24 15:16:55', '7032039195', NULL, 0, '0', 0);
INSERT INTO `b_user_information` VALUES (9, '7632390171', '用户7632390171', NULL, NULL, '2024-09-18 15:13:29', '7632390171', NULL, 0, '0', 0);
INSERT INTO `b_user_information` VALUES (10, '9908760', 'wangsaiqimengwen', NULL, NULL, '2024-10-09 19:18:33', '9908760', NULL, 0, '0', 0);
INSERT INTO `b_user_information` VALUES (11, '9468821', 'wangyanweiie', NULL, NULL, '2024-10-09 19:48:22', '9468821', NULL, 0, '0', 0);
INSERT INTO `b_user_information` VALUES (12, '1845380198288199680', '用户1845380198288199680', NULL, NULL, '2024-10-13 16:24:59', '1845380198288199680', NULL, 0, '0', 0);
INSERT INTO `b_user_information` VALUES (13, '1845380802125369344', '用户1845380802125369344', NULL, NULL, '2024-10-13 16:27:23', '1845380802125369344', NULL, 0, '0', 0);
INSERT INTO `b_user_information` VALUES (14, '1845384325269426176', '用户1845384325269426176', NULL, NULL, '2024-10-13 16:41:23', '1845384325269426176', NULL, 0, '0', 0);
INSERT INTO `b_user_information` VALUES (15, '1845401541947822080', 'Issues专业户', NULL, NULL, '2024-10-13 17:49:48', '1845401541947822080', NULL, 0, '0', 0);
INSERT INTO `b_user_information` VALUES (16, '1854786938406572032', '阿晨Gg', NULL, NULL, '2024-11-08 15:24:01', '1854786938406572032', NULL, 0, '0', 0);

-- ----------------------------
-- Table structure for b_user_main
-- ----------------------------
DROP TABLE IF EXISTS `b_user_main`;
CREATE TABLE `b_user_main`  (
  `uuid` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_cs_0900_ai_ci NOT NULL COMMENT '用户第三方系统的唯一id',
  `username` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '用户名',
  `nickname` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '用户昵称',
  `phone` varchar(11) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '手机号',
  `password` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '密码',
  `salt` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '盐',
  `avatar` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '用户头像',
  `blog` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '用户网址',
  `company` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '所在公司',
  `location` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '位置',
  `email` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '用户邮箱',
  `gender` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '性别',
  `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '用户备注（各平台中的用户个人介绍）',
  `occupation` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '职业',
  `source` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '用户来源',
  `user_type` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '用户类型',
  `create_time` datetime NULL DEFAULT NULL COMMENT '注册时间',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  `state` int NOT NULL DEFAULT 0 COMMENT '用户状态（0：正常，1：冻结，2：已删除）',
  `login_ip` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '最后登录ip',
  `role_id` bigint NULL DEFAULT 1 COMMENT '角色（0：管理员，1，普通用户，2，vip用户）',
  `a_currency` bigint(12) UNSIGNED ZEROFILL NOT NULL DEFAULT 000000000000 COMMENT 'a币',
  PRIMARY KEY (`uuid`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_cs_0900_ai_ci COMMENT = '用户信息表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of b_user_main
-- ----------------------------
INSERT INTO `b_user_main` VALUES ('1636168030713155584', '美团干饭人', '钰紫薇', '', '207cf410532f92a47dee245ce9b11ff71f578ebd763eb3bbea44ebd043d018fb', NULL, 'https://zsh-aurora.oss-cn-beijing.aliyuncs.com/aurora/2023/06/09/5a24510f95f84618aafb16ec43c21e24.jpg', NULL, NULL, NULL, '2978824265@qq.com', NULL, NULL, '美团攻城师', '平台注册', 'tripartite_user', '2024-03-16 08:50:32', '2024-07-10 12:20:56', 0, NULL, 0, 000000001260);

-- ----------------------------
-- Table structure for b_user_order
-- ----------------------------
DROP TABLE IF EXISTS `b_user_order`;
CREATE TABLE `b_user_order`  (
  `id` bigint NOT NULL COMMENT 'id',
  `uid` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_cs_0900_ai_ci NOT NULL COMMENT '用户id',
  `recharge_info_id` bigint NOT NULL COMMENT '充值信息id',
  `recharge_money` decimal(12, 2) NOT NULL COMMENT '充值金额',
  `recharge_currency` bigint NOT NULL COMMENT '获得A币数量',
  `commodity_name` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_cs_0900_ai_ci NOT NULL COMMENT '商品名称',
  `discount` int NOT NULL DEFAULT 0 COMMENT '折扣（0：无折扣，非0：具体折扣）',
  `payment_method` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_cs_0900_ai_ci NULL DEFAULT NULL COMMENT '支付方式',
  `create_time` datetime NOT NULL COMMENT '订单创建时间',
  `payment_state` int NOT NULL DEFAULT 1 COMMENT '支付状态（1：待支付，2：支付完成，3：已过期，4：支付失败）',
  `payment_time` datetime NULL DEFAULT NULL COMMENT '支付时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_cs_0900_ai_ci COMMENT = '用户订单表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of b_user_order
-- ----------------------------

-- ----------------------------
-- Table structure for b_user_report
-- ----------------------------
DROP TABLE IF EXISTS `b_user_report`;
CREATE TABLE `b_user_report`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'id',
  `uid` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_cs_0900_ai_ci NOT NULL COMMENT 'uid',
  `report_time` datetime NOT NULL COMMENT '签到时间',
  `a_currency` bigint NULL DEFAULT NULL COMMENT '获得a币数量',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_cs_0900_ai_ci COMMENT = '用户签到表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of b_user_report
-- ----------------------------

-- ----------------------------
-- Table structure for sys_black_list
-- ----------------------------
DROP TABLE IF EXISTS `sys_black_list`;
CREATE TABLE `sys_black_list`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'id',
  `ip` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_cs_0900_ai_ci NULL DEFAULT NULL COMMENT 'ip地址',
  `home_place` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_cs_0900_ai_ci NULL DEFAULT NULL COMMENT 'IP地址归属地',
  `detail` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_cs_0900_ai_ci NULL COMMENT '明细',
  `uid` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_cs_0900_ai_ci NULL DEFAULT NULL COMMENT 'uid',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_cs_0900_ai_ci COMMENT = '网站黑名单' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_black_list
-- ----------------------------

-- ----------------------------
-- Table structure for sys_config
-- ----------------------------
DROP TABLE IF EXISTS `sys_config`;
CREATE TABLE `sys_config`  (
  `config_id` int NOT NULL COMMENT '参数主键',
  `config_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_cs_0900_ai_ci NULL DEFAULT '' COMMENT '参数名称',
  `config_key` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_cs_0900_ai_ci NULL DEFAULT '' COMMENT '参数键名',
  `config_value` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_cs_0900_ai_ci NULL DEFAULT '' COMMENT '参数键值',
  `config_type` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_cs_0900_ai_ci NULL DEFAULT 'N' COMMENT '系统内置（Y是 N否）',
  `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_cs_0900_ai_ci NULL DEFAULT '' COMMENT '创建者',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_cs_0900_ai_ci NULL DEFAULT '' COMMENT '更新者',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_cs_0900_ai_ci NULL DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`config_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_cs_0900_ai_ci COMMENT = '参数配置表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_config
-- ----------------------------
INSERT INTO `sys_config` VALUES (1, '主框架页-默认皮肤样式名称', 'sys.index.skinName', 'skin-blue', 'Y', 'admin', '2022-06-01 22:11:39', '', NULL, '蓝色 skin-blue、绿色 skin-green、紫色 skin-purple、红色 skin-red、黄色 skin-yellow');
INSERT INTO `sys_config` VALUES (2, '用户管理-账号初始密码', 'sys.user.initPassword', '123456', 'Y', 'admin', '2022-06-01 22:11:39', '', NULL, '初始化密码 123456');
INSERT INTO `sys_config` VALUES (3, '主框架页-侧边栏主题', 'sys.index.sideTheme', 'theme-dark', 'Y', 'admin', '2022-06-01 22:11:39', '', NULL, '深色主题theme-dark，浅色主题theme-light');
INSERT INTO `sys_config` VALUES (4, '账号自助-验证码开关', 'sys.account.captchaOnOff', 'true', 'Y', 'admin', '2022-06-01 22:11:39', '', NULL, '是否开启验证码功能（true开启，false关闭）');
INSERT INTO `sys_config` VALUES (5, '账号自助-是否开启用户注册功能', 'sys.account.registerUser', 'false', 'Y', 'admin', '2022-06-01 22:11:39', '', NULL, '是否开启注册用户功能（true开启，false关闭）');
INSERT INTO `sys_config` VALUES (11, 'OSS预览列表资源开关', 'sys.oss.previewListResource', 'true', 'Y', 'admin', '2022-06-01 22:11:39', '', NULL, 'true:开启, false:关闭');

-- ----------------------------
-- Table structure for sys_dept
-- ----------------------------
DROP TABLE IF EXISTS `sys_dept`;
CREATE TABLE `sys_dept`  (
  `dept_id` bigint NOT NULL COMMENT '部门id',
  `parent_id` bigint NULL DEFAULT 0 COMMENT '父部门id',
  `ancestors` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_cs_0900_ai_ci NULL DEFAULT '' COMMENT '祖级列表',
  `dept_name` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_cs_0900_ai_ci NULL DEFAULT '' COMMENT '部门名称',
  `order_num` int NULL DEFAULT 0 COMMENT '显示顺序',
  `leader` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_cs_0900_ai_ci NULL DEFAULT NULL COMMENT '负责人',
  `phone` varchar(11) CHARACTER SET utf8mb4 COLLATE utf8mb4_cs_0900_ai_ci NULL DEFAULT NULL COMMENT '联系电话',
  `email` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_cs_0900_ai_ci NULL DEFAULT NULL COMMENT '邮箱',
  `status` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_cs_0900_ai_ci NULL DEFAULT '0' COMMENT '部门状态（0正常 1停用）',
  `del_flag` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_cs_0900_ai_ci NULL DEFAULT '0' COMMENT '删除标志（0代表存在 2代表删除）',
  `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_cs_0900_ai_ci NULL DEFAULT '' COMMENT '创建者',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_cs_0900_ai_ci NULL DEFAULT '' COMMENT '更新者',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`dept_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_cs_0900_ai_ci COMMENT = '部门表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_dept
-- ----------------------------
INSERT INTO `sys_dept` VALUES (100, 0, '0', 'Aurora', 0, 'Aurora', '15888888888', '2978824265@qq.com', '0', '0', 'admin', '2022-06-01 22:11:39', 'admin', '2022-09-09 10:22:33');
INSERT INTO `sys_dept` VALUES (101, 100, '0,100', '成都总公司', 1, 'Aurora', '15888888888', 'ry@qq.com', '0', '0', 'admin', '2022-06-01 22:11:39', 'admin', '2022-09-09 10:22:59');
INSERT INTO `sys_dept` VALUES (103, 101, '0,100,101', '研发部门', 1, '若依', '15888888888', 'ry@qq.com', '0', '0', 'admin', '2022-06-01 22:11:39', 'admin', '2022-09-09 10:22:59');
INSERT INTO `sys_dept` VALUES (104, 101, '0,100,101', '市场部门', 2, '若依', '15888888888', 'ry@qq.com', '0', '1', 'admin', '2022-06-01 22:11:39', 'admin', '2022-09-09 10:22:59');
INSERT INTO `sys_dept` VALUES (105, 101, '0,100,101', '测试部门', 3, '若依', '15888888888', 'ry@qq.com', '0', '0', 'admin', '2022-06-01 22:11:39', 'admin', '2022-09-09 10:22:59');
INSERT INTO `sys_dept` VALUES (106, 101, '0,100,101', '财务部门', 4, '若依', '15888888888', 'ry@qq.com', '0', '1', 'admin', '2022-06-01 22:11:39', 'admin', '2022-09-09 10:22:59');
INSERT INTO `sys_dept` VALUES (107, 101, '0,100,101', '运维部门', 5, '若依', '15888888888', 'ry@qq.com', '0', '0', 'admin', '2022-06-01 22:11:39', 'admin', '2022-09-09 10:22:59');

-- ----------------------------
-- Table structure for sys_dict_data
-- ----------------------------
DROP TABLE IF EXISTS `sys_dict_data`;
CREATE TABLE `sys_dict_data`  (
  `dict_code` bigint NOT NULL AUTO_INCREMENT COMMENT '字典编码',
  `dict_sort` int NULL DEFAULT 0 COMMENT '字典排序',
  `dict_label` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_cs_0900_ai_ci NULL DEFAULT '' COMMENT '字典标签',
  `dict_value` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_cs_0900_ai_ci NULL DEFAULT '' COMMENT '字典键值',
  `dict_type` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_cs_0900_ai_ci NULL DEFAULT '' COMMENT '字典类型',
  `css_class` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_cs_0900_ai_ci NULL DEFAULT NULL COMMENT '样式属性（其他样式扩展）',
  `list_class` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_cs_0900_ai_ci NULL DEFAULT NULL COMMENT '表格回显样式',
  `is_default` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_cs_0900_ai_ci NULL DEFAULT 'N' COMMENT '是否默认（Y是 N否）',
  `status` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_cs_0900_ai_ci NULL DEFAULT '0' COMMENT '状态（0正常 1停用）',
  `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_cs_0900_ai_ci NULL DEFAULT '' COMMENT '创建者',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_cs_0900_ai_ci NULL DEFAULT '' COMMENT '更新者',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_cs_0900_ai_ci NULL DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`dict_code`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 35 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_cs_0900_ai_ci COMMENT = '字典数据表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_dict_data
-- ----------------------------
INSERT INTO `sys_dict_data` VALUES (1, 1, '男', '0', 'sys_user_sex', '', '', 'Y', '0', 'admin', '2022-06-01 22:11:39', '', NULL, '性别男');
INSERT INTO `sys_dict_data` VALUES (2, 2, '女', '1', 'sys_user_sex', '', '', 'N', '0', 'admin', '2022-06-01 22:11:39', '', NULL, '性别女');
INSERT INTO `sys_dict_data` VALUES (3, 3, '未知', '2', 'sys_user_sex', '', '', 'N', '0', 'admin', '2022-06-01 22:11:39', '', NULL, '性别未知');
INSERT INTO `sys_dict_data` VALUES (4, 1, '显示', '0', 'sys_show_hide', '', 'primary', 'Y', '0', 'admin', '2022-06-01 22:11:39', '', NULL, '显示菜单');
INSERT INTO `sys_dict_data` VALUES (5, 2, '隐藏', '1', 'sys_show_hide', '', 'danger', 'N', '0', 'admin', '2022-06-01 22:11:39', '', NULL, '隐藏菜单');
INSERT INTO `sys_dict_data` VALUES (6, 1, '正常', '0', 'sys_normal_disable', '', 'primary', 'Y', '0', 'admin', '2022-06-01 22:11:39', '', NULL, '正常状态');
INSERT INTO `sys_dict_data` VALUES (7, 2, '停用', '1', 'sys_normal_disable', '', 'danger', 'N', '0', 'admin', '2022-06-01 22:11:39', '', NULL, '停用状态');
INSERT INTO `sys_dict_data` VALUES (12, 1, '是', 'Y', 'sys_yes_no', '', 'primary', 'Y', '0', 'admin', '2022-06-01 22:11:39', '', NULL, '系统默认是');
INSERT INTO `sys_dict_data` VALUES (13, 2, '否', 'N', 'sys_yes_no', '', 'danger', 'N', '0', 'admin', '2022-06-01 22:11:39', '', NULL, '系统默认否');
INSERT INTO `sys_dict_data` VALUES (14, 1, '通知', '1', 'sys_notice_type', '', 'warning', 'Y', '0', 'admin', '2022-06-01 22:11:39', '', NULL, '通知');
INSERT INTO `sys_dict_data` VALUES (15, 2, '公告', '2', 'sys_notice_type', '', 'success', 'N', '0', 'admin', '2022-06-01 22:11:39', '', NULL, '公告');
INSERT INTO `sys_dict_data` VALUES (16, 1, '正常', '0', 'sys_notice_status', '', 'primary', 'Y', '0', 'admin', '2022-06-01 22:11:39', '', NULL, '正常状态');
INSERT INTO `sys_dict_data` VALUES (17, 2, '关闭', '1', 'sys_notice_status', '', 'danger', 'N', '0', 'admin', '2022-06-01 22:11:39', '', NULL, '关闭状态');
INSERT INTO `sys_dict_data` VALUES (18, 1, '新增', '1', 'sys_oper_type', '', 'info', 'N', '0', 'admin', '2022-06-01 22:11:39', '', NULL, '新增操作');
INSERT INTO `sys_dict_data` VALUES (19, 2, '修改', '2', 'sys_oper_type', '', 'info', 'N', '0', 'admin', '2022-06-01 22:11:39', '', NULL, '修改操作');
INSERT INTO `sys_dict_data` VALUES (20, 3, '删除', '3', 'sys_oper_type', '', 'danger', 'N', '0', 'admin', '2022-06-01 22:11:39', '', NULL, '删除操作');
INSERT INTO `sys_dict_data` VALUES (21, 4, '授权', '4', 'sys_oper_type', '', 'primary', 'N', '0', 'admin', '2022-06-01 22:11:39', '', NULL, '授权操作');
INSERT INTO `sys_dict_data` VALUES (22, 5, '导出', '5', 'sys_oper_type', '', 'warning', 'N', '0', 'admin', '2022-06-01 22:11:39', '', NULL, '导出操作');
INSERT INTO `sys_dict_data` VALUES (23, 6, '导入', '6', 'sys_oper_type', '', 'warning', 'N', '0', 'admin', '2022-06-01 22:11:39', '', NULL, '导入操作');
INSERT INTO `sys_dict_data` VALUES (24, 7, '强退', '7', 'sys_oper_type', '', 'danger', 'N', '0', 'admin', '2022-06-01 22:11:39', '', NULL, '强退操作');
INSERT INTO `sys_dict_data` VALUES (25, 8, '生成代码', '8', 'sys_oper_type', '', 'warning', 'N', '0', 'admin', '2022-06-01 22:11:39', '', NULL, '生成操作');
INSERT INTO `sys_dict_data` VALUES (26, 9, '清空数据', '9', 'sys_oper_type', '', 'danger', 'N', '0', 'admin', '2022-06-01 22:11:39', '', NULL, '清空操作');
INSERT INTO `sys_dict_data` VALUES (27, 1, '成功', '0', 'sys_common_status', '', 'primary', 'N', '0', 'admin', '2022-06-01 22:11:39', '', NULL, '正常状态');
INSERT INTO `sys_dict_data` VALUES (28, 2, '失败', '1', 'sys_common_status', '', 'danger', 'N', '0', 'admin', '2022-06-01 22:11:39', '', NULL, '停用状态');
INSERT INTO `sys_dict_data` VALUES (29, 0, '正常', '0', 'state_type', NULL, 'primary', 'N', '0', 'admin', '2022-08-19 09:43:32', 'aurora', '2023-03-13 20:41:48', '正常');
INSERT INTO `sys_dict_data` VALUES (30, 0, '未启用', '1', 'state_type', NULL, 'danger', 'N', '0', 'admin', '2022-08-19 09:43:50', 'aurora', '2023-03-13 20:42:00', '未启用');
INSERT INTO `sys_dict_data` VALUES (31, 0, '默认', '1', 'lover_data_type', NULL, 'primary', 'N', '0', 'admin', '2022-11-26 16:13:45', 'admin', '2022-11-28 19:20:45', '默认（用于平常展示）');
INSERT INTO `sys_dict_data` VALUES (32, 0, '生日', '2', 'lover_data_type', NULL, 'primary', 'N', '0', 'admin', '2022-11-27 16:07:15', 'admin', '2022-11-28 19:20:53', '生日');
INSERT INTO `sys_dict_data` VALUES (33, 0, '情人节', '5', 'lover_data_type', NULL, 'primary', 'N', '0', 'admin', '2022-11-27 16:07:27', 'admin', '2022-11-28 19:21:50', '情人节');
INSERT INTO `sys_dict_data` VALUES (34, 0, '结婚纪念日', '3', 'lover_data_type', NULL, 'primary', 'N', '0', 'admin', '2022-11-27 16:07:50', 'admin', '2022-11-28 19:21:40', '结婚纪念日');
INSERT INTO `sys_dict_data` VALUES (35, 0, '新年', '4', 'lover_data_type', NULL, 'primary', 'N', '0', 'admin', '2022-11-27 16:08:26', 'admin', '2022-11-27 16:19:23', '新年');

-- ----------------------------
-- Table structure for sys_dict_type
-- ----------------------------
DROP TABLE IF EXISTS `sys_dict_type`;
CREATE TABLE `sys_dict_type`  (
  `dict_id` bigint NOT NULL AUTO_INCREMENT COMMENT '字典主键',
  `dict_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_cs_0900_ai_ci NULL DEFAULT '' COMMENT '字典名称',
  `dict_type` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_cs_0900_ai_ci NULL DEFAULT '' COMMENT '字典类型',
  `status` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_cs_0900_ai_ci NULL DEFAULT '0' COMMENT '状态（0正常 1停用）',
  `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_cs_0900_ai_ci NULL DEFAULT '' COMMENT '创建者',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_cs_0900_ai_ci NULL DEFAULT '' COMMENT '更新者',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_cs_0900_ai_ci NULL DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`dict_id`) USING BTREE,
  UNIQUE INDEX `dict_type`(`dict_type` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1596777517165584385 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_cs_0900_ai_ci COMMENT = '字典类型表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_dict_type
-- ----------------------------
INSERT INTO `sys_dict_type` VALUES (1, '用户性别', 'sys_user_sex', '0', 'admin', '2022-06-01 22:11:39', '', NULL, '用户性别列表');
INSERT INTO `sys_dict_type` VALUES (2, '菜单状态', 'sys_show_hide', '0', 'admin', '2022-06-01 22:11:39', '', NULL, '菜单状态列表');
INSERT INTO `sys_dict_type` VALUES (3, '系统开关', 'sys_normal_disable', '0', 'admin', '2022-06-01 22:11:39', '', NULL, '系统开关列表');
INSERT INTO `sys_dict_type` VALUES (6, '系统是否', 'sys_yes_no', '0', 'admin', '2022-06-01 22:11:39', '', NULL, '系统是否列表');
INSERT INTO `sys_dict_type` VALUES (7, '通知类型', 'sys_notice_type', '0', 'admin', '2022-06-01 22:11:39', '', NULL, '通知类型列表');
INSERT INTO `sys_dict_type` VALUES (8, '通知状态', 'sys_notice_status', '0', 'admin', '2022-06-01 22:11:39', '', NULL, '通知状态列表');
INSERT INTO `sys_dict_type` VALUES (9, '操作类型', 'sys_oper_type', '0', 'admin', '2022-06-01 22:11:39', '', NULL, '操作类型列表');
INSERT INTO `sys_dict_type` VALUES (10, '系统状态', 'sys_common_status', '0', 'admin', '2022-06-01 22:11:39', '', NULL, '登录状态列表');
INSERT INTO `sys_dict_type` VALUES (11, 'State数据状态哦', 'state_type', '0', 'admin', '2022-08-19 09:34:58', 'admin', '2022-08-19 09:34:58', NULL);
INSERT INTO `sys_dict_type` VALUES (1596777517165584385, '数据类型', 'lover_data_type', '0', 'admin', '2022-11-27 16:06:41', 'admin', '2022-11-27 16:06:41', '数据类型');

-- ----------------------------
-- Table structure for sys_logininfor
-- ----------------------------
DROP TABLE IF EXISTS `sys_logininfor`;
CREATE TABLE `sys_logininfor`  (
  `info_id` bigint NOT NULL COMMENT '访问ID',
  `user_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_cs_0900_ai_ci NULL DEFAULT '' COMMENT '用户账号',
  `ipaddr` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_cs_0900_ai_ci NULL DEFAULT '' COMMENT '登录IP地址',
  `login_location` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_cs_0900_ai_ci NULL DEFAULT '' COMMENT '登录地点',
  `browser` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_cs_0900_ai_ci NULL DEFAULT '' COMMENT '浏览器类型',
  `os` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_cs_0900_ai_ci NULL DEFAULT '' COMMENT '操作系统',
  `status` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_cs_0900_ai_ci NULL DEFAULT '0' COMMENT '登录状态（0成功 1失败）',
  `msg` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_cs_0900_ai_ci NULL DEFAULT '' COMMENT '提示消息',
  `login_time` datetime NULL DEFAULT NULL COMMENT '访问时间',
  PRIMARY KEY (`info_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_cs_0900_ai_ci COMMENT = '系统访问记录' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_logininfor
-- ----------------------------

-- ----------------------------
-- Table structure for sys_menu
-- ----------------------------
DROP TABLE IF EXISTS `sys_menu`;
CREATE TABLE `sys_menu`  (
  `menu_id` bigint NOT NULL AUTO_INCREMENT COMMENT '菜单ID',
  `menu_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_cs_0900_ai_ci NOT NULL COMMENT '菜单名称',
  `parent_id` bigint NULL DEFAULT 0 COMMENT '父菜单ID',
  `order_num` int NULL DEFAULT 0 COMMENT '显示顺序',
  `path` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_cs_0900_ai_ci NULL DEFAULT '' COMMENT '路由地址',
  `component` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_cs_0900_ai_ci NULL DEFAULT NULL COMMENT '组件路径',
  `query_param` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_cs_0900_ai_ci NULL DEFAULT NULL COMMENT '路由参数',
  `is_frame` int NULL DEFAULT 1 COMMENT '是否为外链（0是 1否）',
  `is_cache` int NULL DEFAULT 0 COMMENT '是否缓存（0缓存 1不缓存）',
  `menu_type` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_cs_0900_ai_ci NULL DEFAULT '' COMMENT '菜单类型（M目录 C菜单 F按钮）',
  `visible` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_cs_0900_ai_ci NULL DEFAULT '0' COMMENT '菜单状态（0显示 1隐藏）',
  `status` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_cs_0900_ai_ci NULL DEFAULT '0' COMMENT '菜单状态（0正常 1停用）',
  `perms` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_cs_0900_ai_ci NULL DEFAULT NULL COMMENT '权限标识',
  `icon` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_cs_0900_ai_ci NULL DEFAULT '#' COMMENT '菜单图标',
  `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_cs_0900_ai_ci NULL DEFAULT '' COMMENT '创建者',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_cs_0900_ai_ci NULL DEFAULT '' COMMENT '更新者',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_cs_0900_ai_ci NULL DEFAULT '' COMMENT '备注',
  PRIMARY KEY (`menu_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1774273778486673411 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_cs_0900_ai_ci COMMENT = '菜单权限表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_menu
-- ----------------------------
INSERT INTO `sys_menu` VALUES (1, '系统管理', 0, 1, 'system', NULL, '', 1, 0, 'M', '0', '0', '', 'system', 'admin', '2022-06-01 22:11:39', '', NULL, '系统管理目录');
INSERT INTO `sys_menu` VALUES (2, '系统监控', 0, 2, 'monitor', NULL, '', 1, 0, 'M', '0', '0', '', 'monitor', 'admin', '2022-06-01 22:11:39', '', NULL, '系统监控目录');
INSERT INTO `sys_menu` VALUES (3, '系统工具', 0, 3, 'tool', NULL, '', 1, 0, 'M', '1', '0', '', 'tool', 'admin', '2022-06-01 22:11:39', 'aurora', '2023-05-20 08:33:54', '系统工具目录');
INSERT INTO `sys_menu` VALUES (5, '测试菜单', 0, 10, 'demo', NULL, NULL, 1, 0, 'M', '1', '1', NULL, 'star', 'admin', '2022-06-01 22:12:28', 'aurora', '2023-04-12 20:53:15', '');
INSERT INTO `sys_menu` VALUES (100, '用户管理', 1, 1, 'user', 'system/user/index', '', 1, 0, 'C', '0', '0', 'system:user:list', 'user', 'admin', '2022-06-01 22:11:39', '', NULL, '用户管理菜单');
INSERT INTO `sys_menu` VALUES (101, '角色管理', 1, 2, 'role', 'system/role/index', '', 1, 0, 'C', '0', '0', 'system:role:list', 'peoples', 'admin', '2022-06-01 22:11:39', '', NULL, '角色管理菜单');
INSERT INTO `sys_menu` VALUES (102, '菜单管理', 1, 3, 'menu', 'system/menu/index', '', 1, 0, 'C', '0', '0', 'system:menu:list', 'tree-table', 'admin', '2022-06-01 22:11:39', '', NULL, '菜单管理菜单');
INSERT INTO `sys_menu` VALUES (103, '部门管理', 1, 4, 'dept', 'system/dept/index', '', 1, 0, 'C', '1', '0', 'system:dept:list', 'tree', 'admin', '2022-06-01 22:11:39', 'aurora', '2024-03-16 11:53:16', '部门管理菜单');
INSERT INTO `sys_menu` VALUES (104, '岗位管理', 1, 5, 'post', 'system/post/index', '', 1, 0, 'C', '1', '0', 'system:post:list', 'post', 'admin', '2022-06-01 22:11:39', 'aurora', '2024-03-16 11:53:24', '岗位管理菜单');
INSERT INTO `sys_menu` VALUES (105, '字典管理', 1, 6, 'dict', 'system/dict/index', '', 1, 0, 'C', '0', '0', 'system:dict:list', 'dict', 'admin', '2022-06-01 22:11:39', '', NULL, '字典管理菜单');
INSERT INTO `sys_menu` VALUES (106, '参数设置', 1, 7, 'config', 'system/config/index', '', 1, 0, 'C', '0', '0', 'system:config:list', 'edit', 'admin', '2022-06-01 22:11:39', '', NULL, '参数设置菜单');
INSERT INTO `sys_menu` VALUES (107, '通知公告', 1, 8, 'notice', 'system/notice/index', '', 1, 0, 'C', '0', '0', 'system:notice:list', 'message', 'admin', '2022-06-01 22:11:39', '', NULL, '通知公告菜单');
INSERT INTO `sys_menu` VALUES (108, '日志管理', 1, 9, 'log', '', '', 1, 0, 'M', '0', '0', '', 'log', 'admin', '2022-06-01 22:11:39', '', NULL, '日志管理菜单');
INSERT INTO `sys_menu` VALUES (109, '在线用户', 2, 1, 'online', 'monitor/online/index', '', 1, 0, 'C', '0', '0', 'monitor:online:list', 'online', 'admin', '2022-06-01 22:11:39', '', NULL, '在线用户菜单');
INSERT INTO `sys_menu` VALUES (111, '数据监控', 2, 3, 'druid', 'monitor/druid/index', '', 1, 0, 'C', '0', '0', 'monitor:druid:list', 'druid', 'admin', '2022-06-01 22:11:39', '', NULL, '数据监控菜单');
INSERT INTO `sys_menu` VALUES (113, '缓存监控', 2, 5, 'cache', 'monitor/cache/index', '', 1, 0, 'C', '0', '0', 'monitor:cache:list', 'redis', 'admin', '2022-06-01 22:11:39', '', NULL, '缓存监控菜单');
INSERT INTO `sys_menu` VALUES (114, '表单构建', 3, 1, 'build', 'tool/build/index', '', 1, 0, 'C', '0', '0', 'tool:build:list', 'build', 'admin', '2022-06-01 22:11:39', '', NULL, '表单构建菜单');
INSERT INTO `sys_menu` VALUES (115, '代码生成', 3, 2, 'gen', 'tool/gen/index', '', 1, 0, 'C', '0', '0', 'tool:gen:list', 'code', 'admin', '2022-06-01 22:11:39', '', NULL, '代码生成菜单');
INSERT INTO `sys_menu` VALUES (116, '系统接口', 3, 3, 'swagger', 'tool/swagger/index', '', 1, 0, 'C', '0', '0', 'tool:swagger:list', 'swagger', 'admin', '2022-06-01 22:11:39', '', NULL, '系统接口菜单');
INSERT INTO `sys_menu` VALUES (117, 'Admin监控', 2, 5, 'Admin', 'monitor/admin/index', '', 1, 0, 'C', '1', '1', 'monitor:admin:list', 'dashboard', 'admin', '2022-06-01 22:11:39', 'ziwei', '2024-11-17 16:30:25', 'Admin监控菜单');
INSERT INTO `sys_menu` VALUES (118, '文件管理', 1, 10, 'oss', 'system/oss/index', '', 1, 0, 'C', '0', '0', 'system:oss:list', 'upload', 'admin', '2022-06-01 22:11:39', '', NULL, '文件管理菜单');
INSERT INTO `sys_menu` VALUES (120, '任务调度中心', 2, 5, 'XxlJob', 'monitor/xxljob/index', '', 1, 0, 'C', '1', '1', 'monitor:xxljob:list', 'job', 'admin', '2022-06-01 22:11:39', 'ziwei', '2024-11-17 16:30:30', 'Xxl-Job控制台菜单');
INSERT INTO `sys_menu` VALUES (500, '操作日志', 108, 1, 'operlog', 'monitor/operlog/index', '', 1, 0, 'C', '0', '0', 'monitor:operlog:list', 'form', 'admin', '2022-06-01 22:11:39', '', NULL, '操作日志菜单');
INSERT INTO `sys_menu` VALUES (501, '登录日志', 108, 2, 'logininfor', 'monitor/logininfor/index', '', 1, 0, 'C', '0', '0', 'monitor:logininfor:list', 'logininfor', 'admin', '2022-06-01 22:11:39', '', NULL, '登录日志菜单');
INSERT INTO `sys_menu` VALUES (1001, '用户查询', 100, 1, '', '', '', 1, 0, 'F', '0', '0', 'system:user:query', '#', 'admin', '2022-06-01 22:11:39', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1002, '用户新增', 100, 2, '', '', '', 1, 0, 'F', '0', '0', 'system:user:add', '#', 'admin', '2022-06-01 22:11:39', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1003, '用户修改', 100, 3, '', '', '', 1, 0, 'F', '0', '0', 'system:user:edit', '#', 'admin', '2022-06-01 22:11:39', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1004, '用户删除', 100, 4, '', '', '', 1, 0, 'F', '0', '0', 'system:user:remove', '#', 'admin', '2022-06-01 22:11:39', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1005, '用户导出', 100, 5, '', '', '', 1, 0, 'F', '0', '0', 'system:user:export', '#', 'admin', '2022-06-01 22:11:39', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1006, '用户导入', 100, 6, '', '', '', 1, 0, 'F', '0', '0', 'system:user:import', '#', 'admin', '2022-06-01 22:11:39', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1007, '重置密码', 100, 7, '', '', '', 1, 0, 'F', '0', '0', 'system:user:resetPwd', '#', 'admin', '2022-06-01 22:11:39', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1008, '角色查询', 101, 1, '', '', '', 1, 0, 'F', '0', '0', 'system:role:query', '#', 'admin', '2022-06-01 22:11:39', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1009, '角色新增', 101, 2, '', '', '', 1, 0, 'F', '0', '0', 'system:role:add', '#', 'admin', '2022-06-01 22:11:39', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1010, '角色修改', 101, 3, '', '', '', 1, 0, 'F', '0', '0', 'system:role:edit', '#', 'admin', '2022-06-01 22:11:39', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1011, '角色删除', 101, 4, '', '', '', 1, 0, 'F', '0', '0', 'system:role:remove', '#', 'admin', '2022-06-01 22:11:39', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1012, '角色导出', 101, 5, '', '', '', 1, 0, 'F', '0', '0', 'system:role:export', '#', 'admin', '2022-06-01 22:11:39', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1013, '菜单查询', 102, 1, '', '', '', 1, 0, 'F', '0', '0', 'system:menu:query', '#', 'admin', '2022-06-01 22:11:39', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1014, '菜单新增', 102, 2, '', '', '', 1, 0, 'F', '0', '0', 'system:menu:add', '#', 'admin', '2022-06-01 22:11:39', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1015, '菜单修改', 102, 3, '', '', '', 1, 0, 'F', '0', '0', 'system:menu:edit', '#', 'admin', '2022-06-01 22:11:39', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1016, '菜单删除', 102, 4, '', '', '', 1, 0, 'F', '0', '0', 'system:menu:remove', '#', 'admin', '2022-06-01 22:11:39', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1017, '部门查询', 103, 1, '', '', '', 1, 0, 'F', '0', '0', 'system:dept:query', '#', 'admin', '2022-06-01 22:11:39', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1018, '部门新增', 103, 2, '', '', '', 1, 0, 'F', '0', '0', 'system:dept:add', '#', 'admin', '2022-06-01 22:11:39', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1019, '部门修改', 103, 3, '', '', '', 1, 0, 'F', '0', '0', 'system:dept:edit', '#', 'admin', '2022-06-01 22:11:39', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1020, '部门删除', 103, 4, '', '', '', 1, 0, 'F', '0', '0', 'system:dept:remove', '#', 'admin', '2022-06-01 22:11:39', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1021, '岗位查询', 104, 1, '', '', '', 1, 0, 'F', '0', '0', 'system:post:query', '#', 'admin', '2022-06-01 22:11:39', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1022, '岗位新增', 104, 2, '', '', '', 1, 0, 'F', '0', '0', 'system:post:add', '#', 'admin', '2022-06-01 22:11:39', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1023, '岗位修改', 104, 3, '', '', '', 1, 0, 'F', '0', '0', 'system:post:edit', '#', 'admin', '2022-06-01 22:11:39', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1024, '岗位删除', 104, 4, '', '', '', 1, 0, 'F', '0', '0', 'system:post:remove', '#', 'admin', '2022-06-01 22:11:39', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1025, '岗位导出', 104, 5, '', '', '', 1, 0, 'F', '0', '0', 'system:post:export', '#', 'admin', '2022-06-01 22:11:39', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1026, '字典查询', 105, 1, '#', '', '', 1, 0, 'F', '0', '0', 'system:dict:query', '#', 'admin', '2022-06-01 22:11:39', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1027, '字典新增', 105, 2, '#', '', '', 1, 0, 'F', '0', '0', 'system:dict:add', '#', 'admin', '2022-06-01 22:11:39', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1028, '字典修改', 105, 3, '#', '', '', 1, 0, 'F', '0', '0', 'system:dict:edit', '#', 'admin', '2022-06-01 22:11:39', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1029, '字典删除', 105, 4, '#', '', '', 1, 0, 'F', '0', '0', 'system:dict:remove', '#', 'admin', '2022-06-01 22:11:39', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1030, '字典导出', 105, 5, '#', '', '', 1, 0, 'F', '0', '0', 'system:dict:export', '#', 'admin', '2022-06-01 22:11:39', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1031, '参数查询', 106, 1, '#', '', '', 1, 0, 'F', '0', '0', 'system:config:query', '#', 'admin', '2022-06-01 22:11:39', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1032, '参数新增', 106, 2, '#', '', '', 1, 0, 'F', '0', '0', 'system:config:add', '#', 'admin', '2022-06-01 22:11:39', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1033, '参数修改', 106, 3, '#', '', '', 1, 0, 'F', '0', '0', 'system:config:edit', '#', 'admin', '2022-06-01 22:11:39', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1034, '参数删除', 106, 4, '#', '', '', 1, 0, 'F', '0', '0', 'system:config:remove', '#', 'admin', '2022-06-01 22:11:39', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1035, '参数导出', 106, 5, '#', '', '', 1, 0, 'F', '0', '0', 'system:config:export', '#', 'admin', '2022-06-01 22:11:39', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1036, '公告查询', 107, 1, '#', '', '', 1, 0, 'F', '0', '0', 'system:notice:query', '#', 'admin', '2022-06-01 22:11:39', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1037, '公告新增', 107, 2, '#', '', '', 1, 0, 'F', '0', '0', 'system:notice:add', '#', 'admin', '2022-06-01 22:11:39', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1038, '公告修改', 107, 3, '#', '', '', 1, 0, 'F', '0', '0', 'system:notice:edit', '#', 'admin', '2022-06-01 22:11:39', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1039, '公告删除', 107, 4, '#', '', '', 1, 0, 'F', '0', '0', 'system:notice:remove', '#', 'admin', '2022-06-01 22:11:39', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1040, '操作查询', 500, 1, '#', '', '', 1, 0, 'F', '0', '0', 'monitor:operlog:query', '#', 'admin', '2022-06-01 22:11:39', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1041, '操作删除', 500, 2, '#', '', '', 1, 0, 'F', '0', '0', 'monitor:operlog:remove', '#', 'admin', '2022-06-01 22:11:39', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1042, '日志导出', 500, 4, '#', '', '', 1, 0, 'F', '0', '0', 'monitor:operlog:export', '#', 'admin', '2022-06-01 22:11:39', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1043, '登录查询', 501, 1, '#', '', '', 1, 0, 'F', '0', '0', 'monitor:logininfor:query', '#', 'admin', '2022-06-01 22:11:39', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1044, '登录删除', 501, 2, '#', '', '', 1, 0, 'F', '0', '0', 'monitor:logininfor:remove', '#', 'admin', '2022-06-01 22:11:39', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1045, '日志导出', 501, 3, '#', '', '', 1, 0, 'F', '0', '0', 'monitor:logininfor:export', '#', 'admin', '2022-06-01 22:11:39', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1046, '在线查询', 109, 1, '#', '', '', 1, 0, 'F', '0', '0', 'monitor:online:query', '#', 'admin', '2022-06-01 22:11:39', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1047, '批量强退', 109, 2, '#', '', '', 1, 0, 'F', '0', '0', 'monitor:online:batchLogout', '#', 'admin', '2022-06-01 22:11:39', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1048, '单条强退', 109, 3, '#', '', '', 1, 0, 'F', '0', '0', 'monitor:online:forceLogout', '#', 'admin', '2022-06-01 22:11:39', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1055, '生成查询', 115, 1, '#', '', '', 1, 0, 'F', '0', '0', 'tool:gen:query', '#', 'admin', '2022-06-01 22:11:39', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1056, '生成修改', 115, 2, '#', '', '', 1, 0, 'F', '0', '0', 'tool:gen:edit', '#', 'admin', '2022-06-01 22:11:39', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1057, '生成删除', 115, 3, '#', '', '', 1, 0, 'F', '0', '0', 'tool:gen:remove', '#', 'admin', '2022-06-01 22:11:39', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1058, '导入代码', 115, 2, '#', '', '', 1, 0, 'F', '0', '0', 'tool:gen:import', '#', 'admin', '2022-06-01 22:11:39', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1059, '预览代码', 115, 4, '#', '', '', 1, 0, 'F', '0', '0', 'tool:gen:preview', '#', 'admin', '2022-06-01 22:11:39', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1060, '生成代码', 115, 5, '#', '', '', 1, 0, 'F', '0', '0', 'tool:gen:code', '#', 'admin', '2022-06-01 22:11:39', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1500, '测试单表', 5, 1, 'demo', 'demo/demo/index', NULL, 1, 0, 'C', '0', '0', 'demo:demo:list', '#', 'admin', '2022-06-01 22:12:28', '', NULL, '测试单表菜单');
INSERT INTO `sys_menu` VALUES (1501, '测试单表查询', 1500, 1, '#', '', NULL, 1, 0, 'F', '0', '0', 'demo:demo:query', '#', 'admin', '2022-06-01 22:12:28', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1502, '测试单表新增', 1500, 2, '#', '', NULL, 1, 0, 'F', '0', '0', 'demo:demo:add', '#', 'admin', '2022-06-01 22:12:28', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1503, '测试单表修改', 1500, 3, '#', '', NULL, 1, 0, 'F', '0', '0', 'demo:demo:edit', '#', 'admin', '2022-06-01 22:12:28', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1504, '测试单表删除', 1500, 4, '#', '', NULL, 1, 0, 'F', '0', '0', 'demo:demo:remove', '#', 'admin', '2022-06-01 22:12:28', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1505, '测试单表导出', 1500, 5, '#', '', NULL, 1, 0, 'F', '0', '0', 'demo:demo:export', '#', 'admin', '2022-06-01 22:12:28', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1506, '测试树表', 5, 1, 'tree', 'demo/tree/index', NULL, 1, 0, 'C', '0', '0', 'demo:tree:list', '#', 'admin', '2022-06-01 22:12:28', '', NULL, '测试树表菜单');
INSERT INTO `sys_menu` VALUES (1507, '测试树表查询', 1506, 1, '#', '', NULL, 1, 0, 'F', '0', '0', 'demo:tree:query', '#', 'admin', '2022-06-01 22:12:28', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1508, '测试树表新增', 1506, 2, '#', '', NULL, 1, 0, 'F', '0', '0', 'demo:tree:add', '#', 'admin', '2022-06-01 22:12:28', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1509, '测试树表修改', 1506, 3, '#', '', NULL, 1, 0, 'F', '0', '0', 'demo:tree:edit', '#', 'admin', '2022-06-01 22:12:28', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1510, '测试树表删除', 1506, 4, '#', '', NULL, 1, 0, 'F', '0', '0', 'demo:tree:remove', '#', 'admin', '2022-06-01 22:12:28', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1511, '测试树表导出', 1506, 5, '#', '', NULL, 1, 0, 'F', '0', '0', 'demo:tree:export', '#', 'admin', '2022-06-01 22:12:28', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1600, '文件查询', 118, 1, '#', '', '', 1, 0, 'F', '0', '0', 'system:oss:query', '#', 'admin', '2022-06-01 22:11:39', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1601, '文件上传', 118, 2, '#', '', '', 1, 0, 'F', '0', '0', 'system:oss:upload', '#', 'admin', '2022-06-01 22:11:39', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1602, '文件下载', 118, 3, '#', '', '', 1, 0, 'F', '0', '0', 'system:oss:download', '#', 'admin', '2022-06-01 22:11:39', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1603, '文件删除', 118, 4, '#', '', '', 1, 0, 'F', '0', '0', 'system:oss:remove', '#', 'admin', '2022-06-01 22:11:39', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1604, '配置添加', 118, 5, '#', '', '', 1, 0, 'F', '0', '0', 'system:oss:add', '#', 'admin', '2022-06-01 22:11:39', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1605, '配置编辑', 118, 6, '#', '', '', 1, 0, 'F', '0', '0', 'system:oss:edit', '#', 'admin', '2022-06-01 22:11:39', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1559071670050615298, '运营管理', 0, 4, '/operate', NULL, NULL, 1, 0, 'M', '0', '0', NULL, '运营管理', 'admin', '2022-08-15 14:57:07', 'aurora', '2023-03-13 21:50:51', '');
INSERT INTO `sys_menu` VALUES (1559071832202407937, '运维管理', 0, 5, '/operation', NULL, NULL, 1, 0, 'M', '0', '0', NULL, '运维管理', 'admin', '2022-08-15 14:57:46', 'aurora', '2023-03-13 21:51:03', '');
INSERT INTO `sys_menu` VALUES (1559368157548617730, '标签管理', 1559071832202407937, 1, '/label', NULL, NULL, 1, 0, 'M', '0', '0', NULL, 'cascader', 'admin', '2022-08-16 10:35:15', 'aurora', '2023-03-13 20:14:36', '');
INSERT INTO `sys_menu` VALUES (1559369318368387072, '标签分组', 1559368157548617730, 1, 'groupingInfo', 'business/label/grouping-info', NULL, 1, 0, 'C', '0', '0', 'system:groupingInfo:list', 'swagger', 'admin', '2022-08-16 10:40:34', 'admin', '2022-08-16 14:19:42', '标签分组信息菜单');
INSERT INTO `sys_menu` VALUES (1559369318368387073, '标签分组信息查询', 1559369318368387072, 1, '#', '', NULL, 1, 0, 'F', '0', '0', 'system:groupingInfo:query', '#', 'admin', '2022-08-16 10:40:34', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1559369318368387074, '标签分组信息新增', 1559369318368387072, 2, '#', '', NULL, 1, 0, 'F', '0', '0', 'system:groupingInfo:add', '#', 'admin', '2022-08-16 10:40:34', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1559369318368387075, '标签分组信息修改', 1559369318368387072, 3, '#', '', NULL, 1, 0, 'F', '0', '0', 'system:groupingInfo:edit', '#', 'admin', '2022-08-16 10:40:34', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1559369318368387076, '标签分组信息删除', 1559369318368387072, 4, '#', '', NULL, 1, 0, 'F', '0', '0', 'system:groupingInfo:remove', '#', 'admin', '2022-08-16 10:40:34', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1559369318368387077, '标签分组信息导出', 1559369318368387072, 5, '#', '', NULL, 1, 0, 'F', '0', '0', 'system:groupingInfo:export', '#', 'admin', '2022-08-16 10:40:34', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1559377965450379264, '标签信息', 1559368157548617730, 1, 'info', 'business/label/info', NULL, 1, 0, 'C', '0', '0', 'business:label-info:list', 'skill', 'admin', '2022-08-16 11:32:50', 'admin', '2022-08-16 14:20:14', '标签信息菜单');
INSERT INTO `sys_menu` VALUES (1559377965450379265, '标签信息查询', 1559377965450379264, 1, '#', '', NULL, 1, 0, 'F', '0', '0', 'business:label-info:query', '#', 'admin', '2022-08-16 11:32:50', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1559377965450379266, '标签信息新增', 1559377965450379264, 2, '#', '', NULL, 1, 0, 'F', '0', '0', 'business:label-info:add', '#', 'admin', '2022-08-16 11:32:50', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1559377965450379267, '标签信息修改', 1559377965450379264, 3, '#', '', NULL, 1, 0, 'F', '0', '0', 'business:label-info:edit', '#', 'admin', '2022-08-16 11:32:50', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1559377965450379268, '标签信息删除', 1559377965450379264, 4, '#', '', NULL, 1, 0, 'F', '0', '0', 'business:label-info:remove', '#', 'admin', '2022-08-16 11:32:50', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1559377965450379269, '标签信息导出', 1559377965450379264, 5, '#', '', NULL, 1, 0, 'F', '0', '0', 'business:label-info:export', '#', 'admin', '2022-08-16 11:32:50', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1559528924465868800, '文章列表', 1559541694750003201, 1, 'article-inform', 'business/article/article-inform', NULL, 1, 0, 'C', '0', '0', 'article:information:list', 'nested', 'admin', '2022-08-16 22:01:53', 'admin', '2022-08-16 22:34:06', '文章信息菜单');
INSERT INTO `sys_menu` VALUES (1559528924465868801, '文章信息查询', 1559528924465868800, 1, '#', '', NULL, 1, 0, 'F', '0', '0', 'article:information:query', '#', 'admin', '2022-08-16 22:01:53', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1559528924465868802, '文章信息新增', 1559528924465868800, 2, '#', '', NULL, 1, 0, 'F', '0', '0', 'article:information:add', '#', 'admin', '2022-08-16 22:01:53', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1559528924465868803, '文章信息修改', 1559528924465868800, 3, '#', '', NULL, 1, 0, 'F', '0', '0', '·', '#', 'admin', '2022-08-16 22:01:53', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1559528924465868804, '文章信息删除', 1559528924465868800, 4, '#', '', NULL, 1, 0, 'F', '0', '0', 'article:information:remove', '#', 'admin', '2022-08-16 22:01:53', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1559528924465868805, '文章信息导出', 1559528924465868800, 5, '#', '', NULL, 1, 0, 'F', '0', '0', 'article:information:export', '#', 'admin', '2022-08-16 22:01:53', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1559541694750003201, '文章管理', 1559071670050615298, 2, '/article', NULL, NULL, 1, 0, 'M', '0', '0', NULL, 'education', 'admin', '2022-08-16 22:04:50', 'admin', '2022-08-16 22:04:50', '');
INSERT INTO `sys_menu` VALUES (1560425633970720768, '专栏列表', 1559071670050615298, 1, '/special/information', 'business/special/index', NULL, 1, 0, 'C', '0', '0', 'special:information:list', 'example', 'admin', '2022-08-19 08:59:47', 'aurora', '2023-03-13 21:44:54', '专栏信息菜单');
INSERT INTO `sys_menu` VALUES (1560425633970720769, '专栏信息查询', 1560425633970720768, 1, '#', '', NULL, 1, 0, 'F', '0', '0', 'special:information:query', '#', 'admin', '2022-08-19 08:59:47', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1560425633970720770, '专栏信息新增', 1560425633970720768, 2, '#', '', NULL, 1, 0, 'F', '0', '0', 'special:information:add', '#', 'admin', '2022-08-19 08:59:47', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1560425633970720771, '专栏信息修改', 1560425633970720768, 3, '#', '', NULL, 1, 0, 'F', '0', '0', 'special:information:edit', '#', 'admin', '2022-08-19 08:59:47', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1560425633970720772, '专栏信息删除', 1560425633970720768, 4, '#', '', NULL, 1, 0, 'F', '0', '0', 'special:information:remove', '#', 'admin', '2022-08-19 08:59:47', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1560425633970720773, '专栏信息导出', 1560425633970720768, 5, '#', '', NULL, 1, 0, 'F', '0', '0', 'special:information:export', '#', 'admin', '2022-08-19 08:59:47', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1568069868383428608, '用户列表', 1568071215681953794, 1, 'main', 'system/userMain/index', NULL, 1, 0, 'C', '0', '0', 'system:main:list', 'list', 'admin', '2022-09-09 10:55:56', 'admin', '2022-09-09 10:58:29', '用户信息菜单');
INSERT INTO `sys_menu` VALUES (1568069868383428609, '用户信息查询', 1568069868383428608, 1, '#', '', NULL, 1, 0, 'F', '0', '0', 'system:main:query', '#', 'admin', '2022-09-09 10:55:56', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1568069868383428610, '用户信息新增', 1568069868383428608, 2, '#', '', NULL, 1, 0, 'F', '0', '0', 'system:main:add', '#', 'admin', '2022-09-09 10:55:56', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1568069868383428611, '用户信息修改', 1568069868383428608, 3, '#', '', NULL, 1, 0, 'F', '0', '0', 'system:main:edit', '#', 'admin', '2022-09-09 10:55:56', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1568069868383428612, '用户信息删除', 1568069868383428608, 4, '#', '', NULL, 1, 0, 'F', '0', '0', 'system:main:remove', '#', 'admin', '2022-09-09 10:55:56', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1568069868383428613, '用户信息导出', 1568069868383428608, 5, '#', '', NULL, 1, 0, 'F', '0', '0', 'system:main:export', '#', 'admin', '2022-09-09 10:55:56', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1568071215681953794, '前台用户', 0, 6, 'system/userMain', NULL, NULL, 1, 0, 'M', '0', '0', NULL, 'user', 'admin', '2022-09-09 10:58:06', 'aurora', '2023-03-13 22:00:05', '');
INSERT INTO `sys_menu` VALUES (1570601174635941888, '顶部导航', 1583288960459567105, 1, 'configure/navigation', 'business/configure/navigation', NULL, 1, 0, 'C', '0', '0', 'business:navigation:list', 'build', 'admin', '2022-09-16 10:39:42', 'aurora', '2023-03-13 21:44:17', '导航栏配置菜单');
INSERT INTO `sys_menu` VALUES (1570601174635941889, '导航栏配置查询', 1570601174635941888, 1, '#', '', NULL, 1, 0, 'F', '0', '0', 'business:navigation:query', '#', 'admin', '2022-09-16 10:39:42', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1570601174635941890, '导航栏配置新增', 1570601174635941888, 2, '#', '', NULL, 1, 0, 'F', '0', '0', 'business:navigation:add', '#', 'admin', '2022-09-16 10:39:42', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1570601174635941891, '导航栏配置修改', 1570601174635941888, 3, '#', '', NULL, 1, 0, 'F', '0', '0', 'business:navigation:edit', '#', 'admin', '2022-09-16 10:39:42', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1570601174635941892, '导航栏配置删除', 1570601174635941888, 4, '#', '', NULL, 1, 0, 'F', '0', '0', 'business:navigation:remove', '#', 'admin', '2022-09-16 10:39:42', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1570601174635941893, '导航栏配置导出', 1570601174635941888, 5, '#', '', NULL, 1, 0, 'F', '0', '0', 'business:navigation:export', '#', 'admin', '2022-09-16 10:39:42', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1570612161132990464, '侧边栏', 1583288960459567105, 1, 'sidebar', 'business/configure/sidebar', NULL, 1, 0, 'C', '0', '0', 'business:sidebar:list', 'cascader', 'admin', '2022-09-16 11:21:53', 'aurora', '2022-10-21 10:50:14', '侧边栏配置菜单');
INSERT INTO `sys_menu` VALUES (1570612161132990465, '侧边栏配置查询', 1570612161132990464, 1, '#', '', NULL, 1, 0, 'F', '0', '0', 'business:sidebar:query', '#', 'admin', '2022-09-16 11:21:53', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1570612161132990466, '侧边栏配置新增', 1570612161132990464, 2, '#', '', NULL, 1, 0, 'F', '0', '0', 'business:sidebar:add', '#', 'admin', '2022-09-16 11:21:53', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1570612161132990467, '侧边栏配置修改', 1570612161132990464, 3, '#', '', NULL, 1, 0, 'F', '0', '0', 'business:sidebar:edit', '#', 'admin', '2022-09-16 11:21:53', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1570612161132990468, '侧边栏配置删除', 1570612161132990464, 4, '#', '', NULL, 1, 0, 'F', '0', '0', 'business:sidebar:remove', '#', 'admin', '2022-09-16 11:21:53', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1570612161132990469, '侧边栏配置导出', 1570612161132990464, 5, '#', '', NULL, 1, 0, 'F', '0', '0', 'business:sidebar:export', '#', 'admin', '2022-09-16 11:21:53', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1575498935308333056, '收藏夹信息', 1559071670050615298, 1, '/collection/information', 'business/collection/info', NULL, 1, 0, 'C', '0', '0', 'collection:information:list', 'job', 'admin', '2022-09-29 23:25:55', 'aurora', '2023-03-13 20:24:43', '收藏夹信息菜单');
INSERT INTO `sys_menu` VALUES (1575498935308333057, '收藏夹信息查询', 1575498935308333056, 1, '#', '', NULL, 1, 0, 'F', '0', '0', 'collection:information:query', '#', 'admin', '2022-09-29 23:25:55', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1575498935308333058, '收藏夹信息新增', 1575498935308333056, 2, '#', '', NULL, 1, 0, 'F', '0', '0', 'collection:information:add', '#', 'admin', '2022-09-29 23:25:55', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1575498935308333059, '收藏夹信息修改', 1575498935308333056, 3, '#', '', NULL, 1, 0, 'F', '0', '0', 'collection:information:edit', '#', 'admin', '2022-09-29 23:25:55', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1575498935308333060, '收藏夹信息删除', 1575498935308333056, 4, '#', '', NULL, 1, 0, 'F', '0', '0', 'collection:information:remove', '#', 'admin', '2022-09-29 23:25:55', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1575498935308333061, '收藏夹信息导出', 1575498935308333056, 5, '#', '', NULL, 1, 0, 'F', '0', '0', 'collection:information:export', '#', 'admin', '2022-09-29 23:25:55', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1579451198515113984, '点赞数据', 1559071832202407937, 1, 'record', 'business/fabulous/record', NULL, 1, 0, 'C', '0', '0', 'fabulous:record:list', 'button', 'admin', '2022-10-10 20:50:07', 'aurora', '2023-04-12 20:47:20', '点赞菜单');
INSERT INTO `sys_menu` VALUES (1579451198515113985, '点赞查询', 1579451198515113984, 1, '#', '', NULL, 1, 0, 'F', '0', '0', 'fabulous:record:query', '#', 'admin', '2022-10-10 20:50:07', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1579451198515113986, '点赞新增', 1579451198515113984, 2, '#', '', NULL, 1, 0, 'F', '0', '0', 'fabulous:record:add', '#', 'admin', '2022-10-10 20:50:07', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1579451198515113987, '点赞修改', 1579451198515113984, 3, '#', '', NULL, 1, 0, 'F', '0', '0', 'fabulous:record:edit', '#', 'admin', '2022-10-10 20:50:07', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1579451198515113988, '点赞删除', 1579451198515113984, 4, '#', '', NULL, 1, 0, 'F', '0', '0', 'fabulous:record:remove', '#', 'admin', '2022-10-10 20:50:07', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1579451198515113989, '点赞导出', 1579451198515113984, 5, '#', '', NULL, 1, 0, 'F', '0', '0', 'fabulous:record:export', '#', 'admin', '2022-10-10 20:50:07', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1583288960459567105, '系统配置', 0, 3, 'to/configure', NULL, NULL, 1, 0, 'M', '0', '0', NULL, '配置管理', 'aurora', '2022-10-21 10:47:59', 'aurora', '2023-03-13 21:52:46', '');
INSERT INTO `sys_menu` VALUES (1583342505409998848, '网站文档', 1583288960459567105, 1, 'siteFile', 'business/configure/site-file', NULL, 1, 0, 'C', '0', '0', 'site:siteFile:list', 'documentation', 'admin', '2022-10-21 14:32:56', 'aurora', '2023-03-13 21:44:37', '网站文档菜单');
INSERT INTO `sys_menu` VALUES (1583342505409998849, '网站文档查询', 1583342505409998848, 1, '#', '', NULL, 1, 0, 'F', '0', '0', 'site:siteFile:query', '#', 'admin', '2022-10-21 14:32:56', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1583342505409998850, '网站文档新增', 1583342505409998848, 2, '#', '', NULL, 1, 0, 'F', '0', '0', 'site:siteFile:add', '#', 'admin', '2022-10-21 14:32:56', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1583342505409998851, '网站文档修改', 1583342505409998848, 3, '#', '', NULL, 1, 0, 'F', '0', '0', 'site:siteFile:edit', '#', 'admin', '2022-10-21 14:32:56', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1583342505409998852, '网站文档删除', 1583342505409998848, 4, '#', '', NULL, 1, 0, 'F', '0', '0', 'site:siteFile:remove', '#', 'admin', '2022-10-21 14:32:56', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1583342505409998853, '网站文档导出', 1583342505409998848, 5, '#', '', NULL, 1, 0, 'F', '0', '0', 'site:siteFile:export', '#', 'admin', '2022-10-21 14:32:56', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1583349448128163840, '工具信息', 1559071670050615298, 1, 'toolInfo', 'business/configure/tool-info', NULL, 1, 0, 'C', '0', '0', 'business:toolInfo:list', 'nested', 'admin', '2022-10-21 14:49:14', 'aurora', '2023-03-13 21:45:47', '工具信息菜单');
INSERT INTO `sys_menu` VALUES (1583349448128163841, '工具信息查询', 1583349448128163840, 1, '#', '', NULL, 1, 0, 'F', '0', '0', 'business:toolInfo:query', '#', 'admin', '2022-10-21 14:49:14', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1583349448128163842, '工具信息新增', 1583349448128163840, 2, '#', '', NULL, 1, 0, 'F', '0', '0', 'business:toolInfo:add', '#', 'admin', '2022-10-21 14:49:14', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1583349448128163843, '工具信息修改', 1583349448128163840, 3, '#', '', NULL, 1, 0, 'F', '0', '0', 'business:toolInfo:edit', '#', 'admin', '2022-10-21 14:49:14', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1583349448128163844, '工具信息删除', 1583349448128163840, 4, '#', '', NULL, 1, 0, 'F', '0', '0', 'business:toolInfo:remove', '#', 'admin', '2022-10-21 14:49:14', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1583349448128163845, '工具信息导出', 1583349448128163840, 5, '#', '', NULL, 1, 0, 'F', '0', '0', 'business:toolInfo:export', '#', 'admin', '2022-10-21 14:49:14', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1588065146347773952, '用户消息', 1559071670050615298, 1, 'userInfo', 'business/newsUserInfo', NULL, 1, 0, 'C', '0', '0', 'news:userInfo:list', 'form', 'admin', '2022-11-03 15:16:48', 'aurora', '2022-11-03 15:35:07', '用户消息菜单');
INSERT INTO `sys_menu` VALUES (1588065146347773953, '用户消息查询', 1588065146347773952, 1, '#', '', NULL, 1, 0, 'F', '0', '0', 'news:userInfo:query', '#', 'admin', '2022-11-03 15:16:48', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1588065146347773954, '用户消息新增', 1588065146347773952, 2, '#', '', NULL, 1, 0, 'F', '0', '0', 'news:userInfo:add', '#', 'admin', '2022-11-03 15:16:48', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1588065146347773955, '用户消息修改', 1588065146347773952, 3, '#', '', NULL, 1, 0, 'F', '0', '0', 'news:userInfo:edit', '#', 'admin', '2022-11-03 15:16:48', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1588065146347773956, '用户消息删除', 1588065146347773952, 4, '#', '', NULL, 1, 0, 'F', '0', '0', 'news:userInfo:remove', '#', 'admin', '2022-11-03 15:16:48', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1588065146347773957, '用户消息导出', 1588065146347773952, 5, '#', '', NULL, 1, 0, 'F', '0', '0', 'news:userInfo:export', '#', 'admin', '2022-11-03 15:16:48', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1588069973022793728, '文章评论', 1559071670050615298, 1, 'comment', 'business/comment/article-comment', NULL, 1, 0, 'C', '0', '0', 'system:comment:list', 'message', 'admin', '2022-11-03 15:27:32', 'aurora', '2022-11-03 15:36:03', '文章评论菜单');
INSERT INTO `sys_menu` VALUES (1588069973022793729, '文章评论查询', 1588069973022793728, 1, '#', '', NULL, 1, 0, 'F', '0', '0', 'article:comment:query', '#', 'admin', '2022-11-03 15:27:32', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1588069973022793730, '文章评论新增', 1588069973022793728, 2, '#', '', NULL, 1, 0, 'F', '0', '0', 'article:comment:add', '#', 'admin', '2022-11-03 15:27:32', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1588069973022793731, '文章评论修改', 1588069973022793728, 3, '#', '', NULL, 1, 0, 'F', '0', '0', 'article:comment:edit', '#', 'admin', '2022-11-03 15:27:32', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1588069973022793732, '文章评论删除', 1588069973022793728, 4, '#', '', NULL, 1, 0, 'F', '0', '0', 'article:comment:remove', '#', 'admin', '2022-11-03 15:27:32', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1588069973022793733, '文章评论导出', 1588069973022793728, 5, '#', '', NULL, 1, 0, 'F', '0', '0', 'article:comment:export', '#', 'admin', '2022-11-03 15:27:32', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1627500976322580480, '屏蔽池库', 1559071832202407937, 1, '/shieldWord', 'business/shield/shield-word', NULL, 1, 0, 'C', '0', '0', 'shield:shieldWord:list', 'eye', 'admin', '2023-02-20 10:52:38', 'aurora', '2023-03-13 21:46:20', '屏蔽词库菜单');
INSERT INTO `sys_menu` VALUES (1627500976322580481, '屏蔽词库查询', 1627500976322580480, 1, '#', '', NULL, 1, 0, 'F', '0', '0', 'shield:shieldWord:query', '#', 'admin', '2023-02-20 10:52:38', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1627500976322580482, '屏蔽词库新增', 1627500976322580480, 2, '#', '', NULL, 1, 0, 'F', '0', '0', 'shield:shieldWord:add', '#', 'admin', '2023-02-20 10:52:38', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1627500976322580483, '屏蔽词库修改', 1627500976322580480, 3, '#', '', NULL, 1, 0, 'F', '0', '0', 'shield:shieldWord:edit', '#', 'admin', '2023-02-20 10:52:38', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1627500976322580484, '屏蔽词库删除', 1627500976322580480, 4, '#', '', NULL, 1, 0, 'F', '0', '0', 'shield:shieldWord:remove', '#', 'admin', '2023-02-20 10:52:38', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1627500976322580485, '屏蔽词库导出', 1627500976322580480, 5, '#', '', NULL, 1, 0, 'F', '0', '0', 'shield:shieldWord:export', '#', 'admin', '2023-02-20 10:52:38', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1633636367421382656, '话题信息', 1635253301085544449, 1, 'topicInfo', 'business/topic/topic-info', NULL, 1, 0, 'C', '0', '0', 'topic:info:list', '#', 'admin', '2023-03-09 10:17:05', 'aurora', '2023-03-13 20:16:01', '话题信息菜单');
INSERT INTO `sys_menu` VALUES (1633636367421382657, '话题信息查询', 1633636367421382656, 1, '#', '', NULL, 1, 0, 'F', '0', '0', 'topic:info:query', '#', 'admin', '2023-03-09 10:17:05', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1633636367421382658, '话题信息新增', 1633636367421382656, 2, '#', '', NULL, 1, 0, 'F', '0', '0', 'topic:info:add', '#', 'admin', '2023-03-09 10:17:05', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1633636367421382659, '话题信息修改', 1633636367421382656, 3, '#', '', NULL, 1, 0, 'F', '0', '0', 'topic:info:edit', '#', 'admin', '2023-03-09 10:17:05', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1633636367421382660, '话题信息删除', 1633636367421382656, 4, '#', '', NULL, 1, 0, 'F', '0', '0', 'topic:info:remove', '#', 'admin', '2023-03-09 10:17:05', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1633636367421382661, '话题信息导出', 1633636367421382656, 5, '#', '', NULL, 1, 0, 'F', '0', '0', 'topic:info:export', '#', 'admin', '2023-03-09 10:17:05', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1633725175638663168, '参与记录', 1635253301085544449, 1, 'joinInfo', 'business/topic/topic-join-info', NULL, 1, 0, 'C', '1', '1', 'topicJoin:joinInfo:list', '#', 'admin', '2023-03-09 18:21:42', 'ziwei', '2024-11-25 10:27:57', '话题参与信息菜单');
INSERT INTO `sys_menu` VALUES (1633725175638663169, '话题参与信息查询', 1633725175638663168, 1, '#', '', NULL, 1, 0, 'F', '0', '0', 'topicJoin:joinInfo:query', '#', 'admin', '2023-03-09 18:21:42', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1633725175638663170, '话题参与信息新增', 1633725175638663168, 2, '#', '', NULL, 1, 0, 'F', '0', '0', 'topicJoin:joinInfo:add', '#', 'admin', '2023-03-09 18:21:42', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1633725175638663171, '话题参与信息修改', 1633725175638663168, 3, '#', '', NULL, 1, 0, 'F', '0', '0', 'topicJoin:joinInfo:edit', '#', 'admin', '2023-03-09 18:21:42', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1633725175638663172, '话题参与信息删除', 1633725175638663168, 4, '#', '', NULL, 1, 0, 'F', '0', '0', 'topicJoin:joinInfo:remove', '#', 'admin', '2023-03-09 18:21:42', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1633725175638663173, '话题参与信息导出', 1633725175638663168, 5, '#', '', NULL, 1, 0, 'F', '0', '0', 'topicJoin:joinInfo:export', '#', 'admin', '2023-03-09 18:21:42', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1635253301085544449, '话题管理', 1559071670050615298, 3, '/topic', NULL, NULL, 1, 0, 'M', '1', '1', NULL, 'message', 'aurora', '2023-03-13 20:15:43', 'ziwei', '2024-11-25 10:27:46', '');
INSERT INTO `sys_menu` VALUES (1635280042139136002, '数据统计', 0, 7, '/statistics', NULL, NULL, 1, 0, 'M', '1', '1', NULL, '统计', 'aurora', '2023-03-13 22:01:59', 'ziwei', '2024-10-21 11:08:33', '');
INSERT INTO `sys_menu` VALUES (1638717119014952960, '私信记录', 1559071670050615298, 1, 'user', 'business/private-user', NULL, 1, 0, 'C', '0', '0', 'private:user:list', 'guide', 'admin', '2023-03-23 12:39:05', 'aurora', '2023-04-12 20:46:07', '私信记录菜单');
INSERT INTO `sys_menu` VALUES (1638717119014952964, '私信记录删除', 1638717119014952960, 4, '#', '', NULL, 1, 0, 'F', '0', '0', 'private:user:remove', '#', 'admin', '2023-03-23 12:39:05', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1643095134993182720, '充值配置', 1559071832202407937, 1, 'rechargeInfo', 'business/configure/recharge-info', NULL, 1, 0, 'C', '0', '0', 'business:rechargeInfo:list', '充值', 'admin', '2023-04-12 20:45:05', 'aurora', '2023-04-12 20:46:33', '充值信息菜单');
INSERT INTO `sys_menu` VALUES (1643095134993182721, '充值信息查询', 1643095134993182720, 1, '#', '', NULL, 1, 0, 'F', '0', '0', 'business:rechargeInfo:query', '#', 'admin', '2023-04-12 20:45:05', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1643095134993182722, '充值信息新增', 1643095134993182720, 2, '#', '', NULL, 1, 0, 'F', '0', '0', 'business:rechargeInfo:add', '#', 'admin', '2023-04-12 20:45:05', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1643095134993182723, '充值信息修改', 1643095134993182720, 3, '#', '', NULL, 1, 0, 'F', '0', '0', 'business:rechargeInfo:edit', '#', 'admin', '2023-04-12 20:45:06', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1643095134993182724, '充值信息删除', 1643095134993182720, 4, '#', '', NULL, 1, 0, 'F', '0', '0', 'business:rechargeInfo:remove', '#', 'admin', '2023-04-12 20:45:06', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1643095134993182725, '充值信息导出', 1643095134993182720, 5, '#', '', NULL, 1, 0, 'F', '0', '0', 'business:rechargeInfo:export', '#', 'admin', '2023-04-12 20:45:06', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1643097808857788416, '用户订单', 1559071670050615298, 1, 'order', 'business/user/user-order', NULL, 1, 0, 'C', '0', '0', 'business:order:list', 'shopping', 'admin', '2023-04-04 11:56:54', 'aurora', '2023-04-04 12:26:50', '用户订单菜单');
INSERT INTO `sys_menu` VALUES (1643097808857788417, '用户订单查询', 1643097808857788416, 1, '#', '', NULL, 1, 0, 'F', '0', '0', 'business:order:query', '#', 'admin', '2023-04-04 11:56:54', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1643097808857788418, '用户订单新增', 1643097808857788416, 2, '#', '', NULL, 1, 0, 'F', '0', '0', 'business:order:add', '#', 'admin', '2023-04-04 11:56:54', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1643097808857788419, '用户订单修改', 1643097808857788416, 3, '#', '', NULL, 1, 0, 'F', '0', '0', 'business:order:edit', '#', 'admin', '2023-04-04 11:56:54', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1643097808857788420, '用户订单删除', 1643097808857788416, 4, '#', '', NULL, 1, 0, 'F', '0', '0', 'business:order:remove', '#', 'admin', '2023-04-04 11:56:54', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1643097808857788421, '用户订单导出', 1643097808857788416, 5, '#', '', NULL, 1, 0, 'F', '0', '0', 'business:order:export', '#', 'admin', '2023-04-04 11:56:54', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1645281581871460352, '用户签到', 1559071670050615298, 1, 'report', 'business/user/report', NULL, 1, 0, 'C', '0', '0', 'business:user:report:list', 'button', 'admin', '2023-04-10 12:39:06', 'aurora', '2023-04-12 20:46:21', '用户签到菜单');
INSERT INTO `sys_menu` VALUES (1645281581871460353, '用户签到查询', 1645281581871460352, 1, '#', '', NULL, 1, 0, 'F', '0', '0', 'business:user:report:query', '#', 'admin', '2023-04-10 12:39:07', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1645281581871460354, '用户签到新增', 1645281581871460352, 2, '#', '', NULL, 1, 0, 'F', '0', '0', 'business:user:report:add', '#', 'admin', '2023-04-10 12:39:07', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1645281581871460355, '用户签到修改', 1645281581871460352, 3, '#', '', NULL, 1, 0, 'F', '0', '0', 'business:user:report:edit', '#', 'admin', '2023-04-10 12:39:07', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1645281581871460356, '用户签到删除', 1645281581871460352, 4, '#', '', NULL, 1, 0, 'F', '0', '0', 'business:user:report:remove', '#', 'admin', '2023-04-10 12:39:07', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1645281581871460357, '用户签到导出', 1645281581871460352, 5, '#', '', NULL, 1, 0, 'F', '0', '0', 'business:user:report:export', '#', 'admin', '2023-04-10 12:39:07', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1647786462837522432, '用户反馈', 1559071832202407937, 1, 'feedback', 'business/feedback', NULL, 1, 0, 'C', '0', '0', 'business:feedback:list', 'form', 'admin', '2023-04-18 18:41:17', 'aurora', '2023-04-18 18:48:17', '用户反馈菜单');
INSERT INTO `sys_menu` VALUES (1647786462837522433, '用户反馈查询', 1647786462837522432, 1, '#', '', NULL, 1, 0, 'F', '0', '0', 'business:feedback:query', '#', 'admin', '2023-04-18 18:41:17', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1647786462837522434, '用户反馈新增', 1647786462837522432, 2, '#', '', NULL, 1, 0, 'F', '0', '0', 'business:feedback:add', '#', 'admin', '2023-04-18 18:41:17', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1647786462837522435, '用户反馈修改', 1647786462837522432, 3, '#', '', NULL, 1, 0, 'F', '0', '0', 'business:feedback:edit', '#', 'admin', '2023-04-18 18:41:17', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1647786462837522436, '用户反馈删除', 1647786462837522432, 4, '#', '', NULL, 1, 0, 'F', '0', '0', 'business:feedback:remove', '#', 'admin', '2023-04-18 18:41:17', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1647786462837522437, '用户反馈导出', 1647786462837522432, 5, '#', '', NULL, 1, 0, 'F', '0', '0', 'business:feedback:export', '#', 'admin', '2023-04-18 18:41:17', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1648209886288121856, '搜索记录', 1559071670050615298, 1, 'records', 'business/search-records', NULL, 1, 0, 'C', '0', '0', 'business:search:records:list', 'search', 'admin', '2023-04-18 18:41:23', 'aurora', '2023-04-18 18:48:39', '搜索记录菜单');
INSERT INTO `sys_menu` VALUES (1648209886288121857, '搜索记录查询', 1648209886288121856, 1, '#', '', NULL, 1, 0, 'F', '0', '0', 'business:search:records:query', '#', 'admin', '2023-04-18 18:41:23', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1648209886288121858, '搜索记录新增', 1648209886288121856, 2, '#', '', NULL, 1, 0, 'F', '0', '0', 'business:search:records:add', '#', 'admin', '2023-04-18 18:41:23', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1648209886288121859, '搜索记录修改', 1648209886288121856, 3, '#', '', NULL, 1, 0, 'F', '0', '0', 'business:search:records:edit', '#', 'admin', '2023-04-18 18:41:23', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1648209886288121860, '搜索记录删除', 1648209886288121856, 4, '#', '', NULL, 1, 0, 'F', '0', '0', 'business:search:records:remove', '#', 'admin', '2023-04-18 18:41:23', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1648209886288121861, '搜索记录导出', 1648209886288121856, 5, '#', '', NULL, 1, 0, 'F', '0', '0', 'business:search:records:export', '#', 'admin', '2023-04-18 18:41:23', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1650031562833575936, '系统消息', 1559071832202407937, 1, 'systemInfo', 'business/news/newsSystemInfo', NULL, 1, 0, 'C', '0', '0', 'business:news:systemInfo:list', 'guide', 'admin', '2023-05-04 16:03:05', 'aurora', '2023-05-04 21:01:33', '系统消息菜单');
INSERT INTO `sys_menu` VALUES (1650031562833575937, '系统消息查询', 1650031562833575936, 1, '#', '', NULL, 1, 0, 'F', '0', '0', 'business:news:systemInfo:query', '#', 'admin', '2023-05-04 16:03:05', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1650031562833575938, '系统消息新增', 1650031562833575936, 2, '#', '', NULL, 1, 0, 'F', '0', '0', 'business:news:systemInfo:add', '#', 'admin', '2023-05-04 16:03:05', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1650031562833575939, '系统消息修改', 1650031562833575936, 3, '#', '', NULL, 1, 0, 'F', '0', '0', 'business:news:systemInfo:edit', '#', 'admin', '2023-05-04 16:03:05', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1650031562833575940, '系统消息删除', 1650031562833575936, 4, '#', '', NULL, 1, 0, 'F', '0', '0', 'business:news:systemInfo:remove', '#', 'admin', '2023-05-04 16:03:06', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1650031562833575941, '系统消息导出', 1650031562833575936, 5, '#', '', NULL, 1, 0, 'F', '0', '0', 'business:news:systemInfo:export', '#', 'admin', '2023-05-04 16:03:06', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1650319922586042368, '用户浏览历史', 1559071832202407937, 1, 'history', 'business/user/browsingHistory', NULL, 1, 0, 'C', '0', '0', 'browsing:history:list', 'bug', 'admin', '2023-05-04 16:03:34', 'aurora', '2023-05-04 21:01:44', '用户浏览历史菜单');
INSERT INTO `sys_menu` VALUES (1650319922586042369, '用户浏览历史查询', 1650319922586042368, 1, '#', '', NULL, 1, 0, 'F', '0', '0', 'browsing:history:query', '#', 'admin', '2023-05-04 16:03:34', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1650319922586042370, '用户浏览历史新增', 1650319922586042368, 2, '#', '', NULL, 1, 0, 'F', '0', '0', 'browsing:history:add', '#', 'admin', '2023-05-04 16:03:34', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1650319922586042371, '用户浏览历史修改', 1650319922586042368, 3, '#', '', NULL, 1, 0, 'F', '0', '0', 'browsing:history:edit', '#', 'admin', '2023-05-04 16:03:34', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1650319922586042372, '用户浏览历史删除', 1650319922586042368, 4, '#', '', NULL, 1, 0, 'F', '0', '0', 'browsing:history:remove', '#', 'admin', '2023-05-04 16:03:34', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1650319922586042373, '用户浏览历史导出', 1650319922586042368, 5, '#', '', NULL, 1, 0, 'F', '0', '0', 'browsing:history:export', '#', 'admin', '2023-05-04 16:03:34', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1650383488303304704, '名言分组', 1654109240779915266, 1, 'group', 'business/dictum/dictumGroup', NULL, 1, 0, 'C', '0', '0', 'business:dictum:group:list', 'cascader', 'admin', '2023-05-04 16:04:09', 'aurora', '2023-05-04 21:02:47', '名言分组菜单');
INSERT INTO `sys_menu` VALUES (1650383488303304705, '名言分组查询', 1650383488303304704, 1, '#', '', NULL, 1, 0, 'F', '0', '0', 'business:dictum:group:query', '#', 'admin', '2023-05-04 16:04:09', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1650383488303304706, '名言分组新增', 1650383488303304704, 2, '#', '', NULL, 1, 0, 'F', '0', '0', 'business:dictum:group:add', '#', 'admin', '2023-05-04 16:04:09', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1650383488303304707, '名言分组修改', 1650383488303304704, 3, '#', '', NULL, 1, 0, 'F', '0', '0', 'business:dictum:group:edit', '#', 'admin', '2023-05-04 16:04:09', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1650383488303304708, '名言分组删除', 1650383488303304704, 4, '#', '', NULL, 1, 0, 'F', '0', '0', 'business:dictum:group:remove', '#', 'admin', '2023-05-04 16:04:09', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1650383488303304709, '名言分组导出', 1650383488303304704, 5, '#', '', NULL, 1, 0, 'F', '0', '0', 'business:dictum:group:export', '#', 'admin', '2023-05-04 16:04:09', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1650388427805102080, '名言信息', 1654109240779915266, 1, 'info', 'business/dictum/dictumInfo', NULL, 1, 0, 'C', '0', '0', 'business:dictum:info:list', 'log', 'admin', '2023-05-04 16:04:35', 'aurora', '2023-05-04 21:03:04', '名言信息菜单');
INSERT INTO `sys_menu` VALUES (1650388427805102081, '名言信息查询', 1650388427805102080, 1, '#', '', NULL, 1, 0, 'F', '0', '0', 'business:dictum:info:query', '#', 'admin', '2023-05-04 16:04:35', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1650388427805102082, '名言信息新增', 1650388427805102080, 2, '#', '', NULL, 1, 0, 'F', '0', '0', 'business:dictum:info:add', '#', 'admin', '2023-05-04 16:04:35', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1650388427805102083, '名言信息修改', 1650388427805102080, 3, '#', '', NULL, 1, 0, 'F', '0', '0', 'business:dictum:info:edit', '#', 'admin', '2023-05-04 16:04:35', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1650388427805102084, '名言信息删除', 1650388427805102080, 4, '#', '', NULL, 1, 0, 'F', '0', '0', 'business:dictum:info:remove', '#', 'admin', '2023-05-04 16:04:35', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1650388427805102085, '名言信息导出', 1650388427805102080, 5, '#', '', NULL, 1, 0, 'F', '0', '0', 'system:info:export', '#', 'admin', '2023-05-04 16:04:35', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1650495212784291840, '名言专辑', 1654109240779915266, 1, 'album', 'business/dictum/dictumAlbum', NULL, 1, 0, 'C', '0', '0', 'business:dictum:album:list', 'icon', 'admin', '2023-05-04 16:05:02', 'aurora', '2023-05-04 21:03:18', '名言专辑菜单');
INSERT INTO `sys_menu` VALUES (1650495212784291841, '名言专辑查询', 1650495212784291840, 1, '#', '', NULL, 1, 0, 'F', '0', '0', 'business:dictum:album:query', '#', 'admin', '2023-05-04 16:05:02', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1650495212784291842, '名言专辑新增', 1650495212784291840, 2, '#', '', NULL, 1, 0, 'F', '0', '0', 'business:dictum:album:add', '#', 'admin', '2023-05-04 16:05:02', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1650495212784291843, '名言专辑修改', 1650495212784291840, 3, '#', '', NULL, 1, 0, 'F', '0', '0', 'business:dictum:album:edit', '#', 'admin', '2023-05-04 16:05:02', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1650495212784291844, '名言专辑删除', 1650495212784291840, 4, '#', '', NULL, 1, 0, 'F', '0', '0', 'business:dictum:album:remove', '#', 'admin', '2023-05-04 16:05:02', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1650495212784291845, '名言专辑导出', 1650495212784291840, 5, '#', '', NULL, 1, 0, 'F', '0', '0', 'business:dictum:album:export', '#', 'admin', '2023-05-04 16:05:02', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1654109240779915266, '名言管理', 1559071670050615298, 3, '/dicoum', NULL, NULL, 1, 0, 'M', '0', '0', NULL, 'form', 'aurora', '2023-05-04 21:02:30', 'aurora', '2023-05-04 21:02:30', '');
INSERT INTO `sys_menu` VALUES (1774271697071702017, '爱情小站', 0, 99, '/lover', NULL, NULL, 1, 0, 'M', '0', '0', NULL, 'date', 'aurora', '2024-03-31 11:05:12', 'aurora', '2024-03-31 11:05:12', '');
INSERT INTO `sys_menu` VALUES (1774272350540070913, '时光相册', 1774271697071702017, 2, 'album', 'lover/album/index', NULL, 1, 0, 'C', '0', '0', 'lover:album:list', 'cascader', 'aurora', '2024-03-31 11:07:47', 'aurora', '2024-03-31 11:07:47', '');
INSERT INTO `sys_menu` VALUES (1774272573370859522, '网站信息', 1774271697071702017, 1, 'info', 'lover/info/index', NULL, 1, 0, 'C', '0', '0', 'basic:info:list', 'bug', 'aurora', '2024-03-31 11:08:41', 'aurora', '2024-03-31 11:09:23', '');
INSERT INTO `sys_menu` VALUES (1774273075739426818, '爱情记录', 1774271697071702017, 3, 'record', 'lover/record/index', NULL, 1, 0, 'C', '0', '0', 'lover:record:list', 'clipboard', 'aurora', '2024-03-31 11:10:40', 'aurora', '2024-03-31 11:10:40', '');
INSERT INTO `sys_menu` VALUES (1774273205788016641, '爱情清单', 1774271697071702017, 4, 'repertoire', 'lover/repertoire/index', NULL, 1, 0, 'C', '0', '0', 'lover:repertoire:list', 'button', 'aurora', '2024-03-31 11:11:11', 'aurora', '2024-03-31 11:11:11', '');
INSERT INTO `sys_menu` VALUES (1774273328861478913, '轮播图', 1774271697071702017, 5, 'carousel', 'lover/carousel/index', NULL, 1, 0, 'C', '0', '0', 'lover:carousel:list', 'color', 'aurora', '2024-03-31 11:11:41', 'ziwei', '2024-03-31 19:17:31', '');
INSERT INTO `sys_menu` VALUES (1774273433257705474, '祝福语', 1774271697071702017, 6, 'comment', 'lover/comment/index', NULL, 1, 0, 'C', '0', '0', 'lover:comment:list', 'clipboard', 'aurora', '2024-03-31 11:12:06', 'aurora', '2024-03-31 11:12:06', '');
INSERT INTO `sys_menu` VALUES (1774273549771276289, '爱情树', 1774271697071702017, 7, 'tree', 'lover/tree/index', NULL, 1, 0, 'C', '0', '0', 'lover:tree:list', 'cascader', 'aurora', '2024-03-31 11:12:33', 'ziwei', '2024-03-31 19:15:18', '');
INSERT INTO `sys_menu` VALUES (1774273672811184130, '关于我们', 1774271697071702017, 8, 'about', 'lover/about/index', NULL, 1, 0, 'C', '0', '0', 'lover:about:list', 'build', 'aurora', '2024-03-31 11:13:03', 'aurora', '2024-03-31 11:13:03', '');
INSERT INTO `sys_menu` VALUES (1774273778486673410, '基本配置', 1774271697071702017, 9, 'config', 'lover/config/index', NULL, 1, 0, 'C', '0', '0', 'lover:config:list', 'dict', 'aurora', '2024-03-31 11:13:28', 'aurora', '2024-03-31 11:13:28', '');
INSERT INTO `sys_menu` VALUES (1774273778486673411, '系统任务', 1583288960459567105, 4, 'configure/system-task', 'business/configure/system-task', NULL, 1, 0, 'C', '0', '0', NULL, 'skill', 'ziwei', '2024-11-17 15:30:00', 'ziwei', '2024-11-17 16:17:32', '');

-- ----------------------------
-- Table structure for sys_notice
-- ----------------------------
DROP TABLE IF EXISTS `sys_notice`;
CREATE TABLE `sys_notice`  (
  `notice_id` int NOT NULL COMMENT '公告ID',
  `notice_title` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_cs_0900_ai_ci NOT NULL COMMENT '公告标题',
  `notice_type` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_cs_0900_ai_ci NOT NULL COMMENT '公告类型（1通知 2公告）',
  `notice_content` longblob NULL COMMENT '公告内容',
  `status` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_cs_0900_ai_ci NULL DEFAULT '0' COMMENT '公告状态（0正常 1关闭）',
  `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_cs_0900_ai_ci NULL DEFAULT '' COMMENT '创建者',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_cs_0900_ai_ci NULL DEFAULT '' COMMENT '更新者',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_cs_0900_ai_ci NULL DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`notice_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_cs_0900_ai_ci COMMENT = '通知公告表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_notice
-- ----------------------------
INSERT INTO `sys_notice` VALUES (1, '温馨提醒：2018-07-01 新版本发布啦', '2', 0xE696B0E78988E69CACE58685E5AEB9, '0', 'admin', '2022-06-01 22:11:39', '', NULL, '管理员');
INSERT INTO `sys_notice` VALUES (2, '维护通知：2018-07-01 系统凌晨维护', '1', 0xE7BBB4E68AA4E58685E5AEB9, '0', 'admin', '2022-06-01 22:11:39', '', NULL, '管理员');

-- ----------------------------
-- Table structure for sys_oper_log
-- ----------------------------
DROP TABLE IF EXISTS `sys_oper_log`;
CREATE TABLE `sys_oper_log`  (
  `oper_id` bigint NOT NULL AUTO_INCREMENT COMMENT '日志主键',
  `title` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_cs_0900_ai_ci NULL DEFAULT '' COMMENT '模块标题',
  `business_type` int NULL DEFAULT 0 COMMENT '业务类型（0其它 1新增 2修改 3删除）',
  `method` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_cs_0900_ai_ci NULL DEFAULT '' COMMENT '方法名称',
  `request_method` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_cs_0900_ai_ci NULL DEFAULT '' COMMENT '请求方式',
  `operator_type` int NULL DEFAULT 0 COMMENT '操作类别（0其它 1后台用户 2手机端用户）',
  `oper_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_cs_0900_ai_ci NULL DEFAULT '' COMMENT '操作人员',
  `dept_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_cs_0900_ai_ci NULL DEFAULT '' COMMENT '部门名称',
  `oper_url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_cs_0900_ai_ci NULL DEFAULT '' COMMENT '请求URL',
  `oper_ip` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_cs_0900_ai_ci NULL DEFAULT '' COMMENT '主机地址',
  `oper_location` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_cs_0900_ai_ci NULL DEFAULT '' COMMENT '操作地点',
  `oper_param` varchar(2000) CHARACTER SET utf8mb4 COLLATE utf8mb4_cs_0900_ai_ci NULL DEFAULT '' COMMENT '请求参数',
  `json_result` varchar(2000) CHARACTER SET utf8mb4 COLLATE utf8mb4_cs_0900_ai_ci NULL DEFAULT '' COMMENT '返回参数',
  `status` int NULL DEFAULT 0 COMMENT '操作状态（0正常 1异常）',
  `error_msg` varchar(2000) CHARACTER SET utf8mb4 COLLATE utf8mb4_cs_0900_ai_ci NULL DEFAULT '' COMMENT '错误消息',
  `oper_time` datetime NULL DEFAULT NULL COMMENT '操作时间',
  PRIMARY KEY (`oper_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1832581801389240487 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_cs_0900_ai_ci COMMENT = '操作日志记录' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_oper_log
-- ----------------------------

-- ----------------------------
-- Table structure for sys_oss
-- ----------------------------
DROP TABLE IF EXISTS `sys_oss`;
CREATE TABLE `sys_oss`  (
  `oss_id` bigint NOT NULL AUTO_INCREMENT COMMENT '对象存储主键',
  `file_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_cs_0900_ai_ci NOT NULL DEFAULT '' COMMENT '文件名',
  `original_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_cs_0900_ai_ci NOT NULL DEFAULT '' COMMENT '原名',
  `file_suffix` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_cs_0900_ai_ci NOT NULL DEFAULT '' COMMENT '文件后缀名',
  `url` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_cs_0900_ai_ci NOT NULL COMMENT 'URL地址',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_cs_0900_ai_ci NULL DEFAULT '' COMMENT '上传人',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  `update_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_cs_0900_ai_ci NULL DEFAULT '' COMMENT '更新人',
  `service` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_cs_0900_ai_ci NOT NULL DEFAULT 'minio' COMMENT '服务商',
  PRIMARY KEY (`oss_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1830138396180656198 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_cs_0900_ai_ci COMMENT = 'OSS对象存储表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_oss
-- ----------------------------
INSERT INTO `sys_oss` VALUES (1830138396180656194, 'aurora/2024/11/23/95a18bfad6c34f9e9695dee504de7703.png', 'img.png', '.png', 'https://zsh-aurora.oss-cn-beijing.aliyuncs.com/aurora/2024/11/23/95a18bfad6c34f9e9695dee504de7703.png', '2024-11-23 11:12:13', '用户7632390171', '2024-11-23 11:12:13', '用户7632390171', 'aliyun');
INSERT INTO `sys_oss` VALUES (1830138396180656195, 'aurora/2024/11/25/55eaf93b6f394182b8d39808dac2bbe4.png', 'd4.png', '.png', 'https://zsh-aurora.oss-cn-beijing.aliyuncs.com/aurora/2024/11/25/55eaf93b6f394182b8d39808dac2bbe4.png', '2024-11-25 14:45:09', '美团干饭人', '2024-11-25 14:45:09', '美团干饭人', 'aliyun');
INSERT INTO `sys_oss` VALUES (1830138396180656196, 'aurora/2024/11/25/47a9f015010743c08b9a52529cc7e9d4.jpg', 'v2-17aafa441da3975a2dc763855095a345_r.jpg', '.jpg', 'https://zsh-aurora.oss-cn-beijing.aliyuncs.com/aurora/2024/11/25/47a9f015010743c08b9a52529cc7e9d4.jpg', '2024-11-25 14:47:23', '美团干饭人', '2024-11-25 14:47:23', '美团干饭人', 'aliyun');
INSERT INTO `sys_oss` VALUES (1830138396180656197, 'aurora/2024/11/25/763fe02420e1432580538cf7f735eba3.jpg', 'v2-17aafa441da3975a2dc763855095a345_r.jpg', '.jpg', 'https://zsh-aurora.oss-cn-beijing.aliyuncs.com/aurora/2024/11/25/763fe02420e1432580538cf7f735eba3.jpg', '2024-11-25 14:58:13', '美团干饭人', '2024-11-25 14:58:13', '美团干饭人', 'aliyun');
INSERT INTO `sys_oss` VALUES (1830138396180656198, 'aurora/2024/11/25/a019d43046204f3da8e5988adcd48ea1.jpg', 'v2-17aafa441da3975a2dc763855095a345_r.jpg', '.jpg', 'https://zsh-aurora.oss-cn-beijing.aliyuncs.com/aurora/2024/11/25/a019d43046204f3da8e5988adcd48ea1.jpg', '2024-11-25 15:00:29', '美团干饭人', '2024-11-25 15:00:29', '美团干饭人', 'aliyun');

-- ----------------------------
-- Table structure for sys_oss_config
-- ----------------------------
DROP TABLE IF EXISTS `sys_oss_config`;
CREATE TABLE `sys_oss_config`  (
  `oss_config_id` bigint NOT NULL COMMENT '主建',
  `config_key` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_cs_0900_ai_ci NOT NULL DEFAULT '' COMMENT '配置key',
  `access_key` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_cs_0900_ai_ci NULL DEFAULT '' COMMENT 'accessKey',
  `secret_key` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_cs_0900_ai_ci NULL DEFAULT '' COMMENT '秘钥',
  `bucket_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_cs_0900_ai_ci NULL DEFAULT '' COMMENT '桶名称',
  `prefix` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_cs_0900_ai_ci NULL DEFAULT '' COMMENT '前缀',
  `endpoint` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_cs_0900_ai_ci NULL DEFAULT '' COMMENT '访问站点',
  `is_https` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_cs_0900_ai_ci NULL DEFAULT 'N' COMMENT '是否https（Y=是,N=否）',
  `region` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_cs_0900_ai_ci NULL DEFAULT '' COMMENT '域',
  `status` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_cs_0900_ai_ci NULL DEFAULT '1' COMMENT '状态（0=正常,1=停用）',
  `ext1` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_cs_0900_ai_ci NULL DEFAULT '' COMMENT '扩展字段',
  `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_cs_0900_ai_ci NULL DEFAULT '' COMMENT '创建者',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_cs_0900_ai_ci NULL DEFAULT '' COMMENT '更新者',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_cs_0900_ai_ci NULL DEFAULT NULL COMMENT '备注',
  `domain` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_cs_0900_ai_ci NULL DEFAULT NULL,
  PRIMARY KEY (`oss_config_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_cs_0900_ai_ci COMMENT = '对象存储配置表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_oss_config
-- ----------------------------
INSERT INTO `sys_oss_config` VALUES (1, 'minio', 'ruoyi', 'ruoyi123', 'ruoyi', '', 'http://localhost:9000', 'N', '', '1', '', 'admin', '2022-06-01 22:11:39', 'admin', '2022-09-04 20:30:01', NULL, NULL);
INSERT INTO `sys_oss_config` VALUES (2, 'qiniu', 'XXXXXXXXXXXXXXX', 'XXXXXXXXXXXXXXX', 'ruoyi', '', 'http://XXX.XXXX.com', 'N', 'z0', '1', '', 'admin', '2022-06-01 22:11:39', 'admin', '2022-09-04 20:30:15', NULL, NULL);
INSERT INTO `sys_oss_config` VALUES (3, 'aliyun', 'xxxxxxxxxxxx', 'xxxxxxxxxxxxxxxxx', 'qixid', 'qixidi', 'xxx.com', 'Y', '', '0', '', 'admin', '2022-06-01 22:11:39', 'aurora', '2023-02-03 16:14:08', NULL, NULL);
INSERT INTO `sys_oss_config` VALUES (4, 'qcloud', 'XXXXXXXXXXXXXXX', 'XXXXXXXXXXXXXXX', 'ruoyi-1250000000', '', 'http://cos.ap-beijing.myqcloud.com', 'N', 'ap-beijing', '1', '', 'admin', '2022-06-01 22:11:39', 'admin', '2022-06-01 22:11:39', NULL, NULL);

-- ----------------------------
-- Table structure for sys_post
-- ----------------------------
DROP TABLE IF EXISTS `sys_post`;
CREATE TABLE `sys_post`  (
  `post_id` bigint NOT NULL COMMENT '岗位ID',
  `post_code` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_cs_0900_ai_ci NOT NULL COMMENT '岗位编码',
  `post_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_cs_0900_ai_ci NOT NULL COMMENT '岗位名称',
  `post_sort` int NOT NULL COMMENT '显示顺序',
  `status` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_cs_0900_ai_ci NOT NULL COMMENT '状态（0正常 1停用）',
  `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_cs_0900_ai_ci NULL DEFAULT '' COMMENT '创建者',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_cs_0900_ai_ci NULL DEFAULT '' COMMENT '更新者',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_cs_0900_ai_ci NULL DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`post_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_cs_0900_ai_ci COMMENT = '岗位信息表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_post
-- ----------------------------
INSERT INTO `sys_post` VALUES (1, 'ceo', '董事长', 1, '0', 'admin', '2022-06-01 22:11:39', '', NULL, '');
INSERT INTO `sys_post` VALUES (2, 'se', '项目经理', 2, '0', 'admin', '2022-06-01 22:11:39', '', NULL, '');
INSERT INTO `sys_post` VALUES (3, 'hr', '人力资源', 3, '0', 'admin', '2022-06-01 22:11:39', '', NULL, '');
INSERT INTO `sys_post` VALUES (4, 'user', '普通员工', 4, '0', 'admin', '2022-06-01 22:11:39', '', NULL, '');

-- ----------------------------
-- Table structure for sys_role
-- ----------------------------
DROP TABLE IF EXISTS `sys_role`;
CREATE TABLE `sys_role`  (
  `role_id` bigint NOT NULL AUTO_INCREMENT COMMENT '角色ID',
  `role_name` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_cs_0900_ai_ci NOT NULL COMMENT '角色名称',
  `role_key` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_cs_0900_ai_ci NOT NULL COMMENT '角色权限字符串',
  `role_sort` int NOT NULL COMMENT '显示顺序',
  `data_scope` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_cs_0900_ai_ci NULL DEFAULT '1' COMMENT '数据范围（1：全部数据权限 2：自定数据权限 3：本部门数据权限 4：本部门及以下数据权限）',
  `menu_check_strictly` tinyint(1) NULL DEFAULT 1 COMMENT '菜单树选择项是否关联显示',
  `dept_check_strictly` tinyint(1) NULL DEFAULT 1 COMMENT '部门树选择项是否关联显示',
  `status` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_cs_0900_ai_ci NOT NULL COMMENT '角色状态（0正常 1停用）',
  `del_flag` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_cs_0900_ai_ci NULL DEFAULT '0' COMMENT '删除标志（0代表存在 2代表删除）',
  `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_cs_0900_ai_ci NULL DEFAULT '' COMMENT '创建者',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_cs_0900_ai_ci NULL DEFAULT '' COMMENT '更新者',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_cs_0900_ai_ci NULL DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`role_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1568063062693163012 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_cs_0900_ai_ci COMMENT = '角色信息表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_role
-- ----------------------------
INSERT INTO `sys_role` VALUES (1, '超级管理员', 'admin', 1, '1', 1, 1, '0', '0', 'admin', '2022-06-01 22:11:39', '', NULL, '超级管理员');
INSERT INTO `sys_role` VALUES (2, '普通角色', 'common', 2, '2', 1, 1, '0', '0', 'admin', '2022-06-01 22:11:39', '', NULL, '普通角色');
INSERT INTO `sys_role` VALUES (3, '研发', 'development', 0, '1', 1, 1, '0', '0', 'admin', '2022-09-09 10:25:42', 'aurora', '2023-05-05 09:59:26', '研发部门');
INSERT INTO `sys_role` VALUES (4, '测试', 'test', 0, '1', 1, 1, '0', '0', 'admin', '2022-09-09 10:31:30', 'admin', '2022-09-09 10:58:49', '测试部门');
INSERT INTO `sys_role` VALUES (1568063062693163011, '体验角色', 'taste', 6, '1', 1, 1, '0', '0', 'aurora', '2023-03-29 10:59:21', 'aurora', '2023-05-05 13:15:12', '体验角色');
INSERT INTO `sys_role` VALUES (1568063062693163012, '爱情小站', 'love', 0, '1', 1, 1, '0', '0', 'ziwei', '2024-03-31 19:08:26', 'ziwei', '2024-03-31 19:42:13', '爱情小站管理角色');

-- ----------------------------
-- Table structure for sys_role_dept
-- ----------------------------
DROP TABLE IF EXISTS `sys_role_dept`;
CREATE TABLE `sys_role_dept`  (
  `role_id` bigint NOT NULL COMMENT '角色ID',
  `dept_id` bigint NOT NULL COMMENT '部门ID',
  PRIMARY KEY (`role_id`, `dept_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_cs_0900_ai_ci COMMENT = '角色和部门关联表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_role_dept
-- ----------------------------
INSERT INTO `sys_role_dept` VALUES (2, 100);
INSERT INTO `sys_role_dept` VALUES (2, 101);
INSERT INTO `sys_role_dept` VALUES (2, 105);

-- ----------------------------
-- Table structure for sys_role_menu
-- ----------------------------
DROP TABLE IF EXISTS `sys_role_menu`;
CREATE TABLE `sys_role_menu`  (
  `role_id` bigint NOT NULL COMMENT '角色ID',
  `menu_id` bigint NOT NULL COMMENT '菜单ID',
  PRIMARY KEY (`role_id`, `menu_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_cs_0900_ai_ci COMMENT = '角色和菜单关联表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_role_menu
-- ----------------------------
INSERT INTO `sys_role_menu` VALUES (2, 1);
INSERT INTO `sys_role_menu` VALUES (2, 2);
INSERT INTO `sys_role_menu` VALUES (2, 3);
INSERT INTO `sys_role_menu` VALUES (2, 100);
INSERT INTO `sys_role_menu` VALUES (2, 101);
INSERT INTO `sys_role_menu` VALUES (2, 102);
INSERT INTO `sys_role_menu` VALUES (2, 103);
INSERT INTO `sys_role_menu` VALUES (2, 104);
INSERT INTO `sys_role_menu` VALUES (2, 105);
INSERT INTO `sys_role_menu` VALUES (2, 106);
INSERT INTO `sys_role_menu` VALUES (2, 107);
INSERT INTO `sys_role_menu` VALUES (2, 108);
INSERT INTO `sys_role_menu` VALUES (2, 109);
INSERT INTO `sys_role_menu` VALUES (2, 110);
INSERT INTO `sys_role_menu` VALUES (2, 111);
INSERT INTO `sys_role_menu` VALUES (2, 112);
INSERT INTO `sys_role_menu` VALUES (2, 113);
INSERT INTO `sys_role_menu` VALUES (2, 114);
INSERT INTO `sys_role_menu` VALUES (2, 115);
INSERT INTO `sys_role_menu` VALUES (2, 116);
INSERT INTO `sys_role_menu` VALUES (2, 500);
INSERT INTO `sys_role_menu` VALUES (2, 501);
INSERT INTO `sys_role_menu` VALUES (2, 1000);
INSERT INTO `sys_role_menu` VALUES (2, 1001);
INSERT INTO `sys_role_menu` VALUES (2, 1002);
INSERT INTO `sys_role_menu` VALUES (2, 1003);
INSERT INTO `sys_role_menu` VALUES (2, 1004);
INSERT INTO `sys_role_menu` VALUES (2, 1005);
INSERT INTO `sys_role_menu` VALUES (2, 1006);
INSERT INTO `sys_role_menu` VALUES (2, 1007);
INSERT INTO `sys_role_menu` VALUES (2, 1008);
INSERT INTO `sys_role_menu` VALUES (2, 1009);
INSERT INTO `sys_role_menu` VALUES (2, 1010);
INSERT INTO `sys_role_menu` VALUES (2, 1011);
INSERT INTO `sys_role_menu` VALUES (2, 1012);
INSERT INTO `sys_role_menu` VALUES (2, 1013);
INSERT INTO `sys_role_menu` VALUES (2, 1014);
INSERT INTO `sys_role_menu` VALUES (2, 1015);
INSERT INTO `sys_role_menu` VALUES (2, 1016);
INSERT INTO `sys_role_menu` VALUES (2, 1017);
INSERT INTO `sys_role_menu` VALUES (2, 1018);
INSERT INTO `sys_role_menu` VALUES (2, 1019);
INSERT INTO `sys_role_menu` VALUES (2, 1020);
INSERT INTO `sys_role_menu` VALUES (2, 1021);
INSERT INTO `sys_role_menu` VALUES (2, 1022);
INSERT INTO `sys_role_menu` VALUES (2, 1023);
INSERT INTO `sys_role_menu` VALUES (2, 1024);
INSERT INTO `sys_role_menu` VALUES (2, 1025);
INSERT INTO `sys_role_menu` VALUES (2, 1026);
INSERT INTO `sys_role_menu` VALUES (2, 1027);
INSERT INTO `sys_role_menu` VALUES (2, 1028);
INSERT INTO `sys_role_menu` VALUES (2, 1029);
INSERT INTO `sys_role_menu` VALUES (2, 1030);
INSERT INTO `sys_role_menu` VALUES (2, 1031);
INSERT INTO `sys_role_menu` VALUES (2, 1032);
INSERT INTO `sys_role_menu` VALUES (2, 1033);
INSERT INTO `sys_role_menu` VALUES (2, 1034);
INSERT INTO `sys_role_menu` VALUES (2, 1035);
INSERT INTO `sys_role_menu` VALUES (2, 1036);
INSERT INTO `sys_role_menu` VALUES (2, 1037);
INSERT INTO `sys_role_menu` VALUES (2, 1038);
INSERT INTO `sys_role_menu` VALUES (2, 1039);
INSERT INTO `sys_role_menu` VALUES (2, 1040);
INSERT INTO `sys_role_menu` VALUES (2, 1041);
INSERT INTO `sys_role_menu` VALUES (2, 1042);
INSERT INTO `sys_role_menu` VALUES (2, 1043);
INSERT INTO `sys_role_menu` VALUES (2, 1044);
INSERT INTO `sys_role_menu` VALUES (2, 1045);
INSERT INTO `sys_role_menu` VALUES (2, 1046);
INSERT INTO `sys_role_menu` VALUES (2, 1047);
INSERT INTO `sys_role_menu` VALUES (2, 1048);
INSERT INTO `sys_role_menu` VALUES (2, 1055);
INSERT INTO `sys_role_menu` VALUES (2, 1056);
INSERT INTO `sys_role_menu` VALUES (2, 1057);
INSERT INTO `sys_role_menu` VALUES (2, 1058);
INSERT INTO `sys_role_menu` VALUES (2, 1059);
INSERT INTO `sys_role_menu` VALUES (2, 1060);
INSERT INTO `sys_role_menu` VALUES (3, 1);
INSERT INTO `sys_role_menu` VALUES (3, 2);
INSERT INTO `sys_role_menu` VALUES (3, 3);
INSERT INTO `sys_role_menu` VALUES (3, 102);
INSERT INTO `sys_role_menu` VALUES (3, 105);
INSERT INTO `sys_role_menu` VALUES (3, 106);
INSERT INTO `sys_role_menu` VALUES (3, 107);
INSERT INTO `sys_role_menu` VALUES (3, 108);
INSERT INTO `sys_role_menu` VALUES (3, 109);
INSERT INTO `sys_role_menu` VALUES (3, 111);
INSERT INTO `sys_role_menu` VALUES (3, 113);
INSERT INTO `sys_role_menu` VALUES (3, 114);
INSERT INTO `sys_role_menu` VALUES (3, 115);
INSERT INTO `sys_role_menu` VALUES (3, 116);
INSERT INTO `sys_role_menu` VALUES (3, 500);
INSERT INTO `sys_role_menu` VALUES (3, 501);
INSERT INTO `sys_role_menu` VALUES (3, 1013);
INSERT INTO `sys_role_menu` VALUES (3, 1014);
INSERT INTO `sys_role_menu` VALUES (3, 1015);
INSERT INTO `sys_role_menu` VALUES (3, 1016);
INSERT INTO `sys_role_menu` VALUES (3, 1026);
INSERT INTO `sys_role_menu` VALUES (3, 1027);
INSERT INTO `sys_role_menu` VALUES (3, 1028);
INSERT INTO `sys_role_menu` VALUES (3, 1029);
INSERT INTO `sys_role_menu` VALUES (3, 1030);
INSERT INTO `sys_role_menu` VALUES (3, 1031);
INSERT INTO `sys_role_menu` VALUES (3, 1032);
INSERT INTO `sys_role_menu` VALUES (3, 1033);
INSERT INTO `sys_role_menu` VALUES (3, 1034);
INSERT INTO `sys_role_menu` VALUES (3, 1035);
INSERT INTO `sys_role_menu` VALUES (3, 1036);
INSERT INTO `sys_role_menu` VALUES (3, 1037);
INSERT INTO `sys_role_menu` VALUES (3, 1038);
INSERT INTO `sys_role_menu` VALUES (3, 1039);
INSERT INTO `sys_role_menu` VALUES (3, 1040);
INSERT INTO `sys_role_menu` VALUES (3, 1041);
INSERT INTO `sys_role_menu` VALUES (3, 1042);
INSERT INTO `sys_role_menu` VALUES (3, 1043);
INSERT INTO `sys_role_menu` VALUES (3, 1044);
INSERT INTO `sys_role_menu` VALUES (3, 1045);
INSERT INTO `sys_role_menu` VALUES (3, 1046);
INSERT INTO `sys_role_menu` VALUES (3, 1047);
INSERT INTO `sys_role_menu` VALUES (3, 1048);
INSERT INTO `sys_role_menu` VALUES (3, 1055);
INSERT INTO `sys_role_menu` VALUES (3, 1056);
INSERT INTO `sys_role_menu` VALUES (3, 1057);
INSERT INTO `sys_role_menu` VALUES (3, 1058);
INSERT INTO `sys_role_menu` VALUES (3, 1059);
INSERT INTO `sys_role_menu` VALUES (3, 1060);
INSERT INTO `sys_role_menu` VALUES (3, 1559071670050615298);
INSERT INTO `sys_role_menu` VALUES (3, 1559071832202407937);
INSERT INTO `sys_role_menu` VALUES (3, 1559368157548617730);
INSERT INTO `sys_role_menu` VALUES (3, 1559369318368387072);
INSERT INTO `sys_role_menu` VALUES (3, 1559369318368387073);
INSERT INTO `sys_role_menu` VALUES (3, 1559369318368387074);
INSERT INTO `sys_role_menu` VALUES (3, 1559369318368387075);
INSERT INTO `sys_role_menu` VALUES (3, 1559369318368387076);
INSERT INTO `sys_role_menu` VALUES (3, 1559369318368387077);
INSERT INTO `sys_role_menu` VALUES (3, 1559377965450379264);
INSERT INTO `sys_role_menu` VALUES (3, 1559377965450379265);
INSERT INTO `sys_role_menu` VALUES (3, 1559377965450379266);
INSERT INTO `sys_role_menu` VALUES (3, 1559377965450379267);
INSERT INTO `sys_role_menu` VALUES (3, 1559377965450379268);
INSERT INTO `sys_role_menu` VALUES (3, 1559377965450379269);
INSERT INTO `sys_role_menu` VALUES (3, 1559528924465868800);
INSERT INTO `sys_role_menu` VALUES (3, 1559528924465868801);
INSERT INTO `sys_role_menu` VALUES (3, 1559528924465868802);
INSERT INTO `sys_role_menu` VALUES (3, 1559528924465868803);
INSERT INTO `sys_role_menu` VALUES (3, 1559528924465868804);
INSERT INTO `sys_role_menu` VALUES (3, 1559528924465868805);
INSERT INTO `sys_role_menu` VALUES (3, 1559541694750003201);
INSERT INTO `sys_role_menu` VALUES (3, 1560425633970720768);
INSERT INTO `sys_role_menu` VALUES (3, 1560425633970720769);
INSERT INTO `sys_role_menu` VALUES (3, 1560425633970720770);
INSERT INTO `sys_role_menu` VALUES (3, 1560425633970720771);
INSERT INTO `sys_role_menu` VALUES (3, 1560425633970720772);
INSERT INTO `sys_role_menu` VALUES (3, 1560425633970720773);
INSERT INTO `sys_role_menu` VALUES (3, 1568069868383428608);
INSERT INTO `sys_role_menu` VALUES (3, 1568069868383428609);
INSERT INTO `sys_role_menu` VALUES (3, 1568069868383428610);
INSERT INTO `sys_role_menu` VALUES (3, 1568069868383428611);
INSERT INTO `sys_role_menu` VALUES (3, 1568069868383428612);
INSERT INTO `sys_role_menu` VALUES (3, 1568069868383428613);
INSERT INTO `sys_role_menu` VALUES (3, 1568071215681953794);
INSERT INTO `sys_role_menu` VALUES (4, 1);
INSERT INTO `sys_role_menu` VALUES (4, 2);
INSERT INTO `sys_role_menu` VALUES (4, 102);
INSERT INTO `sys_role_menu` VALUES (4, 105);
INSERT INTO `sys_role_menu` VALUES (4, 107);
INSERT INTO `sys_role_menu` VALUES (4, 108);
INSERT INTO `sys_role_menu` VALUES (4, 109);
INSERT INTO `sys_role_menu` VALUES (4, 111);
INSERT INTO `sys_role_menu` VALUES (4, 113);
INSERT INTO `sys_role_menu` VALUES (4, 500);
INSERT INTO `sys_role_menu` VALUES (4, 501);
INSERT INTO `sys_role_menu` VALUES (4, 1013);
INSERT INTO `sys_role_menu` VALUES (4, 1014);
INSERT INTO `sys_role_menu` VALUES (4, 1015);
INSERT INTO `sys_role_menu` VALUES (4, 1016);
INSERT INTO `sys_role_menu` VALUES (4, 1026);
INSERT INTO `sys_role_menu` VALUES (4, 1027);
INSERT INTO `sys_role_menu` VALUES (4, 1028);
INSERT INTO `sys_role_menu` VALUES (4, 1029);
INSERT INTO `sys_role_menu` VALUES (4, 1030);
INSERT INTO `sys_role_menu` VALUES (4, 1036);
INSERT INTO `sys_role_menu` VALUES (4, 1037);
INSERT INTO `sys_role_menu` VALUES (4, 1038);
INSERT INTO `sys_role_menu` VALUES (4, 1039);
INSERT INTO `sys_role_menu` VALUES (4, 1040);
INSERT INTO `sys_role_menu` VALUES (4, 1041);
INSERT INTO `sys_role_menu` VALUES (4, 1042);
INSERT INTO `sys_role_menu` VALUES (4, 1043);
INSERT INTO `sys_role_menu` VALUES (4, 1044);
INSERT INTO `sys_role_menu` VALUES (4, 1045);
INSERT INTO `sys_role_menu` VALUES (4, 1046);
INSERT INTO `sys_role_menu` VALUES (4, 1047);
INSERT INTO `sys_role_menu` VALUES (4, 1048);
INSERT INTO `sys_role_menu` VALUES (4, 1559071670050615298);
INSERT INTO `sys_role_menu` VALUES (4, 1559071832202407937);
INSERT INTO `sys_role_menu` VALUES (4, 1559368157548617730);
INSERT INTO `sys_role_menu` VALUES (4, 1559369318368387072);
INSERT INTO `sys_role_menu` VALUES (4, 1559369318368387073);
INSERT INTO `sys_role_menu` VALUES (4, 1559369318368387074);
INSERT INTO `sys_role_menu` VALUES (4, 1559369318368387075);
INSERT INTO `sys_role_menu` VALUES (4, 1559369318368387076);
INSERT INTO `sys_role_menu` VALUES (4, 1559369318368387077);
INSERT INTO `sys_role_menu` VALUES (4, 1559377965450379264);
INSERT INTO `sys_role_menu` VALUES (4, 1559377965450379265);
INSERT INTO `sys_role_menu` VALUES (4, 1559377965450379266);
INSERT INTO `sys_role_menu` VALUES (4, 1559377965450379267);
INSERT INTO `sys_role_menu` VALUES (4, 1559377965450379268);
INSERT INTO `sys_role_menu` VALUES (4, 1559377965450379269);
INSERT INTO `sys_role_menu` VALUES (4, 1559528924465868800);
INSERT INTO `sys_role_menu` VALUES (4, 1559528924465868801);
INSERT INTO `sys_role_menu` VALUES (4, 1559528924465868802);
INSERT INTO `sys_role_menu` VALUES (4, 1559528924465868803);
INSERT INTO `sys_role_menu` VALUES (4, 1559528924465868804);
INSERT INTO `sys_role_menu` VALUES (4, 1559528924465868805);
INSERT INTO `sys_role_menu` VALUES (4, 1559541694750003201);
INSERT INTO `sys_role_menu` VALUES (4, 1560425633970720768);
INSERT INTO `sys_role_menu` VALUES (4, 1560425633970720769);
INSERT INTO `sys_role_menu` VALUES (4, 1560425633970720770);
INSERT INTO `sys_role_menu` VALUES (4, 1560425633970720771);
INSERT INTO `sys_role_menu` VALUES (4, 1560425633970720772);
INSERT INTO `sys_role_menu` VALUES (4, 1560425633970720773);
INSERT INTO `sys_role_menu` VALUES (4, 1568069868383428608);
INSERT INTO `sys_role_menu` VALUES (4, 1568069868383428609);
INSERT INTO `sys_role_menu` VALUES (4, 1568069868383428610);
INSERT INTO `sys_role_menu` VALUES (4, 1568069868383428611);
INSERT INTO `sys_role_menu` VALUES (4, 1568069868383428612);
INSERT INTO `sys_role_menu` VALUES (4, 1568069868383428613);
INSERT INTO `sys_role_menu` VALUES (4, 1568071215681953794);
INSERT INTO `sys_role_menu` VALUES (1568063062693163009, 1);
INSERT INTO `sys_role_menu` VALUES (1568063062693163009, 2);
INSERT INTO `sys_role_menu` VALUES (1568063062693163009, 3);
INSERT INTO `sys_role_menu` VALUES (1568063062693163009, 102);
INSERT INTO `sys_role_menu` VALUES (1568063062693163009, 105);
INSERT INTO `sys_role_menu` VALUES (1568063062693163009, 107);
INSERT INTO `sys_role_menu` VALUES (1568063062693163009, 108);
INSERT INTO `sys_role_menu` VALUES (1568063062693163009, 109);
INSERT INTO `sys_role_menu` VALUES (1568063062693163009, 111);
INSERT INTO `sys_role_menu` VALUES (1568063062693163009, 113);
INSERT INTO `sys_role_menu` VALUES (1568063062693163009, 114);
INSERT INTO `sys_role_menu` VALUES (1568063062693163009, 115);
INSERT INTO `sys_role_menu` VALUES (1568063062693163009, 116);
INSERT INTO `sys_role_menu` VALUES (1568063062693163009, 117);
INSERT INTO `sys_role_menu` VALUES (1568063062693163009, 120);
INSERT INTO `sys_role_menu` VALUES (1568063062693163009, 500);
INSERT INTO `sys_role_menu` VALUES (1568063062693163009, 501);
INSERT INTO `sys_role_menu` VALUES (1568063062693163009, 1013);
INSERT INTO `sys_role_menu` VALUES (1568063062693163009, 1014);
INSERT INTO `sys_role_menu` VALUES (1568063062693163009, 1015);
INSERT INTO `sys_role_menu` VALUES (1568063062693163009, 1016);
INSERT INTO `sys_role_menu` VALUES (1568063062693163009, 1026);
INSERT INTO `sys_role_menu` VALUES (1568063062693163009, 1027);
INSERT INTO `sys_role_menu` VALUES (1568063062693163009, 1028);
INSERT INTO `sys_role_menu` VALUES (1568063062693163009, 1029);
INSERT INTO `sys_role_menu` VALUES (1568063062693163009, 1030);
INSERT INTO `sys_role_menu` VALUES (1568063062693163009, 1036);
INSERT INTO `sys_role_menu` VALUES (1568063062693163009, 1037);
INSERT INTO `sys_role_menu` VALUES (1568063062693163009, 1038);
INSERT INTO `sys_role_menu` VALUES (1568063062693163009, 1039);
INSERT INTO `sys_role_menu` VALUES (1568063062693163009, 1040);
INSERT INTO `sys_role_menu` VALUES (1568063062693163009, 1041);
INSERT INTO `sys_role_menu` VALUES (1568063062693163009, 1042);
INSERT INTO `sys_role_menu` VALUES (1568063062693163009, 1043);
INSERT INTO `sys_role_menu` VALUES (1568063062693163009, 1044);
INSERT INTO `sys_role_menu` VALUES (1568063062693163009, 1045);
INSERT INTO `sys_role_menu` VALUES (1568063062693163009, 1046);
INSERT INTO `sys_role_menu` VALUES (1568063062693163009, 1047);
INSERT INTO `sys_role_menu` VALUES (1568063062693163009, 1048);
INSERT INTO `sys_role_menu` VALUES (1568063062693163009, 1055);
INSERT INTO `sys_role_menu` VALUES (1568063062693163009, 1056);
INSERT INTO `sys_role_menu` VALUES (1568063062693163009, 1057);
INSERT INTO `sys_role_menu` VALUES (1568063062693163009, 1058);
INSERT INTO `sys_role_menu` VALUES (1568063062693163009, 1059);
INSERT INTO `sys_role_menu` VALUES (1568063062693163009, 1060);
INSERT INTO `sys_role_menu` VALUES (1568063062693163009, 1537410577121243137);
INSERT INTO `sys_role_menu` VALUES (1568063062693163009, 1537410785456517121);
INSERT INTO `sys_role_menu` VALUES (1568063062693163009, 1559071670050615298);
INSERT INTO `sys_role_menu` VALUES (1568063062693163009, 1559071832202407937);
INSERT INTO `sys_role_menu` VALUES (1568063062693163009, 1559368157548617730);
INSERT INTO `sys_role_menu` VALUES (1568063062693163009, 1559369318368387072);
INSERT INTO `sys_role_menu` VALUES (1568063062693163009, 1559369318368387073);
INSERT INTO `sys_role_menu` VALUES (1568063062693163009, 1559369318368387074);
INSERT INTO `sys_role_menu` VALUES (1568063062693163009, 1559369318368387075);
INSERT INTO `sys_role_menu` VALUES (1568063062693163009, 1559369318368387076);
INSERT INTO `sys_role_menu` VALUES (1568063062693163009, 1559369318368387077);
INSERT INTO `sys_role_menu` VALUES (1568063062693163009, 1559377965450379264);
INSERT INTO `sys_role_menu` VALUES (1568063062693163009, 1559377965450379265);
INSERT INTO `sys_role_menu` VALUES (1568063062693163009, 1559377965450379266);
INSERT INTO `sys_role_menu` VALUES (1568063062693163009, 1559377965450379267);
INSERT INTO `sys_role_menu` VALUES (1568063062693163009, 1559377965450379268);
INSERT INTO `sys_role_menu` VALUES (1568063062693163009, 1559377965450379269);
INSERT INTO `sys_role_menu` VALUES (1568063062693163009, 1559528924465868800);
INSERT INTO `sys_role_menu` VALUES (1568063062693163009, 1559528924465868801);
INSERT INTO `sys_role_menu` VALUES (1568063062693163009, 1559528924465868802);
INSERT INTO `sys_role_menu` VALUES (1568063062693163009, 1559528924465868803);
INSERT INTO `sys_role_menu` VALUES (1568063062693163009, 1559528924465868804);
INSERT INTO `sys_role_menu` VALUES (1568063062693163009, 1559528924465868805);
INSERT INTO `sys_role_menu` VALUES (1568063062693163009, 1559541694750003201);
INSERT INTO `sys_role_menu` VALUES (1568063062693163009, 1560425633970720768);
INSERT INTO `sys_role_menu` VALUES (1568063062693163009, 1560425633970720769);
INSERT INTO `sys_role_menu` VALUES (1568063062693163009, 1560425633970720770);
INSERT INTO `sys_role_menu` VALUES (1568063062693163009, 1560425633970720771);
INSERT INTO `sys_role_menu` VALUES (1568063062693163009, 1560425633970720772);
INSERT INTO `sys_role_menu` VALUES (1568063062693163009, 1560425633970720773);
INSERT INTO `sys_role_menu` VALUES (1568063062693163010, 1);
INSERT INTO `sys_role_menu` VALUES (1568063062693163010, 2);
INSERT INTO `sys_role_menu` VALUES (1568063062693163010, 102);
INSERT INTO `sys_role_menu` VALUES (1568063062693163010, 105);
INSERT INTO `sys_role_menu` VALUES (1568063062693163010, 106);
INSERT INTO `sys_role_menu` VALUES (1568063062693163010, 107);
INSERT INTO `sys_role_menu` VALUES (1568063062693163010, 108);
INSERT INTO `sys_role_menu` VALUES (1568063062693163010, 109);
INSERT INTO `sys_role_menu` VALUES (1568063062693163010, 111);
INSERT INTO `sys_role_menu` VALUES (1568063062693163010, 113);
INSERT INTO `sys_role_menu` VALUES (1568063062693163010, 117);
INSERT INTO `sys_role_menu` VALUES (1568063062693163010, 120);
INSERT INTO `sys_role_menu` VALUES (1568063062693163010, 500);
INSERT INTO `sys_role_menu` VALUES (1568063062693163010, 501);
INSERT INTO `sys_role_menu` VALUES (1568063062693163010, 1013);
INSERT INTO `sys_role_menu` VALUES (1568063062693163010, 1014);
INSERT INTO `sys_role_menu` VALUES (1568063062693163010, 1015);
INSERT INTO `sys_role_menu` VALUES (1568063062693163010, 1016);
INSERT INTO `sys_role_menu` VALUES (1568063062693163010, 1026);
INSERT INTO `sys_role_menu` VALUES (1568063062693163010, 1027);
INSERT INTO `sys_role_menu` VALUES (1568063062693163010, 1028);
INSERT INTO `sys_role_menu` VALUES (1568063062693163010, 1029);
INSERT INTO `sys_role_menu` VALUES (1568063062693163010, 1030);
INSERT INTO `sys_role_menu` VALUES (1568063062693163010, 1031);
INSERT INTO `sys_role_menu` VALUES (1568063062693163010, 1032);
INSERT INTO `sys_role_menu` VALUES (1568063062693163010, 1033);
INSERT INTO `sys_role_menu` VALUES (1568063062693163010, 1034);
INSERT INTO `sys_role_menu` VALUES (1568063062693163010, 1035);
INSERT INTO `sys_role_menu` VALUES (1568063062693163010, 1036);
INSERT INTO `sys_role_menu` VALUES (1568063062693163010, 1037);
INSERT INTO `sys_role_menu` VALUES (1568063062693163010, 1038);
INSERT INTO `sys_role_menu` VALUES (1568063062693163010, 1039);
INSERT INTO `sys_role_menu` VALUES (1568063062693163010, 1040);
INSERT INTO `sys_role_menu` VALUES (1568063062693163010, 1041);
INSERT INTO `sys_role_menu` VALUES (1568063062693163010, 1042);
INSERT INTO `sys_role_menu` VALUES (1568063062693163010, 1043);
INSERT INTO `sys_role_menu` VALUES (1568063062693163010, 1044);
INSERT INTO `sys_role_menu` VALUES (1568063062693163010, 1045);
INSERT INTO `sys_role_menu` VALUES (1568063062693163010, 1046);
INSERT INTO `sys_role_menu` VALUES (1568063062693163010, 1047);
INSERT INTO `sys_role_menu` VALUES (1568063062693163010, 1048);
INSERT INTO `sys_role_menu` VALUES (1568063062693163010, 1559071670050615298);
INSERT INTO `sys_role_menu` VALUES (1568063062693163010, 1559071832202407937);
INSERT INTO `sys_role_menu` VALUES (1568063062693163010, 1559368157548617730);
INSERT INTO `sys_role_menu` VALUES (1568063062693163010, 1559369318368387072);
INSERT INTO `sys_role_menu` VALUES (1568063062693163010, 1559369318368387073);
INSERT INTO `sys_role_menu` VALUES (1568063062693163010, 1559369318368387074);
INSERT INTO `sys_role_menu` VALUES (1568063062693163010, 1559369318368387075);
INSERT INTO `sys_role_menu` VALUES (1568063062693163010, 1559369318368387076);
INSERT INTO `sys_role_menu` VALUES (1568063062693163010, 1559369318368387077);
INSERT INTO `sys_role_menu` VALUES (1568063062693163010, 1559377965450379264);
INSERT INTO `sys_role_menu` VALUES (1568063062693163010, 1559377965450379265);
INSERT INTO `sys_role_menu` VALUES (1568063062693163010, 1559377965450379266);
INSERT INTO `sys_role_menu` VALUES (1568063062693163010, 1559377965450379267);
INSERT INTO `sys_role_menu` VALUES (1568063062693163010, 1559377965450379268);
INSERT INTO `sys_role_menu` VALUES (1568063062693163010, 1559377965450379269);
INSERT INTO `sys_role_menu` VALUES (1568063062693163010, 1559528924465868800);
INSERT INTO `sys_role_menu` VALUES (1568063062693163010, 1559528924465868801);
INSERT INTO `sys_role_menu` VALUES (1568063062693163010, 1559528924465868802);
INSERT INTO `sys_role_menu` VALUES (1568063062693163010, 1559528924465868803);
INSERT INTO `sys_role_menu` VALUES (1568063062693163010, 1559528924465868804);
INSERT INTO `sys_role_menu` VALUES (1568063062693163010, 1559528924465868805);
INSERT INTO `sys_role_menu` VALUES (1568063062693163010, 1559541694750003201);
INSERT INTO `sys_role_menu` VALUES (1568063062693163010, 1560425633970720768);
INSERT INTO `sys_role_menu` VALUES (1568063062693163010, 1560425633970720769);
INSERT INTO `sys_role_menu` VALUES (1568063062693163010, 1560425633970720770);
INSERT INTO `sys_role_menu` VALUES (1568063062693163010, 1560425633970720771);
INSERT INTO `sys_role_menu` VALUES (1568063062693163010, 1560425633970720772);
INSERT INTO `sys_role_menu` VALUES (1568063062693163010, 1560425633970720773);
INSERT INTO `sys_role_menu` VALUES (1568063062693163011, 1);
INSERT INTO `sys_role_menu` VALUES (1568063062693163011, 2);
INSERT INTO `sys_role_menu` VALUES (1568063062693163011, 3);
INSERT INTO `sys_role_menu` VALUES (1568063062693163011, 102);
INSERT INTO `sys_role_menu` VALUES (1568063062693163011, 105);
INSERT INTO `sys_role_menu` VALUES (1568063062693163011, 106);
INSERT INTO `sys_role_menu` VALUES (1568063062693163011, 107);
INSERT INTO `sys_role_menu` VALUES (1568063062693163011, 108);
INSERT INTO `sys_role_menu` VALUES (1568063062693163011, 109);
INSERT INTO `sys_role_menu` VALUES (1568063062693163011, 111);
INSERT INTO `sys_role_menu` VALUES (1568063062693163011, 113);
INSERT INTO `sys_role_menu` VALUES (1568063062693163011, 114);
INSERT INTO `sys_role_menu` VALUES (1568063062693163011, 116);
INSERT INTO `sys_role_menu` VALUES (1568063062693163011, 118);
INSERT INTO `sys_role_menu` VALUES (1568063062693163011, 500);
INSERT INTO `sys_role_menu` VALUES (1568063062693163011, 501);
INSERT INTO `sys_role_menu` VALUES (1568063062693163011, 1013);
INSERT INTO `sys_role_menu` VALUES (1568063062693163011, 1026);
INSERT INTO `sys_role_menu` VALUES (1568063062693163011, 1030);
INSERT INTO `sys_role_menu` VALUES (1568063062693163011, 1031);
INSERT INTO `sys_role_menu` VALUES (1568063062693163011, 1035);
INSERT INTO `sys_role_menu` VALUES (1568063062693163011, 1036);
INSERT INTO `sys_role_menu` VALUES (1568063062693163011, 1040);
INSERT INTO `sys_role_menu` VALUES (1568063062693163011, 1041);
INSERT INTO `sys_role_menu` VALUES (1568063062693163011, 1042);
INSERT INTO `sys_role_menu` VALUES (1568063062693163011, 1043);
INSERT INTO `sys_role_menu` VALUES (1568063062693163011, 1044);
INSERT INTO `sys_role_menu` VALUES (1568063062693163011, 1045);
INSERT INTO `sys_role_menu` VALUES (1568063062693163011, 1046);
INSERT INTO `sys_role_menu` VALUES (1568063062693163011, 1047);
INSERT INTO `sys_role_menu` VALUES (1568063062693163011, 1048);
INSERT INTO `sys_role_menu` VALUES (1568063062693163011, 1600);
INSERT INTO `sys_role_menu` VALUES (1568063062693163011, 1602);
INSERT INTO `sys_role_menu` VALUES (1568063062693163011, 1559071670050615298);
INSERT INTO `sys_role_menu` VALUES (1568063062693163011, 1559071832202407937);
INSERT INTO `sys_role_menu` VALUES (1568063062693163011, 1559368157548617730);
INSERT INTO `sys_role_menu` VALUES (1568063062693163011, 1559369318368387072);
INSERT INTO `sys_role_menu` VALUES (1568063062693163011, 1559369318368387073);
INSERT INTO `sys_role_menu` VALUES (1568063062693163011, 1559369318368387077);
INSERT INTO `sys_role_menu` VALUES (1568063062693163011, 1559377965450379264);
INSERT INTO `sys_role_menu` VALUES (1568063062693163011, 1559377965450379265);
INSERT INTO `sys_role_menu` VALUES (1568063062693163011, 1559377965450379269);
INSERT INTO `sys_role_menu` VALUES (1568063062693163011, 1559528924465868800);
INSERT INTO `sys_role_menu` VALUES (1568063062693163011, 1559528924465868801);
INSERT INTO `sys_role_menu` VALUES (1568063062693163011, 1559528924465868805);
INSERT INTO `sys_role_menu` VALUES (1568063062693163011, 1559541694750003201);
INSERT INTO `sys_role_menu` VALUES (1568063062693163011, 1560425633970720768);
INSERT INTO `sys_role_menu` VALUES (1568063062693163011, 1560425633970720769);
INSERT INTO `sys_role_menu` VALUES (1568063062693163011, 1560425633970720773);
INSERT INTO `sys_role_menu` VALUES (1568063062693163011, 1568069868383428608);
INSERT INTO `sys_role_menu` VALUES (1568063062693163011, 1568069868383428609);
INSERT INTO `sys_role_menu` VALUES (1568063062693163011, 1568069868383428613);
INSERT INTO `sys_role_menu` VALUES (1568063062693163011, 1568071215681953794);
INSERT INTO `sys_role_menu` VALUES (1568063062693163011, 1570601174635941888);
INSERT INTO `sys_role_menu` VALUES (1568063062693163011, 1570601174635941889);
INSERT INTO `sys_role_menu` VALUES (1568063062693163011, 1570601174635941893);
INSERT INTO `sys_role_menu` VALUES (1568063062693163011, 1570612161132990464);
INSERT INTO `sys_role_menu` VALUES (1568063062693163011, 1570612161132990465);
INSERT INTO `sys_role_menu` VALUES (1568063062693163011, 1570612161132990469);
INSERT INTO `sys_role_menu` VALUES (1568063062693163011, 1575498935308333056);
INSERT INTO `sys_role_menu` VALUES (1568063062693163011, 1575498935308333057);
INSERT INTO `sys_role_menu` VALUES (1568063062693163011, 1575498935308333061);
INSERT INTO `sys_role_menu` VALUES (1568063062693163011, 1579451198515113984);
INSERT INTO `sys_role_menu` VALUES (1568063062693163011, 1579451198515113985);
INSERT INTO `sys_role_menu` VALUES (1568063062693163011, 1579451198515113989);
INSERT INTO `sys_role_menu` VALUES (1568063062693163011, 1583288960459567105);
INSERT INTO `sys_role_menu` VALUES (1568063062693163011, 1583342505409998848);
INSERT INTO `sys_role_menu` VALUES (1568063062693163011, 1583342505409998849);
INSERT INTO `sys_role_menu` VALUES (1568063062693163011, 1583342505409998853);
INSERT INTO `sys_role_menu` VALUES (1568063062693163011, 1583349448128163840);
INSERT INTO `sys_role_menu` VALUES (1568063062693163011, 1583349448128163841);
INSERT INTO `sys_role_menu` VALUES (1568063062693163011, 1583349448128163845);
INSERT INTO `sys_role_menu` VALUES (1568063062693163011, 1588065146347773952);
INSERT INTO `sys_role_menu` VALUES (1568063062693163011, 1588065146347773953);
INSERT INTO `sys_role_menu` VALUES (1568063062693163011, 1588065146347773957);
INSERT INTO `sys_role_menu` VALUES (1568063062693163011, 1588069973022793728);
INSERT INTO `sys_role_menu` VALUES (1568063062693163011, 1588069973022793729);
INSERT INTO `sys_role_menu` VALUES (1568063062693163011, 1588069973022793733);
INSERT INTO `sys_role_menu` VALUES (1568063062693163011, 1627500976322580480);
INSERT INTO `sys_role_menu` VALUES (1568063062693163011, 1627500976322580481);
INSERT INTO `sys_role_menu` VALUES (1568063062693163011, 1627500976322580485);
INSERT INTO `sys_role_menu` VALUES (1568063062693163011, 1633636367421382656);
INSERT INTO `sys_role_menu` VALUES (1568063062693163011, 1633636367421382657);
INSERT INTO `sys_role_menu` VALUES (1568063062693163011, 1633636367421382661);
INSERT INTO `sys_role_menu` VALUES (1568063062693163011, 1633725175638663168);
INSERT INTO `sys_role_menu` VALUES (1568063062693163011, 1633725175638663169);
INSERT INTO `sys_role_menu` VALUES (1568063062693163011, 1633725175638663173);
INSERT INTO `sys_role_menu` VALUES (1568063062693163011, 1635253301085544449);
INSERT INTO `sys_role_menu` VALUES (1568063062693163011, 1635280042139136002);
INSERT INTO `sys_role_menu` VALUES (1568063062693163011, 1638717119014952960);
INSERT INTO `sys_role_menu` VALUES (1568063062693163011, 1638717119014952964);
INSERT INTO `sys_role_menu` VALUES (1568063062693163011, 1643095134993182720);
INSERT INTO `sys_role_menu` VALUES (1568063062693163011, 1643095134993182721);
INSERT INTO `sys_role_menu` VALUES (1568063062693163011, 1643095134993182725);
INSERT INTO `sys_role_menu` VALUES (1568063062693163011, 1643097808857788416);
INSERT INTO `sys_role_menu` VALUES (1568063062693163011, 1643097808857788417);
INSERT INTO `sys_role_menu` VALUES (1568063062693163011, 1643097808857788421);
INSERT INTO `sys_role_menu` VALUES (1568063062693163011, 1645281581871460352);
INSERT INTO `sys_role_menu` VALUES (1568063062693163011, 1645281581871460353);
INSERT INTO `sys_role_menu` VALUES (1568063062693163011, 1645281581871460357);
INSERT INTO `sys_role_menu` VALUES (1568063062693163011, 1647786462837522432);
INSERT INTO `sys_role_menu` VALUES (1568063062693163011, 1647786462837522433);
INSERT INTO `sys_role_menu` VALUES (1568063062693163011, 1647786462837522437);
INSERT INTO `sys_role_menu` VALUES (1568063062693163011, 1648209886288121856);
INSERT INTO `sys_role_menu` VALUES (1568063062693163011, 1648209886288121857);
INSERT INTO `sys_role_menu` VALUES (1568063062693163011, 1648209886288121861);
INSERT INTO `sys_role_menu` VALUES (1568063062693163011, 1650031562833575936);
INSERT INTO `sys_role_menu` VALUES (1568063062693163011, 1650031562833575937);
INSERT INTO `sys_role_menu` VALUES (1568063062693163011, 1650031562833575941);
INSERT INTO `sys_role_menu` VALUES (1568063062693163011, 1650319922586042368);
INSERT INTO `sys_role_menu` VALUES (1568063062693163011, 1650319922586042369);
INSERT INTO `sys_role_menu` VALUES (1568063062693163011, 1650319922586042373);
INSERT INTO `sys_role_menu` VALUES (1568063062693163011, 1650383488303304704);
INSERT INTO `sys_role_menu` VALUES (1568063062693163011, 1650383488303304705);
INSERT INTO `sys_role_menu` VALUES (1568063062693163011, 1650383488303304709);
INSERT INTO `sys_role_menu` VALUES (1568063062693163011, 1650388427805102080);
INSERT INTO `sys_role_menu` VALUES (1568063062693163011, 1650388427805102081);
INSERT INTO `sys_role_menu` VALUES (1568063062693163011, 1650388427805102085);
INSERT INTO `sys_role_menu` VALUES (1568063062693163011, 1650495212784291840);
INSERT INTO `sys_role_menu` VALUES (1568063062693163011, 1650495212784291841);
INSERT INTO `sys_role_menu` VALUES (1568063062693163011, 1650495212784291845);
INSERT INTO `sys_role_menu` VALUES (1568063062693163011, 1654109240779915266);
INSERT INTO `sys_role_menu` VALUES (1568063062693163012, 1774271697071702017);
INSERT INTO `sys_role_menu` VALUES (1568063062693163012, 1774272350540070913);
INSERT INTO `sys_role_menu` VALUES (1568063062693163012, 1774272573370859522);
INSERT INTO `sys_role_menu` VALUES (1568063062693163012, 1774273075739426818);
INSERT INTO `sys_role_menu` VALUES (1568063062693163012, 1774273205788016641);
INSERT INTO `sys_role_menu` VALUES (1568063062693163012, 1774273328861478913);
INSERT INTO `sys_role_menu` VALUES (1568063062693163012, 1774273433257705474);
INSERT INTO `sys_role_menu` VALUES (1568063062693163012, 1774273549771276289);
INSERT INTO `sys_role_menu` VALUES (1568063062693163012, 1774273672811184130);
INSERT INTO `sys_role_menu` VALUES (1568063062693163012, 1774273778486673410);

-- ----------------------------
-- Table structure for sys_user
-- ----------------------------
DROP TABLE IF EXISTS `sys_user`;
CREATE TABLE `sys_user`  (
  `user_id` bigint NOT NULL COMMENT '用户ID',
  `dept_id` bigint NULL DEFAULT NULL COMMENT '部门ID',
  `user_name` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_cs_0900_ai_ci NOT NULL COMMENT '用户账号',
  `nick_name` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_cs_0900_ai_ci NOT NULL COMMENT '用户昵称',
  `user_type` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_cs_0900_ai_ci NULL DEFAULT 'sys_user' COMMENT '用户类型（sys_user系统用户）',
  `email` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_cs_0900_ai_ci NULL DEFAULT '' COMMENT '用户邮箱',
  `phonenumber` varchar(11) CHARACTER SET utf8mb4 COLLATE utf8mb4_cs_0900_ai_ci NULL DEFAULT '' COMMENT '手机号码',
  `sex` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_cs_0900_ai_ci NULL DEFAULT '0' COMMENT '用户性别（0男 1女 2未知）',
  `avatar` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_cs_0900_ai_ci NULL DEFAULT '' COMMENT '头像地址',
  `password` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_cs_0900_ai_ci NULL DEFAULT '' COMMENT '密码',
  `status` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_cs_0900_ai_ci NULL DEFAULT '0' COMMENT '帐号状态（0正常 1停用）',
  `del_flag` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_cs_0900_ai_ci NULL DEFAULT '0' COMMENT '删除标志（0代表存在 2代表删除）',
  `login_ip` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_cs_0900_ai_ci NULL DEFAULT '' COMMENT '最后登录IP',
  `login_date` datetime NULL DEFAULT NULL COMMENT '最后登录时间',
  `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_cs_0900_ai_ci NULL DEFAULT '' COMMENT '创建者',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_cs_0900_ai_ci NULL DEFAULT '' COMMENT '更新者',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_cs_0900_ai_ci NULL DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`user_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_cs_0900_ai_ci COMMENT = '用户信息表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_user
-- ----------------------------
INSERT INTO `sys_user` VALUES (1, 103, 'admin', '钰紫薇', 'sys_user', 'crazyLionLi@163.com', '15888888888', '1', 'https://zsh-aurora.oss-cn-beijing.aliyuncs.com/aurora/2023/05/05/33c6003d3f9247bea6095abf764042a2.jpg', '$2a$10$zLd9SgqJd.fvMe.WJ2cy.uJZPJ7a5QCNMD1rMtm9ym98LxZ8vsBzS', '0', '0', '183.223.222.168', '2024-11-25 10:26:39', 'admin', '2022-06-01 22:11:39', 'ziwei', '2024-11-25 10:26:39', '管理员');

-- ----------------------------
-- Table structure for sys_user_post
-- ----------------------------
DROP TABLE IF EXISTS `sys_user_post`;
CREATE TABLE `sys_user_post`  (
  `user_id` bigint NOT NULL COMMENT '用户ID',
  `post_id` bigint NOT NULL COMMENT '岗位ID',
  PRIMARY KEY (`user_id`, `post_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_cs_0900_ai_ci COMMENT = '用户与岗位关联表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_user_post
-- ----------------------------
INSERT INTO `sys_user_post` VALUES (1, 1);
INSERT INTO `sys_user_post` VALUES (2, 2);
INSERT INTO `sys_user_post` VALUES (1568065875582181377, 4);

-- ----------------------------
-- Table structure for sys_user_role
-- ----------------------------
DROP TABLE IF EXISTS `sys_user_role`;
CREATE TABLE `sys_user_role`  (
  `user_id` bigint NOT NULL COMMENT '用户ID',
  `role_id` bigint NOT NULL COMMENT '角色ID',
  PRIMARY KEY (`user_id`, `role_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_cs_0900_ai_ci COMMENT = '用户和角色关联表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_user_role
-- ----------------------------
INSERT INTO `sys_user_role` VALUES (1, 1);
INSERT INTO `sys_user_role` VALUES (2, 2);
INSERT INTO `sys_user_role` VALUES (1568065875582181377, 3);
INSERT INTO `sys_user_role` VALUES (1640910234575761410, 2);
INSERT INTO `sys_user_role` VALUES (1640921270200627201, 1568063062693163011);
INSERT INTO `sys_user_role` VALUES (1774393143419375618, 1568063062693163012);

-- ----------------------------
-- Table structure for to_navigation
-- ----------------------------
DROP TABLE IF EXISTS `to_navigation`;
CREATE TABLE `to_navigation`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'id',
  `parent_id` bigint NULL DEFAULT NULL COMMENT '父级菜单',
  `navigation_name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '导航栏名称',
  `navigation_icon` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL COMMENT '图标',
  `icon_color` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '图标颜色',
  `route` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '路由地址',
  `order` int NULL DEFAULT NULL COMMENT '排序',
  `jurisdiction` int NOT NULL DEFAULT 0 COMMENT '权限（0：公开，1，仅自己，2：普通用户，3：vip用户）',
  `is_list` int NULL DEFAULT 0 COMMENT '是否有下拉（0：没有，1：有）',
  `status` int NOT NULL DEFAULT 0 COMMENT '状态（0：有效，1：失效）',
  `type` int NOT NULL DEFAULT 1 COMMENT '类型（1：主页面路由，2：用户页面路由）',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 18 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '导航栏配置' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of to_navigation
-- ----------------------------
INSERT INTO `to_navigation` VALUES (1, NULL, '栖息地', NULL, NULL, '/content', 1, 0, 0, 0, 1);
INSERT INTO `to_navigation` VALUES (2, NULL, '工具集', '', NULL, '/tool', 4, 0, 0, 0, 1);
INSERT INTO `to_navigation` VALUES (3, NULL, '资源', NULL, NULL, '/resources', 3, 0, 0, 1, 1);
INSERT INTO `to_navigation` VALUES (4, NULL, '名言广场', ' <svg t=\"1728440120573\" class=\"icon svg-translateY-2-\" viewBox=\"0 0 1024 1024\" version=\"1.1\" xmlns=\"http://www.w3.org/2000/svg\"\n         p-id=\"55795\" width=\"20\" height=\"20\">\n      <path\n        d=\"M837.762042 456.501128s0.557778 68.855653-33.745545 113.677068c-23.685628 30.946697-45.518637 35.598164-45.518637 35.598164s75.349778-86.953544 37.38106-243.33048C756.157186 198.937359 523.573885 1.802817 523.573885 1.802817s21.882811 106.794491-107.073379 225.332192c-72.421446 66.554821-197.69232 173.010661-214.943584 303.809509-18.06801 137.143569 55.45903 227.991596 93.46759 264.934402-0.737063-7.719244-1.105595-15.468368-1.115555-23.227453 0-136.595751 110.728815-247.294685 247.294685-247.294685s247.314606 110.708894 247.314606 247.294685c0 10.906544-0.776905 21.613882-2.141468 32.151895 23.187612-22.042176 75.499183-81.455451 87.660727-178.219905 13.984281-111.316473-36.275465-170.082328-36.275465-170.082329z m0 0\"\n        fill=\"#4930FF\" p-id=\"55796\"></path>\n      <path\n        d=\"M541.203642 525.356782c-136.56587 0-247.294685 110.698934-247.294685 247.294685 0 136.575831 110.728815 247.284725 247.294685 247.284725s247.304646-110.708894 247.304645-247.284725c0.00996-136.585791-110.738775-247.294685-247.304645-247.294685z m0 0\"\n        fill=\"#FFBE1B\" p-id=\"55797\"></path>\n      <path\n        d=\"M708.74609 520.386585c-11.514123-221.487511-218.887869-285.263404-218.887869-285.263404s53.377325 82.531165 6.852696 143.51817c-50.000778 65.548829-129.91238 87.700568-167.363162 180.789666-15.079916 37.490623-13.9544 70.777994-6.763053 97.650922 41.49467-78.337873 123.806708-131.715197 218.6289-131.715197 61.813711 0 118.288693 22.729437 161.645942 60.220061a232.662983 232.662983 0 0 0 5.886546-65.200218z m0 0\"\n        fill=\"#FF8106\" p-id=\"55798\"></path>\n      <path\n        d=\"M542.996498 529.42059c-136.56587 0-247.294685 110.698934-247.294685 247.294685 0 136.575831 110.728815 247.284725 247.294685 247.284725s247.304646-110.708894 247.304646-247.284725c0.00996-136.595751-110.738775-247.294685-247.304646-247.294685z m0 0\"\n        fill=\"#FFE6B7\" p-id=\"55799\"></path>\n      <path\n        d=\"M543.016419 575.417322c-111.167069 0-201.297953 90.110964-201.297954 201.287993 0 111.167069 90.140845 201.287993 201.297954 201.287993S744.294452 887.862423 744.294452 776.705315c0-111.177029-90.130885-201.287993-201.278033-201.287993z m0 0\"\n        fill=\"#FFBE1B\" p-id=\"55800\"></path>\n      <path\n        d=\"M384.239048 158.74749c-83.746323 69.503074-196.43732 112.959925-227.752549 227.981636-12.012139 44.104272-12.221306 91.415765 1.394444 135.191347 2.350634 7.549918 8.775037 23.147771 8.775037 23.147771s1.165357-16.673566 1.872539-24.303167c4.412419-47.819469 16.733328-95.788343 41.564392-137.243172 13.536067-22.599953 30.060229-42.14209 49.164112-60.220061 65.180297-61.664306 159.653879-106.266594 179.604389-202.084818 0.986071-4.691308 3.49607-24.890826 3.49607-24.890826s-36.036417 44.074391-58.118434 62.42129z m0 0\"\n        fill=\"#4930FF\" p-id=\"55801\"></path>\n      <path\n        d=\"M648.535989 801.606101c-39.333281-9.850751-70.03097-40.56836-79.881721-79.891682l-1.693253-6.703291-1.683293 6.703291c-9.860711 39.343242-40.56836 70.050891-79.891682 79.891682l-6.703292 1.703213 6.703292 1.673333c39.333281 9.850751 70.040931 40.5584 79.891682 79.891682l1.683293 6.713252 1.693253-6.713252c9.850751-39.333281 40.56836-70.03097 79.881721-79.891682l6.723213-1.673333-6.723213-1.703213z m0 0M506.980002 714.224263a51.235857 51.235857 0 0 1-37.251576-37.251576l-0.786865-3.127539-0.786865 3.127539c-4.591705 18.336939-18.914637 32.649911-37.251576 37.251576l-3.127538 0.786865 3.127538 0.776904c18.336939 4.601665 32.649911 18.924597 37.251576 37.261536l0.786865 3.127539 0.786865-3.127539a51.265738 51.265738 0 0 1 37.251576-37.261536l3.137499-0.776904-3.137499-0.786865z\"\n        fill=\"#FFFFFF\" p-id=\"55802\"></path>\n    </svg>', '#ff3838', '/dictum', 2, 0, 0, 0, 1);
INSERT INTO `to_navigation` VALUES (5, NULL, '最近', NULL, NULL, '/user_home/lately', 1, 1, 0, 0, 2);
INSERT INTO `to_navigation` VALUES (6, NULL, '文章', NULL, NULL, '/user_home/article', 2, 0, 0, 0, 2);
INSERT INTO `to_navigation` VALUES (7, NULL, '专栏', NULL, NULL, '/user_home/special', 3, 0, 0, 0, 2);
INSERT INTO `to_navigation` VALUES (8, NULL, '收藏集', NULL, NULL, '/user_home/collection', 4, 0, 0, 0, 2);
INSERT INTO `to_navigation` VALUES (9, NULL, '关注', NULL, NULL, '/user_home/follow', 5, 1, 0, 0, 2);
INSERT INTO `to_navigation` VALUES (10, NULL, '点赞', NULL, NULL, '/user_home/fabulous', 6, 1, 0, 0, 2);
INSERT INTO `to_navigation` VALUES (11, NULL, '活动', NULL, NULL, '/dd', 5, 0, 0, 1, 1);
INSERT INTO `to_navigation` VALUES (13, 16, '标 签', '', NULL, '/label', 5, 0, 0, 0, 1);
INSERT INTO `to_navigation` VALUES (14, NULL, '关于', '<svg t=\"1729844944504\" class=\"icon svg-translateY-2-\" viewBox=\"0 0 1024 1024\" version=\"1.1\" xmlns=\"http://www.w3.org/2000/svg\" p-id=\"17428\" width=\"20\" height=\"20\"><path d=\"M512.22755555 59.16444445c-249.96977778 0-452.608 202.63822222-452.608 452.608 0 249.96977778 202.63822222 452.608 452.608 452.608S964.83555555 761.74222222 964.83555555 511.77244445C964.83555555 261.80266667 762.19733333 59.16444445 512.22755555 59.16444445z m-31.28888888 735.91466666c3.18577778 3.41333333 6.94044445 5.12 11.264 5.12 5.57511111 0 11.83288889-2.73066667 18.65955555-8.41955556 19.79733333-15.47377778 39.70844445-37.66044445 59.96088889-66.44622222l18.09066667 12.288c-23.66577778 35.72622222-51.31377778 65.99111111-83.17155556 90.90844445-23.21066667 18.09066667-45.51111111 27.07911111-67.01511111 27.07911111-14.22222222 0-25.82755555-4.20977778-34.816-12.62933334-8.98844445-8.41955555-13.53955555-19.00088889-13.53955556-31.85777777s4.32355555-34.13333333 12.8568889-63.82933333l67.69777777-232.67555556c10.69511111-37.77422222 16.15644445-61.66755555 16.15644445-71.56622222 0-7.28177778-2.73066667-13.42577778-8.07822222-18.31822222-5.34755555-4.89244445-12.62933333-7.39555555-21.61777778-7.39555556-7.73688889 0-23.66577778 1.93422222-47.67288889 5.80266666v-20.02488888l179.2-29.696-102.51377778 364.20266666c-6.82666667 24.46222222-10.35377778 39.13955555-10.35377778 43.80444445 0.11377778 5.68888889 1.70666667 10.24 4.89244445 13.65333333zM618.95111111 256.11377778c-10.12622222 10.35377778-22.18666667 15.47377778-36.40888889 15.47377777s-26.39644445-5.12-36.75022222-15.47377777c-10.35377778-10.35377778-15.47377778-22.528-15.47377778-36.75022223 0-14.22222222 5.00622222-26.28266667 15.13244445-36.40888888 10.12622222-10.12622222 22.41422222-15.13244445 37.09155555-15.13244445 14.56355555 0 26.85155555 5.00622222 36.75022223 15.13244445 9.89866667 10.12622222 14.79111111 22.18666667 14.7911111 36.40888888s-5.12 26.51022222-15.13244444 36.75022223z\" fill=\"#70c6f2\" p-id=\"17429\"></path></svg>', NULL, '/introduce', 6, 0, 1, 0, 1);
INSERT INTO `to_navigation` VALUES (15, 14, '关于栖息地', NULL, NULL, '/introduce', 1, 0, 0, 0, 1);
INSERT INTO `to_navigation` VALUES (16, NULL, '更多', '', NULL, '/friend', 7, 0, 1, 0, 1);
INSERT INTO `to_navigation` VALUES (17, 16, '友 链', NULL, NULL, '/friend_link', 8, 0, 0, 0, 1);
INSERT INTO `to_navigation` VALUES (18, 16, 'Issues', NULL, NULL, '/issues', 9, 0, 0, 0, 1);

-- ----------------------------
-- Table structure for to_recharge_info
-- ----------------------------
DROP TABLE IF EXISTS `to_recharge_info`;
CREATE TABLE `to_recharge_info`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'id',
  `money` decimal(12, 2) NOT NULL COMMENT '充值金额',
  `currency` bigint NOT NULL COMMENT '获得币的数量',
  `commodity_name` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_cs_0900_ai_ci NULL DEFAULT NULL COMMENT '商品名称',
  `is_discount` int NOT NULL DEFAULT 0 COMMENT '是否开启打折活动（0：未开启，1：活动中）',
  `discount` int NULL DEFAULT NULL COMMENT '具体折扣',
  `start_time` datetime NULL DEFAULT NULL COMMENT '打折活动开启时间',
  `end_time` datetime NULL DEFAULT NULL COMMENT '活动结束时间',
  `member_discount` int NOT NULL DEFAULT 0 COMMENT '会员折扣（0：无会员折扣，非零：具体折扣）',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 6 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_cs_0900_ai_ci COMMENT = '充值信息表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of to_recharge_info
-- ----------------------------
INSERT INTO `to_recharge_info` VALUES (1, 0.10, 10, '一元套餐', 0, NULL, NULL, NULL, 0);
INSERT INTO `to_recharge_info` VALUES (2, 50.00, 800, '五十元套餐', 1, 9, '2023-04-04 14:34:28', '2025-06-19 00:00:00', 0);
INSERT INTO `to_recharge_info` VALUES (3, 10.00, 120, '十元套餐', 0, NULL, NULL, NULL, 0);
INSERT INTO `to_recharge_info` VALUES (4, 20.00, 250, '二十元套餐', 0, NULL, NULL, NULL, 0);
INSERT INTO `to_recharge_info` VALUES (5, 30.00, 400, '三十元套餐', 1, 9, '2023-04-04 14:33:13', '2025-06-19 00:00:00', 0);
INSERT INTO `to_recharge_info` VALUES (6, 100.00, 2000, '一百元套餐', 1, 8, '2023-04-04 14:33:41', '2025-06-19 00:00:00', 0);

-- ----------------------------
-- Table structure for to_shield_word
-- ----------------------------
DROP TABLE IF EXISTS `to_shield_word`;
CREATE TABLE `to_shield_word`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `keyword` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_cs_0900_ai_ci NOT NULL COMMENT '屏蔽字',
  `type` int NOT NULL DEFAULT 1 COMMENT '类型（1：文章，2：评论）',
  `weight` int NOT NULL DEFAULT 1 COMMENT '屏蔽词权重',
  `state` int NOT NULL DEFAULT 0 COMMENT '状态（0：正常，1：已失效）',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `create_by` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_cs_0900_ai_ci NOT NULL COMMENT '创建者',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 6 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_cs_0900_ai_ci COMMENT = '屏蔽词库' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of to_shield_word
-- ----------------------------
INSERT INTO `to_shield_word` VALUES (1, '妈蛋', 1, 1, 0, '2023-02-19 20:57:13', 'admin');
INSERT INTO `to_shield_word` VALUES (2, '傻逼', 1, 1, 0, '2023-02-19 20:57:13', 'admin');
INSERT INTO `to_shield_word` VALUES (3, '操尼玛', 1, 1, 0, '2023-02-19 20:57:13', 'admin');
INSERT INTO `to_shield_word` VALUES (4, 'cnm', 1, 1, 0, '2023-02-19 20:57:13', 'admin');
INSERT INTO `to_shield_word` VALUES (5, 'sb', 1, 1, 0, '2023-02-19 20:57:13', 'admin');
INSERT INTO `to_shield_word` VALUES (6, '叙利亚', 1, 1, 0, '2023-02-19 20:57:13', 'admin');

-- ----------------------------
-- Table structure for to_sidebar
-- ----------------------------
DROP TABLE IF EXISTS `to_sidebar`;
CREATE TABLE `to_sidebar`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'id',
  `parent_id` bigint NULL DEFAULT NULL COMMENT '父级菜单目录',
  `sidebar_name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '侧边栏名称',
  `sidebar_icon` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL COMMENT '图标',
  `icon_color` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '图标颜色',
  `order` int NULL DEFAULT NULL COMMENT '排序',
  `jurisdiction` int NOT NULL DEFAULT 0 COMMENT '权限（0：公开，1：普通用户，2：vip用户）',
  `is_list` int NOT NULL DEFAULT 0 COMMENT '是否有下拉（0：没有，1：有）\'',
  `status` int NOT NULL DEFAULT 0 COMMENT '状态（0：有效1：无效）',
  `route` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '路由地址',
  `type` int NOT NULL DEFAULT 1 COMMENT '配置类型（1：主页面路由配置，2：用户页面配置）',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 13 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '用户侧边栏配置' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of to_sidebar
-- ----------------------------
INSERT INTO `to_sidebar` VALUES (1, NULL, '首页', '<svg t=\"1729847528269\" class=\"icon svg-translateY-2-\" viewBox=\"0 0 1264 1024\" version=\"1.1\" xmlns=\"http://www.w3.org/2000/svg\" p-id=\"65181\" width=\"20\" height=\"20\"><path d=\"M206.095059 631.356235v372.856471h302.652235V620.815059h244.766118v383.337412h302.652235V631.356235L631.115294 206.305882z\" fill=\"#ED2061\" p-id=\"65182\"></path><path d=\"M1239.612235 596.239059l-183.446588-183.416471v218.563765l74.179765 74.149647c15.058824 15.058824 34.846118 22.618353 54.633412 22.618353a77.281882 77.281882 0 0 0 54.633411-131.915294M22.648471 596.239059a77.281882 77.281882 0 0 0 109.266823 109.327059l74.179765-74.179765v-218.593882L22.648471 596.239059z\" fill=\"#F9D11F\" p-id=\"65183\"></path><path d=\"M610.213647 8.643765L206.095059 412.822588v218.533647L631.115294 206.305882l425.050353 425.050353v-218.563764L652.047059 8.643765a29.575529 29.575529 0 0 0-41.803294 0\" fill=\"#FF7E50\" p-id=\"65184\"></path></svg>', '#86909c', 1, 0, 0, 0, '/user_admin/data-index', 2);
INSERT INTO `to_sidebar` VALUES (2, NULL, '内容管理', '<svg t=\"1729847261360\" class=\"icon svg-translateY-2-\" viewBox=\"0 0 1024 1024\" version=\"1.1\" xmlns=\"http://www.w3.org/2000/svg\" p-id=\"59229\" width=\"20\" height=\"20\"><path d=\"M237.056 228.864h203.32l74.184 92.501c6.428 7.965 15.929 12.63 25.884 12.63H768v70.144H284.444a33.906 33.906 0 0 0-32.71 26.851l-14.678 63.374v-265.5z m665.6 206.507a32.711 32.711 0 0 0-25.998-13.142H834.56v-71.907c0-39.652-30.151-71.85-67.186-71.85H556.203l-73.956-94.892a32.825 32.825 0 0 0-25.941-12.913H237.852c-37.034 0-67.185 32.256-67.185 71.85v574.92c0 1.592 0.569 3.071 0.796 4.607 0.228 1.82 0.398 3.414 0.853 5.12 0.968 3.641 2.504 6.827 4.38 9.956 0.57 0.796 0.57 1.764 1.139 2.56 0.17 0.228 0.512 0.284 0.625 0.569a34.36 34.36 0 0 0 10.639 9.102c0.967 0.569 1.99 0.626 3.015 1.024 2.56 1.138 5.12 2.39 7.964 2.73a36.01 36.01 0 0 0 4.153 0.228h605.127a34.304 34.304 0 0 0 32.996-28.842l67.242-359.31a37.717 37.717 0 0 0-6.997-29.81z\" p-id=\"59230\" fill=\"#5C56E3\"></path></svg>', '#86909c', 2, 0, 1, 0, '/dd', 2);
INSERT INTO `to_sidebar` VALUES (3, 2, '文章管理', NULL, NULL, 1, 0, 0, 0, '/user_admin/article_data/article-manage?state=all', 2);
INSERT INTO `to_sidebar` VALUES (4, 2, '专栏管理', NULL, NULL, 2, 0, 0, 0, '/user_admin/special/special-manage', 2);
INSERT INTO `to_sidebar` VALUES (5, NULL, '最新', '<svg t=\"1729846753024\" class=\"icon svg-translateY-2-\" viewBox=\"0 0 1024 1024\" version=\"1.1\" xmlns=\"http://www.w3.org/2000/svg\"              p-id=\"56796\" width=\"20\" height=\"20\">           <path             d=\"M1006.762667 443.690667l-129.066667 324.48h-109.824c4.864-39.68 12.16-91.904 21.888-156.757334a1527.893333 1527.893333 0 0 1-34.986667 86.357334l-30.378666 70.4h-110.08l45.824-324.48h87.210666c-1.664 8.490667-11.221333 70.4-28.672 185.813333a17169.066667 17169.066667 0 0 1 73.514667-185.813333h83.754667l-30.293334 185.813333c18.090667-54.272 42.752-116.224 73.984-185.856h87.125334z m-397.994667 160.128h-138.666667l-20.309333 78.421333c-4.266667 16.384-5.845333 26.965333-4.778667 31.701333 1.109333 4.736 4.906667 7.125333 11.434667 7.125334 8.106667 0 14.378667-3.157333 18.773333-9.429334 4.352-6.272 8.874667-18.474667 13.525334-36.48l12.373333-47.829333h101.546667l-6.912 26.752c-5.802667 22.442667-11.648 39.68-17.493334 51.669333-5.845333 12.032-15.573333 24.874667-29.184 38.528-13.653333 13.653333-29.013333 23.893333-46.293333 30.72-17.237333 6.826667-37.504 10.282667-60.842667 10.282667-22.613333 0-41.728-3.370667-57.301333-10.112-15.573333-6.741333-26.666667-15.957333-33.237333-27.733333a78.677333 78.677333 0 0 1-9.984-38.826667c0-14.08 3.413333-34.645333 10.410666-61.610667l27.392-105.728c8.192-31.744 18.816-56.746667 31.872-75.050666a126.293333 126.293333 0 0 1 51.712-42.069334 164.778667 164.778667 0 0 1 68.992-14.634666c30.08 0 53.333333 5.888 69.802667 17.621333 16.512 11.776 26.154667 27.306667 29.013333 46.72 2.816 19.413333-0.298667 46.72-9.386666 81.877333l-12.458667 48.085334z m-88.576-130.133334c-7.424 0-12.629333 2.346667-15.573333 6.997334-2.986667 4.650667-7.082667 17.152-12.373334 37.546666l-6.869333 26.538667h31.274667l6.869333-26.496c4.864-18.773333 6.997333-30.890667 6.485333-36.352-0.512-5.461333-3.797333-8.192-9.813333-8.192zM431.36 400.554667a138.24 138.24 0 0 0-56.96 44.8c-14.378667 19.456-26.069333 46.08-35.114667 79.786666l-30.165333 112.554667c-7.68 28.672-11.52 50.56-11.434667 65.578667 0.085333 15.018667 3.712 28.757333 10.965334 41.258666 7.253333 12.501333 19.456 22.357333 36.608 29.525334 0.725333 0.298667 1.536 0.469333 2.261333 0.768l-2.858667 10.496H216.106667l-7.850667-248.448-68.394667 248.448H17.237333l150.485334-546.56h122.624l14.421333 246.101333 67.797333-246.101333h122.581334l-42.410667 154.112c-7.253333 2.133333-14.464 4.608-21.376 7.68z\"             p-id=\"56797\"></path>         </svg>', '#1890ff', 3, 0, 0, 0, '/last-article', 1);
INSERT INTO `to_sidebar` VALUES (6, NULL, '关注', '        <svg t=\"1729846310923\" class=\"icon svg-translateY-2-\" viewBox=\"0 0 1024 1024\" version=\"1.1\"              xmlns=\"http://www.w3.org/2000/svg\" p-id=\"45742\" width=\"20\" height=\"20\">           <path             d=\"M189.5 416c-5 0-9-4-9-9 0-93.9 76.4-170.3 170.3-170.3 5 0 9 4 9 9s-4 9-9 9c-84 0-152.4 68.4-152.4 152.4 0.1 4.9-3.9 8.9-8.9 8.9z\"             p-id=\"45743\"></path>           <path             d=\"M512.2 909c-79.9 0-348-305-401-366.2-0.5-0.6-0.9-1.1-1.3-1.7-30-44.7-45.9-96.9-45.9-151 0-148.9 120.6-270 268.8-270 66.9 0 130 24.4 179.4 69 49.4-44.7 112.4-69 179.4-69 148.2 0 268.8 121.1 268.8 270 0 54.1-15.9 106.3-45.9 150.9-0.4 0.6-0.9 1.2-1.3 1.7-53 61.2-321.2 366.3-401 366.3zM139.1 520.2c150.4 173.6 330.9 353 373.1 353 42.2 0 222.7-179.3 373.1-353 25.6-38.5 39.2-83.4 39.2-130C924.5 261.1 820 156 691.6 156c-63.1 0-122.3 25.1-166.5 70.6-6.8 6.9-18.9 6.9-25.7 0-44.3-45.5-103.5-70.6-166.6-70.6-128.4 0-232.9 105-232.9 234.1 0 46.6 13.5 91.5 39.2 130.1z m760.5 10.9h0.2-0.2z\"             p-id=\"45744\"></path>         </svg>', '#ef5777', 1, 0, 0, 0, '/attention-article', 1);
INSERT INTO `to_sidebar` VALUES (7, NULL, '帮助中心', '<svg t=\"1729847431947\" class=\"icon svg-translateY-2-\" viewBox=\"0 0 1024 1024\" version=\"1.1\" xmlns=\"http://www.w3.org/2000/svg\" p-id=\"62727\" width=\"20\" height=\"20\"><path d=\"M585.1136 624.0256m-284.928 0a284.928 284.928 0 1 0 569.856 0 284.928 284.928 0 1 0-569.856 0Z\" fill=\"#CFF6FF\" p-id=\"62728\"></path><path d=\"M513.28 950.6304a438.1696 438.1696 0 1 1 345.0368-708.1472 25.6 25.6 0 0 1-40.2944 31.5904 386.9184 386.9184 0 1 0 81.92 238.4384c0-12.1856-0.5632-24.4224-1.6896-36.5056a25.6 25.6 0 0 1 50.944-4.7616c1.28 13.6192 1.9456 27.5456 1.9456 41.2672a438.6304 438.6304 0 0 1-437.8624 438.1184z\" fill=\"#5C56E3\" p-id=\"62729\" data-spm-anchor-id=\"a313x.search_index.0.i115.325a3a817r2SmH\" class=\"selected\"></path><path d=\"M487.4752 738.7136a31.744 30.2592 0 1 0 63.488 0 31.744 30.2592 0 1 0-63.488 0Z\" fill=\"#1E84E9\" p-id=\"62730\"></path><path d=\"M513.28 676.096a25.6 25.6 0 0 1-25.6-25.6V629.76a150.8352 150.8352 0 0 1 79.7184-133.632 115.3536 115.3536 0 0 0-46.7456-216.9344A115.3024 115.3024 0 0 0 397.9776 394.24a25.6 25.6 0 0 1-51.2 0A167.0144 167.0144 0 0 1 399.36 272.7424a166.4512 166.4512 0 0 1 280.064 110.7456 167.168 167.168 0 0 1-87.9104 157.5936A99.84 99.84 0 0 0 538.88 629.76v20.9408a25.6 25.6 0 0 1-25.6 25.3952z\" fill=\"#1285EF\" p-id=\"62731\"></path></svg>', '#86909c', 4, 0, 1, 0, '/help', 2);
INSERT INTO `to_sidebar` VALUES (8, 7, '常见问题', NULL, NULL, 1, 0, 0, 0, '/user_admin/doubt', 2);
INSERT INTO `to_sidebar` VALUES (9, NULL, '综合', ' <svg t=\"1729846613077\" class=\"icon svg-translateY-2-\" viewBox=\"0 0 1024 1024\" version=\"1.1\" xmlns=\"http://www.w3.org/2000/svg\"              p-id=\"53171\" width=\"20\" height=\"20\">           <path             d=\"M512 0a512 512 0 1 0 512 512 512.64 512.64 0 0 0-512-512z m0 955.712A443.712 443.712 0 1 1 955.712 512 444.416 444.416 0 0 1 512 955.712z\"             fill-opacity=\".3\" p-id=\"53172\"></path>           <path d=\"M307.2 512a204.8 204.8 0 1 0 409.6 0 204.8 204.8 0 0 0-409.6 0z\" p-id=\"53173\"></path>         </svg>', '#1890ff', 2, 0, 0, 0, '/content', 1);
INSERT INTO `to_sidebar` VALUES (10, 7, '反馈中心', NULL, NULL, 2, 0, 0, 0, '/user_admin/feedback', 2);
INSERT INTO `to_sidebar` VALUES (11, NULL, '名言空间', '<svg t=\"1729847337880\" class=\"icon svg-translateY-2-\" viewBox=\"0 0 1024 1024\" version=\"1.1\" xmlns=\"http://www.w3.org/2000/svg\" p-id=\"60577\" width=\"20\" height=\"20\"><path d=\"M955.728 428.224c8.385-8.785 3.76-23.536-8.073-25.753l-276.832-51.854c-4.838-0.906-9.02-3.987-11.38-8.383L525.873 93.229c-2.798-5.23-8.342-7.85-13.875-7.896-5.532 0.045-11.075 2.667-13.873 7.896L364.558 342.234c-2.36 4.396-6.543 7.477-11.381 8.383L76.345 402.471c-11.833 2.217-16.458 16.968-8.073 25.753L269.64 639.086c3.564 3.733 5.205 8.952 4.433 14.1l-46.015 282.032c-1.819 12.126 10.394 21.407 21.298 16.182L505 827.827a16.098 16.098 0 0 1 7-1.58 16.1 16.1 0 0 1 7.003 1.58L774.644 951.4c10.904 5.225 23.117-4.056 21.298-16.182l-46.88-287.298 206.666-219.696z\" fill=\"#FFCD00\" p-id=\"60578\"></path><path d=\"M559.42 493.63c-4.517-3.772-110.987-40.332-273.968-16-3.16 0.47-5.913-0.394-8.04-1.992-0.717 4 3.587 7.152 8.988 7.527 115.064 8.021 179.42 54.987 199.492 71.501 40.78-28.923 71.882-50.606 73.063-51.527 3.668-2.856 3.695-6.811 0.465-9.51m135.65-29.972c-41.744 35.168-160.159 116.897-201.52 148.468-4.864 3.711-3.177 9.424 2.098 11.43 17.045 6.488 36.23 11.95 56.421 16.445l159.784-152.228c12.544-13.184 5.238-29.152-10.422-32.661-1.025 3.011-3.259 5.933-6.36 8.546M817.187 640l-0.101 0.045c-70.456 29.709-241.54 79.623-451.762 72.33-25.386-0.88-50.63-3.715-64.786-6.325-2.067-0.38-3.878-1.012-5.476-1.846-10.567 12.224 2.073 21.462 47.148 30.58 131.886 26.676 286.047 38.934 415.304 30.665l-8.884-54.324c43.727-31.431 64.996-58.546 67.524-62.57 2.899-4.616 1.033-8.555 1.033-8.555\" fill=\"#F1A308\" p-id=\"60579\"></path><path d=\"M818.863 646.995c-53.31 5.137-215.894 3.686-311.826-33.167-5.107-1.962-6.834-7.566-2.129-11.194 40.025-30.84 154.62-110.68 195.014-145.035 7.872-6.696 9.95-15.437 0.375-22.542-18.36-13.623-83.168-36.203-158.198-36.816-107.373-0.88-212.858 29.498-259.133 54.09-10.983 5.837-4.392 21.221 6.83 19.495 164.223-25.24 271.495 12.756 276.045 16.67 3.255 2.798 3.074 6.906-0.5 9.715-3.036 2.389-199.263 143.36-258.23 193.11-9.286 7.834-6.845 24.246 8.35 27.018 14.152 2.582 39.406 5.412 64.784 6.284 210.173 7.214 381.314-42.24 451.755-71.63 0 0-2.148-7.057-13.137-5.998\" fill=\"#FFFFFF\" p-id=\"60580\"></path></svg>', '#86909c', 3, 0, 1, 0, '/dictom', 2);
INSERT INTO `to_sidebar` VALUES (12, 11, '专辑管理', NULL, NULL, 1, 0, 0, 0, '/user_admin/dictum/album-manage', 2);
INSERT INTO `to_sidebar` VALUES (13, 11, '名言管理', NULL, NULL, 2, 0, 0, 0, '/user_admin/dictum/manage?state=all', 2);

-- ----------------------------
-- Table structure for to_site_file
-- ----------------------------
DROP TABLE IF EXISTS `to_site_file`;
CREATE TABLE `to_site_file`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'id',
  `title` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_cs_0900_ai_ci NOT NULL COMMENT '标题',
  `content` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_cs_0900_ai_ci NOT NULL COMMENT '内容',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `uid` bigint NOT NULL COMMENT '创建人',
  `theme` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_cs_0900_ai_ci NOT NULL COMMENT '主题',
  `state` int NOT NULL DEFAULT 0 COMMENT '状态（0：正常，1：已失效）',
  `type` varchar(12) CHARACTER SET utf8mb4 COLLATE utf8mb4_cs_0900_ai_ci NULL DEFAULT NULL COMMENT '类型',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 3 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_cs_0900_ai_ci COMMENT = '网站文档' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of to_site_file
-- ----------------------------
INSERT INTO `to_site_file` VALUES (1, '后端技术文档', '## 前言\n\nAurora为社区论坛类型项目，项目为前后端分离，\n**” 努力的意义就是，以后的日子里，放眼望去，都是自己喜欢的人和事“**\n\n## 项目使用技术\n\n### 后端\n\n基于若依二次开发\n> 若依官网：http://www.ruoyi.vip/\n\n**后端开发框架 Spring Boot**   \n\n**容器框架 Undertow 基于 XNIO 的高性能容器** \n\n> 文档地址：https://undertow.io/undertow-docs/undertow-docs-2.0.0/index.html\n\n**权限认证框架 Sa-Token、Jwt 支持多终端认证系统**\n\n>  Sa-Token官网：https://sa-token.dev33.cn/\n\n**关系数据库 MySQL8.0**\n\n**缓存数据库 Redis**\n\n**数据库框架 Mybatis-Plus 快速 CRUD 增加开发效率**   \n> Mybatis-Plus官网：https://baomidou.com/\n\n**数据库框架 p6spy 更强劲的 SQL 分析**\n\n**序列化框架 Jackson 统一使用 jackson 高效可靠**\n\n**Redis客户端 Redisson 性能强劲、API丰富**\n\n**分布式限流 Redisson 全局、请求IP、集群ID 多种限流**\n\n**分布式幂等 Redisson 拦截重复提交**\n\n> Redisson官网：https://redisson.org/\n\n**分布式锁 Lock4j 注解锁、工具锁** \n\n**分布式任务调度 Xxl-Job 高性能 高可靠 易扩展**\n\n>  Xxl-Job 官网：https://www.xuxueli.com/xxl-job/\n\n\n**分布式云存储 阿里云oss**\n\n**监控框架 SpringBoot-Admin 全方位服务监控**\n\n> SpringBoot-Admin官网：https://github.com/codecentric/spring-boot-admin/\n\n**校验框架 Validation 增强接口安全性 严谨性**\n\n**Excel框架 Alibaba EasyExcel 性能优异 扩展性强**\n\n> Alibaba EasyExcel 官网：https://www.yuque.com/easyexcel/doc/easyexcel\n\n**文档框架 knife4j 美化接口文档**\n\n**工具类框架 Hutool、Lombok 减少代码冗余 增加安全性**\n\n> Hutool官网：https://www.hutool.cn/docs/#/\n\n**部署方式 Docker 容器编排 一键部署业务集群**\n\n> Docker 官网：https://docs.docker.com/engine/install/centos/\n\n## 项目架构\n\n### 技术选型架构图\n\n![image.png](https://zsh-aurora.oss-cn-beijing.aliyuncs.com/aurora/2023/04/14/a01880dec42743a289ca3af239f20893.png)\n\n## 项目仓库地址\n\nhttps://gitee.com/jade_purple/aurora\n\n![](https://cdn.nlark.com/yuque/0/2022/png/2145958/1664630401335-c10c1409-2727-487d-ae9f-331570a04d39.png)', '2023-04-14 14:41:00', 1, 'androidstudio', 0, '网站文档');
INSERT INTO `to_site_file` VALUES (2, '前端技术文档', '## 前言\n\nAurora为社区论坛类型项目，项目为前后端分离，\n**” 努力的意义就是，以后的日子里，放眼望去，都是自己喜欢的人和事“**\n\n## 项目使用技术\n\n### 前端\n基于若依二次开发\n> 若依官网：http://www.ruoyi.vip/\n\n**前端开发框架 Vue、Element UI**\n\n> Vue官网：https://cn.vuejs.org/\n> Element 官网：https://element.eleme.cn/#/zh-CN\n\n**vue router 路由管理**\n\n**Axios 异步请求**\n\n**Sass**\n\nAntv G2Plot 数据可视化\n> 官网：https://g2plot.antv.antgroup.com/\n\nMavon-Editor 富文本编辑器\n> 官网：https://gitee.com/kongfanqun/mavonEditor\n\n## 项目架构\n\n### 技术选型架构图\n\n![image.png](https://zsh-aurora.oss-cn-beijing.aliyuncs.com/aurora/2023/04/14/a01880dec42743a289ca3af239f20893.png)\n\n## 项目仓库地址\n\nhttps://gitee.com/jade_purple/aurora\n\n![](https://cdn.nlark.com/yuque/0/2022/png/2145958/1664630401335-c10c1409-2727-487d-ae9f-331570a04d39.png)', '2023-04-14 14:40:12', 1, 'androidstudio', 0, '网站文档');
INSERT INTO `to_site_file` VALUES (3, '答辩小组', '## 第一组：人民当家作组\n\n## 组员：\n> 姓名：周顺 \n> 学号：20031833\n\n\n> 姓名：杨文龙  \n> 学号：20031770\n\n> 姓名：谌宗洪\n> 学号：20031120\n\n> 姓名：田祥君\n> 学号：20031710\n', '2023-04-14 14:59:24', 1, 'androidstudio', 0, '网站文档');

-- ----------------------------
-- Table structure for to_site_info
-- ----------------------------
DROP TABLE IF EXISTS `to_site_info`;
CREATE TABLE `to_site_info`  (
  `id` bigint NOT NULL,
  `realm_name` varchar(60) CHARACTER SET utf8mb4 COLLATE utf8mb4_cs_0900_ai_ci NULL DEFAULT NULL COMMENT '域名',
  `filings` varchar(60) CHARACTER SET utf8mb4 COLLATE utf8mb4_cs_0900_ai_ci NOT NULL COMMENT '备案',
  `security_filings` varchar(60) CHARACTER SET utf8mb4 COLLATE utf8mb4_cs_0900_ai_ci NULL DEFAULT NULL COMMENT '公安备案',
  `mailbox` varchar(60) CHARACTER SET utf8mb4 COLLATE utf8mb4_cs_0900_ai_ci NULL DEFAULT NULL COMMENT '反馈邮箱',
  `state` int NOT NULL DEFAULT 0 COMMENT '状态（0：正常，1：已删除）',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_cs_0900_ai_ci COMMENT = '网站信息表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of to_site_info
-- ----------------------------
INSERT INTO `to_site_info` VALUES (1, 'qixidi.top', '蜀ICP备：2021006746号-2', '', '2978824265@qq.com', 0, '2023-10-01 00:00:00');

-- ----------------------------
-- Table structure for to_tool_info
-- ----------------------------
DROP TABLE IF EXISTS `to_tool_info`;
CREATE TABLE `to_tool_info`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'id',
  `parent_id` bigint NULL DEFAULT NULL COMMENT '父级id',
  `tool_name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_cs_0900_ai_ci NOT NULL COMMENT '工具名称',
  `describe` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_cs_0900_ai_ci NULL DEFAULT NULL COMMENT '描述',
  `icon` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_cs_0900_ai_ci NULL DEFAULT NULL COMMENT '图标',
  `is_parent` int NOT NULL DEFAULT 1 COMMENT '是否为父级（1；父级，2：子级）',
  `tool_url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_cs_0900_ai_ci NOT NULL COMMENT '工具地址',
  `order` int NULL DEFAULT NULL COMMENT '排序',
  `type` int NOT NULL COMMENT '类型',
  `state` int NOT NULL DEFAULT 0 COMMENT '状态（0：正常，1：已失效）',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_cs_0900_ai_ci NOT NULL COMMENT '创建人',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 82 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_cs_0900_ai_ci COMMENT = '工具信息表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of to_tool_info
-- ----------------------------
INSERT INTO `to_tool_info` VALUES (1, NULL, '开发工具', 'xx', NULL, 1, 'xx', 99, 1, 0, '2023-01-31 19:37:49', '1');
INSERT INTO `to_tool_info` VALUES (2, NULL, '开源项目', 'xx', NULL, 1, 'xx', NULL, 1, 0, '2023-01-31 19:37:49', '1');
INSERT INTO `to_tool_info` VALUES (3, NULL, '工具插件', 'xx', NULL, 1, 'xx', NULL, 1, 0, '2023-01-31 19:37:49', '1');
INSERT INTO `to_tool_info` VALUES (4, NULL, '划水摸鱼', 'xx', NULL, 1, 'xx', NULL, 1, 0, '2023-01-31 19:37:49', '1');
INSERT INTO `to_tool_info` VALUES (5, NULL, '便捷网站', 'xx', NULL, 1, 'xx', NULL, 1, 0, '2023-01-31 19:37:49', '1');
INSERT INTO `to_tool_info` VALUES (6, 2, 'smart-doc', 'API文档生成工具smart-doc', NULL, 2, 'https://juejin.cn/post/7028377863850033183', NULL, 1, 0, '2023-01-31 19:37:49', '1');
INSERT INTO `to_tool_info` VALUES (7, 2, 'JustAuth', '第三方登录开源库JustAuth', 'https://www.justauth.cn/logo.png', 2, 'https://www.justauth.cn/', 1, 1, 0, '2023-01-31 19:37:49', '1');
INSERT INTO `to_tool_info` VALUES (8, 2, 'smallc', '微服务开发平台s微服务开发平台smallc微服务开发平台smallcmallc', 'https://gw.alipayobjects.com/zos/rmsportal/BiazfanxmamNRoxxVxka.png', 2, 'https://gitee.com/smallc/SpringBlade', 1, 1, 0, '2023-01-31 19:37:49', '1');
INSERT INTO `to_tool_info` VALUES (9, 1, 'Another Redis', '更快、更好、更稳定的Redis桌面(GUI)管理客户端，兼容Windows、Mac、Linux，性能出众，轻松加载海量键值', 'https://tse2-mm.cn.bing.net/th/id/OIP-C.VZXID7ujbA_QWs087xfnFAAAAA?rs=1&pid=ImgDetMain', 2, 'https://gitee.com/qishibo/AnotherRedisDesktopManager/releases', 1, 1, 0, '2023-01-31 19:37:49', '1');
INSERT INTO `to_tool_info` VALUES (10, 2, 'IJPay支付平台', 'IJPay让支付触手可及', 'https://gitee.com/javen205/IJPay/raw/dev/assets/img/logo.png', 2, 'https://gitee.com/javen205/IJPay', 1, 1, 0, '2023-01-31 19:37:49', '1');
INSERT INTO `to_tool_info` VALUES (11, 2, 'Sa-Token', '轻量级java权限认证框架Sa-Token', 'https://sa-token.cc/logo.png', 2, 'https://sa-token.cc/', NULL, 1, 0, '2023-01-31 19:37:49', '1');
INSERT INTO `to_tool_info` VALUES (12, 5, '程序员导航', 'xx', 'https://www.cxy521.com/static/img/logo.png', 2, 'https://www.cxy521.com/index.html', 1, 1, 0, '2023-01-31 19:37:49', '1');
INSERT INTO `to_tool_info` VALUES (13, 4, '小霸王游戏机', 'xx', 'https://www.yikm.net/logo.png', 2, 'https://www.yikm.net/', 1, 1, 0, '2023-01-31 19:37:49', '1');
INSERT INTO `to_tool_info` VALUES (14, 5, 'MurphyYiTools-工具集合', 'xx', NULL, 2, 'https://tools.murphyyi.com/', NULL, 1, 0, '2023-01-31 19:37:49', '1');
INSERT INTO `to_tool_info` VALUES (24, 2, 'API文档生成工具smart-doc副本', 'xx', NULL, 2, 'https://juejin.cn/post/7028377863850033183', NULL, 1, 1, '2023-01-31 19:37:49', '1');
INSERT INTO `to_tool_info` VALUES (25, 2, '第三方登录开源库JustAuth副本', '第三方登录开源库JustAuth副本', NULL, 2, 'https://github.com/justauth/JustAuth', NULL, 1, 1, '2023-01-31 19:37:49', '1');
INSERT INTO `to_tool_info` VALUES (26, 2, '微服务开发平台smallc副本', 'xx', 'https://bladex.vip/images/logo.png', 2, 'https://gitee.com/smallc/SpringBlade', 1, 1, 1, '2023-01-31 19:37:49', '1');
INSERT INTO `to_tool_info` VALUES (27, 1, 'redis桌面工具副本', 'xx', NULL, 2, 'https://gitee.com/qishibo/AnotherRedisDesktopManager/releases', NULL, 1, 1, '2023-01-31 19:37:49', '1');
INSERT INTO `to_tool_info` VALUES (28, 2, '支付平台副本', 'xx', NULL, 2, 'https://gitee.com/javen205/IJPay', NULL, 1, 1, '2023-01-31 19:37:49', '1');
INSERT INTO `to_tool_info` VALUES (29, 2, '轻量级java权限认证框架Sa-Token副本', 'xx', NULL, 2, 'https://sa-token.cc/', NULL, 1, 1, '2023-01-31 19:37:49', '1');
INSERT INTO `to_tool_info` VALUES (30, 5, '程序员导航副本', 'xx', NULL, 2, 'https://www.cxy521.com/index.html', NULL, 1, 1, '2023-01-31 19:37:49', '1');
INSERT INTO `to_tool_info` VALUES (31, 4, '小霸王游戏机副本', 'xx', NULL, 2, 'https://www.yikm.net/', NULL, 1, 1, '2023-01-31 19:37:49', '1');
INSERT INTO `to_tool_info` VALUES (32, 5, 'MurphyYiTools-工具集合副本', 'xx', NULL, 2, 'https://tools.murphyyi.com/', NULL, 1, 1, '2023-01-31 19:37:49', '1');
INSERT INTO `to_tool_info` VALUES (33, 3, 'Sa-Token', '轻量级java权限认证框架Sa-Token', 'https://sa-token.cc/logo.png', 2, 'https://sa-token.cc/', NULL, 1, 0, '2023-01-31 19:37:49', '1');
INSERT INTO `to_tool_info` VALUES (34, 3, 'Sa-Token', '轻量级java权限认证框架Sa-Token', 'https://sa-token.cc/logo.png', 2, 'https://sa-token.cc/', NULL, 1, 1, '2023-01-31 19:37:49', '1');
INSERT INTO `to_tool_info` VALUES (35, 3, 'Sa-Token', '轻量级java权限认证框架Sa-Token', 'https://sa-token.cc/logo.png', 2, 'https://sa-token.cc/', NULL, 1, 1, '2023-01-31 19:37:49', '1');
INSERT INTO `to_tool_info` VALUES (36, 3, 'Sa-Token', '轻量级java权限认证框架Sa-Token', 'https://sa-token.cc/logo.png', 2, 'https://sa-token.cc/', NULL, 1, 1, '2023-01-31 19:37:49', '1');
INSERT INTO `to_tool_info` VALUES (37, 3, 'Sa-Token', '轻量级java权限认证框架Sa-Token', 'https://sa-token.cc/logo.png', 2, 'https://sa-token.cc/', NULL, 1, 1, '2023-01-31 19:37:49', '1');
INSERT INTO `to_tool_info` VALUES (38, 3, 'Sa-Token', '轻量级java权限认证框架Sa-Token', 'https://sa-token.cc/logo.png', 2, 'https://sa-token.cc/', NULL, 1, 1, '2023-01-31 19:37:49', '1');
INSERT INTO `to_tool_info` VALUES (39, 3, 'Sa-Token', '轻量级java权限认证框架Sa-Token', 'https://sa-token.cc/logo.png', 2, 'https://sa-token.cc/', NULL, 1, 1, '2023-01-31 19:37:49', '1');
INSERT INTO `to_tool_info` VALUES (40, 3, 'Sa-Token', '轻量级java权限认证框架Sa-Token', 'https://sa-token.cc/logo.png', 2, 'https://sa-token.cc/', NULL, 1, 1, '2023-01-31 19:37:49', '1');
INSERT INTO `to_tool_info` VALUES (41, 3, 'Sa-Token', '轻量级java权限认证框架Sa-Token', 'https://sa-token.cc/logo.png', 2, 'https://sa-token.cc/', NULL, 1, 1, '2023-01-31 19:37:49', '1');
INSERT INTO `to_tool_info` VALUES (42, 3, 'Sa-Token', '轻量级java权限认证框架Sa-Token', 'https://sa-token.cc/logo.png', 2, 'https://sa-token.cc/', NULL, 1, 1, '2023-01-31 19:37:49', '1');
INSERT INTO `to_tool_info` VALUES (43, 3, 'Sa-Token', '轻量级java权限认证框架Sa-Token', 'https://sa-token.cc/logo.png', 2, 'https://sa-token.cc/', NULL, 1, 1, '2023-01-31 19:37:49', '1');
INSERT INTO `to_tool_info` VALUES (44, 3, 'Sa-Token', '轻量级java权限认证框架Sa-Token', 'https://sa-token.cc/logo.png', 2, 'https://sa-token.cc/', NULL, 1, 1, '2023-01-31 19:37:49', '1');
INSERT INTO `to_tool_info` VALUES (45, 3, 'Sa-Token', '轻量级java权限认证框架Sa-Token', 'https://sa-token.cc/logo.png', 2, 'https://sa-token.cc/', NULL, 1, 1, '2023-01-31 19:37:49', '1');
INSERT INTO `to_tool_info` VALUES (46, 3, 'Sa-Token', '轻量级java权限认证框架Sa-Token', 'https://sa-token.cc/logo.png', 2, 'https://sa-token.cc/', NULL, 1, 1, '2023-01-31 19:37:49', '1');
INSERT INTO `to_tool_info` VALUES (47, 3, 'Sa-Token', '轻量级java权限认证框架Sa-Token', 'https://sa-token.cc/logo.png', 2, 'https://sa-token.cc/', NULL, 1, 1, '2023-01-31 19:37:49', '1');
INSERT INTO `to_tool_info` VALUES (48, 3, 'Sa-Token', '轻量级java权限认证框架Sa-Token', 'https://sa-token.cc/logo.png', 2, 'https://sa-token.cc/', NULL, 1, 1, '2023-01-31 19:37:49', '1');
INSERT INTO `to_tool_info` VALUES (49, 3, 'Sa-Token', '轻量级java权限认证框架Sa-Token', 'https://sa-token.cc/logo.png', 2, 'https://sa-token.cc/', NULL, 1, 1, '2023-01-31 19:37:49', '1');
INSERT INTO `to_tool_info` VALUES (50, 3, 'Sa-Token', '轻量级java权限认证框架Sa-Token', 'https://sa-token.cc/logo.png', 2, 'https://sa-token.cc/', NULL, 1, 1, '2023-01-31 19:37:49', '1');
INSERT INTO `to_tool_info` VALUES (51, 3, 'Sa-Token', '轻量级java权限认证框架Sa-Token', 'https://sa-token.cc/logo.png', 2, 'https://sa-token.cc/', NULL, 1, 1, '2023-01-31 19:37:49', '1');
INSERT INTO `to_tool_info` VALUES (52, 3, 'Sa-Token', '轻量级java权限认证框架Sa-Token', 'https://sa-token.cc/logo.png', 2, 'https://sa-token.cc/', NULL, 1, 1, '2023-01-31 19:37:49', '1');
INSERT INTO `to_tool_info` VALUES (53, 3, 'Sa-Token', '轻量级java权限认证框架Sa-Token', 'https://sa-token.cc/logo.png', 2, 'https://sa-token.cc/', NULL, 1, 1, '2023-01-31 19:37:49', '1');
INSERT INTO `to_tool_info` VALUES (54, 3, 'Sa-Token', '轻量级java权限认证框架Sa-Token', 'https://sa-token.cc/logo.png', 2, 'https://sa-token.cc/', NULL, 1, 1, '2023-01-31 19:37:49', '1');
INSERT INTO `to_tool_info` VALUES (55, 3, 'Sa-Token', '轻量级java权限认证框架Sa-Token', 'https://sa-token.cc/logo.png', 2, 'https://sa-token.cc/', NULL, 1, 1, '2023-01-31 19:37:49', '1');
INSERT INTO `to_tool_info` VALUES (56, 3, 'Sa-Token', '轻量级java权限认证框架Sa-Token', 'https://sa-token.cc/logo.png', 2, 'https://sa-token.cc/', NULL, 1, 1, '2023-01-31 19:37:49', '1');
INSERT INTO `to_tool_info` VALUES (57, 3, 'Sa-Token', '轻量级java权限认证框架Sa-Token', 'https://sa-token.cc/logo.png', 2, 'https://sa-token.cc/', NULL, 1, 1, '2023-01-31 19:37:49', '1');
INSERT INTO `to_tool_info` VALUES (58, 3, 'Sa-Token', '轻量级java权限认证框架Sa-Token', 'https://sa-token.cc/logo.png', 2, 'https://sa-token.cc/', NULL, 1, 1, '2023-01-31 19:37:49', '1');
INSERT INTO `to_tool_info` VALUES (59, 3, 'Sa-Token', '轻量级java权限认证框架Sa-Token', 'https://sa-token.cc/logo.png', 2, 'https://sa-token.cc/', NULL, 1, 1, '2023-01-31 19:37:49', '1');
INSERT INTO `to_tool_info` VALUES (60, 1, 'Apache ECharts ', '一个基于 JavaScript 的开源可视化图表库', 'https://echarts.apache.org/zh/images/logo.png?_v_=20200710_1', 2, 'https://echarts.apache.org/zh/index.html', 3, 1, 0, '2023-03-16 09:08:41', 'aurora');
INSERT INTO `to_tool_info` VALUES (61, 1, 'G2Plot ', 'G2Plot 是一套简单、易用、并具备一定扩展能力和组合能力的统计图表库', NULL, 2, 'https://g2plot.antv.antgroup.com/manual/introduction', 4, 1, 0, '2023-03-16 09:10:28', 'aurora');
INSERT INTO `to_tool_info` VALUES (62, 5, 'Processon', '免费开源的画图工具', 'https://www.processon.com/assets/imgs/logo_small.svg', 2, 'https://www.processon.com/', 2, 1, 0, '2023-04-02 08:27:39', 'aurora');
INSERT INTO `to_tool_info` VALUES (63, 2, 'RuoYi', '基于SpringBoot、Spring Security、Jwt、Vue的前后端分离的后台管理系统', 'https://leepoo.top/img/1661137617315.png', 2, 'http://doc.ruoyi.vip/ruoyi-vue/', 3, 1, 0, '2023-04-02 09:07:25', 'aurora');
INSERT INTO `to_tool_info` VALUES (64, 66, 'UToos', '新一代效率工具平台 自由组合插件应用，打造专属你的趁手工具集', 'https://res.u-tools.cn/website/video-play-1.png', 2, 'https://www.u.tools/', 2, 1, 0, '2023-05-06 20:07:18', 'aurora');
INSERT INTO `to_tool_info` VALUES (65, 3, 'xftld', '全球网络中继服务，随时随处尽情使用', NULL, 2, 'https://xftld.org/', 3, 1, 0, '2023-05-06 20:13:24', 'aurora');
INSERT INTO `to_tool_info` VALUES (66, NULL, '装机必备', NULL, NULL, 1, 'xx', 1, 1, 0, '2023-05-31 15:57:43', 'aurora');
INSERT INTO `to_tool_info` VALUES (67, 66, 'Bandizip', '一款轻量且功能十分强大解压软件', 'https://www.bandisoft.com/img.front/bandizip_logo.png', 2, 'http://www.bandisoft.com/', 2, 1, 0, '2023-05-31 17:15:49', 'aurora');
INSERT INTO `to_tool_info` VALUES (68, 3, '语雀', '团队和企业的在线文档协同平台，文档笔记的好帮手', 'https://mdn.alipayobjects.com/huamei_0prmtq/afts/img/A*IVdnTJqUp6gAAAAAAAAAAAAADvuFAQ/original', 2, 'https://www.yuque.com/', 4, 1, 0, '2023-05-31 17:19:54', 'aurora');
INSERT INTO `to_tool_info` VALUES (69, 1, 'Tabby', '一个无限可定制的跨平台终端应用程序，用于本地shell，串行，SSH和Telnet连接。', 'https://tabby.sh/32bf32ff6c87e8d18932.svg', 2, 'https://tabby.sh/', 5, 1, 0, '2023-05-31 17:22:31', 'aurora');
INSERT INTO `to_tool_info` VALUES (70, 2, '爱情小站', '为情侣搭建的爱情记录小站，演示地址：https://huibao.love/', NULL, 2, 'https://gitee.com/jade_purple/lover-adminl', 2, 1, 0, '2023-05-31 17:27:51', 'aurora');
INSERT INTO `to_tool_info` VALUES (71, 1, '顺丰到付', '订单', 'DVD', 2, '的v', 33, 1, 1, '2023-06-09 14:30:27', 'aurora');
INSERT INTO `to_tool_info` VALUES (72, 5, '今日热榜', '每日早晚报 ‧ 订阅动态聚合 ‧ 榜中榜 ‧ 热点日历 ‧ 追踪器 ‧ 同步收藏夹 ‧ 更多榜单', 'https://file.ipadown.com/tophub/assets/images/logo.png', 2, 'https://tophub.today/', 3, 1, 0, '2023-07-14 14:46:14', 'aurora');
INSERT INTO `to_tool_info` VALUES (73, 3, 'Hutool', 'Hutool是一个小而全的Java工具类库，通过静态方法封装，降低相关API的学习成本，提高工作效率，使Java拥有函数式语言般的优雅，让Java语言也可以“甜甜的”。', 'https://plus.hutool.cn/images/hutool.svg', 2, 'https://doc.hutool.cn/pages/index/#%F0%9F%93%9A%E7%AE%80%E4%BB%8B', 4, 1, 0, '2023-07-14 14:47:42', 'aurora');
INSERT INTO `to_tool_info` VALUES (74, 2, '盒子IM', '盒子IM是一个仿微信实现的网页版聊天软件，不依赖任何第三方收费组件。', NULL, 2, 'https://gitee.com/bluexsx/box-im#https://gitee.com/link?target=https%3A%2F%2Fwww.boxim.online', 1, 1, 0, '2024-07-24 09:43:08', 'ziwei');
INSERT INTO `to_tool_info` VALUES (75, 2, '信使 Web builder ', '信使 Web builder 是基于 Material 的 Angular 低代码前端框架，丰富的组件库可提供优秀的数字创新体验， 通过拖拽快速构建响应式、多主题、多语言的 Web 页面。', NULL, 2, 'https://github.com/biaogebusy/web-builder', 1, 1, 0, '2024-07-24 09:44:05', 'ziwei');
INSERT INTO `to_tool_info` VALUES (76, 2, 'PublicCMS建站系统', 'PublicCMS是采用2024年主流技术开发的开源JAVACMS系统。', NULL, 2, 'https://gitee.com/sanluan/PublicCMS', 1, 1, 0, '2024-07-24 09:46:32', 'ziwei');
INSERT INTO `to_tool_info` VALUES (77, 2, 'sz-admin后台脚手架', '一个开源RBAC中后台框架，专为现代应用设计。它结合了最新的技术栈，包括后端的Spring Boot 3、JDK 21、Mybatis Flex、Sa-Token、Knife4j和Flyway，以及前端的Vue 3、Vite5、TypeScript和Element Plus，致力于为您提供一个直观、流畅且功能强大的开发体验。', NULL, 2, 'https://szadmin.cn/', 1, 1, 0, '2024-07-24 09:49:33', 'ziwei');
INSERT INTO `to_tool_info` VALUES (78, 1, 'Apifox', 'API 设计、开发、测试一体化协作平台', 'https://tse4-mm.cn.bing.net/th/id/OIP-C.2OJVBCoDv8mtt8-7r9qJxQAAAA?rs=1&pid=ImgDetMain', 2, 'https://apifox.com/?utm_source=bing&utm_medium=sem&utm_campaign=%E5%93%81%E7%89%8C%E8%AF%8D-Apifox&utm_content=Apifox-%E6%A0%B8%E5%BF%83&utm_term=apifox&search_term=apifox&msclkid=cf7683c00fe7140d4a51e5718e97c72b', 3, 1, 0, '2024-08-06 09:52:50', 'ziwei');
INSERT INTO `to_tool_info` VALUES (79, 66, 'Snipaste', 'Snipaste 是一个简单但强大的截图工具', 'https://zh.snipaste.com/img/logo.svg', 2, 'https://zh.snipaste.com/', 2, 1, 0, '2024-09-19 15:57:24', 'ziwei');
INSERT INTO `to_tool_info` VALUES (80, 2, 'AiEditor', 'AiEditor 是一个面向 AI 的下一代富文本编辑器', 'https://aieditor.dev/assets/image/logo.png', 2, 'https://aieditor.dev/zh/getting-started.html', 3, 1, 0, '2024-09-19 16:01:30', 'ziwei');
INSERT INTO `to_tool_info` VALUES (81, 2, 'relation-graph', 'relation-graph是支持Vue2、Vue3、React的关系数据展示组件', '', 2, 'https://relation-graph.com/#/docs/about', 4, 1, 0, '2024-09-19 16:04:50', 'ziwei');
INSERT INTO `to_tool_info` VALUES (82, 1, 'orbstack', 'OrbStack 是运行 Docker 容器和 Linux 的快速、轻便且简单的方法。使用我们的 Docker Desktop 替代方案以光速进行开发', 'https://orbstack.dev/_next/image?url=%2Fimg%2Ficon64.png&w=48&q=75', 2, 'https://orbstack.dev/', 4, 1, 0, '2024-09-29 10:26:21', 'ziwei');

SET FOREIGN_KEY_CHECKS = 1;
