package org.foo

def test(){
  echo "testing"
}
def test2(){
  echo "testing 2"
}
def envP(){
  sh '''
    env
  '''
}

return this