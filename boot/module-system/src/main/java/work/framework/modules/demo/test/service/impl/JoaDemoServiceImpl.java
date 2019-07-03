package work.framework.modules.demo.test.service.impl;

import work.framework.modules.demo.test.entity.JoaDemo;
import work.framework.modules.demo.test.mapper.JoaDemoMapper;
import work.framework.modules.demo.test.service.IJoaDemoService;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

/**
 * @Description: 流程测试
 * @Author: wang-yan
 * @Date:   2019-05-14
 * @Version: V1.0
 */
@Service
public class JoaDemoServiceImpl extends ServiceImpl<JoaDemoMapper, JoaDemo> implements IJoaDemoService {

}
