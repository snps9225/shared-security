#!/usr/bin/env groovy

package vars

/*
  A helper function for running pipeline code inside
  pipeline image. 
  
  ex: 
  inside_image "openshift_helm", {
    sh "helm version"
  }
*/
def call(String registry, String repository, String image, String creds, Closure body){
 
  /*
  config.images ?: { error "SDP Image Config not defined in Pipeline Config" } ()
  
  def sdp_img_reg = config.images.registry ?:
                    { error "SDP Image Registry not defined in Pipeline Config" } ()
  
  def sdp_img_repo = config.images.repository ?:
                     { return "sdp" }()
                     
  def sdp_img_repo_cred = config.images.cred ?:
                          { error "SDP Image Repository Credential not defined in Pipeline Config" }()
                          
  def docker_args = config.images.docker_args ?:
                    { return ""}()
  */
  String repo = repository
  String img = image
  //sh 'echo \"${repo}\${img}\"'
  
  docker.withRegistry(registry, creds){
    sh 'echo \"inside with registry\"'
    docker.image("${repo}/${img}").inside(""){
      body()
    }
  }
}
