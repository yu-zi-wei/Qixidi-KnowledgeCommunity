package com.qixidi.business.api.frontDesk.special;

import com.light.core.core.domain.PageQuery;

import com.qixidi.business.domain.bo.special.SpecialInformationBo;
import com.qixidi.business.domain.vo.article.ArticleInformationVo;
import com.qixidi.business.domain.vo.special.SpecialInformationVo;
import com.qixidi.business.service.special.ISpecialInformationService;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 【前台-白名单】专栏信息管理
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/white")
public class SpecialFrkController {

    private final ISpecialInformationService iSpecialInformationService;

    /**
     * 查询专栏信息列表
     */
    @GetMapping("/special/list")
    public List<SpecialInformationVo> specialListUid(SpecialInformationBo bo) {
        return iSpecialInformationService.specialListUid(bo);
    }

    /**
     * 查询专栏信息列表
     *
     * @return
     */
    @GetMapping("/aut/special/list")
    public List<SpecialInformationVo> specialList() {
        return iSpecialInformationService.specialList();
    }

    /**
     * 查询用户专栏文章
     *
     * @param bo
     * @param pageQuery
     * @return
     */
    @GetMapping("/select/special")
    public List<ArticleInformationVo> selectSpecial(SpecialInformationBo bo, PageQuery pageQuery) {
        return iSpecialInformationService.selectSpecial(bo, pageQuery);
    }

    /**
     * 修改文章专栏
     *
     * @param ids
     * @param id
     * @param uid
     * @return
     */
    @PostMapping("/update/special/{id}/{uid}")
    public void updateSpecial(@RequestBody List<String> ids,
                              @NotNull(message = "专栏id不能为空") @PathVariable("id") Long id,
                              @NotNull(message = "用户id不能为空") @PathVariable("uid") String uid) {
        iSpecialInformationService.updateSpecial(id, uid, ids);
    }

    /**
     * 获取专栏信息详细信息
     */
    @GetMapping("/special/{id}")
    public SpecialInformationVo getInfo(@PathVariable("id") Long id) {
        return iSpecialInformationService.queryById(id);
    }

    /**
     * 删除专栏信息
     */
    @DeleteMapping("/delete/special/{id}")
    public void remove(@PathVariable Long id) throws Exception {
        iSpecialInformationService.remove(id, true);
    }
}
