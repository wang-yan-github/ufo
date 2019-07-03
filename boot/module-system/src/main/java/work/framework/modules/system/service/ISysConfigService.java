package work.framework.modules.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import work.framework.modules.system.entity.SysConfig;

/**
 * 系统配置信息
 *
 * @author wang-yan
 */
public interface ISysConfigService extends IService<SysConfig> {

    /**
     * 保存配置信息
     */
    void saveConfig(SysConfig config);

    /**
     * 更新配置信息
     */
    void update(SysConfig config);

    /**
     * 根据key，更新value
     */
    void updateValueByKey(String key, String value);

    /**
     * 删除配置信息
     */
    void deleteBatch(Long[] ids);

    /**
     * 根据key，获取配置的value值
     *
     * @param key key
     */
    String getValue(String key);

    /**
     * 根据key，获取value的Object对象
     *
     * @param key   key
     * @param clazz Object对象
     */
    <T> T getConfigObject(String key, Class<T> clazz);

}
