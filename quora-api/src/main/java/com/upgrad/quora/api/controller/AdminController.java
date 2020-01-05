/* Created by Ankit as part of developing Controllers for implementing the given functionality
 * This controller has single endpoints.
 * 1. userDelete() method serves /admin/user/{userId} request
 */
package com.upgrad.quora.api.controller;

import com.upgrad.quora.api.model.UserDeleteResponse;
import com.upgrad.quora.service.business.AdminBusinessService;
import com.upgrad.quora.service.entity.UserEntity;
import com.upgrad.quora.service.exception.AuthorizationFailedException;
import com.upgrad.quora.service.exception.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/* This annotation would designate the class AdminController as a Rest Controller */
@RestController
@RequestMapping("/")
public class AdminController {

    /* Autowire AdminBusinessService to facilitate the CRUD operations on UserEntity */
    @Autowired
    private AdminBusinessService adminBusinessService;

    /* userDelete() method represents an endpoint which would serve /admin/user/{userId} request
     * This method would take two inputs : the authorization string from the Request Header
     * and a userId string which is to be deleted
     * This will call the deleteUser method in AdminBusinessService which would facilitate the deletion
     * of the given user if the user has provided valid authorization.
     */
    @RequestMapping(method = RequestMethod.DELETE, path = "/admin/user/{userId}",
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<UserDeleteResponse> userDelete(@PathVariable("userId") final String userId,
                                                         @RequestHeader("authorization") final String authorization)
            throws UserNotFoundException, AuthorizationFailedException {

        /* Call the deleteUser in service to delete the user record in database*/
        UserEntity deletedUserEntity = adminBusinessService.deleteUser(userId, authorization);

        /* Once the user is deleted, prepare the response with UUID and a status message */
        UserDeleteResponse response = new UserDeleteResponse();
        response.id(deletedUserEntity.getUuid());
        response.status("USER SUCCESSFULLY DELETED");

        /* return the response object back to the client*/
        return new ResponseEntity<UserDeleteResponse>(response, HttpStatus.OK);
    }
}
