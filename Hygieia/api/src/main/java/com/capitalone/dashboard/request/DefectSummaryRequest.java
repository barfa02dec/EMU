package com.capitalone.dashboard.request;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

public class DefectSummaryRequest {
	
	@NotNull
	private String projectName;
	
	@NotNull
	private String metricsProjectId;
	
	@NotNull
	private String projectId;
	
	@NotNull
	private String valueAsOn;
	
	@Min(value=0, message = "value must be greater than or equal to zero" )
	private int lowPriorityDefectsCount;
	
	@Min(value=0, message = "value must be greater than or equal to zero" )
	private int mediumPriorityDefectsCount;
	
	@Min(value=0, message = "value must be greater than or equal to zero" )
	private int highPriorityDefectsCount;
	
	@Min(value=0, message = "value must be greater than or equal to zero" )
	private int criticalPriorityDefectsCount;
	
	@Min(value=0, message = "value must be greater than or equal to zero" )
	private int QA_DefectsCount;
	
	@Min(value=0, message = "value must be greater than or equal to zero" )
	private int UAT_DefectsCount;
	
	@Min(value=0, message = "value must be greater than or equal to zero" )
	private int PROD_DefectsCount;
	
	@Min(value=0, message = "value must be greater than or equal to zero" )
	private int openDefectsWithHighPriorityAndAgeLessThanOrEQ15Days;
	
	@Min(value=0, message = "value must be greater than or equal to zero" )
	private int openDefectsWithLowPriorityAndAgeLessThanOrEQ15Days;
	
	@Min(value=0, message = "value must be greater than or equal to zero" )
	private int openDefectsWithMediumPriorityAndAgeLessThanOrEQ15Days;
	
	@Min(value=0, message = "value must be greater than or equal to zero" )
	private int openDefectsWithCriticalPriorityAndAgeLessThanOrEQ15Days;
	
	
	@Min(value=0, message = "value must be greater than or equal to zero" )
	private int openDefectsWithHighPriorityAndAgeBetween15To30Days;
	
	@Min(value=0, message = "value must be greater than or equal to zero" )
	private int openDefectsWithLowPriorityAndAgeBetween15To30Days;
	

	@Min(value=0, message = "value must be greater than or equal to zero" )
	private int openDefectsWithMediumPriorityAndAgeBetween15To30Days;
	
	@Min(value=0, message = "value must be greater than or equal to zero" )
	private int openDefectsWithCriticalPriorityAndAgeBetween15To30Days;
	
	@Min(value=0, message = "value must be greater than or equal to zero" )
	private int openDefectsWithHighPriorityAndAgeBetween30To60Days;
	
	@Min(value=0, message = "value must be greater than or equal to zero" )
	private int openDefectsWithLowPriorityAndAgeBetween30To60Days;
	
	@Min(value=0, message = "value must be greater than or equal to zero" )
	private int openDefectsWithMediumPriorityAndAgeBetween30To60Days;
	
	@Min(value=0, message = "value must be greater than or equal to zero" )
	private int openDefectsWithCriticalPriorityAndAgeBetween30To60Days;
	
	
	@Min(value=0, message = "value must be greater than or equal to zero" )
	private int openDefectsWithHighPriorityAndAgeBetween60To90Days;
	
	@Min(value=0, message = "value must be greater than or equal to zero" )
	private int openDefectsWithLowPriorityAndAgeBetween60To90Days;
	
	@Min(value=0, message = "value must be greater than or equal to zero" )
	private int openDefectsWithMediumPriorityAndAgeBetween60To90Days;
	
	@Min(value=0, message = "value must be greater than or equal to zero" )
	private int openDefectsWithCriticalPriorityAndAgeBetween60To90Days;
	
	@Min(value=0, message = "value must be greater than or equal to zero" )
	private int openDefectsWithHighPriorityAndAgeGreaterThan90;
	
	@Min(value=0, message = "value must be greater than or equal to zero" )
	private int openDefectsWithLowPriorityAndAgeGreaterThan90;
	
	@Min(value=0, message = "value must be greater than or equal to zero" )
	private int openDefectsWithMediumPriorityAndAgeGreaterThan90;
	
	@Min(value=0, message = "value must be greater than or equal to zero" )
	private int openDefectsWithCriticalPriorityAndAgeGreaterThan90;
	
	
	@Min(value=0, message = "value must be greater than or equal to zero" )
	private int fixedDefectsWithHighPriorityAndResolutionLessThanOrEQ15Days;
	
	@Min(value=0, message = "value must be greater than or equal to zero" )
	private int fixedDefectsWithLowPriorityAndResolutionLessThanOrEQ15Days;
	
	@Min(value=0, message = "value must be greater than or equal to zero" )
	private int fixedDefectsWithMediumPriorityAndResolutionLessThanOrEQ15Days;
	
	@Min(value=0, message = "value must be greater than or equal to zero" )
	private int fixedDefectsWithCriticalPriorityAndResolutionLessThanOrEQ15Days;
	
	
	@Min(value=0, message = "value must be greater than or equal to zero" )
	private int fixedDefectsWithHighPriorityAndResolutionBetween15To30Days;
	
	@Min(value=0, message = "value must be greater than or equal to zero" )
	private int fixedDefectsWithLowPriorityAndResolutionBetween15To30Days;
	
	@Min(value=0, message = "value must be greater than or equal to zero" )
	private int fixedDefectsWithMediumPriorityAndResolutionBetween15To30Days;
	
	@Min(value=0, message = "value must be greater than or equal to zero" )
	private int fixedDefectsWithCriticalPriorityAndResolutionBetween15To30Days;
	
	
	@Min(value=0, message = "value must be greater than or equal to zero" )
	private int fixedDefectsWithHighPriorityAndResolutionBetween30To60Days;
	
	@Min(value=0, message = "value must be greater than or equal to zero" )
	private int fixedDefectsWithLowPriorityAndResolutionBetween30To60Days;
	
	@Min(value=0, message = "value must be greater than or equal to zero" )
	private int fixedDefectsWithMediumPriorityAndResolutionBetween30To60Days;
	
	@Min(value=0, message = "value must be greater than or equal to zero" )
	private int fixedDefectsWithCriticalPriorityAndResolutionBetween30To60Days;
	
	
	@Min(value=0, message = "value must be greater than or equal to zero" )
	private int fixedDefectsWithHighPriorityAndResolutionBetween60To90Days;
	
	@Min(value=0, message = "value must be greater than or equal to zero" )
	private int fixedDefectsWithLowPriorityAndResolutionBetween60To90Days;
	
	@Min(value=0, message = "value must be greater than or equal to zero" )
	private int fixedDefectsWithMediumPriorityAndResolutionBetween60To90Days;
	
	@Min(value=0, message = "value must be greater than or equal to zero" )
	private int fixedDefectsWithCriticalPriorityAndResolutionBetween60To90Days;
	
	
	@Min(value=0, message = "value must be greater than or equal to zero" )
	private int fixedDefectsWithHighPriorityAndResolutionGreaterThan90;
	
	@Min(value=0, message = "value must be greater than or equal to zero" )
	private int fixedDefectsWithLowPriorityAndResolutionGreaterThan90;
	
	@Min(value=0, message = "value must be greater than or equal to zero" )
	private int fixedDefectsWithMediumPriorityAndResolutionGreaterThan90;
	
	@Min(value=0, message = "value must be greater than or equal to zero" )
	private int fixedDefectsWithCriticalPriorityAndResolutionGreaterThan90;
	
	
	public int getLowPriorityDefectsCount() {
		return lowPriorityDefectsCount;
	}
	public void setLowPriorityDefectsCount(int lowPriorityDefectsCount) {
		this.lowPriorityDefectsCount = lowPriorityDefectsCount;
	}
	public int getMediumPriorityDefectsCount() {
		return mediumPriorityDefectsCount;
	}
	public void setMediumPriorityDefectsCount(int mediumPriorityDefectsCount) {
		this.mediumPriorityDefectsCount = mediumPriorityDefectsCount;
	}
	public int getHighPriorityDefectsCount() {
		return highPriorityDefectsCount;
	}
	public void setHighPriorityDefectsCount(int highPriorityDefectsCount) {
		this.highPriorityDefectsCount = highPriorityDefectsCount;
	}
	public int getCriticalPriorityDefectsCount() {
		return criticalPriorityDefectsCount;
	}
	public void setCriticalPriorityDefectsCount(int criticalPriorityDefectsCount) {
		this.criticalPriorityDefectsCount = criticalPriorityDefectsCount;
	}
	public int getQA_DefectsCount() {
		return QA_DefectsCount;
	}
	public void setQA_DefectsCount(int qA_DefectsCount) {
		QA_DefectsCount = qA_DefectsCount;
	}
	public int getUAT_DefectsCount() {
		return UAT_DefectsCount;
	}
	public void setUAT_DefectsCount(int uAT_DefectsCount) {
		UAT_DefectsCount = uAT_DefectsCount;
	}
	public int getPROD_DefectsCount() {
		return PROD_DefectsCount;
	}
	public void setPROD_DefectsCount(int pROD_DefectsCount) {
		PROD_DefectsCount = pROD_DefectsCount;
	}
	public int getOpenDefectsWithHighPriorityAndAgeLessThanOrEQ15Days() {
		return openDefectsWithHighPriorityAndAgeLessThanOrEQ15Days;
	}
	public void setOpenDefectsWithHighPriorityAndAgeLessThanOrEQ15Days(
			int openDefectsWithHighPriorityAndAgeLessThanOrEQ15Days) {
		this.openDefectsWithHighPriorityAndAgeLessThanOrEQ15Days = openDefectsWithHighPriorityAndAgeLessThanOrEQ15Days;
	}
	public int getOpenDefectsWithLowPriorityAndAgeLessThanOrEQ15Days() {
		return openDefectsWithLowPriorityAndAgeLessThanOrEQ15Days;
	}
	public void setOpenDefectsWithLowPriorityAndAgeLessThanOrEQ15Days(
			int openDefectsWithLowPriorityAndAgeLessThanOrEQ15Days) {
		this.openDefectsWithLowPriorityAndAgeLessThanOrEQ15Days = openDefectsWithLowPriorityAndAgeLessThanOrEQ15Days;
	}
	public int getOpenDefectsWithMediumPriorityAndAgeLessThanOrEQ15Days() {
		return openDefectsWithMediumPriorityAndAgeLessThanOrEQ15Days;
	}
	public void setOpenDefectsWithMediumPriorityAndAgeLessThanOrEQ15Days(
			int openDefectsWithMediumPriorityAndAgeLessThanOrEQ15Days) {
		this.openDefectsWithMediumPriorityAndAgeLessThanOrEQ15Days = openDefectsWithMediumPriorityAndAgeLessThanOrEQ15Days;
	}
	public int getOpenDefectsWithCriticalPriorityAndAgeLessThanOrEQ15Days() {
		return openDefectsWithCriticalPriorityAndAgeLessThanOrEQ15Days;
	}
	public void setOpenDefectsWithCriticalPriorityAndAgeLessThanOrEQ15Days(
			int openDefectsWithCriticalPriorityAndAgeLessThanOrEQ15Days) {
		this.openDefectsWithCriticalPriorityAndAgeLessThanOrEQ15Days = openDefectsWithCriticalPriorityAndAgeLessThanOrEQ15Days;
	}
	public int getOpenDefectsWithHighPriorityAndAgeBetween15To30Days() {
		return openDefectsWithHighPriorityAndAgeBetween15To30Days;
	}
	public void setOpenDefectsWithHighPriorityAndAgeBetween15To30Days(
			int openDefectsWithHighPriorityAndAgeBetween15To30Days) {
		this.openDefectsWithHighPriorityAndAgeBetween15To30Days = openDefectsWithHighPriorityAndAgeBetween15To30Days;
	}
	public int getOpenDefectsWithLowPriorityAndAgeBetween15To30Days() {
		return openDefectsWithLowPriorityAndAgeBetween15To30Days;
	}
	public void setOpenDefectsWithLowPriorityAndAgeBetween15To30Days(
			int openDefectsWithLowPriorityAndAgeBetween15To30Days) {
		this.openDefectsWithLowPriorityAndAgeBetween15To30Days = openDefectsWithLowPriorityAndAgeBetween15To30Days;
	}
	public int getOpenDefectsWithMediumPriorityAndAgeBetween15To30Days() {
		return openDefectsWithMediumPriorityAndAgeBetween15To30Days;
	}
	public void setOpenDefectsWithMediumPriorityAndAgeBetween15To30Days(
			int openDefectsWithMediumPriorityAndAgeBetween15To30Days) {
		this.openDefectsWithMediumPriorityAndAgeBetween15To30Days = openDefectsWithMediumPriorityAndAgeBetween15To30Days;
	}
	public int getOpenDefectsWithCriticalPriorityAndAgeBetween15To30Days() {
		return openDefectsWithCriticalPriorityAndAgeBetween15To30Days;
	}
	public void setOpenDefectsWithCriticalPriorityAndAgeBetween15To30Days(
			int openDefectsWithCriticalPriorityAndAgeBetween15To30Days) {
		this.openDefectsWithCriticalPriorityAndAgeBetween15To30Days = openDefectsWithCriticalPriorityAndAgeBetween15To30Days;
	}
	public int getOpenDefectsWithHighPriorityAndAgeBetween30To60Days() {
		return openDefectsWithHighPriorityAndAgeBetween30To60Days;
	}
	public void setOpenDefectsWithHighPriorityAndAgeBetween30To60Days(
			int openDefectsWithHighPriorityAndAgeBetween30To60Days) {
		this.openDefectsWithHighPriorityAndAgeBetween30To60Days = openDefectsWithHighPriorityAndAgeBetween30To60Days;
	}
	public int getOpenDefectsWithLowPriorityAndAgeBetween30To60Days() {
		return openDefectsWithLowPriorityAndAgeBetween30To60Days;
	}
	public void setOpenDefectsWithLowPriorityAndAgeBetween30To60Days(
			int openDefectsWithLowPriorityAndAgeBetween30To60Days) {
		this.openDefectsWithLowPriorityAndAgeBetween30To60Days = openDefectsWithLowPriorityAndAgeBetween30To60Days;
	}
	public int getOpenDefectsWithMediumPriorityAndAgeBetween30To60Days() {
		return openDefectsWithMediumPriorityAndAgeBetween30To60Days;
	}
	public void setOpenDefectsWithMediumPriorityAndAgeBetween30To60Days(
			int openDefectsWithMediumPriorityAndAgeBetween30To60Days) {
		this.openDefectsWithMediumPriorityAndAgeBetween30To60Days = openDefectsWithMediumPriorityAndAgeBetween30To60Days;
	}
	public int getOpenDefectsWithCriticalPriorityAndAgeBetween30To60Days() {
		return openDefectsWithCriticalPriorityAndAgeBetween30To60Days;
	}
	public void setOpenDefectsWithCriticalPriorityAndAgeBetween30To60Days(
			int openDefectsWithCriticalPriorityAndAgeBetween30To60Days) {
		this.openDefectsWithCriticalPriorityAndAgeBetween30To60Days = openDefectsWithCriticalPriorityAndAgeBetween30To60Days;
	}
	public int getOpenDefectsWithHighPriorityAndAgeBetween60To90Days() {
		return openDefectsWithHighPriorityAndAgeBetween60To90Days;
	}
	public void setOpenDefectsWithHighPriorityAndAgeBetween60To90Days(
			int openDefectsWithHighPriorityAndAgeBetween60To90Days) {
		this.openDefectsWithHighPriorityAndAgeBetween60To90Days = openDefectsWithHighPriorityAndAgeBetween60To90Days;
	}
	public int getOpenDefectsWithLowPriorityAndAgeBetween60To90Days() {
		return openDefectsWithLowPriorityAndAgeBetween60To90Days;
	}
	public void setOpenDefectsWithLowPriorityAndAgeBetween60To90Days(
			int openDefectsWithLowPriorityAndAgeBetween60To90Days) {
		this.openDefectsWithLowPriorityAndAgeBetween60To90Days = openDefectsWithLowPriorityAndAgeBetween60To90Days;
	}
	public int getOpenDefectsWithMediumPriorityAndAgeBetween60To90Days() {
		return openDefectsWithMediumPriorityAndAgeBetween60To90Days;
	}
	public void setOpenDefectsWithMediumPriorityAndAgeBetween60To90Days(
			int openDefectsWithMediumPriorityAndAgeBetween60To90Days) {
		this.openDefectsWithMediumPriorityAndAgeBetween60To90Days = openDefectsWithMediumPriorityAndAgeBetween60To90Days;
	}
	public int getOpenDefectsWithCriticalPriorityAndAgeBetween60To90Days() {
		return openDefectsWithCriticalPriorityAndAgeBetween60To90Days;
	}
	public void setOpenDefectsWithCriticalPriorityAndAgeBetween60To90Days(
			int openDefectsWithCriticalPriorityAndAgeBetween60To90Days) {
		this.openDefectsWithCriticalPriorityAndAgeBetween60To90Days = openDefectsWithCriticalPriorityAndAgeBetween60To90Days;
	}
	public int getOpenDefectsWithHighPriorityAndAgeGreaterThan90() {
		return openDefectsWithHighPriorityAndAgeGreaterThan90;
	}
	public void setOpenDefectsWithHighPriorityAndAgeGreaterThan90(int openDefectsWithHighPriorityAndAgeGreaterThan90) {
		this.openDefectsWithHighPriorityAndAgeGreaterThan90 = openDefectsWithHighPriorityAndAgeGreaterThan90;
	}
	public int getOpenDefectsWithLowPriorityAndAgeGreaterThan90() {
		return openDefectsWithLowPriorityAndAgeGreaterThan90;
	}
	public void setOpenDefectsWithLowPriorityAndAgeGreaterThan90(int openDefectsWithLowPriorityAndAgeGreaterThan90) {
		this.openDefectsWithLowPriorityAndAgeGreaterThan90 = openDefectsWithLowPriorityAndAgeGreaterThan90;
	}
	public int getOpenDefectsWithMediumPriorityAndAgeGreaterThan90() {
		return openDefectsWithMediumPriorityAndAgeGreaterThan90;
	}
	public void setOpenDefectsWithMediumPriorityAndAgeGreaterThan90(int openDefectsWithMediumPriorityAndAgeGreaterThan90) {
		this.openDefectsWithMediumPriorityAndAgeGreaterThan90 = openDefectsWithMediumPriorityAndAgeGreaterThan90;
	}
	public int getOpenDefectsWithCriticalPriorityAndAgeGreaterThan90() {
		return openDefectsWithCriticalPriorityAndAgeGreaterThan90;
	}
	public void setOpenDefectsWithCriticalPriorityAndAgeGreaterThan90(
			int openDefectsWithCriticalPriorityAndAgeGreaterThan90) {
		this.openDefectsWithCriticalPriorityAndAgeGreaterThan90 = openDefectsWithCriticalPriorityAndAgeGreaterThan90;
	}
	public int getFixedDefectsWithHighPriorityAndResolutionLessThanOrEQ15Days() {
		return fixedDefectsWithHighPriorityAndResolutionLessThanOrEQ15Days;
	}
	public void setFixedDefectsWithHighPriorityAndResolutionLessThanOrEQ15Days(
			int fixedDefectsWithHighPriorityAndResolutionLessThanOrEQ15Days) {
		this.fixedDefectsWithHighPriorityAndResolutionLessThanOrEQ15Days = fixedDefectsWithHighPriorityAndResolutionLessThanOrEQ15Days;
	}
	public int getFixedDefectsWithLowPriorityAndResolutionLessThanOrEQ15Days() {
		return fixedDefectsWithLowPriorityAndResolutionLessThanOrEQ15Days;
	}
	public void setFixedDefectsWithLowPriorityAndResolutionLessThanOrEQ15Days(
			int fixedDefectsWithLowPriorityAndResolutionLessThanOrEQ15Days) {
		this.fixedDefectsWithLowPriorityAndResolutionLessThanOrEQ15Days = fixedDefectsWithLowPriorityAndResolutionLessThanOrEQ15Days;
	}
	public int getFixedDefectsWithMediumPriorityAndResolutionLessThanOrEQ15Days() {
		return fixedDefectsWithMediumPriorityAndResolutionLessThanOrEQ15Days;
	}
	public void setFixedDefectsWithMediumPriorityAndResolutionLessThanOrEQ15Days(
			int fixedDefectsWithMediumPriorityAndResolutionLessThanOrEQ15Days) {
		this.fixedDefectsWithMediumPriorityAndResolutionLessThanOrEQ15Days = fixedDefectsWithMediumPriorityAndResolutionLessThanOrEQ15Days;
	}
	public int getFixedDefectsWithCriticalPriorityAndResolutionLessThanOrEQ15Days() {
		return fixedDefectsWithCriticalPriorityAndResolutionLessThanOrEQ15Days;
	}
	public void setFixedDefectsWithCriticalPriorityAndResolutionLessThanOrEQ15Days(
			int fixedDefectsWithCriticalPriorityAndResolutionLessThanOrEQ15Days) {
		this.fixedDefectsWithCriticalPriorityAndResolutionLessThanOrEQ15Days = fixedDefectsWithCriticalPriorityAndResolutionLessThanOrEQ15Days;
	}
	public int getFixedDefectsWithHighPriorityAndResolutionBetween15To30Days() {
		return fixedDefectsWithHighPriorityAndResolutionBetween15To30Days;
	}
	public void setFixedDefectsWithHighPriorityAndResolutionBetween15To30Days(
			int fixedDefectsWithHighPriorityAndResolutionBetween15To30Days) {
		this.fixedDefectsWithHighPriorityAndResolutionBetween15To30Days = fixedDefectsWithHighPriorityAndResolutionBetween15To30Days;
	}
	public int getFixedDefectsWithLowPriorityAndResolutionBetween15To30Days() {
		return fixedDefectsWithLowPriorityAndResolutionBetween15To30Days;
	}
	public void setFixedDefectsWithLowPriorityAndResolutionBetween15To30Days(
			int fixedDefectsWithLowPriorityAndResolutionBetween15To30Days) {
		this.fixedDefectsWithLowPriorityAndResolutionBetween15To30Days = fixedDefectsWithLowPriorityAndResolutionBetween15To30Days;
	}
	public int getFixedDefectsWithMediumPriorityAndResolutionBetween15To30Days() {
		return fixedDefectsWithMediumPriorityAndResolutionBetween15To30Days;
	}
	public void setFixedDefectsWithMediumPriorityAndResolutionBetween15To30Days(
			int fixedDefectsWithMediumPriorityAndResolutionBetween15To30Days) {
		this.fixedDefectsWithMediumPriorityAndResolutionBetween15To30Days = fixedDefectsWithMediumPriorityAndResolutionBetween15To30Days;
	}
	public int getFixedDefectsWithCriticalPriorityAndResolutionBetween15To30Days() {
		return fixedDefectsWithCriticalPriorityAndResolutionBetween15To30Days;
	}
	public void setFixedDefectsWithCriticalPriorityAndResolutionBetween15To30Days(
			int fixedDefectsWithCriticalPriorityAndResolutionBetween15To30Days) {
		this.fixedDefectsWithCriticalPriorityAndResolutionBetween15To30Days = fixedDefectsWithCriticalPriorityAndResolutionBetween15To30Days;
	}
	public int getFixedDefectsWithHighPriorityAndResolutionBetween30To60Days() {
		return fixedDefectsWithHighPriorityAndResolutionBetween30To60Days;
	}
	public void setFixedDefectsWithHighPriorityAndResolutionBetween30To60Days(
			int fixedDefectsWithHighPriorityAndResolutionBetween30To60Days) {
		this.fixedDefectsWithHighPriorityAndResolutionBetween30To60Days = fixedDefectsWithHighPriorityAndResolutionBetween30To60Days;
	}
	public int getFixedDefectsWithLowPriorityAndResolutionBetween30To60Days() {
		return fixedDefectsWithLowPriorityAndResolutionBetween30To60Days;
	}
	public void setFixedDefectsWithLowPriorityAndResolutionBetween30To60Days(
			int fixedDefectsWithLowPriorityAndResolutionBetween30To60Days) {
		this.fixedDefectsWithLowPriorityAndResolutionBetween30To60Days = fixedDefectsWithLowPriorityAndResolutionBetween30To60Days;
	}
	public int getFixedDefectsWithMediumPriorityAndResolutionBetween30To60Days() {
		return fixedDefectsWithMediumPriorityAndResolutionBetween30To60Days;
	}
	public void setFixedDefectsWithMediumPriorityAndResolutionBetween30To60Days(
			int fixedDefectsWithMediumPriorityAndResolutionBetween30To60Days) {
		this.fixedDefectsWithMediumPriorityAndResolutionBetween30To60Days = fixedDefectsWithMediumPriorityAndResolutionBetween30To60Days;
	}
	public int getFixedDefectsWithCriticalPriorityAndResolutionBetween30To60Days() {
		return fixedDefectsWithCriticalPriorityAndResolutionBetween30To60Days;
	}
	public void setFixedDefectsWithCriticalPriorityAndResolutionBetween30To60Days(
			int fixedDefectsWithCriticalPriorityAndResolutionBetween30To60Days) {
		this.fixedDefectsWithCriticalPriorityAndResolutionBetween30To60Days = fixedDefectsWithCriticalPriorityAndResolutionBetween30To60Days;
	}
	public int getFixedDefectsWithHighPriorityAndResolutionBetween60To90Days() {
		return fixedDefectsWithHighPriorityAndResolutionBetween60To90Days;
	}
	public void setFixedDefectsWithHighPriorityAndResolutionBetween60To90Days(
			int fixedDefectsWithHighPriorityAndResolutionBetween60To90Days) {
		this.fixedDefectsWithHighPriorityAndResolutionBetween60To90Days = fixedDefectsWithHighPriorityAndResolutionBetween60To90Days;
	}
	public int getFixedDefectsWithLowPriorityAndResolutionBetween60To90Days() {
		return fixedDefectsWithLowPriorityAndResolutionBetween60To90Days;
	}
	public void setFixedDefectsWithLowPriorityAndResolutionBetween60To90Days(
			int fixedDefectsWithLowPriorityAndResolutionBetween60To90Days) {
		this.fixedDefectsWithLowPriorityAndResolutionBetween60To90Days = fixedDefectsWithLowPriorityAndResolutionBetween60To90Days;
	}
	public int getFixedDefectsWithMediumPriorityAndResolutionBetween60To90Days() {
		return fixedDefectsWithMediumPriorityAndResolutionBetween60To90Days;
	}
	public void setFixedDefectsWithMediumPriorityAndResolutionBetween60To90Days(
			int fixedDefectsWithMediumPriorityAndResolutionBetween60To90Days) {
		this.fixedDefectsWithMediumPriorityAndResolutionBetween60To90Days = fixedDefectsWithMediumPriorityAndResolutionBetween60To90Days;
	}
	public int getFixedDefectsWithCriticalPriorityAndResolutionBetween60To90Days() {
		return fixedDefectsWithCriticalPriorityAndResolutionBetween60To90Days;
	}
	public void setFixedDefectsWithCriticalPriorityAndResolutionBetween60To90Days(
			int fixedDefectsWithCriticalPriorityAndResolutionBetween60To90Days) {
		this.fixedDefectsWithCriticalPriorityAndResolutionBetween60To90Days = fixedDefectsWithCriticalPriorityAndResolutionBetween60To90Days;
	}
	public int getFixedDefectsWithHighPriorityAndResolutionGreaterThan90() {
		return fixedDefectsWithHighPriorityAndResolutionGreaterThan90;
	}
	public void setFixedDefectsWithHighPriorityAndResolutionGreaterThan90(
			int fixedDefectsWithHighPriorityAndResolutionGreaterThan90) {
		this.fixedDefectsWithHighPriorityAndResolutionGreaterThan90 = fixedDefectsWithHighPriorityAndResolutionGreaterThan90;
	}
	public int getFixedDefectsWithLowPriorityAndResolutionGreaterThan90() {
		return fixedDefectsWithLowPriorityAndResolutionGreaterThan90;
	}
	public void setFixedDefectsWithLowPriorityAndResolutionGreaterThan90(
			int fixedDefectsWithLowPriorityAndResolutionGreaterThan90) {
		this.fixedDefectsWithLowPriorityAndResolutionGreaterThan90 = fixedDefectsWithLowPriorityAndResolutionGreaterThan90;
	}
	public int getFixedDefectsWithMediumPriorityAndResolutionGreaterThan90() {
		return fixedDefectsWithMediumPriorityAndResolutionGreaterThan90;
	}
	public void setFixedDefectsWithMediumPriorityAndResolutionGreaterThan90(
			int fixedDefectsWithMediumPriorityAndResolutionGreaterThan90) {
		this.fixedDefectsWithMediumPriorityAndResolutionGreaterThan90 = fixedDefectsWithMediumPriorityAndResolutionGreaterThan90;
	}
	public int getFixedDefectsWithCriticalPriorityAndResolutionGreaterThan90() {
		return fixedDefectsWithCriticalPriorityAndResolutionGreaterThan90;
	}
	public void setFixedDefectsWithCriticalPriorityAndResolutionGreaterThan90(
			int fixedDefectsWithCriticalPriorityAndResolutionGreaterThan90) {
		this.fixedDefectsWithCriticalPriorityAndResolutionGreaterThan90 = fixedDefectsWithCriticalPriorityAndResolutionGreaterThan90;
	}
	public String getProjectName() {
		return projectName;
	}
	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}
	public String getMetricsProjectId() {
		return metricsProjectId;
	}
	public void setMetricsProjectId(String metricsProjectId) {
		this.metricsProjectId = metricsProjectId;
	}
	public String getProjectId() {
		return projectId;
	}
	public void setProjectId(String projectId) {
		this.projectId = projectId;
	}
	public String getValueAsOn() {
		return valueAsOn;
	}
	public void setValueAsOn(String valueAsOn) {
		this.valueAsOn = valueAsOn;
	}
	
	
	
	
}
