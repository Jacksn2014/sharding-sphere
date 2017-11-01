/*
 * Copyright 1999-2015 dangdang.com.
 * <p>
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
 * </p>
 */

package io.shardingjdbc.orchestration.spring.datasource;

import io.shardingjdbc.core.api.config.MasterSlaveRuleConfiguration;
import io.shardingjdbc.core.jdbc.core.datasource.MasterSlaveDataSource;
import io.shardingjdbc.orchestration.api.config.OrchestrationMasterSlaveConfiguration;
import io.shardingjdbc.orchestration.internal.config.ConfigurationService;
import io.shardingjdbc.orchestration.internal.state.InstanceStateService;
import io.shardingjdbc.orchestration.reg.base.CoordinatorRegistryCenter;
import lombok.Setter;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.Map;

/**
 * Orchestration master-slave datasource for spring namespace.
 *
 * @author caohao
 */
public class OrchestrationSpringMasterSlaveDataSource extends MasterSlaveDataSource implements ApplicationContextAware {
    
    private final ConfigurationService configurationService;
    
    private final InstanceStateService instanceStateService;
    
    private final OrchestrationMasterSlaveConfiguration config;
    
    @Setter
    private ApplicationContext applicationContext;
    
    public OrchestrationSpringMasterSlaveDataSource(final String name, final boolean overwrite, final CoordinatorRegistryCenter registryCenter, 
                                                    final Map<String, DataSource> dataSourceMap, final MasterSlaveRuleConfiguration config) throws SQLException {
        super(config.build(dataSourceMap));
        this.config = new OrchestrationMasterSlaveConfiguration(name, overwrite, registryCenter, dataSourceMap, config);
        configurationService = new ConfigurationService(name, registryCenter);
        instanceStateService = new InstanceStateService(name, registryCenter);
    }
    
    /**
     * initial orchestration spring master-slave data source.
     */
    public void init() {
        configurationService.addMasterSlaveConfiguration(config, this);
        instanceStateService.addMasterSlaveState(this);
    }
    
//    @Override
//    public void renew(final MasterSlaveRule masterSlaveRule) throws SQLException {
//        DataSourceBeanUtil.createDataSourceBean(applicationContext, masterSlaveRule.getMasterDataSourceName(), masterSlaveRule.getMasterDataSource());
//        for (Entry<String, DataSource> entry : masterSlaveRule.getSlaveDataSourceMap().entrySet()) {
//            DataSourceBeanUtil.createDataSourceBean(applicationContext, entry.getKey(), entry.getValue());
//        }
//        super.renew(masterSlaveRule);
//    }
}
