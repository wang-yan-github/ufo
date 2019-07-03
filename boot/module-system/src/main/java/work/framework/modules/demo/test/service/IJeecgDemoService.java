package work.framework.modules.demo.test.service;

import work.framework.common.system.base.service.BaseService;
import work.framework.modules.demo.test.entity.JeecgDemo;

/**
 * @Description: jeecg 测试demo
 * @Author: wang-yan
 * @Date:  2018-12-29
 * @Version: V1.0
 */
public interface IJeecgDemoService extends BaseService<JeecgDemo> {
	
	public void testTran();
	
	public JeecgDemo getByIdCacheable(String id);
}
