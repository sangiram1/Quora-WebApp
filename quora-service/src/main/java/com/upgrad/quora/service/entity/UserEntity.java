/* Created by Sangeeta as part of Creating Entities for Quora Group Case Study
 * UserEntity is a java class which is mapped to users table in database
 * This entity holds the details about the users registered in Quora
 * It has the attributes - id, uuid, firstName, lastName, username, email, password, salt, country,
 * dob, aboutMe, role, contactNumber mapped to the respective columns in database
 * Necessary NamedQueries are defined to carry forward the required tasks
 */

package com.upgrad.quora.service.entity;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringExclude;
import org.apache.commons.lang3.builder.ToStringStyle;

/* UserEntity class to represent users table in database */
@Entity
@Table(name = "users")
@NamedQueries({
    @NamedQuery(name = "userByUsername", query = "select  u from UserEntity u where u.username "
        + "= :username"),
    @NamedQuery(name = "userByEmail", query = "select  u from UserEntity u where u.email = :email"),
    @NamedQuery(name = "userByUuid", query = "select u from UserEntity u where u.uuid = :uuid")
})

public class UserEntity implements Serializable{

    /* Attribute id corresponds to field ID - primary key in users table */
    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /* Attribute uuid corresponds to UUID field in users table
     * This field is defined as Not null. Hence, the @NotNull annotation is added to this attribute.
     * As per the database schema, the field length is defined at a max of 200 characters.
     */
    @Column(name = "UUID")
    @NotNull
    @Size(max = 200)
    private String uuid;

    /* Attribute firstName corresponds to FIRSTNAME field in users table
     * This field is defined as Not null. Hence, the @NotNull annotation is added to this attribute.
     * As per the database schema, the field length is defined at a max of 30 characters.
     */
    @Column(name = "FIRSTNAME")
    @NotNull
    @Size(max = 30)
    private String firstName;

    /* Attribute lastName corresponds to LASTNAME field in users table
     * This field is defined as Not null. Hence, the @NotNull annotation is added to this attribute.
     * As per the database schema, the field length is defined at a max of 30 characters.
     */
    @Column(name = "LASTNAME")
    @NotNull
    @Size(max = 30)
    private String lastName;

    /* Attribute username corresponds to USERNAME field in users table
     * This field is defined as Not null. Hence, the @NotNull annotation is added to this attribute.
     * As per the database schema, the field length is defined at a max of 30 characters.
     */
    @Column(name = "USERNAME")
    @NotNull
    @Size(max = 30)
    private String username;

    /* Attribute email corresponds to EMAIL field in users table
     * This field is defined as Not null. Hence, the @NotNull annotation is added to this attribute.
     * As per the database schema, the field length is defined at a max of 50 characters.
     */
    @Column(name = "EMAIL")
    @NotNull
    @Size(max = 50)
    private String email;

    /* Attribute password corresponds to PASSWORD field in users table
     * This field is defined as Not null. Hence, the @NotNull annotation is added to this attribute.
     * As per the database schema, the field length is defined at a max of 255 characters.
     * @ToStringExclude annotation will prevent the display of the password using toString() method.
     */
    @Column(name = "PASSWORD")
    @NotNull
    @ToStringExclude
    @Size(max = 255)
    private String password;

    /* Attribute salt corresponds to SALT field in users table
     * This field is defined as Not null. Hence, the @NotNull annotation is added to this attribute.
     * As per the database schema, the field length is defined at a max of 200 characters.
     */
    @Column(name = "SALT")
    @NotNull
    @Size(max = 200)
    private String salt;

    /* Attribute country corresponds to COUNTRY field in users table
     * As per the database schema, the field length is defined at a max of 30 characters.
     */
    @Column(name = "COUNTRY")
    @Size(max = 30)
    private String country;

    /* Attribute aboutMe corresponds to ABOUTME field in users table
     * As per the database schema, the field length is defined at a max of 50 characters.
     */
    @Column(name = "ABOUTME")
    @Size(max = 50)
    private String aboutMe;

    /* Attribute dob corresponds to DOB field in users table
     * As per the database schema, the field length is defined at a max of 30 characters.
     */
    @Column(name = "DOB")
    @Size(max = 30)
    private String dob;

    /* Attribute role corresponds to ROLE field in users table
     * As per the database schema, the field length is defined at a max of 30 characters.
     */
    @Column(name = "ROLE")
    @Size(max = 30)
    private String role;

    /* Attribute contactNumber corresponds to CONTACTNUMBER field in users table
     * As per the database schema, the field length is defined at a max of 30 characters.
     */
    @Column(name = "CONTACTNUMBER")
    @Size(max = 30)
    private String contactNumber;

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

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getAboutMe() {
        return aboutMe;
    }

    public void setAboutMe(String aboutMe) {
        this.aboutMe = aboutMe;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
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
