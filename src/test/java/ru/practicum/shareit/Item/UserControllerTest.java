package ru.practicum.shareit.Item;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.user.controller.UserController;
import ru.practicum.shareit.user.dto.UserResponseDto;
import ru.practicum.shareit.user.service.UserService;

import java.nio.charset.StandardCharsets;

import static java.lang.reflect.Array.get;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.jsonPath;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
public class UserControllerTest {
    @Autowired
    ObjectMapper mapper;

    @MockBean
    UserService userService;

    @Autowired
    private MockMvc mvc;

    private UserResponseDto userDto = new UserResponseDto();

//    @Test
//    void saveNewUser() throws Exception {
//        when(userService.saveUser(any()))
//                .thenReturn(userDto);
//
//        mvc.perform(post("/users")
//                        .content(mapper.writeValueAsString(userDto))
//                        .characterEncoding(StandardCharsets.UTF_8)
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .accept(MediaType.APPLICATION_JSON))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.id", is(userDto.getId()), Long.class))
//                .andExpect(jsonPath("$.name", is(userDto.getName())))
//                .andExpect(jsonPath("$.email", is(userDto.getEmail())));
//    }
//
//    @Test
//    void testGetItem() throws Exception {
//        // Arrange
//        Long itemId = 1L;
//        String expectedResult = "Item 1";
//        when(userService.(itemId)).thenReturn(expectedResult);
//
//        // Act & Assert
//        mvc.perform(get("/items/{id}", itemId) // Выполняем GET-запрос
//                        .contentType(MediaType.APPLICATION_JSON)) // Указываем тип контента
//                .andExpect(status().isOk()) // Проверяем статус ответа
//                .andExpect(content().string(expectedResult)); // Проверяем тело ответа
//    }
}
