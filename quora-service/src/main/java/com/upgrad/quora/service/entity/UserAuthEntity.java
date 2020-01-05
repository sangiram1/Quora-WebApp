/* Created by Sangeeta as part of Creating Entities for Quora Group Case Study
 * UserAuthEntity is a java class which is mapped to user_auth table in database
 * This entity will hold the information regarding bearer user authentication
 * It has the attributes - id, uuid, access_token, expiresAt, loginAt, logoutAt mapped to the
 * respective columns in database
 * It has a many to one relationship with users table
 * Delete Cascade has also been defined for user_auth table w.r.t users table.
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

/* UserAuthEntity class to represent user_auth table in database */
@Entity
@Table(name = "user_auth")
@NamedQueries({
    @NamedQuery(name = "userAuthTokenByAccessToken", query = "select ut from UserAuthEntity ut "
        + "where ut.accessToken =:accessToken")
})
public class UserAuthEntity implements Serializable{

    /* Attribute id corresponds to field ID - primary key in user_auth table */
    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    /* Attribute uuid corresponds to UUID field in user_auth table
     * This field is defined as Not null. Hence, the @NotNull annotation is added to this attribute.
     * As per the database schema, the field length is defined at a max of 200 characters.
     */
    @Column(name = "UUID")
    @NotNull
    @Size(max = 200)
    private String uuid;

    /* Attribute user corresponds to USER_ID field in user_auth table
     * This field is defined as foreign key. Hence, the @ManyToOne annotation is added to this
     * attribute.
     * USER_ID field is mapped as a foreign key which corresponds to primary key ID in users table.
     */
    @ManyToOne
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "USER_ID")
    private UserEntity user;

    /* Attribute accessToken corresponds to ACCESS_TOKEN field in user_auth table
     * This field is defined as Not null. Hence, the @NotNull annotation is added to this attribute.
     * As per the database schema, the field length is defined at a max of 500 characters.
     */
    @Column(name = "ACCESS_TOKEN")
    @NotNull
    @Size(max = 500)
    private String accessToken;

    /* Attribute expiresAt corresponds to EXPIRES_AT field in user_auth table
     * This field is defined as Not null. Hence, the @NotNull annotation is added to this attribute.
     * As per DB schema, the field is defined without timezone, hence used datatype - ZonedDateTime
     */
    @Column(name = "EXPIRES_AT")
    @NotNull
    private ZonedDateTime expiresAt;

    /* Attribute loginAt corresponds to LOGIN_AT field in user_auth table
     * This field is defined as Not null. Hence, the @NotNull annotation is added to this attribute.
     * As per DB schema, the field is defined without timezone, hence used datatype - ZonedDateTime
     */
    @Column(name = "LOGIN_AT")
    @NotNull
    private ZonedDateTime loginAt;

    /* Attribute logoutAt corresponds to LOGOUT_AT field in user_auth table
     * This field is defined as Not null. Hence, the @NotNull annotation is added to this attribute.
     * As per DB schema, the field is defined without timezone, hence used datatype - ZonedDateTime
     */
    @Column(name = "LOGOUT_AT")
    private ZonedDateTime logoutAt;

    /* Getters & Setters for the given attributes */
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public UserEntity getUser() {
        return user;
    }

    public void setUser(UserEntity user) {
        this.user = user;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public ZonedDateTime getExpiresAt() {
        return expiresAt;
    }

    public void setExpiresAt(ZonedDateTime expiresAt) {
        this.expiresAt = expiresAt;
    }

    public ZonedDateTime getLoginAt() {
        return loginAt;
    }

    public void setLoginAt(ZonedDateTime loginAt) {
        this.loginAt = loginAt;
    }

    public ZonedDateTime getLogoutAt() {
        return logoutAt;
    }

    public void setLogoutAt(ZonedDateTime logoutAt) {
        this.logoutAt = logoutAt;
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
