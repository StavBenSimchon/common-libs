package org.examples

import org.examples.Brands
import groovy.json.JsonSlurper
import java.net.URL

class WhitelistUpdater implements Serializable {
  def steps
  def brandConfig
  def brand
  WhitelistUpdater(steps, brand) {
    this.steps=steps
    this.brand = brand
  }
  void request(){
    // String res = new URL("http://api.openweathermap.org/data/2.5/weather?q=a&appid=a").text
    def get = new URL("http://api.openweathermap.org/data/2.5/weather?q=a&appid=a").openConnection();
    def getRC = get.getResponseCode();
    this.steps.println(getRC);
    if (getRC.equals(200)) {
        this.steps.println(get.getInputStream().getText());
    }
  }
  void parseYaml(String fp){
    this.brandConfig = this.steps.readYaml file: fp
  }
  void validateIP(String IP){
    String regex = /^[0-9]+\.[0-9]+\.[0-9]+\.[0-9]+$/
    this.steps.echo ">>1 ${IP.contains('/')}"
    if(IP.contains('/')){
      this.steps.echo ">>3"
      regex = /^[0-9]+\.[0-9]+\.[0-9]+\.[0-9]+\/[0-9]+$/
    }
    this.steps.echo ">>2 ${regex}"
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