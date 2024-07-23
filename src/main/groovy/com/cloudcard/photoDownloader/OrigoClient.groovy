package com.cloudcard.photoDownloader

import com.mashape.unirest.http.HttpResponse

import com.mashape.unirest.http.Unirest
import jakarta.annotation.PostConstruct
import org.apache.http.HttpException
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.stereotype.Component
import com.fasterxml.jackson.databind.ObjectMapper;

import static com.cloudcard.photoDownloader.ApplicationPropertiesValidator.throwIfBlank


@Component
@ConditionalOnProperty(name = 'Origo.useOrigo', havingValue = 'true')
class OrigoClient extends OrigoService {
    // Makes requests to Origo API
    private static final Logger log = LoggerFactory.getLogger(OrigoClient.class);

    @Value('${Origo.eventManagementApi}')
    private String eventManagementApi

    @Value('${Origo.callbackRegistrationApi}')
    private String callbackRegistrationApi

    @Value('${Origo.mobileIdentitiesApi}')
    private String mobileIdentitiesApi

    @Value('${Origo.customerId}')
    private String customerId

    @Value('${Origo.organizationId}')
    private String organizationId

    @Value('${Origo.accessToken}')
    private accessToken

    @Value('${Origo.tokenType} ${Origo.accessToken}')
    private String authorization

    @Value('${Origo.contentType}')
    private String contentType

    @Value('${Origo.applicationVersion}')
    private String applicationVersion

    @Value('${Origo.applicationId}')
    private String applicationId

    String filterId

    String callbackUrl

    Map requestHeaders

    @PostConstruct
    def init() {
            throwIfBlank(eventManagementApi, "The Origo Event Management API URL must be specified.")
            throwIfBlank(callbackRegistrationApi, "The Origo Callback Registration API URL must be specified.")
            throwIfBlank(mobileIdentitiesApi, "The Origo Mobile Identities API URL must be specified.")
            throwIfBlank(customerId, "The Origo customer ID must be specified.")
            throwIfBlank(organizationId, "The Origo organization ID must be specified.")
            throwIfBlank(accessToken, "An Origo access token must be provided.")
            throwIfBlank(contentType, "The Origo content-type header must be specified.")
            throwIfBlank(applicationVersion, "The Origo application version must be specified.")
            throwIfBlank(applicationId, "Your organization's Origo Application ID must be specified.")

            log.info('=================== Accessing HID Origo Services ===================')
            log.info("          Origo Event Management API URL : $eventManagementApi")
            log.info("     Origo Callback Registration API URL : $callbackRegistrationApi")
            log.info("         Origo Mobile Identities API URL : $mobileIdentitiesApi")
            log.info("                       Origo customer ID : $customerId")
            log.info("                   Origo organization ID : $organizationId")
            log.info("              Origo authorization string : $authorization")
            log.info("               Origo content type header : $contentType")
            log.info("               Origo application version : $applicationVersion")
            log.info("                    Origo application ID : $applicationId")

            requestHeaders = [
                    'Authorization' : authorization,
                    'Content-Type' : contentType,
                    'Application-Version' : applicationVersion,
                    'Application-ID' : applicationId
            ]

    }

    def createCallbackSubscription() {
        // Initial action of OrigoIntegration application - Requests all users for given organization.

        HttpResponse<String> response
        String serializedBody = new ObjectMapper().writeValueAsString([
            "url": "$callbackUrl",
            "filterId": "$filterId",
            "httpHeader": "Authorization",
            "secret": "$authorization"
        ])

        try {
            response = Unirest.post(eventManagementApi + "/organization/$organizationId/callback")
                    .headers(requestHeaders)
                    .body(serializedBody)
                    .asString()

//            if (response.status != 201) {
//                log.error() pending shape of response
//            }

            log.info("Response: $response")

        } catch (HttpException e) { // ?
            log.error(e.message)
        }
    }

    static def uploadUserPhoto() {
        // posts photo to User's Origo profile: https://doc.origo.hidglobal.com/api/mobile-identities/#/Photo%20ID/post-customer-organization_id-users-user_id-photo
    }

    static def checkExistingCallbackFns() {
        // checks for existing call back subscriptions.
    }

    static def checkExistingFilters() {
        // checks for current filters. Conditionally calls create filter
    }

    static def createFilter() {
        // Filters what kinds of events this instance of the application will be subscribed to. See documentation for options: https://doc.origo.hidglobal.com/api/events-callbacks/#/Events/post_organization__organization_id__events_filter
    }

    static def storePhoto() {
        // Stores photo in Origo system
    }

    static def storePersonData() {
        // stores 'customFields' information in Origo employee record
    }

}
