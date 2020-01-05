/* Created by Ankit as part of developing Service classes for implementing given functionalities
 * This service has different method to handle different business logic.
 * Below is list of all the methods and their respective functionality:
 * 1. createAnswer() method would facilitate the creation of a answer in database
 * 2. editAnswerContent() method would facilitate the update a given answer
 * 3. deleteAnswer() method would facilitate the deletion of a given answer.
 * 4. getAllAnswersToQuestion() method would furnish the details of all the answers per question
 */

package com.upgrad.quora.service.business;

import com.upgrad.quora.service.dao.AnswerDao;
import com.upgrad.quora.service.dao.QuestionDao;
import com.upgrad.quora.service.entity.AnswerEntity;
import com.upgrad.quora.service.entity.QuestionEntity;
import com.upgrad.quora.service.entity.UserAuthEntity;
import com.upgrad.quora.service.exception.AnswerNotFoundException;
import com.upgrad.quora.service.exception.AuthorizationFailedException;
import com.upgrad.quora.service.exception.InvalidQuestionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZonedDateTime;
import java.util.List;

@Service
public class AnswerBusinessService {

    /* Autowire Dao and Service classes to facilitate the required operations on AnswerEntity */
    @Autowired
    private QuestionDao questionDao;

    @Autowired
    private AnswerDao answerDao;

    @Autowired
    private AuthorizationService authorizationService;

    /* createAnswer() method would facilitate the creation of a answer for given question in database
     * This method would take three inputs : the authorization string for user authorization
     * and a answerEntity object which holds the details of a answer to be persisted
     * and a string of questionId for which answer has to be created
     * It would return the persisted object back to the calling controller.
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public AnswerEntity createAnswer(AnswerEntity answerEntity, final String questionId,
                                     final String authorization)
            throws AuthorizationFailedException, InvalidQuestionException {
        /*
         * Get the question entity from given question id or throw exception if does not exist
         */
        QuestionEntity questionEntity = questionDao.getQuestion(questionId);

        if (questionEntity == null) {
            throw new InvalidQuestionException("QUES-001", "The question entered is invalid");
        }

        /* Check if the authorization/accessToken provided is valid or not. It will check the below:
         *  1.1. User has provided valid access token
         *  1.2. User has not signed out.
         */
        UserAuthEntity userAuthToken = authorizationService.checkAuthorization(authorization,
                "User is signed out.Sign in first to post an answer");

        /* Update the details as user, question and datetime on the answer entity and persist it to
         * DB with the given details
         */
        answerEntity.setUser(userAuthToken.getUser());
        answerEntity.setDate(ZonedDateTime.now());
        answerEntity.setQuestion(questionEntity);

        return answerDao.createAnswer(answerEntity);
    }

    /* editAnswerContent() method would facilitate the update of a answer in database
     * This method would take three inputs : the authorization string for user authorization
     * and a answerContent string which holds updated answer
     * and a answerId string for which answer has to be updated
     * It would return the persisted object back to the calling controller.
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public AnswerEntity editAnswerContent(final String answerId, final String answerContent,
                                          final String authorization)
            throws AuthorizationFailedException, AnswerNotFoundException {

        /* Check if the authorization/accessToken provided is valid or not. It will check the below:
         *  1.1. User has provided valid access token
         *  1.2. User has not signed out.
         */
        UserAuthEntity userAuthToken = authorizationService.checkAuthorization(authorization,
                "User is signed out.Sign in first to edit an answer");

        /* Check if the given answer is valid. Throw exception if the answer doesn't exist */
        AnswerEntity answerToBeUpdated = answerDao.getAnswer(answerId);
        if (answerToBeUpdated == null) {
            throw new AnswerNotFoundException("ANS-001", "Entered answer uuid does not exist");
        }

        /* Check if the logged-in user and the owner of the answer is same */
        final String answerOwner = answerToBeUpdated.getUser().getUuid();
        final String loggedInUser = userAuthToken.getUser().getUuid();
        /* Merge the changes to the answer if the owner and logged in user are the same
         * Else, throw exception that only owner of the answer can edit
         */
        if (answerOwner.equals(loggedInUser)) {
            answerToBeUpdated.setAnswer(answerContent);
            /* Return the updated answerEntity object back to the calling controller */
            return answerDao.updateAnswer(answerToBeUpdated);
        } else {
            throw new AuthorizationFailedException("ATHR-003",
                    "Only the answer owner can edit the answer");
        }
    }

    /* deleteAnswer() method would facilitate the deletion of a answer in database
     * This method would take two inputs : the authorization string for user authorization
     * and a answerId string of which answer data has to be deleted
     * It would return the deleted object data back to the calling controller.
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public AnswerEntity deleteAnswer(final String answerId, final String authorization)
            throws AuthorizationFailedException, AnswerNotFoundException {

        /* Check if the authorization/accessToken provided is valid or not. It will check the below:
         *  1.1. User has provided valid access token
         *  1.2. User has not signed out.
         */
        UserAuthEntity userAuthToken = authorizationService.checkAuthorization(authorization,
                "User is signed out.Sign in first to delete an answer");

        /* Check if the given answer is valid. Throw exception if the answer doesn't exist */
        AnswerEntity answerToBeDeleted = answerDao.getAnswer(answerId);
        if (answerToBeDeleted == null) {
            throw new AnswerNotFoundException("ANS-001", "Entered answer uuid does not exist");
        }

        /* Check if the logged-in user and the owner of the answer is same or admin */
        final String answerOwner = answerToBeDeleted.getUser().getUuid();
        final String loggedInUser = userAuthToken.getUser().getUuid();
        /* Delete the answer if the owner and logged in user are the same or logged in user is admin
         * Else, throw exception that only owner of the answer or admin can delete
         */
        if (answerOwner.equals(loggedInUser) || userAuthToken.getUser().getRole().equals("admin")) {
            /* Return the deleted answerEntity object back to the calling controller */
            return answerDao.deleteAnswer(answerToBeDeleted);
        } else {
            throw new AuthorizationFailedException("ATHR-003",
                    "Only the answer owner or admin can delete the answer");
        }
    }

    /* getAllAnswersToQuestion() method would facilitate the fetch all the answers for a question in database
     * This method would take two inputs : the authorization string for user authorization
     * and a questionId string of which answers have to be fetched
     * It would return the list of answer entity objects back to the calling controller.
     */
    public List<AnswerEntity> getAllAnswersToQuestion(final String authorization, final String questionId)
            throws AuthorizationFailedException, InvalidQuestionException {

        /*
         * Get the question entity from given question id or throw exception if does not exist
         */
        QuestionEntity questionEntity = questionDao.getQuestion(questionId);

        if (questionEntity == null) {
            throw new InvalidQuestionException("QUES-001",
                    "The question with entered uuid whose details are to be seen does not exist");
        }

        /* Check if the authorization/accessToken provided is valid or not. It will check the below:
         *  1.1. User has provided valid access token
         *  1.2. User has not signed out.
         */
        UserAuthEntity userAuthToken = authorizationService.checkAuthorization(authorization,
                "User is signed out.Sign in first to get the answers");

        /* Get the list of all the answers and return the same to the calling controller */
        return answerDao.getAllAnswersToQuestion(questionId);
    }
}
