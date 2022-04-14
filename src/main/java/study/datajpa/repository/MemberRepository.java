package study.datajpa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import study.datajpa.dto.MemberDto;
import study.datajpa.entity.Member;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {

    // Query by method name (간단한 경우 자주 사용)
    List<Member> findByUsernameAndAgeGreaterThan(String username, int age);

    // @NamedQuery -> Member.findByUsername (자주 사용X, 로딩 시점에 쿼리 오류 잡아주지만 엔티티 코드가 더러워짐)
    List<Member> findByUsername(@Param("username") String username);

    // @Query (자주 사용, 익명 NamedQuery)
    @Query("select m from Member m where m.username = :username and m.age = :age")
    List<Member> findUser(@Param("username") String username, @Param("age") int age);

    @Query("select m.username from Member m")
    List<String> findUsernameList();

    @Query("select new study.datajpa.dto.MemberDto(m.id, m.username, t.name) from Member m join m.team t")
    List<MemberDto> findMemberDto();

    @Query("select m from Member m where m.username in :names")
    List<Member> findByNames(@Param("names") Collection<String> names);

    // 다양한 리턴 타입 (리스트, 단건, 옵셔널 등등 매우 다양)
    List<Member> findListByUsername(String username); // 데이터 없으면 빈 컬렉션
    Member findMemberByUsername(String username); // 데이터 없으면 null (스프링 데이터 JPA가 중간에서 예외를 잡아서 동작 방식 변경)
    Optional<Member> findOptionalByUsername(String username); // 단건 조회시 데이터가 2건 이상이면 예외
}
