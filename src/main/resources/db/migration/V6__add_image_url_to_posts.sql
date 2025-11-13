-- V6__add_image_url_to_posts.sql
-- Adiciona uma coluna opcional para armazenar a URL de uma imagem do post
ALTER TABLE posts
ADD (
    image_url VARCHAR2(2000) NULL
);

COMMIT;