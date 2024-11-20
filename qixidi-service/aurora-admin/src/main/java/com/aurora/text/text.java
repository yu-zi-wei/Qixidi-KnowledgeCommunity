package com.aurora.text;

import com.aurora.business.domain.vo.user.UserOrderVo;
import com.aurora.business.mapper.user.UserOrderMapper;
import com.aurora.business.service.impl.article.ArticleInformationServiceImpl;
import com.aurora.system.service.impl.SysUserServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest()
public class text {

    @Autowired
    private ArticleInformationServiceImpl articleInformationService;
    @Autowired
    private SysUserServiceImpl sysUserService;
    @Autowired
    private UserOrderMapper userOrderMapper;

    @Value("${config.redis.host}")
    private String nname;


    @Test
    public void text1() {
        System.out.printf("nname:" + nname);
    }

    @Test
    public void text2() {
        String dd = "1,2,34";
        String[] split = dd.split(",");
        for (String s : split) {
            System.out.println(s);
        }
    }

    @Test
    public void text3() {
        UserOrderVo userOrderVo = userOrderMapper.selectVoById(Long.valueOf("1769026903009792000"));
        System.out.println("userOrderVo:" + userOrderVo);
    }


}
