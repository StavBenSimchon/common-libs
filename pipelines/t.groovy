import groovy.json.JsonSlurper
j_user = 'automation@finovation.com'
j_token = 'vHYY25Yx6lyhCe7Fswd11497'
accessToken = "${j_user}:${j_token}".bytes.encodeBase64().toString()

def makeRequest(String method, String apiAddress, String accessToken, String mimeType, String requestBody){
  URL url = new URL (apiAddress);
  HttpURLConnection con = (HttpURLConnection)url.openConnection();
  con.setRequestMethod(method);
  con.setRequestProperty("Authorization", "Basic " + accessToken); 
  con.setRequestProperty("Accept", "application/json");
  con.setRequestProperty("Content-Type", mimeType);
  con.setDoOutput(true);
  // post
  if(requestBody != ""){
    OutputStream outputStream = new BufferedOutputStream(con.getOutputStream());
    BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outputStream, "utf-8"));
    writer.write(requestBody);
    writer.flush();
    writer.close();
    outputStream.close();   
  }
  con.connect();
  statusCode = con.responseCode;           
  message = con.responseMessage;            
  failure = false;         
  if(statusCode == 200 || statusCode == 201){              
    body = con.content.text;   
    // println body
    return new JsonSlurper().parseText(body)        
  }else{               
    failure = true;            
    body = con.getErrorStream().text;       
    // println body.fields
  }   
}

def transitionTicket(ticket){
  ticket = "CRM-4931"
  url = "https://finovation.atlassian.net/rest/api/2/issue/${ticket}?fields=status"
  def data = makeRequest("GET", url, accessToken, "application/json", "")
  println data.fields.status.name
  url = "https://finovation.atlassian.net/rest/api/3/issue/CRM-4931/transitions"
  data = makeRequest("GET", url, accessToken, "application/json", "")
  data.transitions.each{ 
    println it
    // println it.name + " " it.id 
  }
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