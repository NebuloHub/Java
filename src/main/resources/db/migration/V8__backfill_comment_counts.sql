-- V8__backfill_comment_counts.sql
-- Atualiza o comment_count para todos os posts existentes
-- com base nos dados reais da tabela de comentários.
-- Isso é executado uma vez para preencher os dados da V5.

MERGE INTO posts p
USING (
    SELECT
        id_post,
        COUNT(id_comment) AS total_comments
    FROM comments
    GROUP BY id_post
) c ON (p.id_post = c.id_post)
WHEN MATCHED THEN
    UPDATE SET p.comment_count = c.total_comments;

COMMIT;