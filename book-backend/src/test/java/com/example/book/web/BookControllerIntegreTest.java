package com.example.book.web;

// 통합 테스트
// 모든 Bean들을 IoC에 올리고 테스트 하는 것

import com.example.book.domain.Book;
import com.example.book.domain.BookRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Transactional // 각각의 테스트 함수가 종료될 때마다 트랜잭션을 rollback해줌
@AutoConfigureMockMvc // MockMvc를 IoC에 등록해줌
@SpringBootTest(webEnvironment = WebEnvironment.MOCK)
public class BookControllerIntegreTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private EntityManager entityManager;

    @BeforeEach
    public void init() {
        entityManager.createNativeQuery("ALTER TABLE book ALTER COLUMN id RESTART WITH 1").executeUpdate();
    }

    @Test
    public void save_test() throws Exception {
        Book book = new Book(null, "제목", "작성자");
        String content = new ObjectMapper().writeValueAsString(book); // 객체를 json으로 변경

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
        books.add(new Book(null, "제목1", "작성자1"));
        books.add(new Book(null, "제목2", "작성자2"));
        bookRepository.saveAll(books);

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
        List<Book> books = new ArrayList<>();
        books.add(new Book(null, "제목1", "작성자1"));
        books.add(new Book(null, "제목2", "작성자2"));
        bookRepository.saveAll(books);

        Long id = 1L;
        ResultActions resultAction = mockMvc.perform(get("/book/{id}", id)
                .accept(MediaType.APPLICATION_JSON_UTF8));

        resultAction
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("제목1"))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    public void update_test() throws Exception {
        List<Book> books = new ArrayList<>();
        books.add(new Book(null, "제목1", "작성자1"));
        books.add(new Book(null, "제목2", "작성자2"));
        bookRepository.saveAll(books);

        Long id = 1L;
        Book book = new Book(1L, "수정제목", "수정작성자");
        String content = new ObjectMapper().writeValueAsString(book);

        ResultActions resultAction = mockMvc.perform(put("/book/{id}", id)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(content)
                .accept(MediaType.APPLICATION_JSON_UTF8));

        resultAction
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.title").value("수정제목"))
                .andDo(MockMvcResultHandlers.print());
    }
}
