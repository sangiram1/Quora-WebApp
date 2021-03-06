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

    //This method is to Persist User details in database and returns the user entity back to the business layer for processing
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

    //This method persists the generated JWT token in database.
    public void createToken(UserAuthEntity userAuthEntity) {
        entityManager.persist(userAuthEntity);
    }


    //This method takes in the parameter of access token and verifies from database if its a valid token.
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

    /* Added by Ankit as part of implementing the deleteUser functionality
     * This method will take user entity and will delete the user from the database.
     * This method would return the User details wrapped in the UserEntity object.
     * If the user doesn't exist, it would return null.
     */
    public UserEntity deleteUser(UserEntity userEntity) {
        try {
            // Remove the user from the database
            entityManager.remove(userEntity);
            return userEntity;
        } catch (NoResultException nre) {
            // If user doesn't exist, return null to the service
            return null;
        }
    }

}
