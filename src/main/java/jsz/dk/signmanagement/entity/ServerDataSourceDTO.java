package jsz.dk.signmanagement.entity;

import lombok.Data;

/**
 * @ProjectName: sign-management
 * @Package: jsz.dk.signmanagement.entity
 * @ClassName: ServerDataSourceVO
 * @Author: Strawberry
 * @Description:
 * @Date: 2021/08/26 17:29
 * @Version: 1.0
 */
@Data
public class ServerDataSourceDTO {
    private String name;
    private String ip;
    private int port;
    private String scheme;
    private String username;
    private String password;
    private String type;
    private String args;
    private String remark;
}
