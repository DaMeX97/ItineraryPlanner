package com.dametto.itinerary_server.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;
import java.io.Serializable;

@Entity(name="tourist_attraction")
@Table(name="tourist_attraction")
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class TouristAttractionEntity implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "tourist_attraction_id_seq")
    @SequenceGenerator(name = "tourist_attraction_id_seq", sequenceName ="tourist_attraction_id_seq", allocationSize = 1)
    @ColumnDefault("nextval('tourist_attraction_id_seq')")
    @Column(name = "id")
    private Integer id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "wikidata_url", nullable = false, unique = true)
    private String wikidataUrl;

    @Column(name = "description", nullable = false)
    private String description;

    @Column(name = "image_url")
    private String imageUrl;

    @Column(name = "lat", nullable = false)
    private Double latitude;

    @Column(name = "lon", nullable = false)
    private Double longitude;

    @Column(name = "visits", nullable = false)
    private Long visits;

    @Column(name = "visit_duration_minutes", nullable = false)
    private Integer visitsDurationMinutes;
}
