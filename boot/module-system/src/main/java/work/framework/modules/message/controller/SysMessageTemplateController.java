package work.framework.modules.message.controller;

import java.util.Arrays;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import work.framework.modules.message.service.ISysMessageTemplateService;
import org.apache.poi.ss.formula.functions.T;
import work.framework.common.api.vo.Result;
import work.framework.common.system.base.controller.BaseController;
import work.framework.common.system.query.QueryGenerator;
import work.framework.modules.message.entity.MsgParams;
import work.framework.modules.message.entity.SysMessageTemplate;
import work.framework.modules.message.util.PushMsgUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import lombok.extern.slf4j.Slf4j;

/**
 * @Description: 消息模板
 * @Author: wang-yan
 * @Sate: 2019-04-09
 * @Version: V1.0
 */
@Slf4j
@RestController
@RequestMapping("/message/sysMessageTemplate")
public class SysMessageTemplateController extends BaseController<SysMessageTemplate, ISysMessageTemplateService> {
	@Autowired
	private ISysMessageTemplateService sysMessageTemplateService;
	@Autowired
	private PushMsgUtil pushMsgUtil;

	/**
	 * 分页列表查询
	 * 
	 * @param sysMessageTemplate
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@GetMapping(value = "/list")
	public Result<IPage<SysMessageTemplate>> queryPageList(SysMessageTemplate sysMessageTemplate, @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                                           @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize, HttpServletRequest req) {
		Result<IPage<SysMessageTemplate>> result = new Result<IPage<SysMessageTemplate>>();
		QueryWrapper<SysMessageTemplate> queryWrapper = QueryGenerator.initQueryWrapper(sysMessageTemplate, req.getParameterMap());
		Page<SysMessageTemplate> page = new Page<SysMessageTemplate>(pageNo, pageSize);
		IPage<SysMessageTemplate> pageList = sysMessageTemplateService.page(page, queryWrapper);
		result.setSuccess(true);
		result.setResult(pageList);
		return result;
	}

	/**
	 * 添加
	 * 
	 * @param sysMessageTemplate
	 * @return
	 */
	@PostMapping(value = "/add")
	public Result<T> add(@RequestBody SysMessageTemplate sysMessageTemplate) {
		Result<T> result = new Result<T>();
		try {
			sysMessageTemplateService.save(sysMessageTemplate);
			result.success("添加成功！");
		} catch (Exception e) {
			log.info(e.getMessage(), e);
			result.error500("操作失败");
		}
		return result;
	}

	/**
	 * 编辑
	 * 
	 * @param sysMessageTemplate
	 * @return
	 */
	@PutMapping(value = "/edit")
	public Result<T> edit(@RequestBody SysMessageTemplate sysMessageTemplate) {
		Result<T> result = new Result<T>();
		SysMessageTemplate sysMessageTemplateEntity = sysMessageTemplateService.getById(sysMessageTemplate.getId());
		if (sysMessageTemplateEntity == null) {
			result.error500("未找到对应实体");
		} else {
			boolean ok = sysMessageTemplateService.updateById(sysMessageTemplate);
			if (ok) {
				result.success("修改成功!");
			} else {
				result.error500("修改失败!");
			}
		}

		return result;
	}

	/**
	 * 通过id删除
	 * 
	 * @param id
	 * @return
	 */
	@DeleteMapping(value = "/delete")
	public Result<T> delete(@RequestParam(name = "id", required = true) String id) {
		Result<T> result = new Result<T>();
		SysMessageTemplate sysMessageTemplate = sysMessageTemplateService.getById(id);
		if (sysMessageTemplate == null) {
			result.error500("未找到对应实体");
		} else {
			boolean ok = sysMessageTemplateService.removeById(id);
			if (ok) {
				result.success("删除成功!");
			}
		}

		return result;
	}

	/**
	 * 批量删除
	 * 
	 * @param ids
	 * @return
	 */
	@DeleteMapping(value = "/deleteBatch")
	public Result<T> deleteBatch(@RequestParam(name = "ids", required = true) String ids) {
		Result<T> result = new Result<T>();
		if (ids == null || "".equals(ids.trim())) {
			result.error500("ids参数不允许为空！");
		} else {
			this.sysMessageTemplateService.removeByIds(Arrays.asList(ids.split(",")));
			result.success("删除成功!");
		}
		return result;
	}

	/**
	 * 通过id查询
	 * 
	 * @param id
	 * @return
	 */
	@GetMapping(value = "/queryById")
	public Result<SysMessageTemplate> queryById(@RequestParam(name = "id", required = true) String id) {
		Result<SysMessageTemplate> result = new Result<SysMessageTemplate>();
		SysMessageTemplate sysMessageTemplate = sysMessageTemplateService.getById(id);
		if (sysMessageTemplate == null) {
			result.error500("未找到对应实体");
		} else {
			result.setResult(sysMessageTemplate);
			result.setSuccess(true);
		}
		return result;
	}

	/**
	 * 导出excel
	 *
	 * @param request
	 * @param response
	 */
	@GetMapping(value = "/exportXls")
	public ModelAndView exportXls(HttpServletRequest request,SysMessageTemplate sysMessageTemplate) {
		return super.exportXls(request, sysMessageTemplate, SysMessageTemplate.class,"推送消息模板");
	}

	/**
	 * excel导入
	 *
	 * @param request
	 * @param response
	 * @return
	 */
	@PostMapping(value = "/importExcel")
	public Result<?> importExcel(HttpServletRequest request, HttpServletResponse response) {
		return super.importExcel(request, response, SysMessageTemplate.class);
	}

	/**
	 * 发送消息
	 */
	@PostMapping(value = "/sendMsg")
	public Result<SysMessageTemplate> sendMessage(@RequestBody MsgParams msgParams) {
		Result<SysMessageTemplate> result = new Result<SysMessageTemplate>();
		Map<String, String> map = null;
		try {
			map = (Map<String, String>) JSON.parse(msgParams.getTestData());
		} catch (Exception e) {
			result.error500("解析Json出错！");
			return result;
		}
		boolean is_sendSuccess = pushMsgUtil.sendMessage(msgParams.getMsgType(), msgParams.getTemplateCode(), map, msgParams.getReceiver());
		if (is_sendSuccess)
			result.success("发送消息任务添加成功！");
		else
			result.error500("发送消息任务添加失败！");
		return result;
	}
}
