package name.wangyan.modules.demo.test.service;

import name.wangyan.common.system.base.service.BaseService;
import name.wangyan.modules.demo.test.entity.JeecgDemo;

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
