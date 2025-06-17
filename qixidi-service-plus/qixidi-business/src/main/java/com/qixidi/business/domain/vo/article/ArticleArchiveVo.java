package com.qixidi.business.domain.vo.article;

import com.qixidi.business.domain.entity.article.ArticleInformation;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * @author zi-wei
 * @create 2025/6/16 17:57
 */
@Data
@NoArgsConstructor
public class ArticleArchiveVo {
    
    /**
     * 创建时间
     */
    private String createTime;

    /**
     * 明细
     */
    private List<ArticleBasicsInfoVo> list = new ArrayList<>();

    public ArticleArchiveVo(String createTime, List<ArticleInformation> informations) {
        this.createTime = createTime;
        for (ArticleInformation item : informations) {
            ArticleBasicsInfoVo vo = new ArticleBasicsInfoVo(item);
            list.add(vo);
        }
    }
}
