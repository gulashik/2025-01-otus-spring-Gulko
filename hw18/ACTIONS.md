
# Пересобираем
```
mvn clean package -DskipTests
```
```shell
podman compose down -v
clear
podman compose up -d --build
#podman compose up postgres -d
podman compose ps
```

```shell
# Дожидаемся корректного состояния приложения
clear
while true; do
  STATUS=$(curl -s http://localhost:8080/actuator/health | jq -r .status 2>/dev/null)

  if [ ${STATUS:=DOWN} = "UP" ]; then
    echo "Status: $STATUS - Application is ready"
    break
  fi
  echo "Status: $STATUS - waiting..."
  sleep 5
done
```

```shell
# Создание пользователя
clear
curl -X POST http://localhost:8080/api/users \
  -H "Content-Type: application/json" \
  -d '{
    "name": "John Doe",
    "email": "john@example.com"
  }' | jq
```

```shell
# Получение всех пользователей
clear
curl http://localhost:8080/api/users | jq
```

```shell
# Обновление пользователя
clear
curl -X PUT http://localhost:8080/api/users/1 \
  -H "Content-Type: application/json" \
  -d '{
    "name": "John Smith",
    "email": "john.smith@example.com"
  }' | jq
```

```shell
# Удаление пользователя
curl -X DELETE http://localhost:8080/api/users/1 | jq
```

```shell
clear
podman compose down -v
```