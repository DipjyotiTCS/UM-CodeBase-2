package com.example.usermanagement.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Getter
@Setter
@Entity
@Table(name = "designations")
public class Designation extends BaseEntity {

    @Column(nullable = false, unique = true, length = 120)
    private String title;

    @Column(length = 400)
    private String description;
}
