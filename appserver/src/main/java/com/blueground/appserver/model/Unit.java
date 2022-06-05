package com.blueground.appserver.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Getter
@Setter
@Entity
@Table(name = "units")
public class Unit {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @Column(name = "title")
    private String title;

    @Column(name = "image")
    private String image;
    @Column(name = "description")
    private String description;

    @Column(name = "cancellation")
    private String cancellation;

    @Column(name = "review")
    private Integer review;

    @Column(name = "price")
    private Float price;

    @Column(name = "published")
    private boolean published;

    @Override
    public String toString() {
        return "Unit{" + "id=" + id + ", title=" + title + ", description=" + description + ", published=" + published + '}';
    }

}