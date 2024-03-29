/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package info.avalon566.shardingscaling.core.config;

import lombok.Data;
import lombok.SneakyThrows;

/**
 * Relational database management system configuration.
 *
 * @author avalon566
 */
@Data
public class RdbmsConfiguration implements Cloneable {

    private DataSourceConfiguration dataSourceConfiguration;

    private String tableName;

    private String whereCondition;
    
    /**
     * Clone to new rdbms configuration.
     *
     * @param origin origin rdbms configuration
     * @return new rdbms configuration
     */
    @SneakyThrows
    public static RdbmsConfiguration clone(final RdbmsConfiguration origin) {
        return (RdbmsConfiguration) origin.clone();
    }
    
    /**
     * Get where condition.
     *
     * @return "" if whereCondition is null, otherwise whereCondition
     */
    public String getWhereCondition() {
        return null == whereCondition ? "" : whereCondition;
    }
}
