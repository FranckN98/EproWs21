-- region Create tables

create table company_objective
(
    id          int primary key generated always as identity,
    name        varchar(64) NOT NULL,
    achievement decimal     NOT NULL DEFAULT 0,
    start_date  date        NOT NULL,
    end_date    date        NOT NULL
);

create table company_key_result
(
    id                   int primary key generated always as identity,
    name                 varchar(64) NOT NULL,
    current_value        decimal     NOT NULL DEFAULT 0,
    goal_value           decimal     NOT NULL,
    confidence_level     decimal     NOT NULL,
    achievement          decimal     NOT NULL GENERATED ALWAYS AS ( current_value / NULLIF(goal_value, 0) ) STORED,
    comment              text        NOT NULL,
    company_objective_id int         NOT NULL REFERENCES company_objective (id) ON DELETE CASCADE,
    timestamp            timestamptz NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- historization inspired by https://stackoverflow.com/questions/56295703/how-to-store-table-history-in-postgresql
create table company_key_result_history
(
    id                int primary key generated always as identity,
    ref_id            int         NOT NULL references company_key_result (id) ON DELETE CASCADE,
    change_time_stamp timestamptz NOT NULL DEFAULT now(),
    historical_data   jsonb       NOT NULL
);

create table business_unit
(
    id   integer primary key generated always as identity,
    name varchar(64) NOT NULL
);

create table business_unit_objective
(
    id                     int primary key generated always as identity,
    name                   varchar(64)       NOT NULL,
    achievement            decimal DEFAULT 0 NOT NULL,
    business_unit_id       int               NOT NULL REFERENCES business_unit (id) ON DELETE CASCADE,
    start_date             date              NOT NULL,
    end_date               date              NOT NULL,
    company_key_result_ref int               REFERENCES company_key_result (id) ON DELETE SET NULL
);

create table business_unit_key_result
(
    id                         int primary key generated always as identity,
    name                       varchar(64) NOT NULL,
    current_value              decimal     NOT NULL DEFAULT 0,
    goal_value                 decimal     NOT NULL,
    confidence_level           decimal     NOT NULL,
    achievement                decimal     NOT NULL GENERATED ALWAYS AS ( current_value / NULLIF(goal_value, 0) ) STORED,
    comment                    text        NOT NULL,
    business_unit_objective_id int         NOT NULL REFERENCES business_unit_objective (id) ON DELETE CASCADE,
    timestamp                  timestamptz NOT NULL DEFAULT CURRENT_TIMESTAMP,
    company_key_result_ref     int         REFERENCES company_key_result (id) ON DELETE SET NULL
);

-- historization inspired by https://stackoverflow.com/questions/56295703/how-to-store-table-history-in-postgresql
create table business_unit_key_result_history
(
    id                int primary key generated always as identity,
    ref_id            int         NOT NULL references business_unit_key_result (id) ON DELETE CASCADE,
    change_time_stamp timestamptz NOT NULL DEFAULT now(),
    historical_data   jsonb       NOT NULL
);

create table role
(
    id   int primary key generated always as identity,
    name varchar(64) NOT NULL
);

create table Privilege
(
    id   int primary key generated always as identity,
    name varchar(64) NOT NULL
);

create table privileges_in_role
(
    privilege_id int NOT NULL REFERENCES Privilege (id) ON DELETE CASCADE,
    role_id      int NOT NULL REFERENCES Role (id) ON DELETE CASCADE,
    CONSTRAINT privilege_role_pkey PRIMARY KEY (privilege_id, role_id)
);

create table okr_user
(
    id               int primary key generated always as identity,
    name             varchar(64) NOT NULL,
    surname          varchar(64) NOT NULL,
    password         varchar(64) NOT NULL DEFAULT 'passwort',
    role_id          int         REFERENCES Role (id) ON DELETE SET NULL,
    business_unit_id int REFERENCES business_unit (id) ON DELETE SET DEFAULT
);

-- endregion

-- region triggers

-- historization inspired by https://stackoverflow.com/questions/56295703/how-to-store-table-history-in-postgresql
-- region do_businessunit_keyresult_historization

CREATE OR REPLACE FUNCTION do_businessunit_keyresult_historization()
    RETURNS TRIGGER
    LANGUAGE plpgsql
AS
$$
BEGIN
    INSERT INTO business_unit_key_result_history (ref_id, historical_data)
    VALUES (old.id, to_jsonb(old));

    RETURN NEW;
end;
$$;

CREATE TRIGGER do_businessunit_keyresult_historization
    BEFORE UPDATE
    ON business_unit_key_result
    FOR EACH ROW
EXECUTE PROCEDURE do_businessunit_keyresult_historization();

-- endregion

-- region do_company_keyresult_historization

CREATE OR REPLACE FUNCTION do_company_keyresult_historization()
    RETURNS TRIGGER
    LANGUAGE plpgsql
AS
$$
BEGIN
    INSERT INTO company_key_result_history (ref_id, historical_data)
    VALUES (old.id, to_jsonb(old));

    RETURN NEW;
end;
$$;

CREATE TRIGGER do_company_keyresult_historization
    BEFORE UPDATE
    ON company_key_result
    FOR EACH ROW
EXECUTE PROCEDURE do_company_keyresult_historization();

-- endregion

-- region update_company_objective_achievement

CREATE FUNCTION update_company_objective_achievement()
    RETURNS TRIGGER
    LANGUAGE plpgsql
AS
$$
BEGIN
    WITH subq AS (
        SELECT avg(achievement) as average
        FROM company_key_result
        WHERE company_objective_id = NEW.company_objective_id
    )
    UPDATE company_objective
    SET achievement = subq.average
    FROM subq
    WHERE id = NEW.company_objective_id;

    RETURN NEW;
END;
$$;

CREATE TRIGGER update_company_objective_achievement
    AFTER INSERT OR UPDATE
    ON company_key_result
    FOR EACH ROW
EXECUTE PROCEDURE update_company_objective_achievement();

-- endregion

-- region update_businessunit_objective_achievement

CREATE FUNCTION update_businessunit_objective_achievement()
    RETURNS TRIGGER
    LANGUAGE plpgsql
AS
$$
BEGIN
    WITH subq AS (
        SELECT avg(achievement) as average
        FROM business_unit_key_result
        WHERE business_unit_objective_id = NEW.business_unit_objective_id
    )
    UPDATE business_unit_objective
    SET achievement = subq.average
    FROM subq
    WHERE id = NEW.business_unit_objective_id;

    RETURN NEW;
END;
$$;

CREATE TRIGGER update_businessunit_objective_achievement
    AFTER INSERT OR UPDATE
    ON business_unit_key_result
    FOR EACH ROW
EXECUTE PROCEDURE update_businessunit_objective_achievement();

-- endregion

-- endregion

-- region insert sample data

insert into business_unit (name)
values ('Personal');
insert into business_unit (name)
values ('IT');

insert into company_objective (name, achievement, start_date, end_date)
values ('Geld verdienen', 0, '2021-01-01', '2021-12-31');
insert into company_objective (name, achievement, start_date, end_date)
values ('Menschen einstellen', 0, '2022-01-01', '2022-12-31');

insert into company_key_result (name, current_value, goal_value, confidence_level, comment, company_objective_id)
values ('Test', 1, 10, 99, 'Kommentar', 1);

insert into company_key_result (name, current_value, goal_value, confidence_level, comment, company_objective_id)
values ('Test', 1, 1, 99, 'Kommentar', 2);

update company_key_result
set name = 'Test1'
where id = 1;

insert into business_unit_objective (name, achievement, business_unit_id, start_date, end_date, company_key_result_ref)
values ('BUO1', 0, 1, '2021-01-01', '2021-12-31', 1);
insert into business_unit_objective (name, achievement, business_unit_id, start_date, end_date, company_key_result_ref)
values ('BUO2', 0, 1, '2022-01-01', '2022-12-31', 2);

insert into business_unit_key_result (name, current_value, goal_value, confidence_level, comment,
                                      business_unit_objective_id, company_key_result_ref)
values ('BUO-KR1', 1, 10, 99, 'Kommentar', 1, 1);

insert into business_unit_key_result (name, current_value, goal_value, confidence_level, comment,
                                      business_unit_objective_id, company_key_result_ref)
values ('BUO-KR2', 1, 1, 99, 'Kommentar', 1, 1);

update business_unit_key_result
set name = 'Test1'
where id = 1;

insert into Role (name)
VALUES ('Rolle');

insert into Privilege (name)
VALUES ('Privileg');

insert into privileges_in_role (privilege_id, role_id)
VALUES (1, 1);

insert into okr_user (name, surname, password, role_id, business_unit_id)
VALUES ('Vorname', 'Nachname', 'plaintext', 1, 1);


-- endregion