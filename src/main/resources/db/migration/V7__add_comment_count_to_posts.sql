-- V7__add_comment_count_to_posts.sql
-- Adiciona uma coluna opcional para armazenar o total de comentários (para denormalization)
ALTER TABLE posts
ADD (
    comment_count NUMBER(10) DEFAULT 0 NOT NULL
);

COMMIT;