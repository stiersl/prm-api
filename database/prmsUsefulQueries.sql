------------------------------------------------------------------------------
-- PRM V3.0 Database useful queries
------------------------------------------------------------------------------
-- 
---------------------------------------------------------------------------
-- History
---------------------------------------------------------------------------
-- 0.0.1	3/12/2020	S.Stier	Initial Testing 
-- 0.0.4	4/03/2020	S.Stier	added users table for API access
-- 0.0.5	6/9/2020	S.Stier	Standardized formatting
------------------------------------------------------------------------------

select varId,
	varName,
	serverId,
	varDesc,
	varDescG,
	varType,
	engUnits,
	precison,
	maxScale,
	minScale,
	snapshotRate ,
	snapshotTreshold,
	lastValue,
	lastSampleTime,
	lastQuality,
	active
From Variable;

Select
	varHistoryid,
	varId,
	sampleTime,
	varValue,
	quality
FROM VariableHistoryN;

SELECT v.varName, v.varDesc, vh.sampleTime, vh.varvalue
FROM Variable v
JOIN VariableHistoryN vh   USING (varId);

SELECT varhistoryID, varId, sampleTime, varvalue, quality
FROM VariableHistoryN
WHERE varId = (Select varID
	FROM variable
	WHERE varname = 'VarTest1');

SELECT varId, sampleTime, varvalue
FROM VariableHistoryN

INSERT INTO Variable (varName) VALUES('xxx');

DELETE FROM Variable WHERE varId = 3;

DELETE FROM VariableHistoryN WHERE varId = 3;

SELECT * FROM Users;