/****** Object:  Table [dbo].[ANTIMICROBIAL]    Script Date: 3/18/2025 5:42:18 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[ANTIMICROBIAL](
	[ANTIMICROBIAL_GRP_KEY] [bigint] NOT NULL,
	[ANTIMICROBIAL_AGENT_TESTED_IND] [varchar](50) NULL,
	[SUSCEPTABILITY_METHOD] [varchar](50) NULL,
	[S_I_R_U_RESULT] [varchar](50) NULL,
	[MIC_SIGN] [varchar](50) NULL,
	[MIC_VALUE] [numeric](15, 5) NULL,
	[ANTIMICROBIAL_KEY] [bigint] NOT NULL,
PRIMARY KEY CLUSTERED 
(
	[ANTIMICROBIAL_GRP_KEY] ASC,
	[ANTIMICROBIAL_KEY] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[ANTIMICROBIAL_GROUP]    Script Date: 3/18/2025 5:42:18 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[ANTIMICROBIAL_GROUP](
	[ANTIMICROBIAL_GRP_KEY] [bigint] NOT NULL,
PRIMARY KEY CLUSTERED 
(
	[ANTIMICROBIAL_GRP_KEY] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[BMIRD_CASE]    Script Date: 3/18/2025 5:42:18 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[BMIRD_CASE](
	[INVESTIGATOR_KEY] [bigint] NOT NULL,
	[PHYSICIAN_KEY] [bigint] NOT NULL,
	[PATIENT_KEY] [bigint] NOT NULL,
	[REPORTER_KEY] [bigint] NOT NULL,
	[NURSING_HOME_KEY] [bigint] NOT NULL,
	[DAYCARE_FACILITY_KEY] [bigint] NOT NULL,
	[TRANSFERED_IND] [varchar](50) NULL,
	[INV_ASSIGNED_DT_KEY] [bigint] NOT NULL,
	[BIRTH_WEIGHT_IN_GRAMS] [numeric](18, 0) NULL,
	[BIRTH_WEIGHT_POUNDS] [numeric](18, 0) NULL,
	[WEIGHT_IN_POUNDS] [numeric](18, 0) NULL,
	[WEIGHT_IN_OUNCES] [numeric](18, 0) NULL,
	[WEIGHT_IN_KILOGRAM] [numeric](18, 0) NULL,
	[WEIGHT_UNKNOWN] [varchar](50) NULL,
	[HEIGHT_IN_FEET] [numeric](18, 0) NULL,
	[HEIGHT_IN_INCHES] [numeric](18, 0) NULL,
	[HEIGHT_IN_CENTIMETERS] [numeric](18, 0) NULL,
	[HEIGHT_UNKNOWN] [varchar](50) NULL,
	[OTH_STREP_PNEUMO1_CULT_SITES] [varchar](50) NULL,
	[OTH_STREP_PNEUMO2_CULT_SITES] [varchar](50) NULL,
	[IHC_SPECIMEN_1] [varchar](50) NULL,
	[IHC_SPECIMEN_2] [varchar](50) NULL,
	[IHC_SPECIMEN_3] [varchar](50) NULL,
	[SAMPLE_COLLECTION_DT] [datetime] NULL,
	[CONJ_MENING_VACC] [varchar](50) NULL,
	[TREATMENT_HOSPITAL_NM] [varchar](100) NULL,
	[TREATMENT_HOSPITAL_KEY] [bigint] NULL,
	[OTH_TYPE_OF_INSURANCE] [varchar](50) NULL,
	[BIRTH_WEIGHT_OUNCES] [numeric](18, 0) NULL,
	[PREGNANT_IND] [varchar](50) NULL,
	[OUTCOME_OF_FETUS] [varchar](50) NULL,
	[UNDER_1_MONTH_IND] [varchar](50) NULL,
	[GESTATIONAL_AGE] [numeric](18, 0) NULL,
	[BACTERIAL_SPECIES_ISOLATED] [varchar](50) NULL,
	[FIRST_POSITIVE_CULTURE_DT] [datetime] NULL,
	[UNDERLYING_CONDITION_IND] [varchar](50) NULL,
	[PATIENT_YR_IN_COLLEGE] [varchar](50) NULL,
	[CULTURE_SEROTYPE] [varchar](50) NULL,
	[PATIENT_STATUS_IN_COLLEGE] [varchar](50) NULL,
	[PATIENT_CURR_LIVING_SITUATION] [varchar](50) NULL,
	[HIB_VACC_RECEIVED_IND] [varchar](50) NULL,
	[CULTURE_SEROGROUP] [varchar](50) NULL,
	[ATTENDING_COLLEGE_IND] [varchar](50) NULL,
	[OXACILLIN_ZONE_SIZE] [numeric](18, 0) NULL,
	[OXACILLIN_INTERPRETATION] [varchar](50) NULL,
	[PNEUVACC_RECEIVED_IND] [varchar](50) NULL,
	[PNEUCONJ_RECEIVED_IND] [varchar](50) NULL,
	[FIRST_ADDITIONAL_SPECIMEN_DT] [datetime] NULL,
	[SECOND_ADDITIONAL_SPECIMEN_DT] [datetime] NULL,
	[PATIENT_HAD_SURGERY_IND] [varchar](50) NULL,
	[SURGERY_DT] [datetime] NULL,
	[PATIENT_HAVE_BABY_IND] [varchar](50) NULL,
	[BABY_DELIVERY_DT] [datetime] NULL,
	[IDENT_THRU_AUDIT_IND] [varchar](50) NULL,
	[SAME_PATHOGEN_RECURRENT_IND] [varchar](50) NULL,
	[OTHER_SPECIES_ISOLATE_SITE] [varchar](2000) NULL,
	[OTHER_CASE_IDENT_METHOD] [varchar](2000) NULL,
	[OTHER_HOUSING_OPTION] [varchar](2000) NULL,
	[PATIENT_CURR_ATTEND_SCHOOL_NM] [varchar](100) NULL,
	[POLYSAC_MENINGOC_VACC_IND] [varchar](50) NULL,
	[FAMILY_MEDICAL_INSURANCE_TYPE] [varchar](50) NULL,
	[HIB_CONTACT_IN_LAST_2_MON_IND] [varchar](50) NULL,
	[TYPE_HIB_CONTACT_IN_LAST_2_MON] [varchar](2000) NULL,
	[PRETERM_BIRTH_WK_NBR] [numeric](18, 0) NULL,
	[IMMUNOSUPRESSION_HIV_STATUS] [varchar](2000) NULL,
	[ACUTE_SERUM_AVAILABLE_IND] [varchar](50) NULL,
	[ACUTE_SERUM_AVAILABLE_DT] [datetime] NULL,
	[CONVALESNT_SERUM_AVAILABLE_IND] [varchar](50) NULL,
	[CONVALESNT_SERUM_AVAILABLE_DT] [datetime] NULL,
	[BIRTH_OUTSIDE_HSPTL_IND] [varchar](50) NULL,
	[BIRTH_OUTSIDE_HSPTL_LOCATION] [varchar](50) NULL,
	[BABY_HSPTL_DISCHARGE_DTTIME] [datetime] NULL,
	[BABY_SAME_HSPTL_READMIT_IND] [varchar](50) NULL,
	[BABY_SAME_HSPTL_READMIT_DTTIME] [datetime] NULL,
	[FRM_HOME_TO_DIF_HSPTL_IND] [varchar](50) NULL,
	[FRM_HOME_TO_DIF_HSPTL_DTTIME] [datetime] NULL,
	[MOTHER_LAST_NM] [varchar](50) NULL,
	[MOTHER_FIRST_NM] [varchar](50) NULL,
	[MOTHER_HOSPTL_ADMISSION_DTTIME] [datetime] NULL,
	[MOTHER_PATIENT_CHART_NBR] [varchar](2000) NULL,
	[MOTHER_PENICILLIN_ALLERGY_IND] [varchar](50) NULL,
	[MEMBRANE_RUPTURE_DTTIME] [datetime] NULL,
	[MEMBRANE_RUPTURE_GE_18HRS_IND] [varchar](50) NULL,
	[RUPTURE_BEFORE_LABOR_ONSET] [varchar](50) NULL,
	[MEMBRANE_RUPTURE_TYPE] [varchar](50) NULL,
	[DELIVERY_TYPE] [varchar](50) NULL,
	[MOTHER_INTRAPARTUM_FEVER_IND] [varchar](50) NULL,
	[FIRST_INTRAPARTUM_FEVER_DTTIME] [datetime] NULL,
	[RECEIVED_PRENATAL_CARE_IND] [varchar](50) NULL,
	[PRENATAL_CARE_IN_LABOR_CHART] [varchar](50) NULL,
	[PRENATAL_CARE_VISIT_NBR] [numeric](18, 0) NULL,
	[FIRST_PRENATAL_CARE_VISIT_DT] [datetime] NULL,
	[LAST_PRENATAL_CARE_VISIT_DT] [datetime] NULL,
	[LAST_PRENATAL_CARE_VISIT_EGA] [numeric](18, 0) NULL,
	[GBS_BACTERIURIA_IN_PREGNANCY] [varchar](50) NULL,
	[PREVIOUS_BIRTH_WITH_GBS_IND] [varchar](50) NULL,
	[GBS_CULTURED_BEFORE_ADMISSION] [varchar](50) NULL,
	[GBS_1ST_CULTURE_DT] [datetime] NULL,
	[GBS_1ST_CULTURE_POSITIVE_IND] [varchar](50) NULL,
	[GBS_2ND_CULTURE_DT] [datetime] NULL,
	[GBS_2ND_CULTURE_POSITIVE_IND] [varchar](50) NULL,
	[GBS_AFTER_ADM_BEFORE_DELIVERY] [varchar](50) NULL,
	[AFTER_ADM_GBS_CULTURE_DT] [datetime] NULL,
	[GBS_CULTURE_DELIVERY_AVAILABLE] [varchar](50) NULL,
	[INTRAPARTUM_ANTIBIOTICS_GIVEN] [varchar](50) NULL,
	[FIRST_ANTIBIOTICS_GIVEN_DTTIME] [datetime] NULL,
	[INTRAPARTUMANTIBIOTICSINTERVAL] [varchar](20) NULL,
	[INTRAPARTUM_ANTIBIOTICS_REASON] [varchar](50) NULL,
	[BABY_BIRTH_TIME] [varchar](20) NULL,
	[NEISERRIA_2NDARY_CASE_IND] [varchar](50) NULL,
	[NEISERRIA_2ND_CASE_CONTRACT] [varchar](50) NULL,
	[OTHER_2NDARY_CASE_TYPE] [varchar](2000) NULL,
	[NEISERRIA_RESIST_TO_RIFAMPIN] [varchar](50) NULL,
	[NEISERRIA_RESIST_TO_SULFA] [varchar](50) NULL,
	[FIRST_HSPTL_DISCHARGE_TIME] [varchar](2000) NULL,
	[FIRST_HSPTL_READMISSION_TIME] [varchar](2000) NULL,
	[SECOND_HSPTL_ADMISSION_TIME] [varchar](2000) NULL,
	[ABCCASE] [varchar](50) NULL,
	[HSPTL_MATERNAL_ADMISSION_TIME] [varchar](2000) NULL,
	[MEMBRANE_RUPTURE_TIME] [varchar](2000) NULL,
	[INTRAPARTUM_FEVER_RECORD_TIME] [varchar](2000) NULL,
	[ANTIBIOTICS_1ST_ADMIN_TIME] [varchar](2000) NULL,
	[BMIRD_MULTI_VAL_GRP_KEY] [bigint] NOT NULL,
	[OTHER_PRIOR_ILLNESS] [varchar](2000) NULL,
	[OTHER_MALIGNANCY] [varchar](2000) NULL,
	[ORGAN_TRANSPLANT] [varchar](2000) NULL,
	[DAYCARE_IND] [varchar](50) NULL,
	[NURSING_HOME_IND] [varchar](50) NULL,
	[TYPES_OF_OTHER_INFECTION] [varchar](2000) NULL,
	[BACTERIAL_OTHER_SPECIED] [varchar](2000) NULL,
	[STERILE_SITE_OTHER] [varchar](2000) NULL,
	[UNDERLYING_CONDITIONS_OTHER] [varchar](2000) NULL,
	[CULTURE_SEROGROUP_OTHER] [varchar](2000) NULL,
	[PERSISTENT_DISEASE_IND] [varchar](50) NULL,
	[GBS_CULTURE_POSITIVE_IND] [varchar](50) NULL,
	[ANTIMICROBIAL_GRP_KEY] [bigint] NOT NULL,
	[BACTERIAL_OTHER_ISOLATED] [varchar](50) NULL,
	[INVESTIGATION_KEY] [bigint] NOT NULL,
	[ADT_HSPTL_KEY] [bigint] NOT NULL,
	[RPT_SRC_ORG_KEY] [bigint] NOT NULL,
	[CONDITION_KEY] [bigint] NOT NULL,
	[FAMILY_MED_INSURANCE_TYPE_OTHE] [varchar](2000) NULL,
	[LDF_GROUP_KEY] [bigint] NOT NULL,
	[PRIOR_STATE_CASE_ID] [varchar](2000) NULL,
	[BIRTH_CNTRY_CD] [varchar](50) NULL,
	[INITIAL_HSPTL_NAME] [varchar](100) NULL,
	[BIRTH_HSPTL_NAME] [varchar](100) NULL,
	[FROM_HOME_HSPTL_NAME] [varchar](100) NULL,
	[CULTURE_IDENT_ORG_NAME] [varchar](100) NULL,
	[TRANSFER_FRM_HSPTL_NAME] [varchar](100) NULL,
	[CASE_REPORT_STATUS] [varchar](50) NULL,
	[TRANSFER_FRM_HSPTL_ID] [varchar](100) NULL,
	[BIRTH_HSPTL_ID] [varchar](100) NULL,
	[DIF_HSPTL_ID] [varchar](100) NULL,
	[ABC_STATE_CASE_ID] [varchar](199) NULL,
	[INV_PATIENT_CHART_NBR] [varchar](2000) NULL,
	[OTHSPEC1] [varchar](100) NULL,
	[OTHSPEC2] [varchar](100) NULL,
	[INTBODYSITE] [varchar](100) NULL,
	[OTHILL2] [varchar](100) NULL,
	[OTHILL3] [varchar](100) NULL,
	[OTHNONSTER] [varchar](100) NULL,
	[OTHSEROTYPE] [varchar](100) NULL,
	[HINFAGE] [varchar](100) NULL,
	[ABCSINVLN] [varchar](100) NULL,
	[ABCSINVFN] [varchar](100) NULL,
	[ABCSINVEMAIL] [varchar](100) NULL,
	[ABCSINVTELE] [varchar](100) NULL,
	[ABCSINVEXT] [varchar](100) NULL,
	[GEOCODING_LOCATION_KEY] [bigint] NULL,
 CONSTRAINT [PK_BMIRD_CASE] PRIMARY KEY CLUSTERED 
(
	[INVESTIGATOR_KEY] ASC,
	[PHYSICIAN_KEY] ASC,
	[PATIENT_KEY] ASC,
	[REPORTER_KEY] ASC,
	[NURSING_HOME_KEY] ASC,
	[DAYCARE_FACILITY_KEY] ASC,
	[INV_ASSIGNED_DT_KEY] ASC,
	[BMIRD_MULTI_VAL_GRP_KEY] ASC,
	[ANTIMICROBIAL_GRP_KEY] ASC,
	[INVESTIGATION_KEY] ASC,
	[ADT_HSPTL_KEY] ASC,
	[RPT_SRC_ORG_KEY] ASC,
	[CONDITION_KEY] ASC,
	[LDF_GROUP_KEY] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[BMIRD_MULTI_VALUE_FIELD]    Script Date: 3/18/2025 5:42:18 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[BMIRD_MULTI_VALUE_FIELD](
	[BMIRD_MULTI_VAL_FIELD_KEY] [bigint] NOT NULL,
	[TYPES_OF_INFECTIONS] [varchar](50) NULL,
	[UNDERLYING_CONDITION_NM] [varchar](50) NULL,
	[NON_STERILE_SITE] [varchar](50) NULL,
	[STREP_PNEUMO_2_CULTURE_SITES] [varchar](50) NULL,
	[STREP_PNEUMO_1_CULTURE_SITES] [varchar](50) NULL,
	[GBS_2ND_CULTURE_SITES] [varchar](50) NULL,
	[GBS_1ST_CULTURE_SITES] [varchar](50) NULL,
	[PAST_SIGNIFICANT_MEDICAL_HIST] [varchar](50) NULL,
	[STERILE_SITE] [varchar](50) NULL,
	[AFTER_ADM_GBS_CULTURE_SITES] [varchar](50) NULL,
	[BMIRD_MULTI_VAL_GRP_KEY] [bigint] NOT NULL,
	[OTHER_PRIOR_CONDITION_IND] [varchar](50) NULL,
	[HOW_WAS_CASE_IDENTIFIED] [varchar](50) NULL,
	[OTHERBUG2] [varchar](20) NULL,
	[PCR_SOURCE] [varchar](50) NULL,
	[TYPE_OF_INSURANCE] [varchar](50) NULL,
PRIMARY KEY CLUSTERED 
(
	[BMIRD_MULTI_VAL_FIELD_KEY] ASC,
	[BMIRD_MULTI_VAL_GRP_KEY] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[BMIRD_MULTI_VALUE_FIELD_GROUP]    Script Date: 3/18/2025 5:42:18 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[BMIRD_MULTI_VALUE_FIELD_GROUP](
	[BMIRD_MULTI_VAL_GRP_KEY] [bigint] NOT NULL,
PRIMARY KEY CLUSTERED 
(
	[BMIRD_MULTI_VAL_GRP_KEY] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[CASE_COUNT]    Script Date: 3/18/2025 5:42:18 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[CASE_COUNT](
	[CASE_COUNT] [numeric](18, 0) NULL,
	[INVESTIGATOR_KEY] [bigint] NOT NULL,
	[REPORTER_KEY] [bigint] NOT NULL,
	[PHYSICIAN_KEY] [bigint] NOT NULL,
	[RPT_SRC_ORG_KEY] [bigint] NOT NULL,
	[INV_ASSIGNED_DT_KEY] [bigint] NOT NULL,
	[PATIENT_KEY] [bigint] NOT NULL,
	[INVESTIGATION_KEY] [bigint] NOT NULL,
	[INVESTIGATION_COUNT] [bigint] NULL,
	[CONDITION_KEY] [bigint] NOT NULL,
	[ADT_HSPTL_KEY] [bigint] NULL,
	[INV_START_DT_KEY] [bigint] NOT NULL,
	[DIAGNOSIS_DT_KEY] [bigint] NOT NULL,
	[INV_RPT_DT_KEY] [bigint] NOT NULL,
	[GEOCODING_LOCATION_KEY] [bigint] NULL,
PRIMARY KEY CLUSTERED 
(
	[INV_ASSIGNED_DT_KEY] ASC,
	[INVESTIGATOR_KEY] ASC,
	[REPORTER_KEY] ASC,
	[PHYSICIAN_KEY] ASC,
	[RPT_SRC_ORG_KEY] ASC,
	[PATIENT_KEY] ASC,
	[INVESTIGATION_KEY] ASC,
	[CONDITION_KEY] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[CONDITION]    Script Date: 3/18/2025 5:42:18 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[CONDITION](
	[CONDITION_CD] [varchar](50) NULL,
	[CONDITION_DESC] [varchar](300) NULL,
	[CONDITION_SHORT_NM] [varchar](50) NULL,
	[CONDITION_CD_EFF_DT] [datetime] NULL,
	[CONDITION_CD_END_DT] [datetime] NULL,
	[NND_IND] [varchar](50) NULL,
	[CONDITION_KEY] [bigint] NOT NULL,
	[DISEASE_GRP_CD] [varchar](50) NULL,
	[DISEASE_GRP_DESC] [varchar](50) NULL,
	[PROGRAM_AREA_CD] [varchar](20) NULL,
	[PROGRAM_AREA_DESC] [varchar](100) NULL,
	[CONDITION_CD_SYS_CD_NM] [varchar](100) NULL,
	[ASSIGNING_AUTHORITY_CD] [varchar](199) NULL,
	[ASSIGNING_AUTHORITY_DESC] [varchar](100) NULL,
	[CONDITION_CD_SYS_CD] [varchar](50) NULL,
PRIMARY KEY CLUSTERED 
(
	[CONDITION_KEY] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[CONFIRMATION_METHOD]    Script Date: 3/18/2025 5:42:18 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[CONFIRMATION_METHOD](
	[CONFIRMATION_METHOD_KEY] [bigint] NOT NULL,
	[CONFIRMATION_METHOD_CD] [varchar](50) NULL,
	[CONFIRMATION_METHOD_DESC] [varchar](50) NULL,
PRIMARY KEY CLUSTERED 
(
	[CONFIRMATION_METHOD_KEY] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[CONFIRMATION_METHOD_GROUP]    Script Date: 3/18/2025 5:42:18 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[CONFIRMATION_METHOD_GROUP](
	[INVESTIGATION_KEY] [bigint] NOT NULL,
	[CONFIRMATION_METHOD_KEY] [bigint] NOT NULL,
	[CONFIRMATION_DT] [datetime] NULL,
PRIMARY KEY CLUSTERED 
(
	[INVESTIGATION_KEY] ASC,
	[CONFIRMATION_METHOD_KEY] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[COVID_CASE_DATAMART]    Script Date: 3/18/2025 5:42:18 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[COVID_CASE_DATAMART](
	[COVID_CASE_DATAMART_KEY] [bigint] NOT NULL,
	[public_health_case_uid] [bigint] NOT NULL,
	[INV_LOCAL_ID] [varchar](50) NULL,
	[PATIENT_LOCAL_ID] [varchar](50) NULL,
	[ADD_TIME] [datetime] NULL,
	[LAST_CHG_TIME] [datetime] NULL,
	[CONDITION_CD] [varchar](50) NULL,
	[NOTIFICATION_SUBMIT_DT] [datetime] NULL,
	[NOTIFICATION_SENT_DT] [datetime] NULL,
	[NOTIFICATION_LOCAL_ID] [varchar](50) NULL,
	[NOTIFICATION_STATUS] [varchar](50) NULL,
	[JURISDICTION_CD] [varchar](20) NULL,
	[JURISDICTION_NM] [varchar](255) NULL,
	[PROGRAM_AREA_CD] [varchar](20) NULL,
	[INV_START_DT] [datetime] NULL,
	[INVESTIGATION_STATUS_CD] [varchar](20) NULL,
	[INV_STATE_CASE_ID] [varchar](100) NULL,
	[INV_LEGACY_CASE_ID] [varchar](100) NULL,
	[INV_ASSIGNED_DT] [datetime] NULL,
	[INV_RPT_DT] [datetime] NULL,
	[EARLIEST_RPT_TO_CNTY_DT] [datetime] NULL,
	[EARLIEST_RPT_TO_ST_DT] [datetime] NULL,
	[RPT_SOURCE_CD] [varchar](20) NULL,
	[HSPTLIZD_IND] [varchar](20) NULL,
	[HSPTL_ADMISSION_DT] [datetime] NULL,
	[HSPTL_DISCHARGE_DT] [datetime] NULL,
	[HSPTL_DURATION_DAYS] [numeric](18, 0) NULL,
	[DIAGNOSIS_DT] [datetime] NULL,
	[ILLNESS_ONSET_DT] [datetime] NULL,
	[ILLNESS_END_DT] [datetime] NULL,
	[ILLNESS_DURATION] [varchar](20) NULL,
	[ILLNESS_DURATION_UNIT] [varchar](20) NULL,
	[PATIENT_ONSET_AGE] [varchar](20) NULL,
	[PATIENT_ONSET_AGE_UNIT] [varchar](20) NULL,
	[PATIENT_PREGNANT_IND] [varchar](20) NULL,
	[DIE_FROM_ILLNESS_IND] [varchar](20) NULL,
	[INV_DEATH_DT] [datetime] NULL,
	[DAYCARE_ASSOC_IND] [varchar](20) NULL,
	[FOOD_HANDLER_IND] [varchar](20) NULL,
	[OUTBREAK_IND] [varchar](20) NULL,
	[OUTBREAK_NAME] [varchar](100) NULL,
	[DISEASE_IMPORTED_IND] [varchar](20) NULL,
	[IMPORT_FROM_CNTRY] [varchar](20) NULL,
	[IMPORT_FROM_STATE] [varchar](20) NULL,
	[IMPORT_FROM_CITY] [varchar](250) NULL,
	[IMPORT_FROM_CNTY] [varchar](20) NULL,
	[TRANSMISSION_MODE_CD] [varchar](20) NULL,
	[DETECT_METHOD_CD] [varchar](20) NULL,
	[INV_CASE_STATUS] [varchar](20) NULL,
	[CASE_RPT_MMWR_WK] [varchar](10) NULL,
	[CASE_RPT_MMWR_YR] [varchar](10) NULL,
	[INV_COMMENTS] [varchar](8000) NULL,
	[CTT_INV_PRIORITY_CD] [varchar](50) NULL,
	[CTT_INFECTIOUS_FROM_DT] [datetime] NULL,
	[CTT_INFECTIOUS_TO_DT] [datetime] NULL,
	[CTT_INV_STATUS] [varchar](50) NULL,
	[CTT_INV_COMMENTS] [varchar](8000) NULL,
	[CONFIRMATION_METHOD] [varchar](max) NULL,
	[CONFIRMATION_DT] [datetime] NULL,
	[NOTES] [varchar](max) NULL,
	[PATIENT_GEN_COMMENTS] [varchar](8000) NULL,
	[PATIENT_FIRST_NAME] [varchar](50) NULL,
	[PATIENT_MIDDLE_NAME] [varchar](50) NULL,
	[PATIENT_LAST_NAME] [varchar](50) NULL,
	[PATIENT_NAME_SUFFIX] [varchar](20) NULL,
	[PATIENT_DOB] [datetime] NULL,
	[PATIENT_AGE_REPORTED] [varchar](10) NULL,
	[PATIENT_AGE_RPTD_UNIT] [varchar](20) NULL,
	[PATIENT_BIRTH_COUNTRY] [varchar](20) NULL,
	[PATIENT_CURRENT_SEX] [char](1) NULL,
	[PATIENT_DECEASED_IND] [varchar](20) NULL,
	[PATIENT_DECEASED_DT] [datetime] NULL,
	[PATIENT_MARITAL_STS] [varchar](20) NULL,
	[PATIENT_PRIMARY_LANGUAGE] [varchar](100) NULL,
	[PATIENT_STREET_ADDR_1] [varchar](100) NULL,
	[PATIENT_STREET_ADDR_2] [varchar](100) NULL,
	[PATIENT_CITY] [varchar](100) NULL,
	[PATIENT_STATE] [varchar](20) NULL,
	[PATIENT_ZIP] [varchar](20) NULL,
	[PATIENT_COUNTY] [varchar](20) NULL,
	[PATIENT_COUNTRY] [varchar](20) NULL,
	[PATIENT_TEL_HOME] [varchar](20) NULL,
	[PATIENT_PHONE_WORK] [varchar](20) NULL,
	[PATIENT_PHONE_EXT_WORK] [varchar](20) NULL,
	[PATIENT_TEL_CELL] [varchar](20) NULL,
	[PATIENT_EMAIL] [varchar](100) NULL,
	[PATIENT_ETHNICITY] [varchar](20) NULL,
	[PATIENT_RACE_CALC] [varchar](8000) NULL,
	[HOSPITAL_NAME] [varchar](100) NULL,
	[PHC_INV_LAST_NAME] [varchar](50) NULL,
	[PHC_INV_FIRST_NAME] [varchar](50) NULL,
	[PHYS_LAST_NAME] [varchar](50) NULL,
	[PHYS_FIRST_NAME] [varchar](50) NULL,
	[PHYS_TEL_WORK] [varchar](20) NULL,
	[PHYS_TEL_EXT_WORK] [varchar](20) NULL,
	[RPT_PRV_LAST_NAME] [varchar](50) NULL,
	[RPT_PRV_FIRST_NAME] [varchar](50) NULL,
	[RPT_PRV_TEL_WORK] [varchar](20) NULL,
	[RPT_PRV_TEL_EXT_WORK] [varchar](20) NULL,
	[RPT_ORG_NAME] [varchar](100) NULL,
	[RPT_ORG_TEL_WORK] [varchar](20) NULL,
	[RPT_ORG_TEL_EXT_WORK] [varchar](20) NULL,
	[ABDOMINAL_PAIN] [varchar](2000) NULL,
	[ABN_CHEST_XRAY_IND] [varchar](2000) NULL,
	[ACLF_OUTBREAK_NAME] [varchar](2000) NULL,
	[ACLF_OUTBREAK_OTHER] [varchar](2000) NULL,
	[ACLF_OUTBREAK_ROLE] [varchar](2000) NULL,
	[ACLF_OUTBREAK_TYPE] [varchar](2000) NULL,
	[ADENOVIRUS_RSLT] [varchar](2000) NULL,
	[ADULT_CONG_LIVING_EXP] [varchar](2000) NULL,
	[AIR_TRAVEL_EXP] [varchar](2000) NULL,
	[ANIMAL_EXPOSURE_IND] [varchar](2000) NULL,
	[ANIMAL_TYPE_TXT] [varchar](2000) NULL,
	[ARDS_IND] [varchar](2000) NULL,
	[ATTEND_EVENTS] [varchar](2000) NULL,
	[C_PNEUMONIAE_RSLT] [varchar](2000) NULL,
	[C19_CASE_INTERVIEW] [varchar](2000) NULL,
	[CASE_REPORT_TO_CDC_DT] [varchar](2000) NULL,
	[CASE_STATUS_REASON] [varchar](2000) NULL,
	[CDC_ASSIGNED_ID] [varchar](2000) NULL,
	[CHEST_PAIN_IND] [varchar](2000) NULL,
	[CHILLS_IND] [varchar](2000) NULL,
	[CHILLS_RIGORS] [varchar](2000) NULL,
	[CHINA_HC_HISTORY_IND] [varchar](2000) NULL,
	[CHRONIC_LIVER_DIS_IND] [varchar](2000) NULL,
	[CHRONIC_LUNG_DIS_IND] [varchar](2000) NULL,
	[CHRONIC_RENAL_DIS_IND] [varchar](2000) NULL,
	[CNTRY_USUAL_RESID] [varchar](2000) NULL,
	[CONTACT_24HR_NOTIF] [varchar](2000) NULL,
	[CONTACT_TEST_14DAYS_N] [varchar](2000) NULL,
	[CORONAVIRUS_RSLT] [varchar](2000) NULL,
	[CORRECTIONAL_EXP] [varchar](2000) NULL,
	[CORRECTIONAL_NAME] [varchar](2000) NULL,
	[CORYZA_RUNNY_NOSE_IND] [varchar](2000) NULL,
	[COUGH_IND] [varchar](2000) NULL,
	[COVID_BREAK_PCR_EPI] [varchar](2000) NULL,
	[COVID_BREAKTHROUGH] [varchar](2000) NULL,
	[COVID_CONTACTS_POSIT] [varchar](2000) NULL,
	[COVID_INTERVIEW_DATE] [varchar](2000) NULL,
	[COVID_VARIANT] [varchar](2000) NULL,
	[COVID_VARIANT_CLASSIF] [varchar](2000) NULL,
	[COVID_VARIANT_LABNME] [varchar](2000) NULL,
	[COVID_VARIANT_SEQ_YNU] [varchar](2000) NULL,
	[COVID19_OUTBREAK_NAME] [varchar](2000) NULL,
	[COVID19_OUTBREAK_YNU] [varchar](2000) NULL,
	[COVIDREINFECTIONSTATU] [varchar](2000) NULL,
	[COVIDREINFYNU] [varchar](2000) NULL,
	[CRUISE_TRAVEL_EXP] [varchar](2000) NULL,
	[CTRL_MEASURE_IMP_DT] [varchar](2000) NULL,
	[CTT_CONF_CASE_COMM] [varchar](2000) NULL,
	[CTT_CONF_CASE_HLTHCR] [varchar](2000) NULL,
	[CTT_CONF_CASE_HSHLD] [varchar](2000) NULL,
	[CTT_CONF_CASE_PAT_IND] [varchar](2000) NULL,
	[CURRENT_SMOKER_IND] [varchar](2000) NULL,
	[CV_DISEASE_IND] [varchar](2000) NULL,
	[DGMQ_ID] [varchar](2000) NULL,
	[DIABETES_MELLITUS_IND] [varchar](2000) NULL,
	[DIARRHEA] [varchar](2000) NULL,
	[DIFFICULT_BREATH_IND] [varchar](2000) NULL,
	[DueDate] [varchar](2000) NULL,
	[DYSPNEA_IND] [varchar](2000) NULL,
	[EDUCATIONAL_EXP] [varchar](2000) NULL,
	[EKG_ABNORMAL] [varchar](2000) NULL,
	[EPI_CONTACTSNOTIFEXPO] [varchar](2000) NULL,
	[EPI_LINKED_CASE_ID2] [varchar](2000) NULL,
	[EPI_LINKED_CASE_ID3] [varchar](2000) NULL,
	[EPI_SCHOOL_CARTER] [varchar](2000) NULL,
	[FATIGUE_MALAISE] [varchar](2000) NULL,
	[FEVER] [varchar](2000) NULL,
	[FEVER_HIGHEST_TMP_NBR] [varchar](2000) NULL,
	[FEVERISH_IND] [varchar](2000) NULL,
	[FIRST_RPT_TO_PHD_DT] [varchar](2000) NULL,
	[FLU_A_PCR_RSLT] [varchar](2000) NULL,
	[FLU_A_RAPID_AG_RSLT] [varchar](2000) NULL,
	[FLU_B_PCR_RSLT] [varchar](2000) NULL,
	[FLU_B_RAPID_AG_RSLT] [varchar](2000) NULL,
	[FORMER_SMOKER_IND] [varchar](2000) NULL,
	[FRST_POS_SPEC_CLCT_DT] [varchar](2000) NULL,
	[GRP_HME_NAME] [varchar](2000) NULL,
	[H_METAPNEUMOVRS_RSLT] [varchar](2000) NULL,
	[HC_CONTACT_TYPE] [varchar](2000) NULL,
	[HCW_OCCUPATION] [varchar](2000) NULL,
	[HCW_SETTING] [varchar](2000) NULL,
	[HEADACHE] [varchar](2000) NULL,
	[HEALTHCARE_STAFF_ROLE] [varchar](2000) NULL,
	[HEATLHCARETYPE_OUTBRE] [varchar](2000) NULL,
	[HOSPITAL_ICU_STAY] [varchar](2000) NULL,
	[HYPERTENSION] [varchar](2000) NULL,
	[ICU_ADMIT_DT] [varchar](2000) NULL,
	[ICU_DISCHARGE_DT] [varchar](2000) NULL,
	[IMM_NTNL_NTFBL_CNDTN] [varchar](2000) NULL,
	[IMMEDIATE_NND_DESC] [varchar](2000) NULL,
	[IMMUNO_CONDITION_IND] [varchar](2000) NULL,
	[INNC_NOTIFICATION_DT] [varchar](2000) NULL,
	[ISOLATION_RELEASE_DT] [varchar](2000) NULL,
	[KNOWN_DEATH_DT] [varchar](2000) NULL,
	[LINKED_TO_CASE_ID] [varchar](2000) NULL,
	[LOSS_OF_APPETITE_IND] [varchar](2000) NULL,
	[LOSS_TASTE_SMELL] [varchar](2000) NULL,
	[LOST_TO_FOLLOWUP_IND] [varchar](2000) NULL,
	[M_PNEUMONIAE_RSLT] [varchar](2000) NULL,
	[MDH_AUTOIMMUNE_DISEASE] [varchar](2000) NULL,
	[MYALGIA] [varchar](2000) NULL,
	[NAUSEA] [varchar](2000) NULL,
	[NEURO_DISABLITY_IND] [varchar](2000) NULL,
	[NEURO_DISABLITY_INFO] [varchar](2000) NULL,
	[NONHC_OTBRK_FACNAME] [varchar](2000) NULL,
	[NOTIF_COMMENT] [varchar](2000) NULL,
	[OBESITY_IND] [varchar](2000) NULL,
	[ONSET_DT_NOT_DETER] [varchar](2000) NULL,
	[OTH_CHRONIC_DIS_IND] [varchar](2000) NULL,
	[OTH_CHRONIC_DIS_TXT] [varchar](2000) NULL,
	[OTH_DIAGNOSIS_IND] [varchar](2000) NULL,
	[OTH_EXPOSURE_IND] [varchar](2000) NULL,
	[OTH_EXPOSURE_SPECIFY] [varchar](2000) NULL,
	[OTH_PATHOGEN_TEST_IND] [varchar](2000) NULL,
	[OTH_SYMPTOM_IND] [varchar](2000) NULL,
	[OTHER_SYM_SPEC] [varchar](2000) NULL,
	[OUTBREAK_HC_FAC_NAME] [varchar](2000) NULL,
	[OUTBREAK_HC_FACI_NAME] [varchar](2000) NULL,
	[OUTBREAK_HC_ROLE] [varchar](2000) NULL,
	[OUTBREAK_NOT_HC_YNU] [varchar](2000) NULL,
	[OUTBRK_NONHC_FAC_TYP] [varchar](2000) NULL,
	[PARAINFLUENZA1_4_RSLT] [varchar](2000) NULL,
	[PAT_PROCESS_STATUS] [varchar](2000) NULL,
	[PATIENT_INTERV_24HOUR] [varchar](2000) NULL,
	[PNEUMONIA] [varchar](2000) NULL,
	[PREEXISTING_COND_IND] [varchar](2000) NULL,
	[PREFERRED_LANGUAGE] [varchar](2000) NULL,
	[PSYCH_CONDITION_SPEC] [varchar](2000) NULL,
	[PSYCHIATRIC_CONDITION] [varchar](2000) NULL,
	[PUI_REPORT_TO_CDC_DT] [varchar](2000) NULL,
	[RECEIVED_ECMO_IND] [varchar](2000) NULL,
	[RECEIVED_MV_IND] [varchar](2000) NULL,
	[RHINO_ENTERO_RSLT] [varchar](2000) NULL,
	[RIGORS_IND] [varchar](2000) NULL,
	[RPTNG_CNTY] [varchar](2000) NULL,
	[RSV_RSLT] [varchar](2000) NULL,
	[SCHL_UNIV_CHLDCRE_NME] [varchar](2000) NULL,
	[SCHOOL_BGHRN] [varchar](2000) NULL,
	[SCHOOL_BLN] [varchar](2000) NULL,
	[SCHOOL_BRDWTR] [varchar](2000) NULL,
	[SCHOOL_BVRHD] [varchar](2000) NULL,
	[SCHOOL_CARBON] [varchar](2000) NULL,
	[SCHOOL_CASCADE] [varchar](2000) NULL,
	[SCHOOL_CHOUTEAU] [varchar](2000) NULL,
	[SCHOOL_COUNTY] [varchar](2000) NULL,
	[SCHOOL_CUSTER] [varchar](2000) NULL,
	[SCHOOL_DAWSON] [varchar](2000) NULL,
	[SCHOOL_DL] [varchar](2000) NULL,
	[SCHOOL_FALLON] [varchar](2000) NULL,
	[SCHOOL_FERGUS] [varchar](2000) NULL,
	[SCHOOL_FILLINBLANK] [varchar](2000) NULL,
	[SCHOOL_FLATHEAD] [varchar](2000) NULL,
	[SCHOOL_GALLATIN] [varchar](2000) NULL,
	[SCHOOL_GARFIELD] [varchar](2000) NULL,
	[SCHOOL_GLACIER] [varchar](2000) NULL,
	[SCHOOL_GRANITE] [varchar](2000) NULL,
	[SCHOOL_GV] [varchar](2000) NULL,
	[SCHOOL_HILL] [varchar](2000) NULL,
	[SCHOOL_JB] [varchar](2000) NULL,
	[SCHOOL_JEFFERSON] [varchar](2000) NULL,
	[SCHOOL_LAKE] [varchar](2000) NULL,
	[SCHOOL_LC] [varchar](2000) NULL,
	[SCHOOL_LIBERTY] [varchar](2000) NULL,
	[SCHOOL_LINCOLN] [varchar](2000) NULL,
	[SCHOOL_MADISON] [varchar](2000) NULL,
	[SCHOOL_MCCONE] [varchar](2000) NULL,
	[SCHOOL_MEAGHER] [varchar](2000) NULL,
	[SCHOOL_MINERAL] [varchar](2000) NULL,
	[SCHOOL_MISSOULA] [varchar](2000) NULL,
	[SCHOOL_MUSSELSHELL] [varchar](2000) NULL,
	[SCHOOL_PARK] [varchar](2000) NULL,
	[SCHOOL_PETROLEUM] [varchar](2000) NULL,
	[SCHOOL_PHILLIP] [varchar](2000) NULL,
	[SCHOOL_PONDERA] [varchar](2000) NULL,
	[SCHOOL_POWELL] [varchar](2000) NULL,
	[SCHOOL_PR] [varchar](2000) NULL,
	[SCHOOL_PRAIRIE] [varchar](2000) NULL,
	[SCHOOL_RAVALLI] [varchar](2000) NULL,
	[SCHOOL_RICHLAND] [varchar](2000) NULL,
	[SCHOOL_ROOSEVELT] [varchar](2000) NULL,
	[SCHOOL_ROSEBUD] [varchar](2000) NULL,
	[SCHOOL_SANDERS] [varchar](2000) NULL,
	[SCHOOL_SB] [varchar](2000) NULL,
	[SCHOOL_SG] [varchar](2000) NULL,
	[SCHOOL_SHERIDAN] [varchar](2000) NULL,
	[SCHOOL_STILLWATER] [varchar](2000) NULL,
	[SCHOOL_TETON] [varchar](2000) NULL,
	[SCHOOL_TOOLE] [varchar](2000) NULL,
	[SCHOOL_TREASURE] [varchar](2000) NULL,
	[SCHOOL_VALLEY] [varchar](2000) NULL,
	[SCHOOL_WHEATLAND] [varchar](2000) NULL,
	[SCHOOL_WIBAUX] [varchar](2000) NULL,
	[SCHOOL_YELLOWSTONE] [varchar](2000) NULL,
	[SCHOOLS_DANIELS] [varchar](2000) NULL,
	[SEVERE_ARD_EXP_IND] [varchar](2000) NULL,
	[SHIP_NAME] [varchar](2000) NULL,
	[SORE_THROAT_IND] [varchar](2000) NULL,
	[SOURCE_CASE_ID] [varchar](2000) NULL,
	[SUBSTANCE_ABUSE] [varchar](2000) NULL,
	[SYMPTOM_NOTES] [varchar](2000) NULL,
	[SYMPTOM_STATUS] [varchar](2000) NULL,
	[Symptomatic] [varchar](2000) NULL,
	[TESTING_PERFORMED_IND] [varchar](2000) NULL,
	[TOTAL_MV_DAYS] [varchar](2000) NULL,
	[TOTALCONTACTS] [varchar](2000) NULL,
	[TRANSLATOR_REQ_IND] [varchar](2000) NULL,
	[TRAVEL_DOMESTICALLY] [varchar](2000) NULL,
	[TRAVEL_INTERNATIONAL] [varchar](2000) NULL,
	[TRIBAL_AFFIL_IND] [varchar](2000) NULL,
	[TRIBAL_ENROLLED_IND] [varchar](2000) NULL,
	[TRIBE_NAME] [varchar](2000) NULL,
	[TYPE_OF_RESIDENCE] [varchar](2000) NULL,
	[UNDERLYING_COND_OTH] [varchar](2000) NULL,
	[UNDRLYNG_COND_SPECIFY] [varchar](2000) NULL,
	[UNK_EXPOSURE_SOURCE] [varchar](2000) NULL,
	[US_COVID_CASE_EXP_IND] [varchar](2000) NULL,
	[US_HC_WORKER_IND] [varchar](2000) NULL,
	[WHEEZING_IND] [varchar](2000) NULL,
	[WKPLC_CRITICAL_INFRA] [varchar](2000) NULL,
	[WKPLC_SETTING] [varchar](2000) NULL,
	[WORKPLACE_EXP] [varchar](2000) NULL,
	[BINATIONAL_RPTNG_CRIT] [varchar](8000) NULL,
	[CASE_IDENTIFY_PROCESS] [varchar](8000) NULL,
	[HIGH_RISK_TRAVEL_LOC] [varchar](8000) NULL,
	[INFO_SOURCE] [varchar](8000) NULL,
	[INTL_DESTINATIONS] [varchar](8000) NULL,
	[TRAVEL_STATE] [varchar](8000) NULL,
	[ADDL_SPECIMEN_ID_1] [varchar](2000) NULL,
	[CDC_SPECIMEN_ID_1] [varchar](2000) NULL,
	[CITY_OF_EXP_1] [varchar](2000) NULL,
	[CNTRY_OF_EXP_1] [varchar](2000) NULL,
	[CNTY_OF_EXP_1] [varchar](2000) NULL,
	[ISOLTE_SENT_STATE_LAB_1] [varchar](2000) NULL,
	[OTH_PATHOGEN_TST_1] [varchar](2000) NULL,
	[OTH_PATHOGEN_TST_RSLT_1] [varchar](2000) NULL,
	[PERFORMING_LAB_TYPE_1] [varchar](2000) NULL,
	[SPCMN_COLLECTION_DT_1] [varchar](2000) NULL,
	[SPCMN_SENT_TO_CDC_DT_1] [varchar](2000) NULL,
	[SPCMN_SENT_TO_CDC_IND_1] [varchar](2000) NULL,
	[SPEC_SENT_TO_SPHL_DT_1] [varchar](2000) NULL,
	[SPECIMEN_ID_1] [varchar](2000) NULL,
	[SPECIMEN_SOURCE_1] [varchar](2000) NULL,
	[ST_OR_PROV_OF_EXP_1] [varchar](2000) NULL,
	[STATE_ISOLATE_ID_1] [varchar](2000) NULL,
	[TEST_RESULT_1] [varchar](2000) NULL,
	[TEST_RESULT_COMMENTS_1] [varchar](2000) NULL,
	[TEST_TYPE_1] [varchar](2000) NULL,
	[ADDL_SPECIMEN_ID_2] [varchar](2000) NULL,
	[CDC_SPECIMEN_ID_2] [varchar](2000) NULL,
	[CITY_OF_EXP_2] [varchar](2000) NULL,
	[CNTRY_OF_EXP_2] [varchar](2000) NULL,
	[CNTY_OF_EXP_2] [varchar](2000) NULL,
	[ISOLTE_SENT_STATE_LAB_2] [varchar](2000) NULL,
	[OTH_PATHOGEN_TST_2] [varchar](2000) NULL,
	[OTH_PATHOGEN_TST_RSLT_2] [varchar](2000) NULL,
	[PERFORMING_LAB_TYPE_2] [varchar](2000) NULL,
	[SPCMN_COLLECTION_DT_2] [varchar](2000) NULL,
	[SPCMN_SENT_TO_CDC_DT_2] [varchar](2000) NULL,
	[SPCMN_SENT_TO_CDC_IND_2] [varchar](2000) NULL,
	[SPEC_SENT_TO_SPHL_DT_2] [varchar](2000) NULL,
	[SPECIMEN_ID_2] [varchar](2000) NULL,
	[SPECIMEN_SOURCE_2] [varchar](2000) NULL,
	[ST_OR_PROV_OF_EXP_2] [varchar](2000) NULL,
	[STATE_ISOLATE_ID_2] [varchar](2000) NULL,
	[TEST_RESULT_2] [varchar](2000) NULL,
	[TEST_RESULT_COMMENTS_2] [varchar](2000) NULL,
	[TEST_TYPE_2] [varchar](2000) NULL,
	[ADDL_SPECIMEN_ID_3] [varchar](2000) NULL,
	[CDC_SPECIMEN_ID_3] [varchar](2000) NULL,
	[CITY_OF_EXP_3] [varchar](2000) NULL,
	[CNTRY_OF_EXP_3] [varchar](2000) NULL,
	[CNTY_OF_EXP_3] [varchar](2000) NULL,
	[ISOLTE_SENT_STATE_LAB_3] [varchar](2000) NULL,
	[OTH_PATHOGEN_TST_3] [varchar](2000) NULL,
	[OTH_PATHOGEN_TST_RSLT_3] [varchar](2000) NULL,
	[PERFORMING_LAB_TYPE_3] [varchar](2000) NULL,
	[SPCMN_COLLECTION_DT_3] [varchar](2000) NULL,
	[SPCMN_SENT_TO_CDC_DT_3] [varchar](2000) NULL,
	[SPCMN_SENT_TO_CDC_IND_3] [varchar](2000) NULL,
	[SPEC_SENT_TO_SPHL_DT_3] [varchar](2000) NULL,
	[SPECIMEN_ID_3] [varchar](2000) NULL,
	[SPECIMEN_SOURCE_3] [varchar](2000) NULL,
	[ST_OR_PROV_OF_EXP_3] [varchar](2000) NULL,
	[STATE_ISOLATE_ID_3] [varchar](2000) NULL,
	[TEST_RESULT_3] [varchar](2000) NULL,
	[TEST_RESULT_COMMENTS_3] [varchar](2000) NULL,
	[TEST_TYPE_3] [varchar](2000) NULL,
	[CONGESTION] [varchar](2000) NULL,
PRIMARY KEY CLUSTERED 
(
	[COVID_CASE_DATAMART_KEY] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY] TEXTIMAGE_ON [PRIMARY]
GO
/****** Object:  Table [dbo].[COVID_LAB_CELR_DATAMART]    Script Date: 3/18/2025 5:42:18 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[COVID_LAB_CELR_DATAMART](
	[Testing_lab_accession_number] [varchar](199) NULL,
	[Testing_lab_specimen_ID] [varchar](100) NULL,
	[Submitter_unique_sample_ID] [int] NULL,
	[Submitter_sample_ID_assigner] [int] NULL,
	[Testing_lab_name] [varchar](100) NULL,
	[Testing_lab_ID] [varchar](100) NULL,
	[Testing_lab_ID_type] [varchar](4) NULL,
	[Testing_lab_street_address] [varchar](202) NULL,
	[Testing_lab_city] [varchar](100) NULL,
	[Testing_lab_state] [varchar](2) NULL,
	[Testing_lab_zip_code] [varchar](6) NULL,
	[Patient_ID] [varchar](50) NULL,
	[Patient_ID_assigner] [varchar](3) NOT NULL,
	[Patient_ID_type] [varchar](2) NOT NULL,
	[Patient_DOB] [datetime] NULL,
	[Patient_gender] [char](1) NULL,
	[Patient_race] [varchar](max) NULL,
	[Patient_city] [varchar](100) NULL,
	[Patient_state] [varchar](4) NULL,
	[Patient_zip_code] [varchar](6) NULL,
	[Patient_county] [varchar](21) NULL,
	[Patient_ethnicity] [varchar](20) NULL,
	[Patient_Age] [varchar](10) NULL,
	[Patient_Age_Units] [varchar](20) NULL,
	[Illness_Onset_Date] [varchar](max) NULL,
	[Pregnant] [varchar](2000) NULL,
	[Symptomatic_for_disease] [varchar](2000) NULL,
	[Patient_location] [int] NULL,
	[Employed_in_high_risk_setting] [int] NULL,
	[Public_Health_Case_ID] [varchar](max) NULL,
	[Specimen_type_code] [varchar](50) NULL,
	[Specimen_type_description] [varchar](250) NULL,
	[Specimen_type_code_system] [varchar](3) NULL,
	[Specimen_type_free_text] [varchar](250) NULL,
	[Specimen_collection_date_time] [varchar](23) NULL,
	[Specimen_received_date_time] [int] NULL,
	[Ordering_entity_name] [varchar](4) NOT NULL,
	[Ordering_provider_last_name] [varchar](50) NULL,
	[Ordering_provider_first_name] [varchar](50) NULL,
	[Ordering_provider_street] [varchar](202) NULL,
	[Ordering_provider_city] [varchar](100) NULL,
	[Ordering_provider_state] [varchar](4) NULL,
	[Ordering_provider_zip_code] [varchar](6) NULL,
	[Ordering_facility_name] [varchar](100) NULL,
	[Ordering_facility_street] [varchar](202) NULL,
	[Ordering_facility_city] [varchar](100) NULL,
	[Ordering_facility_state] [varchar](4) NULL,
	[Ordering_facility_zip_code] [varchar](6) NULL,
	[Ordering_facility_phone_number] [varchar](20) NULL,
	[Order_result_status] [varchar](100) NULL,
	[Date_result_released] [datetime] NULL,
	[Ordered_test_code] [varchar](50) NULL,
	[Ordered_test_description] [varchar](250) NULL,
	[Ordered_test_code_system] [varchar](300) NULL,
	[Test_performed_code] [varchar](50) NULL,
	[Test_performed_description] [varchar](250) NULL,
	[Test_performed_code_system] [varchar](300) NULL,
	[Test_result_coded] [varchar](20) NULL,
	[Test_result_description] [varchar](250) NULL,
	[Test_result_code_system] [varchar](300) NULL,
	[Test_Result_free_text] [varchar](250) NULL,
	[Test_result_comparator] [varchar](10) NULL,
	[Test_result_number] [numeric](15, 5) NULL,
	[Test_result_number_separator] [varchar](10) NULL,
	[Test_result_number2] [numeric](15, 5) NULL,
	[Test_result_units] [varchar](20) NULL,
	[Reference_range] [varchar](41) NULL,
	[Abnormal_flag] [varchar](100) NULL,
	[Test_method_description] [int] NULL,
	[Test_result_status] [varchar](100) NULL,
	[Test_date] [datetime] NULL,
	[Reporting_facility_name] [varchar](100) NULL,
	[Reporting_facility_ID] [varchar](100) NULL,
	[lab_update_dt] [datetime] NULL,
	[Report_facil_data_source_app] [int] NULL,
	[csv_file_version_no] [varchar](11) NOT NULL,
	[File_created_date] [datetime] NOT NULL,
	[Employed_in_healthcare] [varchar](2000) NULL,
	[First_test] [varchar](2000) NULL,
	[Hospitalized] [varchar](2000) NULL,
	[ICU] [varchar](2000) NULL,
	[Resident_congregate_setting] [varchar](2000) NULL,
	[Most_recent_test_date] [int] NULL,
	[Most_recent_test_result] [int] NULL,
	[Most_recent_test_type] [int] NULL,
	[Disease_symptoms] [int] NULL,
	[Patient_occupation] [int] NULL,
	[Patient_residency_type] [int] NULL,
	[Ordering_facility_county] [varchar](21) NULL,
	[Ordering_provider_county] [varchar](21) NULL,
	[Ordering_provider_ID] [varchar](199) NULL,
	[Patient_death_date] [datetime] NULL,
	[Patient_death_indicator] [varchar](20) NULL,
	[Specimen_source_site_code] [varchar](20) NULL,
	[Specimen_source_site_code_sys] [int] NULL,
	[Specimen_source_site_descrip] [varchar](100) NULL,
	[Order_test_date] [datetime] NULL,
	[Testing_lab_county] [varchar](20) NULL,
	[Device_instance_ID_1] [varchar](199) NULL,
	[Device_instance_ID_2] [varchar](199) NULL,
	[Device_type_ID_1] [varchar](199) NULL,
	[Device_type_ID_2] [varchar](199) NULL
) ON [PRIMARY] TEXTIMAGE_ON [PRIMARY]
GO
/****** Object:  Table [dbo].[COVID_LAB_DATAMART]    Script Date: 3/18/2025 5:42:18 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[COVID_LAB_DATAMART](
	[COVID_LAB_DATAMART_KEY] [bigint] NOT NULL,
	[Lab_Local_ID] [varchar](50) NULL,
	[Ordered_Test_Cd] [varchar](50) NULL,
	[Ordered_Test_Desc] [varchar](1000) NULL,
	[Ordered_Test_Code_System] [varchar](300) NULL,
	[Order_test_date] [datetime] NULL,
	[Electronic_Ind] [char](1) NULL,
	[Program_Area_Cd] [varchar](20) NULL,
	[Jurisdiction_Cd] [varchar](20) NULL,
	[program_jurisdiction_oid] [bigint] NULL,
	[Lab_Report_Dt] [datetime] NULL,
	[Lab_Rpt_Received_By_PH_Dt] [datetime] NULL,
	[Order_result_status] [varchar](100) NULL,
	[Jurisdiction_Nm] [varchar](50) NULL,
	[Specimen_Cd] [varchar](50) NULL,
	[Specimen_Desc] [varchar](100) NULL,
	[Specimen_type_free_text] [varchar](1000) NULL,
	[Specimen_Id] [varchar](100) NULL,
	[Specimen_Source_Site_Cd] [varchar](20) NULL,
	[Specimen_Source_Site_Desc] [varchar](100) NULL,
	[Testing_Lab_Accession_Number] [varchar](199) NULL,
	[Lab_Added_Dt] [datetime] NULL,
	[Lab_Update_Dt] [datetime] NULL,
	[Specimen_Coll_Dt] [datetime] NULL,
	[Resulted_Test_Cd] [varchar](50) NULL,
	[Resulted_Test_Desc] [varchar](1000) NULL,
	[Resulted_Test_Code_System] [varchar](300) NULL,
	[Device_Instance_Id_1] [varchar](199) NULL,
	[Device_Instance_Id_2] [varchar](199) NULL,
	[Test_result_status] [varchar](100) NULL,
	[Test_Method_Desc] [varchar](2000) NULL,
	[Device_Type_Id_1] [varchar](199) NULL,
	[Device_Type_Id_2] [varchar](199) NULL,
	[Perform_Facility_Name] [varchar](100) NULL,
	[Testing_lab_Address_One] [varchar](100) NULL,
	[Testing_lab_Address_Two] [varchar](100) NULL,
	[Testing_lab_Country] [varchar](20) NULL,
	[Testing_lab_County] [varchar](20) NULL,
	[Testing_lab_County_Desc] [varchar](255) NULL,
	[Testing_lab_City] [varchar](100) NULL,
	[Testing_lab_State_Cd] [varchar](20) NULL,
	[Testing_lab_State] [varchar](2) NULL,
	[Testing_lab_Zip_Cd] [varchar](20) NULL,
	[Result_Cd] [varchar](20) NULL,
	[Result_Cd_Sys] [varchar](300) NULL,
	[Result_Desc] [varchar](300) NULL,
	[Text_Result_Desc] [nvarchar](max) NULL,
	[Numeric_Comparator_Cd] [varchar](10) NULL,
	[Numeric_Value_1] [numeric](15, 5) NULL,
	[Numeric_Value_2] [numeric](15, 5) NULL,
	[Numeric_Unit_Cd] [varchar](20) NULL,
	[Numeric_Low_Range] [varchar](20) NULL,
	[Numeric_High_Range] [varchar](20) NULL,
	[Numeric_Separator_Cd] [varchar](10) NULL,
	[Interpretation_Cd] [varchar](20) NULL,
	[Interpretation_Desc] [varchar](100) NULL,
	[Result_Comments] [nvarchar](max) NULL,
	[Result] [nvarchar](max) NULL,
	[Lab_Comments] [varchar](max) NULL,
	[Result_Category] [varchar](13) NULL,
	[Last_Name] [varchar](50) NULL,
	[Middle_Name] [varchar](50) NULL,
	[First_Name] [varchar](50) NULL,
	[Patient_Local_ID] [varchar](50) NULL,
	[Patient_ID] [varchar](50) NULL,
	[Current_Sex_Cd] [char](1) NULL,
	[Age_Reported] [varchar](10) NULL,
	[Age_Unit_Cd] [varchar](20) NULL,
	[Birth_Dt] [datetime] NULL,
	[Patient_Death_Date] [datetime] NULL,
	[Patient_Death_Ind] [varchar](20) NULL,
	[Phone_Number] [varchar](20) NULL,
	[Address_One] [varchar](100) NULL,
	[Address_Two] [varchar](100) NULL,
	[City] [varchar](100) NULL,
	[State_Cd] [varchar](20) NULL,
	[State] [varchar](2) NULL,
	[Zip_Code] [varchar](20) NULL,
	[County_Cd] [varchar](20) NULL,
	[County_Desc] [varchar](255) NULL,
	[Patient_Race_Calc] [nvarchar](max) NULL,
	[Patient_Ethnicity] [varchar](20) NULL,
	[Reporting_Facility_Name] [varchar](100) NULL,
	[Reporting_Facility_Address_One] [varchar](100) NULL,
	[Reporting_Facility_Address_Two] [varchar](100) NULL,
	[Reporting_Facility_Country] [varchar](20) NULL,
	[Reporting_Facility_County] [varchar](20) NULL,
	[Reporting_Facility_County_Desc] [varchar](255) NULL,
	[Reporting_Facility_City] [varchar](100) NULL,
	[Reporting_Facility_State_Cd] [varchar](20) NULL,
	[Reporting_Facility_State] [varchar](2) NULL,
	[Reporting_Facility_Zip_Cd] [varchar](20) NULL,
	[Reporting_Facility_Clia] [varchar](100) NULL,
	[Reporting_Facility_Phone_Nbr] [varchar](20) NULL,
	[Reporting_Facility_Phone_Ext] [varchar](20) NULL,
	[Ordering_Facility_Name] [varchar](100) NULL,
	[Ordering_Facility_Address_One] [varchar](100) NULL,
	[Ordering_Facility_Address_Two] [varchar](100) NULL,
	[Ordering_Facility_Country] [varchar](20) NULL,
	[Ordering_Facility_County] [varchar](20) NULL,
	[Ordering_Facility_County_Desc] [varchar](255) NULL,
	[Ordering_Facility_City] [varchar](100) NULL,
	[Ordering_Facility_State_Cd] [varchar](20) NULL,
	[Ordering_Facility_State] [varchar](2) NULL,
	[Ordering_Facility_Zip_Cd] [varchar](20) NULL,
	[Ordering_Facility_Phone_Nbr] [varchar](20) NULL,
	[Ordering_Facility_Phone_Ext] [varchar](20) NULL,
	[Ordering_Provider_First_Name] [varchar](50) NULL,
	[Ordering_Provider_Last_Name] [varchar](50) NULL,
	[Ordering_Provider_Address_One] [varchar](100) NULL,
	[Ordering_Provider_Address_Two] [varchar](100) NULL,
	[Ordering_Provider_Country] [varchar](20) NULL,
	[Ordering_Provider_County] [varchar](20) NULL,
	[Ordering_Provider_County_Desc] [varchar](255) NULL,
	[Ordering_Provider_City] [varchar](100) NULL,
	[Ordering_Provider_State_Cd] [varchar](20) NULL,
	[Ordering_Provider_State] [varchar](2) NULL,
	[Ordering_Provider_Zip_Cd] [varchar](20) NULL,
	[Ordering_Provider_Phone_Nbr] [varchar](20) NULL,
	[Ordering_Provider_Phone_Ext] [varchar](20) NULL,
	[Ordering_Provider_Id] [varchar](199) NULL,
	[Associated_Case_ID] [nvarchar](max) NULL,
	[Observation_Uid] [bigint] NOT NULL,
	[EMPLOYED_IN_HEALTHCARE] [varchar](2000) NULL,
	[FIRST_TEST] [varchar](2000) NULL,
	[HOSPITALIZED] [varchar](2000) NULL,
	[ICU] [varchar](2000) NULL,
	[ILLNESS_ONSET_DATE] [varchar](2000) NULL,
	[PATIENT_AGE] [varchar](2000) NULL,
	[PREGNANT] [varchar](2000) NULL,
	[RESIDENT_CONGREGATE_SETTING] [varchar](2000) NULL,
	[SYMPTOMATIC_FOR_DISEASE] [varchar](2000) NULL,
PRIMARY KEY CLUSTERED 
(
	[COVID_LAB_DATAMART_KEY] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY] TEXTIMAGE_ON [PRIMARY]
GO
/****** Object:  Table [dbo].[D_CASE_MANAGEMENT]    Script Date: 3/18/2025 5:42:18 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[D_CASE_MANAGEMENT](
	[ACT_REF_TYPE_CD] [varchar](20) NULL,
	[ADD_USER_ID] [numeric](20, 0) NULL,
	[ADI_900_STATUS_CD] [varchar](20) NULL,
	[ADI_COMPLEXION] [varchar](20) NULL,
	[ADI_EHARS_ID] [varchar](10) NULL,
	[ADI_HAIR] [varchar](20) NULL,
	[ADI_HEIGHT] [varchar](20) NULL,
	[ADI_HEIGHT_LEGACY_CASE] [varchar](20) NULL,
	[ADI_OTHER_IDENTIFYING_INFO] [varchar](2000) NULL,
	[ADI_SIZE_BUILD] [varchar](20) NULL,
	[CA_INIT_INTVWR_ASSGN_DT] [datetime2](3) NULL,
	[CA_INTERVIEWER_ASSIGN_DT] [datetime2](3) NULL,
	[CA_PATIENT_INTV_STATUS] [varchar](29) NULL,
	[CASE_OID] [numeric](20, 0) NULL,
	[CASE_REVIEW_STATUS] [varchar](20) NULL,
	[CASE_REVIEW_STATUS_DATE] [datetime2](3) NULL,
	[CC_CLOSED_DT] [datetime2](3) NULL,
	[D_CASE_MANAGEMENT_KEY] [float] NULL,
	[EPI_LINK_ID] [varchar](20) NULL,
	[FIELD_FOLL_UP_OOJ_OUTCOME] [varchar](20) NULL,
	[FL_FUP_ACTUAL_REF_TYPE] [varchar](15) NULL,
	[FL_FUP_DISPO_DT] [datetime2](3) NULL,
	[FL_FUP_DISPOSITION_CD] [varchar](20) NULL,
	[FL_FUP_DISPOSITION_DESC] [varchar](44) NULL,
	[FL_FUP_EXAM_DT] [datetime2](3) NULL,
	[FL_FUP_EXPECTED_DT] [datetime2](3) NULL,
	[FL_FUP_EXPECTED_IN_IND] [varchar](3) NULL,
	[FL_FUP_FIELD_RECORD_NUM] [varchar](20) NULL,
	[FL_FUP_INIT_ASSGN_DT] [datetime2](3) NULL,
	[FL_FUP_INTERNET_OUTCOME] [varchar](41) NULL,
	[FL_FUP_INTERNET_OUTCOME_CD] [varchar](10) NULL,
	[FL_FUP_INVESTIGATOR_ASSGN_DT] [datetime2](3) NULL,
	[FL_FUP_NOTIFICATION_PLAN_CD] [varchar](15) NULL,
	[FL_FUP_OOJ_OUTCOME] [varchar](44) NULL,
	[FL_FUP_PROV_DIAGNOSIS] [varchar](3) NULL,
	[FL_FUP_PROV_EXM_REASON] [varchar](43) NULL,
	[FLD_FOLL_UP_EXPECTED_IN] [varchar](20) NULL,
	[FLD_FOLL_UP_NOTIFICATION_PLAN] [varchar](20) NULL,
	[FLD_FOLL_UP_PROV_DIAGNOSIS] [varchar](20) NULL,
	[FLD_FOLL_UP_PROV_EXM_REASON] [varchar](20) NULL,
	[INIT_FUP_CLINIC_CODE] [varchar](50) NULL,
	[INIT_FUP_CLOSED_DT] [datetime2](3) NULL,
	[INIT_FUP_INITIAL_FOLL_UP] [varchar](22) NULL,
	[INIT_FUP_INITIAL_FOLL_UP_CD] [varchar](20) NULL,
	[INIT_FUP_INTERNET_FOLL_UP_CD] [varchar](20) NULL,
	[INIT_FOLL_UP_NOTIFIABLE] [varchar](36) NULL,
	[INIT_FUP_NOTIFIABLE_CD] [varchar](20) NULL,
	[INITIATING_AGNCY] [varchar](20) NULL,
	[INTERNET_FOLL_UP] [varchar](3) NULL,
	[INVESTIGATION_KEY] [numeric](20, 0) NULL,
	[OOJ_AGENCY] [varchar](20) NULL,
	[OOJ_DUE_DATE] [datetime2](3) NULL,
	[OOJ_INITG_AGNCY_OUTC_DUE_DATE] [datetime2](3) NULL,
	[OOJ_INITG_AGNCY_OUTC_SNT_DATE] [datetime2](3) NULL,
	[OOJ_INITG_AGNCY_RECD_DATE] [datetime2](3) NULL,
	[OOJ_NUMBER] [varchar](20) NULL,
	[PAT_INTV_STATUS_CD] [varchar](20) NULL,
	[STATUS_900] [varchar](44) NULL,
	[SURV_CLOSED_DT] [datetime2](3) NULL,
	[SURV_INVESTIGATOR_ASSGN_DT] [datetime2](3) NULL,
	[SURV_PATIENT_FOLL_UP] [varchar](20) NULL,
	[SURV_PATIENT_FOLL_UP_CD] [varchar](22) NULL,
	[SURV_PROV_EXM_REASON] [varchar](20) NULL,
	[SURV_PROVIDER_CONTACT] [varchar](27) NULL,
	[SURV_PROVIDER_CONTACT_CD] [varchar](20) NULL,
	[SURV_PROVIDER_DIAGNOSIS] [varchar](20) NULL,
	[SURV_PROVIDER_EXAM_REASON] [varchar](43) NULL
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[D_INTERVIEW]    Script Date: 3/18/2025 5:42:18 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[D_INTERVIEW](
	[D_INTERVIEW_KEY] [float] NULL,
	[IX_STATUS_CD] [varchar](4000) NULL,
	[IX_DATE] [datetime] NULL,
	[IX_INTERVIEWEE_ROLE_CD] [varchar](4000) NULL,
	[IX_TYPE_CD] [varchar](4000) NULL,
	[IX_LOCATION_CD] [varchar](4000) NULL,
	[LOCAL_ID] [varchar](4000) NULL,
	[RECORD_STATUS_CD] [varchar](4000) NULL,
	[RECORD_STATUS_TIME] [datetime] NULL,
	[ADD_TIME] [datetime] NULL,
	[ADD_USER_ID] [numeric](21, 0) NULL,
	[LAST_CHG_TIME] [datetime] NULL,
	[LAST_CHG_USER_ID] [numeric](21, 0) NULL,
	[VERSION_CTRL_NBR] [numeric](11, 0) NULL,
	[IX_STATUS] [varchar](4000) NULL,
	[IX_INTERVIEWEE_ROLE] [varchar](4000) NULL,
	[IX_TYPE] [varchar](4000) NULL,
	[IX_LOCATION] [varchar](4000) NULL,
	[IX_CONTACTS_NAMED_IND] [varchar](4000) NULL,
	[IX_900_SITE_TYPE] [varchar](4000) NULL,
	[IX_INTERVENTION] [varchar](4000) NULL,
	[IX_900_SITE_ID] [varchar](2000) NULL,
	[IX_900_SITE_ZIP] [varchar](2000) NULL,
	[CLN_CARE_STATUS_IXS] [varchar](4000) NULL
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[D_INV_ADMINISTRATIVE]    Script Date: 3/18/2025 5:42:18 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[D_INV_ADMINISTRATIVE](
	[D_INV_ADMINISTRATIVE_KEY] [float] NULL,
	[nbs_case_answer_uid] [numeric](21, 0) NULL,
	[ADM_PUI_REPORT_TO_CDC_DT] [date] NULL,
	[ADM_USArrivalDate] [date] NULL,
	[ADM_CTRL_MEASURE_IMP_DT] [date] NULL,
	[ADM_CASE_REPORT_TO_CDC_DT] [date] NULL,
	[ADM_ABSTRACTION_DT] [date] NULL,
	[ADM_INNC_NOTIFICATION_DT] [date] NULL,
	[ADM_FIRST_RPT_TO_PHD_DT] [date] NULL,
	[ADM_BINATIONAL_RPTNG_CRIT] [varchar](1999) NULL,
	[ADM_BINATIONALREPORTING] [varchar](1999) NULL,
	[ADM_BirthCntryGuard1] [varchar](1999) NULL,
	[ADM_BirthCntryGuard2] [varchar](1999) NULL,
	[ADM_CASE_IDENTIFY_PROCESS] [varchar](1999) NULL,
	[ADM_CASE_IDENTIFY_PROCESS_OTH] [varchar](1999) NULL,
	[ADM_CASE_STATUS_REASON] [varchar](1999) NULL,
	[ADM_CASE_VERIFCTON_CAT] [varchar](1999) NULL,
	[ADM_CauseofDeath] [varchar](1999) NULL,
	[ADM_CountriesLivedIn] [varchar](1999) NULL,
	[ADM_DISSEMINATED_IND] [varchar](1999) NULL,
	[ADM_IMM_NTNL_NTFBL_CNDTN] [varchar](1999) NULL,
	[ADM_INFO_SOURCE] [varchar](1999) NULL,
	[ADM_LivedOutsideUS] [varchar](1999) NULL,
	[ADM_MOVED_DIFF_RPT_AREA] [varchar](1999) NULL,
	[ADM_MOVED_OUT_OF_CNTRY] [varchar](1999) NULL,
	[ADM_MOVED_OUT_OF_STATE] [varchar](1999) NULL,
	[ADM_NK1_RELATIONSHIP] [varchar](1999) NULL,
	[ADM_ONSET_DT_NOT_DETER] [varchar](1999) NULL,
	[ADM_PAT_PROCESS_STATUS] [varchar](1999) NULL,
	[ADM_PAT_TRT_MDR_CASE] [varchar](1999) NULL,
	[ADM_PHVS_YESNOUNKNOWN_CDC] [varchar](1999) NULL,
	[ADM_PUBLISHED_INDICATOR] [varchar](1999) NULL,
	[ADM_REASON_NO_DISPO] [varchar](1999) NULL,
	[ADM_REFERRAL_BASIS_OOJ] [varchar](1999) NULL,
	[ADM_RPTNG_CNTY] [varchar](1999) NULL,
	[ADM_SAMPLED_FOR_ENHC_INV] [varchar](1999) NULL,
	[ADM_StateCaseNoLnkRsn1] [varchar](1999) NULL,
	[ADM_StateCaseNoLnkRsn2] [varchar](1999) NULL,
	[ADM_TB_BIRTHCOUNTRY] [varchar](1999) NULL,
	[ADM_TB_DIAG_STATUS] [varchar](1999) NULL,
	[ADM_TB_REMAIN_IN_US] [varchar](1999) NULL,
	[ADM_TBCASECOUNTSTATUS] [varchar](1999) NULL,
	[ADM_TBINITIALREASONEVAL] [varchar](1999) NULL,
	[ADM_TRANSLATOR_REQ_IND] [varchar](1999) NULL,
	[ADM_TRANSNATIONAL_REF] [varchar](1999) NULL,
	[ADM_USBorn] [varchar](1999) NULL,
	[ADM_RPTD_STT_CSE_NBR4] [varchar](2000) NULL,
	[ADM_ABSTRACTOR_NM] [varchar](2000) NULL,
	[ADM_RPTD_STT_CSE_NBR5] [varchar](2000) NULL,
	[ADM_LinkStateCaseNo1] [varchar](2000) NULL,
	[ADM_RPTD_STT_CSE_NBR2] [varchar](2000) NULL,
	[ADM_NOTIF_COMMENT] [varchar](2000) NULL,
	[ADM_PREV_REPRT_ST_CSE_NBR] [varchar](2000) NULL,
	[ADM_RPTD_STT_CSE_NBR3] [varchar](2000) NULL,
	[ADM_DGMQ_ID] [varchar](2000) NULL,
	[ADM_CDC_ASSIGNED_ID] [varchar](2000) NULL,
	[ADM_NTSS_STATE_CASE_NBR] [varchar](2000) NULL,
	[ADM_IMMEDIATE_NND_DESC] [varchar](2000) NULL,
	[ADM_LinkStateCaseNo2] [varchar](2000) NULL,
	[ADM_CITY_CNTY_CASE_NBR] [varchar](2000) NULL,
	[ADM_PREFERRED_LANGUAGE] [varchar](2000) NULL,
	[ADM_RPTD_STT_CSE_NBR1] [varchar](2000) NULL
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[D_INV_EPIDEMIOLOGY]    Script Date: 3/18/2025 5:42:18 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[D_INV_EPIDEMIOLOGY](
	[D_INV_EPIDEMIOLOGY_KEY] [float] NULL,
	[nbs_case_answer_uid] [numeric](21, 0) NULL,
	[EPI_EPI_CONTACTSNOTIFEXPO] [varchar](2000) NULL,
	[EPI_FemaleSexPartners] [varchar](2000) NULL,
	[EPI_MaleSexPartner] [varchar](2000) NULL,
	[EPI_PEP_FIRST_DOSE_DT] [date] NULL,
	[EPI_RIG_DATE] [date] NULL,
	[EPI_LAB_TEST_DATE] [date] NULL,
	[EPI_DatePEPRec] [date] NULL,
	[EPI_DATE_BLOOD_DONATED] [date] NULL,
	[EPI_FIRST_EXPOSURE_DT] [date] NULL,
	[EPI_EXPOSURE_DT] [date] NULL,
	[EPI_ACLF_OUTBREAK] [varchar](1999) NULL,
	[EPI_ACLF_OUTBREAK_NAME] [varchar](1999) NULL,
	[EPI_ACLF_OUTBREAK_ROLE] [varchar](1999) NULL,
	[EPI_ACLF_OUTBREAK_TYPE] [varchar](1999) NULL,
	[EPI_ANIM_VACC_STAT_ATTACK] [varchar](1999) NULL,
	[EPI_ANIMAL_EXPOSURE_IND] [varchar](1999) NULL,
	[EPI_ANIMAL_SPECIES_ATTACK] [varchar](1999) NULL,
	[EPI_ANIMAL_STATUS] [varchar](1999) NULL,
	[EPI_ANIMAL_VARIANT] [varchar](1999) NULL,
	[EPI_ANTIBODY_RESP_IND] [varchar](1999) NULL,
	[EPI_ChildCareCase] [varchar](1999) NULL,
	[EPI_CHINA_HC_HISTORY_IND] [varchar](1999) NULL,
	[EPI_CNT_RCVD_HLTHCRE_OTS] [varchar](1999) NULL,
	[EPI_CNTRY_USUAL_RESID] [varchar](1999) NULL,
	[EPI_CNTY_OF_EXP] [varchar](1999) NULL,
	[EPI_ContactBabysitter] [varchar](1999) NULL,
	[EPI_ContactChildcare] [varchar](1999) NULL,
	[EPI_ContactHousehold] [varchar](1999) NULL,
	[EPI_ContactOfCase] [varchar](1999) NULL,
	[EPI_ContactOther] [varchar](1999) NULL,
	[EPI_ContactPlaymate] [varchar](1999) NULL,
	[EPI_ContactSexPartner] [varchar](1999) NULL,
	[EPI_COVID_BREAK_PCR_EPI] [varchar](1999) NULL,
	[EPI_COVID_BREAKTHROUGH] [varchar](1999) NULL,
	[EPI_COVID_VARIANT_CLASSIF] [varchar](1999) NULL,
	[EPI_COVID_VARIANT_SEQ_YNU] [varchar](1999) NULL,
	[EPI_COVID19_CLUSTER_YNU] [varchar](1999) NULL,
	[EPI_COVIDREINFECTIONSTATU] [varchar](1999) NULL,
	[EPI_COVIDREINFYNU] [varchar](1999) NULL,
	[EPI_CRCTIONAL_FAC_LNG_TY] [varchar](1999) NULL,
	[EPI_CRCTIONAL_FAC_LNG_TY_OTH] [varchar](1999) NULL,
	[EPI_CRCTIONAL_FAC_TYPE] [varchar](1999) NULL,
	[EPI_CRCTIONAL_FAC_TYPE_OTH] [varchar](1999) NULL,
	[EPI_CTT_CONF_CASE_COMM] [varchar](1999) NULL,
	[EPI_CTT_CONF_CASE_HLTHCR] [varchar](1999) NULL,
	[EPI_CTT_CONF_CASE_HSHLD] [varchar](1999) NULL,
	[EPI_CTT_CONF_CASE_PAT_IND] [varchar](1999) NULL,
	[EPI_DaycareContact] [varchar](1999) NULL,
	[EPI_DOM_ANIM_EXP_IND] [varchar](1999) NULL,
	[EPI_EPI_PEP_PROVOKED] [varchar](1999) NULL,
	[EPI_EpiLinked] [varchar](1999) NULL,
	[EPI_EVALCONTACTINV] [varchar](1999) NULL,
	[EPI_EXP_PERS_VACC_IND] [varchar](1999) NULL,
	[EPI_EXPOS_SITE_BODY] [varchar](1999) NULL,
	[EPI_EXPOSURE_COUNTY] [varchar](1999) NULL,
	[EPI_EXPOSURE_DATE_UNK] [varchar](1999) NULL,
	[EPI_EXPOSURE_STATE] [varchar](1999) NULL,
	[EPI_EXPOSURE_TO_COVID_4WK] [varchar](1999) NULL,
	[EPI_EXPOSURES_RABIES] [varchar](1999) NULL,
	[EPI_FoodHandler] [varchar](1999) NULL,
	[EPI_HC_CONTACT_TYPE] [varchar](1999) NULL,
	[EPI_HEALTHCARE_STAFF_ROLE] [varchar](1999) NULL,
	[EPI_HEATLHCARETYPE_OUTBRE] [varchar](1999) NULL,
	[EPI_HUMAN_EXPOS_TYPE] [varchar](1999) NULL,
	[EPI_ID_BY_BLOOD_SCREEN] [varchar](1999) NULL,
	[EPI_IDENTIFIEDCONTACTINV] [varchar](1999) NULL,
	[EPI_InDayCare] [varchar](1999) NULL,
	[EPI_INDEX_ANIMAL_TEST_IND] [varchar](1999) NULL,
	[EPI_INFECTED_IN_UTERO] [varchar](1999) NULL,
	[EPI_IVDrugUse] [varchar](1999) NULL,
	[EPI_LAB_ACQUIRED_ILLNESS] [varchar](1999) NULL,
	[EPI_LAB_TEST_RESLT_RAB] [varchar](1999) NULL,
	[EPI_LOST_TO_FOLLOWUP_IND] [varchar](1999) NULL,
	[EPI_NON_LAB_ACQ_ILLNESS] [varchar](1999) NULL,
	[EPI_OTH_EXPOSURE_IND] [varchar](1999) NULL,
	[EPI_OUT_OF_COUNTRY_EXPOS] [varchar](1999) NULL,
	[EPI_OUT_OF_STATE_EXPOS] [varchar](1999) NULL,
	[EPI_OUTBREAK_HC_ROLE] [varchar](1999) NULL,
	[EPI_OUTBREAK_NOT_HC_YNU] [varchar](1999) NULL,
	[EPI_OutbreakAssoc] [varchar](1999) NULL,
	[EPI_OutbreakFoodHndlr] [varchar](1999) NULL,
	[EPI_outbreakNonFoodHndlr] [varchar](1999) NULL,
	[EPI_OutbreakUnidentified] [varchar](1999) NULL,
	[EPI_OutbreakWaterborne] [varchar](1999) NULL,
	[EPI_OUTBRK_NONHC_FAC_TYP] [varchar](1999) NULL,
	[EPI_PATIENT_INTERV_24HOUR] [varchar](1999) NULL,
	[EPI_PEP_ADMIN_IND] [varchar](1999) NULL,
	[EPI_PEP_NotAdmin] [varchar](1999) NULL,
	[EPI_PEP_NotRec] [varchar](1999) NULL,
	[EPI_PEP_RECOMMEND_IND] [varchar](1999) NULL,
	[EPI_PEP_REGIMEN] [varchar](1999) NULL,
	[EPI_RCVD_HLTHCRE_OTS_STT] [varchar](1999) NULL,
	[EPI_RCVD_HLTHCRE_OTS_USA] [varchar](1999) NULL,
	[EPI_REAS_INDX_ANM_NOT_TST] [varchar](1999) NULL,
	[EPI_REAS_PREV_VACCIN] [varchar](1999) NULL,
	[EPI_RecDrugUse] [varchar](1999) NULL,
	[EPI_reporting_jurisdict] [varchar](1999) NULL,
	[EPI_RIG_IND] [varchar](1999) NULL,
	[EPI_SCHOOL_BGHRN] [varchar](1999) NULL,
	[EPI_SCHOOL_BLN] [varchar](1999) NULL,
	[EPI_SCHOOL_BRDWTR] [varchar](1999) NULL,
	[EPI_SCHOOL_BVRHD] [varchar](1999) NULL,
	[EPI_SCHOOL_CARBON] [varchar](1999) NULL,
	[EPI_SCHOOL_CARTER] [varchar](1999) NULL,
	[EPI_SCHOOL_CASCADE] [varchar](1999) NULL,
	[EPI_SCHOOL_CHOUTEAU] [varchar](1999) NULL,
	[EPI_SCHOOL_COUNTY] [varchar](1999) NULL,
	[EPI_SCHOOL_CUSTER] [varchar](1999) NULL,
	[EPI_SCHOOL_DAWSON] [varchar](1999) NULL,
	[EPI_SCHOOL_DL] [varchar](1999) NULL,
	[EPI_SCHOOL_FALLON] [varchar](1999) NULL,
	[EPI_SCHOOL_FERGUS] [varchar](1999) NULL,
	[EPI_SCHOOL_FLATHEAD] [varchar](1999) NULL,
	[EPI_SCHOOL_GALLATIN] [varchar](1999) NULL,
	[EPI_SCHOOL_GARFIELD] [varchar](1999) NULL,
	[EPI_SCHOOL_GLACIER] [varchar](1999) NULL,
	[EPI_SCHOOL_GRANITE] [varchar](1999) NULL,
	[EPI_SCHOOL_GV] [varchar](1999) NULL,
	[EPI_SCHOOL_HILL] [varchar](1999) NULL,
	[EPI_SCHOOL_JB] [varchar](1999) NULL,
	[EPI_SCHOOL_JEFFERSON] [varchar](1999) NULL,
	[EPI_SCHOOL_LAKE] [varchar](1999) NULL,
	[EPI_SCHOOL_LC] [varchar](1999) NULL,
	[EPI_SCHOOL_LIBERTY] [varchar](1999) NULL,
	[EPI_SCHOOL_LINCOLN] [varchar](1999) NULL,
	[EPI_SCHOOL_MADISON] [varchar](1999) NULL,
	[EPI_SCHOOL_MCCONE] [varchar](1999) NULL,
	[EPI_SCHOOL_MEAGHER] [varchar](1999) NULL,
	[EPI_SCHOOL_MINERAL] [varchar](1999) NULL,
	[EPI_SCHOOL_MISSOULA] [varchar](1999) NULL,
	[EPI_SCHOOL_MUSSELSHELL] [varchar](1999) NULL,
	[EPI_SCHOOL_PARK] [varchar](1999) NULL,
	[EPI_SCHOOL_PETROLEUM] [varchar](1999) NULL,
	[EPI_SCHOOL_PHILLIP] [varchar](1999) NULL,
	[EPI_SCHOOL_PONDERA] [varchar](1999) NULL,
	[EPI_SCHOOL_POWELL] [varchar](1999) NULL,
	[EPI_SCHOOL_PR] [varchar](1999) NULL,
	[EPI_SCHOOL_PRAIRIE] [varchar](1999) NULL,
	[EPI_SCHOOL_RAVALLI] [varchar](1999) NULL,
	[EPI_SCHOOL_RICHLAND] [varchar](1999) NULL,
	[EPI_SCHOOL_ROOSEVELT] [varchar](1999) NULL,
	[EPI_SCHOOL_ROSEBUD] [varchar](1999) NULL,
	[EPI_SCHOOL_SANDERS] [varchar](1999) NULL,
	[EPI_SCHOOL_SB] [varchar](1999) NULL,
	[EPI_SCHOOL_SG] [varchar](1999) NULL,
	[EPI_SCHOOL_SHERIDAN] [varchar](1999) NULL,
	[EPI_SCHOOL_STILLWATER] [varchar](1999) NULL,
	[EPI_SCHOOL_TETON] [varchar](1999) NULL,
	[EPI_SCHOOL_TOOLE] [varchar](1999) NULL,
	[EPI_SCHOOL_TREASURE] [varchar](1999) NULL,
	[EPI_SCHOOL_VALLEY] [varchar](1999) NULL,
	[EPI_SCHOOL_WHEATLAND] [varchar](1999) NULL,
	[EPI_SCHOOL_WIBAUX] [varchar](1999) NULL,
	[EPI_SCHOOL_YELLOWSTONE] [varchar](1999) NULL,
	[EPI_SCHOOLS_DANIELS] [varchar](1999) NULL,
	[EPI_SERO_MONITOR_IND] [varchar](1999) NULL,
	[EPI_SEVERE_ARD_EXP_IND] [varchar](1999) NULL,
	[EPI_UNK_EXPOSURE_SOURCE] [varchar](1999) NULL,
	[EPI_US_COVID_CASE_EXP_IND] [varchar](1999) NULL,
	[EPI_US_HC_WORKER_IND] [varchar](1999) NULL,
	[EPI_VACC_ADMIN_USA_IND] [varchar](1999) NULL,
	[EPI_VACC_STAT_RPTEDBY_ATT] [varchar](1999) NULL,
	[EPI_VARIANT_TEST_FAC] [varchar](1999) NULL,
	[EPI_VARIANT_TEST_IND] [varchar](1999) NULL,
	[RSK_TBOCCUPATIONCATEGOR] [varchar](1999) NULL,
	[SOURCE_SPREAD] [varchar](1999) NULL,
	[EPI_SCHL_UNIV_CHLDCRE_NME] [varchar](2000) NULL,
	[EPI_ACLF_OUTBREAK_OTHER] [varchar](2000) NULL,
	[EPI_EXPOS_SCENARIO_DESC] [varchar](2000) NULL,
	[EPI_COVID_VARIANT] [varchar](2000) NULL,
	[EPI_COVID_VARIANT_LABNME] [varchar](2000) NULL,
	[EPI_OWNER_ADDRESS] [varchar](2000) NULL,
	[EPI_SCHOOL_FILLINBLANK] [varchar](2000) NULL,
	[EPI_OtherPEPnotrec] [varchar](2000) NULL,
	[EPI_VET_NAME_PRACTICE] [varchar](2000) NULL,
	[EPI_COVID_CONTACTS_POSIT] [varchar](2000) NULL,
	[EPI_OTH_VARIANT_TEST_FAC] [varchar](2000) NULL,
	[EPI_OutbreakFoodItem] [varchar](2000) NULL,
	[EPI_COUNTY_OF_EXPOSURE] [varchar](2000) NULL,
	[EPI_NAME_COVID19_CLUSTER] [varchar](2000) NULL,
	[EPI_OTH_EXP_BODY_SITE] [varchar](2000) NULL,
	[EPI_CONTACT_TEST_14DAYS_N] [varchar](2000) NULL,
	[EPI_OWNER_LAST_NAME] [varchar](2000) NULL,
	[EPI_OWNER_COUNTY] [varchar](2000) NULL,
	[EPI_EPI_LINKED_CASE_ID3] [varchar](2000) NULL,
	[EPI_SPEC_OUT_OF_CNTRY_EXP] [varchar](2000) NULL,
	[EPI_SPEC_OUT_OF_STATE_EXP] [varchar](2000) NULL,
	[EPI_VET_PHONE] [varchar](2000) NULL,
	[EPI_CONTACT_24HR_NOTIF] [varchar](2000) NULL,
	[EPI_NEDSS_ID_ANIMAL] [varchar](2000) NULL,
	[EPI_OUTBREAK_NAME_TXT] [varchar](2000) NULL,
	[EPI_ContactOthSpecify] [varchar](2000) NULL,
	[EPI_CORRECTIONAL_NAME] [varchar](2000) NULL,
	[EPI_NO_EXPOS_TEST_REAS] [varchar](2000) NULL,
	[EPI_OTH_ANM_VAC_ST_RB_ATT] [varchar](2000) NULL,
	[EPI_ACO_PHONE] [varchar](2000) NULL,
	[EPI_OTH_REAS_PREV_VACC] [varchar](2000) NULL,
	[EPI_ANIMAL_TYPE_TXT] [varchar](2000) NULL,
	[EPI_OTH_HUMAN_EXP_TYPE] [varchar](2000) NULL,
	[EPI_OWNER_PHONE] [varchar](2000) NULL,
	[EPI_PEP_refused_other] [varchar](2000) NULL,
	[EPI_EPI_LINKED_CASE_ID2] [varchar](2000) NULL,
	[EPI_OTH_EXPOSURE_SPECIFY] [varchar](2000) NULL,
	[EPI_EPI_LINKED_TO_CASE_ID] [varchar](2000) NULL,
	[EPI_CITY_OF_EXP] [varchar](2000) NULL,
	[EPI_GRP_HME_NAME] [varchar](2000) NULL,
	[EPI_OWNER_FIRST_NAME] [varchar](2000) NULL,
	[EPI_OTH_ANIMAL_SPEC_ATTCK] [varchar](2000) NULL,
	[EPI_oth_med_clinic] [varchar](2000) NULL,
	[EPI_ACO_NAME_TOWN] [varchar](2000) NULL,
	[EPI_OTH_ANIMAL_VARIANT] [varchar](2000) NULL,
	[EPI_Other_not_test_rabies] [varchar](2000) NULL,
	[EPI_PEP_ADMIN_FAC] [varchar](2000) NULL,
	[EPI_NONHC_OTBRK_FACNAME] [varchar](2000) NULL,
	[EPI_ACLF_OUTBREAK_NAME_CO] [varchar](2000) NULL
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[D_INV_HIV]    Script Date: 3/18/2025 5:42:18 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[D_INV_HIV](
	[D_INV_HIV_KEY] [float] NULL,
	[nbs_case_answer_uid] [numeric](21, 0) NULL,
	[HIV_LAST_900_TEST_DT] [date] NULL,
	[HIV_900_TEST_REFERRAL_DT] [date] NULL,
	[HIV_900_RESULT] [varchar](1999) NULL,
	[HIV_900_TEST_IND] [varchar](1999) NULL,
	[HIV_AV_THERAPY_EVER_IND] [varchar](1999) NULL,
	[HIV_AV_THERAPY_LAST_12MO_IND] [varchar](1999) NULL,
	[HIV_CA_900_REASON_NOT_LOC] [varchar](1999) NULL,
	[HIV_EHARS_TRNSMSN_CTGRY] [varchar](1999) NULL,
	[HIV_ENROLL_PRTNR_SRVCS_IND] [varchar](1999) NULL,
	[HIV_HIV_STAT_INV_IN_EHARS] [varchar](1999) NULL,
	[HIV_HIV_STATUS_CD_MTH] [varchar](1999) NULL,
	[HIV_KEEP_900_CARE_APPT_IND] [varchar](1999) NULL,
	[HIV_POST_TEST_900_COUNSELING] [varchar](1999) NULL,
	[HIV_PREVIOUS_900_TEST_IND] [varchar](1999) NULL,
	[HIV_REFER_FOR_900_CARE_IND] [varchar](1999) NULL,
	[HIV_REFER_FOR_900_TEST] [varchar](1999) NULL,
	[HIV_RST_PROVIDED_900_RSLT_IND] [varchar](1999) NULL,
	[HIV_SELF_REPORTED_RSLT_900] [varchar](1999) NULL,
	[HIV_CA_900_OTH_RSN_NOT_LO] [varchar](2000) NULL,
	[HIV_STATE_CASE_ID] [varchar](2000) NULL
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[D_INV_LAB_FINDING]    Script Date: 3/18/2025 5:42:18 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[D_INV_LAB_FINDING](
	[D_INV_LAB_FINDING_KEY] [float] NULL,
	[nbs_case_answer_uid] [numeric](21, 0) NULL,
	[LAB_AST_Result] [varchar](2000) NULL,
	[LAB_D_DIMER_HIGH_VAL] [varchar](2000) NULL,
	[LAB_PLATELETS_HIGH_VAL] [varchar](2000) NULL,
	[LAB_PROTEIN_TEST_HIGH_VAL] [varchar](2000) NULL,
	[LAB_SERUM_WBC_HIGH_VAL] [varchar](2000) NULL,
	[LAB_CRP_HIGH_VALUE] [varchar](2000) NULL,
	[LAB_WBC_LOW_VAL] [varchar](2000) NULL,
	[LAB_PLATELETS_LOW_VAL] [varchar](2000) NULL,
	[LAB_FIBRINOGEN_HIGH_VAL] [varchar](2000) NULL,
	[LAB_LYMPHOCYTES_LOW_VAL] [varchar](2000) NULL,
	[LAB_BNP_HIGH_VAL] [varchar](2000) NULL,
	[LAB_TB_SKIN_TST_MM_INDUR] [varchar](2000) NULL,
	[LAB_URINE_WBC_LOW_VAL] [varchar](2000) NULL,
	[LAB_TestResultUpperLimit] [varchar](2000) NULL,
	[LAB_IL_6_HIGH_VAL] [varchar](2000) NULL,
	[LAB_SERUM_WBC_LOW_VAL] [varchar](2000) NULL,
	[LAB_WBC_HIGH_VAL] [varchar](2000) NULL,
	[LAB_TROPONIN_HIGH_VAL] [varchar](2000) NULL,
	[LAB_NEUTROPHILS_LOW_VAL] [varchar](2000) NULL,
	[LAB_TestResultUpperLimit2] [varchar](2000) NULL,
	[LAB_ALT_Result] [varchar](2000) NULL,
	[LAB_PROTEIN_TEST_LOW_VAL] [varchar](2000) NULL,
	[LAB_NT_PROBNP_HIGH_VAL] [varchar](2000) NULL,
	[LAB_GLUCOSE_LOW_VALUE] [varchar](2000) NULL,
	[LAB_LYMPHOCYTES_HIGH_VAL] [varchar](2000) NULL,
	[LAB_GLUCOSE_HIGH_VALUE] [varchar](2000) NULL,
	[LAB_BAND_TEST_LOW_VAL] [varchar](2000) NULL,
	[LAB_BAND_TEST_HIGH_VAL] [varchar](2000) NULL,
	[LAB_NEUTROPHILS_HIGH_VAL] [varchar](2000) NULL,
	[LAB_URINE_WBC_HIGH_VAL] [varchar](2000) NULL,
	[LAB_FERRITIN_HIGH_VAL] [varchar](2000) NULL,
	[LAB_TotalAntiHAVDate] [date] NULL,
	[LAB_TotalAntiHBcDate] [date] NULL,
	[LAB_IgMAntiHBcDate] [date] NULL,
	[LAB_HIV_SPECIMEN_COLL_DT] [date] NULL,
	[LAB_SMR_PATH_CYTO_RPT_DT] [date] NULL,
	[LAB_NAA_COLL_DT] [date] NULL,
	[LAB_NAA_RPT_DT] [date] NULL,
	[LAB_TB_SKIN_TST_READ_DT] [date] NULL,
	[LAB_SMR_PATH_CYTO_COLL_DT] [date] NULL,
	[LAB_DT_FRST_NEG_CULTURE] [date] NULL,
	[LAB_IGA_TEST_DT] [date] NULL,
	[LAB_CULTURE_OTH_COLL_DT] [date] NULL,
	[LAB_ANTIGEN_TEST_DT] [date] NULL,
	[LAB_VerifiedTestDate] [date] NULL,
	[LAB_IgMAntiHAVDate] [date] NULL,
	[LAB_HBsAg_Date] [date] NULL,
	[LAB_IGRA_COLL_DT] [date] NULL,
	[LAB_HIV_RPT_DT] [date] NULL,
	[LAB_HCVRNA_Date] [date] NULL,
	[LAB_SPUTUM_SMEAR_COLL_DT] [date] NULL,
	[LAB_IGG_TEST_DT] [date] NULL,
	[LAB_TestDate2] [date] NULL,
	[LAB_SPUTUM_CULT_COLL_DT] [date] NULL,
	[LAB_TotalAntiHDV_Date] [date] NULL,
	[LAB_TotalAntiHEV_Date] [date] NULL,
	[LAB_Supplem_antiHCV_Date] [date] NULL,
	[LAB_TB_SKIN_TST_PLACED_DT] [date] NULL,
	[LAB_RT_PCR_TEST_DT] [date] NULL,
	[LAB_FRST_POS_SPEC_CLCT_DT] [date] NULL,
	[LAB_TestDate] [date] NULL,
	[LAB_IGM_TEST_DT] [date] NULL,
	[LAB_NAATDATECOLLECTED] [date] NULL,
	[LAB_HCV_PEAK_TOTAL_DATE] [date] NULL,
	[LAB_HBeAg_Date] [date] NULL,
	[LAB_TotalAntiHCV_Date] [date] NULL,
	[LAB_SPUTUM_SMEAR_RPT_DT] [date] NULL,
	[LAB_CULTURE_OTH_RPT_DT] [date] NULL,
	[LAB_IGRA_RPT_DT] [date] NULL,
	[LAB_SPUTUM_CULT_RPT_DT] [date] NULL,
	[LAB_HBV_NAT_Date] [date] NULL,
	[FLU_A_RAPID_AG_RSLT] [varchar](1999) NULL,
	[LAB_ADENOVIRUS_RSLT] [varchar](1999) NULL,
	[LAB_ANTIGEN_TEST_RESULT] [varchar](1999) NULL,
	[LAB_ANTIGEN_TESTING_IND] [varchar](1999) NULL,
	[LAB_AntiHBsPositive] [varchar](1999) NULL,
	[LAB_AntiHBsTested] [varchar](1999) NULL,
	[LAB_BAND_TEST_UNIT] [varchar](1999) NULL,
	[LAB_BN_INTRP_RSLT] [varchar](1999) NULL,
	[LAB_BNP_TEST_UNIT] [varchar](1999) NULL,
	[LAB_C_PNEUMONIAE_RSLT] [varchar](1999) NULL,
	[LAB_CORONAVIRUS_RSLT] [varchar](1999) NULL,
	[LAB_CRP_INTRP_RSLT] [varchar](1999) NULL,
	[LAB_CRP_TEST_UNIT] [varchar](1999) NULL,
	[LAB_CSF_PLEOCYTOSIS] [varchar](1999) NULL,
	[LAB_CSF_PROTEIN_INTERP] [varchar](1999) NULL,
	[LAB_CSF_VDRL] [varchar](1999) NULL,
	[LAB_CSF_WBC_CNT_INTERP] [varchar](1999) NULL,
	[LAB_CUL_CONV_DOCUM] [varchar](1999) NULL,
	[LAB_CULTURE_OTH_SPM_SRC] [varchar](1999) NULL,
	[LAB_CULTURE_OTH_SPM_SRC_OTH] [varchar](1999) NULL,
	[LAB_CULTURE_OTHER_RSLT] [varchar](1999) NULL,
	[LAB_D_DIMER_INTRP_RSLT] [varchar](1999) NULL,
	[LAB_D_DIMER_UNIT] [varchar](1999) NULL,
	[LAB_DKFLD_DFA_SPEC_STAINS] [varchar](1999) NULL,
	[LAB_FERRITIN_INTRP_RSLT] [varchar](1999) NULL,
	[LAB_FERRITIN_TEST_UNIT] [varchar](1999) NULL,
	[LAB_FIBRINOGEN_INTRP_RSLT] [varchar](1999) NULL,
	[LAB_FIBRINOGEN_TEST_UNIT] [varchar](1999) NULL,
	[LAB_FLU_A_PCR_RSLT] [varchar](1999) NULL,
	[LAB_FLU_B_PCR_RSLT] [varchar](1999) NULL,
	[LAB_FLU_B_RAPID_AG_RSLT] [varchar](1999) NULL,
	[LAB_FLU_VIRUS_TYPE] [varchar](1999) NULL,
	[LAB_GLUCOSE_HIGH_UNIT] [varchar](1999) NULL,
	[LAB_H_METAPNEUMOVRS_RSLT] [varchar](1999) NULL,
	[LAB_HBeAg] [varchar](1999) NULL,
	[LAB_HBsAg] [varchar](1999) NULL,
	[LAB_HBV_NAT] [varchar](1999) NULL,
	[LAB_HCV_PEAK_TOTAL_BILI_E] [varchar](1999) NULL,
	[LAB_HCVRNA] [varchar](1999) NULL,
	[LAB_HepDTest] [varchar](1999) NULL,
	[LAB_IGA_TEST_RESULT] [varchar](1999) NULL,
	[LAB_IGG_TEST_RESULT] [varchar](1999) NULL,
	[LAB_IgM_AntiHAV] [varchar](1999) NULL,
	[LAB_IGM_TEST_RESULT] [varchar](1999) NULL,
	[LAB_IgMAntiHBc] [varchar](1999) NULL,
	[LAB_IGRA_QUAL_RSLT] [varchar](1999) NULL,
	[LAB_IGRA_QUANT_RSLT_UNIT] [varchar](1999) NULL,
	[LAB_IGRA_TEST_TYPE] [varchar](1999) NULL,
	[LAB_IL_6_INTRP_RSLT] [varchar](1999) NULL,
	[LAB_IL_6_UNIT] [varchar](1999) NULL,
	[LAB_ISO_SUBMTD_GENOTYPNG] [varchar](1999) NULL,
	[LAB_LAB_CNFM_CO_INFECTION] [varchar](1999) NULL,
	[LAB_LAB_TESTING_BY] [varchar](1999) NULL,
	[LAB_LYMPHOCYTES_UNIT] [varchar](1999) NULL,
	[LAB_M_PNEUMONIAE_RSLT] [varchar](1999) NULL,
	[LAB_MOLE_DRG_SUSC_COM] [varchar](1999) NULL,
	[LAB_NAA_RSLT] [varchar](1999) NULL,
	[LAB_NAA_SPM_SRC] [varchar](1999) NULL,
	[LAB_NAA_SPM_SRC_OTH] [varchar](1999) NULL,
	[LAB_NAATTESTRESULT] [varchar](1999) NULL,
	[LAB_NEUTROPHILS_UNIT] [varchar](1999) NULL,
	[LAB_NONTREP_SYPH_RSLT_QNT] [varchar](1999) NULL,
	[LAB_NONTREP_SYPH_RSLT_QUA] [varchar](1999) NULL,
	[LAB_NONTREP_SYPH_TEST_TYP] [varchar](1999) NULL,
	[LAB_NT_PROBNP_INTRP_RSLT] [varchar](1999) NULL,
	[LAB_NT_PROBNP_UNIT] [varchar](1999) NULL,
	[LAB_OTH_PATHOGEN_TEST_IND] [varchar](1999) NULL,
	[LAB_PARAINFLUENZA1_4_RSLT] [varchar](1999) NULL,
	[LAB_PHENO_DRG_SUSC_COM] [varchar](1999) NULL,
	[LAB_PLATELETS_UNIT] [varchar](1999) NULL,
	[LAB_PrevNegHepTest] [varchar](1999) NULL,
	[LAB_PROTEIN_TEST_UNIT] [varchar](1999) NULL,
	[LAB_RDN_NT_DOCUM_SCC] [varchar](1999) NULL,
	[LAB_RDN_NT_DOCUM_SCC_OTH] [varchar](1999) NULL,
	[LAB_RHINO_ENTERO_RSLT] [varchar](1999) NULL,
	[LAB_RSV_RSLT] [varchar](1999) NULL,
	[LAB_RT_PCR_TEST_RESULT] [varchar](1999) NULL,
	[LAB_RT_PCR_TESTING_IND] [varchar](1999) NULL,
	[LAB_SEROLOGY_TESTING_IND] [varchar](1999) NULL,
	[LAB_SERUM_PAIR_ANT_RESULT] [varchar](1999) NULL,
	[LAB_SERUM_WBC_UNIT] [varchar](1999) NULL,
	[LAB_SMR_PATH_CYTO_RSLT] [varchar](1999) NULL,
	[LAB_SMR_PATH_CYTO_SPM] [varchar](1999) NULL,
	[LAB_SMR_PATH_CYTO_SPM_OTH] [varchar](1999) NULL,
	[LAB_SMR_PATH_CYTO_TST_TYP] [varchar](1999) NULL,
	[LAB_SPEC_CLCTN_LOC_TYP] [varchar](1999) NULL,
	[LAB_SPEC_CLCTN_LOC_TYP_OTH] [varchar](1999) NULL,
	[LAB_SPEC_COLLECT_FAC_CNTY] [varchar](1999) NULL,
	[LAB_SPEC_COLLECT_FAC_CNTY_OTH] [varchar](1999) NULL,
	[LAB_SPEC_COLLECT_FAC_STT] [varchar](1999) NULL,
	[LAB_SPUTUM_CULT_RSLT] [varchar](1999) NULL,
	[LAB_SPUTUM_SMEAR_RSLT] [varchar](1999) NULL,
	[LAB_Supplem_antiHCV] [varchar](1999) NULL,
	[LAB_SYPHILIS_TST_PS_IND] [varchar](1999) NULL,
	[LAB_SYPHILIS_TST_RSLT_PS] [varchar](1999) NULL,
	[LAB_TB_SKIN_TST_RSLT] [varchar](1999) NULL,
	[LAB_TCHSTY_AT_SPEC_CLCTN] [varchar](1999) NULL,
	[LAB_TESTING_PERFORMED_IND] [varchar](1999) NULL,
	[LAB_TESTS_PERFORMED] [varchar](1999) NULL,
	[LAB_TotalAntiHAV] [varchar](1999) NULL,
	[LAB_TotalAntiHBc] [varchar](1999) NULL,
	[LAB_TotalAntiHCV] [varchar](1999) NULL,
	[LAB_TotalAntiHDV] [varchar](1999) NULL,
	[LAB_TotalAntiHEV] [varchar](1999) NULL,
	[LAB_TREP_SYPH_RESULT_QUAL] [varchar](1999) NULL,
	[LAB_TREP_SYPH_TEST_TYPE] [varchar](1999) NULL,
	[LAB_TROPONIN_INTRP_RSLT] [varchar](1999) NULL,
	[LAB_TROPONIN_TEST_UNIT] [varchar](1999) NULL,
	[LAB_URINE_WBC_UNIT] [varchar](1999) NULL,
	[LAB_VNTILTR_AT_SPEC_CLCTN] [varchar](1999) NULL,
	[LAB_WBC_UNIT] [varchar](1999) NULL,
	[LABDENGUE_SEROTYPE] [varchar](1999) NULL,
	[LAB_IGRA_QUANT_TEST_RSLT] [varchar](2000) NULL,
	[LAB_HETL_ID] [varchar](2000) NULL,
	[LAB_VPD_LAB_MSG_SPECIMEN_ID] [varchar](2000) NULL,
	[LAB_LAB_NAME] [varchar](2000) NULL,
	[LAB_SignalToCutoff] [varchar](2000) NULL
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[D_INV_MEDICAL_HISTORY]    Script Date: 3/18/2025 5:42:18 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[D_INV_MEDICAL_HISTORY](
	[D_INV_MEDICAL_HISTORY_KEY] [float] NULL,
	[nbs_case_answer_uid] [numeric](21, 0) NULL,
	[MDH_DiabetesDxDate] [date] NULL,
	[MDH_DueDate] [date] NULL,
	[MDH_PrevLTBIDxYr] [date] NULL,
	[MDH_PROVIDER_REASON_VISIT_DT] [date] NULL,
	[MDH_AUTOIMMUNE_DISEASE] [varchar](1999) NULL,
	[MDH_CHRONIC_LIVER_DIS_IND] [varchar](1999) NULL,
	[MDH_CHRONIC_LUNG_DIS_IND] [varchar](1999) NULL,
	[MDH_CHRONIC_RENAL_DIS_IND] [varchar](1999) NULL,
	[MDH_CONG_MALFORMATION_IND] [varchar](1999) NULL,
	[MDH_CV_DISEASE_IND] [varchar](1999) NULL,
	[MDH_Diabetes] [varchar](1999) NULL,
	[MDH_DIABETES_MELLITUS_IND] [varchar](1999) NULL,
	[MDH_IMMUNO_CONDITION_IND] [varchar](1999) NULL,
	[MDH_Jaundiced] [varchar](1999) NULL,
	[MDH_NEURO_DISABLITY_IND] [varchar](1999) NULL,
	[MDH_OBESITY_IND] [varchar](1999) NULL,
	[MDH_OTH_CHRONIC_DIS_IND] [varchar](1999) NULL,
	[MDH_PREEXISTING_COND_IND] [varchar](1999) NULL,
	[MDH_PREV_STD_HIST] [varchar](1999) NULL,
	[MDH_PrevAwareInfection] [varchar](1999) NULL,
	[MDH_PreviousLTBIDx] [varchar](1999) NULL,
	[MDH_PrevLTBITxStatus] [varchar](1999) NULL,
	[MDH_PrevTBDxTxStatus] [varchar](1999) NULL,
	[MDH_ProviderOfCare] [varchar](1999) NULL,
	[MDH_PSYCHIATRIC_CONDITION] [varchar](1999) NULL,
	[MDH_ReasonForTest] [varchar](1999) NULL,
	[MDH_SICKLE_CELL_DIS_IND] [varchar](1999) NULL,
	[MDH_SUBSTANCE_ABUSE] [varchar](1999) NULL,
	[MDH_Symptomatic] [varchar](1999) NULL,
	[MDH_TISSUE_ORGAN_TRNSPLNT] [varchar](1999) NULL,
	[MDH_TNF_ANTAGONIST_TX] [varchar](1999) NULL,
	[MDH_UNDERLYING_COND_OTH] [varchar](1999) NULL,
	[MDH_UNDERLYING_CONDITIONS] [varchar](1999) NULL,
	[MDH_VIRAL_HEP_B_C_INF] [varchar](1999) NULL,
	[MDH_MEDICAL_RECORD_NBR] [varchar](2000) NULL,
	[MDH_PSYCH_CONDITION_SPEC] [varchar](2000) NULL,
	[MDH_OTH_CHRONIC_DIS_TXT] [varchar](2000) NULL,
	[MDH_NEURO_DISABLITY_INFO] [varchar](2000) NULL,
	[MDH_PrevTBDxRVCT] [varchar](2000) NULL,
	[MDH_UNDRLYNG_COND_SPECIFY] [varchar](2000) NULL,
	[MDH_OTHER_UNDERLY_COND_FL] [varchar](2000) NULL,
	[MDH_ReasonForTestingOth] [varchar](2000) NULL,
	[MDH_CONG_MALFORMATION_OTH] [varchar](2000) NULL
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[D_INV_RISK_FACTOR]    Script Date: 3/18/2025 5:42:18 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[D_INV_RISK_FACTOR](
	[D_INV_RISK_FACTOR_KEY] [float] NULL,
	[nbs_case_answer_uid] [numeric](21, 0) NULL,
	[RSK_STDTxYr] [varchar](2000) NULL,
	[RSK_IncarcTimeMonths] [varchar](2000) NULL,
	[RSK_NumSexPrtners] [varchar](2000) NULL,
	[RSK_NUM_SEX_PARTNER_12MO] [varchar](2000) NULL,
	[RSK_IncarcYear6Mos] [varchar](2000) NULL,
	[RSK_BloodTransfusionDate] [date] NULL,
	[RSK_YrPrevTBDx] [date] NULL,
	[RSK_AddTBRiskFactors] [varchar](1999) NULL,
	[RSK_ADULT_CONG_LIVING_EXP] [varchar](1999) NULL,
	[RSK_ALCOHOLISM] [varchar](1999) NULL,
	[RSK_ANS_REFUSED_SEX_PARTNER] [varchar](1999) NULL,
	[RSK_ATTEND_EVENTS] [varchar](1999) NULL,
	[RSK_BEEN_INCARCERATD_12MO_IND] [varchar](1999) NULL,
	[RSK_BLOOD_DONOR] [varchar](1999) NULL,
	[RSK_BLOOD_TRANSFUSION] [varchar](1999) NULL,
	[RSK_BloodExpOther] [varchar](1999) NULL,
	[RSK_BloodTransfusion] [varchar](1999) NULL,
	[RSK_BloodWorkerCnctFreq] [varchar](1999) NULL,
	[RSK_BloodWorkerEver] [varchar](1999) NULL,
	[RSK_BloodWorkerOnset] [varchar](1999) NULL,
	[RSK_BREAST_FED_INFANT] [varchar](1999) NULL,
	[RSK_ClottingPrior87] [varchar](1999) NULL,
	[RSK_COCAINE_USE_12MO_IND] [varchar](1999) NULL,
	[RSK_CONGEST_HEART_FAIL] [varchar](1999) NULL,
	[RSK_ContaminatedStick] [varchar](1999) NULL,
	[RSK_CORRECTIONAL_EXP] [varchar](1999) NULL,
	[RSK_CRACK_USE_12MO_IND] [varchar](1999) NULL,
	[RSK_CustodyImmigratCustom] [varchar](1999) NULL,
	[RSK_DentalOralSx] [varchar](1999) NULL,
	[RSK_DIABETES] [varchar](1999) NULL,
	[RSK_ED_MEDS_USE_12MO_IND] [varchar](1999) NULL,
	[RSK_EDUCATIONAL_EXP] [varchar](1999) NULL,
	[RSK_ESRD] [varchar](1999) NULL,
	[RSK_ExcessAlcUsePastYr] [varchar](1999) NULL,
	[RSK_HEMODIALYSIS_BEFORE_ONSET] [varchar](1999) NULL,
	[RSK_HemodialysisLongTerm] [varchar](1999) NULL,
	[RSK_hep_b_ind] [varchar](1999) NULL,
	[RSK_hep_c_ind] [varchar](1999) NULL,
	[RSK_HEPATITISBORC] [varchar](1999) NULL,
	[RSK_HepContactEver] [varchar](1999) NULL,
	[RSK_HEROIN_USE_12MO_IND] [varchar](1999) NULL,
	[RSK_HIVStatus] [varchar](1999) NULL,
	[RSK_HOMELESSEVER] [varchar](1999) NULL,
	[RSK_HomelessWithinPastYr] [varchar](1999) NULL,
	[RSK_HospitalizedPrior] [varchar](1999) NULL,
	[RSK_HYPERTENSION] [varchar](1999) NULL,
	[RSK_IDU] [varchar](1999) NULL,
	[RSK_ImmigrationStatus] [varchar](1999) NULL,
	[RSK_IMMUNE_SUPPRESS_MED] [varchar](1999) NULL,
	[RSK_IMMUNOCOMPROMISENOT] [varchar](1999) NULL,
	[RSK_Incarcerated24Hrs] [varchar](1999) NULL,
	[RSK_Incarcerated6months] [varchar](1999) NULL,
	[RSK_IncarceratedEver] [varchar](1999) NULL,
	[RSK_IncarceratedJail] [varchar](1999) NULL,
	[RSK_IncarcerationPrison] [varchar](1999) NULL,
	[RSK_IncarcJuvenileFacilit] [varchar](1999) NULL,
	[RSK_INJ_DRUG_USE_12MO_IND] [varchar](1999) NULL,
	[RSK_InjectDrugUsePastYr] [varchar](1999) NULL,
	[RSK_IVInjectInfuseOutpt] [varchar](1999) NULL,
	[RSK_JAILEVER] [varchar](1999) NULL,
	[RSK_KIDNEY_DISEASE] [varchar](1999) NULL,
	[RSK_LongTermCareRes] [varchar](1999) NULL,
	[RSK_METH_USE_12MO_IND] [varchar](1999) NULL,
	[RSK_NITR_POP_USE_12MO_IND] [varchar](1999) NULL,
	[RSK_NO_DRUG_USE_12MO_IND] [varchar](1999) NULL,
	[RSK_NonInjectDrugPastYr] [varchar](1999) NULL,
	[RSK_ORGAN_DONOR] [varchar](1999) NULL,
	[RSK_ORGAN_TRANSPLANT] [varchar](1999) NULL,
	[RSK_OTHER_DRUG_USE_12MO_IND] [varchar](1999) NULL,
	[RSK_PATIENT_HOMELESS] [varchar](1999) NULL,
	[RSK_Piercing] [varchar](1999) NULL,
	[RSK_PiercingRcvdFrom] [varchar](1999) NULL,
	[RSK_PrevDxTB] [varchar](1999) NULL,
	[RSK_PrimaryOccupationRisk] [varchar](1999) NULL,
	[RSK_PSWrkrBldCnctFreq] [varchar](1999) NULL,
	[RSK_PublicSafetyWorker] [varchar](1999) NULL,
	[RSK_ResCorrectFacility] [varchar](1999) NULL,
	[RSK_ResLongTermCareFacil] [varchar](1999) NULL,
	[RSK_RISK_FACTORS_ASSESS_IND] [varchar](1999) NULL,
	[RSK_SEX_EXCH_DRGS_MNY_12MO_IND] [varchar](1999) NULL,
	[RSK_SEX_INTOXCTED_HGH_12MO_IND] [varchar](1999) NULL,
	[RSK_SEX_W_ANON_PTRNR_12MO_IND] [varchar](1999) NULL,
	[RSK_SEX_W_FEMALE_12MO_IND] [varchar](1999) NULL,
	[RSK_SEX_W_KNOWN_IDU_12MO_IND] [varchar](1999) NULL,
	[RSK_SEX_W_KNWN_MSM_12M_FML_IND] [varchar](1999) NULL,
	[RSK_SEX_W_MALE_12MO_IND] [varchar](1999) NULL,
	[RSK_SEX_W_TRANSGNDR_12MO_IND] [varchar](1999) NULL,
	[RSK_SEX_WOUT_CONDOM_12MO_IND] [varchar](1999) NULL,
	[RSK_SHARED_INJ_EQUIP_12MO_IND] [varchar](1999) NULL,
	[RSK_SMOKING_STATUS] [varchar](1999) NULL,
	[RSK_SMOKINGSTATUS] [varchar](1999) NULL,
	[RSK_STDTxEver] [varchar](1999) NULL,
	[RSK_STEROIDS] [varchar](1999) NULL,
	[RSK_STROKE] [varchar](1999) NULL,
	[RSK_SurgeryOther] [varchar](1999) NULL,
	[RSK_TARGET_POPULATIONS] [varchar](1999) NULL,
	[RSK_Tattoo] [varchar](1999) NULL,
	[RSK_TattooLocation] [varchar](1999) NULL,
	[RSK_TBDIABETIC] [varchar](1999) NULL,
	[RSK_TBOCCUPATIONCATEGOR] [varchar](1999) NULL,
	[RSK_TNFTHERAPY] [varchar](1999) NULL,
	[RSK_TransfusionPrior92] [varchar](1999) NULL,
	[RSK_TransplantPrior92] [varchar](1999) NULL,
	[RSK_TypeLongTermCareFacil] [varchar](1999) NULL,
	[RSK_TypeOfCorrectFacility] [varchar](1999) NULL,
	[RSK_UNK_SEX_PARTNERS] [varchar](1999) NULL,
	[RSK_veteran_status] [varchar](1999) NULL,
	[RSK_WKPLC_CRITICAL_INFRA] [varchar](1999) NULL,
	[RSK_WORKPLACE_EXP] [varchar](1999) NULL,
	[RSK_StateHIVAIDSPtNum] [varchar](2000) NULL,
	[RSK_OTHER_DRUG_SPEC] [varchar](2000) NULL,
	[RSK_TBOCCUPATIONINDUSTR] [varchar](2000) NULL,
	[RSK_PiercingOthLocSpec] [varchar](2000) NULL,
	[RSK_OtherBldExpSpec] [varchar](2000) NULL,
	[RSK_WKPLC_SETTING] [varchar](2000) NULL,
	[RSK_CityCntyAIDSPTNum] [varchar](2000) NULL,
	[RSK_OtherTBRiskFactors] [varchar](2000) NULL,
	[RSK_TattooLocOthSpec] [varchar](2000) NULL,
	[RSK_TBCURRENTOCCUPATION] [varchar](2000) NULL,
	[RSK_TBEVALUATIONOTHER] [varchar](2000) NULL
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[D_INV_TREATMENT]    Script Date: 3/18/2025 5:42:18 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[D_INV_TREATMENT](
	[D_INV_TREATMENT_KEY] [float] NULL,
	[nbs_case_answer_uid] [numeric](21, 0) NULL,
	[TRT_NumDOTWks] [varchar](2000) NULL,
	[TRT_NumMonthsTherapy] [varchar](2000) NULL,
	[TRT_NumTherapyDosesRcvd] [varchar](2000) NULL,
	[TRT_THERAPY_STOP_DT] [date] NULL,
	[TRT_DateTherapyStart] [date] NULL,
	[TRT_MEDICATION_START_DATE] [date] NULL,
	[TRT_TREATMENT_END_DT] [date] NULL,
	[TRT_interjuris_frm_dt] [date] NULL,
	[TRT_MDR_TRT_START_DT] [date] NULL,
	[TRT_INJCTBLE_MED_STOP_DT] [date] NULL,
	[TRT_FRST_BENZ_PEN_DOSE_DT] [date] NULL,
	[TRT_DateTherapyStop] [date] NULL,
	[TRT_SURGERY_TRT_MDR_TB_DT] [date] NULL,
	[TRT_TREATMENT_START_DT] [date] NULL,
	[TRT_TREATMENT_DATE] [date] NULL,
	[CLN_RSLT_CHEST_STDY] [varchar](1999) NULL,
	[CLN_RSLT_CHEST_STDY_OTH] [varchar](1999) NULL,
	[TRT_ADVERSEEVENTLTBI] [varchar](1999) NULL,
	[TRT_AMIKACIN_IND] [varchar](1999) NULL,
	[TRT_ANTICOAGULATION_IND] [varchar](1999) NULL,
	[TRT_ANTIPLATELETS_IND] [varchar](1999) NULL,
	[TRT_APPROPRIATETREATMENT] [varchar](1999) NULL,
	[TRT_BEDAQUILINE_IND] [varchar](1999) NULL,
	[TRT_CAPREOMYCIN_IND] [varchar](1999) NULL,
	[TRT_CIPROFLOXACIN_IND] [varchar](1999) NULL,
	[TRT_CLOFAZIMINE_IND] [varchar](1999) NULL,
	[TRT_CYCLOSERINE_IND] [varchar](1999) NULL,
	[TRT_DELAMANID_IND] [varchar](1999) NULL,
	[TRT_DISEASE_SITE] [varchar](1999) NULL,
	[TRT_DISEASE_SITE_OTH] [varchar](1999) NULL,
	[TRT_DOT] [varchar](1999) NULL,
	[TRT_ETHAMBUTOL_IND] [varchar](1999) NULL,
	[TRT_ETHIONAMIDE_IND] [varchar](1999) NULL,
	[TRT_FIRST_IVIG_IND] [varchar](1999) NULL,
	[TRT_FRST_BENZPEN_DOSE_TRI] [varchar](1999) NULL,
	[TRT_HealthcareProvTyp] [varchar](1999) NULL,
	[TRT_HEMODIALYSIS_IND] [varchar](1999) NULL,
	[TRT_HI_FLW_NASAL_CANN_IND] [varchar](1999) NULL,
	[TRT_HISTORY_TRT] [varchar](1999) NULL,
	[TRT_interjuris_frm_ind] [varchar](1999) NULL,
	[TRT_INTUBATION_IND] [varchar](1999) NULL,
	[TRT_ISONIAZID_IND] [varchar](1999) NULL,
	[TRT_KANAMYCIN_IND] [varchar](1999) NULL,
	[TRT_LEVOFLOXACIN_IND] [varchar](1999) NULL,
	[TRT_LINEZOLID_IND] [varchar](1999) NULL,
	[TRT_LO_FLW_NASAL_CANN_IND] [varchar](1999) NULL,
	[TRT_MALARIA_INFO] [varchar](1999) NULL,
	[TRT_MOTHER_TREATMENT] [varchar](1999) NULL,
	[TRT_MOTHER_TRT_30D_PRIOR] [varchar](1999) NULL,
	[TRT_MovedToWhere] [varchar](1999) NULL,
	[TRT_MOXIFLOXACIN_IND] [varchar](1999) NULL,
	[TRT_NON_INVASIVE_VENT_IND] [varchar](1999) NULL,
	[TRT_OFLOXACIN_IND] [varchar](1999) NULL,
	[TRT_OTH_DRUG_REGIMEN_IND] [varchar](1999) NULL,
	[TRT_OTHER_QUINOLONES_IND] [varchar](1999) NULL,
	[TRT_OutOfCountryMove] [varchar](1999) NULL,
	[TRT_OutOfStateMove] [varchar](1999) NULL,
	[TRT_PARA_AMNSLCYCACD_IND] [varchar](1999) NULL,
	[TRT_PRETOMANID_IND] [varchar](1999) NULL,
	[TRT_PtMoveDuringTherapy] [varchar](1999) NULL,
	[TRT_PYRAZINAMIDE_IND] [varchar](1999) NULL,
	[TRT_RDN_THERAPY_EXT] [varchar](1999) NULL,
	[TRT_RDN_THERAPY_EXT_OTH] [varchar](1999) NULL,
	[TRT_REASONLTBITXNOTST] [varchar](1999) NULL,
	[TRT_REASONLTBITXNOTST_OTH] [varchar](1999) NULL,
	[TRT_REASONLTBITXSTOP] [varchar](1999) NULL,
	[TRT_REASONLTBITXSTOP_OTH] [varchar](1999) NULL,
	[TRT_ReasonTBThrpyStopped] [varchar](1999) NULL,
	[TRT_ReasonTBThrpyStopped_OTH] [varchar](1999) NULL,
	[TRT_RIFABUTIN_IND] [varchar](1999) NULL,
	[TRT_RIFAMPIN_IND] [varchar](1999) NULL,
	[TRT_RIFAPENTINE_IND] [varchar](1999) NULL,
	[TRT_RSN_NT_TRT_RIPE] [varchar](1999) NULL,
	[TRT_RSN_NT_TRT_RIPE_OTH] [varchar](1999) NULL,
	[TRT_RSN_THERAPY_STOP] [varchar](1999) NULL,
	[TRT_RSN_THERAPY_STOP_OTH] [varchar](1999) NULL,
	[TRT_SECOND_IVIG_IND] [varchar](1999) NULL,
	[TRT_SEROLOGIC_RESPONSE] [varchar](1999) NULL,
	[TRT_STREPTOMYCIN_IND] [varchar](1999) NULL,
	[TRT_TBDRUGREGIMEN] [varchar](1999) NULL,
	[TRT_TBDRUGREGIMEN_OTH] [varchar](1999) NULL,
	[TRT_TransnationalReferral] [varchar](1999) NULL,
	[TRT_treat_start_ind] [varchar](1999) NULL,
	[TRT_TREATMENT_ADMIN_TYPE] [varchar](1999) NULL,
	[TRT_TREATMENT_IND] [varchar](1999) NULL,
	[TRT_TREATMENT_STARTED] [varchar](1999) NULL,
	[TRT_TXADMINISTRATIONTYPES] [varchar](1999) NULL,
	[TRT_VASOACTIVE_MED_IND] [varchar](1999) NULL,
	[TRT_OTH_DRG_REGIMEN_SPEC] [varchar](2000) NULL,
	[TRT_InStateMoveCountyTxt] [varchar](2000) NULL,
	[TRT_IMMUNE_MODULATORS_TXT] [varchar](2000) NULL,
	[TRT_OTHERLTBITREATMENT] [varchar](2000) NULL,
	[TRT_ANTICOAGULATION_TXT] [varchar](2000) NULL,
	[TRT_InStateMoveCityTxt] [varchar](2000) NULL,
	[TRT_ANTIPLATELETS_TXT] [varchar](2000) NULL,
	[TRT_MTHR_OTHER_TREATMENT] [varchar](2000) NULL,
	[EPI_EPI_PEP_PROVOKED] [varchar](2000) NULL,
	[TRT_OTHER_TREATMENT_SPEC] [varchar](2000) NULL,
	[TRT_VASOACTIVE_MED_TXT] [varchar](2000) NULL
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[D_INV_VACCINATION]    Script Date: 3/18/2025 5:42:18 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[D_INV_VACCINATION](
	[D_INV_VACCINATION_KEY] [float] NULL,
	[nbs_case_answer_uid] [numeric](21, 0) NULL,
	[VAC_VaccineDoses] [varchar](2000) NULL,
	[VAC_YearofLastDose] [varchar](2000) NULL,
	[VAC_VaccinationDate] [date] NULL,
	[VAC_LastIGDose] [date] NULL,
	[VAC_ImmuneGlobulin] [varchar](1999) NULL,
	[VAC_Vacc_Rcvd] [varchar](1999) NULL
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[D_ORGANIZATION]    Script Date: 3/18/2025 5:42:18 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[D_ORGANIZATION](
	[ORGANIZATION_KEY] [bigint] NOT NULL,
	[ORGANIZATION_UID] [bigint] NULL,
	[ORGANIZATION_LOCAL_ID] [varchar](50) NULL,
	[ORGANIZATION_RECORD_STATUS] [varchar](50) NULL,
	[ORGANIZATION_NAME] [varchar](50) NULL,
	[ORGANIZATION_GENERAL_COMMENTS] [varchar](2000) NULL,
	[ORGANIZATION_QUICK_CODE] [varchar](50) NULL,
	[ORGANIZATION_STAND_IND_CLASS] [varchar](50) NULL,
	[ORGANIZATION_FACILITY_ID] [varchar](50) NULL,
	[ORGANIZATION_FACILITY_ID_AUTH] [varchar](199) NULL,
	[ORGANIZATION_STREET_ADDRESS_1] [varchar](50) NULL,
	[ORGANIZATION_STREET_ADDRESS_2] [varchar](50) NULL,
	[ORGANIZATION_CITY] [varchar](50) NULL,
	[ORGANIZATION_STATE] [varchar](50) NULL,
	[ORGANIZATION_STATE_CODE] [varchar](50) NULL,
	[ORGANIZATION_ZIP] [varchar](10) NULL,
	[ORGANIZATION_COUNTY] [varchar](50) NULL,
	[ORGANIZATION_COUNTY_CODE] [varchar](50) NULL,
	[ORGANIZATION_COUNTRY] [varchar](50) NULL,
	[ORGANIZATION_ADDRESS_COMMENTS] [varchar](2000) NULL,
	[ORGANIZATION_PHONE_WORK] [varchar](50) NULL,
	[ORGANIZATION_PHONE_EXT_WORK] [varchar](50) NULL,
	[ORGANIZATION_EMAIL] [varchar](50) NULL,
	[ORGANIZATION_PHONE_COMMENTS] [varchar](2000) NULL,
	[ORGANIZATION_ENTRY_METHOD] [varchar](50) NULL,
	[ORGANIZATION_LAST_CHANGE_TIME] [datetime] NULL,
	[ORGANIZATION_ADD_TIME] [datetime] NULL,
	[ORGANIZATION_ADDED_BY] [varchar](50) NULL,
	[ORGANIZATION_LAST_UPDATED_BY] [varchar](50) NULL,
	[ORGANIZATION_FAX] [varchar](50) NULL,
 CONSTRAINT [PK_D_ORGANIZATION] PRIMARY KEY CLUSTERED 
(
	[ORGANIZATION_KEY] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[D_PATIENT]    Script Date: 3/18/2025 5:42:18 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[D_PATIENT](
	[PATIENT_KEY] [bigint] NOT NULL,
	[PATIENT_MPR_UID] [bigint] NULL,
	[PATIENT_RECORD_STATUS] [varchar](50) NULL,
	[PATIENT_LOCAL_ID] [varchar](50) NULL,
	[PATIENT_GENERAL_COMMENTS] [varchar](2000) NULL,
	[PATIENT_FIRST_NAME] [varchar](50) NULL,
	[PATIENT_MIDDLE_NAME] [varchar](50) NULL,
	[PATIENT_LAST_NAME] [varchar](50) NULL,
	[PATIENT_NAME_SUFFIX] [varchar](50) NULL,
	[PATIENT_ALIAS_NICKNAME] [varchar](50) NULL,
	[PATIENT_STREET_ADDRESS_1] [varchar](50) NULL,
	[PATIENT_STREET_ADDRESS_2] [varchar](50) NULL,
	[PATIENT_CITY] [varchar](50) NULL,
	[PATIENT_STATE] [varchar](50) NULL,
	[PATIENT_STATE_CODE] [varchar](50) NULL,
	[PATIENT_ZIP] [varchar](50) NULL,
	[PATIENT_COUNTY] [varchar](50) NULL,
	[PATIENT_COUNTY_CODE] [varchar](50) NULL,
	[PATIENT_COUNTRY] [varchar](50) NULL,
	[PATIENT_WITHIN_CITY_LIMITS] [varchar](10) NULL,
	[PATIENT_PHONE_HOME] [varchar](50) NULL,
	[PATIENT_PHONE_EXT_HOME] [varchar](50) NULL,
	[PATIENT_PHONE_WORK] [varchar](50) NULL,
	[PATIENT_PHONE_EXT_WORK] [varchar](50) NULL,
	[PATIENT_PHONE_CELL] [varchar](50) NULL,
	[PATIENT_EMAIL] [varchar](100) NULL,
	[PATIENT_DOB] [datetime] NULL,
	[PATIENT_AGE_REPORTED] [numeric](18, 0) NULL,
	[PATIENT_AGE_REPORTED_UNIT] [varchar](20) NULL,
	[PATIENT_BIRTH_SEX] [varchar](50) NULL,
	[PATIENT_CURRENT_SEX] [varchar](50) NULL,
	[PATIENT_DECEASED_INDICATOR] [varchar](50) NULL,
	[PATIENT_DECEASED_DATE] [datetime] NULL,
	[PATIENT_MARITAL_STATUS] [varchar](50) NULL,
	[PATIENT_SSN] [varchar](50) NULL,
	[PATIENT_ETHNICITY] [varchar](50) NULL,
	[PATIENT_RACE_CALCULATED] [varchar](50) NULL,
	[PATIENT_RACE_CALC_DETAILS] [varchar](4000) NULL,
	[PATIENT_RACE_AMER_IND_1] [varchar](50) NULL,
	[PATIENT_RACE_AMER_IND_2] [varchar](50) NULL,
	[PATIENT_RACE_AMER_IND_3] [varchar](50) NULL,
	[PATIENT_RACE_AMER_IND_GT3_IND] [varchar](50) NULL,
	[PATIENT_RACE_AMER_IND_ALL] [varchar](2000) NULL,
	[PATIENT_RACE_ASIAN_1] [varchar](50) NULL,
	[PATIENT_RACE_ASIAN_2] [varchar](50) NULL,
	[PATIENT_RACE_ASIAN_3] [varchar](50) NULL,
	[PATIENT_RACE_ASIAN_GT3_IND] [varchar](50) NULL,
	[PATIENT_RACE_ASIAN_ALL] [varchar](2000) NULL,
	[PATIENT_RACE_BLACK_1] [varchar](50) NULL,
	[PATIENT_RACE_BLACK_2] [varchar](50) NULL,
	[PATIENT_RACE_BLACK_3] [varchar](50) NULL,
	[PATIENT_RACE_BLACK_GT3_IND] [varchar](50) NULL,
	[PATIENT_RACE_BLACK_ALL] [varchar](2000) NULL,
	[PATIENT_RACE_NAT_HI_1] [varchar](50) NULL,
	[PATIENT_RACE_NAT_HI_2] [varchar](50) NULL,
	[PATIENT_RACE_NAT_HI_3] [varchar](50) NULL,
	[PATIENT_RACE_NAT_HI_GT3_IND] [varchar](50) NULL,
	[PATIENT_RACE_NAT_HI_ALL] [varchar](2000) NULL,
	[PATIENT_RACE_WHITE_1] [varchar](50) NULL,
	[PATIENT_RACE_WHITE_2] [varchar](50) NULL,
	[PATIENT_RACE_WHITE_3] [varchar](50) NULL,
	[PATIENT_RACE_WHITE_GT3_IND] [varchar](50) NULL,
	[PATIENT_RACE_WHITE_ALL] [varchar](2000) NULL,
	[PATIENT_NUMBER] [varchar](50) NULL,
	[PATIENT_NUMBER_AUTH] [varchar](199) NULL,
	[PATIENT_ENTRY_METHOD] [varchar](50) NULL,
	[PATIENT_LAST_CHANGE_TIME] [datetime] NULL,
	[PATIENT_UID] [bigint] NULL,
	[PATIENT_ADD_TIME] [datetime] NULL,
	[PATIENT_ADDED_BY] [varchar](50) NULL,
	[PATIENT_LAST_UPDATED_BY] [varchar](50) NULL,
	[PATIENT_SPEAKS_ENGLISH] [varchar](100) NULL,
	[PATIENT_UNK_ETHNIC_RSN] [varchar](100) NULL,
	[PATIENT_CURR_SEX_UNK_RSN] [varchar](100) NULL,
	[PATIENT_PREFERRED_GENDER] [varchar](100) NULL,
	[PATIENT_ADDL_GENDER_INFO] [varchar](100) NULL,
	[PATIENT_CENSUS_TRACT] [varchar](100) NULL,
	[PATIENT_RACE_ALL] [varchar](4000) NULL,
	[PATIENT_BIRTH_COUNTRY] [varchar](50) NULL,
	[PATIENT_PRIMARY_OCCUPATION] [varchar](50) NULL,
	[PATIENT_PRIMARY_LANGUAGE] [varchar](50) NULL,
 CONSTRAINT [PK_D_PATIENT] PRIMARY KEY CLUSTERED 
(
	[PATIENT_KEY] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[D_PCR_SOURCE]    Script Date: 3/18/2025 5:42:18 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[D_PCR_SOURCE](
	[VAR_PAM_UID] [bigint] NOT NULL,
	[D_PCR_SOURCE_KEY] [bigint] NOT NULL,
	[SEQ_NBR] [int] NULL,
	[D_PCR_SOURCE_GROUP_KEY] [bigint] NOT NULL,
	[LAST_CHG_TIME] [datetime] NULL,
	[VALUE] [varchar](250) NULL,
 CONSTRAINT [PK_D_PCR_SOURCE] PRIMARY KEY CLUSTERED 
(
	[D_PCR_SOURCE_KEY] ASC,
	[VAR_PAM_UID] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[D_PCR_SOURCE_GROUP]    Script Date: 3/18/2025 5:42:18 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[D_PCR_SOURCE_GROUP](
	[D_PCR_SOURCE_GROUP_KEY] [bigint] NOT NULL,
 CONSTRAINT [PK_D_PCR_SOURCE_GROUP] PRIMARY KEY CLUSTERED 
(
	[D_PCR_SOURCE_GROUP_KEY] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[D_PROVIDER]    Script Date: 3/18/2025 5:42:18 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[D_PROVIDER](
	[PROVIDER_UID] [bigint] NULL,
	[PROVIDER_KEY] [bigint] NOT NULL,
	[PROVIDER_LOCAL_ID] [varchar](50) NULL,
	[PROVIDER_RECORD_STATUS] [varchar](50) NULL,
	[PROVIDER_NAME_PREFIX] [varchar](50) NULL,
	[PROVIDER_FIRST_NAME] [varchar](50) NULL,
	[PROVIDER_MIDDLE_NAME] [varchar](50) NULL,
	[PROVIDER_LAST_NAME] [varchar](50) NULL,
	[PROVIDER_NAME_SUFFIX] [varchar](50) NULL,
	[PROVIDER_NAME_DEGREE] [varchar](50) NULL,
	[PROVIDER_GENERAL_COMMENTS] [varchar](2000) NULL,
	[PROVIDER_QUICK_CODE] [varchar](50) NULL,
	[PROVIDER_REGISTRATION_NUM] [varchar](50) NULL,
	[PROVIDER_REGISRATION_NUM_AUTH] [varchar](199) NULL,
	[PROVIDER_STREET_ADDRESS_1] [varchar](50) NULL,
	[PROVIDER_STREET_ADDRESS_2] [varchar](50) NULL,
	[PROVIDER_CITY] [varchar](50) NULL,
	[PROVIDER_STATE] [varchar](50) NULL,
	[PROVIDER_STATE_CODE] [varchar](50) NULL,
	[PROVIDER_ZIP] [varchar](50) NULL,
	[PROVIDER_COUNTY] [varchar](50) NULL,
	[PROVIDER_COUNTY_CODE] [varchar](50) NULL,
	[PROVIDER_COUNTRY] [varchar](50) NULL,
	[PROVIDER_ADDRESS_COMMENTS] [varchar](2000) NULL,
	[PROVIDER_PHONE_WORK] [varchar](50) NULL,
	[PROVIDER_PHONE_EXT_WORK] [varchar](50) NULL,
	[PROVIDER_EMAIL_WORK] [varchar](50) NULL,
	[PROVIDER_PHONE_COMMENTS] [varchar](2000) NULL,
	[PROVIDER_PHONE_CELL] [varchar](50) NULL,
	[PROVIDER_ENTRY_METHOD] [varchar](50) NULL,
	[PROVIDER_LAST_CHANGE_TIME] [datetime] NULL,
	[PROVIDER_ADD_TIME] [datetime] NULL,
	[PROVIDER_ADDED_BY] [varchar](50) NULL,
	[PROVIDER_LAST_UPDATED_BY] [varchar](50) NULL,
 CONSTRAINT [PK_D_PROVIDER] PRIMARY KEY CLUSTERED 
(
	[PROVIDER_KEY] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[D_RASH_LOC_GEN]    Script Date: 3/18/2025 5:42:18 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[D_RASH_LOC_GEN](
	[VAR_PAM_UID] [bigint] NOT NULL,
	[D_RASH_LOC_GEN_KEY] [bigint] NOT NULL,
	[SEQ_NBR] [int] NULL,
	[D_RASH_LOC_GEN_GROUP_KEY] [bigint] NOT NULL,
	[LAST_CHG_TIME] [datetime] NULL,
	[VALUE] [varchar](250) NULL,
 CONSTRAINT [PK_D_RASH_LOC_GEN] PRIMARY KEY CLUSTERED 
(
	[D_RASH_LOC_GEN_KEY] ASC,
	[VAR_PAM_UID] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[D_RASH_LOC_GEN_GROUP]    Script Date: 3/18/2025 5:42:18 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[D_RASH_LOC_GEN_GROUP](
	[D_RASH_LOC_GEN_GROUP_KEY] [bigint] NOT NULL,
 CONSTRAINT [PK_D_RASH_LOC_GEN_GROUP] PRIMARY KEY CLUSTERED 
(
	[D_RASH_LOC_GEN_GROUP_KEY] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[D_VAR_PAM]    Script Date: 3/18/2025 5:42:18 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[D_VAR_PAM](
	[D_VAR_PAM_KEY] [bigint] NOT NULL,
	[VAR_PAM_UID] [bigint] NOT NULL,
	[VACCINE_TYPE_2] [varchar](50) NULL,
	[VACCINE_MANUFACTURER_1] [varchar](50) NULL,
	[EPI_LINKED] [varchar](50) NULL,
	[PCR_TEST] [varchar](50) NULL,
	[VARICELLA_NO_VACCINE_REASON] [varchar](50) NULL,
	[SEROLOGY_TEST] [varchar](50) NULL,
	[DFA_TEST_RESULT] [varchar](50) NULL,
	[HEALTHCARE_WORKER] [varchar](50) NULL,
	[IGG_TEST_WHOLE_CELL_MFGR] [varchar](50) NULL,
	[PREVIOUS_DIAGNOSIS_AGE_UNIT] [varchar](50) NULL,
	[PREGNANT_TRIMESTER] [varchar](50) NULL,
	[PREVIOUS_DIAGNOSIS] [varchar](50) NULL,
	[LAB_TESTING_OTHER_SPECIFY] [varchar](50) NULL,
	[IGM_TEST_TYPE] [varchar](50) NULL,
	[FEVER_TEMPERATURE_UNIT] [varchar](50) NULL,
	[IGG_TEST] [varchar](50) NULL,
	[MEDICATION_NAME] [varchar](50) NULL,
	[VACCINE_MANUFACTURER_3] [varchar](50) NULL,
	[COMPLICATIONS_PNEUMONIA] [varchar](50) NULL,
	[DFA_TEST] [varchar](50) NULL,
	[VESICLES] [varchar](50) NULL,
	[VARICELLA_NO_2NDVACCINE_REASON] [varchar](50) NULL,
	[IGM_TEST] [varchar](50) NULL,
	[VACCINE_TYPE_4] [varchar](50) NULL,
	[COMPLICATIONS_CEREB_ATAXIA] [varchar](50) NULL,
	[FEVER] [varchar](50) NULL,
	[STRAIN_TYPE] [varchar](50) NULL,
	[LAB_TESTING_OTHER_RESULT] [varchar](50) NULL,
	[CROPS_WAVES] [varchar](50) NULL,
	[TREATED] [varchar](50) NULL,
	[RASH_LOCATION] [varchar](50) NULL,
	[PATIENT_VISIT_HC_PROVIDER] [varchar](50) NULL,
	[RASH_CRUST] [varchar](50) NULL,
	[TRANSMISSION_SETTING] [varchar](50) NULL,
	[CULTURE_TEST_RESULT] [varchar](50) NULL,
	[HEMORRHAGIC] [varchar](50) NULL,
	[CULTURE_TEST] [varchar](50) NULL,
	[VACCINE_TYPE_1] [varchar](50) NULL,
	[PCR_TEST_RESULT] [varchar](50) NULL,
	[LAB_TESTING_OTHER] [varchar](50) NULL,
	[COMPLICATIONS] [varchar](50) NULL,
	[COMPLICATIONS_PNEU_DIAG_BY] [varchar](50) NULL,
	[VACCINE_TYPE_5] [varchar](50) NULL,
	[IGG_TEST_TYPE] [varchar](50) NULL,
	[EPI_LINKED_CASE_TYPE] [varchar](50) NULL,
	[VACCINE_MANUFACTURER_4] [varchar](50) NULL,
	[MACULAR_PAPULAR] [varchar](50) NULL,
	[VACCINE_TYPE_3] [varchar](50) NULL,
	[PREVIOUS_DIAGNOSIS_BY] [varchar](50) NULL,
	[VESICULAR] [varchar](50) NULL,
	[VACCINE_MANUFACTURER_5] [varchar](50) NULL,
	[DEATH_VARICELLA] [varchar](50) NULL,
	[IGG_TEST_GP_ELISA_MFGR] [varchar](50) NULL,
	[VARICELLA_VACCINE] [varchar](50) NULL,
	[IGM_TEST_RESULT] [varchar](50) NULL,
	[IMMUNOCOMPROMISED] [varchar](50) NULL,
	[COMPLICATIONS_OTHER] [varchar](50) NULL,
	[ITCHY] [varchar](50) NULL,
	[PATIENT_BIRTH_COUNTRY] [varchar](50) NULL,
	[DEATH_AUTOPSY] [varchar](50) NULL,
	[COMPLICATIONS_ENCEPHALITIS] [varchar](50) NULL,
	[COMPLICATIONS_HEMORRHAGIC] [varchar](50) NULL,
	[COMPLICATIONS_SKIN_INFECTION] [varchar](50) NULL,
	[COMPLICATIONS_DEHYDRATION] [varchar](50) NULL,
	[VACCINE_MANUFACTURER_2] [varchar](50) NULL,
	[GENOTYPING_SENT_TO_CDC] [varchar](50) NULL,
	[STRAIN_IDENTIFICATION_SENT] [varchar](50) NULL,
	[IGG_TEST_CONVALESCENT_RESULT] [varchar](50) NULL,
	[IGG_TEST_ACUTE_RESULT] [varchar](50) NULL,
	[LAB_TESTING] [varchar](50) NULL,
	[PAPULES] [varchar](50) NULL,
	[SCABS] [varchar](50) NULL,
	[LESIONS_TOTAL] [varchar](50) NULL,
	[MACULES] [varchar](50) NULL,
	[IGG_TEST_ACUTE_DATE] [datetime] NULL,
	[VACCINE_DATE_4] [datetime] NULL,
	[PCR_TEST_DATE] [datetime] NULL,
	[VACCINE_DATE_2] [datetime] NULL,
	[MEDICATION_START_DATE] [datetime] NULL,
	[DFA_TEST_DATE] [datetime] NULL,
	[VACCINE_DATE_3] [datetime] NULL,
	[IGG_TEST_CONVALESCENT_DATE] [datetime] NULL,
	[MEDICATION_STOP_DATE] [datetime] NULL,
	[VACCINE_DATE_1] [datetime] NULL,
	[LAB_TESTING_OTHER_DATE] [datetime] NULL,
	[CULTURE_TEST_DATE] [datetime] NULL,
	[GENOTYPING_SENT_TO_CDC_DATE] [datetime] NULL,
	[FEVER_ONSET_DATE] [datetime] NULL,
	[RASH_ONSET_DATE] [datetime] NULL,
	[DEATH_VARICELLA_DATE] [datetime] NULL,
	[IGM_TEST_DATE] [datetime] NULL,
	[VACCINE_DATE_5] [datetime] NULL,
	[VARICELLA_VACCINE_DOSES_NUMBER] [numeric](18, 0) NULL,
	[FEVER_DURATION_DAYS] [numeric](18, 0) NULL,
	[PREVIOUS_DIAGNOSIS_AGE] [numeric](18, 0) NULL,
	[LESIONS_TOTAL_LT50] [numeric](18, 0) NULL,
	[VESICLES_NUMBER] [numeric](18, 0) NULL,
	[MACULES_NUMBER] [numeric](18, 0) NULL,
	[PAPULES_NUMBER] [numeric](18, 0) NULL,
	[PREGNANT_WEEKS] [numeric](18, 0) NULL,
	[RASH_CRUSTED_DAYS] [numeric](18, 0) NULL,
	[RASH_DURATION_DAYS] [numeric](18, 0) NULL,
	[FEVER_TEMPERATURE] [numeric](18, 0) NULL,
	[PCR_TEST_SOURCE_OTHER] [varchar](50) NULL,
	[VARICELLA_NO_2NDVACCINE_OTHER] [varchar](50) NULL,
	[VACCINE_LOT_5] [varchar](50) NULL,
	[RASH_LOCATION_DERMATOME] [varchar](50) NULL,
	[IGG_TEST_OTHER] [varchar](50) NULL,
	[VACCINE_LOT_4] [varchar](50) NULL,
	[VACCINE_LOT_1] [varchar](50) NULL,
	[DEATH_CAUSE] [varchar](50) NULL,
	[VARICELLA_NO_VACCINE_OTHER] [varchar](50) NULL,
	[IGG_TEST_ACUTE_VALUE] [varchar](50) NULL,
	[TRANSMISSION_SETTING_OTHER] [varchar](50) NULL,
	[IMMUNOCOMPROMISED_CONDITION] [varchar](50) NULL,
	[LAB_TESTING_OTHER_RESULT_VALUE] [varchar](50) NULL,
	[VACCINE_LOT_3] [varchar](50) NULL,
	[MEDICATION_NAME_OTHER] [varchar](50) NULL,
	[PREVIOUS_DIAGNOSIS_BY_OTHER] [varchar](50) NULL,
	[RASH_LOCATION_OTHER] [varchar](50) NULL,
	[IGG_TEST_CONVALESCENT_VALUE] [varchar](50) NULL,
	[IGM_TEST_TYPE_OTHER] [varchar](50) NULL,
	[PCR_TEST_RESULT_OTHER] [varchar](50) NULL,
	[VACCINE_LOT_2] [varchar](50) NULL,
	[IGM_TEST_RESULT_VALUE] [varchar](50) NULL,
	[LAST_CHG_TIME] [datetime] NULL,
	[COMPLICATIONS_OTHER_SPECIFY] [varchar](50) NULL,
 CONSTRAINT [PK__D_VAR_PAM] PRIMARY KEY CLUSTERED 
(
	[D_VAR_PAM_KEY] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[F_INTERVIEW_CASE]    Script Date: 3/18/2025 5:42:18 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[F_INTERVIEW_CASE](
	[D_INTERVIEW_KEY] [float] NOT NULL,
	[PATIENT_KEY] [numeric](18, 0) NULL,
	[IX_INTERVIEWER_KEY] [numeric](18, 0) NULL,
	[INVESTIGATION_KEY] [numeric](18, 0) NULL,
	[INTERPRETER_KEY] [float] NULL,
	[NURSE_KEY] [float] NULL,
	[PHYSICIAN_KEY] [float] NULL,
	[PROXY_KEY] [float] NULL,
	[IX_INTERVIEWEE_KEY] [float] NULL,
	[INTERVENTION_SITE_KEY] [numeric](20, 0) NULL
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[F_PAGE_CASE]    Script Date: 3/18/2025 5:42:18 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[F_PAGE_CASE](
	[D_INV_ADMINISTRATIVE_KEY] [bigint] NULL,
	[D_INV_CLINICAL_KEY] [bigint] NULL,
	[D_INV_COMPLICATION_KEY] [bigint] NULL,
	[D_INV_CONTACT_KEY] [bigint] NULL,
	[D_INV_DEATH_KEY] [bigint] NULL,
	[D_INV_EPIDEMIOLOGY_KEY] [bigint] NULL,
	[D_INV_HIV_KEY] [bigint] NULL,
	[D_INV_PATIENT_OBS_KEY] [bigint] NULL,
	[D_INV_ISOLATE_TRACKING_KEY] [bigint] NULL,
	[D_INV_LAB_FINDING_KEY] [bigint] NULL,
	[D_INV_MEDICAL_HISTORY_KEY] [bigint] NULL,
	[D_INV_MOTHER_KEY] [bigint] NULL,
	[D_INV_OTHER_KEY] [bigint] NULL,
	[D_INV_PREGNANCY_BIRTH_KEY] [bigint] NULL,
	[D_INV_RESIDENCY_KEY] [bigint] NULL,
	[D_INV_RISK_FACTOR_KEY] [bigint] NULL,
	[D_INV_SOCIAL_HISTORY_KEY] [bigint] NULL,
	[D_INV_SYMPTOM_KEY] [bigint] NULL,
	[D_INV_TREATMENT_KEY] [bigint] NULL,
	[D_INV_TRAVEL_KEY] [bigint] NULL,
	[D_INV_UNDER_CONDITION_KEY] [bigint] NULL,
	[D_INV_VACCINATION_KEY] [bigint] NULL,
	[D_INVESTIGATION_REPEAT_KEY] [float] NULL,
	[D_INV_PLACE_REPEAT_KEY] [float] NULL,
	[CONDITION_KEY] [bigint] NULL,
	[INVESTIGATION_KEY] [bigint] NULL,
	[PHYSICIAN_KEY] [bigint] NULL,
	[INVESTIGATOR_KEY] [bigint] NULL,
	[HOSPITAL_KEY] [bigint] NULL,
	[PATIENT_KEY] [bigint] NULL,
	[PERSON_AS_REPORTER_KEY] [bigint] NULL,
	[ORG_AS_REPORTER_KEY] [bigint] NULL,
	[GEOCODING_LOCATION_KEY] [bigint] NULL,
	[ADD_DATE_KEY] [bigint] NULL,
	[LAST_CHG_DATE_KEY] [bigint] NULL
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[F_STD_PAGE_CASE]    Script Date: 3/18/2025 5:42:18 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[F_STD_PAGE_CASE](
	[D_INV_ADMINISTRATIVE_KEY] [bigint] NULL,
	[D_INV_CLINICAL_KEY] [bigint] NULL,
	[D_INV_COMPLICATION_KEY] [bigint] NULL,
	[D_INV_CONTACT_KEY] [bigint] NULL,
	[D_INV_DEATH_KEY] [bigint] NULL,
	[D_INV_EPIDEMIOLOGY_KEY] [bigint] NULL,
	[D_INV_HIV_KEY] [bigint] NULL,
	[D_INV_PATIENT_OBS_KEY] [bigint] NULL,
	[D_INV_ISOLATE_TRACKING_KEY] [bigint] NULL,
	[D_INV_LAB_FINDING_KEY] [bigint] NULL,
	[D_INV_MEDICAL_HISTORY_KEY] [bigint] NULL,
	[D_INV_MOTHER_KEY] [bigint] NULL,
	[D_INV_OTHER_KEY] [bigint] NULL,
	[D_INV_PREGNANCY_BIRTH_KEY] [bigint] NULL,
	[D_INV_RESIDENCY_KEY] [bigint] NULL,
	[D_INV_RISK_FACTOR_KEY] [bigint] NULL,
	[D_INV_SOCIAL_HISTORY_KEY] [bigint] NULL,
	[D_INV_SYMPTOM_KEY] [bigint] NULL,
	[D_INV_TREATMENT_KEY] [bigint] NULL,
	[D_INV_TRAVEL_KEY] [bigint] NULL,
	[D_INV_UNDER_CONDITION_KEY] [bigint] NULL,
	[D_INV_VACCINATION_KEY] [bigint] NULL,
	[D_INVESTIGATION_REPEAT_KEY] [float] NULL,
	[D_INV_PLACE_REPEAT_KEY] [float] NULL,
	[CONDITION_KEY] [bigint] NULL,
	[INVESTIGATION_KEY] [bigint] NULL,
	[PHYSICIAN_KEY] [bigint] NULL,
	[INVESTIGATOR_KEY] [bigint] NULL,
	[HOSPITAL_KEY] [bigint] NULL,
	[PATIENT_KEY] [bigint] NULL,
	[PERSON_AS_REPORTER_KEY] [bigint] NULL,
	[ORG_AS_REPORTER_KEY] [bigint] NULL,
	[ORDERING_FACILITY_KEY] [bigint] NULL,
	[GEOCODING_LOCATION_KEY] [bigint] NULL,
	[CLOSED_BY_KEY] [bigint] NULL,
	[DISPOSITIONED_BY_KEY] [bigint] NULL,
	[FACILITY_FLD_FOLLOW_UP_KEY] [bigint] NULL,
	[INVSTGTR_FLD_FOLLOW_UP_KEY] [bigint] NULL,
	[PROVIDER_FLD_FOLLOW_UP_KEY] [bigint] NULL,
	[SUPRVSR_OF_FLD_FOLLOW_UP_KEY] [bigint] NULL,
	[INIT_ASGNED_FLD_FOLLOW_UP_KEY] [bigint] NULL,
	[INIT_FOLLOW_UP_INVSTGTR_KEY] [bigint] NULL,
	[INIT_ASGNED_INTERVIEWER_KEY] [bigint] NULL,
	[INTERVIEWER_ASSIGNED_KEY] [bigint] NULL,
	[SURVEILLANCE_INVESTIGATOR_KEY] [bigint] NULL,
	[SUPRVSR_OF_CASE_ASSGNMENT_KEY] [bigint] NULL,
	[DELIVERING_HOSP_KEY] [bigint] NULL,
	[DELIVERING_MD_KEY] [bigint] NULL,
	[MOTHER_OB_GYN_KEY] [bigint] NULL,
	[PEDIATRICIAN_KEY] [bigint] NULL,
	[ADD_DATE_KEY] [bigint] NULL,
	[LAST_CHG_DATE_KEY] [bigint] NULL
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[F_VAR_PAM]    Script Date: 3/18/2025 5:42:18 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[F_VAR_PAM](
	[PERSON_KEY] [bigint] NOT NULL,
	[D_VAR_PAM_KEY] [bigint] NOT NULL,
	[PROVIDER_KEY] [bigint] NOT NULL,
	[D_PCR_SOURCE_GROUP_KEY] [bigint] NOT NULL,
	[D_RASH_LOC_GEN_GROUP_KEY] [bigint] NOT NULL,
	[HOSPITAL_KEY] [bigint] NOT NULL,
	[ORG_AS_REPORTER_KEY] [bigint] NOT NULL,
	[PERSON_AS_REPORTER_KEY] [bigint] NOT NULL,
	[PHYSICIAN_KEY] [bigint] NOT NULL,
	[ADD_DATE_KEY] [bigint] NOT NULL,
	[LAST_CHG_DATE_KEY] [bigint] NOT NULL,
	[INVESTIGATION_KEY] [bigint] NOT NULL
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[GENERIC_CASE]    Script Date: 3/18/2025 5:42:18 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[GENERIC_CASE](
	[INVESTIGATOR_KEY] [bigint] NOT NULL,
	[PHYSICIAN_KEY] [bigint] NOT NULL,
	[REPORTER_KEY] [bigint] NOT NULL,
	[INV_ASSIGNED_DT_KEY] [bigint] NOT NULL,
	[ILLNESS_DURATION] [varchar](20) NULL,
	[ILLNESS_DURATION_UNIT] [varchar](20) NULL,
	[PATIENT_AGE_AT_ONSET] [numeric](18, 0) NULL,
	[PATIENT_KEY] [bigint] NOT NULL,
	[PATIENT_AGE_AT_ONSET_UNIT] [varchar](20) NULL,
	[FOOD_HANDLR_IND] [varchar](50) NULL,
	[DAYCARE_ASSOCIATION_IND] [varchar](50) NULL,
	[DETECTION_METHOD] [varchar](20) NULL,
	[DETECTION_METHOD_OTHER] [varchar](100) NULL,
	[ADT_HSPTL_KEY] [bigint] NOT NULL,
	[PATIENT_PREGNANCY_STATUS] [varchar](50) NULL,
	[PELVIC_INFLAMMATORY_DISS_IND] [varchar](50) NULL,
	[RPT_SRC_ORG_KEY] [bigint] NOT NULL,
	[INVESTIGATION_KEY] [bigint] NOT NULL,
	[CONDITION_KEY] [bigint] NOT NULL,
	[LDF_GROUP_KEY] [bigint] NOT NULL,
	[GEOCODING_LOCATION_KEY] [bigint] NULL,
PRIMARY KEY CLUSTERED 
(
	[INVESTIGATOR_KEY] ASC,
	[PHYSICIAN_KEY] ASC,
	[REPORTER_KEY] ASC,
	[INV_ASSIGNED_DT_KEY] ASC,
	[PATIENT_KEY] ASC,
	[RPT_SRC_ORG_KEY] ASC,
	[ADT_HSPTL_KEY] ASC,
	[INVESTIGATION_KEY] ASC,
	[CONDITION_KEY] ASC,
	[LDF_GROUP_KEY] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[HEP_MULTI_VALUE_FIELD]    Script Date: 3/18/2025 5:42:18 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[HEP_MULTI_VALUE_FIELD](
	[HEP_MULTI_VAL_GRP_KEY] [bigint] NOT NULL,
	[HEP_MULTI_VAL_DATA_KEY] [bigint] NOT NULL,
	[REASON_FOR_TESTING] [varchar](50) NULL,
	[DISEASE_DIAGNOSIS] [varchar](50) NULL,
	[CNTRY_TRAVELD_OUT_USA_CAN] [varchar](50) NULL,
	[CNTRY_NPP_TRAVELD_OUT_USA_CAN] [varchar](50) NULL,
	[HEP_B_CONTACT_TYPE] [varchar](50) NULL,
	[TATTOOED_IN6WKMON_LOCATION] [varchar](50) NULL,
	[PIERCING_IN6WKMON_LOCATION] [varchar](50) NULL,
	[HEPB_INCARCERATION_FACILITY_TY] [varchar](50) NULL,
	[HEP_C_CONTACT_TYPE] [varchar](50) NULL,
	[HEP_A_CONTACT_TYPE] [varchar](50) NULL,
PRIMARY KEY CLUSTERED 
(
	[HEP_MULTI_VAL_GRP_KEY] ASC,
	[HEP_MULTI_VAL_DATA_KEY] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[HEP_MULTI_VALUE_FIELD_GROUP]    Script Date: 3/18/2025 5:42:18 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[HEP_MULTI_VALUE_FIELD_GROUP](
	[HEP_MULTI_VAL_GRP_KEY] [bigint] NOT NULL,
PRIMARY KEY CLUSTERED 
(
	[HEP_MULTI_VAL_GRP_KEY] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[HEPATITIS_CASE]    Script Date: 3/18/2025 5:42:18 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[HEPATITIS_CASE](
	[REPORTER_KEY] [bigint] NOT NULL,
	[INVESTIGATOR_KEY] [bigint] NOT NULL,
	[PHYSICIAN_KEY] [bigint] NOT NULL,
	[PATIENT_KEY] [bigint] NOT NULL,
	[ALT_RESULT_DT] [datetime] NULL,
	[INV_ASSIGNED_DT_KEY] [bigint] NOT NULL,
	[AST_RESULT_DT] [datetime] NULL,
	[RPT_SRC_ORG_KEY] [bigint] NOT NULL,
	[PATIENT_SYMPTOMATIC_IND] [varchar](50) NULL,
	[PATIENT_JUNDICED_IND] [varchar](50) NULL,
	[PATIENT_PREGNANT_IND] [varchar](50) NULL,
	[PATIENT_PREGNANCY_DUE_DT] [datetime] NULL,
	[HEP_A_TOTAL_ANTIBODY] [varchar](50) NULL,
	[HEP_A_IGM_ANTIBODY] [varchar](50) NULL,
	[HEP_B_SURFACE_ANTIGEN] [varchar](50) NULL,
	[HEP_B_TOTAL_ANTIBODY] [varchar](50) NULL,
	[HEP_B_IGM_ANTIBODY] [varchar](50) NULL,
	[HEP_C_TOTAL_ANTIBODY] [varchar](50) NULL,
	[HEP_D_TOTAL_ANTIBODY] [varchar](50) NULL,
	[HEP_E_TOTAL_ANTIBODY] [varchar](50) NULL,
	[ANTIHCV_SIGNAL_TO_CUTOFF_RATIO] [varchar](2000) NULL,
	[ANTIHCV_SUPPLEMENTAL_ASSAY] [varchar](50) NULL,
	[HCV_RNA] [varchar](50) NULL,
	[ALT_SGPT_RESULT] [numeric](18, 0) NULL,
	[ALT_SGPT_RESULT_UPPER_LIMIT] [numeric](18, 0) NULL,
	[AST_SGOT_RESULT] [numeric](18, 0) NULL,
	[AST_SGOT_RESULT_UPPER_LIMIT] [numeric](18, 0) NULL,
	[HEP_A_EPLINK_IND] [varchar](50) NULL,
	[HEP_A_CONTACTED_IND] [varchar](50) NULL,
	[D_N_P_EMPLOYEE_IND] [varchar](50) NULL,
	[D_N_P_HOUSEHOLD_CONTACT_IND] [varchar](50) NULL,
	[HEP_A_KEYENT_IN_CHILDCARE_IND] [varchar](50) NULL,
	[HEPA_MALE_SEX_PARTNER_NBR] [varchar](50) NULL,
	[HEPA_FEMALE_SEX_PARTNER_NBR] [varchar](50) NULL,
	[STREET_DRUG_INJECTED_IN_2_6_WK] [varchar](50) NULL,
	[STREET_DRUG_USED_IN_2_6_WK] [varchar](50) NULL,
	[TRAVEL_OUT_USA_CAN_IND] [varchar](50) NULL,
	[HOUSEHOLD_NPP_OUT_USA_CAN] [varchar](50) NULL,
	[PART_OF_AN_OUTBRK_IND] [varchar](50) NULL,
	[ASSOCIATED_OUTBRK_TYPE] [varchar](50) NULL,
	[FOODBORNE_OUTBRK_FOOD_ITEM] [varchar](2000) NULL,
	[FOODHANDLER_2_WK_PRIOR_ONSET] [varchar](50) NULL,
	[HEP_A_VACC_RECEIVED_IND] [varchar](50) NULL,
	[HEP_A_VACC_RECEIVED_DOSE] [varchar](50) NULL,
	[HEP_A_VACC_LAST_RECEIVED_YR] [numeric](18, 0) NULL,
	[IMMUNE_GLOBULIN_RECEIVED_IND] [varchar](50) NULL,
	[GLOBULIN_LAST_RECEIVED_YR] [datetime] NULL,
	[HEP_B_CONTACTED_IND] [varchar](50) NULL,
	[HEP_B_OTHER_CONTACT_TYPE] [varchar](2000) NULL,
	[HEPB_STD_TREATED_IND] [varchar](50) NULL,
	[HEPB_STD_LAST_TREATMENT_YR] [numeric](18, 0) NULL,
	[STREET_DRUG_INJECTED_IN6WKMON] [varchar](50) NULL,
	[HEMODIALYSIS_IN_LAST_6WKMON] [varchar](50) NULL,
	[BLOOD_CONTAMINATION_IN6WKMON] [varchar](50) NULL,
	[HEPB_BLOOD_RECEIVED_IN6WKMON] [varchar](50) NULL,
	[HEPB_BLOOD_RECEIVED_DT] [datetime] NULL,
	[BLOOD_EXPOSURE_IN_LAST6WKMON] [varchar](50) NULL,
	[BLOOD_EXPOSURE_IN6WKMON_OTHER] [varchar](2000) NULL,
	[HEPB_MED_DEN_EMPLOYEE_IN6WKMON] [varchar](50) NULL,
	[HEPB_MED_DEN_BLOOD_CONTACT_FRQ] [varchar](50) NULL,
	[HEPB_PUB_SAFETY_WORKER_IN6WKMO] [varchar](50) NULL,
	[HEPB_PUBSAFETY_BLOODCONTACTFRQ] [varchar](50) NULL,
	[TATTOOED_IN6WKMON_BEFORE_ONSET] [varchar](50) NULL,
	[TATTOOED_IN6WKMONOTHERLOCATION] [varchar](2000) NULL,
	[PIERCING_IN6WKMON_BEFORE_ONSET] [varchar](50) NULL,
	[PIERCING_IN6WKMONOTHERLOCATION] [varchar](2000) NULL,
	[DEN_WORK_OR_SURGERY_IN6WKMON] [varchar](50) NULL,
	[NON_ORAL_SURGERY_IN6WKMON] [varchar](50) NULL,
	[HSPTLIZD_IN6WKMON_BEFORE_ONSET] [varchar](50) NULL,
	[LONGTERMCARE_RESIDENT_IN6WKMON] [varchar](50) NULL,
	[B_INCARCERATED24PLUSHRSIN6WKMO] [varchar](50) NULL,
	[STREET_DRUG_USED_IN6WKMON] [varchar](50) NULL,
	[B_INCARCERATED_6PLUS_MON_IND] [varchar](50) NULL,
	[B_LAST6PLUSMON_INCARCERATE_YR] [numeric](4, 0) NULL,
	[BLAST6PLUSMO_INCARCERATEPERIOD] [numeric](4, 0) NULL,
	[B_LAST_INCARCERATE_PERIOD_UNIT] [varchar](50) NULL,
	[HEP_B_VACC_RECEIVED_IND] [varchar](50) NULL,
	[HEP_B_VACC_SHOT_RECEIVED_NBR] [varchar](50) NULL,
	[HEP_B_VACC_LAST_RECEIVED_YR] [numeric](4, 0) NULL,
	[ANTI_HBSAG_TESTED_IND] [varchar](50) NULL,
	[ANTI_HBS_POSITIVE_REACTIVE_IND] [varchar](50) NULL,
	[HEP_C_CONTACTED_IND] [varchar](50) NULL,
	[MED_DEN_EMPLOYEE_IN_2WK6MO] [varchar](50) NULL,
	[PUBLIC_SAFETY_WORKER_IN_2WK6MO] [varchar](50) NULL,
	[TATTOOED_IN2WK6MO_BEFORE_ONSET] [varchar](50) NULL,
	[TATTOOED_IN2WK6MO_LOCATION] [varchar](50) NULL,
	[PIERCING_IN2WK6MO_BEFORE_ONSET] [varchar](50) NULL,
	[PIERCING_IN2WK6MO_LOCATION] [varchar](50) NULL,
	[STREET_DRUG_INJECTED_IN_2WK6MO] [varchar](50) NULL,
	[STREET_DRUG_USED_IN_2WK6MO] [varchar](50) NULL,
	[HEMODIALYSIS_IN_LAST_2WK6MO] [varchar](50) NULL,
	[BLOOD_CONTAMINATION_IN_2WK6MO] [varchar](50) NULL,
	[HEPC_BLOOD_RECEIVED_IN_2WK6MO] [varchar](50) NULL,
	[BLOOD_EXPOSURE_IN_LAST2WK6MO] [varchar](50) NULL,
	[BLOOD_EXPOSURE_IN2WK6MO_OTHER] [varchar](2000) NULL,
	[DEN_WORK_OR_SURGERY_IN2WK6MO] [varchar](50) NULL,
	[NON_ORAL_SURGERY_IN2WK6MO] [varchar](50) NULL,
	[HSPTLIZD_IN2WK6MO_BEFORE_ONSET] [varchar](50) NULL,
	[LONGTERMCARE_RESIDENT_IN2WK6MO] [varchar](50) NULL,
	[INCARCERATED_24PLUSHRSIN2WK6MO] [varchar](50) NULL,
	[BLOOD_TRANSFUSION_BEFORE_1992] [varchar](50) NULL,
	[ORGAN_TRANSPLANT_BEFORE_1992] [varchar](50) NULL,
	[CLOT_FACTOR_CONCERN_BEFORE1987] [varchar](50) NULL,
	[LONGTERM_HEMODIALYSIS_IND] [varchar](50) NULL,
	[EVER_INJECT_NONPRESCRIBED_DRUG] [varchar](50) NULL,
	[LIFETIME_SEX_PARTNER_NBR] [numeric](15, 5) NULL,
	[EVER_INCARCERATED_IND] [varchar](50) NULL,
	[HEPATITIS_CONTACTED_IND] [varchar](50) NULL,
	[HEPATITIS_CONTACT_TYPE] [varchar](50) NULL,
	[PATIENT_MOTHER_BORN_OUT_USA] [varchar](50) NULL,
	[MOTHER_HBSAG_POSITIVE_IND] [varchar](50) NULL,
	[MOTHR_HBSAG_POSTV_POSTDELIVERY] [varchar](50) NULL,
	[MOTHER_HBSAG_POSITIVE_DT] [datetime] NULL,
	[HEP_B_VACC_DOSE_CHILD_RECEIVED] [varchar](50) NULL,
	[HEPB_1STVACC_CHILD_RECEIVED_DT] [datetime] NULL,
	[HEPB_2NDVACC_CHILD_RECEIVED_DT] [datetime] NULL,
	[HEPB_3RDVACC_CHILD_RECEIVED_DT] [datetime] NULL,
	[CHILD_RECEIVED_HBIG_IND] [varchar](50) NULL,
	[CHILD_RECEIVED_HBIG_DT] [datetime] NULL,
	[OUTPATIENT_IV_INFUSION_IN6WKMO] [varchar](50) NULL,
	[OUTPATIENT_IV_INFUSIONIN2WK6MO] [varchar](50) NULL,
	[HEP_MULTI_VAL_GRP_KEY] [bigint] NOT NULL,
	[OTHER_REASON_FOR_TESTING] [varchar](2000) NULL,
	[ADT_HSPTL_KEY] [bigint] NOT NULL,
	[HEPATITIS_OTHER_CONTACT_TYPE] [varchar](2000) NULL,
	[HEPA_OTHER_CONTACT_TYPE] [varchar](2000) NULL,
	[PIERCING_IN2WK6MO_OTHER_LOCAT] [varchar](2000) NULL,
	[TATTOOED_IN2WK6MOOTHERLOCATION] [varchar](2000) NULL,
	[HEP_C_OTHER_CONTACT_TYPE] [varchar](2000) NULL,
	[HEPC_FEMALE_SEX_PARTNER_NBR] [varchar](50) NULL,
	[HEPC_MALE_SEX_PARTNER_NBR] [varchar](50) NULL,
	[HEPC_MED_DEN_BLOOD_CONTACT_FRQ] [varchar](50) NULL,
	[C_LAST6PLUSMON_INCARCERATE_YR] [numeric](18, 0) NULL,
	[CLAST6PLUSMO_INCARCERATEPERIOD] [numeric](18, 0) NULL,
	[C_LAST_INCARCERATE_PERIOD_UNIT] [varchar](50) NULL,
	[C_INCARCERATED_6PLUS_MON_IND] [varchar](50) NULL,
	[HEPC_INCARCERATE_FACILITY_TYPE] [varchar](50) NULL,
	[HEPC_BLOOD_RECEIVED_DT] [datetime] NULL,
	[HEPC_PUBSAFETY_BLOODCONTACTFRQ] [varchar](50) NULL,
	[HEPB_FEMALE_SEX_PARTNER_NBR] [varchar](50) NULL,
	[HEPB_MALE_SEX_PARTNER_NBR] [varchar](50) NULL,
	[HEPC_STD_TREATED_IND] [varchar](50) NULL,
	[INVESTIGATION_KEY] [bigint] NOT NULL,
	[CONDITION_KEY] [bigint] NOT NULL,
	[HEPC_STD_LAST_TREATMENT_YR] [numeric](18, 0) NULL,
	[HEPC_MED_DEN_EMPLOYEE_IND] [varchar](50) NULL,
	[LDF_GROUP_KEY] [bigint] NOT NULL,
	[HEP_B_E_ANTIGEN] [varchar](50) NULL,
	[HEP_B_DNA] [varchar](50) NULL,
	[GEOCODING_LOCATION_KEY] [bigint] NULL,
	[PLACE_OF_BIRTH] [varchar](250) NULL,
	[INV_PATIENT_CHART_NBR] [varchar](2000) NULL,
PRIMARY KEY CLUSTERED 
(
	[REPORTER_KEY] ASC,
	[INVESTIGATOR_KEY] ASC,
	[PHYSICIAN_KEY] ASC,
	[PATIENT_KEY] ASC,
	[INV_ASSIGNED_DT_KEY] ASC,
	[RPT_SRC_ORG_KEY] ASC,
	[HEP_MULTI_VAL_GRP_KEY] ASC,
	[ADT_HSPTL_KEY] ASC,
	[INVESTIGATION_KEY] ASC,
	[CONDITION_KEY] ASC,
	[LDF_GROUP_KEY] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[HEPATITIS_DATAMART]    Script Date: 3/18/2025 5:42:18 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[HEPATITIS_DATAMART](
	[INIT_NND_NOT_DT] [datetime] NULL,
	[CASE_RPT_MMWR_WEEK] [numeric](18, 0) NULL,
	[CASE_RPT_MMWR_YEAR] [numeric](18, 0) NULL,
	[HEP_D_INFECTION_IND] [varchar](300) NULL,
	[HEP_MEDS_RECVD_IND] [varchar](300) NULL,
	[HEP_C_TOTAL_ANTIBODY] [varchar](300) NULL,
	[DIAGNOSIS_DT] [datetime] NULL,
	[DIE_FRM_THIS_ILLNESS_IND] [varchar](300) NULL,
	[DISEASE_IMPORTED_IND] [varchar](300) NULL,
	[EARLIEST_RPT_TO_CNTY] [datetime] NULL,
	[EARLIEST_RPT_TO_STATE_DT] [datetime] NULL,
	[BINATIONAL_RPTNG_CRIT] [varchar](300) NULL,
	[CHILDCARE_CASE_IND] [varchar](300) NULL,
	[CNTRY_USUAL_RESIDENCE] [varchar](300) NULL,
	[CT_BABYSITTER_IND] [varchar](300) NULL,
	[CT_CHILDCARE_IND] [varchar](300) NULL,
	[CT_HOUSEHOLD_IND] [varchar](300) NULL,
	[HEP_CONTACT_IND] [varchar](300) NULL,
	[HEP_CONTACT_EVER_IND] [varchar](300) NULL,
	[OTHER_CONTACT_IND] [varchar](300) NULL,
	[COM_SRC_OUTBREAK_IND] [varchar](300) NULL,
	[CONTACT_TYPE_OTH] [varchar](100) NULL,
	[CT_PLAYMATE_IND] [varchar](300) NULL,
	[SEXUAL_PARTNER_IND] [varchar](300) NULL,
	[DNP_HOUSEHOLD_CT_IND] [varchar](300) NULL,
	[HEP_A_EPLINK_IND] [varchar](300) NULL,
	[FEMALE_SEX_PRTNR_NBR] [numeric](18, 0) NULL,
	[FOODHNDLR_PRIOR_IND] [varchar](300) NULL,
	[DNP_EMPLOYEE_IND] [varchar](300) NULL,
	[STREET_DRUG_INJECTED] [varchar](300) NULL,
	[MALE_SEX_PRTNR_NBR] [numeric](18, 0) NULL,
	[OUTBREAK_IND] [varchar](300) NULL,
	[OBRK_FOODHNDLR_IND] [varchar](300) NULL,
	[FOOD_OBRK_FOOD_ITEM] [varchar](100) NULL,
	[OBRK_NOFOODHNDLR_IND] [varchar](300) NULL,
	[OBRK_UNIDENTIFIED_IND] [varchar](300) NULL,
	[OBRK_WATERBORNE_IND] [varchar](300) NULL,
	[STREET_DRUG_USED] [varchar](300) NULL,
	[SEX_PREF] [varchar](300) NULL,
	[HSPTL_ADMISSION_DT] [datetime] NULL,
	[HSPTL_DISCHARGE_DT] [datetime] NULL,
	[HSPTL_DURATION_DAYS] [numeric](18, 0) NULL,
	[HSPTLIZD_IND] [varchar](300) NULL,
	[ILLNESS_ONSET_DT] [datetime] NULL,
	[INV_CASE_STATUS] [varchar](300) NULL,
	[INV_COMMENTS] [varchar](2000) NULL,
	[INV_LOCAL_ID] [varchar](25) NULL,
	[INV_RPT_DT] [datetime] NULL,
	[INV_START_DT] [datetime] NULL,
	[INVESTIGATION_STATUS] [varchar](300) NULL,
	[JURISDICTION_NM] [varchar](300) NULL,
	[ALT_SGPT_RESULT] [numeric](18, 0) NULL,
	[ANTI_HBS_POS_REAC_IND] [varchar](300) NULL,
	[AST_SGOT_RESULT] [numeric](18, 0) NULL,
	[HEP_E_ANTIGEN] [varchar](300) NULL,
	[HBE_AG_DT] [date] NULL,
	[HEP_B_SURFACE_ANTIGEN] [varchar](300) NULL,
	[HBS_AG_DT] [date] NULL,
	[HEP_B_DNA] [varchar](300) NULL,
	[HBV_NAT_DT] [date] NULL,
	[HCV_RNA] [varchar](300) NULL,
	[HCV_RNA_DT] [date] NULL,
	[HEP_D_TEST_IND] [varchar](300) NULL,
	[HEP_A_IGM_ANTIBODY] [varchar](300) NULL,
	[IGM_ANTI_HAV_DT] [date] NULL,
	[HEP_B_IGM_ANTIBODY] [varchar](300) NULL,
	[IGM_ANTI_HBC_DT] [date] NULL,
	[PREV_NEG_HEP_TEST_IND] [varchar](300) NULL,
	[ANTIHCV_SIGCUT_RATIO] [varchar](25) NULL,
	[ANTIHCV_SUPP_ASSAY] [varchar](300) NULL,
	[SUPP_ANTI_HCV_DT] [date] NULL,
	[ALT_RESULT_DT] [date] NULL,
	[AST_RESULT_DT] [date] NULL,
	[ALT_SGPT_RSLT_UP_LMT] [numeric](18, 0) NULL,
	[AST_SGOT_RSLT_UP_LMT] [numeric](18, 0) NULL,
	[HEP_A_TOTAL_ANTIBODY] [varchar](300) NULL,
	[TOTAL_ANTI_HAV_DT] [date] NULL,
	[HEP_B_TOTAL_ANTIBODY] [varchar](300) NULL,
	[TOTAL_ANTI_HBC_DT] [date] NULL,
	[TOTAL_ANTI_HCV_DT] [date] NULL,
	[HEP_D_TOTAL_ANTIBODY] [varchar](300) NULL,
	[TOTAL_ANTI_HDV_DT] [date] NULL,
	[HEP_E_TOTAL_ANTIBODY] [varchar](300) NULL,
	[TOTAL_ANTI_HEV_DT] [date] NULL,
	[VERIFIED_TEST_DT] [date] NULL,
	[LEGACY_CASE_ID] [varchar](15) NULL,
	[DIABETES_IND] [varchar](300) NULL,
	[DIABETES_DX_DT] [date] NULL,
	[PREGNANCY_DUE_DT] [date] NULL,
	[PAT_JUNDICED_IND] [varchar](300) NULL,
	[PAT_PREV_AWARE_IND] [varchar](300) NULL,
	[HEP_CARE_PROVIDER] [varchar](300) NULL,
	[TEST_REASON] [varchar](300) NULL,
	[TEST_REASON_OTH] [varchar](150) NULL,
	[SYMPTOMATIC_IND] [varchar](300) NULL,
	[MTH_BORN_OUTSIDE_US] [varchar](300) NULL,
	[MTH_ETHNICITY] [varchar](300) NULL,
	[MTH_HBS_AG_PRIOR_POS] [varchar](300) NULL,
	[MTH_POS_AFTER] [varchar](300) NULL,
	[MTH_POS_TEST_DT] [date] NULL,
	[MTH_RACE] [varchar](300) NULL,
	[MTH_BIRTH_COUNTRY] [varchar](300) NULL,
	[NOT_SUBMIT_DT] [datetime] NULL,
	[PAT_REPORTED_AGE] [numeric](18, 0) NULL,
	[PAT_REPORTED_AGE_UNIT] [varchar](300) NULL,
	[PAT_CITY] [varchar](50) NULL,
	[PAT_COUNTRY] [varchar](300) NULL,
	[PAT_COUNTY] [varchar](300) NULL,
	[PAT_CURR_GENDER] [varchar](300) NULL,
	[PAT_DOB] [datetime] NULL,
	[PAT_ETHNICITY] [varchar](300) NULL,
	[PAT_FIRST_NM] [varchar](50) NULL,
	[PAT_LAST_NM] [varchar](50) NULL,
	[PAT_LOCAL_ID] [varchar](25) NULL,
	[PAT_MIDDLE_NM] [varchar](50) NULL,
	[PAT_PREGNANT_IND] [varchar](300) NULL,
	[PAT_RACE] [varchar](300) NULL,
	[PAT_STATE] [varchar](300) NULL,
	[PAT_STREET_ADDR_1] [varchar](50) NULL,
	[PAT_STREET_ADDR_2] [varchar](50) NULL,
	[PAT_ZIP_CODE] [varchar](10) NULL,
	[RPT_SRC_SOURCE_NM] [varchar](300) NULL,
	[RPT_SRC_STATE] [varchar](300) NULL,
	[RPT_SRC_CD_DESC] [varchar](300) NULL,
	[BLD_EXPOSURE_IND] [varchar](300) NULL,
	[BLD_RECVD_IND] [varchar](300) NULL,
	[BLD_RECVD_DT] [date] NULL,
	[MED_DEN_BLD_CT_FRQ] [varchar](300) NULL,
	[MED_DEN_EMPLOYEE_IND] [varchar](300) NULL,
	[MED_DEN_EMP_EVER_IND] [varchar](300) NULL,
	[CLOTFACTOR_PRIOR_1987] [varchar](300) NULL,
	[BLD_CONTAM_IND] [varchar](300) NULL,
	[DEN_WORK_OR_SURG_IND] [varchar](300) NULL,
	[HEMODIALYSIS_IND] [varchar](300) NULL,
	[LT_HEMODIALYSIS_IND] [varchar](300) NULL,
	[HSPTL_PRIOR_ONSET_IND] [varchar](300) NULL,
	[EVER_INJCT_NOPRSC_DRG] [varchar](300) NULL,
	[INCAR_24PLUSHRS_IND] [varchar](300) NULL,
	[INCAR_6PLUS_MO_IND] [varchar](300) NULL,
	[EVER_INCAR_IND] [varchar](300) NULL,
	[INCAR_TYPE_JAIL_IND] [varchar](300) NULL,
	[INCAR_TYPE_PRISON_IND] [varchar](300) NULL,
	[INCAR_TYPE_JUV_IND] [varchar](300) NULL,
	[LAST6PLUSMO_INCAR_PER] [numeric](18, 0) NULL,
	[LAST6PLUSMO_INCAR_YR] [numeric](18, 0) NULL,
	[OUTPAT_IV_INF_IND] [varchar](300) NULL,
	[LTCARE_RESIDENT_IND] [varchar](300) NULL,
	[LIFE_SEX_PRTNR_NBR] [numeric](18, 0) NULL,
	[BLD_EXPOSURE_OTH] [varchar](200) NULL,
	[PIERC_PRIOR_ONSET_IND] [varchar](300) NULL,
	[PIERC_PERF_LOC_OTH] [varchar](150) NULL,
	[PIERC_PERF_LOC] [varchar](300) NULL,
	[PUB_SAFETY_BLD_CT_FRQ] [varchar](300) NULL,
	[PUB_SAFETY_WORKER_IND] [varchar](300) NULL,
	[STD_TREATED_IND] [varchar](300) NULL,
	[STD_LAST_TREATMENT_YR] [numeric](18, 0) NULL,
	[NON_ORAL_SURGERY_IND] [varchar](300) NULL,
	[TATT_PRIOR_ONSET_IND] [varchar](300) NULL,
	[TATTOO_PERF_LOC] [varchar](300) NULL,
	[TATT_PRIOR_LOC_OTH] [varchar](150) NULL,
	[BLD_TRANSF_PRIOR_1992] [varchar](300) NULL,
	[ORGN_TRNSP_PRIOR_1992] [varchar](300) NULL,
	[TRANSMISSION_MODE] [varchar](300) NULL,
	[HOUSEHOLD_TRAVEL_IND] [varchar](300) NULL,
	[TRAVEL_OUT_USACAN_IND] [varchar](300) NULL,
	[TRAVEL_OUT_USACAN_LOC] [varchar](300) NULL,
	[HOUSEHOLD_TRAVEL_LOC] [varchar](300) NULL,
	[TRAVEL_REASON] [varchar](300) NULL,
	[IMM_GLOB_RECVD_IND] [varchar](300) NULL,
	[GLOB_LAST_RECVD_YR] [date] NULL,
	[VACC_RECVD_IND] [varchar](10) NULL,
	[VACC_DOSE_NBR_1] [numeric](18, 0) NULL,
	[VACC_RECVD_DT_1] [date] NULL,
	[VACC_DOSE_NBR_2] [numeric](18, 0) NULL,
	[VACC_RECVD_DT_2] [date] NULL,
	[VACC_DOSE_NBR_3] [numeric](18, 0) NULL,
	[VACC_RECVD_DT_3] [date] NULL,
	[VACC_DOSE_NBR_4] [numeric](18, 0) NULL,
	[VACC_RECVD_DT_4] [date] NULL,
	[VACC_GT_4_IND] [varchar](300) NULL,
	[VACC_DOSE_RECVD_NBR] [numeric](18, 0) NULL,
	[VACC_LAST_RECVD_YR] [numeric](18, 0) NULL,
	[ANTI_HBSAG_TESTED_IND] [varchar](300) NULL,
	[CONDITION_CD] [varchar](300) NULL,
	[EVENT_DATE] [datetime] NULL,
	[IMPORT_FROM_CITY] [varchar](300) NULL,
	[IMPORT_FROM_COUNTRY] [varchar](300) NULL,
	[IMPORT_FROM_COUNTY] [varchar](300) NULL,
	[IMPORT_FROM_STATE] [varchar](300) NULL,
	[INVESTIGATION_KEY] [bigint] NOT NULL,
	[INVESTIGATOR_NAME] [varchar](300) NULL,
	[PAT_ELECTRONIC_IND] [varchar](300) NULL,
	[PHYS_CITY] [varchar](300) NULL,
	[PHYS_COUNTY] [varchar](300) NULL,
	[PHYS_NAME] [varchar](300) NULL,
	[PHYS_STATE] [varchar](300) NULL,
	[PROGRAM_JURISDICTION_OID] [numeric](20, 0) NULL,
	[RPT_SRC_CITY] [varchar](300) NULL,
	[RPT_SRC_COUNTY] [varchar](300) NULL,
	[RPT_SRC_COUNTY_CD] [varchar](300) NULL,
	[PHYSICIAN_UID] [numeric](18, 0) NULL,
	[PATIENT_UID] [numeric](18, 0) NOT NULL,
	[CASE_UID] [numeric](18, 0) NOT NULL,
	[INVESTIGATOR_UID] [numeric](18, 0) NULL,
	[REPORTING_SOURCE_UID] [numeric](18, 0) NULL,
	[REFRESH_DATETIME] [datetime] NOT NULL,
	[PAT_BIRTH_COUNTRY] [varchar](50) NULL,
	[EVENT_DATE_TYPE] [varchar](100) NULL,
	[INNC_NOTIFICATION_DT] [datetime] NULL
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[INVESTIGATION]    Script Date: 3/18/2025 5:42:18 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[INVESTIGATION](
	[INVESTIGATION_KEY] [bigint] NOT NULL,
	[CASE_OID] [bigint] NULL,
	[CASE_UID] [bigint] NULL,
	[INV_LOCAL_ID] [varchar](50) NULL,
	[INV_SHARE_IND] [varchar](50) NULL,
	[OUTBREAK_NAME] [varchar](100) NULL,
	[INVESTIGATION_STATUS] [varchar](50) NULL,
	[INV_CASE_STATUS] [varchar](50) NULL,
	[CASE_TYPE] [varchar](50) NULL,
	[INV_COMMENTS] [varchar](2000) NULL,
	[JURISDICTION_CD] [varchar](20) NULL,
	[JURISDICTION_NM] [varchar](100) NULL,
	[EARLIEST_RPT_TO_PHD_DT] [datetime] NULL,
	[ILLNESS_ONSET_DT] [datetime] NULL,
	[ILLNESS_END_DT] [datetime] NULL,
	[INV_RPT_DT] [datetime] NULL,
	[INV_START_DT] [datetime] NULL,
	[RPT_SRC_CD_DESC] [varchar](100) NULL,
	[EARLIEST_RPT_TO_CNTY_DT] [datetime] NULL,
	[EARLIEST_RPT_TO_STATE_DT] [datetime] NULL,
	[CASE_RPT_MMWR_WK] [numeric](18, 0) NULL,
	[CASE_RPT_MMWR_YR] [numeric](18, 0) NULL,
	[DISEASE_IMPORTED_IND] [varchar](100) NULL,
	[IMPORT_FRM_CNTRY] [varchar](50) NULL,
	[IMPORT_FRM_STATE] [varchar](50) NULL,
	[IMPORT_FRM_CNTY] [varchar](50) NULL,
	[IMPORT_FRM_CITY] [varchar](2000) NULL,
	[EARLIEST_RPT_TO_CDC_DT] [datetime] NULL,
	[RPT_SRC_CD] [varchar](50) NULL,
	[IMPORT_FRM_CNTRY_CD] [varchar](50) NULL,
	[IMPORT_FRM_STATE_CD] [varchar](50) NULL,
	[IMPORT_FRM_CNTY_CD] [varchar](50) NULL,
	[IMPORT_FRM_CITY_CD] [varchar](50) NULL,
	[DIAGNOSIS_DT] [datetime] NULL,
	[HSPTL_ADMISSION_DT] [datetime] NULL,
	[HSPTL_DISCHARGE_DT] [datetime] NULL,
	[HSPTL_DURATION_DAYS] [numeric](18, 0) NULL,
	[OUTBREAK_IND] [varchar](50) NULL,
	[HSPTLIZD_IND] [varchar](50) NULL,
	[INV_STATE_CASE_ID] [varchar](199) NULL,
	[CITY_COUNTY_CASE_NBR] [varchar](199) NULL,
	[TRANSMISSION_MODE] [varchar](50) NULL,
	[RECORD_STATUS_CD] [varchar](8) NOT NULL,
	[PATIENT_PREGNANT_IND] [varchar](50) NULL,
	[DIE_FRM_THIS_ILLNESS_IND] [varchar](50) NULL,
	[DAYCARE_ASSOCIATION_IND] [varchar](50) NULL,
	[FOOD_HANDLR_IND] [varchar](50) NULL,
	[INVESTIGATION_DEATH_DATE] [datetime] NULL,
	[PATIENT_AGE_AT_ONSET] [numeric](18, 0) NULL,
	[PATIENT_AGE_AT_ONSET_UNIT] [varchar](20) NULL,
	[INV_ASSIGNED_DT] [datetime] NULL,
	[DETECTION_METHOD_DESC_TXT] [varchar](50) NULL,
	[ILLNESS_DURATION] [numeric](18, 0) NULL,
	[ILLNESS_DURATION_UNIT] [varchar](50) NULL,
	[CONTACT_INV_COMMENTS] [varchar](2000) NULL,
	[CONTACT_INV_PRIORITY] [varchar](20) NULL,
	[CONTACT_INFECTIOUS_FROM_DATE] [datetime] NULL,
	[CONTACT_INFECTIOUS_TO_DATE] [datetime] NULL,
	[CONTACT_INV_STATUS] [varchar](20) NULL,
	[REFERRAL_BASIS] [varchar](100) NULL,
	[CURR_PROCESS_STATE] [varchar](100) NULL,
	[INV_PRIORITY_CD] [varchar](100) NULL,
	[COINFECTION_ID] [varchar](100) NULL,
	[LEGACY_CASE_ID] [varchar](199) NULL,
	[INV_CLOSE_DT] [datetime] NULL,
	[PROGRAM_AREA_DESCRIPTION] [varchar](50) NULL,
	[ADD_TIME] [datetime] NULL,
	[LAST_CHG_TIME] [datetime] NULL,
	[INVESTIGATION_ADDED_BY] [varchar](50) NULL,
	[INVESTIGATION_LAST_UPDATED_BY] [varchar](50) NULL,
	[OUTBREAK_NAME_DESC] [varchar](300) NULL,
PRIMARY KEY CLUSTERED 
(
	[INVESTIGATION_KEY] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[LDF_DATA]    Script Date: 3/18/2025 5:42:18 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[LDF_DATA](
	[LDF_DATA_KEY] [bigint] NOT NULL,
	[LDF_GROUP_KEY] [bigint] NULL,
	[LDF_COLUMN_TYPE] [varchar](300) NULL,
	[CONDITION_CD] [varchar](10) NULL,
	[CONDITION_DESC_TXT] [varchar](100) NULL,
	[CDC_NATIONAL_ID] [varchar](50) NULL,
	[CLASS_CD] [varchar](20) NULL,
	[CODE_SET_NM] [varchar](256) NULL,
	[BUSINESS_OBJ_NM] [varchar](50) NULL,
	[DISPLAY_ORDER_NUMBER] [int] NULL,
	[FIELD_SIZE] [varchar](10) NULL,
	[LDF_VALUE] [varchar](2000) NULL,
	[IMPORT_VERSION_NBR] [bigint] NULL,
	[LABEL_TXT] [varchar](300) NULL,
	[LDF_OID] [varchar](50) NULL,
	[NND_IND] [varchar](1) NULL,
	[RECORD_STATUS_CD] [varchar](8) NOT NULL,
PRIMARY KEY CLUSTERED 
(
	[LDF_DATA_KEY] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[LDF_FOODBORNE]    Script Date: 3/18/2025 5:42:18 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[LDF_FOODBORNE](
	[INVESTIGATION_KEY] [numeric](20, 0) NULL,
	[INVESTIGATION_LOCAL_ID] [varchar](50) NULL,
	[PROGRAM_JURISDICTION_OID] [numeric](20, 0) NULL,
	[PATIENT_KEY] [numeric](20, 0) NULL,
	[PATIENT_LOCAL_ID] [varchar](50) NULL,
	[DISEASE_NAME] [varchar](50) NULL,
	[DISEASE_CD] [varchar](10) NULL,
	[C_FDD_Q_8Did_patient_work_as_] [varchar](4000) NULL,
	[C_FDD_Q_31Did_patient_come_in] [varchar](4000) NULL,
	[C_FDD_Q_32Type_of_animal] [varchar](4000) NULL,
	[C_FDD_Q_33Name_or_Location_of] [varchar](4000) NULL,
	[C_FDD_Q_128Was_the_isolate_bi] [varchar](4000) NULL,
	[C_FDD_Q_129Was_isolate_Shiga_] [varchar](4000) NULL,
	[C_FDD_Q_11Did_patient_travel_] [varchar](4000) NULL,
	[C_FDD_Q_12Applicable_incubati] [varchar](4000) NULL,
	[C_FDD_Q_13What_was_the_purpos] [varchar](4000) NULL,
	[C_FDD_Q_14If_Other__please_sp] [varchar](4000) NULL,
	[C_FDD_Q_189Is_patient_a_U_S__] [varchar](4000) NULL,
	[C_FDD_Q_190Was_the_patient_sy] [varchar](4000) NULL,
	[C_FDD_Q_191If_Yes__did_the_pa] [varchar](4000) NULL,
	[C_FDD_Q_1Attend_a_day_care_ce] [varchar](4000) NULL,
	[C_FDD_Q_25Was_there_recreatio] [varchar](4000) NULL,
	[C_FDD_Q_26What_was_the_recrea] [varchar](4000) NULL,
	[C_FDD_Q_2Work_at_a_day_care_c] [varchar](4000) NULL,
	[C_FDD_Q_30Name_or_location_of] [varchar](4000) NULL,
	[C_FDD_Q_3Live_with_a_day_care] [varchar](4000) NULL,
	[C_FDD_Q_77Does_the_patient_kn] [varchar](4000) NULL,
	[C_FDD_Q_79Are_there_other_cas] [varchar](4000) NULL,
	[C_FDD_Q_286Was_botulism_labor] [varchar](4000) NULL,
	[C_FDD_Q_287Was_C__botulinum_i] [varchar](4000) NULL,
	[C_FDD_Q_78If_Yes__did_the_hea] [varchar](4000) NULL,
	[C_FDD_Q_15Destination_1_Type] [varchar](4000) NULL,
	[C_FDD_Q_292Destination_1] [varchar](4000) NULL,
	[C_FDD_Q_17Mode_of_Travel] [varchar](4000) NULL,
	[C_FDD_Q_21What_is_the_source_] [varchar](4000) NULL,
	[C_FDD_Q_24Did_the_patient_dri] [varchar](4000) NULL,
	[C_FDD_Q_93What_is_the_source_] [varchar](4000) NULL,
	[C_FDD_Q_16Destination_1] [varchar](4000) NULL,
	[C_FDD_Q_18Date_of_Arrival] [varchar](4000) NULL,
	[C_FDD_Q_34Did_the_patient_acq] [varchar](4000) NULL,
	[C_FDD_Q_22If_Private_Well__ho] [varchar](4000) NULL,
	[C_FDD_Q_20If_more_than_3_dest] [varchar](4000) NULL,
	[C_FDD_Q_56Destination_2_Type] [varchar](4000) NULL,
	[C_FDD_Q_57Destination_2] [varchar](4000) NULL,
	[C_FDD_Q_58Mode_of_Travel] [varchar](4000) NULL,
	[C_FDD_Q_59Date_of_Arrival] [varchar](4000) NULL,
	[C_FDD_Q_233Did_patient_have_a] [varchar](4000) NULL,
	[C_FDD_Q_234If_Other_Prior_Ill] [varchar](4000) NULL,
	[C_FDD_Q_244Applicable_incubat] [varchar](4000) NULL,
	[C_FDD_Q_374If_Other_Mammal__p] [varchar](4000) NULL,
	[C_FDD_Q_239If_Immunodeficienc] [varchar](4000) NULL,
	[C_FDD_Q_35Has_the_patient_eat] [varchar](4000) NULL,
	[C_FDD_Q_19Date_of_Departure] [varchar](4000) NULL,
	[C_FDD_Q_60Date_of_Departure] [varchar](4000) NULL,
	[C_FDD_Q_61Destination_3_Type] [varchar](4000) NULL,
	[C_FDD_Q_62Destination_3] [varchar](4000) NULL,
	[C_FDD_Q_64Date_of_Arrival] [varchar](4000) NULL,
	[C_FDD_Q_65Date_of_Departure] [varchar](4000) NULL,
	[C_FDD_Q_4What_type_of_day_car] [varchar](4000) NULL,
	[C_FDD_Q_7Does_this_facility_c] [varchar](4000) NULL,
	[C_FDD_Q_10Where_was_patient_a] [varchar](4000) NULL,
	[C_FDD_Q_243If_Other__please_s] [varchar](4000) NULL,
	[C_FDD_Q_23If_Other__specify_o] [varchar](4000) NULL,
	[C_FDD_Q_92If_Other__specify_o] [varchar](4000) NULL,
	[C_FDD_Q_607Was_the_isolate_id] [varchar](4000) NULL,
	[C_FDD_Q_6Is_food_prepared_at_] [varchar](4000) NULL,
	[C_FDD_Q_36Was_the_seafood_eat] [varchar](4000) NULL,
	[C_FDD_Q_37Was_the_seafood_eat] [varchar](4000) NULL,
	[C_FDD_Q_38If_Yes__type_of_raw] [varchar](4000) NULL,
	[C_FDD_Q_41Date_raw_seafood_co] [varchar](4000) NULL,
	[C_FDD_Q_43Where_was_raw_seafo] [varchar](4000) NULL,
	[C_FDD_Q_45If_the_type_of_raw_] [varchar](4000) NULL,
	[C_FDD_Q_235If_Diabetes_Mellit] [varchar](4000) NULL,
	[C_FDD_Q_600What_was_the_EIA_r] [varchar](4000) NULL,
	[C_FDD_Q_605Did_patient_have_H] [varchar](4000) NULL,
	[C_FDD_Q_296If_Other_Reptile__] [varchar](4000) NULL,
	[C_FDD_Q_293Destination_2] [varchar](4000) NULL,
	[C_FDD_Q_294Destination_3] [varchar](4000) NULL,
	[C_FDD_Q_608What_was_the_EIA_r] [varchar](4000) NULL,
	[C_FDD_Q_5What_is_the_name_of_] [varchar](4000) NULL,
	[C_FDD_Q_601What_was_the_EIA_r] [varchar](4000) NULL,
	[C_FDD_Q_9What_was_last_date_w] [varchar](4000) NULL,
	[C_FDD_Q_610What_was_the_EIA_r] [varchar](4000) NULL,
	[C_FDD_Q_28If_Swimming_Pool__p] [varchar](4000) NULL,
	[C_FDD_Q_27If_Other__please_sp] [varchar](4000) NULL,
	[C_FDD_Q_29If_Other__please_sp] [varchar](4000) NULL,
	[C_FDD_Q_97Is_this_a_pregnancy] [varchar](4000) NULL,
	[C_FDD_Q_602What_was_the_PCR_r] [varchar](4000) NULL,
	[C_FDD_Q_240If_Other_Liver_Dis] [varchar](4000) NULL,
	[C_FDD_Q_236If_Organ_Transplan] [varchar](4000) NULL,
	[C_FDD_Q_242If_Other_Renal_Dis] [varchar](4000) NULL,
	[C_FDD_Q_609What_was_the_PCR_r] [varchar](4000) NULL,
	[C_FDD_Q_295If_Other_Amphibian] [varchar](4000) NULL,
	[C_FDD_Q_241If_Other_Malignanc] [varchar](4000) NULL,
	[C_FDD_Q_238If_Hematologic_Dis] [varchar](4000) NULL,
	[C_FDD_Q_194Did_the_patient_re] [varchar](4000) NULL,
	[C_FDD_Q_237If_Gastric_Surgery] [varchar](4000) NULL,
	[C_FDD_Q_613What_was_the_speci] [varchar](4000) NULL,
	[C_FDD_Q_614What_was_the_speci] [varchar](4000) NULL,
	[L_10941394Lost_to_follow_up] [varchar](4000) NULL,
	[C_FDD_Q_160Did_the_patient_ha] [varchar](4000) NULL,
	[C_FDD_Q_161If_Yes__please_spe] [varchar](4000) NULL,
	[C_FDD_Q_162Did_patient_experi] [varchar](4000) NULL,
	[C_FDD_Q_167Did_patient_have_a] [varchar](4000) NULL,
	[C_FDD_Q_170Did_the_patient_ha] [varchar](4000) NULL,
	[C_FDD_Q_172Was_the_case_confi] [varchar](4000) NULL,
	[C_FDD_Q_173Was_the_patient_tr] [varchar](4000) NULL,
	[C_FDD_Q_174Does_the_patient_h] [varchar](4000) NULL,
	[C_FDD_Q_611What_was_the_PCR_r] [varchar](4000) NULL,
	[L_10964800Onset_date_could_no] [varchar](4000) NULL,
	[C_FDD_Q_612What_was_the_PCR_r] [varchar](4000) NULL,
	[C_FDD_Q_131Did_patient_have_E] [varchar](4000) NULL,
	[C_FDD_Q_134Did_patient_have_a] [varchar](4000) NULL,
	[C_FDD_Q_137Did_patient_have_a] [varchar](4000) NULL,
	[C_FDD_Q_139What_suspect_foods] [varchar](4000) NULL,
	[C_FDD_Q_140Please_specify_typ] [varchar](4000) NULL,
	[C_FDD_Q_141If_Other__please_s] [varchar](4000) NULL,
	[C_FDD_Q_142Date_suspect_food_] [varchar](4000) NULL,
	[C_FDD_Q_143Was_larvae_found_i] [varchar](4000) NULL,
	[C_FDD_Q_144Where_was_the_susp] [varchar](4000) NULL,
	[C_FDD_Q_145If_Other__please_s] [varchar](4000) NULL,
	[C_FDD_Q_146How_was_suspect_fo] [varchar](4000) NULL,
	[C_FDD_Q_148What_was_the_metho] [varchar](4000) NULL,
	[C_FDD_Q_94If_Private_Well__ho] [varchar](4000) NULL,
	[L_10959090Control_measures_im] [varchar](4000) NULL,
	[C_FDD_Q_63Mode_of_Travel] [varchar](4000) NULL,
	[C_FDD_Q_603What_was_the_PCR_r] [varchar](4000) NULL,
	[C_FDD_Q_606What_was_the_PCR_r] [varchar](4000) NULL,
	[C_FDD_Q_197Did_the_patient_ha] [varchar](4000) NULL,
	[C_FDD_Q_198If_Yes__please_spe] [varchar](4000) NULL,
	[C_FDD_Q_199Did_patient_have_a] [varchar](4000) NULL,
	[C_FDD_Q_202Did_the_patient_ha] [varchar](4000) NULL,
	[C_FDD_Q_205Did_the_patient_ha] [varchar](4000) NULL,
	[C_FDD_Q_208Did_patient_have_a] [varchar](4000) NULL,
	[C_FDD_Q_210Did_the_patient_ha] [varchar](4000) NULL,
	[C_FDD_Q_213Were_other_organis] [varchar](4000) NULL,
	[C_FDD_Q_214Did_the_patient_ta] [varchar](4000) NULL,
	[C_FDD_Q_215In_the_7_days_befo] [varchar](4000) NULL,
	[C_FDD_Q_196Time_of_onset_of_i] [varchar](4000) NULL,
	[C_FDD_Q_283Specify_AM_PM] [varchar](4000) NULL,
	[C_FDD_Q_184Did_patient_attend] [varchar](4000) NULL,
	[C_FDD_Q_187Did_patient_eat_at] [varchar](4000) NULL,
	[C_FDD_Q_209If_Other__please_s] [varchar](4000) NULL,
	[C_FDD_Q_216If_patient_was_exp] [varchar](4000) NULL,
	[C_FDD_Q_217If_patient_s_skin_] [varchar](4000) NULL,
	[C_FDD_Q_219In_the_7_days_prio] [varchar](4000) NULL,
	[C_FDD_Q_221If_patient_was_exp] [varchar](4000) NULL,
	[C_FDD_Q_224If_skin_was_expose] [varchar](4000) NULL,
	[C_10228530If_Fruit__other_tha] [varchar](4000) NULL,
	[C_FDD_Q_164Specify_how_much_w] [varchar](4000) NULL,
	[C_FDD_Q_166Specify_lbs_kg] [varchar](4000) NULL,
	[C_FDD_Q_168If_Yes__please_spe] [varchar](4000) NULL,
	[C_FDD_Q_169Specify_Fahrenheit] [varchar](4000) NULL,
	[C_FDD_Q_176What_fresh_berries] [varchar](4000) NULL,
	[C_FDD_Q_178What_fresh_herbs_w] [varchar](4000) NULL,
	[C_FDD_Q_180What_fresh_lettuce] [varchar](4000) NULL,
	[C_FDD_Q_181If_Other__please_s] [varchar](4000) NULL,
	[C_FDD_Q_182What_other_types_o] [varchar](4000) NULL,
	[C_FDD_Q_163If_YES__please_spe] [varchar](4000) NULL,
	[C_FDD_Q_165Specify_lbs_kg] [varchar](4000) NULL,
	[C_FDD_Q_200If_Yes__please_spe] [varchar](4000) NULL,
	[C_FDD_Q_201Specify_Fahrenheit] [varchar](4000) NULL,
	[C_FDD_Q_211If_Other__please_s] [varchar](4000) NULL,
	[C_FDD_Q_212Was_the_patient_re] [varchar](4000) NULL,
	[C_FDD_Q_230Has_patient_ever_r] [varchar](4000) NULL,
	[C_FDD_Q_195Was_the_case_trace] [varchar](4000) NULL,
	[C_FDD_Q_192If_Other__please_s] [varchar](4000) NULL,
	[C_FDD_Q_40If_Other_Fish__spec] [varchar](4000) NULL,
	[C_FDD_Q_44If_Other__specify_o] [varchar](4000) NULL,
	[C_FDD_Q_183If_Other__please_s] [varchar](4000) NULL,
	[C_FDD_Q_171If_Other__please_s] [varchar](4000) NULL,
	[C_FDD_Q_188If_Yes__please_spe] [varchar](4000) NULL,
	[C_FDD_Q_630Did_the_patient_ha] [varchar](4000) NULL,
	[C_FDD_Q_631Did_the_patient_ha] [varchar](4000) NULL,
	[C_FDD_Q_632Did_the_patient_ha] [varchar](4000) NULL,
	[C_FDD_Q_257FoodNet_Case] [varchar](4000) NULL,
	[C_FDD_Q_400Was_there_a_second] [varchar](4000) NULL,
	[C_FDD_Q_401Admission_Date] [varchar](4000) NULL,
	[C_FDD_Q_402Discharge_Date] [varchar](4000) NULL,
	[C_FDD_Q_403Did_patient_immigr] [varchar](4000) NULL,
	[C_FDD_Q_404Type_of_Outbreak] [varchar](4000) NULL,
	[C_FDD_Q_604Was_the_case_inter] [varchar](4000) NULL,
	[C_FDD_Q_80Was_patient_transfe] [varchar](4000) NULL,
	[C_FDD_Q_297Specify_AM_PM] [varchar](4000) NULL,
	[C_FDD_Q_42Time_raw_seafood_co] [varchar](4000) NULL,
	[C_FDD_Q_101Outcome_of_pregnan] [varchar](4000) NULL,
	[C_FDD_Q_104Comments_on_pregna] [varchar](4000) NULL,
	[C_FDD_Q_98Type_of_infection_i] [varchar](4000) NULL,
	[C_11335458If_Fruit__other_tha] [varchar](4000) NULL,
	[C_FDD_Q_86In_case_control_stu] [varchar](4000) NULL,
	[C_FDD_Q_89Was_case_found_duri] [varchar](4000) NULL,
	[C_FDD_Q_185If_Yes__please_spe] [varchar](4000) NULL,
	[C_FDD_Q_186Date_of_event] [varchar](4000) NULL,
	[C_FDD_Q_100Comments_on_infect] [varchar](4000) NULL,
	[C_FDD_Q_103If_delivered__date] [varchar](4000) NULL,
	[C_FDD_Q_106Source_of_specimen] [varchar](4000) NULL,
	[C_FDD_Q_108Fetus_neonate_spec] [varchar](4000) NULL,
	[C_FDD_Q_109Type_of_infection_] [varchar](4000) NULL,
	[C_FDD_Q_110If_Other__please_s] [varchar](4000) NULL,
	[C_FDD_Q_282Confirmed_Listeria] [varchar](4000) NULL,
	[C_FDD_Q_177If_Other__please_s] [varchar](4000) NULL,
	[C_FDD_Q_193Was_antibiotic_sen] [varchar](4000) NULL,
	[C_FDD_Q_39If_Other_Shellfish_] [varchar](4000) NULL,
	[C_FDD_Q_81If_Yes__specify_nam] [varchar](4000) NULL,
	[L_32752295Has_the_case_consum] [varchar](4000) NULL,
	[L_32752317In_the_14_days_befo] [varchar](4000) NULL,
	[L_32752312If_yes__was_the_raw] [varchar](4000) NULL,
	[L_32752335Has_the_case_consum] [varchar](4000) NULL,
	[L_32752341In_the_14_days_befo] [varchar](4000) NULL,
	[L_32752594Has_the_case_consum] [varchar](4000) NULL,
	[L_32752595If_yes__was_the_raw] [varchar](4000) NULL,
	[L_32752597What_date_was_the_r] [varchar](4000) NULL,
	[L_32782214Has_the_case_consum] [varchar](4000) NULL,
	[L_32782215If_yes__was_the_raw] [varchar](4000) NULL,
	[L_32782220In_the_14_days_befo] [varchar](4000) NULL,
	[L_32782217What_date_was_the_r] [varchar](4000) NULL,
	[L_32752336If_yes__was_the_raw] [varchar](4000) NULL,
	[L_32752314What_date_was_the_r] [varchar](4000) NULL,
	[C_FDD_Q_113If_food_is_known_o] [varchar](4000) NULL,
	[C_FDD_Q_114Was_botulism_labor] [varchar](4000) NULL,
	[C_FDD_Q_115Was_C_botulinum_is] [varchar](4000) NULL,
	[C_FDD_Q_116Was_food_tested_] [varchar](4000) NULL,
	[C_FDD_Q_117Was_food_positive_] [varchar](4000) NULL,
	[L_32752448Has_the_case_consum] [varchar](4000) NULL,
	[L_32752449If_yes__was_the_raw] [varchar](4000) NULL,
	[L_32752454In_the_14_days_befo] [varchar](4000) NULL,
	[L_32752338What_date_was_the_r] [varchar](4000) NULL,
	[C_FDD_Q_226If_patient_was_inf] [varchar](4000) NULL,
	[L_32752313If_yes__what_is_the] [varchar](4000) NULL,
	[L_32752596If_yes__what_is_the] [varchar](4000) NULL,
	[L_32752598What_date_was_the_r] [varchar](4000) NULL,
	[L_32752337If_yes__what_is_the] [varchar](4000) NULL,
	[L_32782216If_yes__what_is_the] [varchar](4000) NULL,
	[L_32782218What_date_was_the_r] [varchar](4000) NULL,
	[L_32782221If_yes__what_was_th] [varchar](4000) NULL,
	[L_32782222If_yes__what_date_w] [varchar](4000) NULL,
	[L_32752325If_yes__what_was_th] [varchar](4000) NULL,
	[L_32752326If_yes__what_date_w] [varchar](4000) NULL,
	[L_32752342If_yes__what_was_th] [varchar](4000) NULL,
	[L_32752343If_yes__what_date_w] [varchar](4000) NULL,
	[L_32752339What_date_was_the_r] [varchar](4000) NULL,
	[L_32752340If_no__where_was_th] [varchar](4000) NULL,
	[L_32752316If_no__where_was_th] [varchar](4000) NULL,
	[L_32752315What_date_was_the_r] [varchar](4000) NULL,
	[L_32752599If_no__where_was_th] [varchar](4000) NULL,
	[L_32782219If_no__where_was_th] [varchar](4000) NULL,
	[L_32752603If_yes__what_is_the] [varchar](4000) NULL,
	[L_32752605What_date_was_the_r] [varchar](4000) NULL,
	[L_32752601Has_the_case_consum] [varchar](4000) NULL,
	[L_32752602If_yes__was_the_raw] [varchar](4000) NULL,
	[C_FDD_Q_150Please_specify_typ] [varchar](4000) NULL,
	[C_FDD_Q_151If_Other__please_s] [varchar](4000) NULL,
	[C_FDD_Q_152Date_suspect_food_] [varchar](4000) NULL,
	[C_FDD_Q_153Was_larvae_found_i] [varchar](4000) NULL,
	[C_FDD_Q_154Where_was_the_susp] [varchar](4000) NULL,
	[C_FDD_Q_155If_Other__please_s] [varchar](4000) NULL,
	[C_FDD_Q_156How_was_suspect_fo] [varchar](4000) NULL,
	[C_FDD_Q_158What_was_the_metho] [varchar](4000) NULL,
	[L_32752450If_yes__what_is_the] [varchar](4000) NULL,
	[C_FDD_Q_111Comments_on_infect] [varchar](4000) NULL,
	[C_FDD_Q_227If_Other__please_s] [varchar](4000) NULL
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[LDF_GROUP]    Script Date: 3/18/2025 5:42:18 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[LDF_GROUP](
	[LDF_GROUP_KEY] [bigint] NOT NULL,
	[BUSINESS_OBJECT_UID] [bigint] NULL,
PRIMARY KEY CLUSTERED 
(
	[LDF_GROUP_KEY] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[MEASLES_CASE]    Script Date: 3/18/2025 5:42:18 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[MEASLES_CASE](
	[INV_RPT_DT_KEY] [bigint] NOT NULL,
	[INV_ASSIGNED_DT_KEY] [bigint] NOT NULL,
	[INVESTIGATOR_KEY] [bigint] NOT NULL,
	[PATIENT_KEY] [bigint] NOT NULL,
	[PHYSICIAN_KEY] [bigint] NOT NULL,
	[RPT_SRC_ORG_KEY] [bigint] NOT NULL,
	[REPORTER_KEY] [bigint] NOT NULL,
	[PATIENT_HAVE_RASH_IND] [varchar](50) NULL,
	[RASH_ONSET_DT] [datetime] NULL,
	[RASH_LAST_DAY_NBR] [numeric](18, 0) NULL,
	[PATIENT_HAVE_FEVER_IND] [varchar](50) NULL,
	[RASH_GENERALIZED_IND] [varchar](50) NULL,
	[HIGHEST_TEMPERATURE_MEASURED] [numeric](18, 0) NULL,
	[HIGHEST_TEMPERATURE_UNIT] [varchar](50) NULL,
	[COUGH_IND] [varchar](50) NULL,
	[CROUP_IND] [varchar](50) NULL,
	[CORYZA_IND] [varchar](50) NULL,
	[CONJUNCTIVITIS_IND] [varchar](50) NULL,
	[OTTIS_MEDIA_IND] [varchar](50) NULL,
	[DIARRHEA_IND] [varchar](50) NULL,
	[PNEUMONIA_IND] [varchar](50) NULL,
	[ENCEPHALITIS_IND] [varchar](50) NULL,
	[THROMBOCYTOPENIA_IND] [varchar](50) NULL,
	[OTHER_COMPLICATION_IND] [varchar](50) NULL,
	[OTHER_COMPLICATIONS] [varchar](2000) NULL,
	[IGM_SPECIMEN_TAKEN_DT] [datetime] NULL,
	[IGM_TEST_RESULT] [varchar](50) NULL,
	[IGG_ACUTE_SPECIMEN_TAKEN_DT] [datetime] NULL,
	[IGG_CONVALESCENT_SPECIMEN_DT] [datetime] NULL,
	[IGG_TEST_RESULT] [varchar](50) NULL,
	[OTHER_LAB_TEST_DONE_IND] [varchar](50) NULL,
	[OTHER_LAB_TEST_DESC] [varchar](2000) NULL,
	[OTHER_LAB_TEST_DT] [datetime] NULL,
	[OTHER_LAB_TEST_RESULT] [varchar](2000) NULL,
	[SPECIMEN_TO_CDC_GENOTYPING_IND] [varchar](50) NULL,
	[CONTAIN_MEASLES_VACC_RECEIVD] [varchar](50) NULL,
	[NO_MEASLES_VACC_REASON] [varchar](50) NULL,
	[NBR_DOSE_RECEIVED_PRIOR_1BDAY] [numeric](18, 0) NULL,
	[NBR_DOSE_RECEIVED_SINCE_1BDAY] [numeric](18, 0) NULL,
	[TRANSMISSION_SETTING] [varchar](50) NULL,
	[AGE_AND_SETTING_VERIFIED_IND] [varchar](50) NULL,
	[PATIENT_RESIDE_IN_USA_IND] [varchar](50) NULL,
	[EPI_LINKED_TOANOTHER_CASE_IND] [varchar](50) NULL,
	[FEVER_ONSET_DT] [datetime] NULL,
	[GENOTYPING_DT] [datetime] NULL,
	[IGM_TESTING_PERFORMED_IND] [varchar](50) NULL,
	[IGG_TESTING_PERFORMED_IND] [varchar](50) NULL,
	[RASH_OCCUR_IN_18DAYS_ENTER_USA] [varchar](50) NULL,
	[SRC_OF_INFECTION] [varchar](2000) NULL,
	[MEASLES_SPECIMEN_TYPE] [varchar](2000) NULL,
	[BEFORE_B_DAY_VACCINE_REASON] [varchar](50) NULL,
	[AFTER_B_DAY_VACCINE_REASON] [varchar](50) NULL,
	[ADT_HSPTL_KEY] [bigint] NOT NULL,
	[INVESTIGATION_KEY] [bigint] NOT NULL,
	[LAB_MEASLES_TEST_DONE_IND] [varchar](50) NULL,
	[CONDITION_KEY] [bigint] NOT NULL,
	[LDF_GROUP_KEY] [bigint] NOT NULL,
	[HEPATITIS_IND] [varchar](50) NULL,
	[CASE_TRACEABLE_IND] [varchar](50) NULL,
	[GENOTYPE_SEQUENCED_MEASLES] [varchar](50) NULL,
	[GENOTYPE_ID_MEASLES] [varchar](50) NULL,
	[GENOTYPE_OTHER_ID_MEASLES] [varchar](50) NULL,
	[GEOCODING_LOCATION_KEY] [bigint] NULL,
PRIMARY KEY CLUSTERED 
(
	[INV_RPT_DT_KEY] ASC,
	[INV_ASSIGNED_DT_KEY] ASC,
	[INVESTIGATOR_KEY] ASC,
	[PATIENT_KEY] ASC,
	[PHYSICIAN_KEY] ASC,
	[RPT_SRC_ORG_KEY] ASC,
	[REPORTER_KEY] ASC,
	[ADT_HSPTL_KEY] ASC,
	[INVESTIGATION_KEY] ASC,
	[CONDITION_KEY] ASC,
	[LDF_GROUP_KEY] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[NOTIFICATION]    Script Date: 3/18/2025 5:42:19 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[NOTIFICATION](
	[NOTIFICATION_STATUS] [varchar](20) NULL,
	[NOTIFICATION_COMMENTS] [varchar](1000) NULL,
	[NOTIFICATION_KEY] [bigint] NOT NULL,
	[NOTIFICATION_LOCAL_ID] [varchar](50) NULL,
	[NOTIFICATION_SUBMITTED_BY] [numeric](18, 0) NULL,
	[NOTIFICATION_LAST_CHANGE_TIME] [datetime] NULL,
PRIMARY KEY CLUSTERED 
(
	[NOTIFICATION_KEY] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[NOTIFICATION_EVENT]    Script Date: 3/18/2025 5:42:19 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[NOTIFICATION_EVENT](
	[PATIENT_KEY] [bigint] NOT NULL,
	[NOTIFICATION_SENT_DT_KEY] [bigint] NOT NULL,
	[NOTIFICATION_SUBMIT_DT_KEY] [bigint] NOT NULL,
	[NOTIFICATION_UPD_DT_KEY] [bigint] NOT NULL,
	[NOTIFICATION_KEY] [bigint] NOT NULL,
	[COUNT] [numeric](18, 0) NULL,
	[INVESTIGATION_KEY] [bigint] NOT NULL,
	[CONDITION_KEY] [bigint] NOT NULL,
PRIMARY KEY CLUSTERED 
(
	[PATIENT_KEY] ASC,
	[NOTIFICATION_SENT_DT_KEY] ASC,
	[NOTIFICATION_SUBMIT_DT_KEY] ASC,
	[INVESTIGATION_KEY] ASC,
	[CONDITION_KEY] ASC,
	[NOTIFICATION_KEY] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[PERTUSSIS_CASE]    Script Date: 3/18/2025 5:42:19 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[PERTUSSIS_CASE](
	[RPT_SRC_ORG_KEY] [bigint] NOT NULL,
	[REPORTER_KEY] [bigint] NOT NULL,
	[PHYSICIAN_KEY] [bigint] NOT NULL,
	[INVESTIGATOR_KEY] [bigint] NOT NULL,
	[INV_ASSIGNED_DT_KEY] [bigint] NOT NULL,
	[PATIENT_KEY] [bigint] NOT NULL,
	[COUGH_ONSET_DT] [datetime] NULL,
	[PAROXYSMAL_COUGH_IND] [varchar](50) NULL,
	[WHOOP_IND] [varchar](50) NULL,
	[POST_TUSSIVE_VOMITING_IND] [varchar](50) NULL,
	[COUGH_IND] [varchar](50) NULL,
	[APNEA_IND] [varchar](50) NULL,
	[FINAL_INTERVIEW_DT] [datetime] NULL,
	[COUGH_AT_FINAL_INTERVIEW_IND] [varchar](50) NULL,
	[COUGH_DURATION_DAYS] [numeric](18, 0) NULL,
	[PNEUMONIA_XRAY_RESULT] [varchar](50) NULL,
	[GENERIZED_FOCAL_SEIZURE_IND] [varchar](50) NULL,
	[ACUTE_ENCEPHALOPATHY_IND] [varchar](50) NULL,
	[ANTIBIOTICS_GIVEN_IND] [varchar](50) NULL,
	[LAB_TESTING_DONE_IND] [varchar](50) NULL,
	[CULTURE_DT] [datetime] NULL,
	[BORDETELLA_PRT_CULTURE_RESULT] [varchar](50) NULL,
	[SEROLOGY_1_DT] [datetime] NULL,
	[SEROLOGY_1_RESULT] [varchar](50) NULL,
	[SEROLOGY_2_DT] [datetime] NULL,
	[SEROLOGY_2_RESULT] [varchar](50) NULL,
	[PCR_SPECIMEN_DT] [datetime] NULL,
	[PCR_RESULT] [varchar](50) NULL,
	[EVER_RECEIVED_VACCINE_IND] [varchar](50) NULL,
	[BEFORE_ILLNESS_LAST_VACCINE_DT] [datetime] NULL,
	[VACCINE_GIVEN_DOSE_NBR] [varchar](50) NULL,
	[LESS_THAN_3_DOSES_REASON] [varchar](50) NULL,
	[EPI_LINK_TO_OTHER_CASE_IND] [varchar](50) NULL,
	[EPI_LINKED_TO_CASE_ID] [varchar](50) NULL,
	[TRANSMISSION_SETTING] [varchar](20) NULL,
	[SPREAD_BEYOND_XMISSION_SETTING] [varchar](50) NULL,
	[SPREAD_SETTING_OUTSIDE_HOUSE] [varchar](50) NULL,
	[SPREAD_SETTING_OUT_HOUSE_OTHER] [varchar](2000) NULL,
	[ONE_OR_MORE_SUSPECT_SRC_IND] [varchar](50) NULL,
	[SUSPECT_INFECTION_SRC_NBR] [numeric](18, 0) NULL,
	[CONTACT_TO_RECEIVE_PROPHYLAXIS] [numeric](18, 0) NULL,
	[OTHER_LAB_TEST_DONE_IND] [varchar](50) NULL,
	[OTHER_LAB_TEST_DESC] [varchar](2000) NULL,
	[OTHER_LAB_TEST_DT] [datetime] NULL,
	[OTHER_LAB_TEST_RESULT] [varchar](2000) NULL,
	[BORDETELLA_CULTURE_TAKEN_IND] [varchar](50) NULL,
	[BORDETELLA_SEROLOGY_1_DONE_IND] [varchar](50) NULL,
	[BORDETELLA_SEROLOGY_2_DONE_IND] [varchar](50) NULL,
	[BORDETELLA_PCR_TAKEN_IND] [varchar](50) NULL,
	[SPECIMEN_TO_CDC_GENOTYPING_IND] [varchar](50) NULL,
	[SPECIMEN_TO_CDC_GENOTYPING_DT] [datetime] NULL,
	[SPECIMENTO_CDC_GENOTYPING_TYPE] [varchar](2000) NULL,
	[NOT_PERTUSSIS_VACCINED_REASON] [varchar](50) NULL,
	[DOSES_NBR_2WEEKS_BEFORE_SICK] [varchar](50) NULL,
	[ADT_HSPTL_KEY] [bigint] NOT NULL,
	[INVESTIGATION_KEY] [bigint] NOT NULL,
	[CONDITION_KEY] [bigint] NOT NULL,
	[SEROLOGY_LAB1_NM] [varchar](2000) NULL,
	[SEROLOGY_LAB2_NM] [varchar](2000) NULL,
	[PCR_LAB] [varchar](50) NULL,
	[PCR_LAB_NM] [varchar](2000) NULL,
	[PERTUSSIS_SUSPECT_SRC_GRP_KEY] [bigint] NOT NULL,
	[PERTUSSIS_TREATMENT_GRP_KEY] [bigint] NOT NULL,
	[SEROLOGY_LAB1] [varchar](50) NULL,
	[SEROLOGY_LAB2] [varchar](50) NULL,
	[LDF_GROUP_KEY] [bigint] NOT NULL,
	[BIRTH_WEIGHT_IN_GRAMS] [numeric](18, 0) NULL,
	[BIRTH_WEIGHT_POUNDS] [numeric](18, 0) NULL,
	[BIRTH_WEIGHT_OUNCES] [numeric](18, 0) NULL,
	[BIRTH_WEIGHT_UNKNOWN] [varchar](50) NULL,
	[MOTHERS_AGE] [numeric](18, 0) NULL,
	[PATIENT_LESS_THAN_12MONTHS] [varchar](50) NULL,
	[GEOCODING_LOCATION_KEY] [bigint] NULL,
PRIMARY KEY CLUSTERED 
(
	[RPT_SRC_ORG_KEY] ASC,
	[REPORTER_KEY] ASC,
	[PHYSICIAN_KEY] ASC,
	[INVESTIGATOR_KEY] ASC,
	[INV_ASSIGNED_DT_KEY] ASC,
	[PATIENT_KEY] ASC,
	[ADT_HSPTL_KEY] ASC,
	[INVESTIGATION_KEY] ASC,
	[CONDITION_KEY] ASC,
	[PERTUSSIS_SUSPECT_SRC_GRP_KEY] ASC,
	[PERTUSSIS_TREATMENT_GRP_KEY] ASC,
	[LDF_GROUP_KEY] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[PERTUSSIS_SUSPECTED_SOURCE_FLD]    Script Date: 3/18/2025 5:42:19 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[PERTUSSIS_SUSPECTED_SOURCE_FLD](
	[SOURCE_AGE] [numeric](18, 0) NULL,
	[SOURCE_AGE_UNIT] [varchar](50) NULL,
	[SOURCE_GENDER] [varchar](50) NULL,
	[SOURCE_RELATION] [varchar](50) NULL,
	[SOURCE_RELATION_OTHER] [varchar](2000) NULL,
	[SOURCE_VACC_DOSE_NUMBER] [varchar](50) NULL,
	[SOURCE_COUGH_ONSET_DT] [datetime] NULL,
	[PERTUSSIS_SUSPECT_SRC_FLD_KEY] [bigint] NOT NULL,
	[PERTUSSIS_SUSPECT_SRC_GRP_KEY] [bigint] NOT NULL,
PRIMARY KEY CLUSTERED 
(
	[PERTUSSIS_SUSPECT_SRC_FLD_KEY] ASC,
	[PERTUSSIS_SUSPECT_SRC_GRP_KEY] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[PERTUSSIS_SUSPECTED_SOURCE_GRP]    Script Date: 3/18/2025 5:42:19 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[PERTUSSIS_SUSPECTED_SOURCE_GRP](
	[PERTUSSIS_SUSPECT_SRC_GRP_KEY] [bigint] NOT NULL,
PRIMARY KEY CLUSTERED 
(
	[PERTUSSIS_SUSPECT_SRC_GRP_KEY] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[PERTUSSIS_TREATMENT_FIELD]    Script Date: 3/18/2025 5:42:19 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[PERTUSSIS_TREATMENT_FIELD](
	[ANTIBIOTICS_RECEIVED_DESC] [varchar](50) NULL,
	[PERTUSSIS_TREATMENT_FLD_KEY] [bigint] NOT NULL,
	[ANTIBIOTICS_START_DT] [datetime] NULL,
	[ANTIBIOTICS_TAKEN_DAY_NBR] [numeric](4, 0) NULL,
	[PERTUSSIS_TREATMENT_GRP_KEY] [bigint] NOT NULL,
PRIMARY KEY CLUSTERED 
(
	[PERTUSSIS_TREATMENT_FLD_KEY] ASC,
	[PERTUSSIS_TREATMENT_GRP_KEY] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[PERTUSSIS_TREATMENT_GROUP]    Script Date: 3/18/2025 5:42:19 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[PERTUSSIS_TREATMENT_GROUP](
	[PERTUSSIS_TREATMENT_GRP_KEY] [bigint] NOT NULL,
PRIMARY KEY CLUSTERED 
(
	[PERTUSSIS_TREATMENT_GRP_KEY] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[RDB_DATE]    Script Date: 3/18/2025 5:42:19 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[RDB_DATE](
	[DATE_MM_DD_YYYY] [datetime] NULL,
	[DAY_OF_WEEK] [varchar](10) NULL,
	[DAY_NBR_IN_CLNDR_MON] [numeric](4, 0) NULL,
	[DAY_NBR_IN_CLNDR_YR] [numeric](4, 0) NULL,
	[WK_NBR_IN_CLNDR_MON] [numeric](4, 0) NULL,
	[WK_NBR_IN_CLNDR_YR] [numeric](4, 0) NULL,
	[CLNDR_MON_NAME] [varchar](20) NULL,
	[CLNDR_MON_IN_YR] [numeric](4, 0) NULL,
	[CLNDR_QRTR] [numeric](4, 0) NULL,
	[CLNDR_YR] [numeric](18, 0) NULL,
	[DATE_KEY] [bigint] NOT NULL,
PRIMARY KEY CLUSTERED 
(
	[DATE_KEY] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[RUBELLA_CASE]    Script Date: 3/18/2025 5:42:19 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[RUBELLA_CASE](
	[INVESTIGATOR_KEY] [bigint] NOT NULL,
	[PATIENT_KEY] [bigint] NOT NULL,
	[PHYSICIAN_KEY] [bigint] NOT NULL,
	[REPORTER_KEY] [bigint] NOT NULL,
	[INV_ASSIGNED_DT_KEY] [bigint] NOT NULL,
	[LENGTH_OF_TIME_IN_US] [numeric](18, 0) NULL,
	[MACULOPAPULAR_RASH_IND] [varchar](50) NULL,
	[PATIENT_RASH_ONSET_DT] [datetime] NULL,
	[RASH_DURATION_DAYS] [numeric](18, 0) NULL,
	[PATIENT_FEVER_IND] [varchar](50) NULL,
	[HIGHEST_MEASURED_TEMPERATURE] [numeric](18, 0) NULL,
	[HIGHEST_TEMPERATUR_UNIT] [varchar](50) NULL,
	[ARTHRALGIA_ARTHRITIS_SYMPTOM] [varchar](50) NULL,
	[LYMPHADENOPATHY_IND] [varchar](50) NULL,
	[CONJUNCTIVITIS_IND] [varchar](50) NULL,
	[ENCEPHALITIS_IND] [varchar](50) NULL,
	[THROMBOCYTOPENIA_IND] [varchar](50) NULL,
	[OTHER_COMPLICATIONS_IND] [varchar](50) NULL,
	[OTHER_COMPLICATIONS_DESC] [varchar](2000) NULL,
	[CAUSE_OF_DEATH] [varchar](2000) NULL,
	[HSPTL_ADMISSION_DT] [datetime] NULL,
	[HSPTL_DISCHARGE_DT] [datetime] NULL,
	[RUBELLA_LAB_TEST_DONE_IND] [varchar](50) NULL,
	[RUBELLA_IGM_EIA_TEST_IND] [varchar](50) NULL,
	[RUBELLA_IGM_EIA_TEST_DT] [datetime] NULL,
	[RUBELLA_IGM_EIA_TEST_RESULT] [varchar](50) NULL,
	[RUBELLA_IGM_EIA_CAPTURE_IND] [varchar](50) NULL,
	[RUBELLA_IGM_EIA_CAPTURE_DT] [datetime] NULL,
	[RUBELLA_IGM_EIA_CAPTURE_RESULT] [varchar](50) NULL,
	[OTHER_RUBELLA_IGM_IND] [varchar](50) NULL,
	[OTHER_RUBELLA_IGM_DESC] [varchar](2000) NULL,
	[OTHER_RUBELLA_IGM_DT] [datetime] NULL,
	[OTHER_RUBELLA_IGM_RESULT] [varchar](50) NULL,
	[RUBELLA_IGG_EIA_ACUTE_IND] [varchar](50) NULL,
	[RUBELLA_IGG_EIA_ACUTE_DT] [datetime] NULL,
	[RUBELLA_IGG_EIA_CNVLSNT_IND] [varchar](50) NULL,
	[RUBELLA_IGG_EIA_CNVLSNT_DT] [datetime] NULL,
	[IGG_EIA_ACUTE_CNVLSNT_DIFF] [varchar](50) NULL,
	[HEMAG_INHIBIT_ACUTE_IND] [varchar](50) NULL,
	[HEMAG_INHIBIT_ACUTE_DT] [datetime] NULL,
	[HEMAG_INHIBIT_CNVLSNT_IND] [varchar](50) NULL,
	[HEMAG_INHIBIT_CNVLSNT_DT] [datetime] NULL,
	[HEMAGINHIBIT_ACUTECNVLSNT_DIFF] [varchar](50) NULL,
	[CMPLMNT_FIXATION_ACUTE_IND] [varchar](50) NULL,
	[CMPLMNT_FIXATION_ACUTE_DT] [datetime] NULL,
	[CMPLMNT_FIXATION_CNVLSNT_IND] [varchar](50) NULL,
	[CMPLMNT_FIXATION_CNVLSNT_DT] [datetime] NULL,
	[CMPLMNT_FIX_ACUTE_CNVLSNT_DIFF] [varchar](50) NULL,
	[RUBELLA_IGG_OTHER_TEST1_IND] [varchar](50) NULL,
	[RUBELLA_IGG_OTHER_TEST1_DESC] [varchar](2000) NULL,
	[RUBELLA_IGG_OTHER_TEST1_DT] [datetime] NULL,
	[RUBELLA_IGG_OTHER_TEST1_RESULT] [varchar](50) NULL,
	[RUBELLA_IGG_OTHER_TEST2_IND] [varchar](50) NULL,
	[RUBELLA_IGG_OTHER_TEST2_DESC] [varchar](2000) NULL,
	[RUBELLA_IGG_OTHER_TEST2_DT] [datetime] NULL,
	[RUBELLA_IGG_OTHER_TEST2_RESULT] [varchar](50) NULL,
	[RUBELLA_IGG_OTHER_TEST3_IND] [varchar](50) NULL,
	[RUBELLA_IGG_OTHER_TEST3_DESC] [varchar](2000) NULL,
	[RUBELLA_IGG_OTHER_TEST_3_DT] [datetime] NULL,
	[RUBELLA_IGG_OTHER_TEST3_RESULT] [varchar](50) NULL,
	[VIRUS_ISOLATION_PERFORMED_IND] [varchar](50) NULL,
	[VIRUS_ISOLATION_PERFORMED_DT] [datetime] NULL,
	[VIRUS_ISOLATION_PERFORMED_SRC] [varchar](50) NULL,
	[VIRUS_ISOLATION_OTHER_SRC] [varchar](2000) NULL,
	[RT_PCR_PERFORMED_IND] [varchar](50) NULL,
	[RT_PCR_DT] [datetime] NULL,
	[RT_PCR_SRC] [varchar](50) NULL,
	[RT_PCR_RESULT] [varchar](50) NULL,
	[RT_PCR_OTHER_SRC] [varchar](2000) NULL,
	[LATEX_AGGLUTINATION_TESTED_IND] [varchar](50) NULL,
	[LATEX_AGGLUTINATION_TESTED_DT] [datetime] NULL,
	[LATEX_AGGLUTINATION_TESTRESULT] [varchar](50) NULL,
	[IMMUNOFLUORESCENT_ASSAY_IND] [varchar](50) NULL,
	[IMMUNOFLUORESCENT_ASSAY_DT] [datetime] NULL,
	[IMMUNOFLUORESCENT_ASSAY_SRC] [varchar](50) NULL,
	[IMMUNOFLUORESCENT_ASSAY_RESULT] [varchar](50) NULL,
	[IMUNOFLRESNT_ASSAY_OTHER_SRC] [varchar](2000) NULL,
	[OTHER_RUBELLA_TEST_DONE_IND] [varchar](50) NULL,
	[OTHER_RUBELLA_TEST_DESC] [varchar](2000) NULL,
	[OTHER_RUBELLA_TEST_DT] [datetime] NULL,
	[OTHER_RUBELLA_TEST_RESULT] [varchar](2000) NULL,
	[SPECIMEN_TO_CDC_GENOTYPING_IND] [varchar](50) NULL,
	[SPECIMENTO_CDC_GENOTYPING_TYPE] [varchar](20) NULL,
	[GENOTYPING_SPECIMEN_OTHER_TYPE] [varchar](2000) NULL,
	[RUBELLA_VACCINE_RECEIVED_IND] [varchar](50) NULL,
	[RUBELLA_VACCINED_NEVER_REASON] [varchar](50) NULL,
	[ON_AFTER_1ST_DOB_DOSES_NBR] [numeric](18, 0) NULL,
	[TRANSMISSION_SETTING] [varchar](20) NULL,
	[EPI_LINKED_TO_ANOTHER_CASE_IND] [varchar](50) NULL,
	[PREGNANCY_IND] [varchar](50) NULL,
	[PREGNANCY_DELIVERY_EXPECTED_DT] [datetime] NULL,
	[EXPECTED_DELIVERY_PLACE] [varchar](2000) NULL,
	[GESTATION_WK_NBR_AT_RUBELLA] [numeric](18, 0) NULL,
	[GESTATION_TRIMESTER_ST_RUBELLA] [varchar](50) NULL,
	[PREVIOUS_RUBELLA_IMMUNITY_DOC] [varchar](50) NULL,
	[PREVIOUSIMMUNITY_TESTINGRESULT] [varchar](50) NULL,
	[PREVIOUS_IMMUNITY_TESTING_YR] [numeric](18, 0) NULL,
	[WOMAN_AGE_AT_IMMUNITY_TESTING] [numeric](18, 0) NULL,
	[RUBELLA_PRIOR_TO_PREGNANCY_IND] [varchar](50) NULL,
	[SEROLOGICALLYCONFIRMED_RUBELLA] [varchar](50) NULL,
	[PREVIOUS_DISS_YR] [numeric](18, 0) NULL,
	[PREVIOUS_DISS_AGE] [numeric](18, 0) NULL,
	[PREGNANCY_CURRENT_OUTCOME] [varchar](50) NULL,
	[LIVE_BIRTH_OUTCOME] [varchar](50) NULL,
	[NON_LIVING_BIRTH_OUTCOME] [varchar](50) NULL,
	[PREGNANCY_CESSATION_FETUS_WK] [numeric](18, 0) NULL,
	[NON_LIVING_BIRTH_AUTOPSY_STUDY] [varchar](50) NULL,
	[AUTOPSY_PATHOLOGY_STUDY_RESULT] [varchar](2000) NULL,
	[RUBELLA_GENOTYPING_DT] [datetime] NULL,
	[EIA_ACUTE_TEST_RESULT_VALUE] [varchar](2000) NULL,
	[EIA_CNVLSNT_TEST_RESULT_VALUE] [varchar](2000) NULL,
	[HEMAG_INHIBIT_ACUTE_VALUE] [varchar](2000) NULL,
	[HEMAG_INHIBIT_CNVLSNT_VALUE] [varchar](2000) NULL,
	[CMPLMNT_FIXATION_ACUTE_VALUE] [varchar](2000) NULL,
	[CMPLMNT_FIXATION_CNVLSNT_VALUE] [varchar](2000) NULL,
	[ARTHRALGIA_ARTHRITIS_COMPLICAT] [varchar](50) NULL,
	[IGM_EIA_1ST_TEST_RESULT_VALUE] [varchar](2000) NULL,
	[IGM_EIA_2ND_TEST_RESULT_VALUE] [varchar](2000) NULL,
	[OTHER_IGM_TEST_RESULT_VALUE] [varchar](2000) NULL,
	[IGG_OTHER_TEST_1_RESULT_VALUE] [varchar](2000) NULL,
	[IGG_OTHER_TEST_2_RESULT_VALUE] [varchar](2000) NULL,
	[RT_PCR_TEST_RESULT_VALUE] [varchar](2000) NULL,
	[LATEX_AGG_TEST_RESULT_VALUE] [varchar](2000) NULL,
	[ASSAY_TEST_RESULT_VALUE] [varchar](2000) NULL,
	[OTHER_RUBELLA_TESTRESULT_VALUE] [varchar](2000) NULL,
	[INFECTION_SRC] [varchar](2000) NULL,
	[RASH_ONSET_ENTERING_USA] [varchar](50) NULL,
	[IGM_EIA_1ST_METHOD_USED] [varchar](50) NULL,
	[IGM_EIA_2ND_METHOD_USED] [varchar](50) NULL,
	[IGG_OTHER_TEST_3_RESULT_VALUE] [varchar](2000) NULL,
	[ADT_HSPTL_KEY] [bigint] NOT NULL,
	[RPT_SRC_ORG_KEY] [bigint] NOT NULL,
	[INVESTIGATION_KEY] [bigint] NOT NULL,
	[CONDITION_KEY] [bigint] NOT NULL,
	[VIRUS_ISOLATION_RESULT] [varchar](50) NULL,
	[LDF_GROUP_KEY] [bigint] NOT NULL,
	[BIRTH_COUNTRY] [varchar](50) NULL,
	[RUBELLA_CASE_TRACEABLE_IND] [varchar](50) NULL,
	[GENOTYPE_SEQUENCED_RUBELLA] [varchar](50) NULL,
	[GENOTYPE_ID_RUBELLA] [varchar](50) NULL,
	[GENOTYPE_OTHER_ID_RUBELLA] [varchar](50) NULL,
	[GEOCODING_LOCATION_KEY] [bigint] NULL,
PRIMARY KEY CLUSTERED 
(
	[INVESTIGATOR_KEY] ASC,
	[PATIENT_KEY] ASC,
	[PHYSICIAN_KEY] ASC,
	[REPORTER_KEY] ASC,
	[INV_ASSIGNED_DT_KEY] ASC,
	[ADT_HSPTL_KEY] ASC,
	[RPT_SRC_ORG_KEY] ASC,
	[INVESTIGATION_KEY] ASC,
	[CONDITION_KEY] ASC,
	[LDF_GROUP_KEY] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[STD_HIV_DATAMART]    Script Date: 3/18/2025 5:42:19 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[STD_HIV_DATAMART](
	[ADI_900_STATUS] [varchar](44) NULL,
	[ADI_900_STATUS_CD] [varchar](20) NULL,
	[ADM_REFERRAL_BASIS_OOJ] [varchar](1999) NULL,
	[ADM_RPTNG_CNTY] [varchar](1999) NULL,
	[CA_INIT_INTVWR_ASSGN_DT] [datetime] NULL,
	[CA_INTERVIEWER_ASSIGN_DT] [datetime] NULL,
	[CA_PATIENT_INTV_STATUS] [varchar](29) NULL,
	[CALC_5_YEAR_AGE_GROUP] [varchar](2) NULL,
	[CASE_RPT_MMWR_WK] [numeric](18, 0) NULL,
	[CASE_RPT_MMWR_YR] [numeric](18, 0) NULL,
	[CC_CLOSED_DT] [datetime] NULL,
	[CLN_CARE_STATUS_CLOSE_DT] [varchar](1999) NULL,
	[CLN_CONDITION_RESISTANT_TO] [varchar](1999) NULL,
	[CLN_DT_INIT_HLTH_EXM] [date] NULL,
	[CLN_NEUROSYPHILLIS_IND] [varchar](1999) NULL,
	[CLN_PRE_EXP_PROPHY_IND] [varchar](1999) NULL,
	[CLN_PRE_EXP_PROPHY_REFER] [varchar](1999) NULL,
	[CLN_SURV_PROVIDER_DIAG_CD] [varchar](20) NULL,
	[CMP_CONJUNCTIVITIS_IND] [varchar](1999) NULL,
	[CMP_PID_IND] [varchar](1999) NULL,
	[COINFECTION_ID] [varchar](100) NULL,
	[CONDITION_CD] [varchar](50) NULL,
	[CONDITION_KEY] [bigint] NULL,
	[CONFIRMATION_DT] [datetime] NULL,
	[CURR_PROCESS_STATE] [varchar](100) NULL,
	[DETECTION_METHOD_DESC_TXT] [varchar](50) NULL,
	[DIAGNOSIS] [varchar](1999) NULL,
	[DIAGNOSIS_CD] [varchar](3) NULL,
	[DIE_FRM_THIS_ILLNESS_IND] [varchar](50) NULL,
	[DISEASE_IMPORTED_IND] [varchar](100) NULL,
	[DISSEMINATED_IND] [varchar](1999) NULL,
	[EPI_CNTRY_USUAL_RESID] [varchar](1999) NULL,
	[EPI_LINK_ID] [varchar](20) NULL,
	[FACILITY_FLD_FOLLOW_UP_KEY] [bigint] NULL,
	[FIELD_RECORD_NUMBER] [varchar](20) NULL,
	[FL_FUP_ACTUAL_REF_TYPE] [varchar](15) NULL,
	[FL_FUP_DISPO_DT] [datetime] NULL,
	[FL_FUP_DISPOSITION] [varchar](44) NULL,
	[FL_FUP_EXAM_DT] [datetime] NULL,
	[FL_FUP_EXPECTED_DT] [datetime] NULL,
	[FL_FUP_EXPECTED_IN_IND_CD] [varchar](1) NULL,
	[FL_FUP_INIT_ASSGN_DT] [datetime] NULL,
	[FL_FUP_INTERNET_OUTCOME_CD] [varchar](10) NULL,
	[FL_FUP_INVESTIGATOR_ASSGN_DT] [datetime] NULL,
	[FL_FUP_NOTIFICATION_PLAN] [varchar](15) NULL,
	[FL_FUP_OOJ_OUTCOME] [varchar](44) NULL,
	[FL_FUP_PROV_DIAGNOSIS_CD] [varchar](3) NULL,
	[FL_FUP_PROV_EXM_REASON] [varchar](43) NULL,
	[HIV_900_RESULT] [varchar](4000) NULL,
	[HIV_900_TEST_IND] [varchar](4000) NULL,
	[HIV_900_TEST_REFERRAL_DT] [date] NULL,
	[HIV_AV_THERAPY_EVER_IND] [varchar](4000) NULL,
	[HIV_AV_THERAPY_LAST_12MO_IND] [varchar](4000) NULL,
	[HIV_CA_900_OTH_RSN_NOT_LO] [varchar](4000) NULL,
	[HIV_CA_900_REASON_NOT_LOC] [varchar](4000) NULL,
	[HIV_ENROLL_PRTNR_SRVCS_IND] [varchar](4000) NULL,
	[HIV_KEEP_900_CARE_APPT_IND] [varchar](4000) NULL,
	[HIV_LAST_900_TEST_DT] [date] NULL,
	[HIV_POST_TEST_900_COUNSELING] [varchar](4000) NULL,
	[HIV_PREVIOUS_900_TEST_IND] [varchar](4000) NULL,
	[HIV_REFER_FOR_900_CARE_IND] [varchar](4000) NULL,
	[HIV_REFER_FOR_900_TEST] [varchar](4000) NULL,
	[HIV_RST_PROVIDED_900_RSLT_IND] [varchar](4000) NULL,
	[HIV_SELF_REPORTED_RSLT_900] [varchar](4000) NULL,
	[HIV_STATE_CASE_ID] [varchar](2000) NULL,
	[HOSPITAL_KEY] [bigint] NULL,
	[HSPTLIZD_IND] [varchar](50) NULL,
	[INVESTIGATION_KEY] [bigint] NULL,
	[INIT_FUP_CLINIC_CODE] [varchar](50) NULL,
	[INIT_FUP_CLOSED_DT] [datetime] NULL,
	[INIT_FUP_INITIAL_FOLL_UP] [varchar](22) NULL,
	[INIT_FUP_INTERNET_FOLL_UP] [varchar](20) NULL,
	[INIT_FUP_INITIAL_FOLL_UP_CD] [varchar](20) NULL,
	[INIT_FUP_INTERNET_FOLL_UP_CD] [varchar](20) NULL,
	[INIT_FUP_NOTIFIABLE] [varchar](20) NULL,
	[INITIATING_AGNCY] [varchar](20) NULL,
	[INV_ASSIGNED_DT] [datetime] NULL,
	[INV_CASE_STATUS] [varchar](50) NULL,
	[INV_CLOSE_DT] [datetime] NULL,
	[INV_LOCAL_ID] [varchar](50) NULL,
	[INV_RPT_DT] [datetime] NULL,
	[INV_START_DT] [datetime] NULL,
	[INVESTIGATION_DEATH_DATE] [datetime] NULL,
	[INVESTIGATION_STATUS] [varchar](50) NULL,
	[INVESTIGATOR_CLOSED_KEY] [bigint] NULL,
	[INVESTIGATOR_CLOSED_QC] [varchar](50) NULL,
	[INVESTIGATOR_CURRENT_KEY] [bigint] NULL,
	[INVESTIGATOR_CURRENT_QC] [varchar](50) NULL,
	[INVESTIGATOR_DISP_FL_FUP_KEY] [bigint] NULL,
	[INVESTIGATOR_DISP_FL_FUP_QC] [varchar](50) NULL,
	[INVESTIGATOR_FL_FUP_KEY] [bigint] NULL,
	[INVESTIGATOR_FL_FUP_QC] [varchar](50) NULL,
	[INVESTIGATOR_INIT_INTRVW_KEY] [bigint] NULL,
	[INVESTIGATOR_INIT_INTRVW_QC] [varchar](50) NULL,
	[INVESTIGATOR_INIT_FL_FUP_KEY] [bigint] NULL,
	[INVESTIGATOR_INIT_FL_FUP_QC] [varchar](50) NULL,
	[INVESTIGATOR_INITIAL_KEY] [bigint] NULL,
	[INVESTIGATOR_INITIAL_QC] [varchar](50) NULL,
	[INVESTIGATOR_INTERVIEW_KEY] [bigint] NULL,
	[INVESTIGATOR_INTERVIEW_QC] [varchar](50) NULL,
	[INVESTIGATOR_SUPER_CASE_KEY] [bigint] NULL,
	[INVESTIGATOR_SUPER_CASE_QC] [varchar](50) NULL,
	[INVESTIGATOR_SUPER_FL_FUP_KEY] [bigint] NULL,
	[INVESTIGATOR_SUPER_FL_FUP_QC] [varchar](50) NULL,
	[INVESTIGATOR_SURV_KEY] [bigint] NULL,
	[INVESTIGATOR_SURV_QC] [varchar](50) NULL,
	[IPO_CURRENTLY_IN_INSTITUTION] [varchar](1999) NULL,
	[IPO_LIVING_WITH] [varchar](2000) NULL,
	[IPO_NAME_OF_INSTITUTITION] [varchar](2000) NULL,
	[IPO_TIME_AT_ADDRESS_NUM] [varchar](2000) NULL,
	[IPO_TIME_AT_ADDRESS_UNIT] [varchar](1999) NULL,
	[IPO_TIME_IN_COUNTRY_NUM] [varchar](2000) NULL,
	[IPO_TIME_IN_COUNTRY_UNIT] [varchar](1999) NULL,
	[IPO_TIME_IN_STATE_NUM] [varchar](2000) NULL,
	[IPO_TIME_IN_STATE_UNIT] [varchar](1999) NULL,
	[IPO_TYPE_OF_INSTITUTITION] [varchar](1999) NULL,
	[IPO_TYPE_OF_RESIDENCE] [varchar](1999) NULL,
	[IX_DATE_OI] [datetime] NULL,
	[JURISDICTION_CD] [varchar](20) NULL,
	[JURISDICTION_NM] [varchar](100) NULL,
	[LAB_HIV_SPECIMEN_COLL_DT] [date] NULL,
	[LAB_NONTREP_SYPH_RSLT_QNT] [varchar](1999) NULL,
	[LAB_NONTREP_SYPH_RSLT_QUA] [varchar](1999) NULL,
	[LAB_NONTREP_SYPH_TEST_TYP] [varchar](1999) NULL,
	[LAB_SYPHILIS_TST_PS_IND] [varchar](1999) NULL,
	[LAB_SYPHILIS_TST_RSLT_PS] [varchar](1999) NULL,
	[LAB_TESTS_PERFORMED] [varchar](1999) NULL,
	[LAB_TREP_SYPH_RESULT_QUAL] [varchar](1999) NULL,
	[LAB_TREP_SYPH_TEST_TYPE] [varchar](1999) NULL,
	[MDH_PREV_STD_HIST] [varchar](1999) NULL,
	[OOJ_AGENCY_SENT_TO] [varchar](20) NULL,
	[OOJ_DUE_DATE_SENT_TO] [datetime] NULL,
	[OOJ_FR_NUMBER_SENT_TO] [varchar](20) NULL,
	[OOJ_INITG_AGNCY_OUTC_DUE_DATE] [datetime] NULL,
	[OOJ_INITG_AGNCY_OUTC_SNT_DATE] [datetime] NULL,
	[OOJ_INITG_AGNCY_RECD_DATE] [datetime] NULL,
	[ORDERING_FACILITY_KEY] [bigint] NULL,
	[OUTBREAK_IND] [varchar](50) NULL,
	[OUTBREAK_NAME] [varchar](100) NULL,
	[PATIENT_ADDL_GENDER_INFO] [varchar](100) NULL,
	[PATIENT_AGE_AT_ONSET] [numeric](18, 0) NULL,
	[PATIENT_AGE_AT_ONSET_UNIT] [varchar](20) NULL,
	[PATIENT_AGE_REPORTED] [varchar](33) NULL,
	[PATIENT_ALIAS] [varchar](50) NULL,
	[PATIENT_BIRTH_COUNTRY] [varchar](50) NULL,
	[PATIENT_BIRTH_SEX] [varchar](50) NULL,
	[PATIENT_CENSUS_TRACT] [varchar](100) NULL,
	[PATIENT_CITY] [varchar](50) NULL,
	[PATIENT_COUNTRY] [varchar](50) NULL,
	[PATIENT_COUNTY] [varchar](50) NULL,
	[PATIENT_CURR_SEX_UNK_RSN] [varchar](100) NULL,
	[PATIENT_CURRENT_SEX] [varchar](50) NULL,
	[PATIENT_DECEASED_DATE] [datetime] NULL,
	[PATIENT_DECEASED_INDICATOR] [varchar](50) NULL,
	[PATIENT_DOB] [datetime] NULL,
	[PATIENT_EMAIL] [varchar](100) NULL,
	[PATIENT_ETHNICITY] [varchar](50) NULL,
	[PATIENT_LOCAL_ID] [varchar](50) NULL,
	[PATIENT_MARITAL_STATUS] [varchar](50) NULL,
	[PATIENT_NAME] [varchar](153) NULL,
	[PATIENT_PHONE_CELL] [varchar](50) NULL,
	[PATIENT_PHONE_HOME] [varchar](105) NULL,
	[PATIENT_PHONE_WORK] [varchar](105) NULL,
	[PATIENT_PREFERRED_GENDER] [varchar](100) NULL,
	[PATIENT_PREGNANT_IND] [varchar](50) NULL,
	[PATIENT_RACE] [varchar](50) NULL,
	[PATIENT_SEX] [varchar](100) NULL,
	[PATIENT_STATE] [varchar](50) NULL,
	[PATIENT_STREET_ADDRESS_1] [varchar](50) NULL,
	[PATIENT_STREET_ADDRESS_2] [varchar](50) NULL,
	[PATIENT_UNK_ETHNIC_RSN] [varchar](100) NULL,
	[PATIENT_ZIP] [varchar](50) NULL,
	[PBI_IN_PRENATAL_CARE_IND] [varchar](1999) NULL,
	[PBI_PATIENT_PREGNANT_WKS] [varchar](2000) NULL,
	[PBI_PREG_AT_EXAM_IND] [varchar](1999) NULL,
	[PBI_PREG_AT_EXAM_WKS] [varchar](2000) NULL,
	[PBI_PREG_AT_IX_IND] [varchar](1999) NULL,
	[PBI_PREG_AT_IX_WKS] [varchar](2000) NULL,
	[PBI_PREG_IN_LAST_12MO_IND] [varchar](1999) NULL,
	[PBI_PREG_OUTCOME] [varchar](2000) NULL,
	[PHYSICIAN_FL_FUP_KEY] [bigint] NULL,
	[PHYSICIAN_KEY] [bigint] NULL,
	[PROGRAM_AREA_CD] [varchar](20) NULL,
	[PROGRAM_JURISDICTION_OID] [bigint] NULL,
	[REPORTING_ORG_KEY] [bigint] NULL,
	[REPORTING_PROV_KEY] [bigint] NULL,
	[RPT_ELICIT_INTERNET_INFO] [varchar](1999) NULL,
	[RPT_FIRST_NDLSHARE_EXP_DT] [date] NULL,
	[RPT_FIRST_SEX_EXP_DT] [date] NULL,
	[RPT_LAST_NDLSHARE_EXP_DT] [date] NULL,
	[PROVIDER_REASON_VISIT_DT] [date] NULL,
	[REFERRAL_BASIS] [varchar](100) NULL,
	[RPT_LAST_SEX_EXP_DT] [date] NULL,
	[RPT_MET_OP_INTERNET] [varchar](1999) NULL,
	[RPT_NDLSHARE_EXP_FREQ] [varchar](2000) NULL,
	[RPT_RELATIONSHIP_TO_OP] [varchar](1999) NULL,
	[RPT_SEX_EXP_FREQ] [varchar](2000) NULL,
	[RPT_SRC_CD_DESC] [varchar](100) NULL,
	[RPT_SPOUSE_OF_OP] [varchar](1999) NULL,
	[RSK_BEEN_INCARCERATD_12MO_IND] [varchar](1999) NULL,
	[RSK_COCAINE_USE_12MO_IND] [varchar](1999) NULL,
	[RSK_CRACK_USE_12MO_IND] [varchar](1999) NULL,
	[RSK_ED_MEDS_USE_12MO_IND] [varchar](1999) NULL,
	[RSK_HEROIN_USE_12MO_IND] [varchar](1999) NULL,
	[RSK_INJ_DRUG_USE_12MO_IND] [varchar](1999) NULL,
	[RSK_METH_USE_12MO_IND] [varchar](1999) NULL,
	[RSK_NITR_POP_USE_12MO_IND] [varchar](1999) NULL,
	[RSK_NO_DRUG_USE_12MO_IND] [varchar](1999) NULL,
	[RSK_OTHER_DRUG_SPEC] [varchar](2000) NULL,
	[RSK_OTHER_DRUG_USE_12MO_IND] [varchar](1999) NULL,
	[RSK_RISK_FACTORS_ASSESS_IND] [varchar](1999) NULL,
	[RSK_SEX_EXCH_DRGS_MNY_12MO_IND] [varchar](1999) NULL,
	[RSK_SEX_INTOXCTED_HGH_12MO_IND] [varchar](1999) NULL,
	[RSK_SEX_W_ANON_PTRNR_12MO_IND] [varchar](1999) NULL,
	[RSK_SEX_W_FEMALE_12MO_IND] [varchar](1999) NULL,
	[RSK_SEX_W_KNOWN_IDU_12MO_IND] [varchar](1999) NULL,
	[RSK_SEX_W_KNWN_MSM_12M_FML_IND] [varchar](1999) NULL,
	[RSK_SEX_W_MALE_12MO_IND] [varchar](1999) NULL,
	[RSK_SEX_W_TRANSGNDR_12MO_IND] [varchar](1999) NULL,
	[RSK_SEX_WOUT_CONDOM_12MO_IND] [varchar](1999) NULL,
	[RSK_SHARED_INJ_EQUIP_12MO_IND] [varchar](1999) NULL,
	[RSK_TARGET_POPULATIONS] [varchar](1999) NULL,
	[SOC_FEMALE_PRTNRS_12MO_IND] [varchar](1999) NULL,
	[SOC_FEMALE_PRTNRS_12MO_TTL] [varchar](2000) NULL,
	[SOC_MALE_PRTNRS_12MO_IND] [varchar](1999) NULL,
	[SOC_MALE_PRTNRS_12MO_TOTAL] [varchar](2000) NULL,
	[SOC_PLACES_TO_HAVE_SEX] [varchar](1999) NULL,
	[SOC_PLACES_TO_MEET_PARTNER] [varchar](1999) NULL,
	[SOC_PRTNRS_PRD_FML_IND] [varchar](1999) NULL,
	[SOC_PRTNRS_PRD_FML_TTL] [varchar](2000) NULL,
	[SOC_PRTNRS_PRD_MALE_IND] [varchar](1999) NULL,
	[SOC_PRTNRS_PRD_MALE_TTL] [varchar](2000) NULL,
	[SOC_PRTNRS_PRD_TRNSGNDR_IND] [varchar](1999) NULL,
	[SOC_SX_PRTNRS_INTNT_12MO_IND] [varchar](1999) NULL,
	[SOC_TRANSGNDR_PRTNRS_12MO_IND] [varchar](1999) NULL,
	[SOC_TRANSGNDR_PRTNRS_12MO_TTL] [varchar](2000) NULL,
	[SOURCE_SPREAD] [varchar](1999) NULL,
	[STD_PRTNRS_PRD_TRNSGNDR_TTL] [varchar](2000) NULL,
	[SURV_CLOSED_DT] [datetime] NULL,
	[SURV_INVESTIGATOR_ASSGN_DT] [datetime] NULL,
	[SURV_PATIENT_FOLL_UP] [varchar](22) NULL,
	[SURV_PROVIDER_CONTACT] [varchar](20) NULL,
	[SURV_PROVIDER_EXAM_REASON] [varchar](43) NULL,
	[SYM_NEUROLOGIC_SIGN_SYM] [varchar](1999) NULL,
	[SYM_OCULAR_MANIFESTATIONS] [varchar](1999) NULL,
	[SYM_OTIC_MANIFESTATION] [varchar](1999) NULL,
	[SYM_LATE_CLINICAL_MANIFES] [varchar](1999) NULL,
	[TRT_TREATMENT_DATE] [date] NULL
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[TREATMENT]    Script Date: 3/18/2025 5:42:19 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[TREATMENT](
	[TREATMENT_KEY] [bigint] NOT NULL,
	[TREATMENT_UID] [bigint] NULL,
	[TREATMENT_LOCAL_ID] [varchar](50) NULL,
	[TREATMENT_NM] [varchar](150) NULL,
	[TREATMENT_DRUG] [varchar](50) NULL,
	[TREATMENT_DOSAGE_STRENGTH] [varchar](20) NULL,
	[TREATMENT_DOSAGE_STRENGTH_UNIT] [varchar](20) NULL,
	[TREATMENT_FREQUENCY] [varchar](20) NULL,
	[TREATMENT_DURATION] [varchar](10) NULL,
	[TREATMENT_DURATION_UNIT] [varchar](20) NULL,
	[TREATMENT_COMMENTS] [varchar](1000) NULL,
	[TREATMENT_ROUTE] [varchar](25) NULL,
	[CUSTOM_TREATMENT] [varchar](100) NULL,
	[TREATMENT_SHARED_IND] [varchar](50) NULL,
	[TREATMENT_OID] [bigint] NULL,
	[RECORD_STATUS_CD] [varchar](8) NOT NULL,
PRIMARY KEY CLUSTERED 
(
	[TREATMENT_KEY] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[TREATMENT_EVENT]    Script Date: 3/18/2025 5:42:19 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[TREATMENT_EVENT](
	[TREATMENT_DT_KEY] [bigint] NOT NULL,
	[TREATMENT_PROVIDING_ORG_KEY] [bigint] NOT NULL,
	[PATIENT_KEY] [bigint] NOT NULL,
	[TREATMENT_COUNT] [numeric](18, 0) NULL,
	[TREATMENT_KEY] [bigint] NOT NULL,
	[MORB_RPT_KEY] [bigint] NOT NULL,
	[TREATMENT_PHYSICIAN_KEY] [bigint] NOT NULL,
	[INVESTIGATION_KEY] [bigint] NOT NULL,
	[CONDITION_KEY] [bigint] NOT NULL,
	[LDF_GROUP_KEY] [bigint] NOT NULL,
	[RECORD_STATUS_CD] [varchar](8) NOT NULL,
PRIMARY KEY CLUSTERED 
(
	[TREATMENT_DT_KEY] ASC,
	[TREATMENT_PROVIDING_ORG_KEY] ASC,
	[PATIENT_KEY] ASC,
	[TREATMENT_KEY] ASC,
	[MORB_RPT_KEY] ASC,
	[TREATMENT_PHYSICIAN_KEY] ASC,
	[INVESTIGATION_KEY] ASC,
	[CONDITION_KEY] ASC,
	[LDF_GROUP_KEY] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[VAR_PAM_LDF]    Script Date: 3/18/2025 5:42:19 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[VAR_PAM_LDF](
	[INVESTIGATION_KEY] [numeric](20, 0) NULL,
	[VAR_PAM_UID] [numeric](20, 0) NULL,
	[add_time] [datetime2](3) NULL,
	[CMI] [varchar](2000) NULL,
	[LTF] [varchar](2000) NULL,
	[OND] [varchar](2000) NULL
) ON [PRIMARY]
GO
ALTER TABLE [dbo].[ANTIMICROBIAL]  WITH CHECK ADD FOREIGN KEY([ANTIMICROBIAL_GRP_KEY])
REFERENCES [dbo].[ANTIMICROBIAL_GROUP] ([ANTIMICROBIAL_GRP_KEY])
GO
ALTER TABLE [dbo].[BMIRD_CASE]  WITH CHECK ADD FOREIGN KEY([ADT_HSPTL_KEY])
REFERENCES [dbo].[D_ORGANIZATION] ([ORGANIZATION_KEY])
GO
ALTER TABLE [dbo].[BMIRD_CASE]  WITH CHECK ADD FOREIGN KEY([BMIRD_MULTI_VAL_GRP_KEY])
REFERENCES [dbo].[BMIRD_MULTI_VALUE_FIELD_GROUP] ([BMIRD_MULTI_VAL_GRP_KEY])
GO
ALTER TABLE [dbo].[BMIRD_CASE]  WITH CHECK ADD FOREIGN KEY([CONDITION_KEY])
REFERENCES [dbo].[CONDITION] ([CONDITION_KEY])
GO
ALTER TABLE [dbo].[BMIRD_CASE]  WITH CHECK ADD FOREIGN KEY([DAYCARE_FACILITY_KEY])
REFERENCES [dbo].[D_ORGANIZATION] ([ORGANIZATION_KEY])
GO
ALTER TABLE [dbo].[BMIRD_CASE]  WITH CHECK ADD FOREIGN KEY([INV_ASSIGNED_DT_KEY])
REFERENCES [dbo].[RDB_DATE] ([DATE_KEY])
GO
ALTER TABLE [dbo].[BMIRD_CASE]  WITH CHECK ADD FOREIGN KEY([INVESTIGATOR_KEY])
REFERENCES [dbo].[D_PROVIDER] ([PROVIDER_KEY])
GO
ALTER TABLE [dbo].[BMIRD_CASE]  WITH CHECK ADD FOREIGN KEY([INVESTIGATION_KEY])
REFERENCES [dbo].[INVESTIGATION] ([INVESTIGATION_KEY])
GO
ALTER TABLE [dbo].[BMIRD_CASE]  WITH CHECK ADD FOREIGN KEY([LDF_GROUP_KEY])
REFERENCES [dbo].[LDF_GROUP] ([LDF_GROUP_KEY])
GO
ALTER TABLE [dbo].[BMIRD_CASE]  WITH CHECK ADD FOREIGN KEY([NURSING_HOME_KEY])
REFERENCES [dbo].[D_ORGANIZATION] ([ORGANIZATION_KEY])
GO
ALTER TABLE [dbo].[BMIRD_CASE]  WITH CHECK ADD FOREIGN KEY([PATIENT_KEY])
REFERENCES [dbo].[D_PATIENT] ([PATIENT_KEY])
GO
ALTER TABLE [dbo].[BMIRD_CASE]  WITH CHECK ADD FOREIGN KEY([PHYSICIAN_KEY])
REFERENCES [dbo].[D_PROVIDER] ([PROVIDER_KEY])
GO
ALTER TABLE [dbo].[BMIRD_CASE]  WITH CHECK ADD FOREIGN KEY([REPORTER_KEY])
REFERENCES [dbo].[D_PROVIDER] ([PROVIDER_KEY])
GO
ALTER TABLE [dbo].[BMIRD_CASE]  WITH CHECK ADD FOREIGN KEY([RPT_SRC_ORG_KEY])
REFERENCES [dbo].[D_ORGANIZATION] ([ORGANIZATION_KEY])
GO
ALTER TABLE [dbo].[BMIRD_MULTI_VALUE_FIELD]  WITH CHECK ADD FOREIGN KEY([BMIRD_MULTI_VAL_GRP_KEY])
REFERENCES [dbo].[BMIRD_MULTI_VALUE_FIELD_GROUP] ([BMIRD_MULTI_VAL_GRP_KEY])
GO
ALTER TABLE [dbo].[CASE_COUNT]  WITH CHECK ADD FOREIGN KEY([CONDITION_KEY])
REFERENCES [dbo].[CONDITION] ([CONDITION_KEY])
GO
ALTER TABLE [dbo].[CASE_COUNT]  WITH CHECK ADD FOREIGN KEY([INV_ASSIGNED_DT_KEY])
REFERENCES [dbo].[RDB_DATE] ([DATE_KEY])
GO
ALTER TABLE [dbo].[CASE_COUNT]  WITH CHECK ADD FOREIGN KEY([INVESTIGATOR_KEY])
REFERENCES [dbo].[D_PROVIDER] ([PROVIDER_KEY])
GO
ALTER TABLE [dbo].[CASE_COUNT]  WITH CHECK ADD FOREIGN KEY([INVESTIGATION_KEY])
REFERENCES [dbo].[INVESTIGATION] ([INVESTIGATION_KEY])
GO
ALTER TABLE [dbo].[CASE_COUNT]  WITH CHECK ADD FOREIGN KEY([PATIENT_KEY])
REFERENCES [dbo].[D_PATIENT] ([PATIENT_KEY])
GO
ALTER TABLE [dbo].[CASE_COUNT]  WITH CHECK ADD FOREIGN KEY([PHYSICIAN_KEY])
REFERENCES [dbo].[D_PROVIDER] ([PROVIDER_KEY])
GO
ALTER TABLE [dbo].[CASE_COUNT]  WITH CHECK ADD FOREIGN KEY([REPORTER_KEY])
REFERENCES [dbo].[D_PROVIDER] ([PROVIDER_KEY])
GO
ALTER TABLE [dbo].[CASE_COUNT]  WITH CHECK ADD FOREIGN KEY([RPT_SRC_ORG_KEY])
REFERENCES [dbo].[D_ORGANIZATION] ([ORGANIZATION_KEY])
GO
ALTER TABLE [dbo].[CONFIRMATION_METHOD_GROUP]  WITH CHECK ADD FOREIGN KEY([CONFIRMATION_METHOD_KEY])
REFERENCES [dbo].[CONFIRMATION_METHOD] ([CONFIRMATION_METHOD_KEY])
GO
ALTER TABLE [dbo].[CONFIRMATION_METHOD_GROUP]  WITH CHECK ADD FOREIGN KEY([INVESTIGATION_KEY])
REFERENCES [dbo].[INVESTIGATION] ([INVESTIGATION_KEY])
GO
ALTER TABLE [dbo].[D_PCR_SOURCE]  WITH CHECK ADD  CONSTRAINT [FK_D_PCR_SOURCE_D_PCR_SOURCE_GROUP] FOREIGN KEY([D_PCR_SOURCE_GROUP_KEY])
REFERENCES [dbo].[D_PCR_SOURCE_GROUP] ([D_PCR_SOURCE_GROUP_KEY])
GO
ALTER TABLE [dbo].[D_PCR_SOURCE] CHECK CONSTRAINT [FK_D_PCR_SOURCE_D_PCR_SOURCE_GROUP]
GO
ALTER TABLE [dbo].[D_RASH_LOC_GEN]  WITH CHECK ADD  CONSTRAINT [FK_D_RASH_LOC_GEN_D_RASH_LOC_GEN_GROUP] FOREIGN KEY([D_RASH_LOC_GEN_GROUP_KEY])
REFERENCES [dbo].[D_RASH_LOC_GEN_GROUP] ([D_RASH_LOC_GEN_GROUP_KEY])
GO
ALTER TABLE [dbo].[D_RASH_LOC_GEN] CHECK CONSTRAINT [FK_D_RASH_LOC_GEN_D_RASH_LOC_GEN_GROUP]
GO
ALTER TABLE [dbo].[F_VAR_PAM]  WITH CHECK ADD  CONSTRAINT [FK_F_PCR_SOURCE_GROUP] FOREIGN KEY([D_PCR_SOURCE_GROUP_KEY])
REFERENCES [dbo].[D_PCR_SOURCE_GROUP] ([D_PCR_SOURCE_GROUP_KEY])
GO
ALTER TABLE [dbo].[F_VAR_PAM] CHECK CONSTRAINT [FK_F_PCR_SOURCE_GROUP]
GO
ALTER TABLE [dbo].[F_VAR_PAM]  WITH CHECK ADD  CONSTRAINT [FK_F_RASH_LOC_GEN_GROUP] FOREIGN KEY([D_RASH_LOC_GEN_GROUP_KEY])
REFERENCES [dbo].[D_RASH_LOC_GEN_GROUP] ([D_RASH_LOC_GEN_GROUP_KEY])
GO
ALTER TABLE [dbo].[F_VAR_PAM] CHECK CONSTRAINT [FK_F_RASH_LOC_GEN_GROUP]
GO
ALTER TABLE [dbo].[F_VAR_PAM]  WITH CHECK ADD  CONSTRAINT [FK_F_VAR_PAM_D_VAR_PAM] FOREIGN KEY([D_VAR_PAM_KEY])
REFERENCES [dbo].[D_VAR_PAM] ([D_VAR_PAM_KEY])
GO
ALTER TABLE [dbo].[F_VAR_PAM] CHECK CONSTRAINT [FK_F_VAR_PAM_D_VAR_PAM]
GO
ALTER TABLE [dbo].[F_VAR_PAM]  WITH CHECK ADD  CONSTRAINT [FK_F_VAR_PAM_HOSPITAL] FOREIGN KEY([HOSPITAL_KEY])
REFERENCES [dbo].[D_ORGANIZATION] ([ORGANIZATION_KEY])
GO
ALTER TABLE [dbo].[F_VAR_PAM] CHECK CONSTRAINT [FK_F_VAR_PAM_HOSPITAL]
GO
ALTER TABLE [dbo].[F_VAR_PAM]  WITH CHECK ADD  CONSTRAINT [FK_F_VAR_PAM_ORG_REPORTER] FOREIGN KEY([ORG_AS_REPORTER_KEY])
REFERENCES [dbo].[D_ORGANIZATION] ([ORGANIZATION_KEY])
GO
ALTER TABLE [dbo].[F_VAR_PAM] CHECK CONSTRAINT [FK_F_VAR_PAM_ORG_REPORTER]
GO
ALTER TABLE [dbo].[F_VAR_PAM]  WITH CHECK ADD  CONSTRAINT [FK_F_VAR_PAM_PERSON] FOREIGN KEY([PERSON_KEY])
REFERENCES [dbo].[D_PATIENT] ([PATIENT_KEY])
GO
ALTER TABLE [dbo].[F_VAR_PAM] CHECK CONSTRAINT [FK_F_VAR_PAM_PERSON]
GO
ALTER TABLE [dbo].[F_VAR_PAM]  WITH CHECK ADD  CONSTRAINT [FK_F_VAR_PAM_PERSON_REPORTER] FOREIGN KEY([PERSON_AS_REPORTER_KEY])
REFERENCES [dbo].[D_PROVIDER] ([PROVIDER_KEY])
GO
ALTER TABLE [dbo].[F_VAR_PAM] CHECK CONSTRAINT [FK_F_VAR_PAM_PERSON_REPORTER]
GO
ALTER TABLE [dbo].[GENERIC_CASE]  WITH CHECK ADD FOREIGN KEY([ADT_HSPTL_KEY])
REFERENCES [dbo].[D_ORGANIZATION] ([ORGANIZATION_KEY])
GO
ALTER TABLE [dbo].[GENERIC_CASE]  WITH CHECK ADD FOREIGN KEY([CONDITION_KEY])
REFERENCES [dbo].[CONDITION] ([CONDITION_KEY])
GO
ALTER TABLE [dbo].[GENERIC_CASE]  WITH CHECK ADD FOREIGN KEY([INV_ASSIGNED_DT_KEY])
REFERENCES [dbo].[RDB_DATE] ([DATE_KEY])
GO
ALTER TABLE [dbo].[GENERIC_CASE]  WITH CHECK ADD FOREIGN KEY([INVESTIGATION_KEY])
REFERENCES [dbo].[INVESTIGATION] ([INVESTIGATION_KEY])
GO
ALTER TABLE [dbo].[GENERIC_CASE]  WITH CHECK ADD FOREIGN KEY([INVESTIGATOR_KEY])
REFERENCES [dbo].[D_PROVIDER] ([PROVIDER_KEY])
GO
ALTER TABLE [dbo].[GENERIC_CASE]  WITH CHECK ADD FOREIGN KEY([LDF_GROUP_KEY])
REFERENCES [dbo].[LDF_GROUP] ([LDF_GROUP_KEY])
GO
ALTER TABLE [dbo].[GENERIC_CASE]  WITH CHECK ADD FOREIGN KEY([PATIENT_KEY])
REFERENCES [dbo].[D_PATIENT] ([PATIENT_KEY])
GO
ALTER TABLE [dbo].[GENERIC_CASE]  WITH CHECK ADD FOREIGN KEY([PHYSICIAN_KEY])
REFERENCES [dbo].[D_PROVIDER] ([PROVIDER_KEY])
GO
ALTER TABLE [dbo].[GENERIC_CASE]  WITH CHECK ADD FOREIGN KEY([REPORTER_KEY])
REFERENCES [dbo].[D_PROVIDER] ([PROVIDER_KEY])
GO
ALTER TABLE [dbo].[GENERIC_CASE]  WITH CHECK ADD FOREIGN KEY([RPT_SRC_ORG_KEY])
REFERENCES [dbo].[D_ORGANIZATION] ([ORGANIZATION_KEY])
GO
ALTER TABLE [dbo].[HEP_MULTI_VALUE_FIELD]  WITH CHECK ADD FOREIGN KEY([HEP_MULTI_VAL_GRP_KEY])
REFERENCES [dbo].[HEP_MULTI_VALUE_FIELD_GROUP] ([HEP_MULTI_VAL_GRP_KEY])
GO
ALTER TABLE [dbo].[HEPATITIS_CASE]  WITH CHECK ADD FOREIGN KEY([ADT_HSPTL_KEY])
REFERENCES [dbo].[D_ORGANIZATION] ([ORGANIZATION_KEY])
GO
ALTER TABLE [dbo].[HEPATITIS_CASE]  WITH CHECK ADD FOREIGN KEY([CONDITION_KEY])
REFERENCES [dbo].[CONDITION] ([CONDITION_KEY])
GO
ALTER TABLE [dbo].[HEPATITIS_CASE]  WITH CHECK ADD FOREIGN KEY([HEP_MULTI_VAL_GRP_KEY])
REFERENCES [dbo].[HEP_MULTI_VALUE_FIELD_GROUP] ([HEP_MULTI_VAL_GRP_KEY])
GO
ALTER TABLE [dbo].[HEPATITIS_CASE]  WITH CHECK ADD FOREIGN KEY([INV_ASSIGNED_DT_KEY])
REFERENCES [dbo].[RDB_DATE] ([DATE_KEY])
GO
ALTER TABLE [dbo].[HEPATITIS_CASE]  WITH CHECK ADD FOREIGN KEY([INVESTIGATOR_KEY])
REFERENCES [dbo].[D_PROVIDER] ([PROVIDER_KEY])
GO
ALTER TABLE [dbo].[HEPATITIS_CASE]  WITH CHECK ADD FOREIGN KEY([INVESTIGATION_KEY])
REFERENCES [dbo].[INVESTIGATION] ([INVESTIGATION_KEY])
GO
ALTER TABLE [dbo].[HEPATITIS_CASE]  WITH CHECK ADD FOREIGN KEY([LDF_GROUP_KEY])
REFERENCES [dbo].[LDF_GROUP] ([LDF_GROUP_KEY])
GO
ALTER TABLE [dbo].[HEPATITIS_CASE]  WITH CHECK ADD FOREIGN KEY([PATIENT_KEY])
REFERENCES [dbo].[D_PATIENT] ([PATIENT_KEY])
GO
ALTER TABLE [dbo].[HEPATITIS_CASE]  WITH CHECK ADD FOREIGN KEY([PHYSICIAN_KEY])
REFERENCES [dbo].[D_PROVIDER] ([PROVIDER_KEY])
GO
ALTER TABLE [dbo].[HEPATITIS_CASE]  WITH CHECK ADD FOREIGN KEY([REPORTER_KEY])
REFERENCES [dbo].[D_PROVIDER] ([PROVIDER_KEY])
GO
ALTER TABLE [dbo].[HEPATITIS_CASE]  WITH CHECK ADD FOREIGN KEY([RPT_SRC_ORG_KEY])
REFERENCES [dbo].[D_ORGANIZATION] ([ORGANIZATION_KEY])
GO
ALTER TABLE [dbo].[MEASLES_CASE]  WITH CHECK ADD FOREIGN KEY([ADT_HSPTL_KEY])
REFERENCES [dbo].[D_ORGANIZATION] ([ORGANIZATION_KEY])
GO
ALTER TABLE [dbo].[MEASLES_CASE]  WITH CHECK ADD FOREIGN KEY([CONDITION_KEY])
REFERENCES [dbo].[CONDITION] ([CONDITION_KEY])
GO
ALTER TABLE [dbo].[MEASLES_CASE]  WITH CHECK ADD FOREIGN KEY([INV_ASSIGNED_DT_KEY])
REFERENCES [dbo].[RDB_DATE] ([DATE_KEY])
GO
ALTER TABLE [dbo].[MEASLES_CASE]  WITH CHECK ADD FOREIGN KEY([INV_RPT_DT_KEY])
REFERENCES [dbo].[RDB_DATE] ([DATE_KEY])
GO
ALTER TABLE [dbo].[MEASLES_CASE]  WITH CHECK ADD FOREIGN KEY([INVESTIGATOR_KEY])
REFERENCES [dbo].[D_PROVIDER] ([PROVIDER_KEY])
GO
ALTER TABLE [dbo].[MEASLES_CASE]  WITH CHECK ADD FOREIGN KEY([INVESTIGATION_KEY])
REFERENCES [dbo].[INVESTIGATION] ([INVESTIGATION_KEY])
GO
ALTER TABLE [dbo].[MEASLES_CASE]  WITH CHECK ADD FOREIGN KEY([LDF_GROUP_KEY])
REFERENCES [dbo].[LDF_GROUP] ([LDF_GROUP_KEY])
GO
ALTER TABLE [dbo].[MEASLES_CASE]  WITH CHECK ADD FOREIGN KEY([PATIENT_KEY])
REFERENCES [dbo].[D_PATIENT] ([PATIENT_KEY])
GO
ALTER TABLE [dbo].[MEASLES_CASE]  WITH CHECK ADD FOREIGN KEY([PHYSICIAN_KEY])
REFERENCES [dbo].[D_PROVIDER] ([PROVIDER_KEY])
GO
ALTER TABLE [dbo].[MEASLES_CASE]  WITH CHECK ADD FOREIGN KEY([REPORTER_KEY])
REFERENCES [dbo].[D_PROVIDER] ([PROVIDER_KEY])
GO
ALTER TABLE [dbo].[MEASLES_CASE]  WITH CHECK ADD FOREIGN KEY([RPT_SRC_ORG_KEY])
REFERENCES [dbo].[D_ORGANIZATION] ([ORGANIZATION_KEY])
GO
ALTER TABLE [dbo].[NOTIFICATION_EVENT]  WITH CHECK ADD FOREIGN KEY([CONDITION_KEY])
REFERENCES [dbo].[CONDITION] ([CONDITION_KEY])
GO
ALTER TABLE [dbo].[NOTIFICATION_EVENT]  WITH CHECK ADD FOREIGN KEY([INVESTIGATION_KEY])
REFERENCES [dbo].[INVESTIGATION] ([INVESTIGATION_KEY])
GO
ALTER TABLE [dbo].[NOTIFICATION_EVENT]  WITH CHECK ADD FOREIGN KEY([NOTIFICATION_SENT_DT_KEY])
REFERENCES [dbo].[RDB_DATE] ([DATE_KEY])
GO
ALTER TABLE [dbo].[NOTIFICATION_EVENT]  WITH CHECK ADD FOREIGN KEY([NOTIFICATION_SUBMIT_DT_KEY])
REFERENCES [dbo].[RDB_DATE] ([DATE_KEY])
GO
ALTER TABLE [dbo].[NOTIFICATION_EVENT]  WITH CHECK ADD FOREIGN KEY([NOTIFICATION_KEY])
REFERENCES [dbo].[NOTIFICATION] ([NOTIFICATION_KEY])
GO
ALTER TABLE [dbo].[NOTIFICATION_EVENT]  WITH CHECK ADD FOREIGN KEY([PATIENT_KEY])
REFERENCES [dbo].[D_PATIENT] ([PATIENT_KEY])
GO
ALTER TABLE [dbo].[PERTUSSIS_CASE]  WITH CHECK ADD FOREIGN KEY([ADT_HSPTL_KEY])
REFERENCES [dbo].[D_ORGANIZATION] ([ORGANIZATION_KEY])
GO
ALTER TABLE [dbo].[PERTUSSIS_CASE]  WITH CHECK ADD FOREIGN KEY([CONDITION_KEY])
REFERENCES [dbo].[CONDITION] ([CONDITION_KEY])
GO
ALTER TABLE [dbo].[PERTUSSIS_CASE]  WITH CHECK ADD FOREIGN KEY([INV_ASSIGNED_DT_KEY])
REFERENCES [dbo].[RDB_DATE] ([DATE_KEY])
GO
ALTER TABLE [dbo].[PERTUSSIS_CASE]  WITH CHECK ADD FOREIGN KEY([INVESTIGATOR_KEY])
REFERENCES [dbo].[D_PROVIDER] ([PROVIDER_KEY])
GO
ALTER TABLE [dbo].[PERTUSSIS_CASE]  WITH CHECK ADD FOREIGN KEY([INVESTIGATION_KEY])
REFERENCES [dbo].[INVESTIGATION] ([INVESTIGATION_KEY])
GO
ALTER TABLE [dbo].[PERTUSSIS_CASE]  WITH CHECK ADD FOREIGN KEY([LDF_GROUP_KEY])
REFERENCES [dbo].[LDF_GROUP] ([LDF_GROUP_KEY])
GO
ALTER TABLE [dbo].[PERTUSSIS_CASE]  WITH CHECK ADD FOREIGN KEY([PATIENT_KEY])
REFERENCES [dbo].[D_PATIENT] ([PATIENT_KEY])
GO
ALTER TABLE [dbo].[PERTUSSIS_CASE]  WITH CHECK ADD FOREIGN KEY([PERTUSSIS_SUSPECT_SRC_GRP_KEY])
REFERENCES [dbo].[PERTUSSIS_SUSPECTED_SOURCE_GRP] ([PERTUSSIS_SUSPECT_SRC_GRP_KEY])
GO
ALTER TABLE [dbo].[PERTUSSIS_CASE]  WITH CHECK ADD FOREIGN KEY([PERTUSSIS_TREATMENT_GRP_KEY])
REFERENCES [dbo].[PERTUSSIS_TREATMENT_GROUP] ([PERTUSSIS_TREATMENT_GRP_KEY])
GO
ALTER TABLE [dbo].[PERTUSSIS_CASE]  WITH CHECK ADD FOREIGN KEY([PHYSICIAN_KEY])
REFERENCES [dbo].[D_PROVIDER] ([PROVIDER_KEY])
GO
ALTER TABLE [dbo].[PERTUSSIS_CASE]  WITH CHECK ADD FOREIGN KEY([REPORTER_KEY])
REFERENCES [dbo].[D_PROVIDER] ([PROVIDER_KEY])
GO
ALTER TABLE [dbo].[PERTUSSIS_CASE]  WITH CHECK ADD FOREIGN KEY([RPT_SRC_ORG_KEY])
REFERENCES [dbo].[D_ORGANIZATION] ([ORGANIZATION_KEY])
GO
ALTER TABLE [dbo].[PERTUSSIS_SUSPECTED_SOURCE_FLD]  WITH CHECK ADD FOREIGN KEY([PERTUSSIS_SUSPECT_SRC_GRP_KEY])
REFERENCES [dbo].[PERTUSSIS_SUSPECTED_SOURCE_GRP] ([PERTUSSIS_SUSPECT_SRC_GRP_KEY])
GO
ALTER TABLE [dbo].[PERTUSSIS_TREATMENT_FIELD]  WITH CHECK ADD FOREIGN KEY([PERTUSSIS_TREATMENT_GRP_KEY])
REFERENCES [dbo].[PERTUSSIS_TREATMENT_GROUP] ([PERTUSSIS_TREATMENT_GRP_KEY])
GO
ALTER TABLE [dbo].[RUBELLA_CASE]  WITH CHECK ADD FOREIGN KEY([ADT_HSPTL_KEY])
REFERENCES [dbo].[D_ORGANIZATION] ([ORGANIZATION_KEY])
GO
ALTER TABLE [dbo].[RUBELLA_CASE]  WITH CHECK ADD FOREIGN KEY([CONDITION_KEY])
REFERENCES [dbo].[CONDITION] ([CONDITION_KEY])
GO
ALTER TABLE [dbo].[RUBELLA_CASE]  WITH CHECK ADD FOREIGN KEY([INV_ASSIGNED_DT_KEY])
REFERENCES [dbo].[RDB_DATE] ([DATE_KEY])
GO
ALTER TABLE [dbo].[RUBELLA_CASE]  WITH CHECK ADD FOREIGN KEY([INVESTIGATOR_KEY])
REFERENCES [dbo].[D_PROVIDER] ([PROVIDER_KEY])
GO
ALTER TABLE [dbo].[RUBELLA_CASE]  WITH CHECK ADD FOREIGN KEY([INVESTIGATION_KEY])
REFERENCES [dbo].[INVESTIGATION] ([INVESTIGATION_KEY])
GO
ALTER TABLE [dbo].[RUBELLA_CASE]  WITH CHECK ADD FOREIGN KEY([LDF_GROUP_KEY])
REFERENCES [dbo].[LDF_GROUP] ([LDF_GROUP_KEY])
GO
ALTER TABLE [dbo].[RUBELLA_CASE]  WITH CHECK ADD FOREIGN KEY([PATIENT_KEY])
REFERENCES [dbo].[D_PATIENT] ([PATIENT_KEY])
GO
ALTER TABLE [dbo].[RUBELLA_CASE]  WITH CHECK ADD FOREIGN KEY([PHYSICIAN_KEY])
REFERENCES [dbo].[D_PROVIDER] ([PROVIDER_KEY])
GO
ALTER TABLE [dbo].[RUBELLA_CASE]  WITH CHECK ADD FOREIGN KEY([REPORTER_KEY])
REFERENCES [dbo].[D_PROVIDER] ([PROVIDER_KEY])
GO
ALTER TABLE [dbo].[RUBELLA_CASE]  WITH CHECK ADD FOREIGN KEY([RPT_SRC_ORG_KEY])
REFERENCES [dbo].[D_ORGANIZATION] ([ORGANIZATION_KEY])
GO
ALTER TABLE [dbo].[LDF_DATA]  WITH CHECK ADD  CONSTRAINT [CHK_LDFDATA_RECORD_STATUS] CHECK  (([RECORD_STATUS_CD]='INACTIVE' OR [RECORD_STATUS_CD]='ACTIVE'))
GO
ALTER TABLE [dbo].[LDF_DATA] CHECK CONSTRAINT [CHK_LDFDATA_RECORD_STATUS]
GO
ALTER TABLE [dbo].[TREATMENT]  WITH CHECK ADD  CONSTRAINT [CHK_TREATMENT_RECORD_STATUS] CHECK  (([RECORD_STATUS_CD]='INACTIVE' OR [RECORD_STATUS_CD]='ACTIVE'))
GO
ALTER TABLE [dbo].[TREATMENT] CHECK CONSTRAINT [CHK_TREATMENT_RECORD_STATUS]
GO
ALTER TABLE [dbo].[TREATMENT_EVENT]  WITH CHECK ADD  CONSTRAINT [CHK_TRE_EVENT_RECORD_STATUS] CHECK  (([RECORD_STATUS_CD]='INACTIVE' OR [RECORD_STATUS_CD]='ACTIVE'))
GO
ALTER TABLE [dbo].[TREATMENT_EVENT] CHECK CONSTRAINT [CHK_TRE_EVENT_RECORD_STATUS]
GO
