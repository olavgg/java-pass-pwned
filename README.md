# java-pass-pwned
Microservice for checking for password that has been pwned, works with Java 11.

Requires only a 680MB heap with Java 11

Java 8 may need up to 900MB

Inspired by https://www.bloomingpassword.fun/

## Getting started
Download the SHA-1 password database https://haveibeenpwned.com/Passwords

Extract it and create a new file with just 16 chars long sha1 records

`$ cut -c 1-16 pwned-passwords-ordered-by-count.txt > 16.txt`

Copy it to the project folder.

## Building
```$ ./gradlew assemble```

## Testing
This will also run a filter quality test that will fail if you get a false negative

```$ ./gradlew test```

## Run
```$ java -Xmx680M -jar build/libs/password.checker-all.jar```

First run will take a few minutes as you will need to load all hashes into the
cuckoo filter. It will also serialize the cuckoo filter to disk. 
Next runs will be just a few seconds.

## Test with curl
```sh
$ curl -I -XGET http://localhost:8080/password/password123456
HTTP/1.1 200 OK
Date: Sat, 16 Feb 2019 10:27:50 GMT
connection: keep-alive
transfer-encoding: chunked
```

```sh
$ curl -I -XGET http://localhost:8080/sha1/f7c3bc1d808e04732adf679965ccc34ca7ae3441
HTTP/1.1 200 OK
Date: Sat, 16 Feb 2019 10:27:50 GMT
connection: keep-alive
transfer-encoding: chunked
```

```sh
$ curl -I -XGET http://localhost:8080/password/thispasswordisnotknownyet
HTTP/1.1 404 Not Found
Date: Sat, 16 Feb 2019 10:29:03 GMT
transfer-encoding: chunked
connection: close
```