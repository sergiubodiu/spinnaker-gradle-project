package terraform

import terraform.jobs.JobRequest
import terraform.util.TestDefaults
import org.apache.commons.exec.CommandLine
import spock.lang.Shared
import spock.lang.Specification
import spock.lang.Unroll

class LocalJobFriendlyTerraformCommandFactorySpec extends Specification implements TestDefaults {

  @Shared
  LocalJobFriendlyTerraformCommandFactory commandFactory = new LocalJobFriendlyTerraformCommandFactory()

  @Unroll
  void "terraformCommand handles baseCommand as null, empty string and real string"() {
    setup:
      def parameterMap = [
        something: something
      ]

    when:
      def packerCommand = commandFactory.applyTerraformCommand(baseCommand, parameterMap, null, "")

    then:
      packerCommand == expectedPackerCommand

    where:
      something | baseCommand | expectedPackerCommand
      "sudo"    | "sudo"      | ["sudo", "terraform", "apply", "-parallelism=n", "-auto-approve", "-var", "something=sudo"]
      "null"    | null        | ["terraform", "apply", "-parallelism=n", "-auto-approve", "-var", "something=null"]
      "empty"   | ""          | ["terraform", "apply", "-parallelism=n", "-auto-approve", "-var", "something=empty"]
  }

  @Unroll
  void "terraformCommand includes -varFileName only when 'varFile' is specified; varFile is #varFile"() {
    setup:
      def parameterMap = [
        something: "some-var"
      ]

    when:
      def terraformCommand = commandFactory.applyTerraformCommand("", parameterMap, varFile, "")

    then:
      terraformCommand == expectedPackerCommand

    where:
      varFile            | expectedPackerCommand
      null               | ["terraform", "apply", "-parallelism=n", "-auto-approve", "-var", "something=some-var"]
      ""                 | ["terraform", "apply", "-parallelism=n", "-auto-approve", "-var", "something=some-var"]
      "someVarFile.json" | ["terraform", "apply", "-parallelism=n", "-auto-approve", "-var", "something=some-var", "-var-file=someVarFile.json"]
  }

  @Unroll
  void "terraformCommand includes parameter with non-quoted string"() {

    when:
    def terraformCommand = commandFactory.applyTerraformCommand("", parameterMap, null, "")

    then:
    terraformCommand == expectedPackerCommand

    where:
    parameterMap                      | expectedPackerCommand
    [packages: "package1 package2"]   | ["terraform", "apply", "-parallelism=n", "-auto-approve", "-var", "packages=package1 package2"]
  }

  @Unroll
  void 'validate terraform command line' () {
    setup:

    when:
      def packerCommand = commandFactory.applyTerraformCommand("", parameterMap, null, "")
      def jobRequest = new JobRequest(tokenizedCommand: packerCommand, maskedParameters: maskedPackerParameters, jobId: SOME_UUID)
      def commandLine = new CommandLine(jobRequest.tokenizedCommand[0])
      def arguments = (String []) Arrays.copyOfRange(jobRequest.tokenizedCommand.toArray(), 1, jobRequest.tokenizedCommand.size())
      commandLine.addArguments(arguments, false)
      def cmdLineList =  commandLine.toStrings().toList()

    then:
      cmdLineList  == expectedCommandLine

    where:
      parameterMap                          | maskedPackerParameters | expectedCommandLine
      [packages: "package1 package2"]       | []                     | ["terraform", "apply", "-parallelism=n", "-auto-approve", "-var", "packages=package1 package2"]
  }
}
