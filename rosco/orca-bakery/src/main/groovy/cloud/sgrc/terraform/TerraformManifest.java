package cloud.sgrc.terraform;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import java.util.List;
import java.util.Objects;


@JsonIgnoreProperties(ignoreUnknown = true)
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class TerraformManifest {
  private List<TerraformBuild> builds;
  private String lastRunUuid;

  public TerraformBuild getLastBuild() {
    return builds.stream()
      .filter(build -> build.terraformRunUuid.equals(lastRunUuid))
      .findFirst()
      .orElseThrow(() -> new IllegalStateException("Could not find most recent build in terraform manifest"));
  }

  @Override
  public String toString() {
    return "TerraformManifest{" +
            "builds=" + builds +
            ", lastRunUuid='" + lastRunUuid + '\'' +
            '}';
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    TerraformManifest that = (TerraformManifest) o;
    return Objects.equals(builds, that.builds) &&
            Objects.equals(lastRunUuid, that.lastRunUuid);
  }

  @Override
  public int hashCode() {

    return Objects.hash(builds, lastRunUuid);
  }

  public List<TerraformBuild> getBuilds() {
    return builds;
  }

  public void setBuilds(List<TerraformBuild> builds) {
    this.builds = builds;
  }

  public String getLastRunUuid() {
    return lastRunUuid;
  }

  public void setLastRunUuid(String lastRunUuid) {
    this.lastRunUuid = lastRunUuid;
  }

  @JsonIgnoreProperties(ignoreUnknown = true)
  @JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
  public static class TerraformBuild {
    private String name;
    private String builderType;
    private String artifactId;
    private String terraformRunUuid;

    @Override
    public String toString() {
      return "TerraformBuild{" +
              "name='" + name + '\'' +
              ", builderType='" + builderType + '\'' +
              ", artifactId='" + artifactId + '\'' +
              ", terraformRunUuid='" + terraformRunUuid + '\'' +
              '}';
    }

    @Override
    public boolean equals(Object o) {
      if (this == o) return true;
      if (o == null || getClass() != o.getClass()) return false;
      TerraformBuild that = (TerraformBuild) o;
      return Objects.equals(name, that.name) &&
              Objects.equals(builderType, that.builderType) &&
              Objects.equals(artifactId, that.artifactId) &&
              Objects.equals(terraformRunUuid, that.terraformRunUuid);
    }

    @Override
    public int hashCode() {

      return Objects.hash(name, builderType, artifactId, terraformRunUuid);
    }

    public String getName() {
      return name;
    }

    public void setName(String name) {
      this.name = name;
    }

    public String getBuilderType() {
      return builderType;
    }

    public void setBuilderType(String builderType) {
      this.builderType = builderType;
    }

    public String getArtifactId() {
      return artifactId;
    }

    public void setArtifactId(String artifactId) {
      this.artifactId = artifactId;
    }

    public String getTerraformRunUuid() {
      return terraformRunUuid;
    }

    public void setTerraformRunUuid(String terraformRunUuid) {
      this.terraformRunUuid = terraformRunUuid;
    }
  }
}
