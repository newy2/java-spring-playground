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
interface MarketRepository extends JpaRepository<Market, Long> {
    Optional<Market> findByCode(String code);
}

@Entity
@Table(name = "market")
class Market {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String code;

    Market(String code) {
        this.code = code;
    }

    public Market() {

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
public class JpaTest {
    @Autowired
    private MarketRepository marketRepository;

    @Test
    @Transactional
    public void test() {

        Market market = new Market("ABC");
        marketRepository.save(market);
        var result = marketRepository.findByCode("ABC");

        assertTrue(result.isPresent());
    }
}


