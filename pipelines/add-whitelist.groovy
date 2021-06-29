@Library("ci@dev") _
import org.examples.BrandsTool
import org.examples.GitClient
import org.examples.JiraClient
def b = new BrandsTool()
def g = new GitClient()
def jc = new JiraClient()
node {
  stage('init'){
    echo "hey"
  }
}