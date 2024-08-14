package gov.cdc.nnddatapollservice.repository.rdb.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@Entity
@Table(name = "D_INV_VACCINATION")
public class DInvVaccination {
    @Column(name = "D_INV_VACCINATION_KEY")
    private Double dInvVaccinationKey;

    @Column(name = "nbs_case_answer_uid", precision = 21)
    private BigDecimal nbsCaseAnswerUid;

    @jakarta.validation.constraints.Size(max = 2000)
    @Column(name = "VAC_DOSE_PRIOR_TO_ONSET_NBR", length = 2000)
    private String vacDosePriorToOnsetNbr;

    @jakarta.validation.constraints.Size(max = 2000)
    @Column(name = "VAC_NONDOC_SMALLPX_VAC_YR", length = 2000)
    private String vacNondocSmallpxVacYr;

    @jakarta.validation.constraints.Size(max = 2000)
    @Column(name = "VAC_VAC_DOSE_REC_ONAFT1ST_NBR", length = 2000)
    private String vacVacDoseRecOnaft1stNbr;

    @jakarta.validation.constraints.Size(max = 2000)
    @Column(name = "VAC_VaccineDoses", length = 2000)
    private String vacVaccinedoses;

    @jakarta.validation.constraints.Size(max = 2000)
    @Column(name = "VAC_YearofLastDose", length = 2000)
    private String vacYearoflastdose;

    @Column(name = "VAC_DOSE_1_RCVD_DT")
    private LocalDate vacDose1RcvdDt;

    @Column(name = "VAC_DOSE_2_RCVD_DT")
    private LocalDate vacDose2RcvdDt;

    @Column(name = "VAC_LAST_DOSE_PRIOR_TO_ILL_DT")
    private LocalDate vacLastDosePriorToIllDt;

    @Column(name = "VAC_LastIGDose")
    private LocalDate vacLastigdose;

    @jakarta.validation.constraints.Size(max = 1999)
    @Column(name = "ADM_NT_PERTUSSIS_VACC_RESN", length = 1999)
    private String admNtPertussisVaccResn;

    @jakarta.validation.constraints.Size(max = 1999)
    @Column(name = "VAC_ImmuneGlobulin", length = 1999)
    private String vacImmuneglobulin;

    @jakarta.validation.constraints.Size(max = 1999)
    @Column(name = "VAC_NON_DOC_SMALLPOX_VACC", length = 1999)
    private String vacNonDocSmallpoxVacc;

    @jakarta.validation.constraints.Size(max = 1999)
    @Column(name = "VAC_RSN_NOT_VAC_PER_ACIP_R_OTH", length = 1999)
    private String vacRsnNotVacPerAcipROth;

    @jakarta.validation.constraints.Size(max = 1999)
    @Column(name = "VAC_RSN_NOT_VAC_PER_ACIP_REC", length = 1999)
    private String vacRsnNotVacPerAcipRec;

    @jakarta.validation.constraints.Size(max = 1999)
    @Column(name = "VAC_SMALLPOX_VACC_PRIOR", length = 1999)
    private String vacSmallpoxVaccPrior;

    @jakarta.validation.constraints.Size(max = 1999)
    @Column(name = "VAC_VACC_ADMIN_TIM", length = 1999)
    private String vacVaccAdminTim;

    @jakarta.validation.constraints.Size(max = 1999)
    @Column(name = "VAC_VACC_ADMIN_TIM2", length = 1999)
    private String vacVaccAdminTim2;

    @jakarta.validation.constraints.Size(max = 1999)
    @Column(name = "VAC_VACC_ADMIN_TIM3", length = 1999)
    private String vacVaccAdminTim3;

    @jakarta.validation.constraints.Size(max = 1999)
    @Column(name = "VAC_VACC_DATE_UNK", length = 1999)
    private String vacVaccDateUnk;

    @jakarta.validation.constraints.Size(max = 1999)
    @Column(name = "VAC_VACC_PER_ACIP_REC_IND", length = 1999)
    private String vacVaccPerAcipRecInd;

    @jakarta.validation.constraints.Size(max = 1999)
    @Column(name = "VAC_Vacc_Rcvd", length = 1999)
    private String vacVaccRcvd;

    @jakarta.validation.constraints.Size(max = 1999)
    @Column(name = "VACCINE_MANUFACTURER_NM", length = 1999)
    private String vaccineManufacturerNm;

    @jakarta.validation.constraints.Size(max = 1999)
    @Column(name = "VACCINE_MANUFACTURER_NM_OTH", length = 1999)
    private String vaccineManufacturerNmOth;

    @jakarta.validation.constraints.Size(max = 2000)
    @Column(name = "VAC_SMALLPOX_VACC_ADM_YRS", length = 2000)
    private String vacSmallpoxVaccAdmYrs;

    @jakarta.validation.constraints.Size(max = 2000)
    @Column(name = "VAC_VAC_VACCINEDOSES_CD", length = 2000)
    private String vacVacVaccinedosesCd;

    @jakarta.validation.constraints.Size(max = 2000)
    @Column(name = "VAC_VACCINATION_NOTES", length = 2000)
    private String vacVaccinationNotes;

}