package org.finovation

class Utilities implements Serializable {
  def steps
  Utilities(steps) {this.steps = steps}
  def sh(cmd) {
    steps.sh "${cmd}"
  }
}

// def utils = new Utilities(this)
// node {
//   utils.sh 'echo hello'
// }