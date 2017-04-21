package com.example;

import java.io.Serializable;

import org.apache.commons.lang3.builder.ToStringBuilder;

import com.fasterxml.jackson.annotation.JsonProperty;

public class CaptchaResponse implements Serializable {

	private static final long serialVersionUID = -206863049857093922L;
	
	@JsonProperty("success")
	private Boolean success;
	@JsonProperty("challenge_ts")
	private String challengeTimestamp;
	@JsonProperty("hostname")
	private String hostName;
	@JsonProperty("error-codes")
	private String[] errorCodes;
	
	public Boolean getSuccess() {
		return success;
	}
	public void setSuccess(Boolean success) {
		this.success = success;
	}
	public String getChallengeTimestamp() {
		return challengeTimestamp;
	}
	public void setChallengeTimestamp(String challengeTimestamp) {
		this.challengeTimestamp = challengeTimestamp;
	}
	public String getHostName() {
		return hostName;
	}
	public void setHostName(String hostName) {
		this.hostName = hostName;
	}
	public String[] getErrorCodes() {
		return errorCodes;
	}
	public void setErrorCodes(String[] errorCodes) {
		this.errorCodes = errorCodes;
	}
	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
	
}
