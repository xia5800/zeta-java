package com.zeta.monitor.controller;

import com.aizuda.monitor.OshiMonitor;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import com.zeta.monitor.model.ServerInfoDTO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.zetaframework.base.controller.SuperBaseController;
import org.zetaframework.base.result.ApiResult;
import org.zetaframework.core.log.annotation.SysLog;

import java.util.Properties;

/**
 * 系统监控
 *
 * @author gcc
 */
@RequiredArgsConstructor
@Api(tags = "系统监控")
@RestController
@RequestMapping("/api/monitor")
public class MonitorController implements SuperBaseController {

    private final OshiMonitor oshiMonitor;
    private final StringRedisTemplate stringRedisTemplate;

    /**
     * 获取服务器信息
     *
     * @return ApiResult<Boolean>
     */
    @ApiOperationSupport(order = 10)
    @ApiOperation(value = "服务器信息")
    @SysLog
    @GetMapping("/server")
    public ApiResult<ServerInfoDTO> serverInfo() {
        ServerInfoDTO serverInfo = ServerInfoDTO.builder()
                .sysInfo(ServerInfoDTO.SysInfo.build(oshiMonitor.getSysInfo()))
                .cpuInfo(ServerInfoDTO.CpuInfo.build(oshiMonitor.getCpuInfo()))
                .memoryInfo(ServerInfoDTO.MemoryInfo.build(oshiMonitor.getMemoryInfo()))
                .jvmInfo(ServerInfoDTO.JvmInfo.build(oshiMonitor.getJvmInfo()))
                .centralProcessor(ServerInfoDTO.CentralProcessor.build(
                        oshiMonitor.getCentralProcessor().getProcessorIdentifier()
                ))
                .diskInfos(ServerInfoDTO.DiskInfo.build(oshiMonitor.getDiskInfos()))
                .build();
        return success(serverInfo);
    }

    /**
     * 获取Redis信息
     *
     * @return ApiResult<Properties>
     */
    @ApiOperationSupport(order = 20)
    @ApiOperation(value = "Redis信息")
    @SysLog
    @GetMapping("/redis")
    public ApiResult<Properties> redisInfo() {
        Properties info = new Properties();

        RedisConnectionFactory connectionFactory = stringRedisTemplate.getConnectionFactory();
        if (connectionFactory != null) {
            RedisConnection connection = connectionFactory.getConnection();
            info = connection.info();
        }

        return success(info);
    }

}
