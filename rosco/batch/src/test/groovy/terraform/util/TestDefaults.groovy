package terraform.util


trait TestDefaults {

  static final String PACKAGES_NAME = "kato nflx-djangobase-enhanced_0.1-h12.170cdbd_all mongodb"
  static final String NUPKG_PACKAGES_NAME = "googlechrome javaruntime"
  static final String DEBIAN_REPOSITORY = "http://some-debian-repository"
  static final String YUM_REPOSITORY = "http://some-yum-repository"
  static final String CHOCOLATEY_REPOSITORY = "http://some-chocolatey-repository"

  static final String SOME_MILLISECONDS = "1470391070464"
  static final String SOME_UUID = "55c25239-4de5-4f7a-b664-6070a1389680"
  static final String SOME_BUILD_INFO_URL = "http://some-build-server:8080/repogroup/repo/builds/320282"
  static final String SOME_BUILD_NR = "42"
  static final String SOME_COMMIT_HASH = "170cdbd"
  static final String SOME_DOCKER_TAG = "latest"
  static final String SOME_APP_VERSION_STR = "nflx-djangobase-enhanced-0.1-h12.170cdbd"
  static final String SOME_BAKE_RECIPE_NAME = "djangobase"
  static final String SOME_JOB_ID = "123"
  static final String SOME_AMI_ID = "ami-3cf4a854"
  static final String SOME_IMAGE_NAME = "ubuntu/trusty"
  static final String SOME_CLOUD_PROVIDER = "aws"
  static final String SOME_REGION = "eu-west-1"

  def parseDebOsPackageNames(String packages) {
    PackageNameConverter.buildOsPackageNames(DEB_PACKAGE_TYPE, packages.tokenize(" "))
  }

  def parseRpmOsPackageNames(String packages) {
    PackageNameConverter.buildOsPackageNames(RPM_PACKAGE_TYPE, packages.tokenize(" "))
  }

  def parseNupkgOsPackageNames(String packages) {
    PackageNameConverter.buildOsPackageNames(NUPKG_PACKAGE_TYPE, packages.tokenize(" "))
  }
}
