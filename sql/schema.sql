DROP DATABASE IF EXISTS mindmerge_db;
CREATE DATABASE mindmerge_db;
USE mindmerge_db;

CREATE TABLE decision (
  decision_id   INT AUTO_INCREMENT PRIMARY KEY,
  title         VARCHAR(200)  NOT NULL,
  chosen_option VARCHAR(200)  NOT NULL,
  rationale     TEXT          NOT NULL,
  decided_by    VARCHAR(100)  NOT NULL,
  decided_at    DATE          NOT NULL
);

INSERT INTO decision (title, chosen_option, rationale, decided_by, decided_at) VALUES
  ('Testing inputting a decision', 'Test',
   'Testing',
   'Jason Nguyen', '2026-09-22');