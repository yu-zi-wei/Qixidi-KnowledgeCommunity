package com.aurora.business.domain.vo.stat;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Data
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
public class StatReturnDataVo implements Serializable {

    /**
     * 名称
     */
    String name;

    /**
     * 二级名称
     */
    String twoName;
    /**
     * 时间
     */
    String time;
    /**
     * 数据
     */
    Long data;

    public StatReturnDataVo(String name, String time, Long data) {
        this.name = name;
        this.time = time;
        this.data = data;
    }
}
