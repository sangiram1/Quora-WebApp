package com.upgrad.quora.service.dao;

import com.upgrad.quora.service.entity.UserAuthEntity;
import com.upgrad.quora.service.entity.UserEntity;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;

@Repository
public class UserDao {


    @PersistenceContext
    EntityManager entityManager;

    //method to Persist User details in Database
    public UserEntity createUser(UserEntity userEntity) {
        entityManager.persist(userEntity);
        return userEntity;
    }

    //Method to get User Details by passing username as parameter
    public UserEntity getUserByUsername(String username) {
        try {
            return entityManager.createNamedQuery("userByUsername", UserEntity.class).setParameter("username", username).getSingleResult();
        } catch (NoResultException nre) {
            return null;
        }
    }

    //Method to get User Details by passing Email as parameter
    public UserEntity getUserByEmail(String email) {
        try {
            return entityManager.createNamedQuery("userByEmail", UserEntity.class).setParameter("email", email).getSingleResult();
        } catch (NoResultException nre) {
            return null;
        }
    }

    public void createToken(UserAuthEntity userAuthEntity) {
        entityManager.persist(userAuthEntity);
    }


    public UserAuthEntity verifyToken(String accessToken) {
        try {
            return entityManager.createNamedQuery("userAuthTokenByAccessToken", UserAuthEntity.class).setParameter("accessToken", accessToken).getSingleResult();

        } catch (NoResultException nre) {
            return null;
        }
    }

  /* Added by Sangeeta as part of implementing the getUserDetails functionality
   * This method will take userId in UUID format and would pull the details of the given user.
   * This method would return the User details wrapped in the UserEntity object.
   * If the user doesn't exist, it would return null.
   */
  public UserEntity getUserDetails(final String userId) {
    try {
      /* Fetch the user details for the userId given in UUID format and return to service */
      return entityManager.createNamedQuery("userByUuid", UserEntity.class)
          .setParameter("uuid", userId).getSingleResult();
    } catch (NoResultException nre) {
      /* If user doesn't exist, return null to the service */
      return null;
    }
  }

}
