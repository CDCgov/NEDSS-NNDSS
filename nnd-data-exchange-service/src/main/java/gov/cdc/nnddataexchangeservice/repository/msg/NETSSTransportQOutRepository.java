package gov.cdc.nnddataexchangeservice.repository.msg;

import gov.cdc.nnddataexchangeservice.repository.msg.model.NETSSTransportQOut;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.Collection;
import java.util.Optional;

@Repository
public interface NETSSTransportQOutRepository extends JpaRepository<NETSSTransportQOut, Long> {
    @Query("SELECT a FROM NETSSTransportQOut a")
    Optional<Collection<NETSSTransportQOut>> findNetssTransport ();

    @Query("SELECT a FROM NETSSTransportQOut a WHERE a.addTime > :recordStatusTime")
    Optional<Collection<NETSSTransportQOut>> findNetssTransportByCreationTime (@Param("recordStatusTime") Timestamp recordStatusTime);

    @Query(value = "SELECT TOP (:limit) a.* FROM NETSS_TransportQ_out a ORDER BY a.add_time ASC ", nativeQuery = true)
    Optional<Collection<NETSSTransportQOut>> findNetssTransportWLimit(@Param("limit") int limit);

    @Query(value = "SELECT TOP (:limit) a.*  FROM NETSS_TransportQ_out a WHERE a.add_time > :recordStatusTime ORDER BY a.add_time ASC", nativeQuery = true)
    Optional<Collection<NETSSTransportQOut>> findNetssTransportByCreationTimeWLimit (@Param("recordStatusTime") Timestamp recordStatusTime,
                                                                                     @Param("limit") int limit);


}
