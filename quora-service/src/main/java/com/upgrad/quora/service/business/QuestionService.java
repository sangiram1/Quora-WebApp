/* Created by Sangeeta as part of developing Service classes for implementing given functionalities
 * This service has different method to handle different business logic.
 * Below is list of all the methods and their respective functionality:
 * 1. createQuestion() method would facilitate the creation of a question in database
 * 2. getAllQuestions() method would furnish the details of all the questions
 * 3. editQuestionContent() method would facilitate the update of a given question
 * 4. deleteQuestion() method would facilitate the deletion of a given question.
 * 5. getAllQuestionsByUser() method serves would furnish the details of all the questions per user
 */

package com.upgrad.quora.service.business;

import com.upgrad.quora.service.dao.QuestionDao;
import com.upgrad.quora.service.dao.UserDao;
import com.upgrad.quora.service.entity.QuestionEntity;
import com.upgrad.quora.service.entity.UserAuthEntity;
import com.upgrad.quora.service.entity.UserEntity;
import com.upgrad.quora.service.exception.AuthorizationFailedException;
import com.upgrad.quora.service.exception.InvalidQuestionException;
import com.upgrad.quora.service.exception.UserNotFoundException;
import java.time.ZonedDateTime;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/* @Service Annotation would help the container to recognize QuestionService as a service class */
@Service
public class QuestionService {

  /* Autowire Dao and Service classes to facilitate the required operations on QuestionEntity */
  @Autowired
  private QuestionDao questionDao;

  @Autowired
  private UserDao userDao;

  @Autowired
  private AuthorizationService authorizationService;

  /* createQuestion() method would facilitate the creation of a question in database
   * This method would take two inputs : the authorization string for user authorization
   * and a QuestionEntity object which holds the details of a question to be persisted
   * Firstly, the authorization is checked using the AuthorizationService and then,
   * The details of the question will be persisted to database if authorization holds good
   * It would return the persisted object back to the calling controller.
   */
  @Transactional(propagation = Propagation.REQUIRED)
  public QuestionEntity createQuestion(QuestionEntity questionEntity,
      final String authorization) throws AuthorizationFailedException {

    /* Check if the authorization/accessToken provided is valid or not. It will check the below:
     *  1.1. User has provided valid access token
     *  1.2. User has not signed out.
     */
    UserAuthEntity userAuthToken = authorizationService.checkAuthorization(authorization,
        "User is signed out.Sign in first to post a question");

    /* Update the user and datetime on the question and persist it to DB with the given details */
    questionEntity.setUser(userAuthToken.getUser());
    questionEntity.setDate(ZonedDateTime.now());
    QuestionEntity createdQuestion = questionDao.createQuestion(questionEntity);
    /* Return the persisted question details back to controller */
    return createdQuestion;
  }

  /* getAllQuestions() method would furnish the details of all the questions
   * This method would take the authorization string as input for user authorization
   * If the authorization provided is valid, then the getAllQuestions method would furnish the
   * details of all the questions from the database.
   */
  public List<QuestionEntity> getAllQuestions(final String authorization)
      throws AuthorizationFailedException {

    /* Check if the authorization/accessToken provided is valid or not. It will check the below:
     *  1.1. User has provided valid access token
     *  1.2. User has not signed out.
     */
    UserAuthEntity userAuthToken = authorizationService.checkAuthorization(authorization,
        "User is signed out.Sign in first to get all questions");

    /* Get the list of all the questions and return the same to the calling controller */
    List<QuestionEntity> questionEntities = questionDao.getAllQuestions();
    return questionEntities;
  }

}