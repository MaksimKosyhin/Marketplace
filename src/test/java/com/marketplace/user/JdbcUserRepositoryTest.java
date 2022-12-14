package com.marketplace.user;

import com.marketplace.repository.user.DbUser;
import com.marketplace.repository.user.UserRepository;
import com.marketplace.repository.user.UserRole;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class JdbcUserRepositoryTest {

    private final JdbcTemplate jdbcTemplate;
    private final UserRepository userRepository;

    private static PostgreSQLContainer container =
            (PostgreSQLContainer) new PostgreSQLContainer("postgres")
                    .withReuse(true);

    @Autowired
    JdbcUserRepositoryTest(JdbcTemplate jdbcTemplate, UserRepository userRepository) {
        this.jdbcTemplate = jdbcTemplate;
        this.userRepository = userRepository;
    }


    @DynamicPropertySource
    public static void overrideProps(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", container::getJdbcUrl);
        registry.add("spring.datasource.username", container::getUsername);
        registry.add("spring.datasource.password", container::getPassword);
    }

    @BeforeAll
    public static void setUp() {
        container.start();
    }

    @AfterEach
    void tearDown() {
        jdbcTemplate.update("TRUNCATE TABLE categories, users, shops CASCADE");
    }

    private void addUserForTest(DbUser dbUser) {
        String sql = "INSERT INTO users(username, password, role) " +
                "VALUES(?,?,CAST(? AS user_role))";
        jdbcTemplate.update(
                sql,
                dbUser.getUsername(),
                dbUser.getPassword(),
                dbUser.getRole().name()
        );
    }

    @Test
    void addsUser() {
        //given
        DbUser dbUser = new DbUser("Max", "12345", UserRole.ADMIN);

        //when
        userRepository.addUser(dbUser);

        //then
        assertThat(jdbcTemplate.queryForObject(
                "SELECT EXISTS(SELECT 1 FROM users WHERE username = ?)",
                Boolean.class,
                dbUser.getUsername()))
                .isTrue();
    }

    @Test
    void getUser() {
        //given
        DbUser dbUser = new DbUser("Max", "12345", UserRole.ADMIN);
        addUserForTest(dbUser);

        //then
        assertThat(userRepository.getUser(dbUser.getUsername()))
                .isEqualTo(dbUser);
    }
}