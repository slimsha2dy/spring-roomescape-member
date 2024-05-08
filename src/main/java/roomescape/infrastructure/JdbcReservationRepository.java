package roomescape.infrastructure;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationRepository;
import roomescape.domain.ReservationTime;
import roomescape.domain.Theme;

@Repository
public class JdbcReservationRepository implements ReservationRepository {

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert jdbcInsert;
    private final RowMapper<Reservation> reservationRowMapper = (resultSet, rowNum) -> new Reservation(
            resultSet.getLong("reservation_id"),
            resultSet.getString("name"),
            LocalDate.parse(resultSet.getString("date")),
            new ReservationTime(
                    resultSet.getLong("time_id"),
                    LocalTime.parse(resultSet.getString("time_value"))
            ),
            new Theme(
                    resultSet.getLong("theme_id"),
                    resultSet.getString("theme_name"),
                    resultSet.getString("theme_description"),
                    resultSet.getString("theme_thumbnail")
            )
    );
    private final String basicSelectQuery = "SELECT " +
            "r.id AS reservation_id, " +
            "r.name, " +
            "r.date, " +
            "t.id AS time_id, " +
            "t.start_at AS time_value, " +
            "th.id AS theme_id, " +
            "th.name AS theme_name, " +
            "th.description AS theme_description, " +
            "th.thumbnail AS theme_thumbnail " +
            "FROM reservation AS r " +
            "INNER JOIN reservation_time AS t ON r.time_id = t.id " +
            "INNER JOIN theme AS th ON r.theme_id = th.id ";

    public JdbcReservationRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.jdbcInsert = new SimpleJdbcInsert(jdbcTemplate.getDataSource())
                .withTableName("reservation")
                .usingGeneratedKeyColumns("id");
    }

    @Override
    public List<Reservation> findAll() {
        List<Reservation> reservations = jdbcTemplate.query(this.basicSelectQuery, reservationRowMapper);

        return Collections.unmodifiableList(reservations);
    }

    @Override
    public Reservation findById(Long id) {
        String sql = basicSelectQuery + "WHERE reservation_id = ?";
        Reservation reservation = jdbcTemplate.queryForObject(sql, reservationRowMapper, id);
        if (reservation == null) {
            throw new NoSuchElementException("존재하지 않는 아아디입니다.");
        }

        return reservation;
    }

    @Override
    public Reservation save(Reservation reservation) {
        Map<String, Object> params = Map.of(
                "name", reservation.getName(),
                "date", reservation.getDate(),
                "time_id", reservation.getTime().getId(),
                "theme_id", reservation.getTheme().getId()
        );
        Long id = jdbcInsert.executeAndReturnKey(params).longValue();

        return new Reservation(id, reservation.getName(), reservation.getDate(), reservation.getTime(),
                reservation.getTheme());
    }

    @Override
    public void deleteById(Long id) {
        String sql = "DELETE FROM reservation WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }

    @Override
    public boolean existByReservationTimeId(Long id) {
        String sql = basicSelectQuery + "WHERE time_id = ?";
        List<Reservation> reservations = jdbcTemplate.query(sql, reservationRowMapper, id);

        return !reservations.isEmpty();
    }

    @Override
    public boolean existByThemeId(Long id) {
        String sql = basicSelectQuery + "WHERE theme_id = ?";
        List<Reservation> reservations = jdbcTemplate.query(sql, reservationRowMapper, id);

        return !reservations.isEmpty();
    }

    @Override
    public boolean existByDateAndTimeIdAndThemeId(LocalDate date, Long timeId, Long themeId) {
        String sql = basicSelectQuery + "WHERE r.date = ? AND t.id = ? AND th.id = ?";
        List<Reservation> reservations = jdbcTemplate.query(sql, reservationRowMapper, date, timeId, themeId);

        return !reservations.isEmpty();
    }
}
