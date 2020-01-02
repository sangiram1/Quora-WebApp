/* Added by Sangeeta as part of implementing authorization validation
 * Since, this is a common function and needed in every function implementation, creating a separate
 * service class to keep the common code in one place.
 * AuthorizationService is a class which would validate the below conditions:
 * 1. User has provided a valid access token
 * 2. User has not signed out.
 */
package com.upgrad.quora.service.business;

import com.upgrad.quora.service.dao.UserDao;
import com.upgrad.quora.service.entity.UserAuthEntity;
import com.upgrad.quora.service.exception.AuthorizationFailedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuthorizationService {

  /* Autowired the userDao to get the access token verified from database */
  @Autowired
  private UserDao userDao;

  /* checkAuthorization method takes accessToken and an exceptionMessage in case */
  public UserAuthEntity checkAuthorization(final String authorization, final String exceptionMessage)
      throws AuthorizationFailedException {

    /* This will verify if the given authorization token is valid and exists in database */
    UserAuthEntity userAuthToken = userDao.verifyToken(authorization);

    /* If the token doesn't exist, it will throw AuthorizationFailedException
     * saying User has not signed in.
     */
    if (userAuthToken == null) {
      throw new AuthorizationFailedException("ATHR-001", "User has not signed in");
    }

    /* If the user has already logged out, it will throw AuthorizationFailedException
     * saying User has signed out. To perform any action, sign in is needed first.
     */
    if (userAuthToken.getLogoutAt() != null) {
      throw new AuthorizationFailedException("ATHR-002", exceptionMessage);
    }

    /* Return the UserAuthToken object if the above validation passes */
    return userAuthToken;
  }

}
