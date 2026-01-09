pipeline {
  agent any
  stages {
    stage('Snapshot Build & Deploy for Java 21') {
      steps {
        echo 'Start Build and Deployment of the EnFlex.IT Eclipse-Tools ...'
        sh 'sh \'mvn --version\''
        sh 'sh \'mvn clean install -P p2DeployClean -f de.enflexit.tools.core -Dtycho.localArtifacts=ignore -Dtycho.localArtifacts=ignore -Dtycho.p2.transport.min-cache-minutes=0\''
        echo 'Build & Deployment of the EnFlex.IT Eclipse-Tools is done!'
      }
    }

  }
}