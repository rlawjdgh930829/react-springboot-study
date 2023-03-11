package com.example.book.domain;

// 단위 테스트
// DB관련 Bean이 IoC에 등록되는지 테스트

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.jdbc.DataJdbcTest;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@AutoConfigureTestDatabase(replace = Replace.ANY) // 가짜 디비로 테스트 (Replace.NONE - 실제 DB로 테스트)
@DataJdbcTest
public class BookRepositoryUnitTest {

    @Autowired
    private BookRepository bookRepository;
}
