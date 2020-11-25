package study.datajpa.entity;

import lombok.*;
import org.springframework.stereotype.Component;

import javax.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString(of = {"id", "username", "age"})
@NamedQuery(
        name ="Member.findByUsername", //아무거나 해도 되는데 관례상 entity명.메서드명 줌
        query = "select m from Member m where m.username = :username"
)
public class Member {

    @Id @GeneratedValue
    @Column(name="member_id")
    private Long id;

    private String username;

    private int age;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "team_id")
    private Team team;

//    /**
//     * JPA에서 엔티티는 기본 생성자 있어야함
//     * JPA구현체들이 프록시 객체같은 거 생성할 때
//     * 기본생성자있어야 가능
//     * private으로 막아놓으면 또 그게 막혀버릴 수 있으므로,
//     * 아무데서나 호출되지 않도록 protected 까지 설정
//     */
//    protected Member() {
//    }

    public Member(String username) {
        this.username = username;
    }

    public Member(String username, int age, Team team) {
        this.username = username;
        this.age = age;
        //parameter가 null이여도 무시하는 것으로 처리(어차피 공부니까)
        if (team != null) {
            this.team = team;
        }
    }

    public Member(String username, int age) {
        this.username = username;
        this.age = age;
    }

    /**
     * 연관관계 편의 메서드
     */
    public void changeTeam(Team team) {
        this.team = team;
        team.getMembers().add(this);
    }
}
