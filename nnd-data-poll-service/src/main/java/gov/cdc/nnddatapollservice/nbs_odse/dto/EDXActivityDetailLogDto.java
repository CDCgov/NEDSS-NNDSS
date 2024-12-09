package gov.cdc.nnddatapollservice.nbs_odse.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.SerializedName;
import lombok.*;

@Getter
@Setter
public class EDXActivityDetailLogDto {

    @SerializedName("edx_activity_detail_log_uid")
    private Long edxActivityDetailLogUid;

    @SerializedName("edx_activity_log_uid")
    private Long edxActivityLogUid; // Reference to the foreign key (edx_activity_log_uid)

    @SerializedName("record_id")
    private String recordId;

    @SerializedName("record_type")
    private String recordType;

    @SerializedName("record_name")
    private String recordName;

    @SerializedName("log_type")
    private String logType;

    @SerializedName("log_comment")
    private String logComment;
}