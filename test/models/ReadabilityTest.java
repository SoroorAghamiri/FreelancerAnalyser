package models;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import junit.framework.TestCase;
import Helpers.Readability;
import org.mockito.Mockito;
import play.libs.ws.WSResponse;
import play.mvc.Call;
import play.mvc.Http;
import play.mvc.Result;
import play.test.Helpers;

import java.io.IOException;

/**
 * Test class to test the method of Readability class
 * @author Kazi Asif Tanim
 */
public class ReadabilityTest extends TestCase {

    String desc = "I am a representative of an open source first person shooter game. We are looking for a programmer t";

    /**
     * Test method for processReadability in Readability class
     * @author Kazi Asif Tanim
     */
    public void testProcessReadability() throws JsonProcessingException {
        String json = "{\"status\":\"success\",\"result\":{\"projects\":[{\"id\":33250627,\"owner_id\":61316717,\"title\":\"Desarrollar modulo Compras\",\"status\":\"active\",\"sub_status\":null,\"seo_url\":\"java/Desarrollar-modulo-Compras\",\"currency\":{\"id\":1,\"code\":\"USD\",\"sign\":\"$\",\"name\":\"US Dollar\",\"exchange_rate\":1.0,\"country\":\"US\",\"is_external\":false,\"is_escrowcom_supported\":true},\"description\":\"Se requiere programador para desarrollar modulos en cuanto a un sistema de gestion, se requiere que \",\"jobs\":null,\"submitdate\":1647734379,\"preview_description\":\"Se requiere programador para desarrollar modulos en cuanto a un sistema de gestion, se requiere que \",\"deleted\":false,\"nonpublic\":false,\"hidebids\":false,\"type\":\"fixed\",\"bidperiod\":7,\"budget\":{\"minimum\":250.0,\"maximum\":750.0,\"name\":null,\"project_type\":null,\"currency_id\":null},\"hourly_project_info\":null,\"featured\":false,\"urgent\":false,\"assisted\":null,\"active_prepaid_milestone\":null,\"bid_stats\":{\"bid_count\":1,\"bid_avg\":675.0},\"time_submitted\":1647734379,\"time_updated\":1647734379,\"upgrades\":{\"featured\":false,\"sealed\":false,\"nonpublic\":false,\"fulltime\":false,\"urgent\":false,\"qualified\":false,\"NDA\":false,\"assisted\":null,\"active_prepaid_milestone\":null,\"ip_contract\":false,\"success_bundle\":null,\"non_compete\":false,\"project_management\":false,\"pf_only\":false,\"recruiter\":null,\"listed\":null,\"extend\":null,\"unpaid_recruiter\":null},\"qualifications\":null,\"language\":\"es\",\"attachments\":null,\"hireme\":false,\"hireme_initial_bid\":null,\"invited_freelancers\":null,\"recommended_freelancers\":null,\"frontend_project_status\":\"open\",\"nda_signatures\":null,\"location\":{\"country\":{\"name\":null,\"flag_url\":null,\"code\":null,\"highres_flag_url\":null,\"flag_url_cdn\":null,\"highres_flag_url_cdn\":null,\"iso3\":null,\"region_id\":null,\"phone_code\":null,\"demonym\":null,\"person\":null,\"seo_url\":null,\"sanction\":null,\"language_code\":null,\"language_id\":null},\"city\":null,\"latitude\":null,\"longitude\":null,\"vicinity\":null,\"administrative_area\":null,\"full_address\":null,\"administrative_area_code\":null,\"postal_code\":null},\"true_location\":null,\"local\":false,\"negotiated\":false,\"negotiated_bid\":null,\"time_free_bids_expire\":1647728795,\"can_post_review\":null,\"files\":null,\"user_distance\":null,\"from_user_location\":null,\"project_collaborations\":null,\"support_sessions\":null,\"track_ids\":null,\"drive_files\":null,\"nda_details\":null,\"pool_ids\":[\"freelancer\"],\"enterprise_ids\":[],\"timeframe\":null,\"deloitte_details\":null,\"is_escrow_project\":false,\"is_seller_kyc_required\":false,\"is_buyer_kyc_required\":false,\"local_details\":null,\"equipment\":null,\"nda_signatures_new\":null,\"billing_code\":null,\"enterprise_metadata_values\":null,\"project_reject_reason\":{\"description\":null,\"message\":null},\"repost_id\":null,\"client_engagement\":null,\"contract_signatures\":null,\"quotation_id\":null,\"quotation_version_id\":null,\"enterprise_linked_projects_details\":null,\"equipment_groups\":null,\"project_source\":null,\"project_source_reference\":null}],\"users\":null,\"selected_bids\":null,\"total_count\":184},\"request_id\":\"b1ec0fd151ce2cb61e26d4496182c355\"}";
        String exptJson = "{\"status\":\"success\",\"result\":{\"projects\":[{\"id\":33250627,\"owner_id\":61316717,\"title\":\"Desarrollar modulo Compras\",\"status\":\"active\",\"sub_status\":null,\"seo_url\":\"java/Desarrollar-modulo-Compras\",\"currency\":{\"id\":1,\"code\":\"USD\",\"sign\":\"$\",\"name\":\"US Dollar\",\"exchange_rate\":1.0,\"country\":\"US\",\"is_external\":false,\"is_escrowcom_supported\":true},\"description\":\"Se requiere programador para desarrollar modulos en cuanto a un sistema de gestion, se requiere que \",\"jobs\":null,\"submitdate\":1647734379,\"preview_description\":\"Se requiere programador para desarrollar modulos en cuanto a un sistema de gestion, se requiere que \",\"deleted\":false,\"nonpublic\":false,\"hidebids\":false,\"type\":\"fixed\",\"bidperiod\":7,\"budget\":{\"minimum\":250.0,\"maximum\":750.0,\"name\":null,\"project_type\":null,\"currency_id\":null},\"hourly_project_info\":null,\"featured\":false,\"urgent\":false,\"assisted\":null,\"active_prepaid_milestone\":null,\"bid_stats\":{\"bid_count\":1,\"bid_avg\":675.0},\"time_submitted\":1647734379,\"time_updated\":1647734379,\"upgrades\":{\"featured\":false,\"sealed\":false,\"nonpublic\":false,\"fulltime\":false,\"urgent\":false,\"qualified\":false,\"NDA\":false,\"assisted\":null,\"active_prepaid_milestone\":null,\"ip_contract\":false,\"success_bundle\":null,\"non_compete\":false,\"project_management\":false,\"pf_only\":false,\"recruiter\":null,\"listed\":null,\"extend\":null,\"unpaid_recruiter\":null},\"qualifications\":null,\"language\":\"es\",\"attachments\":null,\"hireme\":false,\"hireme_initial_bid\":null,\"invited_freelancers\":null,\"recommended_freelancers\":null,\"frontend_project_status\":\"open\",\"nda_signatures\":null,\"location\":{\"country\":{\"name\":null,\"flag_url\":null,\"code\":null,\"highres_flag_url\":null,\"flag_url_cdn\":null,\"highres_flag_url_cdn\":null,\"iso3\":null,\"region_id\":null,\"phone_code\":null,\"demonym\":null,\"person\":null,\"seo_url\":null,\"sanction\":null,\"language_code\":null,\"language_id\":null},\"city\":null,\"latitude\":null,\"longitude\":null,\"vicinity\":null,\"administrative_area\":null,\"full_address\":null,\"administrative_area_code\":null,\"postal_code\":null},\"true_location\":null,\"local\":false,\"negotiated\":false,\"negotiated_bid\":null,\"time_free_bids_expire\":1647728795,\"can_post_review\":null,\"files\":null,\"user_distance\":null,\"from_user_location\":null,\"project_collaborations\":null,\"support_sessions\":null,\"track_ids\":null,\"drive_files\":null,\"nda_details\":null,\"pool_ids\":[\"freelancer\"],\"enterprise_ids\":[],\"timeframe\":null,\"deloitte_details\":null,\"is_escrow_project\":false,\"is_seller_kyc_required\":false,\"is_buyer_kyc_required\":false,\"local_details\":null,\"equipment\":null,\"nda_signatures_new\":null,\"billing_code\":null,\"enterprise_metadata_values\":null,\"project_reject_reason\":{\"description\":null,\"message\":null},\"repost_id\":null,\"client_engagement\":null,\"contract_signatures\":null,\"quotation_id\":null,\"quotation_version_id\":null,\"enterprise_linked_projects_details\":null,\"equipment_groups\":null,\"project_source\":null,\"project_source_reference\":null}],\"users\":null,\"selected_bids\":null,\"total_count\":184},\"request_id\":\"b1ec0fd151ce2cb61e26d4496182c355\",\"score\":{\"avgFREI\":106.0,\"avgFKGL\":2.45}}";
        ObjectMapper mapper = new ObjectMapper();
        JsonNode jsonNode = mapper.readTree(json);

        assertEquals(exptJson, Readability.processReadability(jsonNode).toString());
    }

    /**
     * Test method for processReadabilityForSingleProject in Readability class
     * @author Kazi Asif Tanim
     */
    public void testProcessReadabilityForSingleProject() {

        assertEquals("\n" +
                "Index: 103.0\n" +
                "Education Level: Early\n" +
                "FKGL: 3.62", Readability.processReadabilityForSingleProject(desc));
    }
}