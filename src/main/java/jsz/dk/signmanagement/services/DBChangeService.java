package jsz.dk.signmanagement.services;

import jsz.dk.signmanagement.common.entity.CustomException;
import jsz.dk.signmanagement.entity.ServerDataSource;

import java.util.List;

/**
 * @ProjectName: sign-management
 * @Package: jsz.dk.signmanagement.services
 * @ClassName: DBChangeService
 * @Author: Strawberry
 * @Description:
 * @Date: 2021/08/26 16:49
 * @Version: 1.0
 */
public interface DBChangeService {

    List<ServerDataSource> get();

    boolean changeDb(long datasourceId) throws CustomException;
}
