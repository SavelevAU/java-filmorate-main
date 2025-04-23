package ru.yandex.practicum.filmorate;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import org.junit.jupiter.api.Test;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class FilmorateApplicationTests {

	@Test
	void testValidFilm() {
		Film film = new Film();
		film.setName("Inception");
		film.setDescription("A mind-bending thriller.");
		film.setReleaseDate(LocalDate.of(2010, 7, 16));
		film.setDuration(148);

		assertDoesNotThrow(() -> validateFilm(film));
	}

	@Test
	void testNameIsEmpty() {
		Film film = new Film();
		film.setName("");
		film.setDescription("A mind-bending thriller.");
		film.setReleaseDate(LocalDate.of(2010, 7, 16));
		film.setDuration(148);

		ValidationException exception = assertThrows(ValidationException.class, () -> validateFilm(film));
		assertEquals("Название фильма не может быть пустым.", exception.getMessage());
	}

	@Test
	void testDescriptionTooLong() {
		Film film = new Film();
		film.setName("Inception");
		film.setDescription("a".repeat(201)); // Строка длиной 201 символ
		film.setReleaseDate(LocalDate.of(2010, 7, 16));
		film.setDuration(148);

		ValidationException exception = assertThrows(ValidationException.class, () -> validateFilm(film));
		assertEquals("Максимальная длина описания — 200 символов.", exception.getMessage());
	}

	@Test
	void testReleaseDateBefore1895() {
		Film film = new Film();
		film.setName("Inception");
		film.setDescription("A mind-bending thriller.");
		film.setReleaseDate(LocalDate.of(1800, 1, 1));
		film.setDuration(148);

		ValidationException exception = assertThrows(ValidationException.class, () -> validateFilm(film));
		assertEquals("Дата релиза не может быть раньше 28 декабря 1895 года.", exception.getMessage());
	}

	@Test
	void testNegativeDuration() {
		Film film = new Film();
		film.setName("Inception");
		film.setDescription("A mind-bending thriller.");
		film.setReleaseDate(LocalDate.of(2010, 7, 16));
		film.setDuration(-100);

		ValidationException exception = assertThrows(ValidationException.class, () -> validateFilm(film));
		assertEquals("Продолжительность фильма должна быть положительным числом.", exception.getMessage());
	}

	private void validateFilm(Film film) {
		if (film.getName() == null || film.getName().isEmpty()) {
			throw new ValidationException("Название фильма не может быть пустым.");
		}
		if (film.getDescription() != null && film.getDescription().length() > 200) {
			throw new ValidationException("Максимальная длина описания — 200 символов.");
		}
		if (film.getReleaseDate() != null && film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
			throw new ValidationException("Дата релиза не может быть раньше 28 декабря 1895 года.");
		}
		if (film.getDuration() <= 0) {
			throw new ValidationException("Продолжительность фильма должна быть положительным числом.");
		}
	}
}