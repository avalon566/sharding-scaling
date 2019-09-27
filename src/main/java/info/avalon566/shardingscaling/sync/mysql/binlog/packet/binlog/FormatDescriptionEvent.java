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

package info.avalon566.shardingscaling.sync.mysql.binlog.packet.binlog;

import info.avalon566.shardingscaling.sync.mysql.binlog.codec.DataTypesCodec;
import io.netty.buffer.ByteBuf;
import lombok.Data;

/**
 * https://dev.mysql.com/doc/internals/en/format-description-event.html
 *
 * @author avalon566
 */
@Data
public class FormatDescriptionEvent {

    private int binglogVersion;

    private String mysqlServerVersion;

    private long createTimestamp;

    private short eventHeaderLength;

    private short checksumType = 1;

    public void parse(final ByteBuf in) {
        binglogVersion = DataTypesCodec.readUnsignedInt2LE(in);
        mysqlServerVersion = DataTypesCodec.readFixedLengthString(50, in);
        createTimestamp = DataTypesCodec.readUnsignedInt4LE(in);
        eventHeaderLength = DataTypesCodec.readUnsignedInt1(in);
        // skip remain data
        in.readBytes(in.readableBytes());
    }
}