import groovy.json.JsonSlurper

def changeTicketsStatus(tickets){
  status = "in QA"
  ret = []
  tickets.each{ ticket ->
    if (changeTicketStatus(ticket)){
      ret.add(ticket)
    }
  }
  return ret
}

def changeTicketStatus(ticket, status){
  if(getTicketStatus() != status){
    return false
  } else{
    changeTicketStatus(status)
    return true
  }
}

def getTicketStatus(){
  // rest jira
}

def changeTicketStatus(status){
  // rest to jira
}
def parseResponse(HttpURLConnection connection){             
  statusCode = connection.responseCode;           
  message = connection.responseMessage;            
  failure = false;         
  if(statusCode == 200 || statusCode == 201){              
    body = connection.content.text;//this would fail the pipeline if there was a 400   
    println body
    return body        
  }else{               
    this.failure = true;            
    this.body = connection.getErrorStream().text;           
  }         
} 
def URLConnection makeRequest(String method, String apiAddress, String accessToken, String mimeType, String requestBody) throws IOException {
        URL url = new URL(apiAddress);
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

        urlConnection.setDoInput(true);
        urlConnection.setDoOutput(!method.equals("GET"));
        urlConnection.setRequestMethod(method);

        urlConnection.setRequestProperty("Authorization", "Bearer " + accessToken);        

        urlConnection.setRequestProperty("Content-Type", mimeType);
        OutputStream outputStream = new BufferedOutputStream(urlConnection.getOutputStream());
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outputStream, "utf-8"));
        writer.write(requestBody);
        writer.flush();
        writer.close();
        outputStream.close();            

        urlConnection.connect();

        return urlConnection;
    }
def getRelevantTicket(ticket){
  def jsonSlurper = new JsonSlurper()
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
  con.setRequestProperty("Content-Type", "application/json; utf-8");
  con.setRequestProperty("Accept", "application/json");
  con.setDoOutput(true);
  con.connect();
  parseResponse(con);
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