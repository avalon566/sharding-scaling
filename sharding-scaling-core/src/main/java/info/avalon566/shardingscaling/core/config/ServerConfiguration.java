package info.avalon566.shardingscaling.core.config;

import lombok.Data;

@Data
public class ServerConfiguration {

    private Integer blockQueueSize = 10000;

    private Integer pushTimeout = 1000;

    private Integer concurrency = 3;

}
