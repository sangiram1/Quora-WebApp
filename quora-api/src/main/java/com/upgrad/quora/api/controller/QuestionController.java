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

  @RequestMapping(method = RequestMethod.GET, path = "/question/all",
      produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
  public ResponseEntity<List<QuestionDetailsResponse>> getAllQuestions(@RequestHeader("authorization")
  final String authorization) throws AuthorizationFailedException {
    /* Get the list of all the questions from database if the authorization holds good. */
    List<QuestionEntity> questionEntities = questionService.getAllQuestions(authorization);

    /* Prepare the response with the required details from database and create a response list */
    List<QuestionDetailsResponse> questionResponseList = new LinkedList<QuestionDetailsResponse>();
    for (QuestionEntity question: questionEntities) {
      QuestionDetailsResponse questionDetailsResponse = new QuestionDetailsResponse();
      questionDetailsResponse.id(question.getUuid()).content(question.getContent());
      questionResponseList.add(questionDetailsResponse);
    }
    /* Return the details of questions in the form of responseList and a Httpstatus.OK to client */
    return new ResponseEntity<List<QuestionDetailsResponse>>(questionResponseList, HttpStatus.OK);
  }

  /* editQuestionContent() represents an endpoint to serve /question/edit/{questionId} request
   * This method would take three inpits: the authorization string from the Request Header,
   * the questionId of the question to be edited
   * and the questionRequest holding the updated details of the question
   * If the authorization and the UUID of the question is valid, then the method would update the
   * details of the given question and persist it to the database.
   */
  @RequestMapping(method = RequestMethod.PUT, path = "/question/edit/{questionId}",
      consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
  public ResponseEntity<QuestionEditResponse> editQuestionContent(@RequestHeader("authorization")
  final String authorization, @PathVariable("questionId") final String questionId,
      final QuestionEditRequest questionEditRequest)
      throws AuthorizationFailedException, InvalidQuestionException {
    /* Call the editQuestionContent from service to update the question with given content */
    String questionContent = questionEditRequest.getContent();
    QuestionEntity updatedQuestion = questionService.editQuestionContent(questionId,
        questionContent, authorization);

    /* Once the question is updated, prepare the response with UUID and a status message */
    QuestionEditResponse questionEditResponse = new QuestionEditResponse();
    questionEditResponse.id(updatedQuestion.getUuid()).status("QUESTION EDITED");
    /* return the response object back to the client*/
    return new ResponseEntity<QuestionEditResponse>(questionEditResponse, HttpStatus.OK);
  }

  /* Upma to add the remaining two endpoints - deleteQuestion() and getAllQuestionsByUser() */
}
