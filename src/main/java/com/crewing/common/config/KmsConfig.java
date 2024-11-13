package com.crewing.common.config;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.kms.AWSKMS;
import com.amazonaws.services.kms.AWSKMSClientBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@RequiredArgsConstructor
@Configuration
public class KmsConfig {
    @Value("${cloud.aws.region.static}")
    private String AWS_REGION;
    @Value("${cloud.aws.credentials.accessKey}")
    private String AWS_CREDENTIALS_ACCESS_KEY;
    @Value("${cloud.aws.credentials.secretKey}")
    private String AWS_CREDENTIALS_SECRET_KEY;

    @Bean
    public AWSKMS amazonKMS() {
        BasicAWSCredentials awsCredit = new BasicAWSCredentials(AWS_CREDENTIALS_ACCESS_KEY, AWS_CREDENTIALS_SECRET_KEY);
        return AWSKMSClientBuilder.standard()
                .withRegion(AWS_REGION)
                .withCredentials(new AWSStaticCredentialsProvider(awsCredit))
                .build();
    }
}
