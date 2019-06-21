package name.wangyan.common.system.base.service.impl;

import name.wangyan.common.system.base.entity.BaseEntity;
import name.wangyan.common.system.base.service.BaseService;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;

/**
 * @Description: ServiceImpl基类
 * @Author: wangYanJava@Gmail.com
 * @Date: 2019-4-21 8:13
 * @Version: 1.0
 */
@Slf4j
public class BaseServiceImpl<M extends BaseMapper<T>, T extends BaseEntity> extends ServiceImpl<M, T> implements BaseService<T> {

}
