insert into authors(full_name)
values ('Dostoyevskiy_1'), ('Murakami_2'), ('Mayn_Rid_3');

insert into genres(name)
values ('Genre_1'), ('Genre_2'), ('Genre_3');

insert into books(title, author_id, genre_id)
values ('BookTitle_1', 1, 1), ('BookTitle_2', 2, 2), ('BookTitle_3', 3, 3);

insert into comments(text, book_id)
values ('Comment_1_book_1', 1), ('Comment_2_book_1', 1), ('Comment_3_book_1', 1),
       ('Comment_1_book_2', 2), ('Comment_2_book_2', 2);