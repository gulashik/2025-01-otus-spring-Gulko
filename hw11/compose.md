Что запущено?
```shell
podman ps -a
```

Запускаем контейнер
```shell
podman compose down && podman compose up -d && podman ps -a
```

Останавливаем контейнер
```shell
podman compose down
```