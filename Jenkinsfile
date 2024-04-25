@Library('gx-library@main') _

import com.genexus.PropertiesHelper

pipeline {
	agent any
	parameters {
		choice(
				name: 'Accion',
				choices: ['Update', 'Build', 'Deploy'],
				description: 'Acción a ejecutar'
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
					
					withCredentials([usernamePassword(credentialsId: projectDefinition.gxserverCredentials, usernameVariable: 'dbUsername', passwordVariable: 'dbPassword')]){
						echo "INFO *${dbUsername}* and *${dbPassword}*"
					}
                }
            }
        }
        stage("Checkout/Update Knowledge Base") {
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
	}
}
