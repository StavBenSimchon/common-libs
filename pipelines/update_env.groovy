import groovy.json.JsonSlurper

def getRelevantTicket(ticket){
  // CRM-5369
  def jsonSlurper = new JsonSlurper()
  jira_user = 'automation@finovation.com'
  jira_token = 'vHYY25Yx6lyhCe7Fswd11497'
  url = "https://finovation.atlassian.net/rest/api/2/issue/${ticket}?fields=status"
  println new URL('http://www.google.com').text
  // println 'http://www.google.com'.toURL().text

  // def http = new HTTPBuilder(url)
  // http.auth.basic(jira_user, jira_token)
  // http.parser[ContentType.JSON] = http.parser.'application/json'
  // http.get(path : path, contentType : ContentType.JSON) { response, json ->
  //   println json
  //   println JsonOutput.toJson(json) 
  // }
}
def getTicketsFromFile(fp){
    return extractTickets(new File(fp).collect {it})
}
def getTicketsFolders(folders, filename){
  res = []
  folders.each{ fldr ->
    fp = "${WORKSPACE}/${fldr}/${filename}"
    res = res.plus(getTicketsFromFile(fp))
  }
  return res
}
def extractTickets(filelines){
    res = []
    flag = true
    for (line in filelines){
        if(flag){
            if(line.startsWith('-')){
                flag = false
                ticket = line.substring(2).tokenize(' ')[0]
                res.add(ticket)
            }
        } else {
            if(line.startsWith('#')){
                break
            }
            ticket = line.substring(2).tokenize(' ')[0]
            res.add(ticket)
        }
    }
    return res
}
node{
    sh '''
     echo "
## asd
## asd
- add-123 asdasdasd
- add-143 fgshdfghk
- add-153 yhnrtgbn
- add-163 xdrtxdrt
### bds
- daa-123
" > log
    '''
    dir("a"){
        git url:"https://github.com/StavBenSimchon/tes1.git", branch: 'main'
        sh '''
        pwd
        ls -al
        '''
    sh '''
     echo "
## asd
## asd
- add-123 asdasdasd
- add-143 fgshdfghk
- add-153 yhnrtgbn
- add-163 xdrtxdrt
### bds
- daa-123
" > log
    '''
    }
    dir("b"){
        git url:"https://github.com/StavBenSimchon/tes1.git" ,branch: 'main'
        sh '''
        pwd
        ls -al
        '''
    sh '''
     echo "
## asd
## asd
- addbbb-123 asdasdasd
- addbbb-143 fgshdfghk
- addbbb-153 yhnrtgbn
- addbbb-163 xdrtxdrt
### bds
- daa-123
" > log
    '''
    }
    sh '''
     ls -al
     ls -al ./a
     ls -al ./b

     cat a/log
     cat b/log
    '''
    // echo "$WORKSPACE"
    fp = "$WORKSPACE/log"
    println getTicketsFromFile(fp)
    folders = ["a", "b"]
    filename = 'log'
    println getTicketsFolders(folders,filename)
    println getRelevantTicket("CRM-5369")
    // def list = new File("$WORKSPACE/log").collect {it}
    // println list
    // tickets = grab_tickets(list)
    // println tickets
    // sh '''
    // cat log | sed -n "/#/,/#/p" | head -n-1 | tail -n+2 | grep -v '#'| cut -d '-' -f2- 
    // '''
}