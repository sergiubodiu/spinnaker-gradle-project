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

package sgrc.orca.terraform;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

public class TerraformManifestService {
  private Path tempDir = Paths.get(System.getProperty("java.io.tmpdir"));
  private ObjectMapper objectMapper = new ObjectMapper();

  public String getManifestFileName(String bakeId) {
    return getManifestPath(bakeId).toString();
  }

  public boolean manifestExists(String bakeId) {
    return Files.exists(getManifestPath(bakeId));
  }

  public TerraformBuild getBuild(String bakeId) {
    terraform.TerraformManifest manifest = getManifest(bakeId);
    return manifest.getLastBuild();
  }

  private terraform.TerraformManifest getManifest(String bakeId) {
    terraform.TerraformManifest manifestData;
    try (
      BufferedInputStream manifestInput =
        new BufferedInputStream(
          Files.newInputStream(getManifestPath(bakeId), StandardOpenOption.READ, StandardOpenOption.DELETE_ON_CLOSE)
        )
    ) {
      manifestData = objectMapper.readValue(manifestInput, terraform.TerraformManifest.class);
    } catch (IOException e) {
      throw new IllegalStateException("Could not read packer manifest file: " + e.getMessage());
    }
    return manifestData;
  }

  private Path getManifestPath(String bakeId) {
    return tempDir.resolve(bakeId + "-manifest.json");
  }
}
