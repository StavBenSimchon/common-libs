@Library("ci@dev") _
import org.examples.Brands
import org.examples.GitClient
def g = new GitClient()
import org.examples.JiraClient
def jc = new JiraClient()
node {
  def b = new Brands()
  stage('init'){
    echo "hey"
  }
}