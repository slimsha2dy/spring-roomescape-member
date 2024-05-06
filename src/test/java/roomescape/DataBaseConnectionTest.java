package roomescape;

import static org.assertj.core.api.Assertions.assertThat;

import java.sql.Connection;
import java.sql.SQLException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@TestPropertySource(properties = {"spring.config.location = classpath:application-test.yml"})
class DataBaseConnectionTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @DisplayName("데이터베이스 연결")
    @Test
    void dataBaseConnection() {
        try (Connection connection = jdbcTemplate.getDataSource().getConnection()) {
            Assertions.assertAll(
                    () -> assertThat(connection).isNotNull(),
                    () -> assertThat(connection.getCatalog()).isEqualTo("TEST"),
                    () -> assertThat(
                            connection.getMetaData().getTables(null, null, "RESERVATION", null).next()).isTrue(),
                    () -> assertThat(
                            connection.getMetaData().getTables(null, null, "RESERVATION_TIME", null).next()).isTrue(),
                    () -> assertThat(
                            connection.getMetaData().getTables(null, null, "THEME", null).next()).isTrue()
            );
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
