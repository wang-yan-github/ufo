package work.framework.modules.system.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import work.framework.modules.system.entity.SysConfig;

/**
 * 系统配置信息
 *
 * @author wang-yan
 */
@Mapper
public interface SysConfigMapper extends BaseMapper<SysConfig> {

    /**
     * 根据key，查询value
     */
    SysConfig queryByKey(String paramKey);

    /**
     * 根据key，更新value
     */
    int updateValueByKey(@Param("paramKey") String paramKey, @Param("paramValue") String paramValue);

}
