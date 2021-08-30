package jsz.dk.signmanagement.services;

import jsz.dk.signmanagement.common.entity.CustomException;
import jsz.dk.signmanagement.entity.ServerDataSourceDTO;

/**
 * @ProjectName: sign-management
 * @Package: jsz.dk.signmanagement.services
 * @ClassName: ServerDataSourceService
 * @Author: Strawberry
 * @Description:
 * @Date: 2021/08/04 17:10
 * @Version: 1.0
 */
public interface ServerDataSourceService {

    boolean insert(ServerDataSourceDTO dto) throws CustomException;
}
