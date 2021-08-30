package jsz.dk.signmanagement.services.impl;

import jsz.dk.signmanagement.common.entity.CustomException;
import jsz.dk.signmanagement.entity.ServerDataSource;
import jsz.dk.signmanagement.interceptor.DynamicDataSource;
import jsz.dk.signmanagement.mapper.DataSourceMapper;
import jsz.dk.signmanagement.services.DBChangeService;
import jsz.dk.signmanagement.utils.DBContextHolder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @ProjectName: sign-management
 * @Package: jsz.dk.signmanagement.services.impl
 * @ClassName: DBChangeServiceImpl
 * @Author: Strawberry
 * @Description:
 * @Date: 2021/08/26 16:50
 * @Version: 1.0
 */
@Service
@Slf4j
public class DBChangeServiceImpl implements DBChangeService {
    private final static String TAG = "[DBChangeService]";

    @Resource
    DataSourceMapper dataSourceMapper;
    @Resource
    private DynamicDataSource dynamicDataSource;

    @Override
    public List<ServerDataSource> get() {
        return dataSourceMapper.get();
    }

    @Override
    public boolean changeDb(long datasourceId) throws CustomException {
        //默认切换到主数据源,进行整体资源的查找
        DBContextHolder.clearDataSource();

        List<ServerDataSource> dataSourcesList = dataSourceMapper.get();

        for (ServerDataSource serverDataSource : dataSourcesList) {
            if (serverDataSource.getId() == (datasourceId)) {
                System.out.println("需要使用的的数据源已经找到,datasourceId是：" + serverDataSource.getId());
                //创建数据源连接&检查 若存在则不需重新创建
                try {
                    dynamicDataSource.createDataSourceWithCheck(serverDataSource);
                }catch (Exception e){
                    throw new CustomException(TAG,e.getMessage());
                }
                //切换到该数据源
                DBContextHolder.setDataSource(serverDataSource.getId());
                return true;
            }
        }
        return false;
    }
}
