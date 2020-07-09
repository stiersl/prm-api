package com.stevenstier.prm.model;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;

public class VariableHistoryN {

	private Long varHistoryid;
	private Long varId;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'hh:mm:ss.SSS")
	private LocalDateTime sampleTime;
	private Double varValue;
	private Integer quality;

	public VariableHistoryN() {
	}

	public Long getVarHistoryid() {
		return varHistoryid;
	}

	public void setVarHistoryid(Long varHistoryid) {
		this.varHistoryid = varHistoryid;
	}

	public Long getVarId() {
		return varId;
	}

	public void setVarId(Long varId) {
		this.varId = varId;
	}

	public LocalDateTime getSampleTime() {
		return sampleTime;
	}

	public void setSampleTime(LocalDateTime sampleTime) {
		this.sampleTime = sampleTime;
	}

	public Double getVarValue() {
		return varValue;
	}

	public void setVarValue(Double varValue) {
		this.varValue = varValue;
	}

	public Integer getQuality() {
		return quality;
	}

	public void setQuality(Integer quality) {
		this.quality = quality;
	}

	@Override
	public String toString() {
		return "[varHistoryid=" + varHistoryid + ", varId=" + varId + ", sampleTime=" + sampleTime + ", varValue="
				+ varValue + ", quality=" + quality + "]";
	}
}