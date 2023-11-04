-- author:RST

CREATE TABLE IF NOT EXISTS POST(
    ID          SERIAL PRIMARY KEY,
    USERID      BIGINT,
    TITLE       VARCHAR(100),
    CONTENT     VARCHAR(4000)
                               );