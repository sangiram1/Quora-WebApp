/* Created by Ankit as part of developing DAO classes for implementing given functionalities
 * This DAO has different method to handle different interactions with Database.
 * Below is list of all the methods and their respective functionality:
 * 1. createAnswer() method would persist the answer to the database
 * 2. getAnswer() method would fetch the details of a given answer id
 * 4. updateAnswer() method would update the given answer
 * 5. deleteAnswer() method would delete the given answer
 * 6. getAllAnswersToQuestion() method would fetch the details of all the answer per question
 */

package com.upgrad.quora.service.dao;

import com.upgrad.quora.service.entity.AnswerEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import sun.util.locale.StringTokenIterator;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
public class AnswerDao {
    /* Define the context for providing instance of EntityManager class */
    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private QuestionDao questionDao;
    /* createAnswer() method would take the answerEntity object as input and persist it to database
     *  It would return the persisted answerEntity object back to the service layer with ID details
     */
    public AnswerEntity createAnswer(AnswerEntity answerEntity) {
        /* Save the answerEntity to the database */
        entityManager.persist(answerEntity);
        return answerEntity;
    }

    /* getAnswer() method would fetch the details of a given answer
     * It uses the NamedQuery getAnswerByUuid and return details of the given answer
     * answerEntity is returned to the service layer if the answer exists else null is returned
     */
    public AnswerEntity getAnswer(final String answerId) {
        try {
            /* Get the details of given answer from the database using the given query */
            AnswerEntity answerEntity = entityManager.createNamedQuery("getAnswerByUuid",
                    AnswerEntity.class).setParameter("uuid", answerId).getSingleResult();
            return answerEntity;
        } catch (NoResultException nre) {
            /* Return null if answer doesn't exist */
            return null;
        }
    }

    /* updateAnswer() method would update the given answer
     * This method would take the updated answerEntity object as input and merge it to database
     * The merged entity object is returned to the service layer.
     */
    public AnswerEntity updateAnswer(AnswerEntity answerToBeUpdated) {
        /* Merge the entity object to the database */
        entityManager.merge(answerToBeUpdated);
        return answerToBeUpdated;
    }

    /* deleteAnswer() method would delete the given answer from database
     * This method would take the answerEntity object as input
     * The deleted entity object is returned to the service layer if exists in database.
     */
    public AnswerEntity deleteAnswer(AnswerEntity answerEntity) {
        try {
            /* Delete the entity object from the database */
            entityManager.remove(answerEntity);
            return answerEntity;
        } catch (NoResultException nre) {
            /* Return null if answer doesn't exist */
            return null;
        }
    }

    /* getAllAnswersToQuestion() method would fetch the details of all given answers to particular question
     * It uses the NamedQuery getAllAnswersToQuestion and return details of all the given answers
     * List of answerEntity is returned to the service layer
     */
    public List<AnswerEntity> getAllAnswersToQuestion(String questionId) {
        /* Get List of all answers for given question's id from the database using the given query */
        List<AnswerEntity> answerEntities = entityManager
                .createNamedQuery("getAllAnswersToQuestion", AnswerEntity.class)
                .setParameter("question", questionDao.getQuestion(questionId)).getResultList();
        return answerEntities;
    }
}
