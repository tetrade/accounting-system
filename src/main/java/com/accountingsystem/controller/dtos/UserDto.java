package com.accountingsystem.controller.dtos;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.springframework.beans.factory.annotation.Value;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.time.LocalDate;

@Data
public class UserDto {
    private int id;

    @Size(min = 5, message = "не должно быть меньше 5 символов")
    @NotBlank
    private String login;

    @NotBlank
    @Pattern(regexp = "^([а-яА-Я]+ )([а-яА-Я]+ )([а-яА-Я]+)", message = "должен быть следующий формат:Ф И О")
    private String fullName;

    @JsonFormat(pattern = "dd.MM.yyyy")
    @NotNull
    private LocalDate terminationDate;

    @NotNull
    private Boolean isAdmin;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Pattern(regexp = "^.*(?=.{6,})(?=.*[a-zA-Z])(?=.*\\d)(?=.*[!#$%&? \"]).*$",
            message = "должно быть не меньше 6 символов и должно содержать не менее одной буквы латиницы, цифры и спец-символа")
    private String newPassword;

    // TODO Возможность редактирования пользователя
}
