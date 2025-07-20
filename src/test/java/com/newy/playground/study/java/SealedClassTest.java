package com.newy.playground.study.java;

import org.junit.jupiter.api.Test;

import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.assertEquals;

/***
 * [요약]
 * 하위 클래스를 제한하는 sealed class 는 라이브러리 개발에 도움이 될지 모르겠지만, 애플리케이션 개발에서는 유용해 보이지 않는다.
 * <p>
 * [단점]
 * - 부모 클래스에서 자식 클래스에 대한 의존성이 생겨서, 자식 클래스가 추가 되는 경우 부모 클래스의 선언부를 같이 수정해야 한다.
 * - switch 문에서 쉽게 타입을 변환할 수 있지만, 이미 클래스 계층이 있다면 다형성 로직으로 구현하는 게 더 낫다.
 *
 */
public class SealedClassTest {
    @Test
    public void non_sealed_클래스를_확장하면_최상위_sealed_클래스의_메서드를_오버라이드할_수_있다() {
        class SomeClass extends NonSealedClass {
            @Override
            String name() {
                return "SomeClass";
            }

            int age() {
                return 10;
            }
        }

        assertEquals("NonSealedClass", new NonSealedClass().name());
        assertEquals("SomeClass", new SomeClass().name());
        assertEquals(10, new SomeClass().age());
    }

    @Test
    public void switch_문에서_sealed_클래스_형변환하기() {
        Function<BaseSealedClass, String> getMessage = (type) -> {
            return switch (type) {
                case FinalClass f -> "[A]" + f.name();
                case NonSealedClass n -> "[B]" + n.name();
                case SecondDepthFinalClass f -> "[C]" + f.name(); // 컴파일러가 2단계 final 클래스의 case 문 선언 위치를 잡아준다.
                case SealedClass s -> "[D]" + s.name();
            };
        };

        assertEquals("[A]FinalClass", getMessage.apply(new FinalClass()));
        assertEquals("[B]NonSealedClass", getMessage.apply(new NonSealedClass()));
        assertEquals("[C]SecondDepthFinalClass", getMessage.apply(new SecondDepthFinalClass()));
        assertEquals("[D]SealedClass", getMessage.apply(new SealedClass()));
    }

    static abstract sealed class BaseSealedClass permits FinalClass, NonSealedClass, SealedClass {
        abstract String name();
    }

    static final class FinalClass extends BaseSealedClass {
        @Override
        String name() {
            return "FinalClass";
        }
    }

    static non-sealed class NonSealedClass extends BaseSealedClass {
        @Override
        String name() {
            return "NonSealedClass";
        }
    }

    static sealed class SealedClass extends BaseSealedClass permits SecondDepthFinalClass {
        @Override
        String name() {
            return "SealedClass";
        }
    }

    static final class SecondDepthFinalClass extends SealedClass {
        @Override
        String name() {
            return "SecondDepthFinalClass";
        }
    }
}
