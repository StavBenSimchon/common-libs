import groovy.json.JsonSlurper
import groovy.json.JsonOutput

j_user = 'automation@finovation.com'
j_token = 'vHYY25Yx6lyhCe7Fswd11497'
accessToken = "${j_user}:${j_token}".bytes.encodeBase64().toString()

def makeRequest(String method, String apiAddress, String accessToken, String mimeType, json){
  URL url = new URL (apiAddress);
  HttpURLConnection con = (HttpURLConnection)url.openConnection();
  con.setRequestMethod(method);
  con.setRequestProperty("Authorization", "Basic " + accessToken); 
  con.setRequestProperty("Accept", "application/json");
  con.setDoOutput(true);
  // post
  if(json){
    con.setRequestProperty("Content-Type", mimeType);
    jsonInputString = JsonOutput.toJson(json)
    [transition:[id:integration_id]]
    jsonInput = '{"transition": {"id": 61}}'
    OutputStream os = con.getOutputStream()
    byte[] input = jsonInput.getBytes("utf-8");
    os.write(input, 0, input.length);	
    os.close();
  }
  con.connect();
  statusCode = con.responseCode;           
  message = con.responseMessage;
  println  statusCode  
  println  message  
  // if (json){
  //     try {
  //       BufferedReader br = new BufferedReader(
  //       new InputStreamReader(con.getInputStream(), "utf-8"))
  //       StringBuilder response = new StringBuilder();
  //       String responseLine = null;
  //       while ((responseLine = br.readLine()) != null) {
  //           response.append(responseLine.trim());
  //       }
  //       println(response.toString());
  //   }catch (Exception e){
  //     println e
  //     continue
  //   }
  // }
  failure = false;         
  if(statusCode == 200 || statusCode == 201){              
    body = con.content.text;   
    // println body
    return new JsonSlurper().parseText(body)        
  } else if (statusCode == 204){}else{               
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
  data = makeRequest("GET", url, accessToken, "application/json", null)
  status="IN QA"
  id = null
  integration_id = 61
  println data
  data.transitions.each{ 
    if(it.name == status){
      println it.name 
      println it.id 
      id = it.id
      dataObj = [transition:[id:integration_id]]
      data = makeRequest("POST", url, accessToken, "application/json", dataObj)
      println data
    }
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