pipeline {
  agent any
  stages {
    stage('Snapshot Build & Deploy for Java 21') {
      steps {
        echo 'Start Build and Deployment of the EnFlex.IT Eclipse-Tools ...'
        sh 'mvn --version'
        sh 'mvn clean install -P p2Deploy -e -X -f de.enflexit.tools -Dtycho.localArtifacts=ignore -Dtycho.localArtifacts=ignore -Dtycho.p2.transport.min-cache-minutes=0'
        echo 'Build & Deployment of the EnFlex.IT Eclipse-Tools is done!'
      }
    }

  }
}