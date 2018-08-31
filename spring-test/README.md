# spring-test

> `spring-test` 모듈은 스프링 컴포넌트들의 [unit testing](https://docs.spring.io/spring/docs/4.3.16.RELEASE/spring-framework-reference/htmlsingle/#unit-testing)과 [integration testing](https://docs.spring.io/spring/docs/4.3.16.RELEASE/spring-framework-reference/htmlsingle/#integration-testing)을  JUnit or TestNG을 통해 지원한다. 또한 스프링 `ApplicationContext`의 일관된 로딩과 이런 컨텍스들의 캐싱을 제공한다. 그리고 코드를 in isolation에서 테스트할 수 있도록 [mock objects](https://docs.spring.io/spring/docs/4.3.16.RELEASE/spring-framework-reference/htmlsingle/#mock-objects)도 제공한다.

- 자세한 내용은 [여기](https://docs.spring.io/spring/docs/current/spring-framework-reference/testing.html)를 참고

#### \@RunWith
Spring TestContext Framework는 JUnit4 를 이용하여 custom runner(JUnit 4.12 이상)를 통해 full integration 을 제공한다. \@RunWith(SpringJUnit4ClassRunner.class)나 짧게 표현한 \@RunWith(SpringRunner.class) annotation을 사용해서, 개발자는 표준 JUnit 4의 unit and integration test를 구현할수 있으며, application context를 로딩하거나 test instances에 dependency injection, transactional 테스트 메소드의 실행 등의 여러가지 이점을 TestContext framework를 통해 얻을 수 있다. 만약 JUnit 4의 `Parameterized`나 `MockitoJUnitRunner` 와 같은 third-party runners를 Spring TestContext과 함께 사용하고 싶다면 [Spring’s support for JUnit rules](https://docs.spring.io/spring/docs/current/spring-framework-reference/testing.html#testcontext-junit4-rules) 를 대신 사용하라.

#### \@WebAppConfiguration

> \@WebAppConfiguration 은 `WebApplicationContext` 를 integration test를 위해 로딩하여 사용하기 위한 클래스 레벨의 annotation이다. 테스트 클래스에 \@WebAppConfiguration 가 존재하는 것 만으로도 웹앱의 root path를 기본값으로 `"file:src/main/webapp"` 로 사용한다.(즉, resource base path이다.) resource base path는 내부적으로 MockServletContext를 생성하는데 사용한다. 

- 자세한 내용은 [여기](https://docs.spring.io/spring/docs/current/spring-framework-reference/testing.html#webappconfiguration)를 참고

#### \@ContextConfiguration

> \@ContextConfiguration 는 integration test를 위해 `Application Context`를 로드하고 구성하는 방법을 결정하는데 사용되는 클래스 레벨의 metadata를 정의한다. 특히 @ContextConfiguration은 context를 로드하는데 사용되는 context resource의 `locations`나 `annotated classes`를 지정하는데 사용한다.


# 작성중....