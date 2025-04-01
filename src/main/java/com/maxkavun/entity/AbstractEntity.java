package com.maxkavun.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@MappedSuperclass
@Getter
@Setter
public abstract class AbstractEntity<ID extends Serializable> implements Serializable {

    @Id
    @Column
    private ID id;
}
