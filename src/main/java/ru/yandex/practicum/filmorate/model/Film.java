package ru.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;

import java.time.LocalDate;

@Data
@Slf4j
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
    @NotNull(message = "продолжительность не может быть равно 0")
    @Positive(message = "продолжительность не может быть меньше 0")
    private long duration;

    public int setId(int id) {
        this.id = id;
        return id;
    }
}
