package com.zeta.monitor.model;

import cn.hutool.core.bean.BeanUtil;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 服务器信息
 *
 * @author gcc
 */
@Data
@Builder
@ApiModel(description = "服务器信息")
public class ServerInfoDTO {

    /** 系统信息 */
    @ApiModelProperty(value = "服务器系统信息")
    private SysInfo sysInfo;

    /** cpu信息 */
    @ApiModelProperty(value = "cpu信息")
    private CpuInfo cupInfo;

    /** 内存信息 */
    @ApiModelProperty(value = "内存信息")
    private MemoryInfo memoryInfo;

    /** Jvm 虚拟机信息 */
    @ApiModelProperty(value = "Jvm 虚拟机信息")
    private JvmInfo jvmInfo;

    /** 中央处理器 */
    @ApiModelProperty(value = "中央处理器")
    private CentralProcessor centralProcessor;

    /** 磁盘信息 */
    @ApiModelProperty(value = "磁盘信息")
    private List<DiskInfo> diskInfos;


    /**
     * 操作系统信息
     *
     * 说明：
     * 自定义返回哪些数据
     */
    @Data
    @ApiModel(description = "操作系统信息")
    public static class SysInfo {
        /** 系统名称 */
        @ApiModelProperty(value = "系统名称")
        private String name;

        /** ip地址 */
        @ApiModelProperty(value = "ip地址")
        private String ip;

        /** 操作系统 */
        @ApiModelProperty(value = "操作系统")
        private String osName;

        /** 系统架构 */
        @ApiModelProperty(value = "系统架构")
        private String osArch;


        public static SysInfo build(com.aizuda.monitor.SysInfo sysInfo) {
            return BeanUtil.toBean(sysInfo, SysInfo.class);
        }
    }

    /**
     * CPU信息
     *
     * 说明：
     * 自定义返回哪些数据
     */
    @Data
    @ApiModel(description = "CPU信息")
    public static class CpuInfo {
        /** 物理处理器数量 */
        @ApiModelProperty(value = "物理处理器数量")
        private Integer physicalProcessorCount = 0;

        /** 逻辑处理器数量 */
        @ApiModelProperty(value = "逻辑处理器数量")
        private Integer logicalProcessorCount = 0;

        /** 系统使用率 */
        @ApiModelProperty(value = "系统使用率")
        private Double systemPercent = 0.0;

        /** 用户使用率 */
        @ApiModelProperty(value = "用户使用率")
        private Double userPercent = 0.0;

        /** 当前等待率 */
        @ApiModelProperty(value = "当前等待率")
        private Double waitPercent = 0.0;

        /** 当前使用率 */
        @ApiModelProperty(value = "当前使用率")
        private Double usePercent = 0.0;

        public static CpuInfo build(com.aizuda.monitor.CpuInfo cpuInfo) {
            return BeanUtil.toBean(cpuInfo, CpuInfo.class);
        }
    }

    /**
     * 内存信息
     *
     * 说明：
     * 自定义返回哪些数据
     */
    @Data
    @ApiModel(description = "内存信息")
    public static class MemoryInfo {
        /** 总计 */
        @ApiModelProperty(value = "总计")
        private String total;

        /** 已使用 */
        @ApiModelProperty(value = "已使用")
        private String used;

        /** 未使用 */
        @ApiModelProperty(value = "未使用")
        private String free;

        /** 使用率 */
        @ApiModelProperty(value = "使用率")
        private Double usePercent = 0.0;

        public static MemoryInfo build(com.aizuda.monitor.MemoryInfo memoryInfo) {
            return BeanUtil.toBean(memoryInfo, MemoryInfo.class);
        }
    }

    /**
     * JVM信息
     *
     * 说明：
     * 自定义返回哪些数据
     */
    @Data
    @ApiModel(description = "JVM信息")
    public static class JvmInfo {
        /** jdk版本 */
        @ApiModelProperty(value = "jdk版本")
        private String jdkVersion;

        /** jdk Home */
        @ApiModelProperty(value = "安装路径")
        private String jdkHome;

        /** jdk name */
        @ApiModelProperty(value = "jdk名称")
        private String jdkName;

        /** 总内存 */
        @ApiModelProperty(value = "总内存")
        private String jvmTotalMemory;

        /** Java虚拟机将尝试使用的最大内存量 */
        @ApiModelProperty(value = "Java虚拟机将尝试使用的最大内存量")
        private String maxMemory;

        /** 空闲内存 */
        @ApiModelProperty(value = "空闲内存")
        private String freeMemory;

        /** 已使用内存 */
        @ApiModelProperty(value = "已使用内存")
        private String usedMemory;

        /** 内存使用率 */
        @ApiModelProperty(value = "内存使用率")
        private Double usePercent = 0.0;

        /**
         * 返回Java虚拟机的启动时间（毫秒）。此方法返回Java虚拟机启动的大致时间。
         */
        @ApiModelProperty(value = "Java虚拟机的启动时间（毫秒）")
        private Long startTime = 0L;

        /**
         * 返回Java虚拟机的正常运行时间（毫秒）
         */
        @ApiModelProperty(value = "Java虚拟机的正常运行时间（毫秒）")
        private Long uptime = 0L;

        public static JvmInfo build(com.aizuda.monitor.JvmInfo jvmInfo) {
            return BeanUtil.toBean(jvmInfo, JvmInfo.class);
        }
    }

    /**
     * 中央处理器信息
     *
     * 说明：
     * 自定义返回哪些数据
     */
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @ApiModel(description = "中央处理器信息")
    public static class CentralProcessor {
        /** cpu名称 */
        @ApiModelProperty(value = "cpu名称")
        private String name;

        /** 是否64位cpu */
        @ApiModelProperty(value = "是否64位cpu")
        private Boolean cpu64bit = false;

        public static CentralProcessor build(oshi.hardware.CentralProcessor.ProcessorIdentifier centralProcessor) {
            return new CentralProcessor(centralProcessor.getName(), centralProcessor.isCpu64bit());
        }
    }

    /**
     * 磁盘信息
     *
     * 说明：
     * 自定义返回哪些数据
     */
    @Data
    @ApiModel(description = "磁盘信息")
    public static class DiskInfo {
        /** 名称 */
        @ApiModelProperty(value = "名称")
        private String name;

        /** 标签 */
        @ApiModelProperty(value = "标签")
        private String label;

        /** 文件系统的逻辑卷名 */
        @ApiModelProperty(value = "文件系统的逻辑卷名")
        private String logicalVolume;

        /** 文件系统的挂载点 */
        @ApiModelProperty(value = "文件系统的挂载点")
        private String mount;

        /**
         * 文件系统的类型（FAT、NTFS、etx2、ext4等）
         */
        @ApiModelProperty(value = "文件系统的类型")
        private String type;

        /** 分区大小 */
        @ApiModelProperty(value = "分区大小")
        private String size;

        /** 已使用 */
        @ApiModelProperty(value = "已使用容量")
        private String used;

        /** 可用容量 */
        @ApiModelProperty(value = "可用容量")
        private String avail;

        /** 总容量 */
        @ApiModelProperty(value = "总容量")
        private Long totalSpace;

        /** 已使用容量 */
        @ApiModelProperty(value = "已使用容量")
        private Long usableSpace;

        /** 已使用百分比 */
        @ApiModelProperty(value = "已使用百分比")
        private Double usePercent = 0.0;


        public static DiskInfo build(com.aizuda.monitor.DiskInfo diskInfo) {
            return BeanUtil.toBean(diskInfo, DiskInfo.class);
        }

        public static List<DiskInfo> build(List<com.aizuda.monitor.DiskInfo> diskInfos) {
            return diskInfos.stream()
                    .map(DiskInfo::build)
                    .collect(Collectors.toList());
        }
    }
}
