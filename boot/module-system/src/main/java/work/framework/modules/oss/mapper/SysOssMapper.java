/**
 * Copyright (c) 2016-2019 人人开源 All rights reserved.
 *
 * https://www.renren.io
 *
 * 版权所有，侵权必究！
 */

package work.framework.modules.oss.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import work.framework.modules.oss.entity.SysOss;

/**
 * 文件上传
 *
 * @author Mark wang-yan
 */
@Mapper
public interface SysOssMapper extends BaseMapper<SysOss> {
	
}
