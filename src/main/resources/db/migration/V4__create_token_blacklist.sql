-- V4__create_token_blacklist.sql
-- Creates the sequence and table for storing invalidated JWTs (logout).

DECLARE
    v_count NUMBER;
BEGIN

    SELECT COUNT(*) INTO v_count FROM user_sequences WHERE sequence_name = 'TOKEN_BLACKLIST_SEQ';
    IF v_count = 0 THEN
        EXECUTE IMMEDIATE 'CREATE SEQUENCE TOKEN_BLACKLIST_SEQ START WITH 1 INCREMENT BY 1 NOCACHE';
    END IF;
END;
/

DECLARE
    v_count NUMBER;
BEGIN

    SELECT COUNT(*) INTO v_count FROM user_tables WHERE table_name = 'TOKEN_BLACKLIST';
    IF v_count = 0 THEN
        EXECUTE IMMEDIATE q'{
            CREATE TABLE token_blacklist (
                id_token    NUMBER(19) PRIMARY KEY,
                token       VARCHAR2(2000) NOT NULL UNIQUE,
                expires_at  TIMESTAMP WITH TIME ZONE NOT NULL
            )
        }';
    END IF;
END;
/

DECLARE
    v_is_identity NUMBER;
BEGIN

    SELECT COUNT(*) INTO v_is_identity
    FROM user_tab_cols
    WHERE table_name = 'TOKEN_BLACKLIST' AND column_name = 'ID_TOKEN' AND identity_column = 'YES';

    IF v_is_identity = 0 THEN
        BEGIN
            EXECUTE IMMEDIATE 'ALTER TABLE token_blacklist MODIFY (id_token DEFAULT TOKEN_BLACKLIST_SEQ.NEXTVAL)';
        EXCEPTION WHEN OTHERS THEN NULL;
        END;
    END IF;
END;
/

COMMIT;