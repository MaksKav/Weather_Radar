package com.maxkavun.entity;

import jakarta.persistence.*;
import java.io.Serializable;

@MappedSuperclass
public abstract class AbstractEntity<ID extends Serializable> implements Serializable {
    public abstract ID getId();
    public abstract void setId(ID id);
}
