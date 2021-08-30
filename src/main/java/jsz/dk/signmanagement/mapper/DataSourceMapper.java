package jsz.dk.signmanagement.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import jsz.dk.signmanagement.entity.ServerDataSource;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @ProjectName: sign-management
 * @Package: jsz.dk.signmanagement.mapper
 * @ClassName: DataSourceMapper
 * @Author: Strawberry
 * @Description:
 * @Date: 2021/08/26 16:45
 * @Version: 1.0
 */
@Mapper
public interface DataSourceMapper extends BaseMapper<ServerDataSource> {
    @Select("SELECT * FROM server_data_source")
    List<ServerDataSource> get();

}
