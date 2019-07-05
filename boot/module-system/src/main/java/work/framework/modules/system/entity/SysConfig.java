package work.framework.modules.system.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * 系统配置信息
 *
 * @author wang-yan
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class SysConfig implements Serializable {
    private static final long serialVersionUID = 1L;

    @TableId
    private Long id;
    @NotBlank(message = "参数名不能为空")
    private String paramKey;
    @NotBlank(message = "参数值不能为空")
    private String paramValue;
    private String remark;

}
