package com.dametto.itinerary_server.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity(name="itinerary")
@Table(name="itinerary")
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class ItineraryEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "itinerary_id_seq")
    @SequenceGenerator(name = "itinerary_id_seq", sequenceName ="itinerary_id_seq", allocationSize = 1)
    @ColumnDefault("nextval('itinerary_id_seq')")
    @Column(name = "id")
    private Integer id;

    @Column(name = "status", nullable = false)
    @ColumnDefault("DRAFT")
    private String status;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "hours_per_day", nullable = false)
    private Integer hoursPerDay;

    @Column(name = "break_minutes", nullable = false)
    private Integer breakMinutes;

    @Column(name = "start_day", nullable = false)
    private Date startDay;

    @Column(name = "public_visibility", nullable = false)
    @ColumnDefault("false")
    private Boolean publicVisibility = false;

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="user_id", nullable=false)
    private UserEntity user;

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name = "city_id", nullable = false)
    private CityEntity city;

    @OneToMany(mappedBy="itinerary", cascade=CascadeType.ALL)
    private List<ItineraryDayEntity> days;
}
