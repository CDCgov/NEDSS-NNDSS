package gov.cdc.nnddatapollservice.nbs_odse.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EDXActivityLogDto {

    private Long edxActivityLogUid;
    private Long sourceUid;
    private Long targetUid;
    private String docType;
    private String recordStatusCd;
    private Timestamp recordStatusTime;
    private String exceptionTxt;
    private String impExpIndCd;
    private String sourceTypeCd;
    private String targetTypeCd;
    private String businessObjLocalId;
    private String docNm;
    private String sourceNm;
    private String algorithmAction;
    private String algorithmName;
    private String messageId;
    private String entityNm;
    private String accessionNbr;
}
