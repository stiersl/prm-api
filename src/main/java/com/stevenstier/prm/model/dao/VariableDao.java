package com.stevenstier.prm.model.dao;

import java.time.LocalDateTime;
import java.util.List;

import com.stevenstier.prm.model.Variable;

public interface VariableDao {

	public List<Variable> getAllVariables();

	public Variable getVariablebyID(long varId);

	public Variable getVariablebyName(String varName);

	public Variable createVariable(Variable var);

	public Boolean deleteVariable(long varId);

	public Boolean updateVariable(Variable var);

	public void deactivateVariable(long varId);

	public void updatelastValue(long varId, String lastvalue, LocalDateTime timeStamp, Integer quality);

}