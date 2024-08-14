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
@Table(name = "D_INV_LAB_FINDING")
public class DInvLabFinding {
    @Column(name = "D_INV_LAB_FINDING_KEY")
    private Double dInvLabFindingKey;

    @Column(name = "nbs_case_answer_uid", precision = 21)
    private BigDecimal nbsCaseAnswerUid;

    @jakarta.validation.constraints.Size(max = 2000)
    @Column(name = "LAB_ALT_Result", length = 2000)
    private String labAltResult;

    @jakarta.validation.constraints.Size(max = 2000)
    @Column(name = "LAB_AST_Result", length = 2000)
    private String labAstResult;

    @jakarta.validation.constraints.Size(max = 2000)
    @Column(name = "LAB_BAND_TEST_HIGH_VAL", length = 2000)
    private String labBandTestHighVal;

    @jakarta.validation.constraints.Size(max = 2000)
    @Column(name = "LAB_BAND_TEST_LOW_VAL", length = 2000)
    private String labBandTestLowVal;

    @jakarta.validation.constraints.Size(max = 2000)
    @Column(name = "LAB_BNP_HIGH_VAL", length = 2000)
    private String labBnpHighVal;

    @jakarta.validation.constraints.Size(max = 2000)
    @Column(name = "LAB_CRP_HIGH_VALUE", length = 2000)
    private String labCrpHighValue;

    @jakarta.validation.constraints.Size(max = 2000)
    @Column(name = "LAB_D_DIMER_HIGH_VAL", length = 2000)
    private String labDDimerHighVal;

    @jakarta.validation.constraints.Size(max = 2000)
    @Column(name = "LAB_FERRITIN_HIGH_VAL", length = 2000)
    private String labFerritinHighVal;

    @jakarta.validation.constraints.Size(max = 2000)
    @Column(name = "LAB_FIBRINOGEN_HIGH_VAL", length = 2000)
    private String labFibrinogenHighVal;

    @jakarta.validation.constraints.Size(max = 2000)
    @Column(name = "LAB_GLUCOSE_HIGH_VALUE", length = 2000)
    private String labGlucoseHighValue;

    @jakarta.validation.constraints.Size(max = 2000)
    @Column(name = "LAB_GLUCOSE_LOW_VALUE", length = 2000)
    private String labGlucoseLowValue;

    @jakarta.validation.constraints.Size(max = 2000)
    @Column(name = "LAB_IL_6_HIGH_VAL", length = 2000)
    private String labIl6HighVal;

    @jakarta.validation.constraints.Size(max = 2000)
    @Column(name = "LAB_LYMPHOCYTES_HIGH_VAL", length = 2000)
    private String labLymphocytesHighVal;

    @jakarta.validation.constraints.Size(max = 2000)
    @Column(name = "LAB_LYMPHOCYTES_LOW_VAL", length = 2000)
    private String labLymphocytesLowVal;

    @jakarta.validation.constraints.Size(max = 2000)
    @Column(name = "LAB_NEUTROPHILS_HIGH_VAL", length = 2000)
    private String labNeutrophilsHighVal;

    @jakarta.validation.constraints.Size(max = 2000)
    @Column(name = "LAB_NEUTROPHILS_LOW_VAL", length = 2000)
    private String labNeutrophilsLowVal;

    @jakarta.validation.constraints.Size(max = 2000)
    @Column(name = "LAB_NT_PROBNP_HIGH_VAL", length = 2000)
    private String labNtProbnpHighVal;

    @jakarta.validation.constraints.Size(max = 2000)
    @Column(name = "LAB_PLATELETS_HIGH_VAL", length = 2000)
    private String labPlateletsHighVal;

    @jakarta.validation.constraints.Size(max = 2000)
    @Column(name = "LAB_PLATELETS_LOW_VAL", length = 2000)
    private String labPlateletsLowVal;

    @jakarta.validation.constraints.Size(max = 2000)
    @Column(name = "LAB_PROTEIN_TEST_HIGH_VAL", length = 2000)
    private String labProteinTestHighVal;

    @jakarta.validation.constraints.Size(max = 2000)
    @Column(name = "LAB_PROTEIN_TEST_LOW_VAL", length = 2000)
    private String labProteinTestLowVal;

    @jakarta.validation.constraints.Size(max = 2000)
    @Column(name = "LAB_SERUM_WBC_HIGH_VAL", length = 2000)
    private String labSerumWbcHighVal;

    @jakarta.validation.constraints.Size(max = 2000)
    @Column(name = "LAB_SERUM_WBC_LOW_VAL", length = 2000)
    private String labSerumWbcLowVal;

    @jakarta.validation.constraints.Size(max = 2000)
    @Column(name = "LAB_TB_SKIN_TST_MM_INDUR", length = 2000)
    private String labTbSkinTstMmIndur;

    @jakarta.validation.constraints.Size(max = 2000)
    @Column(name = "LAB_TestResultUpperLimit", length = 2000)
    private String labTestresultupperlimit;

    @jakarta.validation.constraints.Size(max = 2000)
    @Column(name = "LAB_TestResultUpperLimit2", length = 2000)
    private String labTestresultupperlimit2;

    @jakarta.validation.constraints.Size(max = 2000)
    @Column(name = "LAB_TROPONIN_HIGH_VAL", length = 2000)
    private String labTroponinHighVal;

    @jakarta.validation.constraints.Size(max = 2000)
    @Column(name = "LAB_URINE_WBC_HIGH_VAL", length = 2000)
    private String labUrineWbcHighVal;

    @jakarta.validation.constraints.Size(max = 2000)
    @Column(name = "LAB_URINE_WBC_LOW_VAL", length = 2000)
    private String labUrineWbcLowVal;

    @jakarta.validation.constraints.Size(max = 2000)
    @Column(name = "LAB_WBC_HIGH_VAL", length = 2000)
    private String labWbcHighVal;

    @jakarta.validation.constraints.Size(max = 2000)
    @Column(name = "LAB_WBC_LOW_VAL", length = 2000)
    private String labWbcLowVal;

    @Column(name = "LAB_ANTIGEN_TEST_DT")
    private LocalDate labAntigenTestDt;

    @Column(name = "LAB_CULTURE_OTH_COLL_DT")
    private LocalDate labCultureOthCollDt;

    @Column(name = "LAB_CULTURE_OTH_RPT_DT")
    private LocalDate labCultureOthRptDt;

    @Column(name = "LAB_DT_FRST_NEG_CULTURE")
    private LocalDate labDtFrstNegCulture;

    @Column(name = "LAB_FRST_POS_SPEC_CLCT_DT")
    private LocalDate labFrstPosSpecClctDt;

    @Column(name = "LAB_HBeAg_Date")
    private LocalDate labHbeagDate;

    @Column(name = "LAB_HBsAg_Date")
    private LocalDate labHbsagDate;

    @Column(name = "LAB_HBV_NAT_Date")
    private LocalDate labHbvNatDate;

    @Column(name = "LAB_HCVRNA_Date")
    private LocalDate labHcvrnaDate;

    @Column(name = "LAB_HIV_RPT_DT")
    private LocalDate labHivRptDt;

    @Column(name = "LAB_HIV_SPECIMEN_COLL_DT")
    private LocalDate labHivSpecimenCollDt;

    @Column(name = "LAB_IGA_TEST_DT")
    private LocalDate labIgaTestDt;

    @Column(name = "LAB_IGG_TEST_DT")
    private LocalDate labIggTestDt;

    @Column(name = "LAB_IGM_TEST_DT")
    private LocalDate labIgmTestDt;

    @Column(name = "LAB_IgMAntiHAVDate")
    private LocalDate labIgmantihavdate;

    @Column(name = "LAB_IgMAntiHBcDate")
    private LocalDate labIgmantihbcdate;

    @Column(name = "LAB_IGRA_COLL_DT")
    private LocalDate labIgraCollDt;

    @Column(name = "LAB_IGRA_RPT_DT")
    private LocalDate labIgraRptDt;

    @Column(name = "LAB_LEGACY_SENT_TO_CDC_DT")
    private LocalDate labLegacySentToCdcDt;

    @Column(name = "LAB_NAA_COLL_DT")
    private LocalDate labNaaCollDt;

    @Column(name = "LAB_NAA_RPT_DT")
    private LocalDate labNaaRptDt;

    @Column(name = "LAB_RT_PCR_TEST_DT")
    private LocalDate labRtPcrTestDt;

    @Column(name = "LAB_SMR_PATH_CYTO_COLL_DT")
    private LocalDate labSmrPathCytoCollDt;

    @Column(name = "LAB_SMR_PATH_CYTO_RPT_DT")
    private LocalDate labSmrPathCytoRptDt;

    @Column(name = "LAB_SPUTUM_CULT_COLL_DT")
    private LocalDate labSputumCultCollDt;

    @Column(name = "LAB_SPUTUM_CULT_RPT_DT")
    private LocalDate labSputumCultRptDt;

    @Column(name = "LAB_SPUTUM_SMEAR_COLL_DT")
    private LocalDate labSputumSmearCollDt;

    @Column(name = "LAB_SPUTUM_SMEAR_RPT_DT")
    private LocalDate labSputumSmearRptDt;

    @Column(name = "LAB_Supplem_antiHCV_Date")
    private LocalDate labSupplemAntihcvDate;

    @Column(name = "LAB_TB_SKIN_TST_PLACED_DT")
    private LocalDate labTbSkinTstPlacedDt;

    @Column(name = "LAB_TB_SKIN_TST_READ_DT")
    private LocalDate labTbSkinTstReadDt;

    @Column(name = "LAB_TestDate")
    private LocalDate labTestdate;

    @Column(name = "LAB_TestDate2")
    private LocalDate labTestdate2;

    @Column(name = "LAB_TotalAntiHAVDate")
    private LocalDate labTotalantihavdate;

    @Column(name = "LAB_TotalAntiHBcDate")
    private LocalDate labTotalantihbcdate;

    @Column(name = "LAB_TotalAntiHCV_Date")
    private LocalDate labTotalantihcvDate;

    @Column(name = "LAB_TotalAntiHDV_Date")
    private LocalDate labTotalantihdvDate;

    @Column(name = "LAB_TotalAntiHEV_Date")
    private LocalDate labTotalantihevDate;

    @Column(name = "LAB_VerifiedTestDate")
    private LocalDate labVerifiedtestdate;

    @jakarta.validation.constraints.Size(max = 1999)
    @Column(name = "FLU_A_RAPID_AG_RSLT", length = 1999)
    private String fluARapidAgRslt;

    @jakarta.validation.constraints.Size(max = 1999)
    @Column(name = "LAB_ADENOVIRUS_RSLT", length = 1999)
    private String labAdenovirusRslt;

    @jakarta.validation.constraints.Size(max = 1999)
    @Column(name = "LAB_ANTBDY_BGDF_HIGH_CSF", length = 1999)
    private String labAntbdyBgdfHighCsf;

    @jakarta.validation.constraints.Size(max = 1999)
    @Column(name = "LAB_ANTBDY_BGDF_TSTD_CSF", length = 1999)
    private String labAntbdyBgdfTstdCsf;

    @jakarta.validation.constraints.Size(max = 1999)
    @Column(name = "LAB_ANTIGEN_TEST_RESULT", length = 1999)
    private String labAntigenTestResult;

    @jakarta.validation.constraints.Size(max = 1999)
    @Column(name = "LAB_ANTIGEN_TESTING_IND", length = 1999)
    private String labAntigenTestingInd;

    @jakarta.validation.constraints.Size(max = 1999)
    @Column(name = "LAB_AntiHBsPositive", length = 1999)
    private String labAntihbspositive;

    @jakarta.validation.constraints.Size(max = 1999)
    @Column(name = "LAB_AntiHBsTested", length = 1999)
    private String labAntihbstested;

    @jakarta.validation.constraints.Size(max = 1999)
    @Column(name = "LAB_BAND_TEST_UNIT", length = 1999)
    private String labBandTestUnit;

    @jakarta.validation.constraints.Size(max = 1999)
    @Column(name = "LAB_BN_INTRP_RSLT", length = 1999)
    private String labBnIntrpRslt;

    @jakarta.validation.constraints.Size(max = 1999)
    @Column(name = "LAB_BNP_TEST_UNIT", length = 1999)
    private String labBnpTestUnit;

    @jakarta.validation.constraints.Size(max = 1999)
    @Column(name = "LAB_C_PNEUMONIAE_RSLT", length = 1999)
    private String labCPneumoniaeRslt;

    @jakarta.validation.constraints.Size(max = 1999)
    @Column(name = "LAB_CORONAVIRUS_RSLT", length = 1999)
    private String labCoronavirusRslt;

    @jakarta.validation.constraints.Size(max = 1999)
    @Column(name = "LAB_COVID_19_VARIANT", length = 1999)
    private String labCovid19Variant;

    @jakarta.validation.constraints.Size(max = 1999)
    @Column(name = "LAB_COVID_19_VARIANT_OTH", length = 1999)
    private String labCovid19VariantOth;

    @jakarta.validation.constraints.Size(max = 1999)
    @Column(name = "LAB_CRP_INTRP_RSLT", length = 1999)
    private String labCrpIntrpRslt;

    @jakarta.validation.constraints.Size(max = 1999)
    @Column(name = "LAB_CRP_TEST_UNIT", length = 1999)
    private String labCrpTestUnit;

    @jakarta.validation.constraints.Size(max = 1999)
    @Column(name = "LAB_CSF_PLEOCYTOSIS", length = 1999)
    private String labCsfPleocytosis;

    @jakarta.validation.constraints.Size(max = 1999)
    @Column(name = "LAB_CSF_PROTEIN_INTERP", length = 1999)
    private String labCsfProteinInterp;

    @jakarta.validation.constraints.Size(max = 1999)
    @Column(name = "LAB_CSF_VDRL", length = 1999)
    private String labCsfVdrl;

    @jakarta.validation.constraints.Size(max = 1999)
    @Column(name = "LAB_CSF_WBC_CNT_INTERP", length = 1999)
    private String labCsfWbcCntInterp;

    @jakarta.validation.constraints.Size(max = 1999)
    @Column(name = "LAB_CUL_CONV_DOCUM", length = 1999)
    private String labCulConvDocum;

    @jakarta.validation.constraints.Size(max = 1999)
    @Column(name = "LAB_CULTURE_OTH_SPM_SRC", length = 1999)
    private String labCultureOthSpmSrc;

    @jakarta.validation.constraints.Size(max = 1999)
    @Column(name = "LAB_CULTURE_OTH_SPM_SRC_OTH", length = 1999)
    private String labCultureOthSpmSrcOth;

    @jakarta.validation.constraints.Size(max = 1999)
    @Column(name = "LAB_CULTURE_OTHER_RSLT", length = 1999)
    private String labCultureOtherRslt;

    @jakarta.validation.constraints.Size(max = 1999)
    @Column(name = "LAB_D_DIMER_INTRP_RSLT", length = 1999)
    private String labDDimerIntrpRslt;

    @jakarta.validation.constraints.Size(max = 1999)
    @Column(name = "LAB_D_DIMER_UNIT", length = 1999)
    private String labDDimerUnit;

    @jakarta.validation.constraints.Size(max = 1999)
    @Column(name = "LAB_DKFLD_DFA_SPEC_STAINS", length = 1999)
    private String labDkfldDfaSpecStains;

    @jakarta.validation.constraints.Size(max = 1999)
    @Column(name = "LAB_EOSINOPHIL_NUMBER", length = 1999)
    private String labEosinophilNumber;

    @jakarta.validation.constraints.Size(max = 1999)
    @Column(name = "LAB_EOSINOPHIL_NUMBER_UNIT", length = 1999)
    private String labEosinophilNumberUnit;

    @jakarta.validation.constraints.Size(max = 1999)
    @Column(name = "LAB_EOSINOPHILIA_TESTING", length = 1999)
    private String labEosinophiliaTesting;

    @jakarta.validation.constraints.Size(max = 1999)
    @Column(name = "LAB_FERRITIN_INTRP_RSLT", length = 1999)
    private String labFerritinIntrpRslt;

    @jakarta.validation.constraints.Size(max = 1999)
    @Column(name = "LAB_FERRITIN_TEST_UNIT", length = 1999)
    private String labFerritinTestUnit;

    @jakarta.validation.constraints.Size(max = 1999)
    @Column(name = "LAB_FIBRINOGEN_INTRP_RSLT", length = 1999)
    private String labFibrinogenIntrpRslt;

    @jakarta.validation.constraints.Size(max = 1999)
    @Column(name = "LAB_FIBRINOGEN_TEST_UNIT", length = 1999)
    private String labFibrinogenTestUnit;

    @jakarta.validation.constraints.Size(max = 1999)
    @Column(name = "LAB_FLU_A_PCR_RSLT", length = 1999)
    private String labFluAPcrRslt;

    @jakarta.validation.constraints.Size(max = 1999)
    @Column(name = "LAB_FLU_B_PCR_RSLT", length = 1999)
    private String labFluBPcrRslt;

    @jakarta.validation.constraints.Size(max = 1999)
    @Column(name = "LAB_FLU_B_RAPID_AG_RSLT", length = 1999)
    private String labFluBRapidAgRslt;

    @jakarta.validation.constraints.Size(max = 1999)
    @Column(name = "LAB_GLUCOSE_HIGH_UNIT", length = 1999)
    private String labGlucoseHighUnit;

    @jakarta.validation.constraints.Size(max = 1999)
    @Column(name = "LAB_H_METAPNEUMOVRS_RSLT", length = 1999)
    private String labHMetapneumovrsRslt;

    @jakarta.validation.constraints.Size(max = 1999)
    @Column(name = "LAB_HBeAg", length = 1999)
    private String labHbeag;

    @jakarta.validation.constraints.Size(max = 1999)
    @Column(name = "LAB_HBsAg", length = 1999)
    private String labHbsag;

    @jakarta.validation.constraints.Size(max = 1999)
    @Column(name = "LAB_HBV_NAT", length = 1999)
    private String labHbvNat;

    @jakarta.validation.constraints.Size(max = 1999)
    @Column(name = "LAB_HCVRNA", length = 1999)
    private String labHcvrna;

    @jakarta.validation.constraints.Size(max = 1999)
    @Column(name = "LAB_HepDTest", length = 1999)
    private String labHepdtest;

    @jakarta.validation.constraints.Size(max = 1999)
    @Column(name = "LAB_IGA_TEST_RESULT", length = 1999)
    private String labIgaTestResult;

    @jakarta.validation.constraints.Size(max = 1999)
    @Column(name = "LAB_IGG_TEST_RESULT", length = 1999)
    private String labIggTestResult;

    @jakarta.validation.constraints.Size(max = 1999)
    @Column(name = "LAB_IgM_AntiHAV", length = 1999)
    private String labIgmAntihav;

    @jakarta.validation.constraints.Size(max = 1999)
    @Column(name = "LAB_IGM_TEST_RESULT", length = 1999)
    private String labIgmTestResult;

    @jakarta.validation.constraints.Size(max = 1999)
    @Column(name = "LAB_IgMAntiHBc", length = 1999)
    private String labIgmantihbc;

    @jakarta.validation.constraints.Size(max = 1999)
    @Column(name = "LAB_IGRA_QUAL_RSLT", length = 1999)
    private String labIgraQualRslt;

    @jakarta.validation.constraints.Size(max = 1999)
    @Column(name = "LAB_IGRA_QUANT_RSLT_UNIT", length = 1999)
    private String labIgraQuantRsltUnit;

    @jakarta.validation.constraints.Size(max = 1999)
    @Column(name = "LAB_IGRA_TEST_TYPE", length = 1999)
    private String labIgraTestType;

    @jakarta.validation.constraints.Size(max = 1999)
    @Column(name = "LAB_IL_6_INTRP_RSLT", length = 1999)
    private String labIl6IntrpRslt;

    @jakarta.validation.constraints.Size(max = 1999)
    @Column(name = "LAB_IL_6_UNIT", length = 1999)
    private String labIl6Unit;

    @jakarta.validation.constraints.Size(max = 1999)
    @Column(name = "LAB_ISO_SUBMTD_GENOTYPNG", length = 1999)
    private String labIsoSubmtdGenotypng;

    @jakarta.validation.constraints.Size(max = 1999)
    @Column(name = "LAB_LAB_CNFM_CO_INFECTION", length = 1999)
    private String labLabCnfmCoInfection;

    @jakarta.validation.constraints.Size(max = 1999)
    @Column(name = "LAB_LAB_TESTING_BY", length = 1999)
    private String labLabTestingBy;

    @jakarta.validation.constraints.Size(max = 1999)
    @Column(name = "LAB_LABORATORY_CONFIRMED_IND", length = 1999)
    private String labLaboratoryConfirmedInd;

    @jakarta.validation.constraints.Size(max = 1999)
    @Column(name = "LAB_LYMPHOCYTES_UNIT", length = 1999)
    private String labLymphocytesUnit;

    @jakarta.validation.constraints.Size(max = 1999)
    @Column(name = "LAB_M_PNEUMONIAE_RSLT", length = 1999)
    private String labMPneumoniaeRslt;

    @jakarta.validation.constraints.Size(max = 1999)
    @Column(name = "LAB_MOLE_DRG_SUSC_COM", length = 1999)
    private String labMoleDrgSuscCom;

    @jakarta.validation.constraints.Size(max = 1999)
    @Column(name = "LAB_NAA_RSLT", length = 1999)
    private String labNaaRslt;

    @jakarta.validation.constraints.Size(max = 1999)
    @Column(name = "LAB_NAA_SPM_SRC", length = 1999)
    private String labNaaSpmSrc;

    @jakarta.validation.constraints.Size(max = 1999)
    @Column(name = "LAB_NAA_SPM_SRC_OTH", length = 1999)
    private String labNaaSpmSrcOth;

    @jakarta.validation.constraints.Size(max = 1999)
    @Column(name = "LAB_NEUTROPHILS_UNIT", length = 1999)
    private String labNeutrophilsUnit;

    @jakarta.validation.constraints.Size(max = 1999)
    @Column(name = "LAB_NONTREP_SYPH_RSLT_QNT", length = 1999)
    private String labNontrepSyphRsltQnt;

    @jakarta.validation.constraints.Size(max = 1999)
    @Column(name = "LAB_NONTREP_SYPH_RSLT_QUA", length = 1999)
    private String labNontrepSyphRsltQua;

    @jakarta.validation.constraints.Size(max = 1999)
    @Column(name = "LAB_NONTREP_SYPH_TEST_TYP", length = 1999)
    private String labNontrepSyphTestTyp;

    @jakarta.validation.constraints.Size(max = 1999)
    @Column(name = "LAB_NT_PROBNP_INTRP_RSLT", length = 1999)
    private String labNtProbnpIntrpRslt;

    @jakarta.validation.constraints.Size(max = 1999)
    @Column(name = "LAB_NT_PROBNP_UNIT", length = 1999)
    private String labNtProbnpUnit;

    @jakarta.validation.constraints.Size(max = 1999)
    @Column(name = "LAB_OTH_PATHOGEN_TEST_IND", length = 1999)
    private String labOthPathogenTestInd;

    @jakarta.validation.constraints.Size(max = 1999)
    @Column(name = "LAB_PARAINFLUENZA1_4_RSLT", length = 1999)
    private String labParainfluenza14Rslt;

    @jakarta.validation.constraints.Size(max = 1999)
    @Column(name = "LAB_PHENO_DRG_SUSC_COM", length = 1999)
    private String labPhenoDrgSuscCom;

    @jakarta.validation.constraints.Size(max = 1999)
    @Column(name = "LAB_PLATELETS_UNIT", length = 1999)
    private String labPlateletsUnit;

    @jakarta.validation.constraints.Size(max = 1999)
    @Column(name = "LAB_PrevNegHepTest", length = 1999)
    private String labPrevnegheptest;

    @jakarta.validation.constraints.Size(max = 1999)
    @Column(name = "LAB_PROTEIN_TEST_UNIT", length = 1999)
    private String labProteinTestUnit;

    @jakarta.validation.constraints.Size(max = 1999)
    @Column(name = "LAB_PT_SPEC_COLLECT_IND", length = 1999)
    private String labPtSpecCollectInd;

    @jakarta.validation.constraints.Size(max = 1999)
    @Column(name = "LAB_RDN_NT_DOCUM_SCC", length = 1999)
    private String labRdnNtDocumScc;

    @jakarta.validation.constraints.Size(max = 1999)
    @Column(name = "LAB_RDN_NT_DOCUM_SCC_OTH", length = 1999)
    private String labRdnNtDocumSccOth;

    @jakarta.validation.constraints.Size(max = 1999)
    @Column(name = "LAB_RHINO_ENTERO_RSLT", length = 1999)
    private String labRhinoEnteroRslt;

    @jakarta.validation.constraints.Size(max = 1999)
    @Column(name = "LAB_RSV_RSLT", length = 1999)
    private String labRsvRslt;

    @jakarta.validation.constraints.Size(max = 1999)
    @Column(name = "LAB_RT_PCR_TEST_RESULT", length = 1999)
    private String labRtPcrTestResult;

    @jakarta.validation.constraints.Size(max = 1999)
    @Column(name = "LAB_RT_PCR_TESTING_IND", length = 1999)
    private String labRtPcrTestingInd;

    @jakarta.validation.constraints.Size(max = 1999)
    @Column(name = "LAB_SEROLOGY_TESTING_IND", length = 1999)
    private String labSerologyTestingInd;

    @jakarta.validation.constraints.Size(max = 1999)
    @Column(name = "LAB_SERUM_PAIR_ANT_RESULT", length = 1999)
    private String labSerumPairAntResult;

    @jakarta.validation.constraints.Size(max = 1999)
    @Column(name = "LAB_SERUM_WBC_UNIT", length = 1999)
    private String labSerumWbcUnit;

    @jakarta.validation.constraints.Size(max = 1999)
    @Column(name = "LAB_SMR_PATH_CYTO_RSLT", length = 1999)
    private String labSmrPathCytoRslt;

    @jakarta.validation.constraints.Size(max = 1999)
    @Column(name = "LAB_SMR_PATH_CYTO_SPM", length = 1999)
    private String labSmrPathCytoSpm;

    @jakarta.validation.constraints.Size(max = 1999)
    @Column(name = "LAB_SMR_PATH_CYTO_SPM_OTH", length = 1999)
    private String labSmrPathCytoSpmOth;

    @jakarta.validation.constraints.Size(max = 1999)
    @Column(name = "LAB_SMR_PATH_CYTO_TST_TYP", length = 1999)
    private String labSmrPathCytoTstTyp;

    @jakarta.validation.constraints.Size(max = 1999)
    @Column(name = "LAB_SPEC_CLCTN_LOC_TYP", length = 1999)
    private String labSpecClctnLocTyp;

    @jakarta.validation.constraints.Size(max = 1999)
    @Column(name = "LAB_SPEC_CLCTN_LOC_TYP_OTH", length = 1999)
    private String labSpecClctnLocTypOth;

    @jakarta.validation.constraints.Size(max = 1999)
    @Column(name = "LAB_SPEC_COLLECT_FAC_CNTY", length = 1999)
    private String labSpecCollectFacCnty;

    @jakarta.validation.constraints.Size(max = 1999)
    @Column(name = "LAB_SPEC_COLLECT_FAC_CNTY_OTH", length = 1999)
    private String labSpecCollectFacCntyOth;

    @jakarta.validation.constraints.Size(max = 1999)
    @Column(name = "LAB_SPEC_COLLECT_FAC_STT", length = 1999)
    private String labSpecCollectFacStt;

    @jakarta.validation.constraints.Size(max = 1999)
    @Column(name = "LAB_SPECIMEN_SENT_TO_CDC_IND", length = 1999)
    private String labSpecimenSentToCdcInd;

    @jakarta.validation.constraints.Size(max = 1999)
    @Column(name = "LAB_SPUTUM_CULT_RSLT", length = 1999)
    private String labSputumCultRslt;

    @jakarta.validation.constraints.Size(max = 1999)
    @Column(name = "LAB_SPUTUM_SMEAR_RSLT", length = 1999)
    private String labSputumSmearRslt;

    @jakarta.validation.constraints.Size(max = 1999)
    @Column(name = "LAB_Supplem_antiHCV", length = 1999)
    private String labSupplemAntihcv;

    @jakarta.validation.constraints.Size(max = 1999)
    @Column(name = "LAB_SYPHILIS_TST_PS_IND", length = 1999)
    private String labSyphilisTstPsInd;

    @jakarta.validation.constraints.Size(max = 1999)
    @Column(name = "LAB_SYPHILIS_TST_RSLT_PS", length = 1999)
    private String labSyphilisTstRsltPs;

    @jakarta.validation.constraints.Size(max = 1999)
    @Column(name = "LAB_TB_SKIN_TST_RSLT", length = 1999)
    private String labTbSkinTstRslt;

    @jakarta.validation.constraints.Size(max = 1999)
    @Column(name = "LAB_TCHSTY_AT_SPEC_CLCTN", length = 1999)
    private String labTchstyAtSpecClctn;

    @jakarta.validation.constraints.Size(max = 1999)
    @Column(name = "LAB_TESTING_PERFORMED_IND", length = 1999)
    private String labTestingPerformedInd;

    @jakarta.validation.constraints.Size(max = 1999)
    @Column(name = "LAB_TESTS_PERFORMED", length = 1999)
    private String labTestsPerformed;

    @jakarta.validation.constraints.Size(max = 1999)
    @Column(name = "LAB_TotalAntiHAV", length = 1999)
    private String labTotalantihav;

    @jakarta.validation.constraints.Size(max = 1999)
    @Column(name = "LAB_TotalAntiHBc", length = 1999)
    private String labTotalantihbc;

    @jakarta.validation.constraints.Size(max = 1999)
    @Column(name = "LAB_TotalAntiHCV", length = 1999)
    private String labTotalantihcv;

    @jakarta.validation.constraints.Size(max = 1999)
    @Column(name = "LAB_TotalAntiHDV", length = 1999)
    private String labTotalantihdv;

    @jakarta.validation.constraints.Size(max = 1999)
    @Column(name = "LAB_TotalAntiHEV", length = 1999)
    private String labTotalantihev;

    @jakarta.validation.constraints.Size(max = 1999)
    @Column(name = "LAB_TREP_SYPH_RESULT_QUAL", length = 1999)
    private String labTrepSyphResultQual;

    @jakarta.validation.constraints.Size(max = 1999)
    @Column(name = "LAB_TREP_SYPH_TEST_TYPE", length = 1999)
    private String labTrepSyphTestType;

    @jakarta.validation.constraints.Size(max = 1999)
    @Column(name = "LAB_TROPONIN_INTRP_RSLT", length = 1999)
    private String labTroponinIntrpRslt;

    @jakarta.validation.constraints.Size(max = 1999)
    @Column(name = "LAB_TROPONIN_TEST_UNIT", length = 1999)
    private String labTroponinTestUnit;

    @jakarta.validation.constraints.Size(max = 1999)
    @Column(name = "LAB_URINE_WBC_UNIT", length = 1999)
    private String labUrineWbcUnit;

    @jakarta.validation.constraints.Size(max = 1999)
    @Column(name = "LAB_VNTILTR_AT_SPEC_CLCTN", length = 1999)
    private String labVntiltrAtSpecClctn;

    @jakarta.validation.constraints.Size(max = 1999)
    @Column(name = "LAB_WBC_UNIT", length = 1999)
    private String labWbcUnit;

    @jakarta.validation.constraints.Size(max = 1999)
    @Column(name = "LABDENGUE_SEROTYPE", length = 1999)
    private String labdengueSerotype;

    @jakarta.validation.constraints.Size(max = 2000)
    @Column(name = "LAB_IGRA_QUANT_TEST_RSLT", length = 2000)
    private String labIgraQuantTestRslt;

    @jakarta.validation.constraints.Size(max = 2000)
    @Column(name = "LAB_LEGACY_SPCMN_TY_CDC", length = 2000)
    private String labLegacySpcmnTyCdc;

    @jakarta.validation.constraints.Size(max = 2000)
    @Column(name = "LAB_SignalToCutoff", length = 2000)
    private String labSignaltocutoff;

    @jakarta.validation.constraints.Size(max = 2000)
    @Column(name = "LAB_VPD_LAB_MSG_SPECIMEN_ID", length = 2000)
    private String labVpdLabMsgSpecimenId;

}