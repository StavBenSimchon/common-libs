package org.examples

class Brands implements Serializable {
  def steps
  Brands(steps) {
    this.steps=steps
  }
  def parseYaml(){
    data = steps.readYaml file: "y.yml"
    scan_path = data[scan_path]
  }
}