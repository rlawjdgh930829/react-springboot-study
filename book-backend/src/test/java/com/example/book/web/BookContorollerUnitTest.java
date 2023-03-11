package com.example.book.web;

// 단위 테스트(Controller. filter, ControllerAdvice)
// 컨트롤러 관련 로직만 테스트

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.book.domain.Book;
import com.example.book.service.BookService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@WebMvcTest
public class BookContorollerUnitTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean // IoC환경에 bean등록
    private BookService bookService;

    @Test
    public void save_test() throws Exception {
        Book book = new Book(null, "제목", "작성자");
        String content = new ObjectMapper().writeValueAsString(book); // 객체를 json으로 변경
        when(bookService.저장하기(book)).thenReturn(new Book(1L, "제목", "작성자"));

        ResultActions resultAction = mockMvc.perform(post("/book")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(content)
                .accept(MediaType.APPLICATION_JSON_UTF8));

        resultAction
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.title").value("제목"))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    public void findAll_test() throws Exception {
        List<Book> books = new ArrayList<>();
        books.add(new Book(1L, "제목1", "작성자1"));
        books.add(new Book(2L, "제목2", "작성자2"));
        when(bookService.모두가져오기()).thenReturn(books);

        ResultActions resultAction = mockMvc.perform(get("/book")
                .accept(MediaType.APPLICATION_JSON_UTF8));

        resultAction
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", Matchers.hasSize(2)))
                .andExpect(jsonPath("$.[0].title").value("제목1"))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    public void findById_test() throws Exception {
        Long id = 1L;

        when(bookService.한건가져오기(id)).thenReturn(new Book(1L, "제목1", "작성자1"));

        ResultActions resultAction = mockMvc.perform(get("/book/{id}", id)
                .accept(MediaType.APPLICATION_JSON_UTF8));

        resultAction
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("제목1"))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    public void update_test() throws Exception {
        Long id = 1L;
        Book book = new Book(null, "수정제목", "수정작성자");
        String content = new ObjectMapper().writeValueAsString(book);

        when(bookService.수정하기(id, book)).thenReturn(new Book(1L, "수정제목", "수정작성자"));

        ResultActions resultAction = mockMvc.perform(put("/book/{id}", id)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(content)
                .accept(MediaType.APPLICATION_JSON_UTF8));

        resultAction
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("수정제목"))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    public void delete_test() throws Exception {
        Long id = 1L;

        when(bookService.삭제하기(id)).thenReturn("ok");

        ResultActions resultAction = mockMvc.perform(delete("/book/{id}", id)
                .accept(MediaType.TEXT_PLAIN));

        resultAction
                .andExpect(status().isOk())
                .andDo(MockMvcResultHandlers.print());

        MvcResult requestResult = resultAction.andReturn();
        String result = requestResult.getResponse().getContentAsString();

        result.equals("ok");
    }
}
