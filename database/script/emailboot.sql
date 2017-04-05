CREATE TABLE IF NOT EXISTS email_logger (
  log_id            SERIAL                      NOT NULL,
  log_serial        VARCHAR(100)                NOT NULL,
  log_subject       VARCHAR(100)                NOT NULL,
  log_delivered     BOOL                        NOT NULL DEFAULT 'false',
  log_content       VARCHAR(2500)               NOT NULL,
  log_timestamp     TIMESTAMP WITHOUT TIME ZONE NOT NULL DEFAULT current_timestamp
) WITH (OIDS =FALSE);
ALTER TABLE email_logger OWNER TO emailboot;

DROP DATABASE postgres;
