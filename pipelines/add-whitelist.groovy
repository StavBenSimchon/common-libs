@Library("ci@dev") _
// import org.examples.Brands
// def b = new Brands()
import org.examples.GitClient
def g = new GitClient()
import org.examples.JiraClient
def jc = new JiraClient()
node {
  stage('init'){
    echo "hey"
  }
}