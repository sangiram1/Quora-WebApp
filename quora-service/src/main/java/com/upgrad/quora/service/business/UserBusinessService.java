package com.upgrad.quora.service.business;

import com.upgrad.quora.service.dao.UserDao;
import com.upgrad.quora.service.entity.UserAuthEntity;
import com.upgrad.quora.service.entity.UserEntity;
import com.upgrad.quora.service.exception.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZonedDateTime;
import java.util.UUID;

@Service
public class UserBusinessService {

    @Autowired
    private UserDao userDao;

    @Autowired
    private PasswordCryptographyProvider cryptographyProvider;

    /* Added by Sangeeta as part of implementing getUserDetails functionality
     * Here, autowiring the authorizationService instance.
     * AuthorizationService is a class which would validate the below conditions:
     * 1. User has provided a valid access token
     * 2. User has not signed out.
     */
    @Autowired
    private AuthorizationService authorizationService;

    @Transactional(propagation = Propagation.REQUIRED)
    public UserEntity createUser(UserEntity userEntity) throws SignUpRestrictedException {

        UserEntity username = userDao.getUserByUsername(userEntity.getUsername());
        UserEntity email = userDao.getUserByEmail(userEntity.getEmail());

        if (username != null) {

            throw new SignUpRestrictedException("SGR-001", "Try any other Username, this Username has already been taken");

        }
        if (email != null) {
            throw new SignUpRestrictedException("SGR-002", "This user has already been registered, try with any other emailId");

        } else {

            String encryptedText[] = cryptographyProvider.encrypt(userEntity.getPassword());
            userEntity.setSalt(encryptedText[0]);
            userEntity.setPassword(encryptedText[1]);
            return userDao.createUser(userEntity);
        }

    }

    @Transactional(propagation = Propagation.REQUIRED)
    public UserAuthEntity userAuthentication(String username, String password) throws AuthenticationFailedException {

        UserEntity userEntity = userDao.getUserByUsername(username);

        if (userEntity == null) {
            throw new AuthenticationFailedException("ATH-001", "This username does not exist");

        }

        String encryptedPassword = cryptographyProvider.encrypt(password, userEntity.getSalt());

        if (userEntity.getPassword().equals(encryptedPassword)) {
            JwtTokenProvider jwtTokenProvider = new JwtTokenProvider(encryptedPassword);
            UserAuthEntity userAuthEntity = new UserAuthEntity();
            userAuthEntity.setUser(userEntity);
            userAuthEntity.setUuid(UUID.randomUUID().toString());
            final ZonedDateTime now = ZonedDateTime.now();
            final ZonedDateTime expiresAt = now.plusHours(10);
            userAuthEntity.setAccessToken(jwtTokenProvider.generateToken(userEntity.getUuid(), now, expiresAt));
            userAuthEntity.setExpiresAt(expiresAt);
            userAuthEntity.setLoginAt(now);
            userDao.createToken(userAuthEntity);
            return userAuthEntity;
        } else {
            throw new AuthenticationFailedException("ATH-002", "Password failed");
        }

    }


    @Transactional(propagation = Propagation.REQUIRED)
    public UserAuthEntity accessTokenValidation(String accessToken) throws SignOutRestrictedException {
        UserAuthEntity userAuthEntity = userDao.verifyToken(accessToken);

        if (userAuthEntity == null) {
            throw new SignOutRestrictedException("SGR-001", "User is not Signed in");

        } else {
            final ZonedDateTime now = ZonedDateTime.now();
            userAuthEntity.setLogoutAt(now);
            return userAuthEntity;
        }


    }

    /* Added by Sangeeta as part of implementing the getUserDetails functionality
     * This method will take the userId and authorization as input and does the following:
     * 1. Check if the authorization/accessToken provided is valid or not. It will check the below:
     *    1.1. User has provided valid access token
     *    1.2. User has not signed out.
     * 2. Based on the given UUID, pull the user details and return the same to the controller.
     * 3. If user with given UUID doesn't exist, throw UserNotFoundException to controller.
     */
    public UserEntity getUserDetails(final String userId, final String authorization)
            throws AuthorizationFailedException, UserNotFoundException {
        /* Check if the Authorization is valid or not */
        UserAuthEntity userAuthEntity = authorizationService.checkAuthorization(authorization,
                "User is signed out.Sign in first to get user details");

        /* Based on the given userId, get the user details and return to the controller */
        UserEntity userDetails = userDao.getUserDetails(userId);

        /* If the user doesn't exist, throw exception */
        if (userDetails == null) {
            throw new UserNotFoundException("USR-001", "User with entered uuid does not exist");
        }
        return userDetails;
    }

}