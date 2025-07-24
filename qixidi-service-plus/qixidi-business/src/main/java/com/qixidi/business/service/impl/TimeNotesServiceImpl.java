package com.qixidi.business.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.LocalDateTimeUtil;
import cn.hutool.http.HttpStatus;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.light.core.core.page.TableDataInfo;
import com.qixidi.auth.helper.LoginHelper;
import com.qixidi.business.domain.bo.timeNotes.TimeNotesBo;
import com.qixidi.business.domain.bo.timeNotes.TimeNotesSearchBo;
import com.qixidi.business.domain.entity.TimeNotes;
import com.qixidi.business.domain.enums.CountUserTypeEnums;
import com.qixidi.business.domain.vo.TimeNotesInfoVo;
import com.qixidi.business.domain.vo.TimeNotesVo;
import com.qixidi.business.mapper.TimeNotesMapper;
import com.qixidi.business.mapper.count.CountUserWebsiteMapper;
import com.qixidi.business.service.TimeNotesService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author zi-wei
 * @create 2025/3/26 10:09
 */
@RequiredArgsConstructor
@Service
public class TimeNotesServiceImpl implements TimeNotesService {

    private final TimeNotesMapper timeNotesMapper;
    private final CountUserWebsiteMapper countUserWebsiteMapper;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void add(TimeNotesBo bo) {
        TimeNotes timeNotes = BeanUtil.copyProperties(bo, TimeNotes.class);
        timeNotes.setUid(LoginHelper.getTripartiteUuid());
        timeNotesMapper.insert(timeNotes);
        countUserWebsiteMapper.updateAdd(LoginHelper.getTripartiteUuid(), CountUserTypeEnums.B_TIME_NOTES.getCode());
    }

    @Override
    public void update(TimeNotesBo bo) {
        TimeNotes timeNotes = BeanUtil.copyProperties(bo, TimeNotes.class);
        timeNotesMapper.updateById(timeNotes);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void delete(Long id) {
        timeNotesMapper.deleteById(id);
        countUserWebsiteMapper.updateDelete(LoginHelper.getTripartiteUuid(), CountUserTypeEnums.B_TIME_NOTES.getCode());
    }

    @Override
    public TableDataInfo<TimeNotesVo> list(TimeNotesSearchBo bo, Page<TimeNotes> build) {
        String tripartiteUuid = LoginHelper.getTripartiteUuid();
        LambdaQueryWrapper<TimeNotes> lambdaQueryWrapper = new LambdaQueryWrapper<TimeNotes>()
                .select(TimeNotes::getId, TimeNotes::getTitle, TimeNotes::getRecordTime)
                .like(bo.getTitle() != null, TimeNotes::getTitle, bo.getTitle())
                .orderByDesc(TimeNotes::getRecordTime);
        if (tripartiteUuid != null) {
            lambdaQueryWrapper.eq(TimeNotes::getUid, tripartiteUuid);
        }
        Page<TimeNotes> timeNotesPage = timeNotesMapper.selectPage(build, lambdaQueryWrapper);

        List<TimeNotes> records = timeNotesPage.getRecords();
        TableDataInfo tableDataInfo = new TableDataInfo();
        if (CollectionUtil.isEmpty(records)) return tableDataInfo;
        Map<String, List<TimeNotes>> collect = records.stream().collect(Collectors.groupingBy(
                item -> LocalDateTimeUtil.format(item.getRecordTime(), DatePattern.NORM_MONTH_PATTERN)
        ));

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
                .select(TimeNotes::getId, TimeNotes::getTitle, TimeNotes::getRecordTime)
                .eq(TimeNotes::getUid, LoginHelper.getTripartiteUuid())
                .like(bo.getTitle() != null, TimeNotes::getTitle, bo.getTitle())
                .orderByDesc(TimeNotes::getRecordTime));
        return TableDataInfo.build(timeNotesPage);
    }
}
