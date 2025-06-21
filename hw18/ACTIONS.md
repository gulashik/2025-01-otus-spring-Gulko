
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

# Rate Limiter - тестирование ограничения запросов

```shell
# Тестируем Rate Limiter для API пользователей
clear
echo "Testing Rate Limiter - making rapid requests..."
for i in {1..25}; do
  echo "Request $i:"
  curl -s -w "Status: %{http_code}, Time: %{time_total}s\n" \
    http://localhost:8080/api/users \
    -o /dev/null
  sleep 0.2
done
```

```shell
# Тестируем строгий Rate Limiter для операций создания 
clear
echo "Testing Create Operations Rate Limiter..."
for i in {1..18}; do
  echo "Create request $i:"
  curl -X POST http://localhost:8080/api/users \
    -H "Content-Type: application/json" \
    -d "{
      \"name\": \"RateTest $i\",
      \"email\": \"ratetest$i@example.com\"
    }" \
    -w "Status: %{http_code}, Time: %{time_total}s\n" \
    -s -o /dev/null
  sleep 0.1
done
```

```shell
# Тестируем Rate Limiter для операций удаления 
clear
echo "Testing Create Operations Rate Limiter..."
curl http://localhost:8080/api/users | jq -r '.[].id' | while read id; do
  echo "Deleting user with ID: $id"
  curl -X DELETE "http://localhost:8080/api/users/$id"
  echo
  sleep 0.1
done

```

```shell
clear
podman compose down -v
```