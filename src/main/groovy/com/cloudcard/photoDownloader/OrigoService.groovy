package com.cloudcard.photoDownloader

import jakarta.annotation.PostConstruct
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.stereotype.Service

@Service
@ConditionalOnProperty(name = 'Origo.useOrigo', havingValue = 'true')
class OrigoService {
    // Handles business logic / processing for incoming events and requests from API
    private static final Logger log = LoggerFactory.getLogger(OrigoService.class)

    @Value('${Origo.filterSet}')
    String filterSet

    @Autowired
    OrigoClient origoClient

    @Autowired
    CloudCardClient cloudCardClient

    @Autowired
    OrigoEventStorageServiceLocal eventStorageServiceLocal

    @PostConstruct
    init() {
            List<String> filterSet = ["USER_CREATED"]
            OrigoResponse response = origoClient.createFilter(filterSet)
            String filterId = response.body.filterId
            getEvents(null, null, filterId)
    }

    private boolean getNewAccessToken() {
        OrigoResponse response = origoClient.authenticate()
        boolean result

        if (response.success) {
            String token = response.body.access_token
            origoClient.setAccessToken(token)
            result = true
        } else {
            log.error("ORIGO: Cannot obtain access token")
            result = false
        }

        return result
    }

    private void getEvents(String dateFrom = "", String dateTo = "", String filterId = "", String callbackStatus = "") {
        OrigoResponse response = origoClient.listEvents(dateFrom, dateTo, filterId, callbackStatus)

        if (response.success) {
            ArrayList<Object> events = response.body as ArrayList<Object>
            processNewUsers(events)
        } else if (response.body.responseHeader.statusCode == 401) {
            Closure command = { getEvents(dateFrom, dateTo, filterId, callbackStatus) }
            authenticateAndRetry(command, "getEvents")
        }
    }

    private void authenticateAndRetry(Closure command, String commandName) {
        boolean tokenSaved = getNewAccessToken()
        if (tokenSaved) {
            log.info("ORIGOSERVICE: Retrying ${commandName}() with valid access token.")
            command()
        } else {
            log.error("ORIGOSERVICE: Cannot authenticate.")
        }
    }

    private static void processNewUsers(ArrayList<Object> events) {
        log.info("ORIGOSERVICE: Processing received events.")
        events.each {
            if (it.data.status == "USER_CREATED") {
                log.info("ORIGOSERVICE: Provisioning Origo user, $it.data.userId, in CloudCard API")
                log.info "************************************************************"
            }
        }
    }
}
