package ru.kpfu.itis.fqw.idrisov.daniyar.recommendation.elements.configs.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@ConfigurationProperties(prefix = "minio")
@Component
public class MinioProperties {

    private String url;
    private String accessKey;
    private String secretKey;
    private String bucket;
}
