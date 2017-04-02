#!/usr/bin/python2.7

import requests
import json
import runtests
import sys
from datetime import datetime

loginResp = runtests.getToken('erichtofen', 'oneStupidLongTestPassword23571113', 'phoneTest')
creds = {'Content-Type': 'application/json'}
creds['Token'] = loginResp.content
creds['TokenSignature'] = loginResp.headers['TokenSignature']

#creds['PushDest'] = 93500000000

mmsg = ''

if len(sys.argv) > 1:
    mmsg = sys.argv[1]
else:
    mmsg = str(datetime.now())

print mmsg
pushMessage = {'time':mmsg}

req = requests.post(url="http://push.wmapp.mccollum.enterprises/api/push/user/id/93500000000", data=json.dumps(pushMessage), headers=creds)

print req.status_code
print req.content

