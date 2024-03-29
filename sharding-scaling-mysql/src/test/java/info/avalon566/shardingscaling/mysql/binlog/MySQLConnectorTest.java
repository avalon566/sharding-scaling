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

package info.avalon566.shardingscaling.mysql.binlog;

import io.netty.channel.Channel;
import io.netty.channel.ChannelPipeline;
import io.netty.util.concurrent.Promise;
import info.avalon566.shardingscaling.mysql.binlog.packet.command.BinlogDumpCommandPacket;
import info.avalon566.shardingscaling.mysql.binlog.packet.command.QueryCommandPacket;
import info.avalon566.shardingscaling.mysql.binlog.packet.command.RegisterSlaveCommandPacket;
import info.avalon566.shardingscaling.mysql.binlog.packet.response.InternalResultSet;
import info.avalon566.shardingscaling.mysql.binlog.packet.response.OkPacket;
import info.avalon566.shardingscaling.utils.ReflectionUtil;
import lombok.SneakyThrows;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.ArgumentMatchers;
import org.mockito.junit.MockitoJUnitRunner;

import java.net.InetSocketAddress;
import java.util.concurrent.ExecutionException;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class MySQLConnectorTest {
    
    @Mock
    private Channel channel;
    
    @Mock
    private ChannelPipeline pipeline;
    
    private InetSocketAddress inetSocketAddress;
    
    private MySQLConnector mySQLConnector;
    
    @Before
    public void setUp() throws Exception {
        mySQLConnector = new MySQLConnector(1, "host", 3306, "username", "password");
        when(channel.pipeline()).thenReturn(pipeline);
        inetSocketAddress = new InetSocketAddress("host", 3306);
        when(channel.localAddress()).thenReturn(inetSocketAddress);
    }
    
    @Test
    public void assertConnect() throws NoSuchFieldException, IllegalAccessException {
        final ServerInfo expected = new ServerInfo();
        mockChannelResponse(expected);
        mySQLConnector.connect();
        ServerInfo actual = ReflectionUtil.getFieldValueFromClass(mySQLConnector, "serverInfo", ServerInfo.class);
        assertThat(actual, is(expected));
    }
    
    @Test
    public void assertExecute() throws NoSuchFieldException, IllegalAccessException {
        mockChannelResponse(new OkPacket());
        ReflectionUtil.setFieldValueToClass(mySQLConnector, "channel", channel);
        assertTrue(mySQLConnector.execute(""));
        verify(channel).writeAndFlush(ArgumentMatchers.any(QueryCommandPacket.class));
    }
    
    @Test
    public void assertExecuteUpdate() throws NoSuchFieldException, IllegalAccessException {
        OkPacket expected = new OkPacket();
        ReflectionUtil.setFieldValueToClass(expected, "affectedRows", 10);
        mockChannelResponse(expected);
        ReflectionUtil.setFieldValueToClass(mySQLConnector, "channel", channel);
        assertThat(mySQLConnector.executeUpdate(""), is(10));
        verify(channel).writeAndFlush(ArgumentMatchers.any(QueryCommandPacket.class));
    }
    
    @Test
    public void assertExecuteQuery() throws NoSuchFieldException, IllegalAccessException {
        InternalResultSet expected = new InternalResultSet(null);
        mockChannelResponse(expected);
        ReflectionUtil.setFieldValueToClass(mySQLConnector, "channel", channel);
        assertThat(mySQLConnector.executeQuery(""), is(expected));
        verify(channel).writeAndFlush(ArgumentMatchers.any(QueryCommandPacket.class));
    }
    
    @Test
    public void assertSubscribeBelow56Version() throws NoSuchFieldException, IllegalAccessException, ExecutionException, InterruptedException {
        ServerInfo serverInfo = new ServerInfo();
        serverInfo.setServerVersion(new ServerVersion("5.5.0-log"));
        ReflectionUtil.setFieldValueToClass(mySQLConnector, "serverInfo", serverInfo);
        ReflectionUtil.setFieldValueToClass(mySQLConnector, "channel", channel);
        mockChannelResponse(new OkPacket());
        mySQLConnector.subscribe("", 4L);
        verify(channel).writeAndFlush(ArgumentMatchers.any(RegisterSlaveCommandPacket.class));
        verify(channel).writeAndFlush(ArgumentMatchers.any(BinlogDumpCommandPacket.class));
    }
    
    private void mockChannelResponse(final Object response) {
        new Thread(new Runnable() {
        
            @Override
            @SneakyThrows
            public void run() {
                while (true) {
                    Promise responseCallback = ReflectionUtil.getFieldValueFromClass(mySQLConnector, "responseCallback", Promise.class);
                    if (null != responseCallback) {
                        responseCallback.setSuccess(response);
                        break;
                    }
                }
            }
        }).start();
    }
}
