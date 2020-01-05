/* Created by Ankit as part of developing Service classes for implementing given functionalities
 * This service has single method to handle business logic of deletion of a user.
 * 1. deleteUser() method would facilitate the deletion of a user in database
 */
package com.upgrad.quora.service.business;

import com.upgrad.quora.service.dao.UserDao;
import com.upgrad.quora.service.entity.UserAuthEntity;
import com.upgrad.quora.service.entity.UserEntity;
import com.upgrad.quora.service.exception.AuthorizationFailedException;
import com.upgrad.quora.service.exception.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/* @Service Annotation would help the container to recognize AdminBusinessService as a service class */
@Service
public class AdminBusinessService {

    /* Autowire Dao and Service classes to facilitate the required operations on UserEntity */
    @Autowired
    private UserDao userDao;

    @Autowired
    private AuthorizationService authorizationService;

    /* deleteUser() method would facilitate the deletion of a user in database
     * This method would take two inputs : the authorization string for user authorization
     * and a userUuid string of which user has to be deleted
     * It would return the deleted object data back to the calling controller.
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public UserEntity deleteUser(final String userUuid,
                                 final String authorization) throws AuthorizationFailedException, UserNotFoundException {

        /* Check if the authorization/accessToken provided is valid or not. It will check the below:
         *  1.1. User has provided valid access token
         *  1.2. User has not signed out.
         */
        UserAuthEntity userAuthToken = authorizationService.checkAuthorization(authorization,
                "User is signed out");

        /*
         * Check the user role - if 'nonadmin' then throw exception otherwise proceed to delete the user
         */
        UserEntity userEntity = userAuthToken.getUser();
        String userRole = userEntity.getRole();
        if (userRole.equals("nonadmin")) {
            throw new AuthorizationFailedException("ATHR-003", "Unauthorized Access, Entered user is not an admin");
        }

        /* Based on the given userUuid, get the user details and return to the controller */
        UserEntity userToBeDeleted = userDao.getUserDetails(userUuid);

        /* If the user doesn't exist, throw exception */
        if (userToBeDeleted == null) {
            throw new UserNotFoundException("USR-001", "User with entered uuid to be deleted does not exist");
        }

        /* Return details of the deleted user entity to the calling controller*/
        return userDao.deleteUser(userToBeDeleted);
    }

}
