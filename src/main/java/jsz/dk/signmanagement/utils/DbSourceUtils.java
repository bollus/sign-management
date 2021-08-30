package jsz.dk.signmanagement.utils;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import jsz.dk.signmanagement.entity.ServerDataSource;
import org.apache.commons.lang3.StringUtils;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @ProjectName: sign-management
 * @Package: jsz.dk.signmanagement.utils
 * @ClassName: DbSourceUtils
 * @Author: Strawberry
 * @Description:
 * @Date: 2021/08/05 14:27
 * @Version: 1.0
 */
public class DbSourceUtils {

    /**数据库basic**/
    public static final String MYSQL = "com.mysql.cj.jdbc.Driver";
    /**数据库base**/
    public static final String ORACLE = "oracle.jdbc.driver.OracleDriver";
    /**数据库article**/
    public static final String SQL_2005 = "com.microsoft.sqlserver.jdbc.SQLServerDriver";

    private static String createUrl(String ip, int port, String scheme, LinkedHashMap<String, String> args, String type) {
        StringBuilder sb = new StringBuilder();
        switch (type) {
            case "mysql":
                sb.append("jdbc:mysql://");
                break;
            case "oracle":
            case "mongodb":
            case "sqlserver":
                break;
        }
        sb.append(ip);
        sb.append(":");
        sb.append(port).append("/").append(scheme);
        System.out.println(sb);
        for (Map.Entry<String, String> stringStringEntry : args.entrySet()) {
            String key = (stringStringEntry).getKey();
            String val = (stringStringEntry).getValue();
            if (StringUtils.isBlank(val)) {
                continue;
            }
            sb.append("&");
            sb.append(key).append("=").append(val);
        }
        return String.valueOf(sb);
    }

    public static String createUrl(ServerDataSource dataSource) {
        LinkedHashMap<String, String> argsMap;
        Gson gson = new Gson();
        argsMap = gson.fromJson(dataSource.getArgs(),
                new TypeToken<LinkedHashMap<String, String>>() {
        }.getType());
        return createUrl(dataSource.getIp(), dataSource.getPort(), dataSource.getScheme(), argsMap, dataSource.getType());
    }

    public static String createUrlOfMysql(ServerDataSource dataSource) {
        LinkedHashMap<String, String> argsMap;
        Gson gson = new Gson();
        argsMap = gson.fromJson(dataSource.getArgs(),
                new TypeToken<LinkedHashMap<String, String>>() {
        }.getType());
        return createUrl(dataSource.getIp(), dataSource.getPort(), dataSource.getScheme(), argsMap, "mysql");
    }

    public static String createUrlOfMysql(String ip, int port, String scheme, LinkedHashMap<String, String> args) {
        return createUrl(ip, port, scheme, args, "mysql");
    }

}
