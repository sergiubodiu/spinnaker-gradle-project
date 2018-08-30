package sgrc.orca.terraform.jobs

public class Instance {

  String name
  String imageId
  String instanceType

  String subnet
  List<String> securityGroups
  String privateIp

  Instance() {}

  Instance(String name) {
    this.name = name
  }

  @Override
  String toString() {
    return "Instance{" +
        "name='" + name + '\'' +
        ", imageId='" + imageId + '\'' +
        ", instanceType='" + instanceType + '\'' +
        ", subnet='" + subnet + '\'' +
        ", securityGroups=" + securityGroups +
        ", privateIp='" + privateIp + '\'' +
        '}'
  }
}
