package reportingapi.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;
import reportingapi.model.ApplicationRequest;
import reportingapi.model.ApplicationVO;
import reportingapi.service.ReportingService;

import java.util.List;

@RestController
@RequestMapping("api/v1/reports")
@Slf4j
public class ReportingController {

    @Autowired
    ReportingService reportingService;

    // rest template request to reportingserviceimpl

    @GetMapping
    public ResponseEntity<List<ApplicationVO>> getApplications() {
        log.info("Inside the ReportingController.getApplications");
        List<ApplicationVO> applicationVOS = null;
        try {
            applicationVOS = reportingService.findAll();
            log.info("Reporting response:{}", applicationVOS);
            if (CollectionUtils.isEmpty(applicationVOS)) {
                log.info("Reporting details are not found");
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (Exception ex) {
            log.error("Exception while calling getApplications", ex);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<List<ApplicationVO>>(applicationVOS, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApplicationVO> getApplicationById(@PathVariable("id") long id) {
        log.info("Input to ReportingController.getApplicationById, id:{}", id);
        ApplicationVO applicationVO = null;
        try {
            applicationVO = reportingService.findById(id);
            log.info("Reporting details for the application id:{}, and details:{}", id, applicationVO);
            if (applicationVO == null) {
                log.info("Reporting details not found");
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (Exception ex) {
            log.info("Exception error while processing the ApplicationController.getApplicationById", ex);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<ApplicationVO>(applicationVO, HttpStatus.OK);

    }

    @GetMapping("/appName/id")
    public ResponseEntity<ApplicationVO> getApplicationByName(@RequestParam("name") String appName, @RequestParam("id") long id){
        log.info("Input to ReportingController.getApplicationByName,appName:{} id:{}", appName, id);
        ApplicationVO applicationVO = null;
        try {
            applicationVO = reportingService.findByName(appName, id);
            log.info("Reporting details for the application id:{}, and details:{}", appName, id, applicationVO);
            if (applicationVO == null) {
                log.info("Reporting details not found");
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (Exception ex) {
            log.info("Exception error while processing the ApplicationController.getApplicationByName", ex);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<ApplicationVO>(applicationVO, HttpStatus.OK);

    }
    @PostMapping
    public ResponseEntity<ApplicationVO> save(@RequestBody ApplicationRequest applicationRequest) {
        log.info("Inside ReportingController.save and applicationVO;{}", applicationRequest);
        if (applicationRequest == null) {
            log.info("Invalid Application request");
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        ApplicationVO applicationVO = null;
        try {
            reportingService.saveNew(applicationRequest);
                log.info("Application details are saved");


        } catch (Exception ex) {
            if(applicationVO == null){
            log.error("Exception while saving application");
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);}
        }
        return new ResponseEntity<>(applicationVO, HttpStatus.OK);
    }

    @PutMapping
    public ResponseEntity<String> updateApplication(@RequestBody ApplicationRequest applicationRequest) {
        log.info("Inside ReportingController.update, applicationVO:{}", applicationRequest);
        if (applicationRequest == null) {
            log.info("Invalid Application Request");
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        try {
            reportingService.updateNew(applicationRequest);
        } catch (Exception ex) {
            log.error("Exception while saving applications/reporting");
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>("Reporting has been updated", HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApplicationVO> deleteApplicationById(@PathVariable("id") long id){
        log.info("Input to ReportingController.deleteApplicationById, id:{}", id);
        String applicationVO=null;
        try {
            reportingService.delete(id);
            log.info("Delete Application details for the application id:{}, and details:{}", id, applicationVO);
            log.info("Reporting details found");
            return new ResponseEntity<>(HttpStatus.OK);

        } catch (Exception ex) {
            if (applicationVO == null) {
                log.info("Reporting details not found");
                log.info("Exception error while processing the ReportingController.deleteApplicationById", ex);
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        }return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }

}

