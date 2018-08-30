package terraform

import terraform.util.TestDefaults
import spock.lang.Specification

class TerraformManifestSpec extends Specification implements TestDefaults {
  void "getLastBuild"() {
    setup:
      def firstBuild = new TerraformBuild(terraformRunUuid: UUID.randomUUID().toString())
      def secondBuild = new TerraformBuild(terraformRunUuid: UUID.randomUUID().toString())
    TerraformManifest manifest

    when:
      manifest = new TerraformManifest(builds: [firstBuild, secondBuild], lastRunUuid: firstBuild.getTerraformRunUuid())
    then:
      manifest.getLastBuild() == firstBuild

    when:
      manifest = new TerraformManifest(builds: [firstBuild, secondBuild], lastRunUuid: secondBuild.getTerraformRunUuid())
    then:
      manifest.getLastBuild() == secondBuild

    when:
      manifest = new TerraformManifest(builds: [firstBuild, secondBuild], lastRunUuid: UUID.randomUUID().toString())
      manifest.getLastBuild()
    then:
      thrown(IllegalStateException)

    when:
      manifest = new TerraformManifest(builds: [], lastRunUuid: UUID.randomUUID().toString())
      manifest.getLastBuild()
    then:
      thrown(IllegalStateException)
  }
}
