package biz.neustar.omx.atmos.admin;

import static com.jayway.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasItem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.camel.Exchange;
import org.apache.camel.ExchangePattern;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.impl.DefaultExchange;
import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.jayway.restassured.RestAssured;
import com.jayway.restassured.http.ContentType;

import biz.neustar.omx.cgw.CamelBaseTest;
import biz.neustar.omx.constants.ConnectivityConstants;
import biz.neustar.omx.core.beans.OMXActiveMQMessage;
import biz.neustar.omx.core.exception.ReflowException;
import biz.neustar.omx.model.route.GetRouteDefinitionRequest;
import biz.neustar.omx.model.route.RouteOperationRequest;
import biz.neustar.omx.persistence.common.model.OMXTrackingMessage;
import biz.neustar.omx.persistence.mongo.dao.impl.MongoDBOperationsImpl;
import biz.neustar.omx.util.ReflowHelper;

public class AdminControllerTest extends CamelBaseTest {
	private static final Logger logger = LoggerFactory.getLogger(AdminControllerTest.class);
	private final String BASE_URL = "services/admin/routes/";

	@Autowired
	MongoDBOperationsImpl dbOperationDao;
	
	@BeforeClass
	public static void setup() {
		RestAssured.port = Integer.valueOf(8080);
		RestAssured.basePath = "/atmos/";
		RestAssured.baseURI = "http://localhost";
	}


	@Test
	public void testGetAllRoutes_validDNS() {

		String dnsName = "stomvdvvatmos01.va.neustar.com";

		given().when().parameter("DNS_NAME", dnsName).get(BASE_URL + "getAllRoutes").then().statusCode(200);

	}
	
	@Test
	public void testGetAllRoutes_validDNS_EmplyOrNull_DNS() {

		String dnsName = null;
		given().when().parameter("DNS_NAME", dnsName).get(BASE_URL + "getAllRoutes").then()
		.statusCode(400);

	}
	
	@Test
	public void testStartRoutes() {
		List<String> dnsNames= new ArrayList<>();
		dnsNames.add("stomvdvvatmos01.va.neustar.com");
		List<String> routeIds= new ArrayList<>();
		routeIds.add("direct:omxBasicSrmDocument:PAT_TPE");
		routeIds.add("direct:omxBasicSrmDocument:DEFAULT");
		routeIds.add("direct:omxBasicSrmDocument:HANK");
		RouteOperationRequest routeOperationRequest = new RouteOperationRequest();
		routeOperationRequest.setPublishToAll(false);
		routeOperationRequest.setDnsNames(dnsNames);
		routeOperationRequest.setRouteIds(routeIds);
		
		com.jayway.restassured.response.Response response =given().when().contentType(ContentType.JSON).body(routeOperationRequest)
				.post(BASE_URL + "startRoutes"); 
		String outputJSON = response.print();
		com.jayway.restassured.response.ValidatableResponse validatableResponse = response.then().statusCode(200)
				.assertThat();
		logger.info("testRetrieveConnectivityDocument : {}", outputJSON);
		validatableResponse.body( equalTo("true"));
	}
	
	@Test
	public void testStartRoutes_DNSNamesList_Null() {
		List<String> routeIds= new ArrayList<>();
		routeIds.add("direct:omxBasicSrmDocument:PAT_TPE");
		routeIds.add("direct:omxBasicSrmDocument:DEFAULT");
		routeIds.add("direct:omxBasicSrmDocument:HANK");
		RouteOperationRequest routeOperationRequest = new RouteOperationRequest();
		routeOperationRequest.setPublishToAll(false);
		routeOperationRequest.setDnsNames(null);
		routeOperationRequest.setRouteIds(routeIds);
		com.jayway.restassured.response.Response response =given().when().contentType(ContentType.JSON).body(routeOperationRequest)
				.post(BASE_URL + "startRoutes"); 
		String outputJSON = response.print();
		com.jayway.restassured.response.ValidatableResponse validatableResponse = response.then().statusCode(500)
				.assertThat();
		logger.info("testRetrieveConnectivityDocument : {}", outputJSON);
		validatableResponse.body( equalTo("ATMOS_SYSTEM_ERROR"));
	}
	
	@Test
	public void testStopRoutes() {
		List<String> dnsNames= new ArrayList<>();
		dnsNames.add("stomvdvvatmos01.va.neustar.com");
		List<String> routeIds= new ArrayList<>();
		routeIds.add("direct:omxBasicSrmDocument:PAT_TPE");
		routeIds.add("direct:omxBasicSrmDocument:DEFAULT");
		routeIds.add("direct:omxBasicSrmDocument:HANK");
		RouteOperationRequest routeOperationRequest = new RouteOperationRequest();
		routeOperationRequest.setPublishToAll(false);
		routeOperationRequest.setDnsNames(dnsNames);
		routeOperationRequest.setRouteIds(routeIds);
		com.jayway.restassured.response.Response response =given().when().contentType(ContentType.JSON).body(routeOperationRequest)
				.post(BASE_URL + "stopRoutes"); 
		String outputJSON = response.print();
		com.jayway.restassured.response.ValidatableResponse validatableResponse = response.then().statusCode(200)
				.assertThat();
		logger.info("testStopRoutes : {}", outputJSON);
		validatableResponse.body( equalTo("true"));
	}
	
	@Test
	public void testStopRoutes_DNSNamesList_Null() {
		List<String> routeIds= new ArrayList<>();
		routeIds.add("direct:omxBasicSrmDocument:PAT_TPE");
		routeIds.add("direct:omxBasicSrmDocument:DEFAULT");
		routeIds.add("direct:omxBasicSrmDocument:HANK");
		RouteOperationRequest routeOperationRequest = new RouteOperationRequest();
		routeOperationRequest.setPublishToAll(false);
		routeOperationRequest.setDnsNames(null);
		routeOperationRequest.setRouteIds(routeIds);
		com.jayway.restassured.response.Response response =given().when().contentType(ContentType.JSON).body(routeOperationRequest)
				.post(BASE_URL + "stopRoutes"); 
		String outputJSON = response.print();
		com.jayway.restassured.response.ValidatableResponse validatableResponse = response.then().statusCode(500)
				.assertThat();
		logger.info("testStopRoutes : {}", outputJSON);
		validatableResponse.body( equalTo("ATMOS_SYSTEM_ERROR"));
	}
	

	@Test
	public void testUpdateRoute() {
		String payload ="<?xml version=\"1.0\" encoding=\"UTF-8\" ?><publishToAll>false</publishToAll><dnsNames>stomvdvvatmos01.va.neustar.com</dnsNames><routeIds>omxBasicSrmDocument:BASIC_SRM_EXCEPTION_QUEUE</routeIds>";
		
		com.jayway.restassured.response.Response response =given().when().contentType(ContentType.XML).body(payload)
				.post(BASE_URL + "updateRoute"); 
		String outputJSON = response.print();
		com.jayway.restassured.response.ValidatableResponse validatableResponse = response.then().statusCode(200)
				.assertThat();
		logger.info("testUpdateRoute : {}", outputJSON);
		validatableResponse.body( equalTo("true"));
	}
	
	@Test
	public void testUpdateRoute_payload_Emply() {
		String payload ="";
		
		com.jayway.restassured.response.Response response =given().when().contentType(ContentType.XML).body(payload)
				.post(BASE_URL + "updateRoute"); 
		String outputJSON = response.print();
		com.jayway.restassured.response.ValidatableResponse validatableResponse = response.then().statusCode(400)
				.assertThat();
		logger.info("testUpdateRoute : {}", outputJSON);
		validatableResponse.body( equalTo(""));
	}
	
	@Test
	public void testUpdateConfiguration() {
		
		String criteraMap = "{\"id\": \"56463-56456-56454-89878-96612\",  \"uniqueDocumentId\": \"omxBasicSrmDocument\",  \"auditInfo\": {    \"createdDate\": \"2016-12-22 11:52:11\",    \"updatedDate\": \"2016-12-22 11:52:11\",    \"createdBy\": \"Sudeep\",    \"updatedBy\": \"Sudeep\"  },  \"outageDataInfo\": {    \"scheduledOutageInfo\": {      \"PAT_TPE\": [        {          \"routeStartTime\": \"0 0 09 1/1 * ?\",          \"routeStopTime\": \"0 30 08 1/1 * ?\",          \"active\": true,          \"started\": false,          \"updated\": false,          \"deleted\": false,          \"added\": false        },        {          \"routeStartTime\": \"0 0 09 1/1 * ?\",          \"routeStopTime\": \"0 30 08 1/1 * ?\",          \"active\": true,          \"started\": false,          \"updated\": false,          \"deleted\": false,          \"added\": false        }      ],      \"HANK\": [        {          \"routeStartTime\": \"0 0 09 1/1 * ?\",          \"routeStopTime\": \"0 30 08 1/1 * ?\",          \"active\": true,          \"started\": false,          \"updated\": false,          \"deleted\": false,          \"added\": false        },        {          \"routeStartTime\": \"0 0 09 1/1 * ?\",          \"routeStopTime\": \"0 30 08 1/1 * ?\",          \"active\": true,          \"started\": false,          \"updated\": false,          \"deleted\": false,          \"added\": false        }      ]    },    \"adhocOutageInfo\": {      \"PAT_TPE\": [        {          \"routeStartTime\": \"0 0 09 1/1 * ?\",          \"routeStopTime\": \"0 30 08 1/1 * ?\",          \"active\": true,          \"started\": false,          \"updated\": false,          \"deleted\": false,          \"added\": false        },        {          \"routeStartTime\": \"0 0 09 1/1 * ?\",          \"routeStopTime\": \"0 30 08 1/1 * ?\",          \"active\": true,          \"started\": false,          \"updated\": false,          \"deleted\": false,          \"added\": false        }      ],      \"HANK\": [        {          \"routeStartTime\": \"0 0 09 1/1 * ?\",          \"routeStopTime\": \"0 30 08 1/1 * ?\",          \"active\": true,          \"started\": false,          \"updated\": false,          \"deleted\": false,          \"added\": false        },        {          \"routeStartTime\": \"0 0 09 1/1 * ?\",          \"routeStopTime\": \"0 30 08 1/1 * ?\",          \"active\": true,          \"started\": false,          \"updated\": false,          \"deleted\": false,          \"added\": false        }      ]    }  },  \"routeBuilderBaseInfo\": {    \"queuesInfo\": {      \"requestQueues\": {        \"GROUP_A\": [          \"BASIC_TWC_EVENT_QUEUE\",          \"BASIC_CBND_EVENT_QUEUE\",          \"BASIC_CHAR_EVENT_QUEUE\",          \"BASIC_DSET_EVENT_QUEUE\"        ],        \"DEFAULT\": [          \"BASIC_COMMON_EVENT_QUEUE\",          \"RECEIVE_EVENT_QUEUE\",          \"BASIC_BHN_EVENT_QUEUE\",          \"BASIC_MCC_EVENT_QUEUE\"        ]      },      \"exceptionQueue\": \"BASIC_SRM_EXCEPTION_QUEUE\"    },    \"autoStartup\": false,    \"custTransMappingInfo\": [      {        \"customerId\": \"LDMI_TST\"      },      {        \"customerId\": \"PAT_TPE\"      },      {        \"customerId\": \"ACCP_TST\"      },      {        \"customerId\": \"DEFAULT\"      },      {        \"customerId\": \"INDR_TST\"      },      {        \"customerId\": \"HANK\"      },      {        \"customerId\": \"XO_E2E\"      }    ]  },  \"recurInfo\": {    \"recurInterval\": \"900000\",    \"maxRecurCount\": \"3\"  },  \"omxCommonConfigInfo\": {    \"PAT_TPE\": {      \"ilecEndpointInfo\": {        \"type\": \"soap\",        \"endpointURL\": \"\",        \"namespace\": \"{http://www.neustar.biz/clearinghouse/SOAPResponseHandler/1.0}\",        \"serviceName\": \"SOAPResponseHandler\",        \"portName\": \"SOAPResponseHandler\"      },      \"throttePolicyInfo\": {        \"DEFAULT\": {          \"active\": true,          \"maximumRequestsPerPeriod\": 5000,          \"timePeriod\": 5,          \"timeUnits\": \"Min\",          \"asyncDelayed\": false,          \"rejectExecution\": false,          \"updated\": false,          \"deleted\": false,          \"added\": false        }      }    },    \"HANK\": {      \"ilecEndpointInfo\": {        \"type\": \"soap\",        \"endpointURL\": \"\",        \"namespace\": \"{http://www.neustar.biz/clearinghouse/SOAPResponseHandler/1.0}\",        \"serviceName\": \"SOAPResponseHandler\",        \"portName\": \"SOAPResponseHandler\"      },      \"throttePolicyInfo\": {        \"DEFAULT\": {          \"active\": true,          \"maximumRequestsPerPeriod\": 5000,          \"timePeriod\": 5,          \"timeUnits\": \"Min\",          \"asyncDelayed\": false,          \"rejectExecution\": false,          \"updated\": false,          \"deleted\": false,          \"added\": false        }      }    },    \"DEFAULT\": {      \"ilecEndpointInfo\": {        \"type\": \"soap\",        \"endpointURL\": \"\",        \"namespace\": \"{http://www.neustar.biz/clearinghouse/SOAPResponseHandler/1.0}\",        \"serviceName\": \"SOAPResponseHandler\",        \"portName\": \"SOAPResponseHandler\"      },      \"throttePolicyInfo\": {        \"DEFAULT\": {          \"active\": true,          \"maximumRequestsPerPeriod\": 5000,          \"timePeriod\": 5,          \"timeUnits\": \"Min\",          \"asyncDelayed\": false,          \"rejectExecution\": false,          \"updated\": false,          \"deleted\": false,          \"added\": false        }      }    }  },  \"omxSrmConfigInfo\": {    \"ACCP_TST\": {      \"customerEndPoints\": [        \"http://pat-wln.neustar.com/soa-infra/services/Enterprise/svcCHAsyncResponse/plClient\"      ]    },    \"PAT_TPE\": {      \"customerEndPoints\": [        \"http://pat-wln.neustar.com/soa-infra/services/Enterprise/svcCHAsyncResponse/plClient\"      ]    },    \"HANK\": {      \"customerEndPoints\": [        \"http://pat-wln.neustar.com/soa-infra/services/Enterprise/svcCHAsyncResponse/plClient\",        \"http://localhost:8080\"      ]    },    \"INDR_TST\": {      \"customerEndPoints\": [        \"http://pat-wln.neustar.com/soa-infra/services/Enterprise/svcCHAsyncResponse/plClient\"      ]    },    \"XO_E2E\": {      \"customerEndPoints\": [        \"http://pat-wln.neustar.com/soa-infra/services/Enterprise/svcCHAsyncResponse/plClient\"      ]    }  }}";
		com.jayway.restassured.response.Response response =given().when().contentType(ContentType.JSON).body(criteraMap ).post(BASE_URL + "updateConfiguration/omxBasicSrmDocument"); 
		String outputJSON = response.print();
		com.jayway.restassured.response.ValidatableResponse validatableResponse = response.then().statusCode(200)
				.assertThat();
		logger.info("testUpdateConfiguration : {}", outputJSON);
		validatableResponse.body( equalTo("true"));
	}
	
	@Test
	public void testGetRouteDefinition() {
		
		GetRouteDefinitionRequest routeDefinitionRequest = new GetRouteDefinitionRequest();
		routeDefinitionRequest.setDnsName("stomvdvvatmos01.va.neustar.com");
		routeDefinitionRequest.setRouteId("omxBasicSrmDocument:BASIC_SRM_EXCEPTION_QUEUE");
		com.jayway.restassured.response.Response response =given().when().contentType(ContentType.JSON).body(routeDefinitionRequest ).post(BASE_URL + "getRouteDefinition"); 
		String outputJSON = response.print();
		com.jayway.restassured.response.ValidatableResponse validatableResponse = response.then().statusCode(200)
				.assertThat();
		logger.info("testGetRouteDefinition : {}", outputJSON);
		validatableResponse.contentType(ContentType.XML).body("route.removeHeader.headerName", hasItem("CamelJmsDeliveryMode"));
	}
	
	@Test
	public void testGetRouteDefinition_DnsName_Empty() {
		
		GetRouteDefinitionRequest routeDefinitionRequest = new GetRouteDefinitionRequest();
		routeDefinitionRequest.setDnsName(null);
		routeDefinitionRequest.setRouteId("omxBasicSrmDocument:BASIC_SRM_EXCEPTION_QUEUE");
		com.jayway.restassured.response.Response response =given().when().contentType(ContentType.JSON).body(routeDefinitionRequest ).post(BASE_URL + "getRouteDefinition"); 
		String outputJSON = response.print();
		com.jayway.restassured.response.ValidatableResponse validatableResponse = response.then().statusCode(500)
				.assertThat();
		logger.info("testGetRouteDefinition_RouteId_Empty : {}", outputJSON);
//		validatableResponse.contentType(ContentType.XML).body("route.removeHeader.headerName", hasItem("CamelJmsDeliveryMode"));
	}
	@Test
	public void testRetrieveConnectivityDocument() {
		
		com.jayway.restassured.response.Response response =given().when().get(BASE_URL + "retrieveConfiguration/omxBasicSrmDocument"); 
		String outputJSON = response.print();
		com.jayway.restassured.response.ValidatableResponse validatableResponse = response.then().statusCode(200)
				.assertThat();
		logger.info("testRetrieveConnectivityDocument : {}", outputJSON);
		validatableResponse.body("omxCommonConfigInfo.PAT_TPE.ilecEndpointInfo.portName", equalTo("SOAPResponseHandler"));
	}
	
	@Test
	public void testRetrieveConnectivityDocument_Invalid_URL() {
		
		com.jayway.restassured.response.Response response =given().when().get(BASE_URL + "retrieveConfiguration"); 
		String outputJSON = response.print();
		com.jayway.restassured.response.ValidatableResponse validatableResponse = response.then().statusCode(404)
				.assertThat();
		logger.info("testRetrieveConnectivityDocument : {}", outputJSON);
//		validatableResponse.body("omxCommonConfigInfo.PAT_TPE.ilecEndpointInfo.portName", equalTo("SOAPResponseHandler"));
	}
	
	@Test
	public void testRetrieveConnectivityNames() {
		
		com.jayway.restassured.response.Response response =given().when().get(BASE_URL + "retrieveConnectivityNames"); 
		String outputJSON = response.print();
		com.jayway.restassured.response.ValidatableResponse validatableResponse = response.then().statusCode(200)
				.assertThat();
		logger.info("testRetrieveConnectivityNames : {}", outputJSON);
		validatableResponse.body("'BASIC SRM'.uniqueId", equalTo("omxBasicSrmDocument"));
	}
	

	@Test
	public void testRetrieveMongoDBMessages() {
		String body = "{\"JMSStartTimestamp\" :\"2016-12-11 11:11\"" + ",\"JMSEndTimestamp\": \"2016-12-21 16:30\"}";
		com.jayway.restassured.response.Response response = given().when().contentType(ContentType.JSON).body(body)
				.post(BASE_URL + "retrieveMongoDBMessages");
		String outputJSON = response.print();
		com.jayway.restassured.response.ValidatableResponse validatableResponse = response.then().statusCode(200)
				.assertThat();
		logger.info("testRetrieveMongoDBMessages : {}", outputJSON);
		validatableResponse.body("customer", hasItem("PAT_TPE"));
	}
	@Test
	public void testReflowMongoDBMessage() {
		
		OMXTrackingMessage omxTrackingMessage = getOMXTrackingMessage();
		dbOperationDao.upsertRecord(omxTrackingMessage.getId(), omxTrackingMessage, OMXTrackingMessage.class);
		com.jayway.restassured.response.Response response = given().when().contentType(ContentType.JSON)
				.body(omxTrackingMessage).post(BASE_URL + "reflowMongoDBMessage");
		String outputJSON = response.print();
		com.jayway.restassured.response.ValidatableResponse validatableResponse = response.then().statusCode(200)
				.assertThat();
		logger.info("testReflowMongoDBMessage : {}", outputJSON);
		validatableResponse.body(equalTo("1"));
	}
	
	@Test
	public void testRetrieveActiveMQMessages() {
		Map<String,String> criteraMap = new HashMap<>();
		criteraMap.put("ExceptionQueue", "BASIC_SRM_EXCEPTION_QUEUE");
		criteraMap.put("JMSStartTimestamp", "2016-12-12 11:11");
		criteraMap.put("JMSEndTimestamp", "2016-12-21 18:46");
		criteraMap.put("CustomerIdentifier", "HANK");
		
		String body = "{\"ExceptionQueue\":\"BASIC_SRM_EXCEPTION_QUEUE\",\"JMSStartTimestamp\":\"2016-12-12 11:11\",\"JMSEndTimestamp\":\"2016-12-21 18:46\",\"CustomerIdentifier\":\"HANK\"}";
		com.jayway.restassured.response.Response response = given().when().contentType(ContentType.JSON).body(criteraMap)
				.post(BASE_URL + "retrieveActiveMQMessages");
		String outputJSON = response.print();
		// List<OMXActiveMQMessage> ssss = response.as(List.class);
		com.jayway.restassured.response.ValidatableResponse validatableResponse = response.then().statusCode(200)
				.assertThat();
		logger.info("testRetrieveActiveMQMessages : {}", outputJSON);
		validatableResponse.body("customerIdentifier", hasItem("HANK"));
	}
	
	@Test
	public void testReflowActiveMQMessagesByCriteria() {
		Map<String,String> criteraMap = new HashMap<>();
		criteraMap.put("ExceptionQueue", "BASIC_SRM_EXCEPTION_QUEUE");
		criteraMap.put("JMSStartTimestamp", "2016-12-12 11:11");
		criteraMap.put("JMSEndTimestamp", "2016-12-12 18:46");
		criteraMap.put("CustomerIdentifier", "HANK");
		
		String body = "{\"ExceptionQueue\":\"BASIC_SRM_EXCEPTION_QUEUE\",\"JMSStartTimestamp\":\"2016-12-12 11:11\",\"JMSEndTimestamp\":\"2016-12-12 18:46\",\"CustomerIdentifier\":\"HANK\"}";
		com.jayway.restassured.response.Response response = given().when().contentType(ContentType.JSON).body(criteraMap)
				.post(BASE_URL + "reflowActiveMQMessagesByCriteria");
		String outputJSON = response.print();
		// List<OMXActiveMQMessage> ssss = response.as(List.class);
		com.jayway.restassured.response.ValidatableResponse validatableResponse = response.then().statusCode(200)
				.assertThat();
		logger.info("testReflowActiveMQMessagesByCriteria : {}", outputJSON);
		validatableResponse.body(equalTo("0"));
	}
	
	@Test
	public void testReflowActiveMQMessages() {
		List<OMXActiveMQMessage> omxActiveMQMessages = new ArrayList<>();
		OMXActiveMQMessage omxTrackingMessage = new OMXActiveMQMessage();
		omxTrackingMessage.setCustomerIdentifier("HANK");
		omxTrackingMessage.setEditable(false);
		omxTrackingMessage.setExceptionQueue("BASIC_SRM_EXCEPTION_QUEUE");
		omxTrackingMessage.setMessageId("ID:xen-637-58928-1481722623640-3:6:1:1:1");
		omxTrackingMessage.setPayload(
				"<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?> <soap:Envelope soap:encodingStyle=\"http://schemas.xmlsoap.org/soap/encoding/\" xmlns:ns=\"http://www.neustar.biz/clearinghouse/SOAPResponseHandler/1.0\" xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\"> <soap:Header></soap:Header> <soap:Body> <ns:processEvent> <ns:in0></ns:in0> <ns:in1> <SOAMessage> <UpstreamToSOA> <UpstreamToSOAHeader> <InitSPID value=\"9417\"/> <DateSent value=\"12-29-2015-0516AM\"/> <Action value=\"submit\"/> </UpstreamToSOAHeader> <UpstreamToSOABody> <SvReleaseRequest> <LnpType value=\"lspp\"/> <Subscription> <Tn value=\"913-559-0153\"/> </Subscription> <OldSP value=\"9417\"/> <NewSP value=\"5606\"/> <OldSPDueDate value=\"12-29-2015-0700PM\"/> <ONSPSimplePortIndicator value=\"0\"/> </SvReleaseRequest> </UpstreamToSOABody> </UpstreamToSOA> </SOAMessage> </ns:in1> </ns:processEvent> </soap:Body> </soap:Envelope>");
		omxTrackingMessage.setSubRequest(null);
		omxTrackingMessage.setSupplier(null);
		omxTrackingMessage.setTimestamp("2016-12-14 19:11");

		omxActiveMQMessages.add(omxTrackingMessage);

		// To test the reflow we must insert the test data in the exception
		// queue.
		// insertDataForReflow(omxTrackingMessage);

		com.jayway.restassured.response.Response response = given().when().contentType(ContentType.JSON)
				.body(omxActiveMQMessages).post(BASE_URL + "reflowActiveMQMessages");
		String outputJSON = response.print();
		com.jayway.restassured.response.ValidatableResponse validatableResponse = response.then().statusCode(200)
				.assertThat();
		logger.info("testReflowActiveMQMessages : {}", outputJSON);
		validatableResponse.body(equalTo("1"));
	}
private OMXTrackingMessage getOMXTrackingMessage(){
	List<String> resStrList=  new ArrayList<>();
	resStrList.add(
			"<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?> <soap:Envelope soap:encodingStyle=\"http://schemas.xmlsoap.org/soap/encoding/\" xmlns:ns=\"http://www.neustar.biz/clearinghouse/SOAPResponseHandler/1.0\" xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\"> <soap:Header></soap:Header> <soap:Body> <ns:processEvent> <ns:in0></ns:in0> <ns:in1> <SOAMessage> <UpstreamToSOA> <UpstreamToSOAHeader> <InitSPID value=\"9417\"/> <DateSent value=\"12-29-2015-0516AM\"/> <Action value=\"submit\"/> </UpstreamToSOAHeader> <UpstreamToSOABody> <SvReleaseRequest> <LnpType value=\"lspp\"/> <Subscription> <Tn value=\"913-559-0161\"/> </Subscription> <OldSP value=\"9417\"/> <NewSP value=\"5606\"/> <OldSPDueDate value=\"12-29-2015-0700PM\"/> <ONSPSimplePortIndicator value=\"0\"/> </SvReleaseRequest> </UpstreamToSOABody> </UpstreamToSOA> </SOAMessage> </ns:in1> </ns:processEvent> </soap:Body> </soap:Envelope>");
	OMXTrackingMessage omxTrackingMessage = new OMXTrackingMessage();
	omxTrackingMessage.setId("ID:xen-637-63854-1482234512601-14:1:1:1:1");
	omxTrackingMessage.setUniqueMsgId(null);
	omxTrackingMessage.setCustomer("HANK");
	omxTrackingMessage.setSupplier(null);
	omxTrackingMessage.setServiceType(null);

	omxTrackingMessage.setSubrequest(null);
	omxTrackingMessage.setContactNumbers(new ArrayList<>());
	omxTrackingMessage.setPayload(
			"<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?> <soap:Envelope soap:encodingStyle=\"http://schemas.xmlsoap.org/soap/encoding/\" xmlns:ns=\"http://www.neustar.biz/clearinghouse/SOAPResponseHandler/1.0\" xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\"> <soap:Header></soap:Header> <soap:Body> <ns:processEvent> <ns:in0></ns:in0> <ns:in1> <SOAMessage> <UpstreamToSOA> <UpstreamToSOAHeader> <InitSPID value=\"9417\"/> <DateSent value=\"12-29-2015-0516AM\"/> <Action value=\"submit\"/> </UpstreamToSOAHeader> <UpstreamToSOABody> <SvReleaseRequest> <LnpType value=\"lspp\"/> <Subscription> <Tn value=\"913-559-0161\"/> </Subscription> <OldSP value=\"9417\"/> <NewSP value=\"5606\"/> <OldSPDueDate value=\"12-29-2015-0700PM\"/> <ONSPSimplePortIndicator value=\"0\"/> </SvReleaseRequest> </UpstreamToSOABody> </UpstreamToSOA> </SOAMessage> </ns:in1> </ns:processEvent> </soap:Body> </soap:Envelope>");
	//omxTrackingMessage.setResponseMessage(resStrList);
	omxTrackingMessage.setTransactionType(null);
	omxTrackingMessage.setQueueName("BASIC_BHN_EVENT_QUEUE");
	omxTrackingMessage.setErrorMessage(null);
	omxTrackingMessage.setTimestamp(ReflowHelper.getCurrentDateTimeIntoLong());
//	omxTrackingMessage.setTpError(null);
	omxTrackingMessage.setRecurCount("0");
	omxTrackingMessage.setHeaderMaps(new HashMap<>());
	omxTrackingMessage.setVersion(null);
	omxTrackingMessage.setProcessed(true);
	
	return omxTrackingMessage;
	
}
//	@Test
	public void reflowMongoDBMessage() {
		logger.info("Inside method reflowMongoDBMessage :  ");
		OMXTrackingMessage omxTrackingMessage = getOMXTrackingMessage();
		logger.debug("reflowMongoDBMessage Input omxTrackingMessage message : {}", omxTrackingMessage);
		final ProducerTemplate producerTemplate = camelContext.createProducerTemplate();
		

		logger.debug("reflowMongoDBMessage : Create ProducerTemplate: {} ", producerTemplate);
		String producerQueueName = "";
		int numberOfReflowMsg = 0;
		try {

			Map<String, Object> headers = new HashMap<>();
			headers.put(ConnectivityConstants.CUSTOMER_IDENTIFIER, omxTrackingMessage.getCustomer());
			headers.put(ConnectivityConstants.SUPPLIER, omxTrackingMessage.getSupplier());
			headers.put(ConnectivityConstants.SUBREQUEST, omxTrackingMessage.getSubrequest());
			headers.put(ConnectivityConstants.REGION, omxTrackingMessage.getRegion());
			headers.put(ConnectivityConstants.SERVICE_TYPE, omxTrackingMessage.getServiceType());
			headers.put(ConnectivityConstants.UNIQUE_MSG_ID, omxTrackingMessage.getUniqueMsgId());
			headers.put(ConnectivityConstants.TRANSACTION_TYPE, omxTrackingMessage.getTransactionType());
			headers.put(ConnectivityConstants.IS_PROCESSED, omxTrackingMessage.isProcessed());
			headers.put("MessageID", "ID:xen-637-63854-1482234512633-24:1:1:1:1");
			headers.put("JMSMessageID", "ID:xen-637-63854-1482234512633-24:1:1:1:1");
			headers.put("JMSCorrelationID", "ID:xen-637-63854-1482234512633-24:1:1:1:1");
			//headers.put(Exchange.BREADCRUMB_ID, "ID:xen-637-63854-1482234512633-24:1:1:1:1");
			producerQueueName = omxTrackingMessage.getQueueName();
			Exchange ex = new DefaultExchange(camelContext);
			ex.getIn().setBody(omxTrackingMessage.getPayload());
			ex.getIn().setHeaders(headers);
			ex.setPattern(ExchangePattern.InOnly);
			ex.setProperty("MessageID", "ID:xen-637-63854-1482234512633-24:1:1:1:1");
			ex.setProperty("JMSMessageID", "ID:xen-637-63854-1482234512633-24:1:1:1:1");
			ex.setProperty("JMSCorrelationID", "ID:xen-637-63854-1482234512633-24:1:1:1:1");
			ex.getIn().setMessageId("ID:xen-637-63854-1482234512633-24:1:1:1:1");
			//ex.getOut().setHeaders(headers);
			//ex.getOut().setBody(omxTrackingMessage.getPayload());
			Exchange result = producerTemplate.send(ConnectivityConstants.ACCOUNTID_NODE + producerQueueName+"?messageIdEnabled=false", ex);
			System.out.println(result);
			/*
			 * Object object =
			 * producerTemplate.sendBodyAndHeaders(ConnectivityConstants.
			 * ACTIVEMQ_BROKER +
			 * producerQueueName+"?useMessageIDAsCorrelationID=true",
			 * omxTrackingMessage.getPayload(), headers);
			 */
			numberOfReflowMsg++;

			// producerQueueName = omxTrackingMessage.getQueueName();
			// producerTemplate.sendBodyAndHeaders(ConnectivityConstants.ACTIVEMQ_BROKER
			// + producerQueueName,
			// omxTrackingMessage.getPayload(), headers);
			// numberOfReflowMsg++;
			logger.debug("reflowMongoDBMessage : header is : {}", headers);
		} catch (final Exception e) {
			logger.error(
					"reflowMongoDBMessage : Exception in reflowMongoDBMessage with input parameter omxTrackingMessage :{} \nEXCEPTION is:  {} ",
					omxTrackingMessage, e.getMessage());
			throw new ReflowException(e.getMessage(), e);
		}
		logger.debug("reflowMongoDBMessage : return numberOfReflowMsg from reflowMongoDBMessage method : {}",
				numberOfReflowMsg);
		// return numberOfReflowMsg;

	}
	// private void insertDataForReflow(OMXActiveMQMessage omxActiveMQMessages)
	// {
	//
	// producer = camelContext.createProducerTemplate();
	// producer.send(arg0)
	//
	// }

}
