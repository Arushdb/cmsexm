package edu.dei.examination.phd.client;



import edu.dei.examination.phd.dto.ScholarDTO;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.beans.factory.annotation.Value;

@Component
public class AdmissionClient {

    private final RestTemplate restTemplate;
    private final String admissionServiceBase;

    public AdmissionClient(RestTemplate restTemplate,
                           @Value("${admission.service.base:http://localhost:8081}") String admissionServiceBase) {
        this.restTemplate = restTemplate;
        this.admissionServiceBase = admissionServiceBase;
    }

    public ScholarDTO fetchAdmission(Integer admissionId) {
        String url = String.format("%s/api/admissions/%d", admissionServiceBase, admissionId);
        return restTemplate.getForObject(url, ScholarDTO.class);
    }
}
