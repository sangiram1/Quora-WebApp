/* Created by Sangeeta as part of developing Controllers for implementing the given functionality
 * This controller has a method - userProfile which would serve the request /userprofile/{userId}
 * The userProfile method would take the authorization string from the Request Header as input
 * and also the userId in the form of UUID to pull the user details.
 * It would call getUser method in the UserBusinessService class passing the above two parameters.
 * It would further return the user details wrapped in the UserEntity class which will be further
 * added to the required response class along with the mentioned Http status to the swagger UI.
 */

package com.upgrad.quora.api.controller;

import com.upgrad.quora.api.model.UserDetailsResponse;
import com.upgrad.quora.service.business.UserBusinessService;
import com.upgrad.quora.service.entity.UserEntity;
import com.upgrad.quora.service.exception.AuthorizationFailedException;
import com.upgrad.quora.service.exception.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/* This annotation would designate the class CommonController as a Rest Controller */
@RestController
@RequestMapping("/")
public class CommonController {

  /* This annotation is used to call business logic in the service layer for fetching user details*/
  @Autowired
  private UserBusinessService userBusinessService;

  /* Method representing userProfile endpoint which would serve the request /userprofile/{userId}
   * The userProfile method would take the authorization string from the Request Header as input
   * and also the userId in the form of UUID to pull the user details.
   */
  @RequestMapping(method = RequestMethod.GET, path = "/userprofile/{userId}",
      produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
  public ResponseEntity<UserDetailsResponse> userProfile(@RequestHeader("authorization")
  final String authorization, @PathVariable("userId") final String userId)
      throws AuthorizationFailedException, UserNotFoundException {

    /* Call the getUserDetails method to get the details of the given user */
    UserEntity userDetails = userBusinessService.getUserDetails(userId, authorization);

    /* Build the userDetailsResponse object with the required attributes of the UserEntity object */
    UserDetailsResponse userDetailsResponse = new UserDetailsResponse();
    userDetailsResponse.firstName(userDetails.getFirstName());
    userDetailsResponse.lastName(userDetails.getLastName());
    userDetailsResponse.userName(userDetails.getUsername());
    userDetailsResponse.emailAddress(userDetails.getEmail());
    userDetailsResponse.country(userDetails.getCountry());
    userDetailsResponse.aboutMe(userDetails.getAboutMe());
    userDetailsResponse.dob(userDetails.getDob());
    userDetailsResponse.contactNumber(userDetails.getContactNumber());

    /* Adding the userDetailsResponse and the relevant HttpStatus status to ResponseEntity */
    return new ResponseEntity<UserDetailsResponse>(userDetailsResponse, HttpStatus.OK);
  }
}
