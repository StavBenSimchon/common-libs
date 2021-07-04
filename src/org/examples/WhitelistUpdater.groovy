package org.examples

class WhitelistUpdater implements Serializable {
  def steps
  def brandConfig
  def brand
  WhitelistUpdater(steps, brand) {
    this.steps=steps
    this.brand = brand
  }
  void parseYaml(String fp){
    this.brandConfig = steps.readYaml file: fp
  }
  void validateIP(String IP){
    String regex = /^[0-9]+\.[0-9]+\.[0-9]+\.[0-9]+$/
    this.steps.echo "${IP.contains('/')}"
    if(IP.contains('/')){
      String regex = /^[0-9]+\.[0-9]+\.[0-9]+\.[0-9]+\/[0-9]+$/
    }
    if (! IP ==~ regex) {                                                          
        throw new RuntimeException("ip is not good")
    } 
  }
  private void validateIPList(List<String> IPs){
    IPs.each{ ip ->
      validateIP(ip)
    }
  }
  private void validateIPListMultiLineText(String input){
    List<String> ipList = input.tokenize('\n')
    validateIPList(ipList)
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