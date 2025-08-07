package com.newy.playground.study.spring.data_jpa;

import com.newy.playground.study.spring.test_config.TestContainerConfig;
import jakarta.persistence.*;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertTrue;


@Repository
interface MarketRepository2 extends JpaRepository<Market2, Long> {
    Optional<Market2> findByCode(String code);
}

@Entity
@Table(name = "market")
class Market2 {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String code;

    Market2(String code) {
        this.code = code;
    }

    public Market2() {

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}

@DataJpaTest
@Import(TestContainerConfig.class)
public class OtherJpaTest {
    @Autowired
    private MarketRepository2 marketRepository;

    @Test
    @Transactional
    public void test2() {
        var market = new Market2("123");
        marketRepository.save(market);
        var result = marketRepository.findByCode("123");

        assertTrue(result.isPresent());
    }
}
