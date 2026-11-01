package com.qixidi.business.mapper.privateUser;

import com.qixidi.business.domain.bo.privateUser.PrivateNewsInfoBo;
import com.qixidi.business.domain.entity.news.NewsUserRecord;
import com.qixidi.business.domain.entity.privateUser.PrivateNewsInfo;
import com.qixidi.business.domain.vo.privateUser.PrivateNewsInfoVo;
import com.qixidi.business.domain.vo.privateUser.PrivateUserVo;
import com.light.mybatisPlus.mapper.BaseMapperPlus;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 私信记录Mapper接口
 *
 * @author aurora
 * @date 2023-03-23
 */
@Mapper
public interface PrivateNewsInfoMapper extends BaseMapperPlus<PrivateNewsInfoMapper, PrivateNewsInfo, PrivateNewsInfoVo> {

    Page<PrivateNewsInfoVo> selectListXml(@Param("bo") PrivateNewsInfoBo bo, Page<Object> build);

    /**
     * 后台全站私信消息分页（双方昵称/头像由 Service 组装；senderUids/recipientUids 为按昵称查出的用户集合，null 表示无该条件）
     */
    Page<PrivateNewsInfoVo> selectAdminList(@Param("bo") PrivateNewsInfoBo bo,
                                            @Param("senderUids") List<String> senderUids,
                                            @Param("recipientUids") List<String> recipientUids,
                                            Page<Object> build);

    List<NewsUserRecord> selectRead(@Param("uid") String uid);

    PrivateNewsInfoVo selectLast(@Param("uuid") String uuid, @Param("replyTargetUid") String replyTargetUid);

    List<PrivateUserVo> selectBeenReadList(@Param("uid") String uid, @Param("list") List<PrivateUserVo> records);
}

