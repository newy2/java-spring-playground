package com.newy.playground.study.java;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/***
 * try-with-resource 블록은 파일 같은 외부 리소스를 자동으로 해제해 준다.
 */
public class TryWithResourceTest {
    static int count = 0;

    @Test
    public void try블록이_끝나면_AutoCloseable의_close메서드가_자동으로_호출된다() {
        try (var res1 = new FakeResource(); var res2 = new FakeResource()) {
            res1.increase();
            res2.increase();
            assertEquals(2, count);
        }
        assertEquals(0, count);
    }

    static class FakeResource implements AutoCloseable {
        public void increase() {
            count++;
        }

        @Override
        public void close() {
            count--;
        }
    }
}
