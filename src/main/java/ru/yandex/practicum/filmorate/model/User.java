package ru.yandex.practicum.filmorate.model;

import lombok.*;
import lombok.extern.slf4j.Slf4j;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Past;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
@Builder
@Slf4j
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
public class User {
    private int id;
    @Email
    private String email;
    @NotBlank(message = "логин не может быть пустым и содержать пробелы")
    private String login;
    private String name;
    @Past
    private LocalDate birthday;
    private Set<Integer> friends = new HashSet<>();

    public int setId(int id) {
        this.id = id;
        return id;
    }
}
