#!/usr/bin/env groovy
def call(String name = 'user'){
  echo "Welcome ${name}"
}

def info(message) {
    echo "INFO: ${message}"
}