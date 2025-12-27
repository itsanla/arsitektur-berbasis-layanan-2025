#!/usr/bin/env groovy

import jenkins.model.*
import com.cloudbees.plugins.credentials.*
import com.cloudbees.plugins.credentials.common.*
import com.cloudbees.plugins.credentials.domains.*
import com.cloudbees.plugins.credentials.impl.*
import hudson.util.Secret

def instance = Jenkins.getInstance()

// Get environment variables
def dockerUsername = System.getenv('DOCKER_HUB_USERNAME')
def dockerToken = System.getenv('DOCKER_HUB_TOKEN')

if (dockerUsername && dockerToken) {
    println "Setting up Docker Hub credentials..."
    
    def domain = Domain.global()
    def store = instance.getExtensionList('com.cloudbees.plugins.credentials.SystemCredentialsProvider')[0].getStore()
    
    def dockerCredentials = new UsernamePasswordCredentialsImpl(
        CredentialsScope.GLOBAL,
        "docker-hub-credentials",
        "Docker Hub Credentials",
        dockerUsername,
        dockerToken
    )
    
    store.addCredentials(domain, dockerCredentials)
    println "Docker Hub credentials added successfully!"
} else {
    println "Docker Hub credentials not found in environment variables"
}

instance.save()