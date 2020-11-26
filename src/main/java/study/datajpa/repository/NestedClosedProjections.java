package study.datajpa.repository;

/**
 * 중첩구조 처리 가능
 */
public interface NestedClosedProjections {

    String getUsername();
    TeamInfo getTeam();

    interface TeamInfo {
        String getName();
    }
}
