package work.framework.modules.system.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import work.framework.common.api.vo.Result;
import work.framework.common.system.query.QueryGenerator;
import work.framework.modules.system.entity.SysConfig;
import work.framework.modules.system.service.ISysConfigService;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;

/**
 * 系统配置信息
 *
 * @author wang-yan
 */
@RestController
@RequestMapping("/sys/config")
@Slf4j
public class SysConfigController {
    @Autowired
    private ISysConfigService sysConfigService;

    /**
     * 所有配置列表
     */
    @RequestMapping("/list")
    public Result queryPageList(SysConfig sysConfig, @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize, HttpServletRequest req) {
        Result<IPage<SysConfig>> result = new Result<>();
        QueryWrapper<SysConfig> queryWrapper = QueryGenerator.initQueryWrapper(sysConfig, req.getParameterMap());
        Page<SysConfig> page = new Page<>(pageNo, pageSize);
        IPage<SysConfig> pageList = sysConfigService.page(page, queryWrapper);
        result.setSuccess(true);
        result.setResult(pageList);
        return result;
    }


    /**
     * 配置信息
     */
    @RequestMapping("/info/{id}")
    @ResponseBody
    public Result info(@PathVariable("id") Long id) {
        SysConfig config = sysConfigService.getById(id);

        return Result.ok(new HashMap<>().put("config", config));
    }

    /**
     * 保存配置
     */
    @RequestMapping("/save")
    public Result save(@RequestBody SysConfig config) {
        Result<SysConfig> result = new Result<>();

        try {
            sysConfigService.saveConfig(config);
            result.success("添加成功！");
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            result.error500("操作失败");
        }
        return result;
    }

    /**
     * 修改配置
     */
    @RequestMapping("/update")
    public Result update(@RequestBody SysConfig config) {
        Result<SysConfig> result = new Result<>();
        SysConfig sysConfig = sysConfigService.getById(config.getId());
        if (sysConfig == null) {
            result.error500("未找到对应实体");
        } else {
            boolean ok = sysConfigService.updateById(config);
            if (ok) {
                result.success("修改成功!");
            }
        }

        return Result.ok();
    }

    /**
     * 删除配置
     */
    @RequestMapping("/delete")
    public Result delete(@RequestParam(name = "id", required = true) String id) {
        Result<SysConfig> result = new Result<>();
        SysConfig sysConfig = sysConfigService.getById(id);
        if (sysConfig == null) {
            result.error500("未找到对应实体");
        } else {
            boolean ok = sysConfigService.removeById(id);
            if (ok) {
                result.success("删除成功!");
            }
        }

        return result;
    }

}
