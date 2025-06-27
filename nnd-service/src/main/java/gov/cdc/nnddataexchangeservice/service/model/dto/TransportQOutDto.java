package gov.cdc.nnddataexchangeservice.service.model.dto;

import gov.cdc.nnddataexchangeservice.repository.msg.model.TransportQOut;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TransportQOutDto {
    private Long recordId;
    private String messageId;
    private String payloadFile;
    private byte[] payloadContent;
    private String destinationFilename;
    private String routeInfo;
    private String service;
    private String action;
    private String arguments;
    private String messageRecipient;
    private String messageCreationTime;
    private String encryption;
    private String signature;
    private String publicKeyLdapAddress;
    private String publicKeyLdapBaseDN;
    private String publicKeyLdapDN;
    private String certificateURL;
    private String processingStatus;
    private String transportStatus;
    private String transportErrorCode;
    private String applicationStatus;
    private String applicationErrorCode;
    private String applicationResponse;
    private String messageSentTime;
    private String messageReceivedTime;
    private String responseMessageId;
    private String responseArguments;
    private String responseLocalFile;
    private String responseFilename;
    private byte[] responseContent;
    private String responseMessageOrigin;
    private String responseMessageSignature;
    private Integer priority;

    public TransportQOutDto() {

    }
    // Constructor to convert domain model to DTO
    public TransportQOutDto(TransportQOut transportQOut) {
        this.recordId = transportQOut.getRecordId();
        this.messageId = transportQOut.getMessageId();
        this.payloadFile = transportQOut.getPayloadFile();
        this.payloadContent = transportQOut.getPayloadContent();
        this.destinationFilename = transportQOut.getDestinationFilename();
        this.routeInfo = transportQOut.getRouteInfo();
        this.service = transportQOut.getService();
        this.action = transportQOut.getAction();
        this.arguments = transportQOut.getArguments();
        this.messageRecipient = transportQOut.getMessageRecipient();
        this.messageCreationTime = transportQOut.getMessageCreationTime();
        this.encryption = transportQOut.getEncryption();
        this.signature = transportQOut.getSignature();
        this.publicKeyLdapAddress = transportQOut.getPublicKeyLdapAddress();
        this.publicKeyLdapBaseDN = transportQOut.getPublicKeyLdapBaseDN();
        this.publicKeyLdapDN = transportQOut.getPublicKeyLdapDN();
        this.certificateURL = transportQOut.getCertificateURL();
        this.processingStatus = transportQOut.getProcessingStatus();
        this.transportStatus = transportQOut.getTransportStatus();
        this.transportErrorCode = transportQOut.getTransportErrorCode();
        this.applicationStatus = transportQOut.getApplicationStatus();
        this.applicationErrorCode = transportQOut.getApplicationErrorCode();
        this.applicationResponse = transportQOut.getApplicationResponse();
        this.messageSentTime = transportQOut.getMessageSentTime();
        this.messageReceivedTime = transportQOut.getMessageReceivedTime();
        this.responseMessageId = transportQOut.getResponseMessageId();
        this.responseArguments = transportQOut.getResponseArguments();
        this.responseLocalFile = transportQOut.getResponseLocalFile();
        this.responseFilename = transportQOut.getResponseFilename();
        this.responseContent = transportQOut.getResponseContent();
        this.responseMessageOrigin = transportQOut.getResponseMessageOrigin();
        this.responseMessageSignature = transportQOut.getResponseMessageSignature();
        this.priority = transportQOut.getPriority();
    }
}
