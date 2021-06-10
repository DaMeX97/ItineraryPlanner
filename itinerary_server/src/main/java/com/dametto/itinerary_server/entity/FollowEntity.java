package com.dametto.itinerary_server.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;

@Entity(name="follow")
@Table(name="follow")
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class FollowEntity implements Serializable {

    @EmbeddedId
    FollowKeyEntity id;

    @ManyToOne(fetch= FetchType.LAZY)
    @JoinColumn(name="sender", nullable=false)
    @MapsId("senderId")
    private UserEntity sender;

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="receiver", nullable=false)
    @MapsId("receiverId")
    private UserEntity receiver;

    @Column(name = "type", nullable = false)
    private String type;
}
