package org.examples

class FUpdater implements Serializable {
  def steps
  def brandConfig
  def brand
  FUpdater(steps, brand) {
    this.steps=steps
    this.brand = brand
  }
  void parseYaml(String fp){
    this.brandConfig = steps.readYaml file: fp
  }
  void validateIP(String IP){
    if(IP.contains('/')){
      String regex = /^[0-9]+\.[0-9]+\.[0-9]+\.[0-9]+\/[0-9]+$/
    } else {
      String regex = /^[0-9]+\.[0-9]+\.[0-9]+\.[0-9]+$/
    }
    if (! IP ==~ regex) {                                                          
        throw new RuntimeException("ip is not good")
    } 
  }
  private void validateIPList(List<String> IPs){
    ips.each{ ip ->
      validateIP(ip)
    }
  }
  void addWhitelistIP(String ip){
    // this.brandConfig[]
  }
  void addWhitelistIPList(List<String> ips){
    ips.each{ ip ->
      addWhitelistIP(ip)
    }
  }
}