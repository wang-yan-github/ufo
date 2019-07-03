package work.framework.modules.message.service.impl;

import work.framework.modules.message.service.ISysMessageService;
import work.framework.common.system.base.service.impl.BaseServiceImpl;
import work.framework.modules.message.entity.SysMessage;
import work.framework.modules.message.mapper.SysMessageMapper;
import org.springframework.stereotype.Service;

/**
 * @Description: 消息
 * @Author: wang-yan
 * @Date:  2019-04-09
 * @Version: V1.0
 */
@Service
public class SysMessageServiceImpl extends BaseServiceImpl<SysMessageMapper, SysMessage> implements ISysMessageService {

}
