package ru.yandex.practicum.filmorate.model;

import lombok.*;
import lombok.extern.slf4j.Slf4j;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Data
@Slf4j
@Builder
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
public class Film {
    private int id;
    @NotBlank(message = "название не может быть пустым")
    private String name;
    @Size(max = 200, message = "максимальная длина описания — 200 символов")
    private String description;
    @NotNull(message = "дата релиза — не раньше 28 декабря 1895 года")
    private LocalDate releaseDate;
    @NotNull(message = "продолжительность не может быть равна 0")
    @Positive(message = "продолжительность не может быть меньше 0")
    private long duration;
    private Map<Integer, User> likes = new HashMap<>();
    private String rate;
    private Mpa mpa;
    private Set<Genre> genres;

    public int setId(int id) {
        this.id = id;
        return id;
    }
}
