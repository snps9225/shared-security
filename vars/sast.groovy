#!/usr/bin/env groovy

package vars


def call(String projectName, String sastHigh, String sastMedium, String cxCred, String language) {
    stage("Static Analysis: Checkmarx") {
        String project = projectName
	project = project.replace('\\','\\\\')
        String SASTHigh = sastHigh ?: '5'
	String SASTMedium = sastMedium ?: '5'
        String CxServer = "https://192.69.16.204"
        //String userPreset = userPreset ?: 'High and Medium'
        String creds = cxCred
	String lang = language
        String preset = lang + " - High and Medium" //+ userPreset
	String script = ""

        inside_image "https://registry.hub.docker.com","karnc","checkmarx:openjdk-8","docker-creds", {
         	 	
		dir("${WORKSPACE}")
			{
                withChecks('SAST Scan') 
				{
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
                            
                            def statusCode = sh (script:script, returnStatus:true)
						
                            if(statusCode == 0 || statusCode > 5) {
                                sh "cp /opt/CxConsolePlugin/Checkmarx/Reports/cx_output.xml ${WORKSPACE}"
                                archiveArtifacts artifacts: "cx_output.xml"
                            }
					    }
                }
			}
		}
    }
}
