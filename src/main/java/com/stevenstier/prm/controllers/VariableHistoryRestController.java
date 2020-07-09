package com.stevenstier.prm.controllers;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.stevenstier.prm.model.Variable;
import com.stevenstier.prm.model.VariableHistoryN;
import com.stevenstier.prm.model.dao.VariableDao;
import com.stevenstier.prm.model.dao.VariableHistoryNDao;

@RestController
@CrossOrigin
@RequestMapping("/api/variableHistory")
public class VariableHistoryRestController {

	private VariableDao variableDao;
	private VariableHistoryNDao variableHistoryNDao;

	private static class HistorianValue {
		public long varId;
		public String varValue;
		public String timeStamp;
		public int quality;
	}

	@Autowired
	public VariableHistoryRestController(VariableDao variableDao, VariableHistoryNDao variableHistoryNDao) {
		this.variableDao = variableDao;
		this.variableHistoryNDao = variableHistoryNDao;
	}

	@PostMapping("/")
	@ResponseStatus(HttpStatus.CREATED)
	public Boolean createVariableHistory(@RequestBody HistorianValue newHistorianValue) {
		Boolean result = false;
		long varIdToInsert = newHistorianValue.varId;
		Instant SampleTimeToInsert = null;

		// get the variable from the variables table
		Variable existingVariable = variableDao.getVariablebyID(varIdToInsert);
		// check to see if one was found
		if ((existingVariable != null)) {
			// check to see if a variable value was actually provided.
			if (newHistorianValue.varValue != null) {

				try {
					// if a timestamp was provided then convert the received timestamp to an instant
					// (GMT0)
					if (newHistorianValue.timeStamp != null) {
						SampleTimeToInsert = Instant.parse(newHistorianValue.timeStamp);
					}
					// try to convert the value received into a double
					Double valueToInsert = Double.parseDouble(newHistorianValue.varValue);
					result = variableHistoryNDao.insertVarHistoryByVarId(varIdToInsert, SampleTimeToInsert,
							valueToInsert, newHistorianValue.quality);
				} catch (NumberFormatException e) {
					System.out.println("Error: " + LocalDateTime.now() + "|var_id:" + varIdToInsert + " varValue:("
							+ newHistorianValue.varValue + ") format failure");
				} catch (DateTimeParseException e) {
					System.out.println("Error: " + LocalDateTime.now() + "|var_id:" + varIdToInsert + " sampleTime:("
							+ newHistorianValue.timeStamp + ") parse failure");
				}
			} else {
				System.out
						.println("Error: " + LocalDateTime.now() + "|var_id:" + varIdToInsert + " varValue was null.");
			}
		} else {
			System.out.println("Error: " + LocalDateTime.now() + "|Existing var_id:" + varIdToInsert + " not found.");
		}

		if (!result) {
			throw new ResponseStatusException(HttpStatus.NOT_MODIFIED);
		}

		return result;
	}

	@GetMapping("/{varId}")
	public List<VariableHistoryN> getVariableHistoryN(@PathVariable int varId) {
		List<VariableHistoryN> result = new ArrayList<>();
		// first see if the variable actually exists in the variable table
		Variable existingVariable = variableDao.getVariablebyID(varId);

		if (existingVariable != null) {
			result = variableHistoryNDao.getAllVarHistoryVarId(varId);
		}
		if (result == null) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND);
		}
		return result;
	}
}
