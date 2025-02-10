package com.qixidi.business.service.impl.user;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateUtil;
import com.qixidi.business.domain.bo.user.UserReportBo;
import com.qixidi.business.domain.entity.user.UserReport;
import com.qixidi.business.domain.vo.user.UserReportVo;
import com.qixidi.business.mapper.TripartiteUserMapper;
import com.qixidi.business.mapper.user.UserReportMapper;
import com.qixidi.business.service.user.IUserReportService;
import com.light.core.core.domain.PageQuery;
import com.light.core.core.page.TableDataInfo;
import com.qixidi.auth.helper.LoginHelper;
import com.light.core.utils.StringUtils;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.util.*;

/**
 * 用户签到Service业务层处理
 *
 * @author aurora
 * @date 2023-04-10
 */
@RequiredArgsConstructor
@Service
public class UserReportServiceImpl implements IUserReportService {

    private final UserReportMapper baseMapper;
    private final TripartiteUserMapper tripartiteUserMapper;

    /**
     * 查询用户签到
     *
     * @param id 用户签到主键
     * @return 用户签到
     */
    @Override
    public UserReportVo queryById(Long id) {
        return baseMapper.selectVoById(id);
    }

    /**
     * 查询用户签到列表
     *
     * @param bo 用户签到
     * @return 用户签到
     */
    @Override
    public TableDataInfo<UserReportVo> queryPageList(UserReportBo bo, PageQuery pageQuery) {
        LambdaQueryWrapper<UserReport> lqw = buildQueryWrapper(bo);
        Page<UserReportVo> result = baseMapper.selectVoPage(pageQuery.build(), lqw);
        return TableDataInfo.build(result);
    }

    /**
     * 查询用户签到列表
     *
     * @param bo 用户签到
     * @return 用户签到
     */
    @Override
    public List<UserReportVo> queryList(UserReportBo bo) {
        LambdaQueryWrapper<UserReport> lqw = buildQueryWrapper(bo);
        return baseMapper.selectVoList(lqw);
    }

    private LambdaQueryWrapper<UserReport> buildQueryWrapper(UserReportBo bo) {
        Map<String, Object> params = bo.getParams();
        LambdaQueryWrapper<UserReport> lqw = Wrappers.lambdaQuery();
        lqw.eq(StringUtils.isNotBlank(bo.getUid()), UserReport::getUid, bo.getUid());
        lqw.eq(bo.getReportTime() != null, UserReport::getReportTime, bo.getReportTime());
        lqw.eq(bo.getACurrency() != null, UserReport::getACurrency, bo.getACurrency());
        return lqw;
    }

    /**
     * 用户签到
     *
     * @param bo 用户签到
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean insertByBo(UserReportBo bo) throws Exception {
        String uuid = LoginHelper.getTripartiteUuid();
        Integer integer = baseMapper.selectIsReportTime(bo.getReportTime());
        if (integer > 0) {
            throw new Exception("今日已签到");
        }
        UserReport add = BeanUtil.toBean(bo, UserReport.class);
        add.setUid(uuid);
        tripartiteUserMapper.updateACurrency(uuid, bo.getACurrency());
        boolean flag = baseMapper.insert(add) > 0;
        if (flag) {
            bo.setId(add.getId());
        }
        return flag;
    }

    /**
     * 修改用户签到
     *
     * @param bo 用户签到
     * @return 结果
     */
    @Override
    public Boolean updateByBo(UserReportBo bo) {
        UserReport update = BeanUtil.toBean(bo, UserReport.class);
        return baseMapper.updateById(update) > 0;
    }

    /**
     * 批量删除用户签到
     *
     * @param ids 需要删除的用户签到主键
     * @return 结果
     */
    @Override
    public Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid) {
        return baseMapper.deleteBatchIds(ids) > 0;
    }

    @Override
    public Map<String, Object> List() {
        String uuid = LoginHelper.getTripartiteUuid();
        Map<String, Object> map = new HashMap();
        Long acurrency = tripartiteUserMapper.selectAcurrency(uuid);
        List<String> UserReportVoList = baseMapper.selectReportTime(uuid);
        map.put("list", UserReportVoList);
        map.put("acurrency", acurrency);
        if (CollectionUtils.isEmpty(UserReportVoList)) {
            map.put("ctnFatalism", 0);
            map.put("sumFatalism", 0);
            map.put("isReport", false);
            return map;
        }
        String today = DateUtil.today();
        if (today.equals(UserReportVoList.get(0))) {
            map.put("isReport", true);
        }
        map.put("ctnFatalism", getSignDay(UserReportVoList));
        map.put("sumFatalism", UserReportVoList.size());
        return map;
    }

    /**
     * 获取 某个时间段 最近的时间连续天数
     *
     * @param list 时间集合（格式必须为 yyyy-MM-dd，且时间降序排列）
     * @return
     * @throws ParseException
     */
    private Integer getSignDay(List<String> list) {
        int continuousDay = 1;
        boolean todaySignIn = false;
        Date todays = DateUtil.parse(list.get(0));
        for (int i = 0; i < list.size(); i++) {
            int intervalDay = distanceDay(todays, DateUtil.parse(list.get(i)));
            //当天签到
            if (intervalDay == 0 && i == 0) {
                todaySignIn = true;
            } else if (intervalDay == continuousDay) {
                continuousDay++;
            } else {
                //不连续，终止判断
                break;
            }
        }
        if (!todaySignIn) {
            continuousDay--;
        }
        return continuousDay;
    }

    //    判断当天日期 与以往签到日期相隔天数
    private int distanceDay(Date largeDay, Date smallDay) {
        int day = (int) ((largeDay.getTime() - smallDay.getTime()) / (1000 * 60 * 60 * 24));
        return day;
    }

}

