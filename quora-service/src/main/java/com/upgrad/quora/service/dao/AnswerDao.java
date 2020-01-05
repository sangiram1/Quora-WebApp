package com.upgrad.quora.service.dao;

import com.upgrad.quora.service.entity.AnswerEntity;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

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

}
