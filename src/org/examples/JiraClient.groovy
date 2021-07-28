package org.examples


import groovy.json.JsonSlurper

class JiraClient implements Serializable{
  def steps
  def baseUrl
  def accessToken
  JiraClient(steps,accessToken){
    this.steps = steps
    this.baseUrl = "https://finovation.atlassian.net/rest/api"
    this.accessToken = accessToken
    }

  private urlBuilder(apiVersion, uri){
    url = "${this.baseUrl}/2/${uri}"
    return url
  }

  private makeRequest(String method, String apiAddress, String accessToken, String mimeType, json){
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
      OutputStream os = con.getOutputStream()
      byte[] input = jsonInputString.getBytes("utf-8");
      os.write(input, 0, input.length);	
      os.close();
    }
    try {
      con.connect();
      statusCode = con.responseCode;           
      message = con.responseMessage;
      String returnStr = "";
      switch(statusCode) {
        case [200, 201]:
              body = con.content.text;
              returnStr=new JsonSlurper().parseText(body).toString();
              break;
        case 204:
          returnStr="true";
          break;
        default:
          returnStr = "Some default message or we can remove this default";
          break;
      }
      return returnStr;
    } catch (Exception e) {
      return con.getErrorStream().text.toString();
    } finally {
      con.disconnect();
    }  
  }

  private changeTicketsStatus(tickets, status){
    ret = []
    tickets.each{ ticket ->
      if (this.changeTicketStatus(ticket, status)){
        ret.add(ticket)
      }
    }
    return ret
  }

  private changeTicketStatus(ticket, status){
    statusID = this.checkStatusExists(ticket, status)
    if (statusID){
      this.transitionTicket(ticket, statusID)
      return true
    }else{
      return false
    }
  }

  private transitionTicket(ticket, statusID){
    dataObj = [transition:[id: statusID]]
    url = this.urlBuilder(3, "issue/${ticket}/transitions")
    data = this.makeRequest("POST", url, this.accessToken, "application/json", dataObj)
  }

  private checkStatusExists(ticket, status){
    url = this.urlBuilder(3, "issue/${ticket}/transitions")
    data = this.makeRequest("GET", url, this.accessToken, "application/json", null)
    data.transitions.each{ 
      if(it.name == status){
        return it.id
      }
    }
    return false
  }

  def getTicketStatus(ticket){
    url = this.urlBuilder(2, "issue/${ticket}?fields=status")
    data = this.makeRequest("GET", url, this.accessToken, "application/json", "")
    return data.fields.status.name
  }
}