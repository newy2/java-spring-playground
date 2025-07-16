package com.newy.playground.study.java;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/***
 * [요약]
 * FunctionalInterface 를 사용하면, 유닛 테스트 작성 시 Service 의 OutPort 를 Lambda 로 시뮬레이션 할 수 있지만, Kotlin 만큼 편하지는 않다.
 * <p>
 * [특징]
 * - Java 는 default 파리미터를 지원하지 않아서, Service 생성 헬퍼 메서드를 만들 때 불편하다.
 */
public class LambdaTest {
    @Test
    public void somethingService() {
        var service = new SomethingService(() -> "World", () -> "Jay");
        assertEquals("Hello World(Jay)", service.getData());
    }

    @Test
    public void 에러가_발생하는_상황을_시뮬레이션_한다() {
        var service = newServiceWithFirstOutPort(() -> {
            throw new RuntimeException("에러 발생!");
        });

        var exception = assertThrows(RuntimeException.class, service::getData);
        assertEquals("에러 발생!", exception.getMessage());
    }

    @Test
    public void 두_번째_OutPort를_오버리아드해서_테스트용_데이터를_생성한다() {
        var service = newServiceWithSecondOutPort(() -> "Yoon");
        assertEquals("Hello World(Yoon)", service.getData());
    }

    private SomethingService newServiceWithFirstOutPort(FirstOutPort firstOutPort) {
        return new SomethingService(firstOutPort, getDefaultSecondOutPort());
    }

    private SomethingService newServiceWithSecondOutPort(SecondOutPort secondOutPort) {
        return new SomethingService(getDefaultFirstOutPort(), secondOutPort);
    }

    private FirstOutPort getDefaultFirstOutPort() {
        return () -> "World";
    }

    private SecondOutPort getDefaultSecondOutPort() {
        return () -> "Jay";
    }

    @FunctionalInterface
    interface FirstOutPort {
        String getFirstData();
    }

    @FunctionalInterface
    interface SecondOutPort {
        String getSecondData();
    }

    static class SomethingService {
        private final FirstOutPort firstOutPort;
        private final SecondOutPort secondOutPort;

        SomethingService(FirstOutPort firstOutPort, SecondOutPort secondOutPort) {
            this.firstOutPort = firstOutPort;
            this.secondOutPort = secondOutPort;
        }

        public String getData() {
            return "Hello " + firstOutPort.getFirstData() + "(" + secondOutPort.getSecondData() + ")";
        }
    }
}