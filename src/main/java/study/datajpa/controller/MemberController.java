package study.datajpa.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import study.datajpa.dto.MemberDto;
import study.datajpa.entity.Member;
import study.datajpa.repository.MemberRepository;

import javax.annotation.PostConstruct;

@RestController
@RequiredArgsConstructor
public class MemberController {

    private final MemberRepository memberRepository;

    @GetMapping("/members/{id}")
    public String findMember(@PathVariable("id") Long id){
        Member member = memberRepository.findById(id).get();
        return member.getUsername();
    }

    /**
     * 도메인 클래스 컨버터 기능
     * (스프링 데이터 JPA가 기본으로 해주는 기능임)
     * 사실 권장하진 않음 되게 간단한 경우만 가능
     * (외부에 PK공개되는 경우도 많지 않고 쿼리가 그렇게 간단하게 돌아가는 경우도 없음)
     */
    @GetMapping("/members2/{id}")
    public String findMember2(@PathVariable("id") Member member) {
        return member.getUsername();
    }
    
    @GetMapping("/members")
    public Page<MemberDto> list(@PageableDefault(size=5) Pageable pageable) {
        //리포지토리 기본메서드에 pageable 파라미터 넘기기만 하면
        //페이징 처리됨
        //꼭 그게아니라 @Query로 정의한 메서드도 그냥 마지막 파라미터로 넘겨주면 됨
        //절대 엔티티 자체를 반환하면 안됨!!
        return memberRepository.findAll(pageable)
                .map(MemberDto::new);
    }

    //의존성 주입이 끝난 후 바로 실행되는 로직
    //@PostConstruct
    public void init() {
        for(int i =0; i<100; i++){
            memberRepository.save(new Member("user" + i, i));
        }
    }
}
