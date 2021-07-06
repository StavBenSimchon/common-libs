package org.examples
// @Grab('commons-net:commons-net:3.3')
// this.getClass().classLoader.rootLoader.addURL(new File("lib/commons-net-3.3.jar").toURL())
@Grab('org.yaml:snakeyaml:1.17')
import org.yaml.snakeyaml.Yaml

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
  void cmd(){
    // def thumbnail = ["ls", "file.jpg", "-thumbnail", "100x100", "thumb-file.gif"].execute()
    def thumbnail = "ls -al".execute()
    thumbnail.waitFor()
    this.steps.println "Exit value: ${thumbnail.exitValue()}"
    this.steps.println "Output: ${thumbnail.text}"
  }
  void request(){
    String res = new URL("https://api.openweathermap.org/data/2.5/weather?q=Jerusalem&appid=a4a8af163a68289070abec5d1738cbca").getText()
    this.steps.println res
    def card = new JsonSlurper().parseText(res)
    this.steps.println card
    this.steps.println card['base']

    // def get = new URL("http://api.openweathermap.org/data/2.5/weather?q=a&appid=a").openConnection();
    // def getRC = get.getResponseCode();
    // this.steps.println(getRC);
    // if (getRC.equals(200)) {
    //     this.steps.println(get.getInputStream().getText());
    // }
    // this.steps.println(get.getInputStream().getText());
  }
  void parseYaml(String fp){
    // this.brandConfig = this.steps.readYaml file: fp
    // this.steps.println brandConfig.getClass()
    // this.steps.sh "# env"
    this.steps.echo "$WORKSPACE"
    this.steps.println this.steps
    Yaml parser = new Yaml()
    // LinkedHashMap example = parser.load(("$WORKSPACE/y.yml" as File).text)
    // this.steps.println example
  }
  void saveYaml(String fp, LinkedHashMap data){
    this.steps.writeYaml file: fp, data: data, overwrite: true
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