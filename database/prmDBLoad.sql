------------------------------------------------------------------------------
-- PRM Database Load Script
------------------------------------------------------------------------------
-- This Script will create the Required Database for the PRM Suite - It is 
-- initially written for loading into a Postgres(v11) database
------------------------------------------------------------------------------
-- All rights, including copyrights and patent rights, are reserved by
-- Stier Automation LLC. 
------------------------------------------------------------------------------
-- History
------------------------------------------------------------------------------
-- 0.0.1	3/12/2020	S.Stier	Initial Testing of variables and varHistory	
-- 0.0.2	3/15/2020	S.Stier	Removed Not Null on ServerID and VarDec
-- 0.0.3	3/21/2020	S.Stier	changed Timestamp to TimestampTZ
-- 0.0.4	4/03/2020	S.Stier	added users table for API access
-- 0.0.5	6/9/2020	S.Stier	Standardized formatting
------------------------------------------------------------------------------
--NOTES:
--This script is for new installations only because it drops database objects
--(tables, views, etc.) before creating them.
------------------------------------------------------------------------------

START TRANSACTION;

DROP TABLE IF EXISTS VariableHistoryN;

DROP TABLE IF EXISTS Variable;

DROP TABLE IF EXISTS Users;
-- Table: Users (holds all the list of all users)
CREATE TABLE Users (
	id SERIAL PRIMARY KEY,
	username VARCHAR(255) NOT NULL UNIQUE,-- Username
	password VARCHAR(32) NOT NULL,-- Password
	salt VARCHAR(256) NOT NULL,-- Password Salt
	role VARCHAR(255) NOT NULL DEFAULT('user')
);

-- Table: Variable (holds all the definitions of all the Variable)
CREATE TABLE Variable
(	varId SERIAL,
	varName VARCHAR NOT NULL, --unique Name for the variable
	serverID INTEGER,--points to server collecting variable
	varDesc VARCHAR,--variable description
	varDescG  VARCHAR,--variable description in other language
	varType  VARCHAR  DEFAULT 'N',--number of significant digits to right of decimal point
	engUnits  VARCHAR,--Engineering units of the varaible
	precison INTEGER DEFAULT 0,--number of significant digits to right of decimal point
	maxScale NUMERIC,--maximum value for trending
	minScale NUMERIC,--maximum value for trending
	snapshotRate NUMERIC  DEFAULT 60,-- Number of Seconds between read attempts
	snapshotTreshold NUMERIC DEFAULT 0,--amount the value must change before recording a new value to the historian
	lastValue VARCHAR, -- last historian snapshot value recorded
	lastSampleTime TIMESTAMPTZ, --Sample time of last Data historian snapshot
	lastQuality INTEGER, --last reading quality
	active BOOLEAN DEFAULT true,--indicates if the variable is active- set to false to disable reads
	CONSTRAINT pk_variable_varId PRIMARY KEY (varId),
	CONSTRAINT unq_variable_varName UNIQUE (varName),
	CONSTRAINT chk_variable_varType CHECK (varType in ('N','S','B'))
);


COMMENT ON COLUMN Variable.varName
	IS 'variable name';

COMMENT ON COLUMN Variable.serverID
	IS 'server id - points to server collecting variable';

COMMENT ON COLUMN Variable.varDesc
	IS 'variable description';

COMMENT ON COLUMN Variable.varDescG
	IS 'variable description in other language';

COMMENT ON COLUMN Variable.varType
	IS 'data varType numeric,string,boolean';

COMMENT ON COLUMN Variable.precison
	IS 'number of significant digits to right of decimal point';

COMMENT ON COLUMN Variable.maxScale
	IS 'maximum value for trending';

COMMENT ON COLUMN Variable.minScale
	IS 'minimum value for trending';

COMMENT ON COLUMN Variable.snapshotRate
	IS 'Number of Seconds between read attempts';

COMMENT ON COLUMN Variable.snapshotTreshold
	IS 'amount the value must change before recording a new value to the historian';

COMMENT ON COLUMN Variable.lastValue
	IS 'last historian snapshot value recorded';

COMMENT ON COLUMN Variable.lastSampleTime
	IS 'Sample time of last Data historian snapshot';

COMMENT ON COLUMN Variable.lastQuality
	IS 'last reading quality ';

COMMENT ON COLUMN Variable.active
	IS 'indicates if the variable is active- set to false to disable reads';


-- Table: VariableHistoryN (holds the variable history data for all Variable of varType=numeric)

CREATE TABLE VariableHistoryN
(
	varHistoryId BIGSERIAL,
	varId INTEGER NOT NULL, --foriegn key which points to the variable in the Variable table
	sampleTime TIMESTAMPTZ NOT NULL, --time stamp that sample was taken(GMT0)
	varValue NUMERIC, --value of variable at time of sample
	quality INTEGER, --quality of value reading at sample time
	CONSTRAINT pk_VariableHistoryN_id PRIMARY KEY (varHistoryid),
	CONSTRAINT unq_VariableHistoryN_varId_sampleTime UNIQUE (varId,sampleTime),
	CONSTRAINT fk_VariableHistoryN_Variable_varId FOREIGN KEY (varId) REFERENCES Variable (varId)
);


COMMENT ON COLUMN VariableHistoryN.varId
	IS 'foriegn key which points to the variable in the Variable table';

COMMENT ON COLUMN VariableHistoryN.sampleTime
	IS 'time stamp that sample was taken';

COMMENT ON COLUMN VariableHistoryN.VarValue
	IS 'value of variable at time of sample';

COMMENT ON COLUMN VariableHistoryN.quality
	IS 'quality of value reading at sample time';

COMMIT;

