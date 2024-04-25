@Library('gx-library@main') _

import com.genexus.PropertiesHelper

pipeline {
	agent any
	parameters {
		choice(
				name: 'ACTION',
				choices: ['Update', 'Build', 'Build only', 'Deploy'],
				description: 'Ejecutar'
				)
	}
	stages {
		stage('Read Project Properties') {
            steps {
                script {
                    projectDefinition = readProperties file: 'buildernode.properties';
                    projectDefinition = readProperties defaults: projectDefinition, file: 'knowledgebase.properties';
                                        
                    echo "INFO MSBuild:: ${projectDefinition.msbuildExePath}"
                    echo "INFO GeneXus Installation:: ${projectDefinition.gxBasePath}"
                    echo "INFO KnowledgeBase:: ${projectDefinition.localKBPath}"
                }
            }
        }
        stage("Checkout/Update Knowledge Base") {
			when {
                expression { params.ACTION == 'Update' || params.ACTION == 'Build' || params.ACTION == 'Deploy'}
            }
            steps {
                script {
                    gxserver changelog: true, poll: true,
                        gxCustomPath: "${projectDefinition.gxBasePath}",
                        msbuildCustomPath: "${projectDefinition.msbuildExePath}",
                        serverURL: projectDefinition.gxserverURL,
                        credentialsId: projectDefinition.gxserverCredentials,
                        kbName: projectDefinition.gxserverKB,
                        kbVersion: projectDefinition.gxserverVersion,
                        kbDbServerInstance: "${projectDefinition.kbDbServerInstance}",
                        localKbPath: "${projectDefinition.localKBPath}",
                        localKbVersion: projectDefinition.gxserverKB
                }
            }
        }
		stage("Build Knowledge Base") {
			when {
                expression { params.ACTION == 'Build' || params.ACTION == 'Build only' || params.ACTION == 'Deploy'}
            }
            steps {
                script {
                    environmentDefinition = readProperties defaults: projectDefinition, file: 'environment.properties';
                    environmentDefinition.targetPath = help.getEnvironmentProperty(environmentDefinition, "TargetPath")
                    echo "[INFO] ReadTargetPath = ${environmentDefinition.targetPath}"
                    configureDataStore(environmentDefinition)
                    buildInstallationEnvironment(environmentDefinition)
                }
            }
        }
	}
}
