package gov.cdc.nnddatapollservice.repository.rdb.model;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
@Entity
@Table(name = "Confirmation_method")
public class ConfirmationMethod {
    @EmbeddedId
    private ConfirmationMethodId id;

    @jakarta.validation.constraints.Size(max = 100)
    @Column(name = "confirmation_method_desc_txt", length = 100)
    private String confirmationMethodDescTxt;

    @Column(name = "confirmation_method_time")
    private Instant confirmationMethodTime;

}