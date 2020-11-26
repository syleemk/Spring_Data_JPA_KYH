package study.datajpa.repository;

/**
 * 구체적인 생성자를 이용한 projection
 */
public class UsernameOnlyDto {

    private final String username;

    /**
     * 생성자를 이름을 이용한 프로젝션
     */
    public UsernameOnlyDto(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username; 
    }
}
