package jsz.dk.signmanagement.services.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import jsz.dk.signmanagement.common.entity.CustomException;
import jsz.dk.signmanagement.entity.ServerDataSource;
import jsz.dk.signmanagement.entity.ServerDataSourceDTO;
import jsz.dk.signmanagement.mapper.DataSourceMapper;
import jsz.dk.signmanagement.services.ServerDataSourceService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Iterator;

/**
 * @ProjectName: sign-management
 * @Package: jsz.dk.signmanagement.services.impl
 * @ClassName: ServerDataSourceServiceImpl
 * @Author: Strawberry
 * @Description:
 * @Date: 2021/08/04 17:11
 * @Version: 1.0
 */
@Slf4j
@Service
public class ServerDataSourceServiceImpl implements ServerDataSourceService {
    private final static String TAG = "[ServerDataSourceService]";

    @Resource
    private DataSourceMapper dataSourceMapper;

    @Override
    public boolean insert(ServerDataSourceDTO dto) throws CustomException {
        if (!dto.getIp().matches("([1-9]|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])(\\.(\\d|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])){3}")){
            throw new CustomException(TAG, "IP格式不正确");
        }
        if (dto.getPort()<=0 || dto.getPort()>65535){
            throw new CustomException(TAG, "端口号无效");
        }
        if (StringUtils.isEmpty(dto.getScheme())){
            throw new CustomException(TAG, "库名为空");
        }
        if (StringUtils.isEmpty(dto.getUsername())){
            throw new CustomException(TAG, "数据库用户名为空");
        }
        switch (dto.getType()){
            case "mysql":
            case "oracle":
            case "sqlserver":
                break;
            case "":
                throw new CustomException(TAG, "数据库类型为空");
            default:
                throw new CustomException(TAG, "暂不支持该数据库类型");
        }
        ServerDataSource serverDataSource = new ServerDataSource();
        BeanUtils.copyProperties(dto,serverDataSource);
        return dataSourceMapper.insert(serverDataSource) == 1;
    }

    private String parseJsonToUrlParams(JSONObject json) {
        Iterator<String> it = json.keySet().iterator();
        StringBuilder paramStr = new StringBuilder();
        while (it.hasNext()) {
            String key = it.next();
            String value = json.getString(key);
            paramStr.append("&").append(key).append("=").append(value);
        }
        return paramStr.toString();
    }
}
