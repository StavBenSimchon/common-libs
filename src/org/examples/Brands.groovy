package org.examples
import groovy.yaml.YamlSlurper

class Brands implements Serializable {
  def steps
  Brands(steps) {
    this.steps=steps
  }
  def parseYaml(){
def yamlFile = new File("sample.yml")
// with YAML contents.
yamlFile.write('''\
---
sample: true
Groovy: "Rocks!"
''')
 
// Using File.withReader,
// so reader is closed by Groovy automatically.
yamlFile.withReader { reader ->
    // Use parse method of YamlSlurper.
    def yaml = new YamlSlurper().parse(reader)
     
    assert yaml.sample
    assert yaml.Groovy == 'Rocks!'
}
  }
}