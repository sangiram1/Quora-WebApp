/* Created by Sangeeta as part of developing Controllers for implementing the given functionality
 * This controller has different endpoints. Below is list of endpoints and their respective paths:
 * 1. createQuestion() method serves /question/create request
 * 2. getAllQuestions() method serves /question/all request
 * 3. editQuestionContent() method serves /question/edit/{questionId} request
 * 4. deleteQuestion() method serves /question/delete/{questionId} request
 * 5. getAllQuestionsByUser() method serves question/all/{userId} request
 */

package com.upgrad.quora.api.controller;

import com.upgrad.quora.api.model.QuestionDeleteResponse;
import com.upgrad.quora.api.model.QuestionDetailsResponse;
import com.upgrad.quora.api.model.QuestionEditRequest;
import com.upgrad.quora.api.model.QuestionEditResponse;
import com.upgrad.quora.api.model.QuestionRequest;
import com.upgrad.quora.api.model.QuestionResponse;
import com.upgrad.quora.service.business.QuestionService;
import com.upgrad.quora.service.entity.QuestionEntity;
import com.upgrad.quora.service.exception.AuthorizationFailedException;
import com.upgrad.quora.service.exception.InvalidQuestionException;
import com.upgrad.quora.service.exception.UserNotFoundException;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/* This annotation would designate the class QuestionController as a Rest Controller */
@RestController
@RequestMapping("/")
public class QuestionController {

  /* Autowire QuestionService to facilitate the CRUD operations on QuestionEntity */
  @Autowired
  private QuestionService questionService;

  /* createQuestion() method represents an endpoint which would serve /question/create request
   * This method would take two inputs : the authorization string from the Request Header
   * and a QuestionRequest object which holds the details of a given question
   * This will call the createQuestion method in QuestionService which would facilitate the creation
   * of the given question provided the user has provided valid authorization.
   */
  @RequestMapping(method = RequestMethod.POST, path = "/question/create",
      consumes = MediaType.APPLICATION_JSON_UTF8_VALUE,
      produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
  public ResponseEntity<QuestionResponse> createQuestion(@RequestHeader("authorization")
  final String authorization, final QuestionRequest questionRequest)
      throws AuthorizationFailedException {
    /* Prepare the questionEntity object with the data fed by the user */
    QuestionEntity questionEntity = new QuestionEntity();
    questionEntity.setUuid(UUID.randomUUID().toString());
    questionEntity.setContent(questionRequest.getContent());

    /* Call the createQuestion in service to persist the new question record in database*/
    QuestionEntity createdQuestion = questionService.createQuestion(questionEntity, authorization);

    /* Once the question is persisted, prepare the response with UUID and a status message */
    QuestionResponse questionResponse = new QuestionResponse();
    questionResponse.id(createdQuestion.getUuid()).status("QUESTION CREATED");
    /* return the response object back to the client*/
    return new ResponseEntity<QuestionResponse>(questionResponse, HttpStatus.CREATED);
  }

}
