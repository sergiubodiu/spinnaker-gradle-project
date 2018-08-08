package terraform;

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
      .filter(build -> build.getTerraformRunUuid().equals(lastRunUuid))
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

}
