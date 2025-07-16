package com.newy.playground.study.java;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

/***
 * [요약]
 * 프레임워크나 라이브러리에서 Optional 객체를 반환하는 경우를 위해서 학습한다.
 * 도메인 로직을 구현할 때는 크게 유용해보이지 않는다.
 * <p>
 * [특징]
 * - 값을 반환하는 메서드(예: get, orElse)와 Optional 을 반환하는 메서드(예: or, filter, map 등)가 있다.
 * - 값이 없는 Optional 객체도 Optional 을 반환하는 메서드를 사용할 수 있다.
 */
public class OptionalBehaviorTest {
    @Test
    public void optional_객체를_생성하는_방법() {
        assertFalse(Optional.empty().isPresent());
        assertFalse(Optional.ofNullable(null).isPresent());
        assertTrue(Optional.ofNullable("").isPresent());
        assertTrue(Optional.of("").isPresent());
        assertThrows(NullPointerException.class, () -> Optional.of(null), "Optional#of 메서드에 null 을 전달할 수 없다.");
    }

    @Nested
    @DisplayName("Optional 의 값을 반환하는 메서드 테스트")
    class OptionalGetValueTest {
        @Test
        public void get_메서드는_값을_반환한다() {
            assertThrows(NoSuchElementException.class, () -> Optional.empty().get());
            assertThrows(NoSuchElementException.class, () -> Optional.ofNullable(null).get(), "값이 없으면 에러 발생");
            assertEquals("", Optional.ofNullable("").get());
            assertEquals("", Optional.of("").get());
        }

        @Test
        public void orElseXxx_메서드는_null인_경우_기본_값을_반환한다() {
            assertEquals("a", Optional.of("a").orElse("b"));
            assertEquals("b", Optional.empty().orElse("b"));
            assertEquals("b", Optional.empty().orElseGet(() -> "b"), "orElseGet 은 기본 값을 생성하는 lambda 를 사용한다");
            assertThrows(RuntimeException.class, () -> Optional.empty().orElseThrow(RuntimeException::new));
        }

        @Test
        public void ifPresentXxx_메서드는_null인_경우_기본_값을_lambda로_값을_반환한다() {
            Optional.of("a").ifPresent(value -> assertEquals("a", value));
            Optional.of("a").ifPresentOrElse(value -> {
                assertEquals("a", value);
            }, () -> {
                throw new RuntimeException("Error");
            });
            Optional.empty().ifPresentOrElse(value -> {
                throw new RuntimeException("Error");
            }, () -> {
                assertTrue(true);
            });
        }
    }

    @Nested
    @DisplayName("Optional 에서 Optional 을 반환하는 메서드 테스트")
    class OptionalOperatorTest {
        @Test
        public void or_메소드는_null이_아닌_Optinal을_반환한다() {
            assertEquals(Optional.of("a"), Optional.empty().or(() -> Optional.of("a")));
            assertEquals(Optional.of("a"), Optional.of("a").or(() -> Optional.empty()));
            assertEquals("a", Optional.of("a").or(Optional::empty).get(), "마지막에 get 메서드를 호출하면, Optional 이 아닌 값을 반환한다");
        }

        @Test
        public void filter_메소드는_조건에_맞는_경우_원본_Optional을_반환한다() {
            assertEquals(Optional.of("a"), Optional.of("a").filter(value -> true));
            assertEquals(Optional.empty(), Optional.of("a").filter(value -> false));
            assertEquals(Optional.empty(), Optional.empty().filter(value -> true), "값이 없는 Optional 도 filter 를 사용할 수 있다");
            assertEquals(Optional.empty(), Optional.empty().filter(value -> false));
        }

        @Test
        public void map_메소드는_파라미터로_전달한_lamgda_반환값이_값인_경우에_사용한다() {
            assertEquals(Optional.of("ab"), Optional.of("a").map(value -> value + "b"));
            assertEquals(Optional.empty(), Optional.empty().map(value -> value + "b"));
        }

        @Test
        public void flatMap_메소드는_파라미터로_전달한_lamgda_반환값이_Optional인_경우에_사용한다() {
            assertEquals(Optional.of("ab"), Optional.of("a").flatMap(value -> Optional.of(value + "b")));
            assertEquals(Optional.empty(), Optional.empty().flatMap(value -> Optional.of(value + "b")));
        }
    }
}

