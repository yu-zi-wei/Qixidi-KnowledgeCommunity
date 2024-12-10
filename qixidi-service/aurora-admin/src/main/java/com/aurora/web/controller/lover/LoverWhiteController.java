package com.aurora.web.controller.lover;

import com.aurora.common.annotation.Log;
import com.aurora.common.annotation.RepeatSubmit;
import com.aurora.common.core.domain.PageQuery;
import com.aurora.common.core.domain.R;
import com.aurora.common.core.page.TableDataInfo;
import com.aurora.common.core.validate.AddGroup;
import com.aurora.common.enums.BusinessType;
import com.aurora.common.utils.email.MailUtils;
import com.aurora.lover.domain.bo.*;
import com.aurora.lover.domain.vo.*;
import com.aurora.lover.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 【爱情小站-白名单】前台接口
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/white/lover")
public class LoverWhiteController {
    private final IAlbumService iAlbumService;
    private final IBasicInfoService iBasicInfoService;
    private final ILoverRecordService iLoverRecordService;

    private final IRepertoireService iRepertoireService;

    private final ILoverTreeService iLoverTreeService;
    //关于我们
    private final IAboutService iAboutService;
    //轮播图
    private final ILoverCarouselService iLoverCarouselService;
    //祝福语
    private final ILoverCommentService iLoverCommentService;
    //    网站基本配置
    private final ILoverConfigService iLoverConfigService;

    /**
     * 查询网站基本配置
     */
    @GetMapping("/config")
    public LoverConfigVo configInfo() {
        return iLoverConfigService.configInfo();
    }

    /**
     * 发送邮件
     */
    @GetMapping("/mail")
    public void mailCont() {
        MailUtils.sendHtml("2978824265@qq.com", "验证码通知", "Lover发送邮件");
    }

    /**
     * 查询爱情祝福语列表
     */
    @GetMapping("/comment")
    public TableDataInfo<LoverCommentVo> commentApi(LoverCommentBo bo, PageQuery pageQuery) {
        return iLoverCommentService.queryPageList(bo, pageQuery);
    }

    /**
     * 新增爱情祝福
     */
    @Log(title = "爱情祝福语", businessType = BusinessType.INSERT)
    @RepeatSubmit()
    @PostMapping("/insert/comment")
    public R<Void> commentAdd(@Validated(AddGroup.class) @RequestBody LoverCommentBo bo) {
        return iLoverCommentService.insert(bo) > 0 ? R.ok() : R.fail();
    }

    /**
     * 查询爱情树列表
     */
    @GetMapping("/loverTree")
    public LoverTreeVo loverTreeApi() {
        return iLoverTreeService.loverTreeApi();
    }

    /**
     * 查询关于我们列表
     */
    @GetMapping("/about")
    public AboutVo aboutApi() {
        return iAboutService.aboutApi();
    }

    /**
     * 查询爱情轮播图列表
     */
    @GetMapping("/carousel")
    public List<LoverCarouselVo> carouselApi(LoverCarouselBo bo, PageQuery pageQuery) {
        return iLoverCarouselService.carouselApi(bo, pageQuery);
    }


    /**
     * 时光相册列表
     */
    @GetMapping("album")
    public TableDataInfo<AlbumVo> albumApi(AlbumBo bo, PageQuery pageQuery) {
        return iAlbumService.queryPageListApi(bo, pageQuery);
    }


    /**
     * 查询网站基本信息列表
     */
    @GetMapping("/basicInfo")
    public BasicInfoVo basicInfoApi(BasicInfoBo bo) {
        return iBasicInfoService.basicInfoApi(bo);
    }

    /**
     * 查询爱情记录列表
     */
    @GetMapping("/loverRecord")
    public TableDataInfo<LoverRecordVo> loverRecordApi(LoverRecordBo bo, PageQuery pageQuery) {
        return iLoverRecordService.loverRecordApi(bo, pageQuery);
    }

    /**
     * 获取爱情记录详细信息
     *
     * @param id 主键
     */
    @GetMapping("/loverRecord/{id}")
    public R<LoverRecordVo> getInfo(@NotNull(message = "主键不能为空")
                                    @PathVariable Long id) {
        return R.ok(iLoverRecordService.queryById(id));
    }


    /**
     * 查询爱情清单列表
     */
    @GetMapping("/repertoire")
    public TableDataInfo<RepertoireVo> repertoireApi(RepertoireBo bo, PageQuery pageQuery) {
        return iRepertoireService.repertoireApi(bo, pageQuery);
    }
}
