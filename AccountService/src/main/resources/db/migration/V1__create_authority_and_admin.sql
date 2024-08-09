--CREATE TYPE roles AS ENUM ('ROLE_ADMIN', 'ROLE_EXECUTOR', 'ROLE_USER');

CREATE TABLE authority(id SERIAL PRIMARY KEY,
role VARCHAR(255) CHECK(role = 'ROLE_ADMIN' OR role = 'ROLE_EXECUTOR' OR role = 'ROLE_USER'));

INSERT INTO authority(role) VALUES ('ROLE_ADMIN'), ('ROLE_EXECUTOR'), ('ROLE_USER');

CREATE TABLE account(id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
email VARCHAR(255), password VARCHAR(255),
authority_id int REFERENCES authority(id));

INSERT INTO account(email, password, authority_id) VALUES('admin', '$2a$10$6iASR8Yv/wZPqDOBkIx98eNi2K1EZp2IIdmdItEZjItRXJqEcc.MK', 1);