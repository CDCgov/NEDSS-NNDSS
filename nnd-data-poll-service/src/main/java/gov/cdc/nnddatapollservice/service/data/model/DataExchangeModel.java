package gov.cdc.nnddatapollservice.service.data.model;

import gov.cdc.nnddatapollservice.service.data.model.dto.CNTransportQOutDto;
import gov.cdc.nnddatapollservice.service.data.model.dto.NETSSTransportQOutDto;
import gov.cdc.nnddatapollservice.service.data.model.dto.TransportQOutDto;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;


@Getter
@Setter
public class DataExchangeModel {
    List<CNTransportQOutDto> cnTransportQOutDtoList = new ArrayList<>();
    List<NETSSTransportQOutDto> netssTransportQOutDtoList = new ArrayList<>();
    List<TransportQOutDto> transportQOutDtoList = new ArrayList<>();
}
