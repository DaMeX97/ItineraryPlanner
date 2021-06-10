package com.dametto.itinerary_server.entity;

import com.dametto.itinerary_server.jpa.TouristAttraction;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;
import java.util.List;
import java.util.Set;

@Entity(name="itinerary_day")
@Table(name="itinerary_day")
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class ItineraryDayEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "itinerary_day_id_seq")
    @SequenceGenerator(name = "itinerary_day_id_seq", sequenceName ="itinerary_day_id_seq", allocationSize = 1)
    @ColumnDefault("nextval('itinerary_day_id_seq')")
    @Column(name = "id")
    private Integer id;

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="itinerary_id", nullable=false)
    private ItineraryEntity itinerary;

    @ManyToMany
    @JoinTable(
            name = "itinerary_attraction_day",
            joinColumns = @JoinColumn(name = "itinerary_day_id"),
            inverseJoinColumns = @JoinColumn(name = "tourist_attraction_id"))
    private List<TouristAttractionEntity> attractions;
}
