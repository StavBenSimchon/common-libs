package org.examples

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
    URL apiUrl = new URL("http://api.openweathermap.org/data/2.5/weather?q=telaviv&appid=a4a8af163a68289070abec5d1738cbca")
    HttpURLConnection connection = apiUrl.openConnection();
    connection.setRequestMethod("GET");
    connection.setRequestProperty("Cookie", cookie); 
    connection.doOutput = true; 
    connection.connect(); 
    println connection.content.text;
    this.steps.echo "$apiUrl.text"

    // def card = new JsonSlurper().parseText(apiUrl.text)
    // this.steps.echo "$card"
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