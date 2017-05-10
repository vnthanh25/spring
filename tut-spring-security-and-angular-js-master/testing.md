<span id="_testing_angular_js_and_spring_security_part_viii"></span>
# Testing an AngularJS Application

In this article we continue [our discussion][seventh] of how to use [Spring Security](http://projects.spring.io/spring-security) with [Angular JS](http://angularjs.org) in a "single page application". Here we show how to write and run unit tests for the client-side code using the Javascript test framework [Jasmine](http://jasmine.github.io/2.0/introduction.html). This is the eighth in a series of articles, and you can catch up on the basic building blocks of the application or build it from scratch by reading the [first article][first], or you can just go straight to the [source code in Github](https://github.com/dsyer/spring-security-angular/tree/master/basic) (the same source code as Part I, but with tests now added). This article actually has very little code using Spring or Spring Security, but it covers the client-side testing in a way that might not be so easy to find in the usual Javascript community resources, and one which we feel will be comfortable for the majority of Spring users.

As with the rest of this series, the build tools are typical for Spring users, and not so much for experienced front-end developers. Thus we look for solutions that can be used from a Java IDE, and on the command line with familiar Java build tools. If you already know about Jasmine and Javascript testing, and you are happy using a Node.js based toolchain (e.g. `npm`, `grunt` etc.), then you probably can skip this article completely. If you are more comfortable in Eclipse or IntelliJ, and would prefer to use the same tools for your front end as for the back end, then this article will be of interest. When we need a command line (e.g. for continuous integration), we use Maven in the examples here, but Gradle users will probably find the same code easy to integrate.

[first]: https://spring.io/blog/2015/01/12/spring-and-angular-js-a-secure-single-page-application (First Article in the Series)
[second]: https://spring.io/blog/2015/01/12/the-login-page-angular-js-and-spring-security-part-ii (Second Article in the Series)
[sixth]: https://spring.io/blog/2015/03/23/multiple-ui-applications-and-a-gateway-single-page-application-with-spring-and-angular-js-part-vi (Sixth Article in the Series)
[seventh]: https://spring.io/blog/2015/05/13/modularizing-the-client-angular-js-and-spring-security-part-vii (Seventh Article in the Series)

> Reminder: if you are working through this section with the sample application, be sure to clear your browser cache of cookies and HTTP Basic credentials. In Chrome the best way to do that for a single server is to open a new incognito window.

## Writing a Specification in Jasmine

Our "home" controller in the "basic" application is very simple, so it won't take a lot to test it thoroughly. Here's a reminder of the code (`hello.js`):

```javascript
angular.module('hello', []).controller('home', function($scope, $http) {
  $http.get('resource/').then(function(response) {
    $scope.greeting = response.data;
  })
});
```

The main challenge we face is to provide the `$scope` and `$http` objects in the test, so we can make assertions about how they are used in the controller. Actually, even before we face that challenge we need to be able to create a controller instance, so we can test what happens when it loads. Here's how you can do that.

Create a new file `spec.js` and put it in "src/test/resources/static/js":

```javascript
describe("App", function() {

	beforeEach(module('hello'));

    var $controller;
	beforeEach(inject(function($injector) {
		$controller = $injector.get('$controller');
	}));

	it("loads a controller", function() {
		var controller = $controller('home')
	});

}
```

In this very basic test suite we have 3 important elements:

1. We `describe()` the thing that is being tested (the "App" in this case) with a function.

2. Inside that function we provide a couple of `beforeEach()` callbacks, one of which loads the Angular module "hello", and the other of which creates a factory for controllers, which we call `$controller`.

3. Behaviour is expressed through a call to `it()`, where we state in words what the expectation is, and then provide a function that makes assertions.

The test function here is so trivial it actually doesn't even make assertions, but it does create an instance of the "home" controller, so if that fails then the test will fail.

> NOTE: "src/test/resources/static/js" is a logical place for test code in a Java application, although a case could be made for "src/test/javascript". We will see later why it makes sense to put it in the test classpath, though (indeed if you are used to Spring Boot conventions you may already see why).

Now we need a driver for this Javascript code, in the form of an HTML page that we coudl load in a browser. Create a file called "test.html" and put it in "src/test/resources/static":

```html
<!doctype html>
<html>
<head>

<title>Jasmine Spec Runner</title>
<link rel="stylesheet" type="text/css"
  href="/webjars/jasmine/2.0.0/jasmine.css">
<script type="text/javascript" src="/webjars/jasmine/2.0.0/jasmine.js"></script>
<script type="text/javascript"
  src="/webjars/jasmine/2.0.0/jasmine-html.js"></script>
<script type="text/javascript" src="/webjars/jasmine/2.0.0/boot.js"></script>

<!-- include source files here... -->
<script type="text/javascript" src="/js/angular-bootstrap.js"></script>
<script type="text/javascript" src="/js/hello.js"></script>

<!-- include spec files here... -->
<script type="text/javascript"
  src="/webjars/angularjs/1.3.8/angular-mocks.js"></script>
<script type="text/javascript" src="/js/spec.js"></script>

</head>

<body>
</body>
</html>
```

The HTML is content free, but it loads some Javascript, and it will have a UI once the scripts all run.

First we load the required Jasmine components from `/webjars/**`. The 4 files that we load are just boilerplate - you can do the same thing for any application. To make those available at runtime in a test we will need to add the Jasmine dependency to our "pom.xml":

```xml
<dependency>
  <groupId>org.webjars</groupId>
  <artifactId>jasmine</artifactId>
  <version>2.0.0</version>
  <scope>test</scope>
</dependency>
 ```

Then we come to the application-specific code. The main source code for our front end is "hello.js" so we have to load that, and also its dependencies in the form of "angular-bootstrap.js" (the latter is created by the wro4j maven plugin, so you need to run `mvn package` once successfully before it is loadable).

Finally we need the "spec.js" that we jsut wrote, and its dependencies (any that are not already included the the other scripts), which for an Angular application will nearly always include the "angular-mocks.js". We load it from webjars, so you will also need to add that dependency to "pom.xml":

```xml
<dependency>
  <groupId>org.webjars</groupId>
  <artifactId>angularjs</artifactId>
  <version>1.3.8</version>
  <scope>test</scope>
</dependency>
 ```

> NOTE: The angularjs webjar was already included as a dependency of the wro4j plugin, so that it could build the "angular-bootstrap.js". This is going to be used in a different build step, so we need it again.

## Running the Specs

To run our "test.html" code we need a tiny application (e.g. in "src/test/java/test"):

```java
@SpringBootApplication
@Controller
public class TestApplication {

	@RequestMapping("/")
	public String home() {
		return "forward:/test.html";
	}

	public static void main(String[] args) {
		new SpringApplicationBuilder(TestApplication.class).properties(
				"server.port=9999", "security.basic.enabled=false").run(args);
	}

}
```

The `TestApplication` is pure boilerplate: all applications could run tests the same way. You can run it in your IDE and visit [http://localhost:9999](http://localhost:9999) to see the Javascript running. The one `@RequestMapping` we provided just makes the home page display out test HTML. All (one) tests should be green.

Your developer workflow from here would be to make a change to Javascript code and reload the test application in your browser to run the tests. So simple!

## Improving the Unit Test: Mocking HTTP Backend

To improve the spec to production grade we need to actually assert something about what happens when the controller loads. Since it makes a call to `$http.get()` we need to mock that call to avoid having to run the whole application just for a unit test. To do that we use the Angular `$httpBackend` (in "spec.js"):

```javascript
describe("App", function() {

  beforeEach(module('hello'));

  var $httpBackend, $controller;
  beforeEach(inject(function($injector) {
    $httpBackend = $injector.get('$httpBackend');
    $controller = $injector.get('$controller');
  }));

  afterEach(function() {
    $httpBackend.verifyNoOutstandingExpectation();
    $httpBackend.verifyNoOutstandingRequest();
  });

  it("says Hello Test when controller loads", function() {
    var $scope = {};
    $httpBackend.expectGET('resource/').respond(200, {
      id : 4321,
      content : 'Hello Test'
    });
    var controller = $controller('home', {
      $scope : $scope
    });
    $httpBackend.flush();
    expect($scope.greeting.content).toEqual('Hello Test');
  });

})
```

The new pieces here are:

* The creation of the `$httpBackend` in a `beforeEach()`.

* Adding a new `afterEach()` that verifies the state of the backend.

* In the test function we set expectations for the backend before we create the controller, telling it to expect a call to 'resource/',and what the response should be.

* We also add a call to jasmine `expect()` to assert the outcome.

Without having to start and stop the test application, this test should now be green in the browser.

## Running Specs on the Command Line

It's great to be able to run specs in a browser, because there are excellent developer tools built into modern browsers (e.g. F12 in Chrome). You can set breakpoints and inspect variables, and well as being able to refresh the view to re-run your tests in a live server. But this won't help you with continuous integration: for that you need a way to run the tests from a command line. There is tooling available for whatever build tools you prefer to use, but since we are using Maven here, we will add a plugin to the "pom.xml":

```xml
<plugin>
  <groupId>com.github.searls</groupId>
  <artifactId>jasmine-maven-plugin</artifactId>
  <version>2.0-alpha-01</version>
  <executions>
    <execution>
      <goals>
        <goal>test</goal>
      </goals>
    </execution>
  </executions>
</plugin>
```
The default settings for this plugin won't work with the static resource layout that we already made, so we need a bit of configuration for that:

```
<plugin>
  ...
  <configuration>
    <additionalContexts>
      <context>
        <contextRoot>/lib</contextRoot>
        <directory>${project.build.directory}/generated-resources/static/js</directory>
      </context>
    </additionalContexts>
    <preloadSources>
      <source>/lib/angular-bootstrap.js</source>
      <source>/webjars/angularjs/1.3.8/angular-mocks.js</source>
    </preloadSources>
    <jsSrcDir>${project.basedir}/src/main/resources/static/js</jsSrcDir>
    <jsTestSrcDir>${project.basedir}/src/test/resources/static/js</jsTestSrcDir>
    <webDriverClassName>org.openqa.selenium.phantomjs.PhantomJSDriver</webDriverClassName>
  </configuration>
</plugin>
```

Notice that the `webDriverClassName` is specified as `PhantomJSDriver`, which means you need `phantomjs` to be on your `PATH` at runtime. This works out of the box in [Travis CI](https://travis-ci.org), and requires a simple installation in Linux, MacOS and Windows - you can [download binaries](http://phantomjs.org/download.html) or use a package manager, like `apt-get` on Ubuntu for instance. In principle, any Selenium web driver can be used here (and the default is `HtmlUnitDriver`), but PhantomJS is probably the best one to use for an Angular application.

We also need to make the Angular library available to the plugin so it can load that "angular-mocks.js" dependency:

```
<plugin>
  ...
  <dependencies>
    <dependency>
      <groupId>org.webjars</groupId>
      <artifactId>angularjs</artifactId>
      <version>1.3.8</version>
    </dependency>
  </dependencies>
</plugin>
```

That's it. All boilerplate again (so it can go in a parent pom if you want to share the code between multiple projects). Just run it on the command line:

```
$ mvn jasmine:test
```

The tests also run as part of the Maven "test" lifecycle, so you can just run `mvn test` to run all the Java tests as well as the Javascript ones, slotting very smoothly into your existing build and deployment cycle. Here's the log:

```
$ mvn test
...
[INFO] 
-------------------------------------------------------
 J A S M I N E   S P E C S
-------------------------------------------------------
[INFO] 
App
  says Hello Test when controller loads

Results: 1 specs, 0 failures

[INFO] ------------------------------------------------------------------------
[INFO] BUILD SUCCESS
[INFO] ------------------------------------------------------------------------
[INFO] Total time: 21.064s
[INFO] Finished at: Sun Apr 26 14:46:14 BST 2015
[INFO] Final Memory: 47M/385M
[INFO] ------------------------------------------------------------------------
```

The Jasmine Maven plugin also comes with a goal `mvn jasmine:bdd` that runs a server that you can load in your browser to run the tests (as an alternative to the `TestApplication` above).

## Conclusion

Being able to run unit tests for Javascript is important in a modern web application and it's a topic that we've ignored (or dodged) up to now in this series. With this installment we have presented the basic ingredients of how to write the tests, how to run them at development time and also, importantly, in a continuous integration setting. The approach we have taken is not going to suit everyone, so please don't feel bad about doing it in a different way, but make sure you have all those ingredients. The way we did it here will probably feel comfortable to traditional Java enterprise developers, and integrates well with their existing tools and processes, so if you are in that category I hope you will find it useful as a starting point. More examples of testing with Angular and Jasmine can be found in plenty of places on the internet, but the first point of call might be the ["single" sample](https://github.com/dsyer/spring-security-angular/tree/master/single) from this series, which now has some up to date test code which is a bit less trivial than the code we needed to write for the "basic" sample in this article.
