package org.examples
import groovy.yaml.YamlSlurper

class Brands implements Serializable {
  def steps
  Brands(steps) {
    this.steps=steps
  }
  def parseYaml(){
    data = readYaml file: "y.yml"
    scan_path = data[scan_path]
  }
}