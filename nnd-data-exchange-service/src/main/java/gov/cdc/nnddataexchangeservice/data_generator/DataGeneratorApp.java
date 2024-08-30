package gov.cdc.nnddataexchangeservice.data_generator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static gov.cdc.nnddataexchangeservice.data_generator.RandomValueGenerator.getRandomDate;
import static gov.cdc.nnddataexchangeservice.data_generator.RandomValueGenerator.getRandomString;

@Component
public class DataGeneratorApp  {
    private static final Logger logger = LoggerFactory.getLogger(DataGeneratorApp.class);

    private final JdbcTemplate jdbcTemplate;

    protected int batchSize = 100;
    protected int totalRecord = 100;

    public DataGeneratorApp(@Qualifier("rdbJdbcTemplate") JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void run() {
        try {
            // INVESTIGATE DATA - KEY = 1
            insertInvestigationData();
        } catch (Exception e) {
            logger.info(e.getMessage());
        }

   //     runINV_MEDICAL_HISTORY();
        runD_PATIENT();


    }

    public void runINV_MEDICAL_HISTORY () {
        int batchSize = this.batchSize; // Adjust the batch size as needed
        int totalRecords = this.totalRecord; // Total number of records you want to generate
        int completedRecords = 0;
        for (int i = 0; i < totalRecords / batchSize; i++) {
            insertRandomDataBatchInvMedicalHistory(batchSize);
            completedRecords += batchSize;
            logger.info("Completed {} records out of {}.", completedRecords, totalRecords);
        }
        if (totalRecords % batchSize != 0) {
            int remainingRecords = totalRecords % batchSize;
            insertRandomDataBatchInvMedicalHistory(remainingRecords);
            completedRecords += remainingRecords;
            logger.info("Completed {} records out of {}.", completedRecords, totalRecords);
        }
        logger.info("Data generation complete. Total records generated: {}", completedRecords);
    }
    public void runD_PATIENT() {
        int batchSize = this.batchSize; // Adjust the batch size as needed
        int totalRecords = this.totalRecord; // Total number of records you want to generate
        int completedRecords = 0;

        for (int i = 0; i < totalRecords / batchSize; i++) {
            insertProviderDataBatch(batchSize);
            completedRecords += batchSize;
            logger.info("Completed {} records out of {} for D_PATIENT.", completedRecords, totalRecords);
        }

        if (totalRecords % batchSize != 0) {
            int remainingRecords = totalRecords % batchSize;
            insertProviderDataBatch(remainingRecords);
            completedRecords += remainingRecords;
            logger.info("Completed {} records out of {} for D_PATIENT.", completedRecords, totalRecords);
        }

        logger.info("Data generation complete for D_PATIENT. Total records generated: {}", completedRecords);
    }
    private void insertInvestigationData() {
        String sql = "INSERT INTO [dbo].[INVESTIGATION] (" +
                "[INVESTIGATION_KEY], [CASE_OID], [CASE_UID], [INV_LOCAL_ID], [INV_SHARE_IND], [OUTBREAK_NAME], " +
                "[INVESTIGATION_STATUS], [INV_CASE_STATUS], [CASE_TYPE], [INV_COMMENTS], [JURISDICTION_CD], " +
                "[JURISDICTION_NM], [EARLIEST_RPT_TO_PHD_DT], [ILLNESS_ONSET_DT], [ILLNESS_END_DT], [INV_RPT_DT], " +
                "[INV_START_DT], [RPT_SRC_CD_DESC], [EARLIEST_RPT_TO_CNTY_DT], [EARLIEST_RPT_TO_STATE_DT], [CASE_RPT_MMWR_WK], " +
                "[CASE_RPT_MMWR_YR], [DISEASE_IMPORTED_IND], [IMPORT_FRM_CNTRY], [IMPORT_FRM_STATE], [IMPORT_FRM_CNTY], " +
                "[IMPORT_FRM_CITY], [EARLIEST_RPT_TO_CDC_DT], [RPT_SRC_CD], [IMPORT_FRM_CNTRY_CD], [IMPORT_FRM_STATE_CD], " +
                "[IMPORT_FRM_CNTY_CD], [IMPORT_FRM_CITY_CD], [DIAGNOSIS_DT], [HSPTL_ADMISSION_DT], [HSPTL_DISCHARGE_DT], " +
                "[HSPTL_DURATION_DAYS], [OUTBREAK_IND], [HSPTLIZD_IND], [INV_STATE_CASE_ID], [CITY_COUNTY_CASE_NBR], " +
                "[TRANSMISSION_MODE], [RECORD_STATUS_CD], [PATIENT_PREGNANT_IND], [DIE_FRM_THIS_ILLNESS_IND], " +
                "[DAYCARE_ASSOCIATION_IND], [FOOD_HANDLR_IND], [INVESTIGATION_DEATH_DATE], [PATIENT_AGE_AT_ONSET], " +
                "[PATIENT_AGE_AT_ONSET_UNIT], [INV_ASSIGNED_DT], [DETECTION_METHOD_DESC_TXT], [ILLNESS_DURATION], " +
                "[ILLNESS_DURATION_UNIT], [CONTACT_INV_COMMENTS], [CONTACT_INV_PRIORITY], [CONTACT_INFECTIOUS_FROM_DATE], " +
                "[CONTACT_INFECTIOUS_TO_DATE], [CONTACT_INV_STATUS], [INV_CLOSE_DT], [PROGRAM_AREA_DESCRIPTION], " +
                "[ADD_TIME], [LAST_CHG_TIME], [INVESTIGATION_ADDED_BY], [INVESTIGATION_LAST_UPDATED_BY], [REFERRAL_BASIS], " +
                "[CURR_PROCESS_STATE], [INV_PRIORITY_CD], [COINFECTION_ID], [LEGACY_CASE_ID], [OUTBREAK_NAME_DESC]" +
                ") VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        jdbcTemplate.update(sql, ps -> {
            ps.setInt(1, 1); // INVESTIGATION_KEY
            ps.setNull(2, java.sql.Types.VARCHAR); // CASE_OID
            ps.setNull(3, java.sql.Types.VARCHAR); // CASE_UID
            ps.setString(4, "INV001"); // INV_LOCAL_ID
            ps.setString(5, "Y"); // INV_SHARE_IND
            ps.setString(6, "Outbreak 001"); // OUTBREAK_NAME
            ps.setString(7, "Open"); // INVESTIGATION_STATUS
            ps.setString(8, "Confirmed"); // INV_CASE_STATUS
            ps.setString(9, "Type A"); // CASE_TYPE
            ps.setString(10, "Investigation comments"); // INV_COMMENTS
            ps.setString(11, "123"); // JURISDICTION_CD
            ps.setString(12, "Sample Jurisdiction"); // JURISDICTION_NM
            ps.setDate(13, java.sql.Date.valueOf("2024-08-01")); // EARLIEST_RPT_TO_PHD_DT
            ps.setDate(14, java.sql.Date.valueOf("2024-07-15")); // ILLNESS_ONSET_DT
            ps.setDate(15, java.sql.Date.valueOf("2024-07-25")); // ILLNESS_END_DT
            ps.setDate(16, java.sql.Date.valueOf("2024-07-20")); // INV_RPT_DT
            ps.setDate(17, java.sql.Date.valueOf("2024-07-10")); // INV_START_DT
            ps.setString(18, "Source Description"); // RPT_SRC_CD_DESC
            ps.setDate(19, java.sql.Date.valueOf("2024-07-18")); // EARLIEST_RPT_TO_CNTY_DT
            ps.setDate(20, java.sql.Date.valueOf("2024-07-19")); // EARLIEST_RPT_TO_STATE_DT
            ps.setInt(21, 12); // CASE_RPT_MMWR_WK
            ps.setInt(22, 2024); // CASE_RPT_MMWR_YR
            ps.setString(23, "No"); // DISEASE_IMPORTED_IND
            ps.setString(24, "Country A"); // IMPORT_FRM_CNTRY
            ps.setString(25, "State A"); // IMPORT_FRM_STATE
            ps.setString(26, "County A"); // IMPORT_FRM_CNTY
            ps.setString(27, "City A"); // IMPORT_FRM_CITY
            ps.setDate(28, java.sql.Date.valueOf("2024-07-21")); // EARLIEST_RPT_TO_CDC_DT
            ps.setString(29, "Code 001"); // RPT_SRC_CD
            ps.setString(30, "Country A Code"); // IMPORT_FRM_CNTRY_CD
            ps.setString(31, "State A Code"); // IMPORT_FRM_STATE_CD
            ps.setString(32, "County A Code"); // IMPORT_FRM_CNTY_CD
            ps.setString(33, "City A Code"); // IMPORT_FRM_CITY_CD
            ps.setDate(34, java.sql.Date.valueOf("2024-07-22")); // DIAGNOSIS_DT
            ps.setDate(35, java.sql.Date.valueOf("2024-07-23")); // HSPTL_ADMISSION_DT
            ps.setDate(36, java.sql.Date.valueOf("2024-07-24")); // HSPTL_DISCHARGE_DT
            ps.setInt(37, 2); // HSPTL_DURATION_DAYS
            ps.setString(38, "Yes"); // OUTBREAK_IND
            ps.setString(39, "Yes"); // HSPTLIZD_IND
            ps.setString(40, "State Case ID 001"); // INV_STATE_CASE_ID
            ps.setString(41, "City Case 001"); // CITY_COUNTY_CASE_NBR
            ps.setString(42, "Direct Contact"); // TRANSMISSION_MODE
            ps.setString(43, "Active"); // RECORD_STATUS_CD
            ps.setString(44, "No"); // PATIENT_PREGNANT_IND
            ps.setString(45, "No"); // DIE_FRM_THIS_ILLNESS_IND
            ps.setString(46, "No"); // DAYCARE_ASSOCIATION_IND
            ps.setString(47, "No"); // FOOD_HANDLR_IND
            ps.setDate(48, java.sql.Date.valueOf("2024-07-30")); // INVESTIGATION_DEATH_DATE
            ps.setInt(49, 35); // PATIENT_AGE_AT_ONSET
            ps.setString(50, "Years"); // PATIENT_AGE_AT_ONSET_UNIT
            ps.setDate(51, java.sql.Date.valueOf("2024-07-09")); // INV_ASSIGNED_DT
            ps.setString(52, "Detection Method X"); // DETECTION_METHOD_DESC_TXT
            ps.setInt(53, 10); // ILLNESS_DURATION
            ps.setString(54, "Days"); // ILLNESS_DURATION_UNIT
            ps.setString(55, "Contact comments"); // CONTACT_INV_COMMENTS
            ps.setString(56, "High"); // CONTACT_INV_PRIORITY
            ps.setDate(57, java.sql.Date.valueOf("2024-07-12")); // CONTACT_INFECTIOUS_FROM_DATE
            ps.setDate(58, java.sql.Date.valueOf("2024-07-20")); // CONTACT_INFECTIOUS_TO_DATE
            ps.setString(59, "Completed"); // CONTACT_INV_STATUS
            ps.setDate(60, java.sql.Date.valueOf("2024-08-02")); // INV_CLOSE_DT
            ps.setString(61, "Area Description"); // PROGRAM_AREA_DESCRIPTION
            ps.setDate(62, java.sql.Date.valueOf("2024-07-10")); // ADD_TIME
            ps.setDate(63, java.sql.Date.valueOf("2024-07-25")); // LAST_CHG_TIME
            ps.setString(64, "Admin"); // INVESTIGATION_ADDED_BY
            ps.setString(65, "Updater"); // INVESTIGATION_LAST_UPDATED_BY
            ps.setString(66, "Referral Basis X"); // REFERRAL_BASIS
            ps.setString(67, "Processing State Y"); // CURR_PROCESS_STATE
            ps.setString(68, "Priority X"); // INV_PRIORITY_CD
            ps.setString(69, "Coinfection X"); // COINFECTION_ID
            ps.setString(70, "Legacy Case 001"); // LEGACY_CASE_ID
            ps.setString(71, "Outbreak Desc 001"); // OUTBREAK_NAME_DESC
        });
    }

    private void insertRandomDataBatchInvMedicalHistory(int batchSize) {
        Random random = new Random();
        List<Object[]> batchArgs = new ArrayList<>();

        for (int i = 0; i < batchSize; i++) {
            Object[] params = new Object[]{
                    1, // D_INV_MEDICAL_HISTORY_KEY
                    BigDecimal.valueOf(random.nextDouble() * 1000000), // nbs_case_answer_uid
                    getRandomDate(), // MDH_DiabetesDxDate
                    getRandomDate(), // MDH_DueDate
                    getRandomDate(), // MDH_PROVIDER_REASON_VISIT_DT
                    getRandomDate(), // MDH_SPLENECTOMY_DT
                    getRandomString(random), // MDH_ASPLENIC_IND
                    getRandomString(random), // MDH_AUTOIMMUNE_DISEASE
                    getRandomString(random), // MDH_CHRONIC_LIVER_DIS_IND
                    getRandomString(random), // MDH_CHRONIC_LUNG_DIS_IND
                    getRandomString(random), // MDH_CHRONIC_RENAL_DIS_IND
                    getRandomString(random), // MDH_CONG_MALFORMATION_IND
                    getRandomString(random), // MDH_CURR_BREAST_FEEDING
                    getRandomString(random), // MDH_CV_DISEASE_IND
                    getRandomString(random), // MDH_Diabetes
                    getRandomString(random), // MDH_DIABETES_MELLITUS_IND
                    getRandomString(random), // MDH_DIABTS_MELITS_INSULIN
                    getRandomString(random), // MDH_HIV_VIRAL_LD_UNDETECT
                    getRandomString(random), // MDH_IMMUNO_CONDITION_IND
                    getRandomString(random), // MDH_ISCHEMIC_HEART_DISEAS
                    getRandomString(random), // MDH_Jaundiced
                    getRandomString(random), // MDH_NEURO_DISABLITY_IND
                    getRandomString(random), // MDH_OBESITY_IND
                    getRandomString(random), // MDH_OTH_CHRONIC_DIS_IND
                    getRandomString(random), // MDH_PAT_UNDRLYNG_COND
                    getRandomString(random), // MDH_PREEXISTING_COND_IND
                    getRandomString(random), // MDH_PREV_STD_HIST
                    getRandomString(random), // MDH_PrevAwareInfection
                    getRandomString(random), // MDH_ProviderOfCare
                    getRandomString(random), // MDH_PSYCHIATRIC_CONDITION
                    getRandomString(random), // MDH_ReasonForTest
                    getRandomString(random), // MDH_ReasonForTest_OTH
                    getRandomString(random), // MDH_SICKLE_CELL_DIS_IND
                    getRandomString(random), // MDH_SUBSTANCE_ABUSE
                    getRandomString(random), // MDH_Symptomatic
                    getRandomString(random), // MDH_TISSUE_ORGAN_TRNSPLNT
                    getRandomString(random), // MDH_TNF_ANTAGONIST_TX
                    getRandomString(random), // MDH_UNDERLYING_COND_OTH
                    getRandomString(random), // MDH_VIRAL_HEP_B_C_INF
                    getRandomString(random), // MDH_CONG_MALFORMATION_OTH
                    getRandomString(random), // MDH_GASTRIC_SURGERY_IND
                    getRandomString(random), // MDH_HEMATOLOGIC_DISEASE_
                    getRandomString(random), // MDH_IMMUNODEFICIENCY_TYPE
                    getRandomString(random), // MDH_MEDICAL_RECORD_NBR
                    getRandomString(random), // MDH_NEURO_DISABLITY_INFO
                    getRandomString(random), // MDH_NEURO_DISABLITY_INFO2
                    getRandomString(random), // MDH_NEURO_DISABLITY_INFO3
                    getRandomString(random), // MDH_OTH_CHRONIC_DIS_TXT
                    getRandomString(random), // MDH_PRIOR_ILLNESS_OTH
                    getRandomString(random), // MDH_PSYCH_CONDITION_SPEC
                    getRandomString(random), // MDH_PSYCH_CONDITION_SPEC2
                    getRandomString(random), // MDH_PSYCH_CONDITION_SPEC3
                    getRandomString(random), // MDH_ReasonForTestingOth
                    getRandomString(random), // MDH_SPLENECTOMY_REASON
                    getRandomString(random), // MDH_TYPE_LIVER_DZ_OTH
                    getRandomString(random), // MDH_TYPE_MALIGNANCY_OTH
                    getRandomString(random), // MDH_TYPE_ORGAN_TRANSPLANT
                    getRandomString(random), // MDH_TYPE_RENAL_DZ_OTH
                    getRandomString(random), // MDH_UNDRLYNG_COND_SPECIFY
            };
            batchArgs.add(params);
        }

        jdbcTemplate.batchUpdate(
                "INSERT INTO dbo.D_INV_MEDICAL_HISTORY (" +
                        "D_INV_MEDICAL_HISTORY_KEY, nbs_case_answer_uid, MDH_DiabetesDxDate, MDH_DueDate, " +
                        "MDH_PROVIDER_REASON_VISIT_DT, MDH_SPLENECTOMY_DT, MDH_ASPLENIC_IND, MDH_AUTOIMMUNE_DISEASE, " +
                        "MDH_CHRONIC_LIVER_DIS_IND, MDH_CHRONIC_LUNG_DIS_IND, MDH_CHRONIC_RENAL_DIS_IND, MDH_CONG_MALFORMATION_IND, " +
                        "MDH_CURR_BREAST_FEEDING, MDH_CV_DISEASE_IND, MDH_Diabetes, MDH_DIABETES_MELLITUS_IND, MDH_DIABTS_MELITS_INSULIN, " +
                        "MDH_HIV_VIRAL_LD_UNDETECT, MDH_IMMUNO_CONDITION_IND, MDH_ISCHEMIC_HEART_DISEAS, MDH_Jaundiced, MDH_NEURO_DISABLITY_IND, " +
                        "MDH_OBESITY_IND, MDH_OTH_CHRONIC_DIS_IND, MDH_PAT_UNDRLYNG_COND, MDH_PREEXISTING_COND_IND, MDH_PREV_STD_HIST, " +
                        "MDH_PrevAwareInfection, MDH_ProviderOfCare, MDH_PSYCHIATRIC_CONDITION, MDH_ReasonForTest, MDH_ReasonForTest_OTH, " +
                        "MDH_SICKLE_CELL_DIS_IND, MDH_SUBSTANCE_ABUSE, MDH_Symptomatic, MDH_TISSUE_ORGAN_TRNSPLNT, MDH_TNF_ANTAGONIST_TX, " +
                        "MDH_UNDERLYING_COND_OTH, MDH_VIRAL_HEP_B_C_INF, MDH_CONG_MALFORMATION_OTH, MDH_GASTRIC_SURGERY_IND, MDH_HEMATOLOGIC_DISEASE_, " +
                        "MDH_IMMUNODEFICIENCY_TYPE, MDH_MEDICAL_RECORD_NBR, MDH_NEURO_DISABLITY_INFO, MDH_NEURO_DISABLITY_INFO2, MDH_NEURO_DISABLITY_INFO3, " +
                        "MDH_OTH_CHRONIC_DIS_TXT, MDH_PRIOR_ILLNESS_OTH, MDH_PSYCH_CONDITION_SPEC, MDH_PSYCH_CONDITION_SPEC2, MDH_PSYCH_CONDITION_SPEC3, " +
                        "MDH_ReasonForTestingOth, MDH_SPLENECTOMY_REASON, MDH_TYPE_LIVER_DZ_OTH, MDH_TYPE_MALIGNANCY_OTH, MDH_TYPE_ORGAN_TRANSPLANT, " +
                        "MDH_TYPE_RENAL_DZ_OTH, MDH_UNDRLYNG_COND_SPECIFY) " +
                        "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)",
                batchArgs
        );
    }

    private void insertProviderDataBatch(int batchSize) {
        Random random = new Random();
        List<Object[]> batchArgs = new ArrayList<>();

        for (int i = 0; i < batchSize; i++) {
            Object[] params = new Object[]{
                    (long) random.nextInt(100000), // PROVIDER_UID
                    (long) i + 1, // PROVIDER_KEY
                    getRandomString(random, 50), // PROVIDER_LOCAL_ID
                    getRandomString(random, 50), // PROVIDER_RECORD_STATUS
                    getRandomString(random, 50), // PROVIDER_NAME_PREFIX
                    getRandomString(random, 50), // PROVIDER_FIRST_NAME
                    getRandomString(random, 50), // PROVIDER_MIDDLE_NAME
                    getRandomString(random, 50), // PROVIDER_LAST_NAME
                    getRandomString(random, 50), // PROVIDER_NAME_SUFFIX
                    getRandomString(random, 50), // PROVIDER_NAME_DEGREE
                    getRandomString(random, 2000), // PROVIDER_GENERAL_COMMENTS
                    getRandomString(random, 50), // PROVIDER_QUICK_CODE
                    getRandomString(random, 50), // PROVIDER_REGISTRATION_NUM
                    getRandomString(random, 199), // PROVIDER_REGISRATION_NUM_AUTH
                    getRandomString(random, 50), // PROVIDER_STREET_ADDRESS_1
                    getRandomString(random, 50), // PROVIDER_STREET_ADDRESS_2
                    getRandomString(random, 50), // PROVIDER_CITY
                    getRandomString(random, 50), // PROVIDER_STATE
                    getRandomString(random, 50), // PROVIDER_STATE_CODE
                    getRandomString(random, 50), // PROVIDER_ZIP
                    getRandomString(random, 50), // PROVIDER_COUNTY
                    getRandomString(random, 50), // PROVIDER_COUNTY_CODE
                    getRandomString(random, 50), // PROVIDER_COUNTRY
                    getRandomString(random, 2000), // PROVIDER_ADDRESS_COMMENTS
                    getRandomString(random, 50), // PROVIDER_PHONE_WORK
                    getRandomString(random, 50), // PROVIDER_PHONE_EXT_WORK
                    getRandomString(random, 50), // PROVIDER_EMAIL_WORK
                    getRandomString(random, 2000), // PROVIDER_PHONE_COMMENTS
                    getRandomString(random, 50), // PROVIDER_PHONE_CELL
                    getRandomString(random, 50), // PROVIDER_ENTRY_METHOD
                    getRandomDate(), // PROVIDER_LAST_CHANGE_TIME
                    getRandomDate(), // PROVIDER_ADD_TIME
                    getRandomString(random, 50), // PROVIDER_ADDED_BY
                    getRandomString(random, 50) // PROVIDER_LAST_UPDATED_BY
            };
            batchArgs.add(params);
        }

        jdbcTemplate.batchUpdate(
                "INSERT INTO dbo.D_PROVIDER (" +
                        "PROVIDER_UID, PROVIDER_KEY, PROVIDER_LOCAL_ID, PROVIDER_RECORD_STATUS, PROVIDER_NAME_PREFIX, " +
                        "PROVIDER_FIRST_NAME, PROVIDER_MIDDLE_NAME, PROVIDER_LAST_NAME, PROVIDER_NAME_SUFFIX, PROVIDER_NAME_DEGREE, " +
                        "PROVIDER_GENERAL_COMMENTS, PROVIDER_QUICK_CODE, PROVIDER_REGISTRATION_NUM, PROVIDER_REGISRATION_NUM_AUTH, PROVIDER_STREET_ADDRESS_1, " +
                        "PROVIDER_STREET_ADDRESS_2, PROVIDER_CITY, PROVIDER_STATE, PROVIDER_STATE_CODE, PROVIDER_ZIP, " +
                        "PROVIDER_COUNTY, PROVIDER_COUNTY_CODE, PROVIDER_COUNTRY, PROVIDER_ADDRESS_COMMENTS, PROVIDER_PHONE_WORK, " +
                        "PROVIDER_PHONE_EXT_WORK, PROVIDER_EMAIL_WORK, PROVIDER_PHONE_COMMENTS, PROVIDER_PHONE_CELL, PROVIDER_ENTRY_METHOD, " +
                        "PROVIDER_LAST_CHANGE_TIME, PROVIDER_ADD_TIME, PROVIDER_ADDED_BY, PROVIDER_LAST_UPDATED_BY) " +
                        "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)",
                batchArgs
        );

        logger.info("Batch of {} records inserted successfully.", batchSize);
    }

}