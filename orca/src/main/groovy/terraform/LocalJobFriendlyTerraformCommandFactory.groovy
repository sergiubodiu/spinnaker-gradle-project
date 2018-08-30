package terraform

class LocalJobFriendlyTerraformCommandFactory implements TerraformCommandFactory {

    @Override
    List<String> applyTerraformCommand(String baseCommand,
                                       Map<String, String> parameterMap,
                                       String absoluteVarFilePath,
                                       String absoluteDirOrPlanPath) {
      def terraformCommand = [baseCommand, "terraform", "apply", "-parallelism=n", "-auto-approve"]
      parameterMap.each { key, value ->
        if (key && value) {
          def keyValuePair = "$key=${value instanceof String ? value.trim() : value}"

          terraformCommand << "-var"
          terraformCommand << keyValuePair.toString()
        }
      }

      if (absoluteVarFilePath) {
        terraformCommand << "-var-file=$absoluteVarFilePath".toString()
      }

      terraformCommand << absoluteDirOrPlanPath
      terraformCommand.removeAll([null, ""])

      terraformCommand
    }

  }
