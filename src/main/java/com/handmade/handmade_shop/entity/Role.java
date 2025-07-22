package com.handmade.handmade_shop.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Table(name = "roles")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Role {

    @Id
    @GeneratedValue
    @Column(name = "role_id")
    private UUID roleId;

    @Column(name = "name", unique = true, nullable = false)
    private String name;

}
