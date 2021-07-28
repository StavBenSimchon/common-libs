package org.examples

import groovy.json.JsonSlurper

class JiraClient implements Serializable{
  def steps
  JiraClient(steps){
    this.steps = steps
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
      jsonInputString = JsonOutput.toJson(json)
      OutputStream os = con.getOutputStream()
      byte[] input = jsonInputString.getBytes("utf-8");
      os.write(input, 0, input.length);	
      os.close();
    }
    con.connect();
    statusCode = con.responseCode;           
    message = con.responseMessage;
    failure = false;         
    if(statusCode == 200 || statusCode == 201){              
      body = con.content.text;   
      return new JsonSlurper().parseText(body)        
    } else if (statusCode == 204){
      return true
    }else{               
      failure = true;            
      body = con.getErrorStream().text;       
      return body
    }   
  }

  def changeTicketsStatus(tickets, status){
    ret = []
    tickets.each{ ticket ->
      if (changeTicketStatus(ticket, status)){
        ret.add(ticket)
      }
    }
    return ret
  }

  def changeTicketStatus(ticket, status){
    statusID = checkStatusExists(ticket, status)
    if (statusID){
      transitionTicket(ticket, statusID)
      return true
    }else{
      return false
    }
  }

  def transitionTicket(ticket, statusID){
    dataObj = [transition:[id: statusID]]
    url = "https://finovation.atlassian.net/rest/api/3/issue/${ticket}/transitions"
    data = makeRequest("POST", url, accessToken, "application/json", dataObj)
  }

  def checkStatusExists(ticket, status){
    url = "https://finovation.atlassian.net/rest/api/3/issue/${ticket}/transitions"
    data = makeRequest("GET", url, accessToken, "application/json", null)
    data.transitions.each{ 
      if(it.name == status){
        return it.id
      }
    }
    return false
  }

  def getTicketStatus(ticket){
    // rest jira
    url = "https://finovation.atlassian.net/rest/api/2/issue/${ticket}?fields=status"
    data = makeRequest("GET", url, accessToken, "application/json", "")
    return data.fields.status.name
  }

  def relevantTicketsFilter(tickets, status){
    ret = []
    tickets.each{ ticket ->
      if (getTicketStatus(ticket) == status){
        ret.add(ticket)
      }
    }
    return ret
  }

  def getTicketsFolders(folders, filename){
    res = []
    folders.each{ fldr ->
      // fp = "${WORKSPACE}/${fldr}/${filename}"
      fp = "${fldr}/${filename}"
      res = res.plus(getTicketsFromFile(fp))
    }
    return res
  }
  def getTicketsFromFile(fp){
      return extractTickets(new File(fp).collect {it})
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
}