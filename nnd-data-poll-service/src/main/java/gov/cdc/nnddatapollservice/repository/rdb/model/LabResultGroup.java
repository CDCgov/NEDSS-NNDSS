package gov.cdc.nnddatapollservice.repository.rdb.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "LAB_RESULT_GROUP")
public class LabResultGroup {
    @Id
    @Column(name = "LAB_RESULT_GRP_KEY", nullable = false)
    private Long id;

    //TODO [Reverse Engineering] generate columns from DB
}