package reportingapi.service;

import reportingapi.model.ApplicationRequest;
import reportingapi.model.ApplicationVO;

import java.net.URISyntaxException;
import java.util.List;

public interface ReportingService {

    List<ApplicationVO> findAll() throws URISyntaxException;

    List<ApplicationVO> findAllNew() throws URISyntaxException;
    ApplicationVO findById(long id) throws URISyntaxException;
    ApplicationVO findByIdWeb(long id) throws URISyntaxException;
    String saveNew(ApplicationRequest request) throws URISyntaxException;

    void updateNew(ApplicationRequest application) throws Exception;

    ApplicationVO updateNewWeb(ApplicationRequest request) throws Exception;
    ApplicationVO saveWeb(ApplicationRequest request) throws URISyntaxException;
    void delete(long id) throws URISyntaxException;

    Void deleteWeb(long id);

    ApplicationVO findByName(String name, long id) throws InterruptedException;

    ApplicationVO findByNameRest(String name, long id) throws URISyntaxException;


}
