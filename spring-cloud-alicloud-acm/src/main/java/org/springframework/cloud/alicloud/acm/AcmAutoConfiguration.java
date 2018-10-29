/*
 * Copyright (C) 2018 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.springframework.cloud.alicloud.acm;

import com.taobao.diamond.client.Diamond;
import org.springframework.beans.BeansException;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.alicloud.acm.endpoint.AcmHealthIndicator;
import org.springframework.cloud.alicloud.acm.refresh.AcmContextRefresher;
import org.springframework.cloud.alicloud.acm.refresh.AcmRefreshHistory;
import org.springframework.cloud.alicloud.context.acm.AcmProperties;
import org.springframework.cloud.context.refresh.ContextRefresher;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created on 01/10/2017.
 *
 * @author juven.xuxb
 */
@Configuration
@ConditionalOnClass({Diamond.class})
@EnableConfigurationProperties(AcmProperties.class)
public class AcmAutoConfiguration implements ApplicationContextAware {

    private ApplicationContext applicationContext;

    @Bean
    public AcmPropertySourceRepository acmPropertySourceRepository() {
        return new AcmPropertySourceRepository(applicationContext);
    }

    @Bean
    public AcmHealthIndicator acmHealthIndicator(AcmProperties acmProperties,
                                                 AcmPropertySourceRepository acmPropertySourceRepository) {
        return new AcmHealthIndicator(acmProperties, acmPropertySourceRepository);
    }

    @Bean
    public AcmRefreshHistory acmRefreshHistory() {
        return new AcmRefreshHistory();
    }

    @Bean
    public AcmContextRefresher acmContextRefresher(AcmProperties acmProperties, ContextRefresher contextRefresher,
                                                   AcmRefreshHistory refreshHistory,
                                                   AcmPropertySourceRepository propertySourceRepository) {
        return new AcmContextRefresher(contextRefresher, acmProperties, refreshHistory, propertySourceRepository);
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext)
        throws BeansException {
        this.applicationContext = applicationContext;
    }
}
