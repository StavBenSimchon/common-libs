@Library("ci@dev") _
import org.examples.JiraClient

def jc = new JiraClient(this)

node {
  println jc
}