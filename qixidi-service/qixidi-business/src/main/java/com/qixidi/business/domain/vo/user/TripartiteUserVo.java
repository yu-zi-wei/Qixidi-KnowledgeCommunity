package com.qixidi.business.domain.vo.user;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.light.excel.convert.ExcelDictConvert;
import com.qixidi.business.domain.entity.user.UserInformation;
import lombok.Data;

import java.util.Date;
import com.qixidi.auth.domain.entity.TripartiteUser;


/**
 * 平台用户视图对象 tripartite_user
 *
 * @author ziwei
 * @date 2022-06-12
 */
@Data
@ExcelIgnoreUnannotated
public class TripartiteUserVo extends TripartiteUser {

    /**
     * 是否关注
     */
    private Boolean isFollow = false;

    /**
     * 个人简介
     */
    private String introduce;

    /**
     * 个人主页地址
     */
    private String homepage;
    /**
     * 用户经验值
     */
    private Integer empirical;

    /**
     * 用户等级
     */
    private String grade;

    /**
     * 用户状态（0：正常，1：已删除，2：冻结）
     */
    private Integer userState;

    // 统计字段
    private int fabulousCount;
    private int fansFabulousCount;
    private int collectionCount;
    private int followCount;
    private int fansFollowCount;
    private int commentCount;
    private int fansCommentCount;
    private int articleCount;
    private int specialColumnCount;
    private int fansSpecialColumn;
    private int circleCount;
    private int albumCount;
    private int dictumCount;
    private int timeNotesCount;

    public void fillUserInformation(UserInformation userInformation) {
        this.introduce = userInformation.getIntroduce();
        this.homepage = userInformation.getHomepage();
        this.empirical = userInformation.getEmpirical().intValue();
        this.grade = userInformation.getGrade();
        this.userState = userInformation.getUserState();
    }
}
