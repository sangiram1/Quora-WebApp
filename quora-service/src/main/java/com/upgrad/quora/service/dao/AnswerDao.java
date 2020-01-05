package com.upgrad.quora.service.dao;

import com.upgrad.quora.service.entity.AnswerEntity;
import com.upgrad.quora.service.entity.QuestionEntity;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
public class AnswerDao {
    /* Define the context for providing instance of EntityManager class */
    @PersistenceContext
    private EntityManager entityManager;

    /* createAnswer() method would persist the answer to the database
     * This method would take the answerEntity object as input and persist it to database
     * It would return the persisted answerEntity object back to the service layer with ID details
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

    //method to Delete Answer in Database
    public AnswerEntity deleteAnswer(AnswerEntity answerEntity) {
        try {
            entityManager.remove(answerEntity);
            return answerEntity;
        } catch(NoResultException nre) {
            return null;
        }
    }

    public List<AnswerEntity> getAllAnswersToQuestion(String questionId) {
        /* Get List of all answers for given question's id from the database using the given query */
        List<AnswerEntity> answerEntities = entityManager
                .createNamedQuery("getAllAnswersToQuestion", AnswerEntity.class).getResultList();
        return answerEntities;
    }
}
