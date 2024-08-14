package gov.cdc.nnddatapollservice.repository.rdb.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
@Entity
@Table(name = "CONFIRMATION_METHOD_GROUP")
public class ConfirmationMethodGroup {
    @EmbeddedId
    private ConfirmationMethodGroupId id;

    @MapsId("confirmationMethodKey")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "CONFIRMATION_METHOD_KEY", nullable = false)
    private ConfirmationMethod confirmationMethodKey;

    @Column(name = "CONFIRMATION_DT")
    private Instant confirmationDt;

}