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

package info.avalon566.shardingscaling.core.job.sync;

import info.avalon566.shardingscaling.core.config.SyncConfiguration;
import info.avalon566.shardingscaling.core.job.sync.executor.Event;
import info.avalon566.shardingscaling.core.job.sync.executor.EventType;
import info.avalon566.shardingscaling.core.job.sync.executor.Reporter;
import info.avalon566.shardingscaling.core.exception.SyncExecuteException;
import info.avalon566.shardingscaling.core.sync.SyncExecutor;
import info.avalon566.shardingscaling.core.sync.reader.LogPosition;
import info.avalon566.shardingscaling.core.sync.reader.LogReader;
import info.avalon566.shardingscaling.core.sync.reader.ReaderFactory;
import info.avalon566.shardingscaling.core.sync.writer.Writer;
import info.avalon566.shardingscaling.core.sync.writer.WriterFactory;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

/**
 * Realtime data sync job.
 *
 * @author avalon566
 */
@Slf4j
public class RealtimeDataSyncJob implements SyncJob {

    private final SyncConfiguration syncConfiguration;

    private final LogReader mysqlBinlogReader;

    private final Reporter reporter;

    public RealtimeDataSyncJob(final SyncConfiguration syncConfiguration, final Reporter reporter) {
        this.syncConfiguration = syncConfiguration;
        this.reporter = reporter;
        mysqlBinlogReader = ReaderFactory.newInstanceLogReader(syncConfiguration.getReaderConfiguration(), syncConfiguration.getPosition());
    }

    /**
     * Do something before run,mark binlog position.
     *
     * @return log position
     */
    public final LogPosition preRun() {
        return mysqlBinlogReader.markPosition();
    }

    /**
     * Start to sync realtime data.
     */
    @Override
    public final void run() {
        final List<Writer> writers = new ArrayList<>(syncConfiguration.getConcurrency());
        for (int i = 0; i < syncConfiguration.getConcurrency(); i++) {
            writers.add(WriterFactory.newInstance(syncConfiguration.getWriterConfiguration()));
        }
        try {
            new SyncExecutor(mysqlBinlogReader, writers).execute();
            log.info("realtime data sync finish");
            reporter.report(new Event(EventType.FINISHED));
        } catch (SyncExecuteException ex) {
            log.error("realtime data sync exception exit");
            ex.logExceptions();
            reporter.report(new Event(EventType.EXCEPTION_EXIT));
        }
    }
}