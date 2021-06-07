package org.foo
production='production'
def test(){
  echo "testing"
}
def test2(){
  echo "testing 2"
}

// needs context
def gg(repo){
  git url: "${repo}"
}
def envP(){
  sh 'env'
}
String getProductionTag(){
  return production
}
String getProductionTag2(){
  return "production"
}
return this