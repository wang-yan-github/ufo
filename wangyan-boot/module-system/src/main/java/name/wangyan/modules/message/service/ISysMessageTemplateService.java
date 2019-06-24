package name.wangyan.modules.message.service;

import java.util.List;

import name.wangyan.common.system.base.service.BaseService;
import name.wangyan.modules.message.entity.SysMessageTemplate;

/**
 * @Description: 消息模板
 * @Author: wang-yan
 * @Date:  2019-04-09
 * @Version: V1.0
 */
public interface ISysMessageTemplateService extends BaseService<SysMessageTemplate> {
    List<SysMessageTemplate> selectByCode(String code);
}
