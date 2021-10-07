#!/usr/bin/env groovy

def call(String name) {
    stage("Static Analysis: Checkmarx") {
        //String project = ${env.ProjectName}
	    String project = name
		    project = project.replace('\\','\\\\')
        String SASTHigh = ${env.SASTHigh} ?: '5'
		    String SASTMedium = ${env.SASTMedium} ?: '5'
        String CxServer = ${env.CxServer} ?: 'URL'
        String userPreset = ${env.UserPreset} ?: 'High and Medium'
        String creds = ${env.CxCred}
		    String lang = ${env.Language}
        String preset = lang + " - " + userPreset
		    String script = ""

        inside_sdp_image "checkmarx:openjdk-8", {
         	 	
        dir("${WORKSPACE}") {
            withChecks('SAST Scan') {
                  withCredentials([
                          usernamePassword(credentialsId: creds, passwordVariable: 'CHECKMARX_PASS', usernameVariable: 'CHECKMARX_UNAME')
                          ]) {
                              script = """/opt/CxConsolePlugin/runCxConsole.sh scan -v \
                                      -ProjectName \"$project\" \\
                                      -CxServer \"$CxServer\" \\
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
