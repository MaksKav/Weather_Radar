package com.maxkavun.entity;

import jakarta.persistence.*;
import lombok.*;


import java.math.BigDecimal;

@Entity
@Table(name = "locations")
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Location extends AbstractEntity<Long> {

    @Column(name = "name" , nullable = false)
    private String locationName;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(precision = 9, scale = 6 , nullable = false)
    private BigDecimal latitude;

    @Column(precision = 9, scale = 6 , nullable = false)
    private BigDecimal longitude;

}
