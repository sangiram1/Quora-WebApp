/* Created by Sangeeta as part of Creating Entities for Quora Group Case Study
 * QuestionEntity is a java class which is mapped to question table in database
 * It has the attributes - id, uuid, content, date mapped to the respective columns in database
 * This entity holds the details about the questions posted
 * It has a many to one relationship with users table
 * Delete Cascade has also been defined for question table w.r.t users table
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

/* QuestionEntity class to represent question table in database */
@Entity
@Table(name = "question")
@NamedQueries({
    @NamedQuery(name = "getAllQuestions", query = "select q from QuestionEntity q"),
    @NamedQuery(name = "getQuestionByQuestionId", query = "select q from QuestionEntity q where "
        + "q.uuid=:uuid"),
    @NamedQuery(name = "getAllQuestionsByUser", query = "select q from QuestionEntity q where "
        + "q.user=:userId")
})
public class QuestionEntity implements Serializable{

    /* Attribute id corresponds to field ID - primary key in question table */
    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /* Attribute uuid corresponds to UUID field in question table
     * This field is defined as Not null. Hence, the @NotNull annotation is added to this attribute.
     * As per the database schema, the field length is defined at a max of 200 characters.
     */
    @Column(name = "UUID")
    @NotNull
    @Size(max = 200)
    private String uuid;

    /* Attribute content corresponds to CONTENT field in question table
     * This field is defined as Not null. Hence, the @NotNull annotation is added to this attribute.
     * As per the database schema, the field length is defined at a max of 500 characters.
     */
    @Column(name = "CONTENT")
    @NotNull
    @Size(max = 500)
    private String content;

    /* Attribute date corresponds to DATE field in question table
     * This field is defined as Not null. Hence, the @NotNull annotation is added to this attribute.
     * As per DB schema, the field is defined without timezone, hence used datatype - ZonedDateTime
     */
    @Column(name = "DATE")
    @NotNull
    private ZonedDateTime date;

    /* Attribute user corresponds to USER_ID field in question table
     * This field is defined as foreign key. Hence, the @ManyToOne annotation is added to this
     * attribute.
     * USER_ID field is mapped as a foreign key which corresponds to primary key ID in users table.
     */
    @ManyToOne
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "USER_ID")
    private UserEntity user;

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

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
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
