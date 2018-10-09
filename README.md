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

    ./gradlew run