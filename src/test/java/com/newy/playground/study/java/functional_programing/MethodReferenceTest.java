package com.newy.playground.study.java.functional_programing;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Comparator;
import java.util.function.Function;
import java.util.function.ToIntBiFunction;
import java.util.function.ToIntFunction;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * [요약]
 * 메서드 시그니처가 동일하다면, 다른 타입의 함수형 인터페이스 변수에 메서드 참조를 할당할 수 있다.
 * (예: ToIntBiFunction<String, String> a = String::compareTo; // Function 에 메서드 참조 할당)
 * (예: Comparator<String> b = String::compareTo; // Comparator 에 메서드 참조 할당)
 * <p>
 * 이 규칙 덕분에 기존 Java API를 유지하면서도 함수형 프로그래밍을 사용할 수 있다.
 */
public class MethodReferenceTest {
    @Test
    public void 클래스의_정적_메서드_참조하기() {
        ToIntFunction<String> staticMethod = Integer::parseInt;
        assertEquals(1, staticMethod.applyAsInt("1"));
    }

    @Nested
    class InstanceMethodReferenceTest {
        private final String instance = "b";
        private final String parameterA = "a";
        private final String parameterB = "b";
        private final String parameterC = "c";

        @Test
        public void 클래스의_인스턴스_메서드_참조_사용하기() {
            ToIntBiFunction<String, String> unboundInstanceMethod = String::compareTo;

            assertEquals(1, unboundInstanceMethod.applyAsInt(instance, parameterA), "Function 의 첫 번째 파라미터가 메세지 수신 객체이다.");
            assertEquals(0, unboundInstanceMethod.applyAsInt(instance, parameterB));
            assertEquals(-1, unboundInstanceMethod.applyAsInt(instance, parameterC));
        }

        @Test
        public void 객체의_인스턴스_메서드_참조_사용하기() {
            ToIntFunction<String> boundInstanceMethod = instance::compareTo;

            assertEquals(1, boundInstanceMethod.applyAsInt(parameterA));
            assertEquals(0, boundInstanceMethod.applyAsInt(parameterB));
            assertEquals(-1, boundInstanceMethod.applyAsInt(parameterC));
        }

        @Test
        public void 커링_함수로_객체의_인스턴스_메서드를_흉내낼_수_있다() {
            Function<String, ToIntFunction<String>> curryingFunction = _instance -> parameter -> _instance.compareTo(parameter);
            ToIntFunction<String> boundInstanceMethod = curryingFunction.apply(instance);

            assertEquals(1, boundInstanceMethod.applyAsInt(parameterA));
            assertEquals(0, boundInstanceMethod.applyAsInt(parameterB));
            assertEquals(-1, boundInstanceMethod.applyAsInt(parameterC));
        }

        @Test
        public void 메서드_시그니처가_동일하다면_다른_타입의_함수형_인터페이스예_메서드_참조를_할당할_수_있다() {
            ToIntBiFunction<String, String> unboundInstanceMethod = String::compareTo;
            Comparator<String> functionalInterface = String::compareTo;
            assertEquals(unboundInstanceMethod.applyAsInt(instance, parameterA), functionalInterface.compare(instance, parameterA));

            String[] array = {"2", "1"};
            Arrays.sort(array, functionalInterface);
            assertArrayEquals(new String[]{"1", "2"}, array);
        }
    }
}
