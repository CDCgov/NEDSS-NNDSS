package gov.cdc.nnddatapollservice.nbs_odse.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EDXActivityDetailLogDto {
    private Long edxActivityDetailLogUid;
    private Long edxActivityLogUid; // Reference to the foreign key (edx_activity_log_uid)
    private String recordId;
    private String recordType;
    private String recordName;
    private String logType;
    private String logComment;
}