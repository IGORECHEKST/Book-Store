INSERT INTO books (id, title, author, isbn, price, description, cover_image)
VALUES (1, 'The Hobbit', 'J.R.R. Tolkien', '978-0547928227', 15.00, 'Adventure', 'image.jpg');

INSERT INTO books_categories (book_id, category_id) VALUES (1, 1);
