def comp(f,g):
	return lambda *x: f(g(*x))

import re
minutia_regexp = re.compile(
    "^(?P<id>[0-9]+):"              +   # the integer identifier of the detected minutia
     "(?P<mx>[0-9]+),"              +   # the x-pixel coordinate of the detected minutia
     "(?P<my>[0-9]+):"              +   # the y-pixel coordinate of the detected minutia
     "(?P<dir>[0-9]+):"             +   # the direction of the detected minutia (0:-31) == (0:-360) clockwise
     "(?P<rel>(0\.[0-9]+)|(1\.0)):" +   # the reliability measure assigned to the detected minutia
     "(?P<typ>(RIG)|(BIF)):"        +   # the type of the detected minutia
     "(?P<ftyp>(APP)|(DIS)):"       +   # the type of feature detected
     "(?P<fn>[0-9]+)"               +   # the integer identifier of the type of feature detected
     "(:(?P<neighbours>([0-9]+,[0-9]+;[0-9]+:?)*))?$") # neighbouring minutia

neighbour_regexp = re.compile(
     "^(?P<mx>[0-9]+),"             +  # the x-pixel coordinate of the neighbouring minutia
      "(?P<my>[0-9]+);"             +  # the y-pixel coordinate of the neighbouring minutia
      "(?P<rc>[0-9]+)$")               # the ridge count calculated between the detected minutia and its first neighbor

def toNumberIfPossible(string):
    try:
        return int(string)
    except ValueError:
        try:
            return float(string)
        except ValueError:
            return string

def parseMinutia(string):
    string = ''.join(list(x for x in string if x != ' '))
    m = re.match(minutia_regexp, string)
    if (m is None):
        raise Exception("Does not parse")
    res = {key:toNumberIfPossible(m.group(key)) for key in ["id","mx","my","dir","rel","typ","ftyp","fn"]}
    res["neighbours"] = []
    neighbours = m.group("neighbours")
    if neighbours:
        neighbours = neighbours.split(":")
        for neighbour in neighbours:
            ren = re.match(neighbour_regexp, neighbour)
            res["neighbours"].append({key:toNumberIfPossible(ren.group(key)) for key in ["mx","my","rc"]})
    return res
    
def parseFilename(filename):
    res = None
    with open(filename) as f:
        res = {(mx,my): m 
               for (mx,my,m) 
               in map(
                    comp(lambda x: (x["mx"], x["my"], x), parseMinutia),
                    list(f)[4:]
                )
        }
    for minutia in res.values():
        for n in minutia["neighbours"]:
            n["dir"] = res[(n["mx"],n["my"])]["dir"]
    return list(res.values())