package com.hassan.auth.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;

@Data
@Entity
@Table(name = "emailauth")
public class EmailAuth extends AuditModel {
    @Id
    @GeneratedValue(generator = "question_generator")
    @SequenceGenerator(
            name = "question_generator",
            sequenceName = "question_sequence",
            initialValue = 1000
    )
    @ApiModelProperty(notes = "The database generated user ID")
    private Long id;
    @NotBlank
    @Column(unique = true)
    @ApiModelProperty(notes = "Email Id of the user", required = true)
    private String email;
    @NotBlank
    @ApiModelProperty(notes = "Password of the user", required = true)
    private String password;
}