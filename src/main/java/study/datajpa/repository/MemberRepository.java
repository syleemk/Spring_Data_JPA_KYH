package study.datajpa.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import study.datajpa.dto.MemberDto;
import study.datajpa.entity.Member;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

//리포지토리 어노테이션 필요없음
//JpaRepository상속한 인터페이스 JPA가 알아서 componet scan함
public interface MemberRepository extends JpaRepository<Member, Long> {

    /**
     * 구현체 없이 인터페이스밖에 안만들었는데
     * 메소드 이름만 보고 JPA가 쿼리 메소드를 생성한다
     */
    List<Member> findByUsernameAndAgeGreaterThan(String username, int age);

    /**
     * 스프링 데이터 jpa로 NamedQuery 호출하는 방법
     * 엔티티에는 namedquery 정의되어있어야함
     * 하지만 실무에서 거의 사용안함 ..ㅋㅋ
     * 리포지토리에 바로 jpql 지정할 수 있는데 굳이 쓸일이없음;;
     */
    //네임드 쿼리 주석처리해도 실행됨
    //관례적으로 엔티티.메서드이름으로 먼저 네임드 쿼리를 먼저 찾고 없으면
    //메서드 쿼리를 생성해서 실행함
    //@Query(name = "Member.findByUsername")
    List<Member> findByUsername(@Param("username") String username);

    /**
     * 리포지토리 메서드에 jpql 바로 정의
     * 실무에서 많이 씀 (namedQuery처럼 애플리케이션 로딩시점에 오류 잡아낼 수 있음)
     * @Query로 정의한 jpql은 그냥 이름없는 네임드쿼리라고 보면 됨
     */
    @Query("select m from Member m where m.username = :username and m.age = :age")
    List<Member> findUser(@Param("username") String username, @Param("age") int age);

    /**
     * @Query로 값 조회
     */
    @Query("select m.username from Member m")
    List<String> findUsernameList();

    /**
     * @Query로 DTO 조회
     * DTO 조회시에는 new operation 써야함 (귀찮 )
     */
    @Query("select new study.datajpa.dto.MemberDto(m.id, m.username, t.name) from Member m join m.team t")
    List<MemberDto> findMemberDto();

    /**
     * 컬렉션 파라미터 바인딩
     * IN절 사용
     */
    @Query("select m from Member m where m.username in :names")
    List<Member> findByNames(@Param("names") Collection<String> names);


    /**
     * 반환타입을 유연하게 쓰는 것을 
     * 스프링 데이터 JPA가 지원해줌
     * find...By에서 ...은 아무거나 와도 상관없음 (설명해주는 이름이 오면 됨)
     */
    List<Member> findListByUsername(String username); //컬렉션
    Member findMemberByUsername(String username);//단건
    Optional<Member> findOptionalByUsername(String username);//단건 Optional

    /**
     * 스프링 데이터 JPA 사용한 페이징 처리
     * 반환타입을 Page로 받고
     * 파라미터로 Pageable 넘기면 됨 (Pageable 인터페이스 구현체인 PageRequest를 많이 사용)
     * 반환타입에 따라서 totalCount쿼리를 날릴지 안날릴지 결정이 됨
     * totalCount쿼리는 어찌됐든 DB데이터 전체를 카운트 하는 것이기 때문에
     * 데이터 많아질 수록 최적화가 필요함, 
     * 특히 left outer join한 경우 데이터 수는 조인하기 전과 같으므로 count쿼리까지 조인할 필요없음
     * 따라서 count쿼리 따로 분리하는 방법 제공함
     */
    @Query(value = "select m from Member m left join m.team t",
            countQuery = "select count(m.username) from Member m")
    Page<Member> findByAge(int age, Pageable pageable);
}
