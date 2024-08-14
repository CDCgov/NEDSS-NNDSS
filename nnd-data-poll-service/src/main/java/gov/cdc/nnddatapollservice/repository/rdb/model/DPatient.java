package gov.cdc.nnddatapollservice.repository.rdb.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.Instant;

@Getter
@Setter
@Entity
@Table(name = "D_PATIENT")
public class DPatient {
    @Id
    @Column(name = "PATIENT_KEY", nullable = false)
    private Long id;

    @Column(name = "PATIENT_MPR_UID")
    private Long patientMprUid;

    @jakarta.validation.constraints.Size(max = 50)
    @Column(name = "PATIENT_RECORD_STATUS", length = 50)
    private String patientRecordStatus;

    @jakarta.validation.constraints.Size(max = 50)
    @Column(name = "PATIENT_LOCAL_ID", length = 50)
    private String patientLocalId;

    @jakarta.validation.constraints.Size(max = 2000)
    @Column(name = "PATIENT_GENERAL_COMMENTS", length = 2000)
    private String patientGeneralComments;

    @jakarta.validation.constraints.Size(max = 50)
    @Column(name = "PATIENT_FIRST_NAME", length = 50)
    private String patientFirstName;

    @jakarta.validation.constraints.Size(max = 50)
    @Column(name = "PATIENT_MIDDLE_NAME", length = 50)
    private String patientMiddleName;

    @jakarta.validation.constraints.Size(max = 50)
    @Column(name = "PATIENT_LAST_NAME", length = 50)
    private String patientLastName;

    @jakarta.validation.constraints.Size(max = 50)
    @Column(name = "PATIENT_NAME_SUFFIX", length = 50)
    private String patientNameSuffix;

    @jakarta.validation.constraints.Size(max = 50)
    @Column(name = "PATIENT_ALIAS_NICKNAME", length = 50)
    private String patientAliasNickname;

    @jakarta.validation.constraints.Size(max = 50)
    @Column(name = "PATIENT_STREET_ADDRESS_1", length = 50)
    private String patientStreetAddress1;

    @jakarta.validation.constraints.Size(max = 50)
    @Column(name = "PATIENT_STREET_ADDRESS_2", length = 50)
    private String patientStreetAddress2;

    @jakarta.validation.constraints.Size(max = 50)
    @Column(name = "PATIENT_CITY", length = 50)
    private String patientCity;

    @jakarta.validation.constraints.Size(max = 50)
    @Column(name = "PATIENT_STATE", length = 50)
    private String patientState;

    @jakarta.validation.constraints.Size(max = 50)
    @Column(name = "PATIENT_STATE_CODE", length = 50)
    private String patientStateCode;

    @jakarta.validation.constraints.Size(max = 50)
    @Column(name = "PATIENT_ZIP", length = 50)
    private String patientZip;

    @jakarta.validation.constraints.Size(max = 50)
    @Column(name = "PATIENT_COUNTY", length = 50)
    private String patientCounty;

    @jakarta.validation.constraints.Size(max = 50)
    @Column(name = "PATIENT_COUNTY_CODE", length = 50)
    private String patientCountyCode;

    @jakarta.validation.constraints.Size(max = 50)
    @Column(name = "PATIENT_COUNTRY", length = 50)
    private String patientCountry;

    @jakarta.validation.constraints.Size(max = 10)
    @Column(name = "PATIENT_WITHIN_CITY_LIMITS", length = 10)
    private String patientWithinCityLimits;

    @jakarta.validation.constraints.Size(max = 50)
    @Column(name = "PATIENT_PHONE_HOME", length = 50)
    private String patientPhoneHome;

    @jakarta.validation.constraints.Size(max = 50)
    @Column(name = "PATIENT_PHONE_EXT_HOME", length = 50)
    private String patientPhoneExtHome;

    @jakarta.validation.constraints.Size(max = 50)
    @Column(name = "PATIENT_PHONE_WORK", length = 50)
    private String patientPhoneWork;

    @jakarta.validation.constraints.Size(max = 50)
    @Column(name = "PATIENT_PHONE_EXT_WORK", length = 50)
    private String patientPhoneExtWork;

    @jakarta.validation.constraints.Size(max = 50)
    @Column(name = "PATIENT_PHONE_CELL", length = 50)
    private String patientPhoneCell;

    @jakarta.validation.constraints.Size(max = 100)
    @Column(name = "PATIENT_EMAIL", length = 100)
    private String patientEmail;

    @Column(name = "PATIENT_DOB")
    private Instant patientDob;

    @Column(name = "PATIENT_AGE_REPORTED", precision = 18)
    private BigDecimal patientAgeReported;

    @jakarta.validation.constraints.Size(max = 20)
    @Column(name = "PATIENT_AGE_REPORTED_UNIT", length = 20)
    private String patientAgeReportedUnit;

    @jakarta.validation.constraints.Size(max = 50)
    @Column(name = "PATIENT_BIRTH_SEX", length = 50)
    private String patientBirthSex;

    @jakarta.validation.constraints.Size(max = 50)
    @Column(name = "PATIENT_CURRENT_SEX", length = 50)
    private String patientCurrentSex;

    @jakarta.validation.constraints.Size(max = 50)
    @Column(name = "PATIENT_DECEASED_INDICATOR", length = 50)
    private String patientDeceasedIndicator;

    @Column(name = "PATIENT_DECEASED_DATE")
    private Instant patientDeceasedDate;

    @jakarta.validation.constraints.Size(max = 50)
    @Column(name = "PATIENT_MARITAL_STATUS", length = 50)
    private String patientMaritalStatus;

    @jakarta.validation.constraints.Size(max = 50)
    @Column(name = "PATIENT_SSN", length = 50)
    private String patientSsn;

    @jakarta.validation.constraints.Size(max = 50)
    @Column(name = "PATIENT_ETHNICITY", length = 50)
    private String patientEthnicity;

    @jakarta.validation.constraints.Size(max = 50)
    @Column(name = "PATIENT_RACE_CALCULATED", length = 50)
    private String patientRaceCalculated;

    @jakarta.validation.constraints.Size(max = 4000)
    @Column(name = "PATIENT_RACE_CALC_DETAILS", length = 4000)
    private String patientRaceCalcDetails;

    @jakarta.validation.constraints.Size(max = 50)
    @Column(name = "PATIENT_RACE_AMER_IND_1", length = 50)
    private String patientRaceAmerInd1;

    @jakarta.validation.constraints.Size(max = 50)
    @Column(name = "PATIENT_RACE_AMER_IND_2", length = 50)
    private String patientRaceAmerInd2;

    @jakarta.validation.constraints.Size(max = 50)
    @Column(name = "PATIENT_RACE_AMER_IND_3", length = 50)
    private String patientRaceAmerInd3;

    @jakarta.validation.constraints.Size(max = 50)
    @Column(name = "PATIENT_RACE_AMER_IND_GT3_IND", length = 50)
    private String patientRaceAmerIndGt3Ind;

    @jakarta.validation.constraints.Size(max = 2000)
    @Column(name = "PATIENT_RACE_AMER_IND_ALL", length = 2000)
    private String patientRaceAmerIndAll;

    @jakarta.validation.constraints.Size(max = 50)
    @Column(name = "PATIENT_RACE_ASIAN_1", length = 50)
    private String patientRaceAsian1;

    @jakarta.validation.constraints.Size(max = 50)
    @Column(name = "PATIENT_RACE_ASIAN_2", length = 50)
    private String patientRaceAsian2;

    @jakarta.validation.constraints.Size(max = 50)
    @Column(name = "PATIENT_RACE_ASIAN_3", length = 50)
    private String patientRaceAsian3;

    @jakarta.validation.constraints.Size(max = 50)
    @Column(name = "PATIENT_RACE_ASIAN_GT3_IND", length = 50)
    private String patientRaceAsianGt3Ind;

    @jakarta.validation.constraints.Size(max = 2000)
    @Column(name = "PATIENT_RACE_ASIAN_ALL", length = 2000)
    private String patientRaceAsianAll;

    @jakarta.validation.constraints.Size(max = 50)
    @Column(name = "PATIENT_RACE_BLACK_1", length = 50)
    private String patientRaceBlack1;

    @jakarta.validation.constraints.Size(max = 50)
    @Column(name = "PATIENT_RACE_BLACK_2", length = 50)
    private String patientRaceBlack2;

    @jakarta.validation.constraints.Size(max = 50)
    @Column(name = "PATIENT_RACE_BLACK_3", length = 50)
    private String patientRaceBlack3;

    @jakarta.validation.constraints.Size(max = 50)
    @Column(name = "PATIENT_RACE_BLACK_GT3_IND", length = 50)
    private String patientRaceBlackGt3Ind;

    @jakarta.validation.constraints.Size(max = 2000)
    @Column(name = "PATIENT_RACE_BLACK_ALL", length = 2000)
    private String patientRaceBlackAll;

    @jakarta.validation.constraints.Size(max = 50)
    @Column(name = "PATIENT_RACE_NAT_HI_1", length = 50)
    private String patientRaceNatHi1;

    @jakarta.validation.constraints.Size(max = 50)
    @Column(name = "PATIENT_RACE_NAT_HI_2", length = 50)
    private String patientRaceNatHi2;

    @jakarta.validation.constraints.Size(max = 50)
    @Column(name = "PATIENT_RACE_NAT_HI_3", length = 50)
    private String patientRaceNatHi3;

    @jakarta.validation.constraints.Size(max = 50)
    @Column(name = "PATIENT_RACE_NAT_HI_GT3_IND", length = 50)
    private String patientRaceNatHiGt3Ind;

    @jakarta.validation.constraints.Size(max = 2000)
    @Column(name = "PATIENT_RACE_NAT_HI_ALL", length = 2000)
    private String patientRaceNatHiAll;

    @jakarta.validation.constraints.Size(max = 50)
    @Column(name = "PATIENT_RACE_WHITE_1", length = 50)
    private String patientRaceWhite1;

    @jakarta.validation.constraints.Size(max = 50)
    @Column(name = "PATIENT_RACE_WHITE_2", length = 50)
    private String patientRaceWhite2;

    @jakarta.validation.constraints.Size(max = 50)
    @Column(name = "PATIENT_RACE_WHITE_3", length = 50)
    private String patientRaceWhite3;

    @jakarta.validation.constraints.Size(max = 50)
    @Column(name = "PATIENT_RACE_WHITE_GT3_IND", length = 50)
    private String patientRaceWhiteGt3Ind;

    @jakarta.validation.constraints.Size(max = 2000)
    @Column(name = "PATIENT_RACE_WHITE_ALL", length = 2000)
    private String patientRaceWhiteAll;

    @jakarta.validation.constraints.Size(max = 50)
    @Column(name = "PATIENT_NUMBER", length = 50)
    private String patientNumber;

    @jakarta.validation.constraints.Size(max = 199)
    @Column(name = "PATIENT_NUMBER_AUTH", length = 199)
    private String patientNumberAuth;

    @jakarta.validation.constraints.Size(max = 50)
    @Column(name = "PATIENT_ENTRY_METHOD", length = 50)
    private String patientEntryMethod;

    @Column(name = "PATIENT_LAST_CHANGE_TIME")
    private Instant patientLastChangeTime;

    @Column(name = "PATIENT_UID")
    private Long patientUid;

    @Column(name = "PATIENT_ADD_TIME")
    private Instant patientAddTime;

    @jakarta.validation.constraints.Size(max = 50)
    @Column(name = "PATIENT_ADDED_BY", length = 50)
    private String patientAddedBy;

    @jakarta.validation.constraints.Size(max = 50)
    @Column(name = "PATIENT_LAST_UPDATED_BY", length = 50)
    private String patientLastUpdatedBy;

    @jakarta.validation.constraints.Size(max = 100)
    @Column(name = "PATIENT_SPEAKS_ENGLISH", length = 100)
    private String patientSpeaksEnglish;

    @jakarta.validation.constraints.Size(max = 100)
    @Column(name = "PATIENT_UNK_ETHNIC_RSN", length = 100)
    private String patientUnkEthnicRsn;

    @jakarta.validation.constraints.Size(max = 100)
    @Column(name = "PATIENT_CURR_SEX_UNK_RSN", length = 100)
    private String patientCurrSexUnkRsn;

    @jakarta.validation.constraints.Size(max = 100)
    @Column(name = "PATIENT_PREFERRED_GENDER", length = 100)
    private String patientPreferredGender;

    @jakarta.validation.constraints.Size(max = 100)
    @Column(name = "PATIENT_ADDL_GENDER_INFO", length = 100)
    private String patientAddlGenderInfo;

    @jakarta.validation.constraints.Size(max = 100)
    @Column(name = "PATIENT_CENSUS_TRACT", length = 100)
    private String patientCensusTract;

    @jakarta.validation.constraints.Size(max = 4000)
    @Column(name = "PATIENT_RACE_ALL", length = 4000)
    private String patientRaceAll;

    @jakarta.validation.constraints.Size(max = 50)
    @Column(name = "PATIENT_BIRTH_COUNTRY", length = 50)
    private String patientBirthCountry;

    @jakarta.validation.constraints.Size(max = 50)
    @Column(name = "PATIENT_PRIMARY_OCCUPATION", length = 50)
    private String patientPrimaryOccupation;

    @jakarta.validation.constraints.Size(max = 50)
    @Column(name = "PATIENT_PRIMARY_LANGUAGE", length = 50)
    private String patientPrimaryLanguage;

}