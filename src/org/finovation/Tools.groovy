package org.finovation
class Tools implements Serializable {
    def steps
    Tools(steps) {
        this.steps = steps
    }
    void myEcho(String msg) {
        steps.echo msg
    }
    void myEnv() {
        steps.sh "env"
    }
}