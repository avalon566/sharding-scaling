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

package info.avalon556.shardingscaling.postgresql;

import info.avalon566.shardingscaling.core.spi.ScalingEntry;
import info.avalon566.shardingscaling.core.sync.reader.JdbcReader;
import info.avalon566.shardingscaling.core.sync.reader.LogReader;
import info.avalon566.shardingscaling.core.sync.writer.Writer;

/**
 * PostgreSQL scaling entry.
 *
 * @author yangyi
 */
public final class PostgreSQLScalingEntry implements ScalingEntry {
    
    @Override
    public Class<? extends JdbcReader> getJdbcReaderClass() {
        return PostgreSQLJdbcReader.class;
    }
    
    @Override
    public Class<? extends LogReader> getLogReaderClass() {
        return PostgreSQLWalReader.class;
    }
    
    @Override
    public Class<? extends Writer> getWriterClass() {
        return PostgreSQLWriter.class;
    }
    
    @Override
    public String getDatabaseType() {
        return "PostgreSQL";
    }
}