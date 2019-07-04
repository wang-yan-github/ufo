package work.framework.modules.oss.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import work.framework.common.api.vo.Result;
import work.framework.common.constant.ConfigConstant;
import work.framework.common.exception.BootException;
import work.framework.common.system.query.QueryGenerator;
import work.framework.modules.oss.cloud.CloudStorageConfig;
import work.framework.modules.oss.cloud.OSSFactory;
import work.framework.modules.oss.entity.SysOss;
import work.framework.modules.oss.service.ISysOssService;
import work.framework.modules.oss.validator.ValidatorUtils;
import work.framework.modules.oss.validator.group.AliyunGroup;
import work.framework.modules.oss.validator.group.QcloudGroup;
import work.framework.modules.oss.validator.group.QiniuGroup;
import work.framework.modules.system.service.ISysConfigService;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;

/**
 * 文件上传
 *
 * @author wang-yan
 */
@RestController
@RequestMapping("sys/oss")
public class SysOssController {
    @Autowired
    private ISysOssService sysOssService;
    @Autowired
    private ISysConfigService sysConfigService;

    private final static String KEY = ConfigConstant.CLOUD_STORAGE_CONFIG_KEY;

    /**
     * 列表
     */
    @RequestMapping("/list")
    public Result queryPageList(SysOss sysOss,
                                @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                HttpServletRequest req) {
        Result<IPage<SysOss>> result = new Result<>();
        QueryWrapper<SysOss> queryWrapper = QueryGenerator.initQueryWrapper(sysOss, req.getParameterMap());
        Page<SysOss> page = new Page<>(pageNo, pageSize);
        IPage<SysOss> pageList = sysOssService.page(page, queryWrapper);

        result.setSuccess(true);
        result.setResult(pageList);
        return result;
    }


    /**
     * 云存储配置信息
     */
    @RequestMapping("/config")
    public Result config() {
        CloudStorageConfig config = sysConfigService.getConfigObject(KEY, CloudStorageConfig.class);

        return Result.ok(new HashMap<>().put("config", config));
    }


    /**
     * 保存云存储配置信息
     */
    @RequestMapping("/saveConfig")
    public Result saveConfig(@RequestBody CloudStorageConfig config) {
        //校验类型
        ValidatorUtils.validateEntity(config);

        if (config.getType() == ConfigConstant.CloudService.QINIU.getValue()) {
            //校验七牛数据
            ValidatorUtils.validateEntity(config, QiniuGroup.class);
        } else if (config.getType() == ConfigConstant.CloudService.ALIYUN.getValue()) {
            //校验阿里云数据
            ValidatorUtils.validateEntity(config, AliyunGroup.class);
        } else if (config.getType() == ConfigConstant.CloudService.QCLOUD.getValue()) {
            //校验腾讯云数据
            ValidatorUtils.validateEntity(config, QcloudGroup.class);
        }

        sysConfigService.updateValueByKey(KEY, new Gson().toJson(config));

        return Result.ok();
    }


    /**
     * 上传文件
     */
    @RequestMapping("/upload")
    public Result upload(@RequestParam("file") MultipartFile file) throws Exception {
        if (file.isEmpty()) {
            throw new BootException("上传文件不能为空");
        }

        //上传文件
        String suffix = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf("."));
        String url = OSSFactory.build().uploadSuffix(file.getBytes(), suffix);

        //保存文件信息
        SysOss ossEntity = new SysOss();
        ossEntity.setUrl(url);
        ossEntity.setCreateDate(new Date());
        sysOssService.save(ossEntity);

        return Result.ok(new HashMap<>().put("url", url));
    }


    /**
     * 删除
     */
    @RequestMapping("/delete")
    public Result delete(@RequestBody Long[] ids) {
        sysOssService.removeByIds(Arrays.asList(ids));

        return Result.ok();
    }

}
