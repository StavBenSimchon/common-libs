@Library("ci@dev") _
// import org.finovation.Tools
import org.examples.Brands
// import org.examples.GitClient
// def g = new GitClient(this)
// import org.examples.JiraClient
// def jc = new JiraClient(this)
node {
def b = new Brands(this)
  stage('init'){
    sh """
    #mkdir -p ./test/
    echo 'a: 5' > ./y.yml
    ls -al
    cat ./y.yml
    """
    def methods = Brands.declaredMethods.findAll { !it.synthetic }.name
    println methods
    println b.parseYaml()
    def datas = readYaml file: 'y.yml'
    println datas
  }
}