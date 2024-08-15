package com.drinkeg.drinkeg.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class WineStore {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id")
    private Member owner;

    private String name;

    private String address;

    public WineStore updateOwner(Member owner) {
        this.owner = owner;
        return this;
    }

    public WineStore updateName(String name) {
        this.name = name;
        return this;
    }

    public WineStore updateAddress(String address) {
        this.address = address;
        return this;
    }
}
