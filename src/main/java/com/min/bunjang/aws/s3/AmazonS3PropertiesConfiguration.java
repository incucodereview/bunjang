package com.min.bunjang.aws.s3;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(value = {AmazonS3CredentialsProperties.class, AmazonS3BucketProperties.class})
public class AmazonS3PropertiesConfiguration {
}
