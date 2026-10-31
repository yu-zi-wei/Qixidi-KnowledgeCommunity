package com.qixidi.system.api;

import cn.dev33.satoken.annotation.SaCheckPermission;
import com.light.core.constant.UserConstants;
import com.light.core.core.domain.PageQuery;
import com.light.core.core.page.TableDataInfo;
import com.light.core.enums.BusinessType;
import com.light.excel.utils.ExcelUtil;
import com.light.exception.ServiceException;
import com.qixidi.auth.annotation.Log;
import com.qixidi.auth.annotation.LogModule;

import com.qixidi.system.domain.entity.SysConfig;
import com.qixidi.system.service.ISysConfigService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 参数配置管理
 *
 * @author Lion Li
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/system/config")
@LogModule("参数管理")
public class SysConfigController {

    private final ISysConfigService configService;

    /**
     * 获取参数配置列表
     */
    @SaCheckPermission("config:list")
    @PostMapping("/list")
    public TableDataInfo<SysConfig> list(@RequestBody SysConfig config) {
        return configService.selectPageConfigList(config, config);
    }

    /**
     * 导出参数配置列表
     *
     * @param config
     * @param response
     */
    @Log(title = "导出", businessType = BusinessType.EXPORT)
    @SaCheckPermission("config:export")
    @PostMapping("/export")
    public void export(SysConfig config, HttpServletResponse response) {
        List<SysConfig> list = configService.selectConfigList(config);
        ExcelUtil.exportExcel(list, "参数数据", SysConfig.class, response);
    }

    /**
     * 根据参数编号获取详细信息
     */
    @SaCheckPermission("config:query")
    @GetMapping(value = "/{configId}")
    public SysConfig getInfo(@PathVariable Long configId) {
        return configService.selectConfigById(configId);
    }

    /**
     * 根据参数键名查询参数值
     */
    @GetMapping(value = "/configKey/{configKey}")
    public String getConfigKey(@PathVariable String configKey) {
        return configService.selectConfigByKey(configKey);
    }

    /**
     * 新增参数配置
     */
    @SaCheckPermission("config:add")
    @Log(title = "新增", businessType = BusinessType.INSERT)
    @PostMapping
    public void add(@Validated @RequestBody SysConfig config) {
        if (UserConstants.NOT_UNIQUE.equals(configService.checkConfigKeyUnique(config))) {
            throw new ServiceException("新增参数'" + config.getConfigName() + "'失败，参数键名已存在");
        }
        configService.insertConfig(config);
    }

    /**
     * 修改参数配置
     */
    @SaCheckPermission("config:edit")
    @Log(title = "修改", businessType = BusinessType.UPDATE)
    @PutMapping
    public void edit(@Validated @RequestBody SysConfig config) {
        if (UserConstants.NOT_UNIQUE.equals(configService.checkConfigKeyUnique(config))) {
            throw new ServiceException("修改参数'" + config.getConfigName() + "'失败，参数键名已存在");
        }
        configService.updateConfig(config);
    }

    /**
     * 根据参数键名修改参数配置
     */
    @SaCheckPermission("config:edit")
    @Log(title = "修改", businessType = BusinessType.UPDATE)
    @PutMapping("/updateByKey")
    public void updateByKey(@RequestBody SysConfig config) {
        configService.updateConfig(config);
    }

    /**
     * 删除参数配置
     */
    @SaCheckPermission("config:remove")
    @Log(title = "删除", businessType = BusinessType.DELETE)
    @DeleteMapping("/{configIds}")
    public void remove(@PathVariable Long[] configIds) {
        configService.deleteConfigByIds(configIds);
    }

    /**
     * 刷新参数缓存
     */
    @SaCheckPermission("config:remove")
    @Log(title = "清空", businessType = BusinessType.CLEAN)
    @DeleteMapping("/refreshCache")
    public void refreshCache() {
        configService.resetConfigCache();
    }
}
