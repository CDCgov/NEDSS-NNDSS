package gov.cdc.nnddatapollservice.repository.rdb_modern.model;

import gov.cdc.nnddatapollservice.universal.dto.NrtObservationCodedDto;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "nrt_observation_coded")
public class NrtObservationCoded {

    @Id
    @Column(name = "observation_uid", nullable = false)
    private Long observationUid;

    @Column(name = "ovc_code", nullable = false, length = 20)
    private String ovcCode;

    @Column(name = "ovc_code_system_cd", length = 300)
    private String ovcCodeSystemCd;

    @Column(name = "ovc_code_system_desc_txt", length = 100)
    private String ovcCodeSystemDescTxt;

    @Column(name = "ovc_display_name", length = 300)
    private String ovcDisplayName;

    @Column(name = "ovc_alt_cd", length = 50)
    private String ovcAltCd;

    @Column(name = "ovc_alt_cd_desc_txt", length = 100)
    private String ovcAltCdDescTxt;

    @Column(name = "ovc_alt_cd_system_cd", length = 300)
    private String ovcAltCdSystemCd;

    @Column(name = "ovc_alt_cd_system_desc_txt", length = 100)
    private String ovcAltCdSystemDescTxt;



    public NrtObservationCoded() {

    }

    public NrtObservationCoded(NrtObservationCodedDto dto) {
        observationUid = dto.getObservationUid();
        ovcCode = dto.getOvcCode();
        ovcCodeSystemCd = dto.getOvcCodeSystemCd();
        ovcCodeSystemDescTxt = dto.getOvcCodeSystemDescTxt();
        ovcDisplayName = dto.getOvcDisplayName();
        ovcAltCd = dto.getOvcAltCd();
        ovcAltCdDescTxt = dto.getOvcAltCdDescTxt();
        ovcAltCdSystemCd = dto.getOvcAltCdSystemCd();
        ovcAltCdSystemDescTxt = dto.getOvcAltCdSystemDescTxt();
    }

}