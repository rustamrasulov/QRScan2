package com.miirrr.qrscan.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.List;


@RequiredArgsConstructor
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
@Entity
@Table(name = "shop")
public class Shop extends BaseEntity {

    @JsonProperty("id")
    @NonNull
    @Column(name = "shop_corp_id")
    private String shopCorpId;

    @JsonProperty("name")
    @NonNull
    @Column(name = "name")
    private String name;

    @JsonProperty("inn")
    @NonNull
    @Column(name = "inn")
    private String inn;


    @JsonProperty("ip")
    @Column(name = "ip")
    private String ipName;

    @ManyToOne
    @NonNull
    @JoinColumn(name = "city_id", referencedColumnName = "id")
    private City city;

    @OneToMany(mappedBy = "shop", cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    List<Position> positions;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Shop shop = (Shop) o;

        if (!getShopCorpId().equals(shop.getShopCorpId())) return false;
        if (!getName().equals(shop.getName())) return false;
        return getCity().equals(shop.getCity());
    }

    @Override
    public int hashCode() {
        int result = getShopCorpId().hashCode();
        result = 31 * result + getName().hashCode();
        result = 31 * result + getCity().hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "Store{" +
                "id=" + id +
                ", storeId='" + shopCorpId + '\'' +
                ", name='" + name + '\'' +
                ", INN='" + inn + '\'' +
                ", IP='" + ipName + '\'' +
                ", " + city +
                '}';
    }
}
