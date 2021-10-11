#!/usr/bin/env groovy

package vars


def call(Map config=[:]) {
    stage("Static Analysis: Checkmarx") {
        String project = config.Project
     	project = project.replace('\\','\\\\')
        String SASTHigh = config.High ?: '5'
      	String SASTMedium = config.Medium ?: '5'
        String CxServer = "https://checkmarx.corp.n-able.com" //"https://192.69.16.204"
        String userPreset = config.Preset ?: 'High and Medium'
        String creds = config.CxCred
      	String lang = config.Language
        String preset = lang + " - " + userPreset //" - High and Medium" 
      	String script = ""

        inside_image "https://registry.hub.docker.com","karnc","checkmarx:openjdk-8","docker-creds", {
         	 	
        dir("${WORKSPACE}") {
		withChecks('SAST Scan') {
			withCredentials([
				usernamePassword(credentialsId: creds, passwordVariable: 'CHECKMARX_PASS', usernameVariable: 'CHECKMARX_UNAME')
                          	]) {
				script = """/opt/CxConsolePlugin/runCxConsole.sh scan -v \
                                      -ProjectName \"$project\" \\
                                      -CxServer \"CxServer\" \\
                                      -LocationType \"folder\" \\
                                      -SASTHigh \"$SASTHigh\" \\
                                      -SASTMedium \"$SASTMedium\" \\
                                      -Preset \"$preset\" \\
                                      -ReportXML \"cx_output.xml\" \\
                                      -LocationPath \"${WORKSPACE}\" \\
                                      -TrustedCertificates \\
                                      -Incremental \\
                                      -CxUser \"\$CHECKMARX_UNAME\" \\
                                      -CxPassword \"\$CHECKMARX_PASS\""""

				      /*def statusCode = sh (script:script, returnStatus:true)

				      if(statusCode == 0 || statusCode > 5) {
					  sh "cp /opt/CxConsolePlugin/Checkmarx/Reports/cx_output.xml ${WORKSPACE}"
					  archiveArtifacts artifacts: "cx_output.xml"
				      }*/
        			}
                 	}
		}
	}
    }
}
