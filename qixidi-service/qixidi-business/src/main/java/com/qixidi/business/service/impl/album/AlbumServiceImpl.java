package com.qixidi.business.service.impl.album;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpStatus;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.light.core.core.page.TableDataInfo;
import com.qixidi.business.domain.bo.album.AlbumSearchBo;
import com.qixidi.business.domain.entity.TimeNotes;
import com.qixidi.business.domain.entity.album.Album;
import com.qixidi.business.domain.vo.album.AlbumMonthVo;
import com.qixidi.business.domain.vo.album.AlbumVo;
import com.qixidi.business.mapper.TimeNotesMapper;
import com.qixidi.business.mapper.album.AlbumMapper;
import com.qixidi.business.service.album.IAlbumService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 相册
 *
 * @author zi-wei
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class AlbumServiceImpl implements IAlbumService {

    /**
     * http(s) 链接候选：遇到空白/引号/尖括号终止
     */
    private static final Pattern URL_PATTERN = Pattern.compile("https?://[^\\s\"'<>]+");

    /**
     * 结尾为图片后缀（允许带 ?query 或 #fragment）
     */
    private static final Pattern IMG_SUFFIX_PATTERN =
            Pattern.compile("\\.(jpg|jpeg|png|gif|webp|bmp|svg)([?#].*)?$", Pattern.CASE_INSENSITIVE);

    /**
     * markdown 语法等带来的尾部标点，截掉后再做后缀判断
     */
    private static final Pattern TRAILING_PUNCTUATION =
            Pattern.compile("[)）\\]}】>.,;，。；！？…]+$");

    /**
     * 图片地址长度上限（与 b_album.url 列宽一致），超长的按脏数据跳过
     */
    private static final int MAX_URL_LENGTH = 2048;

    /**
     * 时间筛选参数：yyyy-MM 按月 / yyyy 按年
     */
    private static final Pattern TIME_FILTER_PATTERN = Pattern.compile("^(\\d{4})(?:-(\\d{2}))?$");

    private final AlbumMapper albumMapper;
    private final TimeNotesMapper timeNotesMapper;

    @Override
    public TableDataInfo<AlbumVo> list(AlbumSearchBo bo) {
        LambdaQueryWrapper<Album> wrapper = new LambdaQueryWrapper<Album>()
                .eq(StrUtil.isNotBlank(bo.getUid()), Album::getUid, bo.getUid());
        applyTimeFilter(bo.getMonth(), wrapper);
        Page<Album> page = albumMapper.selectPage(bo.build(),
                wrapper.orderByDesc(Album::getSourceTime)
                        .orderByDesc(Album::getId));
        TableDataInfo<AlbumVo> tableDataInfo = new TableDataInfo<>();
        tableDataInfo.setTotal(page.getTotal());
        tableDataInfo.setCode(HttpStatus.HTTP_OK);
        tableDataInfo.setRows(BeanUtil.copyToList(page.getRecords(), AlbumVo.class));
        return tableDataInfo;
    }

    /**
     * 时间范围过滤：yyyy-MM 按月 / yyyy 按年，走 idx_source_time 索引；非法格式忽略
     */
    private void applyTimeFilter(String month, LambdaQueryWrapper<Album> wrapper) {
        if (StrUtil.isBlank(month)) {
            return;
        }
        Matcher matcher = TIME_FILTER_PATTERN.matcher(month);
        if (!matcher.matches()) {
            log.warn("【相册】忽略非法时间筛选参数: {}", month);
            return;
        }
        int year = Integer.parseInt(matcher.group(1));
        String monthGroup = matcher.group(2);
        LocalDate start = monthGroup != null
                ? LocalDate.of(year, Integer.parseInt(monthGroup), 1)
                : LocalDate.of(year, 1, 1);
        LocalDate end = monthGroup != null ? start.plusMonths(1) : start.plusYears(1);
        wrapper.ge(Album::getSourceTime, start)
                .lt(Album::getSourceTime, end);
    }

    @Override
    public List<AlbumMonthVo> listMonths() {
        List<Map<String, Object>> maps = albumMapper.selectMaps(new QueryWrapper<Album>()
                .select("DATE_FORMAT(source_time, '%Y-%m') as month", "count(*) as count")
                .groupBy("DATE_FORMAT(source_time, '%Y-%m')")
                .orderByDesc("month"));
        List<AlbumMonthVo> months = new ArrayList<>(maps.size());
        for (Map<String, Object> map : maps) {
            Object month = map.get("month");
            // source_time 为空的记录不进目录
            if (month == null) {
                continue;
            }
            months.add(new AlbumMonthVo(month.toString(), ((Number) map.get("count")).longValue()));
        }
        return months;
    }

    /**
     * REQUIRES_NEW：解析失败只回滚相册自身，不污染调用方（小记）的主事务
     */
    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
    @Override
    public void syncImages(Integer source, Long sourceId, String uid, LocalDate sourceTime, String content) {
        albumMapper.delete(new LambdaQueryWrapper<Album>()
                .eq(Album::getSource, source)
                .eq(Album::getSourceId, sourceId));
        List<String> urls = extractImageUrls(content);
        if (CollectionUtil.isEmpty(urls)) {
            return;
        }
        List<Album> albums = new ArrayList<>(urls.size());
        for (String url : urls) {
            Album album = new Album();
            album.setUid(uid);
            album.setSourceId(sourceId);
            album.setSourceTime(sourceTime);
            album.setSource(source);
            album.setUrl(url);
            albums.add(album);
        }
        albumMapper.insertBatch(albums);
    }

    @Override
    public void removeBySource(Integer source, Long sourceId) {
        albumMapper.delete(new LambdaQueryWrapper<Album>()
                .eq(Album::getSource, source)
                .eq(Album::getSourceId, sourceId));
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public int backfillTimeNotes() {
        List<TimeNotes> notes = timeNotesMapper.selectList(new LambdaQueryWrapper<TimeNotes>()
                .select(TimeNotes::getId, TimeNotes::getUid, TimeNotes::getRecordTime, TimeNotes::getContent));
        // 全删重插，保证接口可重复触发
        albumMapper.delete(new LambdaQueryWrapper<Album>()
                .eq(Album::getSource, SOURCE_TIME_NOTES));
        List<Album> albums = new ArrayList<>();
        for (TimeNotes note : notes) {
            for (String url : extractImageUrls(note.getContent())) {
                Album album = new Album();
                album.setUid(note.getUid());
                album.setSourceId(note.getId());
                album.setSourceTime(note.getRecordTime());
                album.setSource(SOURCE_TIME_NOTES);
                album.setUrl(url);
                albums.add(album);
            }
        }
        if (CollectionUtil.isNotEmpty(albums)) {
            albumMapper.insertBatch(albums);
        }
        log.info("【相册回填】扫描小记 {} 条，解析入库图片 {} 张", notes.size(), albums.size());
        return albums.size();
    }

    /**
     * 解析文本中的图片地址：http(s) 打头、结尾为图片后缀（允许 query/hash），按出现顺序去重
     */
    private List<String> extractImageUrls(String content) {
        if (StrUtil.isBlank(content)) {
            return Collections.emptyList();
        }
        LinkedHashSet<String> urls = new LinkedHashSet<>();
        Matcher matcher = URL_PATTERN.matcher(content);
        while (matcher.find()) {
            String url = TRAILING_PUNCTUATION.matcher(matcher.group()).replaceAll("");
            if (url.length() > MAX_URL_LENGTH) {
                log.warn("【相册解析】跳过超长图片地址, length={}: {}...", url.length(), url.substring(0, 100));
                continue;
            }
            if (IMG_SUFFIX_PATTERN.matcher(url).find()) {
                urls.add(url);
            }
        }
        return new ArrayList<>(urls);
    }
}
