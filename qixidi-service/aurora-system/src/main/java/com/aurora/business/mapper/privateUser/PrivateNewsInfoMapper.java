package com.aurora.business.mapper.privateUser;

import com.aurora.business.domain.bo.privateUser.PrivateNewsInfoBo;
import com.aurora.business.domain.entity.news.NewsUserRecord;
import com.aurora.business.domain.entity.privateUser.PrivateNewsInfo;
import com.aurora.business.domain.vo.privateUser.PrivateNewsInfoVo;
import com.aurora.business.domain.vo.privateUser.PrivateUserVo;
import com.aurora.common.core.mapper.BaseMapperPlus;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 私信记录Mapper接口
 *
 * @author aurora
 * @date 2023-03-23
 */
public interface PrivateNewsInfoMapper extends BaseMapperPlus<PrivateNewsInfoMapper, PrivateNewsInfo, PrivateNewsInfoVo> {

    Page<PrivateNewsInfoVo> selectListXml(@Param("bo") PrivateNewsInfoBo bo, Page<Object> build);

    List<NewsUserRecord> selectRead(@Param("uid") String uid);

    PrivateNewsInfoVo selectLast(@Param("uuid") String uuid, @Param("replyTargetUid") String replyTargetUid);

    List<PrivateUserVo> selectBeenReadList(@Param("uid") String uid, @Param("list") List<PrivateUserVo> records);
}

