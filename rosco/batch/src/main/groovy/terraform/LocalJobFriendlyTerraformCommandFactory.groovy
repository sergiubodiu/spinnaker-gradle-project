package terraform

class LocalJobFriendlyTerraformCommandFactory implements TerraformCommandFactory {

    @Override
    List<String> buildTerraformCommand(String baseCommand,
                                    Map<String, String> parameterMap,
                                    String absoluteVarFilePath,
                                    String absoluteTemplateFilePath) {
      def packerCommand = [baseCommand, "packer", "build", "-color=false"]
      parameterMap.each { key, value ->
        if (key && value) {
          def keyValuePair = "$key=${value instanceof String ? value.trim() : value}"

          packerCommand << "-var"
          packerCommand << keyValuePair.toString()
        }
      }

      if (absoluteVarFilePath) {
        packerCommand << "-var-file=$absoluteVarFilePath".toString()
      }

      packerCommand << absoluteTemplateFilePath
      packerCommand.removeAll([null, ""])

      packerCommand
    }

  }
