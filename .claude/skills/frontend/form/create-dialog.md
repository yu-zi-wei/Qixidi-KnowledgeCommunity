# 下拉旁新建 + 创建弹窗模式

场景：表单里的下拉选择器（专辑/专栏等）旁边放「+ 新建」按钮，弹窗创建后自动选中新项并刷新列表。

已有实例（照抄蓝本）：
- `components/readingEssays/AlbumCreateDialog.vue`（随笔专辑，EssayDrawer 内使用）
- `components/article/SpecialCreateDialog.vue`（文章专栏，WritePageContent 发布设置内使用）

---

## 数据流（三层）

```
CreateDialog emit success(id)
  → 表单组件：formData.value.xxxId = id（自动选中）+ emit xxxCreated
    → 页面：@xxx-created 刷新对应 useAsyncData
```

页面刷新绑定必须包箭头函数——`refresh` 的签名是 `(opts?) => Promise<void>`，
直接绑到 emit（参数是 id: number）会类型不匹配：

```vue
<!-- ❌ TS2322 -->
<ArticleWritePageContent @special-created="refreshSpecials" />
<!-- ✅ -->
<ArticleWritePageContent @special-created="() => refreshSpecials()" />
```

## 弹窗组件契约

- Props：`{ show: boolean }`；Emits：`'update:show'` + `success: [id: number]`
- `watch(() => props.show)` 打开时 `formData.value = defaultForm()` 重置
- 封面 OSS 上传用 `customUploadRequest`（useOssApi.uploadFile + onProgress），预览态提供删除/替换
- `handleConfirm`：validate → api.create → message.success → emit update:show false + emit success(id)；
  失败交给 useApi 统一拦截，`catch {}` 留空即可
- 必填校验全放前端 FormRules（后端 Bo 的 AddGroup 校验只做兜底）
- 样式类名带业务前缀（`album-` / `special-`），禁止复用 `cover-upload` 这类通用名（scoped CSS 冲突）

## 后端配套接口（前台创建类）

照 `FdDictumAlbumController.add` / `SpecialFrkController.add`：

```java
@Log(title = "专栏信息", businessType = BusinessType.INSERT)
@RepeatSubmit()
@PostMapping("/aut/special")
public Long add(@Validated(AddGroup.class) @RequestBody SpecialInformationBo bo) {
    if (!iSpecialInformationService.insertByBo(bo)) throw new ServiceException("创建专栏失败");
    return bo.getId();  // 必须返回新 id，前端据此自动选中
}
```

- 不加角色校验：靠登录态（`/aut` 子路径）+ Service `validEntityBeforeSave` 里 `LoginHelper.getTripartiteUuid()` 落 uid
- `insertByBo` 返回 Boolean 并回填 `bo.setId(add.getId())`，Controller 据此返回新 id

## 检查清单

- [ ] 先 Write 弹窗组件文件，再 Edit 引用方（Vite transform 缓存坑）
- [ ] 下拉与按钮同行：外层 flex 容器，按钮 `flex-shrink: 0`
- [ ] 创建成功自动选中新项（formData 赋值）
- [ ] 页面监听 emit 后刷新列表（箭头函数包裹 refresh）
- [ ] 类名带业务前缀；双主题走 CSS 变量
