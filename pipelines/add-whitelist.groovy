@Library("ci@dev") _
// import org.finovation.Tools
import org.examples.Brands
def b = new Brands(this)
// import org.examples.GitClient
// def g = new GitClient(this)
// import org.examples.JiraClient
// def jc = new JiraClient(this)

node {
  stage('init'){
    sh """
    #mkdir -p ./test/
    echo 'a: 5' > ./y.yml
    ls -al
    cat ./y.yml
    """
    b.parseYaml
  }
}