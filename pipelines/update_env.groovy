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
    println getTicketsFromFile(fp)
    // def list = new File("$WORKSPACE/log").collect {it}
    // println list
    // tickets = grab_tickets(list)
    // println tickets
    // sh '''
    // cat log | sed -n "/#/,/#/p" | head -n-1 | tail -n+2 | grep -v '#'| cut -d '-' -f2- 
    // '''
}