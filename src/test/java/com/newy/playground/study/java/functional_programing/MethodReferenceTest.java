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
        public void 번외__String의_compareTo_메서드는_함수형_인터페이스인_Comparator_타입의_변수에_할당할_수도_있다() {
            Comparator<String> functionalInterface = String::compareTo;

            String[] array = {"2", "1"};
            Arrays.sort(array, functionalInterface);
            assertArrayEquals(new String[]{"1", "2"}, array);
        }
    }
}
