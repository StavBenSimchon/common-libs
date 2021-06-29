package org.examples

Class Example implements Serializable{
  def var = null
  def steps
  Example(steps){
    this.steps
    this.var="example"
  }
  void myExample(String msg="John") {
      steps.echo msg
  }
}