package work.framework.modules.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.gson.Gson;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import work.framework.common.exception.BootException;
import work.framework.modules.system.entity.SysConfig;
import work.framework.modules.system.mapper.SysConfigMapper;
import work.framework.modules.system.service.ISysConfigService;

import java.util.Arrays;

/**
 * @Description: 系统配置
 * @Author: wang-yan
 * @Date:  2019-07-04
 * @Version: V1.0
 */
@Service("sysConfigService")
public class SysConfigServiceImpl extends ServiceImpl<SysConfigMapper, SysConfig> implements ISysConfigService {

    @Override
    public void saveConfig(SysConfig config) {
        this.save(config);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(SysConfig config) {
        this.updateById(config);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateValueByKey(String key, String value) {
        baseMapper.updateValueByKey(key, value);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteBatch(Long[] ids) {
        for (Long id : ids) {
            SysConfig config = this.getById(id);
        }

        this.removeByIds(Arrays.asList(ids));
    }

    @Override
    public String getValue(String key) {
        SysConfig sysConfig = new SysConfig();
        sysConfig.setParamKey(key);
        SysConfig config = this.getOne(new QueryWrapper<>(sysConfig));
        if (config == null) {
            config = baseMapper.queryByKey(key);
        }

        return config == null ? null : config.getParamValue();
    }

    @Override
    public <T> T getConfigObject(String key, Class<T> clazz) {
        String value = getValue(key);
        if (StringUtils.isNotBlank(value)) {
            return new Gson().fromJson(value, clazz);
        }

        try {
            return clazz.newInstance();
        } catch (Exception e) {
            throw new BootException("获取参数失败");
        }
    }
}
