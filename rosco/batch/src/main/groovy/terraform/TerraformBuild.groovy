package terraform
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.databind.PropertyNamingStrategy
import com.fasterxml.jackson.databind.annotation.JsonNaming

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
class TerraformBuild {
  String name
  String builderType
  String artifactId
  String terraformRunUuid

  @Override
  String toString() {
    return "TerraformBuild{" +
            "name='" + name + '\'' +
            ", builderType='" + builderType + '\'' +
            ", artifactId='" + artifactId + '\'' +
            ", terraformRunUuid='" + terraformRunUuid + '\'' +
            '}'
  }

  @Override
  boolean equals(Object o) {
    if (this == o) return true
    if (o == null || getClass() != o.getClass()) return false
    TerraformBuild that = (TerraformBuild) o
    return Objects.equals(name, that.name) &&
            Objects.equals(builderType, that.builderType) &&
            Objects.equals(artifactId, that.artifactId) &&
            Objects.equals(terraformRunUuid, that.terraformRunUuid)
  }

  @Override
  int hashCode() {
    return Objects.hash(name, builderType, artifactId, terraformRunUuid)
  }

}