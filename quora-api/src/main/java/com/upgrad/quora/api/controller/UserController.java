package com.upgrad.quora.api.controller;

import com.upgrad.quora.api.model.SigninResponse;
import com.upgrad.quora.api.model.SignoutResponse;
import com.upgrad.quora.api.model.SignupUserRequest;
import com.upgrad.quora.api.model.SignupUserResponse;
import com.upgrad.quora.service.business.UserBusinessService;
import com.upgrad.quora.service.entity.UserAuthEntity;
import com.upgrad.quora.service.entity.UserEntity;
import com.upgrad.quora.service.exception.AuthenticationFailedException;
import com.upgrad.quora.service.exception.SignOutRestrictedException;
import com.upgrad.quora.service.exception.SignUpRestrictedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Base64;
import java.util.UUID;

@RestController
@RequestMapping("/")
public class UserController {

    @Autowired
    private UserBusinessService userBusinessService;

    /*The Sign up Api end point helps the user to register in the application
     *This is a post request called with "/user/signup.
     *It collects all the data from the user and posts in database after validation
     */
    @RequestMapping(method = RequestMethod.POST, path = "/user/signup", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<SignupUserResponse> signup(final SignupUserRequest signupUserRequest) throws SignUpRestrictedException {

        final UserEntity userEntity = new UserEntity();
        userEntity.setUuid(UUID.randomUUID().toString());

        userEntity.setFirstName(signupUserRequest.getFirstName());
        userEntity.setLastName(signupUserRequest.getLastName());
        userEntity.setUsername(signupUserRequest.getUserName());
        userEntity.setEmail(signupUserRequest.getEmailAddress());
        userEntity.setPassword(signupUserRequest.getPassword());
        userEntity.setSalt("abc12345");
        userEntity.setCountry(signupUserRequest.getCountry());
        userEntity.setDob(signupUserRequest.getDob());
        userEntity.setAboutMe(signupUserRequest.getAboutMe());
        userEntity.setRole("nonadmin");
        userEntity.setContactNumber(signupUserRequest.getContactNumber());
        final UserEntity createuserEntity = userBusinessService.createUser(userEntity);
        SignupUserResponse response = new SignupUserResponse().id(createuserEntity.getUuid()).status("USER SUCCESSFULLY REGISTERED");
        return new ResponseEntity<SignupUserResponse>(response, HttpStatus.CREATED);

    }

    /*This Api end point is a post request which takes in the user and password in base 64 format from the request
     *It decodes the base 64 format and separate out the username and password
     * After decoding the base 64 format it sends the username and password for validation
     *After successful validation the user is logged in the application
     */
    @RequestMapping(method = RequestMethod.POST, path = "/user/signin", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<SigninResponse> signin(@RequestHeader("authorization") final String authorization) throws AuthenticationFailedException {
        byte[] decode = Base64.getDecoder().decode(authorization.split("Basic ")[1]);
        String decodedText = new String(decode);
        String decodedArray[] = decodedText.split(":");
        UserAuthEntity userAuthEntity = userBusinessService.userAuthentication(decodedArray[0], decodedArray[1]);
        UserEntity userEntity = userAuthEntity.getUser();
        HttpHeaders header = new HttpHeaders();
        header.add("access_token", userAuthEntity.getAccessToken());
        return new ResponseEntity<SigninResponse>(new SigninResponse().id(userEntity.getUuid()).message("SIGNED IN SUCCESSFULLY"), header, HttpStatus.OK);
    }


    /*This api endpoint takes in the post request and is responsible for successful signout of User.
     *It takes in the parameter of access token generated at the time of login
     * Signs out if the user is logged in and has not signed out yet.
     */
    @RequestMapping(method = RequestMethod.POST, path = "/user/signout", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<SignoutResponse> signout(@RequestHeader("authorization") String accessToken) throws SignOutRestrictedException {
        UserAuthEntity userAuthEntity = userBusinessService.accessTokenValidation(accessToken);
        return new ResponseEntity<SignoutResponse>(new SignoutResponse().id(userAuthEntity.getUuid()).message("SIGNED OUT SUCCESSFULLY"), HttpStatus.OK);

    }


}


