package work.framework.modules.oss.cloud;


import work.framework.common.constant.ConfigConstant;
import work.framework.common.util.SpringContextUtils;
import work.framework.modules.system.service.ISysConfigService;

/**
 * 文件上传Factory
 *
 * @author wang-yan
 */
public final class OSSFactory {
    private static ISysConfigService sysConfigService;

    static {
        OSSFactory.sysConfigService = (ISysConfigService) SpringContextUtils.getBean("sysConfigService");
    }

    public static CloudStorageService build() {
        //获取云存储配置信息
        CloudStorageConfig config = sysConfigService.getConfigObject(ConfigConstant.CLOUD_STORAGE_CONFIG_KEY, CloudStorageConfig.class);

        if (config.getType() == ConfigConstant.CloudService.QINIU.getValue()) {
            return new QiniuCloudStorageService(config);
        } else if (config.getType() == ConfigConstant.CloudService.ALIYUN.getValue()) {
            return new AliyunCloudStorageService(config);
        } else if (config.getType() == ConfigConstant.CloudService.QCLOUD.getValue()) {
            return new QcloudCloudStorageService(config);
        }

        return null;
    }

}
