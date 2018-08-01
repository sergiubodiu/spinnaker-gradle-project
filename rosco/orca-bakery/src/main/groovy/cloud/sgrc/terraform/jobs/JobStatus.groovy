package cloud.sgrc.terraform.jobs;

import groovy.transform.CompileStatic
import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString

@CompileStatic
@EqualsAndHashCode(includes = "id")
@ToString(includeNames = true)
class JobStatus implements Serializable {

  String id

  State state

  Result result

  static enum State {
    PENDING, RUNNING, COMPLETED, SUSPENDED, CANCELLED
  }

  static enum Result {
    SUCCESS, FAILURE
  }
}