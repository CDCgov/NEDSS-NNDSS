package gov.cdc.nnddataexchangeservice.repository.msg.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "TransportQ_out")
@Data
public class TransportQOut {
    @Id
    @Column(name = "recordId", nullable = false)
    private Long recordId;

    @Column(name = "messageId")
    private String messageId;

    @Column(name = "payloadFile")
    private String payloadFile;

    @Lob
    @Column(name = "payloadContent")
    private byte[] payloadContent;

    @Column(name = "destinationFilename")
    private String destinationFilename;

    @Column(name = "routeInfo", nullable = false)
    private String routeInfo;

    @Column(name = "service", nullable = false)
    private String service;

    @Column(name = "action", nullable = false)
    private String action;

    @Column(name = "arguments")
    private String arguments;

    @Column(name = "messageRecipient")
    private String messageRecipient;

    @Column(name = "messageCreationTime")
    private String messageCreationTime;

    @Column(name = "encryption", nullable = false)
    private String encryption;

    @Column(name = "signature", nullable = false)
    private String signature;

    @Column(name = "publicKeyLdapAddress")
    private String publicKeyLdapAddress;

    @Column(name = "publicKeyLdapBaseDN")
    private String publicKeyLdapBaseDN;

    @Column(name = "publicKeyLdapDN")
    private String publicKeyLdapDN;

    @Column(name = "certificateURL")
    private String certificateURL;

    @Column(name = "processingStatus")
    private String processingStatus;

    @Column(name = "transportStatus")
    private String transportStatus;

    @Column(name = "transportErrorCode")
    private String transportErrorCode;

    @Column(name = "applicationStatus")
    private String applicationStatus;

    @Column(name = "applicationErrorCode")
    private String applicationErrorCode;

    @Column(name = "applicationResponse")
    private String applicationResponse;

    @Column(name = "messageSentTime")
    private String messageSentTime;

    @Column(name = "messageReceivedTime")
    private String messageReceivedTime;

    @Column(name = "responseMessageId")
    private String responseMessageId;

    @Column(name = "responseArguments")
    private String responseArguments;

    @Column(name = "responseLocalFile")
    private String responseLocalFile;

    @Column(name = "responseFilename")
    private String responseFilename;

    @Lob
    @Column(name = "responseContent")
    private byte[] responseContent;

    @Column(name = "responseMessageOrigin")
    private String responseMessageOrigin;

    @Column(name = "responseMessageSignature")
    private String responseMessageSignature;

    @Column(name = "priority")
    private Integer priority;
}
