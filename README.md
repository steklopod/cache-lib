# Cache lib for redis![Backend CI](https://github.com/steklopod/cache-lib/workflows/Backend%20CI/badge.svg)

* How to use:

`application.yml`
```yaml
spring:
  cache:
    type: redis
  redis:
    host: localhost
    port: 6379
    timeout: 2000
    password: 123456
```

* `build.gradle.kts`
```kotlin
implementation(project(":common-lib"))
```

```kotlin

@Service
class CustomerCachingService{

    @Cacheable(cacheNames = [USER_CACHE], key = "#userId", unless="#result == null")
    fun users(userId: Int): List<User> = userService.getUsers(userId)

}
```
