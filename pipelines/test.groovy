@Library("ci@dev") _
import org.examples.JiraClient
j_user = 'automation@finovation.com'
j_token = 'vHYY25Yx6lyhCe7Fswd11497'
accessToken = "${j_user}:${j_token}".bytes.encodeBase64().toString()
def jc = new JiraClient(this, accessToken)

node {
  println jc
  t = "CRM-5213"
  println jc.getTicketStatus()
}