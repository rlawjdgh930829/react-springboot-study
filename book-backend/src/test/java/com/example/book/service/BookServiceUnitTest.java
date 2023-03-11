package com.example.book.service;

// 단위 테스트
// Service와 관련된 것들만 테스트

import com.example.book.domain.BookRepository;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class BookServiceUnitTest {
    @InjectMocks // BookService 객체가 만들어질 때 BookServiceUnitTest에 등록된 @Mock로 등록된 모든 것들을 주입받음
    private BookService bookService;

    @Mock
    private BookRepository bookRepository;
}
