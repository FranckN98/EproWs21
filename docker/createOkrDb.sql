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

CREATE FUNCTION update_company_objective_achievement()
    RETURNS TRIGGER
    LANGUAGE plpgsql
AS $$
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
