Поднять образ(если нужно)
```shell
podman compose --file ./../../../../../../docker-compose.yml down 
podman compose --file ./../../../../../../docker-compose.yml up -d 
clear
podman ps -a
```

[AuthorController.java](controller%2FAuthorController.java)</br>
```shell
clear 

curl -s http://localhost:8080/api/v1/authors | jq -c '.[]'
```

[GenreController.java](controller%2FGenreController.java)</br>
```shell
clear 

curl -s http://localhost:8080/api/v1/genres | jq -c '.[]'
```

[BookController.java](controller%2FBookController.java)</br>
```shell
clear 

curl -s http://localhost:8080/api/v1/books | jq -c '.[]'
```
```shell
clear 

curl -s http://localhost:8080/api/v1/book/1 | jq -c 
```
```shell
clear 

curl -X POST -s http://localhost:8080/api/v1/book \
-H "Content-Type: application/json" \
-d '{"id":null,"title":"BookTitle_x","authorDto":{"id":"1"},"genreDto":{"id":"1"}}' \
| jq -c

curl -s http://localhost:8080/api/v1/books | jq -c '.[]'
```
```shell
clear 

curl -X PATCH -s http://localhost:8080/api/v1/book/1 \
-H "Content-Type: application/json" \
-d '{"id":1,"title":"BookTitle_x","authorDto":{"id":"2"},"genreDto":{"id":"2"}}' \
| jq -c

curl -s http://localhost:8080/api/v1/books | jq -c '.[]'
```
```shell
clear 

BOOK_ID=$(curl -s http://localhost:8080/api/v1/books | jq -r '.[0].id')
echo "book with id=$BOOK_ID will be deleted"

curl -X DELETE -s http://localhost:8080/api/v1/book/$BOOK_ID 

curl -s http://localhost:8080/api/v1/books | jq -c '.[]'
```
[CommentController.java](controller%2FCommentController.java)</br>
```shell
clear 

curl -s http://localhost:8080/api/v1/comments | jq -c '.[]'
```
```shell
clear 

curl -s http://localhost:8080/api/v1/comment/1 | jq -c '.[]'
```
```shell
clear 

curl -s 'http://localhost:8080/api/v1/comment?book_id=2' | jq -c '.[]'
```
```shell
clear 

curl -X PATCH -s http://localhost:8080/api/v1/comment/1 \
-H "Content-Type: application/json" \
-d '{"id":"1","text":"Comment_X_book_X","bookDto":{"id":"1"},"genreDto":{"id":"1"}}}' \
| jq -c

curl -s http://localhost:8080/api/v1/comments | jq -c '.[]'
```
```shell
clear 

curl -X POST -s http://localhost:8080/api/v1/comment \
-H "Content-Type: application/json" \
-d '{"id":null,"text":"Comment_New_book_New","bookDto":{"id":"2"},"genreDto":{"id":"2"}}}' \
| jq -c

curl -s http://localhost:8080/api/v1/comments | jq -c '.[]'
```
```shell
clear 

COMMENT_ID=$(curl -s http://localhost:8080/api/v1/comments | jq -r '.[0].id')
echo "comments with id=$COMMENT_ID will be deleted"

curl -X DELETE -s http://localhost:8080/api/v1/comment/$COMMENT_ID 

curl -s http://localhost:8080/api/v1/comments | jq -c '.[]'
```