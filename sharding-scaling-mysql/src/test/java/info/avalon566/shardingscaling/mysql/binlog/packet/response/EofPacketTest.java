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

package info.avalon566.shardingscaling.mysql.binlog.packet.response;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class EofPacketTest {
    
    @Test
    public void assertFromByteBuf() {
        ByteBuf byteBuf = Unpooled.buffer();
        byteBuf.writeByte(0x01);
        byteBuf.writeShortLE(Short.MIN_VALUE);
        byteBuf.writeShortLE(Short.MIN_VALUE);
        EofPacket actual = new EofPacket();
        actual.fromByteBuf(byteBuf);
        assertThat(actual.getFieldCount(), is((short) 1));
        assertThat(actual.getWarningCount(), is(32768));
        assertThat(actual.getStatusFlag(), is(32768));
    }
}
