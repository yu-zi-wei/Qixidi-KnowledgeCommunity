package com.qixidi.business.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.LocalDateTimeUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpStatus;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.light.core.core.page.TableDataInfo;
import com.light.core.enums.MsgEnums;
import com.light.exception.ServiceException;
import com.qixidi.auth.domain.entity.TripartiteUser;
import com.qixidi.auth.domain.enums.UserRoleEnums;
import com.qixidi.auth.helper.LoginHelper;
import com.qixidi.business.domain.bo.timeNotes.TimeNotesBo;
import com.qixidi.business.domain.bo.timeNotes.TimeNotesSearchBo;
import com.qixidi.business.domain.entity.TimeNotes;
import com.qixidi.business.domain.vo.TimeNotesInfoVo;
import com.qixidi.business.domain.vo.TimeNotesVo;
import com.qixidi.business.mapper.TimeNotesMapper;
import com.qixidi.business.service.TimeNotesService;
import com.qixidi.business.service.album.IAlbumService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author zi-wei
 * @create 2025/3/26 10:09
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class TimeNotesServiceImpl implements TimeNotesService {

    private final TimeNotesMapper timeNotesMapper;
    private final IAlbumService albumService;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void add(TimeNotesBo bo) {
        TripartiteUser tripartiteUser = LoginHelper.getTripartiteUser();
        if (!UserRoleEnums.getAdvancedRoleList().contains(tripartiteUser.getRoleId())) {
            throw new ServiceException(MsgEnums.NOT_CREATOR);
        }
        TimeNotes timeNotes = BeanUtil.copyProperties(bo, TimeNotes.class);
        timeNotes.setUid(tripartiteUser.getUuid());
        timeNotesMapper.insert(timeNotes);
        syncAlbumQuietly(timeNotes);
    }

    @Override
    public void update(TimeNotesBo bo) {
        TripartiteUser tripartiteUser = LoginHelper.getTripartiteUser();
        if (!UserRoleEnums.getAdvancedRoleList().contains(tripartiteUser.getRoleId())) {
            throw new ServiceException(MsgEnums.NOT_CREATOR);
        }
        TimeNotes existing = timeNotesMapper.selectById(bo.getId());
        if (existing != null) {
            checkOwnership(existing.getUid());
        }
        TimeNotes timeNotes = BeanUtil.copyProperties(bo, TimeNotes.class);
        timeNotesMapper.updateById(timeNotes);
        // 以库中最新数据为准重新解析（bo 可能不带 uid；内容被清空时同步清空相册）
        TimeNotes updated = timeNotesMapper.selectById(bo.getId());
        if (updated != null) {
            syncAlbumQuietly(updated);
        }
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void delete(Long id) {
        TimeNotes existing = timeNotesMapper.selectById(id);
        if (existing != null) {
            checkOwnership(existing.getUid());
        }
        timeNotesMapper.deleteById(id);
        try {
            albumService.removeBySource(IAlbumService.SOURCE_TIME_NOTES, id);
        } catch (Exception e) {
            log.warn("删除小记相册记录失败, noteId={}: {}", id, e.getMessage());
        }
    }

    /**
     * 前台链路属主校验：前台用户仅可操作自己的小记；后台 sys_user 链路（无前台会话）放行
     */
    private void checkOwnership(String ownerUid) {
        TripartiteUser user = LoginHelper.getTripartiteUser();
        if (user != null && !user.getUuid().equals(ownerUid)) {
            throw new ServiceException("无权操作他人内容");
        }
    }

    /**
     * 解析小记内容图片入相册，失败不影响小记主流程
     */
    private void syncAlbumQuietly(TimeNotes timeNotes) {
        try {
            albumService.syncImages(IAlbumService.SOURCE_TIME_NOTES,
                    timeNotes.getId(), timeNotes.getUid(), timeNotes.getRecordTime(), timeNotes.getContent());
        } catch (Exception e) {
            log.warn("小记图片解析入相册失败, noteId={}: {}", timeNotes.getId(), e.getMessage());
        }
    }

    @Override
    public TableDataInfo<TimeNotesVo> list(TimeNotesSearchBo bo, Page<TimeNotes> build) {
        LambdaQueryWrapper<TimeNotes> lambdaQueryWrapper = new LambdaQueryWrapper<TimeNotes>()
                .select(TimeNotes::getId, TimeNotes::getTitle, TimeNotes::getRecordTime, TimeNotes::getContent)
                .like(bo.getTitle() != null, TimeNotes::getTitle, bo.getTitle())
                .eq(bo.getUid() != null, TimeNotes::getUid, bo.getUid())
                .orderByDesc(TimeNotes::getRecordTime);
        Page<TimeNotes> timeNotesPage = timeNotesMapper.selectPage(build, lambdaQueryWrapper);
        List<TimeNotes> records = timeNotesPage.getRecords();
        TableDataInfo tableDataInfo = new TableDataInfo();
        if (CollectionUtil.isEmpty(records)) return tableDataInfo;
        Map<String, List<TimeNotes>> collect = new HashMap<>();
        for (TimeNotes item : records) {
            if (StrUtil.isNotEmpty(item.getContent()) && !"".equals(item.getContent())) {
                item.setIsContent(true);
            }
            String recordTime = LocalDateTimeUtil.format(item.getRecordTime(), DatePattern.NORM_MONTH_PATTERN);
            collect.computeIfAbsent(recordTime, v -> new ArrayList<>()).add(item);
        }
        //对key 按时间降序排序
        List<Map.Entry<String, List<TimeNotes>>> entryList = new ArrayList<>(collect.entrySet());
        // 定义时间格式化器
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM");
        entryList.sort((o1, o2) -> {
            YearMonth yearMonth1 = YearMonth.parse(o1.getKey(), formatter);
            YearMonth yearMonth2 = YearMonth.parse(o2.getKey(), formatter);
            return yearMonth2.compareTo(yearMonth1);
        });

        List<TimeNotesVo> list = new ArrayList<>();
        for (Map.Entry<String, List<TimeNotes>> entry : entryList) {
            TimeNotesVo timeNotesVo = new TimeNotesVo(entry.getKey(), entry.getValue());
            list.add(timeNotesVo);
        }
        tableDataInfo.setTotal(timeNotesPage.getTotal());
        tableDataInfo.setCode(HttpStatus.HTTP_OK);
        tableDataInfo.setRows(list);
        return tableDataInfo;
    }

    @Override
    public TimeNotesInfoVo getInfo(Long id) {
        String uid = LoginHelper.getTripartiteUuid();
        TimeNotes timeNotes = timeNotesMapper.selectById(id);
        TimeNotesInfoVo timeNotesInfoVo = BeanUtil.copyProperties(timeNotes, TimeNotesInfoVo.class);
        if (uid != null && uid.equals(timeNotes.getUid())) {
            timeNotesInfoVo.setIsAuthor(0);
        }
        return timeNotesInfoVo;
    }

    @Override
    public TableDataInfo<TimeNotes> queryList(TimeNotesSearchBo bo, Page<TimeNotes> build) {
        Page<TimeNotes> timeNotesPage = timeNotesMapper.selectPage(build, new LambdaQueryWrapper<TimeNotes>()
                .select(TimeNotes::getId, TimeNotes::getTitle, TimeNotes::getRecordTime, TimeNotes::getContent)
                .eq(TimeNotes::getUid, LoginHelper.getTripartiteUuid())
                .like(bo.getTitle() != null, TimeNotes::getTitle, bo.getTitle())
                .orderByDesc(TimeNotes::getRecordTime));
        List<TimeNotes> records = timeNotesPage.getRecords();
        for (TimeNotes item : records) {
            if (StrUtil.isNotEmpty(item.getContent())) {
                item.setIsContent(true);
            }
        }
        return TableDataInfo.build(timeNotesPage);
    }
}
