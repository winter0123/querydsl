package study.querydsl.entity;

import jakarta.persistence.*;
import lombok.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString(of = {"id", "name"})
public class Team {
    @Id
    @GeneratedValue
    @Column(name = "team_id")
    private Long id;
    private String name;

    @OneToMany(mappedBy = "team")
    List<Member> members = new ArrayList<>();

    @OneToOne
    @JoinColumn(name = "member_id")
    Member leader;

    public Team(String name) {
        this.name = name;
    }

    public Team(Long id){
        this.id = id;
    }
}