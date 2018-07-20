/*
 * Copyright 2018 Google, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License")
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.netflix.spinnaker.rosco.providers.util;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import java.util.List;
import java.util.Objects;


@JsonIgnoreProperties(ignoreUnknown = true)
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class PackerManifest {
  private List<PackerBuild> builds;
  private String lastRunUuid;

  public PackerBuild getLastBuild() {
    return builds.stream()
      .filter(build -> build.packerRunUuid.equals(lastRunUuid))
      .findFirst()
      .orElseThrow(() -> new IllegalStateException("Could not find most recent build in packer manifest"));
  }

  @Override
  public String toString() {
    return "PackerManifest{" +
            "builds=" + builds +
            ", lastRunUuid='" + lastRunUuid + '\'' +
            '}';
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    PackerManifest that = (PackerManifest) o;
    return Objects.equals(builds, that.builds) &&
            Objects.equals(lastRunUuid, that.lastRunUuid);
  }

  @Override
  public int hashCode() {

    return Objects.hash(builds, lastRunUuid);
  }

  public List<PackerBuild> getBuilds() {
    return builds;
  }

  public void setBuilds(List<PackerBuild> builds) {
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
  public static class PackerBuild {
    private String name;
    private String builderType;
    private String artifactId;
    private String packerRunUuid;

    @Override
    public String toString() {
      return "PackerBuild{" +
              "name='" + name + '\'' +
              ", builderType='" + builderType + '\'' +
              ", artifactId='" + artifactId + '\'' +
              ", packerRunUuid='" + packerRunUuid + '\'' +
              '}';
    }

    @Override
    public boolean equals(Object o) {
      if (this == o) return true;
      if (o == null || getClass() != o.getClass()) return false;
      PackerBuild that = (PackerBuild) o;
      return Objects.equals(name, that.name) &&
              Objects.equals(builderType, that.builderType) &&
              Objects.equals(artifactId, that.artifactId) &&
              Objects.equals(packerRunUuid, that.packerRunUuid);
    }

    @Override
    public int hashCode() {

      return Objects.hash(name, builderType, artifactId, packerRunUuid);
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

    public String getPackerRunUuid() {
      return packerRunUuid;
    }

    public void setPackerRunUuid(String packerRunUuid) {
      this.packerRunUuid = packerRunUuid;
    }
  }
}
