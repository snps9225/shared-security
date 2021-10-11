#!/usr/bin/env groovy

package vars

def call(Map config=[:]) {
    stage("Composition Analysis: WhiteSource") {
      String product 	= config.Product
      String project 	= config.Project
      //String configs 	= ""
      String ApiKey 	= "N-able-api-key"
      String UserKey 	= config.UserKey
      String WssUrl 	= "https://app.whitesourcesoftware.com/agent"
      String script 	= ""
      
      script = 'java -jar /opt/wss-unified-agent.jar'
      inside_image "https://registry.hub.docker.com","karnc","whitesource:openjdk-8","docker-creds", {
        dir("${WORKSPACE}") {
          //unstash name: 'build'
          withChecks('SCA Scan') {
            withCredentials([string(credentialsId: ApiKey, variable: 'API_KEY'), string(credentialsId: UserKey, variable: 'USER_KEY')]) {
              script = script + ' -apiKey ' + "$API_KEY" + ' -userKey ' + "$USER_KEY" + ' -product ' + product + ' -project ' + project
              script = script + ' -wss.url ' + WssUrl + ' -c /opt/wss-unified-agent.config -d ./. -generateScanReport true'
              def statusCode = sh script:script, returnStatus:true
              if(statusCode==0) {
                //sh "cp **/*scan_report.json ${WORKSPACE}"
                //archiveArtifacts artifacts: "**/*scan_report.json"
              }
					  }
				  }
        }
      }
    }
}
