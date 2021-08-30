package jsz.dk.signmanagement.controller;

import jsz.dk.signmanagement.common.annotations.NeedLogin;
import jsz.dk.signmanagement.common.annotations.OperationLogDetail;
import jsz.dk.signmanagement.common.controller.BaseController;
import jsz.dk.signmanagement.common.entity.CustomException;
import jsz.dk.signmanagement.common.entity.ResponseParent;
import jsz.dk.signmanagement.entity.ServerDataSourceDTO;
import jsz.dk.signmanagement.entity.User;
import jsz.dk.signmanagement.enums.OperationType;
import jsz.dk.signmanagement.enums.OperationUnit;
import jsz.dk.signmanagement.enums.ResponseCode;
import jsz.dk.signmanagement.services.DBChangeService;
import jsz.dk.signmanagement.services.ServerDataSourceService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;


/**
 * @ProjectName: sign-management
 * @Package: jsz.dk.signmanagement.controller
 * @ClassName: ServerDataSourceController
 * @Author: Strawberry
 * @Description:
 * @Date: 2021/08/26 18:53
 * @Version: 1.0
 */
@RestController
@RequestMapping("i/v1/data")
public class ServerDataSourceController extends BaseController {

    @Resource
    private ServerDataSourceService serverDataSourceService;
    @Resource
    private DBChangeService dbChangeService;

    @OperationLogDetail(detail = "添加数据源", level = 3, operationUnit = OperationUnit.SERVER_DATA_SOURCE, operationType = OperationType.INSERT)
    @NeedLogin(google = true)
    @ResponseBody
    @PostMapping("insert")
    public ResponseParent<User> insert(@RequestBody ServerDataSourceDTO dto){
        try {
            if (serverDataSourceService.insert(dto)){
                return ResponseParent.succeed("添加成功");
            }else {
                return ResponseParent.fail(ResponseCode.PARAM_VALID_ERROR.getCode(),"业务异常，添加失败");
            }
        } catch (CustomException ce) {
            return ResponseParent.fail(ResponseCode.CUSTOM_FAILED.getCode(), ce.getMsg());
        }
    }

    @OperationLogDetail(detail = "切换数据源", level = 3, operationUnit = OperationUnit.SERVER_DATA_SOURCE, operationType = OperationType.INSERT)
    @NeedLogin(google = true)
    @ResponseBody
    @GetMapping("changedb")
    public ResponseParent<User> changeDb(Long datasourceId){
        try {
            if (dbChangeService.changeDb(datasourceId)){
                return ResponseParent.succeed("切换成功");
            }else {
                return ResponseParent.fail(ResponseCode.PARAM_VALID_ERROR.getCode(),"业务异常，切换失败");
            }
        } catch (CustomException ce) {
            return ResponseParent.fail(ResponseCode.CUSTOM_FAILED.getCode(), ce.getMsg());
        }
    }
}
