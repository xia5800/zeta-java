package org.zetaframework.core.mybatisplus.properties;

import com.baomidou.mybatisplus.annotation.DbType;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.zetaframework.core.mybatisplus.enums.UserIdType;

/**
 * 数据源配置参数
 *
 * @author gcc
 */
@ConfigurationProperties(prefix = DatabaseProperties.PREFIX)
public class DatabaseProperties {
    public static final String PREFIX = "zeta.database";


    /** 是否启用 防止全表更新与删除插件 */
    private Boolean isBlockAttack = false;

    /** 是否启用 sql性能规范插件 */
    private Boolean isIllegalSql = false;

    /** 是否启用 乐观锁插件 */
    private Boolean isOptimisticLocker = false;

    /** 分页大小限制 */
    private Long maxLimit = -1L;

    /** 数据库类型 */
    private DbType dbType = DbType.MYSQL;

    /** 溢出总页数后是否进行处理 */
    private Boolean overflow = true;

    /** 是否生成countSql优化掉left join */
    private Boolean optimizeJoin = true;

    /** hutoolId生成配置 */
    private HutoolId hutoolId = new HutoolId();

    /** 用户id类型, 关系到数据库表中create_by、update_by。ContextUtil中的userId类型 */
    private UserIdType userIdType = UserIdType.Long;



    public class HutoolId {
        /**
         * 终端ID (0-31)      单机配置0 即可。 集群部署，根据情况每个实例自增即可。
         */
        private Long workderId = 0L;
        /**
         * 数据中心ID (0-31)   单机配置0 即可。 集群部署，根据情况每个实例自增即可。
         */
        private Long dataCenterId = 0L;

        public HutoolId() {
        }

        public HutoolId(Long workderId, Long dataCenterId) {
            this.workderId = workderId;
            this.dataCenterId = dataCenterId;
        }

        public Long getWorkderId() {
            return workderId;
        }

        public void setWorkderId(Long workderId) {
            this.workderId = workderId;
        }

        public Long getDataCenterId() {
            return dataCenterId;
        }

        public void setDataCenterId(Long dataCenterId) {
            this.dataCenterId = dataCenterId;
        }
    }

    public Boolean getBlockAttack() {
        return isBlockAttack;
    }

    public void setBlockAttack(Boolean blockAttack) {
        isBlockAttack = blockAttack;
    }

    public Boolean getIllegalSql() {
        return isIllegalSql;
    }

    public void setIllegalSql(Boolean illegalSql) {
        isIllegalSql = illegalSql;
    }

    public Boolean getOptimisticLocker() {
        return isOptimisticLocker;
    }

    public void setOptimisticLocker(Boolean optimisticLocker) {
        isOptimisticLocker = optimisticLocker;
    }

    public Long getMaxLimit() {
        return maxLimit;
    }

    public void setMaxLimit(Long maxLimit) {
        this.maxLimit = maxLimit;
    }

    public DbType getDbType() {
        return dbType;
    }

    public void setDbType(DbType dbType) {
        this.dbType = dbType;
    }

    public Boolean getOverflow() {
        return overflow;
    }

    public void setOverflow(Boolean overflow) {
        this.overflow = overflow;
    }

    public Boolean getOptimizeJoin() {
        return optimizeJoin;
    }

    public void setOptimizeJoin(Boolean optimizeJoin) {
        this.optimizeJoin = optimizeJoin;
    }

    public HutoolId getHutoolId() {
        return hutoolId;
    }

    public void setHutoolId(HutoolId hutoolId) {
        this.hutoolId = hutoolId;
    }

    public UserIdType getUserIdType() {
        return userIdType;
    }

    public void setUserIdType(UserIdType userIdType) {
        this.userIdType = userIdType;
    }
}
