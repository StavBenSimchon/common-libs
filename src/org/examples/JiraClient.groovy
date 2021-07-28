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

  def urlBuilder(apiVersion, uri){
    // def url = "${this.baseUrl}/${apiVersion}/${uri}"
    def url = "https://finovation.atlassian.net/rest/api/2/issue/CRM-5213?fields=status"
    return url
  }

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
      def jsonInputString = JsonOutput.toJson(json)
      OutputStream os = con.getOutputStream()
      byte[] input = jsonInputString.getBytes("utf-8");
      os.write(input, 0, input.length);	
      os.close();
    }
    try {
      con.connect();
      def statusCode = con.responseCode;           
      def message = con.responseMessage;
      String returnStr = "";
      switch(statusCode) {
        case [200, 201]:
              this.steps.echo "here"
              def body = con.content.text;
              def returnStr=new JsonSlurper().parseText(body).toString();
              break;
        case 204:
          def returnStr="true";
          break;
        default:
          def returnStr = "Some default message or we can remove this default";
          break;
      }
      return returnStr;
    } catch (Exception e) {
      throw e
      return con.getErrorStream().text.toString();
    } finally {
      con.disconnect();
    }  
  }

  def changeTicketsStatus(tickets, status){
    def ret = []
    tickets.each{ ticket ->
      if (this.changeTicketStatus(ticket, status)){
        ret.add(ticket)
      }
    }
    return ret
  }

  def changeTicketStatus(ticket, status){
    def statusID = this.checkStatusExists(ticket, status)
    if (statusID){
      this.transitionTicket(ticket, statusID)
      return true
    }else{
      return false
    }
  }

  def transitionTicket(ticket, statusID){
    def dataObj = [transition:[id: statusID]]
    def url = this.urlBuilder(3, "issue/${ticket}/transitions")
    def data = this.makeRequest("POST", url, this.accessToken, "application/json", dataObj)
  }

  def checkStatusExists(ticket, status){
    def url = this.urlBuilder(3, "issue/${ticket}/transitions")
    def data = this.makeRequest("GET", url, this.accessToken, "application/json", null)
    data.transitions.each{ 
      if(it.name == status){
        return it.id
      }
    }
    return false
  }

  def getTicketStatus(ticket){
    def url = this.urlBuilder(2, "issue/${ticket}?fields=status")
    def data = this.makeRequest("GET", url, this.accessToken, "application/json", null)
    return data.fields.status.name
  }
}