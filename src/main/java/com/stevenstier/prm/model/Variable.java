package com.stevenstier.prm.model;

import java.time.LocalDateTime;
import java.util.Objects;

import javax.validation.constraints.NotBlank;

import com.fasterxml.jackson.annotation.JsonFormat;

public class Variable {

	private Long varId;

	@NotBlank(message = "A variable Name is required.")
	private String varName;
	private Integer serverId;
	private String varDesc;
	private String varDescG;
	private String varType;
	private String engUnits;
	private Integer precison;
	private Double maxScale;
	private Double minScale;
	private Integer snapshotRate;
	private Double snapshotTreshold;
	private String lastValue;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'hh:mm:ss.SSS")
	private LocalDateTime lastSampleTime;
	private Integer lastQuality;
	private Boolean active;

	public Long getVarId() {
		return varId;
	}

	public void setVarId(Long varId) {
		this.varId = varId;
	}

	public String getVarName() {
		return varName;
	}

	public void setVarName(String varName) {
		this.varName = varName;
	}

	public Integer getServerId() {
		return serverId;
	}

	public void setServerId(Integer serverId) {
		this.serverId = serverId;
	}

	public String getVarDesc() {
		return varDesc;
	}

	public void setVarDesc(String varDesc) {
		this.varDesc = varDesc;
	}

	public String getVarDescG() {
		return varDescG;
	}

	public void setVarDescG(String varDescG) {
		this.varDescG = varDescG;
	}

	public String getVarType() {
		return varType;
	}

	public void setVarType(String varType) {
		this.varType = varType;
	}

	public String getEngUnits() {
		return engUnits;
	}

	public void setEngUnits(String engUnits) {
		this.engUnits = engUnits;
	}

	public Integer getPrecison() {
		return precison;
	}

	public void setPrecison(Integer precison) {
		this.precison = precison;
	}

	public Double getMaxScale() {
		return maxScale;
	}

	public void setMaxScale(Double maxScale) {
		this.maxScale = maxScale;
	}

	public Double getMinScale() {
		return minScale;
	}

	public void setMinScale(Double minScale) {
		this.minScale = minScale;
	}

	public Integer getSnapshotRate() {
		return snapshotRate;
	}

	public void setSnapshotRate(Integer snapshotRate) {
		this.snapshotRate = snapshotRate;
	}

	public Double getSnapshotTreshold() {
		return snapshotTreshold;
	}

	public void setSnapshotTreshold(Double snapshotTreshold) {
		this.snapshotTreshold = snapshotTreshold;
	}

	public String getLastValue() {
		return lastValue;
	}

	public void setLastValue(String lastValue) {
		this.lastValue = lastValue;
	}

	public LocalDateTime getLastSampleTime() {
		return lastSampleTime;
	}

	public void setLastSampleTime(LocalDateTime lastSampleTime) {
		this.lastSampleTime = lastSampleTime;
	}

	public Integer getLastQuality() {
		return lastQuality;
	}

	public void setLastQuality(Integer lastQuality) {
		this.lastQuality = lastQuality;
	}

	public Boolean getActive() {
		return active;
	}

	public void setActive(Boolean active) {
		this.active = active;
	}

	@Override
	public int hashCode() {
		return Objects.hash(varId, varName);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof Variable)) {
			return false;
		}
		Variable other = (Variable) obj;
		return Objects.equals(varId, other.varId) && Objects.equals(varName, other.varName);
	}

	@Override
	public String toString() {
		return "Variable [varId=" + varId + ", varName=" + varName + ", varDesc=" + varDesc + ", varType=" + varType
				+ ", engUnits=" + engUnits + ", precison=" + precison + ", maxScale=" + maxScale + ", minScale="
				+ minScale + ", snapshotRate=" + snapshotRate + ", snapshotTreshold=" + snapshotTreshold + ", active="
				+ active + "]";
	}

}
