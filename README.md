# prodlisting
Technical Exercise

This code demonstrates a simple happy path where everything works.

Outstanding areas include:

- handle exceptional cases such as the website is down and not reachable
- introducing reasonable timeouts around SiteScrapper#getPage(...)
- additional tests to cover the exceptional cases

Another area I would look to improve is to use an IoC library like [Dagger 2](https://github.com/google/dagger) to inject dependencies.

How to run
----------
The project comes bundled with gradle wrapper

Navigate to the project's root directory and run the following command:

    ./gradlew run
    
Similarly, from the project's root directory, to run the tests:

    ./gradlew clean test    