/* Created by Sangeeta as part of Creating Entities for Quora Group Case Study
 * AnswerEntity is a java class which is mapped to answer table in database
 * It has the attributes - id, uuid, answer, date mapped to the respective columns in database
 * This entity holds the information regarding answers posted by users per question
 * It has a many to one relationship with users and question tables
 * Delete Cascade has also been defined for answer table w.r.t users and question tables
 * Necessary NamedQueries are defined to carry forward the required tasks
 */

package com.upgrad.quora.service.entity;

import java.io.Serializable;
import java.time.ZonedDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

/* AnswerEntity class to represent answer table in database */
@Entity
@Table(name = "answer")
@NamedQueries({
    @NamedQuery(name = "getAnswerByUuid", query = "select a from AnswerEntity a where "
        + "a.uuid = :uuid"),
    @NamedQuery(name = "getAllAnswersToQuestion", query = "select a from AnswerEntity a where "
        + "a.question = :question")
})
public class AnswerEntity implements Serializable {

    /* Attribute id corresponds to field ID - primary key in answer table */
    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /* Attribute uuid corresponds to UUID field in answer table
     * This field is defined as Not null. Hence, the @NotNull annotation is added to this attribute.
     * As per the database schema, the field length is defined at a max of 200 characters.
     */
    @Column(name = "UUID")
    @NotNull
    @Size(max = 200)
    private String uuid;

    /* Attribute answer corresponds to ANS field in answer table
     * This field is defined as Not null. Hence, the @NotNull annotation is added to this attribute.
     * As per the database schema, the field length is defined at a max of 255 characters.
     */
    @Column(name = "ANS")
    @NotNull
    @Size(max = 255)
    private String answer;

    /* Attribute date corresponds to DATE field in answer table
     * This field is defined as Not null. Hence, the @NotNull annotation is added to this attribute.
     * As per DB schema, the field is defined without timezone, hence used datatype - ZonedDateTime
     */
    @Column(name = "DATE")
    @NotNull
    private ZonedDateTime date;

    /* Attribute user corresponds to USER_ID field in answer table
     * This field is defined as foreign key. Hence, the @ManyToOne annotation is added to this
     * attribute.
     * USER_ID field is mapped as a foreign key which corresponds to primary key ID in users table.
     */
    @ManyToOne
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "USER_ID")
    private UserEntity user;

    /* Attribute question corresponds to QUESTION_ID field in answer table
     * This field is defined as foreign key. Hence, the @ManyToOne annotation is added to this
     * attribute.
     * Field QUESTION_ID is mapped as foreign key which corresponds to primary key ID in question
     * table.
     */
    @ManyToOne
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "QUESTION_ID")
    private QuestionEntity question;

    /* Getters & Setters for the given attributes */
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public ZonedDateTime getDate() {
        return date;
    }

    public void setDate(ZonedDateTime date) {
        this.date = date;
    }

    public UserEntity getUser() {
        return user;
    }

    public void setUser(UserEntity user) {
        this.user = user;
    }

    public QuestionEntity getQuestion() {
        return question;
    }

    public void setQuestion(QuestionEntity question) {
        this.question = question;
    }

    /* Overridden equals, hashCode, toString methods as per need */
    @Override
    public boolean equals(Object obj) {
        return new EqualsBuilder().append(this, obj).isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(this).hashCode();
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
    }
}
