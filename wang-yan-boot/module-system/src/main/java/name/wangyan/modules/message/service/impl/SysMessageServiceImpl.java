package name.wangyan.modules.message.service.impl;

import name.wangyan.modules.message.service.ISysMessageService;
import name.wangyan.common.system.base.service.impl.BaseServiceImpl;
import name.wangyan.modules.message.entity.SysMessage;
import name.wangyan.modules.message.mapper.SysMessageMapper;
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
