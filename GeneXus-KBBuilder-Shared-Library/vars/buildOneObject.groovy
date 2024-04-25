/*
 * Job buildOneObject >> This method executes the 'Build' task for one object.
 *
 * Custom Configuration:
 * - "Keep GAM database updated" = false
 * - "Deploy business processes on build" = No
 * - "Populate Data" = false
 * - "Reorganize server tables" = No
 * - "Deploy to cloud" = No
 *
 * Parameters:
 * - args: A map containing the following parameters:
 *   - gxBasePath: The base path of the GeneXus installation.
 *   - localKBPath: The local path of the Knowledge Base.
 *   - environmentName: The name of the environment.
 *   - msbuildExePath: The path to the MSBuild executable.
 *   - forceRebuild: A boolean indicating whether to force a rebuild.
 *	 - ObjectName: The object co compile.
 */
 
def call(Map args = [:]) {
    // Sync cdxci.msbuild
    def fileContents = libraryResource 'com/genexus/templates/cdxci.msbuild'
    writeFile file: 'cdxci.msbuild', text: fileContents
    // Sync properties.msbuild
    fileContents = libraryResource 'com/genexus/templates/properties.msbuild'
    writeFile file: 'properties.msbuild', text: fileContents

    bat label: 'Avoid Datastore connections', 
        script: """
            "${args.msbuildExePath}" "${WORKSPACE}\\properties.msbuild" \
            /p:GX_PROGRAM_DIR="${args.gxBasePath}" \
            /p:localKbPath="${args.localKBPath}" \
            /p:environmentName="${args.environmentName}" \
            /t:AvoidDatastoreConnections
        """

    bat label: 'Build One', 
        script: """
            "${args.msbuildExePath}" "${WORKSPACE}\\cdxci.msbuild" \
            /p:GX_PROGRAM_DIR="${args.gxBasePath}" \
            /p:localKbPath="${args.localKBPath}" \
            /p:environmentName="${args.environmentName}" \
            /p:rebuild="${args.forceRebuild}" \
			/p:ObjectName="${args.ObjectName}" \
            /t:BuildOne
        """
}