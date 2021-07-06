@Library("ci@dev") _
// import org.finovation.Tools
import org.examples.Brands
import org.examples.WhitelistUpdater
// import org.examples.GitClient
// def g = new GitClient(this)
// import org.examples.JiraClient
// def jc = new JiraClient(this)
def b = new Brands(this)
def wl = new WhitelistUpdater(this, 'chalse')
node {
    properties(
    [
        parameters(
            [text(name: 'IPS'),
             string( name: 'TICKET')]
            )

    ]
    )    
  stage('init'){
    sh """
    #mkdir -p ./test/
    echo '
    a: 
    - 5.5.5.5/32
    - 6.6.6.6/30' > ./y.yml
    ls -al
    cat ./y.yml
    """
    def methods = WhitelistUpdater.declaredMethods.findAll { !it.synthetic }.name
    println methods
    wl.request
    wl.validateIPListMultiLineText IPS
    wl.parseYaml 'y.yml'
    def datas = readYaml file: 'y.yml'
    // echo "here $datas.a $datas.getClass()"
    def obj =  b.parseYaml() 
    println obj
    if (obj['a'].contains("7.7.7.7/29")){
      echo "contains"
    } else{
      echo "not contains"
      obj['a'].add("7.7.7.7/29")
      writeYaml file: 'z.yml', data: obj, overwrite: true
      sh '''
        cat ./z.yml
      '''
    }
  }
}