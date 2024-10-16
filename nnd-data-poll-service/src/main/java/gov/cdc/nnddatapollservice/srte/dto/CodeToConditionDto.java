package gov.cdc.nnddatapollservice.srte.dto;
import com.google.gson.annotations.SerializedName;
import lombok.Data;

@Data
public class CodeToConditionDto {

    @SerializedName("code_system_cd")
    private String codeSystemCd;

    @SerializedName("code_system_desc_txt")
    private String codeSystemDescTxt;

    @SerializedName("code")
    private String code;

    @SerializedName("code_desc_txt")
    private String codeDescTxt;

    @SerializedName("code_system_version_id")
    private String codeSystemVersionId;

    @SerializedName("condition_cd")
    private String conditionCd;

    @SerializedName("disease_nm")
    private String diseaseNm;

    @SerializedName("status_cd")
    private Character statusCd;

    @SerializedName("status_time")
    private String statusTime; // Use String if you plan to serialize the datetime

    @SerializedName("nbs_uid")
    private Integer nbsUid;

    @SerializedName("effective_from_time")
    private String effectiveFromTime; // Use String for datetime serialization

    @SerializedName("effective_to_time")
    private String effectiveToTime; // Use String for datetime serialization
}

