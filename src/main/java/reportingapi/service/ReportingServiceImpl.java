package reportingapi.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.support.BasicAuthenticationInterceptor;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.EnableRetry;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reportingapi.model.ApplicationRequest;
import reportingapi.model.ApplicationVO;

import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;


@EnableRetry
@Slf4j
@Service
public class ReportingServiceImpl implements ReportingService{

    @Autowired
    RestTemplate restTemplate;

    @Value("${application.url}")
    private String applicationUrl;

    public ReportingServiceImpl(RestTemplateBuilder restTemplateBuilder) {
        restTemplate = restTemplateBuilder
                                .build();
    }
    HttpHeaders createHeaders(String username, String password) {
        return new HttpHeaders() {{
            final String basicAuth = HttpHeaders.encodeBasicAuth(username, password, StandardCharsets.US_ASCII);
            setBasicAuth(basicAuth);
        }};
    }


   @PreAuthorize("hasRole('ADMIN')")
   // @Retryable(value = {Exception.class}, maxAttempts = 5, backoff = @Backoff(delay=6000))
    @Override
    public List<ApplicationVO> findAll() throws URISyntaxException {
        log.info("Inside ReportingServiceImpl.findAll, and applicationGetAllUrl:{}" );

        URI uri = new URI(applicationUrl);

        ResponseEntity<ApplicationVO[]> response = restTemplate
        .exchange(uri, HttpMethod.GET, new HttpEntity<Void>(createHeaders("admin", "password")),ApplicationVO[].class);
        ApplicationVO[] applicationArray = response.getBody();
        return Arrays.asList(applicationArray);

    }

   // @PreAuthorize("hasRole('ADMIN')")
   /* @Recover
    public List<ApplicationVO> recover(Exception ex){
        System.out.println("#########Recover##########");
        return null;
    }*/
   // @Retryable(value = {Exception.class}, maxAttempts = 5, backoff = @Backoff(delay=6000))
    @PreAuthorize("hasRole('ADMIN')")
    @Override
    public ApplicationVO findById(long id) throws URISyntaxException {
        log.info("Inside ReportingServiceImpl.findById, and applicationGetAllUrl:{}",applicationUrl );
        URI uri = new URI(applicationUrl+"/"+id);
      //  uri.setConnectTimeout(7000);
        ResponseEntity<ApplicationVO> response = restTemplate.exchange(uri,
                HttpMethod.GET, new HttpEntity<Void>(createHeaders("admin", "password")),ApplicationVO.class);
        ApplicationVO applicationArray = response.getBody();
        return applicationArray;
    }

    /*@Recover
    public List<ApplicationVO> recoverGetByID(Exception ex){
        System.out.println("#########RecoverByID##########");
        return null;
    }*/

    public ApplicationVO findByNameRest(String appName, long id) throws URISyntaxException {
        log.info("Inside ReportingServiceImpl.findAll, and applicationGetAllUrl:{}", applicationUrl);
        URI uri = new URI(applicationUrl+"/appName/id?name="+appName+"&id=" + id);
        ResponseEntity<ApplicationVO> response = restTemplate.exchange(uri,
                HttpMethod.GET, new HttpEntity<Void>(createHeaders("admin", "password")),ApplicationVO.class);
        ApplicationVO applicationArray = response.getBody();
        return applicationArray;

    }

    public String saveNew(ApplicationRequest request) throws URISyntaxException {
        log.info("Inside ReportingServiceImpl.saveNew, request:{}", request);

        URI uri = new URI(applicationUrl);

        log.info("Application url to save the application:{}", applicationUrl);

      //  HttpHeaders header=new HttpHeaders();
      //  HttpEntity<ApplicationRequest> entity=new HttpEntity<ApplicationRequest>(request, header);

        ResponseEntity<String> response=restTemplate.exchange(uri, HttpMethod.POST, new HttpEntity<Void>(createHeaders("user", "password")),
                String.class);
        log.info("Response from application save, status code :{} and data:{}" , response.getStatusCode(), response.getBody());
        return response.getBody();
    }

    public void updateNew(ApplicationRequest request) throws URISyntaxException {
        log.info("Inside ReportServiceImpl.Update, request:{}", request);

        URI uri = new URI(applicationUrl);

        log.info("Application url to update the application:{}", applicationUrl);
      //  HttpHeaders header=new HttpHeaders();
     //   HttpEntity<ApplicationRequest> entity=new HttpEntity<ApplicationRequest>(request, header);
        restTemplate.put(String.valueOf(uri), HttpMethod.PUT, new HttpEntity<Void>(createHeaders("user", "password")));

    }

    @Override
    public void delete(long id) throws URISyntaxException {
        log.info("Inside ReportingServiceImpl.deleteNew, request:{}", id);

        URI uri = new URI(applicationUrl+ "/" +id);

        log.info("Application url to save the application:{}", applicationUrl);
       // ResponseEntity<ApplicationVO> response = restTemplate.delete(uri, ApplicationVO.class);
        HttpHeaders header=new HttpHeaders();
       // ApplicationVO applicationArray = response.getBody();
       // return applicationArray;
        HttpEntity<ApplicationRequest> entity=new HttpEntity<ApplicationRequest>(header);
        restTemplate.delete(uri);

        // return null;
    }
  //  @PreAuthorize("hasRole('ADMIN')")
    @Override
    public List<ApplicationVO> findAllNew() {
        log.info("Inside ReportingServiceImpl.findAll, and applicationGetAllUrl:{}",applicationUrl );

        WebClient webClient = WebClient.create(applicationUrl);
        Flux<ApplicationVO> result = webClient.get()
                .retrieve()
                .bodyToFlux(ApplicationVO.class);
        return result.collectList().block();
    }

    @Override
    public ApplicationVO findByIdWeb(long id) {
        log.info("Inside ReportingServiceImpl.findAll, and applicationGetAllUrl:{}", applicationUrl);

        WebClient webClient = WebClient.create(applicationUrl + "/" + id);
        Mono<ApplicationVO> result = webClient.get()
                .retrieve()
                .bodyToMono(ApplicationVO.class);
        return result.block();

    }

    /**
     * Invoke bug tracking api post end point
     * @param
     * @return
     */
    public ApplicationVO saveWeb(ApplicationRequest applicationRequest){
        log.info("Inside ReportingServiceIm.save, applicationRequest:{}", applicationRequest);
        WebClient webClient = WebClient.create(applicationUrl);

        Mono<ApplicationVO> response = webClient.post()
                .body(Mono.just(applicationRequest), ApplicationRequest.class)
                .retrieve()
                .bodyToMono(ApplicationVO.class);
        return response.block();
    }

    public ApplicationVO updateNewWeb(ApplicationRequest applicationRequest){
        log.info("Inside ReportingServiceIm.update, applicationRequest:{}", applicationRequest);
        WebClient webClient = WebClient.create(applicationUrl);

        Mono<ApplicationVO> response = webClient.put()
                .body(Mono.just(applicationRequest), ApplicationRequest.class)
                .retrieve()
                .bodyToMono(ApplicationVO.class);
        return response.block();
    }

    public Void deleteWeb(long id){
        log.info("Inside ReportingServiceIm.delete, id:{}", id);
        WebClient webClient = WebClient.create(applicationUrl + "/" + id);
        Mono<Void> response = webClient.delete()
                .retrieve()
                .bodyToMono(void.class);
        return response.block();

    }

    public ApplicationVO findByName(String appName, long id) throws InterruptedException {
        log.info("Inside ReportingServiceImpl.findAll, and applicationGetAllUrl:{}", applicationUrl);

        WebClient webClient = WebClient.create(applicationUrl + "/appName/id?name=" + appName + "&id=" + id );
        Mono<ApplicationVO> result = webClient.get()
                .retrieve()
                .bodyToMono(ApplicationVO.class);
        return result.block();
    }
}
