package gov.cdc.nnddatapollservice.rdbmodern.dto;

import com.google.gson.annotations.SerializedName;
import lombok.Getter;
import lombok.Setter;


import java.sql.Timestamp;

@Getter
@Setter
public class NrtObservationCodedDto {

    @SerializedName("observation_uid")
    private Long observationUid;

    @SerializedName("ovc_code")
    private String ovcCode;

    @SerializedName("ovc_code_system_cd")
    private String ovcCodeSystemCd;

    @SerializedName("ovc_code_system_desc_txt")
    private String ovcCodeSystemDescTxt;

    @SerializedName("ovc_display_name")
    private String ovcDisplayName;

    @SerializedName("ovc_alt_cd")
    private String ovcAltCd;

    @SerializedName("ovc_alt_cd_desc_txt")
    private String ovcAltCdDescTxt;

    @SerializedName("ovc_alt_cd_system_cd")
    private String ovcAltCdSystemCd;

    @SerializedName("ovc_alt_cd_system_desc_txt")
    private String ovcAltCdSystemDescTxt;

    @SerializedName("max_datetime")
    private Timestamp maxDatetime;
}
