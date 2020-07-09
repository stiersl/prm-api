------------------------------------------------------------------------------
-- PRM Database Test Data Script prmDBTestData.sql
------------------------------------------------------------------------------
-- This Script will create some initial test data for the PRM database
------------------------------------------------------------------------------
-- All rights, including copyrights and patent rights, are reserved by
-- Stier Automation LLC. 
------------------------------------------------------------------------------
-- History
------------------------------------------------------------------------------
-- 0.0.1	6/9/2020	S.Stier	Inital creation
------------------------------------------------------------------------------
--NOTES:
-- this should be run against the prm database.
------------------------------------------------------------------------------

START TRANSACTION;

DELETE FROM VariableHistoryN WHERE varId = (SELECT varId from Variable where varName='VarTest1');

DELETE FROM Variable WHERE varId = (SELECT varId from Variable where varName='VarTest1');

DELETE FROM VariableHistoryN WHERE varId = (SELECT varId from Variable where varName='VarTest2');

DELETE FROM Variable WHERE varId = (SELECT varId from Variable where varName='VarTest2');


INSERT INTO Variable (varName,serverID,varDesc,engUnits,precison,minScale,maxScale)
	VALUES('VarTest1',1,'Test Area 1 Temperature','deg F',1,0.11,100.11);
INSERT INTO Variable (varName,serverID,varDesc,engUnits,precison,minScale,maxScale)
	VALUES('VarTest2',1,'Test Area 2 Temperature','deg F',1,0.22,100.22);

INSERT INTO VariableHistoryN (varId,sampleTime,varValue)
	VALUES((SELECT varId from Variable where varName='VarTest1'),'1/1/2020 07:00:01',42.7);
INSERT INTO VariableHistoryN (varId,sampleTime,varValue)
	VALUES((SELECT varId from Variable where varName='VarTest1'),'1/1/2020 08:30:03',56.2);
INSERT INTO VariableHistoryN (varId,sampleTime,varValue)
	VALUES((SELECT varId from Variable where varName='VarTest1'),'1/1/2020 09:27:05',70.9);

INSERT INTO VariableHistoryN (varId,sampleTime,varValue)
	VALUES((SELECT varId from Variable where varName='VarTest2'),'2/1/2020 07:00:01',69.23);
INSERT INTO VariableHistoryN (varId,sampleTime,varValue)
	VALUES((SELECT varId from Variable where varName='VarTest2'),'2/1/2020 08:30:03',72.34);
INSERT INTO VariableHistoryN (varId,sampleTime,varValue)
	VALUES((SELECT varId from Variable where varName='VarTest2'),'2/1/2020 09:27:05',75.45);

COMMIT;

Select 'you should have 2 varabiles --you have', count(*) from Variable;
Select 'you should have 6 historian points--you have',count(*) from VariableHistoryN;
