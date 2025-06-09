package gov.cdc.nnddataexchangeservice.repository.rdb;

import gov.cdc.nnddataexchangeservice.repository.rdb.model.DataViewConfig;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface DataViewConfigRepository  extends JpaRepository<DataViewConfig, Long> {
    @Query(value = "SELECT * FROM data_view_config a " +
            "WHERE a.query_name = :queryName ",
            nativeQuery = true)
    DataViewConfig findByName(@Param("queryName")  String queryName);
}


/**
 *
 * parameter: test = ? and test2 = ? and test3 in ?
 * api parm: /test?param=test|test, test|test, test, test
 * */