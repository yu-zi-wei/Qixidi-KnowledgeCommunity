package com.qixidi.system.api;

import cn.dev33.satoken.annotation.SaCheckPermission;
import com.light.core.constant.UserConstants;
import com.light.core.core.domain.PageQuery;
import com.light.core.core.page.TableDataInfo;
import com.light.core.enums.BusinessType;
import com.light.excel.utils.ExcelUtil;
import com.light.exception.ServiceException;
import com.qixidi.auth.annotation.Log;

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
public class SysConfigController {

    private final ISysConfigService configService;

    /**
     * 获取参数配置列表
     */
    @SaCheckPermission("system:config:list")
    @GetMapping("/list")
    public TableDataInfo<SysConfig> list(SysConfig config, PageQuery pageQuery) {
        return configService.selectPageConfigList(config, pageQuery);
    }

    /**
     * 导出参数配置列表
     *
     * @param config
     * @param response
     */
    @Log(title = "参数管理", businessType = BusinessType.EXPORT)
    @SaCheckPermission("system:config:export")
    @PostMapping("/export")
    public void export(SysConfig config, HttpServletResponse response) {
        List<SysConfig> list = configService.selectConfigList(config);
        ExcelUtil.exportExcel(list, "参数数据", SysConfig.class, response);
    }

    /**
     * 根据参数编号获取详细信息
     */
    @SaCheckPermission("system:config:query")
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
    @SaCheckPermission("system:config:add")
    @Log(title = "参数管理", businessType = BusinessType.INSERT)
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
    @SaCheckPermission("system:config:edit")
    @Log(title = "参数管理", businessType = BusinessType.UPDATE)
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
    @SaCheckPermission("system:config:edit")
    @Log(title = "参数管理", businessType = BusinessType.UPDATE)
    @PutMapping("/updateByKey")
    public void updateByKey(@RequestBody SysConfig config) {
        configService.updateConfig(config);
    }

    /**
     * 删除参数配置
     */
    @SaCheckPermission("system:config:remove")
    @Log(title = "参数管理", businessType = BusinessType.DELETE)
    @DeleteMapping("/{configIds}")
    public void remove(@PathVariable Long[] configIds) {
        configService.deleteConfigByIds(configIds);
    }

    /**
     * 刷新参数缓存
     */
    @SaCheckPermission("system:config:remove")
    @Log(title = "参数管理", businessType = BusinessType.CLEAN)
    @DeleteMapping("/refreshCache")
    public void refreshCache() {
        configService.resetConfigCache();
    }
}
