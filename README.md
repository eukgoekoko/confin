# confín #

A sample Finagle-based HTTP server which utilizes re-scheduling to sustain the average serving time.


### Prerequisites ###

 * JDK 8
 * [SBT](http://www.scala-sbt.org/)

### How to run ###

    git clone https://github.com/eukgoekoko/confin.git

    cd confin

    sbt run

### How to configure ###
All settings are stored at src/main/resources/application.conf

  * **host** - hostname
  * **port** - port number
  * **avgTimeMillis** - average serving time in milliseconds

### Test results ###

```
> ab -c 2 -n 2 http://localhost:8080/foo/bar

> Concurrency Level:      2
  Time taken for tests:   1.10058 seconds
  Complete requests:      2
  Failed requests:        0

> ab -c 10 -n 10 http://localhost:8080/foo/bar

> Concurrency Level:      10
  Time taken for tests:   5.19288 seconds
  Complete requests:      10
  Failed requests:        0

> ab -c 20 -n 20 http://localhost:8080/foo/bar

> Concurrency Level:      20
  Time taken for tests:   10.187583 seconds
  Complete requests:      20
  Failed requests:        0
```



