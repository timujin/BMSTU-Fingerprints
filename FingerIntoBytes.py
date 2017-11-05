
# coding: utf-8

# In[8]:

import ParseFingers as PF


# In[28]:

testFinger = PF.parseFilename(r"mindtct/minutie/f0001_01.png.min")
testFinger2 = PF.parseFilename(r"mindtct/minutie/f0002_05.png.min")


# In[41]:


"""
M: 190,35,28; (N: 196, 10, 10, 3; N: 208, 70, 13, 1; N: 203, 72, 29, 1; N: 199, 64, 13, 1; N: 195, 74, 30, 2; );
M: 225,203,24; (N: 238, 133, 30, 9; N: 234, 161, 25, 6; N: 253, 70, 29, 16; N: 249, 156, 26, 7; N: 236, 201, 8, 1; );
M: 236,201,8; (N: 240, 55, 13, 14; N: 238, 133, 30, 8; N: 253, 70, 29, 15; N: 249, 156, 26, 6; N: 255, 255, 255, 255; );
M: 234,161,25; (N: 240, 55, 13, 10; N: 238, 133, 30, 3; N: 253, 70, 29, 10; N: 249, 156, 26, 1; N: 236, 201, 8, 6; );
M: 186,212,8; (N: 188, 186, 8, 4; N: 196, 181, 24, 5; N: 203, 199, 8, 2; N: 210, 222, 8, 1; N: 211, 231, 24, 2; );
M: 203,199,8; (N: 234, 161, 25, 5; N: 236, 201, 8, 1; N: 225, 203, 24, 0; N: 210, 222, 8, 3; N: 211, 231, 24, 4; );
M: 86,188,9; (N: 100, 168, 25, 3; N: 95, 184, 25, 0; N: 98, 202, 24, 1; N: 95, 229, 24, 5; N: 92, 223, 24, 5; );
M: 188,186,8; (N: 191, 141, 6, 7; N: 196, 181, 24, 1; N: 225, 203, 24, 2; N: 203, 199, 8, 2; N: 210, 222, 8, 5; );
M: 180,180,24; (N: 183, 161, 8, 3; N: 196, 181, 24, 0; N: 188, 186, 8, 1; N: 203, 199, 8, 3; N: 186, 212, 8, 4; );
M: 81,181,24; (N: 81, 168, 9, 2; N: 100, 168, 25, 2; N: 95, 184, 25, 0; N: 98, 202, 24, 2; N: 86, 188, 9, 1; );
"""
sorted(testFinger2, key=lambda f: -f["rel"])


# In[23]:

def minutiaIntoBytes(minutia, norm=500):
    """
    Data format (bytes):
        0:    mx
        1:    my
        2:    dir
        3-6:  neigh1
        7-10: neigh2
        11-14:  neigh3
        15-18:  neigh4
        19-22:  neigh5
        neigh:
            0: mx
            1: my
            2: dir
            3: rc
    """
    base = [
        int(minutia["mx"] * 256 / norm) ,
        int(minutia["my"] * 256 / norm) ,
        minutia["dir"],
    ]
    for neigh in minutia["neighbours"]:
        base += [
            int(neigh["mx"] * 256 / norm) ,
            int(neigh["my"] * 256 / norm) ,
            neigh["dir"],
            neigh["rc"],
        ]
    for _ in range(5-len(minutia["neighbours"])):
        base += [255]*4
    return (base)


# In[24]:

def fingerIntoBytes(finger, norm=500):
    finger = sorted(finger, key=lambda f: -f["rel"])
    fold = lambda x: x[0]+fold(x[1:]) if x else []
    return fold ([minutiaIntoBytes(minutia,norm) for minutia,_ in zip(finger, range(10))])
        


# In[ ]:




# In[39]:

def writeFingerToFile(filename, finger):
    res = fingerIntoBytes(finger)
    print(len(res), [x-128 for x in res])
    res = bytearray(res)
    f = open(filename, 'wb')
    f.write(res)
    f.close()


# In[40]:

writeFingerToFile("out.dat", testFinger)


# In[ ]:



