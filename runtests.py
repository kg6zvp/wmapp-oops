#!/usr/bin/env python2
#/usr/bin/python2

import json
import requests
import sys

protocol = "http"
server = "auth.wmapp.mccollum.enterprises"

authBaseUrl = protocol+"://"+server+"/api"
tokenBaseUrl = authBaseUrl+"/token"
usersBaseUrl = authBaseUrl+"/users"

loginUrl = tokenBaseUrl+"/getToken"
logoutUrl = tokenBaseUrl+"/invalidateToken"
listUrl = tokenBaseUrl+"/listTokens"
renewUrl = tokenBaseUrl+"/renewToken"
invalidationSubscriptionUrl = tokenBaseUrl+"/subscribeToInvalidation"
tokenValidUrl = tokenBaseUrl+"/tokenValid"

tokenSignature=" "
tokenString=" "

def readTokens():
    with open('token.json', 'rb') as tf:
	tokenString = tf.read()
    with open('sigb64.txt', 'rb') as sf:
	tokenSignature = sf.read().strip('\n').strip('\r')

def getToken(username, password, deviceName):
    hrs = {'Content-Type': 'application/json'}
    loginObject = {'username': username, 'password': password, 'devicename': deviceName}
    return requests.post(url=loginUrl, data=json.dumps(loginObject), headers=hrs)

def invalidateToken(delToken, token, sigb64):
    hrs = {'Content-Type': 'application/json'}
    hrs['Token'] = token
    hrs['TokenSignature'] = sigb64
    return requests.delete(url=logoutUrl+'/'+str(delToken['tokenId']), headers=hrs)

def listTokens(token, sigb64):
    hrs = {'Content-Type': 'application/json'}
    hrs['Token'] = token
    hrs['TokenSignature'] = sigb64
    return requests.get(url=listUrl, headers=hrs)

def renewToken(token, sigb64):
    hrs = {'Content-Type': 'application/json'}
    hrs['Token'] = token
    hrs['TokenSignature'] = sigb64
    return requests.get(url=renewUrl, headers=hrs)

def subscribeToInvalidation(invalidationSubscription, token, sigb64):
    hrs = {'Content-Type': 'application/json'}
    hrs['Token'] = token
    hrs['TokenSignature'] = sigb64
    return requests.post(url=invalidationSubscriptionUrl, headers=hrs)

def isValidToken(token, sigb64):
    hrs = {'Content-Type': 'application/json'}
    hrs['Token'] = token
    hrs['TokenSignature'] = sigb64
    return requests.get(url=tokenValidUrl, headers=hrs)




def checkCode(httpResponse, expectedResponse, failureMessage):
    if httpResponse.status_code != expectedResponse:
	print "\tFailed to "+failureMessage
	print "\t"+str(httpResponse.status_code)
	print "\t"+httpResponse.content
	sys.exit(1)

