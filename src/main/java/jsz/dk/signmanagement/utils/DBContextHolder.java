package jsz.dk.signmanagement.utils;

import lombok.extern.slf4j.Slf4j;

/**
 * @ProjectName: sign-management
 * @Package: jsz.dk.signmanagement.utils
 * @ClassName: DBContextHolder
 * @Author: Strawberry
 * @Description:
 * @Date: 2021/08/04 19:21
 * @Version: 1.0
 */
@Slf4j
public class DBContextHolder {
    // 对当前线程的操作-线程安全的
    private static final ThreadLocal<Long> contextHolder = new ThreadLocal<>();

    // 调用此方法，切换数据源
    public static void setDataSource(long datasourceId) {
        contextHolder.set(datasourceId);
        log.info("已切换到数据源:{}",datasourceId);
    }

    // 获取数据源
    public static long getDataSource() {
        return contextHolder.get();
    }

    // 删除数据源
    public static void clearDataSource() {
        contextHolder.remove();
        log.info("已切换到主数据源");
    }

}
