package com.miirrr.qrscan.entities;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.time.LocalDateTime;


@AllArgsConstructor
@RequiredArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
@Entity
@Table(name = "position")
public class Position extends BaseEntity{

    @Column(name = "qr_code", unique = true)
    @NonNull
    private String name;

    @ManyToOne
    @JoinColumn(name = "shop_id", referencedColumnName = "id")
    private Shop shop;

    @Column(name = "date")
    @NonNull
    private LocalDateTime date;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Position position = (Position) o;

        if (getId() != null ? !getId().equals(position.getId()) : position.getId() != null) return false;
        return getShop() != null ? getShop().equals(position.getShop()) : position.getShop() == null;
    }

    @Override
    public int hashCode() {
        int result = getId() != null ? getId().hashCode() : 0;
        result = 31 * result + (getShop() != null ? getShop().hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Position{" +
                "name='" + name + '\'' +
                ", shop=" + shop +
                ", date=" + date +
                ", id=" + id +
                '}';
    }
}
