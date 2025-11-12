-- V2__create_tables.sql

-- USERS Table
CREATE TABLE users (
    id_user     NUMBER(15) PRIMARY KEY,
    username    VARCHAR2(100) NOT NULL UNIQUE,
    email       VARCHAR2(255) NOT NULL UNIQUE,
    password    VARCHAR2(200) NOT NULL, -- Hashed
    role        VARCHAR2(20) DEFAULT 'ROLE_USER' NOT NULL,
    created_at  TIMESTAMP WITH TIME ZONE DEFAULT SYSTIMESTAMP NOT NULL
);

-- POSTS Table
CREATE TABLE posts (
    id_post         NUMBER(15) PRIMARY KEY,
    title           VARCHAR2(255) NOT NULL,
    description     CLOB, -- Use CLOB for long text
    avg_rating      NUMBER(3, 1) DEFAULT 0.0 NOT NULL,
    rating_count    NUMBER(10) DEFAULT 0 NOT NULL,
    id_user         NUMBER(15) NOT NULL,
    created_at      TIMESTAMP WITH TIME ZONE DEFAULT SYSTIMESTAMP NOT NULL,
    CONSTRAINT fk_post_user FOREIGN KEY (id_user)
        REFERENCES users(id_user)
        ON DELETE CASCADE -- If user is deleted, delete their posts
);

-- RATINGS Table
CREATE TABLE ratings (
    id_rating       NUMBER(15) PRIMARY KEY,
    rating_value    NUMBER(2) NOT NULL, -- 0-10
    id_user         NUMBER(15) NOT NULL,
    id_post         NUMBER(15) NOT NULL,
    CONSTRAINT fk_rating_user FOREIGN KEY (id_user)
        REFERENCES users(id_user)
        ON DELETE CASCADE,
    CONSTRAINT fk_rating_post FOREIGN KEY (id_post)
        REFERENCES posts(id_post)
        ON DELETE CASCADE,
    -- A user can only rate a post once
    CONSTRAINT uq_user_post_rating UNIQUE (id_user, id_post),
    -- Rating value must be between 0 and 10
    CONSTRAINT chk_rating_value CHECK (rating_value >= 0 AND rating_value <= 10)
);

-- COMMENTS Table
CREATE TABLE comments (
    id_comment  NUMBER(15) PRIMARY KEY,
    content     VARCHAR2(2000) NOT NULL,
    id_user     NUMBER(15) NOT NULL,
    id_post     NUMBER(15) NOT NULL,
    created_at  TIMESTAMP WITH TIME ZONE DEFAULT SYSTIMESTAMP NOT NULL,
    CONSTRAINT fk_comment_user FOREIGN KEY (id_user)
        REFERENCES users(id_user)
        ON DELETE CASCADE, -- Or SET NULL if you want to keep comments
    CONSTRAINT fk_comment_post FOREIGN KEY (id_post)
        REFERENCES posts(id_post)
        ON DELETE CASCADE
);

COMMIT;