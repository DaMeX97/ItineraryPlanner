package com.dametto.itinerary_server.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import javax.persistence.*;
import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

@Entity(name="user")
@Table(name="user")
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class UserEntity implements Serializable, UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "user_id_seq")
    @SequenceGenerator(name = "user_id_seq", sequenceName ="user_id_seq", allocationSize = 1)
    @ColumnDefault("nextval('user_id_seq')")
    @Column(name = "id")
    private Integer id;

    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "first_name", nullable = false)
    private String firstName;

    @Column(name = "last_name", nullable = false)
    private String lastName;

    @OneToMany(mappedBy="user", cascade=CascadeType.ALL)
    private List<ItineraryEntity> itineraries;

    @OneToMany(mappedBy="author", cascade=CascadeType.ALL)
    private List<PostEntity> posts;

    @OneToMany(mappedBy="author", cascade=CascadeType.ALL)
    private List<CommentEntity> comments;

    @OneToMany(mappedBy="sender", cascade=CascadeType.ALL)
    // quelli che io seguo
    private List<FollowEntity> following;

    @OneToMany(mappedBy="receiver", cascade=CascadeType.ALL)
    // quelli che mi seguono
    private List<FollowEntity> followers;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    public String getUsername() {
        return this.getEmail();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        UserEntity user = (UserEntity) o;
        return Objects.equals(id, user.id);
    }
}
