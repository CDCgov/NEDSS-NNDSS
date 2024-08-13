package gov.cdc.nnddataexchangeservice.repository.rdb;

import gov.cdc.nnddataexchangeservice.repository.odse.model.CNTransportQOut;
import gov.cdc.nnddataexchangeservice.repository.rdb.model.DataExchangeConfig;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.Optional;

@Repository
public interface DataExchangeConfigRepository extends JpaRepository<DataExchangeConfig, String> {

}
