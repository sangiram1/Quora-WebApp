package com.upgrad.quora.service.business;

import com.upgrad.quora.service.dao.AnswerDao;
import com.upgrad.quora.service.dao.QuestionDao;
import com.upgrad.quora.service.dao.UserDao;
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

@Service
public class AnswerBusinessService {

    /* Autowire Dao and Service classes to facilitate the required operations on AnswerEntity */
    @Autowired
    private QuestionDao questionDao;

    @Autowired
    private UserDao userDao;

    @Autowired
    private AnswerDao answerDao;

    @Autowired
    private AuthorizationService authorizationService;

    @Transactional(propagation = Propagation.REQUIRED)
    public AnswerEntity createAnswer(AnswerEntity answerEntity, final String questionId,
                                     final String authorization) throws AuthorizationFailedException, InvalidQuestionException {

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

        /* Update the question, user and datetime on the answer and persist it to DB with the given details */
        answerEntity.setUser(userAuthToken.getUser());
        answerEntity.setDate(ZonedDateTime.now());
        answerEntity.setQuestion(questionEntity);

        return answerDao.createAnswer(answerEntity);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public AnswerEntity editAnswerContent(final String answerId, final String answerContent,
                                          final String authorization) throws AuthorizationFailedException, AnswerNotFoundException {

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
            throw new AuthorizationFailedException("ATHR-003", "Only the answer owner can edit the answer");
        }
    }

}
