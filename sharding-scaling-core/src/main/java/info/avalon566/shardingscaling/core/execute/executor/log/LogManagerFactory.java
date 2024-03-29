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

package info.avalon566.shardingscaling.core.execute.executor.log;

import info.avalon566.shardingscaling.core.config.RdbmsConfiguration;
import info.avalon566.shardingscaling.core.spi.ScalingEntry;
import info.avalon566.shardingscaling.core.spi.ScalingEntryLoader;
import lombok.SneakyThrows;

/**
 * Log manager factory.
 *
 * @author avalon566
 */
public class LogManagerFactory {

    /**
     * New instance of log manager.
     *
     * @param rdbmsConfiguration rdbms configuration
     * @return log manager
     */
    @SneakyThrows
    public static LogManager newInstanceLogManager(final RdbmsConfiguration rdbmsConfiguration) {
        return newInstanceLogManager(rdbmsConfiguration.getDataSourceConfiguration().getDatabaseType().getName(), rdbmsConfiguration);
    }

    /**
     * New instance of log manager.
     *
     * @param databaseType database type
     * @param rdbmsConfiguration rdbms configuration
     * @return log manager
     */
    @SneakyThrows
    public static LogManager newInstanceLogManager(final String databaseType, final RdbmsConfiguration rdbmsConfiguration) {
        ScalingEntry scalingEntry = ScalingEntryLoader.getScalingEntryByDatabaseType(databaseType);
        return scalingEntry.getLogManagerClass().getConstructor(RdbmsConfiguration.class).newInstance(rdbmsConfiguration);
    }
}
