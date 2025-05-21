package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import ru.yandex.practicum.filmorate.model.Rating;
import ru.yandex.practicum.filmorate.storage.mappers.RatingRowMapper;
import ru.yandex.practicum.filmorate.storage.rating.RatingDbStorage;

import java.util.Collection;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@JdbcTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Import({RatingDbStorage.class, RatingRowMapper.class})
public class RatingApplicationTests {
    private final RatingDbStorage ratingDbStorage;

    @Test
    public void testFindAllMpa() {
        Collection<Rating> allMpa = ratingDbStorage.findAll();
        assertThat(allMpa.size()).isEqualTo(5);
    }

    @Test
    public void testFindMpaById() {
        Optional<Rating> optionalMpa = ratingDbStorage.findById(1);
        assertThat(optionalMpa)
                .isPresent()
                .hasValueSatisfying(mpa ->
                        assertThat(mpa).hasFieldOrPropertyWithValue("name", "G")
                );
    }
}
