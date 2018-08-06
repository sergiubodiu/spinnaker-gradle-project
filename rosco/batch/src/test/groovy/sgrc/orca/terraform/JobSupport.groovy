package sgrc.orca.terraform

import org.springframework.batch.core.Job
import org.springframework.batch.core.JobExecution
import org.springframework.batch.core.JobParametersIncrementer
import org.springframework.batch.core.JobParametersValidator
import org.springframework.batch.core.Step
import org.springframework.batch.core.UnexpectedJobExecutionException
import org.springframework.batch.core.job.DefaultJobParametersValidator
import org.springframework.batch.core.step.NoSuchStepException
import org.springframework.batch.core.step.StepLocator
import org.springframework.beans.factory.BeanNameAware

class JobSupport implements BeanNameAware, Job, StepLocator {

  private Map<String, Step> steps = new HashMap<String, Step>()

  String name

  private boolean restartable = false

  private int startLimit = Integer.MAX_VALUE

  private DefaultJobParametersValidator jobParametersValidator = new DefaultJobParametersValidator()

  JobSupport() {
    super()
  }

  JobSupport(String name) {
    this.name = name
  }

  /**
   * Set the name property if it is not already set. Because of the order of
   * the callbacks in a Spring container the name property will be set first
   * if it is present. Care is needed with bean definition inheritance - if a
   * parent bean has a name, then its children need an explicit name as well,
   * otherwise they will not be unique.
   *
   * @see org.springframework.beans.factory.BeanNameAware#setBeanName(java.lang.String)
   */
  @Override
  void setBeanName(String name) {
    if (this.name == null) {
      this.name = name
    }
  }

  @Override
  String getName() {
    return name
  }

  void setJobParametersValidator(DefaultJobParametersValidator jobParametersValidator) {
    this.jobParametersValidator = jobParametersValidator
  }

  void setSteps(List<Step> steps) {
    this.steps.clear()
    for (Step step : steps) {
      this.steps.put(step.getName(), step)
    }
  }

  void addStep(Step step) {
    this.steps.put(step.getName(), step)
  }

  int getStartLimit() {
    return startLimit
  }

  void setStartLimit(int startLimit) {
    this.startLimit = startLimit
  }

  void setRestartable(boolean restartable) {
    this.restartable = restartable
  }

  @Override
  boolean isRestartable() {
    return restartable
  }

  @Override
  void execute(JobExecution execution) throws UnexpectedJobExecutionException {
    throw new UnsupportedOperationException(
        "JobSupport does not provide an implementation of execute().  Use a smarter subclass.")
  }

  @Override
  JobParametersIncrementer getJobParametersIncrementer() {
    return null
  }

  @Override
  JobParametersValidator getJobParametersValidator() {
    return jobParametersValidator
  }

  @Override
  Collection<String> getStepNames() {
    return steps.keySet()
  }

  @Override
  Step getStep(String stepName) throws NoSuchStepException {
    final Step step = steps.get(stepName)
    if (step == null) {
      throw new NoSuchStepException("Step ["+stepName+"] does not exist for job with name ["+getName()+"]")
    }
    return step
  }

  @Override
  String toString() {
    return "JobSupport{" +
        "name='" + name + '\'' +
        '}'
  }
}