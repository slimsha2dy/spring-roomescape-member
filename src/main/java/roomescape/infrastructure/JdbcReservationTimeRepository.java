package roomescape.infrastructure;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import roomescape.domain.ReservationTime;

@Repository
public class JdbcReservationTimeRepository {

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert jdbcInsert;
    private final RowMapper<ReservationTime> reservationTimeRowMapper = (resultSet, rowNum) -> new ReservationTime(
            resultSet.getLong("id"),
            LocalTime.parse(resultSet.getString("start_at")
            ));

    public JdbcReservationTimeRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.jdbcInsert = new SimpleJdbcInsert(jdbcTemplate.getDataSource())
                .withTableName("reservation_time")
                .usingGeneratedKeyColumns("id");
    }

    public List<ReservationTime> findAll() {
        String sql = "SELECT * FROM reservation_time";

        return jdbcTemplate.query(sql, reservationTimeRowMapper);
    }

    public ReservationTime findById(Long id) {
        String sql = "SELECT * FROM reservation_time WHERE id = ?";
        ReservationTime reservationTime = jdbcTemplate.queryForObject(sql, reservationTimeRowMapper, id);
        if (reservationTime == null) {
            throw new NoSuchElementException("존재하지 않는 아아디입니다.");
        }

        return reservationTime;
    }

    public List<ReservationTime> findByReservationDateAndThemeId(LocalDate date, Long themeId) {
        String sql = "SELECT " +
                "t.id, " +
                "t.start_at, " +
                "FROM reservation AS r " +
                "INNER JOIN reservation_time AS t ON r.time_id = t.id " +
                "WHERE r.date = ? AND r.theme_id = ?";

        return jdbcTemplate.query(sql, reservationTimeRowMapper, date, themeId);
    }

    public ReservationTime save(ReservationTime reservationTime) {
        Map<String, Object> params = Map.of(
                "start_at", reservationTime.getStartAt()
        );
        Long id = jdbcInsert.executeAndReturnKey(params).longValue();

        return new ReservationTime(id, reservationTime.getStartAt());
    }

    public void deleteById(Long id) {
        String sql = "DELETE FROM reservation_time WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }
}
