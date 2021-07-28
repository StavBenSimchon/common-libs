@Library("ci@dev") _
import org.examples.JiraClient
j_user = 'automation@finovation.com'
j_token = 'vHYY25Yx6lyhCe7Fswd11497'
accessToken = "${j_user}:${j_token}".bytes.encodeBase64().toString()
def jc = new JiraClient(this, accessToken)

node {
  println jc
  t = "CRM-4931"
  from_status = "IN QA"
  fake_status="Auasd asd"
  to_status= "INTEGRATION"
  println jc.getTicketStatus(t)
  println jc.relevantTicketsFilter([t],from_status)
  println jc.relevantTicketsFilter([t],fake_status)
  println jc.changeTicketsStatus([t],fake_status)
}