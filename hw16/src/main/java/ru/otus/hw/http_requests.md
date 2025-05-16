
# Все ссылки по Spring Data Rest
```shell
clear
curl --request GET -s --url 'http://localhost:8080/api/datarest/v1' | jq
```
```shell
clear
curl --request GET -s --url 'http://localhost:8080/api/datarest/v1/profile' | jq
```

# Методы поиска по сущностям
```shell
clear
curl --request GET -s --url 'http://localhost:8080/api/datarest/v1/books/search' | jq
curl --request GET -s --url 'http://localhost:8080/api/datarest/v1/comments/search' | jq
```

# Book получаем
```shell
clear
curl --request GET -s --url 'http://localhost:8080/api/datarest/v1/books' | jq
```
```shell
clear
curl --request GET -s --url 'http://localhost:8080/api/datarest/v1/books/search/byTitle?title=BookTitle_1' | jq
```

# Actuator
```shell
clear
curl --request GET -s --url 'http://localhost:8080/actuator/health/myCustom' | jq
```

# Logfile
```shell
clear
curl --request GET -s --url 'http://localhost:8080/actuator/logfile'
```