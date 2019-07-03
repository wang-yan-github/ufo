package work.framework.modules.system.mapper;

import java.util.Date;

import org.apache.ibatis.annotations.Param;
import work.framework.modules.system.entity.SysLog;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * <p>
 * 系统日志表 Mapper 接口
 * </p>
 *
 * @Author wang-yan
 * @since 2018-12-26
 */
public interface SysLogMapper extends BaseMapper<SysLog> {

	/**
	 * @功能：清空所有日志记录
	 */
	public void removeAll();

	/**
	 * 获取系统总访问次数
	 *
	 * @return Long
	 */
	Long findTotalVisitCount();

	//update-begin--Author:wang-yan  Date:20190428 for：传入开始时间，结束时间参数
	/**
	 * 获取系统今日访问次数
	 *
	 * @return Long
	 */
	Long findTodayVisitCount(@Param("dayStart") Date dayStart, @Param("dayEnd") Date dayEnd);

	/**
	 * 获取系统今日访问 IP数
	 *
	 * @return Long
	 */
	Long findTodayIp(@Param("dayStart") Date dayStart, @Param("dayEnd") Date dayEnd);
	//update-end--Author:wang-yan  Date:20190428 for：传入开始时间，结束时间参数
}
