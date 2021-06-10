package com.dametto.itinerary_server.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity(name = "post")
@Table(name="post")
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class PostEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "post_id_seq")
    @SequenceGenerator(name = "post_id_seq", sequenceName ="post_id_seq", allocationSize = 1)
    @ColumnDefault("nextval('post_id_seq')")
    @Column(name = "id")
    private Integer id;

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="author_id", nullable=false)
    private UserEntity author;

    @Column(name = "body", nullable = false)
    private String body;

    @Column(name = "created_at", nullable = false)
    @ColumnDefault("current_timestamp")
    private Date createdAt = new Date();

    @OneToMany(mappedBy="post", cascade=CascadeType.ALL)
    private List<CommentEntity> comments = new ArrayList<>();
}
