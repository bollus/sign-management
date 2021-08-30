package jsz.dk.signmanagement.services;

import jsz.dk.signmanagement.common.entity.CustomException;
import jsz.dk.signmanagement.entity.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @ProjectName: sign-management
 * @Package: jsz.dk.signmanagement.services
 * @ClassName: UserService
 * @Author: Strawberry
 * @Description:
 * @Date: 2021/07/02 18:12
 */
public interface UserService {
    User registry(UserDTO dto) throws CustomException;

    GoogleSecretVO generateGoogleSecret(User user);

    UserLoginVO login(UserDTO dto) throws CustomException;

    boolean logout(User user) throws CustomException;

    void genQrCode(String secretQrCode, HttpServletResponse response) throws CustomException;

    void bindGoogle(GoogleDTO dto, User user, HttpServletRequest request) throws CustomException;

    void googleLogin(Long code, User user, HttpServletRequest request) throws CustomException;
}
