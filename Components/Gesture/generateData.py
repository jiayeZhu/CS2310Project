import time
import random
import math

RECORD_NUMBER = 1000
NEED_HELP_RATE = 0.1
f = open("mockup.txt",'w')
NOW = math.floor(time.time())
for i in range(RECORD_NUMBER):
  # needHelp = 'true' if (random.random()<NEED_HELP_RATE) else 'false'
  # record = '{}\t{}\n'.format(math.floor(time.time()), random.random()<NEED_HELP_RATE)
  # print(record)
  f.write('{}\t{}\n'.format(NOW-(RECORD_NUMBER-i)*1000, random.random()<NEED_HELP_RATE))

f.close()