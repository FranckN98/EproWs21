-- region Create tables

create table BusinessUnit
(
    id   integer primary key generated always as identity,
    name varchar(64)
);

create table CompanyObjective
(
    id          int primary key generated always as identity,
    name        varchar(64),
    achievement decimal DEFAULT 0
);

create table CompanyKeyResult
(
    id                 int primary key generated always as identity,
    name               varchar(64),
    currentValue       decimal     DEFAULT 0,
    goalValue          decimal,
    confidenceLevel    decimal,
    achievement        decimal GENERATED ALWAYS AS ( currentValue / NULLIF(goalValue, 0) ) STORED,
    comment            text,
    companyObjectiveId int REFERENCES CompanyObjective (id),
    timestamp          timestamptz DEFAULT CURRENT_TIMESTAMP
);

-- historization inspired by https://stackoverflow.com/questions/56295703/how-to-store-table-history-in-postgresql
create table CompanyKeyResultHistory
(
    id              int primary key generated always as identity,
    refId           int         NOT NULL references CompanyKeyResult (id),
    changeTimeStamp timestamptz NOT NULL DEFAULT now(),
    historicalData  jsonb
);

create table BusinessUnitObjective
(
    id             int primary key generated always as identity,
    name           varchar(64),
    achievement    decimal DEFAULT 0,
    businessUnitId int REFERENCES BusinessUnit (id)
);

create table BusinessUnitKeyResult
(
    id                      int primary key generated always as identity,
    name                    varchar(64),
    currentValue            decimal     DEFAULT 0,
    goalValue               decimal,
    confidenceLevel         decimal,
    achievement             decimal GENERATED ALWAYS AS ( currentValue / NULLIF(goalValue, 0) ) STORED,
    comment                 text,
    businessUnitObjectiveId int REFERENCES BusinessUnitObjective (id),
    timestamp               timestamptz DEFAULT CURRENT_TIMESTAMP
);

-- historization inspired by https://stackoverflow.com/questions/56295703/how-to-store-table-history-in-postgresql
create table BusinessUnitKeyResultHistory
(
    id              int primary key generated always as identity,
    refId           int         NOT NULL references BusinessUnitKeyResult (id),
    changeTimeStamp timestamptz NOT NULL DEFAULT now(),
    historicalData  jsonb
);

create table Role
(
    id   int primary key generated always as identity,
    name varchar(64)
);

create table Privilege
(
    id   int primary key generated always as identity,
    name varchar(64)
);

create table PrivilegesInRole
(
    privilegeId int REFERENCES Privilege (id),
    roleId      int REFERENCES Role (id),
    CONSTRAINT privilege_role_pkey PRIMARY KEY (privilegeId, roleId)
);

create table OkrUser
(
    id             int primary key generated always as identity,
    name           varchar(64),
    surname        varchar(64),
    password       varchar(64) DEFAULT 'passwort',
    roleId         int REFERENCES Role (id),
    businessUnitId int REFERENCES BusinessUnit (id)
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
    INSERT INTO BusinessUnitKeyResultHistory (refId, historicalData)
    VALUES (old.id, to_jsonb(old));

    RETURN NEW;
end;
$$;

CREATE TRIGGER do_businessunit_keyresult_historization
    BEFORE UPDATE
    ON BusinessUnitKeyResult
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
    INSERT INTO CompanyKeyResultHistory (refId, historicalData)
    VALUES (old.id, to_jsonb(old));

    RETURN NEW;
end;
$$;

CREATE TRIGGER do_company_keyresult_historization
    BEFORE UPDATE
    ON CompanyKeyResult
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
        FROM CompanyKeyResult
        WHERE companyObjectiveId = NEW.companyObjectiveId
    )
    UPDATE CompanyObjective
    SET achievement = subq.average
    FROM subq
    WHERE id = NEW.companyObjectiveId;

    RETURN NEW;
END;
$$;

CREATE TRIGGER update_company_objective_achievement
    AFTER INSERT OR UPDATE
    ON CompanyKeyResult
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
        FROM BusinessUnitKeyResult
        WHERE businessUnitObjectiveId = NEW.businessUnitObjectiveId
    )
    UPDATE BusinessUnitObjective
    SET achievement = subq.average
    FROM subq
    WHERE id = NEW.businessUnitObjectiveId;

    RETURN NEW;
END;
$$;

CREATE TRIGGER update_businessunit_objective_achievement
    AFTER INSERT OR UPDATE
    ON BusinessUnitKeyResult
    FOR EACH ROW
EXECUTE PROCEDURE update_businessunit_objective_achievement();

-- endregion

-- endregion

-- region insert sample data

insert into BusinessUnit (name)
values ('Personal');
insert into BusinessUnit (name)
values ('IT');

insert into CompanyObjective (name, achievement)
values ('Geld verdienen', 0);
insert into CompanyObjective (name, achievement)
values ('Menschen einstellen', 0);

insert into CompanyKeyResult (name, currentValue, goalValue, confidenceLevel, comment, companyObjectiveId)
values ('Test', 1, 10, 99, 'Kommentar', 1);

insert into CompanyKeyResult (name, currentValue, goalValue, confidenceLevel, comment, companyObjectiveId)
values ('Test', 1, 1, 99, 'Kommentar', 1);

update CompanyKeyResult
set name = 'Test1'
where id = 1;

insert into BusinessUnitObjective (name, achievement, businessUnitId)
values ('BUO1', 0, 1);
insert into BusinessUnitObjective (name, achievement, businessUnitId)
values ('BUO2', 0, 1);

insert into BusinessUnitKeyResult (name, currentValue, goalValue, confidenceLevel, comment, businessUnitObjectiveId)
values ('BUO-KR1', 1, 10, 99, 'Kommentar', 1);

insert into BusinessUnitKeyResult (name, currentValue, goalValue, confidenceLevel, comment, businessUnitObjectiveId)
values ('BUO-KR2', 1, 1, 99, 'Kommentar', 1);

update BusinessUnitKeyResult
set name = 'Test1'
where id = 1;

insert into Role (name)
VALUES ('Rolle');

insert into Privilege (name)
VALUES ('Privileg');

insert into privilegesinrole (privilegeId, roleId)
VALUES (1, 1);

insert into OkrUser (name, surname, password, roleId, businessUnitId)
VALUES ('Vorname', 'Nachname', 'plaintext', 1, 1);


-- endregion