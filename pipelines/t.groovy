import groovy.json.JsonSlurper

def parseResponse(HttpURLConnection connection){             
  statusCode = connection.responseCode;           
  message = connection.responseMessage;            
  failure = false;         
  if(statusCode == 200 || statusCode == 201){              
    body = connection.content.text;//this would fail the pipeline if there was a 400   
    println body
    return body        
  }else{               
    failure = true;            
    body = connection.getErrorStream().text;       
    println body 
  }         
} 
def transitionTicket(ticket){
  ticket = "CRM-5369"
  j_user = 'automation@finovation.com'
  j_token = 'vHYY25Yx6lyhCe7Fswd11497'
  def accessToken = "${j_user}:${j_token}".bytes.encodeBase64().toString()
  url = "https://finovation.atlassian.net/rest/api/2/issue/${ticket}?fields=status"
  // println new URL(urls).text
  // println 'http://www.google.com'.toURL().text
  URL url = new URL (url);
  HttpURLConnection con = (HttpURLConnection)url.openConnection();
  con.setRequestMethod("GET");
  con.setRequestProperty("Authorization", "Basic " + accessToken); 
  con.setRequestProperty("Accept", "application/json");
  con.setDoOutput(true);
  con.connect();
  parseResponse(con);
}
def getTicketsFromFile(fp){
    return extractTickets(new File(fp).collect {it})
}
def extractTickets(list){
    res = []
    flag = true
    for (line in list){
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
    }
    dir("b"){
        git url:"https://github.com/StavBenSimchon/tes1.git" ,branch: 'main'
        sh '''
        pwd
        ls -al
        '''
        
    }
    sh '''
     ls -al
    '''
    // echo "$WORKSPACE"
    fp = "$WORKSPACE/log"
    // println getTicketsFromFile(fp)
    transitionTicket("")
    // def list = new File("$WORKSPACE/log").collect {it}
    // println list
    // tickets = grab_tickets(list)
    // println tickets
    // sh '''
    // cat log | sed -n "/#/,/#/p" | head -n-1 | tail -n+2 | grep -v '#'| cut -d '-' -f2- 
    // '''
}