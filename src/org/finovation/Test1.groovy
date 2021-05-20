package org.finovation

class Test1 implements Serializable {
  def steps
  Test1(steps) {this.steps = steps}
  def sh(cmd) {
    steps.sh "${cmd}"
  }
}

// def utils = new Utilities(this)
// node {
//   utils.sh 'echo hello'
// }