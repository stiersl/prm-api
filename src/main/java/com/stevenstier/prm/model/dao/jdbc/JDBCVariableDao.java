package com.stevenstier.prm.model.dao.jdbc;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import com.stevenstier.prm.model.Variable;
import com.stevenstier.prm.model.dao.VariableDao;

@Component
public class JDBCVariableDao implements VariableDao {

	private JdbcTemplate jdbcTemplate;

	@Autowired
	public JDBCVariableDao(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}

	@Override
	public List<Variable> getAllVariables() {
		List<Variable> allVariables = new ArrayList<>();
		String sql = "SELECT * FROM Variable;";
		SqlRowSet results = jdbcTemplate.queryForRowSet(sql);
		while (results.next()) {
			Variable newVariableToAdd = createVariableFromRow(results);
			allVariables.add(newVariableToAdd);
		}
		return allVariables;
	}

	@Override
	public Variable getVariablebyID(long varId) {
		Variable variable = new Variable();
		String sql = "SELECT * FROM Variable WHERE varID = ?;";
		SqlRowSet results = jdbcTemplate.queryForRowSet(sql, varId);
		while (results.next()) {
			variable = createVariableFromRow(results);
		}
		return variable;
	}

	@Override
	public Variable getVariablebyName(String varName) {
		Variable variable = new Variable();
		String sql = "SELECT * FROM Variable WHERE varName = ?;";
		SqlRowSet results = jdbcTemplate.queryForRowSet(sql, varName);
		while (results.next()) {
			variable = createVariableFromRow(results);
		}
		return variable;
	}

	@Override
	public Variable createVariable(Variable var) {

		String sqlInsert = "INSERT INTO Variable (varName,serverID,varDesc,varDescG,varType,"
				+ "engUnits,precison,maxScale,minScale,snapshotRate,snapshotTreshold"
				+ ") VALUES (?,?,?,?,?,?,?,?,?,?,?) RETURNING varId;";

		SqlRowSet results;
		try {
			results = jdbcTemplate.queryForRowSet(sqlInsert, var.getVarName(), var.getServerId(), var.getVarDesc(),
					var.getVarDescG(), var.getVarType(), var.getEngUnits(), var.getPrecison(), var.getMaxScale(),
					var.getMinScale(), var.getSnapshotRate(), var.getSnapshotTreshold());
			if (results.next()) {
				var.setVarId(results.getLong("varId"));
			}
		} catch (DataAccessException e) {

		}
		return var;
	}

	@Override
	public Boolean updateVariable(Variable var) {
		Boolean result = false;
		String sqlInsert = "UPDATE Variable SET varName=?,serverID=?,varDesc=?,varDescG=?,varType=?,"
				+ "engUnits=?,precison=?,maxScale=?,minScale=?,snapshotRate=?,snapshotTreshold=?,active=?"
				+ " WHERE varId = ? ";

		try {
			jdbcTemplate.update(sqlInsert, var.getVarName(), var.getServerId(), var.getVarDesc(), var.getVarDescG(),
					var.getVarType(), var.getEngUnits(), var.getPrecison(), var.getMaxScale(), var.getMinScale(),
					var.getSnapshotRate(), var.getSnapshotTreshold(), var.getActive(), var.getVarId());
			result = true;
		} catch (DataAccessException e) {

		}
		return result;
	}

	@Override
	public void updatelastValue(long varId, String lastValue, LocalDateTime timeStamp, Integer quality) {

		String sqlInsert = "UPDATE Variable SET lastValue=?,lastSampleTime=?,lastQuality=? " + " WHERE varId = ? ";

		jdbcTemplate.update(sqlInsert, lastValue, timeStamp, quality, varId);
	}

	@Override
	public void deactivateVariable(long varId) {
		String sqlInsert = "UPDATE Variable SET active=? WHERE varId = ? ";
		jdbcTemplate.update(sqlInsert, false, varId);
	}

	@Override
	public Boolean deleteVariable(long varId) {
		boolean result = false;

		String sqldelete1 = "DELETE FROM VariableHistoryN WHERE varId = ?;";
		String sqldelete2 = "DELETE FROM Variable WHERE varId = ? ";
		try {
			jdbcTemplate.update(sqldelete1, varId);
			jdbcTemplate.update(sqldelete2, varId);
			result = true;
		} catch (DataAccessException e) {
		}

		return result;
	}

	private Variable createVariableFromRow(SqlRowSet results) {
		Variable newVariable = new Variable();
		newVariable.setVarId(results.getLong("varId"));
		newVariable.setVarName(results.getString("varName"));
		newVariable.setServerId(readValueReturnInteger(results, "serverId"));
		newVariable.setVarDesc(results.getString("varDesc"));
		newVariable.setVarDescG(results.getString("varDescG"));
		newVariable.setVarType(results.getString("varType"));
		newVariable.setEngUnits(results.getString("engUnits"));
		newVariable.setPrecison(readValueReturnInteger(results, "precison"));
		newVariable.setMaxScale(readValueReturnDouble(results, "maxScale"));
		newVariable.setMinScale(readValueReturnDouble(results, "minScale"));
		newVariable.setSnapshotRate(readValueReturnInteger(results, "snapshotRate"));
		newVariable.setSnapshotTreshold(readValueReturnDouble(results, "snapShotTreshold"));
		newVariable.setLastValue(results.getString("lastValue"));
		newVariable.setLastSampleTime(readValueReturnLDT(results, "lastSampleTime"));
		newVariable.setLastQuality(readValueReturnInteger(results, "lastQuality"));
		newVariable.setActive(readValueReturnBoolean(results, "active"));

		return newVariable;
	}

	private Double readValueReturnDouble(SqlRowSet srs, String columnName) {
		String s = srs.getString(columnName);
		return (s != null) ? Double.parseDouble(s) : null;
	}

	private Integer readValueReturnInteger(SqlRowSet srs, String columnName) {
		String s = srs.getString(columnName);
		return (s != null) ? Integer.parseInt(s) : null;
	}

	private Boolean readValueReturnBoolean(SqlRowSet srs, String columnName) {
		String s = srs.getString(columnName);
		return (s != null) ? Boolean.parseBoolean(s) : null;
	}

	private LocalDateTime readValueReturnLDT(SqlRowSet srs, String columnName) {
		Timestamp sqlTimestamp = srs.getTimestamp(columnName);
		return (sqlTimestamp != null) ? sqlTimestamp.toLocalDateTime() : null;
	}

}
