package com.aurora.text;

import com.aurora.business.domain.bo.article.ArticleInformationBo;
import com.aurora.business.service.impl.article.ArticleInformationServiceImpl;
import com.aurora.common.core.domain.PageQuery;
import com.aurora.common.core.domain.entity.SysUser;
import com.aurora.common.core.page.TableDataInfo;
import com.aurora.system.service.impl.SysUserServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class text {

    @Autowired
    ArticleInformationServiceImpl articleInformationService;
    @Autowired
    SysUserServiceImpl sysUserService;

    @Test
    public void text1() {
        PageQuery pageQuery = new PageQuery();
        pageQuery.setPageNum(2);
        pageQuery.setPageSize(3);
        articleInformationService.index(new ArticleInformationBo(), pageQuery);
    }

    @Test
    public void text2() {
        PageQuery pageQuery = new PageQuery();
        pageQuery.setPageNum(2);
        pageQuery.setPageSize(3);
        TableDataInfo<SysUser> sysUserTableDataInfo = sysUserService.selectAllocatedList(new SysUser(), pageQuery);
        sysUserTableDataInfo.getRows().forEach(s -> System.out.println(s));
    }
    @Test
    public void text3() {

    }

}
