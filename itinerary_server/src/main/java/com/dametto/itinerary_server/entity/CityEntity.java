package com.dametto.itinerary_server.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Entity(name = "city")
@Table(name="city")
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class CityEntity implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "city_id_seq")
    @SequenceGenerator(name = "city_id_seq", sequenceName ="city_id_seq", allocationSize = 1)
    @ColumnDefault("nextval('city_id_seq')")
    @Column(name = "id")
    private Integer id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "lat", nullable = false)
    private Double latitude;

    @Column(name = "lon", nullable = false)
    private Double longitude;

    @Column(name = "province_code", nullable = false)
    private String provinceCode;

    @OneToMany(mappedBy="city", cascade=CascadeType.ALL)
    private List<ItineraryEntity> itineraries;
}
