package gov.cdc.nnddatapollservice.repository.rdb.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "F_STD_PAGE_CASE")
public class FStdPageCase {
    @Column(name = "D_INV_ADMINISTRATIVE_KEY")
    private Long dInvAdministrativeKey;

    @Column(name = "D_INV_CLINICAL_KEY")
    private Long dInvClinicalKey;

    @Column(name = "D_INV_COMPLICATION_KEY")
    private Long dInvComplicationKey;

    @Column(name = "D_INV_CONTACT_KEY")
    private Long dInvContactKey;

    @Column(name = "D_INV_DEATH_KEY")
    private Long dInvDeathKey;

    @Column(name = "D_INV_EPIDEMIOLOGY_KEY")
    private Long dInvEpidemiologyKey;

    @Column(name = "D_INV_HIV_KEY")
    private Long dInvHivKey;

    @Column(name = "D_INV_PATIENT_OBS_KEY")
    private Long dInvPatientObsKey;

    @Column(name = "D_INV_ISOLATE_TRACKING_KEY")
    private Long dInvIsolateTrackingKey;

    @Column(name = "D_INV_LAB_FINDING_KEY")
    private Long dInvLabFindingKey;

    @Column(name = "D_INV_MEDICAL_HISTORY_KEY")
    private Long dInvMedicalHistoryKey;

    @Column(name = "D_INV_MOTHER_KEY")
    private Long dInvMotherKey;

    @Column(name = "D_INV_OTHER_KEY")
    private Long dInvOtherKey;

    @Column(name = "D_INV_PREGNANCY_BIRTH_KEY")
    private Long dInvPregnancyBirthKey;

    @Column(name = "D_INV_RESIDENCY_KEY")
    private Long dInvResidencyKey;

    @Column(name = "D_INV_RISK_FACTOR_KEY")
    private Long dInvRiskFactorKey;

    @Column(name = "D_INV_SOCIAL_HISTORY_KEY")
    private Long dInvSocialHistoryKey;

    @Column(name = "D_INV_SYMPTOM_KEY")
    private Long dInvSymptomKey;

    @Column(name = "D_INV_TREATMENT_KEY")
    private Long dInvTreatmentKey;

    @Column(name = "D_INV_TRAVEL_KEY")
    private Long dInvTravelKey;

    @Column(name = "D_INV_UNDER_CONDITION_KEY")
    private Long dInvUnderConditionKey;

    @Column(name = "D_INV_VACCINATION_KEY")
    private Long dInvVaccinationKey;

    @Column(name = "D_INVESTIGATION_REPEAT_KEY")
    private Double dInvestigationRepeatKey;

    @Column(name = "D_INV_PLACE_REPEAT_KEY")
    private Double dInvPlaceRepeatKey;

    @Column(name = "CONDITION_KEY")
    private Long conditionKey;

    @Column(name = "INVESTIGATION_KEY")
    private Long investigationKey;

    @Column(name = "PHYSICIAN_KEY")
    private Long physicianKey;

    @Column(name = "INVESTIGATOR_KEY")
    private Long investigatorKey;

    @Column(name = "HOSPITAL_KEY")
    private Long hospitalKey;

    @Column(name = "PATIENT_KEY")
    private Long patientKey;

    @Column(name = "PERSON_AS_REPORTER_KEY")
    private Long personAsReporterKey;

    @Column(name = "ORG_AS_REPORTER_KEY")
    private Long orgAsReporterKey;

    @Column(name = "ORDERING_FACILITY_KEY")
    private Long orderingFacilityKey;

    @Column(name = "GEOCODING_LOCATION_KEY")
    private Long geocodingLocationKey;

    @Column(name = "CLOSED_BY_KEY")
    private Long closedByKey;

    @Column(name = "DISPOSITIONED_BY_KEY")
    private Long dispositionedByKey;

    @Column(name = "FACILITY_FLD_FOLLOW_UP_KEY")
    private Long facilityFldFollowUpKey;

    @Column(name = "INVSTGTR_FLD_FOLLOW_UP_KEY")
    private Long invstgtrFldFollowUpKey;

    @Column(name = "PROVIDER_FLD_FOLLOW_UP_KEY")
    private Long providerFldFollowUpKey;

    @Column(name = "SUPRVSR_OF_FLD_FOLLOW_UP_KEY")
    private Long suprvsrOfFldFollowUpKey;

    @Column(name = "INIT_ASGNED_FLD_FOLLOW_UP_KEY")
    private Long initAsgnedFldFollowUpKey;

    @Column(name = "INIT_FOLLOW_UP_INVSTGTR_KEY")
    private Long initFollowUpInvstgtrKey;

    @Column(name = "INIT_ASGNED_INTERVIEWER_KEY")
    private Long initAsgnedInterviewerKey;

    @Column(name = "INTERVIEWER_ASSIGNED_KEY")
    private Long interviewerAssignedKey;

    @Column(name = "SURVEILLANCE_INVESTIGATOR_KEY")
    private Long surveillanceInvestigatorKey;

    @Column(name = "SUPRVSR_OF_CASE_ASSGNMENT_KEY")
    private Long suprvsrOfCaseAssgnmentKey;

    @Column(name = "DELIVERING_HOSP_KEY")
    private Long deliveringHospKey;

    @Column(name = "DELIVERING_MD_KEY")
    private Long deliveringMdKey;

    @Column(name = "MOTHER_OB_GYN_KEY")
    private Long motherObGynKey;

    @Column(name = "PEDIATRICIAN_KEY")
    private Long pediatricianKey;

    @Column(name = "ADD_DATE_KEY")
    private Long addDateKey;

    @Column(name = "LAST_CHG_DATE_KEY")
    private Long lastChgDateKey;

}