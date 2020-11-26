package study.datajpa.repository;

/**
 * 이렇게 인터페이스만 정의해두면
 * 스프링 데이터 jpa가 
 * 구현클래스는 프록시 기술을 가지고 만듦
 */
public interface UsernameOnly {

    String getUsername();
}
