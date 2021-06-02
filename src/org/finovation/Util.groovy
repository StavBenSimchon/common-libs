package org.foo

def test(){
  echo "testing"
}
def test2(){
  echo "testing 2"
}
def gg(repo){
  git url: "${repo}"
}

return this