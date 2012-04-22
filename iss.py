import urllib2
from xml.dom.minidom import parseString
import datetime
import time
import calendar

current = []

def getSeconds(data):
    """
    Convert the time into GMT seconds from epoch
    """
    dt = datetime.datetime.strptime(data, "%Y-%m-%dT%H:%M:%S")
    return calendar.timegm(dt.timetuple())

def getTime(pazz, moment):
    """
    Find the start/max/end times of the pass
    """
    nodes = pazz.getElementsByTagName(moment)[0].getElementsByTagName('time')
    data = nodes[0].childNodes[0].data[:-1]
    print "%s: %s" % (moment, data)
    return getSeconds(data)

def fetch():
    """
    Fetch the satellite data in XML and parse out the passes, and a date after
    which we'll need to poll again.
    """
    request = urllib2.Request('http://api.uhaapi.com/satellites/25544/passes?lat=50.72738&lng=-3.47421', headers={'Accept' : 'application/xml'})
    response = urllib2.urlopen(request).read()
    xml = parseString(response)

    valid = xml.getElementsByTagName('to')[0].childNodes[0].data[:-1]
    print "valid: %s" % valid
    valid = getSeconds(valid)
    
    passes = xml.getElementsByTagName('pass')
    
    global current
    
    for pazz in passes:
        times = {}
        times['start'] = getTime(pazz, 'start')
        times['max'] = getTime(pazz, 'max')
        times['end'] = getTime(pazz, 'end')
        current.append(times)


def map(value, f, t, f2, t2):
    range = t -f
    range2 = t2 - f2
    return f2 + ((value - f) * range2 / range)

HALF_HOUR = 30 * 60

def getData():
    """
    Callback that's invoked every second by the Processing draw() loop.
    Just calculates how 'far' away we are from an overhead pass of the ISS as
    a number between 0 and 255, where:
       0 - no pass due
       1-127 - linear build up over 30 minutes
       128-255 - overhead, with linear build up to maximum point
    """
    now = time.mktime(time.gmtime())
    start = current[0]['start']
    max = current[0]['max']
    end = current[0]['end']
    
    print now
    print start
    print max
    print end

    value = 0
    
    if now < start:
        # build up to visibility
        secsTo = start - now
        if secsTo < HALF_HOUR:
            value = map(15*60, HALF_HOUR, 0, 0, 127)
    elif now > end:
        # moving away
        secsSince = now - end
        if secsSince < HALF_HOUR:
            value = map(secsSince, 0, HALF_HOUR, 127, 0)
    else:
        # visible!
        if now < highest:
            # on way to max
            secsTo = highest - now
            value = map(secsTo, highest-start, 0, 128, 255)
            secsSince = end - now
        else:
            # past max
            value = map(secsSince, end-highest, 0, 255, 128)

    print value
    return int(value)


# Main entry point for python code

fetch()

import IssSketch
IssSketch.start(getData)
