package com.newy.playground.study.java;

import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validation;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/***
 * [요약]
 * Record 를 사용하면 입력 모델을 편하게 구현할 수 있다.
 * Bean Validator 로 입력 유효성 검증을 하는 경우, 'compact 생성자'가 아닌 '일반 생성자'를 사용해야 한다.
 */
public class RecordTest {
    @Test
    public void record는_값_객체를_위한_메서드를_자동으로_구현한다() {
        record Person(String name, int age) {
        }

        var person = new Person("Jay", 10);
        assertEquals(new Person("Jay", 10), person);
        assertEquals(new Person("Jay", 10).hashCode(), person.hashCode());
        assertEquals("Person[name=Jay, age=10]", person.toString());
        assertEquals("Jay", person.name(), "getter 메서드도 자동으로 만들어 준다.");
        assertEquals(10, person.age(), "getter 메서드도 자동으로 만들어 준다.");
    }

    @Test
    public void record_복사_메서드는_수동으로_구현해야_한다() {
        record CopyableRecord(String name, int age) {
            CopyableRecord withName(String name) {
                return new CopyableRecord(name, age);
            }

            CopyableRecord withAge(int age) {
                return new CopyableRecord(name, age);
            }
        }

        var person = new CopyableRecord("Jay", 10);
        assertEquals(new CopyableRecord("Jamie", 10), person.withName("Jamie"));
        assertEquals(new CopyableRecord("Jay", 20), person.withAge(20));
    }

    @Nested
    @DisplayName("Bean Validation(JSR-380)를 사용한 입력 유효성 검증 테스트")
    class RecordValidationTest {
        @Test
        public void 일반_생성자를_오버라이드해서_입력_유효성을_검증한다() {
            record ValidationWithNormalConstructor(@NotEmpty String name, @Min(10) int age) implements BaseValidator {
                ValidationWithNormalConstructor(String name, int age) {
                    this.name = name;
                    this.age = age;
                    validate();
                }
            }

            assertDoesNotThrow(() -> new ValidationWithNormalConstructor("Jay", 10));
            assertThrows(ConstraintViolationException.class, () -> new ValidationWithNormalConstructor("Jay", 9), "age 유효성 검증 실패");
            assertThrows(ConstraintViolationException.class, () -> new ValidationWithNormalConstructor("", 10), "name 유효성 검증 실패");
            assertThrows(ConstraintViolationException.class, () -> new ValidationWithNormalConstructor(null, 10), "name 유효성 검증 실패");
        }

        @Test
        public void 팩토리_메서드로_입력_유효성을_검증한다() {
            record ValidationWithFactoryMethod(@NotEmpty String name, @Min(10) int age) implements BaseValidator {
                static ValidationWithFactoryMethod of(String name, int age) {
                    var result = new ValidationWithFactoryMethod(name, age);
                    result.validate();
                    return result;
                }
//              private ValidationWithFactoryMethod(String name, int age) { // 컴파일 에러. private 생성자를 만들 수 없다.
//                  this.name = name;
//                  this.age = age;
//              }
            }

            assertDoesNotThrow(() -> ValidationWithFactoryMethod.of("Jay", 10));
            assertThrows(ConstraintViolationException.class, () -> ValidationWithFactoryMethod.of("Jay", 9));
            assertThrows(ConstraintViolationException.class, () -> ValidationWithFactoryMethod.of("", 10));
            assertThrows(ConstraintViolationException.class, () -> ValidationWithFactoryMethod.of(null, 10));
            assertDoesNotThrow(() -> new ValidationWithFactoryMethod(null, 10), "[단점] 사용자 코드에서 일반 생성자를 호출하는 것을 방지하지 못한다.");
        }

        @Test
        public void compact_constructor_블록에서는_this로_필드에_접근하는_경우_시스템_초기값을_리턴하기_떄문에_BeanValidator를_사용할_수_없다() {
            record ValidationWithCompactConstructor(@NotEmpty String name, @Min(10) int age) implements BaseValidator {
                ValidationWithCompactConstructor {
                    assertEquals(10, age);
                    assertEquals(0, this.age(), "getter 메서드로 필드를 조회하면, 시스템 초기 값을 리턴한다.");
//                  assertEquals(0, this.age, "컴파일 에러. this 연산자로 필드에 접근할 수 없다.");

                    assertEquals("Jay", name);
                    assertNull(this.name(), "getter 메서드로 필드를 조회하면, 시스템 초기 값을 리턴한다.");
//                  assertNull(this.name, "컴파일 에러. this 연산자로 필드에 접근할 수 없다.");

                    validate();
                }
            }

            assertThrows(ConstraintViolationException.class, () -> new ValidationWithCompactConstructor("Jay", 10), "정상적인 입력 값을 사용해도, 입력 유효성 검증에 실패한다.");
        }

        @Test
        public void compact_constructor_의_입력_유효성은_로직으로_직접_구현해야_한다() {
            record ValidationWithCompactConstructor(String name, int age) {
                ValidationWithCompactConstructor {
                    if (age <= 9) {
                        throw new IllegalArgumentException("age 는 10 이상이어야 합니다.");
                    }
                    if (name == null || name.isEmpty()) {
                        throw new IllegalArgumentException("name 길이는 0 이상이어야 합니다.");
                    }
                }
            }

            assertDoesNotThrow(() -> new ValidationWithCompactConstructor("Jay", 10));
            assertThrows(IllegalArgumentException.class, () -> new ValidationWithCompactConstructor("Jay", 9));
            assertThrows(IllegalArgumentException.class, () -> new ValidationWithCompactConstructor("", 10));
            assertThrows(IllegalArgumentException.class, () -> new ValidationWithCompactConstructor(null, 10));
        }

        interface BaseValidator {
            default void validate() {
                // 테스트 가독성을 위해 ValidatorFactory 캐시는 생략한다.
                try (var factory = Validation.buildDefaultValidatorFactory()) {
                    var validations = factory.getValidator().validate(this);
                    if (!validations.isEmpty()) {
                        throw new ConstraintViolationException(validations);
                    }
                }
            }
        }
    }

}