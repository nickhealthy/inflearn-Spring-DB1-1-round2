package hello.jdbc.repository;

import hello.jdbc.domain.Member;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import javax.sql.DataSource;

/**
 * JDBCTemplate 사용 - 템플릿 콜백 패턴 적용(반복 처리)
 * - 커넥션 조회
 * - 커넥션 동기화
 * - PreparedStatement 생성 및 파라미터 바인딩
 * - 쿼리 실행
 * - 결과 바인딩(ResultSet)
 * - 예외 발생시 스프링 예외 변환기 실행, 리소스 종료
 */
@Slf4j
public class MemberRepositoryV5 implements MemberRepository {

    private final JdbcTemplate jdbcTemplate;

    public MemberRepositoryV5(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public Member save(Member member) {
        String sql = "insert into member(member_id, money) values (?, ?)";
        jdbcTemplate.update(sql, member.getMemberId(), member.getMoney());
        return member;
    }

    public Member findById(String memberId) {
        String sql = "select * from member where member_id = ?";
        return jdbcTemplate.queryForObject(sql, memberRowMapper(), memberId);
    }

    public void update(String memberId, int money) {
        String sql = "update member set money=? where member_id=?";
        jdbcTemplate.update(sql, money, memberId);
    }

    public void delete(String memberId) {
        String sql = "delete from member where member_id=?";
        jdbcTemplate.update(sql, memberId);
    }

    private RowMapper<Member> memberRowMapper() {
        return (rs, rowNum) -> {
            Member member = new Member();
            member.setMemberId(rs.getString("member_id"));
            member.setMoney(rs.getInt("money"));
            return member;
        };
    }
}
