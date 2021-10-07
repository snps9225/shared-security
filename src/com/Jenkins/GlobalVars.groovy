package com.jenkins

class GlobalVars {
  private String envType
  private String serviceName
  private String tillerNamespace
  private String deployCommand
  private String rollbackCommand
  private String undeployCommand
  
   ServiceConfig(
            String envType,
            String serviceName,
            String tillerNamespace,
            String deployCommand,
            String rollbackCommand,
            String undeployCommand
    ){
        this.envType = envType
        this.serviceName = serviceName
        this.tillerNamespace = tillerNamespace
        this.deployCommand = deployCommand
        this.rollbackCommand = rollbackCommand
        this.undeployCommand = undeployCommand
    }

    String getEnvType() {
        return envType
    }

    void setEnvType(String envType) {
        this.envType = envType
    }

    String getServiceName() {
        return serviceName
    }

    void setServiceName(String serviceName) {
        this.serviceName = serviceName
    }

    String getTillerNamespace() {
        return tillerNamespace
    }

    void setTillerNamespace(String tillerNamespace) {
        this.tillerNamespace = tillerNamespace
    }

    String getDeployCommand() {
        return deployCommand
    }

    void setDeployCommand(String deployCommand) {
        this.deployCommand = deployCommand
    }

    String getRollbackCommand() {
        return rollbackCommand
    }

    void setRollbackCommand(String rollbackCommand) {
        this.rollbackCommand = rollbackCommand
    }

    String getUndeployCommand() {
        return undeployCommand
    }

    void setUndeployCommand(String undeployCommand) {
        this.undeployCommand = undeployCommand
    }
}
