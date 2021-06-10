package com.dametto.itinerary_server.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;
import java.util.Date;

@Entity(name = "comment")
@Table(name="comment")
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class CommentEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "comment_id_seq")
    @SequenceGenerator(name = "comment_id_seq", sequenceName ="comment_id_seq", allocationSize = 1)
    @ColumnDefault("nextval('comment_id_seq')")
    @Column(name = "id")
    private Integer id;

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="author_id", nullable=false)
    private UserEntity author;

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="post_id", nullable=false)
    private PostEntity post;

    @Column(name = "body", nullable = false)
    private String body;

    @Column(name = "created_at", nullable = false)
    @ColumnDefault("current_timestamp")
    private Date createdAt = new Date();
}
