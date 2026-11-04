package com.qixidi.mcp.tools;

import cn.hutool.core.date.DateUtil;
import com.light.core.core.page.TableDataInfo;
import com.light.core.enums.BusinessType;
import com.light.exception.ServiceException;
import com.qixidi.auth.domain.entity.TripartiteUser;
import com.qixidi.auth.helper.LoginHelper;
import com.qixidi.auth.helper.OperLogRecorder;
import com.qixidi.business.domain.bo.dictum.DictumAlbumBo;
import com.qixidi.business.domain.bo.dictum.DictumGroupBo;
import com.qixidi.business.domain.bo.dictum.DictumInfoBo;
import com.qixidi.business.domain.entity.dictum.DictumInfo;
import com.qixidi.business.domain.enums.CommonStatusEnums;
import com.qixidi.business.domain.vo.dictum.DictumAlbumVo;
import com.qixidi.business.domain.vo.dictum.DictumGroupVo;
import com.qixidi.business.domain.vo.dictum.DictumInfoVo;
import com.qixidi.business.service.dictum.IDictumAlbumService;
import com.qixidi.business.service.dictum.IDictumGroupService;
import com.qixidi.business.service.dictum.IDictumInfoService;
import com.qixidi.mcp.domain.bo.dictum.McpDictumBo;
import com.qixidi.mcp.domain.bo.dictum.McpDictumQueryBo;
import com.qixidi.mcp.domain.vo.dictum.McpDictumAlbumItemVo;
import com.qixidi.mcp.domain.vo.dictum.McpDictumContextVo;
import com.qixidi.mcp.domain.vo.dictum.McpDictumDeleteVo;
import com.qixidi.mcp.domain.vo.dictum.McpDictumGroupItemVo;
import com.qixidi.mcp.domain.vo.dictum.McpDictumItemVo;
import com.qixidi.mcp.domain.vo.dictum.McpDictumPageVo;
import com.qixidi.mcp.domain.vo.dictum.McpDictumSubmitVo;
import com.qixidi.mcp.utils.McpPageUtils;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 随笔 tool：复刻前台随笔链路（insertByBo/updateByBo/deleteWithValidById），
 * update/delete 属主校验走 getByUidAndId（业务方法无属主校验）
 *
 * @author zi-wei
 */
@Component
public class DictumTools {

    /**
     * @Lazy 断循环依赖：与 ArticleTools 同款防御（业务 Service 链可能经 ChatClient 卷入
     * Spring AI 自动装配环），见 ArticleTools 构造器注释
     */
    private final IDictumInfoService dictumInfoService;
    private final IDictumGroupService dictumGroupService;
    private final IDictumAlbumService dictumAlbumService;
    private final OperLogRecorder operLogRecorder;

    public DictumTools(@Lazy IDictumInfoService dictumInfoService,
                       @Lazy IDictumGroupService dictumGroupService,
                       @Lazy IDictumAlbumService dictumAlbumService,
                       OperLogRecorder operLogRecorder) {
        this.dictumInfoService = dictumInfoService;
        this.dictumGroupService = dictumGroupService;
        this.dictumAlbumService = dictumAlbumService;
        this.operLogRecorder = operLogRecorder;
    }

    @Tool(name = "get_dictum_context", description = """
        获取随笔发布上下文，写/改随笔前调用。

        返回：
        - groups：随笔分类列表 {groupId, name, briefIntroduction}（分类全站共享）
        - albums：我的随笔专辑列表 {albumId, name, briefIntroduction, albumState(1 公开 2 私有 3 关注可看)}

        create_dictum / update_dictum 的 groupId 取值来自 groups、albumId 取值来自 albums（不归入专辑可不传）。""")
    public McpDictumContextVo getDictumContext() {
        TripartiteUser user = LoginHelper.getTripartiteUser();
        if (user == null) {
            throw new ServiceException("登录状态无效，请检查 API key");
        }
        McpDictumContextVo vo = new McpDictumContextVo();
        DictumGroupBo groupBo = new DictumGroupBo();
        groupBo.setState(CommonStatusEnums.NORMAL.getCode());
        vo.setGroups(dictumGroupService.queryList(groupBo).stream().map(g -> {
            McpDictumGroupItemVo item = new McpDictumGroupItemVo();
            item.setGroupId(g.getId());
            item.setName(g.getName());
            item.setBriefIntroduction(g.getBriefIntroduction());
            return item;
        }).collect(Collectors.toList()));
        DictumAlbumBo albumBo = new DictumAlbumBo();
        albumBo.setUid(user.getUuid());
        albumBo.setState(CommonStatusEnums.NORMAL.getCode());
        vo.setAlbums(dictumAlbumService.queryList(albumBo).stream().map(a -> {
            McpDictumAlbumItemVo item = new McpDictumAlbumItemVo();
            item.setAlbumId(a.getId());
            item.setName(a.getName());
            item.setBriefIntroduction(a.getBriefIntroduction());
            item.setAlbumState(a.getAlbumState() == null ? null : a.getAlbumState().intValue());
            return item;
        }).collect(Collectors.toList()));
        return vo;
    }

    @Tool(name = "create_dictum", description = "创建一条随笔（阅读摘抄），返回 dictumId。必填 content/groupId/dictumState；groupId 取值来自 get_dictum_context.groups，albumId 来自 albums。")
    public McpDictumSubmitVo createDictum(
        @ToolParam(description = "随笔内容对象") McpDictumBo bo
    ) {
        TripartiteUser user = LoginHelper.getTripartiteUser();
        if (user == null) {
            throw new ServiceException("登录状态无效，请检查 API key");
        }
        checkCreateRequired(bo);
        DictumInfoBo info = toInfoBo(bo);
        // uid 由 service 层强制取当前登录用户，tool 层不传
        if (!Boolean.TRUE.equals(dictumInfoService.insertByBo(info)) || info.getId() == null) {
            throw new ServiceException("随笔创建失败");
        }
        operLogRecorder.record("MCP随笔", "创建随笔", BusinessType.INSERT,
            user.getUsername(), "id=" + info.getId() + ", content=" + brief(bo.getContent()), null);
        return submitVo(info.getId(), "created");
    }

    @Tool(name = "update_dictum", description = "更新自己的随笔，仅传需修改的字段（未传保持不变）。dictumId 必传（来自 search_dictums 或 create_dictum 返回值），仅限本人随笔。")
    public McpDictumSubmitVo updateDictum(
        @ToolParam(description = "随笔内容对象，dictumId 必传，其余仅传需修改字段") McpDictumBo bo
    ) {
        TripartiteUser user = LoginHelper.getTripartiteUser();
        if (user == null) {
            throw new ServiceException("登录状态无效，请检查 API key");
        }
        if (bo.getDictumId() == null) {
            throw new ServiceException("dictumId 不能为空");
        }
        // 业务 updateByBo 无属主校验，tool 层先按 uid+id 校验属主
        checkOwnership(user.getUuid(), bo.getDictumId());
        DictumInfoBo info = toInfoBo(bo);
        info.setId(bo.getDictumId());
        if (!Boolean.TRUE.equals(dictumInfoService.updateByBo(info))) {
            throw new ServiceException("随笔更新失败");
        }
        operLogRecorder.record("MCP随笔", "更新随笔", BusinessType.UPDATE,
            user.getUsername(), "id=" + bo.getDictumId() + ", content=" + brief(bo.getContent()), null);
        return submitVo(bo.getDictumId(), "updated");
    }

    @Tool(name = "delete_dictum", description = "删除自己的随笔，仅限本人，删除后不可恢复。")
    public McpDictumDeleteVo deleteDictum(
        @ToolParam(description = "随笔 id（来自 search_dictums 或 create_dictum 返回值）") Long dictumId
    ) {
        TripartiteUser user = LoginHelper.getTripartiteUser();
        if (user == null) {
            throw new ServiceException("登录状态无效，请检查 API key");
        }
        // 业务 deleteWithValidById 无属主校验，tool 层先按 uid+id 校验属主
        DictumInfo owned = checkOwnership(user.getUuid(), dictumId);
        boolean deleted = dictumInfoService.deleteWithValidById(dictumId, owned.getGroupId());
        operLogRecorder.record("MCP随笔", "删除随笔", BusinessType.DELETE,
            user.getUsername(), "id=" + dictumId, null);
        McpDictumDeleteVo result = new McpDictumDeleteVo();
        result.setDictumId(dictumId);
        result.setDeleted(deleted);
        return result;
    }

    @Tool(name = "search_dictums", description = """
        多条件搜索本人的随笔（阅读摘抄）：内容/标签模糊、分类/专辑/出处作者精确、可见状态。

        返回 {total, rows[]}，rows 每条含：
        dictumId / content / groupId + groupName / albumId + albumName（未归专辑为空）/ labelList（标签数组）/
        author（出处作者）/ worksName（出处作品）/ dictumState(1 公开 2 私有 3 关注可看) / updateTime(yyyy-MM-dd HH:mm:ss)""")
    public McpDictumPageVo searchDictums(
        @ToolParam(description = "查询条件对象") McpDictumQueryBo query,
        @ToolParam(description = "页码，从 1 开始，默认 1", required = false) Integer pageNum,
        @ToolParam(description = "每页条数，默认 100，最大 500", required = false) Integer pageSize
    ) {
        TripartiteUser user = LoginHelper.getTripartiteUser();
        if (user == null) {
            throw new ServiceException("登录状态无效，请检查 API key");
        }
        // 与前台 roleList 同链路：uid 限定本人（BO 自带分页字段，双传 bo,bo）
        DictumInfoBo bo = new DictumInfoBo();
        bo.setUid(user.getUuid());
        bo.setContent(query.getContent());
        bo.setLabel(query.getLabel());
        bo.setGroupId(query.getGroupId());
        bo.setAlbumId(query.getAlbumId());
        bo.setAuthor(query.getAuthor());
        bo.setDictumState(query.getDictumState());
        bo.setPageNum(McpPageUtils.pageNum(pageNum));
        bo.setPageSize(McpPageUtils.pageSize(pageSize));
        TableDataInfo<DictumInfoVo> page = dictumInfoService.queryPageList(bo, bo);
        List<McpDictumItemVo> rows = page.getRows().stream().map(this::toItemVo).collect(Collectors.toList());
        McpDictumPageVo result = new McpDictumPageVo();
        result.setTotal(page.getTotal());
        result.setRows(rows);
        return result;
    }

    /**
     * MCP 入参对象 → 业务 BO（字段映射集中一处，create/update 共用；update 只 set 有值字段，
     * 业务层 updateById 忽略 null 列实现"未传保持不变"）
     */
    private DictumInfoBo toInfoBo(McpDictumBo bo) {
        DictumInfoBo info = new DictumInfoBo();
        info.setContent(bo.getContent());
        info.setGroupId(bo.getGroupId());
        info.setAlbumId(bo.getAlbumId());
        info.setLabel(bo.getLabel());
        info.setAuthor(bo.getAuthor());
        info.setWorksName(bo.getWorksName());
        info.setPicture(bo.getPicture());
        info.setDictumState(bo.getDictumState());
        return info;
    }

    /**
     * 新增必填校验：与前台 @NotBlank/@NotNull(AddGroup) 同语义，
     * MCP 直调 Service 不经过 Controller 参数校验，需在此兜底
     */
    private void checkCreateRequired(McpDictumBo bo) {
        if (bo.getContent() == null || bo.getContent().isBlank()) {
            throw new ServiceException("content 不能为空");
        }
        if (bo.getGroupId() == null) {
            throw new ServiceException("groupId 不能为空，请先调用 get_dictum_context 获取分类");
        }
        if (bo.getDictumState() == null) {
            throw new ServiceException("dictumState 不能为空（1 公开 2 私有 3 关注可看）");
        }
    }

    /**
     * 属主校验：按 uid+id 查单条，查不到即不存在或非本人
     */
    private DictumInfo checkOwnership(String uid, Long dictumId) {
        DictumInfo owned = dictumInfoService.getByUidAndId(uid, dictumId);
        if (owned == null) {
            throw new ServiceException("随笔不存在或无权操作他人内容");
        }
        return owned;
    }

    /**
     * 操作日志正文截断，避免全文进日志
     */
    private String brief(String content) {
        if (content == null) {
            return null;
        }
        return content.length() <= 50 ? content : content.substring(0, 50) + "...";
    }

    private McpDictumItemVo toItemVo(DictumInfoVo v) {
        McpDictumItemVo row = new McpDictumItemVo();
        row.setDictumId(v.getId());
        row.setContent(v.getContent());
        row.setGroupId(v.getGroupId());
        row.setGroupName(v.getGroupName());
        row.setAlbumId(v.getAlbumId());
        row.setAlbumName(v.getAlbumName());
        row.setLabelList(v.getLabelList());
        row.setAuthor(v.getAuthor());
        row.setWorksName(v.getWorksName());
        row.setDictumState(v.getDictumState());
        row.setUpdateTime(v.getUpdateTime() == null ? null : DateUtil.formatDateTime(v.getUpdateTime()));
        return row;
    }

    private McpDictumSubmitVo submitVo(Long dictumId, String status) {
        McpDictumSubmitVo vo = new McpDictumSubmitVo();
        vo.setDictumId(dictumId);
        vo.setStatus(status);
        return vo;
    }
}
