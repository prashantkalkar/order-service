node {
    checkout scm

    stage('abort-on-maven-release-commit') {
        def latestCommitMessage = sh script: 'git log -n 1 --pretty=format:"%s"', returnStdout: true
        if (latestCommitMessage == '[maven-release-plugin] prepare for next development iteration') {
            currentBuild.result = 'ABORTED'
            return
        }
    }

    stage('Build & Test') {
        if(currentBuild.result != 'ABORTED' && currentBuild.result != 'FAILURE') {
            // TODO - Run maven tests, stash the generated pact files.
        }
    }

    stage('build and release artifact') {
        if(currentBuild.result != 'ABORTED' && currentBuild.result != 'FAILURE') {
            sh 'mvn -Dresume=false -DdryRun=true release:prepare -Darguments="-DskipTests"'
            sh 'mvn -Dresume=false release:prepare release:perform -Darguments="-DskipTests"'
        }
    }

    stage('Publish Pact to pact broker') {
        if(currentBuild.result != 'ABORTED' && currentBuild.result != 'FAILURE') {
            // TODO - Unstash pact files.
            // TODO - Extract application version
            // TODO - Publish pacts to pact broker
        }
    }

    // need a better strategy for post build notifications - may be declarative pipeline.
    stage("slack notification") {
        if(currentBuild.result == "FAILURE") {
            slackSend (color: '#E01563', message: "FAILED: Job '${env.JOB_NAME} [${env.BUILD_NUMBER}]' (${env.BUILD_URL})", channel: '#order-service-team')
        } else if(currentBuild.result == "UNSTABLE") {
            slackSend (color: '#E01563', message: "UNSTABLE: Job '${env.JOB_NAME} [${env.BUILD_NUMBER}]' (${env.BUILD_URL})", channel: '#order-service-team')
        } else if(currentBuild.getPreviousBuild() != null && (currentBuild.getPreviousBuild().result == 'FAILURE' || currentBuild.getPreviousBuild().result == 'UNSTABLE')) {
            slackSend (color: '#3EB991', message: "BACK TO NORMAL: Job '${env.JOB_NAME} [${env.BUILD_NUMBER}]' (${env.BUILD_URL})", channel: '#order-service-team')
        }
    }
}