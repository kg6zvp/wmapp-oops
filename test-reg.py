#!/usr/bin/python2.7

import requests
import json
import runtests

loginResp = runtests.getToken('erichtofen', 'oneStupidLongTestPassword23571113', 'phoneTest')
creds = {'Content-Type': 'application/json'}
creds['Token'] = loginResp.content
creds['TokenSignature'] = loginResp.headers['TokenSignature']

pushClient = {}

pushClient['studentId'] = 93500000000
pushClient['username'] = 'erichtofen'
pushClient['type'] = 'FIREBASE'
pushClient['registrationId'] = 'fgMZmc3bG2k:APA91bHh-pJPFuPaze-9MqgFBHUbxu85nc7PblhLBXRhjVj221iHJjZar9yNlJr5N_3dUmymGkE-lg9Xz35NoSX_pa2MPHvDPdSaJJpNwe7Hxx0Ruh3BATzGTRx1ymiAszNbVeYtHotR'

req = requests.post(url="http://push.wmapp.mccollum.enterprises/api/reg/client", data=json.dumps(pushClient), headers=creds)

print req.status_code
print req.content

