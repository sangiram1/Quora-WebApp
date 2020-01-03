/* Created by Sangeeta as part of developing DAO classes for implementing given functionalities
 * This DAO has different method to handle different interactions with Database.
 * Below is list of all the methods and their respective functionality:
 * 1. createQuestion() method would persist the question to the database
 * 2. getAllQuestions() method would fetch the details of all the questions
 * 3. getQuestion() method would fetch the details of a given question
 * 4. updateQuestion() method would update the given question
 * 5. deleteQuestion() method would delete the given question
 * 6. getAllQuestionsByUser() method would fetch the details of all the questions per user
 */

package com.upgrad.quora.service.dao;

import com.upgrad.quora.service.entity.QuestionEntity;
import com.upgrad.quora.service.entity.UserEntity;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;

/* @Repository method helps the container to recognize the QuestionDao as a DAO class */
@Repository
public class QuestionDao {

  /* Define the context for providing instance of EntityManager class */
  @PersistenceContext
  private EntityManager entityManager;

  /* createQuestion() method would persist the question to the database
   * This method would take the questionEntity object as input and persist it to database
   * It would return the persisted questionEntity object back to the service layer with ID details
   */
  public QuestionEntity createQuestion(QuestionEntity questionEntity) {
    /* Save the questionEntity to the database */
    entityManager.persist(questionEntity);
    return questionEntity;
  }

  /* getAllQuestions() method would fetch the details of all the questions
   * It uses the NamedQuery getAllQuestions and return a list of all questions
   * The questionEntities list is returned to the service layer.
   */
  public List<QuestionEntity> getAllQuestions() {
    /* Get List of all questions from the database using the given query */
    List<QuestionEntity> questionEntities = entityManager
        .createNamedQuery("getAllQuestions", QuestionEntity.class).getResultList();
    return questionEntities;
  }
}