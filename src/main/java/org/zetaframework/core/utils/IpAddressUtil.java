package org.zetaframework.core.utils;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.StrUtil;
import org.lionsoul.ip2region.xdb.Searcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.InputStream;
import java.util.Objects;
import java.util.regex.Pattern;

/**
 * ip地址查询工具类
 *
 * 说明：
 * 基于ip2region
 * @author gcc
 */
public class IpAddressUtil {
    private static Logger logger = LoggerFactory.getLogger(IpAddressUtil.class);

    private static final Pattern SPLIT_PATTERN = Pattern.compile("\\|");
    private static final String ZERO  = "0";
    private static Searcher searcher = null;
    private static byte[] vectorIndex = null;

    static {
        // fix: 解决打成jar包无法读取文件ip2region.xdb问题 --by gcc date:2023-08-14
        String tmpDir = System.getProperty("user.dir") + File.separator + "temp";
        String dbPath = tmpDir + File.separator + "ip2region.db";

        File file = new File(dbPath);
        if (!file.exists()) {
            logger.info("init ip region db path [{}]", dbPath);
            InputStream resourceAsStream = IpAddressUtil.class.getResourceAsStream("/ip2region.xdb");
            FileUtil.writeFromStream(resourceAsStream, file);
        }

        try {
            // 1.从 dbPath 中预先加载 VectorIndex 缓存，并且把这个得到的数据作为全局变量，后续反复使用
            vectorIndex = Searcher.loadVectorIndexFromFile(dbPath);

            // 2.使用全局的 vIndex 创建带 VectorIndex 缓存的查询对象。
            searcher = Searcher.newWithVectorIndex(dbPath, vectorIndex);
        }catch (Exception e) {
            logger.error("ip2region初始化失败", e);
        }
    }


    /**
     * 查询ip地址所在地区
     * @param ip
     * @return String ip地址所在地区 eg: 美国|0|华盛顿|0|谷歌
     */
    public static String search(String ip) {
        if (searcher == null) {
            return StrUtil.EMPTY;
        }
        if (Objects.equals(ip, "0:0:0:0:0:0:0:1")) {
            return "0|0|0|内网IP|内网IP";
        }

        String result = StrUtil.EMPTY;
        try {
            result = searcher.search(ip);
        } catch (Exception e) {
            logger.error("根据ip地址查询地区信息失败", e);
        }
        return result;
    }

    /**
     * 查询ip地址所在地区
     * @param ip
     * @return IpInfo
     */
    public static IpInfo searchInfo(String ip) {
        String address = search(ip);
        if (address.isBlank()) {
            return new IpInfo(address);
        }

        String[] info = SPLIT_PATTERN.split(address);
        if (info.length < 5) {
            info = ArrayUtil.resize(info, 5);
        }

        IpInfo ipInfo = new IpInfo();
        ipInfo.setCountry(filterZero(info[0]));
        ipInfo.setRegion(filterZero(info[1]));
        ipInfo.setProvince(filterZero(info[2]));
        ipInfo.setCity(filterZero(info[3]));
        ipInfo.setIsp(filterZero(info[4]));
        ipInfo.setSource(address);
        return ipInfo;
    }

    /**
     * 数据过滤，ip2Region会用0填充没有数据的字段
     * @param info
     * @return String
     */
    private static String filterZero(String info) {
        return StrUtil.isNotEmpty(info) || ZERO.equals(info) ? null : info;
    }


    /**
     * ip地址信息
     */
    public static class IpInfo {
        /** 国家 */
        private String country;
        /** 地区 */
        private String region;
        /** 省 */
        private String province;
        /** 市 */
        private String city;
        /** 网络供应商 */
        private String isp;
        /** 源数据 */
        private String source;

        public IpInfo() {
        }

        public IpInfo(String source) {
            this.source = source;
        }

        public String getCountry() {
            return country;
        }

        public void setCountry(String country) {
            this.country = country;
        }

        public String getRegion() {
            return region;
        }

        public void setRegion(String region) {
            this.region = region;
        }

        public String getProvince() {
            return province;
        }

        public void setProvince(String province) {
            this.province = province;
        }

        public String getCity() {
            return city;
        }

        public void setCity(String city) {
            this.city = city;
        }

        public String getIsp() {
            return isp;
        }

        public void setIsp(String isp) {
            this.isp = isp;
        }

        public String getSource() {
            return source;
        }

        public void setSource(String source) {
            this.source = source;
        }
    }

}
