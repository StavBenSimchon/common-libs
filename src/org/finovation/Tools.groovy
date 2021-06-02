package org.finovation
class Tools implements Serializable {
    def steps
    Tools(steps) {
        this.steps = steps
    }
    void myEcho(String msg) {
        steps.echo msg
    }
    // this wont work outside context block like node{} or pipeline steps
    void myEnv() {
        steps.sh "env"
    }
}