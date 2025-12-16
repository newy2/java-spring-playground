## 프로젝트 목적

Java SE 6 이후에 나온 기능과 Spring Boot MVC 를 학습하기 위한 프로젝트이다.

## 학습 방법

학습 테스트로 Java 와 Spring Boot MVC 의 기능을 학습한다.

## 흥미로운 문법

### try-with-resources (Java 7+)
> try 블록이 끝나면, 파일 같은 외부 리소스를 자동으로 해제해서 사용성이 증가한다.

https://github.com/newy2/java-spring-playground/blob/92eca1ccdf943940af903897b6185a62d7be7720/src/test/java/com/newy/playground/study/java/TryWithResourceTest.java#L12-L33

### 메서드 참조 (Method Reference) (Java 8+)
> 메서드 참조의 파라미터 타입과 리턴 타입이 동일하다면, 메서드 참조를 동등한 타입의 `FunctionalInterface` 변수에 할당할 수 있다. 이 규칙으로 기존 Java API에서 메서드 참조와 람다 표현식을 사용할 수 있다.

https://github.com/newy2/java-spring-playground/blob/92eca1ccdf943940af903897b6185a62d7be7720/src/test/java/com/newy/playground/study/java/functional_programing/MethodReferenceTest.java#L63-L73

### SAM(Single Abstract Method) 인터페이스 (Java 8+)
> Kotlin 만큼 편하지 않지만, SAM 인터페이스를 사용하면, 테스트 코드에서 외부 의존성을 람다 표현식으로 시뮬레이션할 수 있다.

https://github.com/newy2/java-spring-playground/blob/92eca1ccdf943940af903897b6185a62d7be7720/src/test/java/com/newy/playground/study/java/functional_programing/CustomFunctionalInterfaceTest.java#L28-L40

### Record (Java 14+)
> Kotlin 만큼 편하지 않지만, Record 와 Bean Validation 을 조합하면 Input Model 로 사용하기 좋을 거 같다.

https://github.com/newy2/java-spring-playground/blob/92eca1ccdf943940af903897b6185a62d7be7720/src/test/java/com/newy/playground/study/java/RecordTest.java#L50-L66

## 흥미롭지 않은 문법

### Optional (Java 8+)
> 프레임워크(라이브러리 포함) 개발자를 위한 문법 같다. 애플리케이션 개발에서는 사용하지 않는 편이 좋아 보인다. 몇몇 프레임워크의 리턴 타입으로 사용되기 때문에 사용법을 위해 학습한다.

https://github.com/newy2/java-spring-playground/blob/e0888adfd2e378d98761883c1b6ffe87f6274db8/src/test/java/com/newy/playground/study/java/functional_programing/OptionalBehaviorTest.java#L34-L40

### Sealed Class (Java 17+)
> 프레임워크 개발자를 위한 문법 같다. 부모 클래스와 자식 클래스간의 결합도가 생겨서, 자식 클래스 추가 시 부모 클래스의 선언부를 같이 변경해야 한다.

https://github.com/newy2/java-spring-playground/blob/21328b13017bf6de029de50d662dc920917f8b6f/src/test/java/com/newy/playground/study/java/SealedClassTest.java#L37-L52
