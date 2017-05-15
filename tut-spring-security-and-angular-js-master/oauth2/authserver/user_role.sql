--DROP TABLE IF EXISTS user;
CREATE TABLE IF NOT EXISTS users (
  id SERIAL PRIMARY KEY,
  username varchar(100) DEFAULT NULL,
  password varchar(300) DEFAULT NULL,
  enabled bit(1) NULL DEFAULT B'0'
);

--DROP TABLE IF EXISTS user_role;
CREATE TABLE IF NOT EXISTS user_roles (
  id SERIAL PRIMARY KEY,
  username varchar(100) DEFAULT NULL,
  role varchar(300) DEFAULT NULL
);
