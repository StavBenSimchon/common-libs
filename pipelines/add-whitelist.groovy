@Library("ci@dev") _
import org.examples.Brands
import org.examples.Git
import org.examples.JiraClient
def b = new BrandS()
def g = new Git()
def jc = new JiraClient()
node {
  stage('init'){
    echo "hey"
  }
}