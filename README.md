## 프로젝트 목적

Java SE 6 이후에 나온 기능과 Spring Boot MVC 를 학습하기 위한 프로젝트이다.

## 학습 방법

학습 테스트로 Java 와 Spring Boot MVC 의 기능을 학습한다.

## 흥미로웠던 기술

### try-with-resources (Java 7+)
> try 블록이 끝나면, 외부 리소스를 자동으로 해제한다.

https://github.com/newy2/java-spring-playground/blob/92eca1ccdf943940af903897b6185a62d7be7720/src/test/java/com/newy/playground/study/java/TryWithResourceTest.java#L14-L33

### 메서드 참조 (Method Reference) (Java 8+)
> 메서드 참조의 파라미터와 리턴 타입이 동일하다면, 다른 타입의 FunctionalInterface 에 할당할 수 있다.  
> 위의 규칙으로 기존의 Java API(예: Collection)에서 메서드 참조와 람다 표현식을 사용할 수 있다.

https://github.com/newy2/java-spring-playground/blob/92eca1ccdf943940af903897b6185a62d7be7720/src/test/java/com/newy/playground/study/java/functional_programing/MethodReferenceTest.java#L63-L73

### SAM(Single Abstract Method) 인터페이스 (Java 8+)
> Kotlin 만큼 편하지 않지만, SAM 인터페이스를 사용하면, 테스트 코드에서 외부 의존성을 람다 표현식으로 시뮬레이션할 수 있다.

https://github.com/newy2/java-spring-playground/blob/92eca1ccdf943940af903897b6185a62d7be7720/src/test/java/com/newy/playground/study/java/functional_programing/CustomFunctionalInterfaceTest.java#L28-L40

### Record (Java 14+)
> Kotlin 만큼 편하지 않지만, Record 와 Bean Validation 을 조합하면 Input Model 로 사용하기 좋을 거 같다.

https://github.com/newy2/java-spring-playground/blob/92eca1ccdf943940af903897b6185a62d7be7720/src/test/java/com/newy/playground/study/java/RecordTest.java#L50-L66
