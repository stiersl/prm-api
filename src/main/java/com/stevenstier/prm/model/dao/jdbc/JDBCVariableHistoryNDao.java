package com.stevenstier.prm.model.dao.jdbc;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import com.stevenstier.prm.model.VariableHistoryN;
import com.stevenstier.prm.model.dao.VariableHistoryNDao;

@Component
public class JDBCVariableHistoryNDao implements VariableHistoryNDao {

	private JdbcTemplate jdbcTemplate;

	@Autowired
	public JDBCVariableHistoryNDao(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}

	@Override
	public List<VariableHistoryN> getAllVarHistoryVarId(long varId) {
		List<VariableHistoryN> allVariableHistory = new ArrayList<>();
		String sql = "SELECT varHistoryId, varId, sampleTime, varValue, quality " 
				+ " FROM VariableHistoryN"
				+ " WHERE varId = ? ORDER BY SampleTime DESC;";
		SqlRowSet results = jdbcTemplate.queryForRowSet(sql, varId);
		while (results.next()) {
			VariableHistoryN newItem = createItemFromRow(results);
			allVariableHistory.add(newItem);
		}
		return allVariableHistory;
	}

	@Override
	public List<VariableHistoryN> getAllVarHistoryVarName(String varName) {

		List<VariableHistoryN> allVariableHistory = new ArrayList<>();
		String sql = "SELECT  varHistoryId, varId, sampleTime, varValue, quality" 
				+ " FROM VariableHistoryN"
				+ " WHERE varId = (SELECT varId from variable where varName = ?) ORDER BY SampleTime DESC";
		SqlRowSet results = jdbcTemplate.queryForRowSet(sql, varName);
		while (results.next()) {
			VariableHistoryN newItem = createItemFromRow(results);
			allVariableHistory.add(newItem);
		}
		return allVariableHistory;
	}

	@Override
	public List<VariableHistoryN> getVarHistoryByVarId(long varId, LocalDateTime startTime, LocalDateTime endTime) {
		List<VariableHistoryN> allVariableHistory = new ArrayList<>();
		String sql = "SELECT varHistoryId, varId, sampleTime, varValue, quality " 
					+ " FROM VariableHistoryN"
					+ " WHERE varId = ? AND (sampleTime >= ?) AND (sampleTime <= ?) ORDER BY SampleTime DESC;";
		SqlRowSet results = jdbcTemplate.queryForRowSet(sql, varId, startTime, endTime);
		while (results.next()) {
			VariableHistoryN newItem = createItemFromRow(results);
			allVariableHistory.add(newItem);
		}
		return allVariableHistory;
	}

	@Override
	public List<VariableHistoryN> getVarHistoryByVarName(String varName, LocalDateTime startTime,
			LocalDateTime endTime) {

		List<VariableHistoryN> allVariableHistory = new ArrayList<>();
		String sql = "SELECT varHistoryId, varId, sampleTime, varValue, quality " 
				+ " FROM VariableHistoryN"
				+ " WHERE (varId = (SELECT varId from variable where varName = ?)) "
				+ " AND (sampleTime >= ?) AND (sampleTime <= ?) ORDER BY SampleTime DESC;";
		SqlRowSet results = jdbcTemplate.queryForRowSet(sql, varName, startTime, endTime);
		while (results.next()) {
			VariableHistoryN newItem = createItemFromRow(results);
			allVariableHistory.add(newItem);
		}
		return allVariableHistory;
	}

	@Override
	public boolean insertVarHistoryByVarId(long varId, Instant sampleTime, Double varValue, Integer quality) {
		boolean result = false;
		// default the quality to 192 if not provided
		if (quality == null)
			quality = 192;
		// default the sample time to server time if not provided.
		if (sampleTime == null)
			sampleTime = Instant.now();

		OffsetDateTime odt = sampleTime.atOffset(ZoneOffset.UTC);

		String sqlInsert = "INSERT INTO VariableHistoryN (varId,sampleTime,varValue,Quality"
				+ ") VALUES (?,?,?,?) RETURNING varHistoryId;";
		SqlRowSet results;
		try {
			results = jdbcTemplate.queryForRowSet(sqlInsert, varId, odt, varValue, quality);
			if (results.next()) {

				// update the row in variable table
				String sqlUpdate = "UPDATE Variable SET lastValue = ?, lastSampleTime = ?, lastQuality = ? "
						+ " where varId = ?";
				jdbcTemplate.update(sqlUpdate, String.valueOf(varValue), odt, quality, varId);
				result = true;
			}
		} catch (DataAccessException e) {
			e.printStackTrace();
		}
		return result;
	}

	@Override
	public boolean insertVarHistoryByVarName(String varName, Instant sampleTime, Double varValue, Integer quality) {
		boolean result = false;
		// default the quality to 192 if not provided
		if (quality == null)
			quality = 192;
		// default the sample time to server time if not provided.
		if (sampleTime == null)
			sampleTime = Instant.now();
		OffsetDateTime odt = sampleTime.atOffset(ZoneOffset.UTC);

		String sqlInsert = "INSERT INTO VariableHistoryN (varId,sampleTime,varValue,Quality"
				+ ") VALUES ((SELECT varId FROM VARIABLE WHERE VarName=?),?,?,?) RETURNING varHistoryId;";
		SqlRowSet results;
		try {
			results = jdbcTemplate.queryForRowSet(sqlInsert, varName, odt, varValue, quality);
			if (results.next()) {
				// update the row in variable table
				String sqlUpdate = "UPDATE Variable SET lastValue = ?, lastSampleTime = ?, lastQuality = ? "
						+ " where VarName = ?";
				jdbcTemplate.update(sqlUpdate, String.valueOf(varValue), odt, quality, varName);
				result = true;
			}
		} catch (DataAccessException e) {
			e.printStackTrace();
		}
		return result;
	}

	@Override
	public VariableHistoryN getVarHistoryNowByVarId(long varId) {

		VariableHistoryN variableHistoryN = new VariableHistoryN();

		String sql = "SELECT varId, lastValue, lastSampleTime, lastQuality " + " FROM Variable WHERE (varId = ?);";
		SqlRowSet results = jdbcTemplate.queryForRowSet(sql, varId);
		while (results.next()) {
			variableHistoryN.setVarId(results.getLong("varId"));
			variableHistoryN.setSampleTime(readValueReturnLDT(results, "lastSampleTime"));
			variableHistoryN.setVarValue(readValueReturnDouble(results, "lastValue"));
			variableHistoryN.setQuality(readValueReturnInteger(results, "lastQuality"));
		}
		return variableHistoryN;
	}

	@Override
	public VariableHistoryN getVarHistoryNowByVarName(String varName) {
		VariableHistoryN variableHistoryN = new VariableHistoryN();

		String sql = "SELECT varId, lastValue, lastSampleTime, lastQuality " + " FROM Variable WHERE (varName = ?);";
		SqlRowSet results = jdbcTemplate.queryForRowSet(sql, varName);
		while (results.next()) {
			variableHistoryN.setVarId(results.getLong("varId"));
			variableHistoryN.setSampleTime(readValueReturnLDT(results, "lastSampleTime"));
			variableHistoryN.setVarValue(readValueReturnDouble(results, "lastValue"));
			variableHistoryN.setQuality(readValueReturnInteger(results, "lastQuality"));
		}
		return variableHistoryN;
	}

	@Override
	public boolean deleteVarHistoryByVarId(long varId) {
		Boolean result = false;
		String sqlUpdate = "DELETE from VariableHistoryN where varId = ?;";
		try {
			jdbcTemplate.update(sqlUpdate, varId);
			result = true;
		} catch (DataAccessException e) {

		}
		return result;
	}

	@Override
	public boolean deleteVarHistoryByVarName(String varName) {
		Boolean result = false;
		String sqlUpdate = "DELETE FROM VariableHistoryN WHERE varId = (SELECT varId FROM variable WHERE varName = ?);";
		try {
			jdbcTemplate.update(sqlUpdate, varName);
			result = true;
		} catch (DataAccessException e) {

		}
		return result;
	}

	private VariableHistoryN createItemFromRow(SqlRowSet results) {
		VariableHistoryN newVariableHistory = new VariableHistoryN();

		newVariableHistory.setVarHistoryid(results.getLong("varHistoryid"));
		newVariableHistory.setVarId(results.getLong("varId"));
		newVariableHistory.setSampleTime(readValueReturnLDT(results, "sampleTime"));
		newVariableHistory.setVarValue(readValueReturnDouble(results, "varValue"));
		newVariableHistory.setQuality(readValueReturnInteger(results, "quality"));

		return newVariableHistory;
	}

	private Double readValueReturnDouble(SqlRowSet srs, String columnName) {
		String s = srs.getString(columnName);
		return (s != null) ? Double.parseDouble(s) : null;
	}

	private Integer readValueReturnInteger(SqlRowSet srs, String columnName) {
		String s = srs.getString(columnName);
		return (s != null) ? Integer.parseInt(s) : null;
	}

	private LocalDateTime readValueReturnLDT(SqlRowSet srs, String columnName) {
		Timestamp sqlTimestamp = srs.getTimestamp(columnName);
		return (sqlTimestamp != null) ? sqlTimestamp.toLocalDateTime() : null;
	}

}
