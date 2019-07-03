package work.framework.modules.message.service;

import java.util.List;

import work.framework.common.system.base.service.BaseService;
import work.framework.modules.message.entity.SysMessageTemplate;

/**
 * @Description: 消息模板
 * @Author: wang-yan
 * @Date:  2019-04-09
 * @Version: V1.0
 */
public interface ISysMessageTemplateService extends BaseService<SysMessageTemplate> {
    List<SysMessageTemplate> selectByCode(String code);
}
