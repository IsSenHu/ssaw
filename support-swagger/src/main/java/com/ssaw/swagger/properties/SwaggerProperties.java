package com.ssaw.swagger.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author HuSen
 * @date 2019/2/22 16:30
 */
@Getter
@Setter
@ConfigurationProperties(prefix = "ssaw.swagger")
public class SwaggerProperties {

    /** 扫描指定包下的swagger注解 */
    private String basePackages = "";

    /** 组名 */
    private String groupName = "default";

    /** 标题 */
    private String title = "";

    /** 描述 */
    private String description = "";

    /** 团队组织的服务地址 */
    private String termsOfServiceUrl = "";

    /** 版本 */
    private String version = "1.0.0";

    /** 版权 */
    private String license = "";

    /** 版权地址 */
    private String licenseUrl = "";

    /** 你的大名 */
    private String name = "";

    /** 你的主页 */
    private String url = "";

    /** 你的邮箱 */
    private String email = "";
}