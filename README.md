# java-pass-pwned
Microservice for checking for password that has been pwned, works with Java 11.

Requires only a 680MB heap

Inspired by https://www.bloomingpassword.fun/

## Getting started
Download the SHA-1 password database https://haveibeenpwned.com/Passwords

Extract it and create a new file with just 16 chars long sha1 records

`cut -c 1-16 pwned-passwords-ordered-by-count.txt > 16.txt`

Copy it to the project folder.

## Building
```./gradlew build```

## Testing
This will also run a filter quality test that will fail if you get a false negative

```./gradlew test```

## Run
```java -Xmx680M -jar build/libs/password.checker-all.jar```
