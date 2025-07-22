package com.newy.playground.study.java.function;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.math.BigInteger;
import java.util.Comparator;
import java.util.Objects;
import java.util.function.*;

import static org.junit.jupiter.api.Assertions.*;

/***
 * [요약]
 * 표준 함수형 인터페이스는 다른 타입의 표준 함수형 인터페이스와 합성을 지원하지 않는다. (예: Supplier + Consume)
 * 표준 함수형 인터페이스는 다른 라이브러리(예: Collection, Stream 등)에서 사용하기 위해 설계된 것 같다.
 * 성능 문제가 심각하지 않는 한, Primitive 타입을 지원하는 표준 함수형 인터페이스는 사용하지 않을 것 같다.
 */
@DisplayName("Reference 타입 표준 함수형 인터페이스 테스트")
class ReferenceTypeStandardFunctionalInterfaceTest {
    @Nested
    @DisplayName("파라미터가 1개(또는 0개)인 표준 함수형 인터페이스 테스트")
    class OneOrZeroParameterStandardFunctionalInterfaceTest {
        @Nested
        @DisplayName("Function 는 '파라미터 타입'과 '반환 타입'이 다른 함수형 인터페이스이다")
        class FunctionTest {
            private final Function<Integer, String> integerToString = Objects::toString;

            @Test
            public void 호출하기() {
                assertEquals("1", integerToString.apply(1));
            }

            @Test
            public void 연쇄_호출하기() {
                Function<String, String> quote = s -> "'" + s + "'";
                assertEquals("'1'", integerToString.andThen(quote).apply(1));
                assertEquals("'1'", quote.compose(integerToString).apply(1), "compose 의 파라미터 Function 이 먼저 호출된다");
            }

            @Test
            public void 팩토리_메서드() {
                assertEquals(1, Function.identity().apply(1), "파라미터 값과 반환 값이 같은 Function 을 생성한다");
            }
        }

        @Nested
        @DisplayName("Operator 는 '파라미터 타입'과 '반환 타입'이 같은 함수형 인터페이스이다")
        class UnaryOperatorTest {
            private final UnaryOperator<String> toUpper = String::toUpperCase;

            @Test
            public void 호출하기() {
                assertEquals("ABC", toUpper.apply("abc"));
            }

            @Test
            public void 연쇄_호출하기() {
                Function<String, String> appendString = s -> s + "xyz";
                assertEquals("ABCxyz", toUpper.andThen(appendString).apply("abc"));
                assertEquals("ABCXYZ", toUpper.compose(appendString).apply("abc"));
            }

            @Test
            public void 팩토리_메서드() {
                assertEquals(1, UnaryOperator.identity().apply(1));
            }
        }

        @Nested
        @DisplayName("Predicate 는 '파라미터 타입'이 동적이고, '반환 타입'이 boolean 인 함수형 인터페이스이다.")
        class PredicateTest {
            private final Predicate<Integer> isMinSize = n -> n >= 10;

            @Test
            public void 호출하기() {
                assertTrue(isMinSize.test(10));
            }

            @Test
            public void 연쇄_호출하기() {
                Predicate<Integer> isMaxSize = n -> n <= 20;

                assertFalse(isMinSize.negate().test(10), "결과 반전해서 호출하기");
                assertTrue(isMinSize.and(isMaxSize).test(15));
                assertTrue(isMinSize.or(isMaxSize).test(30));
            }

            @Test
            public void 팩토리_메서드() {
                assertTrue(Predicate.isEqual("a").test("a"));
                assertFalse(Predicate.not(isMinSize).test(10));
            }
        }

        @Nested
        @DisplayName("Consume 는 '파라미터 타입'이 동적이고, '반환 타입'이 void 인 함수형 인터페이스이다.")
        class ConsumeTest {
            private int counter;
            private final Consumer<Integer> setCount = n -> counter = n;

            @BeforeEach
            public void setUp() {
                counter = 0;
            }

            @Test
            public void 호출하기() {
                setCount.accept(1);
                assertEquals(1, counter);
            }

            @Test
            public void 연쇄_호출하기() {
                Consumer<Integer> addTwoCount = n -> counter = n + 2;
                setCount.andThen(addTwoCount).accept(1);
                assertEquals(3, counter);
            }

            @Test
            public void 팩토리_메서드() {/* 지원하지 않는다 */}
        }

        @Nested
        @DisplayName("Supplier 는 '파라미터 타입'이 void 이고, '반환 타입'이 동적인 함수형 인터페이스이다.")
        class SupplierTest {
            @Test
            public void 호출하기() {
                Supplier<String> newString = String::new; // 생성자 레퍼런스를 Supplier 에 전달할 수 있다
                Supplier<Integer> newInteger = () -> 1;

                assertEquals("", newString.get());
                assertEquals(1, newInteger.get());
            }

            @Test
            public void 연쇄_호출하기() {/* 지원하지 않는다 */}

            @Test
            public void 팩토리_메서드() {/* 지원하지 않는다 */}
        }
    }

    @Nested
    @DisplayName("파라미터가 2개인 표준 함수형 인터페이스 테스트")
    class TwoParameterStandardFunctionalInterfaceTest {
        @Nested
        @DisplayName("파라미터가 2개인 Function 은 BiFunction 을 사용한다")
        class BiFunctionTest {
            private final BiFunction<Integer, Integer, String> sumToString = (x, y) -> Integer.toString(x + y);

            @Test
            public void 호출하기() {
                assertEquals("3", sumToString.apply(1, 2));
            }

            @Test
            public void 연쇄_호출하기() {
                Function<String, String> quote = s -> "'" + s + "'";
                assertEquals("'3'", sumToString.andThen(quote).apply(1, 2));
                // compose 메서드를 지원하지 않는다.
            }

            @Test
            public void 팩토리_메서드() {/* 지원하지 않는다 */}
        }

        @Nested
        @DisplayName("파라미터가 2개인 Operator 는 BinaryOperator 을 사용한다")
        class BinaryOperatorTest {
            private final BinaryOperator<BigInteger> add = BigInteger::add;

            @Test
            public void 호출하기() {
                assertEquals(BigInteger.valueOf(3), add.apply(BigInteger.ONE, BigInteger.TWO));
            }

            @Test
            public void 연쇄_호출하기() {
                Function<BigInteger, BigInteger> times = n -> n.multiply(BigInteger.TWO);
                assertEquals(BigInteger.valueOf(6), add.andThen(times).apply(BigInteger.ONE, BigInteger.TWO));
                // compose 메서드를 지원하지 않는다.
            }

            @Test
            public void 팩토리_메서드() {
                BinaryOperator<BigInteger> maxBy = BinaryOperator.maxBy(Comparator.comparing(BigInteger::intValue));
                BinaryOperator<BigInteger> minBy = BinaryOperator.minBy(Comparator.comparing(BigInteger::intValue));

                assertEquals(BigInteger.TWO, maxBy.apply(BigInteger.ONE, BigInteger.TWO));
                assertEquals(BigInteger.ONE, minBy.apply(BigInteger.ONE, BigInteger.TWO));
            }
        }

        @Nested
        @DisplayName("파라미터가 2개인 Predicate 는 BiPredicate 를 사용한다")
        class BiPredicateTest {
            private final BiPredicate<Integer, Integer> isValidRange = (start, end) -> start >= 10 && end <= 20;

            @Test
            public void 호출하기() {
                assertTrue(isValidRange.test(10, 20));
            }

            @Test
            public void 연쇄_호출하기() {
                BiPredicate<Integer, Integer> isSecondValidRange = (start, end) -> start >= 15 && end <= 20;

                assertFalse(isValidRange.negate().test(10, 20));
                assertFalse(isValidRange.and(isSecondValidRange).test(14, 20));
                assertTrue(isValidRange.or(isSecondValidRange).test(14, 20));
            }

            @Test
            public void 팩토리_메서드() {/* 지원하지 않는다 */}
        }

        @Nested
        @DisplayName("파라미터가 2개인 경우 Consume 은 BiConsume 를 사용한다")
        class BiConsumeTest {
            private int counter;
            private final BiConsumer<Integer, Integer> setCount = (x, y) -> counter = x * y;

            @BeforeEach
            public void setUp() {
                counter = 0;
            }

            @Test
            public void 호출하기() {
                setCount.accept(2, 3);
                assertEquals(6, counter);
            }

            @Test
            public void 연쇄_호출하기() {
                BiConsumer<Integer, Integer> addTwoCount = (a, b) -> counter += a + b;
                setCount.andThen(addTwoCount).accept(2, 3);
                assertEquals(11, counter);
            }

            @Test
            public void 팩토리_메서드() {/* 지원하지 않는다 */}
        }

        @Nested
        @DisplayName("Supplier 는 파라미터가 없기 때문에 BiSupplier 가 없다")
        class BiSupplierTest {/* 지원하지 않는다 */
        }
    }
}

@DisplayName("Primitive 타입(int, long, double) 표준 함수형 인터페이스 테스트 - 오토박싱/언박싱 을 회피하기 위해 사용")
class PrimitiveTypeStandardFunctionalInterfaceTest {
    @Nested
    @DisplayName("파라미터가 1개인 primitive 타입 표준 함수형 인터페이스 테스트")
    class OneOrZeroParameterStandardFunctionalInterfaceTest {
        @Nested
        @DisplayName("primitive 타입 Function 테스트")
        class PrimitiveTypeFunctionTest {
            @Nested
            @DisplayName("파라미터가 primitive 타입인 경우")
            class ParameterTypeTest {
                @Test
                @DisplayName("{ParameterType}Function 이라는 이름을 사용한다")
                public void 호출하기() {
                    IntFunction<String> intToString = Integer::toString;
                    LongFunction<String> longToString = Long::toString;
                    DoubleFunction<String> doubleToString = Double::toString;

                    assertEquals("1", intToString.apply(1));
                    assertEquals("2", longToString.apply(2L), "이후 테스트 부터 long primitive 타입에 대한 테스트를 생략한다");
                    assertEquals("3.0", doubleToString.apply(3.0), "이후 테스트 부터 double primitive 타입에 대한 테스트를 생략한다");
                }

                @Test
                public void 연쇄_호출하기() {/* 지원하지 않는다 */}

                @Test
                public void 팩토리_메서드() {/* 지원하지 않는다 */}
            }

            @Nested
            @DisplayName("반환 값이 primitive 타입인 경우")
            class ReturnTypeTest {
                @Test
                @DisplayName("To{ReturnType}Function 이라는 이름을 사용한다")
                public void 호출하기() {
                    ToIntFunction<String> stringToInt = Integer::parseInt;
                    assertEquals(1, stringToInt.applyAsInt("1"));
                }

                @Test
                public void 연쇄_호출하기() {/* 지원하지 않는다 */}

                @Test
                public void 팩토리_메서드() {/* 지원하지 않는다 */}
            }

            @Nested
            @DisplayName("파라미터와 반환 값이 primitive 타입인 경우")
            class ParameterAndReturnTypeTest {
                @Test
                @DisplayName("{ParameterType}To{ReturnType}Function 이라는 이름을 사용한다")
                public void 호출하기() {
                    IntToDoubleFunction intToDouble = i -> (double) i + 0.5;
                    assertEquals(1.5, intToDouble.applyAsDouble(1));
                }

                @Test
                public void 연쇄_호출하기() {/* 지원하지 않는다 */}

                @Test
                public void 팩토리_메서드() {/* 지원하지 않는다 */}
            }
        }

        @Nested
        @DisplayName("primitive 타입에 특화된 UnaryOperator 테스트")
        class PrimitiveTypeUnaryOperatorTest {
            private final IntUnaryOperator increase = i -> i + 1;

            @Test
            @DisplayName("{Return & ParameterType}UnaryOperator 이라는 이름을 사용한다")
            public void 호출하기() {
                assertEquals(2, increase.applyAsInt(1));
            }

            @Test
            public void 연쇄_호출하기() {
                IntUnaryOperator times = i -> i * 2;
                assertEquals(4, increase.andThen(times).applyAsInt(1));
                assertEquals(3, increase.compose(times).applyAsInt(1));
            }

            @Test
            public void 팩토리_메서드() {
                assertEquals(1, IntUnaryOperator.identity().applyAsInt(1));
            }
        }

        @Nested
        @DisplayName("primitive 타입에 특화된 Predicate 테스트")
        class PrimitiveTypePredicateTest {
            private final IntPredicate isMinSize = n -> n >= 10;

            @Test
            @DisplayName("{ParameterType}Predicate 이라는 이름을 사용한다")
            public void 호출하기() {
                assertTrue(isMinSize.test(10));
            }

            @Test
            @DisplayName("Predicate 특화 메서드")
            public void 값_반전해서_호출하기() {
                assertFalse(isMinSize.negate().test(10));
            }

            @Test
            public void 연쇄_호출하기() {
                IntPredicate isMaxSize = n -> n <= 20;
                assertTrue(isMinSize.and(isMaxSize).test(15));
                assertTrue(isMinSize.or(isMaxSize).test(30));
            }

            @Test
            public void 팩토리_메서드() {/* 지원하지 않는다 */}
        }

        @Nested
        @DisplayName("primitive 타입에 특화된 Consumer 테스트")
        class PrimitiveTypeConsumerTest {
            private int counter;
            private final IntConsumer setCount = n -> counter = n;

            @BeforeEach
            public void setUp() {
                counter = 0;
            }

            @Test
            @DisplayName("{ParameterType}Consumer 이라는 이름을 사용한다")
            public void 호출하기() {
                setCount.accept(1);
                assertEquals(1, counter);
            }

            @Test
            public void 연쇄_호출하기() {
                IntConsumer addTwoCount = n -> counter = n + 2;
                setCount.andThen(addTwoCount).accept(1);
                assertEquals(3, counter);
            }

            @Test
            public void 팩토리_메서드() {/* 지원하지 않는다 */}
        }

        @Nested
        @DisplayName("primitive 타입에 특화된 Supplier 테스트")
        class PrimitiveTypeSupplierTest {
            @Test
            @DisplayName("{ReturnType}Supplier 이라는 이름을 사용한다")
            public void 호출하기() {
                BooleanSupplier isTrue = () -> true;
                assertTrue(isTrue.getAsBoolean(), "Supplier 는 독특하게 boolean primitive 타입도 지원한다");
            }

            @Test
            public void 연쇄_호출하기() {/* 지원하지 않는다 */}

            @Test
            public void 팩토리_메서드() {/* 지원하지 않는다 */}
        }
    }

    @Nested
    @DisplayName("파라미터가 2개인 primitive 타입 표준 함수형 인터페이스 테스트")
    class TwoParameterStandardFunctionalInterfaceTest {
        @Nested
        @DisplayName("primitive 타입 BiFunction 테스트")
        class PrimitiveTypeBiFunctionTest {
            @Test
            @DisplayName("To{ReturnType}BiFunction 이라는 이름을 사용한다")
            public void 호출하기() {
                ToIntBiFunction<Long, Double> sumToInt = (l, d) -> (int) (l + d);
                assertEquals(3, sumToInt.applyAsInt(1L, 2.0));
            }

            @Test
            public void 연쇄_호출하기() {/* 지원하지 않는다 */}

            @Test
            public void 팩토리_메서드() {/* 지원하지 않는다 */}
        }

        @Nested
        @DisplayName("primitive 타입에 특화된 BinaryOperator 테스트")
        class PrimitiveTypeBinaryOperatorTest {
            @Test
            @DisplayName("{Return & ParameterType}BinaryOperator 이라는 이름을 사용한다")
            public void 호출하기() {
                IntBinaryOperator add = Integer::sum;
                assertEquals(3, add.applyAsInt(1, 2));
            }

            @Test
            public void 연쇄_호출하기() {/* 지원하지 않는다 */}

            @Test
            public void 팩토리_메서드() {/* 지원하지 않는다 */}
        }

        @Nested
        @DisplayName("primitive 타입에 특화된 BiPredicate 테스트")
        class PrimitiveTypeBiPredicateTest {/* 지원하지 않는다 */
        }

        @Nested
        @DisplayName("primitive 타입에 특화된 BiConsumer 테스트")
        class PrimitiveTypeBiConsumerTest {
            private String value = null;

            @Test
            @DisplayName("Obj{Second ParameterType}Consumer 이라는 이름을 사용한다")
            public void 호출하기() {
                ObjIntConsumer setString = (s, i) -> value = s.toString() + i;
                setString.accept("A", 1);
                assertEquals("A1", value);
            }

            @Test
            public void 연쇄_호출하기() {/* 지원하지 않는다 */}

            @Test
            public void 팩토리_메서드() {/* 지원하지 않는다 */}
        }

        @Nested
        @DisplayName("primitive 타입에 특화된 BiSupplier 테스트")
        class PrimitiveTypeBiSupplierTest {/* 지원하지 않는다 */
        }
    }
}

