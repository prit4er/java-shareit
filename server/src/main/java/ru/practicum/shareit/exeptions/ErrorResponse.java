package ru.practicum.shareit.exeptions;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.Map;

@Builder
@Getter
@Setter
@ToString
@NoArgsConstructor
@EqualsAndHashCode
@AllArgsConstructor
public class ErrorResponse {

    private String error;
    private Map<String, String> messages;  // Изменяем на Map для хранения множества сообщений
}
