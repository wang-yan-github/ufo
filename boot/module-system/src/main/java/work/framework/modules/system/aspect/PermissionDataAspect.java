package work.framework.modules.system.aspect;

import java.lang.reflect.Method;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import work.framework.common.aspect.annotation.PermissionData;
import work.framework.common.system.util.JeecgDataAutorUtils;
import work.framework.common.system.util.JwtUtil;
import work.framework.common.system.vo.SysUserCacheInfo;
import work.framework.common.util.SpringContextUtils;
import work.framework.common.util.oConvertUtils;
import work.framework.modules.system.entity.SysPermission;
import work.framework.modules.system.entity.SysPermissionDataRule;
import work.framework.modules.system.service.ISysPermissionDataRuleService;
import work.framework.modules.system.service.ISysPermissionService;
import work.framework.modules.system.service.ISysUserService;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.PathMatcher;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;

import lombok.extern.slf4j.Slf4j;

/**
  * 数据权限切面处理类
  *  当被请求的方法有注解PermissionData时,会在往当前request中写入数据权限信息
 * @Date 2019年4月10日
 * @Version: 1.0
 */
@Aspect
@Component
@Slf4j
public class PermissionDataAspect {
	
	@Autowired
	private ISysPermissionService sysPermissionService;
	
	@Autowired
	private ISysPermissionDataRuleService sysPermissionDataRuleService;
	
	@Autowired
	private ISysUserService sysUserService;
	
	@Pointcut("@annotation(work.framework.common.aspect.annotation.PermissionData)")
	public void pointCut() {
		
	}
	
	@Around("pointCut()")
	public Object arround(ProceedingJoinPoint point) throws  Throwable{
		HttpServletRequest request = SpringContextUtils.getHttpServletRequest();
		MethodSignature signature = (MethodSignature) point.getSignature();
		Method method = signature.getMethod();
		PermissionData pd = method.getAnnotation(PermissionData.class);
		String component = pd.pageComponent();
		SysPermission currentSyspermission=null;
		if(oConvertUtils.isNotEmpty(component)) {
			//1.通过注解属性pageComponent 获取菜单
			LambdaQueryWrapper<SysPermission> query = new LambdaQueryWrapper<SysPermission>();
			query.eq(SysPermission::getDelFlag,0);
			query.eq(SysPermission::getComponent, component);
			currentSyspermission = sysPermissionService.getOne(query);
		}else {
			String requestMethod = request.getMethod();
			String requestPath = request.getRequestURI().substring(request.getContextPath().length());
			requestPath = filterUrl(requestPath);
			log.info("拦截请求>>"+requestPath+";请求类型>>"+requestMethod);
			//1.直接通过前端请求地址查询菜单
			LambdaQueryWrapper<SysPermission> query = new LambdaQueryWrapper<SysPermission>();
			query.eq(SysPermission::getMenuType,2);
			query.eq(SysPermission::getDelFlag,0);
			query.eq(SysPermission::getUrl, requestPath);
			currentSyspermission = sysPermissionService.getOne(query);
			//2.未找到 再通过正则匹配获取菜单
			if(currentSyspermission==null) {
				String regUrl = getRegexpUrl(requestPath);
				if(regUrl!=null) {
					currentSyspermission = sysPermissionService.getOne(new LambdaQueryWrapper<SysPermission>().eq(SysPermission::getMenuType,2).eq(SysPermission::getUrl, regUrl).eq(SysPermission::getDelFlag,0));
				}
			}
		}
		//3.通过用户名+菜单ID 找到权限配置信息 放到request中去
		if(currentSyspermission!=null) {
			String username = JwtUtil.getUserNameByToken(request);
			List<SysPermissionDataRule> dataRules = sysPermissionDataRuleService.queryPermissionDataRules(username, currentSyspermission.getId());
			if(dataRules!=null && dataRules.size()>0) {
				JeecgDataAutorUtils.installDataSearchConditon(request, dataRules);
				
				//TODO 此处将用户信息查找出来放到request中实属无奈  可以优化
				SysUserCacheInfo userinfo = sysUserService.getCacheUser(username);
				JeecgDataAutorUtils.installUserInfo(request, userinfo);
			}
		}
		
		return  point.proceed();
	}
	
	private String filterUrl(String requestPath){
		String url = "";
		if(oConvertUtils.isNotEmpty(requestPath)){
			url = requestPath.replace("\\", "/");
			url = requestPath.replace("//", "/");
			if(url.indexOf("//")>=0){
				url = filterUrl(url);
			}
			/*if(url.startsWith("/")){
				url=url.substring(1);
			}*/
		}
		return url;
	}
	
	/**
	 * 获取请求地址
	 * @param request
	 * @return
	 */
	private String getJgAuthRequsetPath(HttpServletRequest request) {
		String queryString = request.getQueryString();
		String requestPath = request.getRequestURI();
		if(oConvertUtils.isNotEmpty(queryString)){
			requestPath += "?" + queryString;
		}
		if (requestPath.indexOf("&") > -1) {// 去掉其他参数(保留一个参数) 例如：loginController.do?login
			requestPath = requestPath.substring(0, requestPath.indexOf("&"));
		}
		if(requestPath.indexOf("=")!=-1){
			if(requestPath.indexOf(".do")!=-1){
				requestPath = requestPath.substring(0,requestPath.indexOf(".do")+3);
			}else{
				requestPath = requestPath.substring(0,requestPath.indexOf("?"));
			}
		}
		requestPath = requestPath.substring(request.getContextPath().length() + 1);// 去掉项目路径
		return filterUrl(requestPath);
	}
	
	private boolean moHuContain(List<String> list,String key){
		for(String str : list){
			if(key.contains(str)){
				return true;
			}
		}
		return false;
	}
	
	/**
	 * 匹配前端传过来的地址 匹配成功返回正则地址
	 * AntPathMatcher匹配地址
	 *()* 匹配0个或多个字符
	 *()**匹配0个或多个目录
	 */
	private String getRegexpUrl(String url) {
		List<String> list = sysPermissionService.queryPermissionUrlWithStar();
		if(list!=null && list.size()>0) {
			for (String p : list) {
				PathMatcher matcher = new AntPathMatcher();
				if(matcher.match(p, url)) {
					return p;
				}
			}
		}
		return null;
	}
	
}
