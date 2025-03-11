package ru.practicum.shareit.ru.practicum.item;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.comments.dto.CommentDto;
import ru.practicum.shareit.item.ItemController;
import ru.practicum.shareit.item.ItemService;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.MainItemRequestDto;

import java.time.LocalDateTime;
import java.util.Collections;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ItemController.class)
class ItemControllerTest {

    public static final String HEADER_USER_ID = "X-Sharer-User-Id";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ItemService itemService;

    private ItemDto itemDto;
    private CommentDto commentDto;

    @BeforeEach
    void setUp() {
        itemDto = new ItemDto();
        itemDto.setId(1L);
        itemDto.setName("Hammer");
        itemDto.setDescription("A hammer");
        itemDto.setAvailable(true);
        itemDto.setOwner(1L);

        commentDto = new CommentDto();
        commentDto.setId(1L);
        commentDto.setText("Great item!");
        commentDto.setAuthorName("John");
        commentDto.setCreated(LocalDateTime.now());
    }

    @Test
    void getItems() throws Exception {
        long userId = 1L;
        when(itemService.findByUserId(userId)).thenReturn(Collections.singletonList(itemDto));

        mockMvc.perform(get("/items")
                        .header(HEADER_USER_ID, userId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L));
    }

    @Test
    void getItem() throws Exception {
        long itemId = 1L;
        when(itemService.getItemById(itemId)).thenReturn(itemDto);

        mockMvc.perform(get("/items/{item-id}", itemId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L));
    }

    @Test
    void addItem() throws Exception {
        long userId = 1L;
        ItemDto inputDto = new ItemDto();
        inputDto.setName("Hammer");
        inputDto.setDescription("A hammer");
        inputDto.setAvailable(true);

        when(itemService.addItem(any(ItemDto.class), eq(userId))).thenReturn(itemDto);

        mockMvc.perform(post("/items")
                        .header(HEADER_USER_ID, userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(inputDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L));
    }

    @Test
    void updateItem() throws Exception {
        long userId = 1L;
        long itemId = 1L;
        MainItemRequestDto mainItemRequestDto = new MainItemRequestDto();
        mainItemRequestDto.setName("Updated Hammer");
        mainItemRequestDto.setDescription("Updated description");
        mainItemRequestDto.setAvailable(true);

        when(itemService.updateItem(any(MainItemRequestDto.class), eq(userId), eq(itemId))).thenReturn(itemDto);

        mockMvc.perform(patch("/items/{item-id}", itemId)
                        .header(HEADER_USER_ID, userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(mainItemRequestDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L));
    }

    @Test
    void deleteItem() throws Exception {
        long userId = 1L;
        long itemId = 1L;

        mockMvc.perform(delete("/items/{item-id}", itemId)
                        .header(HEADER_USER_ID, userId))
                .andExpect(status().isNoContent());

        verify(itemService).deleteItem(userId, itemId);
    }

    @Test
    void searchItems() throws Exception {
        String text = "hammer";
        when(itemService.searchItems(text)).thenReturn(Collections.singletonList(itemDto));

        mockMvc.perform(get("/items/search")
                        .param("text", text))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L));
    }

    @Test
    void addComment() throws Exception {
        long userId = 1L;
        long itemId = 1L;
        CommentDto inputComment = new CommentDto();
        inputComment.setText("Great item!");

        when(itemService.addComment(eq(itemId), eq(userId), any(CommentDto.class))).thenReturn(commentDto);

        mockMvc.perform(post("/items/{item-id}/comment", itemId)
                        .header(HEADER_USER_ID, userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(inputComment)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.text").value("Great item!"));
    }
}