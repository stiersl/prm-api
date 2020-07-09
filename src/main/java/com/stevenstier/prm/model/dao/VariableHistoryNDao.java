package com.stevenstier.prm.model.dao;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;

import com.stevenstier.prm.model.VariableHistoryN;

public interface VariableHistoryNDao {

	public List<VariableHistoryN> getAllVarHistoryVarId(long varId);

	public List<VariableHistoryN> getAllVarHistoryVarName(String varName);

	public List<VariableHistoryN> getVarHistoryByVarId(long varId, LocalDateTime startTime, LocalDateTime endTime);

	public List<VariableHistoryN> getVarHistoryByVarName(String varName, LocalDateTime startTime,
			LocalDateTime endTime);

	public VariableHistoryN getVarHistoryNowByVarId(long varId);

	public VariableHistoryN getVarHistoryNowByVarName(String varName);

	public boolean insertVarHistoryByVarId(long varId, Instant sampleTime, Double varValue, Integer quality);

	public boolean insertVarHistoryByVarName(String varName, Instant sampleTime, Double varValue, Integer quality);

	public boolean deleteVarHistoryByVarId(long varId);

	public boolean deleteVarHistoryByVarName(String varName);
}
