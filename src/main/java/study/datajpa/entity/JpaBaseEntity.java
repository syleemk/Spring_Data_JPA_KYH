package study.datajpa.entity;

import lombok.Getter;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import java.time.LocalDateTime;

//진짜 상속관계는 아니고 (서브타입 슈퍼타입 매핑 아님)
//속성을 밑에 테이블에 내려서 데이터만 같이 쓸 수 있게 해주는 것
@MappedSuperclass
@Getter
public class JpaBaseEntity {

    @Column(updatable = false) //변경되지 않도록 설정
    private LocalDateTime createdDate;
    private LocalDateTime updatedDate;

    //JPA가 제공하는 이벤트
    //persist가 되기 전에 발생하는 이벤트를 정의할때 사용
    @PrePersist
    public void perPersist() {
        LocalDateTime now = LocalDateTime.now();
        createdDate = now;
        //실제 쿼리할 때, update에 null이 있으면
        //값 처리하기 힘듦 그래서 그냥 넣어두는 것
        updatedDate = now;
    }

    //update하기 전에 호출됨
    @PreUpdate
    public void preUpdate() {
        updatedDate = LocalDateTime.now();
    }

}
