package com.aurora.business.service.impl.tool;

import com.aurora.business.mapper.tool.MysqlToolMapper;
import com.aurora.business.service.tool.MysqlToolService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

@Slf4j
@Service
public class MysqlToolServiceImpl implements MysqlToolService {
    @Autowired
    private MysqlToolMapper mysqlToolMapper;

    /**
     * 数据重置（谨慎操作）
     *
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean emptyData() {
        log.info("数据清除开始...");
        List<String> list = mysqlToolMapper.showTables();
        //不清除的数据表前缀
//        后台基本数据
        Pattern sysPattern = Pattern.compile("sys_");
//        定时任务数据
        Pattern xxlJobPattern = Pattern.compile("xxl_job_");
//        前台基本配置数据
        Pattern toPattern = Pattern.compile("to_");
//        标签数据
        Pattern labelPattern = Pattern.compile("label_");
//        测试数据
        Pattern testPattern = Pattern.compile("test_");
        List<String> yesList = new ArrayList<>();
        list.forEach(item -> {
            if (!sysPattern.matcher(item).find()
                && !xxlJobPattern.matcher(item).find()
                && !toPattern.matcher(item).find()
                && !labelPattern.matcher(item).find()
                && !testPattern.matcher(item).find()
            ) {
                yesList.add(item);
            }
        });
//        清空数据表
        Integer integer = mysqlToolMapper.emptyDataList(yesList);
        log.info("数据清除结束！");
        return integer > 0;
    }
}
