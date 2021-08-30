package jsz.dk.signmanagement.entity;

import jsz.dk.signmanagement.common.entity.SourceEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.LinkedHashMap;

/**
 * @ProjectName: sign-management
 * @Package: jsz.dk.signmanagement.entity
 * @ClassName: ServerDataSource
 * @Author: Strawberry
 * @Description:
 * @Date: 2021/08/04 17:02
 * @Version: 1.0
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class ServerDataSource extends SourceEntity {
    private String name;
    private String ip;
    private int port;
    private String schemaName;
    private String username;
    private String password;
    private String type;
    private String lastLoginIp;
    private String args;
    private String remark;
}
